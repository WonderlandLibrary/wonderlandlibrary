package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

public interface LocationAwareLogger {
  void logMessage(Level paramLevel, Marker paramMarker, String paramString, StackTraceElement paramStackTraceElement, Message paramMessage, Throwable paramThrowable);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\LocationAwareLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */