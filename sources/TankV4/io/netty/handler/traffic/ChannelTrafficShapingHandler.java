package io.netty.handler.traffic;

import io.netty.channel.ChannelHandlerContext;

public class ChannelTrafficShapingHandler extends AbstractTrafficShapingHandler {
   public ChannelTrafficShapingHandler(long var1, long var3, long var5) {
      super(var1, var3, var5);
   }

   public ChannelTrafficShapingHandler(long var1, long var3) {
      super(var1, var3);
   }

   public ChannelTrafficShapingHandler(long var1) {
      super(var1);
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      TrafficCounter var2 = new TrafficCounter(this, var1.executor(), "ChannelTC" + var1.channel().hashCode(), this.checkInterval);
      this.setTrafficCounter(var2);
      var2.start();
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      if (this.trafficCounter != null) {
         this.trafficCounter.stop();
      }

   }
}
