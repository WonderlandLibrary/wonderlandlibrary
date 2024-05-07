package com.masterof13fps.utils.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class ColorUtil
{
  public static Map<String, ChatColor> colors = new HashMap();
  
  public static int transparency(int color, double alpha)
  {
    Color c = new Color(color);
    float r = 0.003921569F * c.getRed();
    float g = 0.003921569F * c.getGreen();
    float b = 0.003921569F * c.getBlue();
    return new Color(r, g, b, (float)alpha).getRGB();
  }
  
  public static Color rainbow(long offset, float fade)
  {
    float hue = (float)(System.nanoTime() + offset) / 1.0E10F % 1.0F;
    long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16);
    Color c = new Color((int)color);
    return new Color(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
  }
  
  public static float[] getRGBA(int color)
  {
    float a = (color >> 24 & 0xFF) / 255.0F;
    float r = (color >> 16 & 0xFF) / 255.0F;
    float g = (color >> 8 & 0xFF) / 255.0F;
    float b = (color & 0xFF) / 255.0F;
    return new float[] { r, g, b, a };
  }
  
  public static int intFromHex(String hex)
  {
    try
    {
      if (hex.equalsIgnoreCase("rainbow")) {
        return rainbow(0L, 1.0F).getRGB();
      }
      return Integer.parseInt(hex, 16);
    }
    catch (NumberFormatException localNumberFormatException) {}
    return -1;
  }
  
  public static String hexFromInt(int color)
  {
    return hexFromInt(new Color(color));
  }
  
  public static String hexFromInt(Color color)
  {
    return Integer.toHexString(color.getRGB()).substring(2);
  }
  
  public static Color blend(Color color1, Color color2)
  {
    return blend(color1, color2);
  }
  
  public static Color darker(Color color, double fraction)
  {
    int red = (int)Math.round(color.getRed() * (1.0D - fraction));
    int green = (int)Math.round(color.getGreen() * (1.0D - fraction));
    int blue = (int)Math.round(color.getBlue() * (1.0D - fraction));
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
  
  public static Color lighter(Color color, double fraction)
  {
    int red = (int)Math.round(color.getRed() * (1.0D + fraction));
    int green = (int)Math.round(color.getGreen() * (1.0D + fraction));
    int blue = (int)Math.round(color.getBlue() * (1.0D + fraction));
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
  
  public static String getHexName(Color color)
  {
    int r = color.getRed();
    int g = color.getGreen();
    int b = color.getBlue();
    
    String rHex = Integer.toString(r, 16);
    String gHex = Integer.toString(g, 16);
    String bHex = Integer.toString(b, 16);
    
    return (rHex.length() == 2 ? rHex : new StringBuilder("0").append(rHex).toString()) + (
      gHex.length() == 2 ? gHex : new StringBuilder("0").append(gHex).toString()) + (
      bHex.length() == 2 ? bHex : new StringBuilder("0").append(bHex).toString());
  }
  
  public static double colorDistance(double r1, double g1, double b1, double r2, double g2, double b2)
  {
    double a = r2 - r1;
    double b = g2 - g1;
    double c = b2 - b1;
    
    return Math.sqrt(a + b + c * c);
  }
  
  public static double colorDistance(double[] color1, double[] color2)
  {
    return colorDistance(color1[0], color1[1], color1[2], 
      color2[0], color2[1], color2[2]);
  }
  
  public static double colorDistance(Color color1, Color color2)
  {
    float[] rgb1 = new float[3];
    float[] rgb2 = new float[3];
    
    color1.getColorComponents(rgb1);
    color2.getColorComponents(rgb2);
    
    return colorDistance(rgb1[0], rgb1[1], rgb1[2], 
      rgb2[0], rgb2[1], rgb2[2]);
  }
  
  public static boolean isDark(double r, double g, double b)
  {
    double dWhite = colorDistance(r, g, b, 1.0D, 1.0D, 1.0D);
    double dBlack = colorDistance(r, g, b, 0.0D, 0.0D, 0.0D);
    
    return dBlack < dWhite;
  }
  
  public static boolean isDark(Color color)
  {
    float r = color.getRed() / 255.0F;
    float g = color.getGreen() / 255.0F;
    float b = color.getBlue() / 255.0F;
    
    return isDark(r, g, b);
  }
  
  public static void setColor(Color c)
  {
    GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
  }
}