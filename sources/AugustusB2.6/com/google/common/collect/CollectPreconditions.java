// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CollectPreconditions
{
    static void checkEntryNotNull(final Object key, final Object value) {
        if (key == null) {
            final String value2 = String.valueOf(value);
            throw new NullPointerException(new StringBuilder(24 + String.valueOf(value2).length()).append("null key in entry: null=").append(value2).toString());
        }
        if (value == null) {
            final String value3 = String.valueOf(key);
            throw new NullPointerException(new StringBuilder(26 + String.valueOf(value3).length()).append("null value in entry: ").append(value3).append("=null").toString());
        }
    }
    
    @CanIgnoreReturnValue
    static int checkNonnegative(final int value, final String name) {
        if (value < 0) {
            throw new IllegalArgumentException(new StringBuilder(40 + String.valueOf(name).length()).append(name).append(" cannot be negative but was: ").append(value).toString());
        }
        return value;
    }
    
    @CanIgnoreReturnValue
    static long checkNonnegative(final long value, final String name) {
        if (value < 0L) {
            throw new IllegalArgumentException(new StringBuilder(49 + String.valueOf(name).length()).append(name).append(" cannot be negative but was: ").append(value).toString());
        }
        return value;
    }
    
    static void checkPositive(final int value, final String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(new StringBuilder(38 + String.valueOf(name).length()).append(name).append(" must be positive but was: ").append(value).toString());
        }
    }
    
    static void checkRemove(final boolean canRemove) {
        Preconditions.checkState(canRemove, (Object)"no calls to next() since the last call to remove()");
    }
}
