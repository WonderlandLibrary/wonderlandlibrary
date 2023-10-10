// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.Comparator;
import java.util.Collection;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.SortedSet;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V>
{
    SortedSet<V> get(@ParametricNullness final K p0);
    
    @CanIgnoreReturnValue
    SortedSet<V> removeAll(@CheckForNull final Object p0);
    
    @CanIgnoreReturnValue
    SortedSet<V> replaceValues(@ParametricNullness final K p0, final Iterable<? extends V> p1);
    
    Map<K, Collection<V>> asMap();
    
    @CheckForNull
    Comparator<? super V> valueComparator();
}
