// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.j2objc.annotations.RetainedWith;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.Spliterator;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Deque;
import java.util.Queue;
import java.util.NavigableMap;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.Map;
import java.util.RandomAccess;
import java.util.List;
import java.util.SortedSet;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import javax.annotation.CheckForNull;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class Synchronized
{
    private Synchronized() {
    }
    
    private static <E> Collection<E> collection(final Collection<E> collection, @CheckForNull final Object mutex) {
        return new SynchronizedCollection<E>((Collection)collection, mutex);
    }
    
    @VisibleForTesting
    static <E> Set<E> set(final Set<E> set, @CheckForNull final Object mutex) {
        return new SynchronizedSet<E>(set, mutex);
    }
    
    private static <E> SortedSet<E> sortedSet(final SortedSet<E> set, @CheckForNull final Object mutex) {
        return new SynchronizedSortedSet<E>(set, mutex);
    }
    
    private static <E> List<E> list(final List<E> list, @CheckForNull final Object mutex) {
        return (list instanceof RandomAccess) ? new SynchronizedRandomAccessList<E>(list, mutex) : new SynchronizedList<E>(list, mutex);
    }
    
    static <E> Multiset<E> multiset(final Multiset<E> multiset, @CheckForNull final Object mutex) {
        if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset) {
            return multiset;
        }
        return new SynchronizedMultiset<E>(multiset, mutex);
    }
    
    static <K, V> Multimap<K, V> multimap(final Multimap<K, V> multimap, @CheckForNull final Object mutex) {
        if (multimap instanceof SynchronizedMultimap || multimap instanceof BaseImmutableMultimap) {
            return multimap;
        }
        return new SynchronizedMultimap<K, V>(multimap, mutex);
    }
    
    static <K, V> ListMultimap<K, V> listMultimap(final ListMultimap<K, V> multimap, @CheckForNull final Object mutex) {
        if (multimap instanceof SynchronizedListMultimap || multimap instanceof BaseImmutableMultimap) {
            return multimap;
        }
        return new SynchronizedListMultimap<K, V>(multimap, mutex);
    }
    
    static <K, V> SetMultimap<K, V> setMultimap(final SetMultimap<K, V> multimap, @CheckForNull final Object mutex) {
        if (multimap instanceof SynchronizedSetMultimap || multimap instanceof BaseImmutableMultimap) {
            return multimap;
        }
        return new SynchronizedSetMultimap<K, V>(multimap, mutex);
    }
    
    static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(final SortedSetMultimap<K, V> multimap, @CheckForNull final Object mutex) {
        if (multimap instanceof SynchronizedSortedSetMultimap) {
            return multimap;
        }
        return new SynchronizedSortedSetMultimap<K, V>(multimap, mutex);
    }
    
    private static <E> Collection<E> typePreservingCollection(final Collection<E> collection, @CheckForNull final Object mutex) {
        if (collection instanceof SortedSet) {
            return (Collection<E>)sortedSet((SortedSet<Object>)(SortedSet)collection, mutex);
        }
        if (collection instanceof Set) {
            return (Collection<E>)set((Set<Object>)(Set)collection, mutex);
        }
        if (collection instanceof List) {
            return (Collection<E>)list((List<Object>)(List)collection, mutex);
        }
        return (Collection<E>)collection((Collection<Object>)collection, mutex);
    }
    
    private static <E> Set<E> typePreservingSet(final Set<E> set, @CheckForNull final Object mutex) {
        if (set instanceof SortedSet) {
            return (Set<E>)sortedSet((SortedSet<Object>)(SortedSet)set, mutex);
        }
        return (Set<E>)set((Set<Object>)set, mutex);
    }
    
    @VisibleForTesting
    static <K, V> Map<K, V> map(final Map<K, V> map, @CheckForNull final Object mutex) {
        return new SynchronizedMap<K, V>(map, mutex);
    }
    
    static <K, V> SortedMap<K, V> sortedMap(final SortedMap<K, V> sortedMap, @CheckForNull final Object mutex) {
        return new SynchronizedSortedMap<K, V>(sortedMap, mutex);
    }
    
    static <K, V> BiMap<K, V> biMap(final BiMap<K, V> bimap, @CheckForNull final Object mutex) {
        if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap) {
            return bimap;
        }
        return new SynchronizedBiMap<K, V>((BiMap)bimap, mutex, (BiMap)null);
    }
    
    @GwtIncompatible
    static <E> NavigableSet<E> navigableSet(final NavigableSet<E> navigableSet, @CheckForNull final Object mutex) {
        return new SynchronizedNavigableSet<E>(navigableSet, mutex);
    }
    
    @GwtIncompatible
    static <E> NavigableSet<E> navigableSet(final NavigableSet<E> navigableSet) {
        return navigableSet(navigableSet, null);
    }
    
    @GwtIncompatible
    static <K, V> NavigableMap<K, V> navigableMap(final NavigableMap<K, V> navigableMap) {
        return navigableMap(navigableMap, null);
    }
    
    @GwtIncompatible
    static <K, V> NavigableMap<K, V> navigableMap(final NavigableMap<K, V> navigableMap, @CheckForNull final Object mutex) {
        return new SynchronizedNavigableMap<K, V>(navigableMap, mutex);
    }
    
    @CheckForNull
    @GwtIncompatible
    private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@CheckForNull final Map.Entry<K, V> entry, @CheckForNull final Object mutex) {
        if (entry == null) {
            return null;
        }
        return new SynchronizedEntry<K, V>(entry, mutex);
    }
    
    static <E> Queue<E> queue(final Queue<E> queue, @CheckForNull final Object mutex) {
        return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue<E>(queue, mutex);
    }
    
    static <E> Deque<E> deque(final Deque<E> deque, @CheckForNull final Object mutex) {
        return new SynchronizedDeque<E>(deque, mutex);
    }
    
    static <R, C, V> Table<R, C, V> table(final Table<R, C, V> table, @CheckForNull final Object mutex) {
        return new SynchronizedTable<R, C, V>(table, mutex);
    }
    
    static class SynchronizedObject implements Serializable
    {
        final Object delegate;
        final Object mutex;
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        SynchronizedObject(final Object delegate, @CheckForNull final Object mutex) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.mutex = ((mutex == null) ? this : mutex);
        }
        
        Object delegate() {
            return this.delegate;
        }
        
        @Override
        public String toString() {
            synchronized (this.mutex) {
                return this.delegate.toString();
            }
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            synchronized (this.mutex) {
                stream.defaultWriteObject();
            }
        }
    }
    
    @VisibleForTesting
    static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E>
    {
        private static final long serialVersionUID = 0L;
        
        private SynchronizedCollection(final Collection<E> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Collection<E> delegate() {
            return (Collection<E>)super.delegate();
        }
        
        @Override
        public boolean add(final E e) {
            synchronized (this.mutex) {
                return this.delegate().add(e);
            }
        }
        
        @Override
        public boolean addAll(final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(c);
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().contains(o);
            }
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return this.delegate().containsAll(c);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        @Override
        public Iterator<E> iterator() {
            return this.delegate().iterator();
        }
        
        @Override
        public Spliterator<E> spliterator() {
            synchronized (this.mutex) {
                return this.delegate().spliterator();
            }
        }
        
        @Override
        public Stream<E> stream() {
            synchronized (this.mutex) {
                return this.delegate().stream();
            }
        }
        
        @Override
        public Stream<E> parallelStream() {
            synchronized (this.mutex) {
                return this.delegate().parallelStream();
            }
        }
        
        @Override
        public void forEach(final Consumer<? super E> action) {
            synchronized (this.mutex) {
                this.delegate().forEach(action);
            }
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().remove(o);
            }
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return this.delegate().retainAll(c);
            }
        }
        
        @Override
        public boolean removeIf(final Predicate<? super E> filter) {
            synchronized (this.mutex) {
                return this.delegate().removeIf(filter);
            }
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.mutex) {
                return this.delegate().toArray();
            }
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            synchronized (this.mutex) {
                return this.delegate().toArray(a);
            }
        }
    }
    
    static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSet(final Set<E> delegate, @CheckForNull final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        Set<E> delegate() {
            return (Set<E>)(Set)super.delegate();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSortedSet(final SortedSet<E> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        SortedSet<E> delegate() {
            return (SortedSet<E>)(SortedSet)super.delegate();
        }
        
        @CheckForNull
        @Override
        public Comparator<? super E> comparator() {
            synchronized (this.mutex) {
                return this.delegate().comparator();
            }
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            synchronized (this.mutex) {
                return (SortedSet<E>)sortedSet((SortedSet<Object>)this.delegate().subSet(fromElement, toElement), this.mutex);
            }
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            synchronized (this.mutex) {
                return (SortedSet<E>)sortedSet((SortedSet<Object>)this.delegate().headSet(toElement), this.mutex);
            }
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            synchronized (this.mutex) {
                return (SortedSet<E>)sortedSet((SortedSet<Object>)this.delegate().tailSet(fromElement), this.mutex);
            }
        }
        
        @Override
        public E first() {
            synchronized (this.mutex) {
                return this.delegate().first();
            }
        }
        
        @Override
        public E last() {
            synchronized (this.mutex) {
                return this.delegate().last();
            }
        }
    }
    
    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedList(final List<E> delegate, @CheckForNull final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        List<E> delegate() {
            return (List<E>)(List)super.delegate();
        }
        
        @Override
        public void add(final int index, final E element) {
            synchronized (this.mutex) {
                this.delegate().add(index, element);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends E> c) {
            synchronized (this.mutex) {
                return this.delegate().addAll(index, c);
            }
        }
        
        @Override
        public E get(final int index) {
            synchronized (this.mutex) {
                return this.delegate().get(index);
            }
        }
        
        @Override
        public int indexOf(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().indexOf(o);
            }
        }
        
        @Override
        public int lastIndexOf(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().lastIndexOf(o);
            }
        }
        
        @Override
        public ListIterator<E> listIterator() {
            return this.delegate().listIterator();
        }
        
        @Override
        public ListIterator<E> listIterator(final int index) {
            return this.delegate().listIterator(index);
        }
        
        @Override
        public E remove(final int index) {
            synchronized (this.mutex) {
                return this.delegate().remove(index);
            }
        }
        
        @Override
        public E set(final int index, final E element) {
            synchronized (this.mutex) {
                return this.delegate().set(index, element);
            }
        }
        
        @Override
        public void replaceAll(final UnaryOperator<E> operator) {
            synchronized (this.mutex) {
                this.delegate().replaceAll(operator);
            }
        }
        
        @Override
        public void sort(final Comparator<? super E> c) {
            synchronized (this.mutex) {
                this.delegate().sort(c);
            }
        }
        
        @Override
        public List<E> subList(final int fromIndex, final int toIndex) {
            synchronized (this.mutex) {
                return (List<E>)list((List<Object>)this.delegate().subList(fromIndex, toIndex), this.mutex);
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedRandomAccessList(final List<E> list, @CheckForNull final Object mutex) {
            super(list, mutex);
        }
    }
    
    private static class SynchronizedMultiset<E> extends SynchronizedCollection<E> implements Multiset<E>
    {
        @CheckForNull
        transient Set<E> elementSet;
        @CheckForNull
        transient Set<Entry<E>> entrySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedMultiset(final Multiset<E> delegate, @CheckForNull final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        Multiset<E> delegate() {
            return (Multiset<E>)(Multiset)super.delegate();
        }
        
        @Override
        public int count(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().count(o);
            }
        }
        
        @Override
        public int add(final E e, final int n) {
            synchronized (this.mutex) {
                return this.delegate().add(e, n);
            }
        }
        
        @Override
        public int remove(@CheckForNull final Object o, final int n) {
            synchronized (this.mutex) {
                return this.delegate().remove(o, n);
            }
        }
        
        @Override
        public int setCount(final E element, final int count) {
            synchronized (this.mutex) {
                return this.delegate().setCount(element, count);
            }
        }
        
        @Override
        public boolean setCount(final E element, final int oldCount, final int newCount) {
            synchronized (this.mutex) {
                return this.delegate().setCount(element, oldCount, newCount);
            }
        }
        
        @Override
        public Set<E> elementSet() {
            synchronized (this.mutex) {
                if (this.elementSet == null) {
                    this.elementSet = (Set<E>)typePreservingSet((Set<Object>)this.delegate().elementSet(), this.mutex);
                }
                return this.elementSet;
            }
        }
        
        @Override
        public Set<Entry<E>> entrySet() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = (Set<Entry<E>>)typePreservingSet((Set<Object>)this.delegate().entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedMultimap<K, V> extends SynchronizedObject implements Multimap<K, V>
    {
        @CheckForNull
        transient Set<K> keySet;
        @CheckForNull
        transient Collection<V> valuesCollection;
        @CheckForNull
        transient Collection<Map.Entry<K, V>> entries;
        @CheckForNull
        transient Map<K, Collection<V>> asMap;
        @CheckForNull
        transient Multiset<K> keys;
        private static final long serialVersionUID = 0L;
        
        @Override
        Multimap<K, V> delegate() {
            return (Multimap<K, V>)super.delegate();
        }
        
        SynchronizedMultimap(final Multimap<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().containsKey(key);
            }
        }
        
        @Override
        public boolean containsValue(@CheckForNull final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsValue(value);
            }
        }
        
        @Override
        public boolean containsEntry(@CheckForNull final Object key, @CheckForNull final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsEntry(key, value);
            }
        }
        
        @Override
        public Collection<V> get(final K key) {
            synchronized (this.mutex) {
                return (Collection<V>)typePreservingCollection((Collection<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public boolean put(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().put(key, value);
            }
        }
        
        @Override
        public boolean putAll(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().putAll(key, values);
            }
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
            synchronized (this.mutex) {
                return this.delegate().putAll(multimap);
            }
        }
        
        @Override
        public Collection<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
            synchronized (this.mutex) {
                return this.delegate().remove(key, value);
            }
        }
        
        @Override
        public Collection<V> removeAll(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        @Override
        public Set<K> keySet() {
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = (Set<K>)typePreservingSet((Set<Object>)this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }
        
        @Override
        public Collection<V> values() {
            synchronized (this.mutex) {
                if (this.valuesCollection == null) {
                    this.valuesCollection = (Collection<V>)collection((Collection<Object>)this.delegate().values(), this.mutex);
                }
                return this.valuesCollection;
            }
        }
        
        @Override
        public Collection<Map.Entry<K, V>> entries() {
            synchronized (this.mutex) {
                if (this.entries == null) {
                    this.entries = (Collection<Map.Entry<K, V>>)typePreservingCollection((Collection<Object>)this.delegate().entries(), this.mutex);
                }
                return this.entries;
            }
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            synchronized (this.mutex) {
                this.delegate().forEach(action);
            }
        }
        
        @Override
        public Map<K, Collection<V>> asMap() {
            synchronized (this.mutex) {
                if (this.asMap == null) {
                    this.asMap = (Map<K, Collection<V>>)new SynchronizedAsMap((Map<Object, Collection<Object>>)this.delegate().asMap(), this.mutex);
                }
                return this.asMap;
            }
        }
        
        @Override
        public Multiset<K> keys() {
            synchronized (this.mutex) {
                if (this.keys == null) {
                    this.keys = Synchronized.multiset(this.delegate().keys(), this.mutex);
                }
                return this.keys;
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    private static class SynchronizedListMultimap<K, V> extends SynchronizedMultimap<K, V> implements ListMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedListMultimap(final ListMultimap<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        ListMultimap<K, V> delegate() {
            return (ListMultimap<K, V>)(ListMultimap)super.delegate();
        }
        
        @Override
        public List<V> get(final K key) {
            synchronized (this.mutex) {
                return (List<V>)list((List<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public List<V> removeAll(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public List<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
    }
    
    private static class SynchronizedSetMultimap<K, V> extends SynchronizedMultimap<K, V> implements SetMultimap<K, V>
    {
        @CheckForNull
        transient Set<Map.Entry<K, V>> entrySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedSetMultimap(final SetMultimap<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        SetMultimap<K, V> delegate() {
            return (SetMultimap<K, V>)(SetMultimap)super.delegate();
        }
        
        @Override
        public Set<V> get(final K key) {
            synchronized (this.mutex) {
                return Synchronized.set(this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public Set<V> removeAll(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public Set<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @Override
        public Set<Map.Entry<K, V>> entries() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(this.delegate().entries(), this.mutex);
                }
                return this.entrySet;
            }
        }
    }
    
    private static class SynchronizedSortedSetMultimap<K, V> extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSortedSetMultimap(final SortedSetMultimap<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap<K, V>)(SortedSetMultimap)super.delegate();
        }
        
        @Override
        public SortedSet<V> get(final K key) {
            synchronized (this.mutex) {
                return (SortedSet<V>)sortedSet((SortedSet<Object>)this.delegate().get(key), this.mutex);
            }
        }
        
        @Override
        public SortedSet<V> removeAll(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().removeAll(key);
            }
        }
        
        @Override
        public SortedSet<V> replaceValues(final K key, final Iterable<? extends V> values) {
            synchronized (this.mutex) {
                return this.delegate().replaceValues(key, values);
            }
        }
        
        @CheckForNull
        @Override
        public Comparator<? super V> valueComparator() {
            synchronized (this.mutex) {
                return this.delegate().valueComparator();
            }
        }
    }
    
    private static class SynchronizedAsMapEntries<K, V> extends SynchronizedSet<Map.Entry<K, Collection<V>>>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedAsMapEntries(final Set<Map.Entry<K, Collection<V>>> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            return new TransformedIterator<Map.Entry<K, Collection<V>>, Map.Entry<K, Collection<V>>>(super.iterator()) {
                @Override
                Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry) {
                    return new ForwardingMapEntry<K, Collection<V>>() {
                        @Override
                        protected Map.Entry<K, Collection<V>> delegate() {
                            return entry;
                        }
                        
                        @Override
                        public Collection<V> getValue() {
                            return (Collection<V>)typePreservingCollection((Collection<Object>)entry.getValue(), SynchronizedAsMapEntries.this.mutex);
                        }
                    };
                }
            };
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.mutex) {
                final Object[] result = ObjectArrays.toArrayImpl(this.delegate());
                return result;
            }
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            synchronized (this.mutex) {
                return ObjectArrays.toArrayImpl(this.delegate(), array);
            }
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return Maps.containsEntryImpl(this.delegate(), o);
            }
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return Collections2.containsAllImpl(this.delegate(), c);
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return Sets.equalsImpl(this.delegate(), o);
            }
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return Maps.removeEntryImpl(this.delegate(), o);
            }
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return Iterators.removeAll(this.delegate().iterator(), c);
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            synchronized (this.mutex) {
                return Iterators.retainAll(this.delegate().iterator(), c);
            }
        }
    }
    
    private static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V>
    {
        @CheckForNull
        transient Set<K> keySet;
        @CheckForNull
        transient Collection<V> values;
        @CheckForNull
        transient Set<Entry<K, V>> entrySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedMap(final Map<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Map<K, V> delegate() {
            return (Map<K, V>)super.delegate();
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().containsKey(key);
            }
        }
        
        @Override
        public boolean containsValue(@CheckForNull final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsValue(value);
            }
        }
        
        @Override
        public Set<Entry<K, V>> entrySet() {
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(this.delegate().entrySet(), this.mutex);
                }
                return this.entrySet;
            }
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            synchronized (this.mutex) {
                this.delegate().forEach(action);
            }
        }
        
        @CheckForNull
        @Override
        public V get(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().get(key);
            }
        }
        
        @CheckForNull
        @Override
        public V getOrDefault(@CheckForNull final Object key, @CheckForNull final V defaultValue) {
            synchronized (this.mutex) {
                return this.delegate().getOrDefault(key, defaultValue);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        @Override
        public Set<K> keySet() {
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.set(this.delegate().keySet(), this.mutex);
                }
                return this.keySet;
            }
        }
        
        @CheckForNull
        @Override
        public V put(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().put(key, value);
            }
        }
        
        @CheckForNull
        @Override
        public V putIfAbsent(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final V oldValue, final V newValue) {
            synchronized (this.mutex) {
                return this.delegate().replace(key, oldValue, newValue);
            }
        }
        
        @CheckForNull
        @Override
        public V replace(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().replace(key, value);
            }
        }
        
        @Override
        public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
            synchronized (this.mutex) {
                return this.delegate().computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            synchronized (this.mutex) {
                return this.delegate().computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            synchronized (this.mutex) {
                return this.delegate().compute(key, remappingFunction);
            }
        }
        
        @Override
        public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.mutex) {
                return this.delegate().merge(key, value, remappingFunction);
            }
        }
        
        @Override
        public void putAll(final Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                this.delegate().putAll(map);
            }
        }
        
        @Override
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            synchronized (this.mutex) {
                this.delegate().replaceAll(function);
            }
        }
        
        @CheckForNull
        @Override
        public V remove(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                return this.delegate().remove(key);
            }
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
            synchronized (this.mutex) {
                return this.delegate().remove(key, value);
            }
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        @Override
        public Collection<V> values() {
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = (Collection<V>)collection((Collection<Object>)this.delegate().values(), this.mutex);
                }
                return this.values;
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
    }
    
    static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedSortedMap(final SortedMap<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        SortedMap<K, V> delegate() {
            return (SortedMap<K, V>)(SortedMap)super.delegate();
        }
        
        @CheckForNull
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.mutex) {
                return this.delegate().comparator();
            }
        }
        
        @Override
        public K firstKey() {
            synchronized (this.mutex) {
                return this.delegate().firstKey();
            }
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            synchronized (this.mutex) {
                return Synchronized.sortedMap(this.delegate().headMap(toKey), this.mutex);
            }
        }
        
        @Override
        public K lastKey() {
            synchronized (this.mutex) {
                return this.delegate().lastKey();
            }
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            synchronized (this.mutex) {
                return Synchronized.sortedMap(this.delegate().subMap(fromKey, toKey), this.mutex);
            }
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            synchronized (this.mutex) {
                return Synchronized.sortedMap(this.delegate().tailMap(fromKey), this.mutex);
            }
        }
    }
    
    @VisibleForTesting
    static class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable
    {
        @CheckForNull
        private transient Set<V> valueSet;
        @CheckForNull
        @RetainedWith
        private transient BiMap<V, K> inverse;
        private static final long serialVersionUID = 0L;
        
        private SynchronizedBiMap(final BiMap<K, V> delegate, @CheckForNull final Object mutex, @CheckForNull final BiMap<V, K> inverse) {
            super(delegate, mutex);
            this.inverse = inverse;
        }
        
        @Override
        BiMap<K, V> delegate() {
            return (BiMap<K, V>)(BiMap)super.delegate();
        }
        
        @Override
        public Set<V> values() {
            synchronized (this.mutex) {
                if (this.valueSet == null) {
                    this.valueSet = Synchronized.set(this.delegate().values(), this.mutex);
                }
                return this.valueSet;
            }
        }
        
        @CheckForNull
        @Override
        public V forcePut(final K key, final V value) {
            synchronized (this.mutex) {
                return this.delegate().forcePut(key, value);
            }
        }
        
        @Override
        public BiMap<V, K> inverse() {
            synchronized (this.mutex) {
                if (this.inverse == null) {
                    this.inverse = (BiMap<V, K>)new SynchronizedBiMap((BiMap<Object, Object>)this.delegate().inverse(), this.mutex, (BiMap<Object, Object>)this);
                }
                return this.inverse;
            }
        }
    }
    
    private static class SynchronizedAsMap<K, V> extends SynchronizedMap<K, Collection<V>>
    {
        @CheckForNull
        transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
        @CheckForNull
        transient Collection<Collection<V>> asMapValues;
        private static final long serialVersionUID = 0L;
        
        SynchronizedAsMap(final Map<K, Collection<V>> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @CheckForNull
        @Override
        public Collection<V> get(@CheckForNull final Object key) {
            synchronized (this.mutex) {
                final Collection<V> collection = super.get(key);
                return (Collection<V>)((collection == null) ? null : typePreservingCollection((Collection<Object>)collection, this.mutex));
            }
        }
        
        @Override
        public Set<Map.Entry<K, Collection<V>>> entrySet() {
            synchronized (this.mutex) {
                if (this.asMapEntrySet == null) {
                    this.asMapEntrySet = (Set<Map.Entry<K, Collection<V>>>)new SynchronizedAsMapEntries((Set<Map.Entry<Object, Collection<Object>>>)this.delegate().entrySet(), this.mutex);
                }
                return this.asMapEntrySet;
            }
        }
        
        @Override
        public Collection<Collection<V>> values() {
            synchronized (this.mutex) {
                if (this.asMapValues == null) {
                    this.asMapValues = (Collection<Collection<V>>)new SynchronizedAsMapValues((Collection<Collection<Object>>)this.delegate().values(), this.mutex);
                }
                return this.asMapValues;
            }
        }
        
        @Override
        public boolean containsValue(@CheckForNull final Object o) {
            return this.values().contains(o);
        }
    }
    
    private static class SynchronizedAsMapValues<V> extends SynchronizedCollection<Collection<V>>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedAsMapValues(final Collection<Collection<V>> delegate, @CheckForNull final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        public Iterator<Collection<V>> iterator() {
            return new TransformedIterator<Collection<V>, Collection<V>>(super.iterator()) {
                @Override
                Collection<V> transform(final Collection<V> from) {
                    return (Collection<V>)typePreservingCollection((Collection<Object>)from, SynchronizedAsMapValues.this.mutex);
                }
            };
        }
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E>
    {
        @CheckForNull
        transient NavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedNavigableSet(final NavigableSet<E> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        NavigableSet<E> delegate() {
            return (NavigableSet<E>)(NavigableSet)super.delegate();
        }
        
        @CheckForNull
        @Override
        public E ceiling(final E e) {
            synchronized (this.mutex) {
                return this.delegate().ceiling(e);
            }
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return this.delegate().descendingIterator();
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            synchronized (this.mutex) {
                if (this.descendingSet == null) {
                    final NavigableSet<E> dS = Synchronized.navigableSet(this.delegate().descendingSet(), this.mutex);
                    return this.descendingSet = dS;
                }
                return this.descendingSet;
            }
        }
        
        @CheckForNull
        @Override
        public E floor(final E e) {
            synchronized (this.mutex) {
                return this.delegate().floor(e);
            }
        }
        
        @Override
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.navigableSet(this.delegate().headSet(toElement, inclusive), this.mutex);
            }
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return this.headSet(toElement, false);
        }
        
        @CheckForNull
        @Override
        public E higher(final E e) {
            synchronized (this.mutex) {
                return this.delegate().higher(e);
            }
        }
        
        @CheckForNull
        @Override
        public E lower(final E e) {
            synchronized (this.mutex) {
                return this.delegate().lower(e);
            }
        }
        
        @CheckForNull
        @Override
        public E pollFirst() {
            synchronized (this.mutex) {
                return this.delegate().pollFirst();
            }
        }
        
        @CheckForNull
        @Override
        public E pollLast() {
            synchronized (this.mutex) {
                return this.delegate().pollLast();
            }
        }
        
        @Override
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            synchronized (this.mutex) {
                return Synchronized.navigableSet(this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
            }
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }
        
        @Override
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.navigableSet(this.delegate().tailSet(fromElement, inclusive), this.mutex);
            }
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return this.tailSet(fromElement, true);
        }
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V>
    {
        @CheckForNull
        transient NavigableSet<K> descendingKeySet;
        @CheckForNull
        transient NavigableMap<K, V> descendingMap;
        @CheckForNull
        transient NavigableSet<K> navigableKeySet;
        private static final long serialVersionUID = 0L;
        
        SynchronizedNavigableMap(final NavigableMap<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        NavigableMap<K, V> delegate() {
            return (NavigableMap<K, V>)(NavigableMap)super.delegate();
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> ceilingEntry(final K key) {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().ceilingEntry(key), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public K ceilingKey(final K key) {
            synchronized (this.mutex) {
                return this.delegate().ceilingKey(key);
            }
        }
        
        @Override
        public NavigableSet<K> descendingKeySet() {
            synchronized (this.mutex) {
                if (this.descendingKeySet == null) {
                    return this.descendingKeySet = Synchronized.navigableSet(this.delegate().descendingKeySet(), this.mutex);
                }
                return this.descendingKeySet;
            }
        }
        
        @Override
        public NavigableMap<K, V> descendingMap() {
            synchronized (this.mutex) {
                if (this.descendingMap == null) {
                    return this.descendingMap = Synchronized.navigableMap(this.delegate().descendingMap(), this.mutex);
                }
                return this.descendingMap;
            }
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> firstEntry() {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().firstEntry(), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> floorEntry(final K key) {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().floorEntry(key), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public K floorKey(final K key) {
            synchronized (this.mutex) {
                return this.delegate().floorKey(key);
            }
        }
        
        @Override
        public NavigableMap<K, V> headMap(final K toKey, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.navigableMap(this.delegate().headMap(toKey, inclusive), this.mutex);
            }
        }
        
        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return this.headMap(toKey, false);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> higherEntry(final K key) {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().higherEntry(key), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public K higherKey(final K key) {
            synchronized (this.mutex) {
                return this.delegate().higherKey(key);
            }
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> lastEntry() {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().lastEntry(), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> lowerEntry(final K key) {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().lowerEntry(key), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public K lowerKey(final K key) {
            synchronized (this.mutex) {
                return this.delegate().lowerKey(key);
            }
        }
        
        @Override
        public Set<K> keySet() {
            return this.navigableKeySet();
        }
        
        @Override
        public NavigableSet<K> navigableKeySet() {
            synchronized (this.mutex) {
                if (this.navigableKeySet == null) {
                    return this.navigableKeySet = Synchronized.navigableSet(this.delegate().navigableKeySet(), this.mutex);
                }
                return this.navigableKeySet;
            }
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> pollFirstEntry() {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().pollFirstEntry(), this.mutex);
            }
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, V> pollLastEntry() {
            synchronized (this.mutex) {
                return (Map.Entry<K, V>)nullableSynchronizedEntry((Map.Entry<Object, Object>)this.delegate().pollLastEntry(), this.mutex);
            }
        }
        
        @Override
        public NavigableMap<K, V> subMap(final K fromKey, final boolean fromInclusive, final K toKey, final boolean toInclusive) {
            synchronized (this.mutex) {
                return Synchronized.navigableMap(this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
            }
        }
        
        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public NavigableMap<K, V> tailMap(final K fromKey, final boolean inclusive) {
            synchronized (this.mutex) {
                return Synchronized.navigableMap(this.delegate().tailMap(fromKey, inclusive), this.mutex);
            }
        }
        
        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return this.tailMap(fromKey, true);
        }
    }
    
    @GwtIncompatible
    private static class SynchronizedEntry<K, V> extends SynchronizedObject implements Map.Entry<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedEntry(final Map.Entry<K, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Map.Entry<K, V> delegate() {
            return (Map.Entry<K, V>)super.delegate();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            synchronized (this.mutex) {
                return this.delegate().equals(obj);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
        
        @Override
        public K getKey() {
            synchronized (this.mutex) {
                return this.delegate().getKey();
            }
        }
        
        @Override
        public V getValue() {
            synchronized (this.mutex) {
                return this.delegate().getValue();
            }
        }
        
        @Override
        public V setValue(final V value) {
            synchronized (this.mutex) {
                return this.delegate().setValue(value);
            }
        }
    }
    
    private static class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedQueue(final Queue<E> delegate, @CheckForNull final Object mutex) {
            super((Collection)delegate, mutex);
        }
        
        @Override
        Queue<E> delegate() {
            return (Queue<E>)(Queue)super.delegate();
        }
        
        @Override
        public E element() {
            synchronized (this.mutex) {
                return this.delegate().element();
            }
        }
        
        @Override
        public boolean offer(final E e) {
            synchronized (this.mutex) {
                return this.delegate().offer(e);
            }
        }
        
        @CheckForNull
        @Override
        public E peek() {
            synchronized (this.mutex) {
                return this.delegate().peek();
            }
        }
        
        @CheckForNull
        @Override
        public E poll() {
            synchronized (this.mutex) {
                return this.delegate().poll();
            }
        }
        
        @Override
        public E remove() {
            synchronized (this.mutex) {
                return this.delegate().remove();
            }
        }
    }
    
    private static final class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E>
    {
        private static final long serialVersionUID = 0L;
        
        SynchronizedDeque(final Deque<E> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Deque<E> delegate() {
            return (Deque<E>)(Deque)super.delegate();
        }
        
        @Override
        public void addFirst(final E e) {
            synchronized (this.mutex) {
                this.delegate().addFirst(e);
            }
        }
        
        @Override
        public void addLast(final E e) {
            synchronized (this.mutex) {
                this.delegate().addLast(e);
            }
        }
        
        @Override
        public boolean offerFirst(final E e) {
            synchronized (this.mutex) {
                return this.delegate().offerFirst(e);
            }
        }
        
        @Override
        public boolean offerLast(final E e) {
            synchronized (this.mutex) {
                return this.delegate().offerLast(e);
            }
        }
        
        @Override
        public E removeFirst() {
            synchronized (this.mutex) {
                return this.delegate().removeFirst();
            }
        }
        
        @Override
        public E removeLast() {
            synchronized (this.mutex) {
                return this.delegate().removeLast();
            }
        }
        
        @CheckForNull
        @Override
        public E pollFirst() {
            synchronized (this.mutex) {
                return this.delegate().pollFirst();
            }
        }
        
        @CheckForNull
        @Override
        public E pollLast() {
            synchronized (this.mutex) {
                return this.delegate().pollLast();
            }
        }
        
        @Override
        public E getFirst() {
            synchronized (this.mutex) {
                return this.delegate().getFirst();
            }
        }
        
        @Override
        public E getLast() {
            synchronized (this.mutex) {
                return this.delegate().getLast();
            }
        }
        
        @CheckForNull
        @Override
        public E peekFirst() {
            synchronized (this.mutex) {
                return this.delegate().peekFirst();
            }
        }
        
        @CheckForNull
        @Override
        public E peekLast() {
            synchronized (this.mutex) {
                return this.delegate().peekLast();
            }
        }
        
        @Override
        public boolean removeFirstOccurrence(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().removeFirstOccurrence(o);
            }
        }
        
        @Override
        public boolean removeLastOccurrence(@CheckForNull final Object o) {
            synchronized (this.mutex) {
                return this.delegate().removeLastOccurrence(o);
            }
        }
        
        @Override
        public void push(final E e) {
            synchronized (this.mutex) {
                this.delegate().push(e);
            }
        }
        
        @Override
        public E pop() {
            synchronized (this.mutex) {
                return this.delegate().pop();
            }
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            synchronized (this.mutex) {
                return this.delegate().descendingIterator();
            }
        }
    }
    
    private static final class SynchronizedTable<R, C, V> extends SynchronizedObject implements Table<R, C, V>
    {
        SynchronizedTable(final Table<R, C, V> delegate, @CheckForNull final Object mutex) {
            super(delegate, mutex);
        }
        
        @Override
        Table<R, C, V> delegate() {
            return (Table<R, C, V>)super.delegate();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            synchronized (this.mutex) {
                return this.delegate().contains(rowKey, columnKey);
            }
        }
        
        @Override
        public boolean containsRow(@CheckForNull final Object rowKey) {
            synchronized (this.mutex) {
                return this.delegate().containsRow(rowKey);
            }
        }
        
        @Override
        public boolean containsColumn(@CheckForNull final Object columnKey) {
            synchronized (this.mutex) {
                return this.delegate().containsColumn(columnKey);
            }
        }
        
        @Override
        public boolean containsValue(@CheckForNull final Object value) {
            synchronized (this.mutex) {
                return this.delegate().containsValue(value);
            }
        }
        
        @CheckForNull
        @Override
        public V get(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            synchronized (this.mutex) {
                return this.delegate().get(rowKey, columnKey);
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.mutex) {
                return this.delegate().isEmpty();
            }
        }
        
        @Override
        public int size() {
            synchronized (this.mutex) {
                return this.delegate().size();
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.mutex) {
                this.delegate().clear();
            }
        }
        
        @CheckForNull
        @Override
        public V put(final R rowKey, final C columnKey, final V value) {
            synchronized (this.mutex) {
                return this.delegate().put(rowKey, columnKey, value);
            }
        }
        
        @Override
        public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
            synchronized (this.mutex) {
                this.delegate().putAll(table);
            }
        }
        
        @CheckForNull
        @Override
        public V remove(@CheckForNull final Object rowKey, @CheckForNull final Object columnKey) {
            synchronized (this.mutex) {
                return this.delegate().remove(rowKey, columnKey);
            }
        }
        
        @Override
        public Map<C, V> row(final R rowKey) {
            synchronized (this.mutex) {
                return Synchronized.map(this.delegate().row(rowKey), this.mutex);
            }
        }
        
        @Override
        public Map<R, V> column(final C columnKey) {
            synchronized (this.mutex) {
                return Synchronized.map(this.delegate().column(columnKey), this.mutex);
            }
        }
        
        @Override
        public Set<Cell<R, C, V>> cellSet() {
            synchronized (this.mutex) {
                return Synchronized.set(this.delegate().cellSet(), this.mutex);
            }
        }
        
        @Override
        public Set<R> rowKeySet() {
            synchronized (this.mutex) {
                return Synchronized.set(this.delegate().rowKeySet(), this.mutex);
            }
        }
        
        @Override
        public Set<C> columnKeySet() {
            synchronized (this.mutex) {
                return Synchronized.set(this.delegate().columnKeySet(), this.mutex);
            }
        }
        
        @Override
        public Collection<V> values() {
            synchronized (this.mutex) {
                return (Collection<V>)collection((Collection<Object>)this.delegate().values(), this.mutex);
            }
        }
        
        @Override
        public Map<R, Map<C, V>> rowMap() {
            synchronized (this.mutex) {
                return Synchronized.map((Map<R, Map<C, V>>)Maps.transformValues((Map<K, Map<C, V>>)this.delegate().rowMap(), (com.google.common.base.Function<? super Map<C, V>, V>)new com.google.common.base.Function<Map<C, V>, Map<C, V>>() {
                    @Override
                    public Map<C, V> apply(final Map<C, V> t) {
                        return Synchronized.map(t, SynchronizedTable.this.mutex);
                    }
                }), this.mutex);
            }
        }
        
        @Override
        public Map<C, Map<R, V>> columnMap() {
            synchronized (this.mutex) {
                return Synchronized.map((Map<C, Map<R, V>>)Maps.transformValues((Map<K, Map<R, V>>)this.delegate().columnMap(), (com.google.common.base.Function<? super Map<R, V>, V>)new com.google.common.base.Function<Map<R, V>, Map<R, V>>() {
                    @Override
                    public Map<R, V> apply(final Map<R, V> t) {
                        return Synchronized.map(t, SynchronizedTable.this.mutex);
                    }
                }), this.mutex);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.mutex) {
                return this.delegate().hashCode();
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (this == obj) {
                return true;
            }
            synchronized (this.mutex) {
                return this.delegate().equals(obj);
            }
        }
    }
}
