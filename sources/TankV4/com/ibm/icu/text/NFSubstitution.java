package com.ibm.icu.text;

import java.text.ParsePosition;

abstract class NFSubstitution {
   int pos;
   NFRuleSet ruleSet = null;
   DecimalFormat numberFormat = null;
   RuleBasedNumberFormat rbnf = null;
   static final boolean $assertionsDisabled = !NFSubstitution.class.desiredAssertionStatus();

   public static NFSubstitution makeSubstitution(int var0, NFRule var1, NFRule var2, NFRuleSet var3, RuleBasedNumberFormat var4, String var5) {
      if (var5.length() == 0) {
         return new NullSubstitution(var0, var3, var4, var5);
      } else {
         switch(var5.charAt(0)) {
         case '<':
            if (var1.getBaseValue() == -1L) {
               throw new IllegalArgumentException("<< not allowed in negative-number rule");
            } else {
               if (var1.getBaseValue() != -2L && var1.getBaseValue() != -3L && var1.getBaseValue() != -4L) {
                  if (var3.isFractionSet()) {
                     return new NumeratorSubstitution(var0, (double)var1.getBaseValue(), var4.getDefaultRuleSet(), var4, var5);
                  }

                  return new MultiplierSubstitution(var0, var1.getDivisor(), var3, var4, var5);
               }

               return new IntegralPartSubstitution(var0, var3, var4, var5);
            }
         case '=':
            return new SameValueSubstitution(var0, var3, var4, var5);
         case '>':
            if (var1.getBaseValue() == -1L) {
               return new AbsoluteValueSubstitution(var0, var3, var4, var5);
            } else {
               if (var1.getBaseValue() != -2L && var1.getBaseValue() != -3L && var1.getBaseValue() != -4L) {
                  if (var3.isFractionSet()) {
                     throw new IllegalArgumentException(">> not allowed in fraction rule set");
                  }

                  return new ModulusSubstitution(var0, var1.getDivisor(), var2, var3, var4, var5);
               }

               return new FractionalPartSubstitution(var0, var3, var4, var5);
            }
         default:
            throw new IllegalArgumentException("Illegal substitution character");
         }
      }
   }

   NFSubstitution(int var1, NFRuleSet var2, RuleBasedNumberFormat var3, String var4) {
      this.pos = var1;
      this.rbnf = var3;
      if (var4.length() >= 2 && var4.charAt(0) == var4.charAt(var4.length() - 1)) {
         var4 = var4.substring(1, var4.length() - 1);
      } else if (var4.length() != 0) {
         throw new IllegalArgumentException("Illegal substitution syntax");
      }

      if (var4.length() == 0) {
         this.ruleSet = var2;
      } else if (var4.charAt(0) == '%') {
         this.ruleSet = var3.findRuleSet(var4);
      } else if (var4.charAt(0) != '#' && var4.charAt(0) != '0') {
         if (var4.charAt(0) != '>') {
            throw new IllegalArgumentException("Illegal substitution syntax");
         }

         this.ruleSet = var2;
         this.numberFormat = null;
      } else {
         this.numberFormat = new DecimalFormat(var4);
         this.numberFormat.setDecimalFormatSymbols(var3.getDecimalFormatSymbols());
      }

   }

   public void setDivisor(int var1, int var2) {
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         boolean var10000;
         label51: {
            NFSubstitution var2 = (NFSubstitution)var1;
            if (this.pos == var2.pos && (this.ruleSet != null || var2.ruleSet == null)) {
               if (this.numberFormat == null) {
                  if (var2.numberFormat == null) {
                     break label51;
                  }
               } else if (this.numberFormat.equals(var2.numberFormat)) {
                  break label51;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   public String toString() {
      return this.ruleSet != null ? this.tokenChar() + this.ruleSet.getName() + this.tokenChar() : this.tokenChar() + this.numberFormat.toPattern() + this.tokenChar();
   }

   public void doSubstitution(long var1, StringBuffer var3, int var4) {
      if (this.ruleSet != null) {
         long var5 = this.transformNumber(var1);
         this.ruleSet.format(var5, var3, var4 + this.pos);
      } else {
         double var7 = this.transformNumber((double)var1);
         if (this.numberFormat.getMaximumFractionDigits() == 0) {
            var7 = Math.floor(var7);
         }

         var3.insert(var4 + this.pos, this.numberFormat.format(var7));
      }

   }

   public void doSubstitution(double var1, StringBuffer var3, int var4) {
      double var5 = this.transformNumber(var1);
      if (var5 == Math.floor(var5) && this.ruleSet != null) {
         this.ruleSet.format((long)var5, var3, var4 + this.pos);
      } else if (this.ruleSet != null) {
         this.ruleSet.format(var5, var3, var4 + this.pos);
      } else {
         var3.insert(var4 + this.pos, this.numberFormat.format(var5));
      }

   }

   public abstract long transformNumber(long var1);

   public abstract double transformNumber(double var1);

   public Number doParse(String var1, ParsePosition var2, double var3, double var5, boolean var7) {
      var5 = this.calcUpperBound(var5);
      Number var8;
      if (this.ruleSet != null) {
         var8 = this.ruleSet.parse(var1, var2, var5);
         if (var7 && !this.ruleSet.isFractionSet() && var2.getIndex() == 0) {
            var8 = this.rbnf.getDecimalFormat().parse(var1, var2);
         }
      } else {
         var8 = this.numberFormat.parse(var1, var2);
      }

      if (var2.getIndex() != 0) {
         double var9 = var8.doubleValue();
         var9 = this.composeRuleValue(var9, var3);
         return (Number)(var9 == (double)((long)var9) ? (long)var9 : new Double(var9));
      } else {
         return var8;
      }
   }

   public abstract double composeRuleValue(double var1, double var3);

   public abstract double calcUpperBound(double var1);

   public final int getPos() {
      return this.pos;
   }

   abstract char tokenChar();

   public boolean isNullSubstitution() {
      return false;
   }

   public boolean isModulusSubstitution() {
      return false;
   }
}
