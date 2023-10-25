/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.ObjectReader
 */
package org.apache.logging.log4j.core.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.parser.ParseException;
import org.apache.logging.log4j.core.parser.TextLogEventParser;

class AbstractJacksonLogEventParser
implements TextLogEventParser {
    private final ObjectReader objectReader;

    AbstractJacksonLogEventParser(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectReader = objectMapper.readerFor(Log4jLogEvent.class);
    }

    @Override
    public LogEvent parseFrom(String input) throws ParseException {
        try {
            return (LogEvent)this.objectReader.readValue(input);
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public LogEvent parseFrom(byte[] input) throws ParseException {
        try {
            return (LogEvent)this.objectReader.readValue(input);
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public LogEvent parseFrom(byte[] input, int offset, int length) throws ParseException {
        try {
            return (LogEvent)this.objectReader.readValue(input, offset, length);
        }
        catch (IOException e) {
            throw new ParseException(e);
        }
    }
}

