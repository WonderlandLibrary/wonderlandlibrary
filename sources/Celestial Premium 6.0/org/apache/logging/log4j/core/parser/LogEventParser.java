/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.parser.ParseException;

public interface LogEventParser {
    public LogEvent parseFrom(byte[] var1) throws ParseException;

    public LogEvent parseFrom(byte[] var1, int var2, int var3) throws ParseException;
}

