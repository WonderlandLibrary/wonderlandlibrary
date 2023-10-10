package joptsimple.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class Strings {
   public static final String EMPTY = "";
   public static final String SINGLE_QUOTE = "'";
   public static final String LINE_SEPARATOR = System.getProperty("line.separator");

   private Strings() {
      throw new UnsupportedOperationException();
   }

   public static String repeat(char var0, int var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(var0);
      }

      return var2.toString();
   }

   public static boolean isNullOrEmpty(String var0) {
      return var0 == null || "".equals(var0);
   }

   public static String surround(String var0, char var1, char var2) {
      return var1 + var0 + var2;
   }

   public static String join(String[] var0, String var1) {
      return join(Arrays.asList(var0), var1);
   }

   public static String join(List var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         var2.append((String)var3.next());
         if (var3.hasNext()) {
            var2.append(var1);
         }
      }

      return var2.toString();
   }
}
