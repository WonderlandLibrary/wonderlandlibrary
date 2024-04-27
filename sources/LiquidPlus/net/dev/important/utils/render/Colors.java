/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package net.dev.important.utils.render;

import java.awt.Color;
import java.text.NumberFormat;
import net.minecraft.entity.EntityLivingBase;

public enum Colors {
    BLACK(-16711423),
    BLUE(-12028161),
    DARKBLUE(-12621684),
    GREEN(-9830551),
    DARKGREEN(-9320847),
    WHITE(-65794),
    AQUA(-7820064),
    DARKAQUA(-12621684),
    GREY(-9868951),
    DARKGREY(-14342875),
    RED(-65536),
    DARKRED(-8388608),
    ORANGE(-29696),
    DARKORANGE(-2263808),
    YELLOW(-256),
    DARKYELLOW(-2702025),
    MAGENTA(-18751),
    DARKMAGENTA(-2252579);

    public int c;

    private Colors(int co) {
        this.c = co;
    }

    public static int getColor(Color color) {
        return Colors.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return Colors.getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha2) {
        return Colors.getColor(brightness, brightness, brightness, alpha2);
    }

    public static int getColor(int red2, int green2, int blue2) {
        return Colors.getColor(red2, green2, blue2, 255);
    }

    public static Color getHealthColor(EntityLivingBase entityLivingBase) {
        float health = entityLivingBase.func_110143_aJ();
        float[] fractions = new float[]{0.0f, 0.15f, 0.55f, 0.7f, 0.9f};
        Color[] colors = new Color[]{new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
        float progress = health / entityLivingBase.func_110138_aP();
        return health >= 0.0f ? Colors.blendColors(fractions, colors, progress).brighter() : colors[0];
    }

    public static int[] getFractionIndicies(float[] fractions, float progress) {
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

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indicies = Colors.getFractionIndicies(fractions, progress);
            float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
            Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = Colors.blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        return new Color(255, 255, 255);
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
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
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            exp.printStackTrace();
        }
        return color3;
    }

    public static int getColor(int red2, int green2, int blue2, int alpha2) {
        int color = 0;
        color |= alpha2 << 24;
        color |= red2 << 16;
        color |= green2 << 8;
        return color |= blue2;
    }
}

