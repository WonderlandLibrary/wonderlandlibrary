package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class APPLEGLSharing {
  public static final int CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE = 268435456;
  
  public static final int CL_CGL_DEVICE_FOR_CURRENT_VIRTUAL_SCREEN_APPLE = 268435458;
  
  public static final int CL_CGL_DEVICES_FOR_SUPPORTED_VIRTUAL_SCREENS_APPLE = 268435459;
  
  public static final int CL_INVALID_GL_CONTEXT_APPLE = -1000;
  
  public static int clGetGLContextInfoAPPLE(CLContext context, PointerBuffer platform_gl_ctx, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret) {
    long function_pointer = CLCapabilities.clGetGLContextInfoAPPLE;
    BufferChecks.checkFunctionAddress(function_pointer);
    BufferChecks.checkBuffer(platform_gl_ctx, 1);
    if (param_value != null)
      BufferChecks.checkDirect(param_value); 
    if (param_value_size_ret != null)
      BufferChecks.checkBuffer(param_value_size_ret, 1); 
    if (param_value_size_ret == null && APIUtil.isDevicesParam(param_name))
      param_value_size_ret = APIUtil.getBufferPointer(); 
    int __result = nclGetGLContextInfoAPPLE(context.getPointer(), MemoryUtil.getAddress(platform_gl_ctx), param_name, ((param_value == null) ? 0L : param_value.remaining()), MemoryUtil.getAddressSafe(param_value), MemoryUtil.getAddressSafe(param_value_size_ret), function_pointer);
    if (__result == 0 && param_value != null && APIUtil.isDevicesParam(param_name))
      context.getParent().registerCLDevices(param_value, param_value_size_ret); 
    return __result;
  }
  
  static native int nclGetGLContextInfoAPPLE(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4, long paramLong5, long paramLong6);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\APPLEGLSharing.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */