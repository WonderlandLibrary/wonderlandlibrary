/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class IntegerCache {
    private static final Integer[] field_181757_a = new Integer[65535];

    public static Integer func_181756_a(int p_181756_0_) {
        Integer n;
        if (p_181756_0_ > 0 && p_181756_0_ < field_181757_a.length) {
            n = field_181757_a[p_181756_0_];
            return n;
        }
        n = p_181756_0_;
        return n;
    }

    static {
        int i = 0;
        int j = field_181757_a.length;
        while (i < j) {
            IntegerCache.field_181757_a[i] = i;
            ++i;
        }
    }
}

