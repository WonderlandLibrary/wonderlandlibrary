package com.ibm.icu.text;

import java.text.ParsePosition;

class NullSubstitution extends NFSubstitution {
   static final boolean $assertionsDisabled = !NullSubstitution.class.desiredAssertionStatus();

   NullSubstitution(int var1, NFRuleSet var2, RuleBasedNumberFormat var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   public String toString() {
      return "";
   }

   public void doSubstitution(long var1, StringBuffer var3, int var4) {
   }

   public void doSubstitution(double var1, StringBuffer var3, int var4) {
   }

   public long transformNumber(long var1) {
      return 0L;
   }

   public double transformNumber(double var1) {
      return 0.0D;
   }

   public Number doParse(String var1, ParsePosition var2, double var3, double var5, boolean var7) {
      return (Number)(var3 == (double)((long)var3) ? (long)var3 : new Double(var3));
   }

   public double composeRuleValue(double var1, double var3) {
      return 0.0D;
   }

   public double calcUpperBound(double var1) {
      return 0.0D;
   }

   public boolean isNullSubstitution() {
      return true;
   }

   char tokenChar() {
      return ' ';
   }
}
