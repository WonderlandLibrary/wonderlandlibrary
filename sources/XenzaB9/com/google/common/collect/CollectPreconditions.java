// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CollectPreconditions
{
    static void checkEntryNotNull(final Object key, final Object value) {
        if (key == null) {
            final String value2 = String.valueOf(String.valueOf(value));
            throw new NullPointerException(new StringBuilder(24 + value2.length()).append("null key in entry: null=").append(value2).toString());
        }
        if (value == null) {
            final String value3 = String.valueOf(String.valueOf(key));
            throw new NullPointerException(new StringBuilder(26 + value3.length()).append("null value in entry: ").append(value3).append("=null").toString());
        }
    }
    
    static int checkNonnegative(final int value, final String name) {
        if (value < 0) {
            final String value2 = String.valueOf(String.valueOf(name));
            throw new IllegalArgumentException(new StringBuilder(40 + value2.length()).append(value2).append(" cannot be negative but was: ").append(value).toString());
        }
        return value;
    }
    
    static void checkRemove(final boolean canRemove) {
        Preconditions.checkState(canRemove, (Object)"no calls to next() since the last call to remove()");
    }
}
