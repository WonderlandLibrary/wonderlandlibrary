package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVBindlessMultiDrawIndirect {
  public static void glMultiDrawArraysIndirectBindlessNV(int mode, ByteBuffer indirect, int drawCount, int stride, int vertexBufferCount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOdisabled(caps);
    BufferChecks.checkBuffer(indirect, ((stride == 0) ? (20 + 24 * vertexBufferCount) : stride) * drawCount);
    nglMultiDrawArraysIndirectBindlessNV(mode, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
  }
  
  static native void nglMultiDrawArraysIndirectBindlessNV(int paramInt1, long paramLong1, int paramInt2, int paramInt3, int paramInt4, long paramLong2);
  
  public static void glMultiDrawArraysIndirectBindlessNV(int mode, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOenabled(caps);
    nglMultiDrawArraysIndirectBindlessNVBO(mode, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
  }
  
  static native void nglMultiDrawArraysIndirectBindlessNVBO(int paramInt1, long paramLong1, int paramInt2, int paramInt3, int paramInt4, long paramLong2);
  
  public static void glMultiDrawElementsIndirectBindlessNV(int mode, int type, ByteBuffer indirect, int drawCount, int stride, int vertexBufferCount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOdisabled(caps);
    BufferChecks.checkBuffer(indirect, ((stride == 0) ? (48 + 24 * vertexBufferCount) : stride) * drawCount);
    nglMultiDrawElementsIndirectBindlessNV(mode, type, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
  }
  
  static native void nglMultiDrawElementsIndirectBindlessNV(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, int paramInt5, long paramLong2);
  
  public static void glMultiDrawElementsIndirectBindlessNV(int mode, int type, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOenabled(caps);
    nglMultiDrawElementsIndirectBindlessNVBO(mode, type, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
  }
  
  static native void nglMultiDrawElementsIndirectBindlessNVBO(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, int paramInt5, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVBindlessMultiDrawIndirect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */