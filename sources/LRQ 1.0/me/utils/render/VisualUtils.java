/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package me.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class VisualUtils
extends MinecraftInstance {
    public static int SkyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright).getRGB();
    }

    public static int getRainbow(int index, int offset, float bright, float st) {
        float hue = (System.currentTimeMillis() + (long)offset * (long)index) % 2000L;
        return Color.getHSBColor(hue /= 2000.0f, st, bright).getRGB();
    }

    public static Color skyRainbow(int var2, float st, float bright) {
        double d;
        double v1 = Math.ceil(System.currentTimeMillis() + (long)(var2 * 109)) / 5.0;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(v1 / 360.0)) : (float)((v1 %= 360.0) / 360.0), st, bright);
    }

    public static int Astolfo(int var2, float bright, float st, int index, int offset, float client2) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(var2 * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / (double)client2)) < 0.5 ? -((float)(rainbowDelay / (double)client2)) : (float)((rainbowDelay %= (double)client2) / (double)client2), st, bright).getRGB();
    }

    public static int getRainbowOpaque(int seconds, float saturation, float brightness, int index) {
        float hue = (float)((System.currentTimeMillis() + (long)index) % (long)(seconds * 1000)) / (float)(seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static int getNormalRainbow(int delay, float sat, float brg) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), sat, brg).getRGB();
    }

    public static void glColor(int cl) {
        float alpha = (float)(cl >> 24 & 0xFF) / 255.0f;
        float red = (float)(cl >> 16 & 0xFF) / 255.0f;
        float green = (float)(cl >> 8 & 0xFF) / 255.0f;
        float blue = (float)(cl & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
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

    public static void glColor(int hex, int alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)((float)alpha / 255.0f));
    }

    public static void glColor(int hex, float alpha) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void glColor(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        GlStateManager.func_179131_c((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static Color reAlpha(Color cIn, float alpha) {
        return new Color((float)cIn.getRed() / 255.0f, (float)cIn.getGreen() / 255.0f, (float)cIn.getBlue() / 255.0f, (float)cIn.getAlpha() / 255.0f * alpha);
    }

    public static Color reAlpha(Color cIn, int alpha) {
        return new Color((float)cIn.getRed() / 255.0f, (float)cIn.getGreen() / 255.0f, (float)cIn.getBlue() / 255.0f, (float)cIn.getAlpha() / 255.0f * (float)alpha);
    }

    public static int reAlpha(int n, float n2) {
        Color color = new Color(n);
        return new Color(0.003921569f * (float)color.getRed(), 0.003921569f * (float)color.getGreen(), 0.003921569f * (float)color.getBlue(), n2).getRGB();
    }

    private static void quickPolygonCircle(float x, float y, float xRadius, float yRadius, int start, int end, int split) {
        for (int i = end; i >= start; i -= split) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)xRadius), (double)((double)y + Math.cos((double)i * Math.PI / 180.0) * (double)yRadius));
        }
    }

    public static void drawCircleRect(float x, float y, float x1, float y1, float radius, int color) {
        VisualUtils.glColor(color);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2884);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)9);
        float xRadius = (float)Math.min((double)(x1 - x) * 0.5, (double)radius);
        float yRadius = (float)Math.min((double)(y1 - y) * 0.5, (double)radius);
        VisualUtils.quickPolygonCircle(x + xRadius, y + yRadius, xRadius, yRadius, 180, 270, 4);
        VisualUtils.quickPolygonCircle(x1 - xRadius, y + yRadius, xRadius, yRadius, 90, 180, 4);
        VisualUtils.quickPolygonCircle(x1 - xRadius, y1 - yRadius, xRadius, yRadius, 0, 90, 4);
        VisualUtils.quickPolygonCircle(x + xRadius, y1 - yRadius, xRadius, yRadius, 270, 360, 4);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawShadow(int x, int y, int width, int height) {
        ScaledResolution sr = new ScaledResolution((Minecraft)mc);
        VisualUtils.drawTexturedRect(x - 9, y - 9, 9, 9, "paneltopleft", sr);
        VisualUtils.drawTexturedRect(x - 9, y + height, 9, 9, "panelbottomleft", sr);
        VisualUtils.drawTexturedRect(x + width, y + height, 9, 9, "panelbottomright", sr);
        VisualUtils.drawTexturedRect(x + width, y - 9, 9, 9, "paneltopright", sr);
        VisualUtils.drawTexturedRect(x - 9, y, 9, height, "panelleft", sr);
        VisualUtils.drawTexturedRect(x + width, y, 9, height, "panelright", sr);
        VisualUtils.drawTexturedRect(x, y - 9, width, 9, "paneltop", sr);
        VisualUtils.drawTexturedRect(x, y + height, width, 9, "panelbottom", sr);
    }

    public static void drawTexturedRect(int x, int y, int width, int height, String image2, ScaledResolution sr) {
        GL11.glPushMatrix();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        mc.getTextureManager().bindTexture(classProvider.createResourceLocation("liquidbounce/shadow/" + image2 + ".png"));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.func_146110_a((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GL11.glPopMatrix();
    }

    public static Color getGradientOffset2(Color color1, Color color2, double gident) {
        if (gident > 1.0) {
            double f1 = gident % 1.0;
            int f2 = (int)gident;
            gident = f2 % 2 == 0 ? f1 : 1.0 - f1;
        }
        double f3 = 1.0 - gident;
        int f4 = (int)((double)color1.getRed() * f3 + (double)color2.getRed() * gident);
        int f5 = (int)((double)color1.getGreen() * f3 + (double)color2.getGreen() * gident);
        int f6 = (int)((double)color1.getBlue() * f3 + (double)color2.getBlue() * gident);
        return new Color(f4, f5, f6);
    }

    public static Color getGradientOffset(Color color1, Color color2, double index) {
        double offs = Math.abs((double)System.currentTimeMillis() / 16.0) / 60.0 + index;
        if (offs > 1.0) {
            double left = offs % 1.0;
            int off = (int)offs;
            offs = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offs;
        int redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offs);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offs);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offs);
        int alphaPart = (int)((double)color1.getAlpha() * inverse_percent + (double)color2.getAlpha() * offs);
        return new Color(redPart, greenPart, bluePart, alphaPart);
    }
}

