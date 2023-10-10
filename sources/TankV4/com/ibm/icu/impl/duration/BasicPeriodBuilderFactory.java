package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import java.util.TimeZone;

class BasicPeriodBuilderFactory implements PeriodBuilderFactory {
   private PeriodFormatterDataService ds;
   private BasicPeriodBuilderFactory.Settings settings;
   private static final short allBits = 255;

   BasicPeriodBuilderFactory(PeriodFormatterDataService var1) {
      this.ds = var1;
      this.settings = new BasicPeriodBuilderFactory.Settings(this);
   }

   static long approximateDurationOf(TimeUnit var0) {
      return TimeUnit.approxDurations[var0.ordinal];
   }

   public PeriodBuilderFactory setAvailableUnitRange(TimeUnit var1, TimeUnit var2) {
      int var3 = 0;

      for(int var4 = var2.ordinal; var4 <= var1.ordinal; ++var4) {
         var3 |= 1 << var4;
      }

      if (var3 == 0) {
         throw new IllegalArgumentException("range " + var1 + " to " + var2 + " is empty");
      } else {
         this.settings = this.settings.setUnits(var3);
         return this;
      }
   }

   public PeriodBuilderFactory setUnitIsAvailable(TimeUnit var1, boolean var2) {
      short var3 = this.settings.uset;
      int var4;
      if (var2) {
         var4 = var3 | 1 << var1.ordinal;
      } else {
         var4 = var3 & ~(1 << var1.ordinal);
      }

      this.settings = this.settings.setUnits(var4);
      return this;
   }

   public PeriodBuilderFactory setMaxLimit(float var1) {
      this.settings = this.settings.setMaxLimit(var1);
      return this;
   }

   public PeriodBuilderFactory setMinLimit(float var1) {
      this.settings = this.settings.setMinLimit(var1);
      return this;
   }

   public PeriodBuilderFactory setAllowZero(boolean var1) {
      this.settings = this.settings.setAllowZero(var1);
      return this;
   }

   public PeriodBuilderFactory setWeeksAloneOnly(boolean var1) {
      this.settings = this.settings.setWeeksAloneOnly(var1);
      return this;
   }

   public PeriodBuilderFactory setAllowMilliseconds(boolean var1) {
      this.settings = this.settings.setAllowMilliseconds(var1);
      return this;
   }

   public PeriodBuilderFactory setLocale(String var1) {
      this.settings = this.settings.setLocale(var1);
      return this;
   }

   public PeriodBuilderFactory setTimeZone(TimeZone var1) {
      return this;
   }

   private BasicPeriodBuilderFactory.Settings getSettings() {
      return this.settings.effectiveSet() == 0 ? null : this.settings.setInUse();
   }

   public PeriodBuilder getFixedUnitBuilder(TimeUnit var1) {
      return FixedUnitBuilder.get(var1, this.getSettings());
   }

   public PeriodBuilder getSingleUnitBuilder() {
      return SingleUnitBuilder.get(this.getSettings());
   }

   public PeriodBuilder getOneOrTwoUnitBuilder() {
      return OneOrTwoUnitBuilder.get(this.getSettings());
   }

   public PeriodBuilder getMultiUnitBuilder(int var1) {
      return MultiUnitBuilder.get(var1, this.getSettings());
   }

   static PeriodFormatterDataService access$000(BasicPeriodBuilderFactory var0) {
      return var0.ds;
   }

   class Settings {
      boolean inUse;
      short uset;
      TimeUnit maxUnit;
      TimeUnit minUnit;
      int maxLimit;
      int minLimit;
      boolean allowZero;
      boolean weeksAloneOnly;
      boolean allowMillis;
      final BasicPeriodBuilderFactory this$0;

      Settings(BasicPeriodBuilderFactory var1) {
         this.this$0 = var1;
         this.uset = 255;
         this.maxUnit = TimeUnit.YEAR;
         this.minUnit = TimeUnit.MILLISECOND;
         this.allowZero = true;
         this.allowMillis = true;
      }

      BasicPeriodBuilderFactory.Settings setUnits(int var1) {
         if (this.uset == var1) {
            return this;
         } else {
            BasicPeriodBuilderFactory.Settings var2 = this.inUse ? this.copy() : this;
            var2.uset = (short)var1;
            if ((var1 & 255) == 255) {
               var2.uset = 255;
               var2.maxUnit = TimeUnit.YEAR;
               var2.minUnit = TimeUnit.MILLISECOND;
            } else {
               int var3 = -1;

               for(int var4 = 0; var4 < TimeUnit.units.length; ++var4) {
                  if (0 != (var1 & 1 << var4)) {
                     if (var3 == -1) {
                        var2.maxUnit = TimeUnit.units[var4];
                     }

                     var3 = var4;
                  }
               }

               if (var3 == -1) {
                  var2.minUnit = var2.maxUnit = null;
               } else {
                  var2.minUnit = TimeUnit.units[var3];
               }
            }

            return var2;
         }
      }

      short effectiveSet() {
         return this.allowMillis ? this.uset : (short)(this.uset & ~(1 << TimeUnit.MILLISECOND.ordinal));
      }

      TimeUnit effectiveMinUnit() {
         if (!this.allowMillis && this.minUnit == TimeUnit.MILLISECOND) {
            int var1 = TimeUnit.units.length - 1;

            do {
               --var1;
               if (var1 < 0) {
                  return TimeUnit.SECOND;
               }
            } while(0 == (this.uset & 1 << var1));

            return TimeUnit.units[var1];
         } else {
            return this.minUnit;
         }
      }

      BasicPeriodBuilderFactory.Settings setMaxLimit(float var1) {
         int var2 = var1 <= 0.0F ? 0 : (int)(var1 * 1000.0F);
         if (var1 == (float)var2) {
            return this;
         } else {
            BasicPeriodBuilderFactory.Settings var3 = this.inUse ? this.copy() : this;
            var3.maxLimit = var2;
            return var3;
         }
      }

      BasicPeriodBuilderFactory.Settings setMinLimit(float var1) {
         int var2 = var1 <= 0.0F ? 0 : (int)(var1 * 1000.0F);
         if (var1 == (float)var2) {
            return this;
         } else {
            BasicPeriodBuilderFactory.Settings var3 = this.inUse ? this.copy() : this;
            var3.minLimit = var2;
            return var3;
         }
      }

      BasicPeriodBuilderFactory.Settings setAllowZero(boolean var1) {
         if (this.allowZero == var1) {
            return this;
         } else {
            BasicPeriodBuilderFactory.Settings var2 = this.inUse ? this.copy() : this;
            var2.allowZero = var1;
            return var2;
         }
      }

      BasicPeriodBuilderFactory.Settings setWeeksAloneOnly(boolean var1) {
         if (this.weeksAloneOnly == var1) {
            return this;
         } else {
            BasicPeriodBuilderFactory.Settings var2 = this.inUse ? this.copy() : this;
            var2.weeksAloneOnly = var1;
            return var2;
         }
      }

      BasicPeriodBuilderFactory.Settings setAllowMilliseconds(boolean var1) {
         if (this.allowMillis == var1) {
            return this;
         } else {
            BasicPeriodBuilderFactory.Settings var2 = this.inUse ? this.copy() : this;
            var2.allowMillis = var1;
            return var2;
         }
      }

      BasicPeriodBuilderFactory.Settings setLocale(String var1) {
         PeriodFormatterData var2 = BasicPeriodBuilderFactory.access$000(this.this$0).get(var1);
         return this.setAllowZero(var2.allowZero()).setWeeksAloneOnly(var2.weeksAloneOnly()).setAllowMilliseconds(var2.useMilliseconds() != 1);
      }

      BasicPeriodBuilderFactory.Settings setInUse() {
         this.inUse = true;
         return this;
      }

      Period createLimited(long var1, boolean var3) {
         if (this.maxLimit > 0) {
            long var4 = BasicPeriodBuilderFactory.approximateDurationOf(this.maxUnit);
            if (var1 * 1000L > (long)this.maxLimit * var4) {
               return Period.moreThan((float)this.maxLimit / 1000.0F, this.maxUnit).inPast(var3);
            }
         }

         if (this.minLimit > 0) {
            TimeUnit var9 = this.effectiveMinUnit();
            long var5 = BasicPeriodBuilderFactory.approximateDurationOf(var9);
            long var7 = var9 == this.minUnit ? (long)this.minLimit : Math.max(1000L, BasicPeriodBuilderFactory.approximateDurationOf(this.minUnit) * (long)this.minLimit / var5);
            if (var1 * 1000L < var7 * var5) {
               return Period.lessThan((float)var7 / 1000.0F, var9).inPast(var3);
            }
         }

         return null;
      }

      public BasicPeriodBuilderFactory.Settings copy() {
         BasicPeriodBuilderFactory.Settings var1 = this.this$0.new Settings(this.this$0);
         var1.inUse = this.inUse;
         var1.uset = this.uset;
         var1.maxUnit = this.maxUnit;
         var1.minUnit = this.minUnit;
         var1.maxLimit = this.maxLimit;
         var1.minLimit = this.minLimit;
         var1.allowZero = this.allowZero;
         var1.weeksAloneOnly = this.weeksAloneOnly;
         var1.allowMillis = this.allowMillis;
         return var1;
      }
   }
}
