package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSortedSetMultimap<K, V> extends AbstractSetMultimap<K, V> implements SortedSetMultimap<K, V> {
  private static final long serialVersionUID = 430848587173315748L;
  
  protected AbstractSortedSetMultimap(Map<K, Collection<V>> map) {
    super(map);
  }
  
  SortedSet<V> createUnmodifiableEmptyCollection() {
    Comparator<? super V> comparator = valueComparator();
    if (comparator == null)
      return Collections.unmodifiableSortedSet(createCollection()); 
    return ImmutableSortedSet.emptySet(valueComparator());
  }
  
  public SortedSet<V> get(@Nullable K key) {
    return (SortedSet<V>)super.get(key);
  }
  
  public SortedSet<V> removeAll(@Nullable Object key) {
    return (SortedSet<V>)super.removeAll(key);
  }
  
  public SortedSet<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    return (SortedSet<V>)super.replaceValues(key, values);
  }
  
  public Map<K, Collection<V>> asMap() {
    return super.asMap();
  }
  
  public Collection<V> values() {
    return super.values();
  }
  
  abstract SortedSet<V> createCollection();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractSortedSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */