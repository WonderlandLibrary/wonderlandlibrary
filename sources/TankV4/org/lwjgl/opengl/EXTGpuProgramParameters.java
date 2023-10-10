package org.lwjgl.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTGpuProgramParameters {
   private EXTGpuProgramParameters() {
   }

   public static void glProgramEnvParameters4EXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramEnvParameters4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer(var3, var2 << 2);
      nglProgramEnvParameters4fvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramEnvParameters4fvEXT(int var0, int var1, int var2, long var3, long var5);

   public static void glProgramLocalParameters4EXT(int var0, int var1, int var2, FloatBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glProgramLocalParameters4fvEXT;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer(var3, var2 << 2);
      nglProgramLocalParameters4fvEXT(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglProgramLocalParameters4fvEXT(int var0, int var1, int var2, long var3, long var5);
}
