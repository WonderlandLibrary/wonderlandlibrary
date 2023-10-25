/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.rect;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.celestial.client.helpers.Helper;
import org.lwjgl.opengl.GL11;

public class RectHelper
implements Helper {
    public static void drawRectBetter(double x, double y, double width, double height, int color) {
        RectHelper.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawSmoothRectBetter(double x, double y, double width, double height, int color) {
        RectHelper.drawSmoothRect((float)x, (float)y, x + width, y + height, color);
    }

    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawMinecraftRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 300.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 300.0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 300.0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 300.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawColorRect(double left, double top, double right, double bottom, Color color1, Color color2, Color color3, Color color4) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        RectHelper.glColor(color2);
        GL11.glVertex2d(left, bottom);
        RectHelper.glColor(color3);
        GL11.glVertex2d(right, bottom);
        RectHelper.glColor(color4);
        GL11.glVertex2d(right, top);
        RectHelper.glColor(color1);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }

    public static void renderShadowVertical(Color c, float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        RectHelper.renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void renderShadowVertical(float lineWidth, double startAlpha, int size, double posX, double posY1, double posY2, boolean right, boolean edges, float red, float green, float blue) {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (right) {
            for (double x = 0.5; x < (double)size; x += 0.5) {
                GL11.glColor4d(red, green, blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX + x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX + x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        } else {
            for (double x = 0.0; x < (double)size; x += 0.5) {
                GL11.glColor4d(red, green, blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX - x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX - x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / (double)size;
            }
        }
    }

    public static void renderShadowHorizontal(Color c, float lineWidth, double startAlpha, int size, double posY, double posX1, double posX2, boolean up, boolean edges) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        RectHelper.renderShadowHorizontal(lineWidth, startAlpha, size, posY, posX1, posX2, up, edges, (float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }

    public static void renderShadowHorizontal(float lineWidth, double startAlpha, int size, double posY, double posX1, double posX2, boolean up, boolean edges, float red, float green, float blue) {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (!up) {
            for (double y = 0.0; y < (double)size; y += 0.5) {
                GL11.glColor4d(red, green, blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX1 - (edges ? y : 0.0), posY + y);
                GL11.glVertex2d(posX2 + (edges ? y : 0.0), posY + y);
                GL11.glEnd();
                alpha = startAlpha - y / (double)size;
            }
        } else {
            for (double y = 0.5; y < (double)size; y += 0.5) {
                GL11.glColor4d(red, green, blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX1 - (edges ? y : 0.0) - 0.5, posY - y);
                GL11.glVertex2d(posX2 + (edges ? y : 0.0) - 0.5, posY - y);
                GL11.glEnd();
                alpha = startAlpha - y / (double)size;
            }
        }
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, bottom, 0.0).endVertex();
        bufferBuilder.pos(right, top, 0.0).endVertex();
        bufferBuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawSmoothRect(float left, float top, double right, double bottom, int color) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        RectHelper.drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RectHelper.drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0 - 1.0, color);
        RectHelper.drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0, top * 2.0f, color);
        RectHelper.drawRect(right * 2.0, top * 2.0f, right * 2.0 + 1.0, bottom * 2.0 - 1.0, color);
        RectHelper.drawRect(left * 2.0f, bottom * 2.0 - 1.0, right * 2.0, bottom * 2.0, color);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
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

    public static void drawSkeetButton(float x, float y, float right, float bottom) {
        RectHelper.drawSmoothRect(x - 31.0f, y - 43.0f, right + 31.0f, bottom - 30.0f, new Color(0, 0, 0, 255).getRGB());
        RectHelper.drawSmoothRect(x - 30.5f, y - 42.5f, right + 30.5f, bottom - 30.5f, new Color(45, 45, 45, 255).getRGB());
        gui.drawGradientRect((int)x - 30, (int)y - 42, right + 30.0f, bottom - 31.5f, new Color(18, 18, 18, 215).getRGB(), new Color(19, 19, 19, 255).getRGB());
    }

    public static void drawSkeetRectWithoutBorder(float x, float y, float right, float bottom) {
        RectHelper.drawRect(x - 41.0f, y - 61.0f, right + 41.0f, bottom + 61.0f, new Color(48, 48, 48, 255).getRGB());
        RectHelper.drawRect(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }

    public static void drawSkeetRect(float x, float y, float right, float bottom) {
        RectHelper.drawRect(x - 46.5f, y - 66.5f, right + 46.5f, bottom + 66.5f, new Color(0, 0, 0, 255).getRGB());
        RectHelper.drawRect(x - 46.0f, y - 66.0f, right + 46.0f, bottom + 66.0f, new Color(48, 48, 48, 255).getRGB());
        RectHelper.drawRect(x - 44.5f, y - 64.5f, right + 44.5f, bottom + 64.5f, new Color(33, 33, 33, 255).getRGB());
        RectHelper.drawRect(x - 43.5f, y - 63.5f, right + 43.5f, bottom + 63.5f, new Color(0, 0, 0, 255).getRGB());
        RectHelper.drawRect(x - 43.0f, y - 63.0f, right + 43.0f, bottom + 63.0f, new Color(9, 9, 9, 255).getRGB());
        RectHelper.drawRect(x - 40.5f, y - 60.5f, right + 40.5f, bottom + 60.5f, new Color(48, 48, 48, 255).getRGB());
        RectHelper.drawRect(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        RectHelper.drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0), top - (!borderIncludedInBounds ? borderWidth : 0.0), right + (!borderIncludedInBounds ? borderWidth : 0.0), bottom + (!borderIncludedInBounds ? borderWidth : 0.0), borderColor);
        RectHelper.drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }

    public static void drawBorder(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        RectHelper.drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0), top - (!borderIncludedInBounds ? borderWidth : 0.0), right + (!borderIncludedInBounds ? borderWidth : 0.0), bottom + (!borderIncludedInBounds ? borderWidth : 0.0), borderColor);
        RectHelper.drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }

    public static void drawRectWithEdge(double x, double y, double width, double height, Color color, Color colorTwo) {
        RectHelper.drawRect(x, y, x + width, y + height, color.getRGB());
        int colorRgb = colorTwo.getRGB();
        RectHelper.drawRect(x - 1.0, y, x, y + height, colorRgb);
        RectHelper.drawRect(x + width, y, x + width + 1.0, y + height, colorRgb);
        RectHelper.drawRect(x - 1.0, y - 1.0, x + width + 1.0, y, colorRgb);
        RectHelper.drawRect(x - 1.0, y + height, x + width + 1.0, y + height + 1.0, colorRgb);
    }

    public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawBorderedRect(double x, double y, double x2, double y2, double width, int color1, int color2) {
        RectHelper.drawRect(x, y, x2, y2, color2);
        RectHelper.drawBorderedRect(x, y, x2, y2, color1, width);
    }

    public static void drawBorderedRect(double x, double y, double width, double height, int color, double lwidth) {
        RectHelper.drawHLine(x, y, width, y, (float)lwidth, color);
        RectHelper.drawHLine(width, y, width, height, (float)lwidth, color);
        RectHelper.drawHLine(x, height, width, height, (float)lwidth, color);
        RectHelper.drawHLine(x, height, x, y, (float)lwidth, color);
    }

    public static void drawOutlinedRect(double x, double y, double w, double h, int color, float width) {
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder2 = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, a);
        GL11.glLineWidth(width);
        BufferBuilder2.begin(2, DefaultVertexFormats.POSITION);
        BufferBuilder2.pos(x, y, 0.0).endVertex();
        BufferBuilder2.pos(x, y + h, 0.0).endVertex();
        BufferBuilder2.pos(x + w, y + h, 0.0).endVertex();
        BufferBuilder2.pos(x + w, y, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedRectShaded(int x, int y, int w, int h, int colorOutline, int shade, float width) {
        int shaded = 0xFFFFFF & colorOutline | (shade & 0xFF) << 24;
        RectHelper.drawRect(x, y, w, h, shaded);
        RectHelper.drawOutlinedRect(x, y, w, h, colorOutline, width);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
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

    public static void drawBorderedRect1(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        RectHelper.drawRect(x, y, x + width, y + height, color);
        RectHelper.drawRect(x, y, x + width, y + lineSize, borderColor);
        RectHelper.drawRect(x, y, x + lineSize, y + height, borderColor);
        RectHelper.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        RectHelper.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }
}

