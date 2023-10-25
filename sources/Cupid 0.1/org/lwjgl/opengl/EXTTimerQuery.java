package org.lwjgl.opengl;

import java.nio.LongBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTTimerQuery {
  public static final int GL_TIME_ELAPSED_EXT = 35007;
  
  public static void glGetQueryObjectEXT(int id, int pname, LongBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjecti64vEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetQueryObjecti64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetQueryObjecti64vEXT(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static long glGetQueryObjectEXT(int id, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjecti64vEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    LongBuffer params = APIUtil.getBufferLong(caps);
    nglGetQueryObjecti64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glGetQueryObjectuEXT(int id, int pname, LongBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjectui64vEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetQueryObjectui64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetQueryObjectui64vEXT(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static long glGetQueryObjectuEXT(int id, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjectui64vEXT;
    BufferChecks.checkFunctionAddress(function_pointer);
    LongBuffer params = APIUtil.getBufferLong(caps);
    nglGetQueryObjectui64vEXT(id, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\EXTTimerQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */