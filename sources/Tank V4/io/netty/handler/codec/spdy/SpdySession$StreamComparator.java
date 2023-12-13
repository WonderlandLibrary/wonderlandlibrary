package io.netty.handler.codec.spdy;

import java.io.Serializable;
import java.util.Comparator;

final class SpdySession$StreamComparator implements Comparator, Serializable {
   private static final long serialVersionUID = 1161471649740544848L;
   final SpdySession this$0;

   SpdySession$StreamComparator(SpdySession var1) {
      this.this$0 = var1;
   }

   public int compare(Integer var1, Integer var2) {
      SpdySession.StreamState var3 = (SpdySession.StreamState)SpdySession.access$000(this.this$0).get(var1);
      SpdySession.StreamState var4 = (SpdySession.StreamState)SpdySession.access$000(this.this$0).get(var2);
      int var5 = var3.getPriority() - var4.getPriority();
      return var5 != 0 ? var5 : var1 - var2;
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Integer)var1, (Integer)var2);
   }
}
