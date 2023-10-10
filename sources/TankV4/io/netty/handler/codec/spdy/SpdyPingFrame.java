package io.netty.handler.codec.spdy;

public interface SpdyPingFrame extends SpdyFrame {
   int getId();

   SpdyPingFrame setId(int var1);
}
