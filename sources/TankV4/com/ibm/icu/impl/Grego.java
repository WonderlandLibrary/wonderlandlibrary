package com.ibm.icu.impl;

public class Grego {
   public static final long MIN_MILLIS = -184303902528000000L;
   public static final long MAX_MILLIS = 183882168921600000L;
   public static final int MILLIS_PER_SECOND = 1000;
   public static final int MILLIS_PER_MINUTE = 60000;
   public static final int MILLIS_PER_HOUR = 3600000;
   public static final int MILLIS_PER_DAY = 86400000;
   private static final int JULIAN_1_CE = 1721426;
   private static final int JULIAN_1970_CE = 2440588;
   private static final int[] MONTH_LENGTH = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   private static final int[] DAYS_BEFORE = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};

   public static final int monthLength(int var0, int var1) {
      return MONTH_LENGTH[var1 + (var0 == 0 ? 12 : 0)];
   }

   public static final int previousMonthLength(int var0, int var1) {
      return var1 > 0 ? monthLength(var0, var1 - 1) : 31;
   }

   public static long fieldsToDay(int var0, int var1, int var2) {
      int var3 = var0 - 1;
      long var4 = (long)(365 * var3) + floorDivide((long)var3, 4L) + 1721423L + floorDivide((long)var3, 400L) - floorDivide((long)var3, 100L) + 2L + (long)DAYS_BEFORE[var1 + (var0 == 0 ? 12 : 0)] + (long)var2;
      return var4 - 2440588L;
   }

   public static int dayOfWeek(long var0) {
      long[] var2 = new long[1];
      floorDivide(var0 + 5L, 7L, var2);
      int var3 = (int)var2[0];
      var3 = var3 == 0 ? 7 : var3;
      return var3;
   }

   public static int[] dayToFields(long var0, int[] var2) {
      if (var2 == null || var2.length < 5) {
         var2 = new int[5];
      }

      var0 += 719162L;
      long[] var3 = new long[1];
      long var4 = floorDivide(var0, 146097L, var3);
      long var6 = floorDivide(var3[0], 36524L, var3);
      long var8 = floorDivide(var3[0], 1461L, var3);
      long var10 = floorDivide(var3[0], 365L, var3);
      int var12 = (int)(400L * var4 + 100L * var6 + 4L * var8 + var10);
      int var13 = (int)var3[0];
      if (var6 != 4L && var10 != 4L) {
         ++var12;
      } else {
         var13 = 365;
      }

      boolean var14 = isLeapYear(var12);
      int var15 = 0;
      int var16 = var14 ? 60 : 59;
      if (var13 >= var16) {
         var15 = var14 ? 1 : 2;
      }

      int var17 = (12 * (var13 + var15) + 6) / 367;
      int var18 = var13 - DAYS_BEFORE[var14 ? var17 + 12 : var17] + 1;
      int var19 = (int)((var0 + 2L) % 7L);
      if (var19 < 1) {
         var19 += 7;
      }

      ++var13;
      var2[0] = var12;
      var2[1] = var17;
      var2[2] = var18;
      var2[3] = var19;
      var2[4] = var13;
      return var2;
   }

   public static int[] timeToFields(long var0, int[] var2) {
      if (var2 == null || var2.length < 6) {
         var2 = new int[6];
      }

      long[] var3 = new long[1];
      long var4 = floorDivide(var0, 86400000L, var3);
      dayToFields(var4, var2);
      var2[5] = (int)var3[0];
      return var2;
   }

   public static long floorDivide(long var0, long var2) {
      return var0 >= 0L ? var0 / var2 : (var0 + 1L) / var2 - 1L;
   }

   private static long floorDivide(long var0, long var2, long[] var4) {
      if (var0 >= 0L) {
         var4[0] = var0 % var2;
         return var0 / var2;
      } else {
         long var5 = (var0 + 1L) / var2 - 1L;
         var4[0] = var0 - var5 * var2;
         return var5;
      }
   }

   public static int getDayOfWeekInMonth(int var0, int var1, int var2) {
      int var3 = (var2 + 6) / 7;
      if (var3 == 4) {
         if (var2 + 7 > monthLength(var0, var1)) {
            var3 = -1;
         }
      } else if (var3 == 5) {
         var3 = -1;
      }

      return var3;
   }
}
