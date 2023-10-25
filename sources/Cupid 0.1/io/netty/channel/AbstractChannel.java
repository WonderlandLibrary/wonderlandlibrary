package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.RejectedExecutionException;

public abstract class AbstractChannel extends DefaultAttributeMap implements Channel {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
  
  static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
  
  static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
  
  private MessageSizeEstimator.Handle estimatorHandle;
  
  private final Channel parent;
  
  static {
    CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
  }
  
  private final long hashCode = ThreadLocalRandom.current().nextLong();
  
  private final Channel.Unsafe unsafe;
  
  private final DefaultChannelPipeline pipeline;
  
  private final ChannelFuture succeededFuture = new SucceededChannelFuture(this, null);
  
  private final VoidChannelPromise voidPromise = new VoidChannelPromise(this, true);
  
  private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(this, false);
  
  private final CloseFuture closeFuture = new CloseFuture(this);
  
  private volatile SocketAddress localAddress;
  
  private volatile SocketAddress remoteAddress;
  
  private volatile EventLoop eventLoop;
  
  private volatile boolean registered;
  
  private boolean strValActive;
  
  private String strVal;
  
  protected AbstractChannel(Channel parent) {
    this.parent = parent;
    this.unsafe = newUnsafe();
    this.pipeline = new DefaultChannelPipeline(this);
  }
  
  public boolean isWritable() {
    ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
    return (buf != null && buf.isWritable());
  }
  
  public Channel parent() {
    return this.parent;
  }
  
  public ChannelPipeline pipeline() {
    return this.pipeline;
  }
  
  public ByteBufAllocator alloc() {
    return config().getAllocator();
  }
  
  public EventLoop eventLoop() {
    EventLoop eventLoop = this.eventLoop;
    if (eventLoop == null)
      throw new IllegalStateException("channel not registered to an event loop"); 
    return eventLoop;
  }
  
  public SocketAddress localAddress() {
    SocketAddress localAddress = this.localAddress;
    if (localAddress == null)
      try {
        this.localAddress = localAddress = unsafe().localAddress();
      } catch (Throwable t) {
        return null;
      }  
    return localAddress;
  }
  
  protected void invalidateLocalAddress() {
    this.localAddress = null;
  }
  
  public SocketAddress remoteAddress() {
    SocketAddress remoteAddress = this.remoteAddress;
    if (remoteAddress == null)
      try {
        this.remoteAddress = remoteAddress = unsafe().remoteAddress();
      } catch (Throwable t) {
        return null;
      }  
    return remoteAddress;
  }
  
  protected void invalidateRemoteAddress() {
    this.remoteAddress = null;
  }
  
  public boolean isRegistered() {
    return this.registered;
  }
  
  public ChannelFuture bind(SocketAddress localAddress) {
    return this.pipeline.bind(localAddress);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress) {
    return this.pipeline.connect(remoteAddress);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
    return this.pipeline.connect(remoteAddress, localAddress);
  }
  
  public ChannelFuture disconnect() {
    return this.pipeline.disconnect();
  }
  
  public ChannelFuture close() {
    return this.pipeline.close();
  }
  
  public ChannelFuture deregister() {
    return this.pipeline.deregister();
  }
  
  public Channel flush() {
    this.pipeline.flush();
    return this;
  }
  
  public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
    return this.pipeline.bind(localAddress, promise);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
    return this.pipeline.connect(remoteAddress, promise);
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
    return this.pipeline.connect(remoteAddress, localAddress, promise);
  }
  
  public ChannelFuture disconnect(ChannelPromise promise) {
    return this.pipeline.disconnect(promise);
  }
  
  public ChannelFuture close(ChannelPromise promise) {
    return this.pipeline.close(promise);
  }
  
  public ChannelFuture deregister(ChannelPromise promise) {
    return this.pipeline.deregister(promise);
  }
  
  public Channel read() {
    this.pipeline.read();
    return this;
  }
  
  public ChannelFuture write(Object msg) {
    return this.pipeline.write(msg);
  }
  
  public ChannelFuture write(Object msg, ChannelPromise promise) {
    return this.pipeline.write(msg, promise);
  }
  
  public ChannelFuture writeAndFlush(Object msg) {
    return this.pipeline.writeAndFlush(msg);
  }
  
  public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
    return this.pipeline.writeAndFlush(msg, promise);
  }
  
  public ChannelPromise newPromise() {
    return new DefaultChannelPromise(this);
  }
  
  public ChannelProgressivePromise newProgressivePromise() {
    return new DefaultChannelProgressivePromise(this);
  }
  
  public ChannelFuture newSucceededFuture() {
    return this.succeededFuture;
  }
  
  public ChannelFuture newFailedFuture(Throwable cause) {
    return new FailedChannelFuture(this, null, cause);
  }
  
  public ChannelFuture closeFuture() {
    return this.closeFuture;
  }
  
  public Channel.Unsafe unsafe() {
    return this.unsafe;
  }
  
  public final int hashCode() {
    return (int)this.hashCode;
  }
  
  public final boolean equals(Object o) {
    return (this == o);
  }
  
  public final int compareTo(Channel o) {
    if (this == o)
      return 0; 
    long ret = this.hashCode - o.hashCode();
    if (ret > 0L)
      return 1; 
    if (ret < 0L)
      return -1; 
    ret = (System.identityHashCode(this) - System.identityHashCode(o));
    if (ret != 0L)
      return (int)ret; 
    throw new Error();
  }
  
  public String toString() {
    boolean active = isActive();
    if (this.strValActive == active && this.strVal != null)
      return this.strVal; 
    SocketAddress remoteAddr = remoteAddress();
    SocketAddress localAddr = localAddress();
    if (remoteAddr != null) {
      SocketAddress srcAddr, dstAddr;
      if (this.parent == null) {
        srcAddr = localAddr;
        dstAddr = remoteAddr;
      } else {
        srcAddr = remoteAddr;
        dstAddr = localAddr;
      } 
      this.strVal = String.format("[id: 0x%08x, %s %s %s]", new Object[] { Integer.valueOf((int)this.hashCode), srcAddr, active ? "=>" : ":>", dstAddr });
    } else if (localAddr != null) {
      this.strVal = String.format("[id: 0x%08x, %s]", new Object[] { Integer.valueOf((int)this.hashCode), localAddr });
    } else {
      this.strVal = String.format("[id: 0x%08x]", new Object[] { Integer.valueOf((int)this.hashCode) });
    } 
    this.strValActive = active;
    return this.strVal;
  }
  
  public final ChannelPromise voidPromise() {
    return this.voidPromise;
  }
  
  final MessageSizeEstimator.Handle estimatorHandle() {
    if (this.estimatorHandle == null)
      this.estimatorHandle = config().getMessageSizeEstimator().newHandle(); 
    return this.estimatorHandle;
  }
  
  protected abstract class AbstractUnsafe implements Channel.Unsafe {
    private ChannelOutboundBuffer outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
    
    private boolean inFlush0;
    
    public final ChannelOutboundBuffer outboundBuffer() {
      return this.outboundBuffer;
    }
    
    public final SocketAddress localAddress() {
      return AbstractChannel.this.localAddress0();
    }
    
    public final SocketAddress remoteAddress() {
      return AbstractChannel.this.remoteAddress0();
    }
    
    public final void register(EventLoop eventLoop, final ChannelPromise promise) {
      if (eventLoop == null)
        throw new NullPointerException("eventLoop"); 
      if (AbstractChannel.this.isRegistered()) {
        promise.setFailure(new IllegalStateException("registered to an event loop already"));
        return;
      } 
      if (!AbstractChannel.this.isCompatible(eventLoop)) {
        promise.setFailure(new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
        return;
      } 
      AbstractChannel.this.eventLoop = eventLoop;
      if (eventLoop.inEventLoop()) {
        register0(promise);
      } else {
        try {
          eventLoop.execute((Runnable)new OneTimeTask() {
                public void run() {
                  AbstractChannel.AbstractUnsafe.this.register0(promise);
                }
              });
        } catch (Throwable t) {
          AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, t);
          closeForcibly();
          AbstractChannel.this.closeFuture.setClosed();
          safeSetFailure(promise, t);
        } 
      } 
    }
    
    private void register0(ChannelPromise promise) {
      try {
        if (!promise.setUncancellable() || !ensureOpen(promise))
          return; 
        AbstractChannel.this.doRegister();
        AbstractChannel.this.registered = true;
        safeSetSuccess(promise);
        AbstractChannel.this.pipeline.fireChannelRegistered();
        if (AbstractChannel.this.isActive())
          AbstractChannel.this.pipeline.fireChannelActive(); 
      } catch (Throwable t) {
        closeForcibly();
        AbstractChannel.this.closeFuture.setClosed();
        safeSetFailure(promise, t);
      } 
    }
    
    public final void bind(SocketAddress localAddress, ChannelPromise promise) {
      if (!promise.setUncancellable() || !ensureOpen(promise))
        return; 
      if (!PlatformDependent.isWindows() && !PlatformDependent.isRoot() && Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && localAddress instanceof InetSocketAddress && !((InetSocketAddress)localAddress).getAddress().isAnyLocalAddress())
        AbstractChannel.logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested."); 
      boolean wasActive = AbstractChannel.this.isActive();
      try {
        AbstractChannel.this.doBind(localAddress);
      } catch (Throwable t) {
        safeSetFailure(promise, t);
        closeIfClosed();
        return;
      } 
      if (!wasActive && AbstractChannel.this.isActive())
        invokeLater((Runnable)new OneTimeTask() {
              public void run() {
                AbstractChannel.this.pipeline.fireChannelActive();
              }
            }); 
      safeSetSuccess(promise);
    }
    
    public final void disconnect(ChannelPromise promise) {
      if (!promise.setUncancellable())
        return; 
      boolean wasActive = AbstractChannel.this.isActive();
      try {
        AbstractChannel.this.doDisconnect();
      } catch (Throwable t) {
        safeSetFailure(promise, t);
        closeIfClosed();
        return;
      } 
      if (wasActive && !AbstractChannel.this.isActive())
        invokeLater((Runnable)new OneTimeTask() {
              public void run() {
                AbstractChannel.this.pipeline.fireChannelInactive();
              }
            }); 
      safeSetSuccess(promise);
      closeIfClosed();
    }
    
    public final void close(final ChannelPromise promise) {
      if (!promise.setUncancellable())
        return; 
      if (this.inFlush0) {
        invokeLater((Runnable)new OneTimeTask() {
              public void run() {
                AbstractChannel.AbstractUnsafe.this.close(promise);
              }
            });
        return;
      } 
      if (AbstractChannel.this.closeFuture.isDone()) {
        safeSetSuccess(promise);
        return;
      } 
      boolean wasActive = AbstractChannel.this.isActive();
      ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
      this.outboundBuffer = null;
      try {
        AbstractChannel.this.doClose();
        AbstractChannel.this.closeFuture.setClosed();
        safeSetSuccess(promise);
      } catch (Throwable t) {
        AbstractChannel.this.closeFuture.setClosed();
        safeSetFailure(promise, t);
      } 
      try {
        outboundBuffer.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
        outboundBuffer.close(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
      } finally {
        if (wasActive && !AbstractChannel.this.isActive())
          invokeLater((Runnable)new OneTimeTask() {
                public void run() {
                  AbstractChannel.this.pipeline.fireChannelInactive();
                }
              }); 
        deregister(voidPromise());
      } 
    }
    
    public final void closeForcibly() {
      try {
        AbstractChannel.this.doClose();
      } catch (Exception e) {
        AbstractChannel.logger.warn("Failed to close a channel.", e);
      } 
    }
    
    public final void deregister(ChannelPromise promise) {
      if (!promise.setUncancellable())
        return; 
      if (!AbstractChannel.this.registered) {
        safeSetSuccess(promise);
        return;
      } 
      try {
        AbstractChannel.this.doDeregister();
      } catch (Throwable t) {
        AbstractChannel.logger.warn("Unexpected exception occurred while deregistering a channel.", t);
      } finally {
        if (AbstractChannel.this.registered) {
          AbstractChannel.this.registered = false;
          invokeLater((Runnable)new OneTimeTask() {
                public void run() {
                  AbstractChannel.this.pipeline.fireChannelUnregistered();
                }
              });
          safeSetSuccess(promise);
        } else {
          safeSetSuccess(promise);
        } 
      } 
    }
    
    public final void beginRead() {
      if (!AbstractChannel.this.isActive())
        return; 
      try {
        AbstractChannel.this.doBeginRead();
      } catch (Exception e) {
        invokeLater((Runnable)new OneTimeTask() {
              public void run() {
                AbstractChannel.this.pipeline.fireExceptionCaught(e);
              }
            });
        close(voidPromise());
      } 
    }
    
    public final void write(Object msg, ChannelPromise promise) {
      int size;
      ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
      if (outboundBuffer == null) {
        safeSetFailure(promise, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
        ReferenceCountUtil.release(msg);
        return;
      } 
      try {
        msg = AbstractChannel.this.filterOutboundMessage(msg);
        size = AbstractChannel.this.estimatorHandle().size(msg);
        if (size < 0)
          size = 0; 
      } catch (Throwable t) {
        safeSetFailure(promise, t);
        ReferenceCountUtil.release(msg);
        return;
      } 
      outboundBuffer.addMessage(msg, size, promise);
    }
    
    public final void flush() {
      ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
      if (outboundBuffer == null)
        return; 
      outboundBuffer.addFlush();
      flush0();
    }
    
    protected void flush0() {
      if (this.inFlush0)
        return; 
      ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
      if (outboundBuffer == null || outboundBuffer.isEmpty())
        return; 
      this.inFlush0 = true;
      if (!AbstractChannel.this.isActive()) {
        try {
          if (AbstractChannel.this.isOpen()) {
            outboundBuffer.failFlushed(AbstractChannel.NOT_YET_CONNECTED_EXCEPTION);
          } else {
            outboundBuffer.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
          } 
        } finally {
          this.inFlush0 = false;
        } 
        return;
      } 
      try {
        AbstractChannel.this.doWrite(outboundBuffer);
      } catch (Throwable t) {
        outboundBuffer.failFlushed(t);
        if (t instanceof java.io.IOException && AbstractChannel.this.config().isAutoClose())
          close(voidPromise()); 
      } finally {
        this.inFlush0 = false;
      } 
    }
    
    public final ChannelPromise voidPromise() {
      return AbstractChannel.this.unsafeVoidPromise;
    }
    
    protected final boolean ensureOpen(ChannelPromise promise) {
      if (AbstractChannel.this.isOpen())
        return true; 
      safeSetFailure(promise, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
      return false;
    }
    
    protected final void safeSetSuccess(ChannelPromise promise) {
      if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess())
        AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise); 
    }
    
    protected final void safeSetFailure(ChannelPromise promise, Throwable cause) {
      if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause))
        AbstractChannel.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause); 
    }
    
    protected final void closeIfClosed() {
      if (AbstractChannel.this.isOpen())
        return; 
      close(voidPromise());
    }
    
    private void invokeLater(Runnable task) {
      try {
        AbstractChannel.this.eventLoop().execute(task);
      } catch (RejectedExecutionException e) {
        AbstractChannel.logger.warn("Can't invoke task later as EventLoop rejected it", e);
      } 
    }
  }
  
  protected void doRegister() throws Exception {}
  
  protected void doDeregister() throws Exception {}
  
  protected Object filterOutboundMessage(Object msg) throws Exception {
    return msg;
  }
  
  protected abstract AbstractUnsafe newUnsafe();
  
  protected abstract boolean isCompatible(EventLoop paramEventLoop);
  
  protected abstract SocketAddress localAddress0();
  
  protected abstract SocketAddress remoteAddress0();
  
  protected abstract void doBind(SocketAddress paramSocketAddress) throws Exception;
  
  protected abstract void doDisconnect() throws Exception;
  
  protected abstract void doClose() throws Exception;
  
  protected abstract void doBeginRead() throws Exception;
  
  protected abstract void doWrite(ChannelOutboundBuffer paramChannelOutboundBuffer) throws Exception;
  
  static final class CloseFuture extends DefaultChannelPromise {
    CloseFuture(AbstractChannel ch) {
      super(ch);
    }
    
    public ChannelPromise setSuccess() {
      throw new IllegalStateException();
    }
    
    public ChannelPromise setFailure(Throwable cause) {
      throw new IllegalStateException();
    }
    
    public boolean trySuccess() {
      throw new IllegalStateException();
    }
    
    public boolean tryFailure(Throwable cause) {
      throw new IllegalStateException();
    }
    
    boolean setClosed() {
      return super.trySuccess();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\AbstractChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */