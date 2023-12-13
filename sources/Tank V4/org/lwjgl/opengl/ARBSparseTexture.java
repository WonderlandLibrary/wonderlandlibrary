package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBSparseTexture {
   public static final int GL_TEXTURE_SPARSE_ARB = 37286;
   public static final int GL_VIRTUAL_PAGE_SIZE_INDEX_ARB = 37287;
   public static final int GL_NUM_SPARSE_LEVELS_ARB = 37290;
   public static final int GL_NUM_VIRTUAL_PAGE_SIZES_ARB = 37288;
   public static final int GL_VIRTUAL_PAGE_SIZE_X_ARB = 37269;
   public static final int GL_VIRTUAL_PAGE_SIZE_Y_ARB = 37270;
   public static final int GL_VIRTUAL_PAGE_SIZE_Z_ARB = 37271;
   public static final int GL_MAX_SPARSE_TEXTURE_SIZE_ARB = 37272;
   public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_ARB = 37273;
   public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_ARB = 37274;
   public static final int GL_SPARSE_TEXTURE_FULL_ARRAY_CUBE_MIPMAPS_ARB = 37289;

   private ARBSparseTexture() {
   }

   public static void glTexPageCommitmentARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      ContextCapabilities var9 = GLContext.getCapabilities();
      long var10 = var9.glTexPageCommitmentARB;
      BufferChecks.checkFunctionAddress(var10);
      nglTexPageCommitmentARB(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10);
   }

   static native void nglTexPageCommitmentARB(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, long var9);

   public static void glTexturePageCommitmentEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glTexturePageCommitmentEXT;
      BufferChecks.checkFunctionAddress(var11);
      nglTexturePageCommitmentEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   static native void nglTexturePageCommitmentEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, long var10);
}
