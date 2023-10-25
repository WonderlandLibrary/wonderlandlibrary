package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
  private transient Collection<Map.Entry<K, V>> entries;
  
  private transient Set<K> keySet;
  
  private transient Multiset<K> keys;
  
  private transient Collection<V> values;
  
  private transient Map<K, Collection<V>> asMap;
  
  public boolean isEmpty() {
    return (size() == 0);
  }
  
  public boolean containsValue(@Nullable Object value) {
    for (Collection<V> collection : asMap().values()) {
      if (collection.contains(value))
        return true; 
    } 
    return false;
  }
  
  public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
    Collection<V> collection = asMap().get(key);
    return (collection != null && collection.contains(value));
  }
  
  public boolean remove(@Nullable Object key, @Nullable Object value) {
    Collection<V> collection = asMap().get(key);
    return (collection != null && collection.remove(value));
  }
  
  public boolean put(@Nullable K key, @Nullable V value) {
    return get(key).add(value);
  }
  
  public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
    Preconditions.checkNotNull(values);
    if (values instanceof Collection) {
      Collection<? extends V> valueCollection = (Collection<? extends V>)values;
      return (!valueCollection.isEmpty() && get(key).addAll(valueCollection));
    } 
    Iterator<? extends V> valueItr = values.iterator();
    return (valueItr.hasNext() && Iterators.addAll(get(key), valueItr));
  }
  
  public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
    boolean changed = false;
    for (Map.Entry<? extends K, ? extends V> entry : multimap.entries())
      changed |= put(entry.getKey(), entry.getValue()); 
    return changed;
  }
  
  public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    Preconditions.checkNotNull(values);
    Collection<V> result = removeAll(key);
    putAll(key, values);
    return result;
  }
  
  public Collection<Map.Entry<K, V>> entries() {
    Collection<Map.Entry<K, V>> result = this.entries;
    return (result == null) ? (this.entries = createEntries()) : result;
  }
  
  Collection<Map.Entry<K, V>> createEntries() {
    if (this instanceof SetMultimap)
      return new EntrySet(); 
    return new Entries();
  }
  
  abstract Iterator<Map.Entry<K, V>> entryIterator();
  
  private class Entries extends Multimaps.Entries<K, V> {
    private Entries() {}
    
    Multimap<K, V> multimap() {
      return AbstractMultimap.this;
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      return AbstractMultimap.this.entryIterator();
    }
  }
  
  private class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
    private EntrySet() {}
    
    public int hashCode() {
      return Sets.hashCodeImpl(this);
    }
    
    public boolean equals(@Nullable Object obj) {
      return Sets.equalsImpl(this, obj);
    }
  }
  
  public Set<K> keySet() {
    Set<K> result = this.keySet;
    return (result == null) ? (this.keySet = createKeySet()) : result;
  }
  
  Set<K> createKeySet() {
    return new Maps.KeySet<K, Collection<V>>(asMap());
  }
  
  public Multiset<K> keys() {
    Multiset<K> result = this.keys;
    return (result == null) ? (this.keys = createKeys()) : result;
  }
  
  Multiset<K> createKeys() {
    return new Multimaps.Keys<K, V>(this);
  }
  
  public Collection<V> values() {
    Collection<V> result = this.values;
    return (result == null) ? (this.values = createValues()) : result;
  }
  
  Collection<V> createValues() {
    return new Values();
  }
  
  class Values extends AbstractCollection<V> {
    public Iterator<V> iterator() {
      return AbstractMultimap.this.valueIterator();
    }
    
    public int size() {
      return AbstractMultimap.this.size();
    }
    
    public boolean contains(@Nullable Object o) {
      return AbstractMultimap.this.containsValue(o);
    }
    
    public void clear() {
      AbstractMultimap.this.clear();
    }
  }
  
  Iterator<V> valueIterator() {
    return Maps.valueIterator(entries().iterator());
  }
  
  public Map<K, Collection<V>> asMap() {
    Map<K, Collection<V>> result = this.asMap;
    return (result == null) ? (this.asMap = createAsMap()) : result;
  }
  
  abstract Map<K, Collection<V>> createAsMap();
  
  public boolean equals(@Nullable Object object) {
    return Multimaps.equalsImpl(this, object);
  }
  
  public int hashCode() {
    return asMap().hashCode();
  }
  
  public String toString() {
    return asMap().toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */