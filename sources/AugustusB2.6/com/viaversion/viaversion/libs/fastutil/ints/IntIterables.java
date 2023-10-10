// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

public final class IntIterables
{
    private IntIterables() {
    }
    
    public static long size(final IntIterable iterable) {
        long c = 0L;
        for (final int dummy : iterable) {
            ++c;
        }
        return c;
    }
}
