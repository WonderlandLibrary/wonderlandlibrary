package com.sun.media.jfxmedia.logging;

public class Logger {
   public static final int OFF = Integer.MAX_VALUE;
   public static final int ERROR = 4;
   public static final int WARNING = 3;
   public static final int INFO = 2;
   public static final int DEBUG = 1;
   private static int currentLevel = Integer.MAX_VALUE;
   private static long startTime = 0L;
   private static final Object lock = new Object();

   private static void startLogger() {
      try {
         String var1 = System.getProperty("jfxmedia.loglevel", "off").toLowerCase();
         Integer var0;
         if (var1.equals("debug")) {
            var0 = 1;
         } else if (var1.equals("warning")) {
            var0 = 3;
         } else if (var1.equals("error")) {
            var0 = 4;
         } else if (var1.equals("info")) {
            var0 = 2;
         } else {
            var0 = Integer.MAX_VALUE;
         }

         setLevel(var0);
         startTime = System.currentTimeMillis();
      } catch (Exception var2) {
      }

      if (canLog(1)) {
         logMsg(1, "Logger initialized");
      }

   }

   private Logger() {
   }

   public static boolean initNative() {
      if (nativeInit()) {
         nativeSetNativeLevel(currentLevel);
         return true;
      } else {
         return false;
      }
   }

   private static native boolean nativeInit();

   public static void setLevel(int var0) {
      currentLevel = var0;

      try {
         nativeSetNativeLevel(var0);
      } catch (UnsatisfiedLinkError var2) {
      }

   }

   private static native void nativeSetNativeLevel(int var0);

   public static boolean canLog(int var0) {
      return var0 >= currentLevel;
   }

   public static void logMsg(int var0, String var1) {
      synchronized(lock) {
         if (var0 >= currentLevel) {
            if (var0 == 4) {
               System.err.println("Error (" + getTimestamp() + "): " + var1);
            } else if (var0 == 3) {
               System.err.println("Warning (" + getTimestamp() + "): " + var1);
            } else if (var0 == 2) {
               System.out.println("Info (" + getTimestamp() + "): " + var1);
            } else if (var0 == 1) {
               System.out.println("Debug (" + getTimestamp() + "): " + var1);
            }

         }
      }
   }

   public static void logMsg(int var0, String var1, String var2, String var3) {
      synchronized(lock) {
         if (var0 >= currentLevel) {
            logMsg(var0, var1 + ":" + var2 + "() " + var3);
         }
      }
   }

   private static String getTimestamp() {
      long var0 = System.currentTimeMillis() - startTime;
      long var2 = var0 / 3600000L;
      long var4 = (var0 - var2 * 60L * 60L * 1000L) / 60000L;
      long var6 = (var0 - var2 * 60L * 60L * 1000L - var4 * 60L * 1000L) / 1000L;
      long var8 = var0 - var2 * 60L * 60L * 1000L - var4 * 60L * 1000L - var6 * 1000L;
      return String.format("%d:%02d:%02d:%03d", var2, var4, var6, var8);
   }

   static {
      startLogger();
   }
}
