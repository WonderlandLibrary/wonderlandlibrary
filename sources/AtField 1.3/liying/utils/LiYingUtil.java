/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package liying.utils;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Random;
import liying.ui.Stencil;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.GLUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class LiYingUtil {
    private static final Random random = new Random();

    public static int astolfoRainbow(int n, int n2, int n3) {
        double d;
        double d2 = Math.ceil(System.currentTimeMillis() + (long)(n * n3)) / (double)n2;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(d2 / 360.0)) : (float)((d2 %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void drawGoodCircle(double d, double d2, float f, int n) {
        LiYingUtil.color(n);
        GLUtil.setup2DRendering(() -> LiYingUtil.lambda$drawGoodCircle$1(f, d, d2));
    }

    public static int clamp_int(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        return n > n3 ? n3 : n;
    }

    private static void lambda$null$0(double d, double d2) {
        GL11.glVertex2d((double)d, (double)d2);
    }

    private static void lambda$null$2(Color color, float f, float f2, float f3, float f4) {
        LiYingUtil.color(color.getRGB(), f);
        GL11.glVertex2d((double)f2, (double)f3);
        LiYingUtil.color(color.getRGB(), 0.0f);
        for (int i = 0; i <= 100; ++i) {
            double d = (double)i * 0.06283 + 3.1415;
            double d2 = Math.sin(d) * (double)f4;
            double d3 = Math.cos(d) * (double)f4;
            GL11.glVertex2d((double)((double)f2 + d2), (double)((double)f3 + d3));
        }
    }

    public static void color(int n) {
        LiYingUtil.color(n, (float)(n >> 24 & 0xFF) / 255.0f);
    }

    public static void fakeCircleGlow(float f, float f2, float f3, Color color, float f4) {
        LiYingUtil.setAlphaLimit(0.0f);
        GL11.glShadeModel((int)7425);
        GLUtil.setup2DRendering(() -> LiYingUtil.lambda$fakeCircleGlow$3(color, f4, f, f2, f3));
        GL11.glShadeModel((int)7424);
        LiYingUtil.setAlphaLimit(1.0f);
    }

    public static Double interpolate(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public static double round(double d, double d2) {
        if (d2 == 0.0) {
            return d;
        }
        if (d2 == 1.0) {
            return Math.round(d);
        }
        double d3 = d2 / 2.0;
        double d4 = Math.floor(d / d2) * d2;
        if (d >= d4 + d3) {
            return new BigDecimal(Math.ceil(d / d2) * d2).doubleValue();
        }
        return new BigDecimal(d4).doubleValue();
    }

    public static int getRandomInRange(int n, int n2) {
        return (int)(Math.random() * (double)(n2 - n) + (double)n);
    }

    private static void lambda$fakeCircleGlow$3(Color color, float f, float f2, float f3, float f4) {
        GLUtil.render(6, () -> LiYingUtil.lambda$null$2(color, f, f2, f3, f4));
    }

    public static Color applyOpacity(Color color, float f) {
        f = Math.min(1.0f, Math.max(0.0f, f));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * f));
    }

    public static void setAlphaLimit(float f) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a((int)516, (float)((float)((double)f * 0.01)));
    }

    public static void color(int n, float f) {
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.func_179131_c((float)f2, (float)f3, (float)f4, (float)f);
    }

    private static void lambda$drawGoodCircle$1(float f, double d, double d2) {
        GL11.glEnable((int)2832);
        GL11.glHint((int)3153, (int)4354);
        GL11.glPointSize((float)(f * (float)(2 * Stencil.mc.field_71474_y.field_74335_Z)));
        GLUtil.render(0, () -> LiYingUtil.lambda$null$0(d, d2));
    }

    public static float interpolateFloat(float f, float f2, double d) {
        return LiYingUtil.interpolate(f, f2, (float)d).floatValue();
    }

    public static int applyOpacity(int n, float f) {
        Color color = new Color(n);
        return LiYingUtil.applyOpacity(color, f).getRGB();
    }

    public static float calculateGaussianValue(float f, float f2) {
        double d = 3.141592653;
        double d2 = 1.0 / Math.sqrt(2.0 * d * (double)(f2 * f2));
        return (float)(d2 * Math.exp((double)(-(f * f)) / (2.0 * (double)(f2 * f2))));
    }

    public static double roundToHalf(double d) {
        return (double)Math.round(d * 2.0) / 2.0;
    }

    public static int interpolateInt(int n, int n2, double d) {
        return LiYingUtil.interpolate(n, n2, (float)d).intValue();
    }
}

