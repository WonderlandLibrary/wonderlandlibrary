// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.message.Message;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

final class MessageSerializer extends StdScalarSerializer<Message>
{
    private static final long serialVersionUID = 1L;
    
    MessageSerializer() {
        super((Class)Message.class);
    }
    
    public void serialize(final Message value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(value.getFormattedMessage());
    }
}
