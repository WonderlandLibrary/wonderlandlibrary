package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class BuddhistCalendar extends GregorianCalendar {
   private static final long serialVersionUID = 2583005278132380631L;
   public static final int BE = 0;
   private static final int BUDDHIST_ERA_START = -543;
   private static final int GREGORIAN_EPOCH = 1970;

   public BuddhistCalendar() {
   }

   public BuddhistCalendar(TimeZone var1) {
      super(var1);
   }

   public BuddhistCalendar(Locale var1) {
      super(var1);
   }

   public BuddhistCalendar(ULocale var1) {
      super(var1);
   }

   public BuddhistCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
   }

   public BuddhistCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
   }

   public BuddhistCalendar(Date var1) {
      this();
      this.setTime(var1);
   }

   public BuddhistCalendar(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public BuddhistCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   protected int handleGetExtendedYear() {
      int var1;
      if (this.newerField(19, 1) == 19) {
         var1 = this.internalGet(19, 1970);
      } else {
         var1 = this.internalGet(1, 2513) + -543;
      }

      return var1;
   }

   protected int handleComputeMonthStart(int var1, int var2, boolean var3) {
      return super.handleComputeMonthStart(var1, var2, var3);
   }

   protected void handleComputeFields(int var1) {
      super.handleComputeFields(var1);
      int var2 = this.internalGet(19) - -543;
      this.internalSet(0, 0);
      this.internalSet(1, var2);
   }

   protected int handleGetLimit(int var1, int var2) {
      return var1 == 0 ? 0 : super.handleGetLimit(var1, var2);
   }

   public String getType() {
      return "buddhist";
   }
}
