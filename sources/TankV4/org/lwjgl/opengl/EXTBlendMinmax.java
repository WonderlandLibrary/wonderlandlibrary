package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendMinmax {
   public static final int GL_FUNC_ADD_EXT = 32774;
   public static final int GL_MIN_EXT = 32775;
   public static final int GL_MAX_EXT = 32776;
   public static final int GL_BLEND_EQUATION_EXT = 32777;

   private EXTBlendMinmax() {
   }

   public static void glBlendEquationEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glBlendEquationEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglBlendEquationEXT(var0, var2);
   }

   static native void nglBlendEquationEXT(int var0, long var1);
}
