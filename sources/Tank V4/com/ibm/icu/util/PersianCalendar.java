package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

/** @deprecated */
public class PersianCalendar extends Calendar {
   private static final long serialVersionUID = -6727306982975111643L;
   private static final int[][] MONTH_COUNT = new int[][]{{31, 31, 0}, {31, 31, 31}, {31, 31, 62}, {31, 31, 93}, {31, 31, 124}, {31, 31, 155}, {30, 30, 186}, {30, 30, 216}, {30, 30, 246}, {30, 30, 276}, {30, 30, 306}, {29, 30, 336}};
   private static final int PERSIAN_EPOCH = 1948320;
   private static final int[][] LIMITS = new int[][]{{0, 0, 0, 0}, {-5000000, -5000000, 5000000, 5000000}, {0, 0, 11, 11}, {1, 1, 52, 53}, new int[0], {1, 1, 29, 31}, {1, 1, 365, 366}, new int[0], {-1, -1, 5, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], new int[0]};

   /** @deprecated */
   public PersianCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   /** @deprecated */
   public PersianCalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   /** @deprecated */
   public PersianCalendar(Locale var1) {
      this(TimeZone.getDefault(), var1);
   }

   /** @deprecated */
   public PersianCalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1);
   }

   /** @deprecated */
   public PersianCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   /** @deprecated */
   public PersianCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   /** @deprecated */
   public PersianCalendar(Date var1) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.setTime(var1);
   }

   /** @deprecated */
   public PersianCalendar(int var1, int var2, int var3) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
   }

   /** @deprecated */
   public PersianCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
      this.set(13, var6);
   }

   /** @deprecated */
   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   /** @deprecated */
   protected int handleGetMonthLength(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   protected int handleGetYearLength(int param1) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      if (var2 < 0 || var2 > 11) {
         int[] var4 = new int[1];
         var1 += floorDivide(var2, 12, var4);
         var2 = var4[0];
      }

      int var5 = 1948319 + 365 * (var1 - 1) + floorDivide(8 * var1 + 21, 33);
      if (var2 != 0) {
         var5 += MONTH_COUNT[var2][2];
      }

      return var5;
   }

   /** @deprecated */
   protected int handleGetExtendedYear() {
      int var1;
      if (this.newerField(19, 1) == 19) {
         var1 = this.internalGet(19, 1);
      } else {
         var1 = this.internalGet(1, 1);
      }

      return var1;
   }

   /** @deprecated */
   protected void handleComputeFields(int var1) {
      long var6 = (long)(var1 - 1948320);
      int var2 = 1 + (int)floorDivide(33L * var6 + 3L, 12053L);
      long var8 = (long)(365 * (var2 - 1) + floorDivide(8 * var2 + 21, 33));
      int var5 = (int)(var6 - var8);
      int var3;
      if (var5 < 216) {
         var3 = var5 / 31;
      } else {
         var3 = (var5 - 6) / 30;
      }

      int var4 = var5 - MONTH_COUNT[var3][2] + 1;
      ++var5;
      this.internalSet(0, 0);
      this.internalSet(1, var2);
      this.internalSet(19, var2);
      this.internalSet(2, var3);
      this.internalSet(5, var4);
      this.internalSet(6, var5);
   }

   /** @deprecated */
   public String getType() {
      return "persian";
   }
}
