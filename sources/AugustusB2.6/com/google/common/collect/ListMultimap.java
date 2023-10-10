// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface ListMultimap<K, V> extends Multimap<K, V>
{
    List<V> get(@ParametricNullness final K p0);
    
    @CanIgnoreReturnValue
    List<V> removeAll(@CheckForNull final Object p0);
    
    @CanIgnoreReturnValue
    List<V> replaceValues(@ParametricNullness final K p0, final Iterable<? extends V> p1);
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@CheckForNull final Object p0);
}
