package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;

final class ZlibUtil {
  static void fail(Inflater z, String message, int resultCode) {
    throw inflaterException(z, message, resultCode);
  }
  
  static void fail(Deflater z, String message, int resultCode) {
    throw deflaterException(z, message, resultCode);
  }
  
  static DecompressionException inflaterException(Inflater z, String message, int resultCode) {
    return new DecompressionException(message + " (" + resultCode + ')' + ((z.msg != null) ? (": " + z.msg) : ""));
  }
  
  static CompressionException deflaterException(Deflater z, String message, int resultCode) {
    return new CompressionException(message + " (" + resultCode + ')' + ((z.msg != null) ? (": " + z.msg) : ""));
  }
  
  static JZlib.WrapperType convertWrapperType(ZlibWrapper wrapper) {
    JZlib.WrapperType convertedWrapperType;
    switch (wrapper) {
      case NONE:
        convertedWrapperType = JZlib.W_NONE;
        return convertedWrapperType;
      case ZLIB:
        convertedWrapperType = JZlib.W_ZLIB;
        return convertedWrapperType;
      case GZIP:
        convertedWrapperType = JZlib.W_GZIP;
        return convertedWrapperType;
      case ZLIB_OR_NONE:
        convertedWrapperType = JZlib.W_ANY;
        return convertedWrapperType;
    } 
    throw new Error();
  }
  
  static int wrapperOverhead(ZlibWrapper wrapper) {
    int overhead;
    switch (wrapper) {
      case NONE:
        overhead = 0;
        return overhead;
      case ZLIB:
      case ZLIB_OR_NONE:
        overhead = 2;
        return overhead;
      case GZIP:
        overhead = 10;
        return overhead;
    } 
    throw new Error();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\compression\ZlibUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */