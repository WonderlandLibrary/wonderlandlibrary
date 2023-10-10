// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Weigher<K, V>
{
    int weigh(final K p0, final V p1);
}
