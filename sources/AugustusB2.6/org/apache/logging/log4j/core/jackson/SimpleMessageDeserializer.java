// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.logging.log4j.message.SimpleMessage;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public final class SimpleMessageDeserializer extends StdScalarDeserializer<SimpleMessage>
{
    private static final long serialVersionUID = 1L;
    
    SimpleMessageDeserializer() {
        super((Class)SimpleMessage.class);
    }
    
    public SimpleMessage deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new SimpleMessage(jp.getValueAsString());
    }
}
