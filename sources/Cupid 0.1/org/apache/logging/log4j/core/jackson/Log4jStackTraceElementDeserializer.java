package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

public final class Log4jStackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement> {
  private static final long serialVersionUID = 1L;
  
  public Log4jStackTraceElementDeserializer() {
    super(StackTraceElement.class);
  }
  
  public StackTraceElement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    JsonToken t = jp.getCurrentToken();
    if (t == JsonToken.START_OBJECT) {
      String className = null, methodName = null, fileName = null;
      int lineNumber = -1;
      while ((t = jp.nextValue()) != JsonToken.END_OBJECT) {
        String propName = jp.getCurrentName();
        if ("class".equals(propName)) {
          className = jp.getText();
          continue;
        } 
        if ("file".equals(propName)) {
          fileName = jp.getText();
          continue;
        } 
        if ("line".equals(propName)) {
          if (t.isNumeric()) {
            lineNumber = jp.getIntValue();
            continue;
          } 
          try {
            lineNumber = Integer.parseInt(jp.getText().trim());
          } catch (NumberFormatException e) {
            throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'line'", e);
          } 
          continue;
        } 
        if ("method".equals(propName)) {
          methodName = jp.getText();
          continue;
        } 
        if ("nativeMethod".equals(propName))
          continue; 
        handleUnknownProperty(jp, ctxt, this._valueClass, propName);
      } 
      return new StackTraceElement(className, methodName, fileName, lineNumber);
    } 
    throw ctxt.mappingException(this._valueClass, t);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\Log4jStackTraceElementDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */