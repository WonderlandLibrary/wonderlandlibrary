package io.netty.util.internal.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class Log4JLogger extends AbstractInternalLogger {
   private static final long serialVersionUID = 2851357342488183058L;
   final transient Logger logger;
   static final String FQCN = Log4JLogger.class.getName();
   final boolean traceCapable;

   Log4JLogger(Logger var1) {
      super(var1.getName());
      this.logger = var1;
      this.traceCapable = this.isTraceCapable();
   }

   private boolean isTraceCapable() {
      try {
         this.logger.isTraceEnabled();
         return true;
      } catch (NoSuchMethodError var2) {
         return false;
      }
   }

   public void trace(String var1) {
      this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, var1, (Throwable)null);
   }

   public void trace(String var1, Object var2) {
      if (this != false) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, var3.getMessage(), var3.getThrowable());
      }

   }

   public void trace(String var1, Object var2, Object var3) {
      if (this != false) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, var4.getMessage(), var4.getThrowable());
      }

   }

   public void trace(String var1, Object... var2) {
      if (this != false) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, var3.getMessage(), var3.getThrowable());
      }

   }

   public void trace(String var1, Throwable var2) {
      this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, var1, var2);
   }

   public boolean isDebugEnabled() {
      return this.logger.isDebugEnabled();
   }

   public void debug(String var1) {
      this.logger.log(FQCN, Level.DEBUG, var1, (Throwable)null);
   }

   public void debug(String var1, Object var2) {
      if (this.logger.isDebugEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.log(FQCN, Level.DEBUG, var3.getMessage(), var3.getThrowable());
      }

   }

   public void debug(String var1, Object var2, Object var3) {
      if (this.logger.isDebugEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.log(FQCN, Level.DEBUG, var4.getMessage(), var4.getThrowable());
      }

   }

   public void debug(String var1, Object... var2) {
      if (this.logger.isDebugEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.log(FQCN, Level.DEBUG, var3.getMessage(), var3.getThrowable());
      }

   }

   public void debug(String var1, Throwable var2) {
      this.logger.log(FQCN, Level.DEBUG, var1, var2);
   }

   public boolean isInfoEnabled() {
      return this.logger.isInfoEnabled();
   }

   public void info(String var1) {
      this.logger.log(FQCN, Level.INFO, var1, (Throwable)null);
   }

   public void info(String var1, Object var2) {
      if (this.logger.isInfoEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.log(FQCN, Level.INFO, var3.getMessage(), var3.getThrowable());
      }

   }

   public void info(String var1, Object var2, Object var3) {
      if (this.logger.isInfoEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.log(FQCN, Level.INFO, var4.getMessage(), var4.getThrowable());
      }

   }

   public void info(String var1, Object... var2) {
      if (this.logger.isInfoEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.log(FQCN, Level.INFO, var3.getMessage(), var3.getThrowable());
      }

   }

   public void info(String var1, Throwable var2) {
      this.logger.log(FQCN, Level.INFO, var1, var2);
   }

   public boolean isWarnEnabled() {
      return this.logger.isEnabledFor(Level.WARN);
   }

   public void warn(String var1) {
      this.logger.log(FQCN, Level.WARN, var1, (Throwable)null);
   }

   public void warn(String var1, Object var2) {
      if (this.logger.isEnabledFor(Level.WARN)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.log(FQCN, Level.WARN, var3.getMessage(), var3.getThrowable());
      }

   }

   public void warn(String var1, Object var2, Object var3) {
      if (this.logger.isEnabledFor(Level.WARN)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.log(FQCN, Level.WARN, var4.getMessage(), var4.getThrowable());
      }

   }

   public void warn(String var1, Object... var2) {
      if (this.logger.isEnabledFor(Level.WARN)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.log(FQCN, Level.WARN, var3.getMessage(), var3.getThrowable());
      }

   }

   public void warn(String var1, Throwable var2) {
      this.logger.log(FQCN, Level.WARN, var1, var2);
   }

   public boolean isErrorEnabled() {
      return this.logger.isEnabledFor(Level.ERROR);
   }

   public void error(String var1) {
      this.logger.log(FQCN, Level.ERROR, var1, (Throwable)null);
   }

   public void error(String var1, Object var2) {
      if (this.logger.isEnabledFor(Level.ERROR)) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.log(FQCN, Level.ERROR, var3.getMessage(), var3.getThrowable());
      }

   }

   public void error(String var1, Object var2, Object var3) {
      if (this.logger.isEnabledFor(Level.ERROR)) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.log(FQCN, Level.ERROR, var4.getMessage(), var4.getThrowable());
      }

   }

   public void error(String var1, Object... var2) {
      if (this.logger.isEnabledFor(Level.ERROR)) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.log(FQCN, Level.ERROR, var3.getMessage(), var3.getThrowable());
      }

   }

   public void error(String var1, Throwable var2) {
      this.logger.log(FQCN, Level.ERROR, var1, var2);
   }
}
