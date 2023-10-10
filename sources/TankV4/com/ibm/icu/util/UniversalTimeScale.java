package com.ibm.icu.util;

import com.ibm.icu.math.BigDecimal;

public final class UniversalTimeScale {
   public static final int JAVA_TIME = 0;
   public static final int UNIX_TIME = 1;
   public static final int ICU4C_TIME = 2;
   public static final int WINDOWS_FILE_TIME = 3;
   public static final int DOTNET_DATE_TIME = 4;
   public static final int MAC_OLD_TIME = 5;
   public static final int MAC_TIME = 6;
   public static final int EXCEL_TIME = 7;
   public static final int DB2_TIME = 8;
   public static final int UNIX_MICROSECONDS_TIME = 9;
   public static final int MAX_SCALE = 10;
   public static final int UNITS_VALUE = 0;
   public static final int EPOCH_OFFSET_VALUE = 1;
   public static final int FROM_MIN_VALUE = 2;
   public static final int FROM_MAX_VALUE = 3;
   public static final int TO_MIN_VALUE = 4;
   public static final int TO_MAX_VALUE = 5;
   public static final int EPOCH_OFFSET_PLUS_1_VALUE = 6;
   /** @deprecated */
   public static final int EPOCH_OFFSET_MINUS_1_VALUE = 7;
   /** @deprecated */
   public static final int UNITS_ROUND_VALUE = 8;
   /** @deprecated */
   public static final int MIN_ROUND_VALUE = 9;
   /** @deprecated */
   public static final int MAX_ROUND_VALUE = 10;
   /** @deprecated */
   public static final int MAX_SCALE_VALUE = 11;
   private static final long ticks = 1L;
   private static final long microseconds = 10L;
   private static final long milliseconds = 10000L;
   private static final long seconds = 10000000L;
   private static final long minutes = 600000000L;
   private static final long hours = 36000000000L;
   private static final long days = 864000000000L;
   private static final UniversalTimeScale.TimeScaleData[] timeScaleTable = new UniversalTimeScale.TimeScaleData[]{new UniversalTimeScale.TimeScaleData(10000L, 621355968000000000L, -9223372036854774999L, 9223372036854774999L, -984472800485477L, 860201606885477L), new UniversalTimeScale.TimeScaleData(10000000L, 621355968000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -984472800485L, 860201606885L), new UniversalTimeScale.TimeScaleData(10000L, 621355968000000000L, -9223372036854774999L, 9223372036854774999L, -984472800485477L, 860201606885477L), new UniversalTimeScale.TimeScaleData(1L, 504911232000000000L, -8718460804854775808L, Long.MAX_VALUE, Long.MIN_VALUE, 8718460804854775807L), new UniversalTimeScale.TimeScaleData(1L, 0L, Long.MIN_VALUE, Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE), new UniversalTimeScale.TimeScaleData(10000000L, 600527520000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -982389955685L, 862284451685L), new UniversalTimeScale.TimeScaleData(10000000L, 631139040000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -985451107685L, 859223299685L), new UniversalTimeScale.TimeScaleData(864000000000L, 599265216000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -11368793L, 9981605L), new UniversalTimeScale.TimeScaleData(864000000000L, 599265216000000000L, Long.MIN_VALUE, Long.MAX_VALUE, -11368793L, 9981605L), new UniversalTimeScale.TimeScaleData(10L, 621355968000000000L, -9223372036854775804L, 9223372036854775804L, -984472800485477580L, 860201606885477580L)};

   private UniversalTimeScale() {
   }

   public static long from(long var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = fromRangeCheck(var0, var2);
      return (var0 + var3.epochOffset) * var3.units;
   }

   public static BigDecimal bigDecimalFrom(double var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = getTimeScaleData(var2);
      BigDecimal var4 = new BigDecimal(String.valueOf(var0));
      BigDecimal var5 = new BigDecimal(var3.units);
      BigDecimal var6 = new BigDecimal(var3.epochOffset);
      return var4.add(var6).multiply(var5);
   }

   public static BigDecimal bigDecimalFrom(long var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = getTimeScaleData(var2);
      BigDecimal var4 = new BigDecimal(var0);
      BigDecimal var5 = new BigDecimal(var3.units);
      BigDecimal var6 = new BigDecimal(var3.epochOffset);
      return var4.add(var6).multiply(var5);
   }

   public static BigDecimal bigDecimalFrom(BigDecimal var0, int var1) {
      UniversalTimeScale.TimeScaleData var2 = getTimeScaleData(var1);
      BigDecimal var3 = new BigDecimal(var2.units);
      BigDecimal var4 = new BigDecimal(var2.epochOffset);
      return var0.add(var4).multiply(var3);
   }

   public static long toLong(long var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = toRangeCheck(var0, var2);
      if (var0 < 0L) {
         return var0 < var3.minRound ? (var0 + var3.unitsRound) / var3.units - var3.epochOffsetP1 : (var0 - var3.unitsRound) / var3.units - var3.epochOffset;
      } else {
         return var0 > var3.maxRound ? (var0 - var3.unitsRound) / var3.units - var3.epochOffsetM1 : (var0 + var3.unitsRound) / var3.units - var3.epochOffset;
      }
   }

   public static BigDecimal toBigDecimal(long var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = getTimeScaleData(var2);
      BigDecimal var4 = new BigDecimal(var0);
      BigDecimal var5 = new BigDecimal(var3.units);
      BigDecimal var6 = new BigDecimal(var3.epochOffset);
      return var4.divide(var5, 4).subtract(var6);
   }

   public static BigDecimal toBigDecimal(BigDecimal var0, int var1) {
      UniversalTimeScale.TimeScaleData var2 = getTimeScaleData(var1);
      BigDecimal var3 = new BigDecimal(var2.units);
      BigDecimal var4 = new BigDecimal(var2.epochOffset);
      return var0.divide(var3, 4).subtract(var4);
   }

   private static UniversalTimeScale.TimeScaleData getTimeScaleData(int var0) {
      if (var0 >= 0 && var0 < 10) {
         return timeScaleTable[var0];
      } else {
         throw new IllegalArgumentException("scale out of range: " + var0);
      }
   }

   public static long getTimeScaleValue(int var0, int var1) {
      UniversalTimeScale.TimeScaleData var2 = getTimeScaleData(var0);
      switch(var1) {
      case 0:
         return var2.units;
      case 1:
         return var2.epochOffset;
      case 2:
         return var2.fromMin;
      case 3:
         return var2.fromMax;
      case 4:
         return var2.toMin;
      case 5:
         return var2.toMax;
      case 6:
         return var2.epochOffsetP1;
      case 7:
         return var2.epochOffsetM1;
      case 8:
         return var2.unitsRound;
      case 9:
         return var2.minRound;
      case 10:
         return var2.maxRound;
      default:
         throw new IllegalArgumentException("value out of range: " + var1);
      }
   }

   private static UniversalTimeScale.TimeScaleData toRangeCheck(long var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = getTimeScaleData(var2);
      if (var0 >= var3.toMin && var0 <= var3.toMax) {
         return var3;
      } else {
         throw new IllegalArgumentException("universalTime out of range:" + var0);
      }
   }

   private static UniversalTimeScale.TimeScaleData fromRangeCheck(long var0, int var2) {
      UniversalTimeScale.TimeScaleData var3 = getTimeScaleData(var2);
      if (var0 >= var3.fromMin && var0 <= var3.fromMax) {
         return var3;
      } else {
         throw new IllegalArgumentException("otherTime out of range:" + var0);
      }
   }

   /** @deprecated */
   public static BigDecimal toBigDecimalTrunc(BigDecimal var0, int var1) {
      UniversalTimeScale.TimeScaleData var2 = getTimeScaleData(var1);
      BigDecimal var3 = new BigDecimal(var2.units);
      BigDecimal var4 = new BigDecimal(var2.epochOffset);
      return var0.divide(var3, 1).subtract(var4);
   }

   private static final class TimeScaleData {
      long units;
      long epochOffset;
      long fromMin;
      long fromMax;
      long toMin;
      long toMax;
      long epochOffsetP1;
      long epochOffsetM1;
      long unitsRound;
      long minRound;
      long maxRound;

      TimeScaleData(long var1, long var3, long var5, long var7, long var9, long var11) {
         this.units = var1;
         this.unitsRound = var1 / 2L;
         this.minRound = Long.MIN_VALUE + this.unitsRound;
         this.maxRound = Long.MAX_VALUE - this.unitsRound;
         this.epochOffset = var3 / var1;
         if (var1 == 1L) {
            this.epochOffsetP1 = this.epochOffsetM1 = this.epochOffset;
         } else {
            this.epochOffsetP1 = this.epochOffset + 1L;
            this.epochOffsetM1 = this.epochOffset - 1L;
         }

         this.toMin = var5;
         this.toMax = var7;
         this.fromMin = var9;
         this.fromMax = var11;
      }
   }
}
