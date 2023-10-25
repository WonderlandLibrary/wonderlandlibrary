package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.StringMap;

class CopyOnWriteSortedArrayThreadContextMap implements ReadOnlyThreadContextMap, ObjectThreadContextMap, CopyOnWrite {
  public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
  
  protected static final int DEFAULT_INITIAL_CAPACITY = 16;
  
  protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
  
  private static final StringMap EMPTY_CONTEXT_DATA = (StringMap)new SortedArrayStringMap(1);
  
  private static volatile int initialCapacity;
  
  private static volatile boolean inheritableMap;
  
  private final ThreadLocal<StringMap> localMap;
  
  static void init() {
    PropertiesUtil properties = PropertiesUtil.getProperties();
    initialCapacity = properties.getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16);
    inheritableMap = properties.getBooleanProperty("isThreadContextMapInheritable");
  }
  
  static {
    EMPTY_CONTEXT_DATA.freeze();
    init();
  }
  
  public CopyOnWriteSortedArrayThreadContextMap() {
    this.localMap = createThreadLocalMap();
  }
  
  private ThreadLocal<StringMap> createThreadLocalMap() {
    if (inheritableMap)
      return new InheritableThreadLocal<StringMap>() {
          protected StringMap childValue(StringMap parentValue) {
            if (parentValue == null)
              return null; 
            StringMap stringMap = CopyOnWriteSortedArrayThreadContextMap.this.createStringMap((ReadOnlyStringMap)parentValue);
            stringMap.freeze();
            return stringMap;
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
  
  public void put(String key, String value) {
    putValue(key, value);
  }
  
  public void putValue(String key, Object value) {
    StringMap map = this.localMap.get();
    map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
    map.putValue(key, value);
    map.freeze();
    this.localMap.set(map);
  }
  
  public void putAll(Map<String, String> values) {
    if (values == null || values.isEmpty())
      return; 
    StringMap map = this.localMap.get();
    map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
    for (Map.Entry<String, String> entry : values.entrySet())
      map.putValue(entry.getKey(), entry.getValue()); 
    map.freeze();
    this.localMap.set(map);
  }
  
  public <V> void putAllValues(Map<String, V> values) {
    if (values == null || values.isEmpty())
      return; 
    StringMap map = this.localMap.get();
    map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
    for (Map.Entry<String, V> entry : values.entrySet())
      map.putValue(entry.getKey(), entry.getValue()); 
    map.freeze();
    this.localMap.set(map);
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
    if (map != null) {
      StringMap copy = createStringMap((ReadOnlyStringMap)map);
      copy.remove(key);
      copy.freeze();
      this.localMap.set(copy);
    } 
  }
  
  public void removeAll(Iterable<String> keys) {
    StringMap map = this.localMap.get();
    if (map != null) {
      StringMap copy = createStringMap((ReadOnlyStringMap)map);
      for (String key : keys)
        copy.remove(key); 
      copy.freeze();
      this.localMap.set(copy);
    } 
  }
  
  public void clear() {
    this.localMap.remove();
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
    return (map == null) ? EMPTY_CONTEXT_DATA : map;
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\CopyOnWriteSortedArrayThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */