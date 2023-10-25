package org.apache.logging.log4j.core.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

@Plugin(name = "DynamicThresholdFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public final class DynamicThresholdFilter extends AbstractFilter {
  @PluginFactory
  public static DynamicThresholdFilter createFilter(@PluginAttribute("key") String key, @PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("defaultThreshold") Level defaultThreshold, @PluginAttribute("onMatch") Filter.Result onMatch, @PluginAttribute("onMismatch") Filter.Result onMismatch) {
    Map<String, Level> map = new HashMap<>();
    for (KeyValuePair pair : pairs)
      map.put(pair.getKey(), Level.toLevel(pair.getValue())); 
    Level level = (defaultThreshold == null) ? Level.ERROR : defaultThreshold;
    return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
  }
  
  private Level defaultThreshold = Level.ERROR;
  
  private final String key;
  
  private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
  
  private Map<String, Level> levelMap = new HashMap<>();
  
  private DynamicThresholdFilter(String key, Map<String, Level> pairs, Level defaultLevel, Filter.Result onMatch, Filter.Result onMismatch) {
    super(onMatch, onMismatch);
    Objects.requireNonNull(key, "key cannot be null");
    this.key = key;
    this.levelMap = pairs;
    this.defaultThreshold = defaultLevel;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (!equalsImpl(obj))
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    DynamicThresholdFilter other = (DynamicThresholdFilter)obj;
    if (!Objects.equals(this.defaultThreshold, other.defaultThreshold))
      return false; 
    if (!Objects.equals(this.key, other.key))
      return false; 
    if (!Objects.equals(this.levelMap, other.levelMap))
      return false; 
    return true;
  }
  
  private Filter.Result filter(Level level, ReadOnlyStringMap contextMap) {
    String value = (String)contextMap.getValue(this.key);
    if (value != null) {
      Level ctxLevel = this.levelMap.get(value);
      if (ctxLevel == null)
        ctxLevel = this.defaultThreshold; 
      return level.isMoreSpecificThan(ctxLevel) ? this.onMatch : this.onMismatch;
    } 
    return Filter.Result.NEUTRAL;
  }
  
  public Filter.Result filter(LogEvent event) {
    return filter(event.getLevel(), event.getContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return filter(level, currentContextData());
  }
  
  private ReadOnlyStringMap currentContextData() {
    return this.injector.rawContextData();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return filter(level, currentContextData());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return filter(level, currentContextData());
  }
  
  public String getKey() {
    return this.key;
  }
  
  public Map<String, Level> getLevelMap() {
    return this.levelMap;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = hashCodeImpl();
    result = 31 * result + ((this.defaultThreshold == null) ? 0 : this.defaultThreshold.hashCode());
    result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
    result = 31 * result + ((this.levelMap == null) ? 0 : this.levelMap.hashCode());
    return result;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("key=").append(this.key);
    sb.append(", default=").append(this.defaultThreshold);
    if (this.levelMap.size() > 0) {
      sb.append('{');
      boolean first = true;
      for (Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
        if (!first) {
          sb.append(", ");
          first = false;
        } 
        sb.append(entry.getKey()).append('=').append(entry.getValue());
      } 
      sb.append('}');
    } 
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\DynamicThresholdFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */