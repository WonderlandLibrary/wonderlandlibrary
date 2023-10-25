package org.apache.logging.log4j.core.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.TriConsumer;

public class JdkMapAdapterStringMap implements StringMap {
  private static final long serialVersionUID = -7348247784983193612L;
  
  private static final String FROZEN = "Frozen collection cannot be modified";
  
  private static final Comparator<? super String> NULL_FIRST_COMPARATOR;
  
  private final Map<String, String> map;
  
  static {
    NULL_FIRST_COMPARATOR = ((left, right) -> (left == null) ? -1 : ((right == null) ? 1 : left.compareTo(right)));
    PUT_ALL = ((key, value, stringStringMap) -> (String)stringStringMap.put(key, value));
  }
  
  private boolean immutable = false;
  
  private transient String[] sortedKeys;
  
  private static TriConsumer<String, String, Map<String, String>> PUT_ALL;
  
  public JdkMapAdapterStringMap() {
    this(new HashMap<>());
  }
  
  public JdkMapAdapterStringMap(Map<String, String> map) {
    this.map = Objects.<Map<String, String>>requireNonNull(map, "map");
  }
  
  public Map<String, String> toMap() {
    return this.map;
  }
  
  private void assertNotFrozen() {
    if (this.immutable)
      throw new UnsupportedOperationException("Frozen collection cannot be modified"); 
  }
  
  public boolean containsKey(String key) {
    return this.map.containsKey(key);
  }
  
  public <V> void forEach(BiConsumer<String, ? super V> action) {
    String[] keys = getSortedKeys();
    for (int i = 0; i < keys.length; i++)
      action.accept(keys[i], this.map.get(keys[i])); 
  }
  
  public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) {
    String[] keys = getSortedKeys();
    for (int i = 0; i < keys.length; i++)
      action.accept(keys[i], this.map.get(keys[i]), state); 
  }
  
  private String[] getSortedKeys() {
    if (this.sortedKeys == null) {
      this.sortedKeys = (String[])this.map.keySet().toArray((Object[])new String[this.map.size()]);
      Arrays.sort(this.sortedKeys, NULL_FIRST_COMPARATOR);
    } 
    return this.sortedKeys;
  }
  
  public <V> V getValue(String key) {
    return (V)this.map.get(key);
  }
  
  public boolean isEmpty() {
    return this.map.isEmpty();
  }
  
  public int size() {
    return this.map.size();
  }
  
  public void clear() {
    if (this.map.isEmpty())
      return; 
    assertNotFrozen();
    this.map.clear();
    this.sortedKeys = null;
  }
  
  public void freeze() {
    this.immutable = true;
  }
  
  public boolean isFrozen() {
    return this.immutable;
  }
  
  public void putAll(ReadOnlyStringMap source) {
    assertNotFrozen();
    source.forEach(PUT_ALL, this.map);
    this.sortedKeys = null;
  }
  
  public void putValue(String key, Object value) {
    assertNotFrozen();
    this.map.put(key, (value == null) ? null : String.valueOf(value));
    this.sortedKeys = null;
  }
  
  public void remove(String key) {
    if (!this.map.containsKey(key))
      return; 
    assertNotFrozen();
    this.map.remove(key);
    this.sortedKeys = null;
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder(this.map.size() * 13);
    result.append('{');
    String[] keys = getSortedKeys();
    for (int i = 0; i < keys.length; i++) {
      if (i > 0)
        result.append(", "); 
      result.append(keys[i]).append('=').append(this.map.get(keys[i]));
    } 
    result.append('}');
    return result.toString();
  }
  
  public boolean equals(Object object) {
    if (object == this)
      return true; 
    if (!(object instanceof JdkMapAdapterStringMap))
      return false; 
    JdkMapAdapterStringMap other = (JdkMapAdapterStringMap)object;
    return (this.map.equals(other.map) && this.immutable == other.immutable);
  }
  
  public int hashCode() {
    return this.map.hashCode() + (this.immutable ? 31 : 0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\impl\JdkMapAdapterStringMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */