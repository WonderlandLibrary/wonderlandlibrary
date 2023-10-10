package io.netty.handler.codec.spdy;

public interface SpdyWindowUpdateFrame extends SpdyFrame {
   int getStreamId();

   SpdyWindowUpdateFrame setStreamId(int var1);

   int getDeltaWindowSize();

   SpdyWindowUpdateFrame setDeltaWindowSize(int var1);
}
