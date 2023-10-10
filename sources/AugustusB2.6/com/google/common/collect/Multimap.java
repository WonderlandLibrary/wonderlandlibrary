// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use ImmutableMultimap, HashMultimap, or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Multimap<K, V>
{
    int size();
    
    boolean isEmpty();
    
    boolean containsKey(@CheckForNull @CompatibleWith("K") final Object p0);
    
    boolean containsValue(@CheckForNull @CompatibleWith("V") final Object p0);
    
    boolean containsEntry(@CheckForNull @CompatibleWith("K") final Object p0, @CheckForNull @CompatibleWith("V") final Object p1);
    
    @CanIgnoreReturnValue
    boolean put(@ParametricNullness final K p0, @ParametricNullness final V p1);
    
    @CanIgnoreReturnValue
    boolean remove(@CheckForNull @CompatibleWith("K") final Object p0, @CheckForNull @CompatibleWith("V") final Object p1);
    
    @CanIgnoreReturnValue
    boolean putAll(@ParametricNullness final K p0, final Iterable<? extends V> p1);
    
    @CanIgnoreReturnValue
    boolean putAll(final Multimap<? extends K, ? extends V> p0);
    
    @CanIgnoreReturnValue
    Collection<V> replaceValues(@ParametricNullness final K p0, final Iterable<? extends V> p1);
    
    @CanIgnoreReturnValue
    Collection<V> removeAll(@CheckForNull @CompatibleWith("K") final Object p0);
    
    void clear();
    
    Collection<V> get(@ParametricNullness final K p0);
    
    Set<K> keySet();
    
    Multiset<K> keys();
    
    Collection<V> values();
    
    Collection<Map.Entry<K, V>> entries();
    
    default void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        this.entries().forEach(entry -> action.accept(entry.getKey(), (Object)entry.getValue()));
    }
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@CheckForNull final Object p0);
    
    int hashCode();
}
