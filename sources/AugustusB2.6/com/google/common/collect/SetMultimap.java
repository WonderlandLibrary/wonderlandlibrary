// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V>
{
    Set<V> get(@ParametricNullness final K p0);
    
    @CanIgnoreReturnValue
    Set<V> removeAll(@CheckForNull final Object p0);
    
    @CanIgnoreReturnValue
    Set<V> replaceValues(@ParametricNullness final K p0, final Iterable<? extends V> p1);
    
    Set<Map.Entry<K, V>> entries();
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@CheckForNull final Object p0);
}
