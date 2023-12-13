package com.ibm.icu.util;

import java.util.Date;

class EasterRule implements DateRule {
   private static GregorianCalendar gregorian = new GregorianCalendar();
   private static GregorianCalendar orthodox = new GregorianCalendar();
   private int daysAfterEaster;
   private GregorianCalendar calendar;

   public EasterRule(int var1, boolean var2) {
      this.calendar = gregorian;
      this.daysAfterEaster = var1;
      if (var2) {
         orthodox.setGregorianChange(new Date(Long.MAX_VALUE));
         this.calendar = orthodox;
      }

   }

   public Date firstAfter(Date var1) {
      return this.doFirstBetween(var1, (Date)null);
   }

   public Date firstBetween(Date var1, Date var2) {
      return this.doFirstBetween(var1, var2);
   }

   public boolean isOn(Date var1) {
      GregorianCalendar var2;
      synchronized(var2 = this.calendar){}
      this.calendar.setTime(var1);
      int var3 = this.calendar.get(6);
      this.calendar.setTime(this.computeInYear(this.calendar.getTime(), this.calendar));
      return this.calendar.get(6) == var3;
   }

   public boolean isBetween(Date var1, Date var2) {
      return this.firstBetween(var1, var2) != null;
   }

   private Date doFirstBetween(Date var1, Date var2) {
      GregorianCalendar var3;
      synchronized(var3 = this.calendar){}
      Date var4 = this.computeInYear(var1, this.calendar);
      if (var4.before(var1)) {
         this.calendar.setTime(var1);
         this.calendar.get(1);
         this.calendar.add(1, 1);
         var4 = this.computeInYear(this.calendar.getTime(), this.calendar);
      }

      return var2 != null && var4.after(var2) ? null : var4;
   }

   private Date computeInYear(Date var1, GregorianCalendar var2) {
      if (var2 == null) {
         var2 = this.calendar;
      }

      synchronized(var2){}
      var2.setTime(var1);
      int var4 = var2.get(1);
      int var5 = var4 % 19;
      boolean var6 = false;
      boolean var7 = false;
      int var8;
      int var9;
      int var12;
      int var13;
      if (var2.getTime().after(var2.getGregorianChange())) {
         var8 = var4 / 100;
         var9 = (var8 - var8 / 4 - (8 * var8 + 13) / 25 + 19 * var5 + 15) % 30;
         var12 = var9 - var9 / 28 * (1 - var9 / 28 * (29 / (var9 + 1)) * ((21 - var5) / 11));
         var13 = (var4 + var4 / 4 + var12 + 2 - var8 + var8 / 4) % 7;
      } else {
         var12 = (19 * var5 + 15) % 30;
         var13 = (var4 + var4 / 4 + var12) % 7;
      }

      var8 = var12 - var13;
      var9 = 3 + (var8 + 40) / 44;
      int var10 = var8 + 28 - 31 * (var9 / 4);
      var2.clear();
      var2.set(0, 1);
      var2.set(1, var4);
      var2.set(2, var9 - 1);
      var2.set(5, var10);
      var2.getTime();
      var2.add(5, this.daysAfterEaster);
      return var2.getTime();
   }
}
