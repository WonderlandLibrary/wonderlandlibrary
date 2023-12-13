package com.ibm.icu.impl.duration;

class MultiUnitBuilder extends PeriodBuilderImpl {
   private int nPeriods;

   MultiUnitBuilder(int var1, BasicPeriodBuilderFactory.Settings var2) {
      super(var2);
      this.nPeriods = var1;
   }

   public static MultiUnitBuilder get(int var0, BasicPeriodBuilderFactory.Settings var1) {
      return var0 > 0 && var1 != null ? new MultiUnitBuilder(var0, var1) : null;
   }

   protected PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings var1) {
      return get(this.nPeriods, var1);
   }

   protected Period handleCreate(long var1, long var3, boolean var5) {
      Period var6 = null;
      int var7 = 0;
      short var8 = this.settings.effectiveSet();

      for(int var9 = 0; var9 < TimeUnit.units.length; ++var9) {
         if (0 != (var8 & 1 << var9)) {
            TimeUnit var10 = TimeUnit.units[var9];
            if (var7 == this.nPeriods) {
               break;
            }

            long var11 = this.approximateDurationOf(var10);
            if (var1 >= var11 || var7 > 0) {
               ++var7;
               double var13 = (double)var1 / (double)var11;
               if (var7 < this.nPeriods) {
                  var13 = Math.floor(var13);
                  var1 -= (long)(var13 * (double)var11);
               }

               if (var6 == null) {
                  var6 = Period.at((float)var13, var10).inPast(var5);
               } else {
                  var6 = var6.and((float)var13, var10);
               }
            }
         }
      }

      return var6;
   }
}
