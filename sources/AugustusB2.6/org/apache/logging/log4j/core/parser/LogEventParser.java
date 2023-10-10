// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.parser;

import org.apache.logging.log4j.core.LogEvent;

public interface LogEventParser
{
    LogEvent parseFrom(final byte[] input) throws ParseException;
    
    LogEvent parseFrom(final byte[] input, final int offset, final int length) throws ParseException;
}
