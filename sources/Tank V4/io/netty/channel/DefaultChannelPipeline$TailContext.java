package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;

final class DefaultChannelPipeline$TailContext extends AbstractChannelHandlerContext implements ChannelInboundHandler {
   private static final String TAIL_NAME = DefaultChannelPipeline.access$300(DefaultChannelPipeline$TailContext.class);

   DefaultChannelPipeline$TailContext(DefaultChannelPipeline var1) {
      super(var1, (EventExecutorGroup)null, TAIL_NAME, true, false);
   }

   public ChannelHandler handler() {
      return this;
   }

   public void channelRegistered(ChannelHandlerContext var1) throws Exception {
   }

   public void channelUnregistered(ChannelHandlerContext var1) throws Exception {
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
   }

   public void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception {
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
   }

   public void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception {
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      DefaultChannelPipeline.logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", var2);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      DefaultChannelPipeline.logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", var2);
      ReferenceCountUtil.release(var2);
   }

   public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
   }
}
