package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATISeparateStencil {
  public static final int GL_STENCIL_BACK_FUNC_ATI = 34816;
  
  public static final int GL_STENCIL_BACK_FAIL_ATI = 34817;
  
  public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI = 34818;
  
  public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI = 34819;
  
  public static void glStencilOpSeparateATI(int face, int sfail, int dpfail, int dppass) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glStencilOpSeparateATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglStencilOpSeparateATI(face, sfail, dpfail, dppass, function_pointer);
  }
  
  static native void nglStencilOpSeparateATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  public static void glStencilFuncSeparateATI(int frontfunc, int backfunc, int ref, int mask) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glStencilFuncSeparateATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglStencilFuncSeparateATI(frontfunc, backfunc, ref, mask, function_pointer);
  }
  
  static native void nglStencilFuncSeparateATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ATISeparateStencil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */