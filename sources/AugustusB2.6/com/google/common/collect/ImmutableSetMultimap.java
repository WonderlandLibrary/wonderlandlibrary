// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.j2objc.annotations.Weak;
import java.util.Arrays;
import java.util.Set;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.MoreObjects;
import java.util.Iterator;
import com.google.common.annotations.Beta;
import java.util.Collection;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.function.Function;
import com.google.common.annotations.GwtIncompatible;
import java.util.Map;
import com.google.j2objc.annotations.RetainedWith;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements SetMultimap<K, V>
{
    private final transient ImmutableSet<V> emptySet;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient ImmutableSetMultimap<V, K> inverse;
    @LazyInit
    @CheckForNull
    @RetainedWith
    private transient ImmutableSet<Map.Entry<K, V>> entries;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction) {
        return CollectCollectors.toImmutableSetMultimap(keyFunction, valueFunction);
    }
    
    public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
        return CollectCollectors.flatteningToImmutableSetMultimap(keyFunction, valuesFunction);
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of() {
        return (ImmutableSetMultimap<K, V>)EmptyImmutableSetMultimap.INSTANCE;
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        return builder.build();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        final Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        builder.put(k5, v5);
        return builder.build();
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableSetMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap) {
        return copyOf(multimap, (Comparator<? super V>)null);
    }
    
    private static <K, V> ImmutableSetMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap, @CheckForNull final Comparator<? super V> valueComparator) {
        Preconditions.checkNotNull(multimap);
        if (multimap.isEmpty() && valueComparator == null) {
            return of();
        }
        if (multimap instanceof ImmutableSetMultimap) {
            final ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap<K, V>)(ImmutableSetMultimap)multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        return fromMapEntries((Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet(), valueComparator);
    }
    
    @Beta
    public static <K, V> ImmutableSetMultimap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        return new Builder<K, V>().putAll(entries).build();
    }
    
    static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(final Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, @CheckForNull final Comparator<? super V> valueComparator) {
        if (mapEntries.isEmpty()) {
            return of();
        }
        final ImmutableMap.Builder<K, ImmutableSet<V>> builder = new ImmutableMap.Builder<K, ImmutableSet<V>>(mapEntries.size());
        int size = 0;
        for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : mapEntries) {
            final K key = (K)entry.getKey();
            final Collection<? extends V> values = (Collection<? extends V>)entry.getValue();
            final ImmutableSet<V> set = valueSet(valueComparator, values);
            if (!set.isEmpty()) {
                builder.put(key, set);
                size += set.size();
            }
        }
        return new ImmutableSetMultimap<K, V>(builder.build(), size, valueComparator);
    }
    
    ImmutableSetMultimap(final ImmutableMap<K, ImmutableSet<V>> map, final int size, @CheckForNull final Comparator<? super V> valueComparator) {
        super((ImmutableMap<K, ? extends ImmutableCollection<Object>>)map, size);
        this.emptySet = emptySet(valueComparator);
    }
    
    @Override
    public ImmutableSet<V> get(final K key) {
        final ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
        return MoreObjects.firstNonNull(set, this.emptySet);
    }
    
    @Override
    public ImmutableSetMultimap<V, K> inverse() {
        final ImmutableSetMultimap<V, K> result = this.inverse;
        return (result == null) ? (this.inverse = this.invert()) : result;
    }
    
    private ImmutableSetMultimap<V, K> invert() {
        final Builder<V, K> builder = builder();
        for (final Map.Entry<K, V> entry : this.entries()) {
            builder.put(entry.getValue(), entry.getKey());
        }
        final ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
        invertedMultimap.inverse = (ImmutableSetMultimap<V, K>)this;
        return invertedMultimap;
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final ImmutableSet<V> removeAll(@CheckForNull final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final ImmutableSet<V> replaceValues(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableSet<Map.Entry<K, V>> entries() {
        final ImmutableSet<Map.Entry<K, V>> result = this.entries;
        return (result == null) ? (this.entries = (ImmutableSet<Map.Entry<K, V>>)new EntrySet((ImmutableSetMultimap<Object, Object>)this)) : result;
    }
    
    private static <V> ImmutableSet<V> valueSet(@CheckForNull final Comparator<? super V> valueComparator, final Collection<? extends V> values) {
        return (ImmutableSet<V>)((valueComparator == null) ? ImmutableSet.copyOf((Collection<?>)values) : ImmutableSortedSet.copyOf((Comparator<? super Object>)valueComparator, (Collection<?>)values));
    }
    
    private static <V> ImmutableSet<V> emptySet(@CheckForNull final Comparator<? super V> valueComparator) {
        return (ImmutableSet<V>)((valueComparator == null) ? ImmutableSet.of() : ImmutableSortedSet.emptySet((Comparator<? super Object>)valueComparator));
    }
    
    private static <V> ImmutableSet.Builder<V> valuesBuilder(@CheckForNull final Comparator<? super V> valueComparator) {
        return (valueComparator == null) ? new ImmutableSet.Builder<V>() : new ImmutableSortedSet.Builder<V>(valueComparator);
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @CheckForNull
    Comparator<? super V> valueComparator() {
        return (this.emptySet instanceof ImmutableSortedSet) ? ((ImmutableSortedSet)this.emptySet).comparator() : null;
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final Comparator<Object> valueComparator = (Comparator<Object>)stream.readObject();
        final int keyCount = stream.readInt();
        if (keyCount < 0) {
            throw new InvalidObjectException(new StringBuilder(29).append("Invalid key count ").append(keyCount).toString());
        }
        final ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
        int tmpSize = 0;
        for (int i = 0; i < keyCount; ++i) {
            final Object key = stream.readObject();
            final int valueCount = stream.readInt();
            if (valueCount <= 0) {
                throw new InvalidObjectException(new StringBuilder(31).append("Invalid value count ").append(valueCount).toString());
            }
            final ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
            for (int j = 0; j < valueCount; ++j) {
                valuesBuilder.add(stream.readObject());
            }
            final ImmutableSet<Object> valueSet = valuesBuilder.build();
            if (valueSet.size() != valueCount) {
                final String value = String.valueOf(key);
                throw new InvalidObjectException(new StringBuilder(40 + String.valueOf(value).length()).append("Duplicate key-value pairs exist for key ").append(value).toString());
            }
            builder.put(key, valueSet);
            tmpSize += valueCount;
        }
        ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
        try {
            tmpMap = builder.build();
        }
        catch (IllegalArgumentException e) {
            throw (InvalidObjectException)new InvalidObjectException(e.getMessage()).initCause(e);
        }
        FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
        FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
        SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
    }
    
    public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V>
    {
        @Override
        Collection<V> newMutableValueCollection() {
            return (Collection<V>)Platform.preservesInsertionOrderOnAddsSet();
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
        @Beta
        @Override
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            super.putAll(entries);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final K key, final Iterable<? extends V> values) {
            super.putAll(key, values);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final K key, final V... values) {
            return this.putAll(key, (Iterable<? extends V>)Arrays.asList(values));
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> putAll(final Multimap<? extends K, ? extends V> multimap) {
            for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                this.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        Builder<K, V> combine(final ImmutableMultimap.Builder<K, V> other) {
            super.combine(other);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> orderKeysBy(final Comparator<? super K> keyComparator) {
            super.orderKeysBy(keyComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<K, V> orderValuesBy(final Comparator<? super V> valueComparator) {
            super.orderValuesBy(valueComparator);
            return this;
        }
        
        @Override
        public ImmutableSetMultimap<K, V> build() {
            Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
            if (this.keyComparator != null) {
                mapEntries = Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
            }
            return ImmutableSetMultimap.fromMapEntries((Collection<? extends Map.Entry<? extends K, ? extends Collection<? extends V>>>)mapEntries, this.valueComparator);
        }
    }
    
    private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>>
    {
        @Weak
        private final transient ImmutableSetMultimap<K, V> multimap;
        
        EntrySet(final ImmutableSetMultimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        public boolean contains(@CheckForNull final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
                return this.multimap.containsEntry(entry.getKey(), entry.getValue());
            }
            return false;
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
    }
    
    @GwtIncompatible
    private static final class SetFieldSettersHolder
    {
        static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER;
        
        static {
            EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
        }
    }
}
