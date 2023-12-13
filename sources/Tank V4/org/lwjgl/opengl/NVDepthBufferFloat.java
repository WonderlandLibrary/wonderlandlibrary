package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVDepthBufferFloat {
   public static final int GL_DEPTH_COMPONENT32F_NV = 36267;
   public static final int GL_DEPTH32F_STENCIL8_NV = 36268;
   public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV_NV = 36269;
   public static final int GL_DEPTH_BUFFER_FLOAT_MODE_NV = 36271;

   private NVDepthBufferFloat() {
   }

   public static void glDepthRangedNV(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDepthRangedNV;
      BufferChecks.checkFunctionAddress(var5);
      nglDepthRangedNV(var0, var2, var5);
   }

   static native void nglDepthRangedNV(double var0, double var2, long var4);

   public static void glClearDepthdNV(double var0) {
      ContextCapabilities var2 = GLContext.getCapabilities();
      long var3 = var2.glClearDepthdNV;
      BufferChecks.checkFunctionAddress(var3);
      nglClearDepthdNV(var0, var3);
   }

   static native void nglClearDepthdNV(double var0, long var2);

   public static void glDepthBoundsdNV(double var0, double var2) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDepthBoundsdNV;
      BufferChecks.checkFunctionAddress(var5);
      nglDepthBoundsdNV(var0, var2, var5);
   }

   static native void nglDepthBoundsdNV(double var0, double var2, long var4);
}
