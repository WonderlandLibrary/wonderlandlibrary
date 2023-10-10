package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTStencilClearTag {
   public static final int GL_STENCIL_TAG_BITS_EXT = 35058;
   public static final int GL_STENCIL_CLEAR_TAG_VALUE_EXT = 35059;

   private EXTStencilClearTag() {
   }

   public static void glStencilClearTagEXT(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glStencilClearTagEXT;
      BufferChecks.checkFunctionAddress(var3);
      nglStencilClearTagEXT(var0, var1, var3);
   }

   static native void nglStencilClearTagEXT(int var0, int var1, long var2);
}
