package io.netty.util.internal.logging;

import org.apache.commons.logging.Log;

class CommonsLogger extends AbstractInternalLogger {
   private static final long serialVersionUID = 8647838678388394885L;
   private final transient Log logger;

   CommonsLogger(Log var1, String var2) {
      super(var2);
      if (var1 == null) {
         throw new NullPointerException("logger");
      } else {
         this.logger = var1;
      }
   }

   public boolean isTraceEnabled() {
      return this.logger.isTraceEnabled();
   }

   public void trace(String var1) {
      this.logger.trace(var1);
   }

   public void trace(String var1, Object var2) {
      if (this.logger.isTraceEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.trace(var3.getMessage(), var3.getThrowable());
      }

   }

   public void trace(String var1, Object var2, Object var3) {
      if (this.logger.isTraceEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.trace(var4.getMessage(), var4.getThrowable());
      }

   }

   public void trace(String var1, Object... var2) {
      if (this.logger.isTraceEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.trace(var3.getMessage(), var3.getThrowable());
      }

   }

   public void trace(String var1, Throwable var2) {
      this.logger.trace(var1, var2);
   }

   public boolean isDebugEnabled() {
      return this.logger.isDebugEnabled();
   }

   public void debug(String var1) {
      this.logger.debug(var1);
   }

   public void debug(String var1, Object var2) {
      if (this.logger.isDebugEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.debug(var3.getMessage(), var3.getThrowable());
      }

   }

   public void debug(String var1, Object var2, Object var3) {
      if (this.logger.isDebugEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.debug(var4.getMessage(), var4.getThrowable());
      }

   }

   public void debug(String var1, Object... var2) {
      if (this.logger.isDebugEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.debug(var3.getMessage(), var3.getThrowable());
      }

   }

   public void debug(String var1, Throwable var2) {
      this.logger.debug(var1, var2);
   }

   public boolean isInfoEnabled() {
      return this.logger.isInfoEnabled();
   }

   public void info(String var1) {
      this.logger.info(var1);
   }

   public void info(String var1, Object var2) {
      if (this.logger.isInfoEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.info(var3.getMessage(), var3.getThrowable());
      }

   }

   public void info(String var1, Object var2, Object var3) {
      if (this.logger.isInfoEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.info(var4.getMessage(), var4.getThrowable());
      }

   }

   public void info(String var1, Object... var2) {
      if (this.logger.isInfoEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.info(var3.getMessage(), var3.getThrowable());
      }

   }

   public void info(String var1, Throwable var2) {
      this.logger.info(var1, var2);
   }

   public boolean isWarnEnabled() {
      return this.logger.isWarnEnabled();
   }

   public void warn(String var1) {
      this.logger.warn(var1);
   }

   public void warn(String var1, Object var2) {
      if (this.logger.isWarnEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.warn(var3.getMessage(), var3.getThrowable());
      }

   }

   public void warn(String var1, Object var2, Object var3) {
      if (this.logger.isWarnEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.warn(var4.getMessage(), var4.getThrowable());
      }

   }

   public void warn(String var1, Object... var2) {
      if (this.logger.isWarnEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.warn(var3.getMessage(), var3.getThrowable());
      }

   }

   public void warn(String var1, Throwable var2) {
      this.logger.warn(var1, var2);
   }

   public boolean isErrorEnabled() {
      return this.logger.isErrorEnabled();
   }

   public void error(String var1) {
      this.logger.error(var1);
   }

   public void error(String var1, Object var2) {
      if (this.logger.isErrorEnabled()) {
         FormattingTuple var3 = MessageFormatter.format(var1, var2);
         this.logger.error(var3.getMessage(), var3.getThrowable());
      }

   }

   public void error(String var1, Object var2, Object var3) {
      if (this.logger.isErrorEnabled()) {
         FormattingTuple var4 = MessageFormatter.format(var1, var2, var3);
         this.logger.error(var4.getMessage(), var4.getThrowable());
      }

   }

   public void error(String var1, Object... var2) {
      if (this.logger.isErrorEnabled()) {
         FormattingTuple var3 = MessageFormatter.arrayFormat(var1, var2);
         this.logger.error(var3.getMessage(), var3.getThrowable());
      }

   }

   public void error(String var1, Throwable var2) {
      this.logger.error(var1, var2);
   }
}
