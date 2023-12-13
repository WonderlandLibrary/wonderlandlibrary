package org.apache.http.util;

public class Asserts {
   public static void check(boolean var0, String var1) {
      if (!var0) {
         throw new IllegalStateException(var1);
      }
   }

   public static void check(boolean var0, String var1, Object... var2) {
      if (!var0) {
         throw new IllegalStateException(String.format(var1, var2));
      }
   }

   public static void notNull(Object var0, String var1) {
      if (var0 == null) {
         throw new IllegalStateException(var1 + " is null");
      }
   }

   public static void notEmpty(CharSequence var0, String var1) {
      if (TextUtils.isEmpty(var0)) {
         throw new IllegalStateException(var1 + " is empty");
      }
   }

   public static void notBlank(CharSequence var0, String var1) {
      if (TextUtils.isBlank(var0)) {
         throw new IllegalStateException(var1 + " is blank");
      }
   }
}
