// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.BiFunction;
import java.util.Map;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use ImmutableRangeMap or TreeRangeMap")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface RangeMap<K extends Comparable, V>
{
    @CheckForNull
    V get(final K p0);
    
    @CheckForNull
    Map.Entry<Range<K>, V> getEntry(final K p0);
    
    Range<K> span();
    
    void put(final Range<K> p0, final V p1);
    
    void putCoalescing(final Range<K> p0, final V p1);
    
    void putAll(final RangeMap<K, V> p0);
    
    void clear();
    
    void remove(final Range<K> p0);
    
    void merge(final Range<K> p0, @CheckForNull final V p1, final BiFunction<? super V, ? super V, ? extends V> p2);
    
    Map<Range<K>, V> asMapOfRanges();
    
    Map<Range<K>, V> asDescendingMapOfRanges();
    
    RangeMap<K, V> subRangeMap(final Range<K> p0);
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
    
    String toString();
}
