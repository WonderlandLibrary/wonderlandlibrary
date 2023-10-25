package org.lwjgl.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLETextureRange {
  public static final int GL_TEXTURE_STORAGE_HINT_APPLE = 34236;
  
  public static final int GL_STORAGE_PRIVATE_APPLE = 34237;
  
  public static final int GL_STORAGE_CACHED_APPLE = 34238;
  
  public static final int GL_STORAGE_SHARED_APPLE = 34239;
  
  public static final int GL_TEXTURE_RANGE_LENGTH_APPLE = 34231;
  
  public static final int GL_TEXTURE_RANGE_POINTER_APPLE = 34232;
  
  public static void glTextureRangeAPPLE(int target, ByteBuffer pointer) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glTextureRangeAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(pointer);
    nglTextureRangeAPPLE(target, pointer.remaining(), MemoryUtil.getAddress(pointer), function_pointer);
  }
  
  static native void nglTextureRangeAPPLE(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static Buffer glGetTexParameterPointervAPPLE(int target, int pname, long result_size) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetTexParameterPointervAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    Buffer __result = nglGetTexParameterPointervAPPLE(target, pname, result_size, function_pointer);
    return __result;
  }
  
  static native Buffer nglGetTexParameterPointervAPPLE(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\APPLETextureRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */