package io.netty.handler.codec.serialization;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

abstract class ReferenceMap<K, V> implements Map<K, V> {
  private final Map<K, Reference<V>> delegate;
  
  protected ReferenceMap(Map<K, Reference<V>> delegate) {
    this.delegate = delegate;
  }
  
  abstract Reference<V> fold(V paramV);
  
  private V unfold(Reference<V> ref) {
    if (ref == null)
      return null; 
    return ref.get();
  }
  
  public int size() {
    return this.delegate.size();
  }
  
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }
  
  public boolean containsKey(Object key) {
    return this.delegate.containsKey(key);
  }
  
  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException();
  }
  
  public V get(Object key) {
    return unfold(this.delegate.get(key));
  }
  
  public V put(K key, V value) {
    return unfold(this.delegate.put(key, fold(value)));
  }
  
  public V remove(Object key) {
    return unfold(this.delegate.remove(key));
  }
  
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
      this.delegate.put(entry.getKey(), fold(entry.getValue())); 
  }
  
  public void clear() {
    this.delegate.clear();
  }
  
  public Set<K> keySet() {
    return this.delegate.keySet();
  }
  
  public Collection<V> values() {
    throw new UnsupportedOperationException();
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\ReferenceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */