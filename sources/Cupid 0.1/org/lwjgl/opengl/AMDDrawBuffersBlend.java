package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDDrawBuffersBlend {
  public static void glBlendFuncIndexedAMD(int buf, int src, int dst) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendFuncIndexedAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendFuncIndexedAMD(buf, src, dst, function_pointer);
  }
  
  static native void nglBlendFuncIndexedAMD(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glBlendFuncSeparateIndexedAMD(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendFuncSeparateIndexedAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendFuncSeparateIndexedAMD(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
  }
  
  static native void nglBlendFuncSeparateIndexedAMD(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
  
  public static void glBlendEquationIndexedAMD(int buf, int mode) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendEquationIndexedAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendEquationIndexedAMD(buf, mode, function_pointer);
  }
  
  static native void nglBlendEquationIndexedAMD(int paramInt1, int paramInt2, long paramLong);
  
  public static void glBlendEquationSeparateIndexedAMD(int buf, int modeRGB, int modeAlpha) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBlendEquationSeparateIndexedAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBlendEquationSeparateIndexedAMD(buf, modeRGB, modeAlpha, function_pointer);
  }
  
  static native void nglBlendEquationSeparateIndexedAMD(int paramInt1, int paramInt2, int paramInt3, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\AMDDrawBuffersBlend.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */