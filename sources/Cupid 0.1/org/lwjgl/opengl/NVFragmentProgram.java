package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVFragmentProgram extends NVProgram {
  public static final int GL_FRAGMENT_PROGRAM_NV = 34928;
  
  public static final int GL_MAX_TEXTURE_COORDS_NV = 34929;
  
  public static final int GL_MAX_TEXTURE_IMAGE_UNITS_NV = 34930;
  
  public static final int GL_FRAGMENT_PROGRAM_BINDING_NV = 34931;
  
  public static final int GL_MAX_FRAGMENT_PROGRAM_LOCAL_PARAMETERS_NV = 34920;
  
  public static void glProgramNamedParameter4fNV(int id, ByteBuffer name, float x, float y, float z, float w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramNamedParameter4fNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(name);
    nglProgramNamedParameter4fNV(id, name.remaining(), MemoryUtil.getAddress(name), x, y, z, w, function_pointer);
  }
  
  static native void nglProgramNamedParameter4fNV(int paramInt1, int paramInt2, long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong2);
  
  public static void glProgramNamedParameter4dNV(int id, ByteBuffer name, double x, double y, double z, double w) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramNamedParameter4dNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(name);
    nglProgramNamedParameter4dNV(id, name.remaining(), MemoryUtil.getAddress(name), x, y, z, w, function_pointer);
  }
  
  static native void nglProgramNamedParameter4dNV(int paramInt1, int paramInt2, long paramLong1, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong2);
  
  public static void glGetProgramNamedParameterNV(int id, ByteBuffer name, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetProgramNamedParameterfvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(name);
    BufferChecks.checkBuffer(params, 4);
    nglGetProgramNamedParameterfvNV(id, name.remaining(), MemoryUtil.getAddress(name), MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetProgramNamedParameterfvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
  
  public static void glGetProgramNamedParameterNV(int id, ByteBuffer name, DoubleBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetProgramNamedParameterdvNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(name);
    BufferChecks.checkBuffer(params, 4);
    nglGetProgramNamedParameterdvNV(id, name.remaining(), MemoryUtil.getAddress(name), MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetProgramNamedParameterdvNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVFragmentProgram.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */