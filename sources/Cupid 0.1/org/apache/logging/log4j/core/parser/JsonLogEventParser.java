package org.apache.logging.log4j.core.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;

public class JsonLogEventParser extends AbstractJacksonLogEventParser {
  public JsonLogEventParser() {
    super((ObjectMapper)new Log4jJsonObjectMapper());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\parser\JsonLogEventParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */