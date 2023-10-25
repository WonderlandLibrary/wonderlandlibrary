package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;

public class NullConfiguration extends AbstractConfiguration {
  public static final String NULL_NAME = "Null";
  
  public NullConfiguration() {
    super(null, ConfigurationSource.NULL_SOURCE);
    setName("Null");
    LoggerConfig root = getRootLogger();
    root.setLevel(Level.OFF);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\NullConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */