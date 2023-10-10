package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public final class EthiopicCalendar extends CECalendar {
   private static final long serialVersionUID = -2438495771339315608L;
   public static final int MESKEREM = 0;
   public static final int TEKEMT = 1;
   public static final int HEDAR = 2;
   public static final int TAHSAS = 3;
   public static final int TER = 4;
   public static final int YEKATIT = 5;
   public static final int MEGABIT = 6;
   public static final int MIAZIA = 7;
   public static final int GENBOT = 8;
   public static final int SENE = 9;
   public static final int HAMLE = 10;
   public static final int NEHASSE = 11;
   public static final int PAGUMEN = 12;
   private static final int JD_EPOCH_OFFSET_AMETE_MIHRET = 1723856;
   private static final int AMETE_MIHRET_DELTA = 5500;
   private static final int AMETE_ALEM = 0;
   private static final int AMETE_MIHRET = 1;
   private static final int AMETE_MIHRET_ERA = 0;
   private static final int AMETE_ALEM_ERA = 1;
   private int eraType = 0;

   public EthiopicCalendar() {
   }

   public EthiopicCalendar(TimeZone var1) {
      super(var1);
   }

   public EthiopicCalendar(Locale var1) {
      super(var1);
   }

   public EthiopicCalendar(ULocale var1) {
      super(var1);
   }

   public EthiopicCalendar(TimeZone var1, Locale var2) {
      super(var1, var2);
   }

   public EthiopicCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2);
   }

   public EthiopicCalendar(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public EthiopicCalendar(Date var1) {
      super(var1);
   }

   public EthiopicCalendar(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public String getType() {
      // $FF: Couldn't be decompiled
   }

   public void setAmeteAlemEra(boolean var1) {
      this.eraType = var1 ? 1 : 0;
   }

   /** @deprecated */
   protected int handleGetExtendedYear() {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   protected void handleComputeFields(int param1) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   protected int handleGetLimit(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   protected int getJDEpochOffset() {
      return 1723856;
   }

   public static int EthiopicToJD(long var0, int var2, int var3) {
      return ceToJD(var0, var2, var3, 1723856);
   }
}
