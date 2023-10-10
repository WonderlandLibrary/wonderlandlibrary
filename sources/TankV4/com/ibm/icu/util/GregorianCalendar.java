package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class GregorianCalendar extends Calendar {
   private static final long serialVersionUID = 9199388694351062137L;
   public static final int BC = 0;
   public static final int AD = 1;
   private static final int EPOCH_YEAR = 1970;
   private static final int[][] MONTH_COUNT = new int[][]{{31, 31, 0, 0}, {28, 29, 31, 31}, {31, 31, 59, 60}, {30, 30, 90, 91}, {31, 31, 120, 121}, {30, 30, 151, 152}, {31, 31, 181, 182}, {31, 31, 212, 213}, {30, 30, 243, 244}, {31, 31, 273, 274}, {30, 30, 304, 305}, {31, 31, 334, 335}};
   private static final int[][] LIMITS = new int[][]{{0, 0, 1, 1}, {1, 1, 5828963, 5838270}, {0, 0, 11, 11}, {1, 1, 52, 53}, new int[0], {1, 1, 28, 31}, {1, 1, 365, 366}, new int[0], {-1, -1, 4, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5838270, -5838270, 5828964, 5838271}, new int[0], {-5838269, -5838269, 5828963, 5838270}, new int[0], new int[0]};
   private long gregorianCutover;
   private transient int cutoverJulianDay;
   private transient int gregorianCutoverYear;
   protected transient boolean isGregorian;
   protected transient boolean invertGregorian;

   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   public GregorianCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public GregorianCalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public GregorianCalendar(Locale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public GregorianCalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public GregorianCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
      this.gregorianCutover = -12219292800000L;
      this.cutoverJulianDay = 2299161;
      this.gregorianCutoverYear = 1582;
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public GregorianCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
      this.gregorianCutover = -12219292800000L;
      this.cutoverJulianDay = 2299161;
      this.gregorianCutoverYear = 1582;
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public GregorianCalendar(int var1, int var2, int var3) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.gregorianCutover = -12219292800000L;
      this.cutoverJulianDay = 2299161;
      this.gregorianCutoverYear = 1582;
      this.set(0, 1);
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
   }

   public GregorianCalendar(int var1, int var2, int var3, int var4, int var5) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.gregorianCutover = -12219292800000L;
      this.cutoverJulianDay = 2299161;
      this.gregorianCutoverYear = 1582;
      this.set(0, 1);
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
   }

   public GregorianCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.gregorianCutover = -12219292800000L;
      this.cutoverJulianDay = 2299161;
      this.gregorianCutoverYear = 1582;
      this.set(0, 1);
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
      this.set(13, var6);
   }

   public void setGregorianChange(Date var1) {
      this.gregorianCutover = var1.getTime();
      if (this.gregorianCutover <= -184303902528000000L) {
         this.gregorianCutoverYear = this.cutoverJulianDay = Integer.MIN_VALUE;
      } else if (this.gregorianCutover >= 183882168921600000L) {
         this.gregorianCutoverYear = this.cutoverJulianDay = Integer.MAX_VALUE;
      } else {
         this.cutoverJulianDay = (int)floorDivide(this.gregorianCutover, 86400000L);
         GregorianCalendar var2 = new GregorianCalendar(this.getTimeZone());
         var2.setTime(var1);
         this.gregorianCutoverYear = var2.get(19);
      }

   }

   public final Date getGregorianChange() {
      return new Date(this.gregorianCutover);
   }

   public boolean isEquivalentTo(Calendar var1) {
      return super.isEquivalentTo(var1) && this.gregorianCutover == ((GregorianCalendar)var1).gregorianCutover;
   }

   public int hashCode() {
      return super.hashCode() ^ (int)this.gregorianCutover;
   }

   public void roll(int var1, int var2) {
      switch(var1) {
      case 3:
         int var3 = this.get(3);
         int var4 = this.get(17);
         int var5 = this.internalGet(6);
         if (this.internalGet(2) == 0) {
            if (var3 >= 52) {
               var5 += this.handleGetYearLength(var4);
            }
         } else if (var3 == 1) {
            var5 -= this.handleGetYearLength(var4 - 1);
         }

         var3 += var2;
         if (var3 < 1 || var3 > 52) {
            int var6 = this.handleGetYearLength(var4);
            int var7 = (var6 - var5 + this.internalGet(7) - this.getFirstDayOfWeek()) % 7;
            if (var7 < 0) {
               var7 += 7;
            }

            if (6 - var7 >= this.getMinimalDaysInFirstWeek()) {
               var6 -= 7;
            }

            int var8 = this.weekNumber(var6, var7 + 1);
            var3 = (var3 + var8 - 1) % var8 + 1;
         }

         this.set(3, var3);
         this.set(1, var4);
         return;
      default:
         super.roll(var1, var2);
      }
   }

   public int getActualMinimum(int var1) {
      return this.getMinimum(var1);
   }

   public int getActualMaximum(int var1) {
      switch(var1) {
      case 1:
         Calendar var2 = (Calendar)this.clone();
         var2.setLenient(true);
         int var3 = var2.get(0);
         Date var4 = var2.getTime();
         int var5 = LIMITS[1][1];
         int var6 = LIMITS[1][2] + 1;

         while(true) {
            while(var5 + 1 < var6) {
               int var7 = (var5 + var6) / 2;
               var2.set(1, var7);
               if (var2.get(1) == var7 && var2.get(0) == var3) {
                  var5 = var7;
               } else {
                  var6 = var7;
                  var2.setTime(var4);
               }
            }

            return var5;
         }
      default:
         return super.getActualMaximum(var1);
      }
   }

   boolean inDaylightTime() {
      if (!this.getTimeZone().useDaylightTime()) {
         return false;
      } else {
         this.complete();
         return this.internalGet(16) != 0;
      }
   }

   protected int handleGetMonthLength(int var1, int var2) {
      if (var2 < 0 || var2 > 11) {
         int[] var3 = new int[1];
         var1 += floorDivide(var2, 12, var3);
         var2 = var3[0];
      }

      return MONTH_COUNT[var2][this >= var1 ? 1 : 0];
   }

   protected int handleGetYearLength(int var1) {
      return this >= var1 ? 366 : 365;
   }

   protected void handleComputeFields(int var1) {
      int var2;
      int var3;
      int var4;
      int var5;
      if (var1 >= this.cutoverJulianDay) {
         var3 = this.getGregorianMonth();
         var4 = this.getGregorianDayOfMonth();
         var5 = this.getGregorianDayOfYear();
         var2 = this.getGregorianYear();
      } else {
         long var6 = (long)(var1 - 1721424);
         var2 = (int)floorDivide(4L * var6 + 1464L, 1461L);
         long var8 = (long)(365 * (var2 - 1) + floorDivide(var2 - 1, 4));
         var5 = (int)(var6 - var8);
         boolean var10 = (var2 & 3) == 0;
         int var11 = 0;
         int var12 = var10 ? 60 : 59;
         if (var5 >= var12) {
            var11 = var10 ? 1 : 2;
         }

         var3 = (12 * (var5 + var11) + 6) / 367;
         var4 = var5 - MONTH_COUNT[var3][var10 ? 3 : 2] + 1;
         ++var5;
      }

      this.internalSet(2, var3);
      this.internalSet(5, var4);
      this.internalSet(6, var5);
      this.internalSet(19, var2);
      byte var13 = 1;
      if (var2 < 1) {
         var13 = 0;
         var2 = 1 - var2;
      }

      this.internalSet(0, var13);
      this.internalSet(1, var2);
   }

   protected int handleGetExtendedYear() {
      int var1;
      if (this.newerField(19, 1) == 19) {
         var1 = this.internalGet(19, 1970);
      } else {
         int var2 = this.internalGet(0, 1);
         if (var2 == 0) {
            var1 = 1 - this.internalGet(1, 1);
         } else {
            var1 = this.internalGet(1, 1970);
         }
      }

      return var1;
   }

   protected int handleComputeJulianDay(int var1) {
      this.invertGregorian = false;
      int var2 = super.handleComputeJulianDay(var1);
      if (this.isGregorian != var2 >= this.cutoverJulianDay) {
         this.invertGregorian = true;
         var2 = super.handleComputeJulianDay(var1);
      }

      return var2;
   }

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      if (var2 < 0 || var2 > 11) {
         int[] var4 = new int[1];
         var1 += floorDivide(var2, 12, var4);
         var2 = var4[0];
      }

      boolean var7 = var1 % 4 == 0;
      int var5 = var1 - 1;
      int var6 = 365 * var5 + floorDivide(var5, 4) + 1721423;
      this.isGregorian = var1 >= this.gregorianCutoverYear;
      if (this.invertGregorian) {
         this.isGregorian = !this.isGregorian;
      }

      if (this.isGregorian) {
         var7 = var7 && (var1 % 100 != 0 || var1 % 400 == 0);
         var6 += floorDivide(var5, 400) - floorDivide(var5, 100) + 2;
      }

      if (var2 != 0) {
         var6 += MONTH_COUNT[var2][var7 ? 3 : 2];
      }

      return var6;
   }

   public String getType() {
      return "gregorian";
   }
}
