/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.util.regex.Pattern;
import net.ccbluex.liquidbounce.utils.render.ApacheMath;
import org.lwjgl.opengl.GL11;

public class ColorManager {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-ORX]");

    public static Color getColorAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static int getColor(int n, int n2, int n3, int n4) {
        return (n & 0xFF) << 24 | (n2 & 0xFF) << 16 | (n3 & 0xFF) << 8 | n4 & 0xFF;
    }

    public static void glColor(Color color) {
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        float f4 = (float)color.getAlpha() / 255.0f;
        GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
    }

    public static int astolfoRainbow(int n, int n2, int n3) {
        double d;
        double d2 = Math.ceil(System.currentTimeMillis() + (long)n * (long)n3) / (double)n2;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(d2 / 360.0)) : (float)((d2 %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static String stripColorCodes(String string) {
        return STRIP_COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public static Color getColorSwitch(Color color, Color color2, float f, int n, long l, double d) {
        long l2 = (long)(d * (double)System.currentTimeMillis() + (double)((long)(-n) * l));
        float f2 = (float)(color.getRed() - color2.getRed()) / f;
        float f3 = (float)(color.getGreen() - color2.getGreen()) / f;
        float f4 = (float)(color.getBlue() - color2.getBlue()) / f;
        float f5 = (float)(color2.getRed() - color.getRed()) / f;
        float f6 = (float)(color2.getGreen() - color.getGreen()) / f;
        float f7 = (float)(color2.getBlue() - color.getBlue()) / f;
        int n2 = ApacheMath.round((float)color2.getRed() + f2 * (float)(l2 % (long)f));
        int n3 = ApacheMath.round((float)color2.getGreen() + f3 * (float)(l2 % (long)f));
        int n4 = ApacheMath.round((float)color2.getBlue() + f4 * (float)(l2 % (long)f));
        int n5 = ApacheMath.round((float)color.getRed() + f5 * (float)(l2 % (long)f));
        int n6 = ApacheMath.round((float)color.getGreen() + f6 * (float)(l2 % (long)f));
        int n7 = ApacheMath.round((float)color.getBlue() + f7 * (float)(l2 % (long)f));
        if (l2 % ((long)f * 2L) < (long)f) {
            return new Color(ColorManager.getColor(255, n5, n6, n7));
        }
        return new Color(ColorManager.getColor(255, n2, n3, n4));
    }

    public static Color rainbow(int n, float f, double d) {
        double d2 = ApacheMath.ceil(((double)System.currentTimeMillis() * d - (double)n) / 20.0);
        return Color.getHSBColor((float)((d2 %= 360.0) / 360.0), f, 1.0f);
    }
}

