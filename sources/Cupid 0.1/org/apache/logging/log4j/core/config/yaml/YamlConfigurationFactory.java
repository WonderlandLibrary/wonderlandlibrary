package org.apache.logging.log4j.core.config.yaml;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.Loader;

@Plugin(name = "YamlConfigurationFactory", category = "ConfigurationFactory")
@Order(7)
public class YamlConfigurationFactory extends ConfigurationFactory {
  private static final String[] SUFFIXES = new String[] { ".yml", ".yaml" };
  
  private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser", "com.fasterxml.jackson.dataformat.yaml.YAMLFactory" };
  
  private final boolean isActive;
  
  public YamlConfigurationFactory() {
    for (String dependency : dependencies) {
      if (!Loader.isClassAvailable(dependency)) {
        LOGGER.debug("Missing dependencies for Yaml support, ConfigurationFactory {} is inactive", getClass().getName());
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
    return (Configuration)new YamlConfiguration(loggerContext, source);
  }
  
  public String[] getSupportedTypes() {
    return SUFFIXES;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\yaml\YamlConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */