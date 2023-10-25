/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.util.Date;
import java.util.Locale;

public class GregorianCalendar
extends Calendar {
    private static final long serialVersionUID = 9199388694351062137L;
    public static final int BC = 0;
    public static final int AD = 1;
    private static final int EPOCH_YEAR = 1970;
    private static final int[][] MONTH_COUNT = new int[][]{{31, 31, 0, 0}, {28, 29, 31, 31}, {31, 31, 59, 60}, {30, 30, 90, 91}, {31, 31, 120, 121}, {30, 30, 151, 152}, {31, 31, 181, 182}, {31, 31, 212, 213}, {30, 30, 243, 244}, {31, 31, 273, 274}, {30, 30, 304, 305}, {31, 31, 334, 335}};
    private static final int[][] LIMITS = new int[][]{{0, 0, 1, 1}, {1, 1, 5828963, 5838270}, {0, 0, 11, 11}, {1, 1, 52, 53}, new int[0], {1, 1, 28, 31}, {1, 1, 365, 366}, new int[0], {-1, -1, 4, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {-5838270, -5838270, 5828964, 5838271}, new int[0], {-5838269, -5838269, 5828963, 5838270}, new int[0], new int[0]};
    private long gregorianCutover = -12219292800000L;
    private transient int cutoverJulianDay = 2299161;
    private transient int gregorianCutoverYear = 1582;
    protected transient boolean isGregorian;
    protected transient boolean invertGregorian;

    protected int handleGetLimit(int field, int limitType) {
        return LIMITS[field][limitType];
    }

    public GregorianCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public GregorianCalendar(TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public GregorianCalendar(Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }

    public GregorianCalendar(ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }

    public GregorianCalendar(TimeZone zone, Locale aLocale) {
        super(zone, aLocale);
        this.setTimeInMillis(System.currentTimeMillis());
    }

    public GregorianCalendar(TimeZone zone, ULocale locale) {
        super(zone, locale);
        this.setTimeInMillis(System.currentTimeMillis());
    }

    public GregorianCalendar(int year, int month, int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(0, 1);
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }

    public GregorianCalendar(int year, int month, int date, int hour, int minute) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(0, 1);
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
    }

    public GregorianCalendar(int year, int month, int date, int hour, int minute, int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(0, 1);
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }

    public void setGregorianChange(Date date) {
        this.gregorianCutover = date.getTime();
        if (this.gregorianCutover <= -184303902528000000L) {
            this.cutoverJulianDay = Integer.MIN_VALUE;
            this.gregorianCutoverYear = Integer.MIN_VALUE;
        } else if (this.gregorianCutover >= 183882168921600000L) {
            this.cutoverJulianDay = Integer.MAX_VALUE;
            this.gregorianCutoverYear = Integer.MAX_VALUE;
        } else {
            this.cutoverJulianDay = (int)GregorianCalendar.floorDivide(this.gregorianCutover, 86400000L);
            GregorianCalendar cal = new GregorianCalendar(this.getTimeZone());
            cal.setTime(date);
            this.gregorianCutoverYear = cal.get(19);
        }
    }

    public final Date getGregorianChange() {
        return new Date(this.gregorianCutover);
    }

    public boolean isLeapYear(int year) {
        return year >= this.gregorianCutoverYear ? year % 4 == 0 && (year % 100 != 0 || year % 400 == 0) : year % 4 == 0;
    }

    public boolean isEquivalentTo(Calendar other) {
        return super.isEquivalentTo(other) && this.gregorianCutover == ((GregorianCalendar)other).gregorianCutover;
    }

    public int hashCode() {
        return super.hashCode() ^ (int)this.gregorianCutover;
    }

    public void roll(int field, int amount) {
        switch (field) {
            case 3: {
                int woy = this.get(3);
                int isoYear = this.get(17);
                int isoDoy = this.internalGet(6);
                if (this.internalGet(2) == 0) {
                    if (woy >= 52) {
                        isoDoy += this.handleGetYearLength(isoYear);
                    }
                } else if (woy == 1) {
                    isoDoy -= this.handleGetYearLength(isoYear - 1);
                }
                if ((woy += amount) < 1 || woy > 52) {
                    int lastDoy = this.handleGetYearLength(isoYear);
                    int lastRelDow = (lastDoy - isoDoy + this.internalGet(7) - this.getFirstDayOfWeek()) % 7;
                    if (lastRelDow < 0) {
                        lastRelDow += 7;
                    }
                    if (6 - lastRelDow >= this.getMinimalDaysInFirstWeek()) {
                        lastDoy -= 7;
                    }
                    int lastWoy = this.weekNumber(lastDoy, lastRelDow + 1);
                    woy = (woy + lastWoy - 1) % lastWoy + 1;
                }
                this.set(3, woy);
                this.set(1, isoYear);
                return;
            }
        }
        super.roll(field, amount);
    }

    public int getActualMinimum(int field) {
        return this.getMinimum(field);
    }

    public int getActualMaximum(int field) {
        switch (field) {
            case 1: {
                Calendar cal = (Calendar)this.clone();
                cal.setLenient(true);
                int era = cal.get(0);
                Date d = cal.getTime();
                int lowGood = LIMITS[1][1];
                int highBad = LIMITS[1][2] + 1;
                while (lowGood + 1 < highBad) {
                    int y = (lowGood + highBad) / 2;
                    cal.set(1, y);
                    if (cal.get(1) == y && cal.get(0) == era) {
                        lowGood = y;
                        continue;
                    }
                    highBad = y;
                    cal.setTime(d);
                }
                return lowGood;
            }
        }
        return super.getActualMaximum(field);
    }

    boolean inDaylightTime() {
        if (!this.getTimeZone().useDaylightTime()) {
            return false;
        }
        this.complete();
        return this.internalGet(16) != 0;
    }

    protected int handleGetMonthLength(int extendedYear, int month) {
        if (month < 0 || month > 11) {
            int[] rem = new int[1];
            extendedYear += GregorianCalendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        return MONTH_COUNT[month][this.isLeapYear(extendedYear) ? 1 : 0];
    }

    protected int handleGetYearLength(int eyear) {
        return this.isLeapYear(eyear) ? 366 : 365;
    }

    protected void handleComputeFields(int julianDay) {
        int eyear;
        int dayOfYear;
        int dayOfMonth;
        int month;
        if (julianDay >= this.cutoverJulianDay) {
            month = this.getGregorianMonth();
            dayOfMonth = this.getGregorianDayOfMonth();
            dayOfYear = this.getGregorianDayOfYear();
            eyear = this.getGregorianYear();
        } else {
            int march1;
            long julianEpochDay = julianDay - 1721424;
            eyear = (int)GregorianCalendar.floorDivide(4L * julianEpochDay + 1464L, 1461L);
            long january1 = 365 * (eyear - 1) + GregorianCalendar.floorDivide(eyear - 1, 4);
            dayOfYear = (int)(julianEpochDay - january1);
            boolean isLeap = (eyear & 3) == 0;
            int correction = 0;
            int n = march1 = isLeap ? 60 : 59;
            if (dayOfYear >= march1) {
                correction = isLeap ? 1 : 2;
            }
            month = (12 * (dayOfYear + correction) + 6) / 367;
            dayOfMonth = dayOfYear - MONTH_COUNT[month][isLeap ? 3 : 2] + 1;
            ++dayOfYear;
        }
        this.internalSet(2, month);
        this.internalSet(5, dayOfMonth);
        this.internalSet(6, dayOfYear);
        this.internalSet(19, eyear);
        int era = 1;
        if (eyear < 1) {
            era = 0;
            eyear = 1 - eyear;
        }
        this.internalSet(0, era);
        this.internalSet(1, eyear);
    }

    protected int handleGetExtendedYear() {
        int era;
        int year = this.newerField(19, 1) == 19 ? this.internalGet(19, 1970) : ((era = this.internalGet(0, 1)) == 0 ? 1 - this.internalGet(1, 1) : this.internalGet(1, 1970));
        return year;
    }

    protected int handleComputeJulianDay(int bestField) {
        this.invertGregorian = false;
        int jd = super.handleComputeJulianDay(bestField);
        if (this.isGregorian != jd >= this.cutoverJulianDay) {
            this.invertGregorian = true;
            jd = super.handleComputeJulianDay(bestField);
        }
        return jd;
    }

    protected int handleComputeMonthStart(int eyear, int month, boolean useMonth) {
        if (month < 0 || month > 11) {
            int[] rem = new int[1];
            eyear += GregorianCalendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        boolean isLeap = eyear % 4 == 0;
        int y = eyear - 1;
        int julianDay = 365 * y + GregorianCalendar.floorDivide(y, 4) + 1721423;
        boolean bl = this.isGregorian = eyear >= this.gregorianCutoverYear;
        if (this.invertGregorian) {
            boolean bl2 = this.isGregorian = !this.isGregorian;
        }
        if (this.isGregorian) {
            isLeap = isLeap && (eyear % 100 != 0 || eyear % 400 == 0);
            julianDay += GregorianCalendar.floorDivide(y, 400) - GregorianCalendar.floorDivide(y, 100) + 2;
        }
        if (month != 0) {
            julianDay += MONTH_COUNT[month][isLeap ? 3 : 2];
        }
        return julianDay;
    }

    public String getType() {
        return "gregorian";
    }
}

