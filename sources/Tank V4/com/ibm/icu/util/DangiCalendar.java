package com.ibm.icu.util;

import java.util.Date;

/** @deprecated */
public class DangiCalendar extends ChineseCalendar {
   private static final long serialVersionUID = 8156297445349501985L;
   private static final int DANGI_EPOCH_YEAR = -2332;
   private static final TimeZone KOREA_ZONE;

   /** @deprecated */
   public DangiCalendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   /** @deprecated */
   public DangiCalendar(Date var1) {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
      this.setTime(var1);
   }

   /** @deprecated */
   public DangiCalendar(TimeZone var1, ULocale var2) {
      super(var1, var2, -2332, KOREA_ZONE);
   }

   /** @deprecated */
   public String getType() {
      return "dangi";
   }

   static {
      InitialTimeZoneRule var0 = new InitialTimeZoneRule("GMT+8", 28800000, 0);
      long[] var1 = new long[]{-2302128000000L};
      long[] var2 = new long[]{-2270592000000L};
      long[] var3 = new long[]{-1829088000000L};
      TimeArrayTimeZoneRule var4 = new TimeArrayTimeZoneRule("Korean 1897", 25200000, 0, var1, 1);
      TimeArrayTimeZoneRule var5 = new TimeArrayTimeZoneRule("Korean 1898-1911", 28800000, 0, var2, 1);
      TimeArrayTimeZoneRule var6 = new TimeArrayTimeZoneRule("Korean 1912-", 32400000, 0, var3, 1);
      RuleBasedTimeZone var7 = new RuleBasedTimeZone("KOREA_ZONE", var0);
      var7.addTransitionRule(var4);
      var7.addTransitionRule(var5);
      var7.addTransitionRule(var6);
      var7.freeze();
      KOREA_ZONE = var7;
   }
}
