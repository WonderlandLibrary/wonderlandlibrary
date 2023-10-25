package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@Plugin(name = "ThreadContextMapFilter", category = "Core", elementType = "filter", printObject = true)
@PluginAliases({"ContextMapFilter"})
@PerformanceSensitive({"allocation"})
public class ThreadContextMapFilter extends MapFilter {
  private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
  
  private final String key;
  
  private final String value;
  
  private final boolean useMap;
  
  public ThreadContextMapFilter(Map<String, List<String>> pairs, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
    super(pairs, oper, onMatch, onMismatch);
    if (pairs.size() == 1) {
      Iterator<Map.Entry<String, List<String>>> iter = pairs.entrySet().iterator();
      Map.Entry<String, List<String>> entry = iter.next();
      if (((List)entry.getValue()).size() == 1) {
        this.key = entry.getKey();
        this.value = ((List<String>)entry.getValue()).get(0);
        this.useMap = false;
      } else {
        this.key = null;
        this.value = null;
        this.useMap = true;
      } 
    } else {
      this.key = null;
      this.value = null;
      this.useMap = true;
    } 
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return filter();
  }
  
  private Filter.Result filter() {
    boolean match = false;
    if (this.useMap) {
      ReadOnlyStringMap currentContextData = null;
      IndexedReadOnlyStringMap map = getStringMap();
      for (int i = 0; i < map.size(); i++) {
        if (currentContextData == null)
          currentContextData = currentContextData(); 
        String toMatch = (String)currentContextData.getValue(map.getKeyAt(i));
        match = (toMatch != null && ((List)map.getValueAt(i)).contains(toMatch));
        if ((!isAnd() && match) || (isAnd() && !match))
          break; 
      } 
    } else {
      match = this.value.equals(currentContextData().getValue(this.key));
    } 
    return match ? this.onMatch : this.onMismatch;
  }
  
  private ReadOnlyStringMap currentContextData() {
    return this.injector.rawContextData();
  }
  
  public Filter.Result filter(LogEvent event) {
    return filter(event.getContextData()) ? this.onMatch : this.onMismatch;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return filter();
  }
  
  @PluginFactory
  public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
    if (pairs == null || pairs.length == 0) {
      LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
      return null;
    } 
    Map<String, List<String>> map = new HashMap<>();
    for (KeyValuePair pair : pairs) {
      String key = pair.getKey();
      if (key == null) {
        LOGGER.error("A null key is not valid in MapFilter");
      } else {
        String value = pair.getValue();
        if (value == null) {
          LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
        } else {
          List<String> list = map.get(pair.getKey());
          if (list != null) {
            list.add(value);
          } else {
            list = new ArrayList<>();
            list.add(value);
            map.put(pair.getKey(), list);
          } 
        } 
      } 
    } 
    if (map.isEmpty()) {
      LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
      return null;
    } 
    boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
    return new ThreadContextMapFilter(map, isAnd, match, mismatch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\ThreadContextMapFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */