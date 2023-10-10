package io.netty.handler.codec.spdy;

public interface SpdyGoAwayFrame extends SpdyFrame {
   int getLastGoodStreamId();

   SpdyGoAwayFrame setLastGoodStreamId(int var1);

   SpdySessionStatus getStatus();

   SpdyGoAwayFrame setStatus(SpdySessionStatus var1);
}
