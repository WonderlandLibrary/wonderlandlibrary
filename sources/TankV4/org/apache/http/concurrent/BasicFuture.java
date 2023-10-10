package org.apache.http.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.util.Args;

public class BasicFuture implements Future, Cancellable {
   private final FutureCallback callback;
   private volatile boolean completed;
   private volatile boolean cancelled;
   private volatile Object result;
   private volatile Exception ex;

   public BasicFuture(FutureCallback var1) {
      this.callback = var1;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public boolean isDone() {
      return this.completed;
   }

   private Object getResult() throws ExecutionException {
      if (this.ex != null) {
         throw new ExecutionException(this.ex);
      } else {
         return this.result;
      }
   }

   public synchronized Object get() throws InterruptedException, ExecutionException {
      while(!this.completed) {
         this.wait();
      }

      return this.getResult();
   }

   public synchronized Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      Args.notNull(var3, "Time unit");
      long var4 = var3.toMillis(var1);
      long var6 = var4 <= 0L ? 0L : System.currentTimeMillis();
      long var8 = var4;
      if (this.completed) {
         return this.getResult();
      } else if (var4 <= 0L) {
         throw new TimeoutException();
      } else {
         do {
            this.wait(var8);
            if (this.completed) {
               return this.getResult();
            }

            var8 = var4 - (System.currentTimeMillis() - var6);
         } while(var8 > 0L);

         throw new TimeoutException();
      }
   }

   public boolean completed(Object var1) {
      synchronized(this){}
      if (this.completed) {
         return false;
      } else {
         this.completed = true;
         this.result = var1;
         this.notifyAll();
         if (this.callback != null) {
            this.callback.completed(var1);
         }

         return true;
      }
   }

   public boolean failed(Exception var1) {
      synchronized(this){}
      if (this.completed) {
         return false;
      } else {
         this.completed = true;
         this.ex = var1;
         this.notifyAll();
         if (this.callback != null) {
            this.callback.failed(var1);
         }

         return true;
      }
   }

   public boolean cancel(boolean var1) {
      synchronized(this){}
      if (this.completed) {
         return false;
      } else {
         this.completed = true;
         this.cancelled = true;
         this.notifyAll();
         if (this.callback != null) {
            this.callback.cancelled();
         }

         return true;
      }
   }

   public boolean cancel() {
      return this.cancel(true);
   }
}
