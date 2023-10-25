package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class RegularImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V> {
  private final transient RegularImmutableSortedSet<K> keySet;
  
  private final transient ImmutableList<V> valueList;
  
  RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
    this.keySet = keySet;
    this.valueList = valueList;
  }
  
  RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
    super(descendingMap);
    this.keySet = keySet;
    this.valueList = valueList;
  }
  
  ImmutableSet<Map.Entry<K, V>> createEntrySet() {
    return new EntrySet();
  }
  
  private class EntrySet extends ImmutableMapEntrySet<K, V> {
    private EntrySet() {}
    
    public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
      return asList().iterator();
    }
    
    ImmutableList<Map.Entry<K, V>> createAsList() {
      return new ImmutableAsList<Map.Entry<K, V>>() {
          private final ImmutableList<K> keyList = RegularImmutableSortedMap.this.keySet().asList();
          
          public Map.Entry<K, V> get(int index) {
            return Maps.immutableEntry(this.keyList.get(index), (V)RegularImmutableSortedMap.this.valueList.get(index));
          }
          
          ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
            return RegularImmutableSortedMap.EntrySet.this;
          }
        };
    }
    
    ImmutableMap<K, V> map() {
      return RegularImmutableSortedMap.this;
    }
  }
  
  public ImmutableSortedSet<K> keySet() {
    return this.keySet;
  }
  
  public ImmutableCollection<V> values() {
    return this.valueList;
  }
  
  public V get(@Nullable Object key) {
    int index = this.keySet.indexOf(key);
    return (index == -1) ? null : this.valueList.get(index);
  }
  
  private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
    if (fromIndex == 0 && toIndex == size())
      return this; 
    if (fromIndex == toIndex)
      return emptyMap(comparator()); 
    return from(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
  }
  
  public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
    return getSubMap(0, this.keySet.headIndex((K)Preconditions.checkNotNull(toKey), inclusive));
  }
  
  public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
    return getSubMap(this.keySet.tailIndex((K)Preconditions.checkNotNull(fromKey), inclusive), size());
  }
  
  ImmutableSortedMap<K, V> createDescendingMap() {
    return new RegularImmutableSortedMap((RegularImmutableSortedSet<K>)this.keySet.descendingSet(), this.valueList.reverse(), this);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\RegularImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */