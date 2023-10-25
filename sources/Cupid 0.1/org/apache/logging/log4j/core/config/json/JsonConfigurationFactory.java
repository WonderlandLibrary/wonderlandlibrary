package org.apache.logging.log4j.core.config.json;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.Loader;

@Plugin(name = "JsonConfigurationFactory", category = "ConfigurationFactory")
@Order(6)
public class JsonConfigurationFactory extends ConfigurationFactory {
  private static final String[] SUFFIXES = new String[] { ".json", ".jsn" };
  
  private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
  
  private final boolean isActive;
  
  public JsonConfigurationFactory() {
    for (String dependency : dependencies) {
      if (!Loader.isClassAvailable(dependency)) {
        LOGGER.debug("Missing dependencies for Json support, ConfigurationFactory {} is inactive", getClass().getName());
        this.isActive = false;
        return;
      } 
    } 
    this.isActive = true;
  }
  
  protected boolean isActive() {
    return this.isActive;
  }
  
  public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
    if (!this.isActive)
      return null; 
    return (Configuration)new JsonConfiguration(loggerContext, source);
  }
  
  public String[] getSupportedTypes() {
    return SUFFIXES;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\json\JsonConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */