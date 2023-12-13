package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVFragmentProgram extends NVProgram {
   public static final int GL_FRAGMENT_PROGRAM_NV = 34928;
   public static final int GL_MAX_TEXTURE_COORDS_NV = 34929;
   public static final int GL_MAX_TEXTURE_IMAGE_UNITS_NV = 34930;
   public static final int GL_FRAGMENT_PROGRAM_BINDING_NV = 34931;
   public static final int GL_MAX_FRAGMENT_PROGRAM_LOCAL_PARAMETERS_NV = 34920;

   private NVFragmentProgram() {
   }

   public static void glProgramNamedParameter4fNV(int var0, ByteBuffer var1, float var2, float var3, float var4, float var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glProgramNamedParameter4fNV;
      BufferChecks.checkFunctionAddress(var7);
      BufferChecks.checkDirect(var1);
      nglProgramNamedParameter4fNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var2, var3, var4, var5, var7);
   }

   static native void nglProgramNamedParameter4fNV(int var0, int var1, long var2, float var4, float var5, float var6, float var7, long var8);

   public static void glProgramNamedParameter4dNV(int var0, ByteBuffer var1, double var2, double var4, double var6, double var8) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glProgramNamedParameter4dNV;
      BufferChecks.checkFunctionAddress(var11);
      BufferChecks.checkDirect(var1);
      nglProgramNamedParameter4dNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), var2, var4, var6, var8, var11);
   }

   static native void nglProgramNamedParameter4dNV(int var0, int var1, long var2, double var4, double var6, double var8, double var10, long var12);

   public static void glGetProgramNamedParameterNV(int var0, ByteBuffer var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramNamedParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetProgramNamedParameterfvNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramNamedParameterfvNV(int var0, int var1, long var2, long var4, long var6);

   public static void glGetProgramNamedParameterNV(int var0, ByteBuffer var1, DoubleBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetProgramNamedParameterdvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkBuffer((DoubleBuffer)var2, 4);
      nglGetProgramNamedParameterdvNV(var0, var1.remaining(), MemoryUtil.getAddress(var1), MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetProgramNamedParameterdvNV(int var0, int var1, long var2, long var4, long var6);
}
