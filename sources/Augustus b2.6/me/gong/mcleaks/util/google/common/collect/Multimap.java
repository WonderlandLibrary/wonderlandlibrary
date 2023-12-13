// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Multimap<K, V>
{
    int size();
    
    boolean isEmpty();
    
    boolean containsKey(@Nullable @CompatibleWith("K") final Object p0);
    
    boolean containsValue(@Nullable @CompatibleWith("V") final Object p0);
    
    boolean containsEntry(@Nullable @CompatibleWith("K") final Object p0, @Nullable @CompatibleWith("V") final Object p1);
    
    @CanIgnoreReturnValue
    boolean put(@Nullable final K p0, @Nullable final V p1);
    
    @CanIgnoreReturnValue
    boolean remove(@Nullable @CompatibleWith("K") final Object p0, @Nullable @CompatibleWith("V") final Object p1);
    
    @CanIgnoreReturnValue
    boolean putAll(@Nullable final K p0, final Iterable<? extends V> p1);
    
    @CanIgnoreReturnValue
    boolean putAll(final Multimap<? extends K, ? extends V> p0);
    
    @CanIgnoreReturnValue
    Collection<V> replaceValues(@Nullable final K p0, final Iterable<? extends V> p1);
    
    @CanIgnoreReturnValue
    Collection<V> removeAll(@Nullable @CompatibleWith("K") final Object p0);
    
    void clear();
    
    Collection<V> get(@Nullable final K p0);
    
    Set<K> keySet();
    
    Multiset<K> keys();
    
    Collection<V> values();
    
    Collection<Map.Entry<K, V>> entries();
    
    default void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        this.entries().forEach(entry -> action.accept(entry.getKey(), (Object)entry.getValue()));
    }
    
    Map<K, Collection<V>> asMap();
    
    boolean equals(@Nullable final Object p0);
    
    int hashCode();
}
