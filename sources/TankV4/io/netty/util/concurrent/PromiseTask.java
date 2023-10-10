package io.netty.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

class PromiseTask extends DefaultPromise implements RunnableFuture {
   protected final Callable task;

   static Callable toCallable(Runnable var0, Object var1) {
      return new PromiseTask.RunnableAdapter(var0, var1);
   }

   PromiseTask(EventExecutor var1, Runnable var2, Object var3) {
      this(var1, toCallable(var2, var3));
   }

   PromiseTask(EventExecutor var1, Callable var2) {
      super(var1);
      this.task = var2;
   }

   public final int hashCode() {
      return System.identityHashCode(this);
   }

   public final boolean equals(Object var1) {
      return this == var1;
   }

   public void run() {
      try {
         if (this.setUncancellableInternal()) {
            Object var1 = this.task.call();
            this.setSuccessInternal(var1);
         }
      } catch (Throwable var2) {
         this.setFailureInternal(var2);
      }

   }

   public final Promise setFailure(Throwable var1) {
      throw new IllegalStateException();
   }

   protected final Promise setFailureInternal(Throwable var1) {
      super.setFailure(var1);
      return this;
   }

   public final boolean tryFailure(Throwable var1) {
      return false;
   }

   protected final boolean tryFailureInternal(Throwable var1) {
      return super.tryFailure(var1);
   }

   public final Promise setSuccess(Object var1) {
      throw new IllegalStateException();
   }

   protected final Promise setSuccessInternal(Object var1) {
      super.setSuccess(var1);
      return this;
   }

   public final boolean trySuccess(Object var1) {
      return false;
   }

   protected final boolean trySuccessInternal(Object var1) {
      return super.trySuccess(var1);
   }

   public final boolean setUncancellable() {
      throw new IllegalStateException();
   }

   protected final boolean setUncancellableInternal() {
      return super.setUncancellable();
   }

   protected StringBuilder toStringBuilder() {
      StringBuilder var1 = super.toStringBuilder();
      var1.setCharAt(var1.length() - 1, ',');
      var1.append(" task: ");
      var1.append(this.task);
      var1.append(')');
      return var1;
   }

   private static final class RunnableAdapter implements Callable {
      final Runnable task;
      final Object result;

      RunnableAdapter(Runnable var1, Object var2) {
         this.task = var1;
         this.result = var2;
      }

      public Object call() {
         this.task.run();
         return this.result;
      }

      public String toString() {
         return "Callable(task: " + this.task + ", result: " + this.result + ')';
      }
   }
}
