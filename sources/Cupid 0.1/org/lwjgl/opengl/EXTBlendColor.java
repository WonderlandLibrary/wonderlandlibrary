package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendColor {
  public static final int GL_CONSTANT_COLOR_EXT = 32769;
  
  public static final int GL_ONE_MINUS_CONSTANT_COLOR_EXT = 32770;
  
  public static final int GL_CONSTANT_ALPHA_EXT = 32771;
  
  public static final int GL_ONE_MINUS_CONSTANT_ALPHA_EXT = 32772;
  
  public static final int GL_BLEND_COLOR_EXT = 32773;
  
  public static void glBlendColorEXT(float red, float green, float blue, float alpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendColorEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendColorEXT(red, green, blue, alpha, function_pointer);
  }
  
  static native void nglBlendColorEXT(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTBlendColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */