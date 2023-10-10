package io.netty.util.internal;

import java.util.ArrayList;
import java.util.Formatter;

public final class StringUtil {
   public static final String NEWLINE;
   private static final String EMPTY_STRING = "";

   private StringUtil() {
   }

   public static String[] split(String var0, char var1) {
      int var2 = var0.length();
      ArrayList var3 = new ArrayList();
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < var2; ++var5) {
         if (var0.charAt(var5) == var1) {
            if (var4 == var5) {
               var3.add("");
            } else {
               var3.add(var0.substring(var4, var5));
            }

            var4 = var5 + 1;
         }
      }

      if (var4 == 0) {
         var3.add(var0);
      } else if (var4 != var2) {
         var3.add(var0.substring(var4, var2));
      } else {
         for(var5 = var3.size() - 1; var5 >= 0 && ((String)var3.get(var5)).isEmpty(); --var5) {
            var3.remove(var5);
         }
      }

      return (String[])var3.toArray(new String[var3.size()]);
   }

   public static String simpleClassName(Object var0) {
      return var0 == null ? "null_object" : simpleClassName(var0.getClass());
   }

   public static String simpleClassName(Class var0) {
      if (var0 == null) {
         return "null_class";
      } else {
         Package var1 = var0.getPackage();
         return var1 != null ? var0.getName().substring(var1.getName().length() + 1) : var0.getName();
      }
   }

   static {
      String var0;
      try {
         var0 = (new Formatter()).format("%n").toString();
      } catch (Exception var2) {
         var0 = "\n";
      }

      NEWLINE = var0;
   }
}
