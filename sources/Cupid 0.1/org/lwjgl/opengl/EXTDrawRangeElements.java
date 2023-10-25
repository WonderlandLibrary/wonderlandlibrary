package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTDrawRangeElements {
  public static final int GL_MAX_ELEMENTS_VERTICES_EXT = 33000;
  
  public static final int GL_MAX_ELEMENTS_INDICES_EXT = 33001;
  
  public static void glDrawRangeElementsEXT(int mode, int start, int end, ByteBuffer pIndices) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawRangeElementsEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOdisabled(caps);
    BufferChecks.checkDirect(pIndices);
    nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), 5121, MemoryUtil.getAddress(pIndices), function_pointer);
  }
  
  public static void glDrawRangeElementsEXT(int mode, int start, int end, IntBuffer pIndices) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawRangeElementsEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOdisabled(caps);
    BufferChecks.checkDirect(pIndices);
    nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), 5125, MemoryUtil.getAddress(pIndices), function_pointer);
  }
  
  public static void glDrawRangeElementsEXT(int mode, int start, int end, ShortBuffer pIndices) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawRangeElementsEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOdisabled(caps);
    BufferChecks.checkDirect(pIndices);
    nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), 5123, MemoryUtil.getAddress(pIndices), function_pointer);
  }
  
  static native void nglDrawRangeElementsEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
  
  public static void glDrawRangeElementsEXT(int mode, int start, int end, int pIndices_count, int type, long pIndices_buffer_offset) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawRangeElementsEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    GLChecks.ensureElementVBOenabled(caps);
    nglDrawRangeElementsEXTBO(mode, start, end, pIndices_count, type, pIndices_buffer_offset, function_pointer);
  }
  
  static native void nglDrawRangeElementsEXTBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTDrawRangeElements.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */