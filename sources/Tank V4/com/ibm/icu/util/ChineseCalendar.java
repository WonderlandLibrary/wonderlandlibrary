package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarAstronomer;
import com.ibm.icu.impl.CalendarCache;
import com.ibm.icu.text.DateFormat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Locale;

public class ChineseCalendar extends Calendar {
   private static final long serialVersionUID = 7312110751940929420L;
   private int epochYear;
   private TimeZone zoneAstro;
   private transient CalendarAstronomer astro;
   private transient CalendarCache winterSolsticeCache;
   private transient CalendarCache newYearCache;
   private transient boolean isLeapYear;
   private static final int[][] LIMITS = new int[][]{{1, 1, 83333, 83333}, {1, 1, 60, 60}, {0, 0, 11, 11}, {1, 1, 50, 55}, new int[0], {1, 1, 29, 30}, {1, 1, 353, 385}, new int[0], {-1, -1, 5, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], {-5000000, -5000000, 5000000, 5000000}, new int[0], new int[0], {0, 0, 1, 1}};
   static final int[][][] CHINESE_DATE_PRECEDENCE = new int[][][]{{{5}, {3, 7}, {4, 7}, {8, 7}, {3, 18}, {4, 18}, {8, 18}, {6}, {37, 22}}, {{3}, {4}, {8}, {40, 7}, {40, 18}}};
   private static final int CHINESE_EPOCH_YEAR = -2636;
   private static final TimeZone CHINA_ZONE = (new SimpleTimeZone(28800000, "CHINA_ZONE")).freeze();
   private static final int SYNODIC_GAP = 25;

   public ChineseCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
   }

   public ChineseCalendar(Date var1) {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
      this.setTime(var1);
   }

   public ChineseCalendar(int var1, int var2, int var3, int var4) {
      this(var1, var2, var3, var4, 0, 0, 0);
   }

   public ChineseCalendar(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
      this.set(14, 0);
      this.set(1, var1);
      this.set(2, var2);
      this.set(22, var3);
      this.set(5, var4);
      this.set(11, var5);
      this.set(12, var6);
      this.set(13, var7);
   }

   public ChineseCalendar(int var1, int var2, int var3, int var4, int var5) {
      this(var1, var2, var3, var4, 0, 0, 0);
   }

   public ChineseCalendar(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
      this.set(14, 0);
      this.set(0, var1);
      this.set(1, var2);
      this.set(2, var3);
      this.set(22, var4);
      this.set(5, var5);
      this.set(11, var6);
      this.set(12, var7);
      this.set(13, var8);
   }

   public ChineseCalendar(Locale var1) {
      this(TimeZone.getDefault(), ULocale.forLocale(var1), -2636, CHINA_ZONE);
   }

   public ChineseCalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
   }

   public ChineseCalendar(TimeZone var1, Locale var2) {
      this(var1, ULocale.forLocale(var2), -2636, CHINA_ZONE);
   }

   public ChineseCalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1, -2636, CHINA_ZONE);
   }

   public ChineseCalendar(TimeZone var1, ULocale var2) {
      this(var1, var2, -2636, CHINA_ZONE);
   }

   /** @deprecated */
   protected ChineseCalendar(TimeZone var1, ULocale var2, int var3, TimeZone var4) {
      super(var1, var2);
      this.astro = new CalendarAstronomer();
      this.winterSolsticeCache = new CalendarCache();
      this.newYearCache = new CalendarCache();
      this.epochYear = var3;
      this.zoneAstro = var4;
      this.setTimeInMillis(System.currentTimeMillis());
   }

   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   protected int handleGetExtendedYear() {
      int var1;
      if (this.newestStamp(0, 1, 0) <= this.getStamp(19)) {
         var1 = this.internalGet(19, 1);
      } else {
         int var2 = this.internalGet(0, 1) - 1;
         var1 = var2 * 60 + this.internalGet(1, 1) - (this.epochYear - -2636);
      }

      return var1;
   }

   protected int handleGetMonthLength(int var1, int var2) {
      int var3 = this.handleComputeMonthStart(var1, var2, true) - 2440588 + 1;
      int var4 = this.newMoonNear(var3 + 25, true);
      return var4 - var3;
   }

   protected DateFormat handleGetDateFormat(String var1, String var2, ULocale var3) {
      return super.handleGetDateFormat(var1, var2, var3);
   }

   protected int[][][] getFieldResolutionTable() {
      return CHINESE_DATE_PRECEDENCE;
   }

   private void offsetMonth(int var1, int var2, int var3) {
      var1 += (int)(29.530588853D * ((double)var3 - 0.5D));
      var1 = this.newMoonNear(var1, true);
      int var4 = var1 + 2440588 - 1 + var2;
      if (var2 > 29) {
         this.set(20, var4 - 1);
         this.complete();
         if (this.getActualMaximum(5) >= var2) {
            this.set(20, var4);
         }
      } else {
         this.set(20, var4);
      }

   }

   public void add(int var1, int var2) {
      switch(var1) {
      case 2:
         if (var2 != 0) {
            int var3 = this.get(5);
            int var4 = this.get(20) - 2440588;
            int var5 = var4 - var3 + 1;
            this.offsetMonth(var5, var3, var2);
         }
         break;
      default:
         super.add(var1, var2);
      }

   }

   public void roll(int var1, int var2) {
      switch(var1) {
      case 2:
         if (var2 != 0) {
            int var3 = this.get(5);
            int var4 = this.get(20) - 2440588;
            int var5 = var4 - var3 + 1;
            int var6 = this.get(2);
            int var7;
            if (this.isLeapYear) {
               if (this.get(22) == 1) {
                  ++var6;
               } else {
                  var7 = var5 - (int)(29.530588853D * ((double)var6 - 0.5D));
                  var7 = this.newMoonNear(var7, true);
                  if (var7 >= var5) {
                     ++var6;
                  }
               }
            }

            var7 = this.isLeapYear ? 13 : 12;
            int var8 = (var6 + var2) % var7;
            if (var8 < 0) {
               var8 += var7;
            }

            if (var8 != var6) {
               this.offsetMonth(var5, var3, var8 - var6);
            }
         }
         break;
      default:
         super.roll(var1, var2);
      }

   }

   private final long daysToMillis(int var1) {
      long var2 = (long)var1 * 86400000L;
      return var2 - (long)this.zoneAstro.getOffset(var2);
   }

   private final int millisToDays(long var1) {
      return (int)floorDivide(var1 + (long)this.zoneAstro.getOffset(var1), 86400000L);
   }

   private int winterSolstice(int var1) {
      long var2 = this.winterSolsticeCache.get((long)var1);
      if (var2 == CalendarCache.EMPTY) {
         long var4 = this.daysToMillis(this.computeGregorianMonthStart(var1, 11) + 1 - 2440588);
         this.astro.setTime(var4);
         long var6 = this.astro.getSunTime(CalendarAstronomer.WINTER_SOLSTICE, true);
         var2 = (long)this.millisToDays(var6);
         this.winterSolsticeCache.put((long)var1, var2);
      }

      return (int)var2;
   }

   private int newMoonNear(int var1, boolean var2) {
      this.astro.setTime(this.daysToMillis(var1));
      long var3 = this.astro.getMoonTime(CalendarAstronomer.NEW_MOON, var2);
      return this.millisToDays(var3);
   }

   private int synodicMonthsBetween(int var1, int var2) {
      return (int)Math.round((double)(var2 - var1) / 29.530588853D);
   }

   private int majorSolarTerm(int var1) {
      this.astro.setTime(this.daysToMillis(var1));
      int var2 = ((int)Math.floor(6.0D * this.astro.getSunLongitude() / 3.141592653589793D) + 2) % 12;
      if (var2 < 1) {
         var2 += 12;
      }

      return var2;
   }

   protected void handleComputeFields(int var1) {
      this.computeChineseFields(var1 - 2440588, this.getGregorianYear(), this.getGregorianMonth(), true);
   }

   private void computeChineseFields(int var1, int var2, int var3, boolean var4) {
      int var6 = this.winterSolstice(var2);
      int var5;
      if (var1 < var6) {
         var5 = this.winterSolstice(var2 - 1);
      } else {
         var5 = var6;
         var6 = this.winterSolstice(var2 + 1);
      }

      int var7 = this.newMoonNear(var5 + 1, true);
      int var8 = this.newMoonNear(var6 + 1, false);
      int var9 = this.newMoonNear(var1 + 1, false);
      this.isLeapYear = this.synodicMonthsBetween(var7, var8) == 12;
      int var10 = this.synodicMonthsBetween(var7, var9);
      if (this.isLeapYear && var7 >= var9) {
         --var10;
      }

      if (var10 < 1) {
         var10 += 12;
      }

      ChineseCalendar var10000;
      label51: {
         if (this.isLeapYear && this == var9) {
            var10000 = this;
            if (var7 >= this.newMoonNear(var9 - 25, false)) {
               boolean var10001 = true;
               break label51;
            }
         }

         var10000 = false;
      }

      ChineseCalendar var11 = var10000;
      this.internalSet(2, var10 - 1);
      this.internalSet(22, var11 != false ? 1 : 0);
      if (var4) {
         int var12 = var2 - this.epochYear;
         int var13 = var2 - -2636;
         if (var10 < 11 || var3 >= 6) {
            ++var12;
            ++var13;
         }

         int var14 = var1 - var9 + 1;
         this.internalSet(19, var12);
         int[] var15 = new int[1];
         int var16 = floorDivide(var13 - 1, 60, var15);
         this.internalSet(0, var16 + 1);
         this.internalSet(1, var15[0] + 1);
         this.internalSet(5, var14);
         int var17 = this.newYear(var2);
         if (var1 < var17) {
            var17 = this.newYear(var2 - 1);
         }

         this.internalSet(6, var1 - var17 + 1);
      }

   }

   private int newYear(int var1) {
      long var2 = this.newYearCache.get((long)var1);
      if (var2 == CalendarCache.EMPTY) {
         int var4 = this.winterSolstice(var1 - 1);
         int var5 = this.winterSolstice(var1);
         int var6 = this.newMoonNear(var4 + 1, true);
         int var7 = this.newMoonNear(var6 + 25, true);
         int var8 = this.newMoonNear(var5 + 1, false);
         if (this.synodicMonthsBetween(var6, var8) != 12 || this == var6 && this != var7) {
            var2 = (long)var7;
         } else {
            var2 = (long)this.newMoonNear(var7 + 25, true);
         }

         this.newYearCache.put((long)var1, var2);
      }

      return (int)var2;
   }

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      if (var2 < 0 || var2 > 11) {
         int[] var4 = new int[1];
         var1 += floorDivide(var2, 12, var4);
         var2 = var4[0];
      }

      int var11 = var1 + this.epochYear - 1;
      int var5 = this.newYear(var11);
      int var6 = this.newMoonNear(var5 + var2 * 29, true);
      int var7 = var6 + 2440588;
      int var8 = this.internalGet(2);
      int var9 = this.internalGet(22);
      int var10 = var3 ? var9 : 0;
      this.computeGregorianFields(var7);
      this.computeChineseFields(var6, this.getGregorianYear(), this.getGregorianMonth(), false);
      if (var2 != this.internalGet(2) || var10 != this.internalGet(22)) {
         var6 = this.newMoonNear(var6 + 25, true);
         var7 = var6 + 2440588;
      }

      this.internalSet(2, var8);
      this.internalSet(22, var9);
      return var7 - 1;
   }

   public String getType() {
      return "chinese";
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.epochYear = -2636;
      this.zoneAstro = CHINA_ZONE;
      var1.defaultReadObject();
      this.astro = new CalendarAstronomer();
      this.winterSolsticeCache = new CalendarCache();
      this.newYearCache = new CalendarCache();
   }
}
