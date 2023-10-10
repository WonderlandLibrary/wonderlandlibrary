package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import java.text.ParsePosition;
import java.util.ArrayList;

final class NFRuleSet {
   private String name;
   private NFRule[] rules;
   private NFRule negativeNumberRule = null;
   private NFRule[] fractionRules = new NFRule[3];
   private boolean isFractionRuleSet = false;
   private boolean isParseable = true;
   private int recursionCount = 0;
   private static final int RECURSION_LIMIT = 50;
   static final boolean $assertionsDisabled = !NFRuleSet.class.desiredAssertionStatus();

   public NFRuleSet(String[] var1, int var2) throws IllegalArgumentException {
      String var3 = var1[var2];
      if (var3.length() == 0) {
         throw new IllegalArgumentException("Empty rule set description");
      } else {
         if (var3.charAt(0) == '%') {
            int var4 = var3.indexOf(58);
            if (var4 == -1) {
               throw new IllegalArgumentException("Rule set name doesn't end in colon");
            }

            this.name = var3.substring(0, var4);

            while(var4 < var3.length()) {
               ++var4;
               if (!PatternProps.isWhiteSpace(var3.charAt(var4))) {
                  break;
               }
            }

            var3 = var3.substring(var4);
            var1[var2] = var3;
         } else {
            this.name = "%default";
         }

         if (var3.length() == 0) {
            throw new IllegalArgumentException("Empty rule set description");
         } else {
            if (this.name.endsWith("@noparse")) {
               this.name = this.name.substring(0, this.name.length() - 8);
               this.isParseable = false;
            }

         }
      }
   }

   public void parseRules(String var1, RuleBasedNumberFormat var2) {
      ArrayList var3 = new ArrayList();
      int var4 = 0;

      for(int var5 = var1.indexOf(59); var4 != -1; var5 = var1.indexOf(59, var5 + 1)) {
         if (var5 != -1) {
            var3.add(var1.substring(var4, var5));
            var4 = var5 + 1;
         } else {
            if (var4 < var1.length()) {
               var3.add(var1.substring(var4));
            }

            var4 = var5;
         }
      }

      ArrayList var6 = new ArrayList();
      NFRule var7 = null;

      for(int var8 = 0; var8 < var3.size(); ++var8) {
         Object var9 = NFRule.makeRules((String)var3.get(var8), this, var7, var2);
         if (var9 instanceof NFRule) {
            var6.add((NFRule)var9);
            var7 = (NFRule)var9;
         } else if (var9 instanceof NFRule[]) {
            NFRule[] var10 = (NFRule[])((NFRule[])var9);

            for(int var11 = 0; var11 < var10.length; ++var11) {
               var6.add(var10[var11]);
               var7 = var10[var11];
            }
         }
      }

      var3 = null;
      long var12 = 0L;
      int var13 = 0;

      while(var13 < var6.size()) {
         NFRule var14 = (NFRule)var6.get(var13);
         switch((int)var14.getBaseValue()) {
         case -4:
            this.fractionRules[2] = var14;
            var6.remove(var13);
            break;
         case -3:
            this.fractionRules[1] = var14;
            var6.remove(var13);
            break;
         case -2:
            this.fractionRules[0] = var14;
            var6.remove(var13);
            break;
         case -1:
            this.negativeNumberRule = var14;
            var6.remove(var13);
            break;
         case 0:
            var14.setBaseValue(var12);
            if (!this.isFractionRuleSet) {
               ++var12;
            }

            ++var13;
            break;
         default:
            if (var14.getBaseValue() < var12) {
               throw new IllegalArgumentException("Rules are not in order, base: " + var14.getBaseValue() + " < " + var12);
            }

            var12 = var14.getBaseValue();
            if (!this.isFractionRuleSet) {
               ++var12;
            }

            ++var13;
         }
      }

      this.rules = new NFRule[var6.size()];
      var6.toArray(this.rules);
   }

   public void makeIntoFractionRuleSet() {
      this.isFractionRuleSet = true;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof NFRuleSet)) {
         return false;
      } else {
         NFRuleSet var2 = (NFRuleSet)var1;
         if (this.name.equals(var2.name) && Utility.objectEquals(this.negativeNumberRule, var2.negativeNumberRule) && Utility.objectEquals(this.fractionRules[0], var2.fractionRules[0]) && Utility.objectEquals(this.fractionRules[1], var2.fractionRules[1]) && Utility.objectEquals(this.fractionRules[2], var2.fractionRules[2]) && this.rules.length == var2.rules.length && this.isFractionRuleSet == var2.isFractionRuleSet) {
            for(int var3 = 0; var3 < this.rules.length; ++var3) {
               if (!this.rules[var3].equals(var2.rules[var3])) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
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
      StringBuilder var1 = new StringBuilder();
      var1.append(this.name + ":\n");

      for(int var2 = 0; var2 < this.rules.length; ++var2) {
         var1.append("    " + this.rules[var2].toString() + "\n");
      }

      if (this.negativeNumberRule != null) {
         var1.append("    " + this.negativeNumberRule.toString() + "\n");
      }

      if (this.fractionRules[0] != null) {
         var1.append("    " + this.fractionRules[0].toString() + "\n");
      }

      if (this.fractionRules[1] != null) {
         var1.append("    " + this.fractionRules[1].toString() + "\n");
      }

      if (this.fractionRules[2] != null) {
         var1.append("    " + this.fractionRules[2].toString() + "\n");
      }

      return var1.toString();
   }

   public boolean isFractionSet() {
      return this.isFractionRuleSet;
   }

   public String getName() {
      return this.name;
   }

   public boolean isPublic() {
      return !this.name.startsWith("%%");
   }

   public boolean isParseable() {
      return this.isParseable;
   }

   public void format(long var1, StringBuffer var3, int var4) {
      NFRule var5 = this.findNormalRule(var1);
      if (++this.recursionCount >= 50) {
         this.recursionCount = 0;
         throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
      } else {
         var5.doFormat(var1, var3, var4);
         --this.recursionCount;
      }
   }

   public void format(double var1, StringBuffer var3, int var4) {
      NFRule var5 = this.findRule(var1);
      if (++this.recursionCount >= 50) {
         this.recursionCount = 0;
         throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
      } else {
         var5.doFormat(var1, var3, var4);
         --this.recursionCount;
      }
   }

   private NFRule findRule(double var1) {
      if (this.isFractionRuleSet) {
         return this.findFractionRuleSetRule(var1);
      } else {
         if (var1 < 0.0D) {
            if (this.negativeNumberRule != null) {
               return this.negativeNumberRule;
            }

            var1 = -var1;
         }

         if (var1 != Math.floor(var1)) {
            if (var1 < 1.0D && this.fractionRules[1] != null) {
               return this.fractionRules[1];
            }

            if (this.fractionRules[0] != null) {
               return this.fractionRules[0];
            }
         }

         return this.fractionRules[2] != null ? this.fractionRules[2] : this.findNormalRule(Math.round(var1));
      }
   }

   private NFRule findNormalRule(long var1) {
      if (this.isFractionRuleSet) {
         return this.findFractionRuleSetRule((double)var1);
      } else {
         if (var1 < 0L) {
            if (this.negativeNumberRule != null) {
               return this.negativeNumberRule;
            }

            var1 = -var1;
         }

         int var3 = 0;
         int var4 = this.rules.length;
         if (var4 > 0) {
            while(var3 < var4) {
               int var5 = var3 + var4 >>> 1;
               if (this.rules[var5].getBaseValue() == var1) {
                  return this.rules[var5];
               }

               if (this.rules[var5].getBaseValue() > var1) {
                  var4 = var5;
               } else {
                  var3 = var5 + 1;
               }
            }

            if (var4 == 0) {
               throw new IllegalStateException("The rule set " + this.name + " cannot format the value " + var1);
            } else {
               NFRule var6 = this.rules[var4 - 1];
               if (var6.shouldRollBack((double)var1)) {
                  if (var4 == 1) {
                     throw new IllegalStateException("The rule set " + this.name + " cannot roll back from the rule '" + var6 + "'");
                  }

                  var6 = this.rules[var4 - 2];
               }

               return var6;
            }
         } else {
            return this.fractionRules[2];
         }
      }
   }

   private NFRule findFractionRuleSetRule(double var1) {
      long var3 = this.rules[0].getBaseValue();

      for(int var5 = 1; var5 < this.rules.length; ++var5) {
         var3 = lcm(var3, this.rules[var5].getBaseValue());
      }

      long var13 = Math.round(var1 * (double)var3);
      long var9 = Long.MAX_VALUE;
      int var11 = 0;

      for(int var12 = 0; var12 < this.rules.length; ++var12) {
         long var7 = var13 * this.rules[var12].getBaseValue() % var3;
         if (var3 - var7 < var7) {
            var7 = var3 - var7;
         }

         if (var7 < var9) {
            var9 = var7;
            var11 = var12;
            if (var7 == 0L) {
               break;
            }
         }
      }

      if (var11 + 1 < this.rules.length && this.rules[var11 + 1].getBaseValue() == this.rules[var11].getBaseValue() && (Math.round(var1 * (double)this.rules[var11].getBaseValue()) < 1L || Math.round(var1 * (double)this.rules[var11].getBaseValue()) >= 2L)) {
         ++var11;
      }

      return this.rules[var11];
   }

   private static long lcm(long var0, long var2) {
      long var4 = var0;
      long var6 = var2;

      int var8;
      for(var8 = 0; (var4 & 1L) == 0L && (var6 & 1L) == 0L; var6 >>= 1) {
         ++var8;
         var4 >>= 1;
      }

      long var9;
      if ((var4 & 1L) == 1L) {
         var9 = -var6;
      } else {
         var9 = var4;
      }

      for(; var9 != 0L; var9 = var4 - var6) {
         while((var9 & 1L) == 0L) {
            var9 >>= 1;
         }

         if (var9 > 0L) {
            var4 = var9;
         } else {
            var6 = -var9;
         }
      }

      long var11 = var4 << var8;
      return var0 / var11 * var2;
   }

   public Number parse(String var1, ParsePosition var2, double var3) {
      ParsePosition var5 = new ParsePosition(0);
      Object var6 = 0L;
      Number var7 = null;
      if (var1.length() == 0) {
         return (Number)var6;
      } else {
         if (this.negativeNumberRule != null) {
            var7 = this.negativeNumberRule.doParse(var1, var2, false, var3);
            if (var2.getIndex() > var5.getIndex()) {
               var6 = var7;
               var5.setIndex(var2.getIndex());
            }

            var2.setIndex(0);
         }

         int var8;
         for(var8 = 0; var8 < 3; ++var8) {
            if (this.fractionRules[var8] != null) {
               var7 = this.fractionRules[var8].doParse(var1, var2, false, var3);
               if (var2.getIndex() > var5.getIndex()) {
                  var6 = var7;
                  var5.setIndex(var2.getIndex());
               }

               var2.setIndex(0);
            }
         }

         for(var8 = this.rules.length - 1; var8 >= 0 && var5.getIndex() < var1.length(); --var8) {
            if (this.isFractionRuleSet || !((double)this.rules[var8].getBaseValue() >= var3)) {
               var7 = this.rules[var8].doParse(var1, var2, this.isFractionRuleSet, var3);
               if (var2.getIndex() > var5.getIndex()) {
                  var6 = var7;
                  var5.setIndex(var2.getIndex());
               }

               var2.setIndex(0);
            }
         }

         var2.setIndex(var5.getIndex());
         return (Number)var6;
      }
   }
}
