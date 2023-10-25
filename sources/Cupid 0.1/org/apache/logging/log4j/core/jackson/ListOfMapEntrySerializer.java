package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ListOfMapEntrySerializer extends StdSerializer<Map<String, String>> {
  private static final long serialVersionUID = 1L;
  
  protected ListOfMapEntrySerializer() {
    super(Map.class, false);
  }
  
  public void serialize(Map<String, String> map, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
    Set<Map.Entry<String, String>> entrySet = map.entrySet();
    MapEntry[] pairs = new MapEntry[entrySet.size()];
    int i = 0;
    for (Map.Entry<String, String> entry : entrySet)
      pairs[i++] = new MapEntry(entry.getKey(), entry.getValue()); 
    jgen.writeObject(pairs);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\ListOfMapEntrySerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */