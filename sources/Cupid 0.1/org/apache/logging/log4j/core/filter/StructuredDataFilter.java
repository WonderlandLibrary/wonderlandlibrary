package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@Plugin(name = "StructuredDataFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public final class StructuredDataFilter extends MapFilter {
  private static final int MAX_BUFFER_SIZE = 2048;
  
  private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();
  
  private StructuredDataFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
    super(map, oper, onMatch, onMismatch);
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    if (msg instanceof StructuredDataMessage)
      return filter((StructuredDataMessage)msg); 
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(LogEvent event) {
    Message msg = event.getMessage();
    if (msg instanceof StructuredDataMessage)
      return filter((StructuredDataMessage)msg); 
    return super.filter(event);
  }
  
  protected Filter.Result filter(StructuredDataMessage message) {
    boolean match = false;
    IndexedReadOnlyStringMap map = getStringMap();
    for (int i = 0; i < map.size(); i++) {
      StringBuilder toMatch = getValue(message, map.getKeyAt(i));
      if (toMatch != null) {
        match = listContainsValue((List<String>)map.getValueAt(i), toMatch);
      } else {
        match = false;
      } 
      if ((!isAnd() && match) || (isAnd() && !match))
        break; 
    } 
    return match ? this.onMatch : this.onMismatch;
  }
  
  private StringBuilder getValue(StructuredDataMessage data, String key) {
    StringBuilder sb = getStringBuilder();
    if (key.equalsIgnoreCase("id")) {
      data.getId().formatTo(sb);
      return sb;
    } 
    if (key.equalsIgnoreCase("id.name"))
      return appendOrNull(data.getId().getName(), sb); 
    if (key.equalsIgnoreCase("type"))
      return appendOrNull(data.getType(), sb); 
    if (key.equalsIgnoreCase("message")) {
      data.formatTo(sb);
      return sb;
    } 
    return appendOrNull(data.get(key), sb);
  }
  
  private StringBuilder getStringBuilder() {
    StringBuilder result = threadLocalStringBuilder.get();
    if (result == null) {
      result = new StringBuilder();
      threadLocalStringBuilder.set(result);
    } 
    StringBuilders.trimToMaxSize(result, 2048);
    result.setLength(0);
    return result;
  }
  
  private StringBuilder appendOrNull(String value, StringBuilder sb) {
    if (value == null)
      return null; 
    sb.append(value);
    return sb;
  }
  
  private boolean listContainsValue(List<String> candidates, StringBuilder toMatch) {
    if (toMatch == null) {
      for (int i = 0; i < candidates.size(); i++) {
        String candidate = candidates.get(i);
        if (candidate == null)
          return true; 
      } 
    } else {
      for (int i = 0; i < candidates.size(); i++) {
        String candidate = candidates.get(i);
        if (candidate == null)
          return false; 
        if (StringBuilders.equals(candidate, 0, candidate.length(), toMatch, 0, toMatch.length()))
          return true; 
      } 
    } 
    return false;
  }
  
  @PluginFactory
  public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
    if (pairs == null || pairs.length == 0) {
      LOGGER.error("keys and values must be specified for the StructuredDataFilter");
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
      LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
      return null;
    } 
    boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
    return new StructuredDataFilter(map, isAnd, match, mismatch);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\StructuredDataFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */