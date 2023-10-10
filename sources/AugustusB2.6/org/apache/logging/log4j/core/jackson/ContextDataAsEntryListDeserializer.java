// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.util.Map;
import org.apache.logging.log4j.util.StringMap;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ContextDataAsEntryListDeserializer extends StdDeserializer<StringMap>
{
    private static final long serialVersionUID = 1L;
    
    ContextDataAsEntryListDeserializer() {
        super((Class)Map.class);
    }
    
    public StringMap deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final List<MapEntry> list = (List<MapEntry>)jp.readValueAs((TypeReference)new TypeReference<List<MapEntry>>() {});
        new ContextDataFactory();
        final StringMap contextData = ContextDataFactory.createContextData();
        for (final MapEntry mapEntry : list) {
            contextData.putValue(mapEntry.getKey(), mapEntry.getValue());
        }
        return contextData;
    }
}
