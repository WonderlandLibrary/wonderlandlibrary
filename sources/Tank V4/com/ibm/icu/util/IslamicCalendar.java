package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarAstronomer;
import com.ibm.icu.impl.CalendarCache;
import java.util.Date;
import java.util.Locale;

public class IslamicCalendar extends Calendar {
   private static final long serialVersionUID = -6253365474073869325L;
   public static final int MUHARRAM = 0;
   public static final int SAFAR = 1;
   public static final int RABI_1 = 2;
   public static final int RABI_2 = 3;
   public static final int JUMADA_1 = 4;
   public static final int JUMADA_2 = 5;
   public static final int RAJAB = 6;
   public static final int SHABAN = 7;
   public static final int RAMADAN = 8;
   public static final int SHAWWAL = 9;
   public static final int DHU_AL_QIDAH = 10;
   public static final int DHU_AL_HIJJAH = 11;
   private static final long HIJRA_MILLIS = -42521587200000L;
   private static final int[][] LIMITS = new int[][]{{0, 0, 0, 0}, {1, 1, 5000000, 5000000}, {0, 0, 11, 11}, {1, 1, 50, 51}, new int[0], {1, 1, 29, 30}, {1, 1, 354, 355}, new int[0], {-1, -1, 5, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {1, 1, 5000000, 5000000}, new int[0], {1, 1, 5000000, 5000000}, new int[0], new int[0]};
   private static CalendarAstronomer astro = new CalendarAstronomer();
   private static CalendarCache cache = new CalendarCache();
   private boolean civil;

   public IslamicCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public IslamicCalendar(TimeZone var1) {
      this(var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public IslamicCalendar(Locale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public IslamicCalendar(ULocale var1) {
      this(TimeZone.getDefault(), var1);
   }

   public IslamicCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
      this.civil = true;
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public IslamicCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
      this.civil = true;
      this.setTimeInMillis(System.currentTimeMillis());
   }

   public IslamicCalendar(Date var1) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.civil = true;
      this.setTime(var1);
   }

   public IslamicCalendar(int var1, int var2, int var3) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.civil = true;
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
   }

   public IslamicCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.civil = true;
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
      this.set(13, var6);
   }

   public void setCivil(boolean var1) {
      if (this.civil != var1) {
         long var2 = this.getTimeInMillis();
         this.civil = var1;
         this.clear();
         this.setTimeInMillis(var2);
      }

   }

   public boolean isCivil() {
      return this.civil;
   }

   protected int handleGetLimit(int var1, int var2) {
      return LIMITS[var1][var2];
   }

   private long yearStart(int var1) {
      return this.civil ? (long)((var1 - 1) * 354) + (long)Math.floor((double)(3 + 11 * var1) / 30.0D) : trueMonthStart((long)(12 * (var1 - 1)));
   }

   private long monthStart(int var1, int var2) {
      int var3 = var1 + var2 / 12;
      int var4 = var2 % 12;
      return this.civil ? (long)Math.ceil(29.5D * (double)var4) + (long)((var3 - 1) * 354) + (long)Math.floor((double)(3 + 11 * var3) / 30.0D) : trueMonthStart((long)(12 * (var3 - 1) + var4));
   }

   private static final long trueMonthStart(long var0) {
      long var2 = cache.get(var0);
      if (var2 == CalendarCache.EMPTY) {
         long var4 = -42521587200000L + (long)Math.floor((double)var0 * 29.530588853D) * 86400000L;
         double var6 = moonAge(var4);
         if (moonAge(var4) >= 0.0D) {
            do {
               var4 -= 86400000L;
               var6 = moonAge(var4);
            } while(var6 >= 0.0D);
         } else {
            do {
               var4 += 86400000L;
               var6 = moonAge(var4);
            } while(var6 < 0.0D);
         }

         var2 = (var4 - -42521587200000L) / 86400000L + 1L;
         cache.put(var0, var2);
      }

      return var2;
   }

   static final double moonAge(long var0) {
      double var2 = 0.0D;
      CalendarAstronomer var4;
      synchronized(var4 = astro){}
      astro.setTime(var0);
      var2 = astro.getMoonAge();
      var2 = var2 * 180.0D / 3.141592653589793D;
      if (var2 > 180.0D) {
         var2 -= 360.0D;
      }

      return var2;
   }

   protected int handleGetMonthLength(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   protected int handleGetYearLength(int param1) {
      // $FF: Couldn't be decompiled
   }

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      return (int)this.monthStart(var1, var2) + 1948439;
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

   protected void handleComputeFields(int var1) {
      long var8 = (long)(var1 - 1948440);
      int var2;
      int var3;
      if (this.civil) {
         var2 = (int)Math.floor((double)(30L * var8 + 10646L) / 10631.0D);
         var3 = (int)Math.ceil((double)(var8 - 29L - this.yearStart(var2)) / 29.5D);
         var3 = Math.min(var3, 11);
      } else {
         int var10 = (int)Math.floor((double)var8 / 29.530588853D);
         long var6 = (long)Math.floor((double)var10 * 29.530588853D - 1.0D);
         if (var8 - var6 >= 25L && moonAge(this.internalGetTimeInMillis()) > 0.0D) {
            ++var10;
         }

         while(trueMonthStart((long)var10) > var8) {
            --var10;
         }

         var2 = var10 / 12 + 1;
         var3 = var10 % 12;
      }

      int var4 = (int)(var8 - this.monthStart(var2, var3)) + 1;
      int var5 = (int)(var8 - this.monthStart(var2, 0) + 1L);
      this.internalSet(0, 0);
      this.internalSet(1, var2);
      this.internalSet(19, var2);
      this.internalSet(2, var3);
      this.internalSet(5, var4);
      this.internalSet(6, var5);
   }

   public String getType() {
      return this.civil ? "islamic-civil" : "islamic";
   }
}
