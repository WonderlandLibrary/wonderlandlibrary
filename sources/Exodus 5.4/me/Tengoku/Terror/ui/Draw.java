/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.ui;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Draw {
    public static void drawCircle(double d, double d2, float f, int n) {
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glBegin((int)9);
        int n2 = 0;
        while (n2 <= 360) {
            GL11.glVertex2d((double)(d + Math.sin((double)n2 * 3.141526 / 180.0) * (double)f), (double)(d2 + Math.cos((double)n2 * 3.141526 / 180.0) * (double)f));
            ++n2;
        }
        GL11.glEnd();
    }

    public static void drawBorderedCircle(double d, double d2, float f, int n, int n2) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        float f2 = 0.1f;
        GL11.glScalef((float)f2, (float)f2, (float)f2);
        d = (int)(d * (double)(1.0f / f2));
        d2 = (int)(d2 * (double)(1.0f / f2));
        Draw.drawCircle(d, d2, f *= 1.0f / f2, n2);
        Draw.drawUnfilledCircle(d, d2, f, 1.0f, n);
        GL11.glScalef((float)(1.0f / f2), (float)(1.0f / f2), (float)(1.0f / f2));
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawBorderedRectangle(double d, double d2, double d3, double d4, double d5, int n, int n2, boolean bl) {
        Draw.drawRectangle(d - (!bl ? d5 : 0.0), d2 - (!bl ? d5 : 0.0), d3 + (!bl ? d5 : 0.0), d4 + (!bl ? d5 : 0.0), n2);
        Draw.drawRectangle(d + (bl ? d5 : 0.0), d2 + (bl ? d5 : 0.0), d3 - (bl ? d5 : 0.0), d4 - (bl ? d5 : 0.0), n);
    }

    public static void drawGradientRect(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(d3, d2, 0.0).color(f2, f3, f4, f).endVertex();
        worldRenderer.pos(d, d2, 0.0).color(f2, f3, f4, f).endVertex();
        worldRenderer.pos(d, d4, 0.0).color(f6, f7, f8, f5).endVertex();
        worldRenderer.pos(d3, d4, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawImg(ResourceLocation resourceLocation, double d, double d2, double d3, double d4) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        float f = 1.0f / (float)d3;
        float f2 = 1.0f / (float)d4;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(d, d2 + d4, 0.0).tex(0.0f * f, (0.0f + (float)d4) * f2).endVertex();
        worldRenderer.pos(d + d3, d2 + d4, 0.0).tex((0.0f + (float)d3) * f, (0.0f + (float)d4) * f2).endVertex();
        worldRenderer.pos(d + d3, d2, 0.0).tex((0.0f + (float)d3) * f, 0.0f * f2).endVertex();
        worldRenderer.pos(d, d2, 0.0).tex(0.0f * f, 0.0f * f2).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
    }

    public static void drawRectangle(double d, double d2, double d3, double d4, int n) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f2, f3, f4, f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(d, d4, 0.0).endVertex();
        worldRenderer.pos(d3, d4, 0.0).endVertex();
        worldRenderer.pos(d3, d2, 0.0).endVertex();
        worldRenderer.pos(d, d2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void glColor(float f, int n, int n2, int n3) {
        float f2 = 0.003921569f * (float)n;
        float f3 = 0.003921569f * (float)n2;
        float f4 = 0.003921569f * (float)n3;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawUnfilledCircle(double d, double d2, float f, float f2, int n) {
        float f3 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f4, (float)f5, (float)f6, (float)f3);
        GL11.glLineWidth((float)f2);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        int n2 = 0;
        while (n2 <= 360) {
            GL11.glVertex2d((double)(d + Math.sin((double)n2 * 3.141526 / 180.0) * (double)f), (double)(d2 + Math.cos((double)n2 * 3.141526 / 180.0) * (double)f));
            ++n2;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void fixTextFlickering() {
        GL11.glPushMatrix();
        GL11.glColor3ub((byte)0, (byte)0, (byte)0);
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, new Color(255, 255, 255).getRGB());
        GL11.glPopMatrix();
    }
}

