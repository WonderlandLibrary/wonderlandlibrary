/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.utils.render.animation;

import net.minecraft.client.Minecraft;

public class AnimationHelper {
    public static int deltaTime;
    public static float speedTarget;

    public static float animation(float f, float f2, float f3, float f4) {
        float f5 = (f2 - f) / Math.max((float)Minecraft.func_175610_ah(), 5.0f) * 15.0f;
        if (f5 > 0.0f) {
            f5 = Math.max(f4, f5);
            f5 = Math.min(f2 - f, f5);
        } else if (f5 < 0.0f) {
            f5 = Math.min(-f4, f5);
            f5 = Math.max(f2 - f, f5);
        }
        return f + f5;
    }

    public static float calculateCompensation(float f, float f2, long l, double d) {
        float f3 = f2 - f;
        if (l < 1L) {
            l = 1L;
        }
        if (l > 1000L) {
            l = 16L;
        }
        double d2 = Math.max(d * (double)l / 16.0, 0.5);
        if ((double)f3 > d) {
            double d3 = d2;
            if ((f2 = (float)((double)f2 - d3)) < f) {
                f2 = f;
            }
        } else if ((double)f3 < -d) {
            double d4 = d2;
            if ((f2 = (float)((double)f2 + d4)) > f) {
                f2 = f;
            }
        } else {
            f2 = f;
        }
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

    static {
        speedTarget = 0.125f;
    }

    public static float animation(float f, float f2, float f3) {
        return AnimationHelper.animation(f, f2, speedTarget, f3);
    }
}

