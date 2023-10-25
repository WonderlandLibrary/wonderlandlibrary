package org.apache.logging.log4j.core.appender.rewrite;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "MapRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class MapRewritePolicy implements RewritePolicy {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final Map<String, Object> map;
  
  private final Mode mode;
  
  private MapRewritePolicy(Map<String, Object> map, Mode mode) {
    this.map = map;
    this.mode = mode;
  }
  
  public LogEvent rewrite(LogEvent source) {
    Message msg = source.getMessage();
    if (msg == null || !(msg instanceof MapMessage))
      return source; 
    MapMessage<?, Object> mapMsg = (MapMessage<?, Object>)msg;
    Map<String, Object> newMap = new HashMap<>(mapMsg.getData());
    switch (this.mode) {
      case Add:
        newMap.putAll(this.map);
        mapMessage = mapMsg.newInstance(newMap);
        return (LogEvent)(new Log4jLogEvent.Builder(source)).setMessage((Message)mapMessage).build();
    } 
    for (Map.Entry<String, Object> entry : this.map.entrySet()) {
      if (newMap.containsKey(entry.getKey()))
        newMap.put(entry.getKey(), entry.getValue()); 
    } 
    MapMessage mapMessage = mapMsg.newInstance(newMap);
    return (LogEvent)(new Log4jLogEvent.Builder(source)).setMessage((Message)mapMessage).build();
  }
  
  public enum Mode {
    Add, Update;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("mode=").append(this.mode);
    sb.append(" {");
    boolean first = true;
    for (Map.Entry<String, Object> entry : this.map.entrySet()) {
      if (!first)
        sb.append(", "); 
      sb.append(entry.getKey()).append('=').append(entry.getValue());
      first = false;
    } 
    sb.append('}');
    return sb.toString();
  }
  
  @PluginFactory
  public static MapRewritePolicy createPolicy(@PluginAttribute("mode") String mode, @PluginElement("KeyValuePair") KeyValuePair[] pairs) {
    Mode op = (mode == null) ? (op = Mode.Add) : Mode.valueOf(mode);
    if (pairs == null || pairs.length == 0) {
      LOGGER.error("keys and values must be specified for the MapRewritePolicy");
      return null;
    } 
    Map<String, Object> map = new HashMap<>();
    for (KeyValuePair pair : pairs) {
      String key = pair.getKey();
      if (key == null) {
        LOGGER.error("A null key is not valid in MapRewritePolicy");
      } else {
        String value = pair.getValue();
        if (value == null) {
          LOGGER.error("A null value for key " + key + " is not allowed in MapRewritePolicy");
        } else {
          map.put(pair.getKey(), pair.getValue());
        } 
      } 
    } 
    if (map.isEmpty()) {
      LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
      return null;
    } 
    return new MapRewritePolicy(map, op);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rewrite\MapRewritePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */