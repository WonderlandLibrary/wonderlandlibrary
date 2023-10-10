package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyGoAwayFrame implements SpdyGoAwayFrame {
   private int lastGoodStreamId;
   private SpdySessionStatus status;

   public DefaultSpdyGoAwayFrame(int var1) {
      this(var1, 0);
   }

   public DefaultSpdyGoAwayFrame(int var1, int var2) {
      this(var1, SpdySessionStatus.valueOf(var2));
   }

   public DefaultSpdyGoAwayFrame(int var1, SpdySessionStatus var2) {
      this.setLastGoodStreamId(var1);
      this.setStatus(var2);
   }

   public int getLastGoodStreamId() {
      return this.lastGoodStreamId;
   }

   public SpdyGoAwayFrame setLastGoodStreamId(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Last-good-stream-ID cannot be negative: " + var1);
      } else {
         this.lastGoodStreamId = var1;
         return this;
      }
   }

   public SpdySessionStatus getStatus() {
      return this.status;
   }

   public SpdyGoAwayFrame setStatus(SpdySessionStatus var1) {
      this.status = var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Last-good-stream-ID = ");
      var1.append(this.getLastGoodStreamId());
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Status: ");
      var1.append(this.getStatus().toString());
      return var1.toString();
   }
}
