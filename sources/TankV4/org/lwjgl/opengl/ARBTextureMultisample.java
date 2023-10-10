package org.lwjgl.opengl;

import java.nio.FloatBuffer;

public final class ARBTextureMultisample {
   public static final int GL_SAMPLE_POSITION = 36432;
   public static final int GL_SAMPLE_MASK = 36433;
   public static final int GL_SAMPLE_MASK_VALUE = 36434;
   public static final int GL_TEXTURE_2D_MULTISAMPLE = 37120;
   public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 37121;
   public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 37122;
   public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 37123;
   public static final int GL_MAX_SAMPLE_MASK_WORDS = 36441;
   public static final int GL_MAX_COLOR_TEXTURE_SAMPLES = 37134;
   public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES = 37135;
   public static final int GL_MAX_INTEGER_SAMPLES = 37136;
   public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 37124;
   public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 37125;
   public static final int GL_TEXTURE_SAMPLES = 37126;
   public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 37127;
   public static final int GL_SAMPLER_2D_MULTISAMPLE = 37128;
   public static final int GL_INT_SAMPLER_2D_MULTISAMPLE = 37129;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 37130;
   public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 37131;
   public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37132;
   public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37133;

   private ARBTextureMultisample() {
   }

   public static void glTexImage2DMultisample(int var0, int var1, int var2, int var3, int var4, boolean var5) {
      GL32.glTexImage2DMultisample(var0, var1, var2, var3, var4, var5);
   }

   public static void glTexImage3DMultisample(int var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      GL32.glTexImage3DMultisample(var0, var1, var2, var3, var4, var5, var6);
   }

   public static void glGetMultisample(int var0, int var1, FloatBuffer var2) {
      GL32.glGetMultisample(var0, var1, var2);
   }

   public static void glSampleMaski(int var0, int var1) {
      GL32.glSampleMaski(var0, var1);
   }
}
