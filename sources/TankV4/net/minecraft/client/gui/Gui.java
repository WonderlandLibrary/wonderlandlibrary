package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Gui {
   public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
   protected static float zLevel;
   public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
   public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");

   public static void drawHorizontalLine(int var0, int var1, int var2, int var3) {
      if (var1 < var0) {
         int var4 = var0;
         var0 = var1;
         var1 = var4;
      }

      drawRect((double)var0, (double)var2, (double)(var1 + 1), (double)(var2 + 1), var3);
   }

   public void drawTexturedModalRect(int var1, int var2, TextureAtlasSprite var3, int var4, int var5) {
      Tessellator var6 = Tessellator.getInstance();
      WorldRenderer var7 = var6.getWorldRenderer();
      var7.begin(7, DefaultVertexFormats.POSITION_TEX);
      var7.pos((double)(var1 + 0), (double)(var2 + var5), (double)zLevel).tex((double)var3.getMinU(), (double)var3.getMaxV()).endVertex();
      var7.pos((double)(var1 + var4), (double)(var2 + var5), (double)zLevel).tex((double)var3.getMaxU(), (double)var3.getMaxV()).endVertex();
      var7.pos((double)(var1 + var4), (double)(var2 + 0), (double)zLevel).tex((double)var3.getMaxU(), (double)var3.getMinV()).endVertex();
      var7.pos((double)(var1 + 0), (double)(var2 + 0), (double)zLevel).tex((double)var3.getMinU(), (double)var3.getMinV()).endVertex();
      var6.draw();
   }

   public static void drawScaledCustomSizeModalRect(double var0, double var2, float var4, float var5, int var6, int var7, int var8, int var9, float var10, float var11) {
      float var12 = 1.0F / var10;
      float var13 = 1.0F / var11;
      Tessellator var14 = Tessellator.getInstance();
      WorldRenderer var15 = var14.getWorldRenderer();
      var15.begin(7, DefaultVertexFormats.POSITION_TEX);
      var15.pos(var0, var2 + (double)var9, 0.0D).tex((double)(var4 * var12), (double)((var5 + (float)var7) * var13)).endVertex();
      var15.pos(var0 + (double)var8, var2 + (double)var9, 0.0D).tex((double)((var4 + (float)var6) * var12), (double)((var5 + (float)var7) * var13)).endVertex();
      var15.pos(var0 + (double)var8, var2, 0.0D).tex((double)((var4 + (float)var6) * var12), (double)(var5 * var13)).endVertex();
      var15.pos(var0, var2, 0.0D).tex((double)(var4 * var12), (double)(var5 * var13)).endVertex();
      var14.draw();
   }

   public static void drawVerticalLine(int var0, int var1, int var2, int var3) {
      if (var2 < var1) {
         int var4 = var1;
         var1 = var2;
         var2 = var4;
      }

      drawRect((double)var0, (double)(var1 + 1), (double)(var0 + 1), (double)var2, var3);
   }

   public void drawCenteredString(FontRenderer var1, String var2, int var3, int var4, int var5) {
      var1.drawStringWithShadow(var2, (float)(var3 - var1.getStringWidth(var2) / 2), (float)var4, var5);
   }

   public static void drawGradientRect(double var0, double var2, float var4, float var5, int var6, int var7) {
      float var8 = (float)(var6 >> 24 & 255) / 255.0F;
      float var9 = (float)(var6 >> 16 & 255) / 255.0F;
      float var10 = (float)(var6 >> 8 & 255) / 255.0F;
      float var11 = (float)(var6 & 255) / 255.0F;
      float var12 = (float)(var7 >> 24 & 255) / 255.0F;
      float var13 = (float)(var7 >> 16 & 255) / 255.0F;
      float var14 = (float)(var7 >> 8 & 255) / 255.0F;
      float var15 = (float)(var7 & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      Tessellator var16 = Tessellator.getInstance();
      WorldRenderer var17 = var16.getWorldRenderer();
      var17.begin(7, DefaultVertexFormats.POSITION_COLOR);
      var17.pos((double)var4, var2, (double)zLevel).color(var9, var10, var11, var8).endVertex();
      var17.pos(var0, var2, (double)zLevel).color(var9, var10, var11, var8).endVertex();
      var17.pos(var0, (double)var5, (double)zLevel).color(var13, var14, var15, var12).endVertex();
      var17.pos((double)var4, (double)var5, (double)zLevel).color(var13, var14, var15, var12).endVertex();
      var16.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public void drawString(FontRenderer var1, String var2, int var3, int var4, int var5) {
      var1.drawStringWithShadow(var2, (float)var3, (float)var4, var5);
   }

   public static void drawTexturedModalRect(int var0, int var1, int var2, int var3, int var4, int var5) {
      float var6 = 0.00390625F;
      float var7 = 0.00390625F;
      Tessellator var8 = Tessellator.getInstance();
      WorldRenderer var9 = var8.getWorldRenderer();
      var9.begin(7, DefaultVertexFormats.POSITION_TEX);
      var9.pos((double)(var0 + 0), (double)(var1 + var5), (double)zLevel).tex((double)((float)(var2 + 0) * var6), (double)((float)(var3 + var5) * var7)).endVertex();
      var9.pos((double)(var0 + var4), (double)(var1 + var5), (double)zLevel).tex((double)((float)(var2 + var4) * var6), (double)((float)(var3 + var5) * var7)).endVertex();
      var9.pos((double)(var0 + var4), (double)(var1 + 0), (double)zLevel).tex((double)((float)(var2 + var4) * var6), (double)((float)(var3 + 0) * var7)).endVertex();
      var9.pos((double)(var0 + 0), (double)(var1 + 0), (double)zLevel).tex((double)((float)(var2 + 0) * var6), (double)((float)(var3 + 0) * var7)).endVertex();
      var8.draw();
   }

   public static void drawTexturedModalRect(float var0, float var1, int var2, int var3, int var4, int var5) {
      float var6 = 0.00390625F;
      float var7 = 0.00390625F;
      Tessellator var8 = Tessellator.getInstance();
      WorldRenderer var9 = var8.getWorldRenderer();
      var9.begin(7, DefaultVertexFormats.POSITION_TEX);
      var9.pos((double)(var0 + 0.0F), (double)(var1 + (float)var5), (double)zLevel).tex((double)((float)(var2 + 0) * var6), (double)((float)(var3 + var5) * var7)).endVertex();
      var9.pos((double)(var0 + (float)var4), (double)(var1 + (float)var5), (double)zLevel).tex((double)((float)(var2 + var4) * var6), (double)((float)(var3 + var5) * var7)).endVertex();
      var9.pos((double)(var0 + (float)var4), (double)(var1 + 0.0F), (double)zLevel).tex((double)((float)(var2 + var4) * var6), (double)((float)(var3 + 0) * var7)).endVertex();
      var9.pos((double)(var0 + 0.0F), (double)(var1 + 0.0F), (double)zLevel).tex((double)((float)(var2 + 0) * var6), (double)((float)(var3 + 0) * var7)).endVertex();
      var8.draw();
   }

   public static void drawModalRectWithCustomSizedTexture(double var0, double var2, float var4, float var5, double var6, double var8, double var10, double var12) {
      float var14 = (float)(1.0D / var10);
      float var15 = (float)(1.0D / var12);
      Tessellator var16 = Tessellator.getInstance();
      WorldRenderer var17 = var16.getWorldRenderer();
      var17.begin(7, DefaultVertexFormats.POSITION_TEX);
      var17.pos(var0, var2 + var8, 0.0D).tex((double)(var4 * var14), (double)((var5 + (float)var8) * var15)).endVertex();
      var17.pos(var0 + var6, var2 + var8, 0.0D).tex((double)((var4 + (float)var6) * var14), (double)((var5 + (float)var8) * var15)).endVertex();
      var17.pos(var0 + var6, var2, 0.0D).tex((double)((var4 + (float)var6) * var14), (double)(var5 * var15)).endVertex();
      var17.pos(var0, var2, 0.0D).tex((double)(var4 * var14), (double)(var5 * var15)).endVertex();
      var16.draw();
   }

   public static void drawRect(double var0, double var2, double var4, double var6, int var8) {
      int var9;
      if (var0 < var4) {
         var9 = (int)var0;
         var0 = var4;
         var4 = (double)var9;
      }

      if (var2 < var6) {
         var9 = (int)var2;
         var2 = var6;
         var6 = (double)var9;
      }

      float var15 = (float)(var8 >> 24 & 255) / 255.0F;
      float var10 = (float)(var8 >> 16 & 255) / 255.0F;
      float var11 = (float)(var8 >> 8 & 255) / 255.0F;
      float var12 = (float)(var8 & 255) / 255.0F;
      Tessellator var13 = Tessellator.getInstance();
      WorldRenderer var14 = var13.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(var10, var11, var12, var15);
      var14.begin(7, DefaultVertexFormats.POSITION);
      var14.pos(var0, var6, 0.0D).endVertex();
      var14.pos(var4, var6, 0.0D).endVertex();
      var14.pos(var4, var2, 0.0D).endVertex();
      var14.pos(var0, var2, 0.0D).endVertex();
      var13.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
