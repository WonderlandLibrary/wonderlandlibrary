package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public final class CopticCalendar extends CECalendar {
   private static final long serialVersionUID = 5903818751846742911L;
   public static final int TOUT = 0;
   public static final int BABA = 1;
   public static final int HATOR = 2;
   public static final int KIAHK = 3;
   public static final int TOBA = 4;
   public static final int AMSHIR = 5;
   public static final int BARAMHAT = 6;
   public static final int BARAMOUDA = 7;
   public static final int BASHANS = 8;
   public static final int PAONA = 9;
   public static final int EPEP = 10;
   public static final int MESRA = 11;
   public static final int NASIE = 12;
   private static final int JD_EPOCH_OFFSET = 1824665;
   private static final int BCE = 0;
   private static final int CE = 1;

   public CopticCalendar() {
   }

   public CopticCalendar(TimeZone var1) {
      super(var1);
   }

   public CopticCalendar(Locale var1) {
      super(var1);
   }

   public CopticCalendar(ULocale var1) {
      super(var1);
   }

   public CopticCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
   }

   public CopticCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
   }

   public CopticCalendar(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public CopticCalendar(Date var1) {
      super(var1);
   }

   public CopticCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public String getType() {
      return "coptic";
   }

   /** @deprecated */
   protected int handleGetExtendedYear() {
      int var1;
      if (this.newerField(19, 1) == 19) {
         var1 = this.internalGet(19, 1);
      } else {
         int var2 = this.internalGet(0, 1);
         if (var2 == 0) {
            var1 = 1 - this.internalGet(1, 1);
         } else {
            var1 = this.internalGet(1, 1);
         }
      }

      return var1;
   }

   /** @deprecated */
   protected void handleComputeFields(int var1) {
      int[] var4 = new int[3];
      jdToCE(var1, this.getJDEpochOffset(), var4);
      byte var2;
      int var3;
      if (var4[0] <= 0) {
         var2 = 0;
         var3 = 1 - var4[0];
      } else {
         var2 = 1;
         var3 = var4[0];
      }

      this.internalSet(19, var4[0]);
      this.internalSet(0, var2);
      this.internalSet(1, var3);
      this.internalSet(2, var4[1]);
      this.internalSet(5, var4[2]);
      this.internalSet(6, 30 * var4[1] + var4[2]);
   }

   /** @deprecated */
   protected int getJDEpochOffset() {
      return 1824665;
   }

   public static int copticToJD(long var0, int var2, int var3) {
      return ceToJD(var0, var2, var3, 1824665);
   }
}
