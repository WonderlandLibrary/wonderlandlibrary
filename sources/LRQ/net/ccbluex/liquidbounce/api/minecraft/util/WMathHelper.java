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

    @JvmStatic
    public static final float wrapAngleTo180_float(float angle) {
        float value = angle % 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    @JvmStatic
    public static final float clamp_float(float num, float min, float max) {
        int $i$f$clamp_float = 0;
        return num < min ? min : (num > max ? max : num);
    }

    @JvmStatic
    public static final double clamp_double(double num, double min, double max) {
        int $i$f$clamp_double = 0;
        return num < min ? min : (num > max ? max : num);
    }

    @JvmStatic
    public static final int floor_double(double value) {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }

    private WMathHelper() {
    }

    static {
        WMathHelper wMathHelper;
        INSTANCE = wMathHelper = new WMathHelper();
    }
}

