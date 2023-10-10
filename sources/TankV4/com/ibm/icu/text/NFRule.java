package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import java.text.ParsePosition;

final class NFRule {
   public static final int NEGATIVE_NUMBER_RULE = -1;
   public static final int IMPROPER_FRACTION_RULE = -2;
   public static final int PROPER_FRACTION_RULE = -3;
   public static final int MASTER_RULE = -4;
   private long baseValue;
   private int radix = 10;
   private short exponent = 0;
   private String ruleText = null;
   private NFSubstitution sub1 = null;
   private NFSubstitution sub2 = null;
   private RuleBasedNumberFormat formatter = null;
   static final boolean $assertionsDisabled = !NFRule.class.desiredAssertionStatus();

   public static Object makeRules(String var0, NFRuleSet var1, NFRule var2, RuleBasedNumberFormat var3) {
      NFRule var4 = new NFRule(var3);
      var0 = var4.parseRuleDescriptor(var0);
      int var5 = var0.indexOf("[");
      int var6 = var0.indexOf("]");
      if (var5 != -1 && var6 != -1 && var5 <= var6 && var4.getBaseValue() != -3L && var4.getBaseValue() != -1L) {
         NFRule var7 = null;
         StringBuilder var8 = new StringBuilder();
         if (var4.baseValue > 0L && (double)var4.baseValue % Math.pow((double)var4.radix, (double)var4.exponent) == 0.0D || var4.baseValue == -2L || var4.baseValue == -4L) {
            var7 = new NFRule(var3);
            if (var4.baseValue >= 0L) {
               var7.baseValue = var4.baseValue;
               if (!var1.isFractionSet()) {
                  ++var4.baseValue;
               }
            } else if (var4.baseValue == -2L) {
               var7.baseValue = -3L;
            } else if (var4.baseValue == -4L) {
               var7.baseValue = var4.baseValue;
               var4.baseValue = -2L;
            }

            var7.radix = var4.radix;
            var7.exponent = var4.exponent;
            var8.append(var0.substring(0, var5));
            if (var6 + 1 < var0.length()) {
               var8.append(var0.substring(var6 + 1));
            }

            var7.ruleText = var8.toString();
            var7.extractSubstitutions(var1, var2, var3);
         }

         var8.setLength(0);
         var8.append(var0.substring(0, var5));
         var8.append(var0.substring(var5 + 1, var6));
         if (var6 + 1 < var0.length()) {
            var8.append(var0.substring(var6 + 1));
         }

         var4.ruleText = var8.toString();
         var4.extractSubstitutions(var1, var2, var3);
         return var7 == null ? var4 : new NFRule[]{var7, var4};
      } else {
         var4.ruleText = var0;
         var4.extractSubstitutions(var1, var2, var3);
         return var4;
      }
   }

   public NFRule(RuleBasedNumberFormat var1) {
      this.formatter = var1;
   }

   private String parseRuleDescriptor(String var1) {
      int var3 = var1.indexOf(":");
      if (var3 == -1) {
         this.setBaseValue(0L);
      } else {
         String var2 = var1.substring(0, var3);
         ++var3;

         while(var3 < var1.length() && PatternProps.isWhiteSpace(var1.charAt(var3))) {
            ++var3;
         }

         var1 = var1.substring(var3);
         if (var2.equals("-x")) {
            this.setBaseValue(-1L);
         } else if (var2.equals("x.x")) {
            this.setBaseValue(-2L);
         } else if (var2.equals("0.x")) {
            this.setBaseValue(-3L);
         } else if (var2.equals("x.0")) {
            this.setBaseValue(-4L);
         } else if (var2.charAt(0) >= '0' && var2.charAt(0) <= '9') {
            StringBuilder var4 = new StringBuilder();
            var3 = 0;

            char var5;
            for(var5 = ' '; var3 < var2.length(); ++var3) {
               var5 = var2.charAt(var3);
               if (var5 >= '0' && var5 <= '9') {
                  var4.append(var5);
               } else {
                  if (var5 == '/' || var5 == '>') {
                     break;
                  }

                  if (!PatternProps.isWhiteSpace(var5) && var5 != ',' && var5 != '.') {
                     throw new IllegalArgumentException("Illegal character in rule descriptor");
                  }
               }
            }

            this.setBaseValue(Long.parseLong(var4.toString()));
            if (var5 == '/') {
               var4.setLength(0);
               ++var3;

               for(; var3 < var2.length(); ++var3) {
                  var5 = var2.charAt(var3);
                  if (var5 >= '0' && var5 <= '9') {
                     var4.append(var5);
                  } else {
                     if (var5 == '>') {
                        break;
                     }

                     if (!PatternProps.isWhiteSpace(var5) && var5 != ',' && var5 != '.') {
                        throw new IllegalArgumentException("Illegal character is rule descriptor");
                     }
                  }
               }

               this.radix = Integer.parseInt(var4.toString());
               if (this.radix == 0) {
                  throw new IllegalArgumentException("Rule can't have radix of 0");
               }

               this.exponent = this.expectedExponent();
            }

            if (var5 == '>') {
               while(var3 < var2.length()) {
                  var5 = var2.charAt(var3);
                  if (var5 != '>' || this.exponent <= 0) {
                     throw new IllegalArgumentException("Illegal character in rule descriptor");
                  }

                  --this.exponent;
                  ++var3;
               }
            }
         }
      }

      if (var1.length() > 0 && var1.charAt(0) == '\'') {
         var1 = var1.substring(1);
      }

      return var1;
   }

   private void extractSubstitutions(NFRuleSet var1, NFRule var2, RuleBasedNumberFormat var3) {
      this.sub1 = this.extractSubstitution(var1, var2, var3);
      this.sub2 = this.extractSubstitution(var1, var2, var3);
   }

   private NFSubstitution extractSubstitution(NFRuleSet var1, NFRule var2, RuleBasedNumberFormat var3) {
      NFSubstitution var4 = null;
      int var5 = this.indexOfAny(new String[]{"<<", "<%", "<#", "<0", ">>", ">%", ">#", ">0", "=%", "=#", "=0"});
      if (var5 == -1) {
         return NFSubstitution.makeSubstitution(this.ruleText.length(), this, var2, var1, var3, "");
      } else {
         int var6;
         if (this.ruleText.substring(var5).startsWith(">>>")) {
            var6 = var5 + 2;
         } else {
            char var7 = this.ruleText.charAt(var5);
            var6 = this.ruleText.indexOf(var7, var5 + 1);
            if (var7 == '<' && var6 != -1 && var6 < this.ruleText.length() - 1 && this.ruleText.charAt(var6 + 1) == var7) {
               ++var6;
            }
         }

         if (var6 == -1) {
            return NFSubstitution.makeSubstitution(this.ruleText.length(), this, var2, var1, var3, "");
         } else {
            var4 = NFSubstitution.makeSubstitution(var5, this, var2, var1, var3, this.ruleText.substring(var5, var6 + 1));
            this.ruleText = this.ruleText.substring(0, var5) + this.ruleText.substring(var6 + 1);
            return var4;
         }
      }
   }

   public final void setBaseValue(long var1) {
      this.baseValue = var1;
      if (this.baseValue >= 1L) {
         this.radix = 10;
         this.exponent = this.expectedExponent();
         if (this.sub1 != null) {
            this.sub1.setDivisor(this.radix, this.exponent);
         }

         if (this.sub2 != null) {
            this.sub2.setDivisor(this.radix, this.exponent);
         }
      } else {
         this.radix = 10;
         this.exponent = 0;
      }

   }

   private short expectedExponent() {
      if (this.radix != 0 && this.baseValue >= 1L) {
         short var1 = (short)((int)(Math.log((double)this.baseValue) / Math.log((double)this.radix)));
         return Math.pow((double)this.radix, (double)(var1 + 1)) <= (double)this.baseValue ? (short)(var1 + 1) : var1;
      } else {
         return 0;
      }
   }

   private int indexOfAny(String[] var1) {
      int var3 = -1;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         int var2 = this.ruleText.indexOf(var1[var4]);
         if (var2 != -1 && (var3 == -1 || var2 < var3)) {
            var3 = var2;
         }
      }

      return var3;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof NFRule)) {
         return false;
      } else {
         NFRule var2 = (NFRule)var1;
         return this.baseValue == var2.baseValue && this.radix == var2.radix && this.exponent == var2.exponent && this.ruleText.equals(var2.ruleText) && this.sub1.equals(var2.sub1) && this.sub2.equals(var2.sub2);
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
      if (this.baseValue == -1L) {
         var1.append("-x: ");
      } else if (this.baseValue == -2L) {
         var1.append("x.x: ");
      } else if (this.baseValue == -3L) {
         var1.append("0.x: ");
      } else if (this.baseValue == -4L) {
         var1.append("x.0: ");
      } else {
         var1.append(String.valueOf(this.baseValue));
         if (this.radix != 10) {
            var1.append('/');
            var1.append(String.valueOf(this.radix));
         }

         int var2 = this.expectedExponent() - this.exponent;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1.append('>');
         }

         var1.append(": ");
      }

      if (this.ruleText.startsWith(" ") && (this.sub1 == null || this.sub1.getPos() != 0)) {
         var1.append("'");
      }

      StringBuilder var4 = new StringBuilder(this.ruleText);
      var4.insert(this.sub2.getPos(), this.sub2.toString());
      var4.insert(this.sub1.getPos(), this.sub1.toString());
      var1.append(var4.toString());
      var1.append(';');
      return var1.toString();
   }

   public final long getBaseValue() {
      return this.baseValue;
   }

   public double getDivisor() {
      return Math.pow((double)this.radix, (double)this.exponent);
   }

   public void doFormat(long var1, StringBuffer var3, int var4) {
      var3.insert(var4, this.ruleText);
      this.sub2.doSubstitution(var1, var3, var4);
      this.sub1.doSubstitution(var1, var3, var4);
   }

   public void doFormat(double var1, StringBuffer var3, int var4) {
      var3.insert(var4, this.ruleText);
      this.sub2.doSubstitution(var1, var3, var4);
      this.sub1.doSubstitution(var1, var3, var4);
   }

   public boolean shouldRollBack(double var1) {
      if (!this.sub1.isModulusSubstitution() && !this.sub2.isModulusSubstitution()) {
         return false;
      } else {
         return var1 % Math.pow((double)this.radix, (double)this.exponent) == 0.0D && (double)this.baseValue % Math.pow((double)this.radix, (double)this.exponent) != 0.0D;
      }
   }

   public Number doParse(String var1, ParsePosition var2, boolean var3, double var4) {
      ParsePosition var6 = new ParsePosition(0);
      String var7 = this.stripPrefix(var1, this.ruleText.substring(0, this.sub1.getPos()), var6);
      int var8 = var1.length() - var7.length();
      if (var6.getIndex() == 0 && this.sub1.getPos() != 0) {
         return 0L;
      } else {
         int var9 = 0;
         double var10 = 0.0D;
         int var12 = 0;
         double var13 = (double)Math.max(0L, this.baseValue);

         do {
            var6.setIndex(0);
            double var15 = this.matchToDelimiter(var7, var12, var13, this.ruleText.substring(this.sub1.getPos(), this.sub2.getPos()), var6, this.sub1, var4).doubleValue();
            if (var6.getIndex() != 0 || this.sub1.isNullSubstitution()) {
               var12 = var6.getIndex();
               String var17 = var7.substring(var6.getIndex());
               ParsePosition var18 = new ParsePosition(0);
               var15 = this.matchToDelimiter(var17, 0, var15, this.ruleText.substring(this.sub2.getPos()), var18, this.sub2, var4).doubleValue();
               if ((var18.getIndex() != 0 || this.sub2.isNullSubstitution()) && var8 + var6.getIndex() + var18.getIndex() > var9) {
                  var9 = var8 + var6.getIndex() + var18.getIndex();
                  var10 = var15;
               }
            }
         } while(this.sub1.getPos() != this.sub2.getPos() && var6.getIndex() > 0 && var6.getIndex() < var7.length() && var6.getIndex() != var12);

         var2.setIndex(var9);
         if (var3 && var9 > 0 && this.sub1.isNullSubstitution()) {
            var10 = 1.0D / var10;
         }

         return (Number)(var10 == (double)((long)var10) ? (long)var10 : new Double(var10));
      }
   }

   private String stripPrefix(String var1, String var2, ParsePosition var3) {
      if (var2.length() == 0) {
         return var1;
      } else {
         int var4 = this.prefixLength(var1, var2);
         if (var4 != 0) {
            var3.setIndex(var3.getIndex() + var4);
            return var1.substring(var4);
         } else {
            return var1;
         }
      }
   }

   private Number matchToDelimiter(String var1, int var2, double var3, String var5, ParsePosition var6, NFSubstitution var7, double var8) {
      ParsePosition var10;
      if (var5 == false) {
         var10 = new ParsePosition(0);
         int[] var17 = this.findText(var1, var5, var2);
         int var13 = var17[0];

         for(int var14 = var17[1]; var13 >= 0; var14 = var17[1]) {
            String var15 = var1.substring(0, var13);
            if (var15.length() > 0) {
               Number var16 = var7.doParse(var15, var10, var3, var8, this.formatter.lenientParseEnabled());
               if (var10.getIndex() == var13) {
                  var6.setIndex(var13 + var14);
                  return var16;
               }
            }

            var10.setIndex(0);
            var17 = this.findText(var1, var5, var13 + var14);
            var13 = var17[0];
         }

         var6.setIndex(0);
         return 0L;
      } else {
         var10 = new ParsePosition(0);
         Object var11 = 0L;
         Number var12 = var7.doParse(var1, var10, var3, var8, this.formatter.lenientParseEnabled());
         if (var10.getIndex() != 0 || var7.isNullSubstitution()) {
            var6.setIndex(var10.getIndex());
            if (var12 != null) {
               var11 = var12;
            }
         }

         return (Number)var11;
      }
   }

   private int prefixLength(String var1, String var2) {
      if (var2.length() == 0) {
         return 0;
      } else {
         RbnfLenientScanner var3 = this.formatter.getLenientScanner();
         if (var3 != null) {
            return var3.prefixLength(var1, var2);
         } else {
            return var1.startsWith(var2) ? var2.length() : 0;
         }
      }
   }

   private int[] findText(String var1, String var2, int var3) {
      RbnfLenientScanner var4 = this.formatter.getLenientScanner();
      return var4 == null ? new int[]{var1.indexOf(var2, var3), var2.length()} : var4.findText(var1, var2, var3);
   }
}
