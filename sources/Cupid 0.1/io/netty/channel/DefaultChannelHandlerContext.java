package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

final class DefaultChannelHandlerContext extends AbstractChannelHandlerContext {
  private final ChannelHandler handler;
  
  DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutorGroup group, String name, ChannelHandler handler) {
    super(pipeline, group, name, isInbound(handler), isOutbound(handler));
    if (handler == null)
      throw new NullPointerException("handler"); 
    this.handler = handler;
  }
  
  public ChannelHandler handler() {
    return this.handler;
  }
  
  private static boolean isInbound(ChannelHandler handler) {
    return handler instanceof ChannelInboundHandler;
  }
  
  private static boolean isOutbound(ChannelHandler handler) {
    return handler instanceof ChannelOutboundHandler;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\DefaultChannelHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */