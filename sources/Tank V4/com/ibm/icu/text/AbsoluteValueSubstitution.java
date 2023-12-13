package com.ibm.icu.text;

class AbsoluteValueSubstitution extends NFSubstitution {
   AbsoluteValueSubstitution(int var1, NFRuleSet var2, RuleBasedNumberFormat var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public long transformNumber(long var1) {
      return Math.abs(var1);
   }

   public double transformNumber(double var1) {
      return Math.abs(var1);
   }

   public double composeRuleValue(double var1, double var3) {
      return -var1;
   }

   public double calcUpperBound(double var1) {
      return Double.MAX_VALUE;
   }

   char tokenChar() {
      return '>';
   }
}
