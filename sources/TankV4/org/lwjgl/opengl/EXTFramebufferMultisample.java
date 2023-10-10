package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTFramebufferMultisample {
   public static final int GL_RENDERBUFFER_SAMPLES_EXT = 36011;
   public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT = 36182;
   public static final int GL_MAX_SAMPLES_EXT = 36183;

   private EXTFramebufferMultisample() {
   }

   public static void glRenderbufferStorageMultisampleEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glRenderbufferStorageMultisampleEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglRenderbufferStorageMultisampleEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglRenderbufferStorageMultisampleEXT(int var0, int var1, int var2, int var3, int var4, long var5);
}
