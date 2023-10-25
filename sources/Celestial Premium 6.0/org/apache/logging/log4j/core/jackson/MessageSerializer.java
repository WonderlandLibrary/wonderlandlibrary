/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonGenerationException
 *  com.fasterxml.jackson.core.JsonGenerator
 *  com.fasterxml.jackson.databind.SerializerProvider
 *  com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
 */
package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import java.io.IOException;
import org.apache.logging.log4j.message.Message;

final class MessageSerializer
extends StdScalarSerializer<Message> {
    private static final long serialVersionUID = 1L;

    MessageSerializer() {
        super(Message.class);
    }

    public void serialize(Message value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(value.getFormattedMessage());
    }
}

