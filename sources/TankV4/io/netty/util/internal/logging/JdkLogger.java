package io.netty.util.internal.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class JdkLogger extends AbstractInternalLogger {
   private static final long serialVersionUID = -1767272577989225979L;
   final transient Logger logger;
   static final String SELF = JdkLogger.class.getName();
   static final String SUPER = AbstractInternalLogger.class.getName();

   JdkLogger(Logger var1) {
      super(var1.getName());
      this.logger = var1;
   }

   public boolean isTraceEnabled() {
      return this.logger.isLoggable(Level.FINEST);
   }

   public void trace(String var1) {
      if (this.logger.isLoggable(Level.FINEST)) {
         this.log(SELF, Level.FINEST, var1, (Throwable)null);
      }

   }

   public void trace(String var1, Object var2) {
      if (this.logger.isLoggable(Level.FINEST)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.log(SELF, Level.FINEST, var3.getMessage(), var3.getThrowable());
      }

   }

   public void trace(String var1, Object var2, Object var3) {
      if (this.logger.isLoggable(Level.FINEST)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.log(SELF, Level.FINEST, var4.getMessage(), var4.getThrowable());
      }

   }

   public void trace(String var1, Object... var2) {
      if (this.logger.isLoggable(Level.FINEST)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.log(SELF, Level.FINEST, var3.getMessage(), var3.getThrowable());
      }

   }

   public void trace(String var1, Throwable var2) {
      if (this.logger.isLoggable(Level.FINEST)) {
         this.log(SELF, Level.FINEST, var1, var2);
      }

   }

   public boolean isDebugEnabled() {
      return this.logger.isLoggable(Level.FINE);
   }

   public void debug(String var1) {
      if (this.logger.isLoggable(Level.FINE)) {
         this.log(SELF, Level.FINE, var1, (Throwable)null);
      }

   }

   public void debug(String var1, Object var2) {
      if (this.logger.isLoggable(Level.FINE)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.log(SELF, Level.FINE, var3.getMessage(), var3.getThrowable());
      }

   }

   public void debug(String var1, Object var2, Object var3) {
      if (this.logger.isLoggable(Level.FINE)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.log(SELF, Level.FINE, var4.getMessage(), var4.getThrowable());
      }

   }

   public void debug(String var1, Object... var2) {
      if (this.logger.isLoggable(Level.FINE)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.log(SELF, Level.FINE, var3.getMessage(), var3.getThrowable());
      }

   }

   public void debug(String var1, Throwable var2) {
      if (this.logger.isLoggable(Level.FINE)) {
         this.log(SELF, Level.FINE, var1, var2);
      }

   }

   public boolean isInfoEnabled() {
      return this.logger.isLoggable(Level.INFO);
   }

   public void info(String var1) {
      if (this.logger.isLoggable(Level.INFO)) {
         this.log(SELF, Level.INFO, var1, (Throwable)null);
      }

   }

   public void info(String var1, Object var2) {
      if (this.logger.isLoggable(Level.INFO)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.log(SELF, Level.INFO, var3.getMessage(), var3.getThrowable());
      }

   }

   public void info(String var1, Object var2, Object var3) {
      if (this.logger.isLoggable(Level.INFO)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.log(SELF, Level.INFO, var4.getMessage(), var4.getThrowable());
      }

   }

   public void info(String var1, Object... var2) {
      if (this.logger.isLoggable(Level.INFO)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.log(SELF, Level.INFO, var3.getMessage(), var3.getThrowable());
      }

   }

   public void info(String var1, Throwable var2) {
      if (this.logger.isLoggable(Level.INFO)) {
         this.log(SELF, Level.INFO, var1, var2);
      }

   }

   public boolean isWarnEnabled() {
      return this.logger.isLoggable(Level.WARNING);
   }

   public void warn(String var1) {
      if (this.logger.isLoggable(Level.WARNING)) {
         this.log(SELF, Level.WARNING, var1, (Throwable)null);
      }

   }

   public void warn(String var1, Object var2) {
      if (this.logger.isLoggable(Level.WARNING)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.log(SELF, Level.WARNING, var3.getMessage(), var3.getThrowable());
      }

   }

   public void warn(String var1, Object var2, Object var3) {
      if (this.logger.isLoggable(Level.WARNING)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.log(SELF, Level.WARNING, var4.getMessage(), var4.getThrowable());
      }

   }

   public void warn(String var1, Object... var2) {
      if (this.logger.isLoggable(Level.WARNING)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.log(SELF, Level.WARNING, var3.getMessage(), var3.getThrowable());
      }

   }

   public void warn(String var1, Throwable var2) {
      if (this.logger.isLoggable(Level.WARNING)) {
         this.log(SELF, Level.WARNING, var1, var2);
      }

   }

   public boolean isErrorEnabled() {
      return this.logger.isLoggable(Level.SEVERE);
   }

   public void error(String var1) {
      if (this.logger.isLoggable(Level.SEVERE)) {
         this.log(SELF, Level.SEVERE, var1, (Throwable)null);
      }

   }

   public void error(String var1, Object var2) {
      if (this.logger.isLoggable(Level.SEVERE)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.log(SELF, Level.SEVERE, var3.getMessage(), var3.getThrowable());
      }

   }

   public void error(String var1, Object var2, Object var3) {
      if (this.logger.isLoggable(Level.SEVERE)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.log(SELF, Level.SEVERE, var4.getMessage(), var4.getThrowable());
      }

   }

   public void error(String var1, Object... var2) {
      if (this.logger.isLoggable(Level.SEVERE)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.log(SELF, Level.SEVERE, var3.getMessage(), var3.getThrowable());
      }

   }

   public void error(String var1, Throwable var2) {
      if (this.logger.isLoggable(Level.SEVERE)) {
         this.log(SELF, Level.SEVERE, var1, var2);
      }

   }

   private void log(String var1, Level var2, String var3, Throwable var4) {
      LogRecord var5 = new LogRecord(var2, var3);
      var5.setLoggerName(this.name());
      var5.setThrown(var4);
      fillCallerData(var1, var5);
      this.logger.log(var5);
   }

   private static void fillCallerData(String var0, LogRecord var1) {
      StackTraceElement[] var2 = (new Throwable()).getStackTrace();
      int var3 = -1;

      int var4;
      for(var4 = 0; var4 < var2.length; ++var4) {
         String var5 = var2[var4].getClassName();
         if (var5.equals(var0) || var5.equals(SUPER)) {
            var3 = var4;
            break;
         }
      }

      var4 = -1;

      for(int var7 = var3 + 1; var7 < var2.length; ++var7) {
         String var6 = var2[var7].getClassName();
         if (!var6.equals(var0) && !var6.equals(SUPER)) {
            var4 = var7;
            break;
         }
      }

      if (var4 != -1) {
         StackTraceElement var8 = var2[var4];
         var1.setSourceClassName(var8.getClassName());
         var1.setSourceMethodName(var8.getMethodName());
      }

   }
}
