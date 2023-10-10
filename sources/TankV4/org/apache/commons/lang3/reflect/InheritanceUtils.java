package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.BooleanUtils;

public class InheritanceUtils {
   public static int distance(Class var0, Class var1) {
      if (var0 != null && var1 != null) {
         if (var0.equals(var1)) {
            return 0;
         } else {
            Class var2 = var0.getSuperclass();
            int var3 = BooleanUtils.toInteger(var1.equals(var2));
            if (var3 == 1) {
               return var3;
            } else {
               var3 += distance(var2, var1);
               return var3 > 0 ? var3 + 1 : -1;
            }
         }
      } else {
         return -1;
      }
   }
}
