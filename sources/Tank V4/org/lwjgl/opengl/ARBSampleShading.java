package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBSampleShading {
   public static final int GL_SAMPLE_SHADING_ARB = 35894;
   public static final int GL_MIN_SAMPLE_SHADING_VALUE_ARB = 35895;

   private ARBSampleShading() {
   }

   public static void glMinSampleShadingARB(float var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMinSampleShadingARB;
      BufferChecks.checkFunctionAddress(var2);
      nglMinSampleShadingARB(var0, var2);
   }

   static native void nglMinSampleShadingARB(float var0, long var1);
}
