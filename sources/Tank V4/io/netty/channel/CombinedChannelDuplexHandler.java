package io.netty.channel;

import java.net.SocketAddress;

public class CombinedChannelDuplexHandler extends ChannelDuplexHandler {
   private ChannelInboundHandler inboundHandler;
   private ChannelOutboundHandler outboundHandler;

   protected CombinedChannelDuplexHandler() {
   }

   public CombinedChannelDuplexHandler(ChannelInboundHandler var1, ChannelOutboundHandler var2) {
      this.init(var1, var2);
   }

   protected final void init(ChannelInboundHandler var1, ChannelOutboundHandler var2) {
      this.validate(var1, var2);
      this.inboundHandler = var1;
      this.outboundHandler = var2;
   }

   private void validate(ChannelInboundHandler var1, ChannelOutboundHandler var2) {
      if (this.inboundHandler != null) {
         throw new IllegalStateException("init() can not be invoked if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with non-default constructor.");
      } else if (var1 == null) {
         throw new NullPointerException("inboundHandler");
      } else if (var2 == null) {
         throw new NullPointerException("outboundHandler");
      } else if (var1 instanceof ChannelOutboundHandler) {
         throw new IllegalArgumentException("inboundHandler must not implement " + ChannelOutboundHandler.class.getSimpleName() + " to get combined.");
      } else if (var2 instanceof ChannelInboundHandler) {
         throw new IllegalArgumentException("outboundHandler must not implement " + ChannelInboundHandler.class.getSimpleName() + " to get combined.");
      }
   }

   protected final ChannelInboundHandler inboundHandler() {
      return this.inboundHandler;
   }

   protected final ChannelOutboundHandler outboundHandler() {
      return this.outboundHandler;
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      if (this.inboundHandler == null) {
         throw new IllegalStateException("init() must be invoked before being added to a " + ChannelPipeline.class.getSimpleName() + " if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with the default constructor.");
      } else {
         this.inboundHandler.handlerAdded(var1);
         this.outboundHandler.handlerAdded(var1);
      }
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.handlerRemoved(var1);
      this.outboundHandler.handlerRemoved(var1);
   }

   public void channelRegistered(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.channelRegistered(var1);
   }

   public void channelUnregistered(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.channelUnregistered(var1);
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.channelActive(var1);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.channelInactive(var1);
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      this.inboundHandler.exceptionCaught(var1, var2);
   }

   public void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception {
      this.inboundHandler.userEventTriggered(var1, var2);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      this.inboundHandler.channelRead(var1, var2);
   }

   public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.channelReadComplete(var1);
   }

   public void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception {
      this.outboundHandler.bind(var1, var2, var3);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception {
      this.outboundHandler.connect(var1, var2, var3, var4);
   }

   public void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.outboundHandler.disconnect(var1, var2);
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.outboundHandler.close(var1, var2);
   }

   public void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.outboundHandler.deregister(var1, var2);
   }

   public void read(ChannelHandlerContext var1) throws Exception {
      this.outboundHandler.read(var1);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.outboundHandler.write(var1, var2, var3);
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      this.outboundHandler.flush(var1);
   }

   public void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception {
      this.inboundHandler.channelWritabilityChanged(var1);
   }
}
