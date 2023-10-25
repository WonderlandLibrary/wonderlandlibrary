/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 */
package org.apache.logging.log4j.core.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jYamlObjectMapper;
import org.apache.logging.log4j.core.parser.AbstractJacksonLogEventParser;

public class YamlLogEventParser
extends AbstractJacksonLogEventParser {
    public YamlLogEventParser() {
        super((ObjectMapper)new Log4jYamlObjectMapper());
    }
}

