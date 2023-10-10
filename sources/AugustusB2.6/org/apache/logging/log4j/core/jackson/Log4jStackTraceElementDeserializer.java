// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public final class Log4jStackTraceElementDeserializer extends StdScalarDeserializer<StackTraceElement>
{
    private static final long serialVersionUID = 1L;
    
    public Log4jStackTraceElementDeserializer() {
        super((Class)StackTraceElement.class);
    }
    
    public StackTraceElement deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            String className = null;
            String methodName = null;
            String fileName = null;
            int lineNumber = -1;
            while ((t = jp.nextValue()) != JsonToken.END_OBJECT) {
                final String propName = jp.getCurrentName();
                if ("class".equals(propName)) {
                    className = jp.getText();
                }
                else if ("file".equals(propName)) {
                    fileName = jp.getText();
                }
                else {
                    if ("line".equals(propName)) {
                        if (t.isNumeric()) {
                            lineNumber = jp.getIntValue();
                            continue;
                        }
                        try {
                            lineNumber = Integer.parseInt(jp.getText().trim());
                            continue;
                        }
                        catch (NumberFormatException e) {
                            throw JsonMappingException.from(jp, "Non-numeric token (" + t + ") for property 'line'", (Throwable)e);
                        }
                    }
                    if ("method".equals(propName)) {
                        methodName = jp.getText();
                    }
                    else {
                        if ("nativeMethod".equals(propName)) {
                            continue;
                        }
                        this.handleUnknownProperty(jp, ctxt, (Object)this._valueClass, propName);
                    }
                }
            }
            return new StackTraceElement(className, methodName, fileName, lineNumber);
        }
        throw ctxt.mappingException(this._valueClass, t);
    }
}
