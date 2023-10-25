/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.AbstractMapBasedMultimap$WrappedCollection.WrappedIterator
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.CollectSpliterators;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMapBasedMultimap<K, V>
extends AbstractMultimap<K, V>
implements Serializable {
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;
    private static final long serialVersionUID = 2447537837011683357L;

    protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }

    final void setMap(Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        for (Collection<Collection<V>> values : map.values()) {
            Preconditions.checkArgument(!values.isEmpty());
            this.totalSize += values.size();
        }
    }

    Collection<V> createUnmodifiableEmptyCollection() {
        return AbstractMapBasedMultimap.unmodifiableCollectionSubclass(this.createCollection());
    }

    abstract Collection<V> createCollection();

    Collection<V> createCollection(@Nullable K key) {
        return this.createCollection();
    }

    Map<K, Collection<V>> backingMap() {
        return this.map;
    }

    @Override
    public int size() {
        return this.totalSize;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean put(@Nullable K key, @Nullable V value) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            if (collection.add(value)) {
                ++this.totalSize;
                this.map.put(key, collection);
                return true;
            }
            throw new AssertionError((Object)"New Collection violated the Collection spec");
        }
        if (collection.add(value)) {
            ++this.totalSize;
            return true;
        }
        return false;
    }

    private Collection<V> getOrCreateCollection(@Nullable K key) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            this.map.put(key, collection);
        }
        return collection;
    }

    @Override
    public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
        Iterator<V> iterator = values.iterator();
        if (!iterator.hasNext()) {
            return this.removeAll(key);
        }
        Collection<V> collection = this.getOrCreateCollection(key);
        Collection<V> oldValues = this.createCollection();
        oldValues.addAll(collection);
        this.totalSize -= collection.size();
        collection.clear();
        while (iterator.hasNext()) {
            if (!collection.add(iterator.next())) continue;
            ++this.totalSize;
        }
        return AbstractMapBasedMultimap.unmodifiableCollectionSubclass(oldValues);
    }

    @Override
    public Collection<V> removeAll(@Nullable Object key) {
        Collection<V> collection = this.map.remove(key);
        if (collection == null) {
            return this.createUnmodifiableEmptyCollection();
        }
        Collection<V> output = this.createCollection();
        output.addAll(collection);
        this.totalSize -= collection.size();
        collection.clear();
        return AbstractMapBasedMultimap.unmodifiableCollectionSubclass(output);
    }

    static <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
        if (collection instanceof NavigableSet) {
            return Sets.unmodifiableNavigableSet((NavigableSet)collection);
        }
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet)collection);
        }
        if (collection instanceof Set) {
            return Collections.unmodifiableSet((Set)collection);
        }
        if (collection instanceof List) {
            return Collections.unmodifiableList((List)collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    @Override
    public void clear() {
        for (Collection<V> collection : this.map.values()) {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }

    @Override
    public Collection<V> get(@Nullable K key) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
        }
        return this.wrapCollection(key, collection);
    }

    Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
        if (collection instanceof NavigableSet) {
            return new WrappedNavigableSet(key, (NavigableSet)collection, null);
        }
        if (collection instanceof SortedSet) {
            return new WrappedSortedSet(key, (SortedSet)collection, null);
        }
        if (collection instanceof Set) {
            return new WrappedSet(key, (Set)collection);
        }
        if (collection instanceof List) {
            return this.wrapList(key, (List)collection, null);
        }
        return new WrappedCollection(key, collection, null);
    }

    private List<V> wrapList(@Nullable K key, List<V> list, @Nullable WrappedCollection ancestor) {
        return list instanceof RandomAccess ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
    }

    private static <E> Iterator<E> iteratorOrListIterator(Collection<E> collection) {
        return collection instanceof List ? ((List)collection).listIterator() : collection.iterator();
    }

    @Override
    Set<K> createKeySet() {
        if (this.map instanceof NavigableMap) {
            return new NavigableKeySet((NavigableMap)this.map);
        }
        if (this.map instanceof SortedMap) {
            return new SortedKeySet((SortedMap)this.map);
        }
        return new KeySet(this.map);
    }

    private void removeValuesForKey(Object key) {
        Collection<V> collection = Maps.safeRemove(this.map, key);
        if (collection != null) {
            int count = collection.size();
            collection.clear();
            this.totalSize -= count;
        }
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }

    @Override
    Iterator<V> valueIterator() {
        return new Itr<V>(){

            @Override
            V output(K key, V value) {
                return value;
            }
        };
    }

    @Override
    Spliterator<V> valueSpliterator() {
        return CollectSpliterators.flatMap(this.map.values().spliterator(), Collection::spliterator, 64, this.size());
    }

    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Itr<Map.Entry<K, V>>(){

            @Override
            Map.Entry<K, V> output(K key, V value) {
                return Maps.immutableEntry(key, value);
            }
        };
    }

    @Override
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return CollectSpliterators.flatMap(this.map.entrySet().spliterator(), keyToValueCollectionEntry -> {
            Object key = keyToValueCollectionEntry.getKey();
            Collection valueCollection = (Collection)keyToValueCollectionEntry.getValue();
            return CollectSpliterators.map(valueCollection.spliterator(), value -> Maps.immutableEntry(key, value));
        }, 64, this.size());
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        this.map.forEach((key, valueCollection) -> valueCollection.forEach((? super T value) -> action.accept((Object)key, (Object)value)));
    }

    @Override
    Map<K, Collection<V>> createAsMap() {
        if (this.map instanceof NavigableMap) {
            return new NavigableAsMap((NavigableMap)this.map);
        }
        if (this.map instanceof SortedMap) {
            return new SortedAsMap((SortedMap)this.map);
        }
        return new AsMap(this.map);
    }

    class NavigableAsMap
    extends SortedAsMap
    implements NavigableMap<K, Collection<V>> {
        NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
            super(submap);
        }

        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap)super.sortedMap();
        }

        @Override
        public Map.Entry<K, Collection<V>> lowerEntry(K key) {
            Map.Entry entry = this.sortedMap().lowerEntry(key);
            return entry == null ? null : this.wrapEntry(entry);
        }

        @Override
        public K lowerKey(K key) {
            return this.sortedMap().lowerKey(key);
        }

        @Override
        public Map.Entry<K, Collection<V>> floorEntry(K key) {
            Map.Entry entry = this.sortedMap().floorEntry(key);
            return entry == null ? null : this.wrapEntry(entry);
        }

        @Override
        public K floorKey(K key) {
            return this.sortedMap().floorKey(key);
        }

        @Override
        public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
            Map.Entry entry = this.sortedMap().ceilingEntry(key);
            return entry == null ? null : this.wrapEntry(entry);
        }

        @Override
        public K ceilingKey(K key) {
            return this.sortedMap().ceilingKey(key);
        }

        @Override
        public Map.Entry<K, Collection<V>> higherEntry(K key) {
            Map.Entry entry = this.sortedMap().higherEntry(key);
            return entry == null ? null : this.wrapEntry(entry);
        }

        @Override
        public K higherKey(K key) {
            return this.sortedMap().higherKey(key);
        }

        @Override
        public Map.Entry<K, Collection<V>> firstEntry() {
            Map.Entry entry = this.sortedMap().firstEntry();
            return entry == null ? null : this.wrapEntry(entry);
        }

        @Override
        public Map.Entry<K, Collection<V>> lastEntry() {
            Map.Entry entry = this.sortedMap().lastEntry();
            return entry == null ? null : this.wrapEntry(entry);
        }

        @Override
        public Map.Entry<K, Collection<V>> pollFirstEntry() {
            return this.pollAsMapEntry(this.entrySet().iterator());
        }

        @Override
        public Map.Entry<K, Collection<V>> pollLastEntry() {
            return this.pollAsMapEntry(this.descendingMap().entrySet().iterator());
        }

        Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
            if (!entryIterator.hasNext()) {
                return null;
            }
            Map.Entry entry = entryIterator.next();
            Collection output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll(entry.getValue());
            entryIterator.remove();
            return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.unmodifiableCollectionSubclass(output));
        }

        @Override
        public NavigableMap<K, Collection<V>> descendingMap() {
            return new NavigableAsMap(this.sortedMap().descendingMap());
        }

        @Override
        public NavigableSet<K> keySet() {
            return (NavigableSet)super.keySet();
        }

        @Override
        NavigableSet<K> createKeySet() {
            return new NavigableKeySet(this.sortedMap());
        }

        @Override
        public NavigableSet<K> navigableKeySet() {
            return this.keySet();
        }

        @Override
        public NavigableSet<K> descendingKeySet() {
            return this.descendingMap().navigableKeySet();
        }

        @Override
        public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }

        @Override
        public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return new NavigableAsMap(this.sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
        }

        @Override
        public NavigableMap<K, Collection<V>> headMap(K toKey) {
            return this.headMap(toKey, false);
        }

        @Override
        public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) {
            return new NavigableAsMap(this.sortedMap().headMap(toKey, inclusive));
        }

        @Override
        public NavigableMap<K, Collection<V>> tailMap(K fromKey) {
            return this.tailMap(fromKey, true);
        }

        @Override
        public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) {
            return new NavigableAsMap(this.sortedMap().tailMap(fromKey, inclusive));
        }
    }

    private class SortedAsMap
    extends AsMap
    implements SortedMap<K, Collection<V>> {
        SortedSet<K> sortedKeySet;

        SortedAsMap(SortedMap<K, Collection<V>> submap) {
            super(submap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap)this.submap;
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }

        @Override
        public K firstKey() {
            return this.sortedMap().firstKey();
        }

        @Override
        public K lastKey() {
            return this.sortedMap().lastKey();
        }

        @Override
        public SortedMap<K, Collection<V>> headMap(K toKey) {
            return new SortedAsMap(this.sortedMap().headMap(toKey));
        }

        @Override
        public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
            return new SortedAsMap(this.sortedMap().subMap(fromKey, toKey));
        }

        @Override
        public SortedMap<K, Collection<V>> tailMap(K fromKey) {
            return new SortedAsMap(this.sortedMap().tailMap(fromKey));
        }

        @Override
        public SortedSet<K> keySet() {
            SortedSet result = this.sortedKeySet;
            return result == null ? (this.sortedKeySet = this.createKeySet()) : result;
        }

        @Override
        SortedSet<K> createKeySet() {
            return new SortedKeySet(this.sortedMap());
        }
    }

    private class AsMap
    extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
        final transient Map<K, Collection<V>> submap;

        AsMap(Map<K, Collection<V>> submap) {
            this.submap = submap;
        }

        @Override
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new AsMapEntries();
        }

        @Override
        public boolean containsKey(Object key) {
            return Maps.safeContainsKey(this.submap, key);
        }

        @Override
        public Collection<V> get(Object key) {
            Collection collection = Maps.safeGet(this.submap, key);
            if (collection == null) {
                return null;
            }
            Object k = key;
            return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
        }

        @Override
        public Set<K> keySet() {
            return AbstractMapBasedMultimap.this.keySet();
        }

        @Override
        public int size() {
            return this.submap.size();
        }

        @Override
        public Collection<V> remove(Object key) {
            Collection collection = this.submap.remove(key);
            if (collection == null) {
                return null;
            }
            Collection output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll(collection);
            AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - collection.size();
            collection.clear();
            return output;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return this == object || this.submap.equals(object);
        }

        @Override
        public int hashCode() {
            return this.submap.hashCode();
        }

        @Override
        public String toString() {
            return this.submap.toString();
        }

        @Override
        public void clear() {
            if (this.submap == AbstractMapBasedMultimap.this.map) {
                AbstractMapBasedMultimap.this.clear();
            } else {
                Iterators.clear(new AsMapIterator());
            }
        }

        Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
            Object key = entry.getKey();
            return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
        }

        class AsMapIterator
        implements Iterator<Map.Entry<K, Collection<V>>> {
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
            Collection<V> collection;

            AsMapIterator() {
                this.delegateIterator = AsMap.this.submap.entrySet().iterator();
            }

            @Override
            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }

            @Override
            public Map.Entry<K, Collection<V>> next() {
                Map.Entry entry = this.delegateIterator.next();
                this.collection = entry.getValue();
                return AsMap.this.wrapEntry(entry);
            }

            @Override
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - this.collection.size();
                this.collection.clear();
            }
        }

        class AsMapEntries
        extends Maps.EntrySet<K, Collection<V>> {
            AsMapEntries() {
            }

            @Override
            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            @Override
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new AsMapIterator();
            }

            @Override
            public Spliterator<Map.Entry<K, Collection<V>>> spliterator() {
                return CollectSpliterators.map(AsMap.this.submap.entrySet().spliterator(), AsMap.this::wrapEntry);
            }

            @Override
            public boolean contains(Object o) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), o);
            }

            @Override
            public boolean remove(Object o) {
                if (!this.contains(o)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry)o;
                AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }
    }

    private abstract class Itr<T>
    implements Iterator<T> {
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        K key;
        Collection<V> collection;
        Iterator<V> valueIterator;

        Itr() {
            this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
            this.key = null;
            this.collection = null;
            this.valueIterator = Iterators.emptyModifiableIterator();
        }

        abstract T output(K var1, V var2);

        @Override
        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }

        @Override
        public T next() {
            if (!this.valueIterator.hasNext()) {
                Map.Entry mapEntry = this.keyIterator.next();
                this.key = mapEntry.getKey();
                this.collection = mapEntry.getValue();
                this.valueIterator = this.collection.iterator();
            }
            return this.output(this.key, this.valueIterator.next());
        }

        @Override
        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMapBasedMultimap.this.totalSize--;
        }
    }

    class NavigableKeySet
    extends SortedKeySet
    implements NavigableSet<K> {
        NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
            super(subMap);
        }

        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap)super.sortedMap();
        }

        @Override
        public K lower(K k) {
            return this.sortedMap().lowerKey(k);
        }

        @Override
        public K floor(K k) {
            return this.sortedMap().floorKey(k);
        }

        @Override
        public K ceiling(K k) {
            return this.sortedMap().ceilingKey(k);
        }

        @Override
        public K higher(K k) {
            return this.sortedMap().higherKey(k);
        }

        @Override
        public K pollFirst() {
            return Iterators.pollNext(this.iterator());
        }

        @Override
        public K pollLast() {
            return Iterators.pollNext(this.descendingIterator());
        }

        @Override
        public NavigableSet<K> descendingSet() {
            return new NavigableKeySet(this.sortedMap().descendingMap());
        }

        @Override
        public Iterator<K> descendingIterator() {
            return this.descendingSet().iterator();
        }

        @Override
        public NavigableSet<K> headSet(K toElement) {
            return this.headSet((K)toElement, false);
        }

        @Override
        public NavigableSet<K> headSet(K toElement, boolean inclusive) {
            return new NavigableKeySet(this.sortedMap().headMap(toElement, inclusive));
        }

        @Override
        public NavigableSet<K> subSet(K fromElement, K toElement) {
            return this.subSet((K)fromElement, true, (K)toElement, false);
        }

        @Override
        public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
            return new NavigableKeySet(this.sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
        }

        @Override
        public NavigableSet<K> tailSet(K fromElement) {
            return this.tailSet((K)fromElement, true);
        }

        @Override
        public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
            return new NavigableKeySet(this.sortedMap().tailMap(fromElement, inclusive));
        }
    }

    private class SortedKeySet
    extends KeySet
    implements SortedSet<K> {
        SortedKeySet(SortedMap<K, Collection<V>> subMap) {
            super(subMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap)super.map();
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }

        @Override
        public K first() {
            return this.sortedMap().firstKey();
        }

        @Override
        public SortedSet<K> headSet(K toElement) {
            return new SortedKeySet(this.sortedMap().headMap(toElement));
        }

        @Override
        public K last() {
            return this.sortedMap().lastKey();
        }

        @Override
        public SortedSet<K> subSet(K fromElement, K toElement) {
            return new SortedKeySet(this.sortedMap().subMap(fromElement, toElement));
        }

        @Override
        public SortedSet<K> tailSet(K fromElement) {
            return new SortedKeySet(this.sortedMap().tailMap(fromElement));
        }
    }

    private class KeySet
    extends Maps.KeySet<K, Collection<V>> {
        KeySet(Map<K, Collection<V>> subMap) {
            super(subMap);
        }

        @Override
        public Iterator<K> iterator() {
            final Iterator entryIterator = this.map().entrySet().iterator();
            return new Iterator<K>(){
                Map.Entry<K, Collection<V>> entry;

                @Override
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }

                @Override
                public K next() {
                    this.entry = (Map.Entry)entryIterator.next();
                    return this.entry.getKey();
                }

                @Override
                public void remove() {
                    CollectPreconditions.checkRemove(this.entry != null);
                    Collection collection = this.entry.getValue();
                    entryIterator.remove();
                    AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - collection.size();
                    collection.clear();
                }
            };
        }

        @Override
        public Spliterator<K> spliterator() {
            return this.map().keySet().spliterator();
        }

        @Override
        public boolean remove(Object key) {
            int count = 0;
            Collection collection = (Collection)this.map().remove(key);
            if (collection != null) {
                count = collection.size();
                collection.clear();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - count;
            }
            return count > 0;
        }

        @Override
        public void clear() {
            Iterators.clear(this.iterator());
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return this.map().keySet().containsAll(c);
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return this == object || this.map().keySet().equals(object);
        }

        @Override
        public int hashCode() {
            return this.map().keySet().hashCode();
        }
    }

    private class RandomAccessWrappedList
    extends WrappedList
    implements RandomAccess {
        RandomAccessWrappedList(K key, @Nullable List<V> delegate, WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
    }

    private class WrappedList
    extends WrappedCollection
    implements List<V> {
        WrappedList(K key, @Nullable List<V> delegate, WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }

        List<V> getListDelegate() {
            return (List)this.getDelegate();
        }

        @Override
        public boolean addAll(int index, Collection<? extends V> c) {
            if (c.isEmpty()) {
                return false;
            }
            int oldSize = this.size();
            boolean changed = this.getListDelegate().addAll(index, c);
            if (changed) {
                int newSize = this.getDelegate().size();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + (newSize - oldSize);
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }

        @Override
        public V get(int index) {
            this.refreshIfEmpty();
            return this.getListDelegate().get(index);
        }

        @Override
        public V set(int index, V element) {
            this.refreshIfEmpty();
            return this.getListDelegate().set(index, element);
        }

        @Override
        public void add(int index, V element) {
            this.refreshIfEmpty();
            boolean wasEmpty = this.getDelegate().isEmpty();
            this.getListDelegate().add(index, element);
            AbstractMapBasedMultimap.this.totalSize++;
            if (wasEmpty) {
                this.addToMap();
            }
        }

        @Override
        public V remove(int index) {
            this.refreshIfEmpty();
            Object value = this.getListDelegate().remove(index);
            AbstractMapBasedMultimap.this.totalSize--;
            this.removeIfEmpty();
            return value;
        }

        @Override
        public int indexOf(Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().lastIndexOf(o);
        }

        @Override
        public ListIterator<V> listIterator() {
            this.refreshIfEmpty();
            return new WrappedListIterator();
        }

        @Override
        public ListIterator<V> listIterator(int index) {
            this.refreshIfEmpty();
            return new WrappedListIterator(index);
        }

        @Override
        public List<V> subList(int fromIndex, int toIndex) {
            this.refreshIfEmpty();
            return AbstractMapBasedMultimap.this.wrapList(this.getKey(), this.getListDelegate().subList(fromIndex, toIndex), this.getAncestor() == null ? this : this.getAncestor());
        }

        private class WrappedListIterator
        extends WrappedIterator
        implements ListIterator<V> {
            WrappedListIterator() {
            }

            public WrappedListIterator(int index) {
                super(WrappedList.this.getListDelegate().listIterator(index));
            }

            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator)this.getDelegateIterator();
            }

            @Override
            public boolean hasPrevious() {
                return this.getDelegateListIterator().hasPrevious();
            }

            @Override
            public V previous() {
                return this.getDelegateListIterator().previous();
            }

            @Override
            public int nextIndex() {
                return this.getDelegateListIterator().nextIndex();
            }

            @Override
            public int previousIndex() {
                return this.getDelegateListIterator().previousIndex();
            }

            @Override
            public void set(V value) {
                this.getDelegateListIterator().set(value);
            }

            @Override
            public void add(V value) {
                boolean wasEmpty = WrappedList.this.isEmpty();
                this.getDelegateListIterator().add(value);
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    WrappedList.this.addToMap();
                }
            }
        }
    }

    class WrappedNavigableSet
    extends WrappedSortedSet
    implements NavigableSet<V> {
        WrappedNavigableSet(K key, @Nullable NavigableSet<V> delegate, WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }

        NavigableSet<V> getSortedSetDelegate() {
            return (NavigableSet)super.getSortedSetDelegate();
        }

        @Override
        public V lower(V v) {
            return this.getSortedSetDelegate().lower(v);
        }

        @Override
        public V floor(V v) {
            return this.getSortedSetDelegate().floor(v);
        }

        @Override
        public V ceiling(V v) {
            return this.getSortedSetDelegate().ceiling(v);
        }

        @Override
        public V higher(V v) {
            return this.getSortedSetDelegate().higher(v);
        }

        @Override
        public V pollFirst() {
            return Iterators.pollNext(this.iterator());
        }

        @Override
        public V pollLast() {
            return Iterators.pollNext(this.descendingIterator());
        }

        private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
            return new WrappedNavigableSet(this.key, wrapped, this.getAncestor() == null ? this : this.getAncestor());
        }

        @Override
        public NavigableSet<V> descendingSet() {
            return this.wrap(this.getSortedSetDelegate().descendingSet());
        }

        @Override
        public Iterator<V> descendingIterator() {
            return new WrappedCollection.WrappedIterator(this.getSortedSetDelegate().descendingIterator());
        }

        @Override
        public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) {
            return this.wrap(this.getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
        }

        @Override
        public NavigableSet<V> headSet(V toElement, boolean inclusive) {
            return this.wrap(this.getSortedSetDelegate().headSet(toElement, inclusive));
        }

        @Override
        public NavigableSet<V> tailSet(V fromElement, boolean inclusive) {
            return this.wrap(this.getSortedSetDelegate().tailSet(fromElement, inclusive));
        }
    }

    private class WrappedSortedSet
    extends WrappedCollection
    implements SortedSet<V> {
        WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }

        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet)this.getDelegate();
        }

        @Override
        public Comparator<? super V> comparator() {
            return this.getSortedSetDelegate().comparator();
        }

        @Override
        public V first() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().first();
        }

        @Override
        public V last() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().last();
        }

        @Override
        public SortedSet<V> headSet(V toElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().headSet(toElement), this.getAncestor() == null ? this : this.getAncestor());
        }

        @Override
        public SortedSet<V> subSet(V fromElement, V toElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().subSet(fromElement, toElement), this.getAncestor() == null ? this : this.getAncestor());
        }

        @Override
        public SortedSet<V> tailSet(V fromElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().tailSet(fromElement), this.getAncestor() == null ? this : this.getAncestor());
        }
    }

    private class WrappedSet
    extends WrappedCollection
    implements Set<V> {
        WrappedSet(K key, Set<V> delegate) {
            super(key, delegate, null);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            int oldSize = this.size();
            boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
            if (changed) {
                int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + (newSize - oldSize);
                this.removeIfEmpty();
            }
            return changed;
        }
    }

    private class WrappedCollection
    extends AbstractCollection<V> {
        final K key;
        Collection<V> delegate;
        final WrappedCollection ancestor;
        final Collection<V> ancestorDelegate;

        WrappedCollection(K key, @Nullable Collection<V> delegate, WrappedCollection ancestor) {
            this.key = key;
            this.delegate = delegate;
            this.ancestor = ancestor;
            this.ancestorDelegate = ancestor == null ? null : ancestor.getDelegate();
        }

        void refreshIfEmpty() {
            Collection newDelegate;
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            } else if (this.delegate.isEmpty() && (newDelegate = (Collection)AbstractMapBasedMultimap.this.map.get(this.key)) != null) {
                this.delegate = newDelegate;
            }
        }

        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            } else if (this.delegate.isEmpty()) {
                AbstractMapBasedMultimap.this.map.remove(this.key);
            }
        }

        K getKey() {
            return this.key;
        }

        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            } else {
                AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
            }
        }

        @Override
        public int size() {
            this.refreshIfEmpty();
            return this.delegate.size();
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object == this) {
                return true;
            }
            this.refreshIfEmpty();
            return this.delegate.equals(object);
        }

        @Override
        public int hashCode() {
            this.refreshIfEmpty();
            return this.delegate.hashCode();
        }

        @Override
        public String toString() {
            this.refreshIfEmpty();
            return this.delegate.toString();
        }

        Collection<V> getDelegate() {
            return this.delegate;
        }

        @Override
        public Iterator<V> iterator() {
            this.refreshIfEmpty();
            return new WrappedIterator();
        }

        @Override
        public Spliterator<V> spliterator() {
            this.refreshIfEmpty();
            return this.delegate.spliterator();
        }

        @Override
        public boolean add(V value) {
            this.refreshIfEmpty();
            boolean wasEmpty = this.delegate.isEmpty();
            boolean changed = this.delegate.add(value);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    this.addToMap();
                }
            }
            return changed;
        }

        WrappedCollection getAncestor() {
            return this.ancestor;
        }

        @Override
        public boolean addAll(Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int oldSize = this.size();
            boolean changed = this.delegate.addAll(collection);
            if (changed) {
                int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + (newSize - oldSize);
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }

        @Override
        public boolean contains(Object o) {
            this.refreshIfEmpty();
            return this.delegate.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            this.refreshIfEmpty();
            return this.delegate.containsAll(c);
        }

        @Override
        public void clear() {
            int oldSize = this.size();
            if (oldSize == 0) {
                return;
            }
            this.delegate.clear();
            AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - oldSize;
            this.removeIfEmpty();
        }

        @Override
        public boolean remove(Object o) {
            this.refreshIfEmpty();
            boolean changed = this.delegate.remove(o);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize--;
                this.removeIfEmpty();
            }
            return changed;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            int oldSize = this.size();
            boolean changed = this.delegate.removeAll(c);
            if (changed) {
                int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + (newSize - oldSize);
                this.removeIfEmpty();
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            Preconditions.checkNotNull(c);
            int oldSize = this.size();
            boolean changed = this.delegate.retainAll(c);
            if (changed) {
                int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + (newSize - oldSize);
                this.removeIfEmpty();
            }
            return changed;
        }

        class WrappedIterator
        implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;

            WrappedIterator() {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator(WrappedCollection.this.delegate);
            }

            WrappedIterator(Iterator<V> delegateIterator) {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = delegateIterator;
            }

            void validateIterator() {
                WrappedCollection.this.refreshIfEmpty();
                if (WrappedCollection.this.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                this.validateIterator();
                return this.delegateIterator.hasNext();
            }

            @Override
            public V next() {
                this.validateIterator();
                return this.delegateIterator.next();
            }

            @Override
            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize--;
                WrappedCollection.this.removeIfEmpty();
            }

            Iterator<V> getDelegateIterator() {
                this.validateIterator();
                return this.delegateIterator;
            }
        }
    }
}

