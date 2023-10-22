package com.sun.webkit.text;

import java.util.Locale;

final class StringCase {
   private static String toLowerCase(String var0) {
      return var0.toLowerCase(Locale.ROOT);
   }

   private static String toUpperCase(String var0) {
      return var0.toUpperCase(Locale.ROOT);
   }

   private static String foldCase(String var0) {
      return var0.toUpperCase(Locale.ROOT).toLowerCase(Locale.ROOT);
   }
}
