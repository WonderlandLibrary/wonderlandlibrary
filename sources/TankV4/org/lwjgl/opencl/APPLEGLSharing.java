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

   private APPLEGLSharing() {
   }

   public static int clGetGLContextInfoAPPLE(CLContext var0, PointerBuffer var1, int var2, ByteBuffer var3, PointerBuffer var4) {
      long var5 = CLCapabilities.clGetGLContextInfoAPPLE;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((PointerBuffer)var1, 1);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var4, 1);
      }

      if (var4 == null && APIUtil.isDevicesParam(var2)) {
         var4 = APIUtil.getBufferPointer();
      }

      int var7 = nclGetGLContextInfoAPPLE(var0.getPointer(), MemoryUtil.getAddress(var1), var2, (long)(var3 == null ? 0 : var3.remaining()), MemoryUtil.getAddressSafe(var3), MemoryUtil.getAddressSafe(var4), var5);
      if (var7 == 0 && var3 != null && APIUtil.isDevicesParam(var2)) {
         ((CLPlatform)var0.getParent()).registerCLDevices(var3, var4);
      }

      return var7;
   }

   static native int nclGetGLContextInfoAPPLE(long var0, long var2, int var4, long var5, long var7, long var9, long var11);
}
