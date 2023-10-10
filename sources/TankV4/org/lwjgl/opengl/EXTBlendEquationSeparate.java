package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendEquationSeparate {
   public static final int GL_BLEND_EQUATION_RGB_EXT = 32777;
   public static final int GL_BLEND_EQUATION_ALPHA_EXT = 34877;

   private EXTBlendEquationSeparate() {
   }

   public static void glBlendEquationSeparateEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBlendEquationSeparateEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglBlendEquationSeparateEXT(var0, var1, var3);
   }

   static native void nglBlendEquationSeparateEXT(int var0, int var1, long var2);
}
