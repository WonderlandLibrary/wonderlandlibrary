package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public enum Severity {
   EMERG(0),
   ALERT(1),
   CRITICAL(2),
   ERROR(3),
   WARNING(4),
   NOTICE(5),
   INFO(6),
   DEBUG(7);

   private final int code;
   private static final Severity[] $VALUES = new Severity[]{EMERG, ALERT, CRITICAL, ERROR, WARNING, NOTICE, INFO, DEBUG};

   private Severity(int var3) {
      this.code = var3;
   }

   public int getCode() {
      return this.code;
   }

   public boolean isEqual(String var1) {
      return this.name().equalsIgnoreCase(var1);
   }

   public static Severity getSeverity(Level var0) {
      switch(var0) {
      case ALL:
         return DEBUG;
      case TRACE:
         return DEBUG;
      case DEBUG:
         return DEBUG;
      case INFO:
         return INFO;
      case WARN:
         return WARNING;
      case ERROR:
         return ERROR;
      case FATAL:
         return ALERT;
      case OFF:
         return EMERG;
      default:
         return DEBUG;
      }
   }
}
