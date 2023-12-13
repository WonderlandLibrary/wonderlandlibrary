package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTFramebufferBlit {
   public static final int GL_READ_FRAMEBUFFER_EXT = 36008;
   public static final int GL_DRAW_FRAMEBUFFER_EXT = 36009;
   public static final int GL_DRAW_FRAMEBUFFER_BINDING_EXT = 36006;
   public static final int GL_READ_FRAMEBUFFER_BINDING_EXT = 36010;

   private EXTFramebufferBlit() {
   }

   public static void glBlitFramebufferEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      ContextCapabilities var10 = GLContext.getCapabilities();
      long var11 = var10.glBlitFramebufferEXT;
      BufferChecks.checkFunctionAddress(var11);
      nglBlitFramebufferEXT(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   static native void nglBlitFramebufferEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10);
}
