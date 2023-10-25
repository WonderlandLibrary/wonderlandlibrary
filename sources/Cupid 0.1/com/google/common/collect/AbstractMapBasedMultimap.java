package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
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
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
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
    for (Collection<V> values : map.values()) {
      Preconditions.checkArgument(!values.isEmpty());
      this.totalSize += values.size();
    } 
  }
  
  Collection<V> createUnmodifiableEmptyCollection() {
    return unmodifiableCollectionSubclass(createCollection());
  }
  
  Collection<V> createCollection(@Nullable K key) {
    return createCollection();
  }
  
  Map<K, Collection<V>> backingMap() {
    return this.map;
  }
  
  public int size() {
    return this.totalSize;
  }
  
  public boolean containsKey(@Nullable Object key) {
    return this.map.containsKey(key);
  }
  
  public boolean put(@Nullable K key, @Nullable V value) {
    Collection<V> collection = this.map.get(key);
    if (collection == null) {
      collection = createCollection(key);
      if (collection.add(value)) {
        this.totalSize++;
        this.map.put(key, collection);
        return true;
      } 
      throw new AssertionError("New Collection violated the Collection spec");
    } 
    if (collection.add(value)) {
      this.totalSize++;
      return true;
    } 
    return false;
  }
  
  private Collection<V> getOrCreateCollection(@Nullable K key) {
    Collection<V> collection = this.map.get(key);
    if (collection == null) {
      collection = createCollection(key);
      this.map.put(key, collection);
    } 
    return collection;
  }
  
  public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    Iterator<? extends V> iterator = values.iterator();
    if (!iterator.hasNext())
      return removeAll(key); 
    Collection<V> collection = getOrCreateCollection(key);
    Collection<V> oldValues = createCollection();
    oldValues.addAll(collection);
    this.totalSize -= collection.size();
    collection.clear();
    while (iterator.hasNext()) {
      if (collection.add(iterator.next()))
        this.totalSize++; 
    } 
    return unmodifiableCollectionSubclass(oldValues);
  }
  
  public Collection<V> removeAll(@Nullable Object key) {
    Collection<V> collection = this.map.remove(key);
    if (collection == null)
      return createUnmodifiableEmptyCollection(); 
    Collection<V> output = createCollection();
    output.addAll(collection);
    this.totalSize -= collection.size();
    collection.clear();
    return unmodifiableCollectionSubclass(output);
  }
  
  Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
    if (collection instanceof SortedSet)
      return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
    if (collection instanceof Set)
      return Collections.unmodifiableSet((Set<? extends V>)collection); 
    if (collection instanceof List)
      return Collections.unmodifiableList((List<? extends V>)collection); 
    return Collections.unmodifiableCollection(collection);
  }
  
  public void clear() {
    for (Collection<V> collection : this.map.values())
      collection.clear(); 
    this.map.clear();
    this.totalSize = 0;
  }
  
  public Collection<V> get(@Nullable K key) {
    Collection<V> collection = this.map.get(key);
    if (collection == null)
      collection = createCollection(key); 
    return wrapCollection(key, collection);
  }
  
  Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
    if (collection instanceof SortedSet)
      return new WrappedSortedSet(key, (SortedSet<V>)collection, null); 
    if (collection instanceof Set)
      return new WrappedSet(key, (Set<V>)collection); 
    if (collection instanceof List)
      return wrapList(key, (List<V>)collection, null); 
    return new WrappedCollection(key, collection, null);
  }
  
  private List<V> wrapList(@Nullable K key, List<V> list, @Nullable WrappedCollection ancestor) {
    return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
  }
  
  private class WrappedCollection extends AbstractCollection<V> {
    final K key;
    
    Collection<V> delegate;
    
    final WrappedCollection ancestor;
    
    final Collection<V> ancestorDelegate;
    
    WrappedCollection(K key, @Nullable Collection<V> delegate, WrappedCollection ancestor) {
      this.key = key;
      this.delegate = delegate;
      this.ancestor = ancestor;
      this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
    }
    
    void refreshIfEmpty() {
      if (this.ancestor != null) {
        this.ancestor.refreshIfEmpty();
        if (this.ancestor.getDelegate() != this.ancestorDelegate)
          throw new ConcurrentModificationException(); 
      } else if (this.delegate.isEmpty()) {
        Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
        if (newDelegate != null)
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
    
    public int size() {
      refreshIfEmpty();
      return this.delegate.size();
    }
    
    public boolean equals(@Nullable Object object) {
      if (object == this)
        return true; 
      refreshIfEmpty();
      return this.delegate.equals(object);
    }
    
    public int hashCode() {
      refreshIfEmpty();
      return this.delegate.hashCode();
    }
    
    public String toString() {
      refreshIfEmpty();
      return this.delegate.toString();
    }
    
    Collection<V> getDelegate() {
      return this.delegate;
    }
    
    public Iterator<V> iterator() {
      refreshIfEmpty();
      return new WrappedIterator();
    }
    
    class WrappedIterator implements Iterator<V> {
      final Iterator<V> delegateIterator;
      
      final Collection<V> originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
      
      WrappedIterator() {
        this.delegateIterator = AbstractMapBasedMultimap.this.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
      }
      
      WrappedIterator(Iterator<V> delegateIterator) {
        this.delegateIterator = delegateIterator;
      }
      
      void validateIterator() {
        AbstractMapBasedMultimap.WrappedCollection.this.refreshIfEmpty();
        if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != this.originalDelegate)
          throw new ConcurrentModificationException(); 
      }
      
      public boolean hasNext() {
        validateIterator();
        return this.delegateIterator.hasNext();
      }
      
      public V next() {
        validateIterator();
        return this.delegateIterator.next();
      }
      
      public void remove() {
        this.delegateIterator.remove();
        AbstractMapBasedMultimap.this.totalSize--;
        AbstractMapBasedMultimap.WrappedCollection.this.removeIfEmpty();
      }
      
      Iterator<V> getDelegateIterator() {
        validateIterator();
        return this.delegateIterator;
      }
    }
    
    public boolean add(V value) {
      refreshIfEmpty();
      boolean wasEmpty = this.delegate.isEmpty();
      boolean changed = this.delegate.add(value);
      if (changed) {
        AbstractMapBasedMultimap.this.totalSize++;
        if (wasEmpty)
          addToMap(); 
      } 
      return changed;
    }
    
    WrappedCollection getAncestor() {
      return this.ancestor;
    }
    
    public boolean addAll(Collection<? extends V> collection) {
      if (collection.isEmpty())
        return false; 
      int oldSize = size();
      boolean changed = this.delegate.addAll(collection);
      if (changed) {
        int newSize = this.delegate.size();
        AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
        if (oldSize == 0)
          addToMap(); 
      } 
      return changed;
    }
    
    public boolean contains(Object o) {
      refreshIfEmpty();
      return this.delegate.contains(o);
    }
    
    public boolean containsAll(Collection<?> c) {
      refreshIfEmpty();
      return this.delegate.containsAll(c);
    }
    
    public void clear() {
      int oldSize = size();
      if (oldSize == 0)
        return; 
      this.delegate.clear();
      AbstractMapBasedMultimap.this.totalSize -= oldSize;
      removeIfEmpty();
    }
    
    public boolean remove(Object o) {
      refreshIfEmpty();
      boolean changed = this.delegate.remove(o);
      if (changed) {
        AbstractMapBasedMultimap.this.totalSize--;
        removeIfEmpty();
      } 
      return changed;
    }
    
    public boolean removeAll(Collection<?> c) {
      if (c.isEmpty())
        return false; 
      int oldSize = size();
      boolean changed = this.delegate.removeAll(c);
      if (changed) {
        int newSize = this.delegate.size();
        AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
        removeIfEmpty();
      } 
      return changed;
    }
    
    public boolean retainAll(Collection<?> c) {
      Preconditions.checkNotNull(c);
      int oldSize = size();
      boolean changed = this.delegate.retainAll(c);
      if (changed) {
        int newSize = this.delegate.size();
        AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
        removeIfEmpty();
      } 
      return changed;
    }
  }
  
  private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
    return (collection instanceof List) ? ((List<V>)collection).listIterator() : collection.iterator();
  }
  
  private class WrappedSet extends WrappedCollection implements Set<V> {
    WrappedSet(K key, Set<V> delegate) {
      super(key, delegate, null);
    }
    
    public boolean removeAll(Collection<?> c) {
      if (c.isEmpty())
        return false; 
      int oldSize = size();
      boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
      if (changed) {
        int newSize = this.delegate.size();
        AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
        removeIfEmpty();
      } 
      return changed;
    }
  }
  
  private class WrappedSortedSet extends WrappedCollection implements SortedSet<V> {
    WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
      super(key, delegate, ancestor);
    }
    
    SortedSet<V> getSortedSetDelegate() {
      return (SortedSet<V>)getDelegate();
    }
    
    public Comparator<? super V> comparator() {
      return getSortedSetDelegate().comparator();
    }
    
    public V first() {
      refreshIfEmpty();
      return getSortedSetDelegate().first();
    }
    
    public V last() {
      refreshIfEmpty();
      return getSortedSetDelegate().last();
    }
    
    public SortedSet<V> headSet(V toElement) {
      refreshIfEmpty();
      return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(toElement), (getAncestor() == null) ? this : getAncestor());
    }
    
    public SortedSet<V> subSet(V fromElement, V toElement) {
      refreshIfEmpty();
      return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(fromElement, toElement), (getAncestor() == null) ? this : getAncestor());
    }
    
    public SortedSet<V> tailSet(V fromElement) {
      refreshIfEmpty();
      return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(fromElement), (getAncestor() == null) ? this : getAncestor());
    }
  }
  
  @GwtIncompatible("NavigableSet")
  class WrappedNavigableSet extends WrappedSortedSet implements NavigableSet<V> {
    WrappedNavigableSet(K key, @Nullable NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
      super(key, delegate, ancestor);
    }
    
    NavigableSet<V> getSortedSetDelegate() {
      return (NavigableSet<V>)super.getSortedSetDelegate();
    }
    
    public V lower(V v) {
      return getSortedSetDelegate().lower(v);
    }
    
    public V floor(V v) {
      return getSortedSetDelegate().floor(v);
    }
    
    public V ceiling(V v) {
      return getSortedSetDelegate().ceiling(v);
    }
    
    public V higher(V v) {
      return getSortedSetDelegate().higher(v);
    }
    
    public V pollFirst() {
      return Iterators.pollNext(iterator());
    }
    
    public V pollLast() {
      return Iterators.pollNext(descendingIterator());
    }
    
    private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
      return new WrappedNavigableSet(this.key, wrapped, (getAncestor() == null) ? this : getAncestor());
    }
    
    public NavigableSet<V> descendingSet() {
      return wrap(getSortedSetDelegate().descendingSet());
    }
    
    public Iterator<V> descendingIterator() {
      return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
    }
    
    public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) {
      return wrap(getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
    }
    
    public NavigableSet<V> headSet(V toElement, boolean inclusive) {
      return wrap(getSortedSetDelegate().headSet(toElement, inclusive));
    }
    
    public NavigableSet<V> tailSet(V fromElement, boolean inclusive) {
      return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive));
    }
  }
  
  private class WrappedList extends WrappedCollection implements List<V> {
    WrappedList(K key, @Nullable List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
      super(key, delegate, ancestor);
    }
    
    List<V> getListDelegate() {
      return (List<V>)getDelegate();
    }
    
    public boolean addAll(int index, Collection<? extends V> c) {
      if (c.isEmpty())
        return false; 
      int oldSize = size();
      boolean changed = getListDelegate().addAll(index, c);
      if (changed) {
        int newSize = getDelegate().size();
        AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
        if (oldSize == 0)
          addToMap(); 
      } 
      return changed;
    }
    
    public V get(int index) {
      refreshIfEmpty();
      return getListDelegate().get(index);
    }
    
    public V set(int index, V element) {
      refreshIfEmpty();
      return getListDelegate().set(index, element);
    }
    
    public void add(int index, V element) {
      refreshIfEmpty();
      boolean wasEmpty = getDelegate().isEmpty();
      getListDelegate().add(index, element);
      AbstractMapBasedMultimap.this.totalSize++;
      if (wasEmpty)
        addToMap(); 
    }
    
    public V remove(int index) {
      refreshIfEmpty();
      V value = getListDelegate().remove(index);
      AbstractMapBasedMultimap.this.totalSize--;
      removeIfEmpty();
      return value;
    }
    
    public int indexOf(Object o) {
      refreshIfEmpty();
      return getListDelegate().indexOf(o);
    }
    
    public int lastIndexOf(Object o) {
      refreshIfEmpty();
      return getListDelegate().lastIndexOf(o);
    }
    
    public ListIterator<V> listIterator() {
      refreshIfEmpty();
      return new WrappedListIterator();
    }
    
    public ListIterator<V> listIterator(int index) {
      refreshIfEmpty();
      return new WrappedListIterator(index);
    }
    
    public List<V> subList(int fromIndex, int toIndex) {
      refreshIfEmpty();
      return AbstractMapBasedMultimap.this.wrapList(getKey(), getListDelegate().subList(fromIndex, toIndex), (getAncestor() == null) ? this : getAncestor());
    }
    
    private class WrappedListIterator extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
      WrappedListIterator() {}
      
      public WrappedListIterator(int index) {
        super(AbstractMapBasedMultimap.WrappedList.this.getListDelegate().listIterator(index));
      }
      
      private ListIterator<V> getDelegateListIterator() {
        return (ListIterator<V>)getDelegateIterator();
      }
      
      public boolean hasPrevious() {
        return getDelegateListIterator().hasPrevious();
      }
      
      public V previous() {
        return getDelegateListIterator().previous();
      }
      
      public int nextIndex() {
        return getDelegateListIterator().nextIndex();
      }
      
      public int previousIndex() {
        return getDelegateListIterator().previousIndex();
      }
      
      public void set(V value) {
        getDelegateListIterator().set(value);
      }
      
      public void add(V value) {
        boolean wasEmpty = AbstractMapBasedMultimap.WrappedList.this.isEmpty();
        getDelegateListIterator().add(value);
        AbstractMapBasedMultimap.this.totalSize++;
        if (wasEmpty)
          AbstractMapBasedMultimap.WrappedList.this.addToMap(); 
      }
    }
  }
  
  private class RandomAccessWrappedList extends WrappedList implements RandomAccess {
    RandomAccessWrappedList(K key, @Nullable List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
      super(key, delegate, ancestor);
    }
  }
  
  Set<K> createKeySet() {
    return (this.map instanceof SortedMap) ? new SortedKeySet((SortedMap<K, Collection<V>>)this.map) : new KeySet(this.map);
  }
  
  private class KeySet extends Maps.KeySet<K, Collection<V>> {
    KeySet(Map<K, Collection<V>> subMap) {
      super(subMap);
    }
    
    public Iterator<K> iterator() {
      final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
      return new Iterator<K>() {
          Map.Entry<K, Collection<V>> entry;
          
          public boolean hasNext() {
            return entryIterator.hasNext();
          }
          
          public K next() {
            this.entry = entryIterator.next();
            return this.entry.getKey();
          }
          
          public void remove() {
            CollectPreconditions.checkRemove((this.entry != null));
            Collection<V> collection = this.entry.getValue();
            entryIterator.remove();
            AbstractMapBasedMultimap.this.totalSize -= collection.size();
            collection.clear();
          }
        };
    }
    
    public boolean remove(Object key) {
      int count = 0;
      Collection<V> collection = map().remove(key);
      if (collection != null) {
        count = collection.size();
        collection.clear();
        AbstractMapBasedMultimap.this.totalSize -= count;
      } 
      return (count > 0);
    }
    
    public void clear() {
      Iterators.clear(iterator());
    }
    
    public boolean containsAll(Collection<?> c) {
      return map().keySet().containsAll(c);
    }
    
    public boolean equals(@Nullable Object object) {
      return (this == object || map().keySet().equals(object));
    }
    
    public int hashCode() {
      return map().keySet().hashCode();
    }
  }
  
  private class SortedKeySet extends KeySet implements SortedSet<K> {
    SortedKeySet(SortedMap<K, Collection<V>> subMap) {
      super(subMap);
    }
    
    SortedMap<K, Collection<V>> sortedMap() {
      return (SortedMap<K, Collection<V>>)map();
    }
    
    public Comparator<? super K> comparator() {
      return sortedMap().comparator();
    }
    
    public K first() {
      return sortedMap().firstKey();
    }
    
    public SortedSet<K> headSet(K toElement) {
      return new SortedKeySet(sortedMap().headMap(toElement));
    }
    
    public K last() {
      return sortedMap().lastKey();
    }
    
    public SortedSet<K> subSet(K fromElement, K toElement) {
      return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
    }
    
    public SortedSet<K> tailSet(K fromElement) {
      return new SortedKeySet(sortedMap().tailMap(fromElement));
    }
  }
  
  @GwtIncompatible("NavigableSet")
  class NavigableKeySet extends SortedKeySet implements NavigableSet<K> {
    NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
      super(subMap);
    }
    
    NavigableMap<K, Collection<V>> sortedMap() {
      return (NavigableMap<K, Collection<V>>)super.sortedMap();
    }
    
    public K lower(K k) {
      return sortedMap().lowerKey(k);
    }
    
    public K floor(K k) {
      return sortedMap().floorKey(k);
    }
    
    public K ceiling(K k) {
      return sortedMap().ceilingKey(k);
    }
    
    public K higher(K k) {
      return sortedMap().higherKey(k);
    }
    
    public K pollFirst() {
      return Iterators.pollNext(iterator());
    }
    
    public K pollLast() {
      return Iterators.pollNext(descendingIterator());
    }
    
    public NavigableSet<K> descendingSet() {
      return new NavigableKeySet(sortedMap().descendingMap());
    }
    
    public Iterator<K> descendingIterator() {
      return descendingSet().iterator();
    }
    
    public NavigableSet<K> headSet(K toElement) {
      return headSet(toElement, false);
    }
    
    public NavigableSet<K> headSet(K toElement, boolean inclusive) {
      return new NavigableKeySet(sortedMap().headMap(toElement, inclusive));
    }
    
    public NavigableSet<K> subSet(K fromElement, K toElement) {
      return subSet(fromElement, true, toElement, false);
    }
    
    public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
      return new NavigableKeySet(sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
    }
    
    public NavigableSet<K> tailSet(K fromElement) {
      return tailSet(fromElement, true);
    }
    
    public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
      return new NavigableKeySet(sortedMap().tailMap(fromElement, inclusive));
    }
  }
  
  private int removeValuesForKey(Object key) {
    Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
    int count = 0;
    if (collection != null) {
      count = collection.size();
      collection.clear();
      this.totalSize -= count;
    } 
    return count;
  }
  
  private abstract class Itr<T> implements Iterator<T> {
    final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
    
    K key = null;
    
    Collection<V> collection = null;
    
    Iterator<V> valueIterator = Iterators.emptyModifiableIterator();
    
    abstract T output(K param1K, V param1V);
    
    public boolean hasNext() {
      return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
    }
    
    public T next() {
      if (!this.valueIterator.hasNext()) {
        Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
        this.key = mapEntry.getKey();
        this.collection = mapEntry.getValue();
        this.valueIterator = this.collection.iterator();
      } 
      return output(this.key, this.valueIterator.next());
    }
    
    public void remove() {
      this.valueIterator.remove();
      if (this.collection.isEmpty())
        this.keyIterator.remove(); 
      AbstractMapBasedMultimap.this.totalSize--;
    }
  }
  
  public Collection<V> values() {
    return super.values();
  }
  
  Iterator<V> valueIterator() {
    return new Itr<V>() {
        V output(K key, V value) {
          return value;
        }
      };
  }
  
  public Collection<Map.Entry<K, V>> entries() {
    return super.entries();
  }
  
  Iterator<Map.Entry<K, V>> entryIterator() {
    return new Itr<Map.Entry<K, V>>() {
        Map.Entry<K, V> output(K key, V value) {
          return Maps.immutableEntry(key, value);
        }
      };
  }
  
  Map<K, Collection<V>> createAsMap() {
    return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap<K, Collection<V>>)this.map) : new AsMap(this.map);
  }
  
  abstract Collection<V> createCollection();
  
  private class AsMap extends Maps.ImprovedAbstractMap<K, Collection<V>> {
    final transient Map<K, Collection<V>> submap;
    
    AsMap(Map<K, Collection<V>> submap) {
      this.submap = submap;
    }
    
    protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
      return new AsMapEntries();
    }
    
    public boolean containsKey(Object key) {
      return Maps.safeContainsKey(this.submap, key);
    }
    
    public Collection<V> get(Object key) {
      Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
      if (collection == null)
        return null; 
      K k = (K)key;
      return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
    }
    
    public Set<K> keySet() {
      return AbstractMapBasedMultimap.this.keySet();
    }
    
    public int size() {
      return this.submap.size();
    }
    
    public Collection<V> remove(Object key) {
      Collection<V> collection = this.submap.remove(key);
      if (collection == null)
        return null; 
      Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
      output.addAll(collection);
      AbstractMapBasedMultimap.this.totalSize -= collection.size();
      collection.clear();
      return output;
    }
    
    public boolean equals(@Nullable Object object) {
      return (this == object || this.submap.equals(object));
    }
    
    public int hashCode() {
      return this.submap.hashCode();
    }
    
    public String toString() {
      return this.submap.toString();
    }
    
    public void clear() {
      if (this.submap == AbstractMapBasedMultimap.this.map) {
        AbstractMapBasedMultimap.this.clear();
      } else {
        Iterators.clear(new AsMapIterator());
      } 
    }
    
    Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
      K key = entry.getKey();
      return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
    }
    
    class AsMapEntries extends Maps.EntrySet<K, Collection<V>> {
      Map<K, Collection<V>> map() {
        return AbstractMapBasedMultimap.AsMap.this;
      }
      
      public Iterator<Map.Entry<K, Collection<V>>> iterator() {
        return new AbstractMapBasedMultimap.AsMap.AsMapIterator();
      }
      
      public boolean contains(Object o) {
        return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o);
      }
      
      public boolean remove(Object o) {
        if (!contains(o))
          return false; 
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
        AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
        return true;
      }
    }
    
    class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>> {
      final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator();
      
      Collection<V> collection;
      
      public boolean hasNext() {
        return this.delegateIterator.hasNext();
      }
      
      public Map.Entry<K, Collection<V>> next() {
        Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
        this.collection = entry.getValue();
        return AbstractMapBasedMultimap.AsMap.this.wrapEntry(entry);
      }
      
      public void remove() {
        this.delegateIterator.remove();
        AbstractMapBasedMultimap.this.totalSize -= this.collection.size();
        this.collection.clear();
      }
    }
  }
  
  private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
    SortedSet<K> sortedKeySet;
    
    SortedAsMap(SortedMap<K, Collection<V>> submap) {
      super(submap);
    }
    
    SortedMap<K, Collection<V>> sortedMap() {
      return (SortedMap<K, Collection<V>>)this.submap;
    }
    
    public Comparator<? super K> comparator() {
      return sortedMap().comparator();
    }
    
    public K firstKey() {
      return sortedMap().firstKey();
    }
    
    public K lastKey() {
      return sortedMap().lastKey();
    }
    
    public SortedMap<K, Collection<V>> headMap(K toKey) {
      return new SortedAsMap(sortedMap().headMap(toKey));
    }
    
    public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
      return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
    }
    
    public SortedMap<K, Collection<V>> tailMap(K fromKey) {
      return new SortedAsMap(sortedMap().tailMap(fromKey));
    }
    
    public SortedSet<K> keySet() {
      SortedSet<K> result = this.sortedKeySet;
      return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
    }
    
    SortedSet<K> createKeySet() {
      return new AbstractMapBasedMultimap.SortedKeySet(sortedMap());
    }
  }
  
  @GwtIncompatible("NavigableAsMap")
  class NavigableAsMap extends SortedAsMap implements NavigableMap<K, Collection<V>> {
    NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
      super(submap);
    }
    
    NavigableMap<K, Collection<V>> sortedMap() {
      return (NavigableMap<K, Collection<V>>)super.sortedMap();
    }
    
    public Map.Entry<K, Collection<V>> lowerEntry(K key) {
      Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
      return (entry == null) ? null : wrapEntry(entry);
    }
    
    public K lowerKey(K key) {
      return sortedMap().lowerKey(key);
    }
    
    public Map.Entry<K, Collection<V>> floorEntry(K key) {
      Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
      return (entry == null) ? null : wrapEntry(entry);
    }
    
    public K floorKey(K key) {
      return sortedMap().floorKey(key);
    }
    
    public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
      Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
      return (entry == null) ? null : wrapEntry(entry);
    }
    
    public K ceilingKey(K key) {
      return sortedMap().ceilingKey(key);
    }
    
    public Map.Entry<K, Collection<V>> higherEntry(K key) {
      Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
      return (entry == null) ? null : wrapEntry(entry);
    }
    
    public K higherKey(K key) {
      return sortedMap().higherKey(key);
    }
    
    public Map.Entry<K, Collection<V>> firstEntry() {
      Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
      return (entry == null) ? null : wrapEntry(entry);
    }
    
    public Map.Entry<K, Collection<V>> lastEntry() {
      Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
      return (entry == null) ? null : wrapEntry(entry);
    }
    
    public Map.Entry<K, Collection<V>> pollFirstEntry() {
      return pollAsMapEntry(entrySet().iterator());
    }
    
    public Map.Entry<K, Collection<V>> pollLastEntry() {
      return pollAsMapEntry(descendingMap().entrySet().iterator());
    }
    
    Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
      if (!entryIterator.hasNext())
        return null; 
      Map.Entry<K, Collection<V>> entry = entryIterator.next();
      Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
      output.addAll(entry.getValue());
      entryIterator.remove();
      return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
    }
    
    public NavigableMap<K, Collection<V>> descendingMap() {
      return new NavigableAsMap(sortedMap().descendingMap());
    }
    
    public NavigableSet<K> keySet() {
      return (NavigableSet<K>)super.keySet();
    }
    
    NavigableSet<K> createKeySet() {
      return new AbstractMapBasedMultimap.NavigableKeySet(sortedMap());
    }
    
    public NavigableSet<K> navigableKeySet() {
      return keySet();
    }
    
    public NavigableSet<K> descendingKeySet() {
      return descendingMap().navigableKeySet();
    }
    
    public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) {
      return subMap(fromKey, true, toKey, false);
    }
    
    public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      return new NavigableAsMap(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
    }
    
    public NavigableMap<K, Collection<V>> headMap(K toKey) {
      return headMap(toKey, false);
    }
    
    public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) {
      return new NavigableAsMap(sortedMap().headMap(toKey, inclusive));
    }
    
    public NavigableMap<K, Collection<V>> tailMap(K fromKey) {
      return tailMap(fromKey, true);
    }
    
    public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) {
      return new NavigableAsMap(sortedMap().tailMap(fromKey, inclusive));
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractMapBasedMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */