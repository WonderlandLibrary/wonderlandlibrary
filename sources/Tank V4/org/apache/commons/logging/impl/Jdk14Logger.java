package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk14Logger implements Log, Serializable {
   private static final long serialVersionUID = 4784713551416303804L;
   protected static final Level dummyLevel;
   protected transient Logger logger = null;
   protected String name = null;

   public Jdk14Logger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   protected void log(Level var1, String var2, Throwable var3) {
      Logger var4 = this.getLogger();
      if (var4.isLoggable(var1)) {
         Throwable var5 = new Throwable();
         StackTraceElement[] var6 = var5.getStackTrace();
         String var7 = this.name;
         String var8 = "unknown";
         if (var6 != null && var6.length > 2) {
            StackTraceElement var9 = var6[2];
            var8 = var9.getMethodName();
         }

         if (var3 == null) {
            var4.logp(var1, var7, var8, var2);
         } else {
            var4.logp(var1, var7, var8, var2, var3);
         }
      }

   }

   public void debug(Object var1) {
      this.log(Level.FINE, String.valueOf(var1), (Throwable)null);
   }

   public void debug(Object var1, Throwable var2) {
      this.log(Level.FINE, String.valueOf(var1), var2);
   }

   public void error(Object var1) {
      this.log(Level.SEVERE, String.valueOf(var1), (Throwable)null);
   }

   public void error(Object var1, Throwable var2) {
      this.log(Level.SEVERE, String.valueOf(var1), var2);
   }

   public void fatal(Object var1) {
      this.log(Level.SEVERE, String.valueOf(var1), (Throwable)null);
   }

   public void fatal(Object var1, Throwable var2) {
      this.log(Level.SEVERE, String.valueOf(var1), var2);
   }

   public Logger getLogger() {
      if (this.logger == null) {
         this.logger = Logger.getLogger(this.name);
      }

      return this.logger;
   }

   public void info(Object var1) {
      this.log(Level.INFO, String.valueOf(var1), (Throwable)null);
   }

   public void info(Object var1, Throwable var2) {
      this.log(Level.INFO, String.valueOf(var1), var2);
   }

   public boolean isDebugEnabled() {
      return this.getLogger().isLoggable(Level.FINE);
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isLoggable(Level.SEVERE);
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isLoggable(Level.SEVERE);
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isLoggable(Level.INFO);
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isLoggable(Level.FINEST);
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isLoggable(Level.WARNING);
   }

   public void trace(Object var1) {
      this.log(Level.FINEST, String.valueOf(var1), (Throwable)null);
   }

   public void trace(Object var1, Throwable var2) {
      this.log(Level.FINEST, String.valueOf(var1), var2);
   }

   public void warn(Object var1) {
      this.log(Level.WARNING, String.valueOf(var1), (Throwable)null);
   }

   public void warn(Object var1, Throwable var2) {
      this.log(Level.WARNING, String.valueOf(var1), var2);
   }

   static {
      dummyLevel = Level.FINE;
   }
}
