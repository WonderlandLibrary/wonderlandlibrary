/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.MarlinConst;

public final class FloatMath
implements MarlinConst {
    static final boolean CHECK_OVERFLOW = true;
    static final boolean CHECK_NAN = true;

    private FloatMath() {
    }

    public static int max(int n, int n2) {
        return n >= n2 ? n : n2;
    }

    public static int min(int n, int n2) {
        return n <= n2 ? n : n2;
    }

    public static int ceil_int(float f) {
        int n = (int)f;
        if (f <= (float)n || n == Integer.MAX_VALUE || Float.isNaN(f)) {
            return n;
        }
        return n + 1;
    }

    public static int ceil_int(double d) {
        int n = (int)d;
        if (d <= (double)n || n == Integer.MAX_VALUE || Double.isNaN(d)) {
            return n;
        }
        return n + 1;
    }

    public static int floor_int(float f) {
        int n = (int)f;
        if (f >= (float)n || n == Integer.MIN_VALUE || Float.isNaN(f)) {
            return n;
        }
        return n - 1;
    }

    public static int floor_int(double d) {
        int n = (int)d;
        if (d >= (double)n || n == Integer.MIN_VALUE || Double.isNaN(d)) {
            return n;
        }
        return n - 1;
    }
}

