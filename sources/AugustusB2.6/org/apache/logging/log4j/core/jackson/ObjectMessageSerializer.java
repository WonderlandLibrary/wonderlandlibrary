// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.message.ObjectMessage;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

final class ObjectMessageSerializer extends StdScalarSerializer<ObjectMessage>
{
    private static final long serialVersionUID = 1L;
    
    ObjectMessageSerializer() {
        super((Class)ObjectMessage.class);
    }
    
    public void serialize(final ObjectMessage value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeObject(value.getParameter());
    }
}
