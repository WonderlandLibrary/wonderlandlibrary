package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

class BasicDurationFormatter implements DurationFormatter {
   private PeriodFormatter formatter;
   private PeriodBuilder builder;
   private DateFormatter fallback;
   private long fallbackLimit;
   private String localeName;
   private TimeZone timeZone;

   public BasicDurationFormatter(PeriodFormatter var1, PeriodBuilder var2, DateFormatter var3, long var4) {
      this.formatter = var1;
      this.builder = var2;
      this.fallback = var3;
      this.fallbackLimit = var4 < 0L ? 0L : var4;
   }

   protected BasicDurationFormatter(PeriodFormatter var1, PeriodBuilder var2, DateFormatter var3, long var4, String var6, TimeZone var7) {
      this.formatter = var1;
      this.builder = var2;
      this.fallback = var3;
      this.fallbackLimit = var4;
      this.localeName = var6;
      this.timeZone = var7;
   }

   public String formatDurationFromNowTo(Date var1) {
      long var2 = System.currentTimeMillis();
      long var4 = var2 - var1.getTime();
      return this.formatDurationFrom(var4, var2);
   }

   public String formatDurationFromNow(long var1) {
      return this.formatDurationFrom(var1, System.currentTimeMillis());
   }

   public String formatDurationFrom(long var1, long var3) {
      String var5 = this.doFallback(var1, var3);
      if (var5 == null) {
         Period var6 = this.doBuild(var1, var3);
         var5 = this.doFormat(var6);
      }

      return var5;
   }

   public DurationFormatter withLocale(String var1) {
      if (!var1.equals(this.localeName)) {
         PeriodFormatter var2 = this.formatter.withLocale(var1);
         PeriodBuilder var3 = this.builder.withLocale(var1);
         DateFormatter var4 = this.fallback == null ? null : this.fallback.withLocale(var1);
         return new BasicDurationFormatter(var2, var3, var4, this.fallbackLimit, var1, this.timeZone);
      } else {
         return this;
      }
   }

   public DurationFormatter withTimeZone(TimeZone var1) {
      if (!var1.equals(this.timeZone)) {
         PeriodBuilder var2 = this.builder.withTimeZone(var1);
         DateFormatter var3 = this.fallback == null ? null : this.fallback.withTimeZone(var1);
         return new BasicDurationFormatter(this.formatter, var2, var3, this.fallbackLimit, this.localeName, var1);
      } else {
         return this;
      }
   }

   protected String doFallback(long var1, long var3) {
      return this.fallback != null && this.fallbackLimit > 0L && Math.abs(var1) >= this.fallbackLimit ? this.fallback.format(var3 + var1) : null;
   }

   protected Period doBuild(long var1, long var3) {
      return this.builder.createWithReferenceDate(var1, var3);
   }

   protected String doFormat(Period var1) {
      if (!var1.isSet()) {
         throw new IllegalArgumentException("period is not set");
      } else {
         return this.formatter.format(var1);
      }
   }
}
