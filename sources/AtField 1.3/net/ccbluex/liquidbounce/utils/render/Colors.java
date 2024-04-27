/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.text.NumberFormat;
import net.minecraft.entity.EntityLivingBase;

public class Colors {
    public static Color blendColors(float[] fArray, Color[] colorArray, float f) {
        if (fArray == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colorArray == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fArray.length != colorArray.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        int[] nArray = Colors.getFractionIndicies(fArray, f);
        float[] fArray2 = new float[]{fArray[nArray[0]], fArray[nArray[1]]};
        Color[] colorArray2 = new Color[]{colorArray[nArray[0]], colorArray[nArray[1]]};
        float f2 = fArray2[1] - fArray2[0];
        float f3 = f - fArray2[0];
        float f4 = f3 / f2;
        return Colors.blend(colorArray2[0], colorArray2[1], 1.0f - f4);
    }

    public static int getColor(int n) {
        return Colors.getColor(n, n, n, 255);
    }

    public static int getRainbow2(int n, int n2) {
        float f = (System.currentTimeMillis() + (long)n2) % (long)n;
        return Color.getHSBColor(f /= (float)n, 0.5f, 0.555f).getRGB();
    }

    public static int getColor(Color color) {
        return Colors.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int n, int n2) {
        return Colors.getColor(n, n, n, n2);
    }

    public static int getColor(int n, int n2, int n3) {
        return Colors.getColor(n, n2, n3, 255);
    }

    public static int[] getFractionIndicies(float[] fArray, float f) {
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

    public static int getColor(int n, int n2, int n3, int n4) {
        int n5 = 0;
        n5 |= n4 << 24;
        n5 |= n << 16;
        n5 |= n2 << 8;
        return n5 |= n3;
    }

    public static Color getHealthColor(EntityLivingBase entityLivingBase) {
        float f = entityLivingBase.func_110143_aJ();
        float[] fArray = new float[]{0.0f, 0.15f, 0.55f, 0.7f, 0.9f};
        Color[] colorArray = new Color[]{new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
        float f2 = f / entityLivingBase.func_110138_aP();
        return f >= 0.0f ? Colors.blendColors(fArray, colorArray, f2).brighter() : colorArray[0];
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
            System.out.println(numberFormat.format(f3) + "; " + numberFormat.format(f4) + "; " + numberFormat.format(f5));
            illegalArgumentException.printStackTrace();
        }
        return color3;
    }

    public static int getRainbow(int n, int n2) {
        float f = (System.currentTimeMillis() + (long)n2) % (long)n;
        return Color.getHSBColor(f /= (float)n, 0.4f, 1.0f).getRGB();
    }

    public static int getRainbow3(int n, int n2) {
        float f = (System.currentTimeMillis() + (long)n2) % (long)n;
        return Color.getHSBColor(f /= (float)n, 0.8f, 1.001f).getRGB();
    }
}

