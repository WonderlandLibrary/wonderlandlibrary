package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

public final class NVVertexArrayRange {
  public static final int GL_VERTEX_ARRAY_RANGE_NV = 34077;
  
  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_NV = 34078;
  
  public static final int GL_VERTEX_ARRAY_RANGE_VALID_NV = 34079;
  
  public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV = 34080;
  
  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_NV = 34081;
  
  public static void glVertexArrayRangeNV(ByteBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglVertexArrayRangeNV(pPointer.remaining(), MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  public static void glVertexArrayRangeNV(DoubleBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglVertexArrayRangeNV(pPointer.remaining() << 3, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  public static void glVertexArrayRangeNV(FloatBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglVertexArrayRangeNV(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  public static void glVertexArrayRangeNV(IntBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglVertexArrayRangeNV(pPointer.remaining() << 2, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  public static void glVertexArrayRangeNV(ShortBuffer pPointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pPointer);
    nglVertexArrayRangeNV(pPointer.remaining() << 1, MemoryUtil.getAddress(pPointer), function_pointer);
  }
  
  static native void nglVertexArrayRangeNV(int paramInt, long paramLong1, long paramLong2);
  
  public static void glFlushVertexArrayRangeNV() {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFlushVertexArrayRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglFlushVertexArrayRangeNV(function_pointer);
  }
  
  static native void nglFlushVertexArrayRangeNV(long paramLong);
  
  public static ByteBuffer glAllocateMemoryNV(int size, float readFrequency, float writeFrequency, float priority) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glAllocateMemoryNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    ByteBuffer __result = nglAllocateMemoryNV(size, readFrequency, writeFrequency, priority, size, function_pointer);
    return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
  }
  
  static native ByteBuffer nglAllocateMemoryNV(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong1, long paramLong2);
  
  public static void glFreeMemoryNV(ByteBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFreeMemoryNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglFreeMemoryNV(MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  static native void nglFreeMemoryNV(long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVVertexArrayRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */