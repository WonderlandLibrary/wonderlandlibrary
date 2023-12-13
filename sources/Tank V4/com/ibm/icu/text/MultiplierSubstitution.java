package com.ibm.icu.text;

class MultiplierSubstitution extends NFSubstitution {
   double divisor;
   static final boolean $assertionsDisabled = !MultiplierSubstitution.class.desiredAssertionStatus();

   MultiplierSubstitution(int var1, double var2, NFRuleSet var4, RuleBasedNumberFormat var5, String var6) {
      super(var1, var4, var5, var6);
      this.divisor = var2;
      if (var2 == 0.0D) {
         throw new IllegalStateException("Substitution with bad divisor (" + var2 + ") " + var6.substring(0, var1) + " | " + var6.substring(var1));
      }
   }

   public void setDivisor(int var1, int var2) {
      this.divisor = Math.pow((double)var1, (double)var2);
      if (this.divisor == 0.0D) {
         throw new IllegalStateException("Substitution with divisor 0");
      }
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         MultiplierSubstitution var2 = (MultiplierSubstitution)var1;
         return this.divisor == var2.divisor;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   public long transformNumber(long var1) {
      return (long)Math.floor((double)var1 / this.divisor);
   }

   public double transformNumber(double var1) {
      return this.ruleSet == null ? var1 / this.divisor : Math.floor(var1 / this.divisor);
   }

   public double composeRuleValue(double var1, double var3) {
      return var1 * this.divisor;
   }

   public double calcUpperBound(double var1) {
      return this.divisor;
   }

   char tokenChar() {
      return '<';
   }
}
