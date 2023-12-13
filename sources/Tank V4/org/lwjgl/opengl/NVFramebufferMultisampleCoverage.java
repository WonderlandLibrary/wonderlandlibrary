package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVFramebufferMultisampleCoverage {
   public static final int GL_RENDERBUFFER_COVERAGE_SAMPLES_NV = 36011;
   public static final int GL_RENDERBUFFER_COLOR_SAMPLES_NV = 36368;
   public static final int GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV = 36369;
   public static final int GL_MULTISAMPLE_COVERAGE_MODES_NV = 36370;

   private NVFramebufferMultisampleCoverage() {
   }

   public static void glRenderbufferStorageMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glRenderbufferStorageMultisampleCoverageNV;
      BufferChecks.checkFunctionAddress(var7);
      nglRenderbufferStorageMultisampleCoverageNV(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglRenderbufferStorageMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, long var6);
}
