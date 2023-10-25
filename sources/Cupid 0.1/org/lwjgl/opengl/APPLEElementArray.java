package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEElementArray {
  public static final int GL_ELEMENT_ARRAY_APPLE = 34664;
  
  public static final int GL_ELEMENT_ARRAY_TYPE_APPLE = 34665;
  
  public static final int GL_ELEMENT_ARRAY_POINTER_APPLE = 34666;
  
  public static void glElementPointerAPPLE(ByteBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glElementPointerAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglElementPointerAPPLE(5121, MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  public static void glElementPointerAPPLE(IntBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glElementPointerAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglElementPointerAPPLE(5125, MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  public static void glElementPointerAPPLE(ShortBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glElementPointerAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglElementPointerAPPLE(5123, MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  static native void nglElementPointerAPPLE(int paramInt, long paramLong1, long paramLong2);
  
  public static void glDrawElementArrayAPPLE(int mode, int first, int count) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawElementArrayAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglDrawElementArrayAPPLE(mode, first, count, function_pointer);
  }
  
  static native void nglDrawElementArrayAPPLE(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glDrawRangeElementArrayAPPLE(int mode, int start, int end, int first, int count) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDrawRangeElementArrayAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglDrawRangeElementArrayAPPLE(mode, start, end, first, count, function_pointer);
  }
  
  static native void nglDrawRangeElementArrayAPPLE(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
  
  public static void glMultiDrawElementArrayAPPLE(int mode, IntBuffer first, IntBuffer count) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawElementArrayAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(first);
    BufferChecks.checkBuffer(count, first.remaining());
    nglMultiDrawElementArrayAPPLE(mode, MemoryUtil.getAddress(first), MemoryUtil.getAddress(count), first.remaining(), function_pointer);
  }
  
  static native void nglMultiDrawElementArrayAPPLE(int paramInt1, long paramLong1, long paramLong2, int paramInt2, long paramLong3);
  
  public static void glMultiDrawRangeElementArrayAPPLE(int mode, int start, int end, IntBuffer first, IntBuffer count) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMultiDrawRangeElementArrayAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(first);
    BufferChecks.checkBuffer(count, first.remaining());
    nglMultiDrawRangeElementArrayAPPLE(mode, start, end, MemoryUtil.getAddress(first), MemoryUtil.getAddress(count), first.remaining(), function_pointer);
  }
  
  static native void nglMultiDrawRangeElementArrayAPPLE(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, int paramInt4, long paramLong3);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\APPLEElementArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */