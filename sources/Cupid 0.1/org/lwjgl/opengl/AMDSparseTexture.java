package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDSparseTexture {
  public static final int GL_TEXTURE_STORAGE_SPARSE_BIT_AMD = 1;
  
  public static final int GL_VIRTUAL_PAGE_SIZE_X_AMD = 37269;
  
  public static final int GL_VIRTUAL_PAGE_SIZE_Y_AMD = 37270;
  
  public static final int GL_VIRTUAL_PAGE_SIZE_Z_AMD = 37271;
  
  public static final int GL_MAX_SPARSE_TEXTURE_SIZE_AMD = 37272;
  
  public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_AMD = 37273;
  
  public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS = 37274;
  
  public static final int GL_MIN_SPARSE_LEVEL_AMD = 37275;
  
  public static final int GL_MIN_LOD_WARNING_AMD = 37276;
  
  public static void glTexStorageSparseAMD(int target, int internalFormat, int width, int height, int depth, int layers, int flags) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexStorageSparseAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexStorageSparseAMD(target, internalFormat, width, height, depth, layers, flags, function_pointer);
  }
  
  static native void nglTexStorageSparseAMD(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong);
  
  public static void glTextureStorageSparseAMD(int texture, int target, int internalFormat, int width, int height, int depth, int layers, int flags) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureStorageSparseAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureStorageSparseAMD(texture, target, internalFormat, width, height, depth, layers, flags, function_pointer);
  }
  
  static native void nglTextureStorageSparseAMD(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\AMDSparseTexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */