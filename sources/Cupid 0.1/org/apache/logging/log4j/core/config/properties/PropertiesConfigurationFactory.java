package org.apache.logging.log4j.core.config.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PropertiesConfigurationFactory", category = "ConfigurationFactory")
@Order(8)
public class PropertiesConfigurationFactory extends ConfigurationFactory {
  protected String[] getSupportedTypes() {
    return new String[] { ".properties" };
  }
  
  public PropertiesConfiguration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
    Properties properties = new Properties();
    try (InputStream configStream = source.getInputStream()) {
      properties.load(configStream);
    } catch (IOException ioe) {
      throw new ConfigurationException("Unable to load " + source.toString(), ioe);
    } 
    return (new PropertiesConfigurationBuilder())
      .setConfigurationSource(source)
      .setRootProperties(properties)
      .setLoggerContext(loggerContext)
      .build();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\properties\PropertiesConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */