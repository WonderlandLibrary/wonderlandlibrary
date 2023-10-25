package org.lwjgl.opencl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.GLSync;

public final class KHRGLEvent {
  public static final int CL_COMMAND_GL_FENCE_SYNC_OBJECT_KHR = 8205;
  
  public static CLEvent clCreateEventFromGLsyncKHR(CLContext context, GLSync sync, IntBuffer errcode_ret) {
    long function_pointer = CLCapabilities.clCreateEventFromGLsyncKHR;
    BufferChecks.checkFunctionAddress(function_pointer);
    if (errcode_ret != null)
      BufferChecks.checkBuffer(errcode_ret, 1); 
    CLEvent __result = new CLEvent(nclCreateEventFromGLsyncKHR(context.getPointer(), sync.getPointer(), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
    return __result;
  }
  
  static native long nclCreateEventFromGLsyncKHR(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\KHRGLEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */