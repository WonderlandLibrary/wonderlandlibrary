package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ATIVertexAttribArrayObject {
  public static void glVertexAttribArrayObjectATI(int index, int size, int type, boolean normalized, int stride, int buffer, int offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribArrayObjectATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttribArrayObjectATI(index, size, type, normalized, stride, buffer, offset, function_pointer);
  }
  
  static native void nglVertexAttribArrayObjectATI(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, int paramInt5, int paramInt6, long paramLong);
  
  public static void glGetVertexAttribArrayObjectATI(int index, int pname, FloatBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVertexAttribArrayObjectfvATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetVertexAttribArrayObjectfvATI(index, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVertexAttribArrayObjectfvATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glGetVertexAttribArrayObjectATI(int index, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVertexAttribArrayObjectivATI;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetVertexAttribArrayObjectivATI(index, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVertexAttribArrayObjectivATI(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ATIVertexAttribArrayObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */