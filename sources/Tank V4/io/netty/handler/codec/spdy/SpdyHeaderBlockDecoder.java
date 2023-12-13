package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

abstract class SpdyHeaderBlockDecoder {
   static SpdyHeaderBlockDecoder newInstance(SpdyVersion var0, int var1) {
      return new SpdyHeaderBlockZlibDecoder(var0, var1);
   }

   abstract void decode(ByteBuf var1, SpdyHeadersFrame var2) throws Exception;

   abstract void reset();

   abstract void end();
}
