/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.novoline;

import java.awt.Color;
import java.text.NumberFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ScaleUtils {
    static Color startColor = new Color(-393028);
    static Color endColor = new Color(-16718593);

    public static double[] getScaledMouseCoordinates(Minecraft minecraft, double d, double d2) {
        double d3 = d;
        double d4 = d2;
        switch (minecraft.field_71474_y.field_74335_Z) {
            case 0: {
                d3 *= 2.0;
                d4 *= 2.0;
                break;
            }
            case 1: {
                d3 *= 0.5;
                d4 *= 0.5;
                break;
            }
            case 3: {
                d3 *= 1.5;
                d4 *= 1.5;
            }
        }
        return new double[]{d3, d4};
    }

    public static void disableRender2D() {
        GL11.glPopMatrix();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }

    public static Color blend(Color color, Color color2, double d) {
        float f = (float)d;
        float f2 = 1.0f - f;
        float[] fArray = new float[3];
        float[] fArray2 = new float[3];
        color.getColorComponents(fArray);
        color2.getColorComponents(fArray2);
        float f3 = fArray[0] * f + fArray2[0] * f2;
        float f4 = fArray[1] * f + fArray2[1] * f2;
        float f5 = fArray[2] * f + fArray2[2] * f2;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        } else if (f3 > 255.0f) {
            f3 = 255.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        } else if (f4 > 255.0f) {
            f4 = 255.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        } else if (f5 > 255.0f) {
            f5 = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(f3, f4, f5);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            illegalArgumentException.printStackTrace();
        }
        return color3;
    }

    public static void scale(Minecraft minecraft) {
        switch (minecraft.field_71474_y.field_74335_Z) {
            case 0: {
                GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
                break;
            }
            case 1: {
                GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
                break;
            }
            case 3: {
                GlStateManager.func_179139_a((double)0.6666666666666667, (double)0.6666666666666667, (double)0.6666666666666667);
            }
        }
    }

    public static int fadeTo(int n, int n2, double d) {
        double d2 = 1.0 - d;
        int n3 = (int)((double)(n >> 16 & 0xFF) * d2 + (double)(n2 >> 16 & 0xFF) * d);
        int n4 = (int)((double)(n >> 8 & 0xFF) * d2 + (double)(n2 >> 8 & 0xFF) * d);
        int n5 = (int)((double)(n & 0xFF) * d2 + (double)(n2 & 0xFF) * d);
        int n6 = (int)((double)(n >> 24 & 0xFF) * d2 + (double)(n2 >> 24 & 0xFF) * d);
        return (n6 & 0xFF) << 24 | (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
    }

    public static void enableRender2D() {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glPushMatrix();
    }

    public static int fadeBetween(int n, int n2) {
        return ScaleUtils.fadeBetween(n, n2, 0L);
    }

    public static int[] getScaledMouseCoordinates(Minecraft minecraft, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        switch (minecraft.field_71474_y.field_74335_Z) {
            case 0: {
                n3 <<= 1;
                n4 <<= 1;
                break;
            }
            case 1: {
                n3 = (int)((double)n3 * 0.5);
                n4 = (int)((double)n4 * 0.5);
                break;
            }
            case 3: {
                n3 = (int)((double)n3 * 1.5);
                n4 = (int)((double)n4 * 1.5);
            }
        }
        return new int[]{n3, n4};
    }

    public static int fadeBetween(int n, int n2, long l) {
        return ScaleUtils.fadeBetween(n, n2, (double)((System.currentTimeMillis() + l) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int n, int n2, double d) {
        if (d > 1.0) {
            d = 1.0 - d % 1.0;
        }
        return ScaleUtils.fadeTo(n, n2, d);
    }

    public static void setColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
    }
}

