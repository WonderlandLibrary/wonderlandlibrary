package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVGpuShader5 {
   public static final int GL_INT64_NV = 5134;
   public static final int GL_UNSIGNED_INT64_NV = 5135;
   public static final int GL_INT8_NV = 36832;
   public static final int GL_INT8_VEC2_NV = 36833;
   public static final int GL_INT8_VEC3_NV = 36834;
   public static final int GL_INT8_VEC4_NV = 36835;
   public static final int GL_INT16_NV = 36836;
   public static final int GL_INT16_VEC2_NV = 36837;
   public static final int GL_INT16_VEC3_NV = 36838;
   public static final int GL_INT16_VEC4_NV = 36839;
   public static final int GL_INT64_VEC2_NV = 36841;
   public static final int GL_INT64_VEC3_NV = 36842;
   public static final int GL_INT64_VEC4_NV = 36843;
   public static final int GL_UNSIGNED_INT8_NV = 36844;
   public static final int GL_UNSIGNED_INT8_VEC2_NV = 36845;
   public static final int GL_UNSIGNED_INT8_VEC3_NV = 36846;
   public static final int GL_UNSIGNED_INT8_VEC4_NV = 36847;
   public static final int GL_UNSIGNED_INT16_NV = 36848;
   public static final int GL_UNSIGNED_INT16_VEC2_NV = 36849;
   public static final int GL_UNSIGNED_INT16_VEC3_NV = 36850;
   public static final int GL_UNSIGNED_INT16_VEC4_NV = 36851;
   public static final int GL_UNSIGNED_INT64_VEC2_NV = 36853;
   public static final int GL_UNSIGNED_INT64_VEC3_NV = 36854;
   public static final int GL_UNSIGNED_INT64_VEC4_NV = 36855;
   public static final int GL_FLOAT16_NV = 36856;
   public static final int GL_FLOAT16_VEC2_NV = 36857;
   public static final int GL_FLOAT16_VEC3_NV = 36858;
   public static final int GL_FLOAT16_VEC4_NV = 36859;
   public static final int GL_PATCHES = 14;

   private NVGpuShader5() {
   }

   public static void glUniform1i64NV(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform1i64NV;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform1i64NV(var0, var1, var4);
   }

   static native void nglUniform1i64NV(int var0, long var1, long var3);

   public static void glUniform2i64NV(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform2i64NV;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform2i64NV(var0, var1, var3, var6);
   }

   static native void nglUniform2i64NV(int var0, long var1, long var3, long var5);

   public static void glUniform3i64NV(int var0, long var1, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glUniform3i64NV;
      BufferChecks.checkFunctionAddress(var8);
      nglUniform3i64NV(var0, var1, var3, var5, var8);
   }

   static native void nglUniform3i64NV(int var0, long var1, long var3, long var5, long var7);

   public static void glUniform4i64NV(int var0, long var1, long var3, long var5, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glUniform4i64NV;
      BufferChecks.checkFunctionAddress(var10);
      nglUniform4i64NV(var0, var1, var3, var5, var7, var10);
   }

   static native void nglUniform4i64NV(int var0, long var1, long var3, long var5, long var7, long var9);

   public static void glUniform1NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1i64vNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1i64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform2NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2i64vNV(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2i64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform3NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3i64vNV(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3i64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform4NV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4i64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4i64vNV(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4i64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform1ui64NV(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniform1ui64NV;
      BufferChecks.checkFunctionAddress(var4);
      nglUniform1ui64NV(var0, var1, var4);
   }

   static native void nglUniform1ui64NV(int var0, long var1, long var3);

   public static void glUniform2ui64NV(int var0, long var1, long var3) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glUniform2ui64NV;
      BufferChecks.checkFunctionAddress(var6);
      nglUniform2ui64NV(var0, var1, var3, var6);
   }

   static native void nglUniform2ui64NV(int var0, long var1, long var3, long var5);

   public static void glUniform3ui64NV(int var0, long var1, long var3, long var5) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glUniform3ui64NV;
      BufferChecks.checkFunctionAddress(var8);
      nglUniform3ui64NV(var0, var1, var3, var5, var8);
   }

   static native void nglUniform3ui64NV(int var0, long var1, long var3, long var5, long var7);

   public static void glUniform4ui64NV(int var0, long var1, long var3, long var5, long var7) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glUniform4ui64NV;
      BufferChecks.checkFunctionAddress(var10);
      nglUniform4ui64NV(var0, var1, var3, var5, var7, var10);
   }

   static native void nglUniform4ui64NV(int var0, long var1, long var3, long var5, long var7, long var9);

   public static void glUniform1uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform1ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform1ui64vNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform1ui64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform2uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform2ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform2ui64vNV(var0, var1.remaining() >> 1, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform2ui64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform3uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform3ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform3ui64vNV(var0, var1.remaining() / 3, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform3ui64vNV(int var0, int var1, long var2, long var4);

   public static void glUniform4uNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniform4ui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniform4ui64vNV(var0, var1.remaining() >> 2, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniform4ui64vNV(int var0, int var1, long var2, long var4);

   public static void glGetUniformNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformi64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetUniformi64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformi64vNV(int var0, int var1, long var2, long var4);

   public static void glGetUniformuNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetUniformui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetUniformui64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetUniformui64vNV(int var0, int var1, long var2, long var4);

   public static void glProgramUniform1i64NV(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform1i64NV;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform1i64NV(var0, var1, var2, var5);
   }

   static native void nglProgramUniform1i64NV(int var0, int var1, long var2, long var4);

   public static void glProgramUniform2i64NV(int var0, int var1, long var2, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform2i64NV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform2i64NV(var0, var1, var2, var4, var7);
   }

   static native void nglProgramUniform2i64NV(int var0, int var1, long var2, long var4, long var6);

   public static void glProgramUniform3i64NV(int var0, int var1, long var2, long var4, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glProgramUniform3i64NV;
      BufferChecks.checkFunctionAddress(var9);
      nglProgramUniform3i64NV(var0, var1, var2, var4, var6, var9);
   }

   static native void nglProgramUniform3i64NV(int var0, int var1, long var2, long var4, long var6, long var8);

   public static void glProgramUniform4i64NV(int var0, int var1, long var2, long var4, long var6, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramUniform4i64NV;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramUniform4i64NV(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramUniform4i64NV(int var0, int var1, long var2, long var4, long var6, long var8, long var10);

   public static void glProgramUniform1NV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1i64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1i64vNV(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1i64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2NV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2i64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2i64vNV(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2i64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3NV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3i64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3i64vNV(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3i64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4NV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4i64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4i64vNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4i64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform1ui64NV(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform1ui64NV;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform1ui64NV(var0, var1, var2, var5);
   }

   static native void nglProgramUniform1ui64NV(int var0, int var1, long var2, long var4);

   public static void glProgramUniform2ui64NV(int var0, int var1, long var2, long var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform2ui64NV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform2ui64NV(var0, var1, var2, var4, var7);
   }

   static native void nglProgramUniform2ui64NV(int var0, int var1, long var2, long var4, long var6);

   public static void glProgramUniform3ui64NV(int var0, int var1, long var2, long var4, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glProgramUniform3ui64NV;
      BufferChecks.checkFunctionAddress(var9);
      nglProgramUniform3ui64NV(var0, var1, var2, var4, var6, var9);
   }

   static native void nglProgramUniform3ui64NV(int var0, int var1, long var2, long var4, long var6, long var8);

   public static void glProgramUniform4ui64NV(int var0, int var1, long var2, long var4, long var6, long var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramUniform4ui64NV;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramUniform4ui64NV(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramUniform4ui64NV(int var0, int var1, long var2, long var4, long var6, long var8, long var10);

   public static void glProgramUniform1uNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1ui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1ui64vNV(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1ui64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2uNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2ui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2ui64vNV(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2ui64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3uNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3ui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3ui64vNV(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3ui64vNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4uNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4ui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4ui64vNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4ui64vNV(int var0, int var1, int var2, long var3, long var5);
}
