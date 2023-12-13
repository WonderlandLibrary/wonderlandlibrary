package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVConditionalRender {
   public static final int GL_QUERY_WAIT_NV = 36371;
   public static final int GL_QUERY_NO_WAIT_NV = 36372;
   public static final int GL_QUERY_BY_REGION_WAIT_NV = 36373;
   public static final int GL_QUERY_BY_REGION_NO_WAIT_NV = 36374;

   private NVConditionalRender() {
   }

   public static void glBeginConditionalRenderNV(int var0, int var1) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glBeginConditionalRenderNV;
      BufferChecks.checkFunctionAddress(var3);
      nglBeginConditionalRenderNV(var0, var1, var3);
   }

   static native void nglBeginConditionalRenderNV(int var0, int var1, long var2);

   public static void glEndConditionalRenderNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glEndConditionalRenderNV;
      BufferChecks.checkFunctionAddress(var1);
      nglEndConditionalRenderNV(var1);
   }

   static native void nglEndConditionalRenderNV(long var0);
}
