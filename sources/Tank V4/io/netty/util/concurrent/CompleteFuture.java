package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public abstract class CompleteFuture extends AbstractFuture {
   private final EventExecutor executor;

   protected CompleteFuture(EventExecutor var1) {
      this.executor = var1;
   }

   protected EventExecutor executor() {
      return this.executor;
   }

   public Future addListener(GenericFutureListener var1) {
      if (var1 == null) {
         throw new NullPointerException("listener");
      } else {
         DefaultPromise.notifyListener(this.executor(), this, var1);
         return this;
      }
   }

   public Future addListeners(GenericFutureListener... var1) {
      if (var1 == null) {
         throw new NullPointerException("listeners");
      } else {
         GenericFutureListener[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GenericFutureListener var5 = var2[var4];
            if (var5 == null) {
               break;
            }

            DefaultPromise.notifyListener(this.executor(), this, var5);
         }

         return this;
      }
   }

   public Future removeListener(GenericFutureListener var1) {
      return this;
   }

   public Future removeListeners(GenericFutureListener... var1) {
      return this;
   }

   public Future await() throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         return this;
      }
   }

   public boolean await(long var1, TimeUnit var3) throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         return true;
      }
   }

   public Future sync() throws InterruptedException {
      return this;
   }

   public Future syncUninterruptibly() {
      return this;
   }

   public boolean await(long var1) throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         return true;
      }
   }

   public Future awaitUninterruptibly() {
      return this;
   }

   public boolean awaitUninterruptibly(long var1, TimeUnit var3) {
      return true;
   }

   public boolean awaitUninterruptibly(long var1) {
      return true;
   }

   public boolean isDone() {
      return true;
   }

   public boolean isCancellable() {
      return false;
   }

   public boolean isCancelled() {
      return false;
   }

   public boolean cancel(boolean var1) {
      return false;
   }
}
