package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

public class SimpleTimeZone extends BasicTimeZone {
   private static final long serialVersionUID = -7034676239311322769L;
   public static final int WALL_TIME = 0;
   public static final int STANDARD_TIME = 1;
   public static final int UTC_TIME = 2;
   private static final byte[] staticMonthLength = new byte[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   private static final int DOM_MODE = 1;
   private static final int DOW_IN_MONTH_MODE = 2;
   private static final int DOW_GE_DOM_MODE = 3;
   private static final int DOW_LE_DOM_MODE = 4;
   private int raw;
   private int dst = 3600000;
   private STZInfo xinfo = null;
   private int startMonth;
   private int startDay;
   private int startDayOfWeek;
   private int startTime;
   private int startTimeMode;
   private int endTimeMode;
   private int endMonth;
   private int endDay;
   private int endDayOfWeek;
   private int endTime;
   private int startYear;
   private boolean useDaylight;
   private int startMode;
   private int endMode;
   private transient boolean transitionRulesInitialized;
   private transient InitialTimeZoneRule initialRule;
   private transient TimeZoneTransition firstTransition;
   private transient AnnualTimeZoneRule stdRule;
   private transient AnnualTimeZoneRule dstRule;
   private transient boolean isFrozen = false;
   static final boolean $assertionsDisabled = !SimpleTimeZone.class.desiredAssertionStatus();

   public SimpleTimeZone(int var1, String var2) {
      super(var2);
      this.construct(var1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3600000);
   }

   public SimpleTimeZone(int var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      super(var2);
      this.construct(var1, var3, var4, var5, var6, 0, var7, var8, var9, var10, 0, 3600000);
   }

   public SimpleTimeZone(int var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      super(var2);
      this.construct(var1, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
   }

   public SimpleTimeZone(int var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      super(var2);
      this.construct(var1, var3, var4, var5, var6, 0, var7, var8, var9, var10, 0, var11);
   }

   public void setID(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         super.setID(var1);
         this.transitionRulesInitialized = false;
      }
   }

   public void setRawOffset(int var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.raw = var1;
         this.transitionRulesInitialized = false;
      }
   }

   public int getRawOffset() {
      return this.raw;
   }

   public void setStartYear(int var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().sy = var1;
         this.startYear = var1;
         this.transitionRulesInitialized = false;
      }
   }

   public void setStartRule(int var1, int var2, int var3, int var4) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().setStart(var1, var2, var3, var4, -1, false);
         this.setStartRule(var1, var2, var3, var4, 0);
      }
   }

   private void setStartRule(int var1, int var2, int var3, int var4, int var5) {
      if (!$assertionsDisabled && this.isFrozen()) {
         throw new AssertionError();
      } else {
         this.startMonth = var1;
         this.startDay = var2;
         this.startDayOfWeek = var3;
         this.startTime = var4;
         this.startTimeMode = var5;
         this.decodeStartRule();
         this.transitionRulesInitialized = false;
      }
   }

   public void setStartRule(int var1, int var2, int var3) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().setStart(var1, -1, -1, var3, var2, false);
         this.setStartRule(var1, var2, 0, var3, 0);
      }
   }

   public void setStartRule(int var1, int var2, int var3, int var4, boolean var5) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().setStart(var1, -1, var3, var4, var2, var5);
         this.setStartRule(var1, var5 ? var2 : -var2, -var3, var4, 0);
      }
   }

   public void setEndRule(int var1, int var2, int var3, int var4) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().setEnd(var1, var2, var3, var4, -1, false);
         this.setEndRule(var1, var2, var3, var4, 0);
      }
   }

   public void setEndRule(int var1, int var2, int var3) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().setEnd(var1, -1, -1, var3, var2, false);
         this.setEndRule(var1, var2, 0, var3);
      }
   }

   public void setEndRule(int var1, int var2, int var3, int var4, boolean var5) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else {
         this.getSTZInfo().setEnd(var1, -1, var3, var4, var2, var5);
         this.setEndRule(var1, var2, var3, var4, 0, var5);
      }
   }

   private void setEndRule(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (!$assertionsDisabled && this.isFrozen()) {
         throw new AssertionError();
      } else {
         this.setEndRule(var1, var6 ? var2 : -var2, -var3, var4, var5);
      }
   }

   private void setEndRule(int var1, int var2, int var3, int var4, int var5) {
      if (!$assertionsDisabled && this.isFrozen()) {
         throw new AssertionError();
      } else {
         this.endMonth = var1;
         this.endDay = var2;
         this.endDayOfWeek = var3;
         this.endTime = var4;
         this.endTimeMode = var5;
         this.decodeEndRule();
         this.transitionRulesInitialized = false;
      }
   }

   public void setDSTSavings(int var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen SimpleTimeZone instance.");
      } else if (var1 <= 0) {
         throw new IllegalArgumentException();
      } else {
         this.dst = var1;
         this.transitionRulesInitialized = false;
      }
   }

   public int getDSTSavings() {
      return this.dst;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.xinfo != null) {
         this.xinfo.applyTo(this);
      }

   }

   public String toString() {
      return "SimpleTimeZone: " + this.getID();
   }

   private STZInfo getSTZInfo() {
      if (this.xinfo == null) {
         this.xinfo = new STZInfo();
      }

      return this.xinfo;
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var3 >= 0 && var3 <= 11) {
         return this.getOffset(var1, var2, var3, var4, var5, var6, Grego.monthLength(var2, var3));
      } else {
         throw new IllegalArgumentException();
      }
   }

   /** @deprecated */
   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var3 >= 0 && var3 <= 11) {
         return this.getOffset(var1, var2, var3, var4, var5, var6, Grego.monthLength(var2, var3), Grego.previousMonthLength(var2, var3));
      } else {
         throw new IllegalArgumentException();
      }
   }

   private int getOffset(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if ((var1 == 1 || var1 == 0) && var3 >= 0 && var3 <= 11 && var4 >= 1 && var4 <= var7 && var5 >= 1 && var5 <= 7 && var6 >= 0 && var6 < 86400000 && var7 >= 28 && var7 <= 31 && var8 >= 28 && var8 <= 31) {
         int var9 = this.raw;
         if (this.useDaylight && var2 >= this.startYear && var1 == 1) {
            boolean var10 = this.startMonth > this.endMonth;
            int var11 = this.compareToRule(var3, var7, var8, var4, var5, var6, this.startTimeMode == 2 ? -this.raw : 0, this.startMode, this.startMonth, this.startDayOfWeek, this.startDay, this.startTime);
            int var12 = 0;
            if (var10 != var11 >= 0) {
               var12 = this.compareToRule(var3, var7, var8, var4, var5, var6, this.endTimeMode == 0 ? this.dst : (this.endTimeMode == 2 ? -this.raw : 0), this.endMode, this.endMonth, this.endDayOfWeek, this.endDay, this.endTime);
            }

            if (!var10 && var11 >= 0 && var12 < 0 || var10 && (var11 >= 0 || var12 < 0)) {
               var9 += this.dst;
            }

            return var9;
         } else {
            return var9;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   /** @deprecated */
   public void getOffsetFromLocal(long var1, int var3, int var4, int[] var5) {
      var5[0] = this.getRawOffset();
      int[] var6 = new int[6];
      Grego.timeToFields(var1, var6);
      var5[1] = this.getOffset(1, var6[0], var6[1], var6[2], var6[3], var6[5]) - var5[0];
      boolean var7 = false;
      if (var5[1] > 0) {
         if ((var3 & 3) == 1 || (var3 & 3) != 3 && (var3 & 12) != 12) {
            var1 -= (long)this.getDSTSavings();
            var7 = true;
         }
      } else if ((var4 & 3) == 3 || (var4 & 3) != 1 && (var4 & 12) == 4) {
         var1 -= (long)this.getDSTSavings();
         var7 = true;
      }

      if (var7) {
         Grego.timeToFields(var1, var6);
         var5[1] = this.getOffset(1, var6[0], var6[1], var6[2], var6[3], var6[5]) - var5[0];
      }

   }

   private int compareToRule(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      var6 += var7;

      while(var6 >= 86400000) {
         var6 -= 86400000;
         ++var4;
         var5 = 1 + var5 % 7;
         if (var4 > var2) {
            var4 = 1;
            ++var1;
         }
      }

      for(; var6 < 0; var6 += 86400000) {
         --var4;
         var5 = 1 + (var5 + 5) % 7;
         if (var4 < 1) {
            var4 = var3;
            --var1;
         }
      }

      if (var1 < var9) {
         return -1;
      } else if (var1 > var9) {
         return 1;
      } else {
         int var13 = 0;
         if (var11 > var2) {
            var11 = var2;
         }

         switch(var8) {
         case 1:
            var13 = var11;
            break;
         case 2:
            if (var11 > 0) {
               var13 = 1 + (var11 - 1) * 7 + (7 + var10 - (var5 - var4 + 1)) % 7;
            } else {
               var13 = var2 + (var11 + 1) * 7 - (7 + (var5 + var2 - var4) - var10) % 7;
            }
            break;
         case 3:
            var13 = var11 + (49 + var10 - var11 - var5 + var4) % 7;
            break;
         case 4:
            var13 = var11 - (49 - var10 + var11 + var5 - var4) % 7;
         }

         if (var4 < var13) {
            return -1;
         } else if (var4 > var13) {
            return 1;
         } else if (var6 < var12) {
            return -1;
         } else if (var6 > var12) {
            return 1;
         } else {
            return 0;
         }
      }
   }

   public boolean useDaylightTime() {
      return this.useDaylight;
   }

   public boolean observesDaylightTime() {
      return this.useDaylight;
   }

   public boolean inDaylightTime(Date var1) {
      GregorianCalendar var2 = new GregorianCalendar(this);
      var2.setTime(var1);
      return var2.inDaylightTime();
   }

   private void construct(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      this.raw = var1;
      this.startMonth = var2;
      this.startDay = var3;
      this.startDayOfWeek = var4;
      this.startTime = var5;
      this.startTimeMode = var6;
      this.endMonth = var7;
      this.endDay = var8;
      this.endDayOfWeek = var9;
      this.endTime = var10;
      this.endTimeMode = var11;
      this.dst = var12;
      this.startYear = 0;
      this.startMode = 1;
      this.endMode = 1;
      this.decodeRules();
      if (var12 <= 0) {
         throw new IllegalArgumentException();
      }
   }

   private void decodeRules() {
      this.decodeStartRule();
      this.decodeEndRule();
   }

   private void decodeStartRule() {
      this.useDaylight = this.startDay != 0 && this.endDay != 0;
      if (this.useDaylight && this.dst == 0) {
         this.dst = 86400000;
      }

      if (this.startDay != 0) {
         if (this.startMonth < 0 || this.startMonth > 11) {
            throw new IllegalArgumentException();
         }

         if (this.startTime < 0 || this.startTime > 86400000 || this.startTimeMode < 0 || this.startTimeMode > 2) {
            throw new IllegalArgumentException();
         }

         if (this.startDayOfWeek == 0) {
            this.startMode = 1;
         } else {
            if (this.startDayOfWeek > 0) {
               this.startMode = 2;
            } else {
               this.startDayOfWeek = -this.startDayOfWeek;
               if (this.startDay > 0) {
                  this.startMode = 3;
               } else {
                  this.startDay = -this.startDay;
                  this.startMode = 4;
               }
            }

            if (this.startDayOfWeek > 7) {
               throw new IllegalArgumentException();
            }
         }

         if (this.startMode == 2) {
            if (this.startDay < -5 || this.startDay > 5) {
               throw new IllegalArgumentException();
            }
         } else if (this.startDay < 1 || this.startDay > staticMonthLength[this.startMonth]) {
            throw new IllegalArgumentException();
         }
      }

   }

   private void decodeEndRule() {
      this.useDaylight = this.startDay != 0 && this.endDay != 0;
      if (this.useDaylight && this.dst == 0) {
         this.dst = 86400000;
      }

      if (this.endDay != 0) {
         if (this.endMonth < 0 || this.endMonth > 11) {
            throw new IllegalArgumentException();
         }

         if (this.endTime < 0 || this.endTime > 86400000 || this.endTimeMode < 0 || this.endTimeMode > 2) {
            throw new IllegalArgumentException();
         }

         if (this.endDayOfWeek == 0) {
            this.endMode = 1;
         } else {
            if (this.endDayOfWeek > 0) {
               this.endMode = 2;
            } else {
               this.endDayOfWeek = -this.endDayOfWeek;
               if (this.endDay > 0) {
                  this.endMode = 3;
               } else {
                  this.endDay = -this.endDay;
                  this.endMode = 4;
               }
            }

            if (this.endDayOfWeek > 7) {
               throw new IllegalArgumentException();
            }
         }

         if (this.endMode == 2) {
            if (this.endDay < -5 || this.endDay > 5) {
               throw new IllegalArgumentException();
            }
         } else if (this.endDay < 1 || this.endDay > staticMonthLength[this.endMonth]) {
            throw new IllegalArgumentException();
         }
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         SimpleTimeZone var2 = (SimpleTimeZone)var1;
         SimpleTimeZone var10000;
         if (this.raw == var2.raw && this.useDaylight == var2.useDaylight) {
            var10000 = this;
            this.getID();
            if (var2.getID() == null && (!this.useDaylight || this.dst == var2.dst && this.startMode == var2.startMode && this.startMonth == var2.startMonth && this.startDay == var2.startDay && this.startDayOfWeek == var2.startDayOfWeek && this.startTime == var2.startTime && this.startTimeMode == var2.startTimeMode && this.endMode == var2.endMode && this.endMonth == var2.endMonth && this.endDay == var2.endDay && this.endDayOfWeek == var2.endDayOfWeek && this.endTime == var2.endTime && this.endTimeMode == var2.endTimeMode && this.startYear == var2.startYear)) {
               boolean var10002 = true;
               return (boolean)var10000;
            }
         }

         var10000 = false;
         return (boolean)var10000;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = super.hashCode() + this.raw ^ (this.raw >>> 8) + (this.useDaylight ? 0 : 1);
      if (!this.useDaylight) {
         var1 += this.dst ^ (this.dst >>> 10) + this.startMode ^ (this.startMode >>> 11) + this.startMonth ^ (this.startMonth >>> 12) + this.startDay ^ (this.startDay >>> 13) + this.startDayOfWeek ^ (this.startDayOfWeek >>> 14) + this.startTime ^ (this.startTime >>> 15) + this.startTimeMode ^ (this.startTimeMode >>> 16) + this.endMode ^ (this.endMode >>> 17) + this.endMonth ^ (this.endMonth >>> 18) + this.endDay ^ (this.endDay >>> 19) + this.endDayOfWeek ^ (this.endDayOfWeek >>> 20) + this.endTime ^ (this.endTime >>> 21) + this.endTimeMode ^ (this.endTimeMode >>> 22) + this.startYear ^ this.startYear >>> 23;
      }

      return var1;
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   public boolean hasSameRules(TimeZone var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof SimpleTimeZone)) {
         return false;
      } else {
         SimpleTimeZone var2 = (SimpleTimeZone)var1;
         return var2 != null && this.raw == var2.raw && this.useDaylight == var2.useDaylight && (!this.useDaylight || this.dst == var2.dst && this.startMode == var2.startMode && this.startMonth == var2.startMonth && this.startDay == var2.startDay && this.startDayOfWeek == var2.startDayOfWeek && this.startTime == var2.startTime && this.startTimeMode == var2.startTimeMode && this.endMode == var2.endMode && this.endMonth == var2.endMonth && this.endDay == var2.endDay && this.endDayOfWeek == var2.endDayOfWeek && this.endTime == var2.endTime && this.endTimeMode == var2.endTimeMode && this.startYear == var2.startYear);
      }
   }

   public TimeZoneTransition getNextTransition(long var1, boolean var3) {
      if (!this.useDaylight) {
         return null;
      } else {
         this.initTransitionRules();
         long var4 = this.firstTransition.getTime();
         if (var1 < var4 || var3 && var1 == var4) {
            return this.firstTransition;
         } else {
            Date var6 = this.stdRule.getNextStart(var1, this.dstRule.getRawOffset(), this.dstRule.getDSTSavings(), var3);
            Date var7 = this.dstRule.getNextStart(var1, this.stdRule.getRawOffset(), this.stdRule.getDSTSavings(), var3);
            if (var6 == null || var7 != null && !var6.before(var7)) {
               return var7 == null || var6 != null && !var7.before(var6) ? null : new TimeZoneTransition(var7.getTime(), this.stdRule, this.dstRule);
            } else {
               return new TimeZoneTransition(var6.getTime(), this.dstRule, this.stdRule);
            }
         }
      }
   }

   public TimeZoneTransition getPreviousTransition(long var1, boolean var3) {
      if (!this.useDaylight) {
         return null;
      } else {
         this.initTransitionRules();
         long var4 = this.firstTransition.getTime();
         if (var1 < var4 || !var3 && var1 == var4) {
            return null;
         } else {
            Date var6 = this.stdRule.getPreviousStart(var1, this.dstRule.getRawOffset(), this.dstRule.getDSTSavings(), var3);
            Date var7 = this.dstRule.getPreviousStart(var1, this.stdRule.getRawOffset(), this.stdRule.getDSTSavings(), var3);
            if (var6 == null || var7 != null && !var6.after(var7)) {
               return var7 == null || var6 != null && !var7.after(var6) ? null : new TimeZoneTransition(var7.getTime(), this.stdRule, this.dstRule);
            } else {
               return new TimeZoneTransition(var6.getTime(), this.dstRule, this.stdRule);
            }
         }
      }
   }

   public TimeZoneRule[] getTimeZoneRules() {
      this.initTransitionRules();
      int var1 = this.useDaylight ? 3 : 1;
      TimeZoneRule[] var2 = new TimeZoneRule[var1];
      var2[0] = this.initialRule;
      if (this.useDaylight) {
         var2[1] = this.stdRule;
         var2[2] = this.dstRule;
      }

      return var2;
   }

   private synchronized void initTransitionRules() {
      if (!this.transitionRulesInitialized) {
         if (this.useDaylight) {
            DateTimeRule var1 = null;
            int var2 = this.startTimeMode == 1 ? 1 : (this.startTimeMode == 2 ? 2 : 0);
            switch(this.startMode) {
            case 1:
               var1 = new DateTimeRule(this.startMonth, this.startDay, this.startTime, var2);
               break;
            case 2:
               var1 = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, this.startTime, var2);
               break;
            case 3:
               var1 = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, true, this.startTime, var2);
               break;
            case 4:
               var1 = new DateTimeRule(this.startMonth, this.startDay, this.startDayOfWeek, false, this.startTime, var2);
            }

            this.dstRule = new AnnualTimeZoneRule(this.getID() + "(DST)", this.getRawOffset(), this.getDSTSavings(), var1, this.startYear, Integer.MAX_VALUE);
            long var5 = this.dstRule.getFirstStart(this.getRawOffset(), 0).getTime();
            var2 = this.endTimeMode == 1 ? 1 : (this.endTimeMode == 2 ? 2 : 0);
            switch(this.endMode) {
            case 1:
               var1 = new DateTimeRule(this.endMonth, this.endDay, this.endTime, var2);
               break;
            case 2:
               var1 = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, this.endTime, var2);
               break;
            case 3:
               var1 = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, true, this.endTime, var2);
               break;
            case 4:
               var1 = new DateTimeRule(this.endMonth, this.endDay, this.endDayOfWeek, false, this.endTime, var2);
            }

            this.stdRule = new AnnualTimeZoneRule(this.getID() + "(STD)", this.getRawOffset(), 0, var1, this.startYear, Integer.MAX_VALUE);
            long var3 = this.stdRule.getFirstStart(this.getRawOffset(), this.dstRule.getDSTSavings()).getTime();
            if (var3 < var5) {
               this.initialRule = new InitialTimeZoneRule(this.getID() + "(DST)", this.getRawOffset(), this.dstRule.getDSTSavings());
               this.firstTransition = new TimeZoneTransition(var3, this.initialRule, this.stdRule);
            } else {
               this.initialRule = new InitialTimeZoneRule(this.getID() + "(STD)", this.getRawOffset(), 0);
               this.firstTransition = new TimeZoneTransition(var5, this.initialRule, this.dstRule);
            }
         } else {
            this.initialRule = new InitialTimeZoneRule(this.getID(), this.getRawOffset(), 0);
         }

         this.transitionRulesInitialized = true;
      }
   }

   public boolean isFrozen() {
      return this.isFrozen;
   }

   public TimeZone freeze() {
      this.isFrozen = true;
      return this;
   }

   public TimeZone cloneAsThawed() {
      SimpleTimeZone var1 = (SimpleTimeZone)super.cloneAsThawed();
      var1.isFrozen = false;
      return var1;
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }
}
