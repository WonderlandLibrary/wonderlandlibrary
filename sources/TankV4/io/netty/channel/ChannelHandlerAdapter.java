package io.netty.channel;

public abstract class ChannelHandlerAdapter implements ChannelHandler {
   boolean added;

   public boolean isSharable() {
      return this.getClass().isAnnotationPresent(ChannelHandler.Sharable.class);
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
   }

   /** @deprecated */
   @Deprecated
   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      var1.fireExceptionCaught(var2);
   }
}
