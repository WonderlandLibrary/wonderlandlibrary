package me.rhys.base.util.render;

import java.awt.Color;
import me.rhys.base.util.vec.Vec4f;

public class ColorUtil {
  public static Color darken(Color color, int percent) {
    return darken(color.getRed(), color.getGreen(), color.getBlue(), percent);
  }
  
  public static Color darken(int color, int percent) {
    int[] values = getColorsI(color);
    return darken(values[0], values[1], values[2], percent);
  }
  
  public static Vec4f getColor(int color) {
    return new Vec4f((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F);
  }
  
  public static int getColor(Vec4f color) {
    return (new Color(color.x, color.y, color.z, color.w)).getRGB();
  }
  
  public static int getColor(int r, int g, int b) {
    return (new Color(r, g, b, 255)).getRGB();
  }
  
  public static Color getHealthColor(float h) {
    if (h >= 15.0F && h <= 20.0F)
      return new Color(28, 255, 0); 
    if (h >= 10.0F && h < 20.0F)
      return new Color(255, 254, 19); 
    if (h > 0.0F && h <= 10.0F)
      return new Color(255, 0, 26); 
    return new Color(221, 24, 34);
  }
  
  public static Color darken(int r, int g, int b, int percent) {
    return new Color((int)Math.max(0.0F, Math.min(255.0F, r - r * percent / 100.0F)), (int)Math.max(0.0F, Math.min(255.0F, g - g * percent / 100.0F)), (int)Math.max(0.0F, Math.min(255.0F, b - b * percent / 100.0F)));
  }
  
  public static Color lighten(int r, int g, int b, int percent) {
    return new Color((int)Math.max(0.0F, Math.min(255.0F, r + r * percent / 100.0F)), (int)Math.max(0.0F, Math.min(255.0F, g + g * percent / 100.0F)), (int)Math.max(0.0F, Math.min(255.0F, b + b * percent / 100.0F)));
  }
  
  public static Color lighten(int color, int percent) {
    int[] values = getColorsI(color);
    return lighten(values[0], values[1], values[2], percent);
  }
  
  public static Color lighten(Color color, int percent) {
    return lighten(color.getRed(), color.getGreen(), color.getBlue(), percent);
  }
  
  public static int rgba(int r, int g, int b, float alpha) {
    return (new Color(r, g, b, (int)(alpha * 255.0F))).getRGB();
  }
  
  public static int[] getColorsI(int color) {
    return new int[] { color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF };
  }
  
  public static boolean isTransparent(int color) {
    return ((color >> 24 & 0xFF) / 255.0F == 0.0F);
  }
  
  public enum Colors {
    TRANSPARENT(ColorUtil.rgba(0, 0, 0, 0.0F)),
    DARK_GRAY(ColorUtil.rgba(50, 50, 50, 1.0F));
    
    Colors(int color) {
      this.color = color;
    }
    
    int color;
    
    public int getColor() {
      return this.color;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\render\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */