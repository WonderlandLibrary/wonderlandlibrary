package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;

public final class FailedFuture extends CompleteFuture {
   private final Throwable cause;

   public FailedFuture(EventExecutor var1, Throwable var2) {
      super(var1);
      if (var2 == null) {
         throw new NullPointerException("cause");
      } else {
         this.cause = var2;
      }
   }

   public Throwable cause() {
      return this.cause;
   }

   public boolean isSuccess() {
      return false;
   }

   public Future sync() {
      PlatformDependent.throwException(this.cause);
      return this;
   }

   public Future syncUninterruptibly() {
      PlatformDependent.throwException(this.cause);
      return this;
   }

   public Object getNow() {
      return null;
   }
}
