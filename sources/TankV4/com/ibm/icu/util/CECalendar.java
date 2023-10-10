package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

abstract class CECalendar extends Calendar {
   private static final long serialVersionUID = -999547623066414271L;
   private static final int[][] LIMITS = new int[][]{{0, 0, 1, 1}, {1, 1, 5000000, 5000000}, {0, 0, 12, 12}, {1, 1, 52, 53}, new int[0], {1, 1, 5, 30}, {1, 1, 365, 366}, new int[0], {-1, -1, 1, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], new int[0]};

   protected CECalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   protected CECalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   protected CECalendar(Locale var1) {
      this(TimeZone.getDefault(), var1);
   }

   protected CECalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1);
   }

   protected CECalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   protected CECalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   protected CECalendar(int var1, int var2, int var3) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(var1, var2, var3);
   }

   protected CECalendar(Date var1) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.setTime(var1);
   }

   protected CECalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(var1, var2, var3, var4, var5, var6);
   }

   protected abstract int getJDEpochOffset();

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      return ceToJD((long)var1, var2, 0, this.getJDEpochOffset());
   }

   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   protected int handleGetMonthLength(int var1, int var2) {
      return (var2 + 1) % 13 != 0 ? 30 : var1 % 4 / 3 + 5;
   }

   public static int ceToJD(long var0, int var2, int var3, int var4) {
      if (var2 >= 0) {
         var0 += (long)(var2 / 13);
         var2 %= 13;
      } else {
         ++var2;
         var0 += (long)(var2 / 13 - 1);
         var2 = var2 % 13 + 12;
      }

      return (int)((long)var4 + 365L * var0 + floorDivide(var0, 4L) + (long)(30 * var2) + (long)var3 - 1L);
   }

   public static void jdToCE(int var0, int var1, int[] var2) {
      int[] var4 = new int[1];
      int var3 = floorDivide(var0 - var1, 1461, var4);
      var2[0] = 4 * var3 + (var4[0] / 365 - var4[0] / 1460);
      int var5 = var4[0] == 1460 ? 365 : var4[0] % 365;
      var2[1] = var5 / 30;
      var2[2] = var5 % 30 + 1;
   }
}
