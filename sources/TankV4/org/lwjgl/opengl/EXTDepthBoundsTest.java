package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class EXTDepthBoundsTest {
   public static final int GL_DEPTH_BOUNDS_TEST_EXT = 34960;
   public static final int GL_DEPTH_BOUNDS_EXT = 34961;

   private EXTDepthBoundsTest() {
   }

   public static void glDepthBoundsEXT(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDepthBoundsEXT;
      BufferChecks.checkFunctionAddress(var5);
      nglDepthBoundsEXT(var0, var2, var5);
   }

   static native void nglDepthBoundsEXT(double var0, double var2, long var4);
}
