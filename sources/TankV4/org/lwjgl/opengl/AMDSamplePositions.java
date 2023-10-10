package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class AMDSamplePositions {
   public static final int GL_SUBSAMPLE_DISTANCE_AMD = 34879;

   private AMDSamplePositions() {
   }

   public static void glSetMultisampleAMD(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glSetMultisamplefvAMD;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 2);
      nglSetMultisamplefvAMD(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglSetMultisamplefvAMD(int var0, int var1, long var2, long var4);
}
