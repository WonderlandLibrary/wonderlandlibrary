package com.sun.javafx.binding;

import javafx.beans.WeakListener;

public class ExpressionHelperBase {
   protected static int trim(int var0, Object[] var1) {
      for(int var2 = 0; var2 < var0; ++var2) {
         Object var3 = var1[var2];
         if (var3 instanceof WeakListener && ((WeakListener)var3).wasGarbageCollected()) {
            int var4 = var0 - var2 - 1;
            if (var4 > 0) {
               System.arraycopy(var1, var2 + 1, var1, var2, var4);
            }

            --var0;
            var1[var0] = null;
            --var2;
         }
      }

      return var0;
   }
}
