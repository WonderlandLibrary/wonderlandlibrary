package cn.hanabi.gui.superskidder.material.clickgui;

import java.awt.Color;

public class ColorUtils {
   public static int randomColor() {
      return -16777216 | (int)(Math.random() * 1.6777215E7);
   }

   public static int transparency(int color, double alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, (float)alpha)).getRGB();
   }

   public static int transparency(Color color, double alpha) {
      return (new Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)alpha)).getRGB();
   }

   public static Color rainbow(long offset, float fade) {
      float hue = (float)(System.nanoTime() + offset) / 1.0E10F % 1.0F;
      long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16);
      Color c = new Color((int)color);
      return new Color((float)c.getRed() / 255.0F * fade, (float)c.getGreen() / 255.0F * fade, (float)c.getBlue() / 255.0F * fade, (float)c.getAlpha() / 255.0F);
   }

   public static float[] getRGBA(int color) {
      float a = (float)(color >> 24 & 255) / 255.0F;
      float r = (float)(color >> 16 & 255) / 255.0F;
      float g = (float)(color >> 8 & 255) / 255.0F;
      float b = (float)(color & 255) / 255.0F;
      return new float[]{r, g, b, a};
   }

   public static int intFromHex(String hex) {
      try {
         return hex.equalsIgnoreCase("rainbow") ? rainbow(0L, 1.0F).getRGB() : Integer.parseInt(hex, 16);
      } catch (NumberFormatException var2) {
         return -1;
      }
   }

   public static String hexFromInt(int color) {
      return hexFromInt(new Color(color));
   }

   public static String hexFromInt(Color color) {
      return Integer.toHexString(color.getRGB()).substring(2);
   }

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
      return color3;
   }

   public static Color blend(Color color1, Color color2) {
      return blend(color1, color2, 0.5);
   }

   public static Color darker(Color color, double fraction) {
      int red = (int)Math.round((double)color.getRed() * (1.0 - fraction));
      int green = (int)Math.round((double)color.getGreen() * (1.0 - fraction));
      int blue = (int)Math.round((double)color.getBlue() * (1.0 - fraction));
      if (red < 0) {
         red = 0;
      } else if (red > 255) {
         red = 255;
      }

      if (green < 0) {
         green = 0;
      } else if (green > 255) {
         green = 255;
      }

      if (blue < 0) {
         blue = 0;
      } else if (blue > 255) {
         blue = 255;
      }

      int alpha = color.getAlpha();
      return new Color(red, green, blue, alpha);
   }

   public static Color lighter(Color color, double fraction) {
      int red = (int)Math.round((double)color.getRed() * (1.0 + fraction));
      int green = (int)Math.round((double)color.getGreen() * (1.0 + fraction));
      int blue = (int)Math.round((double)color.getBlue() * (1.0 + fraction));
      if (red < 0) {
         red = 0;
      } else if (red > 255) {
         red = 255;
      }

      if (green < 0) {
         green = 0;
      } else if (green > 255) {
         green = 255;
      }

      if (blue < 0) {
         blue = 0;
      } else if (blue > 255) {
         blue = 255;
      }

      int alpha = color.getAlpha();
      return new Color(red, green, blue, alpha);
   }

   public static String getHexName(Color color) {
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      String rHex = Integer.toString(r, 16);
      String gHex = Integer.toString(g, 16);
      String bHex = Integer.toString(b, 16);
      return String.valueOf(rHex.length() == 2 ? rHex : "0" + rHex) + (gHex.length() == 2 ? gHex : "0" + gHex) + (bHex.length() == 2 ? bHex : "0" + bHex);
   }

   public static double colorDistance(double r1, double g1, double b1, double r2, double g2, double b2) {
      double a = r2 - r1;
      double b3 = g2 - g1;
      double c = b2 - b1;
      return Math.sqrt(a * a + b3 * b3 + c * c);
   }

   public static double colorDistance(double[] color1, double[] color2) {
      return colorDistance(color1[0], color1[1], color1[2], color2[0], color2[1], color2[2]);
   }

   public static double colorDistance(Color color1, Color color2) {
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      return colorDistance((double)rgb1[0], (double)rgb1[1], (double)rgb1[2], (double)rgb2[0], (double)rgb2[1], (double)rgb2[2]);
   }

   public static boolean isDark(double r, double g, double b) {
      double dWhite = colorDistance(r, g, b, 1.0, 1.0, 1.0);
      double dBlack = colorDistance(r, g, b, 0.0, 0.0, 0.0);
      return dBlack < dWhite;
   }

   public static boolean isDark(Color color) {
      float r = (float)color.getRed() / 255.0F;
      float g = (float)color.getGreen() / 255.0F;
      float b = (float)color.getBlue() / 255.0F;
      return isDark((double)r, (double)g, (double)b);
   }

   public static Color getColorWithOpacity(Color color, int alpha) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
   }

   public static Color getHealthColor(float health, float maxHealth) {
      float[] fractions = new float[]{0.0F, 0.5F, 1.0F};
      Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
      float progress = health / maxHealth;
      return blendColors(fractions, colors, progress).brighter();
   }

   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
      if (fractions.length == colors.length) {
         int[] indices = getFractionIndices(fractions, progress);
         float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
         Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
         float max = range[1] - range[0];
         float value = progress - range[0];
         float weight = value / max;
         Color color = blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
         return color;
      } else {
         throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
      }
   }

   public static int[] getFractionIndices(float[] fractions, float progress) {
      int[] range = new int[2];

      int startPoint;
      for(startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
      }

      if (startPoint >= fractions.length) {
         startPoint = fractions.length - 1;
      }

      range[0] = startPoint - 1;
      range[1] = startPoint;
      return range;
   }

   public static int reAlpha(int color, float alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, alpha)).getRGB();
   }
}
