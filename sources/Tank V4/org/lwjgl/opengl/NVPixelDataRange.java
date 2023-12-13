package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVPixelDataRange {
   public static final int GL_WRITE_PIXEL_DATA_RANGE_NV = 34936;
   public static final int GL_READ_PIXEL_DATA_RANGE_NV = 34937;
   public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV = 34938;
   public static final int GL_READ_PIXEL_DATA_RANGE_LENGTH_NV = 34939;
   public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV = 34940;
   public static final int GL_READ_PIXEL_DATA_RANGE_POINTER_NV = 34941;

   private NVPixelDataRange() {
   }

   public static void glPixelDataRangeNV(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelDataRangeNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglPixelDataRangeNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   public static void glPixelDataRangeNV(int var0, DoubleBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelDataRangeNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglPixelDataRangeNV(var0, var1.remaining() << 3, MemoryUtil.getAddress(var1), var3);
   }

   public static void glPixelDataRangeNV(int var0, FloatBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelDataRangeNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglPixelDataRangeNV(var0, var1.remaining() << 2, MemoryUtil.getAddress(var1), var3);
   }

   public static void glPixelDataRangeNV(int var0, IntBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelDataRangeNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglPixelDataRangeNV(var0, var1.remaining() << 2, MemoryUtil.getAddress(var1), var3);
   }

   public static void glPixelDataRangeNV(int var0, ShortBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glPixelDataRangeNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglPixelDataRangeNV(var0, var1.remaining() << 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglPixelDataRangeNV(int var0, int var1, long var2, long var4);

   public static void glFlushPixelDataRangeNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFlushPixelDataRangeNV;
      BufferChecks.checkFunctionAddress(var2);
      nglFlushPixelDataRangeNV(var0, var2);
   }

   static native void nglFlushPixelDataRangeNV(int var0, long var1);
}
