package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureStorage {
  public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 37167;
  
  public static void glTexStorage1D(int target, int levels, int internalformat, int width) {
    GL42.glTexStorage1D(target, levels, internalformat, width);
  }
  
  public static void glTexStorage2D(int target, int levels, int internalformat, int width, int height) {
    GL42.glTexStorage2D(target, levels, internalformat, width, height);
  }
  
  public static void glTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth) {
    GL42.glTexStorage3D(target, levels, internalformat, width, height, depth);
  }
  
  public static void glTextureStorage1DEXT(int texture, int target, int levels, int internalformat, int width) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureStorage1DEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureStorage1DEXT(texture, target, levels, internalformat, width, function_pointer);
  }
  
  static native void nglTextureStorage1DEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
  
  public static void glTextureStorage2DEXT(int texture, int target, int levels, int internalformat, int width, int height) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureStorage2DEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureStorage2DEXT(texture, target, levels, internalformat, width, height, function_pointer);
  }
  
  static native void nglTextureStorage2DEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
  
  public static void glTextureStorage3DEXT(int texture, int target, int levels, int internalformat, int width, int height, int depth) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureStorage3DEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureStorage3DEXT(texture, target, levels, internalformat, width, height, depth, function_pointer);
  }
  
  static native void nglTextureStorage3DEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBTextureStorage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */