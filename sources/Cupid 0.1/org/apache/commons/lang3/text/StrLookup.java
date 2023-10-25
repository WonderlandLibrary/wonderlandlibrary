package org.apache.commons.lang3.text;

import java.util.Map;

public abstract class StrLookup<V> {
  private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<String>(null);
  
  private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP;
  
  static {
    StrLookup<String> lookup = null;
    try {
      Map<?, ?> propMap = System.getProperties();
      Map<?, ?> map1 = propMap;
      lookup = new MapStrLookup((Map)map1);
    } catch (SecurityException ex) {
      lookup = NONE_LOOKUP;
    } 
    SYSTEM_PROPERTIES_LOOKUP = lookup;
  }
  
  public static StrLookup<?> noneLookup() {
    return NONE_LOOKUP;
  }
  
  public static StrLookup<String> systemPropertiesLookup() {
    return SYSTEM_PROPERTIES_LOOKUP;
  }
  
  public static <V> StrLookup<V> mapLookup(Map<String, V> map) {
    return new MapStrLookup<V>(map);
  }
  
  public abstract String lookup(String paramString);
  
  static class MapStrLookup<V> extends StrLookup<V> {
    private final Map<String, V> map;
    
    MapStrLookup(Map<String, V> map) {
      this.map = map;
    }
    
    public String lookup(String key) {
      if (this.map == null)
        return null; 
      Object obj = this.map.get(key);
      if (obj == null)
        return null; 
      return obj.toString();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\StrLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */