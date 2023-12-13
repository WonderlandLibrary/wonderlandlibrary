package com.sun.webkit;

import java.lang.Thread.State;

final class SeparateThreadTimer extends Timer implements Runnable {
   private final Invoker invoker = Invoker.getInvoker();
   private final FireRunner fireRunner = new FireRunner();
   private final Thread thread = new Thread(this, "WebPane-Timer");

   SeparateThreadTimer() {
      this.thread.setDaemon(true);
   }

   synchronized void setFireTime(long var1) {
      super.setFireTime(var1);
      if (this.thread.getState() == State.NEW) {
         this.thread.start();
      }

      this.notifyAll();
   }

   public synchronized void run() {
      while(true) {
         try {
            if (this.fireTime > 0L) {
               long var1 = System.currentTimeMillis();

               while(true) {
                  if (this.fireTime <= var1) {
                     if (this.fireTime > 0L) {
                        this.invoker.invokeOnEventThread(this.fireRunner.forTime(this.fireTime));
                     }
                     break;
                  }

                  this.wait(this.fireTime - var1);
                  var1 = System.currentTimeMillis();
               }
            }

            this.wait();
         } catch (InterruptedException var3) {
            return;
         }
      }
   }

   public void notifyTick() {
      assert false;

   }

   private final class FireRunner implements Runnable {
      private volatile long time;

      private FireRunner() {
      }

      private Runnable forTime(long var1) {
         this.time = var1;
         return this;
      }

      public void run() {
         SeparateThreadTimer.this.fireTimerEvent(this.time);
      }

      // $FF: synthetic method
      FireRunner(Object var2) {
         this();
      }
   }
}
