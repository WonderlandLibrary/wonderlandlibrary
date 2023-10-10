package org.apache.http.util;

public final class TextUtils {
   public static boolean isEmpty(CharSequence var0) {
      if (var0 == null) {
         return true;
      } else {
         return var0.length() == 0;
      }
   }

   public static boolean isBlank(CharSequence var0) {
      if (var0 == null) {
         return true;
      } else {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            if (!Character.isWhitespace(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }
}
