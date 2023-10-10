package com.ibm.icu.text;

public abstract class UnicodeFilter implements UnicodeMatcher {
   public abstract boolean contains(int var1);

   public int matches(Replaceable var1, int[] var2, int var3, boolean var4) {
      int var5;
      if (var2[0] < var3 && this.contains(var5 = var1.char32At(var2[0]))) {
         var2[0] += UTF16.getCharCount(var5);
         return 2;
      } else if (var2[0] > var3 && this.contains(var1.char32At(var2[0]))) {
         int var10002 = var2[0]--;
         if (var2[0] >= 0) {
            var2[0] -= UTF16.getCharCount(var1.char32At(var2[0])) - 1;
         }

         return 2;
      } else {
         return var4 && var2[0] == var3 ? 1 : 0;
      }
   }

   /** @deprecated */
   protected UnicodeFilter() {
   }
}
