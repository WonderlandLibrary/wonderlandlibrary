/*
 * Decompiled with CFR 0.150.
 */
package ru.smertnix.celestial.utils.render;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import ru.smertnix.celestial.utils.Helper;

import org.lwjgl.opengl.GL11;

public class RenderUtil
implements Helper {
    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != RenderUtil.mc.displayWidth || framebuffer.framebufferHeight != RenderUtil.mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(RenderUtil.mc.displayWidth, RenderUtil.mc.displayHeight, true);
        }
        return framebuffer;
    }

    public static void drawFilledCircleNoGL(int x, int y, double r, int c, int quality) {
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c & 0xFF) / 255.0f;
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            double x2 = Math.sin((double)(i * quality) * Math.PI / 180.0) * r;
            double y2 = Math.cos((double)(i * quality) * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)x + x2, (double)y + y2);
        }
        GL11.glEnd();
    }

    public static void drawBorderedRect(float x, float y, float width, float height, float outlineThickness, int rectColor, int outlineColor) {
        Gui.drawRect2(x, y, width, height, rectColor);
        GL11.glEnable(2848);
        GLUtil.setup2DRendering(() -> {
            RenderUtil.color(outlineColor);
            GL11.glLineWidth(outlineThickness);
            float cornerValue = (float)((double)outlineThickness * 0.19);
            GLUtil.render(1, () -> {
                GL11.glVertex2d(x, y - cornerValue);
                GL11.glVertex2d(x, y + height + cornerValue);
                GL11.glVertex2d(x + width, y + height + cornerValue);
                GL11.glVertex2d(x + width, y - cornerValue);
                GL11.glVertex2d(x, y);
                GL11.glVertex2d(x + width, y);
                GL11.glVertex2d(x, y + height);
                GL11.glVertex2d(x + width, y + height);
            });
        });
        GL11.glDisable(2848);
    }

    public static void renderRoundedRect(float x, float y, float width, float height, float radius, int color) {
        RenderUtil.drawGoodCircle(x + radius, y + radius, radius, color);
        RenderUtil.drawGoodCircle(x + width - radius, y + radius, radius, color);
        RenderUtil.drawGoodCircle(x + radius, y + height - radius, radius, color);
        RenderUtil.drawGoodCircle(x + width - radius, y + height - radius, radius, color);
        Gui.drawRect2(x + radius, y, width - radius * 2.0f, height, color);
        Gui.drawRect2(x, y + radius, width, height - radius * 2.0f, color);
    }

    public static void scale(float x, float y, float scale, Runnable data) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glScalef(scale, scale, 1.0f);
        GL11.glTranslatef(-x, -y, 0.0f);
        data.run();
        GL11.glPopMatrix();
    }

    public static void scaleStart(float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0.0f);
        GlStateManager.scale(scale, scale, 1.0f);
        GlStateManager.translate(-x, -y, 0.0f);
    }

    public static void scaleEnd() {
        GlStateManager.popMatrix();
    }

    public static void drawGoodCircle(double x, double y, float radius, int color) {
        RenderUtil.color(color);
        GLUtil.setup2DRendering(() -> {
            GL11.glEnable(2832);
            GL11.glHint(3153, 4354);
            GL11.glPointSize(radius * (float)(2 * RenderUtil.mc.gameSettings.guiScale));
            GLUtil.render(0, () -> GL11.glVertex2d(x, y));
        });
    }

    public static void fakeCircleGlow(float posX, float posY, float radius, Color color, float maxAlpha) {
        RenderUtil.setAlphaLimit(0.0f);
        GL11.glShadeModel(7425);
        GLUtil.setup2DRendering(() -> GLUtil.render(6, () -> {
            RenderUtil.color(color.getRGB(), maxAlpha);
            GL11.glVertex2d(posX, posY);
            RenderUtil.color(color.getRGB(), 0.0f);
            for (int i = 0; i <= 100; ++i) {
                double angle = (double)i * 0.06283 + 3.1415;
                double x2 = Math.sin(angle) * (double)radius;
                double y2 = Math.cos(angle) * (double)radius;
                GL11.glVertex2d((double)posX + x2, (double)posY + y2);
            }
        }));
        GL11.glShadeModel(7424);
        RenderUtil.setAlphaLimit(1.0f);
    }

    public static double animate(double endPoint, double current, double speed) {
        boolean shouldContinueAnimation;
        boolean bl = shouldContinueAnimation = endPoint > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        double factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : -factor);
    }

    public static void drawCircleNotSmooth(double x, double y, double radius, int color) {
        radius /= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        RenderUtil.color(color);
        GL11.glBegin(6);
        for (double i = 0.0; i <= 360.0; i += 1.0) {
            double angle = i * 0.01745;
            GL11.glVertex2d(x + radius * Math.cos(angle) + radius, y + radius * Math.sin(angle) + radius);
        }
        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
    }

    public static void scissor(double x, double y, double width, double height, Runnable data) {
        GL11.glEnable(3089);
        RenderUtil.scissor(x, y, width, height);
        data.run();
        GL11.glDisable(3089);
    }

    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        double scale = sr.getScaleFactor();
        double finalHeight = height * scale;
        double finalY = ((double)sr.getScaledHeight() - y) * scale;
        double finalX = x * scale;
        double finalWidth = width * scale;
        GL11.glScissor((int)finalX, (int)(finalY - finalHeight), (int)finalWidth, (int)finalHeight);
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, (float)((double)limit * 0.01));
    }

    public static void color(int color, float alpha) {
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GlStateManager.color(r, g2, b, alpha);
    }

    public static void color(int color) {
        RenderUtil.color(color, (float)(color >> 24 & 0xFF) / 255.0f);
    }

    public static void bindTexture(int texture) {
        GL11.glBindTexture(3553, texture);
    }

    public static void resetColor() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}

