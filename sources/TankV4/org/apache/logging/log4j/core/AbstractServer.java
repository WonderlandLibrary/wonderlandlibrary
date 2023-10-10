package org.apache.logging.log4j.core;

import org.apache.logging.log4j.LogManager;

public class AbstractServer {
   private final LoggerContext context = (LoggerContext)LogManager.getContext(false);

   protected AbstractServer() {
   }

   protected void log(LogEvent var1) {
      Logger var2 = this.context.getLogger(var1.getLoggerName());
      if (var2.config.filter(var1.getLevel(), var1.getMarker(), var1.getMessage(), var1.getThrown())) {
         var2.config.logEvent(var1);
      }

   }
}
