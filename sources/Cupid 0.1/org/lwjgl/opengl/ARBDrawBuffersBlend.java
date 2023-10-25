package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBDrawBuffersBlend {
  public static void glBlendEquationiARB(int buf, int mode) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendEquationiARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendEquationiARB(buf, mode, function_pointer);
  }
  
  static native void nglBlendEquationiARB(int paramInt1, int paramInt2, long paramLong);
  
  public static void glBlendEquationSeparateiARB(int buf, int modeRGB, int modeAlpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendEquationSeparateiARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendEquationSeparateiARB(buf, modeRGB, modeAlpha, function_pointer);
  }
  
  static native void nglBlendEquationSeparateiARB(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glBlendFunciARB(int buf, int src, int dst) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendFunciARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendFunciARB(buf, src, dst, function_pointer);
  }
  
  static native void nglBlendFunciARB(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glBlendFuncSeparateiARB(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendFuncSeparateiARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendFuncSeparateiARB(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
  }
  
  static native void nglBlendFuncSeparateiARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBDrawBuffersBlend.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */