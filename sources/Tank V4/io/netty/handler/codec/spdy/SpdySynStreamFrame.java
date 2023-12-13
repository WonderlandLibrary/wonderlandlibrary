package io.netty.handler.codec.spdy;

public interface SpdySynStreamFrame extends SpdyHeadersFrame {
   int getAssociatedToStreamId();

   SpdySynStreamFrame setAssociatedToStreamId(int var1);

   byte getPriority();

   SpdySynStreamFrame setPriority(byte var1);

   boolean isUnidirectional();

   SpdySynStreamFrame setUnidirectional(boolean var1);

   SpdySynStreamFrame setStreamId(int var1);

   SpdySynStreamFrame setLast(boolean var1);

   SpdySynStreamFrame setInvalid();
}
