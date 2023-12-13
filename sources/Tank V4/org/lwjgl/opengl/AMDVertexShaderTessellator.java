package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDVertexShaderTessellator {
   public static final int GL_SAMPLER_BUFFER_AMD = 36865;
   public static final int GL_INT_SAMPLER_BUFFER_AMD = 36866;
   public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER_AMD = 36867;
   public static final int GL_DISCRETE_AMD = 36870;
   public static final int GL_CONTINUOUS_AMD = 36871;
   public static final int GL_TESSELLATION_MODE_AMD = 36868;
   public static final int GL_TESSELLATION_FACTOR_AMD = 36869;

   private AMDVertexShaderTessellator() {
   }

   public static void glTessellationFactorAMD(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glTessellationFactorAMD;
      BufferChecks.checkFunctionAddress(var2);
      nglTessellationFactorAMD(var0, var2);
   }

   static native void nglTessellationFactorAMD(float var0, long var1);

   public static void glTessellationModeAMD(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glTessellationModeAMD;
      BufferChecks.checkFunctionAddress(var2);
      nglTessellationModeAMD(var0, var2);
   }

   static native void nglTessellationModeAMD(int var0, long var1);
}
