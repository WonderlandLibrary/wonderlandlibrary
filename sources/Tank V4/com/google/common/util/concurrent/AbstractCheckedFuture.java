package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class AbstractCheckedFuture extends ForwardingListenableFuture.SimpleForwardingListenableFuture implements CheckedFuture {
   protected AbstractCheckedFuture(ListenableFuture var1) {
      super(var1);
   }

   protected abstract Exception mapException(Exception var1);

   public Object checkedGet() throws Exception {
      try {
         return this.get();
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         throw this.mapException(var2);
      } catch (CancellationException var3) {
         throw this.mapException(var3);
      } catch (ExecutionException var4) {
         throw this.mapException(var4);
      }
   }

   public Object checkedGet(long var1, TimeUnit var3) throws TimeoutException, Exception {
      try {
         return this.get(var1, var3);
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         throw this.mapException(var5);
      } catch (CancellationException var6) {
         throw this.mapException(var6);
      } catch (ExecutionException var7) {
         throw this.mapException(var7);
      }
   }
}
