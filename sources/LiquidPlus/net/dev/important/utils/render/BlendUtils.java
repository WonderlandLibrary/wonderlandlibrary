/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.utils.render;

import java.awt.Color;

public enum BlendUtils {
    GREEN("\u00a7A"),
    GOLD("\u00a76"),
    RED("\u00a7C");

    String colorCode;

    private BlendUtils(String colorCode) {
        this.colorCode = colorCode;
    }

    public static Color getColorWithOpacity(Color color, int alpha2) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha2);
    }

    public static Color getHealthColor(float health, float maxHealth) {
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float progress = health / maxHealth;
        return BlendUtils.blendColors(fractions, colors, progress).brighter();
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indices = BlendUtils.getFractionIndices(fractions, progress);
            float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = BlendUtils.blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static int[] getFractionIndices(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = color1.getColorComponents(new float[3]);
        float[] rgb2 = color2.getColorComponents(new float[3]);
        float red2 = rgb1[0] * r + rgb2[0] * ir;
        float green2 = rgb1[1] * r + rgb2[1] * ir;
        float blue2 = rgb1[2] * r + rgb2[2] * ir;
        if (red2 < 0.0f) {
            red2 = 0.0f;
        } else if (red2 > 255.0f) {
            red2 = 255.0f;
        }
        if (green2 < 0.0f) {
            green2 = 0.0f;
        } else if (green2 > 255.0f) {
            green2 = 255.0f;
        }
        if (blue2 < 0.0f) {
            blue2 = 0.0f;
        } else if (blue2 > 255.0f) {
            blue2 = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red2, green2, blue2);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        return color3;
    }
}

