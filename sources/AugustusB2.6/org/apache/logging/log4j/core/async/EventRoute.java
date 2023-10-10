// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;

public enum EventRoute
{
    ENQUEUE {
        @Override
        public void logMessage(final AsyncLogger asyncLogger, final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        }
        
        @Override
        public void logMessage(final AsyncLoggerConfig asyncLoggerConfig, final LogEvent event) {
            asyncLoggerConfig.logInBackgroundThread(event);
        }
        
        @Override
        public void logMessage(final AsyncAppender asyncAppender, final LogEvent logEvent) {
            asyncAppender.logMessageInBackgroundThread(logEvent);
        }
    }, 
    SYNCHRONOUS {
        @Override
        public void logMessage(final AsyncLogger asyncLogger, final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        }
        
        @Override
        public void logMessage(final AsyncLoggerConfig asyncLoggerConfig, final LogEvent event) {
            asyncLoggerConfig.logToAsyncLoggerConfigsOnCurrentThread(event);
        }
        
        @Override
        public void logMessage(final AsyncAppender asyncAppender, final LogEvent logEvent) {
            asyncAppender.logMessageInCurrentThread(logEvent);
        }
    }, 
    DISCARD {
        @Override
        public void logMessage(final AsyncLogger asyncLogger, final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        }
        
        @Override
        public void logMessage(final AsyncLoggerConfig asyncLoggerConfig, final LogEvent event) {
        }
        
        @Override
        public void logMessage(final AsyncAppender asyncAppender, final LogEvent coreEvent) {
        }
    };
    
    public abstract void logMessage(final AsyncLogger asyncLogger, final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown);
    
    public abstract void logMessage(final AsyncLoggerConfig asyncLoggerConfig, final LogEvent event);
    
    public abstract void logMessage(final AsyncAppender asyncAppender, final LogEvent coreEvent);
}
