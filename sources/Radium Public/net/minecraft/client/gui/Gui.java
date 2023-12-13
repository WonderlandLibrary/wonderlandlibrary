package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Gui {
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");

    public static Tessellator tessellator = Tessellator.getInstance();
    public static WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    protected float zLevel;

    /**
     * Draws a solid color rectangle with the specified coordinates and color (ARGB format). Args: x1, y1, x2, y2, color
     */
    public static void drawRect(float left, float top, float right, float bottom, int color) {
        int alpha = (color >> 24 & 255);
        boolean needBlend = alpha < 0xFF;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if (needBlend) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GL11.glColor4ub(
                    (byte) (color >> 16 & 255),
                    (byte) (color >> 8 & 255),
                    (byte) (color & 255),
                    (byte) alpha);
        } else
            GL11.glColor3ub(
                    (byte) (color >> 16 & 255),
                    (byte) (color >> 8 & 255),
                    (byte) (color & 255));

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(left, top);
        GL11.glVertex2f(left, bottom);
        GL11.glVertex2f(right, bottom);
        GL11.glVertex2f(right, top);
        GL11.glEnd();
        if (needBlend) {
            GL11.glDisable(GL11.GL_BLEND);
            GlStateManager.disableBlend();
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        final int alpha = color >> 24 & 255;
        final boolean needBlend = alpha < 0xFF;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if (needBlend) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4ub(
                    (byte) (color >> 16 & 255),
                    (byte) (color >> 8 & 255),
                    (byte) (color & 255),
                    (byte) alpha);
        } else
            GL11.glColor3ub(
                    (byte) (color >> 16 & 255),
                    (byte) (color >> 8 & 255),
                    (byte) (color & 255));

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        if (needBlend) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * f, (v +  height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     */
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D)
                .tex(u * f, (v + (float) vHeight) * f1)
                .endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D)
                .tex((u + (float) uWidth) * f, (v + (float) vHeight) * f1)
                .endVertex();
        worldrenderer.pos(x + width, y, 0.0D).
                tex((u + (float) uWidth) * f, v * f1)
                .endVertex();
        worldrenderer.pos(x, y, 0.0D)
                .tex(u * f, v * f1)
                .endVertex();
        tessellator.draw();
    }

    /**
     * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
     */
    protected void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
    protected void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(7425);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0.0f).color4f(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0.0f).color4f(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0.0f).color4f(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0.0f).color4f(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public void drawCenteredString(MinecraftFontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public void drawString(MinecraftFontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    private static final float TEXTURE_FACTOR = 0.00390625F;

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        final float tx = textureX * TEXTURE_FACTOR;
        final float txw = (textureX + width) * TEXTURE_FACTOR;
        final float ty = textureY * TEXTURE_FACTOR;
        final float tyh = (textureY + height) * TEXTURE_FACTOR;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(tx, tyh);
        GL11.glVertex2i(x, y + height);

        GL11.glTexCoord2f(txw, tyh);
        GL11.glVertex2i(x + width, y + height);

        GL11.glTexCoord2f(txw, ty);
        GL11.glVertex2i(x + width, y);

        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex2i(x, y);
        GL11.glEnd();
    }

    /**
     * Draws a textured rectangle using the texture currently bound to the TextureManager
     */
    public static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
        final float tx = minU * TEXTURE_FACTOR;
        final float txw = (minU + maxU) * TEXTURE_FACTOR;
        final float ty = minV * TEXTURE_FACTOR;
        final float tyh = (minV + maxV) * TEXTURE_FACTOR;
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(tx, tyh);
        GL11.glVertex2f(xCoord, yCoord + maxV);

        GL11.glTexCoord2f(txw, tyh);
        GL11.glVertex2f(xCoord + maxU, yCoord + maxV);

        GL11.glTexCoord2f(txw, ty);
        GL11.glVertex2f(xCoord + maxU, yCoord);

        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex2f(xCoord, yCoord);
        GL11.glEnd();

//        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//        worldrenderer.pos(xCoord + 0.0F, yCoord + (float) maxV, this.zLevel)
//            .tex((float) (minU + 0) * f, (float) (minV + maxV) * f1)
//            .endVertex();
//        worldrenderer.pos(xCoord + (float) maxU, yCoord + (float) maxV, this.zLevel)
//            .tex((float) (minU + maxU) * f, (float) (minV + maxV) * f1)
//            .endVertex();
//        worldrenderer.pos(xCoord + (float) maxU, yCoord + 0.0F, this.zLevel)
//            .tex((float) (minU + maxU) * f, (float) (minV + 0) * f1)
//            .endVertex();
//        worldrenderer.pos(xCoord + 0.0F, yCoord + 0.0F, this.zLevel)
//            .tex((float) (minU + 0) * f, (float) (minV + 0) * f1)
//            .endVertex();
//        tessellator.draw();
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public static void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
        final float minU = textureSprite.getMinU();
        final float maxU = textureSprite.getMaxU();
        final float minV = textureSprite.getMinV();
        final float maxV = textureSprite.getMaxV();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(minU, maxV);
        GL11.glVertex2i(xCoord, yCoord + heightIn);

        GL11.glTexCoord2f(maxU, maxV);
        GL11.glVertex2i(xCoord + widthIn, yCoord + heightIn);

        GL11.glTexCoord2f(maxU, minV);
        GL11.glVertex2i(xCoord + widthIn, yCoord);

        GL11.glTexCoord2f(minU, minV);
        GL11.glVertex2i(xCoord, yCoord);
        GL11.glEnd();
    }
}
