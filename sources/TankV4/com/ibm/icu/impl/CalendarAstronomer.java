package com.ibm.icu.impl;

import java.util.Date;
import java.util.TimeZone;

public class CalendarAstronomer {
   public static final double SIDEREAL_DAY = 23.93446960027D;
   public static final double SOLAR_DAY = 24.065709816D;
   public static final double SYNODIC_MONTH = 29.530588853D;
   public static final double SIDEREAL_MONTH = 27.32166D;
   public static final double TROPICAL_YEAR = 365.242191D;
   public static final double SIDEREAL_YEAR = 365.25636D;
   public static final int SECOND_MS = 1000;
   public static final int MINUTE_MS = 60000;
   public static final int HOUR_MS = 3600000;
   public static final long DAY_MS = 86400000L;
   public static final long JULIAN_EPOCH_MS = -210866760000000L;
   static final long EPOCH_2000_MS = 946598400000L;
   private static final double PI = 3.141592653589793D;
   private static final double PI2 = 6.283185307179586D;
   private static final double RAD_HOUR = 3.819718634205488D;
   private static final double DEG_RAD = 0.017453292519943295D;
   private static final double RAD_DEG = 57.29577951308232D;
   static final double JD_EPOCH = 2447891.5D;
   static final double SUN_ETA_G = 4.87650757829735D;
   static final double SUN_OMEGA_G = 4.935239984568769D;
   static final double SUN_E = 0.016713D;
   public static final CalendarAstronomer.SolarLongitude VERNAL_EQUINOX = new CalendarAstronomer.SolarLongitude(0.0D);
   public static final CalendarAstronomer.SolarLongitude SUMMER_SOLSTICE = new CalendarAstronomer.SolarLongitude(1.5707963267948966D);
   public static final CalendarAstronomer.SolarLongitude AUTUMN_EQUINOX = new CalendarAstronomer.SolarLongitude(3.141592653589793D);
   public static final CalendarAstronomer.SolarLongitude WINTER_SOLSTICE = new CalendarAstronomer.SolarLongitude(4.71238898038469D);
   static final double moonL0 = 5.556284436750021D;
   static final double moonP0 = 0.6342598060246725D;
   static final double moonN0 = 5.559050068029439D;
   static final double moonI = 0.08980357792017056D;
   static final double moonE = 0.0549D;
   static final double moonA = 384401.0D;
   static final double moonT0 = 0.009042550854582622D;
   static final double moonPi = 0.016592845198710092D;
   public static final CalendarAstronomer.MoonAge NEW_MOON = new CalendarAstronomer.MoonAge(0.0D);
   public static final CalendarAstronomer.MoonAge FIRST_QUARTER = new CalendarAstronomer.MoonAge(1.5707963267948966D);
   public static final CalendarAstronomer.MoonAge FULL_MOON = new CalendarAstronomer.MoonAge(3.141592653589793D);
   public static final CalendarAstronomer.MoonAge LAST_QUARTER = new CalendarAstronomer.MoonAge(4.71238898038469D);
   private long time;
   private double fLongitude;
   private double fLatitude;
   private long fGmtOffset;
   private static final double INVALID = Double.MIN_VALUE;
   private transient double julianDay;
   private transient double julianCentury;
   private transient double sunLongitude;
   private transient double meanAnomalySun;
   private transient double moonLongitude;
   private transient double moonEclipLong;
   private transient double eclipObliquity;
   private transient double siderealT0;
   private transient double siderealTime;
   private transient CalendarAstronomer.Equatorial moonPosition;

   public CalendarAstronomer() {
      this(System.currentTimeMillis());
   }

   public CalendarAstronomer(Date var1) {
      this(var1.getTime());
   }

   public CalendarAstronomer(long var1) {
      this.fLongitude = 0.0D;
      this.fLatitude = 0.0D;
      this.fGmtOffset = 0L;
      this.julianDay = Double.MIN_VALUE;
      this.julianCentury = Double.MIN_VALUE;
      this.sunLongitude = Double.MIN_VALUE;
      this.meanAnomalySun = Double.MIN_VALUE;
      this.moonLongitude = Double.MIN_VALUE;
      this.moonEclipLong = Double.MIN_VALUE;
      this.eclipObliquity = Double.MIN_VALUE;
      this.siderealT0 = Double.MIN_VALUE;
      this.siderealTime = Double.MIN_VALUE;
      this.moonPosition = null;
      this.time = var1;
   }

   public CalendarAstronomer(double var1, double var3) {
      this();
      this.fLongitude = normPI(var1 * 0.017453292519943295D);
      this.fLatitude = normPI(var3 * 0.017453292519943295D);
      this.fGmtOffset = (long)(this.fLongitude * 24.0D * 3600000.0D / 6.283185307179586D);
   }

   public void setTime(long var1) {
      this.time = var1;
      this.clearCache();
   }

   public void setDate(Date var1) {
      this.setTime(var1.getTime());
   }

   public void setJulianDay(double var1) {
      this.time = (long)(var1 * 8.64E7D) + -210866760000000L;
      this.clearCache();
      this.julianDay = var1;
   }

   public long getTime() {
      return this.time;
   }

   public Date getDate() {
      return new Date(this.time);
   }

   public double getJulianDay() {
      if (this.julianDay == Double.MIN_VALUE) {
         this.julianDay = (double)(this.time - -210866760000000L) / 8.64E7D;
      }

      return this.julianDay;
   }

   public double getJulianCentury() {
      if (this.julianCentury == Double.MIN_VALUE) {
         this.julianCentury = (this.getJulianDay() - 2415020.0D) / 36525.0D;
      }

      return this.julianCentury;
   }

   public double getGreenwichSidereal() {
      if (this.siderealTime == Double.MIN_VALUE) {
         double var1 = normalize((double)this.time / 3600000.0D, 24.0D);
         this.siderealTime = normalize(this.getSiderealOffset() + var1 * 1.002737909D, 24.0D);
      }

      return this.siderealTime;
   }

   private double getSiderealOffset() {
      if (this.siderealT0 == Double.MIN_VALUE) {
         double var1 = Math.floor(this.getJulianDay() - 0.5D) + 0.5D;
         double var3 = var1 - 2451545.0D;
         double var5 = var3 / 36525.0D;
         this.siderealT0 = normalize(6.697374558D + 2400.051336D * var5 + 2.5862E-5D * var5 * var5, 24.0D);
      }

      return this.siderealT0;
   }

   public double getLocalSidereal() {
      return normalize(this.getGreenwichSidereal() + (double)this.fGmtOffset / 3600000.0D, 24.0D);
   }

   private long lstToUT(double var1) {
      double var3 = normalize((var1 - this.getSiderealOffset()) * 0.9972695663D, 24.0D);
      long var5 = 86400000L * ((this.time + this.fGmtOffset) / 86400000L) - this.fGmtOffset;
      return var5 + (long)(var3 * 3600000.0D);
   }

   public final CalendarAstronomer.Equatorial eclipticToEquatorial(CalendarAstronomer.Ecliptic var1) {
      return this.eclipticToEquatorial(var1.longitude, var1.latitude);
   }

   public final CalendarAstronomer.Equatorial eclipticToEquatorial(double var1, double var3) {
      double var5 = this.eclipticObliquity();
      double var7 = Math.sin(var5);
      double var9 = Math.cos(var5);
      double var11 = Math.sin(var1);
      double var13 = Math.cos(var1);
      double var15 = Math.sin(var3);
      double var17 = Math.cos(var3);
      double var19 = Math.tan(var3);
      return new CalendarAstronomer.Equatorial(Math.atan2(var11 * var9 - var19 * var7, var13), Math.asin(var15 * var9 + var17 * var7 * var11));
   }

   public final CalendarAstronomer.Equatorial eclipticToEquatorial(double var1) {
      return this.eclipticToEquatorial(var1, 0.0D);
   }

   public CalendarAstronomer.Horizon eclipticToHorizon(double var1) {
      CalendarAstronomer.Equatorial var3 = this.eclipticToEquatorial(var1);
      double var4 = this.getLocalSidereal() * 3.141592653589793D / 12.0D - var3.ascension;
      double var6 = Math.sin(var4);
      double var8 = Math.cos(var4);
      double var10 = Math.sin(var3.declination);
      double var12 = Math.cos(var3.declination);
      double var14 = Math.sin(this.fLatitude);
      double var16 = Math.cos(this.fLatitude);
      double var18 = Math.asin(var10 * var14 + var12 * var16 * var8);
      double var20 = Math.atan2(-var12 * var16 * var6, var10 - var14 * Math.sin(var18));
      return new CalendarAstronomer.Horizon(var20, var18);
   }

   public double getSunLongitude() {
      if (this.sunLongitude == Double.MIN_VALUE) {
         double[] var1 = this.getSunLongitude(this.getJulianDay());
         this.sunLongitude = var1[0];
         this.meanAnomalySun = var1[1];
      }

      return this.sunLongitude;
   }

   double[] getSunLongitude(double var1) {
      double var3 = var1 - 2447891.5D;
      double var5 = norm2PI(0.017202791632524146D * var3);
      double var7 = norm2PI(var5 + 4.87650757829735D - 4.935239984568769D);
      return new double[]{norm2PI(this.trueAnomaly(var7, 0.016713D) + 4.935239984568769D), var7};
   }

   public CalendarAstronomer.Equatorial getSunPosition() {
      return this.eclipticToEquatorial(this.getSunLongitude(), 0.0D);
   }

   public long getSunTime(double var1, boolean var3) {
      return this.timeOfAngle(new CalendarAstronomer.AngleFunc(this) {
         final CalendarAstronomer this$0;

         {
            this.this$0 = var1;
         }

         public double eval() {
            return this.this$0.getSunLongitude();
         }
      }, var1, 365.242191D, 60000L, var3);
   }

   public long getSunTime(CalendarAstronomer.SolarLongitude var1, boolean var2) {
      return this.getSunTime(var1.value, var2);
   }

   public long getSunRiseSet(boolean var1) {
      long var2 = this.time;
      long var4 = (this.time + this.fGmtOffset) / 86400000L * 86400000L - this.fGmtOffset + 43200000L;
      this.setTime(var4 + (var1 ? -6L : 6L) * 3600000L);
      long var6 = this.riseOrSet(new CalendarAstronomer.CoordFunc(this) {
         final CalendarAstronomer this$0;

         {
            this.this$0 = var1;
         }

         public CalendarAstronomer.Equatorial eval() {
            return this.this$0.getSunPosition();
         }
      }, var1, 0.009302604913129777D, 0.009890199094634533D, 5000L);
      this.setTime(var2);
      return var6;
   }

   public CalendarAstronomer.Equatorial getMoonPosition() {
      if (this.moonPosition == null) {
         double var1 = this.getSunLongitude();
         double var3 = this.getJulianDay() - 2447891.5D;
         double var5 = norm2PI(0.22997150421858628D * var3 + 5.556284436750021D);
         double var7 = norm2PI(var5 - 0.001944368345221015D * var3 - 0.6342598060246725D);
         double var9 = 0.022233749341155764D * Math.sin(2.0D * (var5 - var1) - var7);
         double var11 = 0.003242821750205464D * Math.sin(this.meanAnomalySun);
         double var13 = 0.00645771823237902D * Math.sin(this.meanAnomalySun);
         var7 += var9 - var11 - var13;
         double var15 = 0.10975677534091541D * Math.sin(var7);
         double var17 = 0.0037350045992678655D * Math.sin(2.0D * var7);
         this.moonLongitude = var5 + var9 + var15 - var11 + var17;
         double var19 = 0.011489502465878671D * Math.sin(2.0D * (this.moonLongitude - var1));
         this.moonLongitude += var19;
         double var21 = norm2PI(5.559050068029439D - 9.242199067718253E-4D * var3);
         var21 -= 0.0027925268031909274D * Math.sin(this.meanAnomalySun);
         double var23 = Math.sin(this.moonLongitude - var21);
         double var25 = Math.cos(this.moonLongitude - var21);
         this.moonEclipLong = Math.atan2(var23 * Math.cos(0.08980357792017056D), var25) + var21;
         double var27 = Math.asin(var23 * Math.sin(0.08980357792017056D));
         this.moonPosition = this.eclipticToEquatorial(this.moonEclipLong, var27);
      }

      return this.moonPosition;
   }

   public double getMoonAge() {
      this.getMoonPosition();
      return norm2PI(this.moonEclipLong - this.sunLongitude);
   }

   public double getMoonPhase() {
      return 0.5D * (1.0D - Math.cos(this.getMoonAge()));
   }

   public long getMoonTime(double var1, boolean var3) {
      return this.timeOfAngle(new CalendarAstronomer.AngleFunc(this) {
         final CalendarAstronomer this$0;

         {
            this.this$0 = var1;
         }

         public double eval() {
            return this.this$0.getMoonAge();
         }
      }, var1, 29.530588853D, 60000L, var3);
   }

   public long getMoonTime(CalendarAstronomer.MoonAge var1, boolean var2) {
      return this.getMoonTime(var1.value, var2);
   }

   public long getMoonRiseSet(boolean var1) {
      return this.riseOrSet(new CalendarAstronomer.CoordFunc(this) {
         final CalendarAstronomer this$0;

         {
            this.this$0 = var1;
         }

         public CalendarAstronomer.Equatorial eval() {
            return this.this$0.getMoonPosition();
         }
      }, var1, 0.009302604913129777D, 0.009890199094634533D, 60000L);
   }

   private long timeOfAngle(CalendarAstronomer.AngleFunc var1, double var2, double var4, long var6, boolean var8) {
      double var9 = var1.eval();
      double var11 = norm2PI(var2 - var9);
      double var13 = (var11 + (var8 ? 0.0D : -6.283185307179586D)) * var4 * 8.64E7D / 6.283185307179586D;
      double var15 = var13;
      long var17 = this.time;
      this.setTime(this.time + (long)var13);

      do {
         double var19 = var1.eval();
         double var21 = Math.abs(var13 / normPI(var19 - var9));
         var13 = normPI(var2 - var19) * var21;
         if (Math.abs(var13) > Math.abs(var15)) {
            long var23 = (long)(var4 * 8.64E7D / 8.0D);
            this.setTime(var17 + (var8 ? var23 : -var23));
            return this.timeOfAngle(var1, var2, var4, var6, var8);
         }

         var15 = var13;
         var9 = var19;
         this.setTime(this.time + (long)var13);
      } while(Math.abs(var13) > (double)var6);

      return this.time;
   }

   private long riseOrSet(CalendarAstronomer.CoordFunc var1, boolean var2, double var3, double var5, long var7) {
      CalendarAstronomer.Equatorial var9 = null;
      double var10 = Math.tan(this.fLatitude);
      long var12 = Long.MAX_VALUE;
      int var14 = 0;

      double var15;
      double var17;
      do {
         var9 = var1.eval();
         var15 = Math.acos(-var10 * Math.tan(var9.declination));
         var17 = ((var2 ? 6.283185307179586D - var15 : var15) + var9.ascension) * 24.0D / 6.283185307179586D;
         long var19 = this.lstToUT(var17);
         var12 = var19 - this.time;
         this.setTime(var19);
         ++var14;
      } while(var14 < 5 && Math.abs(var12) > var7);

      var15 = Math.cos(var9.declination);
      var17 = Math.acos(Math.sin(this.fLatitude) / var15);
      double var25 = var3 / 2.0D + var5;
      double var21 = Math.asin(Math.sin(var25) / Math.sin(var17));
      long var23 = (long)(240.0D * var21 * 57.29577951308232D / var15 * 1000.0D);
      return this.time + (var2 ? -var23 : var23);
   }

   private static final double normalize(double var0, double var2) {
      return var0 - var2 * Math.floor(var0 / var2);
   }

   private static final double norm2PI(double var0) {
      return normalize(var0, 6.283185307179586D);
   }

   private static final double normPI(double var0) {
      return normalize(var0 + 3.141592653589793D, 6.283185307179586D) - 3.141592653589793D;
   }

   private double trueAnomaly(double var1, double var3) {
      double var7 = var1;

      double var5;
      do {
         var5 = var7 - var3 * Math.sin(var7) - var1;
         var7 -= var5 / (1.0D - var3 * Math.cos(var7));
      } while(Math.abs(var5) > 1.0E-5D);

      return 2.0D * Math.atan(Math.tan(var7 / 2.0D) * Math.sqrt((1.0D + var3) / (1.0D - var3)));
   }

   private double eclipticObliquity() {
      if (this.eclipObliquity == Double.MIN_VALUE) {
         double var1 = 2451545.0D;
         double var3 = (this.getJulianDay() - 2451545.0D) / 36525.0D;
         this.eclipObliquity = 23.439292D - 0.013004166666666666D * var3 - 1.6666666666666665E-7D * var3 * var3 + 5.027777777777778E-7D * var3 * var3 * var3;
         this.eclipObliquity *= 0.017453292519943295D;
      }

      return this.eclipObliquity;
   }

   private void clearCache() {
      this.julianDay = Double.MIN_VALUE;
      this.julianCentury = Double.MIN_VALUE;
      this.sunLongitude = Double.MIN_VALUE;
      this.meanAnomalySun = Double.MIN_VALUE;
      this.moonLongitude = Double.MIN_VALUE;
      this.moonEclipLong = Double.MIN_VALUE;
      this.eclipObliquity = Double.MIN_VALUE;
      this.siderealTime = Double.MIN_VALUE;
      this.siderealT0 = Double.MIN_VALUE;
      this.moonPosition = null;
   }

   public String local(long var1) {
      return (new Date(var1 - (long)TimeZone.getDefault().getRawOffset())).toString();
   }

   private static String radToHms(double var0) {
      int var2 = (int)(var0 * 3.819718634205488D);
      int var3 = (int)((var0 * 3.819718634205488D - (double)var2) * 60.0D);
      int var4 = (int)((var0 * 3.819718634205488D - (double)var2 - (double)var3 / 60.0D) * 3600.0D);
      return Integer.toString(var2) + "h" + var3 + "m" + var4 + "s";
   }

   private static String radToDms(double var0) {
      int var2 = (int)(var0 * 57.29577951308232D);
      int var3 = (int)((var0 * 57.29577951308232D - (double)var2) * 60.0D);
      int var4 = (int)((var0 * 57.29577951308232D - (double)var2 - (double)var3 / 60.0D) * 3600.0D);
      return Integer.toString(var2) + "°" + var3 + "'" + var4 + "\"";
   }

   static String access$000(double var0) {
      return radToHms(var0);
   }

   static String access$100(double var0) {
      return radToDms(var0);
   }

   public static final class Horizon {
      public final double altitude;
      public final double azimuth;

      public Horizon(double var1, double var3) {
         this.altitude = var1;
         this.azimuth = var3;
      }

      public String toString() {
         return Double.toString(this.altitude * 57.29577951308232D) + "," + this.azimuth * 57.29577951308232D;
      }
   }

   public static final class Equatorial {
      public final double ascension;
      public final double declination;

      public Equatorial(double var1, double var3) {
         this.ascension = var1;
         this.declination = var3;
      }

      public String toString() {
         return Double.toString(this.ascension * 57.29577951308232D) + "," + this.declination * 57.29577951308232D;
      }

      public String toHmsString() {
         return CalendarAstronomer.access$000(this.ascension) + "," + CalendarAstronomer.access$100(this.declination);
      }
   }

   public static final class Ecliptic {
      public final double latitude;
      public final double longitude;

      public Ecliptic(double var1, double var3) {
         this.latitude = var1;
         this.longitude = var3;
      }

      public String toString() {
         return Double.toString(this.longitude * 57.29577951308232D) + "," + this.latitude * 57.29577951308232D;
      }
   }

   private interface CoordFunc {
      CalendarAstronomer.Equatorial eval();
   }

   private interface AngleFunc {
      double eval();
   }

   private static class MoonAge {
      double value;

      MoonAge(double var1) {
         this.value = var1;
      }
   }

   private static class SolarLongitude {
      double value;

      SolarLongitude(double var1) {
         this.value = var1;
      }
   }
}
