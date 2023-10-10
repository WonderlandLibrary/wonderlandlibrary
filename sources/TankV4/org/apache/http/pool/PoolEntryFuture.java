package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;

@ThreadSafe
abstract class PoolEntryFuture implements Future {
   private final Lock lock;
   private final FutureCallback callback;
   private final Condition condition;
   private volatile boolean cancelled;
   private volatile boolean completed;
   private Object result;

   PoolEntryFuture(Lock var1, FutureCallback var2) {
      this.lock = var1;
      this.condition = var1.newCondition();
      this.callback = var2;
   }

   public boolean cancel(boolean var1) {
      this.lock.lock();
      boolean var2;
      if (this.completed) {
         var2 = false;
         this.lock.unlock();
         return var2;
      } else {
         this.completed = true;
         this.cancelled = true;
         if (this.callback != null) {
            this.callback.cancelled();
         }

         this.condition.signalAll();
         var2 = true;
         this.lock.unlock();
         return var2;
      }
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public boolean isDone() {
      return this.completed;
   }

   public Object get() throws InterruptedException, ExecutionException {
      try {
         return this.get(0L, TimeUnit.MILLISECONDS);
      } catch (TimeoutException var2) {
         throw new ExecutionException(var2);
      }
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      Args.notNull(var3, "Time unit");
      this.lock.lock();

      Object var4;
      label32: {
         try {
            if (this.completed) {
               var4 = this.result;
               break label32;
            }

            this.result = this.getPoolEntry(var1, var3);
            this.completed = true;
            if (this.callback != null) {
               this.callback.completed(this.result);
            }

            var4 = this.result;
         } catch (IOException var6) {
            this.completed = true;
            this.result = null;
            if (this.callback != null) {
               this.callback.failed(var6);
            }

            throw new ExecutionException(var6);
         }

         this.lock.unlock();
         return var4;
      }

      this.lock.unlock();
      return var4;
   }

   protected abstract Object getPoolEntry(long var1, TimeUnit var3) throws IOException, InterruptedException, TimeoutException;

   public boolean await(Date var1) throws InterruptedException {
      this.lock.lock();
      if (this.cancelled) {
         throw new InterruptedException("Operation interrupted");
      } else {
         boolean var2;
         if (var1 != null) {
            var2 = this.condition.awaitUntil(var1);
         } else {
            this.condition.await();
            var2 = true;
         }

         if (this.cancelled) {
            throw new InterruptedException("Operation interrupted");
         } else {
            this.lock.unlock();
            return var2;
         }
      }
   }

   public void wakeup() {
      this.lock.lock();
      this.condition.signalAll();
      this.lock.unlock();
   }
}
