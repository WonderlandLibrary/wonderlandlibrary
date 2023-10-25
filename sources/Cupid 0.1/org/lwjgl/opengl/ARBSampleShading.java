package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBSampleShading {
  public static final int GL_SAMPLE_SHADING_ARB = 35894;
  
  public static final int GL_MIN_SAMPLE_SHADING_VALUE_ARB = 35895;
  
  public static void glMinSampleShadingARB(float value) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMinSampleShadingARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMinSampleShadingARB(value, function_pointer);
  }
  
  static native void nglMinSampleShadingARB(float paramFloat, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBSampleShading.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */