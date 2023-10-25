package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBBindlessTexture {
  public static final int GL_UNSIGNED_INT64_ARB = 5135;
  
  public static long glGetTextureHandleARB(int texture) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetTextureHandleARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    long __result = nglGetTextureHandleARB(texture, function_pointer);
    return __result;
  }
  
  static native long nglGetTextureHandleARB(int paramInt, long paramLong);
  
  public static long glGetTextureSamplerHandleARB(int texture, int sampler) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetTextureSamplerHandleARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    long __result = nglGetTextureSamplerHandleARB(texture, sampler, function_pointer);
    return __result;
  }
  
  static native long nglGetTextureSamplerHandleARB(int paramInt1, int paramInt2, long paramLong);
  
  public static void glMakeTextureHandleResidentARB(long handle) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMakeTextureHandleResidentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMakeTextureHandleResidentARB(handle, function_pointer);
  }
  
  static native void nglMakeTextureHandleResidentARB(long paramLong1, long paramLong2);
  
  public static void glMakeTextureHandleNonResidentARB(long handle) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMakeTextureHandleNonResidentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMakeTextureHandleNonResidentARB(handle, function_pointer);
  }
  
  static native void nglMakeTextureHandleNonResidentARB(long paramLong1, long paramLong2);
  
  public static long glGetImageHandleARB(int texture, int level, boolean layered, int layer, int format) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetImageHandleARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    long __result = nglGetImageHandleARB(texture, level, layered, layer, format, function_pointer);
    return __result;
  }
  
  static native long nglGetImageHandleARB(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4, long paramLong);
  
  public static void glMakeImageHandleResidentARB(long handle, int access) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMakeImageHandleResidentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMakeImageHandleResidentARB(handle, access, function_pointer);
  }
  
  static native void nglMakeImageHandleResidentARB(long paramLong1, int paramInt, long paramLong2);
  
  public static void glMakeImageHandleNonResidentARB(long handle) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glMakeImageHandleNonResidentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglMakeImageHandleNonResidentARB(handle, function_pointer);
  }
  
  static native void nglMakeImageHandleNonResidentARB(long paramLong1, long paramLong2);
  
  public static void glUniformHandleui64ARB(int location, long value) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUniformHandleui64ARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglUniformHandleui64ARB(location, value, function_pointer);
  }
  
  static native void nglUniformHandleui64ARB(int paramInt, long paramLong1, long paramLong2);
  
  public static void glUniformHandleuARB(int location, LongBuffer value) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glUniformHandleui64vARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(value);
    nglUniformHandleui64vARB(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
  }
  
  static native void nglUniformHandleui64vARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glProgramUniformHandleui64ARB(int program, int location, long value) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramUniformHandleui64ARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglProgramUniformHandleui64ARB(program, location, value, function_pointer);
  }
  
  static native void nglProgramUniformHandleui64ARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glProgramUniformHandleuARB(int program, int location, LongBuffer values) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glProgramUniformHandleui64vARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(values);
    nglProgramUniformHandleui64vARB(program, location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
  }
  
  static native void nglProgramUniformHandleui64vARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
  
  public static boolean glIsTextureHandleResidentARB(long handle) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glIsTextureHandleResidentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    boolean __result = nglIsTextureHandleResidentARB(handle, function_pointer);
    return __result;
  }
  
  static native boolean nglIsTextureHandleResidentARB(long paramLong1, long paramLong2);
  
  public static boolean glIsImageHandleResidentARB(long handle) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glIsImageHandleResidentARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    boolean __result = nglIsImageHandleResidentARB(handle, function_pointer);
    return __result;
  }
  
  static native boolean nglIsImageHandleResidentARB(long paramLong1, long paramLong2);
  
  public static void glVertexAttribL1ui64ARB(int index, long x) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribL1ui64ARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexAttribL1ui64ARB(index, x, function_pointer);
  }
  
  static native void nglVertexAttribL1ui64ARB(int paramInt, long paramLong1, long paramLong2);
  
  public static void glVertexAttribL1uARB(int index, LongBuffer v) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexAttribL1ui64vARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(v, 1);
    nglVertexAttribL1ui64vARB(index, MemoryUtil.getAddress(v), function_pointer);
  }
  
  static native void nglVertexAttribL1ui64vARB(int paramInt, long paramLong1, long paramLong2);
  
  public static void glGetVertexAttribLuARB(int index, int pname, LongBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetVertexAttribLui64vARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 4);
    nglGetVertexAttribLui64vARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetVertexAttribLui64vARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBBindlessTexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */