package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureStorageMultisample {
  public static void glTexStorage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
    GL43.glTexStorage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
  }
  
  public static void glTexStorage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
    GL43.glTexStorage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations);
  }
  
  public static void glTextureStorage2DMultisampleEXT(int texture, int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureStorage2DMultisampleEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureStorage2DMultisampleEXT(texture, target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
  }
  
  static native void nglTextureStorage2DMultisampleEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, long paramLong);
  
  public static void glTextureStorage3DMultisampleEXT(int texture, int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureStorage3DMultisampleEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureStorage3DMultisampleEXT(texture, target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
  }
  
  static native void nglTextureStorage3DMultisampleEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBTextureStorageMultisample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */