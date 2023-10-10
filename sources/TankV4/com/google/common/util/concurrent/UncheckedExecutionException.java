package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public class UncheckedExecutionException extends RuntimeException {
   private static final long serialVersionUID = 0L;

   protected UncheckedExecutionException() {
   }

   protected UncheckedExecutionException(@Nullable String var1) {
      super(var1);
   }

   public UncheckedExecutionException(@Nullable String var1, @Nullable Throwable var2) {
      super(var1, var2);
   }

   public UncheckedExecutionException(@Nullable Throwable var1) {
      super(var1);
   }
}
