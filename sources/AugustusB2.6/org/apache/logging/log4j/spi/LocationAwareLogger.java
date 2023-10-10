// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;

public interface LocationAwareLogger
{
    void logMessage(final Level level, final Marker marker, final String fqcn, final StackTraceElement location, final Message message, final Throwable throwable);
}
