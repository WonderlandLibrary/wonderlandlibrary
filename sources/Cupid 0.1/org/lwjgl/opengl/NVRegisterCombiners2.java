package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVRegisterCombiners2 {
  public static final int GL_PER_STAGE_CONSTANTS_NV = 34101;
  
  public static void glCombinerStageParameterNV(int stage, int pname, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glCombinerStageParameterfvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglCombinerStageParameterfvNV(stage, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglCombinerStageParameterfvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glGetCombinerStageParameterNV(int stage, int pname, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetCombinerStageParameterfvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetCombinerStageParameterfvNV(stage, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetCombinerStageParameterfvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVRegisterCombiners2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */