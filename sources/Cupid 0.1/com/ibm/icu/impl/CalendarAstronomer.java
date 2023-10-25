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
  
  public CalendarAstronomer() {
    this(System.currentTimeMillis());
  }
  
  public CalendarAstronomer(Date d) {
    this(d.getTime());
  }
  
  public CalendarAstronomer(long aTime) {
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
    this.time = aTime;
  }
  
  public CalendarAstronomer(double longitude, double latitude) {
    this();
    this.fLongitude = normPI(longitude * 0.017453292519943295D);
    this.fLatitude = normPI(latitude * 0.017453292519943295D);
    this.fGmtOffset = (long)(this.fLongitude * 24.0D * 3600000.0D / 6.283185307179586D);
  }
  
  public void setTime(long aTime) {
    this.time = aTime;
    clearCache();
  }
  
  public void setDate(Date date) {
    setTime(date.getTime());
  }
  
  public void setJulianDay(double jdn) {
    this.time = (long)(jdn * 8.64E7D) + -210866760000000L;
    clearCache();
    this.julianDay = jdn;
  }
  
  public long getTime() {
    return this.time;
  }
  
  public Date getDate() {
    return new Date(this.time);
  }
  
  public double getJulianDay() {
    if (this.julianDay == Double.MIN_VALUE)
      this.julianDay = (this.time - -210866760000000L) / 8.64E7D; 
    return this.julianDay;
  }
  
  public double getJulianCentury() {
    if (this.julianCentury == Double.MIN_VALUE)
      this.julianCentury = (getJulianDay() - 2415020.0D) / 36525.0D; 
    return this.julianCentury;
  }
  
  public double getGreenwichSidereal() {
    if (this.siderealTime == Double.MIN_VALUE) {
      double UT = normalize(this.time / 3600000.0D, 24.0D);
      this.siderealTime = normalize(getSiderealOffset() + UT * 1.002737909D, 24.0D);
    } 
    return this.siderealTime;
  }
  
  private double getSiderealOffset() {
    if (this.siderealT0 == Double.MIN_VALUE) {
      double JD = Math.floor(getJulianDay() - 0.5D) + 0.5D;
      double S = JD - 2451545.0D;
      double T = S / 36525.0D;
      this.siderealT0 = normalize(6.697374558D + 2400.051336D * T + 2.5862E-5D * T * T, 24.0D);
    } 
    return this.siderealT0;
  }
  
  public double getLocalSidereal() {
    return normalize(getGreenwichSidereal() + this.fGmtOffset / 3600000.0D, 24.0D);
  }
  
  private long lstToUT(double lst) {
    double lt = normalize((lst - getSiderealOffset()) * 0.9972695663D, 24.0D);
    long base = 86400000L * (this.time + this.fGmtOffset) / 86400000L - this.fGmtOffset;
    return base + (long)(lt * 3600000.0D);
  }
  
  public final Equatorial eclipticToEquatorial(Ecliptic ecliptic) {
    return eclipticToEquatorial(ecliptic.longitude, ecliptic.latitude);
  }
  
  public final Equatorial eclipticToEquatorial(double eclipLong, double eclipLat) {
    double obliq = eclipticObliquity();
    double sinE = Math.sin(obliq);
    double cosE = Math.cos(obliq);
    double sinL = Math.sin(eclipLong);
    double cosL = Math.cos(eclipLong);
    double sinB = Math.sin(eclipLat);
    double cosB = Math.cos(eclipLat);
    double tanB = Math.tan(eclipLat);
    return new Equatorial(Math.atan2(sinL * cosE - tanB * sinE, cosL), Math.asin(sinB * cosE + cosB * sinE * sinL));
  }
  
  public final Equatorial eclipticToEquatorial(double eclipLong) {
    return eclipticToEquatorial(eclipLong, 0.0D);
  }
  
  public Horizon eclipticToHorizon(double eclipLong) {
    Equatorial equatorial = eclipticToEquatorial(eclipLong);
    double H = getLocalSidereal() * Math.PI / 12.0D - equatorial.ascension;
    double sinH = Math.sin(H);
    double cosH = Math.cos(H);
    double sinD = Math.sin(equatorial.declination);
    double cosD = Math.cos(equatorial.declination);
    double sinL = Math.sin(this.fLatitude);
    double cosL = Math.cos(this.fLatitude);
    double altitude = Math.asin(sinD * sinL + cosD * cosL * cosH);
    double azimuth = Math.atan2(-cosD * cosL * sinH, sinD - sinL * Math.sin(altitude));
    return new Horizon(azimuth, altitude);
  }
  
  public double getSunLongitude() {
    if (this.sunLongitude == Double.MIN_VALUE) {
      double[] result = getSunLongitude(getJulianDay());
      this.sunLongitude = result[0];
      this.meanAnomalySun = result[1];
    } 
    return this.sunLongitude;
  }
  
  double[] getSunLongitude(double julian) {
    double day = julian - 2447891.5D;
    double epochAngle = norm2PI(0.017202791632524146D * day);
    double meanAnomaly = norm2PI(epochAngle + 4.87650757829735D - 4.935239984568769D);
    return new double[] { norm2PI(trueAnomaly(meanAnomaly, 0.016713D) + 4.935239984568769D), meanAnomaly };
  }
  
  public Equatorial getSunPosition() {
    return eclipticToEquatorial(getSunLongitude(), 0.0D);
  }
  
  private static class SolarLongitude {
    double value;
    
    SolarLongitude(double val) {
      this.value = val;
    }
  }
  
  public static final SolarLongitude VERNAL_EQUINOX = new SolarLongitude(0.0D);
  
  public static final SolarLongitude SUMMER_SOLSTICE = new SolarLongitude(1.5707963267948966D);
  
  public static final SolarLongitude AUTUMN_EQUINOX = new SolarLongitude(Math.PI);
  
  public static final SolarLongitude WINTER_SOLSTICE = new SolarLongitude(4.71238898038469D);
  
  static final double moonL0 = 5.556284436750021D;
  
  static final double moonP0 = 0.6342598060246725D;
  
  static final double moonN0 = 5.559050068029439D;
  
  static final double moonI = 0.08980357792017056D;
  
  static final double moonE = 0.0549D;
  
  static final double moonA = 384401.0D;
  
  static final double moonT0 = 0.009042550854582622D;
  
  static final double moonPi = 0.016592845198710092D;
  
  public long getSunTime(double desired, boolean next) {
    return timeOfAngle(new AngleFunc() {
          public double eval() {
            return CalendarAstronomer.this.getSunLongitude();
          }
        },  desired, 365.242191D, 60000L, next);
  }
  
  public long getSunTime(SolarLongitude desired, boolean next) {
    return getSunTime(desired.value, next);
  }
  
  public long getSunRiseSet(boolean rise) {
    long t0 = this.time;
    long noon = (this.time + this.fGmtOffset) / 86400000L * 86400000L - this.fGmtOffset + 43200000L;
    setTime(noon + (rise ? -6L : 6L) * 3600000L);
    long t = riseOrSet(new CoordFunc() {
          public CalendarAstronomer.Equatorial eval() {
            return CalendarAstronomer.this.getSunPosition();
          }
        },  rise, 0.009302604913129777D, 0.009890199094634533D, 5000L);
    setTime(t0);
    return t;
  }
  
  public Equatorial getMoonPosition() {
    if (this.moonPosition == null) {
      double sunLong = getSunLongitude();
      double day = getJulianDay() - 2447891.5D;
      double meanLongitude = norm2PI(0.22997150421858628D * day + 5.556284436750021D);
      double meanAnomalyMoon = norm2PI(meanLongitude - 0.001944368345221015D * day - 0.6342598060246725D);
      double evection = 0.022233749341155764D * Math.sin(2.0D * (meanLongitude - sunLong) - meanAnomalyMoon);
      double annual = 0.003242821750205464D * Math.sin(this.meanAnomalySun);
      double a3 = 0.00645771823237902D * Math.sin(this.meanAnomalySun);
      meanAnomalyMoon += evection - annual - a3;
      double center = 0.10975677534091541D * Math.sin(meanAnomalyMoon);
      double a4 = 0.0037350045992678655D * Math.sin(2.0D * meanAnomalyMoon);
      this.moonLongitude = meanLongitude + evection + center - annual + a4;
      double variation = 0.011489502465878671D * Math.sin(2.0D * (this.moonLongitude - sunLong));
      this.moonLongitude += variation;
      double nodeLongitude = norm2PI(5.559050068029439D - 9.242199067718253E-4D * day);
      nodeLongitude -= 0.0027925268031909274D * Math.sin(this.meanAnomalySun);
      double y = Math.sin(this.moonLongitude - nodeLongitude);
      double x = Math.cos(this.moonLongitude - nodeLongitude);
      this.moonEclipLong = Math.atan2(y * Math.cos(0.08980357792017056D), x) + nodeLongitude;
      double moonEclipLat = Math.asin(y * Math.sin(0.08980357792017056D));
      this.moonPosition = eclipticToEquatorial(this.moonEclipLong, moonEclipLat);
    } 
    return this.moonPosition;
  }
  
  public double getMoonAge() {
    getMoonPosition();
    return norm2PI(this.moonEclipLong - this.sunLongitude);
  }
  
  public double getMoonPhase() {
    return 0.5D * (1.0D - Math.cos(getMoonAge()));
  }
  
  private static class MoonAge {
    double value;
    
    MoonAge(double val) {
      this.value = val;
    }
  }
  
  public static final MoonAge NEW_MOON = new MoonAge(0.0D);
  
  public static final MoonAge FIRST_QUARTER = new MoonAge(1.5707963267948966D);
  
  public static final MoonAge FULL_MOON = new MoonAge(Math.PI);
  
  public static final MoonAge LAST_QUARTER = new MoonAge(4.71238898038469D);
  
  private long time;
  
  private double fLongitude;
  
  private double fLatitude;
  
  private long fGmtOffset;
  
  private static final double INVALID = 4.9E-324D;
  
  private transient double julianDay;
  
  private transient double julianCentury;
  
  private transient double sunLongitude;
  
  private transient double meanAnomalySun;
  
  private transient double moonLongitude;
  
  private transient double moonEclipLong;
  
  private transient double eclipObliquity;
  
  private transient double siderealT0;
  
  private transient double siderealTime;
  
  private transient Equatorial moonPosition;
  
  public long getMoonTime(double desired, boolean next) {
    return timeOfAngle(new AngleFunc() {
          public double eval() {
            return CalendarAstronomer.this.getMoonAge();
          }
        },  desired, 29.530588853D, 60000L, next);
  }
  
  public long getMoonTime(MoonAge desired, boolean next) {
    return getMoonTime(desired.value, next);
  }
  
  public long getMoonRiseSet(boolean rise) {
    return riseOrSet(new CoordFunc() {
          public CalendarAstronomer.Equatorial eval() {
            return CalendarAstronomer.this.getMoonPosition();
          }
        },  rise, 0.009302604913129777D, 0.009890199094634533D, 60000L);
  }
  
  private long timeOfAngle(AngleFunc func, double desired, double periodDays, long epsilon, boolean next) {
    double lastAngle = func.eval();
    double deltaAngle = norm2PI(desired - lastAngle);
    double deltaT = (deltaAngle + (next ? 0.0D : -6.283185307179586D)) * periodDays * 8.64E7D / 6.283185307179586D;
    double lastDeltaT = deltaT;
    long startTime = this.time;
    setTime(this.time + (long)deltaT);
    do {
      double angle = func.eval();
      double factor = Math.abs(deltaT / normPI(angle - lastAngle));
      deltaT = normPI(desired - angle) * factor;
      if (Math.abs(deltaT) > Math.abs(lastDeltaT)) {
        long delta = (long)(periodDays * 8.64E7D / 8.0D);
        setTime(startTime + (next ? delta : -delta));
        return timeOfAngle(func, desired, periodDays, epsilon, next);
      } 
      lastDeltaT = deltaT;
      lastAngle = angle;
      setTime(this.time + (long)deltaT);
    } while (Math.abs(deltaT) > epsilon);
    return this.time;
  }
  
  private long riseOrSet(CoordFunc func, boolean rise, double diameter, double refraction, long epsilon) {
    Equatorial pos = null;
    double tanL = Math.tan(this.fLatitude);
    long deltaT = Long.MAX_VALUE;
    int count = 0;
    do {
      pos = func.eval();
      double angle = Math.acos(-tanL * Math.tan(pos.declination));
      double lst = ((rise ? (6.283185307179586D - angle) : angle) + pos.ascension) * 24.0D / 6.283185307179586D;
      long newTime = lstToUT(lst);
      deltaT = newTime - this.time;
      setTime(newTime);
    } while (++count < 5 && Math.abs(deltaT) > epsilon);
    double cosD = Math.cos(pos.declination);
    double psi = Math.acos(Math.sin(this.fLatitude) / cosD);
    double x = diameter / 2.0D + refraction;
    double y = Math.asin(Math.sin(x) / Math.sin(psi));
    long delta = (long)(240.0D * y * 57.29577951308232D / cosD * 1000.0D);
    return this.time + (rise ? -delta : delta);
  }
  
  private static final double normalize(double value, double range) {
    return value - range * Math.floor(value / range);
  }
  
  private static final double norm2PI(double angle) {
    return normalize(angle, 6.283185307179586D);
  }
  
  private static final double normPI(double angle) {
    return normalize(angle + Math.PI, 6.283185307179586D) - Math.PI;
  }
  
  private double trueAnomaly(double meanAnomaly, double eccentricity) {
    double delta, E = meanAnomaly;
    do {
      delta = E - eccentricity * Math.sin(E) - meanAnomaly;
      E -= delta / (1.0D - eccentricity * Math.cos(E));
    } while (Math.abs(delta) > 1.0E-5D);
    return 2.0D * Math.atan(Math.tan(E / 2.0D) * Math.sqrt((1.0D + eccentricity) / (1.0D - eccentricity)));
  }
  
  private double eclipticObliquity() {
    if (this.eclipObliquity == Double.MIN_VALUE) {
      double epoch = 2451545.0D;
      double T = (getJulianDay() - 2451545.0D) / 36525.0D;
      this.eclipObliquity = 23.439292D - 0.013004166666666666D * T - 1.6666666666666665E-7D * T * T + 5.027777777777778E-7D * T * T * T;
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
  
  public String local(long localMillis) {
    return (new Date(localMillis - TimeZone.getDefault().getRawOffset())).toString();
  }
  
  public static final class Ecliptic {
    public final double latitude;
    
    public final double longitude;
    
    public Ecliptic(double lat, double lon) {
      this.latitude = lat;
      this.longitude = lon;
    }
    
    public String toString() {
      return Double.toString(this.longitude * 57.29577951308232D) + "," + (this.latitude * 57.29577951308232D);
    }
  }
  
  public static final class Equatorial {
    public final double ascension;
    
    public final double declination;
    
    public Equatorial(double asc, double dec) {
      this.ascension = asc;
      this.declination = dec;
    }
    
    public String toString() {
      return Double.toString(this.ascension * 57.29577951308232D) + "," + (this.declination * 57.29577951308232D);
    }
    
    public String toHmsString() {
      return CalendarAstronomer.radToHms(this.ascension) + "," + CalendarAstronomer.radToDms(this.declination);
    }
  }
  
  public static final class Horizon {
    public final double altitude;
    
    public final double azimuth;
    
    public Horizon(double alt, double azim) {
      this.altitude = alt;
      this.azimuth = azim;
    }
    
    public String toString() {
      return Double.toString(this.altitude * 57.29577951308232D) + "," + (this.azimuth * 57.29577951308232D);
    }
  }
  
  private static String radToHms(double angle) {
    int hrs = (int)(angle * 3.819718634205488D);
    int min = (int)((angle * 3.819718634205488D - hrs) * 60.0D);
    int sec = (int)((angle * 3.819718634205488D - hrs - min / 60.0D) * 3600.0D);
    return Integer.toString(hrs) + "h" + min + "m" + sec + "s";
  }
  
  private static String radToDms(double angle) {
    int deg = (int)(angle * 57.29577951308232D);
    int min = (int)((angle * 57.29577951308232D - deg) * 60.0D);
    int sec = (int)((angle * 57.29577951308232D - deg - min / 60.0D) * 3600.0D);
    return Integer.toString(deg) + "°" + min + "'" + sec + "\"";
  }
  
  private static interface CoordFunc {
    CalendarAstronomer.Equatorial eval();
  }
  
  private static interface AngleFunc {
    double eval();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\CalendarAstronomer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */