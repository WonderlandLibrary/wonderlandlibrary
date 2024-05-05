// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render;

import javax.vecmath.Vector4f;
import java.util.Random;
import org.lwjgl.util.glu.Sphere;
import java.nio.ByteBuffer;
import java.awt.image.ImageObserver;
import java.awt.Image;
import com.jhlabs.image.BoxBlurFilter;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;
import java.awt.Graphics;
import com.jhlabs.image.GaussianFilter;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.util.HashMap;
import javax.vecmath.Matrix4f;
import ru.fluger.client.helpers.Helper;

public class RenderHelper implements Helper
{
    private static final Matrix4f modelMatrix;
    private static final Matrix4f projectionMatrix;
    static bhe camPos;
    private static final bya frustum;
    private static HashMap<Integer, Integer> shadowCache;
    private static ccy blurShader;
    private static bvd buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static nf shader;
    public static HashMap<Integer, Integer> blurSpotCache;
    
    public static void color(final int color, final float alpha) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        bus.c(r, g, b, alpha);
    }
    
    public static void inShaderFBO() {
        try {
            (RenderHelper.blurShader = new ccy(RenderHelper.mc.N(), RenderHelper.mc.O(), RenderHelper.mc.b(), RenderHelper.shader)).a(RenderHelper.mc.d, RenderHelper.mc.e);
            RenderHelper.buffer = RenderHelper.blurShader.a;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void drawFilledCircle(final float xx, final float yy, final float radius, final Color color) {
        final int sections = 50;
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
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }
    
    public static Color injectAlpha(final Color color, int alpha) {
        alpha = rk.a(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static void bindTexture(final int texture) {
        GL11.glBindTexture(3553, texture);
    }
    
    private static void shaderConfigFix(final float intensity, final float blurWidth, final float blurHeight) {
        RenderHelper.blurShader.getShaders().get(0).c().a("Radius").a(intensity);
        RenderHelper.blurShader.getShaders().get(1).c().a("Radius").a(intensity);
        RenderHelper.blurShader.getShaders().get(0).c().a("BlurDir").a(blurWidth, blurHeight);
        RenderHelper.blurShader.getShaders().get(1).c().a("BlurDir").a(blurHeight, blurWidth);
    }
    
    public static void drawBlurredShadow(float x, float y, float width, float height, final int blurRadius, final Color color) {
        GL11.glPushMatrix();
        bus.a(516, 0.01f);
        width += blurRadius * 2;
        height += blurRadius * 2;
        x -= blurRadius;
        y -= blurRadius;
        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;
        final int identifier = (int)(width * height + width + color.hashCode() * blurRadius + blurRadius);
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        bus.m();
        int texId = -1;
        if (RenderHelper.shadowCache.containsKey(identifier)) {
            texId = RenderHelper.shadowCache.get(identifier);
            bus.i(texId);
        }
        else {
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            final BufferedImage original = new BufferedImage((int)width, (int)height, 3);
            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int)(width - blurRadius * 2), (int)(height - blurRadius * 2));
            g.dispose();
            final GaussianFilter op = new GaussianFilter((float)blurRadius);
            final BufferedImage blurred = op.filter(original, null);
            texId = cdt.a(cdt.a(), blurred, true, false);
            RenderHelper.shadowCache.put(identifier, texId);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(_X, _Y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(_X + width, _Y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();
        bus.y();
        bus.l();
        bus.I();
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    public static void blurAreaBoarder(final float x, final float f, final float width, final float height, final float intensity, final float blurWidth, final float blurHeight) {
        final bit scale = new bit(RenderHelper.mc);
        final int factor = scale.e();
        final int factor2 = scale.a();
        final int factor3 = scale.b();
        if (RenderHelper.lastScale != factor || RenderHelper.lastScaleWidth != factor2 || RenderHelper.lastScaleHeight != factor3 || RenderHelper.buffer == null || RenderHelper.blurShader == null) {
            inShaderFBO();
        }
        RenderHelper.lastScale = factor;
        RenderHelper.lastScaleWidth = factor2;
        RenderHelper.lastScaleHeight = factor3;
        GL11.glScissor((int)(x * factor), (int)(RenderHelper.mc.e - f * factor - height * factor) + 1, (int)(width * factor), (int)(height * factor));
        GL11.glEnable(3089);
        shaderConfigFix(intensity, blurWidth, blurHeight);
        RenderHelper.buffer.a(true);
        RenderHelper.blurShader.a(RenderHelper.mc.Y.renderPartialTicks);
        RenderHelper.mc.b().a(true);
        GL11.glDisable(3089);
    }
    
    public static void blurAreaBoarder(final int x, final int y, final int width, final int height, final float intensity) {
        final bit scale = new bit(RenderHelper.mc);
        final int factor = scale.e();
        final int factor2 = scale.a();
        final int factor3 = scale.b();
        if (RenderHelper.lastScale != factor || RenderHelper.lastScaleWidth != factor2 || RenderHelper.lastScaleHeight != factor3 || RenderHelper.buffer == null || RenderHelper.blurShader == null) {
            inShaderFBO();
        }
        RenderHelper.lastScale = factor;
        RenderHelper.lastScaleWidth = factor2;
        RenderHelper.lastScaleHeight = factor3;
        GL11.glScissor(x * factor, RenderHelper.mc.e - y * factor - height * factor, width * factor, height * factor);
        GL11.glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        RenderHelper.buffer.a(true);
        RenderHelper.blurShader.a(RenderHelper.mc.Y.renderPartialTicks);
        RenderHelper.mc.b().a(true);
        GL11.glDisable(3089);
    }
    
    public static void scissorRect(final float x, final float y, final float width, final double height) {
        final bit sr = new bit(RenderHelper.mc);
        final int factor = sr.e();
        GL11.glScissor((int)(x * factor), (int)((sr.b() - height) * factor), (int)((width - x) * factor), (int)((height - y) * factor));
    }
    
    public static void renderBlur(final int x, final int y, final int width, final int height, final int blurWidth, final int blurHeight, final int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder((float)x, (float)y, (float)width, (float)height, (float)blurRadius, (float)blurWidth, (float)blurHeight);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void renderBlur(final int x, final int y, final int width, final int height, final int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, (float)blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void renderBlurredShadow(final Color color, final double x, final double y, final double width, final double height, final int blurRadius) {
        bus.I();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        renderBlurredShadow(x, y, width, height, blurRadius, color);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void blurSpot(final int x, final int y, int width, int height, final int blurRadius, final int iterations) {
        final bit sr = new bit(RenderHelper.mc);
        final double scale = 1.0 / sr.e();
        final int imageDownscale = 2;
        final int imageWidth = (width *= sr.e()) / imageDownscale;
        final int imageHeight = (height *= sr.e()) / imageDownscale;
        final int bpp = 3;
        final int identifier = x * y * width * height * blurRadius + width + height + blurRadius + x + y;
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        int texId = -1;
        if (RenderHelper.blurSpotCache.containsKey(identifier)) {
            texId = RenderHelper.blurSpotCache.get(identifier);
            bus.i(texId);
        }
        else {
            final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp).order(ByteOrder.nativeOrder());
            GL11.glReadPixels(x, RenderHelper.mc.e - y - height, width, height, 6407, 5121, buffer);
            final BufferedImage original = new BufferedImage(width, height, 1);
            for (int xIndex = 0; xIndex < width; ++xIndex) {
                for (int yIndex = 0; yIndex < height; ++yIndex) {
                    final int i = (xIndex + width * yIndex) * bpp;
                    final int r = buffer.get(i) & 0xFF;
                    final int g = buffer.get(i + 1) & 0xFF;
                    final int b = buffer.get(i + 2) & 0xFF;
                    original.setRGB(xIndex, height - (yIndex + 1), 0xFF000000 | r << 16 | g << 8 | b);
                }
            }
            final BoxBlurFilter op = new BoxBlurFilter((float)blurRadius, (float)blurRadius, iterations);
            final BufferedImage image = new BufferedImage(imageWidth, imageHeight, original.getType());
            final Graphics g2 = image.getGraphics();
            g2.drawImage(original, 0, 0, imageWidth, imageHeight, null);
            g2.dispose();
            final BufferedImage blurred = op.filter(image, null);
            texId = cdt.a(cdt.a(), blurred, true, false);
            RenderHelper.blurSpotCache.put(identifier, texId);
        }
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f((float)x, (float)(y + height));
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f((float)(x + width), (float)(y + height));
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f((float)(x + width), (float)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glDisable(3553);
    }
    
    public static void renderBlurredShadow(double x, double y, double width, double height, final int blurRadius, final Color color) {
        bus.I();
        bus.a(516, 0.01f);
        final float _X = (float)((x -= blurRadius) - 0.25);
        final float _Y = (float)((y -= blurRadius) + 0.25);
        final int identifier = (int)((width += blurRadius * 2) * (height += blurRadius * 2) + width + color.hashCode() * blurRadius + blurRadius);
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        int texId = -1;
        if (RenderHelper.shadowCache.containsKey(identifier)) {
            texId = RenderHelper.shadowCache.get(identifier);
            bus.i(texId);
        }
        else {
            width = rk.a(width, 0.01, width);
            height = rk.a(height, 0.01, height);
            final BufferedImage original = new BufferedImage((int)width, (int)height, 2);
            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int)width - blurRadius * 2, (int)height - blurRadius * 2);
            g.dispose();
            final GaussianFilter op = new GaussianFilter((float)blurRadius);
            final BufferedImage blurred = op.filter(original, null);
            texId = cdt.a(cdt.a(), blurred, true, false);
            RenderHelper.shadowCache.put(identifier, texId);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(_X, _Y + (int)height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(_X + (int)width, _Y + (int)height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(_X + (int)width, _Y);
        GL11.glEnd();
        GL11.glDisable(3553);
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static void setColor(final int color) {
        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }
    
    public static void drawEntityBox(final vg entity, final Color color, final boolean fullBox, final float alpha) {
        bus.G();
        bus.b(770, 771);
        GL11.glEnable(3042);
        bus.d(2.0f);
        bus.z();
        GL11.glDisable(2929);
        bus.a(false);
        final double x = entity.M + (entity.p - entity.M) * RenderHelper.mc.Y.renderPartialTicks - RenderHelper.mc.ac().o;
        final double y = entity.N + (entity.q - entity.N) * RenderHelper.mc.Y.renderPartialTicks - RenderHelper.mc.ac().p;
        final double z = entity.O + (entity.r - entity.O) * RenderHelper.mc.Y.renderPartialTicks - RenderHelper.mc.ac().q;
        final bhb axisAlignedBB = entity.bw();
        final bhb axisAlignedBB2 = new bhb(axisAlignedBB.a - entity.p + x - 0.05, axisAlignedBB.b - entity.q + y, axisAlignedBB.c - entity.r + z - 0.05, axisAlignedBB.d - entity.p + x + 0.05, axisAlignedBB.e - entity.q + y + 0.15, axisAlignedBB.f - entity.r + z + 0.05);
        bus.d(2.0f);
        GL11.glEnable(2848);
        bus.c(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
        if (fullBox) {
            drawColorBox(axisAlignedBB2, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
            bus.c(0.0f, 0.0f, 0.0f, 0.5f);
        }
        drawSelectionBoundingBox(axisAlignedBB2);
        bus.d(2.0f);
        bus.y();
        GL11.glEnable(2929);
        bus.a(true);
        bus.l();
        bus.H();
    }
    
    public static void blockEsp(final et blockPos, final Color c, final boolean outline) {
        final double x = blockPos.p() - RenderHelper.mc.ac().o;
        final double y = blockPos.q() - RenderHelper.mc.ac().p;
        final double z = blockPos.r() - RenderHelper.mc.ac().q;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        bus.c(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.15f);
        drawColorBox(new bhb(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            bus.c(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new bhb(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        }
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawSelectionBoundingBox(final bhb boundingBox) {
        final bve tessellator = bve.a();
        final buk vertexbuffer = tessellator.c();
        vertexbuffer.a(3, cdy.e);
        vertexbuffer.b(boundingBox.a, boundingBox.b, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.b, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.b, boundingBox.f).d();
        vertexbuffer.b(boundingBox.a, boundingBox.b, boundingBox.f).d();
        vertexbuffer.b(boundingBox.a, boundingBox.b, boundingBox.c).d();
        tessellator.b();
        vertexbuffer.a(3, cdy.e);
        vertexbuffer.b(boundingBox.a, boundingBox.e, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.e, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.e, boundingBox.f).d();
        vertexbuffer.b(boundingBox.a, boundingBox.e, boundingBox.f).d();
        vertexbuffer.b(boundingBox.a, boundingBox.e, boundingBox.c).d();
        tessellator.b();
        vertexbuffer.a(1, cdy.e);
        vertexbuffer.b(boundingBox.a, boundingBox.b, boundingBox.c).d();
        vertexbuffer.b(boundingBox.a, boundingBox.e, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.b, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.e, boundingBox.c).d();
        vertexbuffer.b(boundingBox.d, boundingBox.b, boundingBox.f).d();
        vertexbuffer.b(boundingBox.d, boundingBox.e, boundingBox.f).d();
        vertexbuffer.b(boundingBox.a, boundingBox.b, boundingBox.f).d();
        vertexbuffer.b(boundingBox.a, boundingBox.e, boundingBox.f).d();
        tessellator.b();
    }
    
    public static void draw3DRect(final float x, final float y, final float w, final float h, final Color startColor, final Color endColor, final float lineWidth) {
        final float alpha = startColor.getAlpha() / 255.0f;
        final float red = startColor.getRed() / 255.0f;
        final float green = startColor.getGreen() / 255.0f;
        final float blue = startColor.getBlue() / 255.0f;
        final float alpha2 = endColor.getAlpha() / 255.0f;
        final float red2 = endColor.getRed() / 255.0f;
        final float green2 = endColor.getGreen() / 255.0f;
        final float blue2 = endColor.getBlue() / 255.0f;
        final bve tessellator = bve.a();
        final buk bufferbuilder = tessellator.c();
        bus.m();
        bus.z();
        bus.d(lineWidth);
        bus.a(770, 771, 1, 0);
        bufferbuilder.a(7, cdy.f);
        bufferbuilder.b(x, h, 0.0).a(red, green, blue, alpha).d();
        bufferbuilder.b(w, h, 0.0).a(red, green, blue, alpha).d();
        bufferbuilder.b(w, y, 0.0).a(red, green, blue, alpha).d();
        bufferbuilder.b(x, y, 0.0).a(red, green, blue, alpha).d();
        tessellator.b();
        bus.y();
        bus.l();
    }
    
    public static void drawSphere(final double x, final double y, final double z, final float size, final int slices, final int stacks) {
        final Sphere s = new Sphere();
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.2f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        s.setDrawStyle(100013);
        GL11.glTranslated(x - RenderHelper.mc.ac().o, y - RenderHelper.mc.ac().p, z - RenderHelper.mc.ac().q);
        s.draw(size, slices, stacks);
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawCircle(final Color color, final float x, final float y, final float radius, final int start, final int end) {
        bus.m();
        bus.z();
        bus.a(770, 771, 1, 0);
        color(color);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        for (float i = (float)end; i >= start; i -= 4.0f) {
            GL11.glVertex2f((float)(x + Math.cos(i * 3.141592653589793 / 180.0) * (radius * 1.001f)), (float)(y + Math.sin(i * 3.141592653589793 / 180.0) * (radius * 1.001f)));
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        bus.y();
        bus.l();
    }
    
    public static void drawCircle3D(final vg entity, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = entity.M + (entity.p - entity.M) * partialTicks - RenderHelper.mc.ac().o;
        final double y = entity.N + (entity.q - entity.N) * partialTicks - RenderHelper.mc.ac().p;
        final double z = entity.O + (entity.r - entity.O) * partialTicks - RenderHelper.mc.ac().q;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.2831855f / points), y, z + radius * Math.sin(i * 6.2831855f / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void drawCircle3D(final avj entity, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = entity.w().p() - RenderHelper.mc.ac().o;
        final double y = entity.w().q() - RenderHelper.mc.ac().p;
        final double z = entity.w().r() - RenderHelper.mc.ac().q;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.2831855f / points), y, z + radius * Math.sin(i * 6.2831855f / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void color(final float red, final float green, final float blue, final float alpha) {
        bus.c(red, green, blue, alpha);
    }
    
    public static void color(final float red, final float green, final float blue) {
        color(red, green, blue, 1.0f);
    }
    
    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void color(final int hexColor) {
        final float red = (hexColor >> 16 & 0xFF) / 255.0f;
        final float green = (hexColor >> 8 & 0xFF) / 255.0f;
        final float blue = (hexColor & 0xFF) / 255.0f;
        final float alpha = (hexColor >> 24 & 0xFF) / 255.0f;
        bus.c(red, green, blue, alpha);
    }
    
    public static void drawArrow(float x, float y, final float scale, final float width, final boolean up, final int hexColor) {
        GL11.glPushMatrix();
        bus.I();
        GL11.glScaled((double)scale, (double)scale, (double)scale);
        x /= scale;
        y /= scale;
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        color(hexColor);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)(y + (up ? 4 : 0)));
        GL11.glVertex2d((double)(x + 3.0f), (double)(y + (up ? 0 : 4)));
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex2d((double)(x + 3.0f), (double)(y + (up ? 0 : 4)));
        GL11.glVertex2d((double)(x + 6.0f), (double)(y + (up ? 4 : 0)));
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawDropCircleShadow(final int x, final int y, final int radius, final int segments, final int opaque, final int shadow) {
        final float a1 = (opaque >> 24 & 0xFF) / 255.0f;
        final float r1 = (opaque >> 16 & 0xFF) / 255.0f;
        final float g1 = (opaque >> 8 & 0xFF) / 255.0f;
        final float b1 = (opaque & 0xFF) / 255.0f;
        final float a2 = (shadow >> 24 & 0xFF) / 255.0f;
        final float r2 = (shadow >> 16 & 0xFF) / 255.0f;
        final float g2 = (shadow >> 8 & 0xFF) / 255.0f;
        final float b2 = (shadow & 0xFF) / 255.0f;
        bus.z();
        bus.m();
        bus.d();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.j(7425);
        final bve tessellator = bve.a();
        final buk buffer = tessellator.c();
        buffer.a(6, cdy.f);
        buffer.b(x, y, 0.0).a(r1, g1, b1, a1).d();
        for (int i = 0; i <= segments; ++i) {
            final double a3 = i / (double)segments * 3.141592653589793 * 2.0 - 1.5707963267948966;
            buffer.b(x - Math.cos(a3) * radius, y + Math.sin(a3) * radius, 0.0).a(r2, g2, b2, a2).d();
        }
        tessellator.b();
        bus.j(7424);
        bus.l();
        bus.e();
        bus.y();
    }
    
    public static void drawRhombus(int left, int top, int right, int bottom, final int color) {
        if (left < right) {
            final int i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final int j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final bve tessellator = bve.a();
        final buk bufferbuilder = tessellator.c();
        bus.m();
        bus.z();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.c(f4, f5, f6, f3);
        bufferbuilder.a(7, cdy.e);
        bufferbuilder.b(left, (bottom + top) / 2.0, 0.0).d();
        bufferbuilder.b((right + left) / 2.0, bottom, 0.0).d();
        bufferbuilder.b(right, (bottom + top) / 2.0, 0.0).d();
        bufferbuilder.b((right + left) / 2.0, top, 0.0).d();
        tessellator.b();
        bus.y();
        bus.l();
    }
    
    public static void drawDropCircleShadow(final int x, final int y, final int radius, final int offset, final int segments, final int opaque, final int shadow) {
        if (offset >= radius) {
            drawDropCircleShadow(x, y, radius, segments, opaque, shadow);
            return;
        }
        final float a1 = (opaque >> 24 & 0xFF) / 255.0f;
        final float r1 = (opaque >> 16 & 0xFF) / 255.0f;
        final float g1 = (opaque >> 8 & 0xFF) / 255.0f;
        final float b1 = (opaque & 0xFF) / 255.0f;
        final float a2 = (shadow >> 24 & 0xFF) / 255.0f;
        final float r2 = (shadow >> 16 & 0xFF) / 255.0f;
        final float g2 = (shadow >> 8 & 0xFF) / 255.0f;
        final float b2 = (shadow & 0xFF) / 255.0f;
        bus.z();
        bus.m();
        bus.d();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.j(7425);
        final bve tessellator = bve.a();
        final buk buffer = tessellator.c();
        buffer.a(6, cdy.f);
        buffer.b(x, y, 0.0).a(r1, g1, b1, a1).d();
        for (int i = 0; i <= segments; ++i) {
            final double a3 = i / (double)segments * 3.141592653589793 * 2.0 - 1.5707963267948966;
            buffer.b(x - Math.cos(a3) * offset, y + Math.sin(a3) * offset, 0.0).a(r1, g1, b1, a1).d();
        }
        tessellator.b();
        buffer.a(7, cdy.f);
        for (int i = 0; i < segments; ++i) {
            final double alpha1 = i / (double)segments * 3.141592653589793 * 2.0 - 1.5707963267948966;
            final double alpha2 = (i + 1) / (double)segments * 3.141592653589793 * 2.0 - 1.5707963267948966;
            buffer.b(x - Math.cos(alpha2) * offset, y + Math.sin(alpha2) * offset, 0.0).a(r1, g1, b1, a1).d();
            buffer.b(x - Math.cos(alpha1) * offset, y + Math.sin(alpha1) * offset, 0.0).a(r1, g1, b1, a1).d();
            buffer.b(x - Math.cos(alpha1) * radius, y + Math.sin(alpha1) * radius, 0.0).a(r2, g2, b2, a2).d();
            buffer.b(x - Math.cos(alpha2) * radius, y + Math.sin(alpha2) * radius, 0.0).a(r2, g2, b2, a2).d();
        }
        tessellator.b();
        bus.j(7424);
        bus.l();
        bus.e();
        bus.y();
    }
    
    public static void drawDropShadow(int left, int top, int right, int bottom, final int shadowScale, final int insideColor, final int shadowColor) {
        left -= shadowScale;
        top -= shadowScale;
        right += shadowScale;
        bottom += shadowScale;
        final float a1 = (insideColor >> 24 & 0xFF) / 255.0f;
        final float r1 = (insideColor >> 16 & 0xFF) / 255.0f;
        final float g1 = (insideColor >> 8 & 0xFF) / 255.0f;
        final float b1 = (insideColor & 0xFF) / 255.0f;
        final float a2 = (shadowColor >> 24 & 0xFF) / 255.0f;
        final float r2 = (shadowColor >> 16 & 0xFF) / 255.0f;
        final float g2 = (shadowColor >> 8 & 0xFF) / 255.0f;
        final float b2 = (shadowColor & 0xFF) / 255.0f;
        bus.z();
        bus.m();
        bus.d();
        bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
        bus.j(7425);
        final bve tessellator = bve.a();
        final buk buffer = tessellator.c();
        buffer.a(7, cdy.f);
        buffer.b(right - shadowScale, top + shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(left + shadowScale, top + shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(left + shadowScale, bottom - shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right - shadowScale, bottom - shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right, top, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(left, top, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(left + shadowScale, top + shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right - shadowScale, top + shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right - shadowScale, bottom - shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(left + shadowScale, bottom - shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(left, bottom, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(right, bottom, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(left + shadowScale, top + shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(left, top, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(left, bottom, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(left + shadowScale, bottom - shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right, top, 0.0).a(r2, g2, b2, a2).d();
        buffer.b(right - shadowScale, top + shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right - shadowScale, bottom - shadowScale, 0.0).a(r1, g1, b1, a1).d();
        buffer.b(right, bottom, 0.0).a(r2, g2, b2, a2).d();
        tessellator.b();
        bus.j(7424);
        bus.l();
        bus.e();
        bus.y();
    }
    
    public static void drawCylinder(final float radius, final float height, final int segments, final boolean flag) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        if (!flag) {
            GL11.glEnable(2884);
        }
        final float[][] vertecesTop = new float[segments][3];
        final float[][] vertecesBottom = new float[segments][3];
        for (int i = 0; i < segments; ++i) {
            vertecesTop[i][0] = (float)Math.cos(6.283185307179586 / segments * i) * radius;
            vertecesTop[i][1] = 0.0f;
            vertecesTop[i][2] = (float)Math.sin(6.283185307179586 / segments * i) * radius;
            vertecesBottom[i][0] = (float)Math.cos(6.283185307179586 / segments * i) * radius;
            vertecesBottom[i][1] = -height;
            vertecesBottom[i][2] = (float)Math.sin(6.283185307179586 / segments * i) * radius;
        }
        GL11.glBegin(9);
        for (int i = 0; i < segments; ++i) {
            GL11.glVertex3f(vertecesTop[segments - 1 - i][0], vertecesTop[segments - 1 - i][1], vertecesTop[segments - 1 - i][2]);
        }
        GL11.glEnd();
        GL11.glBegin(9);
        for (int i = 0; i < segments; ++i) {
            GL11.glVertex3f(vertecesBottom[i][0], vertecesBottom[i][1], vertecesBottom[i][2]);
        }
        GL11.glEnd();
        for (int i = 0; i < segments; ++i) {
            GL11.glBegin(7);
            GL11.glVertex3f(vertecesTop[i][0], vertecesTop[i][1], vertecesTop[i][2]);
            GL11.glVertex3f(vertecesTop[(i + 1) % segments][0], vertecesTop[(i + 1) % segments][1], vertecesTop[(i + 1) % segments][2]);
            GL11.glVertex3f(vertecesBottom[(i + 1) % segments][0], vertecesBottom[(i + 1) % segments][1], vertecesBottom[(i + 1) % segments][2]);
            GL11.glVertex3f(vertecesBottom[i][0], vertecesBottom[i][1], vertecesBottom[i][2]);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    public static void drawCone(final float radius, final float height, final int segments, final boolean flag) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        if (!flag) {
            GL11.glEnable(2884);
        }
        final float[][] verteces = new float[segments][3];
        final float[] topVertex = { 0.0f, 0.0f, 0.0f };
        for (int i = 0; i < segments; ++i) {
            verteces[i][0] = (float)Math.cos(6.283185307179586 / segments * i) * radius;
            verteces[i][1] = -height;
            verteces[i][2] = (float)Math.sin(6.283185307179586 / segments * i) * radius;
        }
        GL11.glBegin(9);
        for (int i = 0; i < segments; ++i) {
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
        }
        GL11.glEnd();
        for (int i = 0; i < segments; ++i) {
            GL11.glBegin(4);
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
            GL11.glVertex3f(topVertex[0], topVertex[1], topVertex[2]);
            GL11.glVertex3f(verteces[(i + 1) % segments][0], verteces[(i + 1) % segments][1], verteces[(i + 1) % segments][2]);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    public static void renderShine(final float rotation, final int iterations) {
        final Random random = new Random(432L);
        bus.z();
        bus.j(7425);
        bus.m();
        bus.a(bus.r.l, bus.l.e);
        bus.d();
        bus.q();
        bus.a(false);
        bus.G();
        final float f1 = rotation;
        float f2 = 0.0f;
        if (f1 > 0.8f) {
            f2 = (f1 - 0.8f) / 0.2f;
        }
        final bve tessellator = bve.a();
        final buk buffer = tessellator.c();
        for (int i = 0; i < (float)iterations; ++i) {
            bus.b(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
            bus.b(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
            bus.b(random.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f);
            bus.b(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
            bus.b(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
            bus.b(random.nextFloat() * 360.0f + f1 * 90.0f, 0.0f, 0.0f, 1.0f);
            final float pos1 = random.nextFloat() * 20.0f + 5.0f + f2 * 10.0f;
            final float pos2 = random.nextFloat() * 2.0f + 1.0f + f2 * 2.0f;
            buffer.a(6, cdy.f);
            buffer.b(0.0, 0.0, 0.0).b(255, 255, 255, (int)(255.0f * (1.0f - f2))).d();
            buffer.b(-0.866 * pos2, pos1, -0.5f * pos2).b(0, 0, 255, 0).d();
            buffer.b(0.866 * pos2, pos1, -0.5f * pos2).b(0, 0, 255, 0).d();
            buffer.b(0.0, pos1, 1.0f * pos2).b(0, 0, 255, 0).d();
            buffer.b(-0.866 * pos2, pos1, -0.5f * pos2).b(0, 0, 255, 0).d();
            tessellator.b();
        }
        bus.H();
        bus.a(true);
        bus.r();
        bus.l();
        bus.j(7424);
        bus.c(1.0f, 1.0f, 1.0f, 1.0f);
        bus.y();
        bus.e();
        bhz.b();
    }
    
    public static void drawCircle(final float xx, final float yy, final float radius, final Color col, final float width, final float position, final float round) {
        final int sections = 100;
        final double dAngle = round * 2.0f * 3.141592653589793 / sections;
        float x = 0.0f;
        float y = 0.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        for (int i = (int)position; i < position + sections; ++i) {
            x = (float)(radius * Math.cos(i * dAngle));
            y = (float)(radius * Math.sin(i * dAngle));
            GL11.glColor4f(col.getRed() / 255.0f, col.getGreen() / 255.0f, col.getBlue() / 255.0f, col.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        for (int i = (int)(position + sections); i > (int)position; --i) {
            x = (float)(radius * Math.cos(i * dAngle));
            y = (float)(radius * Math.sin(i * dAngle));
            GL11.glColor4f(col.getRed() / 255.0f, col.getGreen() / 255.0f, col.getBlue() / 255.0f, col.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        bus.d(0.0f, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawLoadingCircle(final Color color, final float sizeAmount) {
        final float status = (float)(System.currentTimeMillis() * 0.1 % 400.0);
        final float size = sizeAmount;
        final bit res = new bit(RenderHelper.mc);
        final float radius = res.a() / 20.0f;
        drawCircle(res.a() / 2.0f, res.b() / 2.0f - 65.0f, radius, new Color(33, 33, 33, 255), 5.0f, 0.0f, 1.0f);
        drawCircle(res.a() / 2.0f, res.b() / 2.0f - 65.0f, radius, new Color(color.getRGB()), 7.0f, status, size);
    }
    
    public static void drawImage(final nf resourceLocation, final float x, final float y, final float width, final float height, final Color color) {
        GL11.glPushMatrix();
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        cii.c(770, 771, 1, 0);
        bus.c(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        RenderHelper.mc.N().a(resourceLocation);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        bir.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glPopMatrix();
    }
    
    private static Vector4f getTransformedMatrix(final bhe posIn) {
        final bhe relativePos = RenderHelper.camPos.d(posIn);
        final Vector4f vector4f = new Vector4f((float)relativePos.b, (float)relativePos.c, (float)relativePos.d, 1.0f);
        transform(vector4f, RenderHelper.modelMatrix);
        transform(vector4f, RenderHelper.projectionMatrix);
        if (vector4f.w > 0.0f) {
            final Vector4f vector4f2 = vector4f;
            vector4f2.x *= -100000.0f;
            final Vector4f vector4f3 = vector4f;
            vector4f3.y *= -100000.0f;
        }
        else {
            final float invert = 1.0f / vector4f.w;
            final Vector4f vector4f4 = vector4f;
            vector4f4.x *= invert;
            final Vector4f vector4f5 = vector4f;
            vector4f5.y *= invert;
        }
        return vector4f;
    }
    
    private static void transform(final Vector4f vec, final Matrix4f matrix) {
        final float x = vec.x;
        final float y = vec.y;
        final float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }
    
    private static boolean isVisible(final Vector4f pos, final int width, final int height) {
        double wid = width;
        double position = pos.x;
        if (position >= 0.0 && position <= wid) {
            wid = height;
            position = pos.y;
            return position >= 0.0 && position <= wid;
        }
        return false;
    }
    
    public static bhe toScaledScreenPos(final bhe posIn) {
        final Vector4f vector4f = getTransformedMatrix(posIn);
        final bit scaledResolution = new bit(RenderHelper.mc);
        final int width = scaledResolution.a();
        final int height = scaledResolution.b();
        vector4f.x = width / 2.0f + (0.5f * vector4f.x * width + 0.5f);
        vector4f.y = height / 2.0f - (0.5f * vector4f.y * height + 0.5f);
        final double posZ = isVisible(vector4f, width, height) ? 0.0 : -1.0;
        return new bhe(vector4f.x, vector4f.y, posZ);
    }
    
    public static void renderItem(final aip itemStack, final int x, final int y) {
        bus.y();
        bus.a(true);
        bus.m(256);
        bus.k();
        bus.d();
        RenderHelper.mc.ad().a = -150.0f;
        bhz.b();
        RenderHelper.mc.ad().b(itemStack, x, y);
        RenderHelper.mc.ad().a(RenderHelper.mc.k, itemStack, x, y);
        bhz.a();
        RenderHelper.mc.ad().a = 0.0f;
    }
    
    public static void drawColorBox(final bhb axisalignedbb, final float red, final float green, final float blue, final float alpha) {
        final bve ts = bve.a();
        final buk buffer = ts.c();
        buffer.a(7, cdy.g);
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        ts.b();
        buffer.a(7, cdy.g);
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        ts.b();
        buffer.a(7, cdy.g);
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        ts.b();
        buffer.a(7, cdy.g);
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        ts.b();
        buffer.a(7, cdy.g);
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        ts.b();
        buffer.a(7, cdy.g);
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.a, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.c).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.e, axisalignedbb.f).a(red, green, blue, alpha).d();
        buffer.b(axisalignedbb.d, axisalignedbb.b, axisalignedbb.f).a(red, green, blue, alpha).d();
        ts.b();
    }
    
    public static boolean isInViewFrustum(final vg entity) {
        return isInViewFrustum(entity.bw()) || entity.ah;
    }
    
    private static boolean isInViewFrustum(final bhb bb) {
        final vg current = RenderHelper.mc.aa();
        if (current != null) {
            RenderHelper.frustum.a(current.p, current.q, current.r);
        }
        return RenderHelper.frustum.a(bb);
    }
    
    public static void blockEsp(final et blockPos, final Color c, final boolean outline, final double length, final double length2) {
        final double x = blockPos.p() - RenderHelper.mc.ac().o;
        final double y = blockPos.q() - RenderHelper.mc.ac().p;
        final double z = blockPos.r() - RenderHelper.mc.ac().q;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        bus.c(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.15f);
        drawColorBox(new bhb(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            bus.c(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new bhb(x, y, z, x + length2, y + 1.0, z + length));
        }
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void blockEspFrame(final et blockPos, final float red, final float green, final float blue) {
        final double x = blockPos.p() - RenderHelper.mc.ac().o;
        final double y = blockPos.q() - RenderHelper.mc.ac().p;
        final double z = blockPos.r() - RenderHelper.mc.ac().q;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        bus.c(red, green, blue, 1.0f);
        drawSelectionBoundingBox(new bhb(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void renderTriangle(final int color) {
        GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        bus.m();
        bus.z();
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        final bve tessellator = bve.a();
        final buk buffer = tessellator.c();
        buffer.a(4, cdy.e);
        buffer.b(0.0, 4.0, 0.0).d();
        buffer.b(4.0, -4.0, 0.0).d();
        buffer.b(-4.0, -4.0, 0.0).d();
        tessellator.b();
        GL11.glDisable(2848);
        bus.y();
        bus.l();
        GL11.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);
    }
    
    public static void drawTriangle(final float x, final float y, final float size, final float vector, final int color) {
        GL11.glTranslated((double)x, (double)y, 0.0);
        GL11.glRotatef(180.0f + vector, 0.0f, 0.0f, 1.0f);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        bus.c(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(6);
        GL11.glVertex2d(0.0, (double)size);
        GL11.glVertex2d((double)(1.0f * size), (double)(-size));
        GL11.glVertex2d((double)(-(1.0f * size)), (double)(-size));
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glRotatef(-180.0f - vector, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated((double)(-x), (double)(-y), 0.0);
    }
    
    public static void connectPoints(final float xOne, final float yOne, final float xTwo, final float yTwo) {
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(xOne, yOne);
        GL11.glVertex2f(xTwo, yTwo);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    static {
        modelMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        RenderHelper.camPos = new bhe(0.0, 0.0, 0.0);
        frustum = new bya();
        RenderHelper.shadowCache = new HashMap<Integer, Integer>();
        RenderHelper.shader = new nf("shaders/post/blur.json");
        RenderHelper.blurSpotCache = new HashMap<Integer, Integer>();
    }
}
