package io.netty.channel;

import io.netty.util.concurrent.Future;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ChannelPromiseAggregator implements ChannelFutureListener {
   private final ChannelPromise aggregatePromise;
   private Set pendingPromises;

   public ChannelPromiseAggregator(ChannelPromise var1) {
      if (var1 == null) {
         throw new NullPointerException("aggregatePromise");
      } else {
         this.aggregatePromise = var1;
      }
   }

   public ChannelPromiseAggregator add(ChannelPromise... var1) {
      if (var1 == null) {
         throw new NullPointerException("promises");
      } else if (var1.length == 0) {
         return this;
      } else {
         synchronized(this){}
         if (this.pendingPromises == null) {
            int var3;
            if (var1.length > 1) {
               var3 = var1.length;
            } else {
               var3 = 2;
            }

            this.pendingPromises = new LinkedHashSet(var3);
         }

         ChannelPromise[] var8 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ChannelPromise var6 = var8[var5];
            if (var6 != null) {
               this.pendingPromises.add(var6);
               var6.addListener(this);
            }
         }

         return this;
      }
   }

   public synchronized void operationComplete(ChannelFuture var1) throws Exception {
      if (this.pendingPromises == null) {
         this.aggregatePromise.setSuccess();
      } else {
         this.pendingPromises.remove(var1);
         if (!var1.isSuccess()) {
            this.aggregatePromise.setFailure(var1.cause());
            Iterator var2 = this.pendingPromises.iterator();

            while(var2.hasNext()) {
               ChannelPromise var3 = (ChannelPromise)var2.next();
               var3.setFailure(var1.cause());
            }
         } else if (this.pendingPromises.isEmpty()) {
            this.aggregatePromise.setSuccess();
         }
      }

   }

   public void operationComplete(Future var1) throws Exception {
      this.operationComplete((ChannelFuture)var1);
   }
}
