package com.ibm.icu.util;

import java.util.Date;

public class InitialTimeZoneRule extends TimeZoneRule {
   private static final long serialVersionUID = 1876594993064051206L;

   public InitialTimeZoneRule(String var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public boolean isEquivalentTo(TimeZoneRule var1) {
      return var1 instanceof InitialTimeZoneRule ? super.isEquivalentTo(var1) : false;
   }

   public Date getFinalStart(int var1, int var2) {
      return null;
   }

   public Date getFirstStart(int var1, int var2) {
      return null;
   }

   public Date getNextStart(long var1, int var3, int var4, boolean var5) {
      return null;
   }

   public Date getPreviousStart(long var1, int var3, int var4, boolean var5) {
      return null;
   }

   public boolean isTransitionRule() {
      return false;
   }
}
