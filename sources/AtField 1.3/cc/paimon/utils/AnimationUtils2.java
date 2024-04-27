/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.utils;

public final class AnimationUtils2 {
    public static float changer(float f, float f2, float f3, float f4) {
        if ((f += f2) > f4) {
            f = f4;
        }
        if (f < f3) {
            f = f3;
        }
        return f;
    }

    public static float animate(float f, float f2, float f3) {
        boolean bl;
        if (f2 == f) {
            return f2;
        }
        boolean bl2 = bl = f > f2;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        } else if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        double d = Math.max((double)f, (double)f2) - Math.min((double)f, (double)f2);
        double d2 = d * (double)f3;
        if (d2 < 0.1) {
            d2 = 0.1;
        }
        if (bl) {
            if ((f2 += (float)d2) >= f) {
                f2 = f;
            }
        } else if (f < f2 && (f2 -= (float)d2) <= f) {
            f2 = f;
        }
        return f2;
    }

    public static double animate(double d, double d2, double d3) {
        boolean bl;
        if (d2 == d) {
            return d2;
        }
        boolean bl2 = bl = d > d2;
        if (d3 < 0.0) {
            d3 = 0.0;
        } else if (d3 > 1.0) {
            d3 = 1.0;
        }
        double d4 = Math.max(d, d2) - Math.min(d, d2);
        double d5 = d4 * d3;
        if (d5 < 0.1) {
            d5 = 0.1;
        }
        if (bl) {
            if ((d2 += d5) >= d) {
                d2 = d;
            }
        } else if (d < d2 && (d2 -= d5) <= d) {
            d2 = d;
        }
        return d2;
    }

    public static double changer(double d, double d2, double d3, double d4) {
        if ((d += d2) > d4) {
            d = d4;
        }
        if (d < d3) {
            d = d3;
        }
        return d;
    }
}

