package org.apache.logging.log4j.core.config.properties;

import java.io.IOException;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.builder.api.Component;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class PropertiesConfiguration extends BuiltConfiguration implements Reconfigurable {
  public PropertiesConfiguration(LoggerContext loggerContext, ConfigurationSource source, Component root) {
    super(loggerContext, source, root);
  }
  
  public Configuration reconfigure() {
    try {
      ConfigurationSource source = getConfigurationSource().resetInputStream();
      if (source == null)
        return null; 
      PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory();
      PropertiesConfiguration config = factory.getConfiguration(getLoggerContext(), source);
      return (config == null || config.getState() != LifeCycle.State.INITIALIZING) ? null : (Configuration)config;
    } catch (IOException ex) {
      LOGGER.error("Cannot locate file {}: {}", getConfigurationSource(), ex);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\properties\PropertiesConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */