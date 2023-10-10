package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk13LumberjackLogger implements Log, Serializable {
   private static final long serialVersionUID = -8649807923527610591L;
   protected transient Logger logger = null;
   protected String name = null;
   private String sourceClassName = "unknown";
   private String sourceMethodName = "unknown";
   private boolean classAndMethodFound = false;
   protected static final Level dummyLevel;

   public Jdk13LumberjackLogger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   private void log(Level var1, String var2, Throwable var3) {
      if (this.getLogger().isLoggable(var1)) {
         LogRecord var4 = new LogRecord(var1, var2);
         if (!this.classAndMethodFound) {
            this.getClassAndMethod();
         }

         var4.setSourceClassName(this.sourceClassName);
         var4.setSourceMethodName(this.sourceMethodName);
         if (var3 != null) {
            var4.setThrown(var3);
         }

         this.getLogger().log(var4);
      }

   }

   private void getClassAndMethod() {
      try {
         Throwable var1 = new Throwable();
         var1.fillInStackTrace();
         StringWriter var2 = new StringWriter();
         PrintWriter var3 = new PrintWriter(var2);
         var1.printStackTrace(var3);
         String var4 = var2.getBuffer().toString();
         StringTokenizer var5 = new StringTokenizer(var4, "\n");
         var5.nextToken();

         String var6;
         for(var6 = var5.nextToken(); var6.indexOf(this.getClass().getName()) == -1; var6 = var5.nextToken()) {
         }

         while(var6.indexOf(this.getClass().getName()) >= 0) {
            var6 = var5.nextToken();
         }

         int var7 = var6.indexOf("at ") + 3;
         int var8 = var6.indexOf(40);
         String var9 = var6.substring(var7, var8);
         int var10 = var9.lastIndexOf(46);
         this.sourceClassName = var9.substring(0, var10);
         this.sourceMethodName = var9.substring(var10 + 1);
      } catch (Exception var11) {
      }

      this.classAndMethodFound = true;
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
