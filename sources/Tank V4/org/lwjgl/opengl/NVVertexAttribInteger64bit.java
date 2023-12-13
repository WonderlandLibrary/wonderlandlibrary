package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVVertexAttribInteger64bit {
   public static final int GL_INT64_NV = 5134;
   public static final int GL_UNSIGNED_INT64_NV = 5135;

   private NVVertexAttribInteger64bit() {
   }

   public static void glVertexAttribL1i64NV(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribL1i64NV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribL1i64NV(var0, var1, var4);
   }

   static native void nglVertexAttribL1i64NV(int var0, long var1, long var3);

   public static void glVertexAttribL2i64NV(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribL2i64NV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribL2i64NV(var0, var1, var3, var6);
   }

   static native void nglVertexAttribL2i64NV(int var0, long var1, long var3, long var5);

   public static void glVertexAttribL3i64NV(int var0, long var1, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribL3i64NV;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttribL3i64NV(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttribL3i64NV(int var0, long var1, long var3, long var5, long var7);

   public static void glVertexAttribL4i64NV(int var0, long var1, long var3, long var5, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttribL4i64NV;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttribL4i64NV(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttribL4i64NV(int var0, long var1, long var3, long var5, long var7, long var9);

   public static void glVertexAttribL1NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL1i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 1);
      nglVertexAttribL1i64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL1i64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL2NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL2i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 2);
      nglVertexAttribL2i64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL2i64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL3NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL3i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 3);
      nglVertexAttribL3i64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL3i64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL4NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL4i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 4);
      nglVertexAttribL4i64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL4i64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL1ui64NV(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glVertexAttribL1ui64NV;
      BufferChecks.checkFunctionAddress(var4);
      nglVertexAttribL1ui64NV(var0, var1, var4);
   }

   static native void nglVertexAttribL1ui64NV(int var0, long var1, long var3);

   public static void glVertexAttribL2ui64NV(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glVertexAttribL2ui64NV;
      BufferChecks.checkFunctionAddress(var6);
      nglVertexAttribL2ui64NV(var0, var1, var3, var6);
   }

   static native void nglVertexAttribL2ui64NV(int var0, long var1, long var3, long var5);

   public static void glVertexAttribL3ui64NV(int var0, long var1, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glVertexAttribL3ui64NV;
      BufferChecks.checkFunctionAddress(var8);
      nglVertexAttribL3ui64NV(var0, var1, var3, var5, var8);
   }

   static native void nglVertexAttribL3ui64NV(int var0, long var1, long var3, long var5, long var7);

   public static void glVertexAttribL4ui64NV(int var0, long var1, long var3, long var5, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glVertexAttribL4ui64NV;
      BufferChecks.checkFunctionAddress(var10);
      nglVertexAttribL4ui64NV(var0, var1, var3, var5, var7, var10);
   }

   static native void nglVertexAttribL4ui64NV(int var0, long var1, long var3, long var5, long var7, long var9);

   public static void glVertexAttribL1uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL1ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 1);
      nglVertexAttribL1ui64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL1ui64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL2uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL2ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 2);
      nglVertexAttribL2ui64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL2ui64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL3uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL3ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 3);
      nglVertexAttribL3ui64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL3ui64vNV(int var0, long var1, long var3);

   public static void glVertexAttribL4uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glVertexAttribL4ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 4);
      nglVertexAttribL4ui64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglVertexAttribL4ui64vNV(int var0, long var1, long var3);

   public static void glGetVertexAttribLNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribLi64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 4);
      nglGetVertexAttribLi64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribLi64vNV(int var0, int var1, long var2, long var4);

   public static void glGetVertexAttribLuNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetVertexAttribLui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 4);
      nglGetVertexAttribLui64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetVertexAttribLui64vNV(int var0, int var1, long var2, long var4);

   public static void glVertexAttribLFormatNV(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glVertexAttribLFormatNV;
      BufferChecks.checkFunctionAddress(var5);
      nglVertexAttribLFormatNV(var0, var1, var2, var3, var5);
   }

   static native void nglVertexAttribLFormatNV(int var0, int var1, int var2, int var3, long var4);
}
