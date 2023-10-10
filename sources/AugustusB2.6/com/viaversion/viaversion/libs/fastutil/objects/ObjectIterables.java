// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;

public final class ObjectIterables
{
    private ObjectIterables() {
    }
    
    public static <K> long size(final Iterable<K> iterable) {
        long c = 0L;
        for (final K dummy : iterable) {
            ++c;
        }
        return c;
    }
}
