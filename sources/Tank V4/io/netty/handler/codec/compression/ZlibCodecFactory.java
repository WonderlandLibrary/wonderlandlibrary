package io.netty.handler.codec.compression;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class ZlibCodecFactory {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ZlibCodecFactory.class);
   private static final boolean noJdkZlibDecoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibDecoder", true);

   public static ZlibEncoder newZlibEncoder(int var0) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0) : new JdkZlibEncoder(var0));
   }

   public static ZlibEncoder newZlibEncoder(ZlibWrapper var0) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0) : new JdkZlibEncoder(var0));
   }

   public static ZlibEncoder newZlibEncoder(ZlibWrapper var0, int var1) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0, var1) : new JdkZlibEncoder(var0, var1));
   }

   public static ZlibEncoder newZlibEncoder(ZlibWrapper var0, int var1, int var2, int var3) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0, var1, var2, var3) : new JdkZlibEncoder(var0, var1));
   }

   public static ZlibEncoder newZlibEncoder(byte[] var0) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0) : new JdkZlibEncoder(var0));
   }

   public static ZlibEncoder newZlibEncoder(int var0, byte[] var1) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0, var1) : new JdkZlibEncoder(var0, var1));
   }

   public static ZlibEncoder newZlibEncoder(int var0, int var1, int var2, byte[] var3) {
      return (ZlibEncoder)(PlatformDependent.javaVersion() < 7 ? new JZlibEncoder(var0, var1, var2, var3) : new JdkZlibEncoder(var0, var3));
   }

   public static ZlibDecoder newZlibDecoder() {
      return (ZlibDecoder)(PlatformDependent.javaVersion() >= 7 && !noJdkZlibDecoder ? new JdkZlibDecoder() : new JZlibDecoder());
   }

   public static ZlibDecoder newZlibDecoder(ZlibWrapper var0) {
      switch(var0) {
      case ZLIB_OR_NONE:
         return new JZlibDecoder(var0);
      default:
         return (ZlibDecoder)(PlatformDependent.javaVersion() >= 7 && !noJdkZlibDecoder ? new JdkZlibDecoder(var0) : new JZlibDecoder(var0));
      }
   }

   public static ZlibDecoder newZlibDecoder(byte[] var0) {
      return (ZlibDecoder)(PlatformDependent.javaVersion() >= 7 && !noJdkZlibDecoder ? new JdkZlibDecoder(var0) : new JZlibDecoder(var0));
   }

   private ZlibCodecFactory() {
   }

   static {
      logger.debug("-Dio.netty.noJdkZlibDecoder: {}", (Object)noJdkZlibDecoder);
   }
}
