package io.netty.handler.codec.spdy;

public abstract class DefaultSpdyStreamFrame implements SpdyStreamFrame {
   private int streamId;
   private boolean last;

   protected DefaultSpdyStreamFrame(int var1) {
      this.setStreamId(var1);
   }

   public int getStreamId() {
      return this.streamId;
   }

   public SpdyStreamFrame setStreamId(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("Stream-ID must be positive: " + var1);
      } else {
         this.streamId = var1;
         return this;
      }
   }

   public boolean isLast() {
      return this.last;
   }

   public SpdyStreamFrame setLast(boolean var1) {
      this.last = var1;
      return this;
   }
}
