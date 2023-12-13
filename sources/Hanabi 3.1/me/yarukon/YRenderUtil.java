package me.yarukon;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.utils.Colors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

@ObfuscationClass
public class YRenderUtil {
   public static boolean isHoveringBound(float mouseX, float mouseY, float xLeft, float yUp, float width, float height) {
      return mouseX > xLeft && mouseX < xLeft + width && mouseY > yUp && mouseY < yUp + height;
   }

   public static void enableRender2D() {
      GL11.glEnable(3042);
      GL11.glDisable(2884);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.0F);
   }

   public static void disableRender2D() {
      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
   }

   public static void drawRoundedRect(float x, float y, float width, float height, float edgeRadius, int color, float borderWidth, int borderColor) {
      if (color == 16777215) {
         color = Colors.WHITE.c;
      }

      if (borderColor == 16777215) {
         borderColor = Colors.WHITE.c;
      }

      if (edgeRadius < 0.0F) {
         edgeRadius = 0.0F;
      }

      if (edgeRadius > width / 2.0F) {
         edgeRadius = width / 2.0F;
      }

      if (edgeRadius > height / 2.0F) {
         edgeRadius = height / 2.0F;
      }

      drawRect(x + edgeRadius, y + edgeRadius, width - edgeRadius * 2.0F, height - edgeRadius * 2.0F, color);
      drawRect(x + edgeRadius, y, width - edgeRadius * 2.0F, edgeRadius, color);
      drawRect(x + edgeRadius, y + height - edgeRadius, width - edgeRadius * 2.0F, edgeRadius, color);
      drawRect(x, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0F, color);
      drawRect(x + width - edgeRadius, y + edgeRadius, edgeRadius, height - edgeRadius * 2.0F, color);
      enableRender2D();
      color(color);
      GL11.glBegin(6);
      float centerX = x + edgeRadius;
      float centerY = y + edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      int vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      int i;
      double angleRadians;
      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586 * (double)(i + 180) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      GL11.glBegin(6);
      centerX = x + width - edgeRadius;
      centerY = y + edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586 * (double)(i + 90) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      GL11.glBegin(6);
      centerX = x + edgeRadius;
      centerY = y + height - edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586 * (double)(i + 270) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      GL11.glBegin(6);
      centerX = x + width - edgeRadius;
      centerY = y + height - edgeRadius;
      GL11.glVertex2d((double)centerX, (double)centerY);
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = 0; i < vertices + 1; ++i) {
         angleRadians = 6.283185307179586 * (double)i / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glEnd();
      color(borderColor);
      GL11.glLineWidth(borderWidth);
      GL11.glBegin(3);
      centerX = x + edgeRadius;
      centerY = y + edgeRadius;
      vertices = (int)Math.min(Math.max(edgeRadius, 10.0F), 90.0F);

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586 * (double)(i + 180) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)(x + edgeRadius), (double)y);
      GL11.glVertex2d((double)(x + width - edgeRadius), (double)y);
      centerX = x + width - edgeRadius;
      centerY = y + edgeRadius;

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586 * (double)(i + 90) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)(x + width), (double)(y + edgeRadius));
      GL11.glVertex2d((double)(x + width), (double)(y + height - edgeRadius));
      centerX = x + width - edgeRadius;
      centerY = y + height - edgeRadius;

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586 * (double)i / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)(x + width - edgeRadius), (double)(y + height));
      GL11.glVertex2d((double)(x + edgeRadius), (double)(y + height));
      centerX = x + edgeRadius;
      centerY = y + height - edgeRadius;

      for(i = vertices; i >= 0; --i) {
         angleRadians = 6.283185307179586 * (double)(i + 270) / (double)(vertices * 4);
         GL11.glVertex2d((double)centerX + Math.sin(angleRadians) * (double)edgeRadius, (double)centerY + Math.cos(angleRadians) * (double)edgeRadius);
      }

      GL11.glVertex2d((double)x, (double)(y + height - edgeRadius));
      GL11.glVertex2d((double)x, (double)(y + edgeRadius));
      GL11.glEnd();
      disableRender2D();
   }

   public static void color(int color) {
      float f = (float)(color >> 24 & 255) / 255.0F;
      float f1 = (float)(color >> 16 & 255) / 255.0F;
      float f2 = (float)(color >> 8 & 255) / 255.0F;
      float f3 = (float)(color & 255) / 255.0F;
      GL11.glColor4f(f1, f2, f3, f);
   }

   public static void drawRect(float left, float top, float width, float height, int color) {
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
      worldrenderer.pos((double)left, (double)(top + height), 0.0).endVertex();
      worldrenderer.pos((double)(left + width), (double)(top + height), 0.0).endVertex();
      worldrenderer.pos((double)(left + width), (double)top, 0.0).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRectNormal(float left, float top, float right, float bottom, int color) {
      float j;
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
      worldrenderer.pos((double)left, (double)bottom, 0.0).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0).endVertex();
      worldrenderer.pos((double)right, (double)top, 0.0).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
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
      worldrenderer.pos((double)right, (double)top, 0.0).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0).color(f1, f2, f3, f).endVertex();
      worldrenderer.pos((double)left, (double)bottom, 0.0).color(f5, f6, f7, f4).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0).color(f5, f6, f7, f4).endVertex();
      tessellator.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }
}
