package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger implements Log, Serializable {
   private static final long serialVersionUID = 5160705895411730424L;
   private static final String FQCN;
   private transient volatile Logger logger = null;
   private final String name;
   private static final Priority traceLevel;
   static Class class$org$apache$commons$logging$impl$Log4JLogger;
   static Class class$org$apache$log4j$Level;
   static Class class$org$apache$log4j$Priority;

   public Log4JLogger() {
      this.name = null;
   }

   public Log4JLogger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   public Log4JLogger(Logger var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
      } else {
         this.name = var1.getName();
         this.logger = var1;
      }
   }

   public void trace(Object var1) {
      this.getLogger().log(FQCN, traceLevel, var1, (Throwable)null);
   }

   public void trace(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, traceLevel, var1, var2);
   }

   public void debug(Object var1) {
      this.getLogger().log(FQCN, Level.DEBUG, var1, (Throwable)null);
   }

   public void debug(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.DEBUG, var1, var2);
   }

   public void info(Object var1) {
      this.getLogger().log(FQCN, Level.INFO, var1, (Throwable)null);
   }

   public void info(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.INFO, var1, var2);
   }

   public void warn(Object var1) {
      this.getLogger().log(FQCN, Level.WARN, var1, (Throwable)null);
   }

   public void warn(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.WARN, var1, var2);
   }

   public void error(Object var1) {
      this.getLogger().log(FQCN, Level.ERROR, var1, (Throwable)null);
   }

   public void error(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.ERROR, var1, var2);
   }

   public void fatal(Object var1) {
      this.getLogger().log(FQCN, Level.FATAL, var1, (Throwable)null);
   }

   public void fatal(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.FATAL, var1, var2);
   }

   public Logger getLogger() {
      Logger var1 = this.logger;
      if (var1 == null) {
         synchronized(this){}
         var1 = this.logger;
         if (var1 == null) {
            this.logger = var1 = Logger.getLogger(this.name);
         }
      }

      return var1;
   }

   public boolean isDebugEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isEnabledFor(Level.ERROR);
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isEnabledFor(Level.FATAL);
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isInfoEnabled();
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isEnabledFor(traceLevel);
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isEnabledFor(Level.WARN);
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      FQCN = (class$org$apache$commons$logging$impl$Log4JLogger == null ? (class$org$apache$commons$logging$impl$Log4JLogger = class$("org.apache.commons.logging.impl.Log4JLogger")) : class$org$apache$commons$logging$impl$Log4JLogger).getName();
      if (!(class$org$apache$log4j$Priority == null ? (class$org$apache$log4j$Priority = class$("org.apache.log4j.Priority")) : class$org$apache$log4j$Priority).isAssignableFrom(class$org$apache$log4j$Level == null ? (class$org$apache$log4j$Level = class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level)) {
         throw new InstantiationError("Log4J 1.2 not available");
      } else {
         Object var0;
         try {
            var0 = (Priority)(class$org$apache$log4j$Level == null ? (class$org$apache$log4j$Level = class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level).getDeclaredField("TRACE").get((Object)null);
         } catch (Exception var2) {
            var0 = Level.DEBUG;
         }

         traceLevel = (Priority)var0;
      }
   }
}
