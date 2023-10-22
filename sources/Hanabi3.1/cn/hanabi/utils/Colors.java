package cn.hanabi.utils;

import cn.hanabi.Hanabi;
import java.awt.Color;
import java.text.NumberFormat;

public enum Colors {
   BLACK(-16711423),
   BLUE(-12028161),
   DARKBLUE(-12621684),
   GREEN(-9830551),
   DARKGREEN(-9320847),
   WHITE(-65794),
   AQUA(-7820064),
   DARKAQUA(-12621684),
   GREY(-9868951),
   DARKGREY(-14342875),
   RED(-65536),
   DARKRED(-8388608),
   ORANGE(-29696),
   DARKORANGE(-2263808),
   YELLOW(-256),
   DARKYELLOW(-2702025),
   MAGENTA(-18751),
   DARKMAGENTA(-2252579);

   public int c;

   private Colors(int co) {
      this.c = co;
   }

   public static int getColor(Color color) {
      return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static int getColor(int brightness) {
      return getColor(brightness, brightness, brightness, 255);
   }

   public static int getColor(int brightness, int alpha) {
      return getColor(brightness, brightness, brightness, alpha);
   }

   public static int getColor(int red, int green, int blue) {
      return getColor(red, green, blue, 255);
   }

   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      color |= alpha << 24;
      color |= red << 16;
      color |= green << 8;
      color |= blue;
      return color;
   }

   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
      if (fractions != null) {
         if (colors != null) {
            if (fractions.length == colors.length) {
               int[] indicies = getFractionIndicies(fractions, progress);
               if (indicies[0] >= 0 && indicies[0] < fractions.length && indicies[1] >= 0 && indicies[1] < fractions.length) {
                  float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
                  Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
                  float max = range[1] - range[0];
                  float value = progress - range[0];
                  float weight = value / max;
                  Color color = blend(colorRange[0], colorRange[1], (double)(1.0F - weight));
                  return color;
               } else {
                  return colors[0];
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

   public static Color blend(Color color1, Color color2, double ratio) {
      float r = (float)ratio;
      float ir = 1.0F - r;
      float[] rgb1 = new float[3];
      float[] rgb2 = new float[3];
      color1.getColorComponents(rgb1);
      color2.getColorComponents(rgb2);
      float red = rgb1[0] * r + rgb2[0] * ir;
      float green = rgb1[1] * r + rgb2[1] * ir;
      float blue = rgb1[2] * r + rgb2[2] * ir;
      if (red < 0.0F) {
         red = 0.0F;
      } else if (red > 255.0F) {
         red = 255.0F;
      }

      if (green < 0.0F) {
         green = 0.0F;
      } else if (green > 255.0F) {
         green = 255.0F;
      }

      if (blue < 0.0F) {
         blue = 0.0F;
      } else if (blue > 255.0F) {
         blue = 255.0F;
      }

      Color color = null;

      try {
         color = new Color(red, green, blue);
      } catch (IllegalArgumentException var14) {
         NumberFormat nf = NumberFormat.getNumberInstance();
         Hanabi.INSTANCE.println(nf.format((double)red) + "; " + nf.format((double)green) + "; " + nf.format((double)blue));
         var14.printStackTrace();
      }

      return color;
   }

   public static int[] getFractionIndicies(float[] fractions, float progress) {
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

   public static Color getDarker(Color before, int dark, int alpha) {
      int rDank = Math.max(before.getRed() - dark, 0);
      int gDank = Math.max(before.getGreen() - dark, 0);
      int bDank = Math.max(before.getBlue() - dark, 0);
      return new Color(rDank, gDank, bDank, alpha);
   }
}
