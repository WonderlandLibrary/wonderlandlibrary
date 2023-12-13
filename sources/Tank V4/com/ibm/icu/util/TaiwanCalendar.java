package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class TaiwanCalendar extends GregorianCalendar {
   private static final long serialVersionUID = 2583005278132380631L;
   public static final int BEFORE_MINGUO = 0;
   public static final int MINGUO = 1;
   private static final int Taiwan_ERA_START = 1911;
   private static final int GREGORIAN_EPOCH = 1970;

   public TaiwanCalendar() {
   }

   public TaiwanCalendar(TimeZone var1) {
      super(var1);
   }

   public TaiwanCalendar(Locale var1) {
      super(var1);
   }

   public TaiwanCalendar(ULocale var1) {
      super(var1);
   }

   public TaiwanCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
   }

   public TaiwanCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
   }

   public TaiwanCalendar(Date var1) {
      this();
      this.setTime(var1);
   }

   public TaiwanCalendar(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public TaiwanCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   protected int handleGetExtendedYear() {
      boolean var1 = true;
      int var3;
      if (this.newerField(19, 1) == 19 && this.newerField(19, 0) == 19) {
         var3 = this.internalGet(19, 1970);
      } else {
         int var2 = this.internalGet(0, 1);
         if (var2 == 1) {
            var3 = this.internalGet(1, 1) + 1911;
         } else {
            var3 = 1 - this.internalGet(1, 1) + 1911;
         }
      }

      return var3;
   }

   protected void handleComputeFields(int var1) {
      super.handleComputeFields(var1);
      int var2 = this.internalGet(19) - 1911;
      if (var2 > 0) {
         this.internalSet(0, 1);
         this.internalSet(1, var2);
      } else {
         this.internalSet(0, 0);
         this.internalSet(1, 1 - var2);
      }

   }

   protected int handleGetLimit(int var1, int var2) {
      if (var1 == 0) {
         return var2 != 0 && var2 != 1 ? 1 : 0;
      } else {
         return super.handleGetLimit(var1, var2);
      }
   }

   public String getType() {
      return "roc";
   }
}
