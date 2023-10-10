package io.netty.channel.oio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOioMessageChannel extends AbstractOioChannel {
   private final List readBuf = new ArrayList();

   protected AbstractOioMessageChannel(Channel var1) {
      super(var1);
   }

   protected void doRead() {
      ChannelPipeline var1 = this.pipeline();
      boolean var2 = false;
      Throwable var3 = null;

      int var4;
      try {
         var4 = this.doReadMessages(this.readBuf);
         if (var4 < 0) {
            var2 = true;
         }
      } catch (Throwable var6) {
         var3 = var6;
      }

      var4 = this.readBuf.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         var1.fireChannelRead(this.readBuf.get(var5));
      }

      this.readBuf.clear();
      var1.fireChannelReadComplete();
      if (var3 != null) {
         if (var3 instanceof IOException) {
            var2 = true;
         }

         this.pipeline().fireExceptionCaught(var3);
      }

      if (var2 && this.isOpen()) {
         this.unsafe().close(this.unsafe().voidPromise());
      }

   }

   protected abstract int doReadMessages(List var1) throws Exception;
}
