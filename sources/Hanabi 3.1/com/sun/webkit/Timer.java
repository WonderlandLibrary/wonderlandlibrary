package com.sun.webkit;

import java.security.AccessController;

public class Timer {
   private static Timer instance;
   private static Mode mode;
   long fireTime;

   Timer() {
   }

   public static synchronized Mode getMode() {
      if (mode == null) {
         mode = Boolean.valueOf((String)AccessController.doPrivileged(() -> {
            return System.getProperty("com.sun.webkit.platformticks", "true");
         })) ? Timer.Mode.PLATFORM_TICKS : Timer.Mode.SEPARATE_THREAD;
      }

      return mode;
   }

   public static synchronized Timer getTimer() {
      if (instance == null) {
         instance = (Timer)(getMode() == Timer.Mode.PLATFORM_TICKS ? new Timer() : new SeparateThreadTimer());
      }

      return instance;
   }

   public synchronized void notifyTick() {
      if (this.fireTime > 0L && this.fireTime <= System.currentTimeMillis()) {
         this.fireTimerEvent(this.fireTime);
      }

   }

   void fireTimerEvent(long var1) {
      boolean var3 = false;
      synchronized(this) {
         if (var1 == this.fireTime) {
            var3 = true;
            this.fireTime = 0L;
         }
      }

      if (var3) {
         WebPage.lockPage();

         try {
            twkFireTimerEvent();
         } finally {
            WebPage.unlockPage();
         }
      }

   }

   synchronized void setFireTime(long var1) {
      this.fireTime = var1;
   }

   private static void fwkSetFireTime(double var0) {
      getTimer().setFireTime((long)Math.ceil(var0 * 1000.0));
   }

   private static void fwkStopTimer() {
      getTimer().setFireTime(0L);
   }

   private static native void twkFireTimerEvent();

   public static enum Mode {
      PLATFORM_TICKS,
      SEPARATE_THREAD;
   }
}
