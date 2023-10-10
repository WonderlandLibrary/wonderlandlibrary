package com.ibm.icu.text;

import java.text.ParsePosition;

class FractionalPartSubstitution extends NFSubstitution {
   private boolean byDigits = false;
   private boolean useSpaces = true;

   FractionalPartSubstitution(int var1, NFRuleSet var2, RuleBasedNumberFormat var3, String var4) {
      super(var1, var2, var3, var4);
      if (!var4.equals(">>") && !var4.equals(">>>") && var2 != this.ruleSet) {
         this.ruleSet.makeIntoFractionRuleSet();
      } else {
         this.byDigits = true;
         if (var4.equals(">>>")) {
            this.useSpaces = false;
         }
      }

   }

   public void doSubstitution(double var1, StringBuffer var3, int var4) {
      if (!this.byDigits) {
         super.doSubstitution(var1, var3, var4);
      } else {
         DigitList var5 = new DigitList();
         var5.set(var1, 20, true);

         boolean var6;
         for(var6 = false; var5.count > Math.max(0, var5.decimalAt); this.ruleSet.format((long)(var5.digits[--var5.count] - 48), var3, var4 + this.pos)) {
            if (var6 && this.useSpaces) {
               var3.insert(var4 + this.pos, ' ');
            } else {
               var6 = true;
            }
         }

         while(var5.decimalAt < 0) {
            if (var6 && this.useSpaces) {
               var3.insert(var4 + this.pos, ' ');
            } else {
               var6 = true;
            }

            this.ruleSet.format(0L, var3, var4 + this.pos);
            ++var5.decimalAt;
         }
      }

   }

   public long transformNumber(long var1) {
      return 0L;
   }

   public double transformNumber(double var1) {
      return var1 - Math.floor(var1);
   }

   public Number doParse(String var1, ParsePosition var2, double var3, double var5, boolean var7) {
      if (!this.byDigits) {
         return super.doParse(var1, var2, var3, 0.0D, var7);
      } else {
         String var8 = var1;
         ParsePosition var9 = new ParsePosition(1);
         double var10 = 0.0D;
         DigitList var13 = new DigitList();

         while(var8.length() > 0 && var9.getIndex() != 0) {
            var9.setIndex(0);
            int var12 = this.ruleSet.parse(var8, var9, 10.0D).intValue();
            if (var7 && var9.getIndex() == 0) {
               Number var14 = this.rbnf.getDecimalFormat().parse(var8, var9);
               if (var14 != null) {
                  var12 = var14.intValue();
               }
            }

            if (var9.getIndex() != 0) {
               var13.append(48 + var12);
               var2.setIndex(var2.getIndex() + var9.getIndex());
               var8 = var8.substring(var9.getIndex());

               while(var8.length() > 0 && var8.charAt(0) == ' ') {
                  var8 = var8.substring(1);
                  var2.setIndex(var2.getIndex() + 1);
               }
            }
         }

         var10 = var13.count == 0 ? 0.0D : var13.getDouble();
         var10 = this.composeRuleValue(var10, var3);
         return new Double(var10);
      }
   }

   public double composeRuleValue(double var1, double var3) {
      return var1 + var3;
   }

   public double calcUpperBound(double var1) {
      return 0.0D;
   }

   char tokenChar() {
      return '>';
   }
}
