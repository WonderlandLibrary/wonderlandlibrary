// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Objects;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.Beta;
import java.util.Map;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableBiMapFauxverideShim<K, V> implements BiMap<K, V>
{
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
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7), ImmutableMap.entryOf(k8, v8));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7), ImmutableMap.entryOf(k8, v8), ImmutableMap.entryOf(k9, v9));
    }
    
    public static <K, V> ImmutableBiMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        return RegularImmutableBiMap.fromEntries(ImmutableMap.entryOf(k1, v1), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5), ImmutableMap.entryOf(k6, v6), ImmutableMap.entryOf(k7, v7), ImmutableMap.entryOf(k8, v8), ImmutableMap.entryOf(k9, v9), ImmutableMap.entryOf(k10, v10));
    }
    
    @SafeVarargs
    public static <K, V> ImmutableBiMap<K, V> ofEntries(final Map.Entry<? extends K, ? extends V>... entries) {
        final Map.Entry<K, V>[] entries2 = (Map.Entry<K, V>[])entries;
        return RegularImmutableBiMap.fromEntries(entries2);
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    @Beta
    public static <K, V> Builder<K, V> builderWithExpectedSize(final int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        return new Builder<K, V>(expectedSize);
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
    
    @Override
    final ImmutableSet<V> createValues() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Deprecated
    @CheckForNull
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V forcePut(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm((ImmutableBiMap<Object, Object>)this);
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
            return this.buildOrThrow();
        }
        
        @Override
        public ImmutableBiMap<K, V> buildOrThrow() {
            switch (this.size) {
                case 0: {
                    return ImmutableBiMap.of();
                }
                case 1: {
                    final Map.Entry<K, V> onlyEntry = Objects.requireNonNull(this.entries[0]);
                    return ImmutableBiMap.of(onlyEntry.getKey(), onlyEntry.getValue());
                }
                default: {
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = (Map.Entry<K, V>[])Arrays.copyOf((Map.Entry<K, V>[])this.entries, this.size);
                        }
                        Arrays.sort(this.entries, 0, this.size, (Comparator<? super Map.Entry<K, V>>)Ordering.from(this.valueComparator).onResultOf((com.google.common.base.Function<Map.Entry<?, ? extends V>, ? extends V>)Maps.valueFunction()));
                    }
                    this.entriesUsed = true;
                    return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
                }
            }
        }
        
        @VisibleForTesting
        @Override
        ImmutableBiMap<K, V> buildJdkBacked() {
            Preconditions.checkState(this.valueComparator == null, (Object)"buildJdkBacked is for tests only, doesn't support orderEntriesByValue");
            switch (this.size) {
                case 0: {
                    return ImmutableBiMap.of();
                }
                case 1: {
                    final Map.Entry<K, V> onlyEntry = Objects.requireNonNull(this.entries[0]);
                    return ImmutableBiMap.of(onlyEntry.getKey(), onlyEntry.getValue());
                }
                default: {
                    this.entriesUsed = true;
                    return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
                }
            }
        }
    }
    
    private static class SerializedForm<K, V> extends ImmutableMap.SerializedForm<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableBiMap<K, V> bimap) {
            super(bimap);
        }
        
        @Override
        ImmutableBiMap.Builder<K, V> makeBuilder(final int size) {
            return new ImmutableBiMap.Builder<K, V>(size);
        }
    }
}
