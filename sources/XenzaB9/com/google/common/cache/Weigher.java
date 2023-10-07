// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public interface Weigher<K, V>
{
    int weigh(final K p0, final V p1);
}
