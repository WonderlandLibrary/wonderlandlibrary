/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.util;

public final class MathUtil {
    public static int ceilLog2(int i2) {
        return i2 > 0 ? 32 - Integer.numberOfLeadingZeros(i2 - 1) : 0;
    }

    public static int clamp(int i2, int min, int max) {
        if (i2 < min) {
            return min;
        }
        return i2 > max ? max : i2;
    }
}

