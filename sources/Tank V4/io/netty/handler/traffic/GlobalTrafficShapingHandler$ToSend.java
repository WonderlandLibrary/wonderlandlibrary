package io.netty.handler.traffic;

import io.netty.channel.ChannelPromise;

final class GlobalTrafficShapingHandler$ToSend {
   final long date;
   final Object toSend;
   final ChannelPromise promise;

   private GlobalTrafficShapingHandler$ToSend(long var1, Object var3, ChannelPromise var4) {
      this.date = System.currentTimeMillis() + var1;
      this.toSend = var3;
      this.promise = var4;
   }

   GlobalTrafficShapingHandler$ToSend(long var1, Object var3, ChannelPromise var4, GlobalTrafficShapingHandler$1 var5) {
      this(var1, var3, var4);
   }
}
