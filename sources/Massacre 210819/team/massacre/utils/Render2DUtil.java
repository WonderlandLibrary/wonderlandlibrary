package team.massacre.utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Render2DUtil {
   public static void setColor(Color c) {
      GL11.glColor4d((double)((float)c.getRed() / 255.0F), (double)((float)c.getGreen() / 255.0F), (double)((float)c.getBlue() / 255.0F), (double)((float)c.getAlpha() / 255.0F));
   }

   public static void drawFace(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
      try {
         ResourceLocation skin = target.getLocationSkin();
         Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
         GL11.glEnable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
         GL11.glDisable(3042);
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
      float x2 = x + width;
      float y2 = y + height;
      int factor = sr.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)sr.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }

   public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d(left, top);
      GL11.glVertex2d(left, bottom);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(right, bottom);
      GL11.glVertex2d(right, top);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      double x1 = x + width;
      double y1 = y + height;
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glPushAttrib(0);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      x *= 2.0D;
      y *= 2.0D;
      x1 *= 2.0D;
      y1 *= 2.0D;
      GL11.glDisable(3553);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glEnable(2848);
      GL11.glBegin(9);

      int i;
      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x + radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius * -1.0D);
      }

      for(i = 0; i <= 90; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y1 - radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
      }

      for(i = 90; i <= 180; i += 3) {
         GL11.glVertex2d(x1 - radius + Math.sin((double)i * 3.141592653589793D / 180.0D) * radius, y + radius + Math.cos((double)i * 3.141592653589793D / 180.0D) * radius);
      }

      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glScaled(2.0D, 2.0D, 2.0D);
      GL11.glPopAttrib();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRectWH(float x, float y, float width, float height, int color) {
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), color);
   }

   public static void drawBorderedRectWH(float x, float y, float width, float height, float lineSize, int borderColor, int color) {
      drawRectWH(x, y, x + width, y + height, color);
      drawRectWH(x, y, x + width, y + lineSize, borderColor);
      drawRectWH(x, y, x + lineSize, y + height, borderColor);
      drawRectWH(x + width, y, x + width - lineSize, y + height, borderColor);
      drawRectWH(x, y + height, x + width, y + height - lineSize, borderColor);
   }

   public static void drawRect(double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, top, 0.0D).endVertex();
      worldrenderer.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRect(double left, double top, double right, double bottom, float opacity) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(0.1F, 0.1F, 0.1F, opacity);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, top, 0.0D).endVertex();
      worldrenderer.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawGradientRectWH(double left, double top, double width, double height, int startColor, int endColor) {
      float f = (float)(startColor >> 24 & 255) / 255.0F;
      float f1 = (float)(startColor >> 16 & 255) / 255.0F;
      float f2 = (float)(startColor >> 8 & 255) / 255.0F;
      float f3 = (float)(startColor & 255) / 255.0F;
      float f4 = (float)(endColor >> 24 & 255) / 255.0F;
      float f5 = (float)(endColor >> 16 & 255) / 255.0F;
      float f6 = (float)(endColor >> 8 & 255) / 255.0F;
      float f7 = (float)(endColor & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldrenderer.pos(left + width, top, 0.0D).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos(left, top + height, 0.0D).color(f5, f6, f7, f4).endVertex();
      worldrenderer.pos(left + width, top + height, 0.0D).color(f5, f6, f7, f4).endVertex();
      tessellator.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
      float f = (float)(startColor >> 24 & 255) / 255.0F;
      float f1 = (float)(startColor >> 16 & 255) / 255.0F;
      float f2 = (float)(startColor >> 8 & 255) / 255.0F;
      float f3 = (float)(startColor & 255) / 255.0F;
      float f4 = (float)(endColor >> 24 & 255) / 255.0F;
      float f5 = (float)(endColor >> 16 & 255) / 255.0F;
      float f6 = (float)(endColor >> 8 & 255) / 255.0F;
      float f7 = (float)(endColor & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldrenderer.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
      tessellator.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public static void drawOutline(double x, double y, double width, double height, double lineWidth, int color) {
      drawRect(x, y, x + width, y + lineWidth, color);
      drawRect(x, y, x + lineWidth, y + height, color);
      drawRect(x, y + height - lineWidth, x + width, y + height, color);
      drawRect(x + width - lineWidth, y, x + width, y + height, color);
   }

   public static void drawMenuBackground(ScaledResolution sr) {
      drawRect(0.0D, 0.0D, (double)sr.getScaledWidth(), (double)sr.getScaledHeight(), (new Color(34, 34, 34)).getRGB());
   }

   public static void drawImg(ResourceLocation loc, double posX, double posY, double width, double height) {
      GlStateManager.pushMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
      float f = 1.0F / (float)width;
      float f1 = 1.0F / (float)height;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.pos(posX, posY + height, 0.0D).tex((double)(0.0F * f), (double)((0.0F + (float)height) * f1)).endVertex();
      worldrenderer.pos(posX + width, posY + height, 0.0D).tex((double)((0.0F + (float)width) * f), (double)((0.0F + (float)height) * f1)).endVertex();
      worldrenderer.pos(posX + width, posY, 0.0D).tex((double)((0.0F + (float)width) * f), (double)(0.0F * f1)).endVertex();
      worldrenderer.pos(posX, posY, 0.0D).tex((double)(0.0F * f), (double)(0.0F * f1)).endVertex();
      tessellator.draw();
      GlStateManager.popMatrix();
   }

   public static void drawImg(ResourceLocation loc, double posX, double posY, double width, double height, Color c) {
      float f3 = (float)(c.getRGB() >> 24 & 255) / 255.0F;
      float f4 = (float)(c.getRGB() >> 16 & 255) / 255.0F;
      float f5 = (float)(c.getRGB() >> 8 & 255) / 255.0F;
      float f6 = (float)(c.getRGB() & 255) / 255.0F;
      GlStateManager.pushMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color(f3, f4, f5, f6);
      Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
      float f = 1.0F / (float)width;
      float f1 = 1.0F / (float)height;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
      worldrenderer.pos(posX, posY + height, 0.0D).tex((double)(0.0F * f), (double)((0.0F + (float)height) * f1)).endVertex();
      worldrenderer.pos(posX + width, posY + height, 0.0D).tex((double)((0.0F + (float)width) * f), (double)((0.0F + (float)height) * f1)).endVertex();
      worldrenderer.pos(posX + width, posY, 0.0D).tex((double)((0.0F + (float)width) * f), (double)(0.0F * f1)).endVertex();
      worldrenderer.pos(posX, posY, 0.0D).tex((double)(0.0F * f), (double)(0.0F * f1)).endVertex();
      tessellator.draw();
      GlStateManager.popMatrix();
   }

   public static void drawColorPicker(double left, double top, double right, double bottom, int col2) {
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(left, top);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2d(left, bottom);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
      GL11.glVertex2d(right, bottom);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(right, top);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
      GL11.glShadeModel(7424);
   }

   public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
      drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0D), top - (!borderIncludedInBounds ? borderWidth : 0.0D), right + (!borderIncludedInBounds ? borderWidth : 0.0D), bottom + (!borderIncludedInBounds ? borderWidth : 0.0D), borderColor);
      drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0D), top + (borderIncludedInBounds ? borderWidth : 0.0D), right - (borderIncludedInBounds ? borderWidth : 0.0D), bottom - (borderIncludedInBounds ? borderWidth : 0.0D), insideColor);
   }

   public static void drawTriangle(double[] points, float r, float g, float b, float a) {
      GL11.glPushAttrib(1048575);
      GL11.glColor4f(r, g, b, a);
      GL11.glBegin(4);
      GL11.glVertex2d(points[0], points[1]);
      GL11.glVertex2d(points[2], points[3]);
      GL11.glVertex2d(points[4], points[5]);
      GL11.glEnd();
      GL11.glPopAttrib();
   }
}
