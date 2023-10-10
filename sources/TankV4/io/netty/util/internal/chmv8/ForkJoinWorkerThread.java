package io.netty.util.internal.chmv8;

public class ForkJoinWorkerThread extends Thread {
   final ForkJoinPool pool;
   final ForkJoinPool.WorkQueue workQueue;

   protected ForkJoinWorkerThread(ForkJoinPool var1) {
      super("aForkJoinWorkerThread");
      this.pool = var1;
      this.workQueue = var1.registerWorker(this);
   }

   public ForkJoinPool getPool() {
      return this.pool;
   }

   public int getPoolIndex() {
      return this.workQueue.poolIndex >>> 1;
   }

   protected void onStart() {
   }

   protected void onTermination(Throwable var1) {
   }

   public void run() {
      Throwable var1 = null;

      try {
         this.onStart();
         this.pool.runWorker(this.workQueue);
      } catch (Throwable var10) {
         var1 = var10;

         try {
            this.onTermination(var1);
         } catch (Throwable var8) {
            if (var10 == null) {
               var1 = var8;
            }

            this.pool.deregisterWorker(this, var1);
            return;
         }

         this.pool.deregisterWorker(this, var10);
         return;
      }

      try {
         this.onTermination(var1);
      } catch (Throwable var9) {
         if (var1 == null) {
            var1 = var9;
         }

         this.pool.deregisterWorker(this, var1);
         return;
      }

      this.pool.deregisterWorker(this, var1);
   }
}
