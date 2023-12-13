package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class IndianCalendar extends Calendar {
   private static final long serialVersionUID = 3617859668165014834L;
   public static final int CHAITRA = 0;
   public static final int VAISAKHA = 1;
   public static final int JYAISTHA = 2;
   public static final int ASADHA = 3;
   public static final int SRAVANA = 4;
   public static final int BHADRA = 5;
   public static final int ASVINA = 6;
   public static final int KARTIKA = 7;
   public static final int AGRAHAYANA = 8;
   public static final int PAUSA = 9;
   public static final int MAGHA = 10;
   public static final int PHALGUNA = 11;
   public static final int IE = 0;
   private static final int INDIAN_ERA_START = 78;
   private static final int INDIAN_YEAR_START = 80;
   private static final int[][] LIMITS = new int[][]{{0, 0, 0, 0}, {-5000000, -5000000, 5000000, 5000000}, {0, 0, 11, 11}, {1, 1, 52, 53}, new int[0], {1, 1, 30, 31}, {1, 1, 365, 366}, new int[0], {-1, -1, 5, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], new int[0]};

   public IndianCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public IndianCalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public IndianCalendar(Locale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public IndianCalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public IndianCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public IndianCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public IndianCalendar(Date var1) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.setTime(var1);
   }

   public IndianCalendar(int var1, int var2, int var3) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
   }

   public IndianCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
      this.set(13, var6);
   }

   protected int handleGetExtendedYear() {
      int var1;
      if (this.newerField(19, 1) == 19) {
         var1 = this.internalGet(19, 1);
      } else {
         var1 = this.internalGet(1, 1);
      }

      return var1;
   }

   protected int handleGetYearLength(int var1) {
      return super.handleGetYearLength(var1);
   }

   protected int handleGetMonthLength(int var1, int var2) {
      if (var2 < 0 || var2 > 11) {
         int[] var3 = new int[1];
         var1 += floorDivide(var2, 12, var3);
         var2 = var3[0];
      }

      if (var1 + 78 == 0 && var2 == 0) {
         return 31;
      } else {
         return var2 >= 1 && var2 <= 5 ? 31 : 30;
      }
   }

   protected void handleComputeFields(int var1) {
      int[] var10 = jdToGregorian((double)var1);
      int var5 = var10[0] - 78;
      double var2 = gregorianToJD(var10[0], 1, 1);
      int var6 = (int)((double)var1 - var2);
      int var4;
      if (var6 < 80) {
         --var5;
         var4 = var10[0] - 1 == 0 ? 31 : 30;
         var6 += var4 + 155 + 90 + 10;
      } else {
         var4 = var10[0] == 0 ? 31 : 30;
         var6 -= 80;
      }

      int var7;
      int var8;
      if (var6 < var4) {
         var7 = 0;
         var8 = var6 + 1;
      } else {
         int var9 = var6 - var4;
         if (var9 < 155) {
            var7 = var9 / 31 + 1;
            var8 = var9 % 31 + 1;
         } else {
            var9 -= 155;
            var7 = var9 / 30 + 6;
            var8 = var9 % 30 + 1;
         }
      }

      this.internalSet(0, 0);
      this.internalSet(19, var5);
      this.internalSet(1, var5);
      this.internalSet(2, var7);
      this.internalSet(5, var8);
      this.internalSet(6, var6 + 1);
   }

   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      if (var2 < 0 || var2 > 11) {
         var1 += var2 / 12;
         var2 %= 12;
      }

      int var4;
      if (var2 == 12) {
         var4 = 1;
      } else {
         var4 = var2 + 1;
      }

      double var5 = IndianToJD(var1, var4, 1);
      return (int)var5;
   }

   private static double IndianToJD(int var0, int var1, int var2) {
      int var4 = var0 + 78;
      byte var3;
      double var6;
      if (var4 == 0) {
         var3 = 31;
         var6 = gregorianToJD(var4, 3, 21);
      } else {
         var3 = 30;
         var6 = gregorianToJD(var4, 3, 22);
      }

      double var8;
      if (var1 == 1) {
         var8 = var6 + (double)(var2 - 1);
      } else {
         var8 = var6 + (double)var3;
         int var5 = var1 - 2;
         var5 = Math.min(var5, 5);
         var8 += (double)(var5 * 31);
         if (var1 >= 8) {
            var5 = var1 - 7;
            var8 += (double)(var5 * 30);
         }

         var8 += (double)(var2 - 1);
      }

      return var8;
   }

   private static double gregorianToJD(int var0, int var1, int var2) {
      double var3 = 1721425.5D;
      int var5 = var0 - 1;
      int var6 = 365 * var5 + var5 / 4 - var5 / 100 + var5 / 400 + (367 * var1 - 362) / 12 + (var1 <= 2 ? 0 : (var0 == 0 ? -1 : -2)) + var2;
      return (double)(var6 - 1) + var3;
   }

   private static int[] jdToGregorian(double var0) {
      double var2 = 1721425.5D;
      double var4 = Math.floor(var0 - 0.5D) + 0.5D;
      double var6 = var4 - var2;
      double var8 = Math.floor(var6 / 146097.0D);
      double var10 = var6 % 146097.0D;
      double var12 = Math.floor(var10 / 36524.0D);
      double var14 = var10 % 36524.0D;
      double var16 = Math.floor(var14 / 1461.0D);
      double var18 = var14 % 1461.0D;
      double var20 = Math.floor(var18 / 365.0D);
      int var26 = (int)(var8 * 400.0D + var12 * 100.0D + var16 * 4.0D + var20);
      if (var12 != 4.0D && var20 != 4.0D) {
         ++var26;
      }

      double var22 = var4 - gregorianToJD(var26, 1, 1);
      double var24 = (double)(var4 < gregorianToJD(var26, 3, 1) ? 0 : (var26 == 0 ? 1 : 2));
      int var27 = (int)Math.floor(((var22 + var24) * 12.0D + 373.0D) / 367.0D);
      int var28 = (int)(var4 - gregorianToJD(var26, var27, 1)) + 1;
      int[] var29 = new int[]{var26, var27, var28};
      return var29;
   }

   public String getType() {
      return "indian";
   }
}
