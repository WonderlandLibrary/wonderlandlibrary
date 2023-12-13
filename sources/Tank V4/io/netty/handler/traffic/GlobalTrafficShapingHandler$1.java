package io.netty.handler.traffic;

import io.netty.channel.ChannelHandlerContext;
import java.util.List;

class GlobalTrafficShapingHandler$1 implements Runnable {
   final ChannelHandlerContext val$ctx;
   final List val$mqfinal;
   final GlobalTrafficShapingHandler this$0;

   GlobalTrafficShapingHandler$1(GlobalTrafficShapingHandler var1, ChannelHandlerContext var2, List var3) {
      this.this$0 = var1;
      this.val$ctx = var2;
      this.val$mqfinal = var3;
   }

   public void run() {
      GlobalTrafficShapingHandler.access$100(this.this$0, this.val$ctx, this.val$mqfinal);
   }
}
