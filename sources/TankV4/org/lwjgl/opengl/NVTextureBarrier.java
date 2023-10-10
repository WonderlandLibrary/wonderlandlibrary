package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVTextureBarrier {
   private NVTextureBarrier() {
   }

   public static void glTextureBarrierNV() {
      ContextCapabilities var0 = GLContext.getCapabilities();
      long var1 = var0.glTextureBarrierNV;
      BufferChecks.checkFunctionAddress(var1);
      nglTextureBarrierNV(var1);
   }

   static native void nglTextureBarrierNV(long var0);
}
