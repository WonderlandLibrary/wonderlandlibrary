package org.lwjgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public final class BufferUtils {
   public static ByteBuffer createByteBuffer(int var0) {
      return ByteBuffer.allocateDirect(var0).order(ByteOrder.nativeOrder());
   }

   public static ShortBuffer createShortBuffer(int var0) {
      return createByteBuffer(var0 << 1).asShortBuffer();
   }

   public static CharBuffer createCharBuffer(int var0) {
      return createByteBuffer(var0 << 1).asCharBuffer();
   }

   public static IntBuffer createIntBuffer(int var0) {
      return createByteBuffer(var0 << 2).asIntBuffer();
   }

   public static LongBuffer createLongBuffer(int var0) {
      return createByteBuffer(var0 << 3).asLongBuffer();
   }

   public static FloatBuffer createFloatBuffer(int var0) {
      return createByteBuffer(var0 << 2).asFloatBuffer();
   }

   public static DoubleBuffer createDoubleBuffer(int var0) {
      return createByteBuffer(var0 << 3).asDoubleBuffer();
   }

   public static PointerBuffer createPointerBuffer(int var0) {
      return PointerBuffer.allocateDirect(var0);
   }

   public static int getElementSizeExponent(Buffer var0) {
      if (var0 instanceof ByteBuffer) {
         return 0;
      } else if (!(var0 instanceof ShortBuffer) && !(var0 instanceof CharBuffer)) {
         if (!(var0 instanceof FloatBuffer) && !(var0 instanceof IntBuffer)) {
            if (!(var0 instanceof LongBuffer) && !(var0 instanceof DoubleBuffer)) {
               throw new IllegalStateException("Unsupported buffer type: " + var0);
            } else {
               return 3;
            }
         } else {
            return 2;
         }
      } else {
         return 1;
      }
   }

   public static int getOffset(Buffer var0) {
      return var0.position() << getElementSizeExponent(var0);
   }

   public static void zeroBuffer(ByteBuffer var0) {
      zeroBuffer0(var0, (long)var0.position(), (long)var0.remaining());
   }

   public static void zeroBuffer(ShortBuffer var0) {
      zeroBuffer0(var0, (long)var0.position() * 2L, (long)var0.remaining() * 2L);
   }

   public static void zeroBuffer(CharBuffer var0) {
      zeroBuffer0(var0, (long)var0.position() * 2L, (long)var0.remaining() * 2L);
   }

   public static void zeroBuffer(IntBuffer var0) {
      zeroBuffer0(var0, (long)var0.position() * 4L, (long)var0.remaining() * 4L);
   }

   public static void zeroBuffer(FloatBuffer var0) {
      zeroBuffer0(var0, (long)var0.position() * 4L, (long)var0.remaining() * 4L);
   }

   public static void zeroBuffer(LongBuffer var0) {
      zeroBuffer0(var0, (long)var0.position() * 8L, (long)var0.remaining() * 8L);
   }

   public static void zeroBuffer(DoubleBuffer var0) {
      zeroBuffer0(var0, (long)var0.position() * 8L, (long)var0.remaining() * 8L);
   }

   private static native void zeroBuffer0(Buffer var0, long var1, long var3);

   static native long getBufferAddress(Buffer var0);
}
