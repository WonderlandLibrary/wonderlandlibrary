package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import org.apache.logging.log4j.util.IndexedStringMap;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.SortedArrayStringMap;

@Plugin(name = "MapFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public class MapFilter extends AbstractFilter {
  private final IndexedStringMap map;
  
  private final boolean isAnd;
  
  protected MapFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    this.isAnd = oper;
    Objects.requireNonNull(map, "map cannot be null");
    this.map = (IndexedStringMap)new SortedArrayStringMap(map.size());
    for (Map.Entry<String, List<String>> entry : map.entrySet())
      this.map.putValue(entry.getKey(), entry.getValue()); 
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    if (msg instanceof MapMessage)
      return filter((MapMessage<?, ?>)msg) ? this.onMatch : this.onMismatch; 
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(LogEvent event) {
    Message msg = event.getMessage();
    if (msg instanceof MapMessage)
      return filter((MapMessage<?, ?>)msg) ? this.onMatch : this.onMismatch; 
    return Filter.Result.NEUTRAL;
  }
  
  protected boolean filter(MapMessage<?, ?> mapMessage) {
    boolean match = false;
    for (int i = 0; i < this.map.size(); i++) {
      String toMatch = mapMessage.get(this.map.getKeyAt(i));
      match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
      if ((!this.isAnd && match) || (this.isAnd && !match))
        break; 
    } 
    return match;
  }
  
  protected boolean filter(Map<String, String> data) {
    boolean match = false;
    for (int i = 0; i < this.map.size(); i++) {
      String toMatch = data.get(this.map.getKeyAt(i));
      match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
      if ((!this.isAnd && match) || (this.isAnd && !match))
        break; 
    } 
    return match;
  }
  
  protected boolean filter(ReadOnlyStringMap data) {
    boolean match = false;
    for (int i = 0; i < this.map.size(); i++) {
      String toMatch = (String)data.getValue(this.map.getKeyAt(i));
      match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
      if ((!this.isAnd && match) || (this.isAnd && !match))
        break; 
    } 
    return match;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return Filter.Result.NEUTRAL;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("isAnd=").append(this.isAnd);
    if (this.map.size() > 0) {
      sb.append(", {");
      for (int i = 0; i < this.map.size(); i++) {
        if (i > 0)
          sb.append(", "); 
        List<String> list = (List<String>)this.map.getValueAt(i);
        String value = (list.size() > 1) ? list.get(0) : list.toString();
        sb.append(this.map.getKeyAt(i)).append('=').append(value);
      } 
      sb.append('}');
    } 
    return sb.toString();
  }
  
  protected boolean isAnd() {
    return this.isAnd;
  }
  
  @Deprecated
  protected Map<String, List<String>> getMap() {
    Map<String, List<String>> result = new HashMap<>(this.map.size());
    this.map.forEach((key, value) -> (List)result.put(key, (List)value));
    return result;
  }
  
  protected IndexedReadOnlyStringMap getStringMap() {
    return (IndexedReadOnlyStringMap)this.map;
  }
  
  @PluginFactory
  public static MapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
    if (pairs == null || pairs.length == 0) {
      LOGGER.error("keys and values must be specified for the MapFilter");
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
      LOGGER.error("MapFilter is not configured with any valid key value pairs");
      return null;
    } 
    boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
    return new MapFilter(map, isAnd, match, mismatch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\MapFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */