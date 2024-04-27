/*
 * Decompiled with CFR 0.152.
 */
package liying.utils.animation;

public class AnimationUtil {
    public static double animate(double d, double d2, double d3) {
        boolean bl;
        boolean bl2 = bl = d > d2;
        if (d3 < 0.0) {
            d3 = 0.0;
        } else if (d3 > 1.0) {
            d3 = 1.0;
        }
        double d4 = Math.max(d, d2) - Math.min(d, d2);
        double d5 = d4 * d3;
        if (d5 < (double)0.1f) {
            d5 = 0.1f;
        }
        d2 = bl ? d2 + d5 : d2 - d5;
        return d2;
    }

    public static float moveUD(float f, float f2, float f3, float f4) {
        float f5 = (f2 - f) * f3;
        if (f5 > 10.0f) {
            f5 = Math.max(f4, f5);
            f5 = Math.min(f2 - f, f5);
        } else if (f5 < 10.0f) {
            f5 = Math.min(-f4, f5);
            f5 = Math.max(f2 - f, f5);
        }
        return f + f5;
    }

    public static float calculateCompensation(float f, float f2, long l, int n) {
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

    public static float moveTowards(float f, float f2, float f3, float f4) {
        float f5 = (f2 - f) * f3;
        if (f5 > 0.0f) {
            f5 = Math.max(f4, f5);
            f5 = Math.min(f2 - f, f5);
        } else if (f5 < 0.0f) {
            f5 = Math.min(-f4, f5);
            f5 = Math.max(f2 - f, f5);
        }
        return f + f5;
    }

    public static float mvoeUD(float f, float f2, float f3) {
        return AnimationUtil.moveUD(f, f2, 0.125f, f3);
    }

    public static float animate(float f, float f2, double d) {
        boolean bl;
        boolean bl2 = bl = f > f2;
        if (d < 0.0) {
            d = 0.0;
        } else if (d > 1.0) {
            d = 1.0;
        }
        float f3 = Math.max(f, f2) - Math.min(f, f2);
        float f4 = (float)((double)f3 * d);
        f2 = bl ? f2 + f4 : f2 - f4;
        return f2;
    }

    public static float easeOut(float f, float f2) {
        f = f / f2 - 1.0f;
        return f * f * f + 1.0f;
    }
}

