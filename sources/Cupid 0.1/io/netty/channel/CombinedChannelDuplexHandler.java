package io.netty.channel;

import java.net.SocketAddress;

public class CombinedChannelDuplexHandler<I extends ChannelInboundHandler, O extends ChannelOutboundHandler> extends ChannelDuplexHandler {
  private I inboundHandler;
  
  private O outboundHandler;
  
  protected CombinedChannelDuplexHandler() {}
  
  public CombinedChannelDuplexHandler(I inboundHandler, O outboundHandler) {
    init(inboundHandler, outboundHandler);
  }
  
  protected final void init(I inboundHandler, O outboundHandler) {
    validate(inboundHandler, outboundHandler);
    this.inboundHandler = inboundHandler;
    this.outboundHandler = outboundHandler;
  }
  
  private void validate(I inboundHandler, O outboundHandler) {
    if (this.inboundHandler != null)
      throw new IllegalStateException("init() can not be invoked if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with non-default constructor."); 
    if (inboundHandler == null)
      throw new NullPointerException("inboundHandler"); 
    if (outboundHandler == null)
      throw new NullPointerException("outboundHandler"); 
    if (inboundHandler instanceof ChannelOutboundHandler)
      throw new IllegalArgumentException("inboundHandler must not implement " + ChannelOutboundHandler.class.getSimpleName() + " to get combined."); 
    if (outboundHandler instanceof ChannelInboundHandler)
      throw new IllegalArgumentException("outboundHandler must not implement " + ChannelInboundHandler.class.getSimpleName() + " to get combined."); 
  }
  
  protected final I inboundHandler() {
    return this.inboundHandler;
  }
  
  protected final O outboundHandler() {
    return this.outboundHandler;
  }
  
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    if (this.inboundHandler == null)
      throw new IllegalStateException("init() must be invoked before being added to a " + ChannelPipeline.class.getSimpleName() + " if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with the default constructor."); 
    try {
      this.inboundHandler.handlerAdded(ctx);
    } finally {
      this.outboundHandler.handlerAdded(ctx);
    } 
  }
  
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    try {
      this.inboundHandler.handlerRemoved(ctx);
    } finally {
      this.outboundHandler.handlerRemoved(ctx);
    } 
  }
  
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    this.inboundHandler.channelRegistered(ctx);
  }
  
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    this.inboundHandler.channelUnregistered(ctx);
  }
  
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    this.inboundHandler.channelActive(ctx);
  }
  
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    this.inboundHandler.channelInactive(ctx);
  }
  
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    this.inboundHandler.exceptionCaught(ctx, cause);
  }
  
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    this.inboundHandler.userEventTriggered(ctx, evt);
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    this.inboundHandler.channelRead(ctx, msg);
  }
  
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    this.inboundHandler.channelReadComplete(ctx);
  }
  
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    this.outboundHandler.bind(ctx, localAddress, promise);
  }
  
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    this.outboundHandler.connect(ctx, remoteAddress, localAddress, promise);
  }
  
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    this.outboundHandler.disconnect(ctx, promise);
  }
  
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    this.outboundHandler.close(ctx, promise);
  }
  
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    this.outboundHandler.deregister(ctx, promise);
  }
  
  public void read(ChannelHandlerContext ctx) throws Exception {
    this.outboundHandler.read(ctx);
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    this.outboundHandler.write(ctx, msg, promise);
  }
  
  public void flush(ChannelHandlerContext ctx) throws Exception {
    this.outboundHandler.flush(ctx);
  }
  
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    this.inboundHandler.channelWritabilityChanged(ctx);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\CombinedChannelDuplexHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */