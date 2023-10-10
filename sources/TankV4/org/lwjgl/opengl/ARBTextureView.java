package org.lwjgl.opengl;

public final class ARBTextureView {
   public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 33499;
   public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 33500;
   public static final int GL_TEXTURE_VIEW_MIN_LAYER = 33501;
   public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 33502;
   public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 33503;

   private ARBTextureView() {
   }

   public static void glTextureView(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      GL43.glTextureView(var0, var1, var2, var3, var4, var5, var6, var7);
   }
}
