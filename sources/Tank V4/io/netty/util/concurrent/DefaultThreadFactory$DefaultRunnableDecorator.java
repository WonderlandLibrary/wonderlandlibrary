package io.netty.util.concurrent;

final class DefaultThreadFactory$DefaultRunnableDecorator implements Runnable {
   private final Runnable r;

   DefaultThreadFactory$DefaultRunnableDecorator(Runnable var1) {
      this.r = var1;
   }

   public void run() {
      this.r.run();
      FastThreadLocal.removeAll();
   }
}
