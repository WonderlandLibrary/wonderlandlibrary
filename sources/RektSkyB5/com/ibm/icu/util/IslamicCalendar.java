/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarAstronomer;
import com.ibm.icu.impl.CalendarCache;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Locale;

public class IslamicCalendar
extends Calendar {
    private static final long serialVersionUID = -6253365474073869325L;
    public static final int MUHARRAM = 0;
    public static final int SAFAR = 1;
    public static final int RABI_1 = 2;
    public static final int RABI_2 = 3;
    public static final int JUMADA_1 = 4;
    public static final int JUMADA_2 = 5;
    public static final int RAJAB = 6;
    public static final int SHABAN = 7;
    public static final int RAMADAN = 8;
    public static final int SHAWWAL = 9;
    public static final int DHU_AL_QIDAH = 10;
    public static final int DHU_AL_HIJJAH = 11;
    private static final long HIJRA_MILLIS = -42521587200000L;
    private static final long CIVIL_EPOC = 1948440L;
    private static final long ASTRONOMICAL_EPOC = 1948439L;
    private static final int[][] LIMITS = new int[][]{{0, 0, 0, 0}, {1, 1, 5000000, 5000000}, {0, 0, 11, 11}, {1, 1, 50, 51}, new int[0], {1, 1, 29, 30}, {1, 1, 354, 355}, new int[0], {-1, -1, 5, 5}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {1, 1, 5000000, 5000000}, new int[0], {1, 1, 5000000, 5000000}, new int[0], new int[0]};
    private static final int[] UMALQURA_MONTHLENGTH = new int[]{2730, 3412, 3785, 1748, 1770, 876, 2733, 1365, 1705, 1938, 2985, 1492, 2778, 1372, 3373, 1685, 1866, 2900, 2922, 1453, 1198, 2639, 1303, 1675, 1701, 2773, 726, 2395, 1181, 2637, 3366, 3477, 1452, 2486, 698, 2651, 1323, 2709, 1738, 2793, 756, 2422, 694, 2390, 2762, 2980, 3026, 1497, 732, 2413, 1357, 2725, 2898, 2981, 1460, 2486, 1367, 663, 1355, 1699, 1874, 2917, 1386, 2731, 1323, 3221, 3402, 3493, 1482, 2774, 2391, 1195, 2379, 2725, 2898, 2922, 1397, 630, 2231, 1115, 1365, 1449, 1460, 2522, 1245, 622, 2358, 2730, 3412, 3506, 1493, 730, 2395, 1195, 2645, 2889, 2916, 2929, 1460, 2741, 2645, 3365, 3730, 3785, 1748, 2793, 2411, 1195, 2707, 3401, 3492, 3506, 2745, 1210, 2651, 1323, 2709, 2858, 2901, 1372, 1213, 573, 2333, 2709, 2890, 2906, 1389, 694, 2363, 1179, 1621, 1705, 1876, 2922, 1388, 2733, 1365, 2857, 2962, 2985, 1492, 2778, 1370, 2731, 1429, 1865, 1892, 2986, 1461, 694, 2646, 3661, 2853, 2898, 2922, 1453, 686, 2351, 1175, 1611, 1701, 1708, 2774, 1373, 1181, 2637, 3350, 3477, 1450, 1461, 730, 2395, 1197, 1429, 1738, 1764, 2794, 1269, 694, 2390, 2730, 2900, 3026, 1497, 746, 2413, 1197, 2709, 2890, 2981, 1458, 2485, 1238, 2711, 1351, 1683, 1865, 2901, 1386, 2667, 1323, 2699, 3398, 3491, 1482, 2774, 1243, 619, 2379, 2725, 2898, 2921, 1397, 374, 2231, 603, 1323, 1381, 1460, 2522, 1261, 365, 2230, 2726, 3410, 3497, 1492, 2778, 2395, 1195, 1619, 1833, 1890, 2985, 1458, 2741, 1365, 2853, 3474, 3785, 1746, 2793, 1387, 1195, 2645, 3369, 3412, 3498, 2485, 1210, 2619, 1179, 2637, 2730, 2773, 730, 2397, 1118, 2606, 3226, 3413, 1714, 1721, 1210, 2653, 1325, 2709, 2898, 2984, 2996, 1465, 730, 2394, 2890, 3492, 3793, 1768, 2922, 1389, 1333, 1685, 3402, 3496, 3540, 1754, 1371, 669, 1579, 2837, 2890, 2965, 1450, 2734, 2350, 3215, 1319, 1685, 1706, 2774, 1373, 669};
    private static final int UMALQURA_YEAR_START = 1300;
    private static final int UMALQURA_YEAR_END = 1600;
    private static final byte[] UMALQURA_YEAR_START_ESTIMATE_FIX = new byte[]{0, 0, -1, 0, -1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, -1, -1, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 1, 1, 0, 0, -1, 0, 1, 0, 1, 1, 0, 0, -1, 0, 1, 0, 0, 0, -1, 0, 1, 0, 1, 0, 0, 0, -1, 0, 0, 0, 0, -1, -1, 0, -1, 0, 1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, -1, -1, 0, 0, 0, 1, 0, 0, -1, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, -1, 0, 1, 0, 1, 1, 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, -1, 0, 1, 0, 0, -1, -1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 1, 1, 0, 0, -1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 0, 0, -1, 0, -1, 0, 1, 0, 0, 0, -1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 0, -1, -1, 0, -1, 0, 1, 0, 0, -1, -1, 0, 0, 1, 1, 0, 0, -1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
    private static CalendarAstronomer astro = new CalendarAstronomer();
    private static CalendarCache cache = new CalendarCache();
    private boolean civil = true;
    private CalculationType cType = CalculationType.ISLAMIC_CIVIL;

    public IslamicCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public IslamicCalendar(TimeZone zone) {
        this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public IslamicCalendar(Locale aLocale) {
        this(TimeZone.getDefault(), aLocale);
    }

    public IslamicCalendar(ULocale locale) {
        this(TimeZone.getDefault(), locale);
    }

    public IslamicCalendar(TimeZone zone, Locale aLocale) {
        this(zone, ULocale.forLocale(aLocale));
    }

    public IslamicCalendar(TimeZone zone, ULocale locale) {
        super(zone, locale);
        this.setCalcTypeForLocale(locale);
        this.setTimeInMillis(System.currentTimeMillis());
    }

    public IslamicCalendar(Date date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.setTime(date);
    }

    public IslamicCalendar(int year, int month, int date) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }

    public IslamicCalendar(int year, int month, int date, int hour, int minute, int second) {
        super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }

    public void setCivil(boolean beCivil) {
        this.civil = beCivil;
        if (beCivil && this.cType != CalculationType.ISLAMIC_CIVIL) {
            long m2 = this.getTimeInMillis();
            this.cType = CalculationType.ISLAMIC_CIVIL;
            this.clear();
            this.setTimeInMillis(m2);
        } else if (!beCivil && this.cType != CalculationType.ISLAMIC) {
            long m3 = this.getTimeInMillis();
            this.cType = CalculationType.ISLAMIC;
            this.clear();
            this.setTimeInMillis(m3);
        }
    }

    public boolean isCivil() {
        return this.cType == CalculationType.ISLAMIC_CIVIL;
    }

    @Override
    protected int handleGetLimit(int field, int limitType) {
        return LIMITS[field][limitType];
    }

    private static final boolean civilLeapYear(int year) {
        return (14 + 11 * year) % 30 < 11;
    }

    private long yearStart(int year) {
        long ys = 0L;
        if (this.cType == CalculationType.ISLAMIC_CIVIL || this.cType == CalculationType.ISLAMIC_TBLA || this.cType == CalculationType.ISLAMIC_UMALQURA && (year < 1300 || year > 1600)) {
            ys = (long)((year - 1) * 354) + (long)Math.floor((double)(3 + 11 * year) / 30.0);
        } else if (this.cType == CalculationType.ISLAMIC) {
            ys = IslamicCalendar.trueMonthStart(12 * (year - 1));
        } else if (this.cType == CalculationType.ISLAMIC_UMALQURA) {
            int yrStartLinearEstimate = (int)(354.3672 * (double)(year -= 1300) + 460322.05 + 0.5);
            ys = yrStartLinearEstimate + UMALQURA_YEAR_START_ESTIMATE_FIX[year];
        }
        return ys;
    }

    private long monthStart(int year, int month) {
        int realYear = year + month / 12;
        int realMonth = month % 12;
        long ms = 0L;
        if (this.cType == CalculationType.ISLAMIC_CIVIL || this.cType == CalculationType.ISLAMIC_TBLA || this.cType == CalculationType.ISLAMIC_UMALQURA && year < 1300) {
            ms = (long)Math.ceil(29.5 * (double)realMonth) + (long)((realYear - 1) * 354) + (long)Math.floor((double)(3 + 11 * realYear) / 30.0);
        } else if (this.cType == CalculationType.ISLAMIC) {
            ms = IslamicCalendar.trueMonthStart(12 * (realYear - 1) + realMonth);
        } else if (this.cType == CalculationType.ISLAMIC_UMALQURA) {
            ms = this.yearStart(year);
            for (int i2 = 0; i2 < month; ++i2) {
                ms += (long)this.handleGetMonthLength(year, i2);
            }
        }
        return ms;
    }

    private static final long trueMonthStart(long month) {
        long start = cache.get(month);
        if (start == CalendarCache.EMPTY) {
            long origin = -42521587200000L + (long)Math.floor((double)month * 29.530588853) * 86400000L;
            double age = IslamicCalendar.moonAge(origin);
            if (IslamicCalendar.moonAge(origin) >= 0.0) {
                while ((age = IslamicCalendar.moonAge(origin -= 86400000L)) >= 0.0) {
                }
            } else {
                while ((age = IslamicCalendar.moonAge(origin += 86400000L)) < 0.0) {
                }
            }
            start = (origin - -42521587200000L) / 86400000L + 1L;
            cache.put(month, start);
        }
        return start;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static final double moonAge(long time) {
        double age = 0.0;
        CalendarAstronomer calendarAstronomer = astro;
        synchronized (calendarAstronomer) {
            astro.setTime(time);
            age = astro.getMoonAge();
        }
        age = age * 180.0 / Math.PI;
        if (age > 180.0) {
            age -= 360.0;
        }
        return age;
    }

    @Override
    protected int handleGetMonthLength(int extendedYear, int month) {
        int length;
        if (this.cType == CalculationType.ISLAMIC_CIVIL || this.cType == CalculationType.ISLAMIC_TBLA || this.cType == CalculationType.ISLAMIC_UMALQURA && (extendedYear < 1300 || extendedYear > 1600)) {
            length = 29 + (month + 1) % 2;
            if (month == 11 && IslamicCalendar.civilLeapYear(extendedYear)) {
                ++length;
            }
        } else if (this.cType == CalculationType.ISLAMIC) {
            month = 12 * (extendedYear - 1) + month;
            length = (int)(IslamicCalendar.trueMonthStart(month + 1) - IslamicCalendar.trueMonthStart(month));
        } else {
            int idx = extendedYear - 1300;
            int mask = 1 << 11 - month;
            length = (UMALQURA_MONTHLENGTH[idx] & mask) == 0 ? 29 : 30;
        }
        return length;
    }

    @Override
    protected int handleGetYearLength(int extendedYear) {
        int length = 0;
        if (this.cType == CalculationType.ISLAMIC_CIVIL || this.cType == CalculationType.ISLAMIC_TBLA || this.cType == CalculationType.ISLAMIC_UMALQURA && (extendedYear < 1300 || extendedYear > 1600)) {
            length = 354 + (IslamicCalendar.civilLeapYear(extendedYear) ? 1 : 0);
        } else if (this.cType == CalculationType.ISLAMIC) {
            int month = 12 * (extendedYear - 1);
            length = (int)(IslamicCalendar.trueMonthStart(month + 12) - IslamicCalendar.trueMonthStart(month));
        } else if (this.cType == CalculationType.ISLAMIC_UMALQURA) {
            for (int i2 = 0; i2 < 12; ++i2) {
                length += this.handleGetMonthLength(extendedYear, i2);
            }
        }
        return length;
    }

    @Override
    protected int handleComputeMonthStart(int eyear, int month, boolean useMonth) {
        return (int)(this.monthStart(eyear, month) + (this.cType == CalculationType.ISLAMIC_TBLA ? 1948439L : 1948440L) - 1L);
    }

    @Override
    protected int handleGetExtendedYear() {
        int year = this.newerField(19, 1) == 19 ? this.internalGet(19, 1) : this.internalGet(1, 1);
        return year;
    }

    @Override
    protected void handleComputeFields(int julianDay) {
        int year = 0;
        int month = 0;
        int dayOfMonth = 0;
        int dayOfYear = 0;
        long days = (long)julianDay - 1948440L;
        if (this.cType == CalculationType.ISLAMIC_CIVIL || this.cType == CalculationType.ISLAMIC_TBLA) {
            if (this.cType == CalculationType.ISLAMIC_TBLA) {
                days = (long)julianDay - 1948439L;
            }
            year = (int)Math.floor((double)(30L * days + 10646L) / 10631.0);
            month = (int)Math.ceil((double)(days - 29L - this.yearStart(year)) / 29.5);
            month = Math.min(month, 11);
        } else if (this.cType == CalculationType.ISLAMIC) {
            int months = (int)Math.floor((double)days / 29.530588853);
            long monthStart = (long)Math.floor((double)months * 29.530588853 - 1.0);
            if (days - monthStart >= 25L && IslamicCalendar.moonAge(this.internalGetTimeInMillis()) > 0.0) {
                ++months;
            }
            while ((monthStart = IslamicCalendar.trueMonthStart(months)) > days) {
                --months;
            }
            year = months / 12 + 1;
            month = months % 12;
        } else if (this.cType == CalculationType.ISLAMIC_UMALQURA) {
            long umalquraStartdays = this.yearStart(1300);
            if (days < umalquraStartdays) {
                year = (int)Math.floor((double)(30L * days + 10646L) / 10631.0);
                month = (int)Math.ceil((double)(days - 29L - this.yearStart(year)) / 29.5);
                month = Math.min(month, 11);
            } else {
                int y2 = 1299;
                int m2 = 0;
                long d2 = 1L;
                while (d2 > 0L) {
                    if ((d2 = days - this.yearStart(++y2) + 1L) == (long)this.handleGetYearLength(y2)) {
                        m2 = 11;
                        break;
                    }
                    if (d2 >= (long)this.handleGetYearLength(y2)) continue;
                    int monthLen = this.handleGetMonthLength(y2, m2);
                    m2 = 0;
                    while (d2 > (long)monthLen) {
                        d2 -= (long)monthLen;
                        monthLen = this.handleGetMonthLength(y2, ++m2);
                    }
                    break block1;
                }
                year = y2;
                month = m2;
            }
        }
        dayOfMonth = (int)(days - this.monthStart(year, month)) + 1;
        dayOfYear = (int)(days - this.monthStart(year, 0) + 1L);
        this.internalSet(0, 0);
        this.internalSet(1, year);
        this.internalSet(19, year);
        this.internalSet(2, month);
        this.internalSet(5, dayOfMonth);
        this.internalSet(6, dayOfYear);
    }

    public void setCalculationType(CalculationType type) {
        this.cType = type;
        this.civil = this.cType == CalculationType.ISLAMIC_CIVIL;
    }

    public CalculationType getCalculationType() {
        return this.cType;
    }

    private void setCalcTypeForLocale(ULocale locale) {
        String localeCalType = CalendarUtil.getCalendarType(locale);
        if ("islamic-civil".equals(localeCalType)) {
            this.setCalculationType(CalculationType.ISLAMIC_CIVIL);
        } else if ("islamic-umalqura".equals(localeCalType)) {
            this.setCalculationType(CalculationType.ISLAMIC_UMALQURA);
        } else if ("islamic-tbla".equals(localeCalType)) {
            this.setCalculationType(CalculationType.ISLAMIC_TBLA);
        } else if (localeCalType.startsWith("islamic")) {
            this.setCalculationType(CalculationType.ISLAMIC);
        } else {
            this.setCalculationType(CalculationType.ISLAMIC_CIVIL);
        }
    }

    @Override
    public String getType() {
        if (this.cType == null) {
            return "islamic";
        }
        return this.cType.bcpType();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (this.cType == null) {
            this.cType = this.civil ? CalculationType.ISLAMIC_CIVIL : CalculationType.ISLAMIC;
        } else {
            this.civil = this.cType == CalculationType.ISLAMIC_CIVIL;
        }
    }

    public static enum CalculationType {
        ISLAMIC("islamic"),
        ISLAMIC_CIVIL("islamic-civil"),
        ISLAMIC_UMALQURA("islamic-umalqura"),
        ISLAMIC_TBLA("islamic-tbla");

        private String bcpType;

        private CalculationType(String bcpType) {
            this.bcpType = bcpType;
        }

        String bcpType() {
            return this.bcpType;
        }
    }
}

