package org.apache.commons.logging.impl;

import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

public class AvalonLogger implements Log {
   private static volatile Logger defaultLogger = null;
   private final transient Logger logger;

   public AvalonLogger(Logger var1) {
      this.logger = var1;
   }

   public AvalonLogger(String var1) {
      if (defaultLogger == null) {
         throw new NullPointerException("default logger has to be specified if this constructor is used!");
      } else {
         this.logger = defaultLogger.getChildLogger(var1);
      }
   }

   public Logger getLogger() {
      return this.logger;
   }

   public static void setDefaultLogger(Logger var0) {
      defaultLogger = var0;
   }

   public void debug(Object var1, Throwable var2) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(var1), var2);
      }

   }

   public void debug(Object var1) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(var1));
      }

   }

   public void error(Object var1, Throwable var2) {
      if (this.getLogger().isErrorEnabled()) {
         this.getLogger().error(String.valueOf(var1), var2);
      }

   }

   public void error(Object var1) {
      if (this.getLogger().isErrorEnabled()) {
         this.getLogger().error(String.valueOf(var1));
      }

   }

   public void fatal(Object var1, Throwable var2) {
      if (this.getLogger().isFatalErrorEnabled()) {
         this.getLogger().fatalError(String.valueOf(var1), var2);
      }

   }

   public void fatal(Object var1) {
      if (this.getLogger().isFatalErrorEnabled()) {
         this.getLogger().fatalError(String.valueOf(var1));
      }

   }

   public void info(Object var1, Throwable var2) {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info(String.valueOf(var1), var2);
      }

   }

   public void info(Object var1) {
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info(String.valueOf(var1));
      }

   }

   public boolean isDebugEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isErrorEnabled();
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isFatalErrorEnabled();
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isInfoEnabled();
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isWarnEnabled();
   }

   public void trace(Object var1, Throwable var2) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(var1), var2);
      }

   }

   public void trace(Object var1) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(String.valueOf(var1));
      }

   }

   public void warn(Object var1, Throwable var2) {
      if (this.getLogger().isWarnEnabled()) {
         this.getLogger().warn(String.valueOf(var1), var2);
      }

   }

   public void warn(Object var1) {
      if (this.getLogger().isWarnEnabled()) {
         this.getLogger().warn(String.valueOf(var1));
      }

   }
}
