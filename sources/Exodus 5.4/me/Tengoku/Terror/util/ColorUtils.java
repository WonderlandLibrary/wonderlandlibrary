/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.awt.Color;

public enum ColorUtils {
    GREEN("\ufffdA"),
    GOLD("\ufffd6"),
    RED("\ufffdC");

    String colorCode;

    public static int getRainbow(float f, float f2, float f3) {
        float f4 = (float)(System.currentTimeMillis() % (long)((int)(f * 1000.0f))) / (f * 1000.0f);
        int n = Color.HSBtoRGB(f4, f2, f3);
        return n;
    }

    public static int astolfo3(int n) {
        int n2 = (int)((System.currentTimeMillis() / 11L + (long)n) % 360L);
        n2 = (n2 > 180 ? 360 - n2 : n2) + 180;
        return Color.HSBtoRGB((float)n2 / 360.0f, 0.55f, 1.0f);
    }

    public static int[] getFractionIndices(float[] fArray, float f) {
        int[] nArray = new int[2];
        int n = 0;
        while (n < fArray.length && fArray[n] <= f) {
            ++n;
        }
        if (n >= fArray.length) {
            n = fArray.length - 1;
        }
        nArray[0] = n - 1;
        nArray[1] = n;
        return nArray;
    }

    public static int getRainbow(float f, float f2, float f3, float f4, long l) {
        float f5 = (float)((System.currentTimeMillis() - l) % (long)((int)(f2 * 1000.0f))) / (f2 * 1000.0f);
        int n = Color.HSBtoRGB(f5, f3, f4);
        return n;
    }

    public static int getRainbowWave(float f, float f2, float f3, long l) {
        float f4 = (float)((System.currentTimeMillis() + l) % (long)((int)(f * 500.0f))) / (f * 500.0f);
        int n = Color.HSBtoRGB(f4, f2, f3);
        return n;
    }

    public static Color astolfo(float f, float f2, float f3, int n) {
        double d;
        double d2 = Math.ceil(System.currentTimeMillis() - (long)n) / (double)f;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(d2 / 360.0)) : (float)((d2 %= 360.0) / 360.0), f2, f3);
    }

    public static Color getHealthColor(float f, float f2) {
        float[] fArray = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colorArray = new Color[]{new Color(108, 20, 20), new Color(255, 0, 60), Color.GREEN};
        float f3 = f / f2;
        return ColorUtils.blendColors(fArray, colorArray, f3).brighter();
    }

    public static Color getColorWithOpacity(Color color, Color color2, float f, int n, float f2, long l) {
        double d = (float)((System.currentTimeMillis() - l) % (long)((int)(f2 * 500.0f))) / (f2 * 500.0f);
        double d2 = 1.0 - d;
        int n2 = (int)((double)color.getRed() * d2 + (double)color2.getRed() * d2 * d);
        int n3 = (int)((double)color.getGreen() * d2 + (double)color2.getGreen() * d2 * d);
        int n4 = (int)((double)color.getBlue() * d2 + (double)color2.getBlue() * d2 * d);
        Color color3 = Color.getHSBColor((float)d, f, n);
        return new Color(n2, n3, n4, n);
    }

    public static Color blend(Color color, Color color2, double d) {
        float f = (float)d;
        float f2 = 1.0f - f;
        float[] fArray = color.getColorComponents(new float[3]);
        float[] fArray2 = color2.getColorComponents(new float[3]);
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
            // empty catch block
        }
        return color3;
    }

    public static Color getMixedColor(Color color, Color color2, int n, int n2) {
        return ColorUtils.blendColors(new float[]{0.0f, 0.5f, 1.0f}, new Color[]{color, color2, color}, (float)((System.currentTimeMillis() + (long)n) % (long)(n2 * 1000)) / (float)(n2 * 1000));
    }

    public static Color blendColors(float[] fArray, Color[] colorArray, float f) {
        if (fArray.length == colorArray.length) {
            int[] nArray = ColorUtils.getFractionIndices(fArray, f);
            float[] fArray2 = new float[]{fArray[nArray[0]], fArray[nArray[1]]};
            Color[] colorArray2 = new Color[]{colorArray[nArray[0]], colorArray[nArray[1]]};
            float f2 = fArray2[1] - fArray2[0];
            float f3 = f - fArray2[0];
            float f4 = f3 / f2;
            Color color = ColorUtils.blend(colorArray2[0], colorArray2[1], 1.0f - f4);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    private ColorUtils(String string2) {
        this.colorCode = string2;
    }
}

