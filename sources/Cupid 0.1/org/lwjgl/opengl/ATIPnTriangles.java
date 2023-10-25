package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ATIPnTriangles {
  public static final int GL_PN_TRIANGLES_ATI = 34800;
  
  public static final int GL_MAX_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 34801;
  
  public static final int GL_PN_TRIANGLES_POINT_MODE_ATI = 34802;
  
  public static final int GL_PN_TRIANGLES_NORMAL_MODE_ATI = 34803;
  
  public static final int GL_PN_TRIANGLES_TESSELATION_LEVEL_ATI = 34804;
  
  public static final int GL_PN_TRIANGLES_POINT_MODE_LINEAR_ATI = 34805;
  
  public static final int GL_PN_TRIANGLES_POINT_MODE_CUBIC_ATI = 34806;
  
  public static final int GL_PN_TRIANGLES_NORMAL_MODE_LINEAR_ATI = 34807;
  
  public static final int GL_PN_TRIANGLES_NORMAL_MODE_QUADRATIC_ATI = 34808;
  
  public static void glPNTrianglesfATI(int pname, float param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPNTrianglesfATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglPNTrianglesfATI(pname, param, function_pointer);
  }
  
  static native void nglPNTrianglesfATI(int paramInt, float paramFloat, long paramLong);
  
  public static void glPNTrianglesiATI(int pname, int param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPNTrianglesiATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglPNTrianglesiATI(pname, param, function_pointer);
  }
  
  static native void nglPNTrianglesiATI(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ATIPnTriangles.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */