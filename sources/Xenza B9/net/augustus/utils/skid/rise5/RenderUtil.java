// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.rise5;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.util.glu.Sphere;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import java.util.Iterator;
import java.util.List;
import java.util.ConcurrentModificationException;
import net.augustus.Augustus;
import net.minecraft.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.culling.Frustum;
import net.augustus.utils.interfaces.MC;

public final class RenderUtil implements MC
{
    private static final Frustum frustrum;
    public long last2DFrame;
    public long last3DFrame;
    public float delta2DFrameTime;
    public float delta3DFrameTime;
    
    public RenderUtil() {
        this.last2DFrame = System.currentTimeMillis();
        this.last3DFrame = System.currentTimeMillis();
    }
    
    public void push() {
        GL11.glPushMatrix();
    }
    
    public void pop() {
        GL11.glPopMatrix();
    }
    
    public static void enable(final int glTarget) {
        GL11.glEnable(glTarget);
    }
    
    public static void disable(final int glTarget) {
        GL11.glDisable(glTarget);
    }
    
    public static void start() {
        enable(3042);
        GL11.glBlendFunc(770, 771);
        disable(3553);
        disable(2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }
    
    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        enable(2884);
        enable(3553);
        disable(3042);
        color(Color.white);
    }
    
    public void startSmooth() {
        enable(2881);
        enable(2848);
        enable(2832);
    }
    
    public void endSmooth() {
        disable(2832);
        disable(2848);
        disable(2881);
    }
    
    public static void begin(final int glMode) {
        GL11.glBegin(glMode);
    }
    
    public static void end() {
        GL11.glEnd();
    }
    
    public static void vertex(final double x, final double y) {
        GL11.glVertex2d(x, y);
    }
    
    public void translate(final double x, final double y) {
        GL11.glTranslated(x, y, 0.0);
    }
    
    public void scale(final double x, final double y) {
        GL11.glScaled(x, y, 1.0);
    }
    
    public void rotate(final double x, final double y, final double z, final double angle) {
        GL11.glRotated(angle, x, y, z);
    }
    
    public static void color(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
    
    public void color(final double red, final double green, final double blue) {
        color(red, green, blue, 1.0);
    }
    
    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public void color(Color color, final int alpha) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5);
    }
    
    public void lineWidth(final double width) {
        GL11.glLineWidth((float)width);
    }
    
    public static void rect(final double x, final double y, final double width, final double height, final boolean filled, final Color color) {
        start();
        if (color != null) {
            color(color);
        }
        begin(filled ? 6 : 1);
        vertex(x, y);
        vertex(x + width, y);
        vertex(x + width, y + height);
        vertex(x, y + height);
        if (!filled) {
            vertex(x, y);
            vertex(x, y + height);
            vertex(x + width, y);
            vertex(x + width, y + height);
        }
        end();
        stop();
    }
    
    public void rect(final double x, final double y, final double width, final double height, final boolean filled) {
        rect(x, y, width, height, filled, null);
    }
    
    public static void rect(final double x, final double y, final double width, final double height, final Color color) {
        rect(x, y, width, height, true, color);
    }
    
    public void rect(final double x, final double y, final double width, final double height) {
        rect(x, y, width, height, true, null);
    }
    
    public void rectCentered(double x, double y, final double width, final double height, final boolean filled, final Color color) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, filled, color);
    }
    
    public void rectCentered(double x, double y, final double width, final double height, final boolean filled) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, filled, null);
    }
    
    public void rectCentered(double x, double y, final double width, final double height, final Color color) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, true, color);
    }
    
    public void rectCentered(double x, double y, final double width, final double height) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, true, null);
    }
    
    public void gradient(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {
        start();
        GL11.glShadeModel(7425);
        GlStateManager.enableAlpha();
        GL11.glAlphaFunc(516, 0.0f);
        if (color1 != null) {
            color(color1);
        }
        begin(filled ? 7 : 1);
        vertex(x, y);
        vertex(x + width, y);
        if (color2 != null) {
            color(color2);
        }
        vertex(x + width, y + height);
        vertex(x, y + height);
        if (!filled) {
            vertex(x, y);
            vertex(x, y + height);
            vertex(x + width, y);
            vertex(x + width, y + height);
        }
        end();
        GL11.glAlphaFunc(516, 0.1f);
        GlStateManager.disableAlpha();
        GL11.glShadeModel(7424);
        stop();
    }
    
    public void gradient(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        this.gradient(x, y, width, height, true, color1, color2);
    }
    
    public void gradientCentered(double x, double y, final double width, final double height, final Color color1, final Color color2) {
        x -= width / 2.0;
        y -= height / 2.0;
        this.gradient(x, y, width, height, true, color1, color2);
    }
    
    public void gradientSideways(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {
        start();
        GL11.glShadeModel(7425);
        GlStateManager.disableAlpha();
        if (color1 != null) {
            color(color1);
        }
        begin(filled ? 6 : 1);
        vertex(x, y);
        vertex(x, y + height);
        if (color2 != null) {
            color(color2);
        }
        vertex(x + width, y + height);
        vertex(x + width, y);
        end();
        GlStateManager.enableAlpha();
        GL11.glShadeModel(7424);
        stop();
    }
    
    public void gradientSideways(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        this.gradientSideways(x, y, width, height, true, color1, color2);
    }
    
    public void gradientSidewaysCentered(double x, double y, final double width, final double height, final Color color1, final Color color2) {
        x -= width / 2.0;
        y -= height / 2.0;
        this.gradientSideways(x, y, width, height, true, color1, color2);
    }
    
    public void polygon(final double x, final double y, double sideLength, final double amountOfSides, final boolean filled, final Color color) {
        sideLength /= 2.0;
        start();
        if (color != null) {
            color(color);
        }
        if (!filled) {
            GL11.glLineWidth(2.0f);
        }
        GL11.glEnable(2848);
        begin(filled ? 6 : 3);
        for (double i = 0.0; i <= amountOfSides / 4.0; ++i) {
            final double angle = i * 4.0 * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides, final boolean filled) {
        this.polygon(x, y, sideLength, amountOfSides, filled, null);
    }
    
    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides, final Color color) {
        this.polygon(x, y, sideLength, amountOfSides, true, color);
    }
    
    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides) {
        this.polygon(x, y, sideLength, amountOfSides, true, null);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final boolean filled, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, filled, color);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final boolean filled) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, filled, null);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, true, color);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, true, null);
    }
    
    public void circle(final double x, final double y, final double radius, final boolean filled, final Color color) {
        this.polygon(x, y, radius, 360.0, filled, color);
    }
    
    public void circle(final double x, final double y, final double radius, final boolean filled) {
        this.polygon(x, y, radius, 360, filled);
    }
    
    public void circle(final double x, final double y, final double radius, final Color color) {
        this.polygon(x, y, radius, 360, color);
    }
    
    public void circle(final double x, final double y, final double radius) {
        this.polygon(x, y, radius, 360);
    }
    
    public void circleCentered(double x, double y, final double radius, final boolean filled, final Color color) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360.0, filled, color);
    }
    
    public void circleCentered(double x, double y, final double radius, final boolean filled) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360, filled);
    }
    
    public void circleCentered(double x, double y, final double radius, final boolean filled, final int sides) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, sides, filled);
    }
    
    public void circleCentered(double x, double y, final double radius, final Color color) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360, color);
    }
    
    public void circleCentered(double x, double y, final double radius) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360);
    }
    
    public void triangle(final double x, final double y, final double sideLength, final boolean filled, final Color color) {
        this.polygon(x, y, sideLength, 3.0, filled, color);
    }
    
    public void triangle(final double x, final double y, final double sideLength, final boolean filled) {
        this.polygon(x, y, sideLength, 3, filled);
    }
    
    public void triangle(final double x, final double y, final double sideLength, final Color color) {
        this.polygon(x, y, sideLength, 3, color);
    }
    
    public void triangle(final double x, final double y, final double sideLength) {
        this.polygon(x, y, sideLength, 3);
    }
    
    public void triangleCentered(double x, double y, final double sideLength, final boolean filled, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3.0, filled, color);
    }
    
    public void triangleCentered(double x, double y, final double sideLength, final boolean filled) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3, filled);
    }
    
    public void triangleCentered(double x, double y, final double sideLength, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3, color);
    }
    
    public void triangleCentered(double x, double y, final double sideLength) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3);
    }
    
    public void lineNoGl(final double firstX, final double firstY, final double secondX, final double secondY, final Color color) {
        start();
        if (color != null) {
            color(color);
        }
        lineWidth(1.0f);
        GL11.glEnable(2848);
        begin(1);
        vertex(firstX, firstY);
        vertex(secondX, secondY);
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth, final Color color) {
        start();
        if (color != null) {
            color(color);
        }
        this.lineWidth(lineWidth);
        GL11.glEnable(2848);
        begin(1);
        vertex(firstX, firstY);
        vertex(secondX, secondY);
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth) {
        this.line(firstX, firstY, secondX, secondY, lineWidth, null);
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final Color color) {
        this.line(firstX, firstY, secondX, secondY, 0.0, color);
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY) {
        this.line(firstX, firstY, secondX, secondY, 0.0, null);
    }
    
    public void image(final ResourceLocation imageLocation, final float x, final float y, final float width, final float height) {
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        enable(3042);
        GlStateManager.disableAlpha();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        RenderUtil.mc.getTextureManager().bindTexture(imageLocation);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0.0f, 0.0f, (int)width, (int)height, width, height);
        GlStateManager.enableAlpha();
        disable(3042);
    }
    
    public void imageCentered(final ResourceLocation imageLocation, float x, float y, final float width, final float height) {
        x -= width / 2.0f;
        y -= height / 2.0f;
        this.image(imageLocation, x, y, width, height);
    }
    
    public static void scissor(double x, double y, double width, double height) {
        final ScaledResolution sr = new ScaledResolution(RenderUtil.mc);
        final double scale = sr.getScaleFactor();
        y = sr.getScaledHeight() - y;
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;
        GL11.glScissor((int)x, (int)(y - height), (int)width, (int)height);
    }
    
    public void outlineInlinedGradientRect(final double x, final double y, final double width, final double height, final double inlineOffset, final Color color1, final Color color2) {
        this.gradient(x, y, width, inlineOffset, color1, color2);
        this.gradient(x, y + height - inlineOffset, width, inlineOffset, color2, color1);
        this.gradientSideways(x, y, inlineOffset, height, color1, color2);
        this.gradientSideways(x + width - inlineOffset, y, inlineOffset, height, color2, color1);
    }
    
    public void roundedRect(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        float sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        begin(6);
        for (double i = 180.0; i <= 270.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        vertex(x + sideLength, y + sideLength);
        end();
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(6);
        for (double i = 0.0; i <= 90.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + width + sideLength * Math.cos(angle), y + height + sideLength * Math.sin(angle));
        }
        vertex(x + width, y + height);
        end();
        GL11.glDisable(2848);
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(6);
        for (double i = 270.0; i <= 360.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + width + sideLength * Math.cos(angle), y + sideLength * Math.sin(angle) + sideLength);
        }
        vertex(x + width, y + sideLength);
        end();
        GL11.glDisable(2848);
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(6);
        for (double i = 90.0; i <= 180.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + height + sideLength * Math.sin(angle));
        }
        vertex(x + sideLength, y + height);
        end();
        GL11.glDisable(2848);
        stop();
        rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);
        rect(x, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + width, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
    
    public void roundedOutLine(final double x, final double y, double width, double height, final double thickness, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        float sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(1);
        for (double i = 180.0; i <= 270.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        vertex(x + sideLength, y + sideLength);
        end();
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(1);
        for (double i = 0.0; i <= 90.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + width + sideLength * Math.cos(angle), y + height + sideLength * Math.sin(angle));
        }
        vertex(x + width, y + height);
        end();
        GL11.glDisable(2848);
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(1);
        for (double i = 270.0; i <= 360.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + width + sideLength * Math.cos(angle), y + sideLength * Math.sin(angle) + sideLength);
        }
        vertex(x + width, y + sideLength);
        end();
        GL11.glDisable(2848);
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(1);
        for (double i = 90.0; i <= 180.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + height + sideLength * Math.sin(angle));
        }
        vertex(x + sideLength, y + height);
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public static void roundedRectCustom(final double x, final double y, double width, double height, final double edgeRadius, final Color color, final boolean topLeft, final boolean topRight, final boolean bottomLeft, final boolean bottomRight) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        float sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        if (topLeft) {
            GL11.glEnable(2848);
            begin(6);
            for (double i = 180.0; i <= 270.0; ++i) {
                final double angle = i * 6.283185307179586 / 360.0;
                vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
            }
            vertex(x + sideLength, y + sideLength);
            end();
            GL11.glDisable(2848);
            stop();
        }
        else {
            rect(x, y, sideLength, sideLength, color);
        }
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        if (bottomRight) {
            GL11.glEnable(2848);
            begin(6);
            for (double i = 0.0; i <= 90.0; ++i) {
                final double angle = i * 6.283185307179586 / 360.0;
                vertex(x + width + sideLength * Math.cos(angle), y + height + sideLength * Math.sin(angle));
            }
            vertex(x + width, y + height);
            end();
            GL11.glDisable(2848);
            stop();
        }
        else {
            rect(x + width, y + height, sideLength, sideLength, color);
        }
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        if (topRight) {
            GL11.glEnable(2848);
            begin(6);
            for (double i = 270.0; i <= 360.0; ++i) {
                final double angle = i * 6.283185307179586 / 360.0;
                vertex(x + width + sideLength * Math.cos(angle), y + sideLength * Math.sin(angle) + sideLength);
            }
            vertex(x + width, y + sideLength);
            end();
            GL11.glDisable(2848);
            stop();
        }
        else {
            rect(x + width, y, sideLength, sideLength, color);
        }
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        if (bottomLeft) {
            GL11.glEnable(2848);
            begin(6);
            for (double i = 90.0; i <= 180.0; ++i) {
                final double angle = i * 6.283185307179586 / 360.0;
                vertex(x + sideLength * Math.cos(angle) + sideLength, y + height + sideLength * Math.sin(angle));
            }
            vertex(x + sideLength, y + height);
            end();
            GL11.glDisable(2848);
            stop();
        }
        else {
            rect(x, y + height, sideLength, sideLength, color);
        }
        rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);
        rect(x, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + width, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
    
    public void roundedRectTop(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        this.circle(x, y, edgeRadius, color);
        this.circle(x + width - edgeRadius / 2.0, y, edgeRadius, color);
        rect(x, y + halfRadius, width + halfRadius, height, color);
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
    }
    
    public void roundedRectBottom(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        this.circle(x + width - edgeRadius / 2.0, y + height - edgeRadius / 2.0, edgeRadius, color);
        this.circle(x, y + height - edgeRadius / 2.0, edgeRadius, color);
        rect(x, y, width + halfRadius, height, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
    
    public void roundedRectRight(final double x, final double y, double width, double height, final double edgeRadius, final Color color1, final Color color2) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        this.circle(x + width - edgeRadius / 2.0, y, edgeRadius, color2);
        this.circle(x + width - edgeRadius / 2.0, y + height - edgeRadius / 2.0, edgeRadius, color2);
        this.gradientSideways(x, y, width, height + halfRadius, color1, color2);
        rect(x + width, y + halfRadius, 5.0, height - halfRadius, color2);
    }
    
    public void roundedRectRightTop(final double x, final double y, double width, double height, final double edgeRadius, final Color color1, final Color color2) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        this.circle(x + width - edgeRadius / 2.0, y, edgeRadius, color2);
        this.gradientSideways(x, y, width, height + halfRadius, color1, color2);
        rect(x + width, y + halfRadius, 5.0, height, color2);
    }
    
    public void roundedRectRightBottom(final double x, final double y, double width, double height, final double edgeRadius, final Color color1, final Color color2) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        this.circle(x + width - edgeRadius / 2.0, y + height - edgeRadius / 2.0, edgeRadius, color2);
        this.gradientSideways(x, y, width, height + halfRadius, color1, color2);
        rect(x + width, y, 5.0, height, color2);
    }
    
    public void drawBorder(final float x, final float y, final float x2, final float y2, final float width, final int color1) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        color(new Color(color1));
        GL11.glLineWidth(width);
        glBegin(2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawTracerLine(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha, final float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0, 0.0 + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawLine(final double x, final double y, final double z, final double x2, final double y2, final double z2, final float red, final float green, final float blue, final float alpha, final float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        Vec3 renderPos = getRenderPos(x2, y2, z2);
        GL11.glVertex3d(renderPos.xCoord, renderPos.yCoord, renderPos.zCoord);
        renderPos = getRenderPos(x, y, z);
        GL11.glVertex3d(renderPos.xCoord, renderPos.yCoord, renderPos.zCoord);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public void renderBreadCrumb(final Vec3 vec3) {
        GlStateManager.disableDepth();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        try {
            final double x = vec3.xCoord - RenderUtil.mc.getRenderManager().renderPosX;
            final double y = vec3.yCoord - RenderUtil.mc.getRenderManager().renderPosY;
            final double z = vec3.zCoord - RenderUtil.mc.getRenderManager().renderPosZ;
            final double distanceFromPlayer = RenderUtil.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord - 1.0, vec3.zCoord);
            int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
            if (quality > 350) {
                quality = 350;
            }
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            final float scale = 0.04f;
            GL11.glScalef(-0.04f, -0.04f, -0.04f);
            GL11.glRotated((double)(-RenderUtil.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GL11.glRotated((double)RenderUtil.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
            final Color c = Augustus.getInstance().getClientColor();
            drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);
            if (distanceFromPlayer < 4.0) {
                drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);
            }
            if (distanceFromPlayer < 20.0) {
                drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);
            }
            GL11.glScalef(0.8f, 0.8f, 0.8f);
            GL11.glPopMatrix();
        }
        catch (final ConcurrentModificationException ex) {}
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableDepth();
        GL11.glColor3d(255.0, 255.0, 255.0);
    }
    
    public void renderBreadCrumbs(final List<Vec3> vec3s) {
        GlStateManager.disableDepth();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        int i = 0;
        try {
            for (final Vec3 v : vec3s) {
                ++i;
                boolean draw = true;
                final double x = v.xCoord - RenderUtil.mc.getRenderManager().renderPosX;
                final double y = v.yCoord - RenderUtil.mc.getRenderManager().renderPosY;
                final double z = v.zCoord - RenderUtil.mc.getRenderManager().renderPosZ;
                final double distanceFromPlayer = RenderUtil.mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1.0, v.zCoord);
                int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
                if (quality > 350) {
                    quality = 350;
                }
                if (i % 10 != 0 && distanceFromPlayer > 25.0) {
                    draw = false;
                }
                if (i % 3 == 0 && distanceFromPlayer > 15.0) {
                    draw = false;
                }
                if (draw) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);
                    final float scale = 0.04f;
                    GL11.glScalef(-0.04f, -0.04f, -0.04f);
                    GL11.glRotated((double)(-RenderUtil.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                    GL11.glRotated((double)RenderUtil.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
                    final Color c = Augustus.getInstance().getClientColor();
                    drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);
                    if (distanceFromPlayer < 4.0) {
                        drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);
                    }
                    if (distanceFromPlayer < 20.0) {
                        drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);
                    }
                    GL11.glScalef(0.8f, 0.8f, 0.8f);
                    GL11.glPopMatrix();
                }
            }
        }
        catch (final ConcurrentModificationException ex) {}
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableDepth();
        GL11.glColor3d(255.0, 255.0, 255.0);
    }
    
    public static void putVertex3DInWorld(final double x, final double y, final double z) {
        putVertex3d(getRenderPos(x, y, z));
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        begin(1);
        putVertex3d(aa.minX, aa.minY, aa.minZ);
        putVertex3d(aa.maxX, aa.minY, aa.minZ);
        putVertex3d(aa.maxX, aa.minY, aa.maxZ);
        putVertex3d(aa.minX, aa.minY, aa.maxZ);
        putVertex3d(aa.minX, aa.minY, aa.minZ);
        end();
        worldRenderer.begin(7, worldRenderer.getVertexFormat());
        putVertex3d(aa.minX, aa.maxY, aa.minZ);
        putVertex3d(aa.maxX, aa.maxY, aa.minZ);
        putVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3d(aa.minX, aa.maxY, aa.maxZ);
        putVertex3d(aa.minX, aa.maxY, aa.minZ);
        tessellator.draw();
        worldRenderer.begin(7, worldRenderer.getVertexFormat());
        putVertex3d(aa.minX, aa.minY, aa.minZ);
        putVertex3d(aa.minX, aa.maxY, aa.minZ);
        putVertex3d(aa.maxX, aa.minY, aa.minZ);
        putVertex3d(aa.maxX, aa.maxY, aa.minZ);
        putVertex3d(aa.maxX, aa.minY, aa.maxZ);
        putVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3d(aa.minX, aa.minY, aa.maxZ);
        putVertex3d(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB aa) {
        begin(5);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        end();
        begin(5);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        end();
        begin(5);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        end();
        begin(5);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        end();
        begin(5);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        end();
        begin(5);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.maxZ);
        putVertex3DInWorld(aa.minX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.minX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.minZ);
        putVertex3DInWorld(aa.maxX, aa.maxY, aa.maxZ);
        putVertex3DInWorld(aa.maxX, aa.minY, aa.maxZ);
        end();
    }
    
    public static void drawOutlinedBlockESP(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha, final float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawBlockESP(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha, final float lineRed, final float lineGreen, final float lineBlue, final float lineAlpha, final float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawSolidBlockESP(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha) {
        GL11.glPushMatrix();
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedEntityESP(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedEntityESPFixed(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha, final Entity e) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawSolidEntityESP(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawSolidEntityESPFixed(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha, final Entity e) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawEntityESP(final double x, final double y, final double z, final double width, final double height, final float red, final float green, final float blue, final float alpha, final float lineRed, final float lineGreen, final float lineBlue, final float lineAlpha, final float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void draw3DLine(double x, double y, double z, double x1, double y1, double z1, final float red, final float green, final float blue, final float alpha, final float lineWdith) {
        x -= RenderUtil.mc.getRenderManager().renderPosX;
        x1 -= RenderUtil.mc.getRenderManager().renderPosX;
        y -= RenderUtil.mc.getRenderManager().renderPosY;
        y1 -= RenderUtil.mc.getRenderManager().renderPosY;
        z -= RenderUtil.mc.getRenderManager().renderPosZ;
        z1 -= RenderUtil.mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void color4f(final float red, final float green, final float blue, final float alpha) {
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void lineWidth(final float width) {
        GL11.glLineWidth(width);
    }
    
    public static void glBegin(final int mode) {
        GL11.glBegin(mode);
    }
    
    public static void glEnd() {
        GL11.glEnd();
    }
    
    public static void putVertex3d(final double x, final double y, final double z) {
        GL11.glVertex3d(x, y, z);
    }
    
    public static void putVertex3d(final Vec3 vec) {
        GL11.glVertex3d(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public static Vec3 getRenderPos(double x, double y, double z) {
        x -= RenderUtil.mc.getRenderManager().renderPosX;
        y -= RenderUtil.mc.getRenderManager().renderPosY;
        z -= RenderUtil.mc.getRenderManager().renderPosZ;
        return new Vec3(x, y, z);
    }
    
    public static void drawCircle(final int x, final int y, final double r, final float f1, final float f2, final float f3, final float f) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            final double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawFilledCircle(final int x, final int y, final double r, final int c) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            final double x2 = Math.sin(i * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawFilledCircle(final int x, final int y, final double r, final int c, final int quality) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawFilledCircle(final double x, final double y, final double r, final int c, final int quality) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 18; ++i) {
            final double x2 = Math.sin(i * 20 * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * 20 * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
    }
    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
    }
    
    public static void drawSphere(final double red, final double green, final double blue, final double alpha, final double x, final double y, final double z, final float size, final int slices, final int stacks, final float lWidth) {
        final Sphere sphere = new Sphere();
        enableDefaults();
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glTranslated(x, y, z);
        GL11.glLineWidth(lWidth);
        sphere.setDrawStyle(100013);
        sphere.draw(size, slices, stacks);
        disableDefaults();
    }
    
    public static void enableDefaults() {
        RenderUtil.mc.entityRenderer.disableLightmap();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
    }
    
    public static void disableDefaults() {
        GL11.glPopMatrix();
        GL11.glDisable(2848);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        RenderUtil.mc.entityRenderer.enableLightmap();
    }
    
    public static boolean isInViewFrustrum(final Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
    
    private static boolean isInViewFrustrum(final AxisAlignedBB bb) {
        final Entity current = RenderUtil.mc.getRenderViewEntity();
        RenderUtil.frustrum.setPosition(current.posX, current.posY, current.posZ);
        return RenderUtil.frustrum.isBoundingBoxInFrustum(bb);
    }
    
    public static void quickDrawRect(final float x, final float y, final float x2, final float y2, final int color) {
        glColor(color);
        glBegin(7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        glEnd();
    }
    
    public static void quickDrawBorderedRect(final float x, final float y, final float x2, final float y2, final float width, final int color1, final int color2) {
        quickDrawRect(x, y, x2, y2, color2);
        glColor(color1);
        GL11.glLineWidth(width);
        glBegin(2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        glEnd();
    }
    
    private static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void renderGradientRectLeftRight(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos(right, top, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos(left, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, bottom, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void rainbowRectangle(final float x, final float y, final float width, final float height, final float divider) {
        for (int i = 0; i <= width; ++i) {
            rect(x + i, y, 1.0, height, new Color(ColorUtil.getColor(i / divider, 0.7f, 1.0f)));
        }
    }
    
    public static void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos(right, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawModel(final float yaw, final float pitch, final EntityLivingBase entityLivingBase) {
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 50.0f);
        GlStateManager.scale(-50.0f, 50.0f, 50.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float renderYawOffset = entityLivingBase.renderYawOffset;
        final float rotationYaw = entityLivingBase.rotationYaw;
        final float rotationPitch = entityLivingBase.rotationPitch;
        final float prevRotationYawHead = entityLivingBase.prevRotationYawHead;
        final float rotationYawHead = entityLivingBase.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan(pitch / 40.0f) * 20.0), 1.0f, 0.0f, 0.0f);
        entityLivingBase.renderYawOffset = yaw - yaw / yaw * 0.4f;
        entityLivingBase.rotationYaw = yaw - yaw / yaw * 0.2f;
        entityLivingBase.rotationPitch = pitch;
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager renderManager = RenderUtil.mc.getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntityWithPosYaw(entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.setRenderShadow(true);
        entityLivingBase.renderYawOffset = renderYawOffset;
        entityLivingBase.rotationYaw = rotationYaw;
        entityLivingBase.rotationPitch = rotationPitch;
        entityLivingBase.prevRotationYawHead = prevRotationYawHead;
        entityLivingBase.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.resetColor();
    }
    
    public static void skeetRect(final double x, final double y, final double x1, final double y1, final double size) {
        rectangleBordered(x, y - 4.0, x1 + size, y1 + size, 0.5, new Color(60, 60, 60).getRGB(), new Color(10, 10, 10).getRGB());
        rectangleBordered(x + 1.0, y - 3.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
        rectangleBordered(x + 2.5, y - 1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB());
        rectangleBordered(x + 2.5, y - 1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(22, 22, 22).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }
    
    public static void skeetRectSmall(final double x, final double y, final double x1, final double y1, final double size) {
        rectangleBordered(x + 4.35, y + 0.5, x1 + size - 84.5, y1 + size - 4.35, 0.5, new Color(48, 48, 48).getRGB(), new Color(10, 10, 10).getRGB());
        rectangleBordered(x + 5.0, y + 1.0, x1 + size - 85.0, y1 + size - 5.0, 0.5, new Color(17, 17, 17).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }
    
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        frustrum = new Frustum();
    }
}
