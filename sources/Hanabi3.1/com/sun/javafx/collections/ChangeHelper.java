package com.sun.javafx.collections;

import java.util.Arrays;
import java.util.List;

public class ChangeHelper {
   public static String addRemoveChangeToString(int var0, int var1, List var2, List var3) {
      StringBuilder var4 = new StringBuilder();
      if (var3.isEmpty()) {
         var4.append(var2.subList(var0, var1));
         var4.append(" added at ").append(var0);
      } else {
         var4.append(var3);
         if (var0 == var1) {
            var4.append(" removed at ").append(var0);
         } else {
            var4.append(" replaced by ");
            var4.append(var2.subList(var0, var1));
            var4.append(" at ").append(var0);
         }
      }

      return var4.toString();
   }

   public static String permChangeToString(int[] var0) {
      return "permutated by " + Arrays.toString(var0);
   }

   public static String updateChangeToString(int var0, int var1) {
      return "updated at range [" + var0 + ", " + var1 + ")";
   }
}
