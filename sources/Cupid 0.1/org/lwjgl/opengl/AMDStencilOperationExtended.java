package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDStencilOperationExtended {
  public static final int GL_SET_AMD = 34634;
  
  public static final int GL_REPLACE_VALUE_AMD = 34635;
  
  public static final int GL_STENCIL_OP_VALUE_AMD = 34636;
  
  public static final int GL_STENCIL_BACK_OP_VALUE_AMD = 34637;
  
  public static void glStencilOpValueAMD(int face, int value) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glStencilOpValueAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglStencilOpValueAMD(face, value, function_pointer);
  }
  
  static native void nglStencilOpValueAMD(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\AMDStencilOperationExtended.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */