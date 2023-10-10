/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.utils.MathUtil;
import java.awt.Color;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class ColorUtil {
    public static Color generateColor(String seed) {
        int i = 0;
        int index = 1;
        byte[] byArray = seed.getBytes();
        int n = byArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                Random random = new Random(i);
                int r = random.nextInt(200) + 55;
                int g = random.nextInt(200) + 55;
                int b = random.nextInt(200) + 55;
                return new Color(r, g, b);
            }
            Byte c = byArray[n2];
            i += c * ++index;
            ++n2;
        }
    }

    public static float getHue(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
    }

    public static float getSaturation(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[1];
    }

    public static float getBrightness(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[2];
    }

    public static Color lerpColor(Color from, Color to, float blending) {
        blending = MathUtil.clamp(blending, 0.0f, 1.0f);
        float inverse_blending = 1.0f - blending;
        float red = (float)to.getRed() * blending + (float)from.getRed() * inverse_blending;
        float green = (float)to.getGreen() * blending + (float)from.getGreen() * inverse_blending;
        float blue = (float)to.getBlue() * blending + (float)from.getBlue() * inverse_blending;
        float alpha = (float)to.getAlpha() * blending + (float)from.getAlpha() * inverse_blending;
        return new Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
    }

    public static Color fromHSB(float hue, float saturation, float brightness) {
        return new Color(Color.getHSBColor(hue, saturation, brightness).getRGB());
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static Color mutiplyAlpha(Color c, float mul) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)((float)c.getAlpha() * mul));
    }

    public static Color getColorBasedOnHealthPercent(float percent) {
        return ColorUtil.lerpColor(new Color(236, 6, 6, 255), new Color(11, 245, 3, 255), percent);
    }

    public static Color getDimColor(int dim) {
        return (new Color[]{new Color(190, 100, 51, 255), new Color(70, 161, 67, 255), new Color(215, 200, 153, 255)})[dim + 1];
    }
}

