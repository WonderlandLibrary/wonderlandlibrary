package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;

class BasicPeriodFormatter implements PeriodFormatter {
   private BasicPeriodFormatterFactory factory;
   private String localeName;
   private PeriodFormatterData data;
   private BasicPeriodFormatterFactory.Customizations customs;

   BasicPeriodFormatter(BasicPeriodFormatterFactory var1, String var2, PeriodFormatterData var3, BasicPeriodFormatterFactory.Customizations var4) {
      this.factory = var1;
      this.localeName = var2;
      this.data = var3;
      this.customs = var4;
   }

   public String format(Period var1) {
      if (!var1.isSet()) {
         throw new IllegalArgumentException("period is not set");
      } else {
         return this.format(var1.timeLimit, var1.inFuture, var1.counts);
      }
   }

   public PeriodFormatter withLocale(String var1) {
      if (!this.localeName.equals(var1)) {
         PeriodFormatterData var2 = this.factory.getData(var1);
         return new BasicPeriodFormatter(this.factory, var1, var2, this.customs);
      } else {
         return this;
      }
   }

   private String format(int var1, boolean var2, int[] var3) {
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5] > 0) {
            var4 |= 1 << var5;
         }
      }

      int var6;
      if (!this.data.allowZero()) {
         var5 = 0;

         for(var6 = 1; var5 < var3.length; var6 <<= 1) {
            if ((var4 & var6) != 0 && var3[var5] == 1) {
               var4 &= ~var6;
            }

            ++var5;
         }

         if (var4 == 0) {
            return null;
         }
      }

      boolean var26 = false;
      int var9;
      if (this.data.useMilliseconds() != 0 && (var4 & 1 << TimeUnit.MILLISECOND.ordinal) != 0) {
         byte var27 = TimeUnit.SECOND.ordinal;
         byte var7 = TimeUnit.MILLISECOND.ordinal;
         int var8 = 1 << var27;
         var9 = 1 << var7;
         switch(this.data.useMilliseconds()) {
         case 1:
            if ((var4 & var8) == 0) {
               var4 |= var8;
               var3[var27] = 1;
            }

            var3[var27] += (var3[var7] - 1) / 1000;
            var4 &= ~var9;
            var26 = true;
            break;
         case 2:
            if ((var4 & var8) != 0) {
               var3[var27] += (var3[var7] - 1) / 1000;
               var4 &= ~var9;
               var26 = true;
            }
         }
      }

      var6 = 0;

      int var28;
      for(var28 = var3.length - 1; var6 < var3.length && (var4 & 1 << var6) == 0; ++var6) {
      }

      while(var28 > var6 && (var4 & 1 << var28) == 0) {
         --var28;
      }

      boolean var29 = true;

      for(var9 = var6; var9 <= var28; ++var9) {
         if ((var4 & 1 << var9) != 0 && var3[var9] > 1) {
            var29 = false;
            break;
         }
      }

      StringBuffer var30 = new StringBuffer();
      if (!this.customs.displayLimit || var29) {
         var1 = 0;
      }

      int var10;
      if (this.customs.displayDirection && !var29) {
         var10 = var2 ? 2 : 1;
      } else {
         var10 = 0;
      }

      boolean var11 = this.data.appendPrefix(var1, var10, var30);
      boolean var12 = var6 != var28;
      boolean var13 = true;
      boolean var14 = false;
      boolean var15 = this.customs.separatorVariant != 0;
      int var16 = var6;

      for(int var17 = var6; var16 <= var28; var16 = var17) {
         if (var14) {
            this.data.appendSkippedUnit(var30);
            var14 = false;
            var13 = true;
         }

         while(true) {
            ++var17;
            if (var17 >= var28 || (var4 & 1 << var17) != 0) {
               TimeUnit var18 = TimeUnit.units[var16];
               int var19 = var3[var16] - 1;
               byte var20 = this.customs.countVariant;
               if (var16 == var28) {
                  if (var26) {
                     var20 = 5;
                  }
               } else {
                  var20 = 0;
               }

               boolean var21 = var16 == var28;
               boolean var22 = this.data.appendUnit(var18, var19, var20, this.customs.unitVariant, var15, var11, var12, var21, var13, var30);
               var14 |= var22;
               var13 = false;
               if (this.customs.separatorVariant != 0 && var17 <= var28) {
                  boolean var23 = var16 == var6;
                  boolean var24 = var17 == var28;
                  boolean var25 = this.customs.separatorVariant == 2;
                  var11 = this.data.appendUnitSeparator(var18, var25, var23, var24, var30);
               } else {
                  var11 = false;
               }
               break;
            }

            var14 = true;
         }
      }

      this.data.appendSuffix(var1, var10, var30);
      return var30.toString();
   }
}
