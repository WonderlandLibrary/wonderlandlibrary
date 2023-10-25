/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class IntegerCache {
    private static final Integer[] field_181757_a = new Integer[65535];

    public static Integer func_181756_a(int n) {
        return n > 0 && n < field_181757_a.length ? field_181757_a[n] : Integer.valueOf(n);
    }

    static {
        int n = 0;
        int n2 = field_181757_a.length;
        while (n < n2) {
            IntegerCache.field_181757_a[n] = n;
            ++n;
        }
    }
}

