package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.LoggingException;

public class AppenderLoggingException extends LoggingException {
   private static final long serialVersionUID = 6545990597472958303L;

   public AppenderLoggingException(String var1) {
      super(var1);
   }

   public AppenderLoggingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public AppenderLoggingException(Throwable var1) {
      super(var1);
   }
}
