package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class Verify {
   public static void verify(boolean var0) {
      if (!var0) {
         throw new VerifyException();
      }
   }

   public static void verify(boolean var0, @Nullable String var1, @Nullable Object... var2) {
      if (!var0) {
         throw new VerifyException(Preconditions.format(var1, var2));
      }
   }

   public static Object verifyNotNull(@Nullable Object var0) {
      return verifyNotNull(var0, "expected a non-null reference");
   }

   public static Object verifyNotNull(@Nullable Object var0, @Nullable String var1, @Nullable Object... var2) {
      verify(var0 != null, var1, var2);
      return var0;
   }

   private Verify() {
   }
}
