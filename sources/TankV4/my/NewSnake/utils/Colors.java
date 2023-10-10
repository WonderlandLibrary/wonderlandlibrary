package my.NewSnake.utils;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Random;
import net.minecraft.entity.Entity;

public class Colors {
   public static String RANDOM = "§k";
   public static String AQUA = "§b";
   public static String ITALIC = "§o";
   public static String DARK_BLUE = "§1";
   public static String STRIKETHROUGH = "§m";
   public static String GRAY = "§7";
   public static String DARK_PURPLE = "§5";
   public static String DARK_RED = "§4";
   public static String YELLOW = "§e";
   public static String WHITE = "§f";
   public static String BLUE = "§9";
   public static String GOLD = "§6";
   public static String BOLD = "§l";
   public static String DARK_GRAY = "§8";
   public static String DARK_AQUA = "§3";
   public static String RED = "§c";
   public static String RESET = "§r";
   public static String BLACK = "§0";
   public static String UNDERLINE = "§n";
   public static String DARK_GREEN = "§2";
   public static String GREEN = "§a";
   public static String LIGHT_PURPLE = "§d";

   public static int getRandomColor() {
      char[] var0 = "012345678".toCharArray();
      String var1 = "0x";

      for(int var2 = 0; var2 < 6; ++var2) {
         var1 = var1 + var0[(new Random()).nextInt(var0.length)];
      }

      return Integer.decode(var1);
   }

   public static int[] getFractionIndicies(float[] var0, float var1) {
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

   public static int getColor(int var0, int var1) {
      return getColor(var0, var0, var0, var1);
   }

   public static int getColor(Color var0) {
      return getColor(var0.getRed(), var0.getGreen(), var0.getBlue(), var0.getAlpha());
   }

   public static Color rainbow(long var0, float var2, float var3) {
      float var4 = ((float)var0 + (1.0F + var2) * 2.0E8F) / 1.0E10F % 1.0F;
      long var5 = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(var4, 1.0F, 1.0F))), 16);
      Color var7 = new Color((int)var5);
      return new Color((float)var7.getRed() / 255.0F * var3, (float)var7.getGreen() / 255.0F * var3, (float)var7.getBlue() / 255.0F * var3, (float)var7.getAlpha() / 255.0F);
   }

   public static int getTeamColor(Entity var0) {
      boolean var1 = true;
      int var2;
      if (var0.getDisplayName().getUnformattedText().equalsIgnoreCase("ï¿½f[ï¿½cRï¿½f]ï¿½c" + var0.getName())) {
         var2 = getColor(new Color(255, 60, 60));
      } else if (var0.getDisplayName().getUnformattedText().equalsIgnoreCase("ï¿½f[ï¿½9Bï¿½f]ï¿½9" + var0.getName())) {
         var2 = getColor(new Color(60, 60, 255));
      } else if (var0.getDisplayName().getUnformattedText().equalsIgnoreCase("ï¿½f[ï¿½eYï¿½f]ï¿½e" + var0.getName())) {
         var2 = getColor(new Color(255, 255, 60));
      } else if (var0.getDisplayName().getUnformattedText().equalsIgnoreCase("ï¿½f[ï¿½aGï¿½f]ï¿½a" + var0.getName())) {
         var2 = getColor(new Color(60, 255, 60));
      } else {
         var2 = getColor(new Color(255, 255, 255));
      }

      return var2;
   }

   public static int rainbow(int var0, float var1, float var2) {
      double var3 = Math.ceil((double)((System.currentTimeMillis() + (long)var0) / 16L));
      var3 %= 360.0D;
      return Color.getHSBColor((float)(var3 / 360.0D), var1, var2).getRGB();
   }

   public static Color blend(Color var0, Color var1, double var2) {
      float var4 = (float)var2;
      float var5 = 1.0F - var4;
      float[] var6 = new float[3];
      float[] var7 = new float[3];
      var0.getColorComponents(var6);
      var1.getColorComponents(var7);
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
      } catch (IllegalArgumentException var15) {
         NumberFormat var13 = NumberFormat.getNumberInstance();
         System.out.println(var13.format((double)var8) + "; " + var13.format((double)var9) + "; " + var13.format((double)var10));
         var15.printStackTrace();
      }

      return var11;
   }

   public static Color rainbowCol(int var0, float var1, float var2) {
      double var3 = Math.ceil((double)((System.currentTimeMillis() + (long)var0) / 16L));
      var3 %= 360.0D;
      return Color.getHSBColor((float)(var3 / 360.0D), var1, var2);
   }

   public static int reAlpha(int var0, float var1) {
      Color var2 = new Color(var0);
      float var3 = 0.003921569F * (float)var2.getRed();
      float var4 = 0.003921569F * (float)var2.getGreen();
      float var5 = 0.003921569F * (float)var2.getBlue();
      return (new Color(var3, var4, var5, var1)).getRGB();
   }

   public static int getColor(int var0, int var1, int var2, int var3) {
      byte var4 = 0;
      int var5 = var4 | var3 << 24;
      var5 |= var0 << 16;
      var5 |= var1 << 8;
      var5 |= var2;
      return var5;
   }

   public static Color getHealthColor(float var0, float var1) {
      float[] var2 = new float[]{0.0F, 0.5F, 1.0F};
      Color[] var3 = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
      float var4 = var0 / var1;
      return blendColors(var2, var3, var4).brighter();
   }

   public static int getColor(int var0) {
      return getColor(var0, var0, var0, 255);
   }

   public static Color blendColors(float[] var0, Color[] var1, float var2) {
      Color var3 = null;
      if (var0 != null) {
         if (var1 != null) {
            if (var0.length == var1.length) {
               int[] var4 = getFractionIndicies(var0, var2);
               if (var4[0] >= 0 && var4[0] < var0.length && var4[1] >= 0 && var4[1] < var0.length) {
                  float[] var5 = new float[]{var0[var4[0]], var0[var4[1]]};
                  Color[] var6 = new Color[]{var1[var4[0]], var1[var4[1]]};
                  float var7 = var5[1] - var5[0];
                  float var8 = var2 - var5[0];
                  float var9 = var8 / var7;
                  var3 = blend(var6[0], var6[1], (double)(1.0F - var9));
                  return var3;
               } else {
                  return var1[0];
               }
            } else {
               throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
            }
         } else {
            throw new IllegalArgumentException("Colours can't be null");
         }
      } else {
         throw new IllegalArgumentException("Fractions can't be null");
      }
   }

   public static int getColor(int var0, int var1, int var2) {
      return getColor(var0, var1, var2, 255);
   }
}
