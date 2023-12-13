/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.viaversion.viaversion.libs.kyori.adventure.util;

import com.viaversion.viaversion.libs.kyori.adventure.util.MonkeyBars;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class ShadyPines {
    private ShadyPines() {
    }

    @Deprecated
    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(Class<E> type, E ... constants) {
        return MonkeyBars.enumSet(type, constants);
    }

    public static boolean equals(double a2, double b2) {
        return Double.doubleToLongBits(a2) == Double.doubleToLongBits(b2);
    }

    public static boolean equals(float a2, float b2) {
        return Float.floatToIntBits(a2) == Float.floatToIntBits(b2);
    }
}

