// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.Map;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ListOfMapEntrySerializer extends StdSerializer<Map<String, String>>
{
    private static final long serialVersionUID = 1L;
    
    protected ListOfMapEntrySerializer() {
        super((Class)Map.class, false);
    }
    
    public void serialize(final Map<String, String> map, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        final Set<Map.Entry<String, String>> entrySet = map.entrySet();
        final MapEntry[] pairs = new MapEntry[entrySet.size()];
        int i = 0;
        for (final Map.Entry<String, String> entry : entrySet) {
            pairs[i++] = new MapEntry(entry.getKey(), entry.getValue());
        }
        jgen.writeObject((Object)pairs);
    }
}
