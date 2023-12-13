/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.BuddhistCalendar;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.CopticCalendar;
import com.ibm.icu.util.DangiCalendar;
import com.ibm.icu.util.EthiopicCalendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.IndianCalendar;
import com.ibm.icu.util.IslamicCalendar;
import com.ibm.icu.util.JapaneseCalendar;
import com.ibm.icu.util.PersianCalendar;
import com.ibm.icu.util.TaiwanCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;

public abstract class Calendar
implements Serializable,
Cloneable,
Comparable<Calendar> {
    public static final int ERA = 0;
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int WEEK_OF_YEAR = 3;
    public static final int WEEK_OF_MONTH = 4;
    public static final int DATE = 5;
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_YEAR = 6;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    public static final int AM_PM = 9;
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int MILLISECOND = 14;
    public static final int ZONE_OFFSET = 15;
    public static final int DST_OFFSET = 16;
    public static final int YEAR_WOY = 17;
    public static final int DOW_LOCAL = 18;
    public static final int EXTENDED_YEAR = 19;
    public static final int JULIAN_DAY = 20;
    public static final int MILLISECONDS_IN_DAY = 21;
    public static final int IS_LEAP_MONTH = 22;
    @Deprecated
    protected static final int BASE_FIELD_COUNT = 23;
    @Deprecated
    protected static final int MAX_FIELD_COUNT = 32;
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
    public static final int JANUARY = 0;
    public static final int FEBRUARY = 1;
    public static final int MARCH = 2;
    public static final int APRIL = 3;
    public static final int MAY = 4;
    public static final int JUNE = 5;
    public static final int JULY = 6;
    public static final int AUGUST = 7;
    public static final int SEPTEMBER = 8;
    public static final int OCTOBER = 9;
    public static final int NOVEMBER = 10;
    public static final int DECEMBER = 11;
    public static final int UNDECIMBER = 12;
    public static final int AM = 0;
    public static final int PM = 1;
    @Deprecated
    public static final int WEEKDAY = 0;
    @Deprecated
    public static final int WEEKEND = 1;
    @Deprecated
    public static final int WEEKEND_ONSET = 2;
    @Deprecated
    public static final int WEEKEND_CEASE = 3;
    public static final int WALLTIME_LAST = 0;
    public static final int WALLTIME_FIRST = 1;
    public static final int WALLTIME_NEXT_VALID = 2;
    protected static final int ONE_SECOND = 1000;
    protected static final int ONE_MINUTE = 60000;
    protected static final int ONE_HOUR = 3600000;
    protected static final long ONE_DAY = 86400000L;
    protected static final long ONE_WEEK = 604800000L;
    protected static final int JAN_1_1_JULIAN_DAY = 1721426;
    protected static final int EPOCH_JULIAN_DAY = 2440588;
    protected static final int MIN_JULIAN = -2130706432;
    protected static final long MIN_MILLIS = -184303902528000000L;
    protected static final Date MIN_DATE = new Date(-184303902528000000L);
    protected static final int MAX_JULIAN = 0x7F000000;
    protected static final long MAX_MILLIS = 183882168921600000L;
    protected static final Date MAX_DATE = new Date(183882168921600000L);
    private static final int MAX_HOURS = 548;
    private transient int[] fields;
    private transient int[] stamp;
    private long time;
    private transient boolean isTimeSet;
    private transient boolean areFieldsSet;
    private transient boolean areAllFieldsSet;
    private transient boolean areFieldsVirtuallySet;
    private boolean lenient = true;
    private TimeZone zone;
    private int firstDayOfWeek;
    private int minimalDaysInFirstWeek;
    private int weekendOnset;
    private int weekendOnsetMillis;
    private int weekendCease;
    private int weekendCeaseMillis;
    private int repeatedWallTime = 0;
    private int skippedWallTime = 0;
    protected static final int UNSET = 0;
    protected static final int INTERNALLY_SET = 1;
    protected static final int MINIMUM_USER_STAMP = 2;
    private transient int nextStamp = 2;
    private static int STAMP_MAX = 10000;
    private static final long serialVersionUID = 6222646104888790989L;
    private transient int internalSetMask;
    private transient int gregorianYear;
    private transient int gregorianMonth;
    private transient int gregorianDayOfYear;
    private transient int gregorianDayOfMonth;
    private static final ICUCache<String, PatternData> PATTERN_CACHE = new SimpleCache<String, PatternData>();
    private static final String[] DEFAULT_PATTERNS = new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm", "EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}"};
    private static final char QUOTE = '\'';
    private static final int FIELD_DIFF_MAX_INT = Integer.MAX_VALUE;
    private static final int[][] LIMITS = new int[][]{new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {1, 1, 7, 7}, new int[0], {0, 0, 1, 1}, {0, 0, 11, 11}, {0, 0, 23, 23}, {0, 0, 59, 59}, {0, 0, 59, 59}, {0, 0, 999, 999}, {-43200000, -43200000, 43200000, 43200000}, {0, 0, 3600000, 3600000}, new int[0], {1, 1, 7, 7}, new int[0], {-2130706432, -2130706432, 0x7F000000, 0x7F000000}, {0, 0, 86399999, 86399999}, {0, 0, 1, 1}};
    protected static final int MINIMUM = 0;
    protected static final int GREATEST_MINIMUM = 1;
    protected static final int LEAST_MAXIMUM = 2;
    protected static final int MAXIMUM = 3;
    private static final WeekDataCache WEEK_DATA_CACHE = new WeekDataCache();
    protected static final int RESOLVE_REMAP = 32;
    static final int[][][] DATE_PRECEDENCE = new int[][][]{new int[][]{{5}, {3, 7}, {4, 7}, {8, 7}, {3, 18}, {4, 18}, {8, 18}, {6}, {37, 1}, {35, 17}}, new int[][]{{3}, {4}, {8}, {40, 7}, {40, 18}}};
    static final int[][][] DOW_PRECEDENCE = new int[][][]{new int[][]{{7}, {18}}};
    private static final int[] FIND_ZONE_TRANSITION_TIME_UNITS = new int[]{3600000, 1800000, 60000, 1000};
    private static final int[][] GREGORIAN_MONTH_COUNT = new int[][]{{31, 31, 0, 0}, {28, 29, 31, 31}, {31, 31, 59, 60}, {30, 30, 90, 91}, {31, 31, 120, 121}, {30, 30, 151, 152}, {31, 31, 181, 182}, {31, 31, 212, 213}, {30, 30, 243, 244}, {31, 31, 273, 274}, {30, 30, 304, 305}, {31, 31, 334, 335}};
    private static final String[] FIELD_NAME = new String[]{"ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET", "YEAR_WOY", "DOW_LOCAL", "EXTENDED_YEAR", "JULIAN_DAY", "MILLISECONDS_IN_DAY"};
    private ULocale validLocale;
    private ULocale actualLocale;

    protected Calendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }

    protected Calendar(TimeZone zone, Locale aLocale) {
        this(zone, ULocale.forLocale(aLocale));
    }

    protected Calendar(TimeZone zone, ULocale locale) {
        this.zone = zone;
        this.setWeekData(Calendar.getRegionForCalendar(locale));
        this.setCalendarLocale(locale);
        this.initInternal();
    }

    private void setCalendarLocale(ULocale locale) {
        ULocale calLocale = locale;
        if (locale.getVariant().length() != 0 || locale.getKeywords() != null) {
            String calType;
            String region;
            StringBuilder buf = new StringBuilder();
            buf.append(locale.getLanguage());
            String script = locale.getScript();
            if (script.length() > 0) {
                buf.append("_").append(script);
            }
            if ((region = locale.getCountry()).length() > 0) {
                buf.append("_").append(region);
            }
            if ((calType = locale.getKeywordValue("calendar")) != null) {
                buf.append("@calendar=").append(calType);
            }
            calLocale = new ULocale(buf.toString());
        }
        this.setLocale(calLocale, calLocale);
    }

    private void recalculateStamp() {
        this.nextStamp = 1;
        for (int j2 = 0; j2 < this.stamp.length; ++j2) {
            int currentValue = STAMP_MAX;
            int index = -1;
            for (int i2 = 0; i2 < this.stamp.length; ++i2) {
                if (this.stamp[i2] <= this.nextStamp || this.stamp[i2] >= currentValue) continue;
                currentValue = this.stamp[i2];
                index = i2;
            }
            if (index < 0) break;
            this.stamp[index] = ++this.nextStamp;
        }
        ++this.nextStamp;
    }

    private void initInternal() {
        this.fields = this.handleCreateFields();
        if (this.fields == null || this.fields.length < 23 || this.fields.length > 32) {
            throw new IllegalStateException("Invalid fields[]");
        }
        this.stamp = new int[this.fields.length];
        int mask = 4718695;
        for (int i2 = 23; i2 < this.fields.length; ++i2) {
            mask |= 1 << i2;
        }
        this.internalSetMask = mask;
    }

    public static Calendar getInstance() {
        return Calendar.getInstanceInternal(null, null);
    }

    public static Calendar getInstance(TimeZone zone) {
        return Calendar.getInstanceInternal(zone, null);
    }

    public static Calendar getInstance(Locale aLocale) {
        return Calendar.getInstanceInternal(null, ULocale.forLocale(aLocale));
    }

    public static Calendar getInstance(ULocale locale) {
        return Calendar.getInstanceInternal(null, locale);
    }

    public static Calendar getInstance(TimeZone zone, Locale aLocale) {
        return Calendar.getInstanceInternal(zone, ULocale.forLocale(aLocale));
    }

    public static Calendar getInstance(TimeZone zone, ULocale locale) {
        return Calendar.getInstanceInternal(zone, locale);
    }

    private static Calendar getInstanceInternal(TimeZone tz, ULocale locale) {
        if (locale == null) {
            locale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        if (tz == null) {
            tz = TimeZone.getDefault();
        }
        Calendar cal = Calendar.createInstance(locale);
        cal.setTimeZone(tz);
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal;
    }

    private static String getRegionForCalendar(ULocale loc) {
        String region = ULocale.getRegionForSupplementalData(loc, true);
        if (region.length() == 0) {
            region = "001";
        }
        return region;
    }

    private static CalType getCalendarTypeForLocale(ULocale l2) {
        String s2 = CalendarUtil.getCalendarType(l2);
        if (s2 != null) {
            s2 = s2.toLowerCase(Locale.ENGLISH);
            for (CalType type : CalType.values()) {
                if (!s2.equals(type.id)) continue;
                return type;
            }
        }
        return CalType.UNKNOWN;
    }

    private static Calendar createInstance(ULocale locale) {
        Calendar cal = null;
        TimeZone zone = TimeZone.getDefault();
        CalType calType = Calendar.getCalendarTypeForLocale(locale);
        if (calType == CalType.UNKNOWN) {
            calType = CalType.GREGORIAN;
        }
        switch (calType) {
            case GREGORIAN: {
                cal = new GregorianCalendar(zone, locale);
                break;
            }
            case ISO8601: {
                cal = new GregorianCalendar(zone, locale);
                cal.setFirstDayOfWeek(2);
                cal.setMinimalDaysInFirstWeek(4);
                break;
            }
            case BUDDHIST: {
                cal = new BuddhistCalendar(zone, locale);
                break;
            }
            case CHINESE: {
                cal = new ChineseCalendar(zone, locale);
                break;
            }
            case COPTIC: {
                cal = new CopticCalendar(zone, locale);
                break;
            }
            case DANGI: {
                cal = new DangiCalendar(zone, locale);
                break;
            }
            case ETHIOPIC: {
                cal = new EthiopicCalendar(zone, locale);
                break;
            }
            case ETHIOPIC_AMETE_ALEM: {
                cal = new EthiopicCalendar(zone, locale);
                ((EthiopicCalendar)cal).setAmeteAlemEra(true);
                break;
            }
            case HEBREW: {
                cal = new HebrewCalendar(zone, locale);
                break;
            }
            case INDIAN: {
                cal = new IndianCalendar(zone, locale);
                break;
            }
            case ISLAMIC_CIVIL: 
            case ISLAMIC_UMALQURA: 
            case ISLAMIC_TBLA: 
            case ISLAMIC_RGSA: 
            case ISLAMIC: {
                cal = new IslamicCalendar(zone, locale);
                break;
            }
            case JAPANESE: {
                cal = new JapaneseCalendar(zone, locale);
                break;
            }
            case PERSIAN: {
                cal = new PersianCalendar(zone, locale);
                break;
            }
            case ROC: {
                cal = new TaiwanCalendar(zone, locale);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown calendar type");
            }
        }
        return cal;
    }

    public static Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales();
    }

    public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed) {
        String prefRegion = ULocale.getRegionForSupplementalData(locale, true);
        ArrayList<String> values = new ArrayList<String>();
        UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        UResourceBundle calPref = rb.get("calendarPreferenceData");
        UResourceBundle order = null;
        try {
            order = calPref.get(prefRegion);
        }
        catch (MissingResourceException mre) {
            order = calPref.get("001");
        }
        String[] caltypes = order.getStringArray();
        if (commonlyUsed) {
            return caltypes;
        }
        for (int i2 = 0; i2 < caltypes.length; ++i2) {
            values.add(caltypes[i2]);
        }
        for (CalType t2 : CalType.values()) {
            if (values.contains(t2.id)) continue;
            values.add(t2.id);
        }
        return values.toArray(new String[values.size()]);
    }

    public final Date getTime() {
        return new Date(this.getTimeInMillis());
    }

    public final void setTime(Date date) {
        this.setTimeInMillis(date.getTime());
    }

    public long getTimeInMillis() {
        if (!this.isTimeSet) {
            this.updateTime();
        }
        return this.time;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void setTimeInMillis(long millis) {
        if (millis > 183882168921600000L) {
            if (!this.isLenient()) throw new IllegalArgumentException("millis value greater than upper bounds for a Calendar : " + millis);
            millis = 183882168921600000L;
        } else if (millis < -184303902528000000L) {
            if (!this.isLenient()) throw new IllegalArgumentException("millis value less than lower bounds for a Calendar : " + millis);
            millis = -184303902528000000L;
        }
        this.time = millis;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.areFieldsVirtuallySet = true;
        this.isTimeSet = true;
        for (int i2 = 0; i2 < this.fields.length; ++i2) {
            this.stamp[i2] = 0;
            this.fields[i2] = 0;
        }
    }

    public final int get(int field) {
        this.complete();
        return this.fields[field];
    }

    protected final int internalGet(int field) {
        return this.fields[field];
    }

    protected final int internalGet(int field, int defaultValue) {
        return this.stamp[field] > 0 ? this.fields[field] : defaultValue;
    }

    public final void set(int field, int value) {
        if (this.areFieldsVirtuallySet) {
            this.computeFields();
        }
        this.fields[field] = value;
        if (this.nextStamp == STAMP_MAX) {
            this.recalculateStamp();
        }
        ++this.nextStamp;
        this.areFieldsVirtuallySet = false;
        this.areFieldsSet = false;
        this.isTimeSet = false;
    }

    public final void set(int year, int month, int date) {
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
    }

    public final void set(int year, int month, int date, int hour, int minute) {
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
    }

    public final void set(int year, int month, int date, int hour, int minute, int second) {
        this.set(1, year);
        this.set(2, month);
        this.set(5, date);
        this.set(11, hour);
        this.set(12, minute);
        this.set(13, second);
    }

    private static int gregoYearFromIslamicStart(int year) {
        int shift = 0;
        if (year >= 1397) {
            int cycle = (year - 1397) / 67;
            int offset = (year - 1397) % 67;
            shift = 2 * cycle + (offset >= 33 ? 1 : 0);
        } else {
            int cycle = (year - 1396) / 67 - 1;
            int offset = -(year - 1396) % 67;
            shift = 2 * cycle + (offset <= 33 ? 1 : 0);
        }
        return year + 579 - shift;
    }

    @Deprecated
    public final int getRelatedYear() {
        int year = this.get(19);
        CalType type = CalType.GREGORIAN;
        String typeString = this.getType();
        for (CalType testType : CalType.values()) {
            if (!typeString.equals(testType.id)) continue;
            type = testType;
            break;
        }
        switch (type) {
            case PERSIAN: {
                year += 622;
                break;
            }
            case HEBREW: {
                year -= 3760;
                break;
            }
            case CHINESE: {
                year -= 2637;
                break;
            }
            case INDIAN: {
                year += 79;
                break;
            }
            case COPTIC: {
                year += 284;
                break;
            }
            case ETHIOPIC: {
                year += 8;
                break;
            }
            case ETHIOPIC_AMETE_ALEM: {
                year -= 5492;
                break;
            }
            case DANGI: {
                year -= 2333;
                break;
            }
            case ISLAMIC_CIVIL: 
            case ISLAMIC_UMALQURA: 
            case ISLAMIC_TBLA: 
            case ISLAMIC_RGSA: 
            case ISLAMIC: {
                year = Calendar.gregoYearFromIslamicStart(year);
                break;
            }
        }
        return year;
    }

    private static int firstIslamicStartYearFromGrego(int year) {
        int shift = 0;
        if (year >= 1977) {
            int cycle = (year - 1977) / 65;
            int offset = (year - 1977) % 65;
            shift = 2 * cycle + (offset >= 32 ? 1 : 0);
        } else {
            int cycle = (year - 1976) / 65 - 1;
            int offset = -(year - 1976) % 65;
            shift = 2 * cycle + (offset <= 32 ? 1 : 0);
        }
        return year - 579 + shift;
    }

    @Deprecated
    public final void setRelatedYear(int year) {
        CalType type = CalType.GREGORIAN;
        String typeString = this.getType();
        for (CalType testType : CalType.values()) {
            if (!typeString.equals(testType.id)) continue;
            type = testType;
            break;
        }
        switch (type) {
            case PERSIAN: {
                year -= 622;
                break;
            }
            case HEBREW: {
                year += 3760;
                break;
            }
            case CHINESE: {
                year += 2637;
                break;
            }
            case INDIAN: {
                year -= 79;
                break;
            }
            case COPTIC: {
                year -= 284;
                break;
            }
            case ETHIOPIC: {
                year -= 8;
                break;
            }
            case ETHIOPIC_AMETE_ALEM: {
                year += 5492;
                break;
            }
            case DANGI: {
                year += 2333;
                break;
            }
            case ISLAMIC_CIVIL: 
            case ISLAMIC_UMALQURA: 
            case ISLAMIC_TBLA: 
            case ISLAMIC_RGSA: 
            case ISLAMIC: {
                year = Calendar.firstIslamicStartYearFromGrego(year);
                break;
            }
        }
        this.set(19, year);
    }

    public final void clear() {
        for (int i2 = 0; i2 < this.fields.length; ++i2) {
            this.stamp[i2] = 0;
            this.fields[i2] = 0;
        }
        this.areFieldsVirtuallySet = false;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.isTimeSet = false;
    }

    public final void clear(int field) {
        if (this.areFieldsVirtuallySet) {
            this.computeFields();
        }
        this.fields[field] = 0;
        this.stamp[field] = 0;
        this.areFieldsVirtuallySet = false;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.isTimeSet = false;
    }

    public final boolean isSet(int field) {
        return this.areFieldsVirtuallySet || this.stamp[field] != 0;
    }

    protected void complete() {
        if (!this.isTimeSet) {
            this.updateTime();
        }
        if (!this.areFieldsSet) {
            this.computeFields();
            this.areFieldsSet = true;
            this.areAllFieldsSet = true;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Calendar that = (Calendar)obj;
        return this.isEquivalentTo(that) && this.getTimeInMillis() == that.getTime().getTime();
    }

    public boolean isEquivalentTo(Calendar other) {
        return this.getClass() == other.getClass() && this.isLenient() == other.isLenient() && this.getFirstDayOfWeek() == other.getFirstDayOfWeek() && this.getMinimalDaysInFirstWeek() == other.getMinimalDaysInFirstWeek() && this.getTimeZone().equals(other.getTimeZone()) && this.getRepeatedWallTimeOption() == other.getRepeatedWallTimeOption() && this.getSkippedWallTimeOption() == other.getSkippedWallTimeOption();
    }

    public int hashCode() {
        return (this.lenient ? 1 : 0) | this.firstDayOfWeek << 1 | this.minimalDaysInFirstWeek << 4 | this.repeatedWallTime << 7 | this.skippedWallTime << 9 | this.zone.hashCode() << 11;
    }

    private long compare(Object that) {
        long thatMs;
        if (that instanceof Calendar) {
            thatMs = ((Calendar)that).getTimeInMillis();
        } else if (that instanceof Date) {
            thatMs = ((Date)that).getTime();
        } else {
            throw new IllegalArgumentException(that + "is not a Calendar or Date");
        }
        return this.getTimeInMillis() - thatMs;
    }

    public boolean before(Object when) {
        return this.compare(when) < 0L;
    }

    public boolean after(Object when) {
        return this.compare(when) > 0L;
    }

    public int getActualMaximum(int field) {
        int result;
        switch (field) {
            case 5: {
                Calendar cal = (Calendar)this.clone();
                cal.setLenient(true);
                cal.prepareGetActual(field, false);
                result = this.handleGetMonthLength(cal.get(19), cal.get(2));
                break;
            }
            case 6: {
                Calendar cal = (Calendar)this.clone();
                cal.setLenient(true);
                cal.prepareGetActual(field, false);
                result = this.handleGetYearLength(cal.get(19));
                break;
            }
            case 0: 
            case 7: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 18: 
            case 20: 
            case 21: {
                result = this.getMaximum(field);
                break;
            }
            default: {
                result = this.getActualHelper(field, this.getLeastMaximum(field), this.getMaximum(field));
            }
        }
        return result;
    }

    public int getActualMinimum(int field) {
        int result;
        switch (field) {
            case 7: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 18: 
            case 20: 
            case 21: {
                result = this.getMinimum(field);
                break;
            }
            default: {
                result = this.getActualHelper(field, this.getGreatestMinimum(field), this.getMinimum(field));
            }
        }
        return result;
    }

    protected void prepareGetActual(int field, boolean isMinimum) {
        this.set(21, 0);
        switch (field) {
            case 1: 
            case 19: {
                this.set(6, this.getGreatestMinimum(6));
                break;
            }
            case 17: {
                this.set(3, this.getGreatestMinimum(3));
                break;
            }
            case 2: {
                this.set(5, this.getGreatestMinimum(5));
                break;
            }
            case 8: {
                this.set(5, 1);
                this.set(7, this.get(7));
                break;
            }
            case 3: 
            case 4: {
                int dow = this.firstDayOfWeek;
                if (isMinimum && (dow = (dow + 6) % 7) < 1) {
                    dow += 7;
                }
                this.set(7, dow);
            }
        }
        this.set(field, this.getGreatestMinimum(field));
    }

    private int getActualHelper(int field, int startValue, int endValue) {
        if (startValue == endValue) {
            return startValue;
        }
        int delta = endValue > startValue ? 1 : -1;
        Calendar work = (Calendar)this.clone();
        work.complete();
        work.setLenient(true);
        work.prepareGetActual(field, delta < 0);
        work.set(field, startValue);
        if (work.get(field) != startValue && field != 4 && delta > 0) {
            return startValue;
        }
        int result = startValue;
        do {
            work.add(field, delta);
            if (work.get(field) != (startValue += delta)) break;
            result = startValue;
        } while (startValue != endValue);
        return result;
    }

    public final void roll(int field, boolean up) {
        this.roll(field, up ? 1 : -1);
    }

    public void roll(int field, int amount) {
        if (amount == 0) {
            return;
        }
        this.complete();
        switch (field) {
            case 0: 
            case 5: 
            case 9: 
            case 12: 
            case 13: 
            case 14: 
            case 21: {
                int min = this.getActualMinimum(field);
                int max = this.getActualMaximum(field);
                int gap = max - min + 1;
                int value = this.internalGet(field) + amount;
                value = (value - min) % gap;
                if (value < 0) {
                    value += gap;
                }
                this.set(field, value += min);
                return;
            }
            case 10: 
            case 11: {
                long start = this.getTimeInMillis();
                int oldHour = this.internalGet(field);
                int max = this.getMaximum(field);
                int newHour = (oldHour + amount) % (max + 1);
                if (newHour < 0) {
                    newHour += max + 1;
                }
                this.setTimeInMillis(start + 3600000L * ((long)newHour - (long)oldHour));
                return;
            }
            case 2: {
                int max = this.getActualMaximum(2);
                int mon = (this.internalGet(2) + amount) % (max + 1);
                if (mon < 0) {
                    mon += max + 1;
                }
                this.set(2, mon);
                this.pinField(5);
                return;
            }
            case 1: 
            case 17: {
                String calType;
                boolean era0WithYearsThatGoBackwards = false;
                int era = this.get(0);
                if (era == 0 && ((calType = this.getType()).equals("gregorian") || calType.equals("roc") || calType.equals("coptic"))) {
                    amount = -amount;
                    era0WithYearsThatGoBackwards = true;
                }
                int newYear = this.internalGet(field) + amount;
                if (era > 0 || newYear >= 1) {
                    int maxYear = this.getActualMaximum(field);
                    if (maxYear < 32768) {
                        if (newYear < 1) {
                            newYear = maxYear - -newYear % maxYear;
                        } else if (newYear > maxYear) {
                            newYear = (newYear - 1) % maxYear + 1;
                        }
                    } else if (newYear < 1) {
                        newYear = 1;
                    }
                } else if (era0WithYearsThatGoBackwards) {
                    newYear = 1;
                }
                this.set(field, newYear);
                this.pinField(2);
                this.pinField(5);
                return;
            }
            case 19: {
                this.set(field, this.internalGet(field) + amount);
                this.pinField(2);
                this.pinField(5);
                return;
            }
            case 4: {
                int fdm;
                int dow = this.internalGet(7) - this.getFirstDayOfWeek();
                if (dow < 0) {
                    dow += 7;
                }
                if ((fdm = (dow - this.internalGet(5) + 1) % 7) < 0) {
                    fdm += 7;
                }
                int start = 7 - fdm < this.getMinimalDaysInFirstWeek() ? 8 - fdm : 1 - fdm;
                int monthLen = this.getActualMaximum(5);
                int ldm = (monthLen - this.internalGet(5) + dow) % 7;
                int limit = monthLen + 7 - ldm;
                int gap = limit - start;
                int day_of_month = (this.internalGet(5) + amount * 7 - start) % gap;
                if (day_of_month < 0) {
                    day_of_month += gap;
                }
                if ((day_of_month += start) < 1) {
                    day_of_month = 1;
                }
                if (day_of_month > monthLen) {
                    day_of_month = monthLen;
                }
                this.set(5, day_of_month);
                return;
            }
            case 3: {
                int fdy;
                int dow = this.internalGet(7) - this.getFirstDayOfWeek();
                if (dow < 0) {
                    dow += 7;
                }
                if ((fdy = (dow - this.internalGet(6) + 1) % 7) < 0) {
                    fdy += 7;
                }
                int start = 7 - fdy < this.getMinimalDaysInFirstWeek() ? 8 - fdy : 1 - fdy;
                int yearLen = this.getActualMaximum(6);
                int ldy = (yearLen - this.internalGet(6) + dow) % 7;
                int limit = yearLen + 7 - ldy;
                int gap = limit - start;
                int day_of_year = (this.internalGet(6) + amount * 7 - start) % gap;
                if (day_of_year < 0) {
                    day_of_year += gap;
                }
                if ((day_of_year += start) < 1) {
                    day_of_year = 1;
                }
                if (day_of_year > yearLen) {
                    day_of_year = yearLen;
                }
                this.set(6, day_of_year);
                this.clear(2);
                return;
            }
            case 6: {
                long delta = (long)amount * 86400000L;
                long min2 = this.time - (long)(this.internalGet(6) - 1) * 86400000L;
                int yearLength = this.getActualMaximum(6);
                this.time = (this.time + delta - min2) % ((long)yearLength * 86400000L);
                if (this.time < 0L) {
                    this.time += (long)yearLength * 86400000L;
                }
                this.setTimeInMillis(this.time + min2);
                return;
            }
            case 7: 
            case 18: {
                long delta = (long)amount * 86400000L;
                int leadDays = this.internalGet(field);
                if ((leadDays -= field == 7 ? this.getFirstDayOfWeek() : 1) < 0) {
                    leadDays += 7;
                }
                long min2 = this.time - (long)leadDays * 86400000L;
                this.time = (this.time + delta - min2) % 604800000L;
                if (this.time < 0L) {
                    this.time += 604800000L;
                }
                this.setTimeInMillis(this.time + min2);
                return;
            }
            case 8: {
                long delta = (long)amount * 604800000L;
                int preWeeks = (this.internalGet(5) - 1) / 7;
                int postWeeks = (this.getActualMaximum(5) - this.internalGet(5)) / 7;
                long min2 = this.time - (long)preWeeks * 604800000L;
                long gap2 = 604800000L * (long)(preWeeks + postWeeks + 1);
                this.time = (this.time + delta - min2) % gap2;
                if (this.time < 0L) {
                    this.time += gap2;
                }
                this.setTimeInMillis(this.time + min2);
                return;
            }
            case 20: {
                this.set(field, this.internalGet(field) + amount);
                return;
            }
        }
        throw new IllegalArgumentException("Calendar.roll(" + this.fieldName(field) + ") not supported");
    }

    public void add(int field, int amount) {
        int newWallTime;
        if (amount == 0) {
            return;
        }
        long delta = amount;
        boolean keepWallTimeInvariant = true;
        switch (field) {
            case 0: {
                this.set(field, this.get(field) + amount);
                this.pinField(0);
                return;
            }
            case 1: 
            case 17: {
                String calType;
                int era = this.get(0);
                if (era == 0 && ((calType = this.getType()).equals("gregorian") || calType.equals("roc") || calType.equals("coptic"))) {
                    amount = -amount;
                }
            }
            case 2: 
            case 19: {
                boolean oldLenient = this.isLenient();
                this.setLenient(true);
                this.set(field, this.get(field) + amount);
                this.pinField(5);
                if (!oldLenient) {
                    this.complete();
                    this.setLenient(oldLenient);
                }
                return;
            }
            case 3: 
            case 4: 
            case 8: {
                delta *= 604800000L;
                break;
            }
            case 9: {
                delta *= 43200000L;
                break;
            }
            case 5: 
            case 6: 
            case 7: 
            case 18: 
            case 20: {
                delta *= 86400000L;
                break;
            }
            case 10: 
            case 11: {
                delta *= 3600000L;
                keepWallTimeInvariant = false;
                break;
            }
            case 12: {
                delta *= 60000L;
                keepWallTimeInvariant = false;
                break;
            }
            case 13: {
                delta *= 1000L;
                keepWallTimeInvariant = false;
                break;
            }
            case 14: 
            case 21: {
                keepWallTimeInvariant = false;
                break;
            }
            default: {
                throw new IllegalArgumentException("Calendar.add(" + this.fieldName(field) + ") not supported");
            }
        }
        int prevOffset = 0;
        int prevWallTime = 0;
        if (keepWallTimeInvariant) {
            prevOffset = this.get(16) + this.get(15);
            prevWallTime = this.get(21);
        }
        this.setTimeInMillis(this.getTimeInMillis() + delta);
        if (keepWallTimeInvariant && (newWallTime = this.get(21)) != prevWallTime) {
            long t2 = this.internalGetTimeInMillis();
            int newOffset = this.get(16) + this.get(15);
            if (newOffset != prevOffset) {
                long adjAmount = (long)(prevOffset - newOffset) % 86400000L;
                if (adjAmount != 0L) {
                    this.setTimeInMillis(t2 + adjAmount);
                    newWallTime = this.get(21);
                }
                if (newWallTime != prevWallTime) {
                    switch (this.skippedWallTime) {
                        case 1: {
                            if (adjAmount <= 0L) break;
                            this.setTimeInMillis(t2);
                            break;
                        }
                        case 0: {
                            if (adjAmount >= 0L) break;
                            this.setTimeInMillis(t2);
                            break;
                        }
                        case 2: {
                            long tmpT = adjAmount > 0L ? this.internalGetTimeInMillis() : t2;
                            Long immediatePrevTrans = this.getImmediatePreviousZoneTransition(tmpT);
                            if (immediatePrevTrans != null) {
                                this.setTimeInMillis(immediatePrevTrans);
                                break;
                            }
                            throw new RuntimeException("Could not locate a time zone transition before " + tmpT);
                        }
                    }
                }
            }
        }
    }

    public String getDisplayName(Locale loc) {
        return this.getClass().getName();
    }

    public String getDisplayName(ULocale loc) {
        return this.getClass().getName();
    }

    @Override
    public int compareTo(Calendar that) {
        long v2 = this.getTimeInMillis() - that.getTimeInMillis();
        return v2 < 0L ? -1 : (v2 > 0L ? 1 : 0);
    }

    public DateFormat getDateTimeFormat(int dateStyle, int timeStyle, Locale loc) {
        return Calendar.formatHelper(this, ULocale.forLocale(loc), dateStyle, timeStyle);
    }

    public DateFormat getDateTimeFormat(int dateStyle, int timeStyle, ULocale loc) {
        return Calendar.formatHelper(this, loc, dateStyle, timeStyle);
    }

    protected DateFormat handleGetDateFormat(String pattern, Locale locale) {
        return this.handleGetDateFormat(pattern, null, ULocale.forLocale(locale));
    }

    protected DateFormat handleGetDateFormat(String pattern, String override, Locale locale) {
        return this.handleGetDateFormat(pattern, override, ULocale.forLocale(locale));
    }

    protected DateFormat handleGetDateFormat(String pattern, ULocale locale) {
        return this.handleGetDateFormat(pattern, null, locale);
    }

    protected DateFormat handleGetDateFormat(String pattern, String override, ULocale locale) {
        FormatConfiguration fmtConfig = new FormatConfiguration();
        fmtConfig.pattern = pattern;
        fmtConfig.override = override;
        fmtConfig.formatData = new DateFormatSymbols(this, locale);
        fmtConfig.loc = locale;
        fmtConfig.cal = this;
        return SimpleDateFormat.getInstance(fmtConfig);
    }

    private static DateFormat formatHelper(Calendar cal, ULocale loc, int dateStyle, int timeStyle) {
        if (timeStyle < -1 || timeStyle > 3) {
            throw new IllegalArgumentException("Illegal time style " + timeStyle);
        }
        if (dateStyle < -1 || dateStyle > 3) {
            throw new IllegalArgumentException("Illegal date style " + dateStyle);
        }
        PatternData patternData = PatternData.make(cal, loc);
        String override = null;
        String pattern = null;
        if (timeStyle >= 0 && dateStyle >= 0) {
            pattern = SimpleFormatterImpl.formatRawPattern(patternData.getDateTimePattern(dateStyle), 2, 2, patternData.patterns[timeStyle], patternData.patterns[dateStyle + 4]);
            if (patternData.overrides != null) {
                String dateOverride = patternData.overrides[dateStyle + 4];
                String timeOverride = patternData.overrides[timeStyle];
                override = Calendar.mergeOverrideStrings(patternData.patterns[dateStyle + 4], patternData.patterns[timeStyle], dateOverride, timeOverride);
            }
        } else if (timeStyle >= 0) {
            pattern = patternData.patterns[timeStyle];
            if (patternData.overrides != null) {
                override = patternData.overrides[timeStyle];
            }
        } else if (dateStyle >= 0) {
            pattern = patternData.patterns[dateStyle + 4];
            if (patternData.overrides != null) {
                override = patternData.overrides[dateStyle + 4];
            }
        } else {
            throw new IllegalArgumentException("No date or time style specified");
        }
        DateFormat result = cal.handleGetDateFormat(pattern, override, loc);
        result.setCalendar(cal);
        return result;
    }

    private static PatternData getPatternData(ULocale locale, String calType) {
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
        ICUResourceBundle dtPatternsRb = rb.findWithFallback("calendar/" + calType + "/DateTimePatterns");
        if (dtPatternsRb == null) {
            dtPatternsRb = rb.getWithFallback("calendar/gregorian/DateTimePatterns");
        }
        int patternsSize = dtPatternsRb.getSize();
        String[] dateTimePatterns = new String[patternsSize];
        String[] dateTimePatternsOverrides = new String[patternsSize];
        block4: for (int i2 = 0; i2 < patternsSize; ++i2) {
            ICUResourceBundle concatenationPatternRb = (ICUResourceBundle)dtPatternsRb.get(i2);
            switch (concatenationPatternRb.getType()) {
                case 0: {
                    dateTimePatterns[i2] = concatenationPatternRb.getString();
                    continue block4;
                }
                case 8: {
                    dateTimePatterns[i2] = concatenationPatternRb.getString(0);
                    dateTimePatternsOverrides[i2] = concatenationPatternRb.getString(1);
                }
            }
        }
        return new PatternData(dateTimePatterns, dateTimePatternsOverrides);
    }

    @Deprecated
    public static String getDateTimePattern(Calendar cal, ULocale uLocale, int dateStyle) {
        PatternData patternData = PatternData.make(cal, uLocale);
        return patternData.getDateTimePattern(dateStyle);
    }

    private static String mergeOverrideStrings(String datePattern, String timePattern, String dateOverride, String timeOverride) {
        if (dateOverride == null && timeOverride == null) {
            return null;
        }
        if (dateOverride == null) {
            return Calendar.expandOverride(timePattern, timeOverride);
        }
        if (timeOverride == null) {
            return Calendar.expandOverride(datePattern, dateOverride);
        }
        if (dateOverride.equals(timeOverride)) {
            return dateOverride;
        }
        return Calendar.expandOverride(datePattern, dateOverride) + ";" + Calendar.expandOverride(timePattern, timeOverride);
    }

    private static String expandOverride(String pattern, String override) {
        if (override.indexOf(61) >= 0) {
            return override;
        }
        boolean inQuotes = false;
        char prevChar = ' ';
        StringBuilder result = new StringBuilder();
        StringCharacterIterator it = new StringCharacterIterator(pattern);
        char c2 = it.first();
        while (c2 != '\uffff') {
            if (c2 == '\'') {
                inQuotes = !inQuotes;
                prevChar = c2;
            } else {
                if (!inQuotes && c2 != prevChar) {
                    if (result.length() > 0) {
                        result.append(";");
                    }
                    result.append(c2);
                    result.append("=");
                    result.append(override);
                }
                prevChar = c2;
            }
            c2 = it.next();
        }
        return result.toString();
    }

    protected void pinField(int field) {
        int max = this.getActualMaximum(field);
        int min = this.getActualMinimum(field);
        if (this.fields[field] > max) {
            this.set(field, max);
        } else if (this.fields[field] < min) {
            this.set(field, min);
        }
    }

    protected int weekNumber(int desiredDay, int dayOfPeriod, int dayOfWeek) {
        int periodStartDayOfWeek = (dayOfWeek - this.getFirstDayOfWeek() - dayOfPeriod + 1) % 7;
        if (periodStartDayOfWeek < 0) {
            periodStartDayOfWeek += 7;
        }
        int weekNo = (desiredDay + periodStartDayOfWeek - 1) / 7;
        if (7 - periodStartDayOfWeek >= this.getMinimalDaysInFirstWeek()) {
            ++weekNo;
        }
        return weekNo;
    }

    protected final int weekNumber(int dayOfPeriod, int dayOfWeek) {
        return this.weekNumber(dayOfPeriod, dayOfPeriod, dayOfWeek);
    }

    public int fieldDifference(Date when, int field) {
        long targetMs;
        int min = 0;
        long startMs = this.getTimeInMillis();
        if (startMs < (targetMs = when.getTime())) {
            int max;
            block13: {
                max = 1;
                while (true) {
                    this.setTimeInMillis(startMs);
                    this.add(field, max);
                    long ms = this.getTimeInMillis();
                    if (ms == targetMs) {
                        return max;
                    }
                    if (ms > targetMs) break block13;
                    if (max >= Integer.MAX_VALUE) break;
                    min = max;
                    if ((max <<= 1) >= 0) continue;
                    max = Integer.MAX_VALUE;
                }
                throw new RuntimeException();
            }
            while (max - min > 1) {
                int t2 = min + (max - min) / 2;
                this.setTimeInMillis(startMs);
                this.add(field, t2);
                long ms = this.getTimeInMillis();
                if (ms == targetMs) {
                    return t2;
                }
                if (ms > targetMs) {
                    max = t2;
                    continue;
                }
                min = t2;
            }
        } else if (startMs > targetMs) {
            int max;
            block14: {
                max = -1;
                do {
                    this.setTimeInMillis(startMs);
                    this.add(field, max);
                    long ms = this.getTimeInMillis();
                    if (ms == targetMs) {
                        return max;
                    }
                    if (ms < targetMs) break block14;
                    min = max;
                } while ((max <<= 1) != 0);
                throw new RuntimeException();
            }
            while (min - max > 1) {
                int t3 = min + (max - min) / 2;
                this.setTimeInMillis(startMs);
                this.add(field, t3);
                long ms = this.getTimeInMillis();
                if (ms == targetMs) {
                    return t3;
                }
                if (ms < targetMs) {
                    max = t3;
                    continue;
                }
                min = t3;
            }
        }
        this.setTimeInMillis(startMs);
        this.add(field, min);
        return min;
    }

    public void setTimeZone(TimeZone value) {
        this.zone = value;
        this.areFieldsSet = false;
    }

    public TimeZone getTimeZone() {
        return this.zone;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void setRepeatedWallTimeOption(int option) {
        if (option != 0 && option != 1) {
            throw new IllegalArgumentException("Illegal repeated wall time option - " + option);
        }
        this.repeatedWallTime = option;
    }

    public int getRepeatedWallTimeOption() {
        return this.repeatedWallTime;
    }

    public void setSkippedWallTimeOption(int option) {
        if (option != 0 && option != 1 && option != 2) {
            throw new IllegalArgumentException("Illegal skipped wall time option - " + option);
        }
        this.skippedWallTime = option;
    }

    public int getSkippedWallTimeOption() {
        return this.skippedWallTime;
    }

    public void setFirstDayOfWeek(int value) {
        if (this.firstDayOfWeek != value) {
            if (value < 1 || value > 7) {
                throw new IllegalArgumentException("Invalid day of week");
            }
            this.firstDayOfWeek = value;
            this.areFieldsSet = false;
        }
    }

    public int getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public void setMinimalDaysInFirstWeek(int value) {
        if (value < 1) {
            value = 1;
        } else if (value > 7) {
            value = 7;
        }
        if (this.minimalDaysInFirstWeek != value) {
            this.minimalDaysInFirstWeek = value;
            this.areFieldsSet = false;
        }
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDaysInFirstWeek;
    }

    protected abstract int handleGetLimit(int var1, int var2);

    protected int getLimit(int field, int limitType) {
        switch (field) {
            case 7: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 18: 
            case 20: 
            case 21: 
            case 22: {
                return LIMITS[field][limitType];
            }
            case 4: {
                int limit;
                if (limitType == 0) {
                    limit = this.getMinimalDaysInFirstWeek() == 1 ? 1 : 0;
                } else if (limitType == 1) {
                    limit = 1;
                } else {
                    int minDaysInFirst = this.getMinimalDaysInFirstWeek();
                    int daysInMonth = this.handleGetLimit(5, limitType);
                    limit = limitType == 2 ? (daysInMonth + (7 - minDaysInFirst)) / 7 : (daysInMonth + 6 + (7 - minDaysInFirst)) / 7;
                }
                return limit;
            }
        }
        return this.handleGetLimit(field, limitType);
    }

    public final int getMinimum(int field) {
        return this.getLimit(field, 0);
    }

    public final int getMaximum(int field) {
        return this.getLimit(field, 3);
    }

    public final int getGreatestMinimum(int field) {
        return this.getLimit(field, 1);
    }

    public final int getLeastMaximum(int field) {
        return this.getLimit(field, 2);
    }

    @Deprecated
    public int getDayOfWeekType(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Invalid day of week");
        }
        if (this.weekendOnset == this.weekendCease) {
            if (dayOfWeek != this.weekendOnset) {
                return 0;
            }
            return this.weekendOnsetMillis == 0 ? 1 : 2;
        }
        if (this.weekendOnset < this.weekendCease ? dayOfWeek < this.weekendOnset || dayOfWeek > this.weekendCease : dayOfWeek > this.weekendCease && dayOfWeek < this.weekendOnset) {
            return 0;
        }
        if (dayOfWeek == this.weekendOnset) {
            return this.weekendOnsetMillis == 0 ? 1 : 2;
        }
        if (dayOfWeek == this.weekendCease) {
            return this.weekendCeaseMillis >= 86400000 ? 1 : 3;
        }
        return 1;
    }

    @Deprecated
    public int getWeekendTransition(int dayOfWeek) {
        if (dayOfWeek == this.weekendOnset) {
            return this.weekendOnsetMillis;
        }
        if (dayOfWeek == this.weekendCease) {
            return this.weekendCeaseMillis;
        }
        throw new IllegalArgumentException("Not weekend transition day");
    }

    public boolean isWeekend(Date date) {
        this.setTime(date);
        return this.isWeekend();
    }

    public boolean isWeekend() {
        int dow = this.get(7);
        int dowt = this.getDayOfWeekType(dow);
        switch (dowt) {
            case 0: {
                return false;
            }
            case 1: {
                return true;
            }
        }
        int millisInDay = this.internalGet(14) + 1000 * (this.internalGet(13) + 60 * (this.internalGet(12) + 60 * this.internalGet(11)));
        int transition = this.getWeekendTransition(dow);
        return dowt == 2 ? millisInDay >= transition : millisInDay < transition;
    }

    public Object clone() {
        try {
            Calendar other = (Calendar)super.clone();
            other.fields = new int[this.fields.length];
            other.stamp = new int[this.fields.length];
            System.arraycopy(this.fields, 0, other.fields, 0, this.fields.length);
            System.arraycopy(this.stamp, 0, other.stamp, 0, this.fields.length);
            other.zone = (TimeZone)this.zone.clone();
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.getClass().getName());
        buffer.append("[time=");
        buffer.append(this.isTimeSet ? String.valueOf(this.time) : "?");
        buffer.append(",areFieldsSet=");
        buffer.append(this.areFieldsSet);
        buffer.append(",areAllFieldsSet=");
        buffer.append(this.areAllFieldsSet);
        buffer.append(",lenient=");
        buffer.append(this.lenient);
        buffer.append(",zone=");
        buffer.append(this.zone);
        buffer.append(",firstDayOfWeek=");
        buffer.append(this.firstDayOfWeek);
        buffer.append(",minimalDaysInFirstWeek=");
        buffer.append(this.minimalDaysInFirstWeek);
        buffer.append(",repeatedWallTime=");
        buffer.append(this.repeatedWallTime);
        buffer.append(",skippedWallTime=");
        buffer.append(this.skippedWallTime);
        for (int i2 = 0; i2 < this.fields.length; ++i2) {
            buffer.append(',').append(this.fieldName(i2)).append('=');
            buffer.append(this.isSet(i2) ? String.valueOf(this.fields[i2]) : "?");
        }
        buffer.append(']');
        return buffer.toString();
    }

    public static WeekData getWeekDataForRegion(String region) {
        return WEEK_DATA_CACHE.createInstance(region, region);
    }

    public WeekData getWeekData() {
        return new WeekData(this.firstDayOfWeek, this.minimalDaysInFirstWeek, this.weekendOnset, this.weekendOnsetMillis, this.weekendCease, this.weekendCeaseMillis);
    }

    public Calendar setWeekData(WeekData wdata) {
        this.setFirstDayOfWeek(wdata.firstDayOfWeek);
        this.setMinimalDaysInFirstWeek(wdata.minimalDaysInFirstWeek);
        this.weekendOnset = wdata.weekendOnset;
        this.weekendOnsetMillis = wdata.weekendOnsetMillis;
        this.weekendCease = wdata.weekendCease;
        this.weekendCeaseMillis = wdata.weekendCeaseMillis;
        return this;
    }

    private static WeekData getWeekDataForRegionInternal(String region) {
        if (region == null) {
            region = "001";
        }
        UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        UResourceBundle weekDataInfo = rb.get("weekData");
        UResourceBundle weekDataBundle = null;
        try {
            weekDataBundle = weekDataInfo.get(region);
        }
        catch (MissingResourceException mre) {
            if (!region.equals("001")) {
                weekDataBundle = weekDataInfo.get("001");
            }
            throw mre;
        }
        int[] wdi = weekDataBundle.getIntVector();
        return new WeekData(wdi[0], wdi[1], wdi[2], wdi[3], wdi[4], wdi[5]);
    }

    private void setWeekData(String region) {
        if (region == null) {
            region = "001";
        }
        WeekData wdata = (WeekData)WEEK_DATA_CACHE.getInstance(region, region);
        this.setWeekData(wdata);
    }

    private void updateTime() {
        this.computeTime();
        if (this.isLenient() || !this.areAllFieldsSet) {
            this.areFieldsSet = false;
        }
        this.isTimeSet = true;
        this.areFieldsVirtuallySet = false;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        if (!this.isTimeSet) {
            try {
                this.updateTime();
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.initInternal();
        this.isTimeSet = true;
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
        this.areFieldsVirtuallySet = true;
        this.nextStamp = 2;
    }

    protected void computeFields() {
        int millisInDay;
        int[] offsets = new int[2];
        this.getTimeZone().getOffset(this.time, false, offsets);
        long localMillis = this.time + (long)offsets[0] + (long)offsets[1];
        int mask = this.internalSetMask;
        for (int i2 = 0; i2 < this.fields.length; ++i2) {
            this.stamp[i2] = (mask & 1) == 0 ? 1 : 0;
            mask >>= 1;
        }
        long days = Calendar.floorDivide(localMillis, 86400000L);
        this.fields[20] = (int)days + 2440588;
        this.computeGregorianAndDOWFields(this.fields[20]);
        this.handleComputeFields(this.fields[20]);
        this.computeWeekFields();
        this.fields[21] = millisInDay = (int)(localMillis - days * 86400000L);
        this.fields[14] = millisInDay % 1000;
        this.fields[13] = (millisInDay /= 1000) % 60;
        this.fields[12] = (millisInDay /= 60) % 60;
        this.fields[11] = millisInDay /= 60;
        this.fields[9] = millisInDay / 12;
        this.fields[10] = millisInDay % 12;
        this.fields[15] = offsets[0];
        this.fields[16] = offsets[1];
    }

    private final void computeGregorianAndDOWFields(int julianDay) {
        this.computeGregorianFields(julianDay);
        int dow = this.fields[7] = Calendar.julianDayToDayOfWeek(julianDay);
        int dowLocal = dow - this.getFirstDayOfWeek() + 1;
        if (dowLocal < 1) {
            dowLocal += 7;
        }
        this.fields[18] = dowLocal;
    }

    protected final void computeGregorianFields(int julianDay) {
        int march1;
        long gregorianEpochDay = julianDay - 1721426;
        int[] rem = new int[1];
        int n400 = Calendar.floorDivide(gregorianEpochDay, 146097, rem);
        int n100 = Calendar.floorDivide(rem[0], 36524, rem);
        int n4 = Calendar.floorDivide(rem[0], 1461, rem);
        int n1 = Calendar.floorDivide(rem[0], 365, rem);
        int year = 400 * n400 + 100 * n100 + 4 * n4 + n1;
        int dayOfYear = rem[0];
        if (n100 == 4 || n1 == 4) {
            dayOfYear = 365;
        } else {
            ++year;
        }
        boolean isLeap = (year & 3) == 0 && (year % 100 != 0 || year % 400 == 0);
        int correction = 0;
        int n2 = march1 = isLeap ? 60 : 59;
        if (dayOfYear >= march1) {
            correction = isLeap ? 1 : 2;
        }
        int month = (12 * (dayOfYear + correction) + 6) / 367;
        int dayOfMonth = dayOfYear - GREGORIAN_MONTH_COUNT[month][isLeap ? 3 : 2] + 1;
        this.gregorianYear = year;
        this.gregorianMonth = month;
        this.gregorianDayOfMonth = dayOfMonth;
        this.gregorianDayOfYear = dayOfYear + 1;
    }

    private final void computeWeekFields() {
        int eyear = this.fields[19];
        int dayOfWeek = this.fields[7];
        int dayOfYear = this.fields[6];
        int yearOfWeekOfYear = eyear;
        int relDow = (dayOfWeek + 7 - this.getFirstDayOfWeek()) % 7;
        int relDowJan1 = (dayOfWeek - dayOfYear + 7001 - this.getFirstDayOfWeek()) % 7;
        int woy = (dayOfYear - 1 + relDowJan1) / 7;
        if (7 - relDowJan1 >= this.getMinimalDaysInFirstWeek()) {
            ++woy;
        }
        if (woy == 0) {
            int prevDoy = dayOfYear + this.handleGetYearLength(eyear - 1);
            woy = this.weekNumber(prevDoy, dayOfWeek);
        } else {
            int lastDoy = this.handleGetYearLength(eyear);
            if (dayOfYear >= lastDoy - 5) {
                int lastRelDow = (relDow + lastDoy - dayOfYear) % 7;
                if (lastRelDow < 0) {
                    lastRelDow += 7;
                }
                if (6 - lastRelDow >= this.getMinimalDaysInFirstWeek() && dayOfYear + 7 - relDow > lastDoy) {
                    woy = 1;
                    ++yearOfWeekOfYear;
                }
            }
        }
        this.fields[3] = woy;
        this.fields[17] = --yearOfWeekOfYear;
        int dayOfMonth = this.fields[5];
        this.fields[4] = this.weekNumber(dayOfMonth, dayOfWeek);
        this.fields[8] = (dayOfMonth - 1) / 7 + 1;
    }

    protected int resolveFields(int[][][] precedenceTable) {
        int bestField = -1;
        for (int g2 = 0; g2 < precedenceTable.length && bestField < 0; ++g2) {
            int[][] group = precedenceTable[g2];
            int bestStamp = 0;
            block1: for (int l2 = 0; l2 < group.length; ++l2) {
                int i2;
                int[] line = group[l2];
                int lineStamp = 0;
                int n2 = i2 = line[0] >= 32 ? 1 : 0;
                while (i2 < line.length) {
                    int s2 = this.stamp[line[i2]];
                    if (s2 == 0) continue block1;
                    lineStamp = Math.max(lineStamp, s2);
                    ++i2;
                }
                if (lineStamp <= bestStamp) continue;
                int tempBestField = line[0];
                if (tempBestField >= 32) {
                    if ((tempBestField &= 0x1F) != 5 || this.stamp[4] < this.stamp[tempBestField]) {
                        bestField = tempBestField;
                    }
                } else {
                    bestField = tempBestField;
                }
                if (bestField != tempBestField) continue;
                bestStamp = lineStamp;
            }
        }
        return bestField >= 32 ? bestField & 0x1F : bestField;
    }

    protected int newestStamp(int first, int last, int bestStampSoFar) {
        int bestStamp = bestStampSoFar;
        for (int i2 = first; i2 <= last; ++i2) {
            if (this.stamp[i2] <= bestStamp) continue;
            bestStamp = this.stamp[i2];
        }
        return bestStamp;
    }

    protected final int getStamp(int field) {
        return this.stamp[field];
    }

    protected int newerField(int defaultField, int alternateField) {
        if (this.stamp[alternateField] > this.stamp[defaultField]) {
            return alternateField;
        }
        return defaultField;
    }

    protected void validateFields() {
        for (int field = 0; field < this.fields.length; ++field) {
            if (this.stamp[field] < 2) continue;
            this.validateField(field);
        }
    }

    protected void validateField(int field) {
        switch (field) {
            case 5: {
                int y2 = this.handleGetExtendedYear();
                this.validateField(field, 1, this.handleGetMonthLength(y2, this.internalGet(2)));
                break;
            }
            case 6: {
                int y3 = this.handleGetExtendedYear();
                this.validateField(field, 1, this.handleGetYearLength(y3));
                break;
            }
            case 8: {
                if (this.internalGet(field) == 0) {
                    throw new IllegalArgumentException("DAY_OF_WEEK_IN_MONTH cannot be zero");
                }
                this.validateField(field, this.getMinimum(field), this.getMaximum(field));
                break;
            }
            default: {
                this.validateField(field, this.getMinimum(field), this.getMaximum(field));
            }
        }
    }

    protected final void validateField(int field, int min, int max) {
        int value = this.fields[field];
        if (value < min || value > max) {
            throw new IllegalArgumentException(this.fieldName(field) + '=' + value + ", valid range=" + min + ".." + max);
        }
    }

    protected void computeTime() {
        long millisInDay;
        if (!this.isLenient()) {
            this.validateFields();
        }
        int julianDay = this.computeJulianDay();
        long millis = Calendar.julianDayToMillis(julianDay);
        if (this.stamp[21] >= 2 && this.newestStamp(9, 14, 0) <= this.stamp[21]) {
            millisInDay = this.internalGet(21);
        } else {
            int hour = Math.abs(this.internalGet(11));
            millisInDay = (hour = Math.max(hour, Math.abs(this.internalGet(10)))) > 548 ? this.computeMillisInDayLong() : (long)this.computeMillisInDay();
        }
        if (this.stamp[15] >= 2 || this.stamp[16] >= 2) {
            this.time = millis + millisInDay - (long)(this.internalGet(15) + this.internalGet(16));
        } else if (!this.lenient || this.skippedWallTime == 2) {
            long tmpTime;
            int zoneOffset1;
            int zoneOffset = this.computeZoneOffset(millis, millisInDay);
            if (zoneOffset != (zoneOffset1 = this.zone.getOffset(tmpTime = millis + millisInDay - (long)zoneOffset))) {
                if (!this.lenient) {
                    throw new IllegalArgumentException("The specified wall time does not exist due to time zone offset transition.");
                }
                assert (this.skippedWallTime == 2) : this.skippedWallTime;
                Long immediatePrevTransition = this.getImmediatePreviousZoneTransition(tmpTime);
                if (immediatePrevTransition == null) {
                    throw new RuntimeException("Could not locate a time zone transition before " + tmpTime);
                }
                this.time = immediatePrevTransition;
            } else {
                this.time = tmpTime;
            }
        } else {
            this.time = millis + millisInDay - (long)this.computeZoneOffset(millis, millisInDay);
        }
    }

    private Long getImmediatePreviousZoneTransition(long base) {
        Long transitionTime = null;
        if (this.zone instanceof BasicTimeZone) {
            TimeZoneTransition transition = ((BasicTimeZone)this.zone).getPreviousTransition(base, true);
            if (transition != null) {
                transitionTime = transition.getTime();
            }
        } else {
            transitionTime = Calendar.getPreviousZoneTransitionTime(this.zone, base, 0x6DDD00L);
            if (transitionTime == null) {
                transitionTime = Calendar.getPreviousZoneTransitionTime(this.zone, base, 108000000L);
            }
        }
        return transitionTime;
    }

    private static Long getPreviousZoneTransitionTime(TimeZone tz, long base, long duration) {
        int offsetL;
        assert (duration > 0L);
        long upper = base;
        long lower = base - duration - 1L;
        int offsetU = tz.getOffset(upper);
        if (offsetU == (offsetL = tz.getOffset(lower))) {
            return null;
        }
        return Calendar.findPreviousZoneTransitionTime(tz, offsetU, upper, lower);
    }

    private static Long findPreviousZoneTransitionTime(TimeZone tz, int upperOffset, long upper, long lower) {
        boolean onUnitTime = false;
        long mid = 0L;
        for (int unit : FIND_ZONE_TRANSITION_TIME_UNITS) {
            long uunits = upper / (long)unit;
            long lunits = lower / (long)unit;
            if (uunits <= lunits) continue;
            mid = (lunits + uunits + 1L >>> 1) * (long)unit;
            onUnitTime = true;
            break;
        }
        if (!onUnitTime) {
            mid = upper + lower >>> 1;
        }
        if (onUnitTime) {
            if (mid != upper) {
                int midOffset = tz.getOffset(mid);
                if (midOffset != upperOffset) {
                    return Calendar.findPreviousZoneTransitionTime(tz, upperOffset, upper, mid);
                }
                upper = mid;
            }
            --mid;
        } else {
            mid = upper + lower >>> 1;
        }
        if (mid == lower) {
            return upper;
        }
        int midOffset = tz.getOffset(mid);
        if (midOffset != upperOffset) {
            if (onUnitTime) {
                return upper;
            }
            return Calendar.findPreviousZoneTransitionTime(tz, upperOffset, upper, mid);
        }
        return Calendar.findPreviousZoneTransitionTime(tz, upperOffset, mid, lower);
    }

    @Deprecated
    protected int computeMillisInDay() {
        int bestStamp;
        int millisInDay = 0;
        int hourOfDayStamp = this.stamp[11];
        int hourStamp = Math.max(this.stamp[10], this.stamp[9]);
        int n2 = bestStamp = hourStamp > hourOfDayStamp ? hourStamp : hourOfDayStamp;
        if (bestStamp != 0) {
            if (bestStamp == hourOfDayStamp) {
                millisInDay += this.internalGet(11);
            } else {
                millisInDay += this.internalGet(10);
                millisInDay += 12 * this.internalGet(9);
            }
        }
        millisInDay *= 60;
        millisInDay += this.internalGet(12);
        millisInDay *= 60;
        millisInDay += this.internalGet(13);
        millisInDay *= 1000;
        return millisInDay += this.internalGet(14);
    }

    @Deprecated
    protected long computeMillisInDayLong() {
        int bestStamp;
        long millisInDay = 0L;
        int hourOfDayStamp = this.stamp[11];
        int hourStamp = Math.max(this.stamp[10], this.stamp[9]);
        int n2 = bestStamp = hourStamp > hourOfDayStamp ? hourStamp : hourOfDayStamp;
        if (bestStamp != 0) {
            if (bestStamp == hourOfDayStamp) {
                millisInDay += (long)this.internalGet(11);
            } else {
                millisInDay += (long)this.internalGet(10);
                millisInDay += (long)(12 * this.internalGet(9));
            }
        }
        millisInDay *= 60L;
        millisInDay += (long)this.internalGet(12);
        millisInDay *= 60L;
        millisInDay += (long)this.internalGet(13);
        millisInDay *= 1000L;
        return millisInDay += (long)this.internalGet(14);
    }

    @Deprecated
    protected int computeZoneOffset(long millis, int millisInDay) {
        int[] offsets = new int[2];
        long wall = millis + (long)millisInDay;
        if (this.zone instanceof BasicTimeZone) {
            int duplicatedTimeOpt = this.repeatedWallTime == 1 ? 4 : 12;
            int nonExistingTimeOpt = this.skippedWallTime == 1 ? 12 : 4;
            ((BasicTimeZone)this.zone).getOffsetFromLocal(wall, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        } else {
            long tgmt;
            this.zone.getOffset(wall, true, offsets);
            boolean sawRecentNegativeShift = false;
            if (this.repeatedWallTime == 1) {
                tgmt = wall - (long)(offsets[0] + offsets[1]);
                int offsetBefore6 = this.zone.getOffset(tgmt - 21600000L);
                int offsetDelta = offsets[0] + offsets[1] - offsetBefore6;
                assert (offsetDelta > -21600000) : offsetDelta;
                if (offsetDelta < 0) {
                    sawRecentNegativeShift = true;
                    this.zone.getOffset(wall + (long)offsetDelta, true, offsets);
                }
            }
            if (!sawRecentNegativeShift && this.skippedWallTime == 1) {
                tgmt = wall - (long)(offsets[0] + offsets[1]);
                this.zone.getOffset(tgmt, false, offsets);
            }
        }
        return offsets[0] + offsets[1];
    }

    @Deprecated
    protected int computeZoneOffset(long millis, long millisInDay) {
        int[] offsets = new int[2];
        long wall = millis + millisInDay;
        if (this.zone instanceof BasicTimeZone) {
            int duplicatedTimeOpt = this.repeatedWallTime == 1 ? 4 : 12;
            int nonExistingTimeOpt = this.skippedWallTime == 1 ? 12 : 4;
            ((BasicTimeZone)this.zone).getOffsetFromLocal(wall, nonExistingTimeOpt, duplicatedTimeOpt, offsets);
        } else {
            long tgmt;
            this.zone.getOffset(wall, true, offsets);
            boolean sawRecentNegativeShift = false;
            if (this.repeatedWallTime == 1) {
                tgmt = wall - (long)(offsets[0] + offsets[1]);
                int offsetBefore6 = this.zone.getOffset(tgmt - 21600000L);
                int offsetDelta = offsets[0] + offsets[1] - offsetBefore6;
                assert (offsetDelta > -21600000) : offsetDelta;
                if (offsetDelta < 0) {
                    sawRecentNegativeShift = true;
                    this.zone.getOffset(wall + (long)offsetDelta, true, offsets);
                }
            }
            if (!sawRecentNegativeShift && this.skippedWallTime == 1) {
                tgmt = wall - (long)(offsets[0] + offsets[1]);
                this.zone.getOffset(tgmt, false, offsets);
            }
        }
        return offsets[0] + offsets[1];
    }

    protected int computeJulianDay() {
        int bestField;
        if (this.stamp[20] >= 2) {
            int bestStamp = this.newestStamp(0, 8, 0);
            if ((bestStamp = this.newestStamp(17, 19, bestStamp)) <= this.stamp[20]) {
                return this.internalGet(20);
            }
        }
        if ((bestField = this.resolveFields(this.getFieldResolutionTable())) < 0) {
            bestField = 5;
        }
        return this.handleComputeJulianDay(bestField);
    }

    protected int[][][] getFieldResolutionTable() {
        return DATE_PRECEDENCE;
    }

    protected abstract int handleComputeMonthStart(int var1, int var2, boolean var3);

    protected abstract int handleGetExtendedYear();

    protected int handleGetMonthLength(int extendedYear, int month) {
        return this.handleComputeMonthStart(extendedYear, month + 1, true) - this.handleComputeMonthStart(extendedYear, month, true);
    }

    protected int handleGetYearLength(int eyear) {
        return this.handleComputeMonthStart(eyear + 1, 0, false) - this.handleComputeMonthStart(eyear, 0, false);
    }

    protected int[] handleCreateFields() {
        return new int[23];
    }

    protected int getDefaultMonthInYear(int extendedYear) {
        return 0;
    }

    protected int getDefaultDayInMonth(int extendedYear, int month) {
        return 1;
    }

    protected int handleComputeJulianDay(int bestField) {
        boolean useMonth = bestField == 5 || bestField == 4 || bestField == 8;
        int year = bestField == 3 && this.newerField(17, 1) == 17 ? this.internalGet(17) : this.handleGetExtendedYear();
        this.internalSet(19, year);
        int month = useMonth ? this.internalGet(2, this.getDefaultMonthInYear(year)) : 0;
        int julianDay = this.handleComputeMonthStart(year, month, useMonth);
        if (bestField == 5) {
            if (this.isSet(5)) {
                return julianDay + this.internalGet(5, this.getDefaultDayInMonth(year, month));
            }
            return julianDay + this.getDefaultDayInMonth(year, month);
        }
        if (bestField == 6) {
            return julianDay + this.internalGet(6);
        }
        int firstDOW = this.getFirstDayOfWeek();
        int first = Calendar.julianDayToDayOfWeek(julianDay + 1) - firstDOW;
        if (first < 0) {
            first += 7;
        }
        int dowLocal = 0;
        switch (this.resolveFields(DOW_PRECEDENCE)) {
            case 7: {
                dowLocal = this.internalGet(7) - firstDOW;
                break;
            }
            case 18: {
                dowLocal = this.internalGet(18) - 1;
            }
        }
        if ((dowLocal %= 7) < 0) {
            dowLocal += 7;
        }
        int date = 1 - first + dowLocal;
        if (bestField == 8) {
            int dim;
            if (date < 1) {
                date += 7;
            }
            if ((dim = this.internalGet(8, 1)) >= 0) {
                date += 7 * (dim - 1);
            } else {
                int m2 = this.internalGet(2, 0);
                int monthLength = this.handleGetMonthLength(year, m2);
                date += ((monthLength - date) / 7 + dim + 1) * 7;
            }
        } else {
            if (7 - first < this.getMinimalDaysInFirstWeek()) {
                date += 7;
            }
            date += 7 * (this.internalGet(bestField) - 1);
        }
        return julianDay + date;
    }

    protected int computeGregorianMonthStart(int year, int month) {
        if (month < 0 || month > 11) {
            int[] rem = new int[1];
            year += Calendar.floorDivide(month, 12, rem);
            month = rem[0];
        }
        boolean isLeap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
        int y2 = year - 1;
        int julianDay = 365 * y2 + Calendar.floorDivide(y2, 4) - Calendar.floorDivide(y2, 100) + Calendar.floorDivide(y2, 400) + 1721426 - 1;
        if (month != 0) {
            julianDay += GREGORIAN_MONTH_COUNT[month][isLeap ? 3 : 2];
        }
        return julianDay;
    }

    protected void handleComputeFields(int julianDay) {
        this.internalSet(2, this.getGregorianMonth());
        this.internalSet(5, this.getGregorianDayOfMonth());
        this.internalSet(6, this.getGregorianDayOfYear());
        int eyear = this.getGregorianYear();
        this.internalSet(19, eyear);
        int era = 1;
        if (eyear < 1) {
            era = 0;
            eyear = 1 - eyear;
        }
        this.internalSet(0, era);
        this.internalSet(1, eyear);
    }

    protected final int getGregorianYear() {
        return this.gregorianYear;
    }

    protected final int getGregorianMonth() {
        return this.gregorianMonth;
    }

    protected final int getGregorianDayOfYear() {
        return this.gregorianDayOfYear;
    }

    protected final int getGregorianDayOfMonth() {
        return this.gregorianDayOfMonth;
    }

    public final int getFieldCount() {
        return this.fields.length;
    }

    protected final void internalSet(int field, int value) {
        if ((1 << field & this.internalSetMask) == 0) {
            throw new IllegalStateException("Subclass cannot set " + this.fieldName(field));
        }
        this.fields[field] = value;
        this.stamp[field] = 1;
    }

    protected static final boolean isGregorianLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    protected static final int gregorianMonthLength(int y2, int m2) {
        return GREGORIAN_MONTH_COUNT[m2][Calendar.isGregorianLeapYear(y2) ? 1 : 0];
    }

    protected static final int gregorianPreviousMonthLength(int y2, int m2) {
        return m2 > 0 ? Calendar.gregorianMonthLength(y2, m2 - 1) : 31;
    }

    protected static final long floorDivide(long numerator, long denominator) {
        return numerator >= 0L ? numerator / denominator : (numerator + 1L) / denominator - 1L;
    }

    protected static final int floorDivide(int numerator, int denominator) {
        return numerator >= 0 ? numerator / denominator : (numerator + 1) / denominator - 1;
    }

    protected static final int floorDivide(int numerator, int denominator, int[] remainder) {
        if (numerator >= 0) {
            remainder[0] = numerator % denominator;
            return numerator / denominator;
        }
        int quotient = (numerator + 1) / denominator - 1;
        remainder[0] = numerator - quotient * denominator;
        return quotient;
    }

    protected static final int floorDivide(long numerator, int denominator, int[] remainder) {
        if (numerator >= 0L) {
            remainder[0] = (int)(numerator % (long)denominator);
            return (int)(numerator / (long)denominator);
        }
        int quotient = (int)((numerator + 1L) / (long)denominator - 1L);
        remainder[0] = (int)(numerator - (long)quotient * (long)denominator);
        return quotient;
    }

    protected String fieldName(int field) {
        try {
            return FIELD_NAME[field];
        }
        catch (ArrayIndexOutOfBoundsException e2) {
            return "Field " + field;
        }
    }

    protected static final int millisToJulianDay(long millis) {
        return (int)(2440588L + Calendar.floorDivide(millis, 86400000L));
    }

    protected static final long julianDayToMillis(int julian) {
        return (long)(julian - 2440588) * 86400000L;
    }

    protected static final int julianDayToDayOfWeek(int julian) {
        int dayOfWeek = (julian + 2) % 7;
        if (dayOfWeek < 1) {
            dayOfWeek += 7;
        }
        return dayOfWeek;
    }

    protected final long internalGetTimeInMillis() {
        return this.time;
    }

    public String getType() {
        return "unknown";
    }

    @Deprecated
    public boolean haveDefaultCentury() {
        return true;
    }

    public final ULocale getLocale(ULocale.Type type) {
        return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
    }

    final void setLocale(ULocale valid, ULocale actual) {
        if (valid == null != (actual == null)) {
            throw new IllegalArgumentException();
        }
        this.validLocale = valid;
        this.actualLocale = actual;
    }

    private static class WeekDataCache
    extends SoftCache<String, WeekData, String> {
        private WeekDataCache() {
        }

        @Override
        protected WeekData createInstance(String key, String data) {
            return Calendar.getWeekDataForRegionInternal(key);
        }
    }

    public static final class WeekData {
        public final int firstDayOfWeek;
        public final int minimalDaysInFirstWeek;
        public final int weekendOnset;
        public final int weekendOnsetMillis;
        public final int weekendCease;
        public final int weekendCeaseMillis;

        public WeekData(int fdow, int mdifw, int weekendOnset, int weekendOnsetMillis, int weekendCease, int weekendCeaseMillis) {
            this.firstDayOfWeek = fdow;
            this.minimalDaysInFirstWeek = mdifw;
            this.weekendOnset = weekendOnset;
            this.weekendOnsetMillis = weekendOnsetMillis;
            this.weekendCease = weekendCease;
            this.weekendCeaseMillis = weekendCeaseMillis;
        }

        public int hashCode() {
            return ((((this.firstDayOfWeek * 37 + this.minimalDaysInFirstWeek) * 37 + this.weekendOnset) * 37 + this.weekendOnsetMillis) * 37 + this.weekendCease) * 37 + this.weekendCeaseMillis;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof WeekData)) {
                return false;
            }
            WeekData that = (WeekData)other;
            return this.firstDayOfWeek == that.firstDayOfWeek && this.minimalDaysInFirstWeek == that.minimalDaysInFirstWeek && this.weekendOnset == that.weekendOnset && this.weekendOnsetMillis == that.weekendOnsetMillis && this.weekendCease == that.weekendCease && this.weekendCeaseMillis == that.weekendCeaseMillis;
        }

        public String toString() {
            return "{" + this.firstDayOfWeek + ", " + this.minimalDaysInFirstWeek + ", " + this.weekendOnset + ", " + this.weekendOnsetMillis + ", " + this.weekendCease + ", " + this.weekendCeaseMillis + "}";
        }
    }

    @Deprecated
    public static class FormatConfiguration {
        private String pattern;
        private String override;
        private DateFormatSymbols formatData;
        private Calendar cal;
        private ULocale loc;

        private FormatConfiguration() {
        }

        @Deprecated
        public String getPatternString() {
            return this.pattern;
        }

        @Deprecated
        public String getOverrideString() {
            return this.override;
        }

        @Deprecated
        public Calendar getCalendar() {
            return this.cal;
        }

        @Deprecated
        public ULocale getLocale() {
            return this.loc;
        }

        @Deprecated
        public DateFormatSymbols getDateFormatSymbols() {
            return this.formatData;
        }
    }

    static class PatternData {
        private String[] patterns;
        private String[] overrides;

        public PatternData(String[] patterns, String[] overrides) {
            this.patterns = patterns;
            this.overrides = overrides;
        }

        private String getDateTimePattern(int dateStyle) {
            int glueIndex = 8;
            if (this.patterns.length >= 13) {
                glueIndex += dateStyle + 1;
            }
            String dateTimePattern = this.patterns[glueIndex];
            return dateTimePattern;
        }

        private static PatternData make(Calendar cal, ULocale loc) {
            String calType = cal.getType();
            String key = loc.getBaseName() + "+" + calType;
            PatternData patternData = (PatternData)PATTERN_CACHE.get(key);
            if (patternData == null) {
                try {
                    patternData = Calendar.getPatternData(loc, calType);
                }
                catch (MissingResourceException e2) {
                    patternData = new PatternData(DEFAULT_PATTERNS, null);
                }
                PATTERN_CACHE.put(key, patternData);
            }
            return patternData;
        }
    }

    private static enum CalType {
        GREGORIAN("gregorian"),
        ISO8601("iso8601"),
        BUDDHIST("buddhist"),
        CHINESE("chinese"),
        COPTIC("coptic"),
        DANGI("dangi"),
        ETHIOPIC("ethiopic"),
        ETHIOPIC_AMETE_ALEM("ethiopic-amete-alem"),
        HEBREW("hebrew"),
        INDIAN("indian"),
        ISLAMIC("islamic"),
        ISLAMIC_CIVIL("islamic-civil"),
        ISLAMIC_RGSA("islamic-rgsa"),
        ISLAMIC_TBLA("islamic-tbla"),
        ISLAMIC_UMALQURA("islamic-umalqura"),
        JAPANESE("japanese"),
        PERSIAN("persian"),
        ROC("roc"),
        UNKNOWN("unknown");

        String id;

        private CalType(String id) {
            this.id = id;
        }
    }
}

