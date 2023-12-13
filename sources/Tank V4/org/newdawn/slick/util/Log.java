package org.newdawn.slick.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class Log {
   private static boolean verbose = true;
   private static boolean forcedVerbose = false;
   private static final String forceVerboseProperty = "org.newdawn.slick.forceVerboseLog";
   private static final String forceVerbosePropertyOnValue = "true";
   private static LogSystem logSystem = new DefaultLogSystem();

   private Log() {
   }

   public static void setLogSystem(LogSystem var0) {
      logSystem = var0;
   }

   public static void setVerbose(boolean var0) {
      if (!forcedVerbose) {
         verbose = var0;
      }
   }

   public static void checkVerboseLogSetting() {
      try {
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               String var1 = System.getProperty("org.newdawn.slick.forceVerboseLog");
               if (var1 != null && var1.equalsIgnoreCase("true")) {
                  Log.setForcedVerboseOn();
               }

               return null;
            }
         });
      } catch (Throwable var1) {
      }

   }

   public static void setForcedVerboseOn() {
      forcedVerbose = true;
      verbose = true;
   }

   public static void error(String var0, Throwable var1) {
      logSystem.error(var0, var1);
   }

   public static void error(Throwable var0) {
      logSystem.error(var0);
   }

   public static void error(String var0) {
      logSystem.error(var0);
   }

   public static void warn(String var0) {
      logSystem.warn(var0);
   }

   public static void warn(String var0, Throwable var1) {
      logSystem.warn(var0, var1);
   }

   public static void info(String var0) {
      if (verbose || forcedVerbose) {
         logSystem.info(var0);
      }

   }

   public static void debug(String var0) {
      if (verbose || forcedVerbose) {
         logSystem.debug(var0);
      }

   }
}
