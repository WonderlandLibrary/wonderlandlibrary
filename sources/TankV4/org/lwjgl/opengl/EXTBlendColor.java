package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTBlendColor {
   public static final int GL_CONSTANT_COLOR_EXT = 32769;
   public static final int GL_ONE_MINUS_CONSTANT_COLOR_EXT = 32770;
   public static final int GL_CONSTANT_ALPHA_EXT = 32771;
   public static final int GL_ONE_MINUS_CONSTANT_ALPHA_EXT = 32772;
   public static final int GL_BLEND_COLOR_EXT = 32773;

   private EXTBlendColor() {
   }

   public static void glBlendColorEXT(float var0, float var1, float var2, float var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glBlendColorEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglBlendColorEXT(var0, var1, var2, var3, var5);
   }

   static native void nglBlendColorEXT(float var0, float var1, float var2, float var3, long var4);
}
