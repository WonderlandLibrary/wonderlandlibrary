package com.google.common.util.concurrent;

import javax.annotation.Nullable;

public final class SettableFuture extends AbstractFuture {
   public static SettableFuture create() {
      return new SettableFuture();
   }

   private SettableFuture() {
   }

   public boolean set(@Nullable Object var1) {
      return super.set(var1);
   }

   public boolean setException(Throwable var1) {
      return super.setException(var1);
   }
}
