package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;

public class PluginConfigurationVisitor extends AbstractPluginVisitor<PluginConfiguration> {
  public PluginConfigurationVisitor() {
    super(PluginConfiguration.class);
  }
  
  public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
    if (this.conversionType.isInstance(configuration)) {
      log.append("Configuration");
      if (configuration.getName() != null)
        log.append('(').append(configuration.getName()).append(')'); 
      return configuration;
    } 
    LOGGER.warn("Variable annotated with @PluginConfiguration is not compatible with type {}.", configuration
        .getClass());
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginConfigurationVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */