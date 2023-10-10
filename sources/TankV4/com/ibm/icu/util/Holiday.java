package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;

public abstract class Holiday implements DateRule {
   private String name;
   private DateRule rule;
   private static Holiday[] noHolidays = new Holiday[0];

   public static Holiday[] getHolidays() {
      return getHolidays(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static Holiday[] getHolidays(Locale var0) {
      return getHolidays(ULocale.forLocale(var0));
   }

   public static Holiday[] getHolidays(ULocale var0) {
      Holiday[] var1 = noHolidays;

      try {
         UResourceBundle var2 = UResourceBundle.getBundleInstance("com.ibm.icu.impl.data.HolidayBundle", var0);
         var1 = (Holiday[])((Holiday[])var2.getObject("holidays"));
      } catch (MissingResourceException var3) {
      }

      return var1;
   }

   public Date firstAfter(Date var1) {
      return this.rule.firstAfter(var1);
   }

   public Date firstBetween(Date var1, Date var2) {
      return this.rule.firstBetween(var1, var2);
   }

   public boolean isOn(Date var1) {
      return this.rule.isOn(var1);
   }

   public boolean isBetween(Date var1, Date var2) {
      return this.rule.isBetween(var1, var2);
   }

   protected Holiday(String var1, DateRule var2) {
      this.name = var1;
      this.rule = var2;
   }

   public String getDisplayName() {
      return this.getDisplayName(ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public String getDisplayName(Locale var1) {
      return this.getDisplayName(ULocale.forLocale(var1));
   }

   public String getDisplayName(ULocale var1) {
      String var2 = this.name;

      try {
         UResourceBundle var3 = UResourceBundle.getBundleInstance("com.ibm.icu.impl.data.HolidayBundle", var1);
         var2 = var3.getString(this.name);
      } catch (MissingResourceException var4) {
      }

      return var2;
   }

   public DateRule getRule() {
      return this.rule;
   }

   public void setRule(DateRule var1) {
      this.rule = var1;
   }
}
