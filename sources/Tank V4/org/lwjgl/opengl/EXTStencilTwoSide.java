package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTStencilTwoSide {
   public static final int GL_STENCIL_TEST_TWO_SIDE_EXT = 35088;
   public static final int GL_ACTIVE_STENCIL_FACE_EXT = 35089;

   private EXTStencilTwoSide() {
   }

   public static void glActiveStencilFaceEXT(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glActiveStencilFaceEXT;
      BufferChecks.checkFunctionAddress(var2);
      nglActiveStencilFaceEXT(var0, var2);
   }

   static native void nglActiveStencilFaceEXT(int var0, long var1);
}
