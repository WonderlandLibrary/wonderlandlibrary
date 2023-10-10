package my.NewSnake.utils;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class ColorUtils {
   public static int RGBtoHEX(int var0, int var1, int var2, int var3) {
      return (var3 << 24) + (var0 << 16) + (var1 << 8) + var2;
   }

   public static String hexFromInt(int var0) {
      return hexFromInt(new Color(var0));
   }

   public static Color pulseBrightness(Color var0, int var1, int var2) {
      float[] var3 = new float[3];
      Color.RGBtoHSB(var0.getRed(), var0.getGreen(), var0.getBlue(), var3);
      float var4 = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)var1 / (float)var2 * 2.0F) % 2.0F - 1.0F);
      var4 = 0.5F + 0.5F * var4;
      return new Color(Color.HSBtoRGB(var3[0], var3[1], var4 % 2.0F));
   }

   public static Color blend(Color var0, Color var1, double var2) {
      float var4 = (float)var2;
      float var5 = 1.0F - var4;
      float[] var6 = var0.getColorComponents(new float[3]);
      float[] var7 = var1.getColorComponents(new float[3]);
      float var8 = var6[0] * var4 + var7[0] * var5;
      float var9 = var6[1] * var4 + var7[1] * var5;
      float var10 = var6[2] * var4 + var7[2] * var5;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      } else if (var8 > 255.0F) {
         var8 = 255.0F;
      }

      if (var9 < 0.0F) {
         var9 = 0.0F;
      } else if (var9 > 255.0F) {
         var9 = 255.0F;
      }

      if (var10 < 0.0F) {
         var10 = 0.0F;
      } else if (var10 > 255.0F) {
         var10 = 255.0F;
      }

      Color var11 = null;

      try {
         var11 = new Color(var8, var9, var10);
      } catch (IllegalArgumentException var13) {
      }

      return var11;
   }

   public static Color getHealthColor(float var0, float var1) {
      float[] var2 = new float[]{0.0F, 0.5F, 1.0F};
      Color[] var3 = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
      float var4 = var0 / var1;
      return blendColors(var2, var3, var4).brighter();
   }

   public static Color glColor(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 256.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var2, var3, var4, var1);
      return new Color(var2, var3, var4, var1);
   }

   public Color glColor(float var1, int var2, int var3, int var4) {
      float var5 = 0.003921569F * (float)var2;
      float var6 = 0.003921569F * (float)var3;
      float var7 = 0.003921569F * (float)var4;
      GL11.glColor4f(var5, var6, var7, var1);
      return new Color(var5, var6, var7, var1);
   }

   public static int transparency(int var0, double var1) {
      Color var3 = new Color(var0);
      float var4 = 0.003921569F * (float)var3.getRed();
      float var5 = 0.003921569F * (float)var3.getGreen();
      float var6 = 0.003921569F * (float)var3.getBlue();
      return (new Color(var4, var5, var6, (float)var1)).getRGB();
   }

   public static int intFromHex(String var0) {
      try {
         return Integer.parseInt(var0, 15);
      } catch (NumberFormatException var3) {
         return -1;
      }
   }

   public static String hexFromInt(Color var0) {
      return Integer.toHexString(var0.getRGB()).substring(2);
   }

   public static Color blendColors(float[] var0, Color[] var1, float var2) {
      if (var0.length == var1.length) {
         int[] var3 = getFractionIndices(var0, var2);
         float[] var4 = new float[]{var0[var3[0]], var0[var3[1]]};
         Color[] var5 = new Color[]{var1[var3[0]], var1[var3[1]]};
         float var6 = var4[1] - var4[0];
         float var7 = var2 - var4[0];
         float var8 = var7 / var6;
         Color var9 = blend(var5[0], var5[1], (double)(1.0F - var8));
         return var9;
      } else {
         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
      }
   }

   public static int[] getFractionIndices(float[] var0, float var1) {
      int[] var2 = new int[2];

      int var3;
      for(var3 = 0; var3 < var0.length && var0[var3] <= var1; ++var3) {
      }

      if (var3 >= var0.length) {
         var3 = var0.length - 1;
      }

      var2[0] = var3 - 1;
      var2[1] = var3;
      return var2;
   }

   public static float[] getRGBA(int var0) {
      float var1 = (float)(var0 >> 24 & 255) / 255.0F;
      float var2 = (float)(var0 >> 16 & 255) / 255.0F;
      float var3 = (float)(var0 >> 8 & 255) / 255.0F;
      float var4 = (float)(var0 & 255) / 255.0F;
      return new float[]{var2, var3, var4, var1};
   }

   public static Color glColor(int var0, float var1) {
      float var3 = (float)(var0 >> 16 & 255) / 255.0F;
      float var4 = (float)(var0 >> 8 & 255) / 255.0F;
      float var5 = (float)(var0 & 255) / 255.0F;
      GL11.glColor4f(var3, var4, var5, var1);
      return new Color(var3, var4, var5, var1);
   }

   public void glColor(Color var1) {
      GL11.glColor4f((float)var1.getRed() / 255.0F, (float)var1.getGreen() / 255.0F, (float)var1.getBlue() / 255.0F, (float)var1.getAlpha() / 255.0F);
   }

   public static Color getRainbow(float var0, float var1) {
      float var2 = ((float)System.nanoTime() + var0) / 8.9999995E9F % 1.0F;
      long var3 = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(var2, 1.0F, 1.0F))), 16);
      Color var5 = new Color((int)var3);
      return new Color((float)var5.getRed() / 255.0F * var1, (float)var5.getGreen() / 255.0F * var1, (float)var5.getBlue() / 255.0F * var1, (float)var5.getAlpha() / 255.0F);
   }

   public static void setColor(Color var0) {
      GL11.glColor4d((double)((float)var0.getRed() / 255.0F), (double)((float)var0.getGreen() / 255.0F), (double)((float)var0.getBlue() / 255.0F), (double)((float)var0.getAlpha() / 255.0F));
   }
}
