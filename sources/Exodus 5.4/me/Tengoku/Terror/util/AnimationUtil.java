/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

public enum AnimationUtil {
    INSTANCE;


    public double animate(double d, double d2, double d3) {
        boolean bl;
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
        d2 = bl ? (d2 += d5) : (d2 -= d5);
        return d2;
    }

    public float calculateCompensation(float f, float f2, long l, int n) {
        float f3 = f2 - f;
        if (l < 1L) {
            l = 1L;
        }
        if (f3 > (float)n) {
            double d = (double)((long)n * l / 16L) < 0.25 ? 0.5 : (double)((long)n * l / 16L);
            if ((f2 = (float)((double)f2 - d)) < f) {
                f2 = f;
            }
        } else if (f3 < (float)(-n)) {
            double d = (double)((long)n * l / 16L) < 0.25 ? 0.5 : (double)((long)n * l / 16L);
            if ((f2 = (float)((double)f2 + d)) > f) {
                f2 = f;
            }
        } else {
            f2 = f;
        }
        return f2;
    }

    public float value(long l) {
        return Math.min(1.0f, (float)Math.pow((double)(System.currentTimeMillis() - l) / 10.0, 1.4) / 80.0f);
    }
}

