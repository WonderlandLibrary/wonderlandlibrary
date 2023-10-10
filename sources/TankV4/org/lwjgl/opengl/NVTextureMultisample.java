package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVTextureMultisample {
   public static final int GL_TEXTURE_COVERAGE_SAMPLES_NV = 36933;
   public static final int GL_TEXTURE_COLOR_SAMPLES_NV = 36934;

   private NVTextureMultisample() {
   }

   public static void glTexImage2DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexImage2DMultisampleCoverageNV;
      BufferChecks.checkFunctionAddress(var8);
      nglTexImage2DMultisampleCoverageNV(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTexImage2DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, long var7);

   public static void glTexImage3DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTexImage3DMultisampleCoverageNV;
      BufferChecks.checkFunctionAddress(var9);
      nglTexImage3DMultisampleCoverageNV(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglTexImage3DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, long var8);

   public static void glTextureImage2DMultisampleNV(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTextureImage2DMultisampleNV;
      BufferChecks.checkFunctionAddress(var8);
      nglTextureImage2DMultisampleNV(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTextureImage2DMultisampleNV(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, long var7);

   public static void glTextureImage3DMultisampleNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureImage3DMultisampleNV;
      BufferChecks.checkFunctionAddress(var9);
      nglTextureImage3DMultisampleNV(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglTextureImage3DMultisampleNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, long var8);

   public static void glTextureImage2DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureImage2DMultisampleCoverageNV;
      BufferChecks.checkFunctionAddress(var9);
      nglTextureImage2DMultisampleCoverageNV(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglTextureImage2DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, long var8);

   public static void glTextureImage3DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTextureImage3DMultisampleCoverageNV;
      BufferChecks.checkFunctionAddress(var10);
      nglTextureImage3DMultisampleCoverageNV(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglTextureImage3DMultisampleCoverageNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, long var9);
}
