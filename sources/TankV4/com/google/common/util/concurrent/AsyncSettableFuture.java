package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import javax.annotation.Nullable;

final class AsyncSettableFuture extends ForwardingListenableFuture {
   private final AsyncSettableFuture.NestedFuture nested = new AsyncSettableFuture.NestedFuture();
   private final ListenableFuture dereferenced;

   public static AsyncSettableFuture create() {
      return new AsyncSettableFuture();
   }

   private AsyncSettableFuture() {
      this.dereferenced = Futures.dereference(this.nested);
   }

   protected ListenableFuture delegate() {
      return this.dereferenced;
   }

   public boolean setFuture(ListenableFuture var1) {
      return this.nested.setFuture((ListenableFuture)Preconditions.checkNotNull(var1));
   }

   public boolean setValue(@Nullable Object var1) {
      return this.setFuture(Futures.immediateFuture(var1));
   }

   public boolean setException(Throwable var1) {
      return this.setFuture(Futures.immediateFailedFuture(var1));
   }

   public boolean isSet() {
      return this.nested.isDone();
   }

   protected Future delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   private static final class NestedFuture extends AbstractFuture {
      private NestedFuture() {
      }

      boolean setFuture(ListenableFuture var1) {
         boolean var2 = this.set(var1);
         if (this.isCancelled()) {
            var1.cancel(this.wasInterrupted());
         }

         return var2;
      }

      NestedFuture(Object var1) {
         this();
      }
   }
}
