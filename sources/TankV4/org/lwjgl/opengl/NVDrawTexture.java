package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVDrawTexture {
   private NVDrawTexture() {
   }

   public static void glDrawTextureNV(int var0, int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      ContextCapabilities var11 = GLContext.getCapabilities();
      long var12 = var11.glDrawTextureNV;
      BufferChecks.checkFunctionAddress(var12);
      nglDrawTextureNV(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var12);
   }

   static native void nglDrawTextureNV(int var0, int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, long var11);
}
