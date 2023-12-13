// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ContextDataSerializer extends StdSerializer<ReadOnlyStringMap>
{
    private static final long serialVersionUID = 1L;
    private static final TriConsumer<String, Object, JsonGenerator> WRITE_STRING_FIELD_INTO;
    
    protected ContextDataSerializer() {
        super((Class)Map.class, false);
    }
    
    public void serialize(final ReadOnlyStringMap contextData, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        contextData.forEach(ContextDataSerializer.WRITE_STRING_FIELD_INTO, jgen);
        jgen.writeEndObject();
    }
    
    static {
        WRITE_STRING_FIELD_INTO = ((key, value, jsonGenerator) -> {
            try {
                if (value == null) {
                    jsonGenerator.writeNullField(key);
                }
                else {
                    jsonGenerator.writeStringField(key, String.valueOf(value));
                }
            }
            catch (final Exception ex) {
                new IllegalStateException("Problem with key " + key, ex);
                throw;
            }
        });
    }
}
