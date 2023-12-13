package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

public class LogKitLogger implements Log, Serializable {
   private static final long serialVersionUID = 3768538055836059519L;
   protected transient volatile Logger logger = null;
   protected String name = null;

   public LogKitLogger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   public Logger getLogger() {
      Logger var1 = this.logger;
      if (var1 == null) {
         synchronized(this){}
         var1 = this.logger;
         if (var1 == null) {
            this.logger = var1 = Hierarchy.getDefaultHierarchy().getLoggerFor(this.name);
         }
      }

      return var1;
   }

   public void trace(Object var1) {
      this.debug(var1);
   }

   public void trace(Object var1, Throwable var2) {
      this.debug(var1, var2);
   }

   public void debug(Object var1) {
      if (var1 != null) {
         this.getLogger().debug(String.valueOf(var1));
      }

   }

   public void debug(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().debug(String.valueOf(var1), var2);
      }

   }

   public void info(Object var1) {
      if (var1 != null) {
         this.getLogger().info(String.valueOf(var1));
      }

   }

   public void info(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().info(String.valueOf(var1), var2);
      }

   }

   public void warn(Object var1) {
      if (var1 != null) {
         this.getLogger().warn(String.valueOf(var1));
      }

   }

   public void warn(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().warn(String.valueOf(var1), var2);
      }

   }

   public void error(Object var1) {
      if (var1 != null) {
         this.getLogger().error(String.valueOf(var1));
      }

   }

   public void error(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().error(String.valueOf(var1), var2);
      }

   }

   public void fatal(Object var1) {
      if (var1 != null) {
         this.getLogger().fatalError(String.valueOf(var1));
      }

   }

   public void fatal(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().fatalError(String.valueOf(var1), var2);
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
}
