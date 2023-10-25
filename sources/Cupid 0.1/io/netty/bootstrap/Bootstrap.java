package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

public final class Bootstrap extends AbstractBootstrap<Bootstrap, Channel> {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
  
  private volatile SocketAddress remoteAddress;
  
  public Bootstrap() {}
  
  private Bootstrap(Bootstrap bootstrap) {
    super(bootstrap);
    this.remoteAddress = bootstrap.remoteAddress;
  }
  
  public Bootstrap remoteAddress(SocketAddress remoteAddress) {
    this.remoteAddress = remoteAddress;
    return this;
  }
  
  public Bootstrap remoteAddress(String inetHost, int inetPort) {
    this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
    return this;
  }
  
  public Bootstrap remoteAddress(InetAddress inetHost, int inetPort) {
    this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
    return this;
  }
  
  public ChannelFuture connect() {
    validate();
    SocketAddress remoteAddress = this.remoteAddress;
    if (remoteAddress == null)
      throw new IllegalStateException("remoteAddress not set"); 
    return doConnect(remoteAddress, localAddress());
  }
  
  public ChannelFuture connect(String inetHost, int inetPort) {
    return connect(new InetSocketAddress(inetHost, inetPort));
  }
  
  public ChannelFuture connect(InetAddress inetHost, int inetPort) {
    return connect(new InetSocketAddress(inetHost, inetPort));
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress) {
    if (remoteAddress == null)
      throw new NullPointerException("remoteAddress"); 
    validate();
    return doConnect(remoteAddress, localAddress());
  }
  
  public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
    if (remoteAddress == null)
      throw new NullPointerException("remoteAddress"); 
    validate();
    return doConnect(remoteAddress, localAddress);
  }
  
  private ChannelFuture doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
    final ChannelFuture regFuture = initAndRegister();
    final Channel channel = regFuture.channel();
    if (regFuture.cause() != null)
      return regFuture; 
    final ChannelPromise promise = channel.newPromise();
    if (regFuture.isDone()) {
      doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
    } else {
      regFuture.addListener((GenericFutureListener)new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
              Bootstrap.doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
            }
          });
    } 
    return (ChannelFuture)promise;
  }
  
  private static void doConnect0(final ChannelFuture regFuture, final Channel channel, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
    channel.eventLoop().execute(new Runnable() {
          public void run() {
            if (regFuture.isSuccess()) {
              if (localAddress == null) {
                channel.connect(remoteAddress, promise);
              } else {
                channel.connect(remoteAddress, localAddress, promise);
              } 
              promise.addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
              promise.setFailure(regFuture.cause());
            } 
          }
        });
  }
  
  void init(Channel channel) throws Exception {
    ChannelPipeline p = channel.pipeline();
    p.addLast(new ChannelHandler[] { handler() });
    Map<ChannelOption<?>, Object> options = options();
    synchronized (options) {
      for (Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
        try {
          if (!channel.config().setOption(e.getKey(), e.getValue()))
            logger.warn("Unknown channel option: " + e); 
        } catch (Throwable t) {
          logger.warn("Failed to set a channel option: " + channel, t);
        } 
      } 
    } 
    Map<AttributeKey<?>, Object> attrs = attrs();
    synchronized (attrs) {
      for (Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet())
        channel.attr(e.getKey()).set(e.getValue()); 
    } 
  }
  
  public Bootstrap validate() {
    super.validate();
    if (handler() == null)
      throw new IllegalStateException("handler not set"); 
    return this;
  }
  
  public Bootstrap clone() {
    return new Bootstrap(this);
  }
  
  public String toString() {
    if (this.remoteAddress == null)
      return super.toString(); 
    StringBuilder buf = new StringBuilder(super.toString());
    buf.setLength(buf.length() - 1);
    buf.append(", remoteAddress: ");
    buf.append(this.remoteAddress);
    buf.append(')');
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\bootstrap\Bootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */