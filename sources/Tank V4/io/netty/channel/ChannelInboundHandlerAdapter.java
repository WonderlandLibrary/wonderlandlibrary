package io.netty.channel;

public class ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelInboundHandler {
   public void channelRegistered(ChannelHandlerContext var1) throws Exception {
      var1.fireChannelRegistered();
   }

   public void channelUnregistered(ChannelHandlerContext var1) throws Exception {
      var1.fireChannelUnregistered();
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      var1.fireChannelActive();
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      var1.fireChannelInactive();
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      var1.fireChannelRead(var2);
   }

   public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
      var1.fireChannelReadComplete();
   }

   public void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception {
      var1.fireUserEventTriggered(var2);
   }

   public void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception {
      var1.fireChannelWritabilityChanged();
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      var1.fireExceptionCaught(var2);
   }
}
