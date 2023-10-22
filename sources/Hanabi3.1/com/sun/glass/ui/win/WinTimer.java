package com.sun.glass.ui.win;

import com.sun.glass.ui.Timer;

final class WinTimer extends Timer {
   private static final int minPeriod = _getMinPeriod();
   private static final int maxPeriod = _getMaxPeriod();

   protected WinTimer(Runnable var1) {
      super(var1);
   }

   private static native int _getMinPeriod();

   private static native int _getMaxPeriod();

   static int getMinPeriod_impl() {
      return minPeriod;
   }

   static int getMaxPeriod_impl() {
      return maxPeriod;
   }

   protected long _start(Runnable var1) {
      throw new RuntimeException("vsync timer not supported");
   }

   protected native long _start(Runnable var1, int var2);

   protected native void _stop(long var1);

   protected void _pause(long var1) {
   }

   protected void _resume(long var1) {
   }
}
