// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.util.stream.Collector;
import java.util.function.Function;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableBiMapFauxverideShim<K, V> implements BiMap<K, V>
{
    @Beta
    public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
    }
    
    public static <K, V> ImmutableBiMap<K, V> of() {
        return (ImmutableBiMap<K, V>)RegularImmutableBiMap.EMPTY;
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1) {
        return new SingletonImmutableBiMap<K, V>(k1, v1);
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return (ImmutableBiMap<K, V>)RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return (ImmutableBiMap<K, V>)RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return (ImmutableBiMap<K, V>)RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return (ImmutableBiMap<K, V>)RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableBiMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableBiMap) {
            final ImmutableBiMap<K, V> bimap = (ImmutableBiMap<K, V>)(ImmutableBiMap)map;
            if (!bimap.isPartialView()) {
                return bimap;
            }
        }
        return copyOf((Iterable<? extends Map.Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    @Beta
    public static <K, V> ImmutableBiMap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        final Map.Entry<K, V>[] entryArray = Iterables.toArray(entries, ImmutableBiMap.EMPTY_ENTRY_ARRAY);
        switch (entryArray.length) {
            case 0: {
                return of();
            }
            case 1: {
                final Map.Entry<K, V> entry = entryArray[0];
                return of(entry.getKey(), entry.getValue());
            }
            default: {
                return RegularImmutableBiMap.fromEntries(entryArray);
            }
        }
    }
    
    ImmutableBiMap() {
    }
    
    @Override
    public abstract ImmutableBiMap<V, K> inverse();
    
    @Override
    public ImmutableSet<V> values() {
        return (ImmutableSet<V>)this.inverse().keySet();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public V forcePut(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm(this);
    }
    
    public static final class Builder<K, V> extends ImmutableMap.Builder<K, V>
    {
        public Builder() {
        }
        
        Builder(final int size) {
            super(size);
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final K key, final V value) {
            super.put(key, value);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        @Override
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            super.putAll(entries);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        @Override
        public Builder<K, V> orderEntriesByValue(final Comparator<? super V> valueComparator) {
            super.orderEntriesByValue(valueComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        Builder<K, V> combine(final ImmutableMap.Builder<K, V> builder) {
            super.combine(builder);
            return this;
        }
        
        @Override
        public ImmutableBiMap<K, V> build() {
            switch (this.size) {
                case 0: {
                    return ImmutableBiMap.of();
                }
                case 1: {
                    return ImmutableBiMap.of((K)this.entries[0].getKey(), (V)this.entries[0].getValue());
                }
                default: {
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = (ImmutableMapEntry<K, V>[])Arrays.copyOf((ImmutableMapEntry<K, V>[])this.entries, this.size);
                        }
                        Arrays.sort(this.entries, 0, this.size, (Comparator<? super ImmutableMapEntry<K, V>>)Ordering.from(this.valueComparator).onResultOf((me.gong.mcleaks.util.google.common.base.Function<Map.Entry<?, ? extends V>, ? extends V>)Maps.valueFunction()));
                    }
                    this.entriesUsed = (this.size == this.entries.length);
                    return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
                }
            }
        }
    }
    
    private static class SerializedForm extends ImmutableMap.SerializedForm
    {
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableBiMap<?, ?> bimap) {
            super(bimap);
        }
        
        @Override
        Object readResolve() {
            final ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<Object, Object>();
            return this.createMap(builder);
        }
    }
}
