// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Set;
import java.util.Comparator;
import java.util.Collection;
import java.util.Map;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.SortedSet;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V>
{
    SortedSet<V> get(@Nullable final K p0);
    
    @CanIgnoreReturnValue
    SortedSet<V> removeAll(@Nullable final Object p0);
    
    @CanIgnoreReturnValue
    SortedSet<V> replaceValues(final K p0, final Iterable<? extends V> p1);
    
    Map<K, Collection<V>> asMap();
    
    Comparator<? super V> valueComparator();
}
