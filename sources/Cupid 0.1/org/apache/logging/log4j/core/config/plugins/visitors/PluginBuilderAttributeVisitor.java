package org.apache.logging.log4j.core.config.plugins.visitors;

import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.util.StringBuilders;

public class PluginBuilderAttributeVisitor extends AbstractPluginVisitor<PluginBuilderAttribute> {
  public PluginBuilderAttributeVisitor() {
    super(PluginBuilderAttribute.class);
  }
  
  public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
    String overridden = this.annotation.value();
    String name = overridden.isEmpty() ? this.member.getName() : overridden;
    Map<String, String> attributes = node.getAttributes();
    String rawValue = removeAttributeValue(attributes, name, this.aliases);
    String replacedValue = this.substitutor.replace(event, rawValue);
    Object value = convert(replacedValue, null);
    Object debugValue = this.annotation.sensitive() ? "*****" : value;
    StringBuilders.appendKeyDqValue(log, name, debugValue);
    return value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginBuilderAttributeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */