package com.ibm.icu.util;

import java.util.Arrays;
import java.util.Date;

public class TimeArrayTimeZoneRule extends TimeZoneRule {
   private static final long serialVersionUID = -1117109130077415245L;
   private final long[] startTimes;
   private final int timeType;

   public TimeArrayTimeZoneRule(String var1, int var2, int var3, long[] var4, int var5) {
      super(var1, var2, var3);
      if (var4 != null && var4.length != 0) {
         this.startTimes = (long[])var4.clone();
         Arrays.sort(this.startTimes);
         this.timeType = var5;
      } else {
         throw new IllegalArgumentException("No start times are specified.");
      }
   }

   public long[] getStartTimes() {
      return (long[])this.startTimes.clone();
   }

   public int getTimeType() {
      return this.timeType;
   }

   public Date getFirstStart(int var1, int var2) {
      return new Date(this.getUTC(this.startTimes[0], var1, var2));
   }

   public Date getFinalStart(int var1, int var2) {
      return new Date(this.getUTC(this.startTimes[this.startTimes.length - 1], var1, var2));
   }

   public Date getNextStart(long var1, int var3, int var4, boolean var5) {
      int var6;
      for(var6 = this.startTimes.length - 1; var6 >= 0; --var6) {
         long var7 = this.getUTC(this.startTimes[var6], var3, var4);
         if (var7 < var1 || !var5 && var7 == var1) {
            break;
         }
      }

      return var6 == this.startTimes.length - 1 ? null : new Date(this.getUTC(this.startTimes[var6 + 1], var3, var4));
   }

   public Date getPreviousStart(long var1, int var3, int var4, boolean var5) {
      for(int var6 = this.startTimes.length - 1; var6 >= 0; --var6) {
         long var7 = this.getUTC(this.startTimes[var6], var3, var4);
         if (var7 < var1 || var5 && var7 == var1) {
            return new Date(var7);
         }
      }

      return null;
   }

   public boolean isEquivalentTo(TimeZoneRule var1) {
      if (!(var1 instanceof TimeArrayTimeZoneRule)) {
         return false;
      } else {
         return this.timeType == ((TimeArrayTimeZoneRule)var1).timeType && Arrays.equals(this.startTimes, ((TimeArrayTimeZoneRule)var1).startTimes) ? super.isEquivalentTo(var1) : false;
      }
   }

   public boolean isTransitionRule() {
      return true;
   }

   private long getUTC(long var1, int var3, int var4) {
      if (this.timeType != 2) {
         var1 -= (long)var3;
      }

      if (this.timeType == 0) {
         var1 -= (long)var4;
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(", timeType=");
      var1.append(this.timeType);
      var1.append(", startTimes=[");

      for(int var2 = 0; var2 < this.startTimes.length; ++var2) {
         if (var2 != 0) {
            var1.append(", ");
         }

         var1.append(Long.toString(this.startTimes[var2]));
      }

      var1.append("]");
      return var1.toString();
   }
}
