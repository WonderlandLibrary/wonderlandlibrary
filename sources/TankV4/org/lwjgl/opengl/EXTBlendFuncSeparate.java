package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendFuncSeparate {
   public static final int GL_BLEND_DST_RGB_EXT = 32968;
   public static final int GL_BLEND_SRC_RGB_EXT = 32969;
   public static final int GL_BLEND_DST_ALPHA_EXT = 32970;
   public static final int GL_BLEND_SRC_ALPHA_EXT = 32971;

   private EXTBlendFuncSeparate() {
   }

   public static void glBlendFuncSeparateEXT(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBlendFuncSeparateEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglBlendFuncSeparateEXT(var0, var1, var2, var3, var5);
   }

   static native void nglBlendFuncSeparateEXT(int var0, int var1, int var2, int var3, long var4);
}
