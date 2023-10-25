package org.lwjgl.opengl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class ARBOcclusionQuery {
  public static final int GL_SAMPLES_PASSED_ARB = 35092;
  
  public static final int GL_QUERY_COUNTER_BITS_ARB = 34916;
  
  public static final int GL_CURRENT_QUERY_ARB = 34917;
  
  public static final int GL_QUERY_RESULT_ARB = 34918;
  
  public static final int GL_QUERY_RESULT_AVAILABLE_ARB = 34919;
  
  public static void glGenQueriesARB(IntBuffer ids) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGenQueriesARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(ids);
    nglGenQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
  }
  
  static native void nglGenQueriesARB(int paramInt, long paramLong1, long paramLong2);
  
  public static int glGenQueriesARB() {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGenQueriesARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer ids = APIUtil.getBufferInt(caps);
    nglGenQueriesARB(1, MemoryUtil.getAddress(ids), function_pointer);
    return ids.get(0);
  }
  
  public static void glDeleteQueriesARB(IntBuffer ids) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDeleteQueriesARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkDirect(ids);
    nglDeleteQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
  }
  
  static native void nglDeleteQueriesARB(int paramInt, long paramLong1, long paramLong2);
  
  public static void glDeleteQueriesARB(int id) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glDeleteQueriesARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglDeleteQueriesARB(1, APIUtil.getInt(caps, id), function_pointer);
  }
  
  public static boolean glIsQueryARB(int id) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glIsQueryARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    boolean __result = nglIsQueryARB(id, function_pointer);
    return __result;
  }
  
  static native boolean nglIsQueryARB(int paramInt, long paramLong);
  
  public static void glBeginQueryARB(int target, int id) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glBeginQueryARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglBeginQueryARB(target, id, function_pointer);
  }
  
  static native void nglBeginQueryARB(int paramInt1, int paramInt2, long paramLong);
  
  public static void glEndQueryARB(int target) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glEndQueryARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    nglEndQueryARB(target, function_pointer);
  }
  
  static native void nglEndQueryARB(int paramInt, long paramLong);
  
  public static void glGetQueryARB(int target, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryivARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetQueryivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  @Deprecated
  public static int glGetQueryARB(int target, int pname) {
    return glGetQueryiARB(target, pname);
  }
  
  public static int glGetQueryiARB(int target, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryivARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glGetQueryObjectARB(int id, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjectivARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetQueryObjectivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static int glGetQueryObjectiARB(int id, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjectivARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
  
  public static void glGetQueryObjectuARB(int id, int pname, IntBuffer params) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjectuivARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(params, 1);
    nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
  }
  
  static native void nglGetQueryObjectuivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static int glGetQueryObjectuiARB(int id, int pname) {
    ContextCapabilities caps = GLContext.getCapabilities();
    long function_pointer = caps.glGetQueryObjectuivARB;
    BufferChecks.checkFunctionAddress(function_pointer);
    IntBuffer params = APIUtil.getBufferInt(caps);
    nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
    return params.get(0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ARBOcclusionQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */