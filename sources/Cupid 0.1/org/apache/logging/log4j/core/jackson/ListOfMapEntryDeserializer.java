package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfMapEntryDeserializer extends StdDeserializer<Map<String, String>> {
  private static final long serialVersionUID = 1L;
  
  ListOfMapEntryDeserializer() {
    super(Map.class);
  }
  
  public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    List<MapEntry> list = (List<MapEntry>)jp.readValueAs(new TypeReference<List<MapEntry>>() {
        
        });
    HashMap<String, String> map = new HashMap<>(list.size());
    for (MapEntry mapEntry : list)
      map.put(mapEntry.getKey(), mapEntry.getValue()); 
    return map;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\jackson\ListOfMapEntryDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */