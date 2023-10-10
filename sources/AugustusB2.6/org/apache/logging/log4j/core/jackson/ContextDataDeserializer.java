// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.util.Map;
import org.apache.logging.log4j.util.StringMap;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ContextDataDeserializer extends StdDeserializer<StringMap>
{
    private static final long serialVersionUID = 1L;
    
    ContextDataDeserializer() {
        super((Class)Map.class);
    }
    
    public StringMap deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final StringMap contextData = ContextDataFactory.createContextData();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            final String fieldName = jp.getCurrentName();
            jp.nextToken();
            contextData.putValue(fieldName, jp.getText());
        }
        return contextData;
    }
}
