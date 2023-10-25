package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.TriConsumer;

public class DefaultThreadContextMap implements ThreadContextMap, ReadOnlyStringMap {
  private static final long serialVersionUID = 8218007901108944053L;
  
  public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
  
  private final boolean useMap;
  
  private final ThreadLocal<Map<String, String>> localMap;
  
  private static boolean inheritableMap;
  
  static {
    init();
  }
  
  static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
    if (inheritableMap)
      return new InheritableThreadLocal<Map<String, String>>() {
          protected Map<String, String> childValue(Map<String, String> parentValue) {
            return (parentValue != null && isMapEnabled) ? 
              Collections.<String, String>unmodifiableMap(new HashMap<>(parentValue)) : null;
          }
        }; 
    return new ThreadLocal<>();
  }
  
  static void init() {
    inheritableMap = PropertiesUtil.getProperties().getBooleanProperty("isThreadContextMapInheritable");
  }
  
  public DefaultThreadContextMap() {
    this(true);
  }
  
  public DefaultThreadContextMap(boolean useMap) {
    this.useMap = useMap;
    this.localMap = createThreadLocalMap(useMap);
  }
  
  public void put(String key, String value) {
    if (!this.useMap)
      return; 
    Map<String, String> map = this.localMap.get();
    map = (map == null) ? new HashMap<>(1) : new HashMap<>(map);
    map.put(key, value);
    this.localMap.set(Collections.unmodifiableMap(map));
  }
  
  public void putAll(Map<String, String> m) {
    if (!this.useMap)
      return; 
    Map<String, String> map = this.localMap.get();
    map = (map == null) ? new HashMap<>(m.size()) : new HashMap<>(map);
    for (Map.Entry<String, String> e : m.entrySet())
      map.put(e.getKey(), e.getValue()); 
    this.localMap.set(Collections.unmodifiableMap(map));
  }
  
  public String get(String key) {
    Map<String, String> map = this.localMap.get();
    return (map == null) ? null : map.get(key);
  }
  
  public void remove(String key) {
    Map<String, String> map = this.localMap.get();
    if (map != null) {
      Map<String, String> copy = new HashMap<>(map);
      copy.remove(key);
      this.localMap.set(Collections.unmodifiableMap(copy));
    } 
  }
  
  public void removeAll(Iterable<String> keys) {
    Map<String, String> map = this.localMap.get();
    if (map != null) {
      Map<String, String> copy = new HashMap<>(map);
      for (String key : keys)
        copy.remove(key); 
      this.localMap.set(Collections.unmodifiableMap(copy));
    } 
  }
  
  public void clear() {
    this.localMap.remove();
  }
  
  public Map<String, String> toMap() {
    return getCopy();
  }
  
  public boolean containsKey(String key) {
    Map<String, String> map = this.localMap.get();
    return (map != null && map.containsKey(key));
  }
  
  public <V> void forEach(BiConsumer<String, ? super V> action) {
    Map<String, String> map = this.localMap.get();
    if (map == null)
      return; 
    for (Map.Entry<String, String> entry : map.entrySet()) {
      V value = (V)entry.getValue();
      action.accept(entry.getKey(), value);
    } 
  }
  
  public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) {
    Map<String, String> map = this.localMap.get();
    if (map == null)
      return; 
    for (Map.Entry<String, String> entry : map.entrySet()) {
      V value = (V)entry.getValue();
      action.accept(entry.getKey(), value, state);
    } 
  }
  
  public <V> V getValue(String key) {
    Map<String, String> map = this.localMap.get();
    return (map == null) ? null : (V)map.get(key);
  }
  
  public Map<String, String> getCopy() {
    Map<String, String> map = this.localMap.get();
    return (map == null) ? new HashMap<>() : new HashMap<>(map);
  }
  
  public Map<String, String> getImmutableMapOrNull() {
    return this.localMap.get();
  }
  
  public boolean isEmpty() {
    Map<String, String> map = this.localMap.get();
    return (map == null || map.isEmpty());
  }
  
  public int size() {
    Map<String, String> map = this.localMap.get();
    return (map == null) ? 0 : map.size();
  }
  
  public String toString() {
    Map<String, String> map = this.localMap.get();
    return (map == null) ? "{}" : map.toString();
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    Map<String, String> map = this.localMap.get();
    result = 31 * result + ((map == null) ? 0 : map.hashCode());
    result = 31 * result + Boolean.valueOf(this.useMap).hashCode();
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (obj instanceof DefaultThreadContextMap) {
      DefaultThreadContextMap defaultThreadContextMap = (DefaultThreadContextMap)obj;
      if (this.useMap != defaultThreadContextMap.useMap)
        return false; 
    } 
    if (!(obj instanceof ThreadContextMap))
      return false; 
    ThreadContextMap other = (ThreadContextMap)obj;
    Map<String, String> map = this.localMap.get();
    Map<String, String> otherMap = other.getImmutableMapOrNull();
    return Objects.equals(map, otherMap);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\DefaultThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */