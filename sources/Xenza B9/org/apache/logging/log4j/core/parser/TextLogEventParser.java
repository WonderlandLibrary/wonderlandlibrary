// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;

public interface TextLogEventParser extends LogEventParser
{
    LogEvent parseFrom(final String input) throws ParseException;
}
