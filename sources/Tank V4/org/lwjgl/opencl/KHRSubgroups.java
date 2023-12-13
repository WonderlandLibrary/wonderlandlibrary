package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class KHRSubgroups {
   private KHRSubgroups() {
   }

   public static int clGetKernelSubGroupInfoKHR(CLKernel var0, CLDevice var1, int var2, ByteBuffer var3, ByteBuffer var4, PointerBuffer var5) {
      long var6 = CLCapabilities.clGetKernelSubGroupInfoKHR;
      BufferChecks.checkFunctionAddress(var6);
      if (var3 != null) {
         BufferChecks.checkDirect(var3);
      }

      if (var4 != null) {
         BufferChecks.checkDirect(var4);
      }

      if (var5 != null) {
         BufferChecks.checkBuffer((PointerBuffer)var5, 1);
      }

      int var8 = nclGetKernelSubGroupInfoKHR(var0.getPointer(), var1 == null ? 0L : var1.getPointer(), var2, (long)(var3 == null ? 0 : var3.remaining()), MemoryUtil.getAddressSafe(var3), (long)(var4 == null ? 0 : var4.remaining()), MemoryUtil.getAddressSafe(var4), MemoryUtil.getAddressSafe(var5), var6);
      return var8;
   }

   static native int nclGetKernelSubGroupInfoKHR(long var0, long var2, int var4, long var5, long var7, long var9, long var11, long var13, long var15);
}
