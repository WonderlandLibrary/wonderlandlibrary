package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class NVVertexArrayRange {
   public static final int GL_VERTEX_ARRAY_RANGE_NV = 34077;
   public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_NV = 34078;
   public static final int GL_VERTEX_ARRAY_RANGE_VALID_NV = 34079;
   public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV = 34080;
   public static final int GL_VERTEX_ARRAY_RANGE_POINTER_NV = 34081;

   private NVVertexArrayRange() {
   }

   public static void glVertexArrayRangeNV(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexArrayRangeNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglVertexArrayRangeNV(var0.remaining(), MemoryUtil.getAddress(var0), var2);
   }

   public static void glVertexArrayRangeNV(DoubleBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexArrayRangeNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglVertexArrayRangeNV(var0.remaining() << 3, MemoryUtil.getAddress(var0), var2);
   }

   public static void glVertexArrayRangeNV(FloatBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexArrayRangeNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglVertexArrayRangeNV(var0.remaining() << 2, MemoryUtil.getAddress(var0), var2);
   }

   public static void glVertexArrayRangeNV(IntBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexArrayRangeNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglVertexArrayRangeNV(var0.remaining() << 2, MemoryUtil.getAddress(var0), var2);
   }

   public static void glVertexArrayRangeNV(ShortBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glVertexArrayRangeNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglVertexArrayRangeNV(var0.remaining() << 1, MemoryUtil.getAddress(var0), var2);
   }

   static native void nglVertexArrayRangeNV(int var0, long var1, long var3);

   public static void glFlushVertexArrayRangeNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glFlushVertexArrayRangeNV;
      BufferChecks.checkFunctionAddress(var1);
      nglFlushVertexArrayRangeNV(var1);
   }

   static native void nglFlushVertexArrayRangeNV(long var0);

   public static ByteBuffer glAllocateMemoryNV(int var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glAllocateMemoryNV;
      BufferChecks.checkFunctionAddress(var5);
      ByteBuffer var7 = nglAllocateMemoryNV(var0, var1, var2, var3, (long)var0, var5);
      return LWJGLUtil.CHECKS && var7 == null ? null : var7.order(ByteOrder.nativeOrder());
   }

   static native ByteBuffer nglAllocateMemoryNV(int var0, float var1, float var2, float var3, long var4, long var6);

   public static void glFreeMemoryNV(ByteBuffer var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glFreeMemoryNV;
      BufferChecks.checkFunctionAddress(var2);
      BufferChecks.checkDirect(var0);
      nglFreeMemoryNV(MemoryUtil.getAddress(var0), var2);
   }

   static native void nglFreeMemoryNV(long var0, long var2);
}
