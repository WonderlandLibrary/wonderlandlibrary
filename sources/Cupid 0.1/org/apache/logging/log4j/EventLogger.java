package org.apache.logging.log4j;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.spi.ExtendedLogger;

public final class EventLogger {
  public static final Marker EVENT_MARKER = MarkerManager.getMarker("EVENT");
  
  private static final String NAME = "EventLogger";
  
  private static final String FQCN = EventLogger.class.getName();
  
  private static final ExtendedLogger LOGGER = LogManager.getContext(false).getLogger("EventLogger");
  
  public static void logEvent(StructuredDataMessage msg) {
    LOGGER.logIfEnabled(FQCN, Level.OFF, EVENT_MARKER, (Message)msg, null);
  }
  
  public static void logEvent(StructuredDataMessage msg, Level level) {
    LOGGER.logIfEnabled(FQCN, level, EVENT_MARKER, (Message)msg, null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\EventLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */