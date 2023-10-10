// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.j2objc.annotations.Weak;
import com.google.common.annotations.GwtIncompatible;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import com.google.errorprone.annotations.DoNotMock;
import com.google.common.base.Preconditions;
import java.util.function.BiConsumer;
import java.util.Spliterator;
import java.util.Objects;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import com.google.errorprone.annotations.DoNotCall;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.Beta;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> extends BaseImmutableMultimap<K, V> implements Serializable
{
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;
    private static final long serialVersionUID = 0L;
    
    public static <K, V> ImmutableMultimap<K, V> of() {
        return (ImmutableMultimap<K, V>)ImmutableListMultimap.of();
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1) {
        return ImmutableListMultimap.of(k1, v1);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return ImmutableListMultimap.of(k1, v1, k2, v2);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }
    
    public static <K, V> ImmutableMultimap<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> ImmutableMultimap<K, V> copyOf(final Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            final ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap<K, V>)(ImmutableMultimap)multimap;
            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }
        return (ImmutableMultimap<K, V>)ImmutableListMultimap.copyOf((Multimap<?, ?>)multimap);
    }
    
    @Beta
    public static <K, V> ImmutableMultimap<K, V> copyOf(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        return (ImmutableMultimap<K, V>)ImmutableListMultimap.copyOf((Iterable<? extends Map.Entry<?, ?>>)entries);
    }
    
    ImmutableMultimap(final ImmutableMap<K, ? extends ImmutableCollection<V>> map, final int size) {
        this.map = map;
        this.size = size;
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public ImmutableCollection<V> removeAll(@CheckForNull final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public ImmutableCollection<V> replaceValues(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract ImmutableCollection<V> get(final K p0);
    
    public abstract ImmutableMultimap<V, K> inverse();
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean putAll(final K key, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @DoNotCall("Always throws UnsupportedOperationException")
    @Override
    public final boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
        throw new UnsupportedOperationException();
    }
    
    boolean isPartialView() {
        return this.map.isPartialView();
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return value != null && super.containsValue(value);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }
    
    @Override
    Set<K> createKeySet() {
        throw new AssertionError((Object)"unreachable");
    }
    
    @Override
    public ImmutableMap<K, Collection<V>> asMap() {
        return (ImmutableMap<K, Collection<V>>)this.map;
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    public ImmutableCollection<Map.Entry<K, V>> entries() {
        return (ImmutableCollection<Map.Entry<K, V>>)(ImmutableCollection)super.entries();
    }
    
    @Override
    ImmutableCollection<Map.Entry<K, V>> createEntries() {
        return (ImmutableCollection<Map.Entry<K, V>>)new EntryCollection((ImmutableMultimap<Object, Object>)this);
    }
    
    @Override
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
        return new UnmodifiableIterator<Map.Entry<K, V>>() {
            final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> asMapItr = ImmutableMultimap.this.map.entrySet().iterator();
            @CheckForNull
            K currentKey = null;
            Iterator<V> valueItr = Iterators.emptyIterator();
            
            @Override
            public boolean hasNext() {
                return this.valueItr.hasNext() || this.asMapItr.hasNext();
            }
            
            @Override
            public Map.Entry<K, V> next() {
                if (!this.valueItr.hasNext()) {
                    final Map.Entry<K, ? extends ImmutableCollection<V>> entry = (Map.Entry<K, ? extends ImmutableCollection<V>>)this.asMapItr.next();
                    this.currentKey = entry.getKey();
                    this.valueItr = (Iterator<V>)((ImmutableCollection)entry.getValue()).iterator();
                }
                return Maps.immutableEntry((K)Objects.requireNonNull((K)this.currentKey), this.valueItr.next());
            }
        };
    }
    
    @Override
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        final K key;
        final Collection<V> valueCollection;
        return CollectSpliterators.flatMap(this.asMap().entrySet().spliterator(), keyToValueCollectionEntry -> {
            key = keyToValueCollectionEntry.getKey();
            valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue();
            return (Spliterator<Map.Entry<K, V>>)CollectSpliterators.map(valueCollection.spliterator(), value -> Maps.immutableEntry(key, value));
        }, 0x40 | ((this instanceof SetMultimap) ? 1 : 0), this.size());
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        this.asMap().forEach((key, valueCollection) -> valueCollection.forEach(value -> action.accept((Object)key, (Object)value)));
    }
    
    @Override
    public ImmutableMultiset<K> keys() {
        return (ImmutableMultiset<K>)(ImmutableMultiset)super.keys();
    }
    
    @Override
    ImmutableMultiset<K> createKeys() {
        return new Keys();
    }
    
    @Override
    public ImmutableCollection<V> values() {
        return (ImmutableCollection<V>)(ImmutableCollection)super.values();
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)new Values((ImmutableMultimap<Object, Object>)this);
    }
    
    @Override
    UnmodifiableIterator<V> valueIterator() {
        return new UnmodifiableIterator<V>() {
            Iterator<? extends ImmutableCollection<V>> valueCollectionItr = ImmutableMultimap.this.map.values().iterator();
            Iterator<V> valueItr = Iterators.emptyIterator();
            
            @Override
            public boolean hasNext() {
                return this.valueItr.hasNext() || this.valueCollectionItr.hasNext();
            }
            
            @Override
            public V next() {
                if (!this.valueItr.hasNext()) {
                    this.valueItr = (Iterator<V>)((ImmutableCollection)this.valueCollectionItr.next()).iterator();
                }
                return this.valueItr.next();
            }
        };
    }
    
    @DoNotMock
    public static class Builder<K, V>
    {
        final Map<K, Collection<V>> builderMap;
        @CheckForNull
        Comparator<? super K> keyComparator;
        @CheckForNull
        Comparator<? super V> valueComparator;
        
        public Builder() {
            this.builderMap = Platform.preservesInsertionOrderOnPutsMap();
        }
        
        Collection<V> newMutableValueCollection() {
            return new ArrayList<V>();
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final K key, final V value) {
            CollectPreconditions.checkEntryNotNull(key, value);
            Collection<V> valueCollection = this.builderMap.get(key);
            if (valueCollection == null) {
                this.builderMap.put(key, valueCollection = this.newMutableValueCollection());
            }
            valueCollection.add(value);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final Map.Entry<? extends K, ? extends V> entry) {
            return (Builder<K, V>)this.put(entry.getKey(), entry.getValue());
        }
        
        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            for (final Map.Entry<? extends K, ? extends V> entry : entries) {
                this.put(entry);
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(final K key, final Iterable<? extends V> values) {
            if (key == null) {
                final String original = "null key in entry: null=";
                final String value2 = String.valueOf(Iterables.toString(values));
                throw new NullPointerException((value2.length() != 0) ? original.concat(value2) : new String(original));
            }
            Collection<V> valueCollection = this.builderMap.get(key);
            if (valueCollection != null) {
                for (final V value : values) {
                    CollectPreconditions.checkEntryNotNull(key, value);
                    valueCollection.add(value);
                }
                return this;
            }
            final Iterator<? extends V> valuesItr = values.iterator();
            if (!valuesItr.hasNext()) {
                return this;
            }
            valueCollection = this.newMutableValueCollection();
            while (valuesItr.hasNext()) {
                final V value = (V)valuesItr.next();
                CollectPreconditions.checkEntryNotNull(key, value);
                valueCollection.add(value);
            }
            this.builderMap.put(key, valueCollection);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(final K key, final V... values) {
            return this.putAll(key, (Iterable<? extends V>)Arrays.asList(values));
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(final Multimap<? extends K, ? extends V> multimap) {
            for (final Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                this.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
            }
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> orderKeysBy(final Comparator<? super K> keyComparator) {
            this.keyComparator = Preconditions.checkNotNull(keyComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> orderValuesBy(final Comparator<? super V> valueComparator) {
            this.valueComparator = Preconditions.checkNotNull(valueComparator);
            return this;
        }
        
        @CanIgnoreReturnValue
        Builder<K, V> combine(final Builder<K, V> other) {
            for (final Map.Entry<K, Collection<V>> entry : other.builderMap.entrySet()) {
                this.putAll(entry.getKey(), (Iterable<? extends V>)entry.getValue());
            }
            return this;
        }
        
        public ImmutableMultimap<K, V> build() {
            Collection<Map.Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
            if (this.keyComparator != null) {
                mapEntries = Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(mapEntries);
            }
            return (ImmutableMultimap<K, V>)ImmutableListMultimap.fromMapEntries((Collection<? extends Map.Entry<?, ? extends Collection<?>>>)mapEntries, (Comparator<? super Object>)this.valueComparator);
        }
    }
    
    @GwtIncompatible
    static class FieldSettersHolder
    {
        static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER;
        static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER;
        
        static {
            MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
            SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
        }
    }
    
    private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>>
    {
        @Weak
        final ImmutableMultimap<K, V> multimap;
        private static final long serialVersionUID = 0L;
        
        EntryCollection(final ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }
        
        @Override
        boolean isPartialView() {
            return this.multimap.isPartialView();
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
                return this.multimap.containsEntry(entry.getKey(), entry.getValue());
            }
            return false;
        }
    }
    
    class Keys extends ImmutableMultiset<K>
    {
        @Override
        public boolean contains(@CheckForNull final Object object) {
            return ImmutableMultimap.this.containsKey(object);
        }
        
        @Override
        public int count(@CheckForNull final Object element) {
            final Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
            return (values == null) ? 0 : values.size();
        }
        
        @Override
        public ImmutableSet<K> elementSet() {
            return ImmutableMultimap.this.keySet();
        }
        
        @Override
        public int size() {
            return ImmutableMultimap.this.size();
        }
        
        @Override
        Multiset.Entry<K> getEntry(final int index) {
            final Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
            return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
        
        @GwtIncompatible
        @Override
        Object writeReplace() {
            return new KeysSerializedForm(ImmutableMultimap.this);
        }
    }
    
    @GwtIncompatible
    private static final class KeysSerializedForm implements Serializable
    {
        final ImmutableMultimap<?, ?> multimap;
        
        KeysSerializedForm(final ImmutableMultimap<?, ?> multimap) {
            this.multimap = multimap;
        }
        
        Object readResolve() {
            return this.multimap.keys();
        }
    }
    
    private static final class Values<K, V> extends ImmutableCollection<V>
    {
        @Weak
        private final transient ImmutableMultimap<K, V> multimap;
        private static final long serialVersionUID = 0L;
        
        Values(final ImmutableMultimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        public boolean contains(@CheckForNull final Object object) {
            return this.multimap.containsValue(object);
        }
        
        @Override
        public UnmodifiableIterator<V> iterator() {
            return this.multimap.valueIterator();
        }
        
        @GwtIncompatible
        @Override
        int copyIntoArray(final Object[] dst, int offset) {
            for (final ImmutableCollection<V> valueCollection : this.multimap.map.values()) {
                offset = valueCollection.copyIntoArray(dst, offset);
            }
            return offset;
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
}
