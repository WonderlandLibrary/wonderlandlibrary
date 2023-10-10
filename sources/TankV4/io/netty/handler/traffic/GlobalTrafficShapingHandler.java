package io.netty.handler.traffic;

import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ScheduledExecutorService;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler extends AbstractTrafficShapingHandler {
   void createGlobalTrafficCounter(ScheduledExecutorService var1) {
      if (var1 == null) {
         throw new NullPointerException("executor");
      } else {
         TrafficCounter var2 = new TrafficCounter(this, var1, "GlobalTC", this.checkInterval);
         this.setTrafficCounter(var2);
         var2.start();
      }
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2, long var4, long var6) {
      super(var2, var4, var6);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2, long var4) {
      super(var2, var4);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2) {
      super(var2);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(EventExecutor var1) {
      this.createGlobalTrafficCounter(var1);
   }

   public final void release() {
      if (this.trafficCounter != null) {
         this.trafficCounter.stop();
      }

   }
}
