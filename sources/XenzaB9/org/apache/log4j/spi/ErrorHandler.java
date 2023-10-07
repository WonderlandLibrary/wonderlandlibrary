// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public interface ErrorHandler
{
    void setLogger(final Logger logger);
    
    void error(final String message, final Exception e, final int errorCode);
    
    void error(final String message);
    
    void error(final String message, final Exception e, final int errorCode, final LoggingEvent event);
    
    void setAppender(final Appender appender);
    
    void setBackupAppender(final Appender appender);
}
