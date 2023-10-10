package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class EXTSeparateShaderObjects {
   public static final int GL_ACTIVE_PROGRAM_EXT = 35725;

   private EXTSeparateShaderObjects() {
   }

   public static void glUseShaderProgramEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glUseShaderProgramEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglUseShaderProgramEXT(var0, var1, var3);
   }

   static native void nglUseShaderProgramEXT(int var0, int var1, long var2);

   public static void glActiveProgramEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glActiveProgramEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglActiveProgramEXT(var0, var2);
   }

   static native void nglActiveProgramEXT(int var0, long var1);

   public static int glCreateShaderProgramEXT(int var0, ByteBuffer var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCreateShaderProgramEXT;
      BufferChecks.checkFunctionAddress(var3);
      BufferChecks.checkDirect(var1);
      BufferChecks.checkNullTerminated(var1);
      int var5 = nglCreateShaderProgramEXT(var0, MemoryUtil.getAddress(var1), var3);
      return var5;
   }

   static native int nglCreateShaderProgramEXT(int var0, long var1, long var3);

   public static int glCreateShaderProgramEXT(int var0, CharSequence var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glCreateShaderProgramEXT;
      BufferChecks.checkFunctionAddress(var3);
      int var5 = nglCreateShaderProgramEXT(var0, APIUtil.getBufferNT(var2, var1), var3);
      return var5;
   }
}
