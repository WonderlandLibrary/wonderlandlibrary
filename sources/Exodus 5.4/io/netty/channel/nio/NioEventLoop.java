/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.EventLoopException;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.nio.NioTask;
import io.netty.channel.nio.SelectedSelectionKeySet;
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
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class NioEventLoop
extends SingleThreadEventLoop {
    private int cancelledKeys;
    private volatile int ioRatio = 50;
    private static final int CLEANUP_INTERVAL = 256;
    private static final InternalLogger logger;
    Selector selector;
    private boolean needsToSelectAgain;
    private final SelectorProvider provider;
    private static final boolean DISABLE_KEYSET_OPTIMIZATION;
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
    private SelectedSelectionKeySet selectedKeys;
    private final AtomicBoolean wakenUp = new AtomicBoolean();
    private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;

    void selectNow() throws IOException {
        block0: {
            this.selector.selectNow();
            if (!this.wakenUp.get()) break block0;
            this.selector.wakeup();
        }
    }

    public void setIoRatio(int n) {
        if (n <= 0 || n > 100) {
            throw new IllegalArgumentException("ioRatio: " + n + " (expected: 0 < ioRatio <= 100)");
        }
        this.ioRatio = n;
    }

    @Override
    protected Runnable pollTask() {
        Runnable runnable = super.pollTask();
        if (this.needsToSelectAgain) {
            this.selectAgain();
        }
        return runnable;
    }

    private Selector openSelector() {
        AbstractSelector abstractSelector;
        try {
            abstractSelector = this.provider.openSelector();
        }
        catch (IOException iOException) {
            throw new ChannelException("failed to open a new selector", iOException);
        }
        if (DISABLE_KEYSET_OPTIMIZATION) {
            return abstractSelector;
        }
        try {
            SelectedSelectionKeySet selectedSelectionKeySet = new SelectedSelectionKeySet();
            Class<?> clazz = Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
            if (!clazz.isAssignableFrom(abstractSelector.getClass())) {
                return abstractSelector;
            }
            Field field = clazz.getDeclaredField("selectedKeys");
            Field field2 = clazz.getDeclaredField("publicSelectedKeys");
            field.setAccessible(true);
            field2.setAccessible(true);
            field.set(abstractSelector, selectedSelectionKeySet);
            field2.set(abstractSelector, selectedSelectionKeySet);
            this.selectedKeys = selectedSelectionKeySet;
            logger.trace("Instrumented an optimized java.util.Set into: {}", (Object)abstractSelector);
        }
        catch (Throwable throwable) {
            this.selectedKeys = null;
            logger.trace("Failed to instrument an optimized java.util.Set into: {}", (Object)abstractSelector, (Object)throwable);
        }
        return abstractSelector;
    }

    @Override
    protected void run() {
        while (true) {
            boolean bl = this.wakenUp.getAndSet(false);
            try {
                if (this.hasTasks()) {
                    this.selectNow();
                } else {
                    this.select(bl);
                    if (this.wakenUp.get()) {
                        this.selector.wakeup();
                    }
                }
                this.cancelledKeys = 0;
                this.needsToSelectAgain = false;
                int n = this.ioRatio;
                if (n == 100) {
                    this.processSelectedKeys();
                    this.runAllTasks();
                } else {
                    long l = System.nanoTime();
                    this.processSelectedKeys();
                    long l2 = System.nanoTime() - l;
                    this.runAllTasks(l2 * (long)(100 - n) / (long)n);
                }
                if (!this.isShuttingDown()) continue;
                this.closeAll();
                if (!this.confirmShutdown()) continue;
            }
            catch (Throwable throwable) {
                logger.warn("Unexpected exception in the selector loop.", throwable);
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException interruptedException) {}
                continue;
            }
            break;
        }
    }

    static {
        block5: {
            logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
            DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
            String string = "sun.nio.ch.bugLevel";
            try {
                String string2 = SystemPropertyUtil.get(string);
                if (string2 == null) {
                    System.setProperty(string, "");
                }
            }
            catch (SecurityException securityException) {
                if (!logger.isDebugEnabled()) break block5;
                logger.debug("Unable to get/set System Property: {}", (Object)string, (Object)securityException);
            }
        }
        int n = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
        if (n < 3) {
            n = 0;
        }
        SELECTOR_AUTO_REBUILD_THRESHOLD = n;
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noKeySetOptimization: {}", (Object)DISABLE_KEYSET_OPTIMIZATION);
            logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", (Object)SELECTOR_AUTO_REBUILD_THRESHOLD);
        }
    }

    @Override
    protected void cleanup() {
        try {
            this.selector.close();
        }
        catch (IOException iOException) {
            logger.warn("Failed to close a selector.", iOException);
        }
    }

    @Override
    protected void wakeup(boolean bl) {
        if (!bl && this.wakenUp.compareAndSet(false, true)) {
            this.selector.wakeup();
        }
    }

    private void selectAgain() {
        this.needsToSelectAgain = false;
        try {
            this.selector.selectNow();
        }
        catch (Throwable throwable) {
            logger.warn("Failed to update SelectionKeys.", throwable);
        }
    }

    public int getIoRatio() {
        return this.ioRatio;
    }

    private static void invokeChannelUnregistered(NioTask<SelectableChannel> nioTask, SelectionKey selectionKey, Throwable throwable) {
        try {
            nioTask.channelUnregistered(selectionKey.channel(), throwable);
        }
        catch (Exception exception) {
            logger.warn("Unexpected exception while running NioTask.channelUnregistered()", exception);
        }
    }

    private void select(boolean bl) throws IOException {
        block10: {
            Selector selector = this.selector;
            try {
                int n = 0;
                long l = System.nanoTime();
                long l2 = l + this.delayNanos(l);
                while (true) {
                    long l3;
                    if ((l3 = (l2 - l + 500000L) / 1000000L) <= 0L) {
                        if (n != 0) break;
                        selector.selectNow();
                        n = 1;
                        break;
                    }
                    int n2 = selector.select(l3);
                    ++n;
                    if (n2 != 0 || bl || this.wakenUp.get() || this.hasTasks() || this.hasScheduledTasks()) break;
                    if (Thread.interrupted()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
                        }
                        n = 1;
                        break;
                    }
                    long l4 = System.nanoTime();
                    if (l4 - TimeUnit.MILLISECONDS.toNanos(l3) >= l) {
                        n = 1;
                    } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && n >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
                        logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding selector.", (Object)n);
                        this.rebuildSelector();
                        selector = this.selector;
                        selector.selectNow();
                        n = 1;
                        break;
                    }
                    l = l4;
                }
                if (n > 3 && logger.isDebugEnabled()) {
                    logger.debug("Selector.select() returned prematurely {} times in a row.", (Object)(n - 1));
                }
            }
            catch (CancelledKeyException cancelledKeyException) {
                if (!logger.isDebugEnabled()) break block10;
                logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector - JDK bug?", cancelledKeyException);
            }
        }
    }

    public void register(SelectableChannel selectableChannel, int n, NioTask<?> nioTask) {
        if (selectableChannel == null) {
            throw new NullPointerException("ch");
        }
        if (n == 0) {
            throw new IllegalArgumentException("interestOps must be non-zero.");
        }
        if ((n & ~selectableChannel.validOps()) != 0) {
            throw new IllegalArgumentException("invalid interestOps: " + n + "(validOps: " + selectableChannel.validOps() + ')');
        }
        if (nioTask == null) {
            throw new NullPointerException("task");
        }
        if (this.isShutdown()) {
            throw new IllegalStateException("event loop shut down");
        }
        try {
            selectableChannel.register(this.selector, n, nioTask);
        }
        catch (Exception exception) {
            throw new EventLoopException("failed to register a channel", exception);
        }
    }

    void cancel(SelectionKey selectionKey) {
        selectionKey.cancel();
        ++this.cancelledKeys;
        if (this.cancelledKeys >= 256) {
            this.cancelledKeys = 0;
            this.needsToSelectAgain = true;
        }
    }

    private void processSelectedKeysPlain(Set<SelectionKey> set) {
        if (set.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> iterator = set.iterator();
        while (true) {
            SelectionKey selectionKey = iterator.next();
            Object object = selectionKey.attachment();
            iterator.remove();
            if (object instanceof AbstractNioChannel) {
                NioEventLoop.processSelectedKey(selectionKey, (AbstractNioChannel)object);
            } else {
                NioTask nioTask = (NioTask)object;
                NioEventLoop.processSelectedKey(selectionKey, nioTask);
            }
            if (!iterator.hasNext()) break;
            if (!this.needsToSelectAgain) continue;
            this.selectAgain();
            set = this.selector.selectedKeys();
            if (set.isEmpty()) break;
            iterator = set.iterator();
        }
    }

    private void closeAll() {
        this.selectAgain();
        Set<SelectionKey> set = this.selector.keys();
        ArrayList<AbstractNioChannel> arrayList = new ArrayList<AbstractNioChannel>(set.size());
        for (SelectionKey object : set) {
            Object object2 = object.attachment();
            if (object2 instanceof AbstractNioChannel) {
                arrayList.add((AbstractNioChannel)object2);
                continue;
            }
            object.cancel();
            NioTask nioTask = (NioTask)object2;
            NioEventLoop.invokeChannelUnregistered(nioTask, object, null);
        }
        for (AbstractNioChannel abstractNioChannel : arrayList) {
            abstractNioChannel.unsafe().close(abstractNioChannel.unsafe().voidPromise());
        }
    }

    private void processSelectedKeys() {
        if (this.selectedKeys != null) {
            this.processSelectedKeysOptimized(this.selectedKeys.flip());
        } else {
            this.processSelectedKeysPlain(this.selector.selectedKeys());
        }
    }

    @Override
    protected Queue<Runnable> newTaskQueue() {
        return PlatformDependent.newMpscQueue();
    }

    private static void processSelectedKey(SelectionKey selectionKey, AbstractNioChannel abstractNioChannel) {
        Channel.Unsafe unsafe = abstractNioChannel.unsafe();
        if (!selectionKey.isValid()) {
            unsafe.close(unsafe.voidPromise());
            return;
        }
        try {
            int n = selectionKey.readyOps();
            if ((n & 0x11) != 0 || n == 0) {
                unsafe.read();
                if (!abstractNioChannel.isOpen()) {
                    return;
                }
            }
            if ((n & 4) != 0) {
                abstractNioChannel.unsafe().forceFlush();
            }
            if ((n & 8) != 0) {
                int n2 = selectionKey.interestOps();
                selectionKey.interestOps(n2 &= 0xFFFFFFF7);
                unsafe.finishConnect();
            }
        }
        catch (CancelledKeyException cancelledKeyException) {
            unsafe.close(unsafe.voidPromise());
        }
    }

    private static void processSelectedKey(SelectionKey selectionKey, NioTask<SelectableChannel> nioTask) {
        int n = 0;
        try {
            nioTask.channelReady(selectionKey.channel(), selectionKey);
            n = 1;
        }
        catch (Exception exception) {
            selectionKey.cancel();
            NioEventLoop.invokeChannelUnregistered(nioTask, selectionKey, exception);
            n = 2;
            switch (n) {
                case 0: {
                    selectionKey.cancel();
                    NioEventLoop.invokeChannelUnregistered(nioTask, selectionKey, null);
                    break;
                }
                case 1: {
                    if (selectionKey.isValid()) break;
                    NioEventLoop.invokeChannelUnregistered(nioTask, selectionKey, null);
                }
            }
        }
        switch (n) {
            case 0: {
                selectionKey.cancel();
                NioEventLoop.invokeChannelUnregistered(nioTask, selectionKey, null);
                break;
            }
            case 1: {
                if (selectionKey.isValid()) break;
                NioEventLoop.invokeChannelUnregistered(nioTask, selectionKey, null);
            }
        }
    }

    private void processSelectedKeysOptimized(SelectionKey[] selectionKeyArray) {
        SelectionKey selectionKey;
        int n = 0;
        while ((selectionKey = selectionKeyArray[n]) != null) {
            selectionKeyArray[n] = null;
            Object object = selectionKey.attachment();
            if (object instanceof AbstractNioChannel) {
                NioEventLoop.processSelectedKey(selectionKey, (AbstractNioChannel)object);
            } else {
                NioTask nioTask = (NioTask)object;
                NioEventLoop.processSelectedKey(selectionKey, nioTask);
            }
            if (this.needsToSelectAgain) {
                while (selectionKeyArray[n] != null) {
                    selectionKeyArray[n] = null;
                    ++n;
                }
                this.selectAgain();
                selectionKeyArray = this.selectedKeys.flip();
                n = -1;
            }
            ++n;
        }
    }

    NioEventLoop(NioEventLoopGroup nioEventLoopGroup, ThreadFactory threadFactory, SelectorProvider selectorProvider) {
        super(nioEventLoopGroup, threadFactory, false);
        if (selectorProvider == null) {
            throw new NullPointerException("selectorProvider");
        }
        this.provider = selectorProvider;
        this.selector = this.openSelector();
    }

    public void rebuildSelector() {
        int n;
        block14: {
            Selector selector;
            if (!this.inEventLoop()) {
                this.execute(new Runnable(){

                    @Override
                    public void run() {
                        NioEventLoop.this.rebuildSelector();
                    }
                });
                return;
            }
            Selector selector2 = this.selector;
            if (selector2 == null) {
                return;
            }
            try {
                selector = this.openSelector();
            }
            catch (Exception exception) {
                logger.warn("Failed to create a new Selector.", exception);
                return;
            }
            n = 0;
            while (true) {
                try {
                    for (SelectionKey selectionKey : selector2.keys()) {
                        Object object;
                        Object object2 = selectionKey.attachment();
                        try {
                            if (!selectionKey.isValid() || selectionKey.channel().keyFor(selector) != null) continue;
                            int n2 = selectionKey.interestOps();
                            selectionKey.cancel();
                            object = selectionKey.channel().register(selector, n2, object2);
                            if (object2 instanceof AbstractNioChannel) {
                                ((AbstractNioChannel)object2).selectionKey = object;
                            }
                            ++n;
                        }
                        catch (Exception exception) {
                            logger.warn("Failed to re-register a Channel to the new Selector.", exception);
                            if (object2 instanceof AbstractNioChannel) {
                                object = (AbstractNioChannel)object2;
                                ((AbstractNioChannel)object).unsafe().close(((AbstractNioChannel)object).unsafe().voidPromise());
                                continue;
                            }
                            object = (NioTask)object2;
                            NioEventLoop.invokeChannelUnregistered((NioTask<SelectableChannel>)object, selectionKey, exception);
                        }
                    }
                }
                catch (ConcurrentModificationException concurrentModificationException) {
                    continue;
                }
                break;
            }
            this.selector = selector;
            try {
                selector2.close();
            }
            catch (Throwable throwable) {
                if (!logger.isWarnEnabled()) break block14;
                logger.warn("Failed to close the old Selector.", throwable);
            }
        }
        logger.info("Migrated " + n + " channel(s) to the new Selector.");
    }
}

