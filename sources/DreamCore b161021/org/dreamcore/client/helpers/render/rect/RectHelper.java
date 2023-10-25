package org.dreamcore.client.helpers.render.rect;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.helpers.Helper;

import java.awt.*;

public class RectHelper implements Helper {

    public static long delta = 0L;

    public static void drawRoundedRect(double x, double y, double width, double height, float radius, Color color) {

        float x2 = (float) (x + ((radius / 2F) + 0.5F));
        float y2 = (float) (y + ((radius / 2F) + 0.5F));
        float width2 = (float) (width - ((radius / 2F) + 0.5F));
        float height2 = (float) (height - ((radius / 2F) + 0.5F));

        drawRect(x2, y2, x2 + width2, y2 + height2, color.getRGB());

        polygon(x, y, radius * 2, 360, true, color);
        polygon(x + width2 - radius + 1.2, y, radius * 2, 360, true, color);

        polygon(x + width2 - radius + 1.2, y + height2 - radius + 1, radius * 2, 360, true, color);
        polygon(x, y + height2 - radius + 1, radius * 2, 360, true, color);

        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        drawRect(x2 - radius / 2 - 0.5F, y2 + radius / 2, x2 + width2, y2 + height2 - radius / 2, color.getRGB());
        drawRect(x2, y2 + radius / 2, x2 + width2 + radius / 2 + 0.5f, y2 + height2 - radius / 2, color.getRGB());
        drawRect(x2 + radius / 2, y2 - radius / 2 - 0.5F, x2 + width2 - radius / 2, y + height2 - radius / 2, color.getRGB());
        drawRect(x2 + radius / 2, y2, x2 + width2 - radius / 2, y2 + height2 + radius / 2 + 0.5f, color.getRGB());
    }

    public static void drawVerticalGradientSmoothRect(float left, float top, float right, float bottom, int color, int color2) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        gui.drawGradientRect(left, top, right, bottom, color, color2);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        gui.drawGradientRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color, color2);
        gui.drawGradientRect(left * 2, top * 2 - 1, right * 2, top * 2, color, color2);
        gui.drawGradientRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color, color2);
        gui.drawGradientRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color, color2);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2F, 2F, 2F);
    }

    public static void drawVerticalGradientRect(float left, float top, float right, float bottom, int color, int color2) {
        gui.drawGradientRect(left, top, right, bottom, color, color2);
    }

    public static void drawVerticalGradientBetterRect(float x, float y, float width, float height, int color, int color2) {
        gui.drawGradientRect(x, y, x + width, y + height, color, color2);
    }

    public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
        sideLength /= 2;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.disableAlpha();
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        if (!filled) {
            GL11.glLineWidth(1);
        }
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBegin(filled ? GL11.GL_TRIANGLE_FAN : GL11.GL_LINE_STRIP);

        for (double i = 0; i <= amountOfSides; i++) {
            double angle = i * (Math.PI * 2) / amountOfSides;
            GL11.glVertex2d(x + (sideLength * Math.cos(angle)) + sideLength, y + (sideLength * Math.sin(angle)) + sideLength);
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }


    public static void drawRectBetter(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    public static void drawGradientRectBetter(float x, float y, float width, float height, int color, int color2) {
        drawGradientRect(x, y, x + width, y + height, color, color2);
    }

    public static void drawSmoothGradientRect(double left, double top, double right, double bottom, int color, int color2) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        drawGradientRect(left, top, right, bottom, color, color2);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawGradientRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color, color2);
        drawGradientRect(left * 2, top * 2 - 1, right * 2, top * 2, color, color2);
        drawGradientRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color, color2);
        drawGradientRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color, color2);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2F, 2F, 2F);
    }


    public static void drawSmoothRectBetter(float x, float y, float width, float height, int color) {
        drawSmoothRect(x, y, x + width, y + height, color);
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawSmoothRect(float left, float top, float right, float bottom, int color) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color);
        drawRect(left * 2, top * 2 - 1, right * 2, top * 2, color);
        drawRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color);
        drawRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2F, 2F, 2F);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int color, int color2) {
        float f = (float) (color >> 24 & 255) / 255.0F;
        float f1 = (float) (color >> 16 & 255) / 255.0F;
        float f2 = (float) (color >> 8 & 255) / 255.0F;
        float f3 = (float) (color & 255) / 255.0F;
        float f4 = (float) (color2 >> 24 & 255) / 255.0F;
        float f5 = (float) (color2 >> 16 & 255) / 255.0F;
        float f6 = (float) (color2 >> 8 & 255) / 255.0F;
        float f7 = (float) (color2 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, top, gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, gui.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(right, bottom, gui.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, top, gui.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawSkeetButton(float x, float y, float right, float bottom) {
        drawSmoothRect(x - 31.0f, y - 43.0f, right + 31.0f, bottom - 30.0f, new Color(0, 0, 0, 255).getRGB());
        drawSmoothRect(x - 30.5f, y - 42.5f, right + 30.5f, bottom - 30.5f, new Color(45, 45, 45, 255).getRGB());
        gui.drawGradientRect((int) x - 30, (int) y - 42, right + 30, bottom - 31, new Color(48, 48, 48, 255).getRGB(), new Color(19, 19, 19, 255).getRGB());
    }

    public static void drawSkeetRectWithoutBorder(float x, float y, float right, float bottom) {
        drawSmoothRect(x - 41f, y - 61f, right + 41f, bottom + 61f, new Color(48, 48, 48, 255).getRGB());
        drawSmoothRect(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }

    public static void drawSkeetRect(float x, float y, float right, float bottom) {
        drawRect(x - 46.5f, y - 66.5f, right + 46.5f, bottom + 66.5f, new Color(0, 0, 0, 255).getRGB());
        drawRect(x - 46.0f, y - 66.0f, right + 46.0f, bottom + 66.0f, new Color(48, 48, 48, 255).getRGB());
        drawRect(x - 44.5f, y - 64.5f, right + 44.5f, bottom + 64.5f, new Color(33, 33, 33, 255).getRGB());
        drawRect(x - 43.5f, y - 63.5f, right + 43.5f, bottom + 63.5f, new Color(0, 0, 0, 255).getRGB());
        drawRect(x - 43.0f, y - 63.0f, right + 43.0f, bottom + 63.0f, new Color(9, 9, 9, 255).getRGB());
        drawRect(x - 40.5f, y - 60.5f, right + 40.5f, bottom + 60.5f, new Color(48, 48, 48, 255).getRGB());
        drawRect(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }

    public static void drawBorderedRect(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0), top - (!borderIncludedInBounds ? borderWidth : 0), right + (!borderIncludedInBounds ? borderWidth : 0), bottom + (!borderIncludedInBounds ? borderWidth : 0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0), top + (borderIncludedInBounds ? borderWidth : 0), right - ((borderIncludedInBounds ? borderWidth : 0)), bottom - ((borderIncludedInBounds ? borderWidth : 0)), insideColor);
    }

    public static void drawBorder(float left, float top, float right, float bottom, float borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        drawRect(left - (!borderIncludedInBounds ? borderWidth : 0), top - (!borderIncludedInBounds ? borderWidth : 0), right + (!borderIncludedInBounds ? borderWidth : 0), bottom + (!borderIncludedInBounds ? borderWidth : 0), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0), top + (borderIncludedInBounds ? borderWidth : 0), right - ((borderIncludedInBounds ? borderWidth : 0)), bottom - ((borderIncludedInBounds ? borderWidth : 0)), insideColor);
    }

    public static void drawOutlineRect(float x, float y, float width, float height, Color color, Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        int colorRgb = colorTwo.getRGB();
        drawRect(x - 1, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1, y + height, colorRgb);
        drawRect(x - 1, y - 1, x + width + 1, y, colorRgb);
        drawRect(x - 1, y + height, x + width + 1, y + height + 1, colorRgb);
    }
}
