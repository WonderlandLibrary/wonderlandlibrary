/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.parser.LogEventParser;
import org.apache.logging.log4j.core.parser.ParseException;

public interface TextLogEventParser
extends LogEventParser {
    public LogEvent parseFrom(String var1) throws ParseException;
}

