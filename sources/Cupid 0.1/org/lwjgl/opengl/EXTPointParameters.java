package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTPointParameters {
  public static final int GL_POINT_SIZE_MIN_EXT = 33062;
  
  public static final int GL_POINT_SIZE_MAX_EXT = 33063;
  
  public static final int GL_POINT_FADE_THRESHOLD_SIZE_EXT = 33064;
  
  public static final int GL_DISTANCE_ATTENUATION_EXT = 33065;
  
  public static void glPointParameterfEXT(int pname, float param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPointParameterfEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglPointParameterfEXT(pname, param, function_pointer);
  }
  
  static native void nglPointParameterfEXT(int paramInt, float paramFloat, long paramLong);
  
  public static void glPointParameterEXT(int pname, FloatBuffer pfParams) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPointParameterfvEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(pfParams, 4);
    nglPointParameterfvEXT(pname, MemoryUtil.getAddress(pfParams), function_pointer);
  }
  
  static native void nglPointParameterfvEXT(int paramInt, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTPointParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */