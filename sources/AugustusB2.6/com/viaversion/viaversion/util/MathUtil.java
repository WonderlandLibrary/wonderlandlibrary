// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

public final class MathUtil
{
    public static int ceilLog2(final int i) {
        return (i > 0) ? (32 - Integer.numberOfLeadingZeros(i - 1)) : 0;
    }
    
    public static int clamp(final int i, final int min, final int max) {
        if (i < min) {
            return min;
        }
        return (i > max) ? max : i;
    }
}
