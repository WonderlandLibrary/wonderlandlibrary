package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class Gui
{
  public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
  public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
  public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
  protected float zLevel;
  private static final String __OBFID = "CL_00000662";
  public static final Gui INSTANCE = new Gui();
  
  public Gui() {}
  
  public static void drawHorizontalLine(int startX, int endX, int y, int color)
  {
    if (endX < startX)
    {
      int var5 = startX;
      startX = endX;
      endX = var5;
    }
    drawRect(startX, y, endX + 1, y + 1, color);
  }
  
  public static void drawVerticalLine(int x, int startY, int endY, int color)
  {
    if (endY < startY)
    {
      int var5 = startY;
      startY = endY;
      endY = var5;
    }
    drawRect(x, startY + 1, x + 1, endY, color);
  }
  
  public static void drawRect(double x1, double y1, double x2, double y2, int color)
  {
    if (x1 < x2)
    {
      double var5 = x1;
      x1 = x2;
      x2 = var5;
    }
    if (y1 < y2)
    {
      double var5 = y1;
      y1 = y2;
      y2 = var5;
    }
    float var11 = (color >> 24 & 0xFF) / 255.0F;
    float var6 = (color >> 16 & 0xFF) / 255.0F;
    float var7 = (color >> 8 & 0xFF) / 255.0F;
    float var8 = (color & 0xFF) / 255.0F;
    Tessellator var9 = Tessellator.getInstance();
    WorldRenderer var10 = var9.getWorldRenderer();
    GlStateManager.enableBlend();
    GlStateManager.func_179090_x();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.color(var6, var7, var8, var11);
    var10.startDrawingQuads();
    var10.addVertex(x1, y2, 0.0D);
    var10.addVertex(x2, y2, 0.0D);
    var10.addVertex(x2, y1, 0.0D);
    var10.addVertex(x1, y1, 0.0D);
    var9.draw();
    GlStateManager.func_179098_w();
    GlStateManager.disableBlend();
  }
  
  protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
  {
    float var7 = (startColor >> 24 & 0xFF) / 255.0F;
    float var8 = (startColor >> 16 & 0xFF) / 255.0F;
    float var9 = (startColor >> 8 & 0xFF) / 255.0F;
    float var10 = (startColor & 0xFF) / 255.0F;
    float var11 = (endColor >> 24 & 0xFF) / 255.0F;
    float var12 = (endColor >> 16 & 0xFF) / 255.0F;
    float var13 = (endColor >> 8 & 0xFF) / 255.0F;
    float var14 = (endColor & 0xFF) / 255.0F;
    GlStateManager.func_179090_x();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.shadeModel(7425);
    Tessellator var15 = Tessellator.getInstance();
    WorldRenderer var16 = var15.getWorldRenderer();
    var16.startDrawingQuads();
    var16.func_178960_a(var8, var9, var10, var7);
    var16.addVertex(right, top, this.zLevel);
    var16.addVertex(left, top, this.zLevel);
    var16.func_178960_a(var12, var13, var14, var11);
    var16.addVertex(left, bottom, this.zLevel);
    var16.addVertex(right, bottom, this.zLevel);
    var15.draw();
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.func_179098_w();
  }
  
  public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
  {
    fontRendererIn.func_175063_a(text, x - fontRendererIn.getStringWidth(text) / 2, y, color);
  }
  
  public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
  {
    fontRendererIn.func_175063_a(text, x, y, color);
  }
  
  public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
  {
    float var7 = 0.00390625F;
    float var8 = 0.00390625F;
    Tessellator var9 = Tessellator.getInstance();
    WorldRenderer var10 = var9.getWorldRenderer();
    var10.startDrawingQuads();
    var10.addVertexWithUV(x + 0, y + height, this.zLevel, (textureX + 0) * var7, (textureY + height) * var8);
    var10.addVertexWithUV(x + width, y + height, this.zLevel, (textureX + width) * var7, (textureY + height) * var8);
    var10.addVertexWithUV(x + width, y + 0, this.zLevel, (textureX + width) * var7, (textureY + 0) * var8);
    var10.addVertexWithUV(x + 0, y + 0, this.zLevel, (textureX + 0) * var7, (textureY + 0) * var8);
    var9.draw();
  }
  
  public void func_175174_a(float p_175174_1_, float p_175174_2_, int p_175174_3_, int p_175174_4_, int p_175174_5_, int p_175174_6_)
  {
    float var7 = 0.00390625F;
    float var8 = 0.00390625F;
    Tessellator var9 = Tessellator.getInstance();
    WorldRenderer var10 = var9.getWorldRenderer();
    var10.startDrawingQuads();
    var10.addVertexWithUV(p_175174_1_ + 0.0F, p_175174_2_ + p_175174_6_, this.zLevel, (p_175174_3_ + 0) * var7, (p_175174_4_ + p_175174_6_) * var8);
    var10.addVertexWithUV(p_175174_1_ + p_175174_5_, p_175174_2_ + p_175174_6_, this.zLevel, (p_175174_3_ + p_175174_5_) * var7, (p_175174_4_ + p_175174_6_) * var8);
    var10.addVertexWithUV(p_175174_1_ + p_175174_5_, p_175174_2_ + 0.0F, this.zLevel, (p_175174_3_ + p_175174_5_) * var7, (p_175174_4_ + 0) * var8);
    var10.addVertexWithUV(p_175174_1_ + 0.0F, p_175174_2_ + 0.0F, this.zLevel, (p_175174_3_ + 0) * var7, (p_175174_4_ + 0) * var8);
    var9.draw();
  }
  
  public void func_175175_a(int p_175175_1_, int p_175175_2_, TextureAtlasSprite p_175175_3_, int p_175175_4_, int p_175175_5_)
  {
    Tessellator var6 = Tessellator.getInstance();
    WorldRenderer var7 = var6.getWorldRenderer();
    var7.startDrawingQuads();
    var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMaxV());
    var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMaxV());
    var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMinV());
    var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMinV());
    var6.draw();
  }
  
  public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
  {
    float var8 = 1.0F / textureWidth;
    float var9 = 1.0F / textureHeight;
    Tessellator var10 = Tessellator.getInstance();
    WorldRenderer var11 = var10.getWorldRenderer();
    var11.startDrawingQuads();
    var11.addVertexWithUV(x, y + height, 0.0D, u * var8, (v + height) * var9);
    var11.addVertexWithUV(x + width, y + height, 0.0D, (u + width) * var8, (v + height) * var9);
    var11.addVertexWithUV(x + width, y, 0.0D, (u + width) * var8, v * var9);
    var11.addVertexWithUV(x, y, 0.0D, u * var8, v * var9);
    var10.draw();
  }
  
  public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
  {
    float var10 = 1.0F / tileWidth;
    float var11 = 1.0F / tileHeight;
    Tessellator var12 = Tessellator.getInstance();
    WorldRenderer var13 = var12.getWorldRenderer();
    var13.startDrawingQuads();
    var13.addVertexWithUV(x, y + height, 0.0D, u * var10, (v + vHeight) * var11);
    var13.addVertexWithUV(x + width, y + height, 0.0D, (u + uWidth) * var10, (v + vHeight) * var11);
    var13.addVertexWithUV(x + width, y, 0.0D, (u + uWidth) * var10, v * var11);
    var13.addVertexWithUV(x, y, 0.0D, u * var10, v * var11);
    var12.draw();
  }
}
