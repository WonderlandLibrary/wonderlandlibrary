// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.Function;
import java.util.Objects;
import java.util.ListIterator;
import java.util.NavigableSet;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.ConcurrentModificationException;
import java.util.AbstractCollection;
import java.util.function.BiConsumer;
import java.util.Spliterator;
import java.util.SortedMap;
import java.util.NavigableMap;
import java.util.Set;
import java.util.RandomAccess;
import java.util.List;
import java.util.Collections;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable
{
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;
    private static final long serialVersionUID = 2447537837011683357L;
    
    protected AbstractMapBasedMultimap(final Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }
    
    final void setMap(final Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        for (final Collection<V> values : map.values()) {
            Preconditions.checkArgument(!values.isEmpty());
            this.totalSize += values.size();
        }
    }
    
    Collection<V> createUnmodifiableEmptyCollection() {
        return this.unmodifiableCollectionSubclass(this.createCollection());
    }
    
    abstract Collection<V> createCollection();
    
    Collection<V> createCollection(@ParametricNullness final K key) {
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
    public boolean containsKey(@CheckForNull final Object key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
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
        else {
            if (collection.add(value)) {
                ++this.totalSize;
                return true;
            }
            return false;
        }
    }
    
    private Collection<V> getOrCreateCollection(@ParametricNullness final K key) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
            this.map.put(key, collection);
        }
        return collection;
    }
    
    @Override
    public Collection<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        final Iterator<? extends V> iterator = values.iterator();
        if (!iterator.hasNext()) {
            return this.removeAll(key);
        }
        final Collection<V> collection = this.getOrCreateCollection(key);
        final Collection<V> oldValues = this.createCollection();
        oldValues.addAll((Collection<? extends V>)collection);
        this.totalSize -= collection.size();
        collection.clear();
        while (iterator.hasNext()) {
            if (collection.add((V)iterator.next())) {
                ++this.totalSize;
            }
        }
        return this.unmodifiableCollectionSubclass(oldValues);
    }
    
    @Override
    public Collection<V> removeAll(@CheckForNull final Object key) {
        final Collection<V> collection = this.map.remove(key);
        if (collection == null) {
            return this.createUnmodifiableEmptyCollection();
        }
        final Collection<V> output = this.createCollection();
        output.addAll((Collection<? extends V>)collection);
        this.totalSize -= collection.size();
        collection.clear();
        return this.unmodifiableCollectionSubclass(output);
    }
    
     <E> Collection<E> unmodifiableCollectionSubclass(final Collection<E> collection) {
        return Collections.unmodifiableCollection((Collection<? extends E>)collection);
    }
    
    @Override
    public void clear() {
        for (final Collection<V> collection : this.map.values()) {
            collection.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }
    
    @Override
    public Collection<V> get(@ParametricNullness final K key) {
        Collection<V> collection = this.map.get(key);
        if (collection == null) {
            collection = this.createCollection(key);
        }
        return this.wrapCollection(key, collection);
    }
    
    Collection<V> wrapCollection(@ParametricNullness final K key, final Collection<V> collection) {
        return new WrappedCollection(key, collection, null);
    }
    
    final List<V> wrapList(@ParametricNullness final K key, final List<V> list, @CheckForNull final WrappedCollection ancestor) {
        return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
    }
    
    private static <E> Iterator<E> iteratorOrListIterator(final Collection<E> collection) {
        return (collection instanceof List) ? ((List)collection).listIterator() : collection.iterator();
    }
    
    @Override
    Set<K> createKeySet() {
        return (Set<K>)new KeySet(this.map);
    }
    
    final Set<K> createMaybeNavigableKeySet() {
        if (this.map instanceof NavigableMap) {
            return (Set<K>)new NavigableKeySet((NavigableMap)this.map);
        }
        if (this.map instanceof SortedMap) {
            return (Set<K>)new SortedKeySet((SortedMap)this.map);
        }
        return (Set<K>)new KeySet(this.map);
    }
    
    private void removeValuesForKey(@CheckForNull final Object key) {
        final Collection<V> collection = Maps.safeRemove(this.map, key);
        if (collection != null) {
            final int count = collection.size();
            collection.clear();
            this.totalSize -= count;
        }
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    @Override
    Collection<V> createValues() {
        return (Collection<V>)new Values();
    }
    
    @Override
    Iterator<V> valueIterator() {
        return new Itr<V>(this) {
            @ParametricNullness
            @Override
            V output(@ParametricNullness final K key, @ParametricNullness final V value) {
                return value;
            }
        };
    }
    
    @Override
    Spliterator<V> valueSpliterator() {
        return CollectSpliterators.flatMap(this.map.values().spliterator(), Collection::spliterator, 64, this.size());
    }
    
    @Override
    Multiset<K> createKeys() {
        return (Multiset<K>)new Multimaps.Keys((Multimap<Object, Object>)this);
    }
    
    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return super.entries();
    }
    
    @Override
    Collection<Map.Entry<K, V>> createEntries() {
        if (this instanceof SetMultimap) {
            return (Collection<Map.Entry<K, V>>)new EntrySet();
        }
        return (Collection<Map.Entry<K, V>>)new Entries();
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        return new Itr<Map.Entry<K, V>>(this) {
            @Override
            Map.Entry<K, V> output(@ParametricNullness final K key, @ParametricNullness final V value) {
                return Maps.immutableEntry(key, value);
            }
        };
    }
    
    @Override
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        final K key;
        final Collection<V> valueCollection;
        return CollectSpliterators.flatMap(this.map.entrySet().spliterator(), keyToValueCollectionEntry -> {
            key = keyToValueCollectionEntry.getKey();
            valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue();
            return (Spliterator<Map.Entry<K, V>>)CollectSpliterators.map(valueCollection.spliterator(), value -> Maps.immutableEntry(key, value));
        }, 64, this.size());
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> action) {
        Preconditions.checkNotNull(action);
        this.map.forEach((key, valueCollection) -> valueCollection.forEach(value -> action.accept((Object)key, (Object)value)));
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return new AsMap(this.map);
    }
    
    final Map<K, Collection<V>> createMaybeNavigableAsMap() {
        if (this.map instanceof NavigableMap) {
            return new NavigableAsMap((NavigableMap)this.map);
        }
        if (this.map instanceof SortedMap) {
            return new SortedAsMap((SortedMap)this.map);
        }
        return new AsMap(this.map);
    }
    
    class WrappedCollection extends AbstractCollection<V>
    {
        @ParametricNullness
        final K key;
        Collection<V> delegate;
        @CheckForNull
        final WrappedCollection ancestor;
        @CheckForNull
        final Collection<V> ancestorDelegate;
        
        WrappedCollection(final K key, @CheckForNull final Collection<V> delegate, final WrappedCollection ancestor) {
            this.key = key;
            this.delegate = delegate;
            this.ancestor = ancestor;
            this.ancestorDelegate = ((ancestor == null) ? null : ancestor.getDelegate());
        }
        
        void refreshIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            }
            else if (this.delegate.isEmpty()) {
                final Collection<V> newDelegate = AbstractMapBasedMultimap.this.map.get(this.key);
                if (newDelegate != null) {
                    this.delegate = newDelegate;
                }
            }
        }
        
        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            }
            else if (this.delegate.isEmpty()) {
                AbstractMapBasedMultimap.this.map.remove(this.key);
            }
        }
        
        @ParametricNullness
        K getKey() {
            return this.key;
        }
        
        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            }
            else {
                AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
            }
        }
        
        @Override
        public int size() {
            this.refreshIfEmpty();
            return this.delegate.size();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
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
        public boolean add(@ParametricNullness final V value) {
            this.refreshIfEmpty();
            final boolean wasEmpty = this.delegate.isEmpty();
            final boolean changed = this.delegate.add(value);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        @CheckForNull
        WrappedCollection getAncestor() {
            return this.ancestor;
        }
        
        @Override
        public boolean addAll(final Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.delegate.addAll(collection);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            this.refreshIfEmpty();
            return this.delegate.contains(o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            this.refreshIfEmpty();
            return this.delegate.containsAll(c);
        }
        
        @Override
        public void clear() {
            final int oldSize = this.size();
            if (oldSize == 0) {
                return;
            }
            this.delegate.clear();
            AbstractMapBasedMultimap.this.totalSize -= oldSize;
            this.removeIfEmpty();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            this.refreshIfEmpty();
            final boolean changed = this.delegate.remove(o);
            if (changed) {
                AbstractMapBasedMultimap.this.totalSize--;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.delegate.removeAll(c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            Preconditions.checkNotNull(c);
            final int oldSize = this.size();
            final boolean changed = this.delegate.retainAll(c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
        
        class WrappedIterator implements Iterator<V>
        {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;
            
            WrappedIterator() {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = (Iterator<V>)iteratorOrListIterator((Collection<Object>)WrappedCollection.this.delegate);
            }
            
            WrappedIterator(final Iterator<V> delegateIterator) {
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
            
            @ParametricNullness
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
    
    class WrappedSet extends WrappedCollection implements Set<V>
    {
        WrappedSet(final K key, final Set<V> delegate) {
            super(key, delegate, null);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
            if (changed) {
                final int newSize = this.delegate.size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                this.removeIfEmpty();
            }
            return changed;
        }
    }
    
    class WrappedSortedSet extends WrappedCollection implements SortedSet<V>
    {
        WrappedSortedSet(final K key, @CheckForNull final SortedSet<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
        
        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet<V>)(SortedSet)this.getDelegate();
        }
        
        @CheckForNull
        @Override
        public Comparator<? super V> comparator() {
            return this.getSortedSetDelegate().comparator();
        }
        
        @ParametricNullness
        @Override
        public V first() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().first();
        }
        
        @ParametricNullness
        @Override
        public V last() {
            this.refreshIfEmpty();
            return this.getSortedSetDelegate().last();
        }
        
        @Override
        public SortedSet<V> headSet(@ParametricNullness final V toElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().headSet(toElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        @Override
        public SortedSet<V> subSet(@ParametricNullness final V fromElement, @ParametricNullness final V toElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().subSet(fromElement, toElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        @Override
        public SortedSet<V> tailSet(@ParametricNullness final V fromElement) {
            this.refreshIfEmpty();
            return new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().tailSet(fromElement), (this.getAncestor() == null) ? this : this.getAncestor());
        }
    }
    
    class WrappedNavigableSet extends WrappedSortedSet implements NavigableSet<V>
    {
        WrappedNavigableSet(final K key, @CheckForNull final NavigableSet<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
        
        @Override
        NavigableSet<V> getSortedSetDelegate() {
            return (NavigableSet<V>)(NavigableSet)super.getSortedSetDelegate();
        }
        
        @CheckForNull
        @Override
        public V lower(@ParametricNullness final V v) {
            return this.getSortedSetDelegate().lower(v);
        }
        
        @CheckForNull
        @Override
        public V floor(@ParametricNullness final V v) {
            return this.getSortedSetDelegate().floor(v);
        }
        
        @CheckForNull
        @Override
        public V ceiling(@ParametricNullness final V v) {
            return this.getSortedSetDelegate().ceiling(v);
        }
        
        @CheckForNull
        @Override
        public V higher(@ParametricNullness final V v) {
            return this.getSortedSetDelegate().higher(v);
        }
        
        @CheckForNull
        @Override
        public V pollFirst() {
            return Iterators.pollNext(this.iterator());
        }
        
        @CheckForNull
        @Override
        public V pollLast() {
            return Iterators.pollNext(this.descendingIterator());
        }
        
        private NavigableSet<V> wrap(final NavigableSet<V> wrapped) {
            return new WrappedNavigableSet(this.key, wrapped, (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        @Override
        public NavigableSet<V> descendingSet() {
            return this.wrap(this.getSortedSetDelegate().descendingSet());
        }
        
        @Override
        public Iterator<V> descendingIterator() {
            return new WrappedIterator(this.getSortedSetDelegate().descendingIterator());
        }
        
        @Override
        public NavigableSet<V> subSet(@ParametricNullness final V fromElement, final boolean fromInclusive, @ParametricNullness final V toElement, final boolean toInclusive) {
            return this.wrap(this.getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        @Override
        public NavigableSet<V> headSet(@ParametricNullness final V toElement, final boolean inclusive) {
            return this.wrap(this.getSortedSetDelegate().headSet(toElement, inclusive));
        }
        
        @Override
        public NavigableSet<V> tailSet(@ParametricNullness final V fromElement, final boolean inclusive) {
            return this.wrap(this.getSortedSetDelegate().tailSet(fromElement, inclusive));
        }
    }
    
    class WrappedList extends WrappedCollection implements List<V>
    {
        WrappedList(final K key, @CheckForNull final List<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
        
        List<V> getListDelegate() {
            return (List<V>)(List)this.getDelegate();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends V> c) {
            if (c.isEmpty()) {
                return false;
            }
            final int oldSize = this.size();
            final boolean changed = this.getListDelegate().addAll(index, c);
            if (changed) {
                final int newSize = this.getDelegate().size();
                AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
                if (oldSize == 0) {
                    this.addToMap();
                }
            }
            return changed;
        }
        
        @ParametricNullness
        @Override
        public V get(final int index) {
            this.refreshIfEmpty();
            return this.getListDelegate().get(index);
        }
        
        @ParametricNullness
        @Override
        public V set(final int index, @ParametricNullness final V element) {
            this.refreshIfEmpty();
            return this.getListDelegate().set(index, element);
        }
        
        @Override
        public void add(final int index, @ParametricNullness final V element) {
            this.refreshIfEmpty();
            final boolean wasEmpty = this.getDelegate().isEmpty();
            this.getListDelegate().add(index, element);
            AbstractMapBasedMultimap.this.totalSize++;
            if (wasEmpty) {
                this.addToMap();
            }
        }
        
        @ParametricNullness
        @Override
        public V remove(final int index) {
            this.refreshIfEmpty();
            final V value = this.getListDelegate().remove(index);
            AbstractMapBasedMultimap.this.totalSize--;
            this.removeIfEmpty();
            return value;
        }
        
        @Override
        public int indexOf(@CheckForNull final Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().indexOf(o);
        }
        
        @Override
        public int lastIndexOf(@CheckForNull final Object o) {
            this.refreshIfEmpty();
            return this.getListDelegate().lastIndexOf(o);
        }
        
        @Override
        public ListIterator<V> listIterator() {
            this.refreshIfEmpty();
            return new WrappedListIterator();
        }
        
        @Override
        public ListIterator<V> listIterator(final int index) {
            this.refreshIfEmpty();
            return new WrappedListIterator(index);
        }
        
        @Override
        public List<V> subList(final int fromIndex, final int toIndex) {
            this.refreshIfEmpty();
            return AbstractMapBasedMultimap.this.wrapList(this.getKey(), this.getListDelegate().subList(fromIndex, toIndex), (this.getAncestor() == null) ? this : this.getAncestor());
        }
        
        private class WrappedListIterator extends WrappedIterator implements ListIterator<V>
        {
            WrappedListIterator() {
            }
            
            public WrappedListIterator(final int index) {
                super(WrappedList.this.getListDelegate().listIterator(index));
            }
            
            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator<V>)(ListIterator)this.getDelegateIterator();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.getDelegateListIterator().hasPrevious();
            }
            
            @ParametricNullness
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
            public void set(@ParametricNullness final V value) {
                this.getDelegateListIterator().set(value);
            }
            
            @Override
            public void add(@ParametricNullness final V value) {
                final boolean wasEmpty = WrappedList.this.isEmpty();
                this.getDelegateListIterator().add(value);
                AbstractMapBasedMultimap.this.totalSize++;
                if (wasEmpty) {
                    WrappedList.this.addToMap();
                }
            }
        }
    }
    
    private class RandomAccessWrappedList extends WrappedList implements RandomAccess
    {
        RandomAccessWrappedList(@ParametricNullness final AbstractMapBasedMultimap this$0, final K key, @CheckForNull final List<V> delegate, final WrappedCollection ancestor) {
            super(key, delegate, ancestor);
        }
    }
    
    private class KeySet extends Maps.KeySet<K, Collection<V>>
    {
        KeySet(final Map<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        @Override
        public Iterator<K> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.map().entrySet().iterator();
            return new Iterator<K>() {
                @CheckForNull
                Map.Entry<K, Collection<V>> entry;
                
                @Override
                public boolean hasNext() {
                    return entryIterator.hasNext();
                }
                
                @ParametricNullness
                @Override
                public K next() {
                    this.entry = entryIterator.next();
                    return this.entry.getKey();
                }
                
                @Override
                public void remove() {
                    Preconditions.checkState(this.entry != null, (Object)"no calls to next() since the last call to remove()");
                    final Collection<V> collection = this.entry.getValue();
                    entryIterator.remove();
                    AbstractMapBasedMultimap.this.totalSize -= collection.size();
                    collection.clear();
                    this.entry = null;
                }
            };
        }
        
        @Override
        public Spliterator<K> spliterator() {
            return this.map().keySet().spliterator();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key) {
            int count = 0;
            final Collection<V> collection = this.map().remove(key);
            if (collection != null) {
                count = collection.size();
                collection.clear();
                AbstractMapBasedMultimap.this.totalSize -= count;
            }
            return count > 0;
        }
        
        @Override
        public void clear() {
            Iterators.clear(this.iterator());
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.map().keySet().containsAll(c);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            return this == object || this.map().keySet().equals(object);
        }
        
        @Override
        public int hashCode() {
            return this.map().keySet().hashCode();
        }
    }
    
    private class SortedKeySet extends KeySet implements SortedSet<K>
    {
        SortedKeySet(final SortedMap<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap<K, Collection<V>>)(SortedMap)super.map();
        }
        
        @CheckForNull
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        @ParametricNullness
        @Override
        public K first() {
            return this.sortedMap().firstKey();
        }
        
        @Override
        public SortedSet<K> headSet(@ParametricNullness final K toElement) {
            return new SortedKeySet(this.sortedMap().headMap(toElement));
        }
        
        @ParametricNullness
        @Override
        public K last() {
            return this.sortedMap().lastKey();
        }
        
        @Override
        public SortedSet<K> subSet(@ParametricNullness final K fromElement, @ParametricNullness final K toElement) {
            return new SortedKeySet(this.sortedMap().subMap(fromElement, toElement));
        }
        
        @Override
        public SortedSet<K> tailSet(@ParametricNullness final K fromElement) {
            return new SortedKeySet(this.sortedMap().tailMap(fromElement));
        }
    }
    
    class NavigableKeySet extends SortedKeySet implements NavigableSet<K>
    {
        NavigableKeySet(final NavigableMap<K, Collection<V>> subMap) {
            super(subMap);
        }
        
        @Override
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap<K, Collection<V>>)(NavigableMap)super.sortedMap();
        }
        
        @CheckForNull
        @Override
        public K lower(@ParametricNullness final K k) {
            return this.sortedMap().lowerKey(k);
        }
        
        @CheckForNull
        @Override
        public K floor(@ParametricNullness final K k) {
            return this.sortedMap().floorKey(k);
        }
        
        @CheckForNull
        @Override
        public K ceiling(@ParametricNullness final K k) {
            return this.sortedMap().ceilingKey(k);
        }
        
        @CheckForNull
        @Override
        public K higher(@ParametricNullness final K k) {
            return this.sortedMap().higherKey(k);
        }
        
        @CheckForNull
        @Override
        public K pollFirst() {
            return Iterators.pollNext(this.iterator());
        }
        
        @CheckForNull
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
        public NavigableSet<K> headSet(@ParametricNullness final K toElement) {
            return this.headSet(toElement, false);
        }
        
        @Override
        public NavigableSet<K> headSet(@ParametricNullness final K toElement, final boolean inclusive) {
            return new NavigableKeySet(this.sortedMap().headMap(toElement, inclusive));
        }
        
        @Override
        public NavigableSet<K> subSet(@ParametricNullness final K fromElement, @ParametricNullness final K toElement) {
            return this.subSet(fromElement, true, toElement, false);
        }
        
        @Override
        public NavigableSet<K> subSet(@ParametricNullness final K fromElement, final boolean fromInclusive, @ParametricNullness final K toElement, final boolean toInclusive) {
            return new NavigableKeySet(this.sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        @Override
        public NavigableSet<K> tailSet(@ParametricNullness final K fromElement) {
            return this.tailSet(fromElement, true);
        }
        
        @Override
        public NavigableSet<K> tailSet(@ParametricNullness final K fromElement, final boolean inclusive) {
            return new NavigableKeySet(this.sortedMap().tailMap(fromElement, inclusive));
        }
    }
    
    private abstract class Itr<T> implements Iterator<T>
    {
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        @CheckForNull
        K key;
        @CheckForNull
        Collection<V> collection;
        Iterator<V> valueIterator;
        
        Itr() {
            this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
            this.key = null;
            this.collection = null;
            this.valueIterator = Iterators.emptyModifiableIterator();
        }
        
        abstract T output(@ParametricNullness final K p0, @ParametricNullness final V p1);
        
        @Override
        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }
        
        @Override
        public T next() {
            if (!this.valueIterator.hasNext()) {
                final Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
                this.key = mapEntry.getKey();
                this.collection = mapEntry.getValue();
                this.valueIterator = this.collection.iterator();
            }
            return this.output(NullnessCasts.uncheckedCastNullableTToT(this.key), this.valueIterator.next());
        }
        
        @Override
        public void remove() {
            this.valueIterator.remove();
            if (Objects.requireNonNull(this.collection).isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMapBasedMultimap.this.totalSize--;
        }
    }
    
    private class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>>
    {
        final transient Map<K, Collection<V>> submap;
        
        AsMap(final Map<K, Collection<V>> submap) {
            this.submap = submap;
        }
        
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return (Set<Map.Entry<K, Collection<V>>>)new AsMapEntries();
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            return Maps.safeContainsKey(this.submap, key);
        }
        
        @CheckForNull
        @Override
        public Collection<V> get(@CheckForNull final Object key) {
            final Collection<V> collection = Maps.safeGet(this.submap, key);
            if (collection == null) {
                return null;
            }
            final K k = (K)key;
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
        
        @CheckForNull
        @Override
        public Collection<V> remove(@CheckForNull final Object key) {
            final Collection<V> collection = this.submap.remove(key);
            if (collection == null) {
                return null;
            }
            final Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection<? extends V>)collection);
            AbstractMapBasedMultimap.this.totalSize -= collection.size();
            collection.clear();
            return output;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
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
            }
            else {
                Iterators.clear(new AsMapIterator());
            }
        }
        
        Map.Entry<K, Collection<V>> wrapEntry(final Map.Entry<K, Collection<V>> entry) {
            final K key = entry.getKey();
            return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
        }
        
        class AsMapEntries extends Maps.EntrySet<K, Collection<V>>
        {
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
                return CollectSpliterators.map(AsMap.this.submap.entrySet().spliterator(), (Function<? super Map.Entry<K, Collection<V>>, ? extends Map.Entry<K, Collection<V>>>)AsMap.this::wrapEntry);
            }
            
            @Override
            public boolean contains(@CheckForNull final Object o) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), o);
            }
            
            @Override
            public boolean remove(@CheckForNull final Object o) {
                if (!this.contains(o)) {
                    return false;
                }
                final Map.Entry<?, ?> entry = Objects.requireNonNull(o);
                AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }
        
        class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>>
        {
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
            @CheckForNull
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
                final Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
                this.collection = entry.getValue();
                return AsMap.this.wrapEntry(entry);
            }
            
            @Override
            public void remove() {
                Preconditions.checkState(this.collection != null, (Object)"no calls to next() since the last call to remove()");
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
                this.collection.clear();
                this.collection = null;
            }
        }
    }
    
    private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>>
    {
        @CheckForNull
        SortedSet<K> sortedKeySet;
        
        SortedAsMap(final SortedMap<K, Collection<V>> submap) {
            super(submap);
        }
        
        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap<K, Collection<V>>)(SortedMap)this.submap;
        }
        
        @CheckForNull
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap().comparator();
        }
        
        @ParametricNullness
        @Override
        public K firstKey() {
            return this.sortedMap().firstKey();
        }
        
        @ParametricNullness
        @Override
        public K lastKey() {
            return this.sortedMap().lastKey();
        }
        
        @Override
        public SortedMap<K, Collection<V>> headMap(@ParametricNullness final K toKey) {
            return new SortedAsMap(this.sortedMap().headMap(toKey));
        }
        
        @Override
        public SortedMap<K, Collection<V>> subMap(@ParametricNullness final K fromKey, @ParametricNullness final K toKey) {
            return new SortedAsMap(this.sortedMap().subMap(fromKey, toKey));
        }
        
        @Override
        public SortedMap<K, Collection<V>> tailMap(@ParametricNullness final K fromKey) {
            return new SortedAsMap(this.sortedMap().tailMap(fromKey));
        }
        
        @Override
        public SortedSet<K> keySet() {
            final SortedSet<K> result = this.sortedKeySet;
            return (result == null) ? (this.sortedKeySet = this.createKeySet()) : result;
        }
        
        @Override
        SortedSet<K> createKeySet() {
            return new AbstractMapBasedMultimap.SortedKeySet(this.sortedMap());
        }
    }
    
    class NavigableAsMap extends SortedAsMap implements NavigableMap<K, Collection<V>>
    {
        NavigableAsMap(final NavigableMap<K, Collection<V>> submap) {
            super(submap);
        }
        
        @Override
        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap<K, Collection<V>>)(NavigableMap)super.sortedMap();
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> lowerEntry(@ParametricNullness final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().lowerEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @CheckForNull
        @Override
        public K lowerKey(@ParametricNullness final K key) {
            return this.sortedMap().lowerKey(key);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> floorEntry(@ParametricNullness final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().floorEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @CheckForNull
        @Override
        public K floorKey(@ParametricNullness final K key) {
            return this.sortedMap().floorKey(key);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> ceilingEntry(@ParametricNullness final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().ceilingEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @CheckForNull
        @Override
        public K ceilingKey(@ParametricNullness final K key) {
            return this.sortedMap().ceilingKey(key);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> higherEntry(@ParametricNullness final K key) {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().higherEntry(key);
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @CheckForNull
        @Override
        public K higherKey(@ParametricNullness final K key) {
            return this.sortedMap().higherKey(key);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> firstEntry() {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().firstEntry();
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> lastEntry() {
            final Map.Entry<K, Collection<V>> entry = this.sortedMap().lastEntry();
            return (entry == null) ? null : this.wrapEntry(entry);
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> pollFirstEntry() {
            return this.pollAsMapEntry(this.entrySet().iterator());
        }
        
        @CheckForNull
        @Override
        public Map.Entry<K, Collection<V>> pollLastEntry() {
            return (Map.Entry<K, Collection<V>>)this.pollAsMapEntry((Iterator<Map.Entry<Object, Collection<V>>>)this.descendingMap().entrySet().iterator());
        }
        
        @CheckForNull
        Map.Entry<K, Collection<V>> pollAsMapEntry(final Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
            if (!entryIterator.hasNext()) {
                return null;
            }
            final Map.Entry<K, Collection<V>> entry = entryIterator.next();
            final Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection<? extends V>)entry.getValue());
            entryIterator.remove();
            return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
        }
        
        @Override
        public NavigableMap<K, Collection<V>> descendingMap() {
            return new NavigableAsMap(this.sortedMap().descendingMap());
        }
        
        @Override
        public NavigableSet<K> keySet() {
            return (NavigableSet<K>)(NavigableSet)super.keySet();
        }
        
        @Override
        NavigableSet<K> createKeySet() {
            return new AbstractMapBasedMultimap.NavigableKeySet(this.sortedMap());
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
        public NavigableMap<K, Collection<V>> subMap(@ParametricNullness final K fromKey, @ParametricNullness final K toKey) {
            return this.subMap(fromKey, true, toKey, false);
        }
        
        @Override
        public NavigableMap<K, Collection<V>> subMap(@ParametricNullness final K fromKey, final boolean fromInclusive, @ParametricNullness final K toKey, final boolean toInclusive) {
            return new NavigableAsMap(this.sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
        }
        
        @Override
        public NavigableMap<K, Collection<V>> headMap(@ParametricNullness final K toKey) {
            return this.headMap(toKey, false);
        }
        
        @Override
        public NavigableMap<K, Collection<V>> headMap(@ParametricNullness final K toKey, final boolean inclusive) {
            return new NavigableAsMap(this.sortedMap().headMap(toKey, inclusive));
        }
        
        @Override
        public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness final K fromKey) {
            return this.tailMap(fromKey, true);
        }
        
        @Override
        public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness final K fromKey, final boolean inclusive) {
            return new NavigableAsMap(this.sortedMap().tailMap(fromKey, inclusive));
        }
    }
}
