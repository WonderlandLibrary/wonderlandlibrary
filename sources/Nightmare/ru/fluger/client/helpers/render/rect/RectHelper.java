// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render.rect;

import org.lwjgl.opengl.GL11;
import java.awt.Color;
import ru.fluger.client.helpers.Helper;

public class RectHelper implements Helper
{
    public static void drawRectBetter(final double x, final double y, final double width, final double height, final int color) {
        drawRect(x, y, x + width, y + height, color);
    }
    
    public static void drawSmoothRectBetter(final double x, final double y, final double width, final double height, final int color) {
        drawSmoothRect((float)x, (float)y, x + width, y + height, color);
    }
    
    public static void glColor(final Color color) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;
        bus.c(red, green, blue, alpha);
    }
    
    public static void drawMinecraftRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        bus.z();
        bus.m();
        bus.d();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.j(7425);
        final bve tessellator = bve.a();
        final buk bufferbuilder = tessellator.c();
        bufferbuilder.a(7, cdy.f);
        bufferbuilder.b(right, top, 300.0).a(f2, f3, f4, f).d();
        bufferbuilder.b(left, top, 300.0).a(f2, f3, f4, f).d();
        bufferbuilder.b(left, bottom, 300.0).a(f6, f7, f8, f5).d();
        bufferbuilder.b(right, bottom, 300.0).a(f6, f7, f8, f5).d();
        tessellator.b();
        bus.j(7424);
        bus.l();
        bus.e();
        bus.y();
    }
    
    public static void drawColorRect(final double left, final double top, final double right, final double bottom, final Color color1, final Color color2, final Color color3, final Color color4) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(color2);
        GL11.glVertex2d(left, bottom);
        glColor(color3);
        GL11.glVertex2d(right, bottom);
        glColor(color4);
        GL11.glVertex2d(right, top);
        glColor(color1);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        bir.a(0, 0, 0, 0, 0);
    }
    
    public static void renderShadowVertical(final Color c, final float lineWidth, final double startAlpha, final int size, final double posX, final double posY1, final double posY2, final boolean right, final boolean edges) {
        bus.I();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void renderShadowVertical(final float lineWidth, final double startAlpha, final int size, final double posX, final double posY1, final double posY2, final boolean right, final boolean edges, final float red, final float green, final float blue) {
        double alpha = startAlpha;
        bus.a(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (right) {
            for (double x = 0.5; x < size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX + x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX + x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / size;
            }
        }
        else {
            for (double x = 0.0; x < size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX - x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX - x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / size;
            }
        }
    }
    
    public static void renderShadowHorizontal(final Color c, final float lineWidth, final double startAlpha, final int size, final double posY, final double posX1, final double posX2, final boolean up, final boolean edges) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        renderShadowHorizontal(lineWidth, startAlpha, size, posY, posX1, posX2, up, edges, c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void renderShadowHorizontal(final float lineWidth, final double startAlpha, final int size, final double posY, final double posX1, final double posX2, final boolean up, final boolean edges, final float red, final float green, final float blue) {
        double alpha = startAlpha;
        bus.a(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (!up) {
            for (double y = 0.0; y < size; y += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX1 - (edges ? y : 0.0), posY + y);
                GL11.glVertex2d(posX2 + (edges ? y : 0.0), posY + y);
                GL11.glEnd();
                alpha = startAlpha - y / size;
            }
        }
        else {
            for (double y = 0.5; y < size; y += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX1 - (edges ? y : 0.0) - 0.5, posY - y);
                GL11.glVertex2d(posX2 + (edges ? y : 0.0) - 0.5, posY - y);
                GL11.glEnd();
                alpha = startAlpha - y / size;
            }
        }
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
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final bve tessellator = bve.a();
        final buk bufferBuilder = tessellator.c();
        bus.m();
        bus.z();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.c(f4, f5, f6, f3);
        bufferBuilder.a(7, cdy.e);
        bufferBuilder.b(left, bottom, 0.0).d();
        bufferBuilder.b(right, bottom, 0.0).d();
        bufferBuilder.b(right, top, 0.0).d();
        bufferBuilder.b(left, top, 0.0).d();
        tessellator.b();
        bus.y();
        bus.l();
    }
    
    public static void drawSmoothRect(final float left, final float top, final double right, final double bottom, final int color) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0 - 1.0, color);
        drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0, top * 2.0f, color);
        drawRect(right * 2.0, top * 2.0f, right * 2.0 + 1.0, bottom * 2.0 - 1.0, color);
        drawRect(left * 2.0f, bottom * 2.0 - 1.0, right * 2.0, bottom * 2.0, color);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public static void drawGradientRect(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawSkeetButton(final float x, final float y, final float right, final float bottom) {
        drawSmoothRect(x - 31.0f, y - 43.0f, right + 31.0f, bottom - 30.0f, new Color(0, 0, 0, 255).getRGB());
        drawSmoothRect(x - 30.5f, y - 42.5f, right + 30.5f, bottom - 30.5f, new Color(45, 45, 45, 255).getRGB());
        RectHelper.gui.drawGradientRect((float)((int)x - 30), (float)((int)y - 42), right + 30.0f, bottom - 31.5f, new Color(18, 18, 18, 215).getRGB(), new Color(19, 19, 19, 255).getRGB());
    }
    
    public static void drawSkeetRectWithoutBorder(final float x, final float y, final float right, final float bottom) {
        drawRect(x - 41.0f, y - 61.0f, right + 41.0f, bottom + 61.0f, new Color(48, 48, 48, 255).getRGB());
        drawRect(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }
    
    public static void drawSkeetRect(final float x, final float y, final float right, final float bottom) {
        drawRect(x - 46.5f, y - 66.5f, right + 46.5f, bottom + 66.5f, new Color(0, 0, 0, 255).getRGB());
        drawRect(x - 46.0f, y - 66.0f, right + 46.0f, bottom + 66.0f, new Color(48, 48, 48, 255).getRGB());
        drawRect(x - 44.5f, y - 64.5f, right + 44.5f, bottom + 64.5f, new Color(33, 33, 33, 255).getRGB());
        drawRect(x - 43.5f, y - 63.5f, right + 43.5f, bottom + 63.5f, new Color(0, 0, 0, 255).getRGB());
        drawRect(x - 43.0f, y - 63.0f, right + 43.0f, bottom + 63.0f, new Color(9, 9, 9, 255).getRGB());
        drawRect(x - 40.5f, y - 60.5f, right + 40.5f, bottom + 60.5f, new Color(48, 48, 48, 255).getRGB());
        drawRect(x - 40.0f, y - 60.0f, right + 40.0f, bottom + 60.0f, new Color(17, 17, 17, 255).getRGB());
    }
    
    public static void drawBorderedRect(final double left, final double top, final double right, final double bottom, final double borderWidth, final int insideColor, final int borderColor, final boolean borderIncludedInBounds) {
        drawRect(left - (borderIncludedInBounds ? 0.0 : borderWidth), top - (borderIncludedInBounds ? 0.0 : borderWidth), right + (borderIncludedInBounds ? 0.0 : borderWidth), bottom + (borderIncludedInBounds ? 0.0 : borderWidth), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    
    public static void drawBorder(final double left, final double top, final double right, final double bottom, final double borderWidth, final int insideColor, final int borderColor, final boolean borderIncludedInBounds) {
        drawRect(left - (borderIncludedInBounds ? 0.0 : borderWidth), top - (borderIncludedInBounds ? 0.0 : borderWidth), right + (borderIncludedInBounds ? 0.0 : borderWidth), bottom + (borderIncludedInBounds ? 0.0 : borderWidth), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    
    public static void drawRectWithEdge(final double x, final double y, final double width, final double height, final Color color, final Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        final int colorRgb = colorTwo.getRGB();
        drawRect(x - 1.0, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1.0, y + height, colorRgb);
        drawRect(x - 1.0, y - 1.0, x + width + 1.0, y, colorRgb);
        drawRect(x - 1.0, y + height, x + width + 1.0, y + height + 1.0, colorRgb);
    }
    
    public static void drawHLine(final double x, final double y, final double x1, final double y1, final float width, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        bus.m();
        bus.z();
        bus.a(770, 771, 1, 0);
        bus.c(red, green, blue, alpha);
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glPopMatrix();
        bus.y();
        bus.l();
        bus.c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double y2, final double width, final int color1, final int color2) {
        drawRect(x, y, x2, y2, color2);
        drawBorderedRect(x, y, x2, y2, color1, width);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double width, final double height, final int color, final double lwidth) {
        drawHLine(x, y, width, y, (float)lwidth, color);
        drawHLine(width, y, width, height, (float)lwidth, color);
        drawHLine(x, height, width, height, (float)lwidth, color);
        drawHLine(x, height, x, y, (float)lwidth, color);
    }
    
    public static void drawOutlinedRect(final double x, final double y, final double w, final double h, final int color, final float width) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final bve tessellator = bve.a();
        final buk BufferBuilder2 = tessellator.c();
        bus.m();
        bus.z();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.c(r, g, b, a);
        GL11.glLineWidth(width);
        BufferBuilder2.a(2, cdy.e);
        BufferBuilder2.b(x, y, 0.0).d();
        BufferBuilder2.b(x, y + h, 0.0).d();
        BufferBuilder2.b(x + w, y + h, 0.0).d();
        BufferBuilder2.b(x + w, y, 0.0).d();
        tessellator.b();
        bus.d(1.0f, 1.0f, 1.0f);
        bus.y();
        bus.l();
    }
    
    public static void drawOutlinedRectShaded(final int x, final int y, final int w, final int h, final int colorOutline, final int shade, final float width) {
        final int shaded = (0xFFFFFF & colorOutline) | (shade & 0xFF) << 24;
        drawRect(x, y, w, h, shaded);
        drawOutlinedRect(x, y, w, h, colorOutline, width);
    }
    
    public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawBorderedRect1(final double x, final double y, final double width, final double height, final double lineSize, final int borderColor, final int color) {
        drawRect(x, y, x + width, y + height, color);
        drawRect(x, y, x + width, y + lineSize, borderColor);
        drawRect(x, y, x + lineSize, y + height, borderColor);
        drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }
}
