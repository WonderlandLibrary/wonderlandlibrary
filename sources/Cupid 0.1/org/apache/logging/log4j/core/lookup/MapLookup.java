package org.apache.logging.log4j.core.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(name = "map", category = "Lookup")
public class MapLookup implements StrLookup {
  private final Map<String, String> map;
  
  public MapLookup() {
    this.map = null;
  }
  
  public MapLookup(Map<String, String> map) {
    this.map = map;
  }
  
  static Map<String, String> initMap(String[] srcArgs, Map<String, String> destMap) {
    for (int i = 0; i < srcArgs.length; i++) {
      int next = i + 1;
      String value = srcArgs[i];
      destMap.put(Integer.toString(i), value);
      destMap.put(value, (next < srcArgs.length) ? srcArgs[next] : null);
    } 
    return destMap;
  }
  
  static HashMap<String, String> newMap(int initialCapacity) {
    return new HashMap<>(initialCapacity);
  }
  
  @Deprecated
  public static void setMainArguments(String... args) {
    MainMapLookup.setMainArguments(args);
  }
  
  static Map<String, String> toMap(List<String> args) {
    if (args == null)
      return null; 
    int size = args.size();
    return initMap(args.<String>toArray(new String[size]), newMap(size));
  }
  
  static Map<String, String> toMap(String[] args) {
    if (args == null)
      return null; 
    return initMap(args, newMap(args.length));
  }
  
  protected Map<String, String> getMap() {
    return this.map;
  }
  
  public String lookup(LogEvent event, String key) {
    boolean isMapMessage = (event != null && event.getMessage() instanceof MapMessage);
    if (this.map == null && !isMapMessage)
      return null; 
    if (this.map != null && this.map.containsKey(key)) {
      String obj = this.map.get(key);
      if (obj != null)
        return obj; 
    } 
    if (isMapMessage)
      return ((MapMessage)event.getMessage()).get(key); 
    return null;
  }
  
  public String lookup(String key) {
    if (this.map == null)
      return null; 
    return this.map.get(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\MapLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */