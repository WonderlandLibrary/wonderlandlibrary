/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

public class Gui {
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;
    private static final String __OBFID = "CL_00000662";

    public static void drawHorizontalLine(float startX, float endX, float y, int color) {
        if (endX < startX) {
            float var5 = startX;
            startX = endX;
            endX = var5;
        }
        Gui.drawRect(startX, y, endX + 1.0f, y + 1.0f, color);
    }

    public static void drawVerticalLine(float x, float startY, float endY, int color) {
        if (endY < startY) {
            float var5 = startY;
            startY = endY;
            endY = var5;
        }
        Gui.drawRect(x, startY + 1.0f, x + 1.0f, endY, color);
    }

    public static void drawRect(double left, double offset, double right, double d, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (offset < d) {
            var5 = offset;
            offset = d;
            d = var5;
        }
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        var10.startDrawingQuads();
        var10.addVertex(left, d, 0.0);
        var10.addVertex(right, d, 0.0);
        var10.addVertex(right, offset, 0.0);
        var10.addVertex(left, offset, 0.0);
        var9.draw();
        GlStateManager.func_179098_w();
        GlStateManager.disableBlend();
    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float var7 = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float var8 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float var9 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float var10 = (float)(startColor & 0xFF) / 255.0f;
        float var11 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float var12 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float var13 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float var14 = (float)(endColor & 0xFF) / 255.0f;
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

    public void drawCenteredString(FontRenderer fontRendererIn, String text, float f, float g, int color) {
        fontRendererIn.drawStringWithShadow(text, f - (float)(fontRendererIn.getStringWidth(text) / 2), g, color);
    }

    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, x, y, color);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float var7 = 0.00390625f;
        float var8 = 0.00390625f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0, y + height, this.zLevel, (float)(textureX + 0) * var7, (float)(textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, this.zLevel, (float)(textureX + width) * var7, (float)(textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0, this.zLevel, (float)(textureX + width) * var7, (float)(textureY + 0) * var8);
        var10.addVertexWithUV(x + 0, y + 0, this.zLevel, (float)(textureX + 0) * var7, (float)(textureY + 0) * var8);
        var9.draw();
    }

    public void func_175174_a(float p_175174_1_, float p_175174_2_, int p_175174_3_, int p_175174_4_, int p_175174_5_, int p_175174_6_) {
        float var7 = 0.00390625f;
        float var8 = 0.00390625f;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(p_175174_1_ + 0.0f, p_175174_2_ + (float)p_175174_6_, this.zLevel, (float)(p_175174_3_ + 0) * var7, (float)(p_175174_4_ + p_175174_6_) * var8);
        var10.addVertexWithUV(p_175174_1_ + (float)p_175174_5_, p_175174_2_ + (float)p_175174_6_, this.zLevel, (float)(p_175174_3_ + p_175174_5_) * var7, (float)(p_175174_4_ + p_175174_6_) * var8);
        var10.addVertexWithUV(p_175174_1_ + (float)p_175174_5_, p_175174_2_ + 0.0f, this.zLevel, (float)(p_175174_3_ + p_175174_5_) * var7, (float)(p_175174_4_ + 0) * var8);
        var10.addVertexWithUV(p_175174_1_ + 0.0f, p_175174_2_ + 0.0f, this.zLevel, (float)(p_175174_3_ + 0) * var7, (float)(p_175174_4_ + 0) * var8);
        var9.draw();
    }

    public void func_175175_a(int p_175175_1_, int p_175175_2_, TextureAtlasSprite p_175175_3_, int p_175175_4_, int p_175175_5_) {
        Tessellator var6 = Tessellator.getInstance();
        WorldRenderer var7 = var6.getWorldRenderer();
        var7.startDrawingQuads();
        var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMaxV());
        var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + p_175175_5_, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMaxV());
        var7.addVertexWithUV(p_175175_1_ + p_175175_4_, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMaxU(), p_175175_3_.getMinV());
        var7.addVertexWithUV(p_175175_1_ + 0, p_175175_2_ + 0, this.zLevel, p_175175_3_.getMinU(), p_175175_3_.getMinV());
        var6.draw();
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float var8 = 1.0f / textureWidth;
        float var9 = 1.0f / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0, u * var8, (v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0, (u + (float)width) * var8, (v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0, (u + (float)width) * var8, v * var9);
        var11.addVertexWithUV(x, y, 0.0, u * var8, v * var9);
        var10.draw();
    }

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float var10 = 1.0f / tileWidth;
        float var11 = 1.0f / tileHeight;
        Tessellator var12 = Tessellator.getInstance();
        WorldRenderer var13 = var12.getWorldRenderer();
        var13.startDrawingQuads();
        var13.addVertexWithUV(x, y + height, 0.0, u * var10, (v + (float)vHeight) * var11);
        var13.addVertexWithUV(x + width, y + height, 0.0, (u + (float)uWidth) * var10, (v + (float)vHeight) * var11);
        var13.addVertexWithUV(x + width, y, 0.0, (u + (float)uWidth) * var10, v * var11);
        var13.addVertexWithUV(x, y, 0.0, u * var10, v * var11);
        var12.draw();
    }
}

