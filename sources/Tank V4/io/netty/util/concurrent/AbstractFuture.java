package io.netty.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFuture implements Future {
   public Object get() throws InterruptedException, ExecutionException {
      this.await();
      Throwable var1 = this.cause();
      if (var1 == null) {
         return this.getNow();
      } else {
         throw new ExecutionException(var1);
      }
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      if (this.await(var1, var3)) {
         Throwable var4 = this.cause();
         if (var4 == null) {
            return this.getNow();
         } else {
            throw new ExecutionException(var4);
         }
      } else {
         throw new TimeoutException();
      }
   }
}
