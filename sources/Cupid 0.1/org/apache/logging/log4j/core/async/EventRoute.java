package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.message.Message;

public enum EventRoute {
  ENQUEUE {
    public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {}
    
    public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
      asyncLoggerConfig.logInBackgroundThread(event);
    }
    
    public void logMessage(AsyncAppender asyncAppender, LogEvent logEvent) {
      asyncAppender.logMessageInBackgroundThread(logEvent);
    }
  },
  SYNCHRONOUS {
    public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {}
    
    public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
      asyncLoggerConfig.logToAsyncLoggerConfigsOnCurrentThread(event);
    }
    
    public void logMessage(AsyncAppender asyncAppender, LogEvent logEvent) {
      asyncAppender.logMessageInCurrentThread(logEvent);
    }
  },
  DISCARD {
    public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {}
    
    public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {}
    
    public void logMessage(AsyncAppender asyncAppender, LogEvent coreEvent) {}
  };
  
  public abstract void logMessage(AsyncLogger paramAsyncLogger, String paramString, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  public abstract void logMessage(AsyncLoggerConfig paramAsyncLoggerConfig, LogEvent paramLogEvent);
  
  public abstract void logMessage(AsyncAppender paramAsyncAppender, LogEvent paramLogEvent);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\EventRoute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */