package io.netty.handler.codec.spdy;

public interface SpdyStreamFrame extends SpdyFrame {
   int getStreamId();

   SpdyStreamFrame setStreamId(int var1);

   boolean isLast();

   SpdyStreamFrame setLast(boolean var1);
}
