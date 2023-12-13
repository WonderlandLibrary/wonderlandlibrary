package org.apache.commons.io;

class ThreadMonitor implements Runnable {
   private final Thread thread;
   private final long timeout;

   public static Thread start(long var0) {
      return start(Thread.currentThread(), var0);
   }

   public static Thread start(Thread var0, long var1) {
      Thread var3 = null;
      if (var1 > 0L) {
         ThreadMonitor var4 = new ThreadMonitor(var0, var1);
         var3 = new Thread(var4, ThreadMonitor.class.getSimpleName());
         var3.setDaemon(true);
         var3.start();
      }

      return var3;
   }

   public static void stop(Thread var0) {
      if (var0 != null) {
         var0.interrupt();
      }

   }

   private ThreadMonitor(Thread var1, long var2) {
      this.thread = var1;
      this.timeout = var2;
   }

   public void run() {
      try {
         Thread.sleep(this.timeout);
         this.thread.interrupt();
      } catch (InterruptedException var2) {
      }

   }
}
