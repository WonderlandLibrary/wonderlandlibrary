package com.ibm.icu.impl.duration;

class SingleUnitBuilder extends PeriodBuilderImpl {
   SingleUnitBuilder(BasicPeriodBuilderFactory.Settings var1) {
      super(var1);
   }

   public static SingleUnitBuilder get(BasicPeriodBuilderFactory.Settings var0) {
      return var0 == null ? null : new SingleUnitBuilder(var0);
   }

   protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings var1) {
      return get(var1);
   }

   protected Period handleCreate(long var1, long var3, boolean var5) {
      short var6 = this.settings.effectiveSet();

      for(int var7 = 0; var7 < TimeUnit.units.length; ++var7) {
         if (0 != (var6 & 1 << var7)) {
            TimeUnit var8 = TimeUnit.units[var7];
            long var9 = this.approximateDurationOf(var8);
            if (var1 >= var9) {
               return Period.at((float)((double)var1 / (double)var9), var8).inPast(var5);
            }
         }
      }

      return null;
   }
}
