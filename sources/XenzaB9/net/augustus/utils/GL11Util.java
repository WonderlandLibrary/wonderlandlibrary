// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import java.awt.Color;
import net.augustus.utils.interfaces.MC;

public class GL11Util implements MC
{
    public static Color darker(final Color color, float factor) {
        factor = MathHelper.clamp_float(factor, 0.001f, 0.999f);
        return new Color(Math.max((int)(color.getRed() * factor), 0), Math.max((int)(color.getGreen() * factor), 0), Math.max((int)(color.getBlue() * factor), 0), color.getAlpha());
    }
    
    public static Color brighter(final Color color, float factor) {
        factor = MathHelper.clamp_float(factor, 0.001f, 0.999f);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        final int alpha = color.getAlpha();
        final int i = (int)(1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) {
            r = i;
        }
        if (g > 0 && g < i) {
            g = i;
        }
        if (b > 0 && b < i) {
            b = i;
        }
        return new Color(Math.min((int)(r / factor), 255), Math.min((int)(g / factor), 255), Math.min((int)(b / factor), 255), alpha);
    }
    
    public static void scissorBox(final int x, final int y, final int width, final int height) {
        final ScaledResolution scaledResolution = new ScaledResolution(GL11Util.mc);
        final int factor = scaledResolution.getScaleFactor();
        GL11.glEnable(3089);
        GL11.glScissor(x * factor, (scaledResolution.getScaledHeight() - (y + height)) * factor, (x + width - x) * factor, (y + height - y) * factor);
    }
    
    public static void drawRect(final float x, final float y, final float width, final float height, final int color) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(color);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawRoundedRect(final float x, final float y, final float width, final float height, final float radius, final int color) {
        final float x2 = x + (radius / 2.0f + 0.5f);
        final float y2 = y + (radius / 2.0f + 0.5f);
        final float calcWidth = width - (radius / 2.0f + 0.5f);
        final float calcHeight = height - (radius / 2.0f + 0.5f);
        relativeRect(x2 + radius / 2.0f, y2 - radius / 2.0f - 0.5f, x2 + calcWidth - radius / 2.0f, y + calcHeight - radius / 2.0f, color);
        relativeRect(x2 + radius / 2.0f, y2, x2 + calcWidth - radius / 2.0f, y2 + calcHeight + radius / 2.0f + 0.5f, color);
        relativeRect(x2 - radius / 2.0f - 0.5f, y2 + radius / 2.0f, x2 + calcWidth, y2 + calcHeight - radius / 2.0f, color);
        relativeRect(x2, y2 + radius / 2.0f + 0.5f, x2 + calcWidth + radius / 2.0f + 0.5f, y2 + calcHeight - radius / 2.0f, color);
        polygonCircle(x, y - 0.15, radius * 2.0f, 360.0, color);
        polygonCircle(x + calcWidth - radius + 1.0, y - 0.15, radius * 2.0f, 360.0, color);
        polygonCircle(x, y + calcHeight - radius + 1.0f, radius * 2.0f, 360.0, color);
        polygonCircle(x + calcWidth - radius + 1.0f, y + calcHeight - radius + 1.0f, radius * 2.0f, 360.0, color);
    }
    
    public static void drawRoundedRect2(final double x, final double y, final double width, final double height, final double radius, final int color) {
        drawRoundedRect((float)x, (float)y, (float)width, (float)height, (float)radius, color);
    }
    
    public static void relativeRect(final float left, final float top, final float right, final float bottom, final int color) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static final void polygonCircle(final double x, final double y, double sideLength, final double degree, final int color) {
        sideLength *= 0.5;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GlStateManager.disableAlpha();
        glColor(color);
        GL11.glLineWidth(1.0f);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (double i = 0.0; i <= degree; ++i) {
            final double angle = i * 6.283185307179586 / degree;
            GL11.glVertex2d(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableAlpha();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawFilledCircle(final float xx, final float yy, final float radius, final Color color) {
        final int sections = 1920;
        final double dAngle = 6.283185307179586 / sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; ++i) {
            final float x = (float)(radius * Math.sin(i * dAngle));
            final float y = (float)(radius * Math.cos(i * dAngle));
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }
    
    public static void drawHorizontalGradient(final float x, final float y, final float width, final float height, final int leftColor, final int rightColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(leftColor);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        glColor(rightColor);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawVerticalGradient(final float x, final float y, final float width, final float height, final int topColor, final int bottomColor) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        glColor(bottomColor);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
}
