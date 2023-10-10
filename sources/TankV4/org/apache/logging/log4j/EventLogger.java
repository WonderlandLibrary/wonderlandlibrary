package org.apache.logging.log4j;

import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.AbstractLoggerWrapper;

public final class EventLogger {
   private static final String NAME = "EventLogger";
   public static final Marker EVENT_MARKER = MarkerManager.getMarker("EVENT");
   private static final String FQCN = EventLogger.class.getName();
   private static AbstractLoggerWrapper loggerWrapper;

   private EventLogger() {
   }

   public static void logEvent(StructuredDataMessage var0) {
      loggerWrapper.log(EVENT_MARKER, FQCN, Level.OFF, var0, (Throwable)null);
   }

   public static void logEvent(StructuredDataMessage var0, Level var1) {
      loggerWrapper.log(EVENT_MARKER, FQCN, var1, var0, (Throwable)null);
   }

   static {
      Logger var0 = LogManager.getLogger("EventLogger");
      if (!(var0 instanceof AbstractLogger)) {
         throw new LoggingException("Logger returned must be based on AbstractLogger");
      } else {
         loggerWrapper = new AbstractLoggerWrapper((AbstractLogger)var0, "EventLogger", (MessageFactory)null);
      }
   }
}
