package io.netty.channel.embedded;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

public class EmbeddedChannel extends AbstractChannel {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
  
  private static final ChannelMetadata METADATA = new ChannelMetadata(false);
  
  private final EmbeddedEventLoop loop = new EmbeddedEventLoop();
  
  private final ChannelConfig config = (ChannelConfig)new DefaultChannelConfig((Channel)this);
  
  private final SocketAddress localAddress = new EmbeddedSocketAddress();
  
  private final SocketAddress remoteAddress = new EmbeddedSocketAddress();
  
  private final Queue<Object> inboundMessages = new ArrayDeque();
  
  private final Queue<Object> outboundMessages = new ArrayDeque();
  
  private Throwable lastException;
  
  private int state;
  
  public EmbeddedChannel(ChannelHandler... handlers) {
    super(null);
    if (handlers == null)
      throw new NullPointerException("handlers"); 
    int nHandlers = 0;
    ChannelPipeline p = pipeline();
    for (ChannelHandler h : handlers) {
      if (h == null)
        break; 
      nHandlers++;
      p.addLast(new ChannelHandler[] { h });
    } 
    if (nHandlers == 0)
      throw new IllegalArgumentException("handlers is empty."); 
    this.loop.register((Channel)this);
    p.addLast(new ChannelHandler[] { (ChannelHandler)new LastInboundHandler() });
  }
  
  public ChannelMetadata metadata() {
    return METADATA;
  }
  
  public ChannelConfig config() {
    return this.config;
  }
  
  public boolean isOpen() {
    return (this.state < 2);
  }
  
  public boolean isActive() {
    return (this.state == 1);
  }
  
  public Queue<Object> inboundMessages() {
    return this.inboundMessages;
  }
  
  @Deprecated
  public Queue<Object> lastInboundBuffer() {
    return inboundMessages();
  }
  
  public Queue<Object> outboundMessages() {
    return this.outboundMessages;
  }
  
  @Deprecated
  public Queue<Object> lastOutboundBuffer() {
    return outboundMessages();
  }
  
  public Object readInbound() {
    return this.inboundMessages.poll();
  }
  
  public Object readOutbound() {
    return this.outboundMessages.poll();
  }
  
  public boolean writeInbound(Object... msgs) {
    ensureOpen();
    if (msgs.length == 0)
      return !this.inboundMessages.isEmpty(); 
    ChannelPipeline p = pipeline();
    for (Object m : msgs)
      p.fireChannelRead(m); 
    p.fireChannelReadComplete();
    runPendingTasks();
    checkException();
    return !this.inboundMessages.isEmpty();
  }
  
  public boolean writeOutbound(Object... msgs) {
    ensureOpen();
    if (msgs.length == 0)
      return !this.outboundMessages.isEmpty(); 
    RecyclableArrayList futures = RecyclableArrayList.newInstance(msgs.length);
    try {
      for (Object m : msgs) {
        if (m == null)
          break; 
        futures.add(write(m));
      } 
      flush();
      int size = futures.size();
      int i;
      for (i = 0; i < size; i++) {
        ChannelFuture future = (ChannelFuture)futures.get(i);
        assert future.isDone();
        if (future.cause() != null)
          recordException(future.cause()); 
      } 
      runPendingTasks();
      checkException();
      i = !this.outboundMessages.isEmpty() ? 1 : 0;
      return i;
    } finally {
      futures.recycle();
    } 
  }
  
  public boolean finish() {
    close();
    runPendingTasks();
    checkException();
    return (!this.inboundMessages.isEmpty() || !this.outboundMessages.isEmpty());
  }
  
  public void runPendingTasks() {
    try {
      this.loop.runTasks();
    } catch (Exception e) {
      recordException(e);
    } 
  }
  
  private void recordException(Throwable cause) {
    if (this.lastException == null) {
      this.lastException = cause;
    } else {
      logger.warn("More than one exception was raised. Will report only the first one and log others.", cause);
    } 
  }
  
  public void checkException() {
    Throwable t = this.lastException;
    if (t == null)
      return; 
    this.lastException = null;
    PlatformDependent.throwException(t);
  }
  
  protected final void ensureOpen() {
    if (!isOpen()) {
      recordException(new ClosedChannelException());
      checkException();
    } 
  }
  
  protected boolean isCompatible(EventLoop loop) {
    return loop instanceof EmbeddedEventLoop;
  }
  
  protected SocketAddress localAddress0() {
    return isActive() ? this.localAddress : null;
  }
  
  protected SocketAddress remoteAddress0() {
    return isActive() ? this.remoteAddress : null;
  }
  
  protected void doRegister() throws Exception {
    this.state = 1;
  }
  
  protected void doBind(SocketAddress localAddress) throws Exception {}
  
  protected void doDisconnect() throws Exception {
    doClose();
  }
  
  protected void doClose() throws Exception {
    this.state = 2;
  }
  
  protected void doBeginRead() throws Exception {}
  
  protected AbstractChannel.AbstractUnsafe newUnsafe() {
    return new DefaultUnsafe();
  }
  
  protected void doWrite(ChannelOutboundBuffer in) throws Exception {
    while (true) {
      Object msg = in.current();
      if (msg == null)
        break; 
      ReferenceCountUtil.retain(msg);
      this.outboundMessages.add(msg);
      in.remove();
    } 
  }
  
  private class DefaultUnsafe extends AbstractChannel.AbstractUnsafe {
    private DefaultUnsafe() {
      super(EmbeddedChannel.this);
    }
    
    public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
      safeSetSuccess(promise);
    }
  }
  
  private final class LastInboundHandler extends ChannelInboundHandlerAdapter {
    private LastInboundHandler() {}
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      EmbeddedChannel.this.inboundMessages.add(msg);
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      EmbeddedChannel.this.recordException(cause);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\embedded\EmbeddedChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */