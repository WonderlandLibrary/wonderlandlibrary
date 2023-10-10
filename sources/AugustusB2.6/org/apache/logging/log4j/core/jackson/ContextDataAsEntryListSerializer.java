// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import org.apache.logging.log4j.util.BiConsumer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ContextDataAsEntryListSerializer extends StdSerializer<ReadOnlyStringMap>
{
    private static final long serialVersionUID = 1L;
    
    protected ContextDataAsEntryListSerializer() {
        super((Class)Map.class, false);
    }
    
    public void serialize(final ReadOnlyStringMap contextData, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        final MapEntry[] pairs = new MapEntry[contextData.size()];
        contextData.forEach(new BiConsumer<String, Object>() {
            int i = 0;
            
            @Override
            public void accept(final String key, final Object value) {
                pairs[this.i++] = new MapEntry(key, String.valueOf(value));
            }
        });
        jgen.writeObject((Object)pairs);
    }
}
