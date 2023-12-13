package io.netty.handler.codec.spdy;

public interface SpdyHeadersFrame extends SpdyStreamFrame {
   boolean isInvalid();

   SpdyHeadersFrame setInvalid();

   boolean isTruncated();

   SpdyHeadersFrame setTruncated();

   SpdyHeaders headers();

   SpdyHeadersFrame setStreamId(int var1);

   SpdyHeadersFrame setLast(boolean var1);
}
