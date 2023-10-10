package org.lwjgl.util.mapped;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public class MappedHelper {
   public static void setup(MappedObject var0, ByteBuffer var1, int var2, int var3) {
      if (LWJGLUtil.CHECKS && var0.baseAddress != 0L) {
         throw new IllegalStateException("this method should not be called by user-code");
      } else if (LWJGLUtil.CHECKS && !var1.isDirect()) {
         throw new IllegalArgumentException("bytebuffer must be direct");
      } else {
         var0.preventGC = var1;
         if (LWJGLUtil.CHECKS && var2 <= 0) {
            throw new IllegalArgumentException("invalid alignment");
         } else if (LWJGLUtil.CHECKS && (var3 <= 0 || var3 % var2 != 0)) {
            throw new IllegalStateException("sizeof not a multiple of alignment");
         } else {
            long var4 = MemoryUtil.getAddress(var1);
            if (LWJGLUtil.CHECKS && var4 % (long)var2 != 0L) {
               throw new IllegalStateException("buffer address not aligned on " + var2 + " bytes");
            } else {
               var0.baseAddress = var0.viewAddress = var4;
            }
         }
      }
   }

   public static void checkAddress(long var0, MappedObject var2) {
      var2.checkAddress(var0);
   }

   public static void put_views(MappedSet2 var0, int var1) {
      var0.view(var1);
   }

   public static void put_views(MappedSet3 var0, int var1) {
      var0.view(var1);
   }

   public static void put_views(MappedSet4 var0, int var1) {
      var0.view(var1);
   }

   public static void put_view(MappedObject var0, int var1, int var2) {
      var0.setViewAddress(var0.baseAddress + (long)(var1 * var2));
   }

   public static int get_view(MappedObject var0, int var1) {
      return (int)(var0.viewAddress - var0.baseAddress) / var1;
   }

   public static void put_view_shift(MappedObject var0, int var1, int var2) {
      var0.setViewAddress(var0.baseAddress + (long)(var1 << var2));
   }

   public static int get_view_shift(MappedObject var0, int var1) {
      return (int)(var0.viewAddress - var0.baseAddress) >> var1;
   }

   public static void put_view_next(MappedObject var0, int var1) {
      var0.setViewAddress(var0.viewAddress + (long)var1);
   }

   public static MappedObject dup(MappedObject var0, MappedObject var1) {
      var1.baseAddress = var0.baseAddress;
      var1.viewAddress = var0.viewAddress;
      var1.preventGC = var0.preventGC;
      return var1;
   }

   public static MappedObject slice(MappedObject var0, MappedObject var1) {
      var1.baseAddress = var0.viewAddress;
      var1.viewAddress = var0.viewAddress;
      var1.preventGC = var0.preventGC;
      return var1;
   }

   public static void copy(MappedObject var0, MappedObject var1, int var2) {
      if (MappedObject.CHECKS) {
         var0.checkRange(var2);
         var1.checkRange(var2);
      }

      MappedObjectUnsafe.INSTANCE.copyMemory(var0.viewAddress, var1.viewAddress, (long)var2);
   }

   public static ByteBuffer newBuffer(long var0, int var2) {
      return MappedObjectUnsafe.newBuffer(var0, var2);
   }

   public static void bput(byte var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putByte(var1, var0);
   }

   public static void bput(MappedObject var0, byte var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putByte(var0.viewAddress + (long)var2, var1);
   }

   public static byte bget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getByte(var0);
   }

   public static byte bget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getByte(var0.viewAddress + (long)var1);
   }

   public static void bvput(byte var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putByteVolatile((Object)null, var1, var0);
   }

   public static void bvput(MappedObject var0, byte var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putByteVolatile((Object)null, var0.viewAddress + (long)var2, var1);
   }

   public static byte bvget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getByteVolatile((Object)null, var0);
   }

   public static byte bvget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getByteVolatile((Object)null, var0.viewAddress + (long)var1);
   }

   public static void sput(short var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putShort(var1, var0);
   }

   public static void sput(MappedObject var0, short var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putShort(var0.viewAddress + (long)var2, var1);
   }

   public static short sget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getShort(var0);
   }

   public static short sget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getShort(var0.viewAddress + (long)var1);
   }

   public static void svput(short var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putShortVolatile((Object)null, var1, var0);
   }

   public static void svput(MappedObject var0, short var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putShortVolatile((Object)null, var0.viewAddress + (long)var2, var1);
   }

   public static short svget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getShortVolatile((Object)null, var0);
   }

   public static short svget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getShortVolatile((Object)null, var0.viewAddress + (long)var1);
   }

   public static void cput(char var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putChar(var1, var0);
   }

   public static void cput(MappedObject var0, char var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putChar(var0.viewAddress + (long)var2, var1);
   }

   public static char cget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getChar(var0);
   }

   public static char cget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getChar(var0.viewAddress + (long)var1);
   }

   public static void cvput(char var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putCharVolatile((Object)null, var1, var0);
   }

   public static void cvput(MappedObject var0, char var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putCharVolatile((Object)null, var0.viewAddress + (long)var2, var1);
   }

   public static char cvget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getCharVolatile((Object)null, var0);
   }

   public static char cvget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getCharVolatile((Object)null, var0.viewAddress + (long)var1);
   }

   public static void iput(int var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putInt(var1, var0);
   }

   public static void iput(MappedObject var0, int var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putInt(var0.viewAddress + (long)var2, var1);
   }

   public static int iget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getInt(var0);
   }

   public static int iget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getInt(var0.viewAddress + (long)var1);
   }

   public static void ivput(int var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putIntVolatile((Object)null, var1, var0);
   }

   public static void ivput(MappedObject var0, int var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putIntVolatile((Object)null, var0.viewAddress + (long)var2, var1);
   }

   public static int ivget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getIntVolatile((Object)null, var0);
   }

   public static int ivget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getIntVolatile((Object)null, var0.viewAddress + (long)var1);
   }

   public static void fput(float var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putFloat(var1, var0);
   }

   public static void fput(MappedObject var0, float var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putFloat(var0.viewAddress + (long)var2, var1);
   }

   public static float fget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getFloat(var0);
   }

   public static float fget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getFloat(var0.viewAddress + (long)var1);
   }

   public static void fvput(float var0, long var1) {
      MappedObjectUnsafe.INSTANCE.putFloatVolatile((Object)null, var1, var0);
   }

   public static void fvput(MappedObject var0, float var1, int var2) {
      MappedObjectUnsafe.INSTANCE.putFloatVolatile((Object)null, var0.viewAddress + (long)var2, var1);
   }

   public static float fvget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getFloatVolatile((Object)null, var0);
   }

   public static float fvget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getFloatVolatile((Object)null, var0.viewAddress + (long)var1);
   }

   public static void jput(long var0, long var2) {
      MappedObjectUnsafe.INSTANCE.putLong(var2, var0);
   }

   public static void jput(MappedObject var0, long var1, int var3) {
      MappedObjectUnsafe.INSTANCE.putLong(var0.viewAddress + (long)var3, var1);
   }

   public static long jget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getLong(var0);
   }

   public static long jget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getLong(var0.viewAddress + (long)var1);
   }

   public static void jvput(long var0, long var2) {
      MappedObjectUnsafe.INSTANCE.putLongVolatile((Object)null, var2, var0);
   }

   public static void jvput(MappedObject var0, long var1, int var3) {
      MappedObjectUnsafe.INSTANCE.putLongVolatile((Object)null, var0.viewAddress + (long)var3, var1);
   }

   public static long jvget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getLongVolatile((Object)null, var0);
   }

   public static long jvget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getLongVolatile((Object)null, var0.viewAddress + (long)var1);
   }

   public static void aput(long var0, long var2) {
      MappedObjectUnsafe.INSTANCE.putAddress(var2, var0);
   }

   public static void aput(MappedObject var0, long var1, int var3) {
      MappedObjectUnsafe.INSTANCE.putAddress(var0.viewAddress + (long)var3, var1);
   }

   public static long aget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getAddress(var0);
   }

   public static long aget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getAddress(var0.viewAddress + (long)var1);
   }

   public static void dput(double var0, long var2) {
      MappedObjectUnsafe.INSTANCE.putDouble(var2, var0);
   }

   public static void dput(MappedObject var0, double var1, int var3) {
      MappedObjectUnsafe.INSTANCE.putDouble(var0.viewAddress + (long)var3, var1);
   }

   public static double dget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getDouble(var0);
   }

   public static double dget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getDouble(var0.viewAddress + (long)var1);
   }

   public static void dvput(double var0, long var2) {
      MappedObjectUnsafe.INSTANCE.putDoubleVolatile((Object)null, var2, var0);
   }

   public static void dvput(MappedObject var0, double var1, int var3) {
      MappedObjectUnsafe.INSTANCE.putDoubleVolatile((Object)null, var0.viewAddress + (long)var3, var1);
   }

   public static double dvget(long var0) {
      return MappedObjectUnsafe.INSTANCE.getDoubleVolatile((Object)null, var0);
   }

   public static double dvget(MappedObject var0, int var1) {
      return MappedObjectUnsafe.INSTANCE.getDoubleVolatile((Object)null, var0.viewAddress + (long)var1);
   }
}
