package com.ibm.icu.impl;

import java.util.Comparator;

public class MultiComparator implements Comparator {
   private Comparator[] comparators;

   public MultiComparator(Comparator... var1) {
      this.comparators = var1;
   }

   public int compare(Object var1, Object var2) {
      for(int var3 = 0; var3 < this.comparators.length; ++var3) {
         int var4 = this.comparators[var3].compare(var1, var2);
         if (var4 != 0) {
            if (var4 > 0) {
               return var3 + 1;
            }

            return -(var3 + 1);
         }
      }

      return 0;
   }
}
