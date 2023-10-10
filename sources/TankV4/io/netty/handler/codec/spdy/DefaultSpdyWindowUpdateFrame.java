package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyWindowUpdateFrame implements SpdyWindowUpdateFrame {
   private int streamId;
   private int deltaWindowSize;

   public DefaultSpdyWindowUpdateFrame(int var1, int var2) {
      this.setStreamId(var1);
      this.setDeltaWindowSize(var2);
   }

   public int getStreamId() {
      return this.streamId;
   }

   public SpdyWindowUpdateFrame setStreamId(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Stream-ID cannot be negative: " + var1);
      } else {
         this.streamId = var1;
         return this;
      }
   }

   public int getDeltaWindowSize() {
      return this.deltaWindowSize;
   }

   public SpdyWindowUpdateFrame setDeltaWindowSize(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("Delta-Window-Size must be positive: " + var1);
      } else {
         this.deltaWindowSize = var1;
         return this;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Stream-ID = ");
      var1.append(this.getStreamId());
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Delta-Window-Size = ");
      var1.append(this.getDeltaWindowSize());
      return var1.toString();
   }
}
