package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSetMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements SetMultimap<K, V> {
  private static final long serialVersionUID = 7431625294878419160L;
  
  protected AbstractSetMultimap(Map<K, Collection<V>> map) {
    super(map);
  }
  
  Set<V> createUnmodifiableEmptyCollection() {
    return ImmutableSet.of();
  }
  
  public Set<V> get(@Nullable K key) {
    return (Set<V>)super.get(key);
  }
  
  public Set<Map.Entry<K, V>> entries() {
    return (Set<Map.Entry<K, V>>)super.entries();
  }
  
  public Set<V> removeAll(@Nullable Object key) {
    return (Set<V>)super.removeAll(key);
  }
  
  public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    return (Set<V>)super.replaceValues(key, values);
  }
  
  public Map<K, Collection<V>> asMap() {
    return super.asMap();
  }
  
  public boolean put(@Nullable K key, @Nullable V value) {
    return super.put(key, value);
  }
  
  public boolean equals(@Nullable Object object) {
    return super.equals(object);
  }
  
  abstract Set<V> createCollection();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */