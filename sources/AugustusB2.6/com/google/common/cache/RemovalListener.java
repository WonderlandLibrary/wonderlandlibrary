// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface RemovalListener<K, V>
{
    void onRemoval(final RemovalNotification<K, V> p0);
}
