package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;

class Quantifier implements UnicodeMatcher {
   private UnicodeMatcher matcher;
   private int minCount;
   private int maxCount;
   public static final int MAX = Integer.MAX_VALUE;

   public Quantifier(UnicodeMatcher var1, int var2, int var3) {
      if (var1 != null && var2 >= 0 && var3 >= 0 && var2 <= var3) {
         this.matcher = var1;
         this.minCount = var2;
         this.maxCount = var3;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int matches(Replaceable var1, int[] var2, int var3, boolean var4) {
      int var5 = var2[0];
      int var6 = 0;

      while(var6 < this.maxCount) {
         int var7 = var2[0];
         int var8 = this.matcher.matches(var1, var2, var3, var4);
         if (var8 != 2) {
            if (var4 && var8 == 1) {
               return 1;
            }
            break;
         }

         ++var6;
         if (var7 == var2[0]) {
            break;
         }
      }

      if (var4 && var2[0] == var3) {
         return 1;
      } else if (var6 >= this.minCount) {
         return 2;
      } else {
         var2[0] = var5;
         return 0;
      }
   }

   public String toPattern(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.matcher.toPattern(var1));
      if (this.minCount == 0) {
         if (this.maxCount == 1) {
            return var2.append('?').toString();
         }

         if (this.maxCount == Integer.MAX_VALUE) {
            return var2.append('*').toString();
         }
      } else if (this.minCount == 1 && this.maxCount == Integer.MAX_VALUE) {
         return var2.append('+').toString();
      }

      var2.append('{');
      var2.append(Utility.hex((long)this.minCount, 1));
      var2.append(',');
      if (this.maxCount != Integer.MAX_VALUE) {
         var2.append(Utility.hex((long)this.maxCount, 1));
      }

      var2.append('}');
      return var2.toString();
   }

   public boolean matchesIndexValue(int var1) {
      return this.minCount == 0 || this.matcher.matchesIndexValue(var1);
   }

   public void addMatchSetTo(UnicodeSet var1) {
      if (this.maxCount > 0) {
         this.matcher.addMatchSetTo(var1);
      }

   }
}
