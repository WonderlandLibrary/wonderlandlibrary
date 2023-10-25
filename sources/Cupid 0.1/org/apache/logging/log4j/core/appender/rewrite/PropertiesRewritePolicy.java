package org.apache.logging.log4j.core.appender.rewrite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.StringMap;

@Plugin(name = "PropertiesRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class PropertiesRewritePolicy implements RewritePolicy {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private final Map<Property, Boolean> properties;
  
  private final Configuration config;
  
  private PropertiesRewritePolicy(Configuration config, List<Property> props) {
    this.config = config;
    this.properties = new HashMap<>(props.size());
    for (Property property : props) {
      Boolean interpolate = Boolean.valueOf(property.getValue().contains("${"));
      this.properties.put(property, interpolate);
    } 
  }
  
  public LogEvent rewrite(LogEvent source) {
    StringMap newContextData = ContextDataFactory.createContextData(source.getContextData());
    for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
      Property prop = entry.getKey();
      newContextData.putValue(prop.getName(), ((Boolean)entry.getValue()).booleanValue() ? this.config
          .getStrSubstitutor().replace(prop.getValue()) : prop.getValue());
    } 
    return (LogEvent)(new Log4jLogEvent.Builder(source)).setContextData(newContextData).build();
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(" {");
    boolean first = true;
    for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
      if (!first)
        sb.append(", "); 
      Property prop = entry.getKey();
      sb.append(prop.getName()).append('=').append(prop.getValue());
      first = false;
    } 
    sb.append('}');
    return sb.toString();
  }
  
  @PluginFactory
  public static PropertiesRewritePolicy createPolicy(@PluginConfiguration Configuration config, @PluginElement("Properties") Property[] props) {
    if (props == null || props.length == 0) {
      LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
      return null;
    } 
    List<Property> properties = Arrays.asList(props);
    return new PropertiesRewritePolicy(config, properties);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rewrite\PropertiesRewritePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */