package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEVertexArrayRange {
  public static final int GL_VERTEX_ARRAY_RANGE_APPLE = 34077;
  
  public static final int GL_VERTEX_ARRAY_RANGE_LENGTH_APPLE = 34078;
  
  public static final int GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_APPLE = 34080;
  
  public static final int GL_VERTEX_ARRAY_RANGE_POINTER_APPLE = 34081;
  
  public static final int GL_VERTEX_ARRAY_STORAGE_HINT_APPLE = 34079;
  
  public static final int GL_STORAGE_CACHED_APPLE = 34238;
  
  public static final int GL_STORAGE_SHARED_APPLE = 34239;
  
  public static final int GL_DRAW_PIXELS_APPLE = 35338;
  
  public static final int GL_FENCE_APPLE = 35339;
  
  public static void glVertexArrayRangeAPPLE(ByteBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayRangeAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglVertexArrayRangeAPPLE(pointer.remaining(), MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  static native void nglVertexArrayRangeAPPLE(int paramInt, long paramLong1, long paramLong2);
  
  public static void glFlushVertexArrayRangeAPPLE(ByteBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glFlushVertexArrayRangeAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglFlushVertexArrayRangeAPPLE(pointer.remaining(), MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  static native void nglFlushVertexArrayRangeAPPLE(int paramInt, long paramLong1, long paramLong2);
  
  public static void glVertexArrayParameteriAPPLE(int pname, int param) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glVertexArrayParameteriAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglVertexArrayParameteriAPPLE(pname, param, function_pointer);
  }
  
  static native void nglVertexArrayParameteriAPPLE(int paramInt1, int paramInt2, long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\APPLEVertexArrayRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */