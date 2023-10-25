package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.StringMap;

class GarbageFreeSortedArrayThreadContextMap implements ReadOnlyThreadContextMap, ObjectThreadContextMap {
  public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
  
  protected static final int DEFAULT_INITIAL_CAPACITY = 16;
  
  protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
  
  protected final ThreadLocal<StringMap> localMap;
  
  private static volatile int initialCapacity;
  
  private static volatile boolean inheritableMap;
  
  static void init() {
    PropertiesUtil properties = PropertiesUtil.getProperties();
    initialCapacity = properties.getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16);
    inheritableMap = properties.getBooleanProperty("isThreadContextMapInheritable");
  }
  
  static {
    init();
  }
  
  public GarbageFreeSortedArrayThreadContextMap() {
    this.localMap = createThreadLocalMap();
  }
  
  private ThreadLocal<StringMap> createThreadLocalMap() {
    if (inheritableMap)
      return new InheritableThreadLocal<StringMap>() {
          protected StringMap childValue(StringMap parentValue) {
            return (parentValue != null) ? GarbageFreeSortedArrayThreadContextMap.this.createStringMap((ReadOnlyStringMap)parentValue) : null;
          }
        }; 
    return new ThreadLocal<>();
  }
  
  protected StringMap createStringMap() {
    return (StringMap)new SortedArrayStringMap(initialCapacity);
  }
  
  protected StringMap createStringMap(ReadOnlyStringMap original) {
    return (StringMap)new SortedArrayStringMap(original);
  }
  
  private StringMap getThreadLocalMap() {
    StringMap map = this.localMap.get();
    if (map == null) {
      map = createStringMap();
      this.localMap.set(map);
    } 
    return map;
  }
  
  public void put(String key, String value) {
    getThreadLocalMap().putValue(key, value);
  }
  
  public void putValue(String key, Object value) {
    getThreadLocalMap().putValue(key, value);
  }
  
  public void putAll(Map<String, String> values) {
    if (values == null || values.isEmpty())
      return; 
    StringMap map = getThreadLocalMap();
    for (Map.Entry<String, String> entry : values.entrySet())
      map.putValue(entry.getKey(), entry.getValue()); 
  }
  
  public <V> void putAllValues(Map<String, V> values) {
    if (values == null || values.isEmpty())
      return; 
    StringMap map = getThreadLocalMap();
    for (Map.Entry<String, V> entry : values.entrySet())
      map.putValue(entry.getKey(), entry.getValue()); 
  }
  
  public String get(String key) {
    return getValue(key);
  }
  
  public <V> V getValue(String key) {
    StringMap map = this.localMap.get();
    return (map == null) ? null : (V)map.getValue(key);
  }
  
  public void remove(String key) {
    StringMap map = this.localMap.get();
    if (map != null)
      map.remove(key); 
  }
  
  public void removeAll(Iterable<String> keys) {
    StringMap map = this.localMap.get();
    if (map != null)
      for (String key : keys)
        map.remove(key);  
  }
  
  public void clear() {
    StringMap map = this.localMap.get();
    if (map != null)
      map.clear(); 
  }
  
  public boolean containsKey(String key) {
    StringMap map = this.localMap.get();
    return (map != null && map.containsKey(key));
  }
  
  public Map<String, String> getCopy() {
    StringMap map = this.localMap.get();
    return (map == null) ? new HashMap<>() : map.toMap();
  }
  
  public StringMap getReadOnlyContextData() {
    StringMap map = this.localMap.get();
    if (map == null) {
      map = createStringMap();
      this.localMap.set(map);
    } 
    return map;
  }
  
  public Map<String, String> getImmutableMapOrNull() {
    StringMap map = this.localMap.get();
    return (map == null) ? null : Collections.<String, String>unmodifiableMap(map.toMap());
  }
  
  public boolean isEmpty() {
    StringMap map = this.localMap.get();
    return (map == null || map.isEmpty());
  }
  
  public String toString() {
    StringMap map = this.localMap.get();
    return (map == null) ? "{}" : map.toString();
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    StringMap map = this.localMap.get();
    result = 31 * result + ((map == null) ? 0 : map.hashCode());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (!(obj instanceof ThreadContextMap))
      return false; 
    ThreadContextMap other = (ThreadContextMap)obj;
    Map<String, String> map = getImmutableMapOrNull();
    Map<String, String> otherMap = other.getImmutableMapOrNull();
    return Objects.equals(map, otherMap);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\GarbageFreeSortedArrayThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */