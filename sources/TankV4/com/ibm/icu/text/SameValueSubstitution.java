package com.ibm.icu.text;

class SameValueSubstitution extends NFSubstitution {
   SameValueSubstitution(int var1, NFRuleSet var2, RuleBasedNumberFormat var3, String var4) {
      super(var1, var2, var3, var4);
      if (var4.equals("==")) {
         throw new IllegalArgumentException("== is not a legal token");
      }
   }

   public long transformNumber(long var1) {
      return var1;
   }

   public double transformNumber(double var1) {
      return var1;
   }

   public double composeRuleValue(double var1, double var3) {
      return var1;
   }

   public double calcUpperBound(double var1) {
      return var1;
   }

   char tokenChar() {
      return '=';
   }
}
