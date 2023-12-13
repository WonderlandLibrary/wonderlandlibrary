package org.lwjgl.opengl;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBGpuShaderFp64 {
   public static final int GL_DOUBLE = 5130;
   public static final int GL_DOUBLE_VEC2 = 36860;
   public static final int GL_DOUBLE_VEC3 = 36861;
   public static final int GL_DOUBLE_VEC4 = 36862;
   public static final int GL_DOUBLE_MAT2 = 36678;
   public static final int GL_DOUBLE_MAT3 = 36679;
   public static final int GL_DOUBLE_MAT4 = 36680;
   public static final int GL_DOUBLE_MAT2x3 = 36681;
   public static final int GL_DOUBLE_MAT2x4 = 36682;
   public static final int GL_DOUBLE_MAT3x2 = 36683;
   public static final int GL_DOUBLE_MAT3x4 = 36684;
   public static final int GL_DOUBLE_MAT4x2 = 36685;
   public static final int GL_DOUBLE_MAT4x3 = 36686;

   private ARBGpuShaderFp64() {
   }

   public static void glUniform1d(int var0, double var1) {
      GL40.glUniform1d(var0, var1);
   }

   public static void glUniform2d(int var0, double var1, double var3) {
      GL40.glUniform2d(var0, var1, var3);
   }

   public static void glUniform3d(int var0, double var1, double var3, double var5) {
      GL40.glUniform3d(var0, var1, var3, var5);
   }

   public static void glUniform4d(int var0, double var1, double var3, double var5, double var7) {
      GL40.glUniform4d(var0, var1, var3, var5, var7);
   }

   public static void glUniform1(int var0, DoubleBuffer var1) {
      GL40.glUniform1(var0, var1);
   }

   public static void glUniform2(int var0, DoubleBuffer var1) {
      GL40.glUniform2(var0, var1);
   }

   public static void glUniform3(int var0, DoubleBuffer var1) {
      GL40.glUniform3(var0, var1);
   }

   public static void glUniform4(int var0, DoubleBuffer var1) {
      GL40.glUniform4(var0, var1);
   }

   public static void glUniformMatrix2(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix2(var0, var1, var2);
   }

   public static void glUniformMatrix3(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix3(var0, var1, var2);
   }

   public static void glUniformMatrix4(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix4(var0, var1, var2);
   }

   public static void glUniformMatrix2x3(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix2x3(var0, var1, var2);
   }

   public static void glUniformMatrix2x4(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix2x4(var0, var1, var2);
   }

   public static void glUniformMatrix3x2(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix3x2(var0, var1, var2);
   }

   public static void glUniformMatrix3x4(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix3x4(var0, var1, var2);
   }

   public static void glUniformMatrix4x2(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix4x2(var0, var1, var2);
   }

   public static void glUniformMatrix4x3(int var0, boolean var1, DoubleBuffer var2) {
      GL40.glUniformMatrix4x3(var0, var1, var2);
   }

   public static void glGetUniform(int var0, int var1, DoubleBuffer var2) {
      GL40.glGetUniform(var0, var1, var2);
   }

   public static void glProgramUniform1dEXT(int var0, int var1, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniform1dEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniform1dEXT(var0, var1, var2, var5);
   }

   static native void nglProgramUniform1dEXT(int var0, int var1, double var2, long var4);

   public static void glProgramUniform2dEXT(int var0, int var1, double var2, double var4) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramUniform2dEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramUniform2dEXT(var0, var1, var2, var4, var7);
   }

   static native void nglProgramUniform2dEXT(int var0, int var1, double var2, double var4, long var6);

   public static void glProgramUniform3dEXT(int var0, int var1, double var2, double var4, double var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glProgramUniform3dEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglProgramUniform3dEXT(var0, var1, var2, var4, var6, var9);
   }

   static native void nglProgramUniform3dEXT(int var0, int var1, double var2, double var4, double var6, long var8);

   public static void glProgramUniform4dEXT(int var0, int var1, double var2, double var4, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramUniform4dEXT;
      BufferChecks.checkFunctionAddress(var11);
      nglProgramUniform4dEXT(var0, var1, var2, var4, var6, var8, var11);
   }

   static native void nglProgramUniform4dEXT(int var0, int var1, double var2, double var4, double var6, double var8, long var10);

   public static void glProgramUniform1EXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform1dvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform1dvEXT(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform1dvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform2EXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform2dvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform2dvEXT(var0, var1, var2.remaining() >> 1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform2dvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform3EXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform3dvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform3dvEXT(var0, var1, var2.remaining() / 3, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform3dvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniform4EXT(int var0, int var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniform4dvEXT;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniform4dvEXT(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniform4dvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramUniformMatrix2EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2dvEXT(var0, var1, var3.remaining() >> 2, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3dvEXT(var0, var1, var3.remaining() / 9, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4dvEXT(var0, var1, var3.remaining() >> 4, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x3EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x3dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x3dvEXT(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x3dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix2x4EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix2x4dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix2x4dvEXT(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix2x4dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x2EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x2dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x2dvEXT(var0, var1, var3.remaining() / 6, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x2dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix3x4EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix3x4dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix3x4dvEXT(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix3x4dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x2EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x2dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x2dvEXT(var0, var1, var3.remaining() >> 3, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x2dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);

   public static void glProgramUniformMatrix4x3EXT(int var0, int var1, boolean var2, DoubleBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformMatrix4x3dvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramUniformMatrix4x3dvEXT(var0, var1, var3.remaining() / 12, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramUniformMatrix4x3dvEXT(int var0, int var1, int var2, boolean var3, long var4, long var6);
}
