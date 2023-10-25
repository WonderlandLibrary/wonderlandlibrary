package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBIndirectParameters {
  public static final int GL_PARAMETER_BUFFER_ARB = 33006;
  
  public static final int GL_PARAMETER_BUFFER_BINDING_ARB = 33007;
  
  public static void glMultiDrawArraysIndirectCountARB(int mode, ByteBuffer indirect, long drawcount, int maxdrawcount, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOdisabled(caps);
    BufferChecks.checkBuffer(indirect, ((stride == 0) ? 16 : stride) * maxdrawcount);
    nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
  }
  
  static native void nglMultiDrawArraysIndirectCountARB(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, long paramLong3);
  
  public static void glMultiDrawArraysIndirectCountARB(int mode, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOenabled(caps);
    nglMultiDrawArraysIndirectCountARBBO(mode, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
  }
  
  static native void nglMultiDrawArraysIndirectCountARBBO(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, long paramLong3);
  
  public static void glMultiDrawArraysIndirectCountARB(int mode, IntBuffer indirect, long drawcount, int maxdrawcount, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOdisabled(caps);
    BufferChecks.checkBuffer(indirect, ((stride == 0) ? 4 : (stride >> 2)) * maxdrawcount);
    nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
  }
  
  public static void glMultiDrawElementsIndirectCountARB(int mode, int type, ByteBuffer indirect, long drawcount, int maxdrawcount, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOdisabled(caps);
    BufferChecks.checkBuffer(indirect, ((stride == 0) ? 20 : stride) * maxdrawcount);
    nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
  }
  
  static native void nglMultiDrawElementsIndirectCountARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3);
  
  public static void glMultiDrawElementsIndirectCountARB(int mode, int type, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOenabled(caps);
    nglMultiDrawElementsIndirectCountARBBO(mode, type, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
  }
  
  static native void nglMultiDrawElementsIndirectCountARBBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3);
  
  public static void glMultiDrawElementsIndirectCountARB(int mode, int type, IntBuffer indirect, long drawcount, int maxdrawcount, int stride) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureIndirectBOdisabled(caps);
    BufferChecks.checkBuffer(indirect, ((stride == 0) ? 5 : (stride >> 2)) * maxdrawcount);
    nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBIndirectParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */