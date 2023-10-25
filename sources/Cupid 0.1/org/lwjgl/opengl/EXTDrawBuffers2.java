package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTDrawBuffers2 {
  public static void glColorMaskIndexedEXT(int buf, boolean r, boolean g, boolean b, boolean a) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glColorMaskIndexedEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglColorMaskIndexedEXT(buf, r, g, b, a, function_pointer);
  }
  
  static native void nglColorMaskIndexedEXT(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong);
  
  public static void glGetBooleanIndexedEXT(int value, int index, ByteBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetBooleanIndexedvEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(data, 4);
    nglGetBooleanIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
  }
  
  static native void nglGetBooleanIndexedvEXT(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static boolean glGetBooleanIndexedEXT(int value, int index) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetBooleanIndexedvEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    ByteBuffer data = APIUtil.getBufferByte(caps, 1);
    nglGetBooleanIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
    return (data.get(0) == 1);
  }
  
  public static void glGetIntegerIndexedEXT(int value, int index, IntBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetIntegerIndexedvEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(data, 4);
    nglGetIntegerIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
  }
  
  static native void nglGetIntegerIndexedvEXT(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static int glGetIntegerIndexedEXT(int value, int index) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetIntegerIndexedvEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer data = APIUtil.getBufferInt(caps);
    nglGetIntegerIndexedvEXT(value, index, MemoryUtil.getAddress(data), function_pointer);
    return data.get(0);
  }
  
  public static void glEnableIndexedEXT(int target, int index) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glEnableIndexedEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglEnableIndexedEXT(target, index, function_pointer);
  }
  
  static native void nglEnableIndexedEXT(int paramInt1, int paramInt2, long paramLong);
  
  public static void glDisableIndexedEXT(int target, int index) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDisableIndexedEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglDisableIndexedEXT(target, index, function_pointer);
  }
  
  static native void nglDisableIndexedEXT(int paramInt1, int paramInt2, long paramLong);
  
  public static boolean glIsEnabledIndexedEXT(int target, int index) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glIsEnabledIndexedEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    boolean __result = nglIsEnabledIndexedEXT(target, index, function_pointer);
    return __result;
  }
  
  static native boolean nglIsEnabledIndexedEXT(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTDrawBuffers2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */