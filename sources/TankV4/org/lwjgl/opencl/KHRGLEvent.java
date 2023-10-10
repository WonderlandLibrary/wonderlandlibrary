package org.lwjgl.opencl;

import java.nio.IntBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.GLSync;

public final class KHRGLEvent {
   public static final int CL_COMMAND_GL_FENCE_SYNC_OBJECT_KHR = 8205;

   private KHRGLEvent() {
   }

   public static CLEvent clCreateEventFromGLsyncKHR(CLContext var0, GLSync var1, IntBuffer var2) {
      long var3 = CLCapabilities.clCreateEventFromGLsyncKHR;
      BufferChecks.checkFunctionAddress(var3);
      if (var2 != null) {
         BufferChecks.checkBuffer((IntBuffer)var2, 1);
      }

      CLEvent var5 = new CLEvent(nclCreateEventFromGLsyncKHR(var0.getPointer(), var1.getPointer(), MemoryUtil.getAddressSafe(var2), var3), var0);
      return var5;
   }

   static native long nclCreateEventFromGLsyncKHR(long var0, long var2, long var4, long var6);
}
