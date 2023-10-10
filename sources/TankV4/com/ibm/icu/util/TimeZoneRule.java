package com.ibm.icu.util;

import java.io.Serializable;
import java.util.Date;

public abstract class TimeZoneRule implements Serializable {
   private static final long serialVersionUID = 6374143828553768100L;
   private final String name;
   private final int rawOffset;
   private final int dstSavings;

   public TimeZoneRule(String var1, int var2, int var3) {
      this.name = var1;
      this.rawOffset = var2;
      this.dstSavings = var3;
   }

   public String getName() {
      return this.name;
   }

   public int getRawOffset() {
      return this.rawOffset;
   }

   public int getDSTSavings() {
      return this.dstSavings;
   }

   public boolean isEquivalentTo(TimeZoneRule var1) {
      return this.rawOffset == var1.rawOffset && this.dstSavings == var1.dstSavings;
   }

   public abstract Date getFirstStart(int var1, int var2);

   public abstract Date getFinalStart(int var1, int var2);

   public abstract Date getNextStart(long var1, int var3, int var4, boolean var5);

   public abstract Date getPreviousStart(long var1, int var3, int var4, boolean var5);

   public abstract boolean isTransitionRule();

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("name=" + this.name);
      var1.append(", stdOffset=" + this.rawOffset);
      var1.append(", dstSaving=" + this.dstSavings);
      return var1.toString();
   }
}
