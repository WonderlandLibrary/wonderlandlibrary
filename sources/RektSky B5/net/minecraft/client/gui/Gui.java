/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Gui {
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;

    protected void drawHorizontalLine(int startX, int endX, int y2, int color) {
        if (endX < startX) {
            int i2 = startX;
            startX = endX;
            endX = i2;
        }
        Gui.drawRect(startX, y2, endX + 1, y2 + 1, color);
    }

    protected void drawVerticalLine(int x2, int startY, int endY, int color) {
        if (endY < startY) {
            int i2 = startY;
            startY = endY;
            endY = i2;
        }
        Gui.drawRect(x2, startY + 1, x2 + 1, endY, color);
    }

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i2 = left;
            left = right;
            right = i2;
        }
        if (top < bottom) {
            int j2 = top;
            top = bottom;
            bottom = j2;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f22 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f2, f1, f22, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
        if (left < right) {
            int i2 = left;
            left = right;
            right = i2;
        }
        if (top < bottom) {
            int j2 = top;
            top = bottom;
            bottom = j2;
        }
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f2 = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f22 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, this.zLevel).color(f1, f22, f3, f2).endVertex();
        worldrenderer.pos(left, top, this.zLevel).color(f1, f22, f3, f2).endVertex();
        worldrenderer.pos(left, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x2, int y2, int color) {
        fontRendererIn.drawStringWithShadow(text, x2 - fontRendererIn.getStringWidth(text) / 2, y2, color);
    }

    public void drawString(FontRenderer fontRendererIn, String text, int x2, int y2, int color) {
        fontRendererIn.drawStringWithShadow(text, x2, y2, color);
    }

    public void drawTexturedModalRect(int x2, int y2, int textureX, int textureY, int width, int height) {
        float f2 = 0.00390625f;
        float f1 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x2 + 0, y2 + height, this.zLevel).tex((float)(textureX + 0) * f2, (float)(textureY + height) * f1).endVertex();
        worldrenderer.pos(x2 + width, y2 + height, this.zLevel).tex((float)(textureX + width) * f2, (float)(textureY + height) * f1).endVertex();
        worldrenderer.pos(x2 + width, y2 + 0, this.zLevel).tex((float)(textureX + width) * f2, (float)(textureY + 0) * f1).endVertex();
        worldrenderer.pos(x2 + 0, y2 + 0, this.zLevel).tex((float)(textureX + 0) * f2, (float)(textureY + 0) * f1).endVertex();
        tessellator.draw();
    }

    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
        float f2 = 0.00390625f;
        float f1 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0.0f, yCoord + (float)maxV, this.zLevel).tex((float)(minU + 0) * f2, (float)(minV + maxV) * f1).endVertex();
        worldrenderer.pos(xCoord + (float)maxU, yCoord + (float)maxV, this.zLevel).tex((float)(minU + maxU) * f2, (float)(minV + maxV) * f1).endVertex();
        worldrenderer.pos(xCoord + (float)maxU, yCoord + 0.0f, this.zLevel).tex((float)(minU + maxU) * f2, (float)(minV + 0) * f1).endVertex();
        worldrenderer.pos(xCoord + 0.0f, yCoord + 0.0f, this.zLevel).tex((float)(minU + 0) * f2, (float)(minV + 0) * f1).endVertex();
        tessellator.draw();
    }

    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0, yCoord + heightIn, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + heightIn, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + 0, this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
        worldrenderer.pos(xCoord + 0, yCoord + 0, this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }

    public static void drawModalRectWithCustomSizedTexture(int x2, int y2, float u2, float v2, int width, int height, float textureWidth, float textureHeight) {
        float f2 = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x2, y2 + height, 0.0).tex(u2 * f2, (v2 + (float)height) * f1).endVertex();
        worldrenderer.pos(x2 + width, y2 + height, 0.0).tex((u2 + (float)width) * f2, (v2 + (float)height) * f1).endVertex();
        worldrenderer.pos(x2 + width, y2, 0.0).tex((u2 + (float)width) * f2, v2 * f1).endVertex();
        worldrenderer.pos(x2, y2, 0.0).tex(u2 * f2, v2 * f1).endVertex();
        tessellator.draw();
    }

    public static void drawScaledCustomSizeModalRect(int x2, int y2, float u2, float v2, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f2 = 1.0f / tileWidth;
        float f1 = 1.0f / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x2, y2 + height, 0.0).tex(u2 * f2, (v2 + (float)vHeight) * f1).endVertex();
        worldrenderer.pos(x2 + width, y2 + height, 0.0).tex((u2 + (float)uWidth) * f2, (v2 + (float)vHeight) * f1).endVertex();
        worldrenderer.pos(x2 + width, y2, 0.0).tex((u2 + (float)uWidth) * f2, v2 * f1).endVertex();
        worldrenderer.pos(x2, y2, 0.0).tex(u2 * f2, v2 * f1).endVertex();
        tessellator.draw();
    }
}

