package io.netty.handler.codec.spdy;

public interface SpdyRstStreamFrame extends SpdyStreamFrame {
   SpdyStreamStatus getStatus();

   SpdyRstStreamFrame setStatus(SpdyStreamStatus var1);

   SpdyRstStreamFrame setStreamId(int var1);

   SpdyRstStreamFrame setLast(boolean var1);
}
