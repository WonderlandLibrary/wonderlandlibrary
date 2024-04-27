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

    public static double round(double value, double inc) {
        if (inc == 0.0) {
            return value;
        }
        if (inc == 1.0) {
            return Math.round(value);
        }
        double halfOfInc = inc / 2.0;
        double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc).doubleValue();
        }
        return new BigDecimal(floored).doubleValue();
    }

    public static char replaceString(char c, int n) {
        c = (char)(c ^ n);
        return c;
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (double)(sigma * sigma));
        return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(sigma * sigma))));
    }

    public static int clamp_int(int p_clamp_int_0_, int p_clamp_int_1_, int p_clamp_int_2_) {
        if (p_clamp_int_0_ < p_clamp_int_1_) {
            return p_clamp_int_1_;
        }
        return Math.min(p_clamp_int_0_, p_clamp_int_2_);
    }

    public static int getRandomInRange(int min, int max) {
        return (int)(Math.random() * (double)(max - min) + (double)min);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return MathUtils.interpolate(oldValue, newValue, (float)interpolationValue).floatValue();
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }

    public static double roundToHalf(double d) {
        return (double)Math.round(d * 2.0) / 2.0;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundToDecimalPlace(double value, double inc) {
        double halfOfInc = inc / 2.0;
        double floored = StrictMath.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(StrictMath.ceil(value / inc) * inc, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
        }
        return new BigDecimal(floored, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
    }

    public static double incValue(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static float clampValue(float value, float floor, float cap) {
        if (value < floor) {
            return floor;
        }
        return Math.min(value, cap);
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }
}

