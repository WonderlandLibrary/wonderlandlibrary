/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

public class MouseUtil {
    public static boolean mouseWithinBounds(int n, int n2, double d, double d2, double d3, double d4) {
        return (double)n >= d && (double)n <= d + d3 && (double)n2 >= d2 && (double)n2 <= d2 + d4;
    }
}

