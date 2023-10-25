package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVPixelDataRange {
  public static final int GL_WRITE_PIXEL_DATA_RANGE_NV = 34936;
  
  public static final int GL_READ_PIXEL_DATA_RANGE_NV = 34937;
  
  public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV = 34938;
  
  public static final int GL_READ_PIXEL_DATA_RANGE_LENGTH_NV = 34939;
  
  public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV = 34940;
  
  public static final int GL_READ_PIXEL_DATA_RANGE_POINTER_NV = 34941;
  
  public static void glPixelDataRangeNV(int target, ByteBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPixelDataRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(data);
    nglPixelDataRangeNV(target, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
  }
  
  public static void glPixelDataRangeNV(int target, DoubleBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPixelDataRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(data);
    nglPixelDataRangeNV(target, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
  }
  
  public static void glPixelDataRangeNV(int target, FloatBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPixelDataRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(data);
    nglPixelDataRangeNV(target, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
  }
  
  public static void glPixelDataRangeNV(int target, IntBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPixelDataRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(data);
    nglPixelDataRangeNV(target, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
  }
  
  public static void glPixelDataRangeNV(int target, ShortBuffer data) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glPixelDataRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(data);
    nglPixelDataRangeNV(target, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
  }
  
  static native void nglPixelDataRangeNV(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static void glFlushPixelDataRangeNV(int target) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFlushPixelDataRangeNV;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglFlushPixelDataRangeNV(target, function_pointer);
  }
  
  static native void nglFlushPixelDataRangeNV(int paramInt, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\NVPixelDataRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */