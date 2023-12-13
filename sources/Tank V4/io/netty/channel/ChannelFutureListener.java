package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelFutureListener extends GenericFutureListener {
   ChannelFutureListener CLOSE = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture var1) {
         var1.channel().close();
      }

      public void operationComplete(Future var1) throws Exception {
         this.operationComplete((ChannelFuture)var1);
      }
   };
   ChannelFutureListener CLOSE_ON_FAILURE = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture var1) {
         if (!var1.isSuccess()) {
            var1.channel().close();
         }

      }

      public void operationComplete(Future var1) throws Exception {
         this.operationComplete((ChannelFuture)var1);
      }
   };
   ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener() {
      public void operationComplete(ChannelFuture var1) {
         if (!var1.isSuccess()) {
            var1.channel().pipeline().fireExceptionCaught(var1.cause());
         }

      }

      public void operationComplete(Future var1) throws Exception {
         this.operationComplete((ChannelFuture)var1);
      }
   };
}
