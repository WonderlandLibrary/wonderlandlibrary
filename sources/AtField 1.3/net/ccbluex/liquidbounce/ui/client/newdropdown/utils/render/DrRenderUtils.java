/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WDefaultVertexFormats;
import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Utils;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.GLUtil;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class DrRenderUtils
implements Utils {
    public static float zLevel;

    public static void drawClickGuiArrow(float f, float f2, float f3, Animation animation, int n) {
        GL11.glTranslatef((float)f, (float)f2, (float)0.0f);
        GLUtil.setup2DRendering(() -> DrRenderUtils.lambda$drawClickGuiArrow$1(n, f3, animation));
        GL11.glTranslatef((float)(-f), (float)(-f2), (float)0.0f);
    }

    private static void lambda$fakeCircleGlow$5(Color color, float f, float f2, float f3, float f4) {
        GLUtil.render(6, () -> DrRenderUtils.lambda$null$4(color, f, f2, f3, f4));
    }

    public static void drawRect2(double d, double d2, double d3, double d4, int n) {
        DrRenderUtils.resetColor();
        GLUtil.setup2DRendering(() -> DrRenderUtils.lambda$drawRect2$7(n, d, d2, d4, d3));
    }

    public static void drawModalRectWithCustomSizedTexture(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 1.0f / f7;
        float f10 = 1.0f / f8;
        Tessellator tessellator = Tessellator.func_178181_a();
        IWorldRenderer iWorldRenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        iWorldRenderer.begin(7, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_COLOR));
        iWorldRenderer.pos(f, f2 + f6, 0.0).tex(f3 * f9, (f4 + f6) * f10).endVertex();
        iWorldRenderer.pos(f + f5, f2 + f6, 0.0).tex((f3 + f5) * f9, (f4 + f6) * f10).endVertex();
        iWorldRenderer.pos(f + f5, f2, 0.0).tex((f3 + f5) * f9, f4 * f10).endVertex();
        iWorldRenderer.pos(f, f2, 0.0).tex(f3 * f9, f4 * f10).endVertex();
        tessellator.func_78381_a();
    }

    public static int interpolateColor(int n, int n2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        Color color = new Color(n);
        Color color2 = new Color(n2);
        return DrRenderUtils.interpolateColorC(color, color2, f).getRGB();
    }

    public static int applyOpacity(int n, float f) {
        Color color = new Color(n);
        return DrRenderUtils.applyOpacity(color, f).getRGB();
    }

    public static void renderRoundedRect(float f, float f2, float f3, float f4, float f5, int n) {
        DrRenderUtils.drawGoodCircle(f + f5, f2 + f5, f5, n);
        DrRenderUtils.drawGoodCircle(f + f3 - f5, f2 + f5, f5, n);
        DrRenderUtils.drawGoodCircle(f + f5, f2 + f4 - f5, f5, n);
        DrRenderUtils.drawGoodCircle(f + f3 - f5, f2 + f4 - f5, f5, n);
        DrRenderUtils.drawRect2(f + f5, f2, f3 - f5 * 2.0f, f4, n);
        DrRenderUtils.drawRect2(f, f2 + f5, f3, f4 - f5 * 2.0f, n);
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
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        IWorldRenderer iWorldRenderer = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance().getWorldRenderer();
        iWorldRenderer.begin(7, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_COLOR));
        iWorldRenderer.pos(d3, d2, zLevel).color(f2, f3, f4, f).endVertex();
        iWorldRenderer.pos(d, d2, zLevel).color(f2, f3, f4, f).endVertex();
        iWorldRenderer.pos(d, d4, zLevel).color(f6, f7, f8, f5).endVertex();
        iWorldRenderer.pos(d3, d4, zLevel).color(f6, f7, f8, f5).endVertex();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void setAlphaLimit(float f) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)((float)((double)f * 0.01)));
    }

    private static void lambda$drawRect2$7(int n, double d, double d2, double d3, double d4) {
        GLUtil.render(7, () -> DrRenderUtils.lambda$null$6(n, d, d2, d3, d4));
    }

    public static Color brighter(Color color, float f) {
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        int n4 = color.getAlpha();
        int n5 = (int)(1.0 / (1.0 - (double)f));
        if (n == 0 && n2 == 0 && n3 == 0) {
            return new Color(n5, n5, n5, n4);
        }
        if (n > 0 && n < n5) {
            n = n5;
        }
        if (n2 > 0 && n2 < n5) {
            n2 = n5;
        }
        if (n3 > 0 && n3 < n5) {
            n3 = n5;
        }
        return new Color(Math.min((int)((float)n / f), 255), Math.min((int)((float)n2 / f), 255), Math.min((int)((float)n3 / f), 255), n4);
    }

    public static void resetColor() {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static void lambda$null$0(int n, float f, Animation animation) {
        DrRenderUtils.color(n);
        double d = DrRenderUtils.interpolate(0.0, (double)f / 2.0, animation.getOutput());
        if (animation.getOutput() >= 0.48) {
            GL11.glVertex2d((double)(f / 2.0f), (double)DrRenderUtils.interpolate((double)f / 2.0, 0.0, animation.getOutput()));
        }
        GL11.glVertex2d((double)0.0, (double)d);
        if (animation.getOutput() < 0.48) {
            GL11.glVertex2d((double)(f / 2.0f), (double)DrRenderUtils.interpolate((double)f / 2.0, 0.0, animation.getOutput()));
        }
        GL11.glVertex2d((double)f, (double)d);
    }

    public static Color darker(Color color, float f) {
        return new Color(Math.max((int)((float)color.getRed() * f), 0), Math.max((int)((float)color.getGreen() * f), 0), Math.max((int)((float)color.getBlue() * f), 0), color.getAlpha());
    }

    public static void drawGradientRectSideways(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        ITessellator iTessellator = LiquidBounce.INSTANCE.getWrapper().getClassProvider().getTessellatorInstance();
        IWorldRenderer iWorldRenderer = iTessellator.getWorldRenderer();
        iWorldRenderer.begin(7, MinecraftInstance.classProvider.getVertexFormatEnum(WDefaultVertexFormats.POSITION_COLOR));
        iWorldRenderer.pos(d3, d2, zLevel).color(f6, f7, f8, f5).endVertex();
        iWorldRenderer.pos(d, d2, zLevel).color(f2, f3, f4, f).endVertex();
        iWorldRenderer.pos(d, d4, zLevel).color(f2, f3, f4, f).endVertex();
        iWorldRenderer.pos(d3, d4, zLevel).color(f6, f7, f8, f5).endVertex();
        iTessellator.draw();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void scale(float f, float f2, float f3, Runnable runnable) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)f, (float)f2, (float)0.0f);
        GL11.glScalef((float)f3, (float)f3, (float)1.0f);
        GL11.glTranslatef((float)(-f), (float)(-f2), (float)0.0f);
        runnable.run();
        GL11.glPopMatrix();
    }

    private static void lambda$drawClickGuiArrow$1(int n, float f, Animation animation) {
        GLUtil.render(5, () -> DrRenderUtils.lambda$null$0(n, f, animation));
    }

    public static Double interpolate(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public static void fakeCircleGlow(float f, float f2, float f3, Color color, float f4) {
        DrRenderUtils.setAlphaLimit(0.0f);
        GL11.glShadeModel((int)7425);
        GLUtil.setup2DRendering(() -> DrRenderUtils.lambda$fakeCircleGlow$5(color, f4, f, f2, f3));
        GL11.glShadeModel((int)7424);
        DrRenderUtils.setAlphaLimit(1.0f);
    }

    public static void color(int n) {
        DrRenderUtils.color(n, (float)(n >> 24 & 0xFF) / 255.0f);
    }

    public static boolean isHovering(float f, float f2, float f3, float f4, int n, int n2) {
        return (float)n >= f && (float)n2 >= f2 && (float)n < f + f3 && (float)n2 < f2 + f4;
    }

    public static void scissor(double d, double d2, double d3, double d4) {
        IScaledResolution iScaledResolution = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createScaledResolution(mc);
        double d5 = iScaledResolution.getScaleFactor();
        double d6 = d4 * d5;
        double d7 = ((double)iScaledResolution.getScaledHeight() - d2) * d5;
        double d8 = d * d5;
        double d9 = d3 * d5;
        GL11.glScissor((int)((int)d8), (int)((int)(d7 - d6)), (int)((int)d9), (int)((int)d6));
    }

    private static void lambda$null$2(double d, double d2) {
        GL11.glVertex2d((double)d, (double)d2);
    }

    private static void lambda$null$4(Color color, float f, float f2, float f3, float f4) {
        DrRenderUtils.color(color.getRGB(), f);
        GL11.glVertex2d((double)f2, (double)f3);
        DrRenderUtils.color(color.getRGB(), 0.0f);
        for (int i = 0; i <= 100; ++i) {
            double d = (double)i * 0.06283 + 3.1415;
            double d2 = Math.sin(d) * (double)f4;
            double d3 = Math.cos(d) * (double)f4;
            GL11.glVertex2d((double)((double)f2 + d2), (double)((double)f3 + d3));
        }
    }

    public static double animate(double d, double d2, double d3) {
        boolean bl;
        boolean bl2 = bl = d > d2;
        if (d3 < 0.0) {
            d3 = 0.0;
        } else if (d3 > 1.0) {
            d3 = 1.0;
        }
        double d4 = Math.max(d, d2) - Math.min(d, d2);
        double d5 = d4 * d3;
        return d2 + (bl ? d5 : -d5);
    }

    public static int interpolateInt(int n, int n2, double d) {
        return DrRenderUtils.interpolate(n, n2, (float)d).intValue();
    }

    public static Color applyOpacity(Color color, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * f));
    }

    public static void drawGoodCircle(double d, double d2, float f, int n) {
        DrRenderUtils.color(n);
        GLUtil.setup2DRendering(() -> DrRenderUtils.lambda$drawGoodCircle$3(f, d, d2));
    }

    public static Color interpolateColorC(Color color, Color color2, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return new Color(DrRenderUtils.interpolateInt(color.getRed(), color2.getRed(), f), DrRenderUtils.interpolateInt(color.getGreen(), color2.getGreen(), f), DrRenderUtils.interpolateInt(color.getBlue(), color2.getBlue(), f), DrRenderUtils.interpolateInt(color.getAlpha(), color2.getAlpha(), f));
    }

    public static void drawGradientRectSideways2(double d, double d2, double d3, double d4, int n, int n2) {
        DrRenderUtils.drawGradientRectSideways(d, d2, d + d3, d2 + d4, n, n2);
    }

    public static void color(int n, float f) {
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
    }

    public static void drawGradientRect2(double d, double d2, double d3, double d4, int n, int n2) {
        DrRenderUtils.drawGradientRect(d, d2, d + d3, d2 + d4, n, n2);
    }

    private static void lambda$drawGoodCircle$3(float f, double d, double d2) {
        GL11.glEnable((int)2832);
        GL11.glHint((int)3153, (int)4354);
        GL11.glPointSize((float)(f * (float)(2 * mc.getGameSettings().getGuiScale())));
        GLUtil.render(0, () -> DrRenderUtils.lambda$null$2(d, d2));
    }

    private static void lambda$null$6(int n, double d, double d2, double d3, double d4) {
        DrRenderUtils.color(n);
        GL11.glVertex2d((double)d, (double)d2);
        GL11.glVertex2d((double)d, (double)(d2 + d3));
        GL11.glVertex2d((double)(d + d4), (double)(d2 + d3));
        GL11.glVertex2d((double)(d + d4), (double)d2);
    }
}

