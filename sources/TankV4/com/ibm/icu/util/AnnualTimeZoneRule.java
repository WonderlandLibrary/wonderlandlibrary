package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.util.Date;

public class AnnualTimeZoneRule extends TimeZoneRule {
   private static final long serialVersionUID = -8870666707791230688L;
   public static final int MAX_YEAR = Integer.MAX_VALUE;
   private final DateTimeRule dateTimeRule;
   private final int startYear;
   private final int endYear;

   public AnnualTimeZoneRule(String var1, int var2, int var3, DateTimeRule var4, int var5, int var6) {
      super(var1, var2, var3);
      this.dateTimeRule = var4;
      this.startYear = var5;
      this.endYear = var6 > Integer.MAX_VALUE ? Integer.MAX_VALUE : var6;
   }

   public DateTimeRule getRule() {
      return this.dateTimeRule;
   }

   public int getStartYear() {
      return this.startYear;
   }

   public int getEndYear() {
      return this.endYear;
   }

   public Date getStartInYear(int var1, int var2, int var3) {
      if (var1 >= this.startYear && var1 <= this.endYear) {
         int var6 = this.dateTimeRule.getDateRuleType();
         long var4;
         if (var6 == 0) {
            var4 = Grego.fieldsToDay(var1, this.dateTimeRule.getRuleMonth(), this.dateTimeRule.getRuleDayOfMonth());
         } else {
            boolean var7 = true;
            int var8;
            int var9;
            if (var6 == 1) {
               var8 = this.dateTimeRule.getRuleWeekInMonth();
               if (var8 > 0) {
                  var4 = Grego.fieldsToDay(var1, this.dateTimeRule.getRuleMonth(), 1);
                  var4 += (long)(7 * (var8 - 1));
               } else {
                  var7 = false;
                  var4 = Grego.fieldsToDay(var1, this.dateTimeRule.getRuleMonth(), Grego.monthLength(var1, this.dateTimeRule.getRuleMonth()));
                  var4 += (long)(7 * (var8 + 1));
               }
            } else {
               var8 = this.dateTimeRule.getRuleMonth();
               var9 = this.dateTimeRule.getRuleDayOfMonth();
               if (var6 == 3) {
                  var7 = false;
                  if (var8 == 1 && var9 == 29 && !Grego.isLeapYear(var1)) {
                     --var9;
                  }
               }

               var4 = Grego.fieldsToDay(var1, var8, var9);
            }

            var8 = Grego.dayOfWeek(var4);
            var9 = this.dateTimeRule.getRuleDayOfWeek() - var8;
            if (var7) {
               var9 = var9 < 0 ? var9 + 7 : var9;
            } else {
               var9 = var9 > 0 ? var9 - 7 : var9;
            }

            var4 += (long)var9;
         }

         long var10 = var4 * 86400000L + (long)this.dateTimeRule.getRuleMillisInDay();
         if (this.dateTimeRule.getTimeRuleType() != 2) {
            var10 -= (long)var2;
         }

         if (this.dateTimeRule.getTimeRuleType() == 0) {
            var10 -= (long)var3;
         }

         return new Date(var10);
      } else {
         return null;
      }
   }

   public Date getFirstStart(int var1, int var2) {
      return this.getStartInYear(this.startYear, var1, var2);
   }

   public Date getFinalStart(int var1, int var2) {
      return this.endYear == Integer.MAX_VALUE ? null : this.getStartInYear(this.endYear, var1, var2);
   }

   public Date getNextStart(long var1, int var3, int var4, boolean var5) {
      int[] var6 = Grego.timeToFields(var1, (int[])null);
      int var7 = var6[0];
      if (var7 < this.startYear) {
         return this.getFirstStart(var3, var4);
      } else {
         Date var8 = this.getStartInYear(var7, var3, var4);
         if (var8 != null && (var8.getTime() < var1 || !var5 && var8.getTime() == var1)) {
            var8 = this.getStartInYear(var7 + 1, var3, var4);
         }

         return var8;
      }
   }

   public Date getPreviousStart(long var1, int var3, int var4, boolean var5) {
      int[] var6 = Grego.timeToFields(var1, (int[])null);
      int var7 = var6[0];
      if (var7 > this.endYear) {
         return this.getFinalStart(var3, var4);
      } else {
         Date var8 = this.getStartInYear(var7, var3, var4);
         if (var8 != null && (var8.getTime() > var1 || !var5 && var8.getTime() == var1)) {
            var8 = this.getStartInYear(var7 - 1, var3, var4);
         }

         return var8;
      }
   }

   public boolean isEquivalentTo(TimeZoneRule var1) {
      if (!(var1 instanceof AnnualTimeZoneRule)) {
         return false;
      } else {
         AnnualTimeZoneRule var2 = (AnnualTimeZoneRule)var1;
         return this.startYear == var2.startYear && this.endYear == var2.endYear && this.dateTimeRule.equals(var2.dateTimeRule) ? super.isEquivalentTo(var1) : false;
      }
   }

   public boolean isTransitionRule() {
      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(", rule={" + this.dateTimeRule + "}");
      var1.append(", startYear=" + this.startYear);
      var1.append(", endYear=");
      if (this.endYear == Integer.MAX_VALUE) {
         var1.append("max");
      } else {
         var1.append(this.endYear);
      }

      return var1.toString();
   }
}
