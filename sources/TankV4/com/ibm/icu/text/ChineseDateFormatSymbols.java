package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

/** @deprecated */
public class ChineseDateFormatSymbols extends DateFormatSymbols {
   static final long serialVersionUID = 6827816119783952890L;
   String[] isLeapMonth;

   /** @deprecated */
   public ChineseDateFormatSymbols() {
      this(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   /** @deprecated */
   public ChineseDateFormatSymbols(Locale var1) {
      super(ChineseCalendar.class, ULocale.forLocale(var1));
   }

   /** @deprecated */
   public ChineseDateFormatSymbols(ULocale var1) {
      super(ChineseCalendar.class, var1);
   }

   /** @deprecated */
   public ChineseDateFormatSymbols(Calendar var1, Locale var2) {
      super(var1 == null ? null : var1.getClass(), var2);
   }

   /** @deprecated */
   public ChineseDateFormatSymbols(Calendar var1, ULocale var2) {
      super(var1 == null ? null : var1.getClass(), var2);
   }

   /** @deprecated */
   public String getLeapMonth(int var1) {
      return this.isLeapMonth[var1];
   }

   /** @deprecated */
   protected void initializeData(ULocale var1, CalendarData var2) {
      super.initializeData(var1, var2);
      this.initializeIsLeapMonth();
   }

   void initializeData(DateFormatSymbols var1) {
      super.initializeData(var1);
      if (var1 instanceof ChineseDateFormatSymbols) {
         this.isLeapMonth = ((ChineseDateFormatSymbols)var1).isLeapMonth;
      } else {
         this.initializeIsLeapMonth();
      }

   }

   private void initializeIsLeapMonth() {
      this.isLeapMonth = new String[2];
      this.isLeapMonth[0] = "";
      this.isLeapMonth[1] = this.leapMonthPatterns != null ? this.leapMonthPatterns[0].replace("{0}", "") : "";
   }
}
