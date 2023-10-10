// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;

public class JsonLogEventParser extends AbstractJacksonLogEventParser
{
    public JsonLogEventParser() {
        super(new Log4jJsonObjectMapper());
    }
}
