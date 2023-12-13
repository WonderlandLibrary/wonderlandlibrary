package com.ibm.icu.impl.duration;

class OneOrTwoUnitBuilder extends PeriodBuilderImpl {
   OneOrTwoUnitBuilder(BasicPeriodBuilderFactory.Settings var1) {
      super(var1);
   }

   public static OneOrTwoUnitBuilder get(BasicPeriodBuilderFactory.Settings var0) {
      return var0 == null ? null : new OneOrTwoUnitBuilder(var0);
   }

   protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings var1) {
      return get(var1);
   }

   protected Period handleCreate(long var1, long var3, boolean var5) {
      Period var6 = null;
      short var7 = this.settings.effectiveSet();

      for(int var8 = 0; var8 < TimeUnit.units.length; ++var8) {
         if (0 != (var7 & 1 << var8)) {
            TimeUnit var9 = TimeUnit.units[var8];
            long var10 = this.approximateDurationOf(var9);
            if (var1 >= var10 || var6 != null) {
               double var12 = (double)var1 / (double)var10;
               if (var6 != null) {
                  if (var12 >= 1.0D) {
                     var6 = var6.and((float)var12, var9);
                  }
                  break;
               }

               if (var12 >= 2.0D) {
                  var6 = Period.at((float)var12, var9);
                  break;
               }

               var6 = Period.at(1.0F, var9).inPast(var5);
               var1 -= var10;
            }
         }
      }

      return var6;
   }
}
