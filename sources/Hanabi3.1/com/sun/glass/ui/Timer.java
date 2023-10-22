package com.sun.glass.ui;

public abstract class Timer {
   private static final double UNSET_PERIOD = -1.0;
   private static final double SET_PERIOD = -2.0;
   private final Runnable runnable;
   private long ptr;
   private double period = -1.0;

   protected abstract long _start(Runnable var1);

   protected abstract long _start(Runnable var1, int var2);

   protected abstract void _stop(long var1);

   protected abstract void _pause(long var1);

   protected abstract void _resume(long var1);

   protected Timer(Runnable var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("runnable shouldn't be null");
      } else {
         this.runnable = var1;
      }
   }

   public static int getMinPeriod() {
      return Application.GetApplication().staticTimer_getMinPeriod();
   }

   public static int getMaxPeriod() {
      return Application.GetApplication().staticTimer_getMaxPeriod();
   }

   public synchronized void start(int var1) {
      if (var1 >= getMinPeriod() && var1 <= getMaxPeriod()) {
         if (this.ptr != 0L) {
            this.stop();
         }

         this.ptr = this._start(this.runnable, var1);
         if (this.ptr == 0L) {
            this.period = -1.0;
            throw new RuntimeException("Failed to start the timer");
         } else {
            this.period = (double)var1;
         }
      } else {
         throw new IllegalArgumentException("period is out of range");
      }
   }

   public synchronized void start() {
      if (this.ptr != 0L) {
         this.stop();
      }

      this.ptr = this._start(this.runnable);
      if (this.ptr == 0L) {
         this.period = -1.0;
         throw new RuntimeException("Failed to start the timer");
      } else {
         this.period = -2.0;
      }
   }

   public synchronized void stop() {
      if (this.ptr != 0L) {
         this._stop(this.ptr);
         this.ptr = 0L;
         this.period = -1.0;
      }

   }

   public synchronized void pause() {
      if (this.ptr != 0L) {
         this._pause(this.ptr);
      }

   }

   public synchronized void resume() {
      if (this.ptr != 0L) {
         this._resume(this.ptr);
      }

   }

   public synchronized boolean isRunning() {
      return this.period != -1.0;
   }
}
