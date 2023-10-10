package org.apache.http.util;

import java.util.Collection;

public class Args {
   public static void check(boolean var0, String var1) {
      if (!var0) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static void check(boolean var0, String var1, Object... var2) {
      if (!var0) {
         throw new IllegalArgumentException(String.format(var1, var2));
      }
   }

   public static Object notNull(Object var0, String var1) {
      if (var0 == null) {
         throw new IllegalArgumentException(var1 + " may not be null");
      } else {
         return var0;
      }
   }

   public static CharSequence notEmpty(CharSequence var0, String var1) {
      if (var0 == null) {
         throw new IllegalArgumentException(var1 + " may not be null");
      } else if (TextUtils.isEmpty(var0)) {
         throw new IllegalArgumentException(var1 + " may not be empty");
      } else {
         return var0;
      }
   }

   public static CharSequence notBlank(CharSequence var0, String var1) {
      if (var0 == null) {
         throw new IllegalArgumentException(var1 + " may not be null");
      } else if (TextUtils.isBlank(var0)) {
         throw new IllegalArgumentException(var1 + " may not be blank");
      } else {
         return var0;
      }
   }

   public static Collection notEmpty(Collection var0, String var1) {
      if (var0 == null) {
         throw new IllegalArgumentException(var1 + " may not be null");
      } else if (var0.isEmpty()) {
         throw new IllegalArgumentException(var1 + " may not be empty");
      } else {
         return var0;
      }
   }

   public static int positive(int var0, String var1) {
      if (var0 <= 0) {
         throw new IllegalArgumentException(var1 + " may not be negative or zero");
      } else {
         return var0;
      }
   }

   public static long positive(long var0, String var2) {
      if (var0 <= 0L) {
         throw new IllegalArgumentException(var2 + " may not be negative or zero");
      } else {
         return var0;
      }
   }

   public static int notNegative(int var0, String var1) {
      if (var0 < 0) {
         throw new IllegalArgumentException(var1 + " may not be negative");
      } else {
         return var0;
      }
   }

   public static long notNegative(long var0, String var2) {
      if (var0 < 0L) {
         throw new IllegalArgumentException(var2 + " may not be negative");
      } else {
         return var0;
      }
   }
}
