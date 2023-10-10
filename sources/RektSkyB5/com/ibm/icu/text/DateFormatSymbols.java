/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.NumberingSystem;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

public class DateFormatSymbols
implements Serializable,
Cloneable {
    public static final int FORMAT = 0;
    public static final int STANDALONE = 1;
    @Deprecated
    public static final int NUMERIC = 2;
    @Deprecated
    public static final int DT_CONTEXT_COUNT = 3;
    public static final int ABBREVIATED = 0;
    public static final int WIDE = 1;
    public static final int NARROW = 2;
    public static final int SHORT = 3;
    @Deprecated
    public static final int DT_WIDTH_COUNT = 4;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_WIDE = 0;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_ABBREV = 1;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_NARROW = 2;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_WIDE = 3;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_ABBREV = 4;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_NARROW = 5;
    static final int DT_LEAP_MONTH_PATTERN_NUMERIC = 6;
    static final int DT_MONTH_PATTERN_COUNT = 7;
    static final String DEFAULT_TIME_SEPARATOR = ":";
    static final String ALTERNATE_TIME_SEPARATOR = ".";
    String[] eras = null;
    String[] eraNames = null;
    String[] narrowEras = null;
    String[] months = null;
    String[] shortMonths = null;
    String[] narrowMonths = null;
    String[] standaloneMonths = null;
    String[] standaloneShortMonths = null;
    String[] standaloneNarrowMonths = null;
    String[] weekdays = null;
    String[] shortWeekdays = null;
    String[] shorterWeekdays = null;
    String[] narrowWeekdays = null;
    String[] standaloneWeekdays = null;
    String[] standaloneShortWeekdays = null;
    String[] standaloneShorterWeekdays = null;
    String[] standaloneNarrowWeekdays = null;
    String[] ampms = null;
    String[] ampmsNarrow = null;
    private String timeSeparator = null;
    String[] shortQuarters = null;
    String[] quarters = null;
    String[] standaloneShortQuarters = null;
    String[] standaloneQuarters = null;
    String[] leapMonthPatterns = null;
    String[] shortYearNames = null;
    String[] shortZodiacNames = null;
    private String[][] zoneStrings = null;
    static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXxrbB";
    String localPatternChars = null;
    String[] abbreviatedDayPeriods = null;
    String[] wideDayPeriods = null;
    String[] narrowDayPeriods = null;
    String[] standaloneAbbreviatedDayPeriods = null;
    String[] standaloneWideDayPeriods = null;
    String[] standaloneNarrowDayPeriods = null;
    private static final long serialVersionUID = -5987973545549424702L;
    private static final String[][] CALENDAR_CLASSES = new String[][]{{"GregorianCalendar", "gregorian"}, {"JapaneseCalendar", "japanese"}, {"BuddhistCalendar", "buddhist"}, {"TaiwanCalendar", "roc"}, {"PersianCalendar", "persian"}, {"IslamicCalendar", "islamic"}, {"HebrewCalendar", "hebrew"}, {"ChineseCalendar", "chinese"}, {"IndianCalendar", "indian"}, {"CopticCalendar", "coptic"}, {"EthiopicCalendar", "ethiopic"}};
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>();
    Map<CapitalizationContextUsage, boolean[]> capitalization = null;
    static final int millisPerHour = 3600000;
    private static CacheBase<String, DateFormatSymbols, ULocale> DFSCACHE;
    private static final String[] LEAP_MONTH_PATTERNS_PATHS;
    private static final String[] DAY_PERIOD_KEYS;
    private ULocale requestedLocale;
    private ULocale validLocale;
    private ULocale actualLocale;

    public DateFormatSymbols() {
        this(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public DateFormatSymbols(Locale locale) {
        this(ULocale.forLocale(locale));
    }

    public DateFormatSymbols(ULocale locale) {
        this.initializeData(locale, CalendarUtil.getCalendarType(locale));
    }

    public static DateFormatSymbols getInstance() {
        return new DateFormatSymbols();
    }

    public static DateFormatSymbols getInstance(Locale locale) {
        return new DateFormatSymbols(locale);
    }

    public static DateFormatSymbols getInstance(ULocale locale) {
        return new DateFormatSymbols(locale);
    }

    public static Locale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableLocales();
    }

    public static ULocale[] getAvailableULocales() {
        return ICUResourceBundle.getAvailableULocales();
    }

    public String[] getEras() {
        return this.duplicate(this.eras);
    }

    public void setEras(String[] newEras) {
        this.eras = this.duplicate(newEras);
    }

    public String[] getEraNames() {
        return this.duplicate(this.eraNames);
    }

    public void setEraNames(String[] newEraNames) {
        this.eraNames = this.duplicate(newEraNames);
    }

    public String[] getMonths() {
        return this.duplicate(this.months);
    }

    public String[] getMonths(int context, int width) {
        String[] returnValue = null;
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        returnValue = this.months;
                        break;
                    }
                    case 0: 
                    case 3: {
                        returnValue = this.shortMonths;
                        break;
                    }
                    case 2: {
                        returnValue = this.narrowMonths;
                    }
                }
                break;
            }
            case 1: {
                switch (width) {
                    case 1: {
                        returnValue = this.standaloneMonths;
                        break block0;
                    }
                    case 0: 
                    case 3: {
                        returnValue = this.standaloneShortMonths;
                        break block0;
                    }
                    case 2: {
                        returnValue = this.standaloneNarrowMonths;
                    }
                }
            }
        }
        if (returnValue == null) {
            throw new IllegalArgumentException("Bad context or width argument");
        }
        return this.duplicate(returnValue);
    }

    public void setMonths(String[] newMonths) {
        this.months = this.duplicate(newMonths);
    }

    public void setMonths(String[] newMonths, int context, int width) {
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        this.months = this.duplicate(newMonths);
                        break block0;
                    }
                    case 0: {
                        this.shortMonths = this.duplicate(newMonths);
                        break block0;
                    }
                    case 2: {
                        this.narrowMonths = this.duplicate(newMonths);
                        break block0;
                    }
                }
                break;
            }
            case 1: {
                switch (width) {
                    case 1: {
                        this.standaloneMonths = this.duplicate(newMonths);
                        break block0;
                    }
                    case 0: {
                        this.standaloneShortMonths = this.duplicate(newMonths);
                        break block0;
                    }
                    case 2: {
                        this.standaloneNarrowMonths = this.duplicate(newMonths);
                        break block0;
                    }
                }
            }
        }
    }

    public String[] getShortMonths() {
        return this.duplicate(this.shortMonths);
    }

    public void setShortMonths(String[] newShortMonths) {
        this.shortMonths = this.duplicate(newShortMonths);
    }

    public String[] getWeekdays() {
        return this.duplicate(this.weekdays);
    }

    public String[] getWeekdays(int context, int width) {
        String[] returnValue = null;
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        returnValue = this.weekdays;
                        break;
                    }
                    case 0: {
                        returnValue = this.shortWeekdays;
                        break;
                    }
                    case 3: {
                        returnValue = this.shorterWeekdays != null ? this.shorterWeekdays : this.shortWeekdays;
                        break;
                    }
                    case 2: {
                        returnValue = this.narrowWeekdays;
                    }
                }
                break;
            }
            case 1: {
                switch (width) {
                    case 1: {
                        returnValue = this.standaloneWeekdays;
                        break block0;
                    }
                    case 0: {
                        returnValue = this.standaloneShortWeekdays;
                        break block0;
                    }
                    case 3: {
                        returnValue = this.standaloneShorterWeekdays != null ? this.standaloneShorterWeekdays : this.standaloneShortWeekdays;
                        break block0;
                    }
                    case 2: {
                        returnValue = this.standaloneNarrowWeekdays;
                    }
                }
            }
        }
        if (returnValue == null) {
            throw new IllegalArgumentException("Bad context or width argument");
        }
        return this.duplicate(returnValue);
    }

    public void setWeekdays(String[] newWeekdays, int context, int width) {
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        this.weekdays = this.duplicate(newWeekdays);
                        break;
                    }
                    case 0: {
                        this.shortWeekdays = this.duplicate(newWeekdays);
                        break;
                    }
                    case 3: {
                        this.shorterWeekdays = this.duplicate(newWeekdays);
                        break;
                    }
                    case 2: {
                        this.narrowWeekdays = this.duplicate(newWeekdays);
                    }
                }
                break;
            }
            case 1: {
                switch (width) {
                    case 1: {
                        this.standaloneWeekdays = this.duplicate(newWeekdays);
                        break block0;
                    }
                    case 0: {
                        this.standaloneShortWeekdays = this.duplicate(newWeekdays);
                        break block0;
                    }
                    case 3: {
                        this.standaloneShorterWeekdays = this.duplicate(newWeekdays);
                        break block0;
                    }
                    case 2: {
                        this.standaloneNarrowWeekdays = this.duplicate(newWeekdays);
                    }
                }
            }
        }
    }

    public void setWeekdays(String[] newWeekdays) {
        this.weekdays = this.duplicate(newWeekdays);
    }

    public String[] getShortWeekdays() {
        return this.duplicate(this.shortWeekdays);
    }

    public void setShortWeekdays(String[] newAbbrevWeekdays) {
        this.shortWeekdays = this.duplicate(newAbbrevWeekdays);
    }

    public String[] getQuarters(int context, int width) {
        String[] returnValue = null;
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        returnValue = this.quarters;
                        break;
                    }
                    case 0: 
                    case 3: {
                        returnValue = this.shortQuarters;
                        break;
                    }
                    case 2: {
                        returnValue = null;
                    }
                }
                break;
            }
            case 1: {
                switch (width) {
                    case 1: {
                        returnValue = this.standaloneQuarters;
                        break block0;
                    }
                    case 0: 
                    case 3: {
                        returnValue = this.standaloneShortQuarters;
                        break block0;
                    }
                    case 2: {
                        returnValue = null;
                    }
                }
            }
        }
        if (returnValue == null) {
            throw new IllegalArgumentException("Bad context or width argument");
        }
        return this.duplicate(returnValue);
    }

    public void setQuarters(String[] newQuarters, int context, int width) {
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        this.quarters = this.duplicate(newQuarters);
                        break block0;
                    }
                    case 0: {
                        this.shortQuarters = this.duplicate(newQuarters);
                        break block0;
                    }
                    case 2: {
                        break block0;
                    }
                }
                break;
            }
            case 1: {
                switch (width) {
                    case 1: {
                        this.standaloneQuarters = this.duplicate(newQuarters);
                        break block0;
                    }
                    case 0: {
                        this.standaloneShortQuarters = this.duplicate(newQuarters);
                        break block0;
                    }
                    case 2: {
                        break block0;
                    }
                }
            }
        }
    }

    public String[] getYearNames(int context, int width) {
        if (this.shortYearNames != null) {
            return this.duplicate(this.shortYearNames);
        }
        return null;
    }

    public void setYearNames(String[] yearNames, int context, int width) {
        if (context == 0 && width == 0) {
            this.shortYearNames = this.duplicate(yearNames);
        }
    }

    public String[] getZodiacNames(int context, int width) {
        if (this.shortZodiacNames != null) {
            return this.duplicate(this.shortZodiacNames);
        }
        return null;
    }

    public void setZodiacNames(String[] zodiacNames, int context, int width) {
        if (context == 0 && width == 0) {
            this.shortZodiacNames = this.duplicate(zodiacNames);
        }
    }

    @Deprecated
    public String getLeapMonthPattern(int context, int width) {
        if (this.leapMonthPatterns != null) {
            int leapMonthPatternIndex = -1;
            switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            leapMonthPatternIndex = 0;
                            break;
                        }
                        case 0: 
                        case 3: {
                            leapMonthPatternIndex = 1;
                            break;
                        }
                        case 2: {
                            leapMonthPatternIndex = 2;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            leapMonthPatternIndex = 3;
                            break;
                        }
                        case 0: 
                        case 3: {
                            leapMonthPatternIndex = 1;
                            break;
                        }
                        case 2: {
                            leapMonthPatternIndex = 5;
                        }
                    }
                    break;
                }
                case 2: {
                    leapMonthPatternIndex = 6;
                }
            }
            if (leapMonthPatternIndex < 0) {
                throw new IllegalArgumentException("Bad context or width argument");
            }
            return this.leapMonthPatterns[leapMonthPatternIndex];
        }
        return null;
    }

    @Deprecated
    public void setLeapMonthPattern(String leapMonthPattern, int context, int width) {
        if (this.leapMonthPatterns != null) {
            int leapMonthPatternIndex = -1;
            block0 : switch (context) {
                case 0: {
                    switch (width) {
                        case 1: {
                            leapMonthPatternIndex = 0;
                            break block0;
                        }
                        case 0: {
                            leapMonthPatternIndex = 1;
                            break block0;
                        }
                        case 2: {
                            leapMonthPatternIndex = 2;
                            break block0;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (width) {
                        case 1: {
                            leapMonthPatternIndex = 3;
                            break block0;
                        }
                        case 0: {
                            leapMonthPatternIndex = 1;
                            break block0;
                        }
                        case 2: {
                            leapMonthPatternIndex = 5;
                            break block0;
                        }
                    }
                    break;
                }
                case 2: {
                    leapMonthPatternIndex = 6;
                    break;
                }
            }
            if (leapMonthPatternIndex >= 0) {
                this.leapMonthPatterns[leapMonthPatternIndex] = leapMonthPattern;
            }
        }
    }

    public String[] getAmPmStrings() {
        return this.duplicate(this.ampms);
    }

    public void setAmPmStrings(String[] newAmpms) {
        this.ampms = this.duplicate(newAmpms);
    }

    @Deprecated
    public String getTimeSeparatorString() {
        return this.timeSeparator;
    }

    @Deprecated
    public void setTimeSeparatorString(String newTimeSeparator) {
        this.timeSeparator = newTimeSeparator;
    }

    public String[][] getZoneStrings() {
        if (this.zoneStrings != null) {
            return this.duplicate(this.zoneStrings);
        }
        String[] tzIDs = TimeZone.getAvailableIDs();
        TimeZoneNames tznames = TimeZoneNames.getInstance(this.validLocale);
        tznames.loadAllDisplayNames();
        TimeZoneNames.NameType[] types = new TimeZoneNames.NameType[]{TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_DAYLIGHT};
        long now = System.currentTimeMillis();
        String[][] array = new String[tzIDs.length][5];
        for (int i2 = 0; i2 < tzIDs.length; ++i2) {
            String canonicalID = TimeZone.getCanonicalID(tzIDs[i2]);
            if (canonicalID == null) {
                canonicalID = tzIDs[i2];
            }
            array[i2][0] = tzIDs[i2];
            tznames.getDisplayNames(canonicalID, types, now, array[i2], 1);
        }
        this.zoneStrings = array;
        return this.zoneStrings;
    }

    public void setZoneStrings(String[][] newZoneStrings) {
        this.zoneStrings = this.duplicate(newZoneStrings);
    }

    public String getLocalPatternChars() {
        return this.localPatternChars;
    }

    public void setLocalPatternChars(String newLocalPatternChars) {
        this.localPatternChars = newLocalPatternChars;
    }

    public Object clone() {
        try {
            DateFormatSymbols other = (DateFormatSymbols)super.clone();
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    public int hashCode() {
        return this.requestedLocale.toString().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        DateFormatSymbols that = (DateFormatSymbols)obj;
        return Utility.arrayEquals(this.eras, (Object)that.eras) && Utility.arrayEquals(this.eraNames, (Object)that.eraNames) && Utility.arrayEquals(this.months, (Object)that.months) && Utility.arrayEquals(this.shortMonths, (Object)that.shortMonths) && Utility.arrayEquals(this.narrowMonths, (Object)that.narrowMonths) && Utility.arrayEquals(this.standaloneMonths, (Object)that.standaloneMonths) && Utility.arrayEquals(this.standaloneShortMonths, (Object)that.standaloneShortMonths) && Utility.arrayEquals(this.standaloneNarrowMonths, (Object)that.standaloneNarrowMonths) && Utility.arrayEquals(this.weekdays, (Object)that.weekdays) && Utility.arrayEquals(this.shortWeekdays, (Object)that.shortWeekdays) && Utility.arrayEquals(this.shorterWeekdays, (Object)that.shorterWeekdays) && Utility.arrayEquals(this.narrowWeekdays, (Object)that.narrowWeekdays) && Utility.arrayEquals(this.standaloneWeekdays, (Object)that.standaloneWeekdays) && Utility.arrayEquals(this.standaloneShortWeekdays, (Object)that.standaloneShortWeekdays) && Utility.arrayEquals(this.standaloneShorterWeekdays, (Object)that.standaloneShorterWeekdays) && Utility.arrayEquals(this.standaloneNarrowWeekdays, (Object)that.standaloneNarrowWeekdays) && Utility.arrayEquals(this.ampms, (Object)that.ampms) && Utility.arrayEquals(this.ampmsNarrow, (Object)that.ampmsNarrow) && Utility.arrayEquals(this.abbreviatedDayPeriods, (Object)that.abbreviatedDayPeriods) && Utility.arrayEquals(this.wideDayPeriods, (Object)that.wideDayPeriods) && Utility.arrayEquals(this.narrowDayPeriods, (Object)that.narrowDayPeriods) && Utility.arrayEquals(this.standaloneAbbreviatedDayPeriods, (Object)that.standaloneAbbreviatedDayPeriods) && Utility.arrayEquals(this.standaloneWideDayPeriods, (Object)that.standaloneWideDayPeriods) && Utility.arrayEquals(this.standaloneNarrowDayPeriods, (Object)that.standaloneNarrowDayPeriods) && Utility.arrayEquals(this.timeSeparator, (Object)that.timeSeparator) && DateFormatSymbols.arrayOfArrayEquals(this.zoneStrings, that.zoneStrings) && this.requestedLocale.getDisplayName().equals(that.requestedLocale.getDisplayName()) && Utility.arrayEquals(this.localPatternChars, (Object)that.localPatternChars);
    }

    protected void initializeData(ULocale desiredLocale, String type) {
        String key = desiredLocale.getBaseName() + '+' + type;
        String ns = desiredLocale.getKeywordValue("numbers");
        if (ns != null && ns.length() > 0) {
            key = key + '+' + ns;
        }
        DateFormatSymbols dfs = DFSCACHE.getInstance(key, desiredLocale);
        this.initializeData(dfs);
    }

    void initializeData(DateFormatSymbols dfs) {
        this.eras = dfs.eras;
        this.eraNames = dfs.eraNames;
        this.narrowEras = dfs.narrowEras;
        this.months = dfs.months;
        this.shortMonths = dfs.shortMonths;
        this.narrowMonths = dfs.narrowMonths;
        this.standaloneMonths = dfs.standaloneMonths;
        this.standaloneShortMonths = dfs.standaloneShortMonths;
        this.standaloneNarrowMonths = dfs.standaloneNarrowMonths;
        this.weekdays = dfs.weekdays;
        this.shortWeekdays = dfs.shortWeekdays;
        this.shorterWeekdays = dfs.shorterWeekdays;
        this.narrowWeekdays = dfs.narrowWeekdays;
        this.standaloneWeekdays = dfs.standaloneWeekdays;
        this.standaloneShortWeekdays = dfs.standaloneShortWeekdays;
        this.standaloneShorterWeekdays = dfs.standaloneShorterWeekdays;
        this.standaloneNarrowWeekdays = dfs.standaloneNarrowWeekdays;
        this.ampms = dfs.ampms;
        this.ampmsNarrow = dfs.ampmsNarrow;
        this.timeSeparator = dfs.timeSeparator;
        this.shortQuarters = dfs.shortQuarters;
        this.quarters = dfs.quarters;
        this.standaloneShortQuarters = dfs.standaloneShortQuarters;
        this.standaloneQuarters = dfs.standaloneQuarters;
        this.leapMonthPatterns = dfs.leapMonthPatterns;
        this.shortYearNames = dfs.shortYearNames;
        this.shortZodiacNames = dfs.shortZodiacNames;
        this.abbreviatedDayPeriods = dfs.abbreviatedDayPeriods;
        this.wideDayPeriods = dfs.wideDayPeriods;
        this.narrowDayPeriods = dfs.narrowDayPeriods;
        this.standaloneAbbreviatedDayPeriods = dfs.standaloneAbbreviatedDayPeriods;
        this.standaloneWideDayPeriods = dfs.standaloneWideDayPeriods;
        this.standaloneNarrowDayPeriods = dfs.standaloneNarrowDayPeriods;
        this.zoneStrings = dfs.zoneStrings;
        this.localPatternChars = dfs.localPatternChars;
        this.capitalization = dfs.capitalization;
        this.actualLocale = dfs.actualLocale;
        this.validLocale = dfs.validLocale;
        this.requestedLocale = dfs.requestedLocale;
    }

    private DateFormatSymbols(ULocale desiredLocale, ICUResourceBundle b2, String calendarType) {
        this.initializeData(desiredLocale, b2, calendarType);
    }

    @Deprecated
    protected void initializeData(ULocale desiredLocale, ICUResourceBundle b2, String calendarType) {
        NumberingSystem ns;
        CapitalizationContextUsage[] allUsages;
        CalendarDataSink calendarSink = new CalendarDataSink();
        if (b2 == null) {
            b2 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", desiredLocale);
        }
        while (calendarType != null) {
            ICUResourceBundle dataForType = b2.findWithFallback("calendar/" + calendarType);
            if (dataForType == null) {
                if (!"gregorian".equals(calendarType)) {
                    calendarType = "gregorian";
                    calendarSink.visitAllResources();
                    continue;
                }
                throw new MissingResourceException("The 'gregorian' calendar type wasn't found for the locale: " + desiredLocale.getBaseName(), this.getClass().getName(), "gregorian");
            }
            calendarSink.preEnumerate(calendarType);
            dataForType.getAllItemsWithFallback("", calendarSink);
            if (calendarType.equals("gregorian")) break;
            calendarType = calendarSink.nextCalendarType;
            if (calendarType != null) continue;
            calendarType = "gregorian";
            calendarSink.visitAllResources();
        }
        Map<String, String[]> arrays = calendarSink.arrays;
        Map<String, Map<String, String>> maps = calendarSink.maps;
        this.eras = arrays.get("eras/abbreviated");
        this.eraNames = arrays.get("eras/wide");
        this.narrowEras = arrays.get("eras/narrow");
        this.months = arrays.get("monthNames/format/wide");
        this.shortMonths = arrays.get("monthNames/format/abbreviated");
        this.narrowMonths = arrays.get("monthNames/format/narrow");
        this.standaloneMonths = arrays.get("monthNames/stand-alone/wide");
        this.standaloneShortMonths = arrays.get("monthNames/stand-alone/abbreviated");
        this.standaloneNarrowMonths = arrays.get("monthNames/stand-alone/narrow");
        String[] lWeekdays = arrays.get("dayNames/format/wide");
        this.weekdays = new String[8];
        this.weekdays[0] = "";
        System.arraycopy(lWeekdays, 0, this.weekdays, 1, lWeekdays.length);
        String[] aWeekdays = arrays.get("dayNames/format/abbreviated");
        this.shortWeekdays = new String[8];
        this.shortWeekdays[0] = "";
        System.arraycopy(aWeekdays, 0, this.shortWeekdays, 1, aWeekdays.length);
        String[] sWeekdays = arrays.get("dayNames/format/short");
        this.shorterWeekdays = new String[8];
        this.shorterWeekdays[0] = "";
        System.arraycopy(sWeekdays, 0, this.shorterWeekdays, 1, sWeekdays.length);
        String[] nWeekdays = arrays.get("dayNames/format/narrow");
        if (nWeekdays == null && (nWeekdays = arrays.get("dayNames/stand-alone/narrow")) == null && (nWeekdays = arrays.get("dayNames/format/abbreviated")) == null) {
            throw new MissingResourceException("Resource not found", this.getClass().getName(), "dayNames/format/abbreviated");
        }
        this.narrowWeekdays = new String[8];
        this.narrowWeekdays[0] = "";
        System.arraycopy(nWeekdays, 0, this.narrowWeekdays, 1, nWeekdays.length);
        String[] swWeekdays = null;
        swWeekdays = arrays.get("dayNames/stand-alone/wide");
        this.standaloneWeekdays = new String[8];
        this.standaloneWeekdays[0] = "";
        System.arraycopy(swWeekdays, 0, this.standaloneWeekdays, 1, swWeekdays.length);
        String[] saWeekdays = null;
        saWeekdays = arrays.get("dayNames/stand-alone/abbreviated");
        this.standaloneShortWeekdays = new String[8];
        this.standaloneShortWeekdays[0] = "";
        System.arraycopy(saWeekdays, 0, this.standaloneShortWeekdays, 1, saWeekdays.length);
        String[] ssWeekdays = null;
        ssWeekdays = arrays.get("dayNames/stand-alone/short");
        this.standaloneShorterWeekdays = new String[8];
        this.standaloneShorterWeekdays[0] = "";
        System.arraycopy(ssWeekdays, 0, this.standaloneShorterWeekdays, 1, ssWeekdays.length);
        String[] snWeekdays = null;
        snWeekdays = arrays.get("dayNames/stand-alone/narrow");
        this.standaloneNarrowWeekdays = new String[8];
        this.standaloneNarrowWeekdays[0] = "";
        System.arraycopy(snWeekdays, 0, this.standaloneNarrowWeekdays, 1, snWeekdays.length);
        this.ampms = arrays.get("AmPmMarkers");
        this.ampmsNarrow = arrays.get("AmPmMarkersNarrow");
        this.quarters = arrays.get("quarters/format/wide");
        this.shortQuarters = arrays.get("quarters/format/abbreviated");
        this.standaloneQuarters = arrays.get("quarters/stand-alone/wide");
        this.standaloneShortQuarters = arrays.get("quarters/stand-alone/abbreviated");
        this.abbreviatedDayPeriods = this.loadDayPeriodStrings(maps.get("dayPeriod/format/abbreviated"));
        this.wideDayPeriods = this.loadDayPeriodStrings(maps.get("dayPeriod/format/wide"));
        this.narrowDayPeriods = this.loadDayPeriodStrings(maps.get("dayPeriod/format/narrow"));
        this.standaloneAbbreviatedDayPeriods = this.loadDayPeriodStrings(maps.get("dayPeriod/stand-alone/abbreviated"));
        this.standaloneWideDayPeriods = this.loadDayPeriodStrings(maps.get("dayPeriod/stand-alone/wide"));
        this.standaloneNarrowDayPeriods = this.loadDayPeriodStrings(maps.get("dayPeriod/stand-alone/narrow"));
        for (int i2 = 0; i2 < 7; ++i2) {
            String leapMonthPattern;
            Map<String, String> monthPatternMap;
            String monthPatternPath = LEAP_MONTH_PATTERNS_PATHS[i2];
            if (monthPatternPath == null || (monthPatternMap = maps.get(monthPatternPath)) == null || (leapMonthPattern = monthPatternMap.get("leap")) == null) continue;
            if (this.leapMonthPatterns == null) {
                this.leapMonthPatterns = new String[7];
            }
            this.leapMonthPatterns[i2] = leapMonthPattern;
        }
        this.shortYearNames = arrays.get("cyclicNameSets/years/format/abbreviated");
        this.shortZodiacNames = arrays.get("cyclicNameSets/zodiacs/format/abbreviated");
        this.requestedLocale = desiredLocale;
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", desiredLocale);
        this.localPatternChars = patternChars;
        ULocale uloc = rb.getULocale();
        this.setLocale(uloc, uloc);
        this.capitalization = new HashMap<CapitalizationContextUsage, boolean[]>();
        boolean[] noTransforms = new boolean[]{false, false};
        for (CapitalizationContextUsage usage : allUsages = CapitalizationContextUsage.values()) {
            this.capitalization.put(usage, noTransforms);
        }
        ICUResourceBundle contextTransformsBundle = null;
        try {
            contextTransformsBundle = rb.getWithFallback("contextTransforms");
        }
        catch (MissingResourceException e2) {
            contextTransformsBundle = null;
        }
        if (contextTransformsBundle != null) {
            UResourceBundleIterator ctIterator = contextTransformsBundle.getIterator();
            while (ctIterator.hasNext()) {
                String usageKey;
                CapitalizationContextUsage usage;
                UResourceBundle contextTransformUsage = ctIterator.next();
                int[] intVector = contextTransformUsage.getIntVector();
                if (intVector.length < 2 || (usage = contextUsageTypeMap.get(usageKey = contextTransformUsage.getKey())) == null) continue;
                boolean[] transforms = new boolean[]{intVector[0] != 0, intVector[1] != 0};
                this.capitalization.put(usage, transforms);
            }
        }
        String nsName = (ns = NumberingSystem.getInstance(desiredLocale)) == null ? "latn" : ns.getName();
        String tsPath = "NumberElements/" + nsName + "/symbols/timeSeparator";
        try {
            this.setTimeSeparatorString(rb.getStringWithFallback(tsPath));
        }
        catch (MissingResourceException e3) {
            this.setTimeSeparatorString(DEFAULT_TIME_SEPARATOR);
        }
    }

    private static final boolean arrayOfArrayEquals(Object[][] aa1, Object[][] aa2) {
        if (aa1 == aa2) {
            return true;
        }
        if (aa1 == null || aa2 == null) {
            return false;
        }
        if (aa1.length != aa2.length) {
            return false;
        }
        boolean equal = true;
        for (int i2 = 0; i2 < aa1.length && (equal = Utility.arrayEquals(aa1[i2], (Object)aa2[i2])); ++i2) {
        }
        return equal;
    }

    private String[] loadDayPeriodStrings(Map<String, String> resourceMap) {
        String[] strings = new String[DAY_PERIOD_KEYS.length];
        if (resourceMap != null) {
            for (int i2 = 0; i2 < DAY_PERIOD_KEYS.length; ++i2) {
                strings[i2] = resourceMap.get(DAY_PERIOD_KEYS[i2]);
            }
        }
        return strings;
    }

    private final String[] duplicate(String[] srcArray) {
        return (String[])srcArray.clone();
    }

    private final String[][] duplicate(String[][] srcArray) {
        String[][] aCopy = new String[srcArray.length][];
        for (int i2 = 0; i2 < srcArray.length; ++i2) {
            aCopy[i2] = this.duplicate(srcArray[i2]);
        }
        return aCopy;
    }

    public DateFormatSymbols(Calendar cal, Locale locale) {
        this.initializeData(ULocale.forLocale(locale), cal.getType());
    }

    public DateFormatSymbols(Calendar cal, ULocale locale) {
        this.initializeData(locale, cal.getType());
    }

    public DateFormatSymbols(Class<? extends Calendar> calendarClass, Locale locale) {
        this(calendarClass, ULocale.forLocale(locale));
    }

    public DateFormatSymbols(Class<? extends Calendar> calendarClass, ULocale locale) {
        String fullName = calendarClass.getName();
        int lastDot = fullName.lastIndexOf(46);
        String className = fullName.substring(lastDot + 1);
        String calType = null;
        for (String[] calClassInfo : CALENDAR_CLASSES) {
            if (!calClassInfo[0].equals(className)) continue;
            calType = calClassInfo[1];
            break;
        }
        if (calType == null) {
            calType = className.replaceAll("Calendar", "").toLowerCase(Locale.ENGLISH);
        }
        this.initializeData(locale, calType);
    }

    public DateFormatSymbols(ResourceBundle bundle, Locale locale) {
        this(bundle, ULocale.forLocale(locale));
    }

    public DateFormatSymbols(ResourceBundle bundle, ULocale locale) {
        this.initializeData(locale, (ICUResourceBundle)bundle, CalendarUtil.getCalendarType(locale));
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, Locale locale) throws MissingResourceException {
        return null;
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, ULocale locale) throws MissingResourceException {
        return null;
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Calendar cal, Locale locale) throws MissingResourceException {
        return null;
    }

    @Deprecated
    public static ResourceBundle getDateFormatBundle(Calendar cal, ULocale locale) throws MissingResourceException {
        return null;
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

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    static {
        contextUsageTypeMap.put("month-format-except-narrow", CapitalizationContextUsage.MONTH_FORMAT);
        contextUsageTypeMap.put("month-standalone-except-narrow", CapitalizationContextUsage.MONTH_STANDALONE);
        contextUsageTypeMap.put("month-narrow", CapitalizationContextUsage.MONTH_NARROW);
        contextUsageTypeMap.put("day-format-except-narrow", CapitalizationContextUsage.DAY_FORMAT);
        contextUsageTypeMap.put("day-standalone-except-narrow", CapitalizationContextUsage.DAY_STANDALONE);
        contextUsageTypeMap.put("day-narrow", CapitalizationContextUsage.DAY_NARROW);
        contextUsageTypeMap.put("era-name", CapitalizationContextUsage.ERA_WIDE);
        contextUsageTypeMap.put("era-abbr", CapitalizationContextUsage.ERA_ABBREV);
        contextUsageTypeMap.put("era-narrow", CapitalizationContextUsage.ERA_NARROW);
        contextUsageTypeMap.put("zone-long", CapitalizationContextUsage.ZONE_LONG);
        contextUsageTypeMap.put("zone-short", CapitalizationContextUsage.ZONE_SHORT);
        contextUsageTypeMap.put("metazone-long", CapitalizationContextUsage.METAZONE_LONG);
        contextUsageTypeMap.put("metazone-short", CapitalizationContextUsage.METAZONE_SHORT);
        DFSCACHE = new SoftCache<String, DateFormatSymbols, ULocale>(){

            @Override
            protected DateFormatSymbols createInstance(String key, ULocale locale) {
                int typeStart = key.indexOf(43) + 1;
                int typeLimit = key.indexOf(43, typeStart);
                if (typeLimit < 0) {
                    typeLimit = key.length();
                }
                String type = key.substring(typeStart, typeLimit);
                return new DateFormatSymbols(locale, null, type);
            }
        };
        LEAP_MONTH_PATTERNS_PATHS = new String[7];
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[0] = "monthPatterns/format/wide";
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[1] = "monthPatterns/format/abbreviated";
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[2] = "monthPatterns/format/narrow";
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[3] = "monthPatterns/stand-alone/wide";
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[4] = "monthPatterns/stand-alone/abbreviated";
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[5] = "monthPatterns/stand-alone/narrow";
        DateFormatSymbols.LEAP_MONTH_PATTERNS_PATHS[6] = "monthPatterns/numeric/all";
        DAY_PERIOD_KEYS = new String[]{"midnight", "noon", "morning1", "afternoon1", "evening1", "night1", "morning2", "afternoon2", "evening2", "night2"};
    }

    private static final class CalendarDataSink
    extends UResource.Sink {
        Map<String, String[]> arrays = new TreeMap<String, String[]>();
        Map<String, Map<String, String>> maps = new TreeMap<String, Map<String, String>>();
        List<String> aliasPathPairs = new ArrayList<String>();
        String currentCalendarType = null;
        String nextCalendarType = null;
        private Set<String> resourcesToVisit;
        private String aliasRelativePath;
        private static final String CALENDAR_ALIAS_PREFIX = "/LOCALE/calendar/";

        CalendarDataSink() {
        }

        void visitAllResources() {
            this.resourcesToVisit = null;
        }

        void preEnumerate(String calendarType) {
            this.currentCalendarType = calendarType;
            this.nextCalendarType = null;
            this.aliasPathPairs.clear();
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            boolean modified;
            assert (this.currentCalendarType != null && !this.currentCalendarType.isEmpty());
            HashSet<String> resourcesToVisitNext = null;
            UResource.Table calendarData = value.getTable();
            int i2 = 0;
            while (calendarData.getKeyAndValue(i2, key, value)) {
                String keyString = key.toString();
                AliasType aliasType = this.processAliasFromValue(keyString, value);
                if (aliasType != AliasType.GREGORIAN) {
                    if (aliasType == AliasType.DIFFERENT_CALENDAR) {
                        if (resourcesToVisitNext == null) {
                            resourcesToVisitNext = new HashSet<String>();
                        }
                        resourcesToVisitNext.add(this.aliasRelativePath);
                    } else if (aliasType == AliasType.SAME_CALENDAR) {
                        if (!this.arrays.containsKey(keyString) && !this.maps.containsKey(keyString)) {
                            this.aliasPathPairs.add(this.aliasRelativePath);
                            this.aliasPathPairs.add(keyString);
                        }
                    } else if (this.resourcesToVisit == null || this.resourcesToVisit.isEmpty() || this.resourcesToVisit.contains(keyString) || keyString.equals("AmPmMarkersAbbr")) {
                        if (keyString.startsWith("AmPmMarkers")) {
                            if (!keyString.endsWith("%variant") && !this.arrays.containsKey(keyString)) {
                                String[] dataArray = value.getStringArray();
                                this.arrays.put(keyString, dataArray);
                            }
                        } else if (keyString.equals("eras") || keyString.equals("dayNames") || keyString.equals("monthNames") || keyString.equals("quarters") || keyString.equals("dayPeriod") || keyString.equals("monthPatterns") || keyString.equals("cyclicNameSets")) {
                            this.processResource(keyString, key, value);
                        }
                    }
                }
                ++i2;
            }
            do {
                modified = false;
                int i3 = 0;
                while (i3 < this.aliasPathPairs.size()) {
                    boolean mod = false;
                    String alias = this.aliasPathPairs.get(i3);
                    if (this.arrays.containsKey(alias)) {
                        this.arrays.put(this.aliasPathPairs.get(i3 + 1), this.arrays.get(alias));
                        mod = true;
                    } else if (this.maps.containsKey(alias)) {
                        this.maps.put(this.aliasPathPairs.get(i3 + 1), this.maps.get(alias));
                        mod = true;
                    }
                    if (mod) {
                        this.aliasPathPairs.remove(i3 + 1);
                        this.aliasPathPairs.remove(i3);
                        modified = true;
                        continue;
                    }
                    i3 += 2;
                }
            } while (modified && !this.aliasPathPairs.isEmpty());
            if (resourcesToVisitNext != null) {
                this.resourcesToVisit = resourcesToVisitNext;
            }
        }

        protected void processResource(String path, UResource.Key key, UResource.Value value) {
            UResource.Table table = value.getTable();
            HashMap<String, String> stringMap = null;
            int i2 = 0;
            while (table.getKeyAndValue(i2, key, value)) {
                if (!key.endsWith("%variant")) {
                    String keyString = key.toString();
                    if (value.getType() == 0) {
                        if (i2 == 0) {
                            stringMap = new HashMap<String, String>();
                            this.maps.put(path, stringMap);
                        }
                        assert (stringMap != null);
                        stringMap.put(keyString, value.getString());
                    } else {
                        assert (stringMap == null);
                        String currentPath = path + "/" + keyString;
                        if ((!currentPath.startsWith("cyclicNameSets") || "cyclicNameSets/years/format/abbreviated".startsWith(currentPath) || "cyclicNameSets/zodiacs/format/abbreviated".startsWith(currentPath) || "cyclicNameSets/dayParts/format/abbreviated".startsWith(currentPath)) && !this.arrays.containsKey(currentPath) && !this.maps.containsKey(currentPath)) {
                            AliasType aliasType = this.processAliasFromValue(currentPath, value);
                            if (aliasType == AliasType.SAME_CALENDAR) {
                                this.aliasPathPairs.add(this.aliasRelativePath);
                                this.aliasPathPairs.add(currentPath);
                            } else {
                                assert (aliasType == AliasType.NONE);
                                if (value.getType() == 8) {
                                    String[] dataArray = value.getStringArray();
                                    this.arrays.put(currentPath, dataArray);
                                } else if (value.getType() == 2) {
                                    this.processResource(currentPath, key, value);
                                }
                            }
                        }
                    }
                }
                ++i2;
            }
        }

        private AliasType processAliasFromValue(String currentRelativePath, UResource.Value value) {
            if (value.getType() == 3) {
                int typeLimit;
                String aliasPath = value.getAliasString();
                if (aliasPath.startsWith(CALENDAR_ALIAS_PREFIX) && aliasPath.length() > CALENDAR_ALIAS_PREFIX.length() && (typeLimit = aliasPath.indexOf(47, CALENDAR_ALIAS_PREFIX.length())) > CALENDAR_ALIAS_PREFIX.length()) {
                    String aliasCalendarType = aliasPath.substring(CALENDAR_ALIAS_PREFIX.length(), typeLimit);
                    this.aliasRelativePath = aliasPath.substring(typeLimit + 1);
                    if (this.currentCalendarType.equals(aliasCalendarType) && !currentRelativePath.equals(this.aliasRelativePath)) {
                        return AliasType.SAME_CALENDAR;
                    }
                    if (!this.currentCalendarType.equals(aliasCalendarType) && currentRelativePath.equals(this.aliasRelativePath)) {
                        if (aliasCalendarType.equals("gregorian")) {
                            return AliasType.GREGORIAN;
                        }
                        if (this.nextCalendarType == null || this.nextCalendarType.equals(aliasCalendarType)) {
                            this.nextCalendarType = aliasCalendarType;
                            return AliasType.DIFFERENT_CALENDAR;
                        }
                    }
                }
                throw new ICUException("Malformed 'calendar' alias. Path: " + aliasPath);
            }
            return AliasType.NONE;
        }

        private static enum AliasType {
            SAME_CALENDAR,
            DIFFERENT_CALENDAR,
            GREGORIAN,
            NONE;

        }
    }

    static enum CapitalizationContextUsage {
        OTHER,
        MONTH_FORMAT,
        MONTH_STANDALONE,
        MONTH_NARROW,
        DAY_FORMAT,
        DAY_STANDALONE,
        DAY_NARROW,
        ERA_WIDE,
        ERA_ABBREV,
        ERA_NARROW,
        ZONE_LONG,
        ZONE_SHORT,
        METAZONE_LONG,
        METAZONE_SHORT;

    }
}

