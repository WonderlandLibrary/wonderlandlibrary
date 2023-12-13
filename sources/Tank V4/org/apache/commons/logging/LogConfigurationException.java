package org.apache.commons.logging;

public class LogConfigurationException extends RuntimeException {
   private static final long serialVersionUID = 8486587136871052495L;
   protected Throwable cause;

   public LogConfigurationException() {
      this.cause = null;
   }

   public LogConfigurationException(String var1) {
      super(var1);
      this.cause = null;
   }

   public LogConfigurationException(Throwable var1) {
      this(var1 == null ? null : var1.toString(), var1);
   }

   public LogConfigurationException(String var1, Throwable var2) {
      super(var1 + " (Caused by " + var2 + ")");
      this.cause = null;
      this.cause = var2;
   }

   public Throwable getCause() {
      return this.cause;
   }
}
