package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public class VerifyException extends RuntimeException {
   public VerifyException() {
   }

   public VerifyException(@Nullable String var1) {
      super(var1);
   }
}
