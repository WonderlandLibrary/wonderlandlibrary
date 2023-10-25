/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonParser
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.DeserializationContext
 *  com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;
import org.apache.logging.log4j.message.SimpleMessage;

public final class SimpleMessageDeserializer
extends StdScalarDeserializer<SimpleMessage> {
    private static final long serialVersionUID = 1L;

    SimpleMessageDeserializer() {
        super(SimpleMessage.class);
    }

    public SimpleMessage deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new SimpleMessage(jp.getValueAsString());
    }
}

