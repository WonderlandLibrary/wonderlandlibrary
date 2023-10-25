package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public class ConfigurationFactoryData {
  public final Configuration configuration;
  
  public ConfigurationFactoryData(Configuration configuration) {
    this.configuration = configuration;
  }
  
  public Configuration getConfiguration() {
    return this.configuration;
  }
  
  public LoggerContext getLoggerContext() {
    return (this.configuration != null) ? this.configuration.getLoggerContext() : null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\ConfigurationFactoryData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */