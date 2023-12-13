package io.netty.handler.traffic;

import io.netty.channel.ChannelHandlerContext;

class ChannelTrafficShapingHandler$1 implements Runnable {
   final ChannelHandlerContext val$ctx;
   final ChannelTrafficShapingHandler this$0;

   ChannelTrafficShapingHandler$1(ChannelTrafficShapingHandler var1, ChannelHandlerContext var2) {
      this.this$0 = var1;
      this.val$ctx = var2;
   }

   public void run() {
      ChannelTrafficShapingHandler.access$100(this.this$0, this.val$ctx);
   }
}
