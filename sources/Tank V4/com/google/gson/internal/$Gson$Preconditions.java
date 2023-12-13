package com.google.gson.internal;

public final class $Gson$Preconditions {
   public static Object checkNotNull(Object var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return var0;
      }
   }

   public static void checkArgument(boolean var0) {
      if (!var0) {
         throw new IllegalArgumentException();
      }
   }
}
