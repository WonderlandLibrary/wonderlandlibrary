package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;

final class FailedChannelFuture extends CompleteChannelFuture {
   private final Throwable cause;

   public FailedChannelFuture(Channel var1, EventExecutor var2, Throwable var3) {
      super(var1, var2);
      if (var3 == null) {
         throw new NullPointerException("cause");
      } else {
         this.cause = var3;
      }
   }

   public Throwable cause() {
      return this.cause;
   }

   public boolean isSuccess() {
      return false;
   }

   public ChannelFuture sync() {
      PlatformDependent.throwException(this.cause);
      return this;
   }

   public ChannelFuture syncUninterruptibly() {
      PlatformDependent.throwException(this.cause);
      return this;
   }

   public Future syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   public Future sync() throws InterruptedException {
      return this.sync();
   }
}
