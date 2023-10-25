package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final class DefaultChannelPipeline implements ChannelPipeline {
  static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
  
  private static final WeakHashMap<Class<?>, String>[] nameCaches = (WeakHashMap<Class<?>, String>[])new WeakHashMap[Runtime.getRuntime().availableProcessors()];
  
  final AbstractChannel channel;
  
  final AbstractChannelHandlerContext head;
  
  final AbstractChannelHandlerContext tail;
  
  static {
    for (int i = 0; i < nameCaches.length; i++)
      nameCaches[i] = new WeakHashMap<Class<?>, String>(); 
  }
  
  private final Map<String, AbstractChannelHandlerContext> name2ctx = new HashMap<String, AbstractChannelHandlerContext>(4);
  
  final Map<EventExecutorGroup, EventExecutor> childExecutors = new IdentityHashMap<EventExecutorGroup, EventExecutor>();
  
  public DefaultChannelPipeline(AbstractChannel channel) {
    if (channel == null)
      throw new NullPointerException("channel"); 
    this.channel = channel;
    this.tail = new TailContext(this);
    this.head = new HeadContext(this);
    this.head.next = this.tail;
    this.tail.prev = this.head;
  }
  
  public Channel channel() {
    return this.channel;
  }
  
  public ChannelPipeline addFirst(String name, ChannelHandler handler) {
    return addFirst(null, name, handler);
  }
  
  public ChannelPipeline addFirst(EventExecutorGroup group, String name, ChannelHandler handler) {
    synchronized (this) {
      checkDuplicateName(name);
      AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
      addFirst0(name, newCtx);
    } 
    return this;
  }
  
  private void addFirst0(String name, AbstractChannelHandlerContext newCtx) {
    checkMultiplicity(newCtx);
    AbstractChannelHandlerContext nextCtx = this.head.next;
    newCtx.prev = this.head;
    newCtx.next = nextCtx;
    this.head.next = newCtx;
    nextCtx.prev = newCtx;
    this.name2ctx.put(name, newCtx);
    callHandlerAdded(newCtx);
  }
  
  public ChannelPipeline addLast(String name, ChannelHandler handler) {
    return addLast(null, name, handler);
  }
  
  public ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler) {
    synchronized (this) {
      checkDuplicateName(name);
      AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
      addLast0(name, newCtx);
    } 
    return this;
  }
  
  private void addLast0(String name, AbstractChannelHandlerContext newCtx) {
    checkMultiplicity(newCtx);
    AbstractChannelHandlerContext prev = this.tail.prev;
    newCtx.prev = prev;
    newCtx.next = this.tail;
    prev.next = newCtx;
    this.tail.prev = newCtx;
    this.name2ctx.put(name, newCtx);
    callHandlerAdded(newCtx);
  }
  
  public ChannelPipeline addBefore(String baseName, String name, ChannelHandler handler) {
    return addBefore(null, baseName, name, handler);
  }
  
  public ChannelPipeline addBefore(EventExecutorGroup group, String baseName, String name, ChannelHandler handler) {
    synchronized (this) {
      AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
      checkDuplicateName(name);
      AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
      addBefore0(name, ctx, newCtx);
    } 
    return this;
  }
  
  private void addBefore0(String name, AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
    checkMultiplicity(newCtx);
    newCtx.prev = ctx.prev;
    newCtx.next = ctx;
    ctx.prev.next = newCtx;
    ctx.prev = newCtx;
    this.name2ctx.put(name, newCtx);
    callHandlerAdded(newCtx);
  }
  
  public ChannelPipeline addAfter(String baseName, String name, ChannelHandler handler) {
    return addAfter(null, baseName, name, handler);
  }
  
  public ChannelPipeline addAfter(EventExecutorGroup group, String baseName, String name, ChannelHandler handler) {
    synchronized (this) {
      AbstractChannelHandlerContext ctx = getContextOrDie(baseName);
      checkDuplicateName(name);
      AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, group, name, handler);
      addAfter0(name, ctx, newCtx);
    } 
    return this;
  }
  
  private void addAfter0(String name, AbstractChannelHandlerContext ctx, AbstractChannelHandlerContext newCtx) {
    checkDuplicateName(name);
    checkMultiplicity(newCtx);
    newCtx.prev = ctx;
    newCtx.next = ctx.next;
    ctx.next.prev = newCtx;
    ctx.next = newCtx;
    this.name2ctx.put(name, newCtx);
    callHandlerAdded(newCtx);
  }
  
  public ChannelPipeline addFirst(ChannelHandler... handlers) {
    return addFirst((EventExecutorGroup)null, handlers);
  }
  
  public ChannelPipeline addFirst(EventExecutorGroup executor, ChannelHandler... handlers) {
    if (handlers == null)
      throw new NullPointerException("handlers"); 
    if (handlers.length == 0 || handlers[0] == null)
      return this; 
    int size;
    for (size = 1; size < handlers.length && 
      handlers[size] != null; size++);
    for (int i = size - 1; i >= 0; i--) {
      ChannelHandler h = handlers[i];
      addFirst(executor, generateName(h), h);
    } 
    return this;
  }
  
  public ChannelPipeline addLast(ChannelHandler... handlers) {
    return addLast((EventExecutorGroup)null, handlers);
  }
  
  public ChannelPipeline addLast(EventExecutorGroup executor, ChannelHandler... handlers) {
    if (handlers == null)
      throw new NullPointerException("handlers"); 
    for (ChannelHandler h : handlers) {
      if (h == null)
        break; 
      addLast(executor, generateName(h), h);
    } 
    return this;
  }
  
  private String generateName(ChannelHandler handler) {
    String name;
    WeakHashMap<Class<?>, String> cache = nameCaches[(int)(Thread.currentThread().getId() % nameCaches.length)];
    Class<?> handlerType = handler.getClass();
    synchronized (cache) {
      name = cache.get(handlerType);
      if (name == null) {
        name = generateName0(handlerType);
        cache.put(handlerType, name);
      } 
    } 
    synchronized (this) {
      if (this.name2ctx.containsKey(name)) {
        String baseName = name.substring(0, name.length() - 1);
        for (int i = 1;; i++) {
          String newName = baseName + i;
          if (!this.name2ctx.containsKey(newName)) {
            name = newName;
            break;
          } 
        } 
      } 
    } 
    return name;
  }
  
  private static String generateName0(Class<?> handlerType) {
    return StringUtil.simpleClassName(handlerType) + "#0";
  }
  
  public ChannelPipeline remove(ChannelHandler handler) {
    remove(getContextOrDie(handler));
    return this;
  }
  
  public ChannelHandler remove(String name) {
    return remove(getContextOrDie(name)).handler();
  }
  
  public <T extends ChannelHandler> T remove(Class<T> handlerType) {
    return (T)remove(getContextOrDie(handlerType)).handler();
  }
  
  private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
    AbstractChannelHandlerContext context;
    Future future;
    assert ctx != this.head && ctx != this.tail;
    synchronized (this) {
      if (!ctx.channel().isRegistered() || ctx.executor().inEventLoop()) {
        remove0(ctx);
        return ctx;
      } 
      future = ctx.executor().submit(new Runnable() {
            public void run() {
              synchronized (DefaultChannelPipeline.this) {
                DefaultChannelPipeline.this.remove0(ctx);
              } 
            }
          });
      context = ctx;
    } 
    waitForFuture((Future<?>)future);
    return context;
  }
  
  void remove0(AbstractChannelHandlerContext ctx) {
    AbstractChannelHandlerContext prev = ctx.prev;
    AbstractChannelHandlerContext next = ctx.next;
    prev.next = next;
    next.prev = prev;
    this.name2ctx.remove(ctx.name());
    callHandlerRemoved(ctx);
  }
  
  public ChannelHandler removeFirst() {
    if (this.head.next == this.tail)
      throw new NoSuchElementException(); 
    return remove(this.head.next).handler();
  }
  
  public ChannelHandler removeLast() {
    if (this.head.next == this.tail)
      throw new NoSuchElementException(); 
    return remove(this.tail.prev).handler();
  }
  
  public ChannelPipeline replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler) {
    replace(getContextOrDie(oldHandler), newName, newHandler);
    return this;
  }
  
  public ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler) {
    return replace(getContextOrDie(oldName), newName, newHandler);
  }
  
  public <T extends ChannelHandler> T replace(Class<T> oldHandlerType, String newName, ChannelHandler newHandler) {
    return (T)replace(getContextOrDie(oldHandlerType), newName, newHandler);
  }
  
  private ChannelHandler replace(final AbstractChannelHandlerContext ctx, final String newName, ChannelHandler newHandler) {
    Future future;
    assert ctx != this.head && ctx != this.tail;
    synchronized (this) {
      boolean sameName = ctx.name().equals(newName);
      if (!sameName)
        checkDuplicateName(newName); 
      final AbstractChannelHandlerContext newCtx = new DefaultChannelHandlerContext(this, (EventExecutorGroup)ctx.executor, newName, newHandler);
      if (!newCtx.channel().isRegistered() || newCtx.executor().inEventLoop()) {
        replace0(ctx, newName, newCtx);
        return ctx.handler();
      } 
      future = newCtx.executor().submit(new Runnable() {
            public void run() {
              synchronized (DefaultChannelPipeline.this) {
                DefaultChannelPipeline.this.replace0(ctx, newName, newCtx);
              } 
            }
          });
    } 
    waitForFuture((Future<?>)future);
    return ctx.handler();
  }
  
  private void replace0(AbstractChannelHandlerContext oldCtx, String newName, AbstractChannelHandlerContext newCtx) {
    checkMultiplicity(newCtx);
    AbstractChannelHandlerContext prev = oldCtx.prev;
    AbstractChannelHandlerContext next = oldCtx.next;
    newCtx.prev = prev;
    newCtx.next = next;
    prev.next = newCtx;
    next.prev = newCtx;
    if (!oldCtx.name().equals(newName))
      this.name2ctx.remove(oldCtx.name()); 
    this.name2ctx.put(newName, newCtx);
    oldCtx.prev = newCtx;
    oldCtx.next = newCtx;
    callHandlerAdded(newCtx);
    callHandlerRemoved(oldCtx);
  }
  
  private static void checkMultiplicity(ChannelHandlerContext ctx) {
    ChannelHandler handler = ctx.handler();
    if (handler instanceof ChannelHandlerAdapter) {
      ChannelHandlerAdapter h = (ChannelHandlerAdapter)handler;
      if (!h.isSharable() && h.added)
        throw new ChannelPipelineException(h.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times."); 
      h.added = true;
    } 
  }
  
  private void callHandlerAdded(final ChannelHandlerContext ctx) {
    if (ctx.channel().isRegistered() && !ctx.executor().inEventLoop()) {
      ctx.executor().execute(new Runnable() {
            public void run() {
              DefaultChannelPipeline.this.callHandlerAdded0(ctx);
            }
          });
      return;
    } 
    callHandlerAdded0(ctx);
  }
  
  private void callHandlerAdded0(ChannelHandlerContext ctx) {
    try {
      ctx.handler().handlerAdded(ctx);
    } catch (Throwable t) {
      boolean removed = false;
      try {
        remove((AbstractChannelHandlerContext)ctx);
        removed = true;
      } catch (Throwable t2) {
        if (logger.isWarnEnabled())
          logger.warn("Failed to remove a handler: " + ctx.name(), t2); 
      } 
      if (removed) {
        fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", t));
      } else {
        fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", t));
      } 
    } 
  }
  
  private void callHandlerRemoved(final AbstractChannelHandlerContext ctx) {
    if (ctx.channel().isRegistered() && !ctx.executor().inEventLoop()) {
      ctx.executor().execute(new Runnable() {
            public void run() {
              DefaultChannelPipeline.this.callHandlerRemoved0(ctx);
            }
          });
      return;
    } 
    callHandlerRemoved0(ctx);
  }
  
  private void callHandlerRemoved0(AbstractChannelHandlerContext ctx) {
    try {
      ctx.handler().handlerRemoved(ctx);
      ctx.setRemoved();
    } catch (Throwable t) {
      fireExceptionCaught(new ChannelPipelineException(ctx.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
    } 
  }
  
  private static void waitForFuture(Future<?> future) {
    try {
      future.get();
    } catch (ExecutionException ex) {
      PlatformDependent.throwException(ex.getCause());
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    } 
  }
  
  public ChannelHandler first() {
    ChannelHandlerContext first = firstContext();
    if (first == null)
      return null; 
    return first.handler();
  }
  
  public ChannelHandlerContext firstContext() {
    AbstractChannelHandlerContext first = this.head.next;
    if (first == this.tail)
      return null; 
    return this.head.next;
  }
  
  public ChannelHandler last() {
    AbstractChannelHandlerContext last = this.tail.prev;
    if (last == this.head)
      return null; 
    return last.handler();
  }
  
  public ChannelHandlerContext lastContext() {
    AbstractChannelHandlerContext last = this.tail.prev;
    if (last == this.head)
      return null; 
    return last;
  }
  
  public ChannelHandler get(String name) {
    ChannelHandlerContext ctx = context(name);
    if (ctx == null)
      return null; 
    return ctx.handler();
  }
  
  public <T extends ChannelHandler> T get(Class<T> handlerType) {
    ChannelHandlerContext ctx = context(handlerType);
    if (ctx == null)
      return null; 
    return (T)ctx.handler();
  }
  
  public ChannelHandlerContext context(String name) {
    if (name == null)
      throw new NullPointerException("name"); 
    synchronized (this) {
      return this.name2ctx.get(name);
    } 
  }
  
  public ChannelHandlerContext context(ChannelHandler handler) {
    if (handler == null)
      throw new NullPointerException("handler"); 
    AbstractChannelHandlerContext ctx = this.head.next;
    while (true) {
      if (ctx == null)
        return null; 
      if (ctx.handler() == handler)
        return ctx; 
      ctx = ctx.next;
    } 
  }
  
  public ChannelHandlerContext context(Class<? extends ChannelHandler> handlerType) {
    if (handlerType == null)
      throw new NullPointerException("handlerType"); 
    AbstractChannelHandlerContext ctx = this.head.next;
    while (true) {
      if (ctx == null)
        return null; 
      if (handlerType.isAssignableFrom(ctx.handler().getClass()))
        return ctx; 
      ctx = ctx.next;
    } 
  }
  
  public List<String> names() {
    List<String> list = new ArrayList<String>();
    AbstractChannelHandlerContext ctx = this.head.next;
    while (true) {
      if (ctx == null)
        return list; 
      list.add(ctx.name());
      ctx = ctx.next;
    } 
  }
  
  public Map<String, ChannelHandler> toMap() {
    Map<String, ChannelHandler> map = new LinkedHashMap<String, ChannelHandler>();
    AbstractChannelHandlerContext ctx = this.head.next;
    while (true) {
      if (ctx == this.tail)
        return map; 
      map.put(ctx.name(), ctx.handler());
      ctx = ctx.next;
    } 
  }
  
  public Iterator<Map.Entry<String, ChannelHandler>> iterator() {
    return toMap().entrySet().iterator();
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(StringUtil.simpleClassName(this));
    buf.append('{');
    AbstractChannelHandlerContext ctx = this.head.next;
    while (ctx != this.tail) {
      buf.append('(');
      buf.append(ctx.name());
      buf.append(" = ");
      buf.append(ctx.handler().getClass().getName());
      buf.append(')');
      ctx = ctx.next;
      if (ctx == this.tail)
        break; 
      buf.append(", ");
    } 
    buf.append('}');
    return buf.toString();
  }
  
  public ChannelPipeline fireChannelRegistered() {
    this.head.fireChannelRegistered();
    return this;
  }
  
  public ChannelPipeline fireChannelUnregistered() {
    this.head.fireChannelUnregistered();
    if (!this.channel.isOpen())
      teardownAll(); 
    return this;
  }
  
  private void teardownAll() {
    this.tail.prev.teardown();
  }
  
  public ChannelPipeline fireChannelActive() {
    this.head.fireChannelActive();
    if (this.channel.config().isAutoRead())
      this.channel.read(); 
    return this;
  }
  
  public ChannelPipeline fireChannelInactive() {
    this.head.fireChannelInactive();
    return this;
  }
  
  public ChannelPipeline fireExceptionCaught(Throwable cause) {
    this.head.fireExceptionCaught(cause);
    return this;
  }
  
  public ChannelPipeline fireUserEventTriggered(Object event) {
    this.head.fireUserEventTriggered(event);
    return this;
  }
  
  public ChannelPipeline fireChannelRead(Object msg) {
    this.head.fireChannelRead(msg);
    return this;
  }
  
  public ChannelPipeline fireChannelReadComplete() {
    this.head.fireChannelReadComplete();
    if (this.channel.config().isAutoRead())
      read(); 
    return this;
  }
  
  public ChannelPipeline fireChannelWritabilityChanged() {
    this.head.fireChannelWritabilityChanged();
    return this;
  }
  
  public ChannelFuture bind(SocketAddress localAddress) {
    return this.tail.bind(localAddress);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress) {
    return this.tail.connect(remoteAddress);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
    return this.tail.connect(remoteAddress, localAddress);
  }
  
  public ChannelFuture disconnect() {
    return this.tail.disconnect();
  }
  
  public ChannelFuture close() {
    return this.tail.close();
  }
  
  public ChannelFuture deregister() {
    return this.tail.deregister();
  }
  
  public ChannelPipeline flush() {
    this.tail.flush();
    return this;
  }
  
  public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
    return this.tail.bind(localAddress, promise);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
    return this.tail.connect(remoteAddress, promise);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
    return this.tail.connect(remoteAddress, localAddress, promise);
  }
  
  public ChannelFuture disconnect(ChannelPromise promise) {
    return this.tail.disconnect(promise);
  }
  
  public ChannelFuture close(ChannelPromise promise) {
    return this.tail.close(promise);
  }
  
  public ChannelFuture deregister(ChannelPromise promise) {
    return this.tail.deregister(promise);
  }
  
  public ChannelPipeline read() {
    this.tail.read();
    return this;
  }
  
  public ChannelFuture write(Object msg) {
    return this.tail.write(msg);
  }
  
  public ChannelFuture write(Object msg, ChannelPromise promise) {
    return this.tail.write(msg, promise);
  }
  
  public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
    return this.tail.writeAndFlush(msg, promise);
  }
  
  public ChannelFuture writeAndFlush(Object msg) {
    return this.tail.writeAndFlush(msg);
  }
  
  private void checkDuplicateName(String name) {
    if (this.name2ctx.containsKey(name))
      throw new IllegalArgumentException("Duplicate handler name: " + name); 
  }
  
  private AbstractChannelHandlerContext getContextOrDie(String name) {
    AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(name);
    if (ctx == null)
      throw new NoSuchElementException(name); 
    return ctx;
  }
  
  private AbstractChannelHandlerContext getContextOrDie(ChannelHandler handler) {
    AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(handler);
    if (ctx == null)
      throw new NoSuchElementException(handler.getClass().getName()); 
    return ctx;
  }
  
  private AbstractChannelHandlerContext getContextOrDie(Class<? extends ChannelHandler> handlerType) {
    AbstractChannelHandlerContext ctx = (AbstractChannelHandlerContext)context(handlerType);
    if (ctx == null)
      throw new NoSuchElementException(handlerType.getName()); 
    return ctx;
  }
  
  static final class TailContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {
    private static final String TAIL_NAME = DefaultChannelPipeline.generateName0(TailContext.class);
    
    TailContext(DefaultChannelPipeline pipeline) {
      super(pipeline, (EventExecutorGroup)null, TAIL_NAME, true, false);
    }
    
    public ChannelHandler handler() {
      return this;
    }
    
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {}
    
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {}
    
    public void channelActive(ChannelHandlerContext ctx) throws Exception {}
    
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {}
    
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {}
    
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}
    
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}
    
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {}
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      DefaultChannelPipeline.logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", cause);
    }
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      try {
        DefaultChannelPipeline.logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", msg);
      } finally {
        ReferenceCountUtil.release(msg);
      } 
    }
    
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {}
  }
  
  static final class HeadContext extends AbstractChannelHandlerContext implements ChannelOutboundHandler {
    private static final String HEAD_NAME = DefaultChannelPipeline.generateName0(HeadContext.class);
    
    protected final Channel.Unsafe unsafe;
    
    HeadContext(DefaultChannelPipeline pipeline) {
      super(pipeline, (EventExecutorGroup)null, HEAD_NAME, false, true);
      this.unsafe = pipeline.channel().unsafe();
    }
    
    public ChannelHandler handler() {
      return this;
    }
    
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {}
    
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {}
    
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
      this.unsafe.bind(localAddress, promise);
    }
    
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
      this.unsafe.connect(remoteAddress, localAddress, promise);
    }
    
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
      this.unsafe.disconnect(promise);
    }
    
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
      this.unsafe.close(promise);
    }
    
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
      this.unsafe.deregister(promise);
    }
    
    public void read(ChannelHandlerContext ctx) {
      this.unsafe.beginRead();
    }
    
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
      this.unsafe.write(msg, promise);
    }
    
    public void flush(ChannelHandlerContext ctx) throws Exception {
      this.unsafe.flush();
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      ctx.fireExceptionCaught(cause);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\DefaultChannelPipeline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */