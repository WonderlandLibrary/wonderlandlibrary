/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.utils;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class AnimationUtil {
    public static double delta;

    public static float calculateCompensation(float f, float f2, double d, long l) {
        float f3 = f2 - f;
        double d2 = (double)l * (d / 50.0);
        f2 = (double)f3 > d ? ((double)f2 - d2 > (double)f ? (float)((double)f2 - d2) : f) : ((double)f3 < -d ? ((double)f2 + d2 < (double)f ? (float)((double)f2 + d2) : f) : f);
        return f2;
    }

    public static double animate(double d, double d2, double d3) {
        boolean bl = d > d2;
        boolean bl2 = bl;
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
        d2 = bl ? (d2 = d2 + d5) : (d2 = d2 - d5);
        return d2;
    }

    public static double easing(double d, double d2, double d3) {
        return Math.abs(d2 - d) * d3;
    }

    public static float getAnimationState(float f, float f2, float f3) {
        float f4 = (float)(delta * (double)(f3 / 1000.0f));
        f = f < f2 ? (f + f4 < f2 ? (f += f4) : f2) : (f - f4 > f2 ? (f -= f4) : f2);
        return f;
    }

    public static float lstransition(float f, float f2, double d) {
        double d2 = Math.abs(f2 - f);
        float f3 = (float)Math.abs((double)(f2 - (f2 - Math.abs(f2 - f))) / (100.0 - d * 10.0));
        float f4 = f;
        if (d2 > 0.0) {
            if (f < f2) {
                f4 += f3 * (float)RenderUtils.deltaTime;
            } else if (f > f2) {
                f4 -= f3 * (float)RenderUtils.deltaTime;
            }
        } else {
            f4 = f2;
        }
        if ((double)Math.abs(f2 - f4) < 0.01 && f4 != f2) {
            f4 = f2;
        }
        return f4;
    }

    public static float easeOut(float f, float f2) {
        f = f / f2 - 1.0f;
        return f * f * f + 1.0f;
    }
}

