package org.lwjgl.opencl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class KHRICD {
   public static final int CL_PLATFORM_ICD_SUFFIX_KHR = 2336;
   public static final int CL_PLATFORM_NOT_FOUND_KHR = -1001;

   private KHRICD() {
   }

   public static int clIcdGetPlatformIDsKHR(PointerBuffer var0, IntBuffer var1) {
      long var2 = CLCapabilities.clIcdGetPlatformIDsKHR;
      BufferChecks.checkFunctionAddress(var2);
      if (var0 != null) {
         BufferChecks.checkDirect(var0);
      }

      if (var1 != null) {
         BufferChecks.checkBuffer((IntBuffer)var1, 1);
      }

      int var4 = nclIcdGetPlatformIDsKHR(var0 == null ? 0 : var0.remaining(), MemoryUtil.getAddressSafe(var0), MemoryUtil.getAddressSafe(var1), var2);
      return var4;
   }

   static native int nclIcdGetPlatformIDsKHR(int var0, long var1, long var3, long var5);
}
