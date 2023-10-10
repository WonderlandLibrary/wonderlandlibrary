package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureStorageMultisample {
   private ARBTextureStorageMultisample() {
   }

   public static void glTexStorage2DMultisample(int var0, int var1, int var2, int var3, int var4, boolean var5) {
      GL43.glTexStorage2DMultisample(var0, var1, var2, var3, var4, var5);
   }

   public static void glTexStorage3DMultisample(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      GL43.glTexStorage3DMultisample(var0, var1, var2, var3, var4, var5, var6);
   }

   public static void glTextureStorage2DMultisampleEXT(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTextureStorage2DMultisampleEXT;
      BufferChecks.checkFunctionAddress(var8);
      nglTextureStorage2DMultisampleEXT(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTextureStorage2DMultisampleEXT(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6, long var7);

   public static void glTextureStorage3DMultisampleEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureStorage3DMultisampleEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglTextureStorage3DMultisampleEXT(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglTextureStorage3DMultisampleEXT(int var0, int var1, int var2, int var3, int var4, int var5, int var6, boolean var7, long var8);
}
