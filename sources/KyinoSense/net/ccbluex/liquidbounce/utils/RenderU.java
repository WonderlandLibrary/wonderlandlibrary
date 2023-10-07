/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class RenderU
extends MinecraftInstance {
    public static int deltaTime;
    private static final int[] DISPLAY_LISTS_2D;
    private static Frustum frustrum;

    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float)time + (1.0f + count) * 2.0E8f) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
    }

    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderU.glColor(color);
        RenderU.drawFilledBox(axisAlignedBB);
        GlStateManager.func_179117_G();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void glColor114514(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void drawTriAngle(float cx, float cy, float r, float n, int color) {
        GL11.glPushMatrix();
        cx = (float)((double)cx * 2.0);
        cy = (float)((double)cy * 2.0);
        double b = Math.PI * 2 / (double)n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = (double)r * 2.0;
        double y = 0.0;
        RenderU.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179117_G();
        RenderU.glColor114514(color);
        GL11.glBegin((int)2);
        int ii = 0;
        while ((float)ii < n) {
            GL11.glVertex2d((double)(x + (double)cx), (double)(y + (double)cy));
            double t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ++ii;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderU.disableGL2D();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    public static void drawRect(float x, float y, float x2, float y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderU.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderU.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void quickDrawRect(float x, float y, float x2, float y2, int color) {
        RenderU.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
    }

    public static void drawRect(float x, float y, float x2, float y2, Color color) {
        RenderU.drawRect(x, y, x2, y2, color.getRGB());
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float width, int color1, int color2) {
        RenderU.drawRect(x, y, x2, y2, color2);
        RenderU.drawBorder(x, y, x2, y2, width, color1);
    }

    public static void drawBorder(float x, float y, float x2, float y2, float width, int color1) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderU.glColor(color1);
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation image2, Color color) {
        GL11.glDisable((int)2839);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)(color.getRed() / 255), (float)(color.getGreen() / 255), (float)(color.getBlue() / 255), (float)(color.getAlpha() / 255));
        mc.func_110434_K().func_110577_a(image2);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2839);
    }

    public static void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.func_179131_c((float)((float)red / 255.0f), (float)((float)green / 255.0f), (float)((float)blue / 255.0f), (float)((float)alpha / 255.0f));
    }

    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)(0.01 * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation += (double)add) : finalState) : (animation - (double)add > finalState ? (animation -= (double)add) : finalState);
        return animation;
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        RenderU.glColor(col1);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        RenderU.glColor(col2);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void circle(float x, float y, float radius, int fill) {
        RenderU.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle(float x, float y, float radius, Color fill) {
        RenderU.arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arcIiiilllIIiii(float x, float y, float start, float end, float radius, int color, float lineWidth) {
        RenderU.illlIIIIiii(x, y, start, end, radius, radius, color, lineWidth);
    }

    public static void drawOutFullCircle(float x, float y, float radius, int fill, float lineWidth) {
        RenderU.arcIiiilllIIiii(x, y, 0.0f, 360.0f, radius, fill, lineWidth);
    }

    public static void drawOutFullCircle(float x, float y, float radius, int fill, float lineWidth, float start, float end) {
        RenderU.arcIiiilllIIiii(x, y, start, end, radius, fill, lineWidth);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        RenderU.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, Color color) {
        RenderU.arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawHLine(float par1, float par2, float par3, int par4) {
        if (par2 < par1) {
            float var5 = par1;
            par1 = par2;
            par2 = var5;
        }
        RenderU.drawRect(par1, par3, par2 + 1.0f, par3 + 1.0f, par4);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderU.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void enableGL3D(float lineWidth) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)lineWidth);
    }

    public static void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 0xFF) / 255.0f;
        float red = (float)(colorHex >> 16 & 0xFF) / 255.0f;
        float green = (float)(colorHex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(colorHex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)(alpha == 0.0f ? 1.0f : alpha));
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        RenderU.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        RenderU.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        RenderU.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        RenderU.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        RenderU.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        RenderU.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        RenderU.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        RenderU.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        RenderU.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        RenderU.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderU.disableGL2D();
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
    }

    public static void illlIIIIiii(float x, float y, float start, float end, float w, float h, int color, float lineWidth) {
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)3);
        for (float i = end; i >= start; i -= 4.0f) {
            GL11.glVertex2d((double)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)w * 1.001), (double)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)h * 1.001));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
        double ldy;
        double ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        if (start > end) {
            float temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)3);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = Math.cos((double)i * Math.PI / 180.0) * (double)w * (double)1.001f;
            ldy = Math.sin((double)i * Math.PI / 180.0) * (double)h * (double)1.001f;
            GL11.glVertex2d((double)((double)x + ldx), (double)((double)y + ldy));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = Math.cos((double)i * Math.PI / 180.0) * (double)w;
            ldy = Math.sin((double)i * Math.PI / 180.0) * (double)h;
            GL11.glVertex2d((double)((double)x + ldx), (double)((double)y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void arcEllipse(float x, float y, float start, float end, float w, float h, Color color) {
        float ldy;
        float ldx;
        float i;
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        Tessellator var9 = Tessellator.func_178181_a();
        var9.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        if ((float)color.getAlpha() > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (i = end; i >= start; i -= 4.0f) {
                ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w * 1.001f;
                ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (i = end; i >= start; i -= 4.0f) {
            ldx = (float)Math.cos((double)i * Math.PI / 180.0) * w;
            ldy = (float)Math.sin((double)i * Math.PI / 180.0) * h;
            GL11.glVertex2f((float)(x + ldx), (float)(y + ldy));
        }
        GL11.glEnd();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldRenderer = tessellator.func_178180_c();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        worldRenderer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72339_c).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72336_d, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72338_b, aa.field_72334_f).func_181675_d();
        worldRenderer.func_181662_b(aa.field_72340_a, aa.field_72337_e, aa.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void pre() {
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public static void post() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
    }

    public static void rectangle(double x, double y, double x2, double y2, int color) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        RenderU.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double)RenderU.mc.field_71428_T.field_74281_c;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderU.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        RenderU.rectangle(x + width, y, x1 - width, y + width, borderColor);
        RenderU.rectangle(x, y, x + width, y1, borderColor);
        RenderU.rectangle(x1 - width, y, x1, y1, borderColor);
        RenderU.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = mc.func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), 0.0).func_181673_a((double)(u * f), (double)((v + height) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), 0.0).func_181673_a((double)((u + width) * f), (double)((v + height) * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, 0.0).func_181673_a((double)((u + width) * f), (double)(v * f1)).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, 0.0).func_181673_a((double)(u * f), (double)(v * f1)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)x, (float)y1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x1, (float)y);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
        RenderU.enableGL2D();
        RenderU.glColor(borderColor);
        RenderU.drawRect(x + width, y, x1 - width, y + width);
        RenderU.drawRect(x, y, x + width, y1);
        RenderU.drawRect(x1 - width, y, x1, y1);
        RenderU.drawRect(x + width, y1 - width, x1 - width, y1);
        RenderU.disableGL2D();
    }

    public static int width() {
        return new ScaledResolution(mc).func_78326_a();
    }

    public static int height() {
        return new ScaledResolution(mc).func_78328_b();
    }

    static {
        DISPLAY_LISTS_2D = new int[4];
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            RenderU.DISPLAY_LISTS_2D[i] = GL11.glGenLists((int)1);
        }
        GL11.glNewList((int)DISPLAY_LISTS_2D[0], (int)4864);
        RenderU.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        RenderU.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        RenderU.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        RenderU.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[1], (int)4864);
        RenderU.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        RenderU.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        RenderU.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        RenderU.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[2], (int)4864);
        RenderU.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        RenderU.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        RenderU.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        RenderU.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList((int)DISPLAY_LISTS_2D[3], (int)4864);
        RenderU.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        RenderU.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        RenderU.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        RenderU.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
        frustrum = new Frustum();
    }
}

