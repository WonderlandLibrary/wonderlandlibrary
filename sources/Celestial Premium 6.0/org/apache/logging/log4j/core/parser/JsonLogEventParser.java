/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.apache.logging.log4j.core.parser.AbstractJacksonLogEventParser;

public class JsonLogEventParser
extends AbstractJacksonLogEventParser {
    public JsonLogEventParser() {
        super(new Log4jJsonObjectMapper());
    }
}

