package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.util.StringBuilders;

public class PluginAttributeVisitor extends AbstractPluginVisitor<PluginAttribute> {
  public PluginAttributeVisitor() {
    super(PluginAttribute.class);
  }
  
  public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
    String name = this.annotation.value();
    Map<String, String> attributes = node.getAttributes();
    String rawValue = removeAttributeValue(attributes, name, this.aliases);
    String replacedValue = this.substitutor.replace(event, rawValue);
    Object defaultValue = findDefaultValue(event);
    Object value = convert(replacedValue, defaultValue);
    Object debugValue = this.annotation.sensitive() ? "*****" : value;
    StringBuilders.appendKeyDqValue(log, name, debugValue);
    return value;
  }
  
  private Object findDefaultValue(LogEvent event) {
    if (this.conversionType == int.class || this.conversionType == Integer.class)
      return Integer.valueOf(this.annotation.defaultInt()); 
    if (this.conversionType == long.class || this.conversionType == Long.class)
      return Long.valueOf(this.annotation.defaultLong()); 
    if (this.conversionType == boolean.class || this.conversionType == Boolean.class)
      return Boolean.valueOf(this.annotation.defaultBoolean()); 
    if (this.conversionType == float.class || this.conversionType == Float.class)
      return Float.valueOf(this.annotation.defaultFloat()); 
    if (this.conversionType == double.class || this.conversionType == Double.class)
      return Double.valueOf(this.annotation.defaultDouble()); 
    if (this.conversionType == byte.class || this.conversionType == Byte.class)
      return Byte.valueOf(this.annotation.defaultByte()); 
    if (this.conversionType == char.class || this.conversionType == Character.class)
      return Character.valueOf(this.annotation.defaultChar()); 
    if (this.conversionType == short.class || this.conversionType == Short.class)
      return Short.valueOf(this.annotation.defaultShort()); 
    if (this.conversionType == Class.class)
      return this.annotation.defaultClass(); 
    return this.substitutor.replace(event, this.annotation.defaultString());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginAttributeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */