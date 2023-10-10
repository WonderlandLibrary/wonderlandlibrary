/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.EmptyImmutableSetMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Serialization;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public class ImmutableSetMultimap<K, V>
extends ImmutableMultimap<K, V>
implements SetMultimap<K, V> {
    private final transient ImmutableSet<V> emptySet;
    private transient ImmutableSetMultimap<V, K> inverse;
    private transient ImmutableSet<Map.Entry<K, V>> entries;
    @GwtIncompatible(value="not needed in emulated source.")
    private static final long serialVersionUID = 0L;

    public static <K, V> ImmutableSetMultimap<K, V> of() {
        return EmptyImmutableSetMultimap.INSTANCE;
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        builder.put((Object)k1, (Object)v1);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        builder.put((Object)k1, (Object)v1);
        builder.put((Object)k2, (Object)v2);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        builder.put((Object)k1, (Object)v1);
        builder.put((Object)k2, (Object)v2);
        builder.put((Object)k3, (Object)v3);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        builder.put((Object)k1, (Object)v1);
        builder.put((Object)k2, (Object)v2);
        builder.put((Object)k3, (Object)v3);
        builder.put((Object)k4, (Object)v4);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        builder.put((Object)k1, (Object)v1);
        builder.put((Object)k2, (Object)v2);
        builder.put((Object)k3, (Object)v3);
        builder.put((Object)k4, (Object)v4);
        builder.put((Object)k5, (Object)v5);
        return builder.build();
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        return ImmutableSetMultimap.copyOf(multimap, null);
    }

    private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, Comparator<? super V> valueComparator) {
        ImmutableSetMultimap kvMultimap;
        Preconditions.checkNotNull(multimap);
        if (multimap.isEmpty() && valueComparator == null) {
            return ImmutableSetMultimap.of();
        }
        if (multimap instanceof ImmutableSetMultimap && !(kvMultimap = (ImmutableSetMultimap)multimap).isPartialView()) {
            return kvMultimap;
        }
        ImmutableMap.Builder<K, ImmutableSet<? extends V>> builder = ImmutableMap.builder();
        int size = 0;
        for (Map.Entry<K, Collection<V>> entry : multimap.asMap().entrySet()) {
            K key = entry.getKey();
            Collection<? extends V> values = entry.getValue();
            ImmutableSet<V> set = ImmutableSetMultimap.valueSet(valueComparator, values);
            if (set.isEmpty()) continue;
            builder.put(key, set);
            size += set.size();
        }
        return new ImmutableSetMultimap(builder.build(), size, valueComparator);
    }

    ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, @Nullable Comparator<? super V> valueComparator) {
        super(map, size);
        this.emptySet = ImmutableSetMultimap.emptySet(valueComparator);
    }

    @Override
    public ImmutableSet<V> get(@Nullable K key) {
        ImmutableSet set = (ImmutableSet)this.map.get(key);
        return Objects.firstNonNull(set, this.emptySet);
    }

    @Override
    public ImmutableSetMultimap<V, K> inverse() {
        ImmutableSetMultimap<K, V> result = this.inverse;
        return result == null ? (this.inverse = this.invert()) : result;
    }

    private ImmutableSetMultimap<V, K> invert() {
        Builder<K, V> builder = ImmutableSetMultimap.builder();
        for (Map.Entry entry : this.entries()) {
            builder.put(entry.getValue(), entry.getKey());
        }
        ImmutableMultimap invertedMultimap = builder.build();
        ((ImmutableSetMultimap)invertedMultimap).inverse = this;
        return invertedMultimap;
    }

    @Override
    @Deprecated
    public ImmutableSet<V> removeAll(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableSet<Map.Entry<K, V>> entries() {
        ImmutableSet<Map.Entry<K, V>> result = this.entries;
        return result == null ? (this.entries = new EntrySet(this)) : result;
    }

    private static <V> ImmutableSet<V> valueSet(@Nullable Comparator<? super V> valueComparator, Collection<? extends V> values) {
        return valueComparator == null ? ImmutableSet.copyOf(values) : ImmutableSortedSet.copyOf(valueComparator, values);
    }

    private static <V> ImmutableSet<V> emptySet(@Nullable Comparator<? super V> valueComparator) {
        return valueComparator == null ? ImmutableSet.of() : ImmutableSortedSet.emptySet(valueComparator);
    }

    @GwtIncompatible(value="java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap(this, stream);
    }

    @Nullable
    Comparator<? super V> valueComparator() {
        return this.emptySet instanceof ImmutableSortedSet ? ((ImmutableSortedSet)this.emptySet).comparator() : null;
    }

    @GwtIncompatible(value="java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        ImmutableMap tmpMap;
        stream.defaultReadObject();
        Comparator valueComparator = (Comparator)stream.readObject();
        int keyCount = stream.readInt();
        if (keyCount < 0) {
            throw new InvalidObjectException("Invalid key count " + keyCount);
        }
        ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
        int tmpSize = 0;
        for (int i2 = 0; i2 < keyCount; ++i2) {
            Object key = stream.readObject();
            int valueCount = stream.readInt();
            if (valueCount <= 0) {
                throw new InvalidObjectException("Invalid value count " + valueCount);
            }
            Object[] array = new Object[valueCount];
            for (int j2 = 0; j2 < valueCount; ++j2) {
                array[j2] = stream.readObject();
            }
            ImmutableSet<Object> valueSet = ImmutableSetMultimap.valueSet(valueComparator, Arrays.asList(array));
            if (valueSet.size() != array.length) {
                throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
            }
            builder.put(key, valueSet);
            tmpSize += valueCount;
        }
        try {
            tmpMap = builder.build();
        }
        catch (IllegalArgumentException e2) {
            throw (InvalidObjectException)new InvalidObjectException(e2.getMessage()).initCause(e2);
        }
        ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set((ImmutableMultimap)this, tmpMap);
        ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set((ImmutableMultimap)this, tmpSize);
        ImmutableMultimap.FieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, ImmutableSetMultimap.emptySet(valueComparator));
    }

    private static final class EntrySet<K, V>
    extends ImmutableSet<Map.Entry<K, V>> {
        private final transient ImmutableSetMultimap<K, V> multimap;

        EntrySet(ImmutableSetMultimap<K, V> multimap) {
            this.multimap = multimap;
        }

        @Override
        public boolean contains(@Nullable Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)object;
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

    public static final class Builder<K, V>
    extends ImmutableMultimap.Builder<K, V> {
        public Builder() {
            this.builderMultimap = new BuilderMultimap();
        }

        @Override
        public Builder<K, V> put(K key, V value) {
            this.builderMultimap.put(Preconditions.checkNotNull(key), Preconditions.checkNotNull(value));
            return this;
        }

        @Override
        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            this.builderMultimap.put(Preconditions.checkNotNull(entry.getKey()), Preconditions.checkNotNull(entry.getValue()));
            return this;
        }

        @Override
        public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
            Collection collection = this.builderMultimap.get(Preconditions.checkNotNull(key));
            for (V value : values) {
                collection.add(Preconditions.checkNotNull(value));
            }
            return this;
        }

        @Override
        public Builder<K, V> putAll(K key, V ... values) {
            return this.putAll((Object)key, Arrays.asList(values));
        }

        @Override
        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Map.Entry<K, Collection<V>> entry : multimap.asMap().entrySet()) {
                this.putAll((Object)entry.getKey(), entry.getValue());
            }
            return this;
        }

        @Override
        public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
            this.keyComparator = Preconditions.checkNotNull(keyComparator);
            return this;
        }

        @Override
        public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
            super.orderValuesBy(valueComparator);
            return this;
        }

        @Override
        public ImmutableSetMultimap<K, V> build() {
            if (this.keyComparator != null) {
                BuilderMultimap sortedCopy = new BuilderMultimap();
                ArrayList entries = Lists.newArrayList(this.builderMultimap.asMap().entrySet());
                Collections.sort(entries, Ordering.from(this.keyComparator).onKeys());
                for (Map.Entry entry : entries) {
                    sortedCopy.putAll(entry.getKey(), (Iterable)entry.getValue());
                }
                this.builderMultimap = sortedCopy;
            }
            return ImmutableSetMultimap.copyOf(this.builderMultimap, this.valueComparator);
        }
    }

    private static class BuilderMultimap<K, V>
    extends AbstractMapBasedMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        BuilderMultimap() {
            super(new LinkedHashMap());
        }

        @Override
        Collection<V> createCollection() {
            return Sets.newLinkedHashSet();
        }
    }
}

