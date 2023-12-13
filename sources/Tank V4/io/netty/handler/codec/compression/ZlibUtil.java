package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.JZlib.WrapperType;

final class ZlibUtil {
   static void fail(Inflater var0, String var1, int var2) {
      throw inflaterException(var0, var1, var2);
   }

   static void fail(Deflater var0, String var1, int var2) {
      throw deflaterException(var0, var1, var2);
   }

   static CompressionException inflaterException(Inflater var0, String var1, int var2) {
      return new CompressionException(var1 + " (" + var2 + ')' + (var0.msg != null ? ": " + var0.msg : ""));
   }

   static CompressionException deflaterException(Deflater var0, String var1, int var2) {
      return new CompressionException(var1 + " (" + var2 + ')' + (var0.msg != null ? ": " + var0.msg : ""));
   }

   static WrapperType convertWrapperType(ZlibWrapper var0) {
      WrapperType var1;
      switch(var0) {
      case NONE:
         var1 = JZlib.W_NONE;
         break;
      case ZLIB:
         var1 = JZlib.W_ZLIB;
         break;
      case GZIP:
         var1 = JZlib.W_GZIP;
         break;
      case ZLIB_OR_NONE:
         var1 = JZlib.W_ANY;
         break;
      default:
         throw new Error();
      }

      return var1;
   }

   static int wrapperOverhead(ZlibWrapper var0) {
      byte var1;
      switch(var0) {
      case NONE:
         var1 = 0;
         break;
      case ZLIB:
      case ZLIB_OR_NONE:
         var1 = 2;
         break;
      case GZIP:
         var1 = 10;
         break;
      default:
         throw new Error();
      }

      return var1;
   }

   private ZlibUtil() {
   }
}
