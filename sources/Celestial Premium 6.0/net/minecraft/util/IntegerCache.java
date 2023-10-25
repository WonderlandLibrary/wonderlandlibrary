/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

public class IntegerCache {
    private static final Integer[] CACHE = new Integer[65535];

    public static Integer getInteger(int value) {
        return value > 0 && value < CACHE.length ? CACHE[value] : value;
    }

    static {
        int j = CACHE.length;
        for (int i = 0; i < j; ++i) {
            IntegerCache.CACHE[i] = i;
        }
    }
}

