package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

public class DefaultErrorHandler implements ErrorHandler {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final int MAX_EXCEPTIONS = 3;
   private static final int EXCEPTION_INTERVAL = 300000;
   private int exceptionCount = 0;
   private long lastException;
   private final Appender appender;

   public DefaultErrorHandler(Appender var1) {
      this.appender = var1;
   }

   public void error(String var1) {
      long var2 = System.currentTimeMillis();
      if (this.lastException + 300000L < var2 || this.exceptionCount++ < 3) {
         LOGGER.error(var1);
      }

      this.lastException = var2;
   }

   public void error(String var1, Throwable var2) {
      long var3 = System.currentTimeMillis();
      if (this.lastException + 300000L < var3 || this.exceptionCount++ < 3) {
         LOGGER.error(var1, var2);
      }

      this.lastException = var3;
      if (!this.appender.ignoreExceptions() && var2 != null && !(var2 instanceof AppenderLoggingException)) {
         throw new AppenderLoggingException(var1, var2);
      }
   }

   public void error(String var1, LogEvent var2, Throwable var3) {
      long var4 = System.currentTimeMillis();
      if (this.lastException + 300000L < var4 || this.exceptionCount++ < 3) {
         LOGGER.error(var1, var3);
      }

      this.lastException = var4;
      if (!this.appender.ignoreExceptions() && var3 != null && !(var3 instanceof AppenderLoggingException)) {
         throw new AppenderLoggingException(var1, var3);
      }
   }
}
