package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class KHRGLSharing {
   public static final int CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR = -1000;
   public static final int CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR = 8198;
   public static final int CL_DEVICES_FOR_GL_CONTEXT_KHR = 8199;
   public static final int CL_GL_CONTEXT_KHR = 8200;
   public static final int CL_EGL_DISPLAY_KHR = 8201;
   public static final int CL_GLX_DISPLAY_KHR = 8202;
   public static final int CL_WGL_HDC_KHR = 8203;
   public static final int CL_CGL_SHAREGROUP_KHR = 8204;

   private KHRGLSharing() {
   }

   public static int clGetGLContextInfoKHR(PointerBuffer var0, int var1, ByteBuffer var2, PointerBuffer var3) {
      long var4 = CLCapabilities.clGetGLContextInfoKHR;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkDirect(var0);
      BufferChecks.checkNullTerminated(var0);
      if (var2 != null) {
         BufferChecks.checkDirect(var2);
      }

      if (var3 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var3, 1);
      }

      if (var3 == null && APIUtil.isDevicesParam(var1)) {
         var3 = APIUtil.getBufferPointer();
      }

      int var6 = nclGetGLContextInfoKHR(MemoryUtil.getAddress(var0), var1, (long)(var2 == null ? 0 : var2.remaining()), MemoryUtil.getAddressSafe(var2), MemoryUtil.getAddressSafe(var3), var4);
      if (var6 == 0 && var2 != null && APIUtil.isDevicesParam(var1)) {
         APIUtil.getCLPlatform(var0).registerCLDevices(var2, var3);
      }

      return var6;
   }

   static native int nclGetGLContextInfoKHR(long var0, int var2, long var3, long var5, long var7, long var9);
}
