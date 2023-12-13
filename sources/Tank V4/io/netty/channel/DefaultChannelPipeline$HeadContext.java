package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;
import java.net.SocketAddress;

final class DefaultChannelPipeline$HeadContext extends AbstractChannelHandlerContext implements ChannelOutboundHandler {
   private static final String HEAD_NAME = DefaultChannelPipeline.access$300(DefaultChannelPipeline$HeadContext.class);
   protected final Channel.Unsafe unsafe;

   DefaultChannelPipeline$HeadContext(DefaultChannelPipeline var1) {
      super(var1, (EventExecutorGroup)null, HEAD_NAME, false, true);
      this.unsafe = var1.channel().unsafe();
   }

   public ChannelHandler handler() {
      return this;
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
   }

   public void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception {
      this.unsafe.bind(var2, var3);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception {
      this.unsafe.connect(var2, var3, var4);
   }

   public void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.unsafe.disconnect(var2);
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.unsafe.close(var2);
   }

   public void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.unsafe.deregister(var2);
   }

   public void read(ChannelHandlerContext var1) {
      this.unsafe.beginRead();
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.unsafe.write(var2, var3);
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      this.unsafe.flush();
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      var1.fireExceptionCaught(var2);
   }
}
