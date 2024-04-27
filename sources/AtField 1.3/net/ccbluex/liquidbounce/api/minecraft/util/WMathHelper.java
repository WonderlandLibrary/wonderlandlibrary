/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.jvm.JvmStatic;

public final class WMathHelper {
    public static final WMathHelper INSTANCE;

    static {
        WMathHelper wMathHelper;
        INSTANCE = wMathHelper = new WMathHelper();
    }

    @JvmStatic
    public static final float clamp_float(float f, float f2, float f3) {
        boolean bl = false;
        return f < f2 ? f2 : (f > f3 ? f3 : f);
    }

    @JvmStatic
    public static final int floor_double(double d) {
        int n = (int)d;
        return d < (double)n ? n - 1 : n;
    }

    @JvmStatic
    public static final float wrapAngleTo180_float(float f) {
        float f2 = f % 360.0f;
        if (f2 >= 180.0f) {
            f2 -= 360.0f;
        }
        if (f2 < -180.0f) {
            f2 += 360.0f;
        }
        return f2;
    }

    @JvmStatic
    public static final double clamp_double(double d, double d2, double d3) {
        boolean bl = false;
        return d < d2 ? d2 : (d > d3 ? d3 : d);
    }

    private WMathHelper() {
    }
}

