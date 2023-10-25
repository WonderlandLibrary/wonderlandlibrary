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
  
  public static void glTexPageCommitmentARB(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, boolean commit) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexPageCommitmentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexPageCommitmentARB(target, level, xoffset, yoffset, zoffset, width, height, depth, commit, function_pointer);
  }
  
  static native void nglTexPageCommitmentARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean, long paramLong);
  
  public static void glTexturePageCommitmentEXT(int texture, int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, boolean commit) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexturePageCommitmentEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexturePageCommitmentEXT(texture, target, level, xoffset, yoffset, zoffset, width, height, depth, commit, function_pointer);
  }
  
  static native void nglTexturePageCommitmentEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, boolean paramBoolean, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBSparseTexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */