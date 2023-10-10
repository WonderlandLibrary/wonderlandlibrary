// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.parser;

import java.io.IOException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

class AbstractJacksonLogEventParser implements TextLogEventParser
{
    private final ObjectReader objectReader;
    
    AbstractJacksonLogEventParser(final ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectReader = objectMapper.readerFor((Class)Log4jLogEvent.class);
    }
    
    @Override
    public LogEvent parseFrom(final String input) throws ParseException {
        try {
            return (LogEvent)this.objectReader.readValue(input);
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }
    
    @Override
    public LogEvent parseFrom(final byte[] input) throws ParseException {
        try {
            return (LogEvent)this.objectReader.readValue(input);
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }
    
    @Override
    public LogEvent parseFrom(final byte[] input, final int offset, final int length) throws ParseException {
        try {
            return (LogEvent)this.objectReader.readValue(input, offset, length);
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }
}
