package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarCache;
import java.util.Date;
import java.util.Locale;

public class HebrewCalendar extends Calendar {
   private static final long serialVersionUID = -1952524560588825816L;
   public static final int TISHRI = 0;
   public static final int HESHVAN = 1;
   public static final int KISLEV = 2;
   public static final int TEVET = 3;
   public static final int SHEVAT = 4;
   public static final int ADAR_1 = 5;
   public static final int ADAR = 6;
   public static final int NISAN = 7;
   public static final int IYAR = 8;
   public static final int SIVAN = 9;
   public static final int TAMUZ = 10;
   public static final int AV = 11;
   public static final int ELUL = 12;
   private static final int[][] LIMITS = new int[][]{{0, 0, 0, 0}, {-5000000, -5000000, 5000000, 5000000}, {0, 0, 12, 12}, {1, 1, 51, 56}, new int[0], {1, 1, 29, 30}, {1, 1, 353, 385}, new int[0], {-1, -1, 5, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], new int[0]};
   private static final int[][] MONTH_LENGTH = new int[][]{{30, 30, 30}, {29, 29, 30}, {29, 30, 30}, {29, 29, 29}, {30, 30, 30}, {30, 30, 30}, {29, 29, 29}, {30, 30, 30}, {29, 29, 29}, {30, 30, 30}, {29, 29, 29}, {30, 30, 30}, {29, 29, 29}};
   private static final int[][] MONTH_START = new int[][]{{0, 0, 0}, {30, 30, 30}, {59, 59, 60}, {88, 89, 90}, {117, 118, 119}, {147, 148, 149}, {147, 148, 149}, {176, 177, 178}, {206, 207, 208}, {235, 236, 237}, {265, 266, 267}, {294, 295, 296}, {324, 325, 326}, {353, 354, 355}};
   private static final int[][] LEAP_MONTH_START = new int[][]{{0, 0, 0}, {30, 30, 30}, {59, 59, 60}, {88, 89, 90}, {117, 118, 119}, {147, 148, 149}, {177, 178, 179}, {206, 207, 208}, {236, 237, 238}, {265, 266, 267}, {295, 296, 297}, {324, 325, 326}, {354, 355, 356}, {383, 384, 385}};
   private static CalendarCache cache = new CalendarCache();
   private static final long HOUR_PARTS = 1080L;
   private static final long DAY_PARTS = 25920L;
   private static final int MONTH_DAYS = 29;
   private static final long MONTH_FRACT = 13753L;
   private static final long MONTH_PARTS = 765433L;
   private static final long BAHARAD = 12084L;

   public HebrewCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public HebrewCalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public HebrewCalendar(Locale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public HebrewCalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public HebrewCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public HebrewCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public HebrewCalendar(int var1, int var2, int var3) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
   }

   public HebrewCalendar(Date var1) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.setTime(var1);
   }

   public HebrewCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
      this.set(13, var6);
   }

   public void add(int var1, int var2) {
      switch(var1) {
      case 2:
         int var3 = this.get(2);
         int var4 = this.get(1);
         boolean var5;
         if (var2 > 0) {
            var5 = var3 < 5;
            var3 += var2;

            while(true) {
               if (var5 && var3 >= 5 && var4 < 0) {
                  ++var3;
               }

               if (var3 <= 12) {
                  break;
               }

               var3 -= 13;
               ++var4;
               var5 = true;
            }
         } else {
            var5 = var3 > 5;
            var3 += var2;

            while(true) {
               if (var5 && var3 <= 5 && var4 < 0) {
                  --var3;
               }

               if (var3 >= 0) {
                  break;
               }

               var3 += 13;
               --var4;
               var5 = true;
            }
         }

         this.set(2, var3);
         this.set(1, var4);
         this.pinField(5);
         break;
      default:
         super.add(var1, var2);
      }

   }

   public void roll(int var1, int var2) {
      switch(var1) {
      case 2:
         int var3 = this.get(2);
         int var4 = this.get(1);
         boolean var5 = isLeapYear(var4);
         int var6 = monthsInYear(var4);
         int var7 = var3 + var2 % var6;
         if (!var5) {
            if (var2 > 0 && var3 < 5 && var7 >= 5) {
               ++var7;
            } else if (var2 < 0 && var3 > 5 && var7 <= 5) {
               --var7;
            }
         }

         this.set(2, (var7 + 13) % 13);
         this.pinField(5);
         return;
      default:
         super.roll(var1, var2);
      }
   }

   private static long startOfYear(int var0) {
      long var1 = cache.get((long)var0);
      if (var1 == CalendarCache.EMPTY) {
         int var3 = (235 * var0 - 234) / 19;
         long var4 = (long)var3 * 13753L + 12084L;
         var1 = (long)(var3 * 29) + var4 / 25920L;
         var4 %= 25920L;
         int var6 = (int)(var1 % 7L);
         if (var6 == 2 || var6 == 4 || var6 == 6) {
            ++var1;
            var6 = (int)(var1 % 7L);
         }

         if (var6 == 1 && var4 > 16404L && var0 < 0) {
            var1 += 2L;
         } else if (var6 == 0 && var4 > 23269L && var0 - 1 < 0) {
            ++var1;
         }

         cache.put((long)var0, var1);
      }

      return var1;
   }

   private final int yearType(int var1) {
      int var2 = this.handleGetYearLength(var1);
      if (var2 > 380) {
         var2 -= 30;
      }

      boolean var3 = false;
      byte var4;
      switch(var2) {
      case 353:
         var4 = 0;
         break;
      case 354:
         var4 = 1;
         break;
      case 355:
         var4 = 2;
         break;
      default:
         throw new IllegalArgumentException("Illegal year length " + var2 + " in year " + var1);
      }

      return var4;
   }

   private static int monthsInYear(int var0) {
      return var0 < 0 ? 13 : 12;
   }

   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   protected int handleGetMonthLength(int var1, int var2) {
      while(var2 < 0) {
         --var1;
         var2 += monthsInYear(var1);
      }

      while(var2 > 12) {
         var2 -= monthsInYear(var1++);
      }

      switch(var2) {
      case 1:
      case 2:
         return MONTH_LENGTH[var2][this.yearType(var1)];
      default:
         return MONTH_LENGTH[var2][0];
      }
   }

   protected int handleGetYearLength(int var1) {
      return (int)(startOfYear(var1 + 1) - startOfYear(var1));
   }

   protected void handleComputeFields(int var1) {
      long var2 = (long)(var1 - 347997);
      long var4 = var2 * 25920L / 765433L;
      int var6 = (int)((19L * var4 + 234L) / 235L) + 1;
      long var7 = startOfYear(var6);

      int var9;
      for(var9 = (int)(var2 - var7); var9 < 1; var9 = (int)(var2 - var7)) {
         --var6;
         var7 = startOfYear(var6);
      }

      int var10 = this.yearType(var6);
      int[][] var11 = var6 < 0 ? LEAP_MONTH_START : MONTH_START;

      int var12;
      for(var12 = 0; var9 > var11[var12][var10]; ++var12) {
      }

      --var12;
      int var13 = var9 - var11[var12][var10];
      this.internalSet(0, 0);
      this.internalSet(1, var6);
      this.internalSet(19, var6);
      this.internalSet(2, var12);
      this.internalSet(5, var13);
      this.internalSet(6, var9);
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

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      while(var2 < 0) {
         --var1;
         var2 += monthsInYear(var1);
      }

      while(var2 > 12) {
         var2 -= monthsInYear(var1++);
      }

      long var4 = startOfYear(var1);
      if (var2 != 0) {
         if (var1 < 0) {
            var4 += (long)LEAP_MONTH_START[var2][this.yearType(var1)];
         } else {
            var4 += (long)MONTH_START[var2][this.yearType(var1)];
         }
      }

      return (int)(var4 + 347997L);
   }

   public String getType() {
      return "hebrew";
   }
}
