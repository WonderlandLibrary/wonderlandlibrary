package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;

abstract class SpdyHeaderBlockEncoder {
  static SpdyHeaderBlockEncoder newInstance(SpdyVersion version, int compressionLevel, int windowBits, int memLevel) {
    if (PlatformDependent.javaVersion() >= 7)
      return new SpdyHeaderBlockZlibEncoder(version, compressionLevel); 
    return new SpdyHeaderBlockJZlibEncoder(version, compressionLevel, windowBits, memLevel);
  }
  
  abstract ByteBuf encode(SpdyHeadersFrame paramSpdyHeadersFrame) throws Exception;
  
  abstract void end();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\spdy\SpdyHeaderBlockEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */