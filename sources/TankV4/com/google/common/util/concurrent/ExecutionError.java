package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public class ExecutionError extends Error {
   private static final long serialVersionUID = 0L;

   protected ExecutionError() {
   }

   protected ExecutionError(@Nullable String var1) {
      super(var1);
   }

   public ExecutionError(@Nullable String var1, @Nullable Error var2) {
      super(var1, var2);
   }

   public ExecutionError(@Nullable Error var1) {
      super(var1);
   }
}
