package io.netty.channel;

import io.netty.util.concurrent.Future;

public final class ChannelPromiseNotifier implements ChannelFutureListener {
   private final ChannelPromise[] promises;

   public ChannelPromiseNotifier(ChannelPromise... var1) {
      if (var1 == null) {
         throw new NullPointerException("promises");
      } else {
         ChannelPromise[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ChannelPromise var5 = var2[var4];
            if (var5 == null) {
               throw new IllegalArgumentException("promises contains null ChannelPromise");
            }
         }

         this.promises = (ChannelPromise[])var1.clone();
      }
   }

   public void operationComplete(ChannelFuture var1) throws Exception {
      int var4;
      if (var1.isSuccess()) {
         ChannelPromise[] var7 = this.promises;
         int var8 = var7.length;

         for(var4 = 0; var4 < var8; ++var4) {
            ChannelPromise var9 = var7[var4];
            var9.setSuccess();
         }

      } else {
         Throwable var2 = var1.cause();
         ChannelPromise[] var3 = this.promises;
         var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ChannelPromise var6 = var3[var5];
            var6.setFailure(var2);
         }

      }
   }

   public void operationComplete(Future var1) throws Exception {
      this.operationComplete((ChannelFuture)var1);
   }
}
