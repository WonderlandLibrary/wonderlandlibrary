// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Spliterators;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.BiFunction;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.Objects;
import java.util.EnumMap;
import java.util.SortedMap;
import com.google.common.annotations.Beta;
import java.util.AbstractMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.j2objc.annotations.RetainedWith;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Map;

@DoNotMock("Use ImmutableMap.of or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable
{
    static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient ImmutableSet<Entry<K, V>> entrySet;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient ImmutableSet<K> keySet;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient ImmutableCollection<V> values;
    @LazyInit
    @CheckForNull
    private transient ImmutableSetMultimap<K, V> multimapView;
    
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
    }
    
    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction) {
        return CollectCollectors.toImmutableMap(keyFunction, valueFunction, mergeFunction);
    }
    
    public static <K, V> ImmutableMap<K, V> of() {
        return (ImmutableMap<K, V>)RegularImmutableMap.EMPTY;
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1) {
        return ImmutableBiMap.of(k1, v1);
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8), entryOf(k9, v9));
    }
    
    public static <K, V> ImmutableMap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8), entryOf(k9, v9), entryOf(k10, v10));
    }
    
    @SafeVarargs
    public static <K, V> ImmutableMap<K, V> ofEntries(final Entry<? extends K, ? extends V>... entries) {
        final Entry<K, V>[] entries2 = (Entry<K, V>[])entries;
        return RegularImmutableMap.fromEntries(entries2);
    }
    
    static <K, V> Entry<K, V> entryOf(final K key, final V value) {
        CollectPreconditions.checkEntryNotNull(key, value);
        return new AbstractMap.SimpleImmutableEntry<K, V>(key, value);
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    @Beta
    public static <K, V> Builder<K, V> builderWithExpectedSize(final int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        return new Builder<K, V>(expectedSize);
    }
    
    static void checkNoConflict(final boolean safe, final String conflictDescription, final Entry<?, ?> entry1, final Entry<?, ?> entry2) {
        if (!safe) {
            throw conflictException(conflictDescription, entry1, entry2);
        }
    }
    
    static IllegalArgumentException conflictException(final String conflictDescription, final Object entry1, final Object entry2) {
        final String value = String.valueOf(entry1);
        final String value2 = String.valueOf(entry2);
        return new IllegalArgumentException(new StringBuilder(34 + String.valueOf(conflictDescription).length() + String.valueOf(value).length() + String.valueOf(value2).length()).append("Multiple entries with same ").append(conflictDescription).append(": ").append(value).append(" and ").append(value2).toString());
    }
    
    public static <K, V> ImmutableMap<K, V> copyOf(final Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableMap && !(map instanceof SortedMap)) {
            final ImmutableMap<K, V> kvMap = (ImmutableMap<K, V>)(ImmutableMap)map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }
        else if (map instanceof EnumMap) {
            final ImmutableMap<K, V> kvMap = copyOfEnumMap((EnumMap<K, ? extends V>)(EnumMap)map);
            return kvMap;
        }
        return copyOf((Iterable<? extends Entry<? extends K, ? extends V>>)map.entrySet());
    }
    
    @Beta
    public static <K, V> ImmutableMap<K, V> copyOf(final Iterable<? extends Entry<? extends K, ? extends V>> entries) {
        final Entry<K, V>[] entryArray = Iterables.toArray(entries, ImmutableMap.EMPTY_ENTRY_ARRAY);
        switch (entryArray.length) {
            case 0: {
                return of();
            }
            case 1: {
                final Entry<K, V> onlyEntry = Objects.requireNonNull(entryArray[0]);
                return of(onlyEntry.getKey(), onlyEntry.getValue());
            }
            default: {
                return RegularImmutableMap.fromEntries(entryArray);
            }
        }
    }
    
    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(final EnumMap<K, ? extends V> original) {
        final EnumMap<K, V> copy = new EnumMap<K, V>(original);
        for (final Entry<K, V> entry : copy.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(copy);
    }
    
    ImmutableMap() {
    }
    
    @Deprecated
    @CheckForNull
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V put(final K k, final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CheckForNull
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V putIfAbsent(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean replace(final K key, final V oldValue, final V newValue) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CheckForNull
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V replace(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CheckForNull
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final V remove(@CheckForNull final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        return this.get(key) != null;
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.values().contains(value);
    }
    
    @CheckForNull
    @Override
    public abstract V get(@CheckForNull final Object p0);
    
    @CheckForNull
    @Override
    public final V getOrDefault(@CheckForNull final Object key, @CheckForNull final V defaultValue) {
        final V result = this.get(key);
        if (result != null) {
            return result;
        }
        return defaultValue;
    }
    
    @Override
    public ImmutableSet<Entry<K, V>> entrySet() {
        final ImmutableSet<Entry<K, V>> result = this.entrySet;
        return (result == null) ? (this.entrySet = this.createEntrySet()) : result;
    }
    
    abstract ImmutableSet<Entry<K, V>> createEntrySet();
    
    @Override
    public ImmutableSet<K> keySet() {
        final ImmutableSet<K> result = this.keySet;
        return (result == null) ? (this.keySet = this.createKeySet()) : result;
    }
    
    abstract ImmutableSet<K> createKeySet();
    
    UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator<Entry<K, V>> entryIterator = this.entrySet().iterator();
        return new UnmodifiableIterator<K>(this) {
            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }
            
            @Override
            public K next() {
                return ((Entry)entryIterator.next()).getKey();
            }
        };
    }
    
    Spliterator<K> keySpliterator() {
        return CollectSpliterators.map(this.entrySet().spliterator(), (Function<? super Entry<K, V>, ? extends K>)Entry::getKey);
    }
    
    @Override
    public ImmutableCollection<V> values() {
        final ImmutableCollection<V> result = this.values;
        return (result == null) ? (this.values = this.createValues()) : result;
    }
    
    abstract ImmutableCollection<V> createValues();
    
    public ImmutableSetMultimap<K, V> asMultimap() {
        if (this.isEmpty()) {
            return ImmutableSetMultimap.of();
        }
        final ImmutableSetMultimap<K, V> result = this.multimapView;
        return (result == null) ? (this.multimapView = new ImmutableSetMultimap<K, V>(new MapViewOfValuesAsSingletonSets(), this.size(), null)) : result;
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return Maps.equalsImpl(this, object);
    }
    
    abstract boolean isPartialView();
    
    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }
    
    boolean isHashCodeFast() {
        return false;
    }
    
    @Override
    public String toString() {
        return Maps.toStringImpl(this);
    }
    
    Object writeReplace() {
        return new SerializedForm((ImmutableMap<Object, Object>)this);
    }
    
    static {
        EMPTY_ENTRY_ARRAY = new Entry[0];
    }
    
    @DoNotMock
    public static class Builder<K, V>
    {
        @CheckForNull
        Comparator<? super V> valueComparator;
        Entry<K, V>[] entries;
        int size;
        boolean entriesUsed;
        
        public Builder() {
            this(4);
        }
        
        Builder(final int initialCapacity) {
            this.entries = (Entry<K, V>[])new Entry[initialCapacity];
            this.size = 0;
            this.entriesUsed = false;
        }
        
        private void ensureCapacity(final int minCapacity) {
            if (minCapacity > this.entries.length) {
                this.entries = Arrays.copyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
                this.entriesUsed = false;
            }
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final K key, final V value) {
            this.ensureCapacity(this.size + 1);
            final Entry<K, V> entry = ImmutableMap.entryOf(key, value);
            this.entries[this.size++] = entry;
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final Entry<? extends K, ? extends V> entry) {
            return (Builder<K, V>)this.put(entry.getKey(), entry.getValue());
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(final Map<? extends K, ? extends V> map) {
            return this.putAll(map.entrySet());
        }
        
        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(final Iterable<? extends Entry<? extends K, ? extends V>> entries) {
            if (entries instanceof Collection) {
                this.ensureCapacity(this.size + ((Collection)entries).size());
            }
            for (final Entry<? extends K, ? extends V> entry : entries) {
                this.put(entry);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> orderEntriesByValue(final Comparator<? super V> valueComparator) {
            Preconditions.checkState(this.valueComparator == null, (Object)"valueComparator was already set");
            this.valueComparator = Preconditions.checkNotNull(valueComparator, (Object)"valueComparator");
            return this;
        }
        
        @CanIgnoreReturnValue
        Builder<K, V> combine(final Builder<K, V> other) {
            Preconditions.checkNotNull(other);
            this.ensureCapacity(this.size + other.size);
            System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
            this.size += other.size;
            return this;
        }
        
        public ImmutableMap<K, V> build() {
            return this.buildOrThrow();
        }
        
        public ImmutableMap<K, V> buildOrThrow() {
            if (this.valueComparator != null) {
                if (this.entriesUsed) {
                    this.entries = Arrays.copyOf(this.entries, this.size);
                }
                Arrays.sort(this.entries, 0, this.size, (Comparator<? super Entry<K, V>>)Ordering.from(this.valueComparator).onResultOf((com.google.common.base.Function<Entry<?, ? extends V>, ? extends V>)Maps.valueFunction()));
            }
            switch (this.size) {
                case 0: {
                    return ImmutableMap.of();
                }
                case 1: {
                    final Entry<K, V> onlyEntry = Objects.requireNonNull(this.entries[0]);
                    return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
                }
                default: {
                    this.entriesUsed = true;
                    return RegularImmutableMap.fromEntryArray(this.size, this.entries);
                }
            }
        }
        
        @VisibleForTesting
        ImmutableMap<K, V> buildJdkBacked() {
            Preconditions.checkState(this.valueComparator == null, (Object)"buildJdkBacked is only for testing; can't use valueComparator");
            switch (this.size) {
                case 0: {
                    return ImmutableMap.of();
                }
                case 1: {
                    final Entry<K, V> onlyEntry = Objects.requireNonNull(this.entries[0]);
                    return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
                }
                default: {
                    this.entriesUsed = true;
                    return JdkBackedImmutableMap.create(this.size, this.entries);
                }
            }
        }
    }
    
    abstract static class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V>
    {
        abstract UnmodifiableIterator<Entry<K, V>> entryIterator();
        
        Spliterator<Entry<K, V>> entrySpliterator() {
            return Spliterators.spliterator((Iterator<? extends Entry<K, V>>)this.entryIterator(), (long)this.size(), 1297);
        }
        
        @Override
        ImmutableSet<K> createKeySet() {
            return (ImmutableSet<K>)new ImmutableMapKeySet((ImmutableMap<Object, Object>)this);
        }
        
        @Override
        ImmutableSet<Entry<K, V>> createEntrySet() {
            class EntrySetImpl extends ImmutableMapEntrySet<K, V>
            {
                @Override
                ImmutableMap<K, V> map() {
                    return (ImmutableMap<K, V>)IteratorBasedImmutableMap.this;
                }
                
                @Override
                public UnmodifiableIterator<Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            }
            return (ImmutableSet<Entry<K, V>>)new EntrySetImpl();
        }
        
        @Override
        ImmutableCollection<V> createValues() {
            return (ImmutableCollection<V>)new ImmutableMapValues((ImmutableMap<Object, Object>)this);
        }
    }
    
    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>>
    {
        @Override
        public int size() {
            return ImmutableMap.this.size();
        }
        
        @Override
        ImmutableSet<K> createKeySet() {
            return ImmutableMap.this.keySet();
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            return ImmutableMap.this.containsKey(key);
        }
        
        @CheckForNull
        @Override
        public ImmutableSet<V> get(@CheckForNull final Object key) {
            final V outerValue = ImmutableMap.this.get(key);
            return (outerValue == null) ? null : ImmutableSet.of(outerValue);
        }
        
        @Override
        boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }
        
        @Override
        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }
        
        @Override
        boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }
        
        @Override
        UnmodifiableIterator<Entry<K, ImmutableSet<V>>> entryIterator() {
            final Iterator<Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>(this) {
                @Override
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }
                
                @Override
                public Entry<K, ImmutableSet<V>> next() {
                    final Entry<K, V> backingEntry = backingIterator.next();
                    return new AbstractMapEntry<K, ImmutableSet<V>>(this) {
                        @Override
                        public K getKey() {
                            return backingEntry.getKey();
                        }
                        
                        @Override
                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.of(backingEntry.getValue());
                        }
                    };
                }
            };
        }
    }
    
    static class SerializedForm<K, V> implements Serializable
    {
        private static final boolean USE_LEGACY_SERIALIZATION = true;
        private final Object keys;
        private final Object values;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableMap<K, V> map) {
            final Object[] keys = new Object[map.size()];
            final Object[] values = new Object[map.size()];
            int i = 0;
            for (final Entry<?, ?> entry : map.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                ++i;
            }
            this.keys = keys;
            this.values = values;
        }
        
        final Object readResolve() {
            if (!(this.keys instanceof ImmutableSet)) {
                return this.legacyReadResolve();
            }
            final ImmutableSet<K> keySet = (ImmutableSet<K>)this.keys;
            final ImmutableCollection<V> values = (ImmutableCollection<V>)this.values;
            final Builder<K, V> builder = this.makeBuilder(keySet.size());
            final UnmodifiableIterator<K> keyIter = keySet.iterator();
            final UnmodifiableIterator<V> valueIter = values.iterator();
            while (keyIter.hasNext()) {
                builder.put(keyIter.next(), valueIter.next());
            }
            return builder.build();
        }
        
        final Object legacyReadResolve() {
            final K[] keys = (K[])this.keys;
            final V[] values = (V[])this.values;
            final Builder<K, V> builder = this.makeBuilder(keys.length);
            for (int i = 0; i < keys.length; ++i) {
                builder.put(keys[i], values[i]);
            }
            return builder.build();
        }
        
        Builder<K, V> makeBuilder(final int size) {
            return new Builder<K, V>(size);
        }
    }
}
