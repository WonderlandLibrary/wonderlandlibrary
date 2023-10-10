package com.ibm.icu.impl.duration;

import java.util.TimeZone;

abstract class PeriodBuilderImpl implements PeriodBuilder {
   protected BasicPeriodBuilderFactory.Settings settings;

   public Period create(long var1) {
      return this.createWithReferenceDate(var1, System.currentTimeMillis());
   }

   public long approximateDurationOf(TimeUnit var1) {
      return BasicPeriodBuilderFactory.approximateDurationOf(var1);
   }

   public Period createWithReferenceDate(long var1, long var3) {
      boolean var5 = var1 < 0L;
      if (var5) {
         var1 = -var1;
      }

      Period var6 = this.settings.createLimited(var1, var5);
      if (var6 == null) {
         var6 = this.handleCreate(var1, var3, var5);
         if (var6 == null) {
            var6 = Period.lessThan(1.0F, this.settings.effectiveMinUnit()).inPast(var5);
         }
      }

      return var6;
   }

   public PeriodBuilder withTimeZone(TimeZone var1) {
      return this;
   }

   public PeriodBuilder withLocale(String var1) {
      BasicPeriodBuilderFactory.Settings var2 = this.settings.setLocale(var1);
      return (PeriodBuilder)(var2 != this.settings ? this.withSettings(var2) : this);
   }

   protected abstract PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings var1);

   protected abstract Period handleCreate(long var1, long var3, boolean var5);

   protected PeriodBuilderImpl(BasicPeriodBuilderFactory.Settings var1) {
      this.settings = var1;
   }
}
