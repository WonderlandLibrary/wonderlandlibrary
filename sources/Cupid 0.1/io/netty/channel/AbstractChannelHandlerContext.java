package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.RecyclableMpscLinkedQueueNode;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

abstract class AbstractChannelHandlerContext extends DefaultAttributeMap implements ChannelHandlerContext {
  volatile AbstractChannelHandlerContext next;
  
  volatile AbstractChannelHandlerContext prev;
  
  private final boolean inbound;
  
  private final boolean outbound;
  
  private final AbstractChannel channel;
  
  private final DefaultChannelPipeline pipeline;
  
  private final String name;
  
  private boolean removed;
  
  final EventExecutor executor;
  
  private ChannelFuture succeededFuture;
  
  private volatile Runnable invokeChannelReadCompleteTask;
  
  private volatile Runnable invokeReadTask;
  
  private volatile Runnable invokeChannelWritableStateChangedTask;
  
  private volatile Runnable invokeFlushTask;
  
  AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutorGroup group, String name, boolean inbound, boolean outbound) {
    if (name == null)
      throw new NullPointerException("name"); 
    this.channel = pipeline.channel;
    this.pipeline = pipeline;
    this.name = name;
    if (group != null) {
      EventExecutor childExecutor = pipeline.childExecutors.get(group);
      if (childExecutor == null) {
        childExecutor = group.next();
        pipeline.childExecutors.put(group, childExecutor);
      } 
      this.executor = childExecutor;
    } else {
      this.executor = null;
    } 
    this.inbound = inbound;
    this.outbound = outbound;
  }
  
  void teardown() {
    EventExecutor executor = executor();
    if (executor.inEventLoop()) {
      teardown0();
    } else {
      executor.execute(new Runnable() {
            public void run() {
              AbstractChannelHandlerContext.this.teardown0();
            }
          });
    } 
  }
  
  private void teardown0() {
    AbstractChannelHandlerContext prev = this.prev;
    if (prev != null) {
      synchronized (this.pipeline) {
        this.pipeline.remove0(this);
      } 
      prev.teardown();
    } 
  }
  
  public Channel channel() {
    return this.channel;
  }
  
  public ChannelPipeline pipeline() {
    return this.pipeline;
  }
  
  public ByteBufAllocator alloc() {
    return channel().config().getAllocator();
  }
  
  public EventExecutor executor() {
    if (this.executor == null)
      return channel().eventLoop(); 
    return this.executor;
  }
  
  public String name() {
    return this.name;
  }
  
  public ChannelHandlerContext fireChannelRegistered() {
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelRegistered();
    } else {
      executor.execute((Runnable)new OneTimeTask() {
            public void run() {
              next.invokeChannelRegistered();
            }
          });
    } 
    return this;
  }
  
  private void invokeChannelRegistered() {
    try {
      ((ChannelInboundHandler)handler()).channelRegistered(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireChannelUnregistered() {
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelUnregistered();
    } else {
      executor.execute((Runnable)new OneTimeTask() {
            public void run() {
              next.invokeChannelUnregistered();
            }
          });
    } 
    return this;
  }
  
  private void invokeChannelUnregistered() {
    try {
      ((ChannelInboundHandler)handler()).channelUnregistered(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireChannelActive() {
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelActive();
    } else {
      executor.execute((Runnable)new OneTimeTask() {
            public void run() {
              next.invokeChannelActive();
            }
          });
    } 
    return this;
  }
  
  private void invokeChannelActive() {
    try {
      ((ChannelInboundHandler)handler()).channelActive(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireChannelInactive() {
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelInactive();
    } else {
      executor.execute((Runnable)new OneTimeTask() {
            public void run() {
              next.invokeChannelInactive();
            }
          });
    } 
    return this;
  }
  
  private void invokeChannelInactive() {
    try {
      ((ChannelInboundHandler)handler()).channelInactive(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireExceptionCaught(final Throwable cause) {
    if (cause == null)
      throw new NullPointerException("cause"); 
    final AbstractChannelHandlerContext next = this.next;
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeExceptionCaught(cause);
    } else {
      try {
        executor.execute((Runnable)new OneTimeTask() {
              public void run() {
                next.invokeExceptionCaught(cause);
              }
            });
      } catch (Throwable t) {
        if (DefaultChannelPipeline.logger.isWarnEnabled()) {
          DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", t);
          DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
        } 
      } 
    } 
    return this;
  }
  
  private void invokeExceptionCaught(Throwable cause) {
    try {
      handler().exceptionCaught(this, cause);
    } catch (Throwable t) {
      if (DefaultChannelPipeline.logger.isWarnEnabled())
        DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler's exceptionCaught() method while handling the following exception:", cause); 
    } 
  }
  
  public ChannelHandlerContext fireUserEventTriggered(final Object event) {
    if (event == null)
      throw new NullPointerException("event"); 
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeUserEventTriggered(event);
    } else {
      executor.execute((Runnable)new OneTimeTask() {
            public void run() {
              next.invokeUserEventTriggered(event);
            }
          });
    } 
    return this;
  }
  
  private void invokeUserEventTriggered(Object event) {
    try {
      ((ChannelInboundHandler)handler()).userEventTriggered(this, event);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireChannelRead(final Object msg) {
    if (msg == null)
      throw new NullPointerException("msg"); 
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelRead(msg);
    } else {
      executor.execute((Runnable)new OneTimeTask() {
            public void run() {
              next.invokeChannelRead(msg);
            }
          });
    } 
    return this;
  }
  
  private void invokeChannelRead(Object msg) {
    try {
      ((ChannelInboundHandler)handler()).channelRead(this, msg);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireChannelReadComplete() {
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelReadComplete();
    } else {
      Runnable task = next.invokeChannelReadCompleteTask;
      if (task == null)
        next.invokeChannelReadCompleteTask = task = new Runnable() {
            public void run() {
              next.invokeChannelReadComplete();
            }
          }; 
      executor.execute(task);
    } 
    return this;
  }
  
  private void invokeChannelReadComplete() {
    try {
      ((ChannelInboundHandler)handler()).channelReadComplete(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelHandlerContext fireChannelWritabilityChanged() {
    final AbstractChannelHandlerContext next = findContextInbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeChannelWritabilityChanged();
    } else {
      Runnable task = next.invokeChannelWritableStateChangedTask;
      if (task == null)
        next.invokeChannelWritableStateChangedTask = task = new Runnable() {
            public void run() {
              next.invokeChannelWritabilityChanged();
            }
          }; 
      executor.execute(task);
    } 
    return this;
  }
  
  private void invokeChannelWritabilityChanged() {
    try {
      ((ChannelInboundHandler)handler()).channelWritabilityChanged(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelFuture bind(SocketAddress localAddress) {
    return bind(localAddress, newPromise());
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress) {
    return connect(remoteAddress, newPromise());
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
    return connect(remoteAddress, localAddress, newPromise());
  }
  
  public ChannelFuture disconnect() {
    return disconnect(newPromise());
  }
  
  public ChannelFuture close() {
    return close(newPromise());
  }
  
  public ChannelFuture deregister() {
    return deregister(newPromise());
  }
  
  public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
    if (localAddress == null)
      throw new NullPointerException("localAddress"); 
    if (!validatePromise(promise, false))
      return promise; 
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeBind(localAddress, promise);
    } else {
      safeExecute(executor, (Runnable)new OneTimeTask() {
            public void run() {
              next.invokeBind(localAddress, promise);
            }
          }promise, null);
    } 
    return promise;
  }
  
  private void invokeBind(SocketAddress localAddress, ChannelPromise promise) {
    try {
      ((ChannelOutboundHandler)handler()).bind(this, localAddress, promise);
    } catch (Throwable t) {
      notifyOutboundHandlerException(t, promise);
    } 
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
    return connect(remoteAddress, null, promise);
  }
  
  public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
    if (remoteAddress == null)
      throw new NullPointerException("remoteAddress"); 
    if (!validatePromise(promise, false))
      return promise; 
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeConnect(remoteAddress, localAddress, promise);
    } else {
      safeExecute(executor, (Runnable)new OneTimeTask() {
            public void run() {
              next.invokeConnect(remoteAddress, localAddress, promise);
            }
          }promise, null);
    } 
    return promise;
  }
  
  private void invokeConnect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
    try {
      ((ChannelOutboundHandler)handler()).connect(this, remoteAddress, localAddress, promise);
    } catch (Throwable t) {
      notifyOutboundHandlerException(t, promise);
    } 
  }
  
  public ChannelFuture disconnect(final ChannelPromise promise) {
    if (!validatePromise(promise, false))
      return promise; 
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      if (!channel().metadata().hasDisconnect()) {
        next.invokeClose(promise);
      } else {
        next.invokeDisconnect(promise);
      } 
    } else {
      safeExecute(executor, (Runnable)new OneTimeTask() {
            public void run() {
              if (!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                next.invokeClose(promise);
              } else {
                next.invokeDisconnect(promise);
              } 
            }
          },  promise, null);
    } 
    return promise;
  }
  
  private void invokeDisconnect(ChannelPromise promise) {
    try {
      ((ChannelOutboundHandler)handler()).disconnect(this, promise);
    } catch (Throwable t) {
      notifyOutboundHandlerException(t, promise);
    } 
  }
  
  public ChannelFuture close(final ChannelPromise promise) {
    if (!validatePromise(promise, false))
      return promise; 
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeClose(promise);
    } else {
      safeExecute(executor, (Runnable)new OneTimeTask() {
            public void run() {
              next.invokeClose(promise);
            }
          },  promise, null);
    } 
    return promise;
  }
  
  private void invokeClose(ChannelPromise promise) {
    try {
      ((ChannelOutboundHandler)handler()).close(this, promise);
    } catch (Throwable t) {
      notifyOutboundHandlerException(t, promise);
    } 
  }
  
  public ChannelFuture deregister(final ChannelPromise promise) {
    if (!validatePromise(promise, false))
      return promise; 
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeDeregister(promise);
    } else {
      safeExecute(executor, (Runnable)new OneTimeTask() {
            public void run() {
              next.invokeDeregister(promise);
            }
          },  promise, null);
    } 
    return promise;
  }
  
  private void invokeDeregister(ChannelPromise promise) {
    try {
      ((ChannelOutboundHandler)handler()).deregister(this, promise);
    } catch (Throwable t) {
      notifyOutboundHandlerException(t, promise);
    } 
  }
  
  public ChannelHandlerContext read() {
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeRead();
    } else {
      Runnable task = next.invokeReadTask;
      if (task == null)
        next.invokeReadTask = task = new Runnable() {
            public void run() {
              next.invokeRead();
            }
          }; 
      executor.execute(task);
    } 
    return this;
  }
  
  private void invokeRead() {
    try {
      ((ChannelOutboundHandler)handler()).read(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelFuture write(Object msg) {
    return write(msg, newPromise());
  }
  
  public ChannelFuture write(Object msg, ChannelPromise promise) {
    if (msg == null)
      throw new NullPointerException("msg"); 
    if (!validatePromise(promise, true)) {
      ReferenceCountUtil.release(msg);
      return promise;
    } 
    write(msg, false, promise);
    return promise;
  }
  
  private void invokeWrite(Object msg, ChannelPromise promise) {
    try {
      ((ChannelOutboundHandler)handler()).write(this, msg, promise);
    } catch (Throwable t) {
      notifyOutboundHandlerException(t, promise);
    } 
  }
  
  public ChannelHandlerContext flush() {
    final AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeFlush();
    } else {
      Runnable task = next.invokeFlushTask;
      if (task == null)
        next.invokeFlushTask = task = new Runnable() {
            public void run() {
              next.invokeFlush();
            }
          }; 
      safeExecute(executor, task, this.channel.voidPromise(), null);
    } 
    return this;
  }
  
  private void invokeFlush() {
    try {
      ((ChannelOutboundHandler)handler()).flush(this);
    } catch (Throwable t) {
      notifyHandlerException(t);
    } 
  }
  
  public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
    if (msg == null)
      throw new NullPointerException("msg"); 
    if (!validatePromise(promise, true)) {
      ReferenceCountUtil.release(msg);
      return promise;
    } 
    write(msg, true, promise);
    return promise;
  }
  
  private void write(Object msg, boolean flush, ChannelPromise promise) {
    AbstractChannelHandlerContext next = findContextOutbound();
    EventExecutor executor = next.executor();
    if (executor.inEventLoop()) {
      next.invokeWrite(msg, promise);
      if (flush)
        next.invokeFlush(); 
    } else {
      Runnable task;
      int size = this.channel.estimatorHandle().size(msg);
      if (size > 0) {
        ChannelOutboundBuffer buffer = this.channel.unsafe().outboundBuffer();
        if (buffer != null)
          buffer.incrementPendingOutboundBytes(size); 
      } 
      if (flush) {
        task = WriteAndFlushTask.newInstance(next, msg, size, promise);
      } else {
        task = WriteTask.newInstance(next, msg, size, promise);
      } 
      safeExecute(executor, task, promise, msg);
    } 
  }
  
  public ChannelFuture writeAndFlush(Object msg) {
    return writeAndFlush(msg, newPromise());
  }
  
  private static void notifyOutboundHandlerException(Throwable cause, ChannelPromise promise) {
    if (promise instanceof VoidChannelPromise)
      return; 
    if (!promise.tryFailure(cause) && 
      DefaultChannelPipeline.logger.isWarnEnabled())
      DefaultChannelPipeline.logger.warn("Failed to fail the promise because it's done already: {}", promise, cause); 
  }
  
  private void notifyHandlerException(Throwable cause) {
    if (inExceptionCaught(cause)) {
      if (DefaultChannelPipeline.logger.isWarnEnabled())
        DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause); 
      return;
    } 
    invokeExceptionCaught(cause);
  }
  
  private static boolean inExceptionCaught(Throwable cause) {
    do {
      StackTraceElement[] trace = cause.getStackTrace();
      if (trace != null)
        for (StackTraceElement t : trace) {
          if (t == null)
            break; 
          if ("exceptionCaught".equals(t.getMethodName()))
            return true; 
        }  
      cause = cause.getCause();
    } while (cause != null);
    return false;
  }
  
  public ChannelPromise newPromise() {
    return new DefaultChannelPromise(channel(), executor());
  }
  
  public ChannelProgressivePromise newProgressivePromise() {
    return new DefaultChannelProgressivePromise(channel(), executor());
  }
  
  public ChannelFuture newSucceededFuture() {
    ChannelFuture succeededFuture = this.succeededFuture;
    if (succeededFuture == null)
      this.succeededFuture = succeededFuture = new SucceededChannelFuture(channel(), executor()); 
    return succeededFuture;
  }
  
  public ChannelFuture newFailedFuture(Throwable cause) {
    return new FailedChannelFuture(channel(), executor(), cause);
  }
  
  private boolean validatePromise(ChannelPromise promise, boolean allowVoidPromise) {
    if (promise == null)
      throw new NullPointerException("promise"); 
    if (promise.isDone()) {
      if (promise.isCancelled())
        return false; 
      throw new IllegalArgumentException("promise already done: " + promise);
    } 
    if (promise.channel() != channel())
      throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", new Object[] { promise.channel(), channel() })); 
    if (promise.getClass() == DefaultChannelPromise.class)
      return true; 
    if (!allowVoidPromise && promise instanceof VoidChannelPromise)
      throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation"); 
    if (promise instanceof AbstractChannel.CloseFuture)
      throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline"); 
    return true;
  }
  
  private AbstractChannelHandlerContext findContextInbound() {
    AbstractChannelHandlerContext ctx = this;
    while (true) {
      ctx = ctx.next;
      if (ctx.inbound)
        return ctx; 
    } 
  }
  
  private AbstractChannelHandlerContext findContextOutbound() {
    AbstractChannelHandlerContext ctx = this;
    while (true) {
      ctx = ctx.prev;
      if (ctx.outbound)
        return ctx; 
    } 
  }
  
  public ChannelPromise voidPromise() {
    return this.channel.voidPromise();
  }
  
  void setRemoved() {
    this.removed = true;
  }
  
  public boolean isRemoved() {
    return this.removed;
  }
  
  private static void safeExecute(EventExecutor executor, Runnable runnable, ChannelPromise promise, Object msg) {
    try {
      executor.execute(runnable);
    } catch (Throwable cause) {
      try {
        promise.setFailure(cause);
      } finally {
        if (msg != null)
          ReferenceCountUtil.release(msg); 
      } 
    } 
  }
  
  static abstract class AbstractWriteTask extends RecyclableMpscLinkedQueueNode<Runnable> implements Runnable {
    private AbstractChannelHandlerContext ctx;
    
    private Object msg;
    
    private ChannelPromise promise;
    
    private int size;
    
    private AbstractWriteTask(Recycler.Handle handle) {
      super(handle);
    }
    
    protected static void init(AbstractWriteTask task, AbstractChannelHandlerContext ctx, Object msg, int size, ChannelPromise promise) {
      task.ctx = ctx;
      task.msg = msg;
      task.promise = promise;
      task.size = size;
    }
    
    public final void run() {
      try {
        if (this.size > 0) {
          ChannelOutboundBuffer buffer = this.ctx.channel.unsafe().outboundBuffer();
          if (buffer != null)
            buffer.decrementPendingOutboundBytes(this.size); 
        } 
        write(this.ctx, this.msg, this.promise);
      } finally {
        this.ctx = null;
        this.msg = null;
        this.promise = null;
      } 
    }
    
    public Runnable value() {
      return this;
    }
    
    protected void write(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
      ctx.invokeWrite(msg, promise);
    }
  }
  
  static final class WriteTask extends AbstractWriteTask implements SingleThreadEventLoop.NonWakeupRunnable {
    private static final Recycler<WriteTask> RECYCLER = new Recycler<WriteTask>() {
        protected AbstractChannelHandlerContext.WriteTask newObject(Recycler.Handle handle) {
          return new AbstractChannelHandlerContext.WriteTask(handle);
        }
      };
    
    private static WriteTask newInstance(AbstractChannelHandlerContext ctx, Object msg, int size, ChannelPromise promise) {
      WriteTask task = (WriteTask)RECYCLER.get();
      init(task, ctx, msg, size, promise);
      return task;
    }
    
    private WriteTask(Recycler.Handle handle) {
      super(handle);
    }
    
    protected void recycle(Recycler.Handle handle) {
      RECYCLER.recycle(this, handle);
    }
  }
  
  static final class WriteAndFlushTask extends AbstractWriteTask {
    private static final Recycler<WriteAndFlushTask> RECYCLER = new Recycler<WriteAndFlushTask>() {
        protected AbstractChannelHandlerContext.WriteAndFlushTask newObject(Recycler.Handle handle) {
          return new AbstractChannelHandlerContext.WriteAndFlushTask(handle);
        }
      };
    
    private static WriteAndFlushTask newInstance(AbstractChannelHandlerContext ctx, Object msg, int size, ChannelPromise promise) {
      WriteAndFlushTask task = (WriteAndFlushTask)RECYCLER.get();
      init(task, ctx, msg, size, promise);
      return task;
    }
    
    private WriteAndFlushTask(Recycler.Handle handle) {
      super(handle);
    }
    
    public void write(AbstractChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
      super.write(ctx, msg, promise);
      ctx.invokeFlush();
    }
    
    protected void recycle(Recycler.Handle handle) {
      RECYCLER.recycle(this, handle);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\AbstractChannelHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */