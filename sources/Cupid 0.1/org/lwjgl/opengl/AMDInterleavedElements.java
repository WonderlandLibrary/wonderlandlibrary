package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDInterleavedElements {
  public static final int GL_VERTEX_ELEMENT_SWIZZLE_AMD = 37284;
  
  public static final int GL_VERTEX_ID_SWIZZLE_AMD = 37285;
  
  public static void glVertexAttribParameteriAMD(int index, int pname, int param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribParameteriAMD;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttribParameteriAMD(index, pname, param, function_pointer);
  }
  
  static native void nglVertexAttribParameteriAMD(int paramInt1, int paramInt2, int paramInt3, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\AMDInterleavedElements.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */