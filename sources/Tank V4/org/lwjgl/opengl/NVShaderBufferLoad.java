package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVShaderBufferLoad {
   public static final int GL_BUFFER_GPU_ADDRESS_NV = 36637;
   public static final int GL_GPU_ADDRESS_NV = 36660;
   public static final int GL_MAX_SHADER_BUFFER_ADDRESS_NV = 36661;

   private NVShaderBufferLoad() {
   }

   public static void glMakeBufferResidentNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeBufferResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeBufferResidentNV(var0, var1, var3);
   }

   static native void nglMakeBufferResidentNV(int var0, int var1, long var2);

   public static void glMakeBufferNonResidentNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMakeBufferNonResidentNV;
      BufferChecks.checkFunctionAddress(var2);
      nglMakeBufferNonResidentNV(var0, var2);
   }

   static native void nglMakeBufferNonResidentNV(int var0, long var1);

   public static boolean glIsBufferResidentNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsBufferResidentNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsBufferResidentNV(var0, var2);
      return var4;
   }

   static native boolean nglIsBufferResidentNV(int var0, long var1);

   public static void glMakeNamedBufferResidentNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glMakeNamedBufferResidentNV;
      BufferChecks.checkFunctionAddress(var3);
      nglMakeNamedBufferResidentNV(var0, var1, var3);
   }

   static native void nglMakeNamedBufferResidentNV(int var0, int var1, long var2);

   public static void glMakeNamedBufferNonResidentNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMakeNamedBufferNonResidentNV;
      BufferChecks.checkFunctionAddress(var2);
      nglMakeNamedBufferNonResidentNV(var0, var2);
   }

   static native void nglMakeNamedBufferNonResidentNV(int var0, long var1);

   public static boolean glIsNamedBufferResidentNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glIsNamedBufferResidentNV;
      BufferChecks.checkFunctionAddress(var2);
      boolean var4 = nglIsNamedBufferResidentNV(var0, var2);
      return var4;
   }

   static native boolean nglIsNamedBufferResidentNV(int var0, long var1);

   public static void glGetBufferParameteruNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetBufferParameterui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetBufferParameterui64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetBufferParameterui64vNV(int var0, int var1, long var2, long var4);

   public static long glGetBufferParameterui64NV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetBufferParameterui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetBufferParameterui64vNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetNamedBufferParameteruNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetNamedBufferParameterui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((LongBuffer)var2, 1);
      nglGetNamedBufferParameterui64vNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetNamedBufferParameterui64vNV(int var0, int var1, long var2, long var4);

   public static long glGetNamedBufferParameterui64NV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetNamedBufferParameterui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      LongBuffer var5 = APIUtil.getBufferLong(var2);
      nglGetNamedBufferParameterui64vNV(var0, var1, MemoryUtil.getAddress(var5), var3);
      return var5.get(0);
   }

   public static void glGetIntegeruNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glGetIntegerui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkBuffer((LongBuffer)var1, 1);
      nglGetIntegerui64vNV(var0, MemoryUtil.getAddress(var1), var3);
   }

   static native void nglGetIntegerui64vNV(int var0, long var1, long var3);

   public static long glGetIntegerui64NV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glGetIntegerui64vNV;
      BufferChecks.checkFunctionAddress(var2);
      LongBuffer var4 = APIUtil.getBufferLong(var1);
      nglGetIntegerui64vNV(var0, MemoryUtil.getAddress(var4), var2);
      return var4.get(0);
   }

   public static void glUniformui64NV(int var0, long var1) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glUniformui64NV;
      BufferChecks.checkFunctionAddress(var4);
      nglUniformui64NV(var0, var1, var4);
   }

   static native void nglUniformui64NV(int var0, long var1, long var3);

   public static void glUniformuNV(int var0, LongBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUniformui64vNV;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      nglUniformui64vNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var3);
   }

   static native void nglUniformui64vNV(int var0, int var1, long var2, long var4);

   public static void glGetUniformuNV(int var0, int var1, LongBuffer var2) {
      NVGpuShader5.glGetUniformuNV(var0, var1, var2);
   }

   public static void glProgramUniformui64NV(int var0, int var1, long var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramUniformui64NV;
      BufferChecks.checkFunctionAddress(var5);
      nglProgramUniformui64NV(var0, var1, var2, var5);
   }

   static native void nglProgramUniformui64NV(int var0, int var1, long var2, long var4);

   public static void glProgramUniformuNV(int var0, int var1, LongBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glProgramUniformui64vNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var2);
      nglProgramUniformui64vNV(var0, var1, var2.remaining(), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglProgramUniformui64vNV(int var0, int var1, int var2, long var3, long var5);
}
