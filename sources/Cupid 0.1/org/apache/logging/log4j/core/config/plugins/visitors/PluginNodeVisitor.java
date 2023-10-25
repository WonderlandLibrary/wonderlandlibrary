package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginNode;

public class PluginNodeVisitor extends AbstractPluginVisitor<PluginNode> {
  public PluginNodeVisitor() {
    super(PluginNode.class);
  }
  
  public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
    if (this.conversionType.isInstance(node)) {
      log.append("Node=").append(node.getName());
      return node;
    } 
    LOGGER.warn("Variable annotated with @PluginNode is not compatible with the type {}.", node.getClass());
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginNodeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */