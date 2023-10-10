package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVParameterBufferObject {
   public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 36256;
   public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 36257;
   public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 36258;
   public static final int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 36259;
   public static final int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 36260;

   private NVParameterBufferObject() {
   }

   public static void glProgramBufferParametersNV(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramBufferParametersfvNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramBufferParametersfvNV(var0, var1, var2, var3.remaining() >> 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramBufferParametersfvNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glProgramBufferParametersINV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramBufferParametersIivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramBufferParametersIivNV(var0, var1, var2, var3.remaining() >> 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramBufferParametersIivNV(int var0, int var1, int var2, int var3, long var4, long var6);

   public static void glProgramBufferParametersIuNV(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramBufferParametersIuivNV;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglProgramBufferParametersIuivNV(var0, var1, var2, var3.remaining() >> 2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramBufferParametersIuivNV(int var0, int var1, int var2, int var3, long var4, long var6);
}
