package org.apache.logging.log4j.core.lookup;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "main", category = "Lookup")
public class MainMapLookup extends MapLookup {
  static final MapLookup MAIN_SINGLETON = new MapLookup(MapLookup.newMap(0));
  
  public MainMapLookup() {}
  
  public MainMapLookup(Map<String, String> map) {
    super(map);
  }
  
  public static void setMainArguments(String... args) {
    if (args == null)
      return; 
    initMap(args, MAIN_SINGLETON.getMap());
  }
  
  public String lookup(LogEvent event, String key) {
    return MAIN_SINGLETON.getMap().get(key);
  }
  
  public String lookup(String key) {
    return MAIN_SINGLETON.getMap().get(key);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\lookup\MainMapLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */