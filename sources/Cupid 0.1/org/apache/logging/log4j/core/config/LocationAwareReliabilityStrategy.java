package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Supplier;

public interface LocationAwareReliabilityStrategy {
  void log(Supplier<LoggerConfig> paramSupplier, String paramString1, String paramString2, StackTraceElement paramStackTraceElement, Marker paramMarker, Level paramLevel, Message paramMessage, Throwable paramThrowable);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\LocationAwareReliabilityStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */