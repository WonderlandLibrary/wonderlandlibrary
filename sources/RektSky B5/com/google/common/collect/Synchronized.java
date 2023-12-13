/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.SortedSetMultimap;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
final class Synchronized {
    private Synchronized() {
    }

    private static <E> Collection<E> collection(Collection<E> collection, @Nullable Object mutex) {
        return new SynchronizedCollection(collection, mutex);
    }

    @VisibleForTesting
    static <E> Set<E> set(Set<E> set, @Nullable Object mutex) {
        return new SynchronizedSet<E>(set, mutex);
    }

    private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @Nullable Object mutex) {
        return new SynchronizedSortedSet<E>(set, mutex);
    }

    private static <E> List<E> list(List<E> list, @Nullable Object mutex) {
        return list instanceof RandomAccess ? new SynchronizedRandomAccessList<E>(list, mutex) : new SynchronizedList<E>(list, mutex);
    }

    static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object mutex) {
        if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset) {
            return multiset;
        }
        return new SynchronizedMultiset<E>(multiset, mutex);
    }

    static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object mutex) {
        if (multimap instanceof SynchronizedMultimap || multimap instanceof ImmutableMultimap) {
            return multimap;
        }
        return new SynchronizedMultimap<K, V>(multimap, mutex);
    }

    static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @Nullable Object mutex) {
        if (multimap instanceof SynchronizedListMultimap || multimap instanceof ImmutableListMultimap) {
            return multimap;
        }
        return new SynchronizedListMultimap<K, V>(multimap, mutex);
    }

    static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @Nullable Object mutex) {
        if (multimap instanceof SynchronizedSetMultimap || multimap instanceof ImmutableSetMultimap) {
            return multimap;
        }
        return new SynchronizedSetMultimap<K, V>(multimap, mutex);
    }

    static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @Nullable Object mutex) {
        if (multimap instanceof SynchronizedSortedSetMultimap) {
            return multimap;
        }
        return new SynchronizedSortedSetMultimap<K, V>(multimap, mutex);
    }

    private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object mutex) {
        if (collection instanceof SortedSet) {
            return Synchronized.sortedSet((SortedSet)collection, mutex);
        }
        if (collection instanceof Set) {
            return Synchronized.set((Set)collection, mutex);
        }
        if (collection instanceof List) {
            return Synchronized.list((List)collection, mutex);
        }
        return Synchronized.collection(collection, mutex);
    }

    private static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object mutex) {
        if (set instanceof SortedSet) {
            return Synchronized.sortedSet((SortedSet)set, mutex);
        }
        return Synchronized.set(set, mutex);
    }

    @VisibleForTesting
    static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object mutex) {
        return new SynchronizedMap<K, V>(map, mutex);
    }

    static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @Nullable Object mutex) {
        return new SynchronizedSortedMap<K, V>(sortedMap, mutex);
    }

    static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @Nullable Object mutex) {
        if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap) {
            return bimap;
        }
        return new SynchronizedBiMap(bimap, mutex, null);
    }

    @GwtIncompatible(value="NavigableSet")
    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @Nullable Object mutex) {
        return new SynchronizedNavigableSet<E>(navigableSet, mutex);
    }

    @GwtIncompatible(value="NavigableSet")
    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
        return Synchronized.navigableSet(navigableSet, null);
    }

    @GwtIncompatible(value="NavigableMap")
    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
        return Synchronized.navigableMap(navigableMap, null);
    }

    @GwtIncompatible(value="NavigableMap")
    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @Nullable Object mutex) {
        return new SynchronizedNavigableMap<K, V>(navigableMap, mutex);
    }

    @GwtIncompatible(value="works but is needed only for NavigableMap")
    private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@Nullable Map.Entry<K, V> entry, @Nullable Object mutex) {
        if (entry == null) {
            return null;
        }
        return new SynchronizedEntry<K, V>(entry, mutex);
    }

    static <E> Queue<E> queue(Queue<E> queue, @Nullable Object mutex) {
        return queue instanceof SynchronizedQueue ? queue : new SynchronizedQueue(queue, mutex);
    }

    @GwtIncompatible(value="Deque")
    static <E> Deque<E> deque(Deque<E> deque, @Nullable Object mutex) {
        return new SynchronizedDeque<E>(deque, mutex);
    }

    @GwtIncompatible(value="Deque")
    private static final class SynchronizedDeque<E>
    extends SynchronizedQueue<E>
    implements Deque<E> {
        private static final long serialVersionUID = 0L;

        SynchronizedDeque(Deque<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Deque<E> delegate() {
            return (Deque)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addFirst(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().addFirst(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addLast(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().addLast(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean offerFirst(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().offerFirst(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean offerLast(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().offerLast(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E removeFirst() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeFirst();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E removeLast() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeLast();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E pollFirst() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().pollFirst();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E pollLast() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().pollLast();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E getFirst() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().getFirst();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E getLast() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().getLast();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E peekFirst() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().peekFirst();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E peekLast() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().peekLast();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeFirstOccurrence(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeFirstOccurrence(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeLastOccurrence(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeLastOccurrence(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void push(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().push(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E pop() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().pop();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Iterator<E> descendingIterator() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().descendingIterator();
            }
        }
    }

    private static class SynchronizedQueue<E>
    extends SynchronizedCollection<E>
    implements Queue<E> {
        private static final long serialVersionUID = 0L;

        SynchronizedQueue(Queue<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Queue<E> delegate() {
            return (Queue)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E element() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().element();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean offer(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().offer(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E peek() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().peek();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E poll() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().poll();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E remove() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().remove();
            }
        }
    }

    @GwtIncompatible(value="works but is needed only for NavigableMap")
    private static class SynchronizedEntry<K, V>
    extends SynchronizedObject
    implements Map.Entry<K, V> {
        private static final long serialVersionUID = 0L;

        SynchronizedEntry(Map.Entry<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Map.Entry<K, V> delegate() {
            return (Map.Entry)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object obj) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().equals(obj);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().hashCode();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K getKey() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().getKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V getValue() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().getValue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V setValue(V value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().setValue(value);
            }
        }
    }

    @GwtIncompatible(value="NavigableMap")
    @VisibleForTesting
    static class SynchronizedNavigableMap<K, V>
    extends SynchronizedSortedMap<K, V>
    implements NavigableMap<K, V> {
        transient NavigableSet<K> descendingKeySet;
        transient NavigableMap<K, V> descendingMap;
        transient NavigableSet<K> navigableKeySet;
        private static final long serialVersionUID = 0L;

        SynchronizedNavigableMap(NavigableMap<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        NavigableMap<K, V> delegate() {
            return (NavigableMap)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> ceilingEntry(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().ceilingEntry(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K ceilingKey(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().ceilingKey(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableSet<K> descendingKeySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.descendingKeySet == null) {
                    this.descendingKeySet = Synchronized.navigableSet(this.delegate().descendingKeySet(), this.mutex);
                    return this.descendingKeySet;
                }
                return this.descendingKeySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableMap<K, V> descendingMap() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.descendingMap == null) {
                    this.descendingMap = Synchronized.navigableMap(this.delegate().descendingMap(), this.mutex);
                    return this.descendingMap;
                }
                return this.descendingMap;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> firstEntry() {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().firstEntry(), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> floorEntry(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().floorEntry(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K floorKey(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().floorKey(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.navigableMap(this.delegate().headMap(toKey, inclusive), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> higherEntry(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().higherEntry(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K higherKey(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().higherKey(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> lastEntry() {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().lastEntry(), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> lowerEntry(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().lowerEntry(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K lowerKey(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().lowerKey(key);
            }
        }

        @Override
        public Set<K> keySet() {
            return this.navigableKeySet();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableSet<K> navigableKeySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.navigableKeySet == null) {
                    this.navigableKeySet = Synchronized.navigableSet(this.delegate().navigableKeySet(), this.mutex);
                    return this.navigableKeySet;
                }
                return this.navigableKeySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().pollFirstEntry(), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map.Entry<K, V> pollLastEntry() {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.nullableSynchronizedEntry(this.delegate().pollLastEntry(), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.navigableMap(this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.navigableMap(this.delegate().tailMap(fromKey, inclusive), this.mutex);
            }
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            return this.headMap(toKey, false);
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            return this.tailMap(fromKey, true);
        }
    }

    @GwtIncompatible(value="NavigableSet")
    @VisibleForTesting
    static class SynchronizedNavigableSet<E>
    extends SynchronizedSortedSet<E>
    implements NavigableSet<E> {
        transient NavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;

        SynchronizedNavigableSet(NavigableSet<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        NavigableSet<E> delegate() {
            return (NavigableSet)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E ceiling(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().ceiling(e2);
            }
        }

        @Override
        public Iterator<E> descendingIterator() {
            return this.delegate().descendingIterator();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableSet<E> descendingSet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.descendingSet == null) {
                    NavigableSet dS = Synchronized.navigableSet(this.delegate().descendingSet(), this.mutex);
                    this.descendingSet = dS;
                    return dS;
                }
                return this.descendingSet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E floor(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().floor(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.navigableSet(this.delegate().headSet(toElement, inclusive), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E higher(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().higher(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E lower(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().lower(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E pollFirst() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().pollFirst();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E pollLast() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().pollLast();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.navigableSet(this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.navigableSet(this.delegate().tailSet(fromElement, inclusive), this.mutex);
            }
        }

        @Override
        public SortedSet<E> headSet(E toElement) {
            return this.headSet(toElement, false);
        }

        @Override
        public SortedSet<E> subSet(E fromElement, E toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }

        @Override
        public SortedSet<E> tailSet(E fromElement) {
            return this.tailSet(fromElement, true);
        }
    }

    private static class SynchronizedAsMapValues<V>
    extends SynchronizedCollection<Collection<V>> {
        private static final long serialVersionUID = 0L;

        SynchronizedAsMapValues(Collection<Collection<V>> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        public Iterator<Collection<V>> iterator() {
            final Iterator iterator = super.iterator();
            return new ForwardingIterator<Collection<V>>(){

                @Override
                protected Iterator<Collection<V>> delegate() {
                    return iterator;
                }

                @Override
                public Collection<V> next() {
                    return Synchronized.typePreservingCollection((Collection)super.next(), SynchronizedAsMapValues.this.mutex);
                }
            };
        }
    }

    private static class SynchronizedAsMap<K, V>
    extends SynchronizedMap<K, Collection<V>> {
        transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
        transient Collection<Collection<V>> asMapValues;
        private static final long serialVersionUID = 0L;

        SynchronizedAsMap(Map<K, Collection<V>> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> get(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                Collection collection = (Collection)super.get(key);
                return collection == null ? null : Synchronized.typePreservingCollection(collection, this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<Map.Entry<K, Collection<V>>> entrySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.asMapEntrySet == null) {
                    this.asMapEntrySet = new SynchronizedAsMapEntries(this.delegate().entrySet(), this.mutex);
                }
                return this.asMapEntrySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<Collection<V>> values() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.asMapValues == null) {
                    this.asMapValues = new SynchronizedAsMapValues(this.delegate().values(), this.mutex);
                }
                return this.asMapValues;
            }
        }

        @Override
        public boolean containsValue(Object o2) {
            return this.values().contains(o2);
        }
    }

    @VisibleForTesting
    static class SynchronizedBiMap<K, V>
    extends SynchronizedMap<K, V>
    implements BiMap<K, V>,
    Serializable {
        private transient Set<V> valueSet;
        private transient BiMap<V, K> inverse;
        private static final long serialVersionUID = 0L;

        private SynchronizedBiMap(BiMap<K, V> delegate, @Nullable Object mutex, @Nullable BiMap<V, K> inverse) {
            super(delegate, mutex);
            this.inverse = inverse;
        }

        @Override
        BiMap<K, V> delegate() {
            return (BiMap)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<V> values() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.valueSet == null) {
                    this.valueSet = Synchronized.set(this.delegate().values(), this.mutex);
                }
                return this.valueSet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V forcePut(K key, V value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().forcePut(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public BiMap<V, K> inverse() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.inverse == null) {
                    this.inverse = new SynchronizedBiMap<K, V>(this.delegate().inverse(), this.mutex, this);
                }
                return this.inverse;
            }
        }
    }

    static class SynchronizedSortedMap<K, V>
    extends SynchronizedMap<K, V>
    implements SortedMap<K, V> {
        private static final long serialVersionUID = 0L;

        SynchronizedSortedMap(SortedMap<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        SortedMap<K, V> delegate() {
            return (SortedMap)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Comparator<? super K> comparator() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().comparator();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K firstKey() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().firstKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedMap<K, V> headMap(K toKey) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedMap(this.delegate().headMap(toKey), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K lastKey() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().lastKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedMap(this.delegate().subMap(fromKey, toKey), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedMap(this.delegate().tailMap(fromKey), this.mutex);
            }
        }
    }

    private static class SynchronizedMap<K, V>
    extends SynchronizedObject
    implements Map<K, V> {
        transient Set<K> keySet;
        transient Collection<V> values;
        transient Set<Map.Entry<K, V>> entrySet;
        private static final long serialVersionUID = 0L;

        SynchronizedMap(Map<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Map<K, V> delegate() {
            return (Map)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().containsKey(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsValue(Object value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().containsValue(value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(this.delegate().entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V get(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().get(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<K> keySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.set(this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V put(K key, V value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().put(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().putAll(map);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public V remove(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().remove(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> values() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.values == null) {
                    this.values = Synchronized.collection(this.delegate().values(), this.mutex);
                }
                return this.values;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().equals(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().hashCode();
            }
        }
    }

    private static class SynchronizedAsMapEntries<K, V>
    extends SynchronizedSet<Map.Entry<K, Collection<V>>> {
        private static final long serialVersionUID = 0L;

        SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator iterator = super.iterator();
            return new ForwardingIterator<Map.Entry<K, Collection<V>>>(){

                @Override
                protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
                    return iterator;
                }

                @Override
                public Map.Entry<K, Collection<V>> next() {
                    final Map.Entry entry = (Map.Entry)super.next();
                    return new ForwardingMapEntry<K, Collection<V>>(){

                        @Override
                        protected Map.Entry<K, Collection<V>> delegate() {
                            return entry;
                        }

                        @Override
                        public Collection<V> getValue() {
                            return Synchronized.typePreservingCollection((Collection)entry.getValue(), SynchronizedAsMapEntries.this.mutex);
                        }
                    };
                }
            };
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Object[] toArray() {
            Object object = this.mutex;
            synchronized (object) {
                return ObjectArrays.toArrayImpl(this.delegate());
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public <T> T[] toArray(T[] array) {
            Object object = this.mutex;
            synchronized (object) {
                return ObjectArrays.toArrayImpl(this.delegate(), array);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean contains(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return Maps.containsEntryImpl(this.delegate(), o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsAll(Collection<?> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return Collections2.containsAllImpl(this.delegate(), c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.mutex;
            synchronized (object) {
                return Sets.equalsImpl(this.delegate(), o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return Maps.removeEntryImpl(this.delegate(), o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(Collection<?> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return Iterators.removeAll(this.delegate().iterator(), c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(Collection<?> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return Iterators.retainAll(this.delegate().iterator(), c2);
            }
        }
    }

    private static class SynchronizedSortedSetMultimap<K, V>
    extends SynchronizedSetMultimap<K, V>
    implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedSet<V> get(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedSet(this.delegate().get(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedSet<V> removeAll(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeAll(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().replaceValues(key, values);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Comparator<? super V> valueComparator() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().valueComparator();
            }
        }
    }

    private static class SynchronizedSetMultimap<K, V>
    extends SynchronizedMultimap<K, V>
    implements SetMultimap<K, V> {
        transient Set<Map.Entry<K, V>> entrySet;
        private static final long serialVersionUID = 0L;

        SynchronizedSetMultimap(SetMultimap<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        SetMultimap<K, V> delegate() {
            return (SetMultimap)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<V> get(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.set(this.delegate().get(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<V> removeAll(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeAll(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().replaceValues(key, values);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<Map.Entry<K, V>> entries() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(this.delegate().entries(), this.mutex);
                }
                return this.entrySet;
            }
        }
    }

    private static class SynchronizedListMultimap<K, V>
    extends SynchronizedMultimap<K, V>
    implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        SynchronizedListMultimap(ListMultimap<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        ListMultimap<K, V> delegate() {
            return (ListMultimap)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public List<V> get(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.list(this.delegate().get(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public List<V> removeAll(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeAll(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public List<V> replaceValues(K key, Iterable<? extends V> values) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().replaceValues(key, values);
            }
        }
    }

    private static class SynchronizedMultimap<K, V>
    extends SynchronizedObject
    implements Multimap<K, V> {
        transient Set<K> keySet;
        transient Collection<V> valuesCollection;
        transient Collection<Map.Entry<K, V>> entries;
        transient Map<K, Collection<V>> asMap;
        transient Multiset<K> keys;
        private static final long serialVersionUID = 0L;

        @Override
        Multimap<K, V> delegate() {
            return (Multimap)super.delegate();
        }

        SynchronizedMultimap(Multimap<K, V> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().containsKey(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsValue(Object value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().containsValue(value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsEntry(Object key, Object value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().containsEntry(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> get(K key) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.typePreservingCollection(this.delegate().get(key), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean put(K key, V value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().put(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean putAll(K key, Iterable<? extends V> values) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().putAll(key, values);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().putAll(multimap);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().replaceValues(key, values);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object key, Object value) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().remove(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> removeAll(Object key) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeAll(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<K> keySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.typePreservingSet(this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<V> values() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.valuesCollection == null) {
                    this.valuesCollection = Synchronized.collection(this.delegate().values(), this.mutex);
                }
                return this.valuesCollection;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Collection<Map.Entry<K, V>> entries() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.entries == null) {
                    this.entries = Synchronized.typePreservingCollection(this.delegate().entries(), this.mutex);
                }
                return this.entries;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Map<K, Collection<V>> asMap() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.asMap == null) {
                    this.asMap = new SynchronizedAsMap(this.delegate().asMap(), this.mutex);
                }
                return this.asMap;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Multiset<K> keys() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.keys == null) {
                    this.keys = Synchronized.multiset(this.delegate().keys(), this.mutex);
                }
                return this.keys;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().equals(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().hashCode();
            }
        }
    }

    private static class SynchronizedMultiset<E>
    extends SynchronizedCollection<E>
    implements Multiset<E> {
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;
        private static final long serialVersionUID = 0L;

        SynchronizedMultiset(Multiset<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Multiset<E> delegate() {
            return (Multiset)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int count(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().count(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int add(E e2, int n2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().add(e2, n2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int remove(Object o2, int n2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().remove(o2, n2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int setCount(E element, int count) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().setCount(element, count);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean setCount(E element, int oldCount, int newCount) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().setCount(element, oldCount, newCount);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<E> elementSet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.elementSet == null) {
                    this.elementSet = Synchronized.typePreservingSet(this.delegate().elementSet(), this.mutex);
                }
                return this.elementSet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Set<Multiset.Entry<E>> entrySet() {
            Object object = this.mutex;
            synchronized (object) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.typePreservingSet(this.delegate().entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().equals(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().hashCode();
            }
        }
    }

    private static class SynchronizedRandomAccessList<E>
    extends SynchronizedList<E>
    implements RandomAccess {
        private static final long serialVersionUID = 0L;

        SynchronizedRandomAccessList(List<E> list, @Nullable Object mutex) {
            super(list, mutex);
        }
    }

    private static class SynchronizedList<E>
    extends SynchronizedCollection<E>
    implements List<E> {
        private static final long serialVersionUID = 0L;

        SynchronizedList(List<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        List<E> delegate() {
            return (List)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void add(int index, E element) {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().add(index, element);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(int index, Collection<? extends E> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().addAll(index, c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E get(int index) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().get(index);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int indexOf(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().indexOf(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int lastIndexOf(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().lastIndexOf(o2);
            }
        }

        @Override
        public ListIterator<E> listIterator() {
            return this.delegate().listIterator();
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return this.delegate().listIterator(index);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E remove(int index) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().remove(index);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E set(int index, E element) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().set(index, element);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.list(this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().equals(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().hashCode();
            }
        }
    }

    static class SynchronizedSortedSet<E>
    extends SynchronizedSet<E>
    implements SortedSet<E> {
        private static final long serialVersionUID = 0L;

        SynchronizedSortedSet(SortedSet<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        SortedSet<E> delegate() {
            return (SortedSet)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Comparator<? super E> comparator() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().comparator();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedSet<E> subSet(E fromElement, E toElement) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedSet(this.delegate().subSet(fromElement, toElement), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedSet<E> headSet(E toElement) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedSet(this.delegate().headSet(toElement), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public SortedSet<E> tailSet(E fromElement) {
            Object object = this.mutex;
            synchronized (object) {
                return Synchronized.sortedSet(this.delegate().tailSet(fromElement), this.mutex);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E first() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().first();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public E last() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().last();
            }
        }
    }

    static class SynchronizedSet<E>
    extends SynchronizedCollection<E>
    implements Set<E> {
        private static final long serialVersionUID = 0L;

        SynchronizedSet(Set<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Set<E> delegate() {
            return (Set)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o2) {
            if (o2 == this) {
                return true;
            }
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().equals(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().hashCode();
            }
        }
    }

    @VisibleForTesting
    static class SynchronizedCollection<E>
    extends SynchronizedObject
    implements Collection<E> {
        private static final long serialVersionUID = 0L;

        private SynchronizedCollection(Collection<E> delegate, @Nullable Object mutex) {
            super(delegate, mutex);
        }

        @Override
        Collection<E> delegate() {
            return (Collection)super.delegate();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean add(E e2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().add(e2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean addAll(Collection<? extends E> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().addAll(c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.mutex;
            synchronized (object) {
                this.delegate().clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean contains(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().contains(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsAll(Collection<?> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().containsAll(c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().isEmpty();
            }
        }

        @Override
        public Iterator<E> iterator() {
            return this.delegate().iterator();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object o2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().remove(o2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean removeAll(Collection<?> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().removeAll(c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean retainAll(Collection<?> c2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().retainAll(c2);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Object[] toArray() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().toArray();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public <T> T[] toArray(T[] a2) {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate().toArray(a2);
            }
        }
    }

    static class SynchronizedObject
    implements Serializable {
        final Object delegate;
        final Object mutex;
        @GwtIncompatible(value="not needed in emulated source")
        private static final long serialVersionUID = 0L;

        SynchronizedObject(Object delegate, @Nullable Object mutex) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.mutex = mutex == null ? this : mutex;
        }

        Object delegate() {
            return this.delegate;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public String toString() {
            Object object = this.mutex;
            synchronized (object) {
                return this.delegate.toString();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @GwtIncompatible(value="java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            Object object = this.mutex;
            synchronized (object) {
                stream.defaultWriteObject();
            }
        }
    }
}

