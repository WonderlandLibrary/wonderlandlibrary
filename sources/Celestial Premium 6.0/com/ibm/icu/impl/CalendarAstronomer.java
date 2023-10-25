/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import java.util.Date;
import java.util.TimeZone;

public class CalendarAstronomer {
    public static final double SIDEREAL_DAY = 23.93446960027;
    public static final double SOLAR_DAY = 24.065709816;
    public static final double SYNODIC_MONTH = 29.530588853;
    public static final double SIDEREAL_MONTH = 27.32166;
    public static final double TROPICAL_YEAR = 365.242191;
    public static final double SIDEREAL_YEAR = 365.25636;
    public static final int SECOND_MS = 1000;
    public static final int MINUTE_MS = 60000;
    public static final int HOUR_MS = 3600000;
    public static final long DAY_MS = 86400000L;
    public static final long JULIAN_EPOCH_MS = -210866760000000L;
    static final long EPOCH_2000_MS = 946598400000L;
    private static final double PI = Math.PI;
    private static final double PI2 = Math.PI * 2;
    private static final double RAD_HOUR = 3.819718634205488;
    private static final double DEG_RAD = Math.PI / 180;
    private static final double RAD_DEG = 57.29577951308232;
    static final double JD_EPOCH = 2447891.5;
    static final double SUN_ETA_G = 4.87650757829735;
    static final double SUN_OMEGA_G = 4.935239984568769;
    static final double SUN_E = 0.016713;
    public static final SolarLongitude VERNAL_EQUINOX = new SolarLongitude(0.0);
    public static final SolarLongitude SUMMER_SOLSTICE = new SolarLongitude(1.5707963267948966);
    public static final SolarLongitude AUTUMN_EQUINOX = new SolarLongitude(Math.PI);
    public static final SolarLongitude WINTER_SOLSTICE = new SolarLongitude(4.71238898038469);
    static final double moonL0 = 5.556284436750021;
    static final double moonP0 = 0.6342598060246725;
    static final double moonN0 = 5.559050068029439;
    static final double moonI = 0.08980357792017056;
    static final double moonE = 0.0549;
    static final double moonA = 384401.0;
    static final double moonT0 = 0.009042550854582622;
    static final double moonPi = 0.016592845198710092;
    public static final MoonAge NEW_MOON = new MoonAge(0.0);
    public static final MoonAge FIRST_QUARTER = new MoonAge(1.5707963267948966);
    public static final MoonAge FULL_MOON = new MoonAge(Math.PI);
    public static final MoonAge LAST_QUARTER = new MoonAge(4.71238898038469);
    private long time;
    private double fLongitude = 0.0;
    private double fLatitude = 0.0;
    private long fGmtOffset = 0L;
    private static final double INVALID = Double.MIN_VALUE;
    private transient double julianDay = Double.MIN_VALUE;
    private transient double julianCentury = Double.MIN_VALUE;
    private transient double sunLongitude = Double.MIN_VALUE;
    private transient double meanAnomalySun = Double.MIN_VALUE;
    private transient double moonLongitude = Double.MIN_VALUE;
    private transient double moonEclipLong = Double.MIN_VALUE;
    private transient double eclipObliquity = Double.MIN_VALUE;
    private transient double siderealT0 = Double.MIN_VALUE;
    private transient double siderealTime = Double.MIN_VALUE;
    private transient Equatorial moonPosition = null;

    public CalendarAstronomer() {
        this(System.currentTimeMillis());
    }

    public CalendarAstronomer(Date d) {
        this(d.getTime());
    }

    public CalendarAstronomer(long aTime) {
        this.time = aTime;
    }

    public CalendarAstronomer(double longitude, double latitude) {
        this();
        this.fLongitude = CalendarAstronomer.normPI(longitude * (Math.PI / 180));
        this.fLatitude = CalendarAstronomer.normPI(latitude * (Math.PI / 180));
        this.fGmtOffset = (long)(this.fLongitude * 24.0 * 3600000.0 / (Math.PI * 2));
    }

    public void setTime(long aTime) {
        this.time = aTime;
        this.clearCache();
    }

    public void setDate(Date date) {
        this.setTime(date.getTime());
    }

    public void setJulianDay(double jdn) {
        this.time = (long)(jdn * 8.64E7) + -210866760000000L;
        this.clearCache();
        this.julianDay = jdn;
    }

    public long getTime() {
        return this.time;
    }

    public Date getDate() {
        return new Date(this.time);
    }

    public double getJulianDay() {
        if (this.julianDay == Double.MIN_VALUE) {
            this.julianDay = (double)(this.time - -210866760000000L) / 8.64E7;
        }
        return this.julianDay;
    }

    public double getJulianCentury() {
        if (this.julianCentury == Double.MIN_VALUE) {
            this.julianCentury = (this.getJulianDay() - 2415020.0) / 36525.0;
        }
        return this.julianCentury;
    }

    public double getGreenwichSidereal() {
        if (this.siderealTime == Double.MIN_VALUE) {
            double UT = CalendarAstronomer.normalize((double)this.time / 3600000.0, 24.0);
            this.siderealTime = CalendarAstronomer.normalize(this.getSiderealOffset() + UT * 1.002737909, 24.0);
        }
        return this.siderealTime;
    }

    private double getSiderealOffset() {
        if (this.siderealT0 == Double.MIN_VALUE) {
            double JD = Math.floor(this.getJulianDay() - 0.5) + 0.5;
            double S = JD - 2451545.0;
            double T = S / 36525.0;
            this.siderealT0 = CalendarAstronomer.normalize(6.697374558 + 2400.051336 * T + 2.5862E-5 * T * T, 24.0);
        }
        return this.siderealT0;
    }

    public double getLocalSidereal() {
        return CalendarAstronomer.normalize(this.getGreenwichSidereal() + (double)this.fGmtOffset / 3600000.0, 24.0);
    }

    private long lstToUT(double lst) {
        double lt = CalendarAstronomer.normalize((lst - this.getSiderealOffset()) * 0.9972695663, 24.0);
        long base = 86400000L * ((this.time + this.fGmtOffset) / 86400000L) - this.fGmtOffset;
        return base + (long)(lt * 3600000.0);
    }

    public final Equatorial eclipticToEquatorial(Ecliptic ecliptic) {
        return this.eclipticToEquatorial(ecliptic.longitude, ecliptic.latitude);
    }

    public final Equatorial eclipticToEquatorial(double eclipLong, double eclipLat) {
        double obliq = this.eclipticObliquity();
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
        return this.eclipticToEquatorial(eclipLong, 0.0);
    }

    public Horizon eclipticToHorizon(double eclipLong) {
        Equatorial equatorial = this.eclipticToEquatorial(eclipLong);
        double H = this.getLocalSidereal() * Math.PI / 12.0 - equatorial.ascension;
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
            double[] result = this.getSunLongitude(this.getJulianDay());
            this.sunLongitude = result[0];
            this.meanAnomalySun = result[1];
        }
        return this.sunLongitude;
    }

    double[] getSunLongitude(double julian) {
        double day = julian - 2447891.5;
        double epochAngle = CalendarAstronomer.norm2PI(0.017202791632524146 * day);
        double meanAnomaly = CalendarAstronomer.norm2PI(epochAngle + 4.87650757829735 - 4.935239984568769);
        return new double[]{CalendarAstronomer.norm2PI(this.trueAnomaly(meanAnomaly, 0.016713) + 4.935239984568769), meanAnomaly};
    }

    public Equatorial getSunPosition() {
        return this.eclipticToEquatorial(this.getSunLongitude(), 0.0);
    }

    public long getSunTime(double desired, boolean next) {
        return this.timeOfAngle(new AngleFunc(){

            public double eval() {
                return CalendarAstronomer.this.getSunLongitude();
            }
        }, desired, 365.242191, 60000L, next);
    }

    public long getSunTime(SolarLongitude desired, boolean next) {
        return this.getSunTime(desired.value, next);
    }

    public long getSunRiseSet(boolean rise) {
        long t0 = this.time;
        long noon = (this.time + this.fGmtOffset) / 86400000L * 86400000L - this.fGmtOffset + 43200000L;
        this.setTime(noon + (rise ? -6L : 6L) * 3600000L);
        long t = this.riseOrSet(new CoordFunc(){

            public Equatorial eval() {
                return CalendarAstronomer.this.getSunPosition();
            }
        }, rise, 0.009302604913129777, 0.009890199094634533, 5000L);
        this.setTime(t0);
        return t;
    }

    public Equatorial getMoonPosition() {
        if (this.moonPosition == null) {
            double sunLong = this.getSunLongitude();
            double day = this.getJulianDay() - 2447891.5;
            double meanLongitude = CalendarAstronomer.norm2PI(0.22997150421858628 * day + 5.556284436750021);
            double meanAnomalyMoon = CalendarAstronomer.norm2PI(meanLongitude - 0.001944368345221015 * day - 0.6342598060246725);
            double evection = 0.022233749341155764 * Math.sin(2.0 * (meanLongitude - sunLong) - meanAnomalyMoon);
            double annual = 0.003242821750205464 * Math.sin(this.meanAnomalySun);
            double a3 = 0.00645771823237902 * Math.sin(this.meanAnomalySun);
            double center = 0.10975677534091541 * Math.sin(meanAnomalyMoon += evection - annual - a3);
            double a4 = 0.0037350045992678655 * Math.sin(2.0 * meanAnomalyMoon);
            this.moonLongitude = meanLongitude + evection + center - annual + a4;
            double variation = 0.011489502465878671 * Math.sin(2.0 * (this.moonLongitude - sunLong));
            this.moonLongitude += variation;
            double nodeLongitude = CalendarAstronomer.norm2PI(5.559050068029439 - 9.242199067718253E-4 * day);
            double y = Math.sin(this.moonLongitude - (nodeLongitude -= 0.0027925268031909274 * Math.sin(this.meanAnomalySun)));
            double x = Math.cos(this.moonLongitude - nodeLongitude);
            this.moonEclipLong = Math.atan2(y * Math.cos(0.08980357792017056), x) + nodeLongitude;
            double moonEclipLat = Math.asin(y * Math.sin(0.08980357792017056));
            this.moonPosition = this.eclipticToEquatorial(this.moonEclipLong, moonEclipLat);
        }
        return this.moonPosition;
    }

    public double getMoonAge() {
        this.getMoonPosition();
        return CalendarAstronomer.norm2PI(this.moonEclipLong - this.sunLongitude);
    }

    public double getMoonPhase() {
        return 0.5 * (1.0 - Math.cos(this.getMoonAge()));
    }

    public long getMoonTime(double desired, boolean next) {
        return this.timeOfAngle(new AngleFunc(){

            public double eval() {
                return CalendarAstronomer.this.getMoonAge();
            }
        }, desired, 29.530588853, 60000L, next);
    }

    public long getMoonTime(MoonAge desired, boolean next) {
        return this.getMoonTime(desired.value, next);
    }

    public long getMoonRiseSet(boolean rise) {
        return this.riseOrSet(new CoordFunc(){

            public Equatorial eval() {
                return CalendarAstronomer.this.getMoonPosition();
            }
        }, rise, 0.009302604913129777, 0.009890199094634533, 60000L);
    }

    private long timeOfAngle(AngleFunc func, double desired, double periodDays, long epsilon, boolean next) {
        double deltaT;
        double lastAngle = func.eval();
        double deltaAngle = CalendarAstronomer.norm2PI(desired - lastAngle);
        double lastDeltaT = deltaT = (deltaAngle + (next ? 0.0 : Math.PI * -2)) * (periodDays * 8.64E7) / (Math.PI * 2);
        long startTime = this.time;
        this.setTime(this.time + (long)deltaT);
        do {
            double angle = func.eval();
            double factor = Math.abs(deltaT / CalendarAstronomer.normPI(angle - lastAngle));
            deltaT = CalendarAstronomer.normPI(desired - angle) * factor;
            if (Math.abs(deltaT) > Math.abs(lastDeltaT)) {
                long delta = (long)(periodDays * 8.64E7 / 8.0);
                this.setTime(startTime + (next ? delta : -delta));
                return this.timeOfAngle(func, desired, periodDays, epsilon, next);
            }
            lastDeltaT = deltaT;
            lastAngle = angle;
            this.setTime(this.time + (long)deltaT);
        } while (Math.abs(deltaT) > (double)epsilon);
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
            double lst = ((rise ? Math.PI * 2 - angle : angle) + pos.ascension) * 24.0 / (Math.PI * 2);
            long newTime = this.lstToUT(lst);
            deltaT = newTime - this.time;
            this.setTime(newTime);
        } while (++count < 5 && Math.abs(deltaT) > epsilon);
        double cosD = Math.cos(pos.declination);
        double psi = Math.acos(Math.sin(this.fLatitude) / cosD);
        double x = diameter / 2.0 + refraction;
        double y = Math.asin(Math.sin(x) / Math.sin(psi));
        long delta = (long)(240.0 * y * 57.29577951308232 / cosD * 1000.0);
        return this.time + (rise ? -delta : delta);
    }

    private static final double normalize(double value, double range) {
        return value - range * Math.floor(value / range);
    }

    private static final double norm2PI(double angle) {
        return CalendarAstronomer.normalize(angle, Math.PI * 2);
    }

    private static final double normPI(double angle) {
        return CalendarAstronomer.normalize(angle + Math.PI, Math.PI * 2) - Math.PI;
    }

    private double trueAnomaly(double meanAnomaly, double eccentricity) {
        double delta;
        double E = meanAnomaly;
        do {
            delta = E - eccentricity * Math.sin(E) - meanAnomaly;
            E -= delta / (1.0 - eccentricity * Math.cos(E));
        } while (Math.abs(delta) > 1.0E-5);
        return 2.0 * Math.atan(Math.tan(E / 2.0) * Math.sqrt((1.0 + eccentricity) / (1.0 - eccentricity)));
    }

    private double eclipticObliquity() {
        if (this.eclipObliquity == Double.MIN_VALUE) {
            double epoch = 2451545.0;
            double T = (this.getJulianDay() - 2451545.0) / 36525.0;
            this.eclipObliquity = 23.439292 - 0.013004166666666666 * T - 1.6666666666666665E-7 * T * T + 5.027777777777778E-7 * T * T * T;
            this.eclipObliquity *= Math.PI / 180;
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
        return new Date(localMillis - (long)TimeZone.getDefault().getRawOffset()).toString();
    }

    private static String radToHms(double angle) {
        int hrs = (int)(angle * 3.819718634205488);
        int min = (int)((angle * 3.819718634205488 - (double)hrs) * 60.0);
        int sec = (int)((angle * 3.819718634205488 - (double)hrs - (double)min / 60.0) * 3600.0);
        return Integer.toString(hrs) + "h" + min + "m" + sec + "s";
    }

    private static String radToDms(double angle) {
        int deg = (int)(angle * 57.29577951308232);
        int min = (int)((angle * 57.29577951308232 - (double)deg) * 60.0);
        int sec = (int)((angle * 57.29577951308232 - (double)deg - (double)min / 60.0) * 3600.0);
        return Integer.toString(deg) + "\u00b0" + min + "'" + sec + "\"";
    }

    public static final class Horizon {
        public final double altitude;
        public final double azimuth;

        public Horizon(double alt, double azim) {
            this.altitude = alt;
            this.azimuth = azim;
        }

        public String toString() {
            return Double.toString(this.altitude * 57.29577951308232) + "," + this.azimuth * 57.29577951308232;
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
            return Double.toString(this.ascension * 57.29577951308232) + "," + this.declination * 57.29577951308232;
        }

        public String toHmsString() {
            return CalendarAstronomer.radToHms(this.ascension) + "," + CalendarAstronomer.radToDms(this.declination);
        }
    }

    public static final class Ecliptic {
        public final double latitude;
        public final double longitude;

        public Ecliptic(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }

        public String toString() {
            return Double.toString(this.longitude * 57.29577951308232) + "," + this.latitude * 57.29577951308232;
        }
    }

    private static interface CoordFunc {
        public Equatorial eval();
    }

    private static interface AngleFunc {
        public double eval();
    }

    private static class MoonAge {
        double value;

        MoonAge(double val) {
            this.value = val;
        }
    }

    private static class SolarLongitude {
        double value;

        SolarLongitude(double val) {
            this.value = val;
        }
    }
}

