package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVTextureMultisample {
  public static final int GL_TEXTURE_COVERAGE_SAMPLES_NV = 36933;
  
  public static final int GL_TEXTURE_COLOR_SAMPLES_NV = 36934;
  
  public static void glTexImage2DMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, boolean fixedSampleLocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexImage2DMultisampleCoverageNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexImage2DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations, function_pointer);
  }
  
  static native void nglTexImage2DMultisampleCoverageNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, long paramLong);
  
  public static void glTexImage3DMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTexImage3DMultisampleCoverageNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTexImage3DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
  }
  
  static native void nglTexImage3DMultisampleCoverageNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, long paramLong);
  
  public static void glTextureImage2DMultisampleNV(int texture, int target, int samples, int internalFormat, int width, int height, boolean fixedSampleLocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureImage2DMultisampleNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureImage2DMultisampleNV(texture, target, samples, internalFormat, width, height, fixedSampleLocations, function_pointer);
  }
  
  static native void nglTextureImage2DMultisampleNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, long paramLong);
  
  public static void glTextureImage3DMultisampleNV(int texture, int target, int samples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureImage3DMultisampleNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureImage3DMultisampleNV(texture, target, samples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
  }
  
  static native void nglTextureImage3DMultisampleNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, long paramLong);
  
  public static void glTextureImage2DMultisampleCoverageNV(int texture, int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, boolean fixedSampleLocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureImage2DMultisampleCoverageNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureImage2DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations, function_pointer);
  }
  
  static native void nglTextureImage2DMultisampleCoverageNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, long paramLong);
  
  public static void glTextureImage3DMultisampleCoverageNV(int texture, int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureImage3DMultisampleCoverageNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglTextureImage3DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
  }
  
  static native void nglTextureImage3DMultisampleCoverageNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVTextureMultisample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */