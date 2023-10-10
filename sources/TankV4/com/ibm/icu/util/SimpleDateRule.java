package com.ibm.icu.util;

import java.util.Date;

public class SimpleDateRule implements DateRule {
   private static GregorianCalendar gCalendar = new GregorianCalendar();
   private Calendar calendar;
   private int month;
   private int dayOfMonth;
   private int dayOfWeek;

   public SimpleDateRule(int var1, int var2) {
      this.calendar = gCalendar;
      this.month = var1;
      this.dayOfMonth = var2;
      this.dayOfWeek = 0;
   }

   SimpleDateRule(int var1, int var2, Calendar var3) {
      this.calendar = gCalendar;
      this.month = var1;
      this.dayOfMonth = var2;
      this.dayOfWeek = 0;
      this.calendar = var3;
   }

   public SimpleDateRule(int var1, int var2, int var3, boolean var4) {
      this.calendar = gCalendar;
      this.month = var1;
      this.dayOfMonth = var2;
      this.dayOfWeek = var4 ? var3 : -var3;
   }

   public Date firstAfter(Date var1) {
      return this.doFirstBetween(var1, (Date)null);
   }

   public Date firstBetween(Date var1, Date var2) {
      return this.doFirstBetween(var1, var2);
   }

   public boolean isOn(Date var1) {
      Calendar var2 = this.calendar;
      synchronized(var2){}
      var2.setTime(var1);
      int var4 = var2.get(6);
      var2.setTime(this.computeInYear(var2.get(1), var2));
      return var2.get(6) == var4;
   }

   public boolean isBetween(Date var1, Date var2) {
      return this.firstBetween(var1, var2) != null;
   }

   private Date doFirstBetween(Date var1, Date var2) {
      Calendar var3 = this.calendar;
      synchronized(var3){}
      var3.setTime(var1);
      int var5 = var3.get(1);
      int var6 = var3.get(2);
      if (var6 > this.month) {
         ++var5;
      }

      Date var7 = this.computeInYear(var5, var3);
      if (var6 == this.month && var7.before(var1)) {
         var7 = this.computeInYear(var5 + 1, var3);
      }

      return var2 != null && var7.after(var2) ? null : var7;
   }

   private Date computeInYear(int var1, Calendar var2) {
      synchronized(var2){}
      var2.clear();
      var2.set(0, var2.getMaximum(0));
      var2.set(1, var1);
      var2.set(2, this.month);
      var2.set(5, this.dayOfMonth);
      if (this.dayOfWeek != 0) {
         var2.setTime(var2.getTime());
         int var4 = var2.get(7);
         boolean var5 = false;
         int var7;
         if (this.dayOfWeek > 0) {
            var7 = (this.dayOfWeek - var4 + 7) % 7;
         } else {
            var7 = -((this.dayOfWeek + var4 + 7) % 7);
         }

         var2.add(5, var7);
      }

      return var2.getTime();
   }
}
