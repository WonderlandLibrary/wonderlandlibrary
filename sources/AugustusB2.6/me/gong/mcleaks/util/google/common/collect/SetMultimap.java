// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Collection;
import java.util.Map;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V>
{
    Set<V> get(@Nullable final K p0);
    
    @CanIgnoreReturnValue
    Set<V> removeAll(@Nullable final Object p0);
    
    @CanIgnoreReturnValue
    Set<V> replaceValues(final K p0, final Iterable<? extends V> p1);
    
    Set<Map.Entry<K, V>> entries();
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@Nullable final Object p0);
}
