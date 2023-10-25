package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Log4jXmlObjectMapper extends XmlMapper {
  private static final long serialVersionUID = 1L;
  
  public Log4jXmlObjectMapper() {
    this(true, false);
  }
  
  public Log4jXmlObjectMapper(boolean includeStacktrace, boolean stacktraceAsString) {
    super(new Log4jXmlModule(includeStacktrace, stacktraceAsString));
    setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\Log4jXmlObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */