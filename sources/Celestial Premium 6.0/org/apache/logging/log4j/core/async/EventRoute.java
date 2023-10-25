/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.async.AsyncLogger;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.message.Message;

public enum EventRoute {
    ENQUEUE{

        @Override
        public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
        }

        @Override
        public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
            asyncLoggerConfig.logInBackgroundThread(event);
        }

        @Override
        public void logMessage(AsyncAppender asyncAppender, LogEvent logEvent) {
            asyncAppender.logMessageInBackgroundThread(logEvent);
        }
    }
    ,
    SYNCHRONOUS{

        @Override
        public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
        }

        @Override
        public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
            asyncLoggerConfig.logToAsyncLoggerConfigsOnCurrentThread(event);
        }

        @Override
        public void logMessage(AsyncAppender asyncAppender, LogEvent logEvent) {
            asyncAppender.logMessageInCurrentThread(logEvent);
        }
    }
    ,
    DISCARD{

        @Override
        public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
        }

        @Override
        public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
        }

        @Override
        public void logMessage(AsyncAppender asyncAppender, LogEvent coreEvent) {
        }
    };


    public abstract void logMessage(AsyncLogger var1, String var2, Level var3, Marker var4, Message var5, Throwable var6);

    public abstract void logMessage(AsyncLoggerConfig var1, LogEvent var2);

    public abstract void logMessage(AsyncAppender var1, LogEvent var2);
}

