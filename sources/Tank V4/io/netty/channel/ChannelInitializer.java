package io.netty.channel;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public abstract class ChannelInitializer extends ChannelInboundHandlerAdapter {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);

   protected abstract void initChannel(Channel var1) throws Exception;

   public final void channelRegistered(ChannelHandlerContext var1) throws Exception {
      ChannelPipeline var2 = var1.pipeline();
      boolean var3 = false;

      try {
         this.initChannel(var1.channel());
         var2.remove((ChannelHandler)this);
         var1.fireChannelRegistered();
         var3 = true;
      } catch (Throwable var6) {
         logger.warn("Failed to initialize a channel. Closing: " + var1.channel(), var6);
         if (var2.context((ChannelHandler)this) != null) {
            var2.remove((ChannelHandler)this);
         }

         if (!var3) {
            var1.close();
         }

         return;
      }

      if (var2.context((ChannelHandler)this) != null) {
         var2.remove((ChannelHandler)this);
      }

      if (!var3) {
         var1.close();
      }

   }
}
