/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.math;

public class MathUtil {
    public static double incValue(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static double roundToHalf(double d) {
        return (double)Math.round(d * 2.0) / 2.0;
    }
}

