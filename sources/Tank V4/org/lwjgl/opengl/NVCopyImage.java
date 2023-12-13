package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class NVCopyImage {
   private NVCopyImage() {
   }

   public static void glCopyImageSubDataNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14) {
      ContextCapabilities var15 = GLContext.getCapabilities();
      long var16 = var15.glCopyImageSubDataNV;
      BufferChecks.checkFunctionAddress(var16);
      nglCopyImageSubDataNV(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var16);
   }

   static native void nglCopyImageSubDataNV(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, long var15);
}
