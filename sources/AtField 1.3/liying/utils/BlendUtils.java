/*
 * Decompiled with CFR 0.152.
 */
package liying.utils;

import java.awt.Color;

public enum BlendUtils {
    GREEN("\u00a7A"),
    GOLD("\u00a76"),
    RED("\u00a7C");

    String colorCode;

    public static Color getHealthColor(float f, float f2) {
        float[] fArray = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colorArray = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float f3 = f / f2;
        return BlendUtils.blendColors(fArray, colorArray, f3).brighter();
    }

    public static Color blendColors(float[] fArray, Color[] colorArray, float f) {
        if (fArray.length == colorArray.length) {
            int[] nArray = BlendUtils.getFractionIndices(fArray, f);
            float[] fArray2 = new float[]{fArray[nArray[0]], fArray[nArray[1]]};
            Color[] colorArray2 = new Color[]{colorArray[nArray[0]], colorArray[nArray[1]]};
            float f2 = fArray2[1] - fArray2[0];
            float f3 = f - fArray2[0];
            float f4 = f3 / f2;
            Color color = BlendUtils.blend(colorArray2[0], colorArray2[1], 1.0f - f4);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static Color getColorWithOpacity(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static int[] getFractionIndices(float[] fArray, float f) {
        int n;
        int[] nArray = new int[2];
        for (n = 0; n < fArray.length && fArray[n] <= f; ++n) {
        }
        if (n >= fArray.length) {
            n = fArray.length - 1;
        }
        nArray[0] = n - 1;
        nArray[1] = n;
        return nArray;
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

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private BlendUtils() {
        void var3_1;
        this.colorCode = var3_1;
    }
}

