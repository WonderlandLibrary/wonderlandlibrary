package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVPrimitiveRestart {
   public static final int GL_PRIMITIVE_RESTART_NV = 34136;
   public static final int GL_PRIMITIVE_RESTART_INDEX_NV = 34137;

   private NVPrimitiveRestart() {
   }

   public static void glPrimitiveRestartNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glPrimitiveRestartNV;
      BufferChecks.checkFunctionAddress(var1);
      nglPrimitiveRestartNV(var1);
   }

   static native void nglPrimitiveRestartNV(long var0);

   public static void glPrimitiveRestartIndexNV(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glPrimitiveRestartIndexNV;
      BufferChecks.checkFunctionAddress(var2);
      nglPrimitiveRestartIndexNV(var0, var2);
   }

   static native void nglPrimitiveRestartIndexNV(int var0, long var1);
}
