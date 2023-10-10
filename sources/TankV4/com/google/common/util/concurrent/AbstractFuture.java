package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import javax.annotation.Nullable;

public abstract class AbstractFuture implements ListenableFuture {
   private final AbstractFuture.Sync sync = new AbstractFuture.Sync();
   private final ExecutionList executionList = new ExecutionList();

   protected AbstractFuture() {
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, TimeoutException, ExecutionException {
      return this.sync.get(var3.toNanos(var1));
   }

   public Object get() throws InterruptedException, ExecutionException {
      return this.sync.get();
   }

   public boolean isDone() {
      return this.sync.isDone();
   }

   public boolean isCancelled() {
      return this.sync.isCancelled();
   }

   public boolean cancel(boolean var1) {
      if (!this.sync.cancel(var1)) {
         return false;
      } else {
         this.executionList.execute();
         if (var1) {
            this.interruptTask();
         }

         return true;
      }
   }

   protected void interruptTask() {
   }

   protected final boolean wasInterrupted() {
      return this.sync.wasInterrupted();
   }

   public void addListener(Runnable var1, Executor var2) {
      this.executionList.add(var1, var2);
   }

   protected boolean set(@Nullable Object var1) {
      boolean var2 = this.sync.set(var1);
      if (var2) {
         this.executionList.execute();
      }

      return var2;
   }

   protected boolean setException(Throwable var1) {
      boolean var2 = this.sync.setException((Throwable)Preconditions.checkNotNull(var1));
      if (var2) {
         this.executionList.execute();
      }

      return var2;
   }

   static final CancellationException cancellationExceptionWithCause(@Nullable String var0, @Nullable Throwable var1) {
      CancellationException var2 = new CancellationException(var0);
      var2.initCause(var1);
      return var2;
   }

   static final class Sync extends AbstractQueuedSynchronizer {
      private static final long serialVersionUID = 0L;
      static final int RUNNING = 0;
      static final int COMPLETING = 1;
      static final int COMPLETED = 2;
      static final int CANCELLED = 4;
      static final int INTERRUPTED = 8;
      private Object value;
      private Throwable exception;

      protected int tryAcquireShared(int var1) {
         return this != false ? 1 : -1;
      }

      protected boolean tryReleaseShared(int var1) {
         this.setState(var1);
         return true;
      }

      Object get(long var1) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
         if (!this.tryAcquireSharedNanos(-1, var1)) {
            throw new TimeoutException("Timeout waiting for task.");
         } else {
            return this.getValue();
         }
      }

      Object get() throws CancellationException, ExecutionException, InterruptedException {
         this.acquireSharedInterruptibly(-1);
         return this.getValue();
      }

      private Object getValue() throws CancellationException, ExecutionException {
         int var1 = this.getState();
         switch(var1) {
         case 2:
            if (this.exception != null) {
               throw new ExecutionException(this.exception);
            }

            return this.value;
         case 4:
         case 8:
            throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
         default:
            throw new IllegalStateException("Error, synchronizer in invalid state: " + var1);
         }
      }

      boolean isCancelled() {
         return (this.getState() & 12) != 0;
      }

      boolean wasInterrupted() {
         return this.getState() == 8;
      }

      boolean set(@Nullable Object var1) {
         return this.complete(var1, (Throwable)null, 2);
      }

      boolean setException(Throwable var1) {
         return this.complete((Object)null, var1, 2);
      }

      boolean cancel(boolean var1) {
         return this.complete((Object)null, (Throwable)null, var1 ? 8 : 4);
      }

      private boolean complete(@Nullable Object var1, @Nullable Throwable var2, int var3) {
         boolean var4 = this.compareAndSetState(0, 1);
         if (var4) {
            this.value = var1;
            this.exception = (Throwable)((var3 & 12) != 0 ? new CancellationException("Future.cancel() was called.") : var2);
            this.releaseShared(var3);
         } else if (this.getState() == 1) {
            this.acquireShared(-1);
         }

         return var4;
      }
   }
}
