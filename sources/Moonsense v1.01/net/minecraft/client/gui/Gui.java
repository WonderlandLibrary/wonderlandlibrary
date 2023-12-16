// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static final ResourceLocation optionsBackground;
    public static final ResourceLocation statIcons;
    public static final ResourceLocation icons;
    public float zLevel;
    private static final String __OBFID = "CL_00000662";
    
    static {
        optionsBackground = new ResourceLocation("textures/gui/options_background.png");
        statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
        icons = new ResourceLocation("textures/gui/icons.png");
    }
    
    protected void drawHorizontalLine(int startX, int endX, final int y, final int color) {
        if (endX < startX) {
            final int var5 = startX;
            startX = endX;
            endX = var5;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }
    
    protected void drawVerticalLine(final int x, int startY, int endY, final int color) {
        if (endY < startY) {
            final int var5 = startY;
            startY = endY;
            endY = var5;
        }
        drawRect(x, startY + 1, x + 1, endY, color);
    }
    
    public static void drawRect(int left, int top, int right, int bottom, final int color) {
        if (left < right) {
            final int var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final int var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        var11.startDrawingQuads();
        var11.addVertex(left, bottom, 0.0);
        var11.addVertex(right, bottom, 0.0);
        var11.addVertex(right, top, 0.0);
        var11.addVertex(left, top, 0.0);
        var10.draw();
        GlStateManager.func_179098_w();
        GlStateManager.disableBlend();
    }
    
    public void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float var7 = (startColor >> 24 & 0xFF) / 255.0f;
        final float var8 = (startColor >> 16 & 0xFF) / 255.0f;
        final float var9 = (startColor >> 8 & 0xFF) / 255.0f;
        final float var10 = (startColor & 0xFF) / 255.0f;
        final float var11 = (endColor >> 24 & 0xFF) / 255.0f;
        final float var12 = (endColor >> 16 & 0xFF) / 255.0f;
        final float var13 = (endColor >> 8 & 0xFF) / 255.0f;
        final float var14 = (endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator var15 = Tessellator.getInstance();
        final WorldRenderer var16 = var15.getWorldRenderer();
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
    
    public void drawCenteredString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.func_175063_a(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }
    
    public void drawString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.func_175063_a(text, (float)x, (float)y, color);
    }
    
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0, y + height, this.zLevel, (textureX + 0) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, this.zLevel, (textureX + width) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0, this.zLevel, (textureX + width) * var7, (textureY + 0) * var8);
        var10.addVertexWithUV(x + 0, y + 0, this.zLevel, (textureX + 0) * var7, (textureY + 0) * var8);
        var9.draw();
    }
    
    public void drawTexturedModalRect(final float x, final float y, final float textureX, final float textureY, final float width, final float height) {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0.0f, y + height, this.zLevel, (textureX + 0.0f) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, this.zLevel, (textureX + width) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0.0f, this.zLevel, (textureX + width) * var7, (textureY + 0.0f) * var8);
        var10.addVertexWithUV(x + 0.0f, y + 0.0f, this.zLevel, (textureX + 0.0f) * var7, (textureY + 0.0f) * var8);
        var9.draw();
    }
    
    public void func_175174_a(final float p_175174_1_, final float p_175174_2_, final int p_175174_3_, final int p_175174_4_, final int p_175174_5_, final int p_175174_6_) {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(p_175174_1_ + 0.0f, p_175174_2_ + p_175174_6_, this.zLevel, (p_175174_3_ + 0) * var7, (p_175174_4_ + p_175174_6_) * var8);
        var10.addVertexWithUV(p_175174_1_ + p_175174_5_, p_175174_2_ + p_175174_6_, this.zLevel, (p_175174_3_ + p_175174_5_) * var7, (p_175174_4_ + p_175174_6_) * var8);
        var10.addVertexWithUV(p_175174_1_ + p_175174_5_, p_175174_2_ + 0.0f, this.zLevel, (p_175174_3_ + p_175174_5_) * var7, (p_175174_4_ + 0) * var8);
        var10.addVertexWithUV(p_175174_1_ + 0.0f, p_175174_2_ + 0.0f, this.zLevel, (p_175174_3_ + 0) * var7, (p_175174_4_ + 0) * var8);
        var9.draw();
    }
    
    public void func_175175_a(final int p_175175_1_, final int p_175175_2_, final TextureAtlasSprite p_175175_3_, final int p_175175_4_, final int p_175175_5_) {
        final Tessellator var6 = Tessellator.getInstance();
        final WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMaxV());
        var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMaxV());
        var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMinV());
        var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMinV());
        var6.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final int x, final int y, final float u, final float v, final int width, final int height, final float textureWidth, final float textureHeight) {
        final float var8 = 1.0f / textureWidth;
        final float var9 = 1.0f / textureHeight;
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0, u * var8, (v + height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0, (u + width) * var8, (v + height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0, (u + width) * var8, v * var9);
        var11.addVertexWithUV(x, y, 0.0, u * var8, v * var9);
        var10.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final int x, final int y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        final float var10 = 1.0f / tileWidth;
        final float var11 = 1.0f / tileHeight;
        final Tessellator var12 = Tessellator.getInstance();
        final WorldRenderer var13 = var12.getWorldRenderer();
        var13.startDrawingQuads();
        var13.addVertexWithUV(x, y + height, 0.0, u * var10, (v + vHeight) * var11);
        var13.addVertexWithUV(x + width, y + height, 0.0, (u + uWidth) * var10, (v + vHeight) * var11);
        var13.addVertexWithUV(x + width, y, 0.0, (u + uWidth) * var10, v * var11);
        var13.addVertexWithUV(x, y, 0.0, u * var10, v * var11);
        var12.draw();
    }
}
