package com.ibm.icu.text;

import java.text.ParsePosition;

class NumeratorSubstitution extends NFSubstitution {
   double denominator;
   boolean withZeros;
   static final boolean $assertionsDisabled = !NumeratorSubstitution.class.desiredAssertionStatus();

   NumeratorSubstitution(int var1, double var2, NFRuleSet var4, RuleBasedNumberFormat var5, String var6) {
      super(var1, var4, var5, fixdesc(var6));
      this.denominator = var2;
      this.withZeros = var6.endsWith("<<");
   }

   static String fixdesc(String var0) {
      return var0.endsWith("<<") ? var0.substring(0, var0.length() - 1) : var0;
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NumeratorSubstitution var2 = (NumeratorSubstitution)var1;
         return this.denominator == var2.denominator;
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

   public void doSubstitution(double var1, StringBuffer var3, int var4) {
      double var5 = this.transformNumber(var1);
      if (this.withZeros && this.ruleSet != null) {
         long var7 = (long)var5;
         int var9 = var3.length();

         while((double)(var7 *= 10L) < this.denominator) {
            var3.insert(var4 + this.pos, ' ');
            this.ruleSet.format(0L, var3, var4 + this.pos);
         }

         var4 += var3.length() - var9;
      }

      if (var5 == Math.floor(var5) && this.ruleSet != null) {
         this.ruleSet.format((long)var5, var3, var4 + this.pos);
      } else if (this.ruleSet != null) {
         this.ruleSet.format(var5, var3, var4 + this.pos);
      } else {
         var3.insert(var4 + this.pos, this.numberFormat.format(var5));
      }

   }

   public long transformNumber(long var1) {
      return Math.round((double)var1 * this.denominator);
   }

   public double transformNumber(double var1) {
      return (double)Math.round(var1 * this.denominator);
   }

   public Number doParse(String var1, ParsePosition var2, double var3, double var5, boolean var7) {
      int var8 = 0;
      if (this.withZeros) {
         String var9 = var1;
         ParsePosition var10 = new ParsePosition(1);

         while(var9.length() > 0 && var10.getIndex() != 0) {
            var10.setIndex(0);
            this.ruleSet.parse(var9, var10, 1.0D).intValue();
            if (var10.getIndex() == 0) {
               break;
            }

            ++var8;
            var2.setIndex(var2.getIndex() + var10.getIndex());
            var9 = var9.substring(var10.getIndex());

            while(var9.length() > 0 && var9.charAt(0) == ' ') {
               var9 = var9.substring(1);
               var2.setIndex(var2.getIndex() + 1);
            }
         }

         var1 = var1.substring(var2.getIndex());
         var2.setIndex(0);
      }

      Object var14 = super.doParse(var1, var2, this.withZeros ? 1.0D : var3, var5, false);
      if (this.withZeros) {
         long var15 = ((Number)var14).longValue();

         long var12;
         for(var12 = 1L; var12 <= var15; var12 *= 10L) {
         }

         while(var8 > 0) {
            var12 *= 10L;
            --var8;
         }

         var14 = new Double((double)var15 / (double)var12);
      }

      return (Number)var14;
   }

   public double composeRuleValue(double var1, double var3) {
      return var1 / var3;
   }

   public double calcUpperBound(double var1) {
      return this.denominator;
   }

   char tokenChar() {
      return '<';
   }
}
