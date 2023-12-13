package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBTextureBufferRange {
   public static final int GL_TEXTURE_BUFFER_OFFSET = 37277;
   public static final int GL_TEXTURE_BUFFER_SIZE = 37278;
   public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 37279;

   private ARBTextureBufferRange() {
   }

   public static void glTexBufferRange(int var0, int var1, int var2, long var3, long var5) {
      GL43.glTexBufferRange(var0, var1, var2, var3, var5);
   }

   public static void glTextureBufferRangeEXT(int var0, int var1, int var2, int var3, long var4, long var6) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureBufferRangeEXT;
      BufferChecks.checkFunctionAddress(var9);
      nglTextureBufferRangeEXT(var0, var1, var2, var3, var4, var6, var9);
   }

   static native void nglTextureBufferRangeEXT(int var0, int var1, int var2, int var3, long var4, long var6, long var8);
}
