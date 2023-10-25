package io.netty.channel;

public interface ChannelInboundHandler extends ChannelHandler {
  void channelRegistered(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
  
  void channelUnregistered(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
  
  void channelActive(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
  
  void channelInactive(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
  
  void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) throws Exception;
  
  void channelReadComplete(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
  
  void userEventTriggered(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) throws Exception;
  
  void channelWritabilityChanged(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
  
  void exceptionCaught(ChannelHandlerContext paramChannelHandlerContext, Throwable paramThrowable) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelInboundHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */