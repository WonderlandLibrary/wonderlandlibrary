package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class APPLEObjectPurgeable {
  public static final int GL_RELEASED_APPLE = 35353;
  
  public static final int GL_VOLATILE_APPLE = 35354;
  
  public static final int GL_RETAINED_APPLE = 35355;
  
  public static final int GL_UNDEFINED_APPLE = 35356;
  
  public static final int GL_PURGEABLE_APPLE = 35357;
  
  public static final int GL_BUFFER_OBJECT_APPLE = 34227;
  
  public static int glObjectPurgeableAPPLE(int objectType, int name, int option) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glObjectPurgeableAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    int __result = nglObjectPurgeableAPPLE(objectType, name, option, function_pointer);
    return __result;
  }
  
  static native int nglObjectPurgeableAPPLE(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static int glObjectUnpurgeableAPPLE(int objectType, int name, int option) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glObjectUnpurgeableAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    int __result = nglObjectUnpurgeableAPPLE(objectType, name, option, function_pointer);
    return __result;
  }
  
  static native int nglObjectUnpurgeableAPPLE(int paramInt1, int paramInt2, int paramInt3, long paramLong);
  
  public static void glGetObjectParameterAPPLE(int objectType, int name, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetObjectParameterivAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetObjectParameterivAPPLE(objectType, name, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetObjectParameterivAPPLE(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
  
  public static int glGetObjectParameteriAPPLE(int objectType, int name, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetObjectParameterivAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetObjectParameterivAPPLE(objectType, name, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\APPLEObjectPurgeable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */