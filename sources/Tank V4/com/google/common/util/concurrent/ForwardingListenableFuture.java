package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class ForwardingListenableFuture extends ForwardingFuture implements ListenableFuture {
   protected ForwardingListenableFuture() {
   }

   protected abstract ListenableFuture delegate();

   public void addListener(Runnable var1, Executor var2) {
      this.delegate().addListener(var1, var2);
   }

   protected Future delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   public abstract static class SimpleForwardingListenableFuture extends ForwardingListenableFuture {
      private final ListenableFuture delegate;

      protected SimpleForwardingListenableFuture(ListenableFuture var1) {
         this.delegate = (ListenableFuture)Preconditions.checkNotNull(var1);
      }

      protected final ListenableFuture delegate() {
         return this.delegate;
      }

      protected Future delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
