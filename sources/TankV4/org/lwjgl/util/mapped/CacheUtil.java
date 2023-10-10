package org.lwjgl.util.mapped;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CacheUtil {
   private static final int CACHE_LINE_SIZE;

   private CacheUtil() {
   }

   public static int getCacheLineSize() {
      return CACHE_LINE_SIZE;
   }

   public static ByteBuffer createByteBuffer(int var0) {
      ByteBuffer var1 = ByteBuffer.allocateDirect(var0 + CACHE_LINE_SIZE);
      if (MemoryUtil.getAddress(var1) % (long)CACHE_LINE_SIZE != 0L) {
         var1.position(CACHE_LINE_SIZE - (int)(MemoryUtil.getAddress(var1) & (long)(CACHE_LINE_SIZE - 1)));
      }

      var1.limit(var1.position() + var0);
      return var1.slice().order(ByteOrder.nativeOrder());
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
      return new PointerBuffer(createByteBuffer(var0 * PointerBuffer.getPointerSize()));
   }

   static {
      Integer var0 = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineSize");
      if (var0 != null) {
         if (var0 < 1) {
            throw new IllegalStateException("Invalid CacheLineSize specified: " + var0);
         }

         CACHE_LINE_SIZE = var0;
      } else if (Runtime.getRuntime().availableProcessors() == 1) {
         if (LWJGLUtil.DEBUG) {
            LWJGLUtil.log("Cannot detect cache line size on single-core CPUs, assuming 64 bytes.");
         }

         CACHE_LINE_SIZE = 64;
      } else {
         CACHE_LINE_SIZE = CacheLineSize.getCacheLineSize();
      }

   }
}
