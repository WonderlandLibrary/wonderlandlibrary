/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DateFormatSymbols
implements Serializable,
Cloneable {
    public static final int FORMAT = 0;
    public static final int STANDALONE = 1;
    public static final int DT_CONTEXT_COUNT = 2;
    public static final int ABBREVIATED = 0;
    public static final int WIDE = 1;
    public static final int NARROW = 2;
    public static final int SHORT = 3;
    public static final int DT_WIDTH_COUNT = 4;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_WIDE = 0;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_ABBREV = 1;
    static final int DT_LEAP_MONTH_PATTERN_FORMAT_NARROW = 2;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_WIDE = 3;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_ABBREV = 4;
    static final int DT_LEAP_MONTH_PATTERN_STANDALONE_NARROW = 5;
    static final int DT_LEAP_MONTH_PATTERN_NUMERIC = 6;
    static final int DT_MONTH_PATTERN_COUNT = 7;
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
    String[] shortQuarters = null;
    String[] quarters = null;
    String[] standaloneShortQuarters = null;
    String[] standaloneQuarters = null;
    String[] leapMonthPatterns = null;
    String[] shortYearNames = null;
    private String[][] zoneStrings = null;
    static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
    String localPatternChars = null;
    private static final long serialVersionUID = -5987973545549424702L;
    private static final String[][] CALENDAR_CLASSES = new String[][]{{"GregorianCalendar", "gregorian"}, {"JapaneseCalendar", "japanese"}, {"BuddhistCalendar", "buddhist"}, {"TaiwanCalendar", "roc"}, {"PersianCalendar", "persian"}, {"IslamicCalendar", "islamic"}, {"HebrewCalendar", "hebrew"}, {"ChineseCalendar", "chinese"}, {"IndianCalendar", "indian"}, {"CopticCalendar", "coptic"}, {"EthiopicCalendar", "ethiopic"}};
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>();
    Map<CapitalizationContextUsage, boolean[]> capitalization = null;
    static final int millisPerHour = 3600000;
    private static ICUCache<String, DateFormatSymbols> DFSCACHE;
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
                        break block0;
                    }
                    case 0: 
                    case 3: {
                        returnValue = this.shortMonths;
                        break block0;
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
                        break block0;
                    }
                    case 0: {
                        returnValue = this.shortWeekdays;
                        break block0;
                    }
                    case 3: {
                        returnValue = this.shorterWeekdays != null ? this.shorterWeekdays : this.shortWeekdays;
                        break block0;
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
        return this.duplicate(returnValue);
    }

    public void setWeekdays(String[] newWeekdays, int context, int width) {
        block0 : switch (context) {
            case 0: {
                switch (width) {
                    case 1: {
                        this.weekdays = this.duplicate(newWeekdays);
                        break block0;
                    }
                    case 0: {
                        this.shortWeekdays = this.duplicate(newWeekdays);
                        break block0;
                    }
                    case 3: {
                        this.shorterWeekdays = this.duplicate(newWeekdays);
                        break block0;
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
                        break block0;
                    }
                    case 0: 
                    case 3: {
                        returnValue = this.shortQuarters;
                        break block0;
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

    public String[] getAmPmStrings() {
        return this.duplicate(this.ampms);
    }

    public void setAmPmStrings(String[] newAmpms) {
        this.ampms = this.duplicate(newAmpms);
    }

    public String[][] getZoneStrings() {
        if (this.zoneStrings != null) {
            return this.duplicate(this.zoneStrings);
        }
        String[] tzIDs = TimeZone.getAvailableIDs();
        TimeZoneNames tznames = TimeZoneNames.getInstance(this.validLocale);
        long now = System.currentTimeMillis();
        String[][] array = new String[tzIDs.length][5];
        for (int i = 0; i < tzIDs.length; ++i) {
            String canonicalID = TimeZone.getCanonicalID(tzIDs[i]);
            if (canonicalID == null) {
                canonicalID = tzIDs[i];
            }
            array[i][0] = tzIDs[i];
            array[i][1] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_STANDARD, now);
            array[i][2] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_STANDARD, now);
            array[i][3] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_DAYLIGHT, now);
            array[i][4] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_DAYLIGHT, now);
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
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
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
        return Utility.arrayEquals(this.eras, (Object)that.eras) && Utility.arrayEquals(this.eraNames, (Object)that.eraNames) && Utility.arrayEquals(this.months, (Object)that.months) && Utility.arrayEquals(this.shortMonths, (Object)that.shortMonths) && Utility.arrayEquals(this.narrowMonths, (Object)that.narrowMonths) && Utility.arrayEquals(this.standaloneMonths, (Object)that.standaloneMonths) && Utility.arrayEquals(this.standaloneShortMonths, (Object)that.standaloneShortMonths) && Utility.arrayEquals(this.standaloneNarrowMonths, (Object)that.standaloneNarrowMonths) && Utility.arrayEquals(this.weekdays, (Object)that.weekdays) && Utility.arrayEquals(this.shortWeekdays, (Object)that.shortWeekdays) && Utility.arrayEquals(this.shorterWeekdays, (Object)that.shorterWeekdays) && Utility.arrayEquals(this.narrowWeekdays, (Object)that.narrowWeekdays) && Utility.arrayEquals(this.standaloneWeekdays, (Object)that.standaloneWeekdays) && Utility.arrayEquals(this.standaloneShortWeekdays, (Object)that.standaloneShortWeekdays) && Utility.arrayEquals(this.standaloneShorterWeekdays, (Object)that.standaloneShorterWeekdays) && Utility.arrayEquals(this.standaloneNarrowWeekdays, (Object)that.standaloneNarrowWeekdays) && Utility.arrayEquals(this.ampms, (Object)that.ampms) && DateFormatSymbols.arrayOfArrayEquals(this.zoneStrings, that.zoneStrings) && this.requestedLocale.getDisplayName().equals(that.requestedLocale.getDisplayName()) && Utility.arrayEquals(this.localPatternChars, (Object)that.localPatternChars);
    }

    protected void initializeData(ULocale desiredLocale, String type) {
        String key = desiredLocale.getBaseName() + "+" + type;
        DateFormatSymbols dfs = DFSCACHE.get(key);
        if (dfs == null) {
            CalendarData calData = new CalendarData(desiredLocale, type);
            this.initializeData(desiredLocale, calData);
            if (this.getClass().getName().equals("com.ibm.icu.text.DateFormatSymbols")) {
                dfs = (DateFormatSymbols)this.clone();
                DFSCACHE.put(key, dfs);
            }
        } else {
            this.initializeData(dfs);
        }
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
        this.shortQuarters = dfs.shortQuarters;
        this.quarters = dfs.quarters;
        this.standaloneShortQuarters = dfs.standaloneShortQuarters;
        this.standaloneQuarters = dfs.standaloneQuarters;
        this.leapMonthPatterns = dfs.leapMonthPatterns;
        this.shortYearNames = dfs.shortYearNames;
        this.zoneStrings = dfs.zoneStrings;
        this.localPatternChars = dfs.localPatternChars;
        this.capitalization = dfs.capitalization;
        this.actualLocale = dfs.actualLocale;
        this.validLocale = dfs.validLocale;
        this.requestedLocale = dfs.requestedLocale;
    }

    protected void initializeData(ULocale desiredLocale, CalendarData calData) {
        CapitalizationContextUsage[] allUsages;
        this.eras = calData.getEras("abbreviated");
        this.eraNames = calData.getEras("wide");
        this.narrowEras = calData.getEras("narrow");
        this.months = calData.getStringArray("monthNames", "wide");
        this.shortMonths = calData.getStringArray("monthNames", "abbreviated");
        this.narrowMonths = calData.getStringArray("monthNames", "narrow");
        this.standaloneMonths = calData.getStringArray("monthNames", "stand-alone", "wide");
        this.standaloneShortMonths = calData.getStringArray("monthNames", "stand-alone", "abbreviated");
        this.standaloneNarrowMonths = calData.getStringArray("monthNames", "stand-alone", "narrow");
        String[] lWeekdays = calData.getStringArray("dayNames", "wide");
        this.weekdays = new String[8];
        this.weekdays[0] = "";
        System.arraycopy(lWeekdays, 0, this.weekdays, 1, lWeekdays.length);
        String[] aWeekdays = calData.getStringArray("dayNames", "abbreviated");
        this.shortWeekdays = new String[8];
        this.shortWeekdays[0] = "";
        System.arraycopy(aWeekdays, 0, this.shortWeekdays, 1, aWeekdays.length);
        String[] sWeekdays = calData.getStringArray("dayNames", "short");
        this.shorterWeekdays = new String[8];
        this.shorterWeekdays[0] = "";
        System.arraycopy(sWeekdays, 0, this.shorterWeekdays, 1, sWeekdays.length);
        String[] nWeekdays = null;
        try {
            nWeekdays = calData.getStringArray("dayNames", "narrow");
        }
        catch (MissingResourceException e) {
            try {
                nWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
            }
            catch (MissingResourceException e1) {
                nWeekdays = calData.getStringArray("dayNames", "abbreviated");
            }
        }
        this.narrowWeekdays = new String[8];
        this.narrowWeekdays[0] = "";
        System.arraycopy(nWeekdays, 0, this.narrowWeekdays, 1, nWeekdays.length);
        String[] swWeekdays = null;
        swWeekdays = calData.getStringArray("dayNames", "stand-alone", "wide");
        this.standaloneWeekdays = new String[8];
        this.standaloneWeekdays[0] = "";
        System.arraycopy(swWeekdays, 0, this.standaloneWeekdays, 1, swWeekdays.length);
        String[] saWeekdays = null;
        saWeekdays = calData.getStringArray("dayNames", "stand-alone", "abbreviated");
        this.standaloneShortWeekdays = new String[8];
        this.standaloneShortWeekdays[0] = "";
        System.arraycopy(saWeekdays, 0, this.standaloneShortWeekdays, 1, saWeekdays.length);
        String[] ssWeekdays = null;
        ssWeekdays = calData.getStringArray("dayNames", "stand-alone", "short");
        this.standaloneShorterWeekdays = new String[8];
        this.standaloneShorterWeekdays[0] = "";
        System.arraycopy(ssWeekdays, 0, this.standaloneShorterWeekdays, 1, ssWeekdays.length);
        String[] snWeekdays = null;
        snWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
        this.standaloneNarrowWeekdays = new String[8];
        this.standaloneNarrowWeekdays[0] = "";
        System.arraycopy(snWeekdays, 0, this.standaloneNarrowWeekdays, 1, snWeekdays.length);
        this.ampms = calData.getStringArray("AmPmMarkers");
        this.quarters = calData.getStringArray("quarters", "wide");
        this.shortQuarters = calData.getStringArray("quarters", "abbreviated");
        this.standaloneQuarters = calData.getStringArray("quarters", "stand-alone", "wide");
        this.standaloneShortQuarters = calData.getStringArray("quarters", "stand-alone", "abbreviated");
        ICUResourceBundle monthPatternsBundle = null;
        try {
            monthPatternsBundle = calData.get("monthPatterns");
        }
        catch (MissingResourceException e) {
            monthPatternsBundle = null;
        }
        if (monthPatternsBundle != null) {
            this.leapMonthPatterns = new String[7];
            this.leapMonthPatterns[0] = calData.get("monthPatterns", "wide").get("leap").getString();
            this.leapMonthPatterns[1] = calData.get("monthPatterns", "abbreviated").get("leap").getString();
            this.leapMonthPatterns[2] = calData.get("monthPatterns", "narrow").get("leap").getString();
            this.leapMonthPatterns[3] = calData.get("monthPatterns", "stand-alone", "wide").get("leap").getString();
            this.leapMonthPatterns[4] = calData.get("monthPatterns", "stand-alone", "abbreviated").get("leap").getString();
            this.leapMonthPatterns[5] = calData.get("monthPatterns", "stand-alone", "narrow").get("leap").getString();
            this.leapMonthPatterns[6] = calData.get("monthPatterns", "numeric", "all").get("leap").getString();
        }
        ICUResourceBundle cyclicNameSetsBundle = null;
        try {
            cyclicNameSetsBundle = calData.get("cyclicNameSets");
        }
        catch (MissingResourceException e) {
            cyclicNameSetsBundle = null;
        }
        if (cyclicNameSetsBundle != null) {
            this.shortYearNames = calData.get("cyclicNameSets", "years", "format", "abbreviated").getStringArray();
        }
        this.requestedLocale = desiredLocale;
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", desiredLocale);
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
        catch (MissingResourceException e) {
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
        for (int i = 0; i < aa1.length && (equal = Utility.arrayEquals(aa1[i], (Object)aa2[i])); ++i) {
        }
        return equal;
    }

    private final String[] duplicate(String[] srcArray) {
        return (String[])srcArray.clone();
    }

    private final String[][] duplicate(String[][] srcArray) {
        String[][] aCopy = new String[srcArray.length][];
        for (int i = 0; i < srcArray.length; ++i) {
            aCopy[i] = this.duplicate(srcArray[i]);
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
        this.initializeData(locale, new CalendarData((ICUResourceBundle)bundle, CalendarUtil.getCalendarType(locale)));
    }

    public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, Locale locale) throws MissingResourceException {
        return null;
    }

    public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, ULocale locale) throws MissingResourceException {
        return null;
    }

    public static ResourceBundle getDateFormatBundle(Calendar cal, Locale locale) throws MissingResourceException {
        return null;
    }

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
        DFSCACHE = new SimpleCache<String, DateFormatSymbols>();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
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

