package com.ibm.icu.impl.duration;

class FixedUnitBuilder extends PeriodBuilderImpl {
   private TimeUnit unit;

   public static FixedUnitBuilder get(TimeUnit var0, BasicPeriodBuilderFactory.Settings var1) {
      return var1 != null && (var1.effectiveSet() & 1 << var0.ordinal) != 0 ? new FixedUnitBuilder(var0, var1) : null;
   }

   FixedUnitBuilder(TimeUnit var1, BasicPeriodBuilderFactory.Settings var2) {
      super(var2);
      this.unit = var1;
   }

   protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings var1) {
      return get(this.unit, var1);
   }

   protected Period handleCreate(long var1, long var3, boolean var5) {
      if (this.unit == null) {
         return null;
      } else {
         long var6 = this.approximateDurationOf(this.unit);
         return Period.at((float)((double)var1 / (double)var6), this.unit).inPast(var5);
      }
   }
}
