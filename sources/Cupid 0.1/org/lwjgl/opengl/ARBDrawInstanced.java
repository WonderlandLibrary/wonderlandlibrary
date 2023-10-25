package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBDrawInstanced {
  public static void glDrawArraysInstancedARB(int mode, int first, int count, int primcount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawArraysInstancedARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglDrawArraysInstancedARB(mode, first, count, primcount, function_pointer);
  }
  
  static native void nglDrawArraysInstancedARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
  
  public static void glDrawElementsInstancedARB(int mode, ByteBuffer indices, int primcount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawElementsInstancedARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOdisabled(caps);
    BufferChecks.checkDirect(indices);
    nglDrawElementsInstancedARB(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), primcount, function_pointer);
  }
  
  public static void glDrawElementsInstancedARB(int mode, IntBuffer indices, int primcount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawElementsInstancedARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOdisabled(caps);
    BufferChecks.checkDirect(indices);
    nglDrawElementsInstancedARB(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), primcount, function_pointer);
  }
  
  public static void glDrawElementsInstancedARB(int mode, ShortBuffer indices, int primcount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawElementsInstancedARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOdisabled(caps);
    BufferChecks.checkDirect(indices);
    nglDrawElementsInstancedARB(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), primcount, function_pointer);
  }
  
  static native void nglDrawElementsInstancedARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, long paramLong2);
  
  public static void glDrawElementsInstancedARB(int mode, int indices_count, int type, long indices_buffer_offset, int primcount) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawElementsInstancedARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOenabled(caps);
    nglDrawElementsInstancedARBBO(mode, indices_count, type, indices_buffer_offset, primcount, function_pointer);
  }
  
  static native void nglDrawElementsInstancedARBBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBDrawInstanced.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */