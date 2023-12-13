package com.ibm.icu.util;

import java.io.Serializable;

public class DateTimeRule implements Serializable {
   private static final long serialVersionUID = 2183055795738051443L;
   public static final int DOM = 0;
   public static final int DOW = 1;
   public static final int DOW_GEQ_DOM = 2;
   public static final int DOW_LEQ_DOM = 3;
   public static final int WALL_TIME = 0;
   public static final int STANDARD_TIME = 1;
   public static final int UTC_TIME = 2;
   private final int dateRuleType;
   private final int month;
   private final int dayOfMonth;
   private final int dayOfWeek;
   private final int weekInMonth;
   private final int timeRuleType;
   private final int millisInDay;
   private static final String[] DOWSTR = new String[]{"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
   private static final String[] MONSTR = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

   public DateTimeRule(int var1, int var2, int var3, int var4) {
      this.dateRuleType = 0;
      this.month = var1;
      this.dayOfMonth = var2;
      this.millisInDay = var3;
      this.timeRuleType = var4;
      this.dayOfWeek = 0;
      this.weekInMonth = 0;
   }

   public DateTimeRule(int var1, int var2, int var3, int var4, int var5) {
      this.dateRuleType = 1;
      this.month = var1;
      this.weekInMonth = var2;
      this.dayOfWeek = var3;
      this.millisInDay = var4;
      this.timeRuleType = var5;
      this.dayOfMonth = 0;
   }

   public DateTimeRule(int var1, int var2, int var3, boolean var4, int var5, int var6) {
      this.dateRuleType = var4 ? 2 : 3;
      this.month = var1;
      this.dayOfMonth = var2;
      this.dayOfWeek = var3;
      this.millisInDay = var5;
      this.timeRuleType = var6;
      this.weekInMonth = 0;
   }

   public int getDateRuleType() {
      return this.dateRuleType;
   }

   public int getRuleMonth() {
      return this.month;
   }

   public int getRuleDayOfMonth() {
      return this.dayOfMonth;
   }

   public int getRuleDayOfWeek() {
      return this.dayOfWeek;
   }

   public int getRuleWeekInMonth() {
      return this.weekInMonth;
   }

   public int getTimeRuleType() {
      return this.timeRuleType;
   }

   public int getRuleMillisInDay() {
      return this.millisInDay;
   }

   public String toString() {
      String var1 = null;
      String var2 = null;
      switch(this.dateRuleType) {
      case 0:
         var1 = Integer.toString(this.dayOfMonth);
         break;
      case 1:
         var1 = Integer.toString(this.weekInMonth) + DOWSTR[this.dayOfWeek];
         break;
      case 2:
         var1 = DOWSTR[this.dayOfWeek] + ">=" + Integer.toString(this.dayOfMonth);
         break;
      case 3:
         var1 = DOWSTR[this.dayOfWeek] + "<=" + Integer.toString(this.dayOfMonth);
      }

      switch(this.timeRuleType) {
      case 0:
         var2 = "WALL";
         break;
      case 1:
         var2 = "STD";
         break;
      case 2:
         var2 = "UTC";
      }

      int var3 = this.millisInDay;
      int var4 = var3 % 1000;
      var3 /= 1000;
      int var5 = var3 % 60;
      var3 /= 60;
      int var6 = var3 % 60;
      int var7 = var3 / 60;
      StringBuilder var8 = new StringBuilder();
      var8.append("month=");
      var8.append(MONSTR[this.month]);
      var8.append(", date=");
      var8.append(var1);
      var8.append(", time=");
      var8.append(var7);
      var8.append(":");
      var8.append(var6 / 10);
      var8.append(var6 % 10);
      var8.append(":");
      var8.append(var5 / 10);
      var8.append(var5 % 10);
      var8.append(".");
      var8.append(var4 / 100);
      var8.append(var4 / 10 % 10);
      var8.append(var4 % 10);
      var8.append("(");
      var8.append(var2);
      var8.append(")");
      return var8.toString();
   }
}
