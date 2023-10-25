package org.apache.logging.log4j.core.config.yaml;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;

public class YamlConfiguration extends JsonConfiguration {
  public YamlConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
    super(loggerContext, configSource);
  }
  
  protected ObjectMapper getObjectMapper() {
    return (new ObjectMapper((JsonFactory)new YAMLFactory())).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
  }
  
  public Configuration reconfigure() {
    try {
      ConfigurationSource source = getConfigurationSource().resetInputStream();
      if (source == null)
        return null; 
      return (Configuration)new YamlConfiguration(getLoggerContext(), source);
    } catch (IOException ex) {
      LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\yaml\YamlConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */