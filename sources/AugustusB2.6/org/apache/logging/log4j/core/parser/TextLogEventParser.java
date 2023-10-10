// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;

public interface TextLogEventParser extends LogEventParser
{
    LogEvent parseFrom(final String input) throws ParseException;
}
