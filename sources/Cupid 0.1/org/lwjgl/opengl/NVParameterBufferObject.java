package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVParameterBufferObject {
  public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_BINDINGS_NV = 36256;
  
  public static final int GL_MAX_PROGRAM_PARAMETER_BUFFER_SIZE_NV = 36257;
  
  public static final int GL_VERTEX_PROGRAM_PARAMETER_BUFFER_NV = 36258;
  
  public static final int GL_GEOMETRY_PROGRAM_PARAMETER_BUFFER_NV = 36259;
  
  public static final int GL_FRAGMENT_PROGRAM_PARAMETER_BUFFER_NV = 36260;
  
  public static void glProgramBufferParametersNV(int target, int buffer, int index, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramBufferParametersfvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(params);
    nglProgramBufferParametersfvNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglProgramBufferParametersfvNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
  
  public static void glProgramBufferParametersINV(int target, int buffer, int index, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramBufferParametersIivNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(params);
    nglProgramBufferParametersIivNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglProgramBufferParametersIivNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
  
  public static void glProgramBufferParametersIuNV(int target, int buffer, int index, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramBufferParametersIuivNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(params);
    nglProgramBufferParametersIuivNV(target, buffer, index, params.remaining() >> 2, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglProgramBufferParametersIuivNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVParameterBufferObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */