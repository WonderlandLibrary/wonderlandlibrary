package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBPointParameters {
  public static final int GL_POINT_SIZE_MIN_ARB = 33062;
  
  public static final int GL_POINT_SIZE_MAX_ARB = 33063;
  
  public static final int GL_POINT_FADE_THRESHOLD_SIZE_ARB = 33064;
  
  public static final int GL_POINT_DISTANCE_ATTENUATION_ARB = 33065;
  
  public static void glPointParameterfARB(int pname, float param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPointParameterfARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglPointParameterfARB(pname, param, function_pointer);
  }
  
  static native void nglPointParameterfARB(int paramInt, float paramFloat, long paramLong);
  
  public static void glPointParameterARB(int pname, FloatBuffer pfParams) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPointParameterfvARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(pfParams, 4);
    nglPointParameterfvARB(pname, MemoryUtil.getAddress(pfParams), function_pointer);
  }
  
  static native void nglPointParameterfvARB(int paramInt, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBPointParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */