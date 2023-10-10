package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVGpuProgram4 {
   public static final int GL_PROGRAM_ATTRIB_COMPONENTS_NV = 35078;
   public static final int GL_PROGRAM_RESULT_COMPONENTS_NV = 35079;
   public static final int GL_MAX_PROGRAM_ATTRIB_COMPONENTS_NV = 35080;
   public static final int GL_MAX_PROGRAM_RESULT_COMPONENTS_NV = 35081;
   public static final int GL_MAX_PROGRAM_GENERIC_ATTRIBS_NV = 36261;
   public static final int GL_MAX_PROGRAM_GENERIC_RESULTS_NV = 36262;

   private NVGpuProgram4() {
   }

   public static void glProgramLocalParameterI4iNV(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramLocalParameterI4iNV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramLocalParameterI4iNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramLocalParameterI4iNV(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramLocalParameterI4NV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramLocalParameterI4ivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglProgramLocalParameterI4ivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramLocalParameterI4ivNV(int var0, int var1, long var2, long var4);

   public static void glProgramLocalParametersI4NV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramLocalParametersI4ivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramLocalParametersI4ivNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramLocalParametersI4ivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramLocalParameterI4uiNV(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramLocalParameterI4uiNV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramLocalParameterI4uiNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramLocalParameterI4uiNV(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramLocalParameterI4uNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramLocalParameterI4uivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglProgramLocalParameterI4uivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramLocalParameterI4uivNV(int var0, int var1, long var2, long var4);

   public static void glProgramLocalParametersI4uNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramLocalParametersI4uivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramLocalParametersI4uivNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramLocalParametersI4uivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramEnvParameterI4iNV(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramEnvParameterI4iNV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramEnvParameterI4iNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramEnvParameterI4iNV(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramEnvParameterI4NV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramEnvParameterI4ivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglProgramEnvParameterI4ivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramEnvParameterI4ivNV(int var0, int var1, long var2, long var4);

   public static void glProgramEnvParametersI4NV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramEnvParametersI4ivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramEnvParametersI4ivNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramEnvParametersI4ivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramEnvParameterI4uiNV(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramEnvParameterI4uiNV;
      BufferChecks.checkFunctionAddress(var7);
      nglProgramEnvParameterI4uiNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglProgramEnvParameterI4uiNV(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glProgramEnvParameterI4uNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramEnvParameterI4uivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglProgramEnvParameterI4uivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramEnvParameterI4uivNV(int var0, int var1, long var2, long var4);

   public static void glProgramEnvParametersI4uNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramEnvParametersI4uivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramEnvParametersI4uivNV(var0, var1, var2.remaining() >> 2, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramEnvParametersI4uivNV(int var0, int var1, int var2, long var3, long var5);

   public static void glGetProgramLocalParameterINV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramLocalParameterIivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetProgramLocalParameterIivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramLocalParameterIivNV(int var0, int var1, long var2, long var4);

   public static void glGetProgramLocalParameterIuNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramLocalParameterIuivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetProgramLocalParameterIuivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramLocalParameterIuivNV(int var0, int var1, long var2, long var4);

   public static void glGetProgramEnvParameterINV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramEnvParameterIivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetProgramEnvParameterIivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramEnvParameterIivNV(int var0, int var1, long var2, long var4);

   public static void glGetProgramEnvParameterIuNV(int var0, int var1, IntBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramEnvParameterIuivNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((IntBuffer)var2, 4);
      nglGetProgramEnvParameterIuivNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramEnvParameterIuivNV(int var0, int var1, long var2, long var4);
}
