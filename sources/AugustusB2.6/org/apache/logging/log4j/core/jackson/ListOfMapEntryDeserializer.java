// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.util.Map;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ListOfMapEntryDeserializer extends StdDeserializer<Map<String, String>>
{
    private static final long serialVersionUID = 1L;
    
    ListOfMapEntryDeserializer() {
        super((Class)Map.class);
    }
    
    public Map<String, String> deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final List<MapEntry> list = (List<MapEntry>)jp.readValueAs((TypeReference)new TypeReference<List<MapEntry>>() {});
        final HashMap<String, String> map = new HashMap<String, String>(list.size());
        for (final MapEntry mapEntry : list) {
            map.put(mapEntry.getKey(), mapEntry.getValue());
        }
        return map;
    }
}
