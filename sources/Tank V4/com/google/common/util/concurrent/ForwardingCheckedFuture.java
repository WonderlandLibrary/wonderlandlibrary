package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class ForwardingCheckedFuture extends ForwardingListenableFuture implements CheckedFuture {
   public Object checkedGet() throws Exception {
      return this.delegate().checkedGet();
   }

   public Object checkedGet(long var1, TimeUnit var3) throws TimeoutException, Exception {
      return this.delegate().checkedGet(var1, var3);
   }

   protected abstract CheckedFuture delegate();

   protected ListenableFuture delegate() {
      return this.delegate();
   }

   protected Future delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   @Beta
   public abstract static class SimpleForwardingCheckedFuture extends ForwardingCheckedFuture {
      private final CheckedFuture delegate;

      protected SimpleForwardingCheckedFuture(CheckedFuture var1) {
         this.delegate = (CheckedFuture)Preconditions.checkNotNull(var1);
      }

      protected final CheckedFuture delegate() {
         return this.delegate;
      }

      protected ListenableFuture delegate() {
         return this.delegate();
      }

      protected Future delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
