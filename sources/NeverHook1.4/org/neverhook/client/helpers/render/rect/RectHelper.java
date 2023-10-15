/*     */ package org.neverhook.client.helpers.render.rect;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.neverhook.client.helpers.Helper;
/*     */ 
/*     */ 
/*     */ public class RectHelper
/*     */   implements Helper
/*     */ {
/*  15 */   public static long delta = 0L;
/*     */ 
/*     */   
/*     */   public static void drawRoundedRect(double x, double y, double width, double height, float radius, Color color) {
/*  19 */     float x2 = (float)(x + (radius / 2.0F + 0.5F));
/*  20 */     float y2 = (float)(y + (radius / 2.0F + 0.5F));
/*  21 */     float width2 = (float)(width - (radius / 2.0F + 0.5F));
/*  22 */     float height2 = (float)(height - (radius / 2.0F + 0.5F));
/*     */     
/*  24 */     drawRect(x2, y2, (x2 + width2), (y2 + height2), color.getRGB());
/*     */     
/*  26 */     polygon(x, y, (radius * 2.0F), 360.0D, true, color);
/*  27 */     polygon(x + width2 - radius + 1.2D, y, (radius * 2.0F), 360.0D, true, color);
/*     */     
/*  29 */     polygon(x + width2 - radius + 1.2D, y + height2 - radius + 1.0D, (radius * 2.0F), 360.0D, true, color);
/*  30 */     polygon(x, y + height2 - radius + 1.0D, (radius * 2.0F), 360.0D, true, color);
/*     */     
/*  32 */     GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  33 */     drawRect((x2 - radius / 2.0F - 0.5F), (y2 + radius / 2.0F), (x2 + width2), (y2 + height2 - radius / 2.0F), color.getRGB());
/*  34 */     drawRect(x2, (y2 + radius / 2.0F), (x2 + width2 + radius / 2.0F + 0.5F), (y2 + height2 - radius / 2.0F), color.getRGB());
/*  35 */     drawRect((x2 + radius / 2.0F), (y2 - radius / 2.0F - 0.5F), (x2 + width2 - radius / 2.0F), y + height2 - (radius / 2.0F), color.getRGB());
/*  36 */     drawRect((x2 + radius / 2.0F), y2, (x2 + width2 - radius / 2.0F), (y2 + height2 + radius / 2.0F + 0.5F), color.getRGB());
/*     */   }
/*     */   
/*     */   public static void drawVerticalGradientSmoothRect(float left, float top, float right, float bottom, int color, int color2) {
/*  40 */     GL11.glEnable(3042);
/*  41 */     GL11.glEnable(2848);
/*  42 */     gui.drawGradientRect(left, top, right, bottom, color, color2);
/*  43 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
/*  44 */     gui.drawGradientRect(left * 2.0F - 1.0F, top * 2.0F, left * 2.0F, bottom * 2.0F - 1.0F, color, color2);
/*  45 */     gui.drawGradientRect(left * 2.0F, top * 2.0F - 1.0F, right * 2.0F, top * 2.0F, color, color2);
/*  46 */     gui.drawGradientRect(right * 2.0F, top * 2.0F, right * 2.0F + 1.0F, bottom * 2.0F - 1.0F, color, color2);
/*  47 */     gui.drawGradientRect(left * 2.0F, bottom * 2.0F - 1.0F, right * 2.0F, bottom * 2.0F, color, color2);
/*  48 */     GL11.glDisable(2848);
/*  49 */     GL11.glDisable(3042);
/*  50 */     GL11.glScalef(2.0F, 2.0F, 2.0F);
/*     */   }
/*     */   
/*     */   public static void drawVerticalGradientRect(float left, float top, float right, float bottom, int color, int color2) {
/*  54 */     gui.drawGradientRect(left, top, right, bottom, color, color2);
/*     */   }
/*     */   
/*     */   public static void drawVerticalGradientBetterRect(float x, float y, float width, float height, int color, int color2) {
/*  58 */     gui.drawGradientRect(x, y, x + width, y + height, color, color2);
/*     */   }
/*     */   
/*     */   public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
/*  62 */     sideLength /= 2.0D;
/*  63 */     GL11.glEnable(3042);
/*  64 */     GL11.glBlendFunc(770, 771);
/*  65 */     GL11.glDisable(3553);
/*  66 */     GL11.glDisable(2884);
/*  67 */     GlStateManager.disableAlpha();
/*  68 */     GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  69 */     if (!filled) {
/*  70 */       GL11.glLineWidth(1.0F);
/*     */     }
/*  72 */     GL11.glEnable(2848);
/*  73 */     GL11.glBegin(filled ? 6 : 3);
/*     */     double i;
/*  75 */     for (i = 0.0D; i <= amountOfSides; i++) {
/*  76 */       double angle = i * 6.283185307179586D / amountOfSides;
/*  77 */       GL11.glVertex2d(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
/*     */     } 
/*     */     
/*  80 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  81 */     GL11.glEnd();
/*  82 */     GL11.glDisable(2848);
/*  83 */     GlStateManager.enableAlpha();
/*  84 */     GL11.glEnable(2884);
/*  85 */     GL11.glEnable(3553);
/*  86 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawRectBetter(double x, double y, double width, double height, int color) {
/*  91 */     drawRect(x, y, x + width, y + height, color);
/*     */   }
/*     */   
/*     */   public static void drawGradientRectBetter(float x, float y, float width, float height, int color, int color2) {
/*  95 */     drawGradientRect(x, y, (x + width), (y + height), color, color2);
/*     */   }
/*     */   
/*     */   public static void drawSmoothGradientRect(double left, double top, double right, double bottom, int color, int color2) {
/*  99 */     GL11.glEnable(3042);
/* 100 */     GL11.glEnable(2848);
/* 101 */     drawGradientRect(left, top, right, bottom, color, color2);
/* 102 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 103 */     drawGradientRect(left * 2.0D - 1.0D, top * 2.0D, left * 2.0D, bottom * 2.0D - 1.0D, color, color2);
/* 104 */     drawGradientRect(left * 2.0D, top * 2.0D - 1.0D, right * 2.0D, top * 2.0D, color, color2);
/* 105 */     drawGradientRect(right * 2.0D, top * 2.0D, right * 2.0D + 1.0D, bottom * 2.0D - 1.0D, color, color2);
/* 106 */     drawGradientRect(left * 2.0D, bottom * 2.0D - 1.0D, right * 2.0D, bottom * 2.0D, color, color2);
/* 107 */     GL11.glDisable(2848);
/* 108 */     GL11.glDisable(3042);
/* 109 */     GL11.glScalef(2.0F, 2.0F, 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawSmoothRectBetter(float x, float y, float width, float height, int color) {
/* 114 */     drawSmoothRect(x, y, x + width, y + height, color);
/*     */   }
/*     */   
/*     */   public static void drawRect(double left, double top, double right, double bottom, int color) {
/* 118 */     if (left < right) {
/*     */       
/* 120 */       double i = left;
/* 121 */       left = right;
/* 122 */       right = i;
/*     */     } 
/*     */     
/* 125 */     if (top < bottom) {
/*     */       
/* 127 */       double j = top;
/* 128 */       top = bottom;
/* 129 */       bottom = j;
/*     */     } 
/*     */     
/* 132 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
/* 133 */     float f = (color >> 16 & 0xFF) / 255.0F;
/* 134 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
/* 135 */     float f2 = (color & 0xFF) / 255.0F;
/* 136 */     Tessellator tessellator = Tessellator.getInstance();
/* 137 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
/* 138 */     GlStateManager.enableBlend();
/* 139 */     GlStateManager.disableTexture2D();
/* 140 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 141 */     GlStateManager.color(f, f1, f2, f3);
/* 142 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
/* 143 */     bufferbuilder.pos(left, bottom, 0.0D).endVertex();
/* 144 */     bufferbuilder.pos(right, bottom, 0.0D).endVertex();
/* 145 */     bufferbuilder.pos(right, top, 0.0D).endVertex();
/* 146 */     bufferbuilder.pos(left, top, 0.0D).endVertex();
/* 147 */     tessellator.draw();
/* 148 */     GlStateManager.enableTexture2D();
/* 149 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   public static void drawSmoothRect(float left, float top, float right, float bottom, int color) {
/* 153 */     GL11.glEnable(3042);
/* 154 */     GL11.glEnable(2848);
/* 155 */     drawRect(left, top, right, bottom, color);
/* 156 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 157 */     drawRect((left * 2.0F - 1.0F), (top * 2.0F), (left * 2.0F), (bottom * 2.0F - 1.0F), color);
/* 158 */     drawRect((left * 2.0F), (top * 2.0F - 1.0F), (right * 2.0F), (top * 2.0F), color);
/* 159 */     drawRect((right * 2.0F), (top * 2.0F), (right * 2.0F + 1.0F), (bottom * 2.0F - 1.0F), color);
/* 160 */     drawRect((left * 2.0F), (bottom * 2.0F - 1.0F), (right * 2.0F), (bottom * 2.0F), color);
/* 161 */     GL11.glDisable(2848);
/* 162 */     GL11.glDisable(3042);
/* 163 */     GL11.glScalef(2.0F, 2.0F, 2.0F);
/*     */   }
/*     */   
/*     */   public static void drawGradientRect(double left, double top, double right, double bottom, int color, int color2) {
/* 167 */     float f = (color >> 24 & 0xFF) / 255.0F;
/* 168 */     float f1 = (color >> 16 & 0xFF) / 255.0F;
/* 169 */     float f2 = (color >> 8 & 0xFF) / 255.0F;
/* 170 */     float f3 = (color & 0xFF) / 255.0F;
/* 171 */     float f4 = (color2 >> 24 & 0xFF) / 255.0F;
/* 172 */     float f5 = (color2 >> 16 & 0xFF) / 255.0F;
/* 173 */     float f6 = (color2 >> 8 & 0xFF) / 255.0F;
/* 174 */     float f7 = (color2 & 0xFF) / 255.0F;
/* 175 */     GlStateManager.disableTexture2D();
/* 176 */     GlStateManager.enableBlend();
/* 177 */     GlStateManager.disableAlpha();
/* 178 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/* 179 */     GlStateManager.shadeModel(7425);
/* 180 */     Tessellator tessellator = Tessellator.getInstance();
/* 181 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
/* 182 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 183 */     bufferbuilder.pos(left, top, gui.zLevel).color(f1, f2, f3, f).endVertex();
/* 184 */     bufferbuilder.pos(left, bottom, gui.zLevel).color(f1, f2, f3, f).endVertex();
/* 185 */     bufferbuilder.pos(right, bottom, gui.zLevel).color(f5, f6, f7, f4).endVertex();
/* 186 */     bufferbuilder.pos(right, top, gui.zLevel).color(f5, f6, f7, f4).endVertex();
/* 187 */     tessellator.draw();
/* 188 */     GlStateManager.shadeModel(7424);
/* 189 */     GlStateManager.disableBlend();
/* 190 */     GlStateManager.enableAlpha();
/* 191 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */   public static void drawSkeetButton(float x, float y, float right, float bottom) {
/* 195 */     drawSmoothRect(x - 31.0F, y - 43.0F, right + 31.0F, bottom - 30.0F, (new Color(0, 0, 0, 255)).getRGB());
/* 196 */     drawSmoothRect(x - 30.5F, y - 42.5F, right + 30.5F, bottom - 30.5F, (new Color(45, 45, 45, 255)).getRGB());
/* 197 */     gui.drawGradientRect(((int)x - 30), ((int)y - 42), right + 30.0F, bottom - 31.0F, (new Color(48, 48, 48, 255)).getRGB(), (new Color(19, 19, 19, 255)).getRGB());
/*     */   }
/*     */   
/*     */   public static void drawSkeetRectWithoutBorder(float x, float y, float right, float bottom) {
/* 201 */     drawSmoothRect(x - 41.0F, y - 61.0F, right + 41.0F, bottom + 61.0F, (new Color(48, 48, 48, 255)).getRGB());
/* 202 */     drawSmoothRect(x - 40.0F, y - 60.0F, right + 40.0F, bottom + 60.0F, (new Color(17, 17, 17, 255)).getRGB());
/*     */   }
/*     */   
/*     */   public static void drawSkeetRect(float x, float y, float right, float bottom) {
/* 206 */     drawRect((x - 46.5F), (y - 66.5F), (right + 46.5F), (bottom + 66.5F), (new Color(0, 0, 0, 255)).getRGB());
/* 207 */     drawRect((x - 46.0F), (y - 66.0F), (right + 46.0F), (bottom + 66.0F), (new Color(48, 48, 48, 255)).getRGB());
/* 208 */     drawRect((x - 44.5F), (y - 64.5F), (right + 44.5F), (bottom + 64.5F), (new Color(33, 33, 33, 255)).getRGB());
/* 209 */     drawRect((x - 43.5F), (y - 63.5F), (right + 43.5F), (bottom + 63.5F), (new Color(0, 0, 0, 255)).getRGB());
/* 210 */     drawRect((x - 43.0F), (y - 63.0F), (right + 43.0F), (bottom + 63.0F), (new Color(9, 9, 9, 255)).getRGB());
/* 211 */     drawRect((x - 40.5F), (y - 60.5F), (right + 40.5F), (bottom + 60.5F), (new Color(48, 48, 48, 255)).getRGB());
/* 212 */     drawRect((x - 40.0F), (y - 60.0F), (right + 40.0F), (bottom + 60.0F), (new Color(17, 17, 17, 255)).getRGB());
/*     */   }
/*     */   
/*     */   public static void drawBorderedRect(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
/* 216 */     drawRect((left - (!borderIncludedInBounds ? borderWidth : 0.0F)), (top - (!borderIncludedInBounds ? borderWidth : 0.0F)), (right + (!borderIncludedInBounds ? borderWidth : 0.0F)), (bottom + (!borderIncludedInBounds ? borderWidth : 0.0F)), borderColor);
/* 217 */     drawRect((left + (borderIncludedInBounds ? borderWidth : 0.0F)), (top + (borderIncludedInBounds ? borderWidth : 0.0F)), (right - (borderIncludedInBounds ? borderWidth : 0.0F)), (bottom - (borderIncludedInBounds ? borderWidth : 0.0F)), insideColor);
/*     */   }
/*     */   
/*     */   public static void drawBorder(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
/* 221 */     drawRect((left - (!borderIncludedInBounds ? borderWidth : 0.0F)), (top - (!borderIncludedInBounds ? borderWidth : 0.0F)), (right + (!borderIncludedInBounds ? borderWidth : 0.0F)), (bottom + (!borderIncludedInBounds ? borderWidth : 0.0F)), borderColor);
/* 222 */     drawRect((left + (borderIncludedInBounds ? borderWidth : 0.0F)), (top + (borderIncludedInBounds ? borderWidth : 0.0F)), (right - (borderIncludedInBounds ? borderWidth : 0.0F)), (bottom - (borderIncludedInBounds ? borderWidth : 0.0F)), insideColor);
/*     */   }
/*     */   
/*     */   public static void drawOutlineRect(float x, float y, float width, float height, Color color, Color colorTwo) {
/* 226 */     drawRect(x, y, (x + width), (y + height), color.getRGB());
/* 227 */     int colorRgb = colorTwo.getRGB();
/* 228 */     drawRect((x - 1.0F), y, x, (y + height), colorRgb);
/* 229 */     drawRect((x + width), y, (x + width + 1.0F), (y + height), colorRgb);
/* 230 */     drawRect((x - 1.0F), (y - 1.0F), (x + width + 1.0F), y, colorRgb);
/* 231 */     drawRect((x - 1.0F), (y + height), (x + width + 1.0F), (y + height + 1.0F), colorRgb);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\helpers\render\rect\RectHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */