package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sun.misc.Cleaner;
import sun.misc.Unsafe;

final class PlatformDependent0 {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
   private static final Unsafe UNSAFE;
   private static final boolean BIG_ENDIAN;
   private static final long CLEANER_FIELD_OFFSET;
   private static final long ADDRESS_FIELD_OFFSET;
   private static final Field CLEANER_FIELD;
   private static final boolean UNALIGNED;

   static boolean hasUnsafe() {
      return UNSAFE != null;
   }

   static void throwException(Throwable var0) {
      UNSAFE.throwException(var0);
   }

   static void freeDirectBufferUnsafe(ByteBuffer var0) {
      try {
         Cleaner var1 = (Cleaner)getObject(var0, CLEANER_FIELD_OFFSET);
         if (var1 == null) {
            throw new IllegalArgumentException("attempted to deallocate the buffer which was allocated via JNIEnv->NewDirectByteBuffer()");
         }

         var1.clean();
      } catch (Throwable var3) {
      }

   }

   static void freeDirectBuffer(ByteBuffer var0) {
      if (CLEANER_FIELD != null) {
         try {
            Cleaner var1 = (Cleaner)CLEANER_FIELD.get(var0);
            if (var1 == null) {
               throw new IllegalArgumentException("attempted to deallocate the buffer which was allocated via JNIEnv->NewDirectByteBuffer()");
            }

            var1.clean();
         } catch (Throwable var2) {
         }

      }
   }

   static long directBufferAddress(ByteBuffer var0) {
      return getLong(var0, ADDRESS_FIELD_OFFSET);
   }

   static long arrayBaseOffset() {
      return (long)UNSAFE.arrayBaseOffset(byte[].class);
   }

   static Object getObject(Object var0, long var1) {
      return UNSAFE.getObject(var0, var1);
   }

   static int getInt(Object var0, long var1) {
      return UNSAFE.getInt(var0, var1);
   }

   private static long getLong(Object var0, long var1) {
      return UNSAFE.getLong(var0, var1);
   }

   static long objectFieldOffset(Field var0) {
      return UNSAFE.objectFieldOffset(var0);
   }

   static byte getByte(long var0) {
      return UNSAFE.getByte(var0);
   }

   static short getShort(long var0) {
      if (UNALIGNED) {
         return UNSAFE.getShort(var0);
      } else {
         return BIG_ENDIAN ? (short)(getByte(var0) << 8 | getByte(var0 + 1L) & 255) : (short)(getByte(var0 + 1L) << 8 | getByte(var0) & 255);
      }
   }

   static int getInt(long var0) {
      if (UNALIGNED) {
         return UNSAFE.getInt(var0);
      } else {
         return BIG_ENDIAN ? getByte(var0) << 24 | (getByte(var0 + 1L) & 255) << 16 | (getByte(var0 + 2L) & 255) << 8 | getByte(var0 + 3L) & 255 : getByte(var0 + 3L) << 24 | (getByte(var0 + 2L) & 255) << 16 | (getByte(var0 + 1L) & 255) << 8 | getByte(var0) & 255;
      }
   }

   static long getLong(long var0) {
      if (UNALIGNED) {
         return UNSAFE.getLong(var0);
      } else {
         return BIG_ENDIAN ? (long)getByte(var0) << 56 | ((long)getByte(var0 + 1L) & 255L) << 48 | ((long)getByte(var0 + 2L) & 255L) << 40 | ((long)getByte(var0 + 3L) & 255L) << 32 | ((long)getByte(var0 + 4L) & 255L) << 24 | ((long)getByte(var0 + 5L) & 255L) << 16 | ((long)getByte(var0 + 6L) & 255L) << 8 | (long)getByte(var0 + 7L) & 255L : (long)getByte(var0 + 7L) << 56 | ((long)getByte(var0 + 6L) & 255L) << 48 | ((long)getByte(var0 + 5L) & 255L) << 40 | ((long)getByte(var0 + 4L) & 255L) << 32 | ((long)getByte(var0 + 3L) & 255L) << 24 | ((long)getByte(var0 + 2L) & 255L) << 16 | ((long)getByte(var0 + 1L) & 255L) << 8 | (long)getByte(var0) & 255L;
      }
   }

   static void putByte(long var0, byte var2) {
      UNSAFE.putByte(var0, var2);
   }

   static void putShort(long var0, short var2) {
      if (UNALIGNED) {
         UNSAFE.putShort(var0, var2);
      } else if (BIG_ENDIAN) {
         putByte(var0, (byte)(var2 >>> 8));
         putByte(var0 + 1L, (byte)var2);
      } else {
         putByte(var0 + 1L, (byte)(var2 >>> 8));
         putByte(var0, (byte)var2);
      }

   }

   static void putInt(long var0, int var2) {
      if (UNALIGNED) {
         UNSAFE.putInt(var0, var2);
      } else if (BIG_ENDIAN) {
         putByte(var0, (byte)(var2 >>> 24));
         putByte(var0 + 1L, (byte)(var2 >>> 16));
         putByte(var0 + 2L, (byte)(var2 >>> 8));
         putByte(var0 + 3L, (byte)var2);
      } else {
         putByte(var0 + 3L, (byte)(var2 >>> 24));
         putByte(var0 + 2L, (byte)(var2 >>> 16));
         putByte(var0 + 1L, (byte)(var2 >>> 8));
         putByte(var0, (byte)var2);
      }

   }

   static void putLong(long var0, long var2) {
      if (UNALIGNED) {
         UNSAFE.putLong(var0, var2);
      } else if (BIG_ENDIAN) {
         putByte(var0, (byte)((int)(var2 >>> 56)));
         putByte(var0 + 1L, (byte)((int)(var2 >>> 48)));
         putByte(var0 + 2L, (byte)((int)(var2 >>> 40)));
         putByte(var0 + 3L, (byte)((int)(var2 >>> 32)));
         putByte(var0 + 4L, (byte)((int)(var2 >>> 24)));
         putByte(var0 + 5L, (byte)((int)(var2 >>> 16)));
         putByte(var0 + 6L, (byte)((int)(var2 >>> 8)));
         putByte(var0 + 7L, (byte)((int)var2));
      } else {
         putByte(var0 + 7L, (byte)((int)(var2 >>> 56)));
         putByte(var0 + 6L, (byte)((int)(var2 >>> 48)));
         putByte(var0 + 5L, (byte)((int)(var2 >>> 40)));
         putByte(var0 + 4L, (byte)((int)(var2 >>> 32)));
         putByte(var0 + 3L, (byte)((int)(var2 >>> 24)));
         putByte(var0 + 2L, (byte)((int)(var2 >>> 16)));
         putByte(var0 + 1L, (byte)((int)(var2 >>> 8)));
         putByte(var0, (byte)((int)var2));
      }

   }

   static void copyMemory(long var0, long var2, long var4) {
      UNSAFE.copyMemory(var0, var2, var4);
   }

   static void copyMemory(Object var0, long var1, Object var3, long var4, long var6) {
      UNSAFE.copyMemory(var0, var1, var3, var4, var6);
   }

   private PlatformDependent0() {
   }

   static {
      BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
      ByteBuffer var0 = ByteBuffer.allocateDirect(1);

      Field var1;
      try {
         var1 = var0.getClass().getDeclaredField("cleaner");
         var1.setAccessible(true);
         Cleaner var2 = (Cleaner)var1.get(var0);
         var2.clean();
      } catch (Throwable var12) {
         var1 = null;
      }

      CLEANER_FIELD = var1;
      logger.debug("java.nio.ByteBuffer.cleaner: {}", (Object)(var1 != null ? "available" : "unavailable"));

      Field var13;
      try {
         var13 = Buffer.class.getDeclaredField("address");
         var13.setAccessible(true);
         if (var13.getLong(ByteBuffer.allocate(1)) != 0L) {
            var13 = null;
         } else {
            var0 = ByteBuffer.allocateDirect(1);
            if (var13.getLong(var0) == 0L) {
               var13 = null;
            }

            Cleaner var3 = (Cleaner)var1.get(var0);
            var3.clean();
         }
      } catch (Throwable var11) {
         var13 = null;
      }

      logger.debug("java.nio.Buffer.address: {}", (Object)(var13 != null ? "available" : "unavailable"));
      Unsafe var14;
      if (var13 != null && var1 != null) {
         try {
            Field var4 = Unsafe.class.getDeclaredField("theUnsafe");
            var4.setAccessible(true);
            var14 = (Unsafe)var4.get((Object)null);
            logger.debug("sun.misc.Unsafe.theUnsafe: {}", (Object)(var14 != null ? "available" : "unavailable"));

            try {
               var14.getClass().getDeclaredMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
               logger.debug("sun.misc.Unsafe.copyMemory: available");
            } catch (NoSuchMethodError var8) {
               logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
               throw var8;
            } catch (NoSuchMethodException var9) {
               logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
               throw var9;
            }
         } catch (Throwable var10) {
            var14 = null;
         }
      } else {
         var14 = null;
      }

      UNSAFE = var14;
      if (var14 == null) {
         CLEANER_FIELD_OFFSET = -1L;
         ADDRESS_FIELD_OFFSET = -1L;
         UNALIGNED = false;
      } else {
         ADDRESS_FIELD_OFFSET = objectFieldOffset(var13);
         CLEANER_FIELD_OFFSET = objectFieldOffset(var1);

         boolean var15;
         try {
            Class var5 = Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
            Method var16 = var5.getDeclaredMethod("unaligned");
            var16.setAccessible(true);
            var15 = Boolean.TRUE.equals(var16.invoke((Object)null));
         } catch (Throwable var7) {
            String var6 = SystemPropertyUtil.get("os.arch", "");
            var15 = var6.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
         }

         UNALIGNED = var15;
         logger.debug("java.nio.Bits.unaligned: {}", (Object)UNALIGNED);
      }

   }
}
