package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureStorage {
   public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 37167;

   private ARBTextureStorage() {
   }

   public static void glTexStorage1D(int var0, int var1, int var2, int var3) {
      GL42.glTexStorage1D(var0, var1, var2, var3);
   }

   public static void glTexStorage2D(int var0, int var1, int var2, int var3, int var4) {
      GL42.glTexStorage2D(var0, var1, var2, var3, var4);
   }

   public static void glTexStorage3D(int var0, int var1, int var2, int var3, int var4, int var5) {
      GL42.glTexStorage3D(var0, var1, var2, var3, var4, var5);
   }

   public static void glTextureStorage1DEXT(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glTextureStorage1DEXT;
      BufferChecks.checkFunctionAddress(var6);
      nglTextureStorage1DEXT(var0, var1, var2, var3, var4, var6);
   }

   static native void nglTextureStorage1DEXT(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glTextureStorage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glTextureStorage2DEXT;
      BufferChecks.checkFunctionAddress(var7);
      nglTextureStorage2DEXT(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglTextureStorage2DEXT(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glTextureStorage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTextureStorage3DEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglTextureStorage3DEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTextureStorage3DEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);
}
