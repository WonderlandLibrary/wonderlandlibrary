package io.netty.channel.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.EventLoopException;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class NioEventLoop extends SingleThreadEventLoop {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
  
  private static final int CLEANUP_INTERVAL = 256;
  
  private static final boolean DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
  
  private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
  
  private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
  
  Selector selector;
  
  private SelectedSelectionKeySet selectedKeys;
  
  private final SelectorProvider provider;
  
  static {
    String key = "sun.nio.ch.bugLevel";
    try {
      String buglevel = SystemPropertyUtil.get(key);
      if (buglevel == null)
        System.setProperty(key, ""); 
    } catch (SecurityException e) {
      if (logger.isDebugEnabled())
        logger.debug("Unable to get/set System Property: {}", key, e); 
    } 
    int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
    if (selectorAutoRebuildThreshold < 3)
      selectorAutoRebuildThreshold = 0; 
    SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
    if (logger.isDebugEnabled()) {
      logger.debug("-Dio.netty.noKeySetOptimization: {}", Boolean.valueOf(DISABLE_KEYSET_OPTIMIZATION));
      logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
    } 
  }
  
  private final AtomicBoolean wakenUp = new AtomicBoolean();
  
  private volatile int ioRatio = 50;
  
  private int cancelledKeys;
  
  private boolean needsToSelectAgain;
  
  NioEventLoop(NioEventLoopGroup parent, ThreadFactory threadFactory, SelectorProvider selectorProvider) {
    super((EventLoopGroup)parent, threadFactory, false);
    if (selectorProvider == null)
      throw new NullPointerException("selectorProvider"); 
    this.provider = selectorProvider;
    this.selector = openSelector();
  }
  
  private Selector openSelector() {
    Selector selector;
    try {
      selector = this.provider.openSelector();
    } catch (IOException e) {
      throw new ChannelException("failed to open a new selector", e);
    } 
    if (DISABLE_KEYSET_OPTIMIZATION)
      return selector; 
    try {
      SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
      Class<?> selectorImplClass = Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
      if (!selectorImplClass.isAssignableFrom(selector.getClass()))
        return selector; 
      Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
      Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
      selectedKeysField.setAccessible(true);
      publicSelectedKeysField.setAccessible(true);
      selectedKeysField.set(selector, selectedKeySet);
      publicSelectedKeysField.set(selector, selectedKeySet);
      this.selectedKeys = selectedKeySet;
      logger.trace("Instrumented an optimized java.util.Set into: {}", selector);
    } catch (Throwable t) {
      this.selectedKeys = null;
      logger.trace("Failed to instrument an optimized java.util.Set into: {}", selector, t);
    } 
    return selector;
  }
  
  protected Queue<Runnable> newTaskQueue() {
    return PlatformDependent.newMpscQueue();
  }
  
  public void register(SelectableChannel ch, int interestOps, NioTask<?> task) {
    if (ch == null)
      throw new NullPointerException("ch"); 
    if (interestOps == 0)
      throw new IllegalArgumentException("interestOps must be non-zero."); 
    if ((interestOps & (ch.validOps() ^ 0xFFFFFFFF)) != 0)
      throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch.validOps() + ')'); 
    if (task == null)
      throw new NullPointerException("task"); 
    if (isShutdown())
      throw new IllegalStateException("event loop shut down"); 
    try {
      ch.register(this.selector, interestOps, task);
    } catch (Exception e) {
      throw new EventLoopException("failed to register a channel", e);
    } 
  }
  
  public int getIoRatio() {
    return this.ioRatio;
  }
  
  public void setIoRatio(int ioRatio) {
    if (ioRatio <= 0 || ioRatio > 100)
      throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)"); 
    this.ioRatio = ioRatio;
  }
  
  public void rebuildSelector() {
    Selector newSelector;
    if (!inEventLoop()) {
      execute(new Runnable() {
            public void run() {
              NioEventLoop.this.rebuildSelector();
            }
          });
      return;
    } 
    Selector oldSelector = this.selector;
    if (oldSelector == null)
      return; 
    try {
      newSelector = openSelector();
    } catch (Exception e) {
      logger.warn("Failed to create a new Selector.", e);
      return;
    } 
    int nChannels = 0;
    while (true) {
      try {
        for (SelectionKey key : oldSelector.keys()) {
          Object a = key.attachment();
          try {
            if (!key.isValid() || key.channel().keyFor(newSelector) != null)
              continue; 
            int interestOps = key.interestOps();
            key.cancel();
            SelectionKey newKey = key.channel().register(newSelector, interestOps, a);
            if (a instanceof AbstractNioChannel)
              ((AbstractNioChannel)a).selectionKey = newKey; 
            nChannels++;
          } catch (Exception e) {
            logger.warn("Failed to re-register a Channel to the new Selector.", e);
            if (a instanceof AbstractNioChannel) {
              AbstractNioChannel ch = (AbstractNioChannel)a;
              ch.unsafe().close(ch.unsafe().voidPromise());
              continue;
            } 
            NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
            invokeChannelUnregistered(task, key, e);
          } 
        } 
        break;
      } catch (ConcurrentModificationException e) {}
    } 
    this.selector = newSelector;
    try {
      oldSelector.close();
    } catch (Throwable t) {
      if (logger.isWarnEnabled())
        logger.warn("Failed to close the old Selector.", t); 
    } 
    logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
  }
  
  protected void run() {
    while (true) {
      boolean oldWakenUp = this.wakenUp.getAndSet(false);
      try {
        if (hasTasks()) {
          selectNow();
        } else {
          select(oldWakenUp);
          if (this.wakenUp.get())
            this.selector.wakeup(); 
        } 
        this.cancelledKeys = 0;
        this.needsToSelectAgain = false;
        int ioRatio = this.ioRatio;
        if (ioRatio == 100) {
          processSelectedKeys();
          runAllTasks();
        } else {
          long ioStartTime = System.nanoTime();
          processSelectedKeys();
          long ioTime = System.nanoTime() - ioStartTime;
          runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
        } 
        if (isShuttingDown()) {
          closeAll();
          if (confirmShutdown())
            break; 
        } 
      } catch (Throwable t) {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException e) {}
      } 
    } 
  }
  
  private void processSelectedKeys() {
    if (this.selectedKeys != null) {
      processSelectedKeysOptimized(this.selectedKeys.flip());
    } else {
      processSelectedKeysPlain(this.selector.selectedKeys());
    } 
  }
  
  protected void cleanup() {
    try {
      this.selector.close();
    } catch (IOException e) {
      logger.warn("Failed to close a selector.", e);
    } 
  }
  
  void cancel(SelectionKey key) {
    key.cancel();
    this.cancelledKeys++;
    if (this.cancelledKeys >= 256) {
      this.cancelledKeys = 0;
      this.needsToSelectAgain = true;
    } 
  }
  
  protected Runnable pollTask() {
    Runnable task = super.pollTask();
    if (this.needsToSelectAgain)
      selectAgain(); 
    return task;
  }
  
  private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
    if (selectedKeys.isEmpty())
      return; 
    Iterator<SelectionKey> i = selectedKeys.iterator();
    while (true) {
      SelectionKey k = i.next();
      Object a = k.attachment();
      i.remove();
      if (a instanceof AbstractNioChannel) {
        processSelectedKey(k, (AbstractNioChannel)a);
      } else {
        NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
        processSelectedKey(k, task);
      } 
      if (!i.hasNext())
        break; 
      if (this.needsToSelectAgain) {
        selectAgain();
        selectedKeys = this.selector.selectedKeys();
        if (selectedKeys.isEmpty())
          break; 
        i = selectedKeys.iterator();
      } 
    } 
  }
  
  private void processSelectedKeysOptimized(SelectionKey[] selectedKeys) {
    for (int i = 0;; i++) {
      SelectionKey k = selectedKeys[i];
      if (k == null)
        break; 
      selectedKeys[i] = null;
      Object a = k.attachment();
      if (a instanceof AbstractNioChannel) {
        processSelectedKey(k, (AbstractNioChannel)a);
      } else {
        NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
        processSelectedKey(k, task);
      } 
      if (this.needsToSelectAgain) {
        while (selectedKeys[i] != null) {
          selectedKeys[i] = null;
          i++;
        } 
        selectAgain();
        selectedKeys = this.selectedKeys.flip();
        i = -1;
      } 
    } 
  }
  
  private static void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
    AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
    if (!k.isValid()) {
      unsafe.close(unsafe.voidPromise());
      return;
    } 
    try {
      int readyOps = k.readyOps();
      if ((readyOps & 0x11) != 0 || readyOps == 0) {
        unsafe.read();
        if (!ch.isOpen())
          return; 
      } 
      if ((readyOps & 0x4) != 0)
        ch.unsafe().forceFlush(); 
      if ((readyOps & 0x8) != 0) {
        int ops = k.interestOps();
        ops &= 0xFFFFFFF7;
        k.interestOps(ops);
        unsafe.finishConnect();
      } 
    } catch (CancelledKeyException ignored) {
      unsafe.close(unsafe.voidPromise());
    } 
  }
  
  private static void processSelectedKey(SelectionKey k, NioTask<SelectableChannel> task) {
    int state = 0;
    try {
      task.channelReady(k.channel(), k);
      state = 1;
    } catch (Exception e) {
      k.cancel();
      invokeChannelUnregistered(task, k, e);
      state = 2;
    } finally {
      switch (state) {
        case 0:
          k.cancel();
          invokeChannelUnregistered(task, k, (Throwable)null);
          break;
        case 1:
          if (!k.isValid())
            invokeChannelUnregistered(task, k, (Throwable)null); 
          break;
      } 
    } 
  }
  
  private void closeAll() {
    selectAgain();
    Set<SelectionKey> keys = this.selector.keys();
    Collection<AbstractNioChannel> channels = new ArrayList<AbstractNioChannel>(keys.size());
    for (SelectionKey k : keys) {
      Object a = k.attachment();
      if (a instanceof AbstractNioChannel) {
        channels.add((AbstractNioChannel)a);
        continue;
      } 
      k.cancel();
      NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
      invokeChannelUnregistered(task, k, (Throwable)null);
    } 
    for (AbstractNioChannel ch : channels)
      ch.unsafe().close(ch.unsafe().voidPromise()); 
  }
  
  private static void invokeChannelUnregistered(NioTask<SelectableChannel> task, SelectionKey k, Throwable cause) {
    try {
      task.channelUnregistered(k.channel(), cause);
    } catch (Exception e) {
      logger.warn("Unexpected exception while running NioTask.channelUnregistered()", e);
    } 
  }
  
  protected void wakeup(boolean inEventLoop) {
    if (!inEventLoop && this.wakenUp.compareAndSet(false, true))
      this.selector.wakeup(); 
  }
  
  void selectNow() throws IOException {
    try {
      this.selector.selectNow();
    } finally {
      if (this.wakenUp.get())
        this.selector.wakeup(); 
    } 
  }
  
  private void select(boolean oldWakenUp) throws IOException {
    Selector selector = this.selector;
    try {
      int selectCnt = 0;
      long currentTimeNanos = System.nanoTime();
      long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
      while (true) {
        long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
        if (timeoutMillis <= 0L) {
          if (selectCnt == 0) {
            selector.selectNow();
            selectCnt = 1;
          } 
          break;
        } 
        int selectedKeys = selector.select(timeoutMillis);
        selectCnt++;
        if (selectedKeys != 0 || oldWakenUp || this.wakenUp.get() || hasTasks() || hasScheduledTasks())
          break; 
        if (Thread.interrupted()) {
          if (logger.isDebugEnabled())
            logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop."); 
          selectCnt = 1;
          break;
        } 
        long time = System.nanoTime();
        if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
          selectCnt = 1;
        } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
          logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding selector.", Integer.valueOf(selectCnt));
          rebuildSelector();
          selector = this.selector;
          selector.selectNow();
          selectCnt = 1;
          break;
        } 
        currentTimeNanos = time;
      } 
      if (selectCnt > 3 && 
        logger.isDebugEnabled())
        logger.debug("Selector.select() returned prematurely {} times in a row.", Integer.valueOf(selectCnt - 1)); 
    } catch (CancelledKeyException e) {
      if (logger.isDebugEnabled())
        logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector - JDK bug?", e); 
    } 
  }
  
  private void selectAgain() {
    this.needsToSelectAgain = false;
    try {
      this.selector.selectNow();
    } catch (Throwable t) {
      logger.warn("Failed to update SelectionKeys.", t);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\nio\NioEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */