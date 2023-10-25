package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractListMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements ListMultimap<K, V> {
  private static final long serialVersionUID = 6588350623831699109L;
  
  protected AbstractListMultimap(Map<K, Collection<V>> map) {
    super(map);
  }
  
  List<V> createUnmodifiableEmptyCollection() {
    return ImmutableList.of();
  }
  
  public List<V> get(@Nullable K key) {
    return (List<V>)super.get(key);
  }
  
  public List<V> removeAll(@Nullable Object key) {
    return (List<V>)super.removeAll(key);
  }
  
  public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    return (List<V>)super.replaceValues(key, values);
  }
  
  public boolean put(@Nullable K key, @Nullable V value) {
    return super.put(key, value);
  }
  
  public Map<K, Collection<V>> asMap() {
    return super.asMap();
  }
  
  public boolean equals(@Nullable Object object) {
    return super.equals(object);
  }
  
  abstract List<V> createCollection();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */