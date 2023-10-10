// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.NotNull;
import java.util.Set;

public final class ShadyPines
{
    private ShadyPines() {
    }
    
    @Deprecated
    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(final Class<E> type, final E... constants) {
        return MonkeyBars.enumSet(type, constants);
    }
    
    public static boolean equals(final double a, final double b) {
        return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
    }
    
    public static boolean equals(final float a, final float b) {
        return Float.floatToIntBits(a) == Float.floatToIntBits(b);
    }
}
