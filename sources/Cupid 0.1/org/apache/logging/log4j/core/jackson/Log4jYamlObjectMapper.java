package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class Log4jYamlObjectMapper extends YAMLMapper {
  private static final long serialVersionUID = 1L;
  
  public Log4jYamlObjectMapper() {
    this(false, true, false);
  }
  
  public Log4jYamlObjectMapper(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString) {
    registerModule((Module)new Log4jYamlModule(encodeThreadContextAsList, includeStacktrace, stacktraceAsString));
    setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\Log4jYamlObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */