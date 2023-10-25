package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.TriConsumer;

public class ContextDataSerializer extends StdSerializer<ReadOnlyStringMap> {
  private static final long serialVersionUID = 1L;
  
  private static final TriConsumer<String, Object, JsonGenerator> WRITE_STRING_FIELD_INTO;
  
  protected ContextDataSerializer() {
    super(Map.class, false);
  }
  
  public void serialize(ReadOnlyStringMap contextData, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
    jgen.writeStartObject();
    contextData.forEach(WRITE_STRING_FIELD_INTO, jgen);
    jgen.writeEndObject();
  }
  
  static {
    WRITE_STRING_FIELD_INTO = ((key, value, jsonGenerator) -> {
        try {
          if (value == null) {
            jsonGenerator.writeNullField(key);
          } else {
            jsonGenerator.writeStringField(key, String.valueOf(value));
          } 
        } catch (Exception ex) {
          throw new IllegalStateException("Problem with key " + key, ex);
        } 
      });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\ContextDataSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */