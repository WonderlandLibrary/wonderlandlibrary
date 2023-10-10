package org.lwjgl.opencl;

import org.lwjgl.BufferChecks;

public final class KHRTerminateContext {
   public static final int CL_DEVICE_TERMINATE_CAPABILITY_KHR = 8207;
   public static final int CL_CONTEXT_TERMINATE_KHR = 8208;

   private KHRTerminateContext() {
   }

   public static int clTerminateContextKHR(CLContext var0) {
      long var1 = CLCapabilities.clTerminateContextKHR;
      BufferChecks.checkFunctionAddress(var1);
      int var3 = nclTerminateContextKHR(var0.getPointer(), var1);
      return var3;
   }

   static native int nclTerminateContextKHR(long var0, long var2);
}
