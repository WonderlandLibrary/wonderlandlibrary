// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.font.AbstractFontRenderer;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static final ResourceLocation optionsBackground;
    public static final ResourceLocation statIcons;
    public static final ResourceLocation icons;
    public static float zLevel;
    
    protected AbstractFontRenderer getFont() {
        return HUDMod.customFont.isEnabled() ? FontUtil.tenacityFont20 : Minecraft.getMinecraft().fontRendererObj;
    }
    
    protected void drawHorizontalLine(int startX, int endX, final int y, final int color) {
        if (endX < startX) {
            final int i = startX;
            startX = endX;
            endX = i;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }
    
    protected void drawVerticalLine(final int x, int startY, int endY, final int color) {
        if (endY < startY) {
            final int i = startY;
            startY = endY;
            endY = i;
        }
        drawRect(x, startY + 1, x + 1, endY, color);
    }
    
    public static void drawRect2(final double x, final double y, final double width, final double height, final int color) {
        RenderUtil.resetColor();
        RenderUtil.setAlphaLimit(0.0f);
        GLUtil.setup2DRendering(true);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x, y, 0.0).color(color).endVertex();
        worldrenderer.pos(x, y + height, 0.0).color(color).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).color(color).endVertex();
        worldrenderer.pos(x + width, y, 0.0).color(color).endVertex();
        tessellator.draw();
        GLUtil.end2DRendering();
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        RenderUtil.resetColor();
        RenderUtil.setAlphaLimit(0.0f);
        GLUtil.setup2DRendering(true);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(left, bottom, 0.0).color(color).endVertex();
        worldrenderer.pos(right, bottom, 0.0).color(color).endVertex();
        worldrenderer.pos(right, top, 0.0).color(color).endVertex();
        worldrenderer.pos(left, top, 0.0).color(color).endVertex();
        tessellator.draw();
        GLUtil.end2DRendering();
    }
    
    public static void drawGradientRectSideways2(final double x, final double y, final double width, final double height, final int startColor, final int endColor) {
        drawGradientRectSideways(x, y, x + width, y + height, startColor, endColor);
    }
    
    public static void drawGradientRectSideways(final double left, final double top, final double right, final double bottom, final int startColor, final int endColor) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.setup2DRendering(true);
        GlStateManager.shadeModel(7425);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, Gui.zLevel).color(endColor).endVertex();
        worldrenderer.pos(left, top, Gui.zLevel).color(startColor).endVertex();
        worldrenderer.pos(left, bottom, Gui.zLevel).color(startColor).endVertex();
        worldrenderer.pos(right, bottom, Gui.zLevel).color(endColor).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GLUtil.end2DRendering();
    }
    
    public static void drawGradientRect2(final double x, final double y, final double width, final double height, final int startColor, final int endColor) {
        drawGradientRect(x, y, x + width, y + height, startColor, endColor);
    }
    
    public static void drawGradientRect(final double left, final double top, final double right, final double bottom, final int startColor, final int endColor) {
        RenderUtil.setAlphaLimit(0.0f);
        RenderUtil.resetColor();
        GLUtil.setup2DRendering(true);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, Gui.zLevel).color(startColor).endVertex();
        worldrenderer.pos(left, top, Gui.zLevel).color(startColor).endVertex();
        worldrenderer.pos(left, bottom, Gui.zLevel).color(endColor).endVertex();
        worldrenderer.pos(right, bottom, Gui.zLevel).color(endColor).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GLUtil.end2DRendering();
    }
    
    public void drawCenteredString(final IFontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, x - fontRendererIn.getStringWidth(text) / 2.0f, (float)y, color);
    }
    
    public void drawString(final IFontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, Gui.zLevel).tex(textureX * f, (textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, Gui.zLevel).tex((textureX + width) * f, (textureY + height) * f2).endVertex();
        worldrenderer.pos(x + width, y, Gui.zLevel).tex((textureX + width) * f, textureY * f2).endVertex();
        worldrenderer.pos(x, y, Gui.zLevel).tex(textureX * f, textureY * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0.0f, yCoord + maxV, Gui.zLevel).tex(minU * f, (minV + maxV) * f2).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + maxV, Gui.zLevel).tex((minU + maxU) * f, (minV + maxV) * f2).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + 0.0f, Gui.zLevel).tex((minU + maxU) * f, minV * f2).endVertex();
        worldrenderer.pos(xCoord + 0.0f, yCoord + 0.0f, Gui.zLevel).tex(minU * f, minV * f2).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn, final int heightIn) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord, yCoord + heightIn, Gui.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + heightIn, Gui.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord, Gui.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
        worldrenderer.pos(xCoord, yCoord, Gui.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + height) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + width) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final float x, final float y, final float u, final float v, final float uWidth, final float vHeight, final float width, final float height, final float tileWidth, final float tileHeight) {
        final float f = 1.0f / tileWidth;
        final float f2 = 1.0f / tileHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * f, (v + vHeight) * f2).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f2).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + uWidth) * f, v * f2).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    static {
        optionsBackground = new ResourceLocation("textures/gui/options_background.png");
        statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
        icons = new ResourceLocation("textures/gui/icons.png");
    }
}
