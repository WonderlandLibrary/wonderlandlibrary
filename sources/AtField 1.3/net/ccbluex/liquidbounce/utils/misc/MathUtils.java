/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public final class MathUtils {
    private static final Random random = new Random();

    public static float calculateGaussianValue(float f, float f2) {
        double d = 3.141592653;
        double d2 = 1.0 / Math.sqrt(2.0 * d * (double)(f2 * f2));
        return (float)(d2 * Math.exp((double)(-(f * f)) / (2.0 * (double)(f2 * f2))));
    }

    public static float interpolateFloat(float f, float f2, double d) {
        return MathUtils.interpolate(f, f2, (float)d).floatValue();
    }

    public static int clamp_int(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        return Math.min(n, n3);
    }

    public static double randomNumber(double d, double d2) {
        return Math.random() * (d - d2) + d2;
    }

    public static double roundToHalf(double d) {
        return (double)Math.round(d * 2.0) / 2.0;
    }

    public static int getRandomInRange(int n, int n2) {
        return (int)(Math.random() * (double)(n2 - n) + (double)n);
    }

    public static Double interpolate(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public static float clampValue(float f, float f2, float f3) {
        if (f < f2) {
            return f2;
        }
        return Math.min(f, f3);
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

    public static double roundToDecimalPlace(double d, double d2) {
        double d3 = d2 / 2.0;
        double d4 = StrictMath.floor(d / d2) * d2;
        if (d >= d4 + d3) {
            return new BigDecimal(StrictMath.ceil(d / d2) * d2, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
        }
        return new BigDecimal(d4, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
    }

    public static double incValue(double d, double d2) {
        double d3 = 1.0 / d2;
        return (double)Math.round(d * d3) / d3;
    }

    public static double roundToPlace(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static char replaceString(char c, int n) {
        c = (char)(c ^ n);
        return c;
    }
}

