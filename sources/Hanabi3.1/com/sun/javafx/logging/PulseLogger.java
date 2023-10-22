package com.sun.javafx.logging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PulseLogger {
   public static final boolean PULSE_LOGGING_ENABLED;
   private static final Logger[] loggers;

   public static void pulseStart() {
      Logger[] var0 = loggers;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Logger var3 = var0[var2];
         var3.pulseStart();
      }

   }

   public static void pulseEnd() {
      Logger[] var0 = loggers;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Logger var3 = var0[var2];
         var3.pulseEnd();
      }

   }

   public static void renderStart() {
      Logger[] var0 = loggers;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Logger var3 = var0[var2];
         var3.renderStart();
      }

   }

   public static void renderEnd() {
      Logger[] var0 = loggers;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Logger var3 = var0[var2];
         var3.renderEnd();
      }

   }

   public static void addMessage(String var0) {
      Logger[] var1 = loggers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Logger var4 = var1[var3];
         var4.addMessage(var0);
      }

   }

   public static void incrementCounter(String var0) {
      Logger[] var1 = loggers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Logger var4 = var1[var3];
         var4.incrementCounter(var0);
      }

   }

   public static void newPhase(String var0) {
      Logger[] var1 = loggers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Logger var4 = var1[var3];
         var4.newPhase(var0);
      }

   }

   public static void newInput(String var0) {
      Logger[] var1 = loggers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Logger var4 = var1[var3];
         var4.newInput(var0);
      }

   }

   static {
      ArrayList var0 = new ArrayList();
      Logger var1 = PrintLogger.getInstance();
      if (var1 != null) {
         var0.add(var1);
      }

      try {
         Class var2 = Class.forName("com.sun.javafx.logging.JFRLogger");
         if (var2 != null) {
            Method var3 = var2.getDeclaredMethod("getInstance");
            var1 = (Logger)var3.invoke((Object)null);
            if (var1 != null) {
               var0.add(var1);
            }
         }
      } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoClassDefFoundError var4) {
      }

      loggers = (Logger[])var0.toArray(new Logger[var0.size()]);
      PULSE_LOGGING_ENABLED = loggers.length > 0;
   }
}
