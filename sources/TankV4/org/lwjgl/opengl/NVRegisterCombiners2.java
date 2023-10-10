package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class NVRegisterCombiners2 {
   public static final int GL_PER_STAGE_CONSTANTS_NV = 34101;

   private NVRegisterCombiners2() {
   }

   public static void glCombinerStageParameterNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glCombinerStageParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglCombinerStageParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglCombinerStageParameterfvNV(int var0, int var1, long var2, long var4);

   public static void glGetCombinerStageParameterNV(int var0, int var1, FloatBuffer var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetCombinerStageParameterfvNV;
      BufferChecks.checkFunctionAddress(var4);
      BufferChecks.checkBuffer((FloatBuffer)var2, 4);
      nglGetCombinerStageParameterfvNV(var0, var1, MemoryUtil.getAddress(var2), var4);
   }

   static native void nglGetCombinerStageParameterfvNV(int var0, int var1, long var2, long var4);
}
