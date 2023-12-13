package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BackgroundInitializer implements ConcurrentInitializer {
   private ExecutorService externalExecutor;
   private ExecutorService executor;
   private Future future;

   protected BackgroundInitializer() {
      this((ExecutorService)null);
   }

   protected BackgroundInitializer(ExecutorService var1) {
      this.setExternalExecutor(var1);
   }

   public final synchronized ExecutorService getExternalExecutor() {
      return this.externalExecutor;
   }

   public final synchronized void setExternalExecutor(ExecutorService var1) {
      if (this != null) {
         throw new IllegalStateException("Cannot set ExecutorService after start()!");
      } else {
         this.externalExecutor = var1;
      }
   }

   public synchronized boolean start() {
      if (this != null) {
         this.executor = this.getExternalExecutor();
         ExecutorService var1;
         if (this.executor == null) {
            this.executor = var1 = this.createExecutor();
         } else {
            var1 = null;
         }

         this.future = this.executor.submit(this.createTask(var1));
         return true;
      } else {
         return false;
      }
   }

   public Object get() throws ConcurrentException {
      try {
         return this.getFuture().get();
      } catch (ExecutionException var2) {
         ConcurrentUtils.handleCause(var2);
         return null;
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw new ConcurrentException(var3);
      }
   }

   public synchronized Future getFuture() {
      if (this.future == null) {
         throw new IllegalStateException("start() must be called first!");
      } else {
         return this.future;
      }
   }

   protected final synchronized ExecutorService getActiveExecutor() {
      return this.executor;
   }

   protected int getTaskCount() {
      return 1;
   }

   protected abstract Object initialize() throws Exception;

   private Callable createTask(ExecutorService var1) {
      return new BackgroundInitializer.InitializationTask(this, var1);
   }

   private ExecutorService createExecutor() {
      return Executors.newFixedThreadPool(this.getTaskCount());
   }

   private class InitializationTask implements Callable {
      private final ExecutorService execFinally;
      final BackgroundInitializer this$0;

      public InitializationTask(BackgroundInitializer var1, ExecutorService var2) {
         this.this$0 = var1;
         this.execFinally = var2;
      }

      public Object call() throws Exception {
         Object var1 = this.this$0.initialize();
         if (this.execFinally != null) {
            this.execFinally.shutdown();
         }

         return var1;
      }
   }
}
