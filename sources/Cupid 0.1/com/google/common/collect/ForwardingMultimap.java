package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingMultimap<K, V> extends ForwardingObject implements Multimap<K, V> {
  public Map<K, Collection<V>> asMap() {
    return delegate().asMap();
  }
  
  public void clear() {
    delegate().clear();
  }
  
  public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
    return delegate().containsEntry(key, value);
  }
  
  public boolean containsKey(@Nullable Object key) {
    return delegate().containsKey(key);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return delegate().containsValue(value);
  }
  
  public Collection<Map.Entry<K, V>> entries() {
    return delegate().entries();
  }
  
  public Collection<V> get(@Nullable K key) {
    return delegate().get(key);
  }
  
  public boolean isEmpty() {
    return delegate().isEmpty();
  }
  
  public Multiset<K> keys() {
    return delegate().keys();
  }
  
  public Set<K> keySet() {
    return delegate().keySet();
  }
  
  public boolean put(K key, V value) {
    return delegate().put(key, value);
  }
  
  public boolean putAll(K key, Iterable<? extends V> values) {
    return delegate().putAll(key, values);
  }
  
  public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
    return delegate().putAll(multimap);
  }
  
  public boolean remove(@Nullable Object key, @Nullable Object value) {
    return delegate().remove(key, value);
  }
  
  public Collection<V> removeAll(@Nullable Object key) {
    return delegate().removeAll(key);
  }
  
  public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
    return delegate().replaceValues(key, values);
  }
  
  public int size() {
    return delegate().size();
  }
  
  public Collection<V> values() {
    return delegate().values();
  }
  
  public boolean equals(@Nullable Object object) {
    return (object == this || delegate().equals(object));
  }
  
  public int hashCode() {
    return delegate().hashCode();
  }
  
  protected abstract Multimap<K, V> delegate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ForwardingMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */