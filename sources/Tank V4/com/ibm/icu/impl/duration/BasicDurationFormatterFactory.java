package com.ibm.icu.impl.duration;

import java.util.Locale;
import java.util.TimeZone;

class BasicDurationFormatterFactory implements DurationFormatterFactory {
   private BasicPeriodFormatterService ps;
   private PeriodFormatter formatter;
   private PeriodBuilder builder;
   private DateFormatter fallback;
   private long fallbackLimit;
   private String localeName;
   private TimeZone timeZone;
   private BasicDurationFormatter f;

   BasicDurationFormatterFactory(BasicPeriodFormatterService var1) {
      this.ps = var1;
      this.localeName = Locale.getDefault().toString();
      this.timeZone = TimeZone.getDefault();
   }

   public DurationFormatterFactory setPeriodFormatter(PeriodFormatter var1) {
      if (var1 != this.formatter) {
         this.formatter = var1;
         this.reset();
      }

      return this;
   }

   public DurationFormatterFactory setPeriodBuilder(PeriodBuilder var1) {
      if (var1 != this.builder) {
         this.builder = var1;
         this.reset();
      }

      return this;
   }

   public DurationFormatterFactory setFallback(DateFormatter var1) {
      boolean var2 = var1 == null ? this.fallback != null : !var1.equals(this.fallback);
      if (var2) {
         this.fallback = var1;
         this.reset();
      }

      return this;
   }

   public DurationFormatterFactory setFallbackLimit(long var1) {
      if (var1 < 0L) {
         var1 = 0L;
      }

      if (var1 != this.fallbackLimit) {
         this.fallbackLimit = var1;
         this.reset();
      }

      return this;
   }

   public DurationFormatterFactory setLocale(String var1) {
      if (!var1.equals(this.localeName)) {
         this.localeName = var1;
         if (this.builder != null) {
            this.builder = this.builder.withLocale(var1);
         }

         if (this.formatter != null) {
            this.formatter = this.formatter.withLocale(var1);
         }

         this.reset();
      }

      return this;
   }

   public DurationFormatterFactory setTimeZone(TimeZone var1) {
      if (!var1.equals(this.timeZone)) {
         this.timeZone = var1;
         if (this.builder != null) {
            this.builder = this.builder.withTimeZone(var1);
         }

         this.reset();
      }

      return this;
   }

   public DurationFormatter getFormatter() {
      if (this.f == null) {
         if (this.fallback != null) {
            this.fallback = this.fallback.withLocale(this.localeName).withTimeZone(this.timeZone);
         }

         this.formatter = this.getPeriodFormatter();
         this.builder = this.getPeriodBuilder();
         this.f = this.createFormatter();
      }

      return this.f;
   }

   public PeriodFormatter getPeriodFormatter() {
      if (this.formatter == null) {
         this.formatter = this.ps.newPeriodFormatterFactory().setLocale(this.localeName).getFormatter();
      }

      return this.formatter;
   }

   public PeriodBuilder getPeriodBuilder() {
      if (this.builder == null) {
         this.builder = this.ps.newPeriodBuilderFactory().setLocale(this.localeName).setTimeZone(this.timeZone).getSingleUnitBuilder();
      }

      return this.builder;
   }

   public DateFormatter getFallback() {
      return this.fallback;
   }

   public long getFallbackLimit() {
      return this.fallback == null ? 0L : this.fallbackLimit;
   }

   public String getLocaleName() {
      return this.localeName;
   }

   public TimeZone getTimeZone() {
      return this.timeZone;
   }

   protected BasicDurationFormatter createFormatter() {
      return new BasicDurationFormatter(this.formatter, this.builder, this.fallback, this.fallbackLimit, this.localeName, this.timeZone);
   }

   protected void reset() {
      this.f = null;
   }
}
