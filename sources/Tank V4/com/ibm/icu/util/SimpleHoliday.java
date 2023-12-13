package com.ibm.icu.util;

import java.util.Date;

public class SimpleHoliday extends Holiday {
   public static final SimpleHoliday NEW_YEARS_DAY = new SimpleHoliday(0, 1, "New Year's Day");
   public static final SimpleHoliday EPIPHANY = new SimpleHoliday(0, 6, "Epiphany");
   public static final SimpleHoliday MAY_DAY = new SimpleHoliday(4, 1, "May Day");
   public static final SimpleHoliday ASSUMPTION = new SimpleHoliday(7, 15, "Assumption");
   public static final SimpleHoliday ALL_SAINTS_DAY = new SimpleHoliday(10, 1, "All Saints' Day");
   public static final SimpleHoliday ALL_SOULS_DAY = new SimpleHoliday(10, 2, "All Souls' Day");
   public static final SimpleHoliday IMMACULATE_CONCEPTION = new SimpleHoliday(11, 8, "Immaculate Conception");
   public static final SimpleHoliday CHRISTMAS_EVE = new SimpleHoliday(11, 24, "Christmas Eve");
   public static final SimpleHoliday CHRISTMAS = new SimpleHoliday(11, 25, "Christmas");
   public static final SimpleHoliday BOXING_DAY = new SimpleHoliday(11, 26, "Boxing Day");
   public static final SimpleHoliday ST_STEPHENS_DAY = new SimpleHoliday(11, 26, "St. Stephen's Day");
   public static final SimpleHoliday NEW_YEARS_EVE = new SimpleHoliday(11, 31, "New Year's Eve");

   public SimpleHoliday(int var1, int var2, String var3) {
      super(var3, new SimpleDateRule(var1, var2));
   }

   public SimpleHoliday(int var1, int var2, String var3, int var4) {
      super(var3, rangeRule(var4, 0, new SimpleDateRule(var1, var2)));
   }

   public SimpleHoliday(int var1, int var2, String var3, int var4, int var5) {
      super(var3, rangeRule(var4, var5, new SimpleDateRule(var1, var2)));
   }

   public SimpleHoliday(int var1, int var2, int var3, String var4) {
      super(var4, new SimpleDateRule(var1, var2, var3 > 0 ? var3 : -var3, var3 > 0));
   }

   public SimpleHoliday(int var1, int var2, int var3, String var4, int var5) {
      super(var4, rangeRule(var5, 0, new SimpleDateRule(var1, var2, var3 > 0 ? var3 : -var3, var3 > 0)));
   }

   public SimpleHoliday(int var1, int var2, int var3, String var4, int var5, int var6) {
      super(var4, rangeRule(var5, var6, new SimpleDateRule(var1, var2, var3 > 0 ? var3 : -var3, var3 > 0)));
   }

   private static DateRule rangeRule(int var0, int var1, DateRule var2) {
      if (var0 == 0 && var1 == 0) {
         return var2;
      } else {
         RangeDateRule var3 = new RangeDateRule();
         if (var0 != 0) {
            GregorianCalendar var4 = new GregorianCalendar(var0, 0, 1);
            var3.add(var4.getTime(), var2);
         } else {
            var3.add(var2);
         }

         if (var1 != 0) {
            Date var5 = (new GregorianCalendar(var1, 11, 31)).getTime();
            var3.add(var5, (DateRule)null);
         }

         return var3;
      }
   }
}
