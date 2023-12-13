package com.google.common.util.concurrent;

import javax.annotation.Nullable;

public class UncheckedTimeoutException extends RuntimeException {
   private static final long serialVersionUID = 0L;

   public UncheckedTimeoutException() {
   }

   public UncheckedTimeoutException(@Nullable String var1) {
      super(var1);
   }

   public UncheckedTimeoutException(@Nullable Throwable var1) {
      super(var1);
   }

   public UncheckedTimeoutException(@Nullable String var1, @Nullable Throwable var2) {
      super(var1, var2);
   }
}
