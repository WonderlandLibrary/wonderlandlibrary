package me.rhys.base.util.container;

import java.util.HashMap;
import java.util.Map;

public class MapContainer<K, V> {
  private final Map<K, V> map = new HashMap<>();
  
  public Map<K, V> getMap() {
    return this.map;
  }
  
  public void put(K key, V value) {
    this.map.put(key, value);
  }
  
  public void pop(K key) {
    this.map.remove(key);
  }
  
  public V get(K key) {
    return this.map.get(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\container\MapContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */