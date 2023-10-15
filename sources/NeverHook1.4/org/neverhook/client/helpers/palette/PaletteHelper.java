/*    */ package org.neverhook.client.helpers.palette;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.neverhook.client.feature.impl.hud.ClickGui;
/*    */ import org.neverhook.client.feature.impl.hud.HUD;
/*    */ import org.neverhook.client.helpers.Helper;
/*    */ 
/*    */ public class PaletteHelper
/*    */   implements Helper
/*    */ {
/*    */   public static int getHealthColor(float health, float maxHealth) {
/* 12 */     return Color.HSBtoRGB(Math.max(0.0F, Math.min(health, maxHealth) / maxHealth) / 3.0F, 1.0F, 0.8F) | 0xFF000000;
/*    */   }
/*    */   
/*    */   public static int getColor(Color color) {
/* 16 */     return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
/*    */   }
/*    */   
/*    */   public static int getColor(int bright) {
/* 20 */     return getColor(bright, bright, bright, 255);
/*    */   }
/*    */   
/*    */   public static int getColor(int red, int green, int blue, int alpha) {
/* 24 */     int color = 0;
/* 25 */     color |= alpha << 24;
/* 26 */     color |= red << 16;
/* 27 */     color |= green << 8;
/* 28 */     color |= blue;
/* 29 */     return color;
/*    */   }
/*    */   
/*    */   public static int getColor(int brightness, int alpha) {
/* 33 */     return getColor(brightness, brightness, brightness, alpha);
/*    */   }
/*    */   
/*    */   public static Color rainbow(int delay, float saturation, float brightness) {
/* 37 */     double rainbow = Math.ceil(((System.currentTimeMillis() + delay) / 16L));
/* 38 */     rainbow %= 360.0D;
/* 39 */     return Color.getHSBColor((float)(rainbow / 360.0D), saturation, brightness);
/*    */   }
/*    */   
/*    */   public static int fadeColor(int startColor, int endColor, float progress) {
/* 43 */     if (progress > 1.0F) {
/* 44 */       progress = 1.0F - progress % 1.0F;
/*    */     }
/* 46 */     return fade(startColor, endColor, progress);
/*    */   }
/*    */   
/*    */   public static int fade(int startColor, int endColor, float progress) {
/* 50 */     float invert = 1.0F - progress;
/* 51 */     int r = (int)((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
/* 52 */     int g = (int)((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
/* 53 */     int b = (int)((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
/* 54 */     int a = (int)((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
/* 55 */     return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*    */   }
/*    */   
/*    */   public static Color astolfo(boolean clickgui, int yOffset) {
/* 59 */     float speed = clickgui ? (ClickGui.speed.getNumberValue() * 100.0F) : (HUD.time.getNumberValue() * 100.0F);
/* 60 */     float hue = (float)(System.currentTimeMillis() % (int)speed + yOffset);
/* 61 */     if (hue > speed) {
/* 62 */       hue -= speed;
/*    */     }
/* 64 */     hue /= speed;
/* 65 */     if (hue > 0.5F) {
/* 66 */       hue = 0.5F - hue - 0.5F;
/*    */     }
/* 68 */     hue += 0.5F;
/* 69 */     return Color.getHSBColor(hue, 0.4F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\palette\PaletteHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */