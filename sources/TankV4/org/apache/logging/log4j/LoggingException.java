package org.apache.logging.log4j;

public class LoggingException extends RuntimeException {
   private static final long serialVersionUID = 6366395965071580537L;

   public LoggingException(String var1) {
      super(var1);
   }

   public LoggingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public LoggingException(Throwable var1) {
      super(var1);
   }
}
