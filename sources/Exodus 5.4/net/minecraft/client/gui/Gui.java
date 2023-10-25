/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.gui;

import java.awt.Color;
import javax.vecmath.Vector3d;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Gui {
    public static final ResourceLocation icons;
    public static final ResourceLocation statIcons;
    public static final ResourceLocation optionsBackground;
    protected static float zLevel;

    public static void drawScaledCustomSizeModalRect(int n, int n2, float f, float f2, int n3, int n4, int n5, int n6, float f3, float f4) {
        float f5 = 1.0f / f3;
        float f6 = 1.0f / f4;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(n, n2 + n6, 0.0).tex(f * f5, (f2 + (float)n4) * f6).endVertex();
        worldRenderer.pos(n + n5, n2 + n6, 0.0).tex((f + (float)n3) * f5, (f2 + (float)n4) * f6).endVertex();
        worldRenderer.pos(n + n5, n2, 0.0).tex((f + (float)n3) * f5, f2 * f6).endVertex();
        worldRenderer.pos(n, n2, 0.0).tex(f * f5, f2 * f6).endVertex();
        tessellator.draw();
    }

    public static void renderRoundedQuad(Vector3d vector3d, Vector3d vector3d2, int n, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glBegin((int)9);
        int n2 = -90;
        double[][] dArrayArray = new double[][]{{vector3d2.x, vector3d2.y}, {vector3d2.x, vector3d.y}, {vector3d.x, vector3d.y}, {vector3d.x, vector3d2.y}};
        int n3 = 0;
        while (n3 < 4) {
            double[] dArray = dArrayArray[n3];
            int n4 = n2 += 90;
            while (n4 < 90 + n2) {
                double d = Math.toRadians(n4);
                double d2 = Math.sin(d) * (double)n;
                double d3 = Math.cos(d) * (double)n;
                GL11.glVertex2d((double)(dArray[0] + d2), (double)(dArray[1] + d3));
                ++n4;
            }
            ++n3;
        }
        GL11.glEnd();
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public void drawTexturedModalRect(float f, float f2, int n, int n2, int n3, int n4) {
        float f3 = 0.00390625f;
        float f4 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(f + 0.0f, f2 + (float)n4, zLevel).tex((float)(n + 0) * f3, (float)(n2 + n4) * f4).endVertex();
        worldRenderer.pos(f + (float)n3, f2 + (float)n4, zLevel).tex((float)(n + n3) * f3, (float)(n2 + n4) * f4).endVertex();
        worldRenderer.pos(f + (float)n3, f2 + 0.0f, zLevel).tex((float)(n + n3) * f3, (float)(n2 + 0) * f4).endVertex();
        worldRenderer.pos(f + 0.0f, f2 + 0.0f, zLevel).tex((float)(n + 0) * f3, (float)(n2 + 0) * f4).endVertex();
        tessellator.draw();
    }

    protected void drawVerticalLine(int n, int n2, int n3, int n4) {
        if (n3 < n2) {
            int n5 = n2;
            n2 = n3;
            n3 = n5;
        }
        Gui.drawRect(n, n2 + 1, n + 1, n3, n4);
    }

    public void drawCenteredString(FontRenderer fontRenderer, String string, int n, int n2, int n3) {
        fontRenderer.drawStringWithShadow(string, n - fontRenderer.getStringWidth(string) / 2, n2, n3);
    }

    public void drawTexturedModalRect(int n, int n2, int n3, int n4, int n5, int n6) {
        float f = 0.00390625f;
        float f2 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(n + 0, n2 + n6, zLevel).tex((float)(n3 + 0) * f, (float)(n4 + n6) * f2).endVertex();
        worldRenderer.pos(n + n5, n2 + n6, zLevel).tex((float)(n3 + n5) * f, (float)(n4 + n6) * f2).endVertex();
        worldRenderer.pos(n + n5, n2 + 0, zLevel).tex((float)(n3 + n5) * f, (float)(n4 + 0) * f2).endVertex();
        worldRenderer.pos(n + 0, n2 + 0, zLevel).tex((float)(n3 + 0) * f, (float)(n4 + 0) * f2).endVertex();
        tessellator.draw();
    }

    public static void drawGradientRect(double d, double d2, float f, float f2, int n, int n2) {
        float f3 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f8 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f9 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f10 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(f, d2, zLevel).color(f4, f5, f6, f3).endVertex();
        worldRenderer.pos(d, d2, zLevel).color(f4, f5, f6, f3).endVertex();
        worldRenderer.pos(d, f2, zLevel).color(f8, f9, f10, f7).endVertex();
        worldRenderer.pos(f, f2, zLevel).color(f8, f9, f10, f7).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawHorizontalLine(int n, int n2, int n3, int n4) {
        if (n2 < n) {
            int n5 = n;
            n = n2;
            n2 = n5;
        }
        Gui.drawRect(n, n3, n2 + 1, n3 + 1, n4);
    }

    public static void drawModalRectWithCustomSizedTexture(float f, float f2, float f3, float f4, int n, int n2, float f5, float f6) {
        float f7 = 1.0f / f5;
        float f8 = 1.0f / f6;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(f, f2 + (float)n2, 0.0).tex(f3 * f7, (f4 + (float)n2) * f8).endVertex();
        worldRenderer.pos(f + (float)n, f2 + (float)n2, 0.0).tex((f3 + (float)n) * f7, (f4 + (float)n2) * f8).endVertex();
        worldRenderer.pos(f + (float)n, f2, 0.0).tex((f3 + (float)n) * f7, f4 * f8).endVertex();
        worldRenderer.pos(f, f2, 0.0).tex(f3 * f7, f4 * f8).endVertex();
        tessellator.draw();
    }

    public void drawTexturedModalRect(int n, int n2, TextureAtlasSprite textureAtlasSprite, int n3, int n4) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(n + 0, n2 + n4, zLevel).tex(textureAtlasSprite.getMinU(), textureAtlasSprite.getMaxV()).endVertex();
        worldRenderer.pos(n + n3, n2 + n4, zLevel).tex(textureAtlasSprite.getMaxU(), textureAtlasSprite.getMaxV()).endVertex();
        worldRenderer.pos(n + n3, n2 + 0, zLevel).tex(textureAtlasSprite.getMaxU(), textureAtlasSprite.getMinV()).endVertex();
        worldRenderer.pos(n + 0, n2 + 0, zLevel).tex(textureAtlasSprite.getMinU(), textureAtlasSprite.getMinV()).endVertex();
        tessellator.draw();
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
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

    public void drawString(FontRenderer fontRenderer, String string, int n, int n2, int n3) {
        fontRenderer.drawStringWithShadow(string, n, n2, n3);
    }

    static {
        optionsBackground = new ResourceLocation("textures/gui/options_background.png");
        statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
        icons = new ResourceLocation("textures/gui/icons.png");
    }
}

