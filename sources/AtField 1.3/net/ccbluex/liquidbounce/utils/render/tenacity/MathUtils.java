/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render.tenacity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class MathUtils {
    public static Double interpolate(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public static float getRandomFloat(float f, float f2) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextFloat() * (f - f2) + f2;
    }

    public static double getRandomInRange(double d, double d2) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextDouble() * (d2 - d) + d;
    }

    public static int interpolateInt(int n, int n2, double d) {
        return MathUtils.interpolate(n, n2, (float)d).intValue();
    }

    public static double roundToHalf(double d) {
        return (double)Math.round(d * 2.0) / 2.0;
    }

    public static double lerp(double d, double d2, double d3) {
        return (1.0 - d3) * d + d3 * d2;
    }

    public static float interpolateFloat(float f, float f2, double d) {
        return MathUtils.interpolate(f, f2, (float)d).floatValue();
    }

    public static float getRandomInRange(float f, float f2) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextFloat() * (f2 - f) + f;
    }

    public static int getRandomInRange(int n, int n2) {
        return (int)(Math.random() * (double)(n2 - n) + (double)n);
    }

    public static double round(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static float calculateGaussianValue(float f, float f2) {
        double d = 3.141592653;
        double d2 = 1.0 / Math.sqrt(2.0 * d * (double)(f2 * f2));
        return (float)(d2 * Math.exp((double)(-(f * f)) / (2.0 * (double)(f2 * f2))));
    }

    public static double round(double d, double d2) {
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale((int)d2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}

