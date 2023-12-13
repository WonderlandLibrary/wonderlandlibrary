package com.ibm.icu.text;

import java.text.ParsePosition;

class ModulusSubstitution extends NFSubstitution {
   double divisor;
   NFRule ruleToUse;
   static final boolean $assertionsDisabled = !ModulusSubstitution.class.desiredAssertionStatus();

   ModulusSubstitution(int var1, double var2, NFRule var4, NFRuleSet var5, RuleBasedNumberFormat var6, String var7) {
      super(var1, var5, var6, var7);
      this.divisor = var2;
      if (var2 == 0.0D) {
         throw new IllegalStateException("Substitution with bad divisor (" + var2 + ") " + var7.substring(0, var1) + " | " + var7.substring(var1));
      } else {
         if (var7.equals(">>>")) {
            this.ruleToUse = var4;
         } else {
            this.ruleToUse = null;
         }

      }
   }

   public void setDivisor(int var1, int var2) {
      this.divisor = Math.pow((double)var1, (double)var2);
      if (this.divisor == 0.0D) {
         throw new IllegalStateException("Substitution with bad divisor");
      }
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         ModulusSubstitution var2 = (ModulusSubstitution)var1;
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

   public void doSubstitution(long var1, StringBuffer var3, int var4) {
      if (this.ruleToUse == null) {
         super.doSubstitution(var1, var3, var4);
      } else {
         long var5 = this.transformNumber(var1);
         this.ruleToUse.doFormat(var5, var3, var4 + this.pos);
      }

   }

   public void doSubstitution(double var1, StringBuffer var3, int var4) {
      if (this.ruleToUse == null) {
         super.doSubstitution(var1, var3, var4);
      } else {
         double var5 = this.transformNumber(var1);
         this.ruleToUse.doFormat(var5, var3, var4 + this.pos);
      }

   }

   public long transformNumber(long var1) {
      return (long)Math.floor((double)var1 % this.divisor);
   }

   public double transformNumber(double var1) {
      return Math.floor(var1 % this.divisor);
   }

   public Number doParse(String var1, ParsePosition var2, double var3, double var5, boolean var7) {
      if (this.ruleToUse == null) {
         return super.doParse(var1, var2, var3, var5, var7);
      } else {
         Number var8 = this.ruleToUse.doParse(var1, var2, false, var5);
         if (var2.getIndex() != 0) {
            double var9 = var8.doubleValue();
            var9 = this.composeRuleValue(var9, var3);
            return (Number)(var9 == (double)((long)var9) ? (long)var9 : new Double(var9));
         } else {
            return var8;
         }
      }
   }

   public double composeRuleValue(double var1, double var3) {
      return var3 - var3 % this.divisor + var1;
   }

   public double calcUpperBound(double var1) {
      return this.divisor;
   }

   public boolean isModulusSubstitution() {
      return true;
   }

   char tokenChar() {
      return '>';
   }
}
