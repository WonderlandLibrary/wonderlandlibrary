package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendEquationSeparate {
  public static final int GL_BLEND_EQUATION_RGB_EXT = 32777;
  
  public static final int GL_BLEND_EQUATION_ALPHA_EXT = 34877;
  
  public static void glBlendEquationSeparateEXT(int modeRGB, int modeAlpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendEquationSeparateEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendEquationSeparateEXT(modeRGB, modeAlpha, function_pointer);
  }
  
  static native void nglBlendEquationSeparateEXT(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTBlendEquationSeparate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */