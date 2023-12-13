/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.PatternTokenizer;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DateTimePatternGenerator
implements Freezable<DateTimePatternGenerator>,
Cloneable {
    private static final boolean DEBUG = false;
    private static final String[] LAST_RESORT_ALLOWED_HOUR_FORMAT = new String[]{"H"};
    static final Map<String, String[]> LOCALE_TO_ALLOWED_HOUR;
    public static final int ERA = 0;
    public static final int YEAR = 1;
    public static final int QUARTER = 2;
    public static final int MONTH = 3;
    public static final int WEEK_OF_YEAR = 4;
    public static final int WEEK_OF_MONTH = 5;
    public static final int WEEKDAY = 6;
    public static final int DAY = 7;
    public static final int DAY_OF_YEAR = 8;
    public static final int DAY_OF_WEEK_IN_MONTH = 9;
    public static final int DAYPERIOD = 10;
    public static final int HOUR = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int FRACTIONAL_SECOND = 14;
    public static final int ZONE = 15;
    @Deprecated
    public static final int TYPE_LIMIT = 16;
    private static final DisplayWidth APPENDITEM_WIDTH;
    private static final int APPENDITEM_WIDTH_INT;
    private static final DisplayWidth[] CLDR_FIELD_WIDTH;
    public static final int MATCH_NO_OPTIONS = 0;
    public static final int MATCH_HOUR_FIELD_LENGTH = 2048;
    @Deprecated
    public static final int MATCH_MINUTE_FIELD_LENGTH = 4096;
    @Deprecated
    public static final int MATCH_SECOND_FIELD_LENGTH = 8192;
    public static final int MATCH_ALL_FIELDS_LENGTH = 65535;
    private TreeMap<DateTimeMatcher, PatternWithSkeletonFlag> skeleton2pattern = new TreeMap();
    private TreeMap<String, PatternWithSkeletonFlag> basePattern_pattern = new TreeMap();
    private String decimal = "?";
    private String dateTimeFormat = "{1} {0}";
    private String[] appendItemFormats = new String[16];
    private String[][] fieldDisplayNames = new String[16][DisplayWidth.access$100()];
    private char defaultHourFormatChar = (char)72;
    private volatile boolean frozen = false;
    private transient DateTimeMatcher current = new DateTimeMatcher();
    private transient FormatParser fp = new FormatParser();
    private transient DistanceInfo _distanceInfo = new DistanceInfo();
    private String[] allowedHourFormats;
    private static final int FRACTIONAL_MASK = 16384;
    private static final int SECOND_AND_FRACTIONAL_MASK = 24576;
    private static ICUCache<String, DateTimePatternGenerator> DTPNG_CACHE;
    private static final String[] CLDR_FIELD_APPEND;
    private static final String[] CLDR_FIELD_NAME;
    private static final String[] FIELD_NAME;
    private static final String[] CANONICAL_ITEMS;
    private static final Set<String> CANONICAL_SET;
    private Set<String> cldrAvailableFormatKeys = new HashSet<String>(20);
    private static final int DATE_MASK = 1023;
    private static final int TIME_MASK = 64512;
    private static final int DELTA = 16;
    private static final int NUMERIC = 256;
    private static final int NONE = 0;
    private static final int NARROW = -257;
    private static final int SHORTER = -258;
    private static final int SHORT = -259;
    private static final int LONG = -260;
    private static final int EXTRA_FIELD = 65536;
    private static final int MISSING_FIELD = 4096;
    private static final int[][] types;

    public static DateTimePatternGenerator getEmptyInstance() {
        DateTimePatternGenerator instance = new DateTimePatternGenerator();
        instance.addCanonicalItems();
        instance.fillInMissing();
        return instance;
    }

    protected DateTimePatternGenerator() {
    }

    public static DateTimePatternGenerator getInstance() {
        return DateTimePatternGenerator.getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public static DateTimePatternGenerator getInstance(ULocale uLocale) {
        return DateTimePatternGenerator.getFrozenInstance(uLocale).cloneAsThawed();
    }

    public static DateTimePatternGenerator getInstance(Locale locale) {
        return DateTimePatternGenerator.getInstance(ULocale.forLocale(locale));
    }

    @Deprecated
    public static DateTimePatternGenerator getFrozenInstance(ULocale uLocale) {
        String localeKey = uLocale.toString();
        DateTimePatternGenerator result = DTPNG_CACHE.get(localeKey);
        if (result != null) {
            return result;
        }
        result = new DateTimePatternGenerator();
        result.initData(uLocale);
        result.freeze();
        DTPNG_CACHE.put(localeKey, result);
        return result;
    }

    private void initData(ULocale uLocale) {
        PatternInfo returnInfo = new PatternInfo();
        this.addCanonicalItems();
        this.addICUPatterns(returnInfo, uLocale);
        this.addCLDRData(returnInfo, uLocale);
        this.setDateTimeFromCalendar(uLocale);
        this.setDecimalSymbols(uLocale);
        this.getAllowedHourFormats(uLocale);
        this.fillInMissing();
    }

    private void addICUPatterns(PatternInfo returnInfo, ULocale uLocale) {
        for (int i2 = 0; i2 <= 3; ++i2) {
            SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateInstance(i2, uLocale);
            this.addPattern(df.toPattern(), false, returnInfo);
            df = (SimpleDateFormat)DateFormat.getTimeInstance(i2, uLocale);
            this.addPattern(df.toPattern(), false, returnInfo);
            if (i2 != 3) continue;
            this.consumeShortTimePattern(df.toPattern(), returnInfo);
        }
    }

    private String getCalendarTypeToUse(ULocale uLocale) {
        String calendarTypeToUse = uLocale.getKeywordValue("calendar");
        if (calendarTypeToUse == null) {
            String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", uLocale, true);
            calendarTypeToUse = preferredCalendarTypes[0];
        }
        if (calendarTypeToUse == null) {
            calendarTypeToUse = "gregorian";
        }
        return calendarTypeToUse;
    }

    private void consumeShortTimePattern(String shortTimePattern, PatternInfo returnInfo) {
        FormatParser fp = new FormatParser();
        fp.set(shortTimePattern);
        List<Object> items = fp.getItems();
        for (int idx = 0; idx < items.size(); ++idx) {
            VariableField fld;
            Object item = items.get(idx);
            if (!(item instanceof VariableField) || (fld = (VariableField)item).getType() != 11) continue;
            this.defaultHourFormatChar = fld.toString().charAt(0);
            break;
        }
        this.hackTimes(returnInfo, shortTimePattern);
    }

    private void fillInMissing() {
        for (int i2 = 0; i2 < 16; ++i2) {
            if (this.getAppendItemFormat(i2) == null) {
                this.setAppendItemFormat(i2, "{0} \u251c{2}: {1}\u2524");
            }
            if (this.getFieldDisplayName(i2, DisplayWidth.WIDE) == null) {
                this.setFieldDisplayName(i2, DisplayWidth.WIDE, "F" + i2);
            }
            if (this.getFieldDisplayName(i2, DisplayWidth.ABBREVIATED) == null) {
                this.setFieldDisplayName(i2, DisplayWidth.ABBREVIATED, this.getFieldDisplayName(i2, DisplayWidth.WIDE));
            }
            if (this.getFieldDisplayName(i2, DisplayWidth.NARROW) != null) continue;
            this.setFieldDisplayName(i2, DisplayWidth.NARROW, this.getFieldDisplayName(i2, DisplayWidth.ABBREVIATED));
        }
    }

    private void addCLDRData(PatternInfo returnInfo, ULocale uLocale) {
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", uLocale);
        String calendarTypeToUse = this.getCalendarTypeToUse(uLocale);
        AppendItemFormatsSink appendItemFormatsSink = new AppendItemFormatsSink();
        try {
            rb.getAllItemsWithFallback("calendar/" + calendarTypeToUse + "/appendItems", appendItemFormatsSink);
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        AppendItemNamesSink appendItemNamesSink = new AppendItemNamesSink();
        try {
            rb.getAllItemsWithFallback("fields", appendItemNamesSink);
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        AvailableFormatsSink availableFormatsSink = new AvailableFormatsSink(returnInfo);
        try {
            rb.getAllItemsWithFallback("calendar/" + calendarTypeToUse + "/availableFormats", availableFormatsSink);
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
    }

    private void setDateTimeFromCalendar(ULocale uLocale) {
        String dateTimeFormat = Calendar.getDateTimePattern(Calendar.getInstance(uLocale), uLocale, 2);
        this.setDateTimeFormat(dateTimeFormat);
    }

    private void setDecimalSymbols(ULocale uLocale) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(uLocale);
        this.setDecimal(String.valueOf(dfs.getDecimalSeparator()));
    }

    private void getAllowedHourFormats(ULocale uLocale) {
        String langCountry;
        String[] list;
        ULocale max = ULocale.addLikelySubtags(uLocale);
        String country = max.getCountry();
        if (country.isEmpty()) {
            country = "001";
        }
        if ((list = LOCALE_TO_ALLOWED_HOUR.get(langCountry = max.getLanguage() + "_" + country)) == null && (list = LOCALE_TO_ALLOWED_HOUR.get(country)) == null) {
            list = LAST_RESORT_ALLOWED_HOUR_FORMAT;
        }
        this.allowedHourFormats = list;
    }

    @Deprecated
    public char getDefaultHourFormatChar() {
        return this.defaultHourFormatChar;
    }

    @Deprecated
    public void setDefaultHourFormatChar(char defaultHourFormatChar) {
        this.defaultHourFormatChar = defaultHourFormatChar;
    }

    private void hackTimes(PatternInfo returnInfo, String shortTimePattern) {
        this.fp.set(shortTimePattern);
        StringBuilder mmss = new StringBuilder();
        boolean gotMm = false;
        for (int i2 = 0; i2 < this.fp.items.size(); ++i2) {
            Object item = this.fp.items.get(i2);
            if (item instanceof String) {
                if (!gotMm) continue;
                mmss.append(this.fp.quoteLiteral(item.toString()));
                continue;
            }
            char ch = item.toString().charAt(0);
            if (ch == 'm') {
                gotMm = true;
                mmss.append(item);
                continue;
            }
            if (ch == 's') {
                if (!gotMm) break;
                mmss.append(item);
                this.addPattern(mmss.toString(), false, returnInfo);
                break;
            }
            if (gotMm || ch == 'z' || ch == 'Z' || ch == 'v' || ch == 'V') break;
        }
        BitSet variables = new BitSet();
        BitSet nuke = new BitSet();
        for (int i3 = 0; i3 < this.fp.items.size(); ++i3) {
            Object item = this.fp.items.get(i3);
            if (!(item instanceof VariableField)) continue;
            variables.set(i3);
            char ch = item.toString().charAt(0);
            if (ch != 's' && ch != 'S') continue;
            nuke.set(i3);
            for (int j2 = i3 - 1; j2 >= 0 && !variables.get(j2); ++j2) {
                nuke.set(i3);
            }
        }
        String hhmm = DateTimePatternGenerator.getFilteredPattern(this.fp, nuke);
        this.addPattern(hhmm, false, returnInfo);
    }

    private static String getFilteredPattern(FormatParser fp, BitSet nuke) {
        StringBuilder result = new StringBuilder();
        for (int i2 = 0; i2 < fp.items.size(); ++i2) {
            if (nuke.get(i2)) continue;
            Object item = fp.items.get(i2);
            if (item instanceof String) {
                result.append(fp.quoteLiteral(item.toString()));
                continue;
            }
            result.append(item.toString());
        }
        return result.toString();
    }

    @Deprecated
    public static int getAppendFormatNumber(UResource.Key key) {
        for (int i2 = 0; i2 < CLDR_FIELD_APPEND.length; ++i2) {
            if (!key.contentEquals(CLDR_FIELD_APPEND[i2])) continue;
            return i2;
        }
        return -1;
    }

    @Deprecated
    public static int getAppendFormatNumber(String string) {
        for (int i2 = 0; i2 < CLDR_FIELD_APPEND.length; ++i2) {
            if (!CLDR_FIELD_APPEND[i2].equals(string)) continue;
            return i2;
        }
        return -1;
    }

    private static int getCLDRFieldAndWidthNumber(UResource.Key key) {
        for (int i2 = 0; i2 < CLDR_FIELD_NAME.length; ++i2) {
            for (int j2 = 0; j2 < DisplayWidth.COUNT; ++j2) {
                String fullKey = CLDR_FIELD_NAME[i2].concat(DateTimePatternGenerator.CLDR_FIELD_WIDTH[j2].cldrKey());
                if (!key.contentEquals(fullKey)) continue;
                return i2 * DisplayWidth.COUNT + j2;
            }
        }
        return -1;
    }

    public String getBestPattern(String skeleton) {
        return this.getBestPattern(skeleton, null, 0);
    }

    public String getBestPattern(String skeleton, int options) {
        return this.getBestPattern(skeleton, null, options);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String getBestPattern(String skeleton, DateTimeMatcher skipMatcher, int options) {
        String timePattern;
        String datePattern;
        EnumSet<DTPGflags> flags = EnumSet.noneOf(DTPGflags.class);
        String skeletonMapped = this.mapSkeletonMetacharacters(skeleton, flags);
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            this.current.set(skeletonMapped, this.fp, false);
            PatternWithMatcher bestWithMatcher = this.getBestRaw(this.current, -1, this._distanceInfo, skipMatcher);
            if (this._distanceInfo.missingFieldMask == 0 && this._distanceInfo.extraFieldMask == 0) {
                return this.adjustFieldTypes(bestWithMatcher, this.current, flags, options);
            }
            int neededFields = this.current.getFieldMask();
            datePattern = this.getBestAppending(this.current, neededFields & 0x3FF, this._distanceInfo, skipMatcher, flags, options);
            timePattern = this.getBestAppending(this.current, neededFields & 0xFC00, this._distanceInfo, skipMatcher, flags, options);
        }
        if (datePattern == null) {
            return timePattern == null ? "" : timePattern;
        }
        if (timePattern == null) {
            return datePattern;
        }
        return SimpleFormatterImpl.formatRawPattern(this.getDateTimeFormat(), 2, 2, timePattern, datePattern);
    }

    private String mapSkeletonMetacharacters(String skeleton, EnumSet<DTPGflags> flags) {
        StringBuilder skeletonCopy = new StringBuilder();
        boolean inQuoted = false;
        for (int patPos = 0; patPos < skeleton.length(); ++patPos) {
            char patChr = skeleton.charAt(patPos);
            if (patChr == '\'') {
                inQuoted = !inQuoted;
                continue;
            }
            if (inQuoted) continue;
            if (patChr == 'j' || patChr == 'C') {
                int extraLen = 0;
                while (patPos + 1 < skeleton.length() && skeleton.charAt(patPos + 1) == patChr) {
                    ++extraLen;
                    ++patPos;
                }
                int hourLen = 1 + (extraLen & 1);
                int dayPeriodLen = extraLen < 2 ? 1 : 3 + (extraLen >> 1);
                char hourChar = 'h';
                char dayPeriodChar = 'a';
                if (patChr == 'j') {
                    hourChar = this.defaultHourFormatChar;
                } else {
                    String preferred = this.allowedHourFormats[0];
                    hourChar = preferred.charAt(0);
                    char last = preferred.charAt(preferred.length() - 1);
                    if (last == 'b' || last == 'B') {
                        dayPeriodChar = last;
                    }
                }
                if (hourChar == 'H' || hourChar == 'k') {
                    dayPeriodLen = 0;
                }
                while (dayPeriodLen-- > 0) {
                    skeletonCopy.append(dayPeriodChar);
                }
                while (hourLen-- > 0) {
                    skeletonCopy.append(hourChar);
                }
                continue;
            }
            if (patChr == 'J') {
                skeletonCopy.append('H');
                flags.add(DTPGflags.SKELETON_USES_CAP_J);
                continue;
            }
            skeletonCopy.append(patChr);
        }
        return skeletonCopy.toString();
    }

    public DateTimePatternGenerator addPattern(String pattern, boolean override, PatternInfo returnInfo) {
        return this.addPatternWithSkeleton(pattern, null, override, returnInfo);
    }

    @Deprecated
    public DateTimePatternGenerator addPatternWithSkeleton(String pattern, String skeletonToUse, boolean override, PatternInfo returnInfo) {
        PatternWithSkeletonFlag previousValue;
        this.checkFrozen();
        DateTimeMatcher matcher = skeletonToUse == null ? new DateTimeMatcher().set(pattern, this.fp, false) : new DateTimeMatcher().set(skeletonToUse, this.fp, false);
        String basePattern = matcher.getBasePattern();
        PatternWithSkeletonFlag previousPatternWithSameBase = this.basePattern_pattern.get(basePattern);
        if (previousPatternWithSameBase != null && (!previousPatternWithSameBase.skeletonWasSpecified || skeletonToUse != null && !override)) {
            returnInfo.status = 1;
            returnInfo.conflictingPattern = previousPatternWithSameBase.pattern;
            if (!override) {
                return this;
            }
        }
        if ((previousValue = this.skeleton2pattern.get(matcher)) != null) {
            returnInfo.status = 2;
            returnInfo.conflictingPattern = previousValue.pattern;
            if (!override || skeletonToUse != null && previousValue.skeletonWasSpecified) {
                return this;
            }
        }
        returnInfo.status = 0;
        returnInfo.conflictingPattern = "";
        PatternWithSkeletonFlag patWithSkelFlag = new PatternWithSkeletonFlag(pattern, skeletonToUse != null);
        this.skeleton2pattern.put(matcher, patWithSkelFlag);
        this.basePattern_pattern.put(basePattern, patWithSkelFlag);
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getSkeleton(String pattern) {
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            this.current.set(pattern, this.fp, false);
            return this.current.toString();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public String getSkeletonAllowingDuplicates(String pattern) {
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            this.current.set(pattern, this.fp, true);
            return this.current.toString();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public String getCanonicalSkeletonAllowingDuplicates(String pattern) {
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            this.current.set(pattern, this.fp, true);
            return this.current.toCanonicalString();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getBaseSkeleton(String pattern) {
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            this.current.set(pattern, this.fp, false);
            return this.current.getBasePattern();
        }
    }

    public Map<String, String> getSkeletons(Map<String, String> result) {
        if (result == null) {
            result = new LinkedHashMap<String, String>();
        }
        for (DateTimeMatcher item : this.skeleton2pattern.keySet()) {
            PatternWithSkeletonFlag patternWithSkelFlag = this.skeleton2pattern.get(item);
            String pattern = patternWithSkelFlag.pattern;
            if (CANONICAL_SET.contains(pattern)) continue;
            result.put(item.toString(), pattern);
        }
        return result;
    }

    public Set<String> getBaseSkeletons(Set<String> result) {
        if (result == null) {
            result = new HashSet<String>();
        }
        result.addAll(this.basePattern_pattern.keySet());
        return result;
    }

    public String replaceFieldTypes(String pattern, String skeleton) {
        return this.replaceFieldTypes(pattern, skeleton, 0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String replaceFieldTypes(String pattern, String skeleton, int options) {
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            PatternWithMatcher patternNoMatcher = new PatternWithMatcher(pattern, null);
            return this.adjustFieldTypes(patternNoMatcher, this.current.set(skeleton, this.fp, false), EnumSet.noneOf(DTPGflags.class), options);
        }
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.checkFrozen();
        this.dateTimeFormat = dateTimeFormat;
    }

    public String getDateTimeFormat() {
        return this.dateTimeFormat;
    }

    public void setDecimal(String decimal) {
        this.checkFrozen();
        this.decimal = decimal;
    }

    public String getDecimal() {
        return this.decimal;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public Collection<String> getRedundants(Collection<String> output) {
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            if (output == null) {
                output = new LinkedHashSet<String>();
            }
            for (DateTimeMatcher cur : this.skeleton2pattern.keySet()) {
                String trial;
                PatternWithSkeletonFlag patternWithSkelFlag = this.skeleton2pattern.get(cur);
                String pattern = patternWithSkelFlag.pattern;
                if (CANONICAL_SET.contains(pattern) || !(trial = this.getBestPattern(cur.toString(), cur, 0)).equals(pattern)) continue;
                output.add(pattern);
            }
            return output;
        }
    }

    public void setAppendItemFormat(int field, String value) {
        this.checkFrozen();
        this.appendItemFormats[field] = value;
    }

    public String getAppendItemFormat(int field) {
        return this.appendItemFormats[field];
    }

    public void setAppendItemName(int field, String value) {
        this.setFieldDisplayName(field, APPENDITEM_WIDTH, value);
    }

    public String getAppendItemName(int field) {
        return this.getFieldDisplayName(field, APPENDITEM_WIDTH);
    }

    @Deprecated
    private void setFieldDisplayName(int field, DisplayWidth width, String value) {
        this.checkFrozen();
        if (field < 16 && field >= 0) {
            this.fieldDisplayNames[field][width.ordinal()] = value;
        }
    }

    public String getFieldDisplayName(int field, DisplayWidth width) {
        if (field >= 16 || field < 0) {
            return "";
        }
        return this.fieldDisplayNames[field][width.ordinal()];
    }

    @Deprecated
    public static boolean isSingleField(String skeleton) {
        char first = skeleton.charAt(0);
        for (int i2 = 1; i2 < skeleton.length(); ++i2) {
            if (skeleton.charAt(i2) == first) continue;
            return false;
        }
        return true;
    }

    private void setAvailableFormat(String key) {
        this.checkFrozen();
        this.cldrAvailableFormatKeys.add(key);
    }

    private boolean isAvailableFormatSet(String key) {
        return this.cldrAvailableFormatKeys.contains(key);
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public DateTimePatternGenerator freeze() {
        this.frozen = true;
        return this;
    }

    @Override
    public DateTimePatternGenerator cloneAsThawed() {
        DateTimePatternGenerator result = (DateTimePatternGenerator)this.clone();
        this.frozen = false;
        return result;
    }

    public Object clone() {
        try {
            DateTimePatternGenerator result = (DateTimePatternGenerator)super.clone();
            result.skeleton2pattern = (TreeMap)this.skeleton2pattern.clone();
            result.basePattern_pattern = (TreeMap)this.basePattern_pattern.clone();
            result.appendItemFormats = (String[])this.appendItemFormats.clone();
            result.fieldDisplayNames = (String[][])this.fieldDisplayNames.clone();
            result.current = new DateTimeMatcher();
            result.fp = new FormatParser();
            result._distanceInfo = new DistanceInfo();
            result.frozen = false;
            return result;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException("Internal Error", e2);
        }
    }

    @Deprecated
    public boolean skeletonsAreSimilar(String id, String skeleton) {
        if (id.equals(skeleton)) {
            return true;
        }
        TreeSet<String> parser1 = this.getSet(id);
        TreeSet<String> parser2 = this.getSet(skeleton);
        if (parser1.size() != parser2.size()) {
            return false;
        }
        Iterator<String> it2 = parser2.iterator();
        for (String item : parser1) {
            String item2;
            int index2;
            int index1 = DateTimePatternGenerator.getCanonicalIndex(item, false);
            if (types[index1][1] == types[index2 = DateTimePatternGenerator.getCanonicalIndex(item2 = it2.next(), false)][1]) continue;
            return false;
        }
        return true;
    }

    private TreeSet<String> getSet(String id) {
        List<Object> items = this.fp.set(id).getItems();
        TreeSet<String> result = new TreeSet<String>();
        for (Object obj : items) {
            String item = obj.toString();
            if (item.startsWith("G") || item.startsWith("a")) continue;
            result.add(item);
        }
        return result;
    }

    private void checkFrozen() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
    }

    private String getBestAppending(DateTimeMatcher source, int missingFields, DistanceInfo distInfo, DateTimeMatcher skipMatcher, EnumSet<DTPGflags> flags, int options) {
        String resultPattern = null;
        if (missingFields != 0) {
            PatternWithMatcher resultPatternWithMatcher = this.getBestRaw(source, missingFields, distInfo, skipMatcher);
            resultPattern = this.adjustFieldTypes(resultPatternWithMatcher, source, flags, options);
            while (distInfo.missingFieldMask != 0) {
                if ((distInfo.missingFieldMask & 0x6000) == 16384 && (missingFields & 0x6000) == 24576) {
                    resultPatternWithMatcher.pattern = resultPattern;
                    flags = EnumSet.copyOf(flags);
                    flags.add(DTPGflags.FIX_FRACTIONAL_SECONDS);
                    resultPattern = this.adjustFieldTypes(resultPatternWithMatcher, source, flags, options);
                    distInfo.missingFieldMask &= 0xFFFFBFFF;
                    continue;
                }
                int startingMask = distInfo.missingFieldMask;
                PatternWithMatcher tempWithMatcher = this.getBestRaw(source, distInfo.missingFieldMask, distInfo, skipMatcher);
                String temp = this.adjustFieldTypes(tempWithMatcher, source, flags, options);
                int foundMask = startingMask & ~distInfo.missingFieldMask;
                int topField = this.getTopBitNumber(foundMask);
                resultPattern = SimpleFormatterImpl.formatRawPattern(this.getAppendFormat(topField), 2, 3, resultPattern, temp, this.getAppendName(topField));
            }
        }
        return resultPattern;
    }

    private String getAppendName(int foundMask) {
        return "'" + this.fieldDisplayNames[foundMask][APPENDITEM_WIDTH_INT] + "'";
    }

    private String getAppendFormat(int foundMask) {
        return this.appendItemFormats[foundMask];
    }

    private int getTopBitNumber(int foundMask) {
        int i2 = 0;
        while (foundMask != 0) {
            foundMask >>>= 1;
            ++i2;
        }
        return i2 - 1;
    }

    private void addCanonicalItems() {
        PatternInfo patternInfo = new PatternInfo();
        for (int i2 = 0; i2 < CANONICAL_ITEMS.length; ++i2) {
            this.addPattern(String.valueOf(CANONICAL_ITEMS[i2]), false, patternInfo);
        }
    }

    private PatternWithMatcher getBestRaw(DateTimeMatcher source, int includeMask, DistanceInfo missingFields, DateTimeMatcher skipMatcher) {
        int bestDistance = Integer.MAX_VALUE;
        PatternWithMatcher bestPatternWithMatcher = new PatternWithMatcher("", null);
        DistanceInfo tempInfo = new DistanceInfo();
        for (DateTimeMatcher trial : this.skeleton2pattern.keySet()) {
            int distance;
            if (trial.equals(skipMatcher) || (distance = source.getDistance(trial, includeMask, tempInfo)) >= bestDistance) continue;
            bestDistance = distance;
            PatternWithSkeletonFlag patternWithSkelFlag = this.skeleton2pattern.get(trial);
            bestPatternWithMatcher.pattern = patternWithSkelFlag.pattern;
            bestPatternWithMatcher.matcherWithSkeleton = patternWithSkelFlag.skeletonWasSpecified ? trial : null;
            missingFields.setTo(tempInfo);
            if (distance != 0) continue;
            break;
        }
        return bestPatternWithMatcher;
    }

    private String adjustFieldTypes(PatternWithMatcher patternWithMatcher, DateTimeMatcher inputRequest, EnumSet<DTPGflags> flags, int options) {
        this.fp.set(patternWithMatcher.pattern);
        StringBuilder newPattern = new StringBuilder();
        for (Object item : this.fp.getItems()) {
            if (item instanceof String) {
                newPattern.append(this.fp.quoteLiteral((String)item));
                continue;
            }
            VariableField variableField = (VariableField)item;
            StringBuilder fieldBuilder = new StringBuilder(variableField.toString());
            int type = variableField.getType();
            if (flags.contains((Object)DTPGflags.FIX_FRACTIONAL_SECONDS) && type == 13) {
                fieldBuilder.append(this.decimal);
                inputRequest.original.appendFieldTo(14, fieldBuilder);
            } else if (inputRequest.type[type] != 0) {
                char c2;
                char reqFieldChar = inputRequest.original.getFieldChar(type);
                int reqFieldLen = inputRequest.original.getFieldLength(type);
                if (reqFieldChar == 'E' && reqFieldLen < 3) {
                    reqFieldLen = 3;
                }
                int adjFieldLen = reqFieldLen;
                DateTimeMatcher matcherWithSkeleton = patternWithMatcher.matcherWithSkeleton;
                if (type == 11 && (options & 0x800) == 0 || type == 12 && (options & 0x1000) == 0 || type == 13 && (options & 0x2000) == 0) {
                    adjFieldLen = fieldBuilder.length();
                } else if (matcherWithSkeleton != null) {
                    int skelFieldLen = matcherWithSkeleton.original.getFieldLength(type);
                    boolean patFieldIsNumeric = variableField.isNumeric();
                    boolean skelFieldIsNumeric = matcherWithSkeleton.fieldIsNumeric(type);
                    if (skelFieldLen == reqFieldLen || patFieldIsNumeric && !skelFieldIsNumeric || skelFieldIsNumeric && !patFieldIsNumeric) {
                        adjFieldLen = fieldBuilder.length();
                    }
                }
                char c3 = c2 = type != 11 && type != 3 && type != 6 && (type != 1 || reqFieldChar == 'Y') ? reqFieldChar : fieldBuilder.charAt(0);
                if (type == 11 && flags.contains((Object)DTPGflags.SKELETON_USES_CAP_J)) {
                    c2 = this.defaultHourFormatChar;
                }
                fieldBuilder = new StringBuilder();
                for (int i2 = adjFieldLen; i2 > 0; --i2) {
                    fieldBuilder.append(c2);
                }
            }
            newPattern.append((CharSequence)fieldBuilder);
        }
        return newPattern.toString();
    }

    @Deprecated
    public String getFields(String pattern) {
        this.fp.set(pattern);
        StringBuilder newPattern = new StringBuilder();
        for (Object item : this.fp.getItems()) {
            if (item instanceof String) {
                newPattern.append(this.fp.quoteLiteral((String)item));
                continue;
            }
            newPattern.append("{" + DateTimePatternGenerator.getName(item.toString()) + "}");
        }
        return newPattern.toString();
    }

    private static String showMask(int mask) {
        StringBuilder result = new StringBuilder();
        for (int i2 = 0; i2 < 16; ++i2) {
            if ((mask & 1 << i2) == 0) continue;
            if (result.length() != 0) {
                result.append(" | ");
            }
            result.append(FIELD_NAME[i2]);
            result.append(" ");
        }
        return result.toString();
    }

    private static String getName(String s2) {
        int i2 = DateTimePatternGenerator.getCanonicalIndex(s2, true);
        String name = FIELD_NAME[types[i2][1]];
        name = types[i2][2] < 0 ? name + ":S" : name + ":N";
        return name;
    }

    private static int getCanonicalIndex(String s2, boolean strict) {
        int len = s2.length();
        if (len == 0) {
            return -1;
        }
        char ch = s2.charAt(0);
        for (int i2 = 1; i2 < len; ++i2) {
            if (s2.charAt(i2) == ch) continue;
            return -1;
        }
        int bestRow = -1;
        for (int i3 = 0; i3 < types.length; ++i3) {
            int[] row = types[i3];
            if (row[0] != ch) continue;
            bestRow = i3;
            if (row[3] > len || row[row.length - 1] < len) continue;
            return i3;
        }
        return strict ? -1 : bestRow;
    }

    private static char getCanonicalChar(int field, char reference) {
        if (reference == 'h' || reference == 'K') {
            return 'h';
        }
        for (int i2 = 0; i2 < types.length; ++i2) {
            int[] row = types[i2];
            if (row[1] != field) continue;
            return (char)row[0];
        }
        throw new IllegalArgumentException("Could not find field " + field);
    }

    static {
        HashMap temp = new HashMap();
        ICUResourceBundle suppData = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        DayPeriodAllowedHoursSink allowedHoursSink = new DayPeriodAllowedHoursSink(temp);
        suppData.getAllItemsWithFallback("timeData", allowedHoursSink);
        LOCALE_TO_ALLOWED_HOUR = Collections.unmodifiableMap(temp);
        APPENDITEM_WIDTH = DisplayWidth.WIDE;
        APPENDITEM_WIDTH_INT = APPENDITEM_WIDTH.ordinal();
        CLDR_FIELD_WIDTH = DisplayWidth.values();
        DTPNG_CACHE = new SimpleCache<String, DateTimePatternGenerator>();
        CLDR_FIELD_APPEND = new String[]{"Era", "Year", "Quarter", "Month", "Week", "*", "Day-Of-Week", "Day", "*", "*", "*", "Hour", "Minute", "Second", "*", "Timezone"};
        CLDR_FIELD_NAME = new String[]{"era", "year", "quarter", "month", "week", "weekOfMonth", "weekday", "day", "dayOfYear", "weekdayOfMonth", "dayperiod", "hour", "minute", "second", "*", "zone"};
        FIELD_NAME = new String[]{"Era", "Year", "Quarter", "Month", "Week_in_Year", "Week_in_Month", "Weekday", "Day", "Day_Of_Year", "Day_of_Week_in_Month", "Dayperiod", "Hour", "Minute", "Second", "Fractional_Second", "Zone"};
        CANONICAL_ITEMS = new String[]{"G", "y", "Q", "M", "w", "W", "E", "d", "D", "F", "a", "H", "m", "s", "S", "v"};
        CANONICAL_SET = new HashSet<String>(Arrays.asList(CANONICAL_ITEMS));
        types = new int[][]{{71, 0, -259, 1, 3}, {71, 0, -260, 4}, {71, 0, -257, 5}, {121, 1, 256, 1, 20}, {89, 1, 272, 1, 20}, {117, 1, 288, 1, 20}, {114, 1, 304, 1, 20}, {85, 1, -259, 1, 3}, {85, 1, -260, 4}, {85, 1, -257, 5}, {81, 2, 256, 1, 2}, {81, 2, -259, 3}, {81, 2, -260, 4}, {81, 2, -257, 5}, {113, 2, 272, 1, 2}, {113, 2, -275, 3}, {113, 2, -276, 4}, {113, 2, -273, 5}, {77, 3, 256, 1, 2}, {77, 3, -259, 3}, {77, 3, -260, 4}, {77, 3, -257, 5}, {76, 3, 272, 1, 2}, {76, 3, -275, 3}, {76, 3, -276, 4}, {76, 3, -273, 5}, {108, 3, 272, 1, 1}, {119, 4, 256, 1, 2}, {87, 5, 256, 1}, {69, 6, -259, 1, 3}, {69, 6, -260, 4}, {69, 6, -257, 5}, {69, 6, -258, 6}, {99, 6, 288, 1, 2}, {99, 6, -291, 3}, {99, 6, -292, 4}, {99, 6, -289, 5}, {99, 6, -290, 6}, {101, 6, 272, 1, 2}, {101, 6, -275, 3}, {101, 6, -276, 4}, {101, 6, -273, 5}, {101, 6, -274, 6}, {100, 7, 256, 1, 2}, {103, 7, 272, 1, 20}, {68, 8, 256, 1, 3}, {70, 9, 256, 1}, {97, 10, -259, 1, 3}, {97, 10, -260, 4}, {97, 10, -257, 5}, {98, 10, -275, 1, 3}, {98, 10, -276, 4}, {98, 10, -273, 5}, {66, 10, -307, 1, 3}, {66, 10, -308, 4}, {66, 10, -305, 5}, {72, 11, 416, 1, 2}, {107, 11, 432, 1, 2}, {104, 11, 256, 1, 2}, {75, 11, 272, 1, 2}, {109, 12, 256, 1, 2}, {115, 13, 256, 1, 2}, {65, 13, 272, 1, 1000}, {83, 14, 256, 1, 1000}, {118, 15, -291, 1}, {118, 15, -292, 4}, {122, 15, -259, 1, 3}, {122, 15, -260, 4}, {90, 15, -273, 1, 3}, {90, 15, -276, 4}, {90, 15, -275, 5}, {79, 15, -275, 1}, {79, 15, -276, 4}, {86, 15, -275, 1}, {86, 15, -276, 2}, {86, 15, -277, 3}, {86, 15, -278, 4}, {88, 15, -273, 1}, {88, 15, -275, 2}, {88, 15, -276, 4}, {120, 15, -273, 1}, {120, 15, -275, 2}, {120, 15, -276, 4}};
    }

    private static class DistanceInfo {
        int missingFieldMask;
        int extraFieldMask;

        private DistanceInfo() {
        }

        void clear() {
            this.extraFieldMask = 0;
            this.missingFieldMask = 0;
        }

        void setTo(DistanceInfo other) {
            this.missingFieldMask = other.missingFieldMask;
            this.extraFieldMask = other.extraFieldMask;
        }

        void addMissing(int field) {
            this.missingFieldMask |= 1 << field;
        }

        void addExtra(int field) {
            this.extraFieldMask |= 1 << field;
        }

        public String toString() {
            return "missingFieldMask: " + DateTimePatternGenerator.showMask(this.missingFieldMask) + ", extraFieldMask: " + DateTimePatternGenerator.showMask(this.extraFieldMask);
        }
    }

    private static class DateTimeMatcher
    implements Comparable<DateTimeMatcher> {
        private int[] type = new int[16];
        private SkeletonFields original = new SkeletonFields();
        private SkeletonFields baseOriginal = new SkeletonFields();
        private boolean addedDefaultDayPeriod = false;

        private DateTimeMatcher() {
        }

        public boolean fieldIsNumeric(int field) {
            return this.type[field] > 0;
        }

        public String toString() {
            return this.original.toString(this.addedDefaultDayPeriod);
        }

        public String toCanonicalString() {
            return this.original.toCanonicalString(this.addedDefaultDayPeriod);
        }

        String getBasePattern() {
            return this.baseOriginal.toString(this.addedDefaultDayPeriod);
        }

        DateTimeMatcher set(String pattern, FormatParser fp, boolean allowDuplicateFields) {
            Arrays.fill(this.type, 0);
            this.original.clear();
            this.baseOriginal.clear();
            this.addedDefaultDayPeriod = false;
            fp.set(pattern);
            for (Object obj : fp.getItems()) {
                if (!(obj instanceof VariableField)) continue;
                VariableField item = (VariableField)obj;
                String value = item.toString();
                int canonicalIndex = item.getCanonicalIndex();
                int[] row = types[canonicalIndex];
                int field = row[1];
                if (!this.original.isFieldEmpty(field)) {
                    char ch1 = this.original.getFieldChar(field);
                    char ch2 = value.charAt(0);
                    if (allowDuplicateFields || ch1 == 'r' && ch2 == 'U' || ch1 == 'U' && ch2 == 'r') continue;
                    throw new IllegalArgumentException("Conflicting fields:\t" + ch1 + ", " + value + "\t in " + pattern);
                }
                this.original.populate(field, value);
                char repeatChar = (char)row[0];
                int repeatCount = row[3];
                if ("GEzvQ".indexOf(repeatChar) >= 0) {
                    repeatCount = 1;
                }
                this.baseOriginal.populate(field, repeatChar, repeatCount);
                int subField = row[2];
                if (subField > 0) {
                    subField += value.length();
                }
                this.type[field] = subField;
            }
            if (!this.original.isFieldEmpty(11)) {
                if (this.original.getFieldChar(11) == 'h' || this.original.getFieldChar(11) == 'K') {
                    if (this.original.isFieldEmpty(10)) {
                        for (int i2 = 0; i2 < types.length; ++i2) {
                            int[] row = types[i2];
                            if (row[1] != 10) continue;
                            this.original.populate(10, (char)row[0], row[3]);
                            this.baseOriginal.populate(10, (char)row[0], row[3]);
                            this.type[10] = row[2];
                            this.addedDefaultDayPeriod = true;
                            break;
                        }
                    }
                } else if (!this.original.isFieldEmpty(10)) {
                    this.original.clearField(10);
                    this.baseOriginal.clearField(10);
                    this.type[10] = 0;
                }
            }
            return this;
        }

        int getFieldMask() {
            int result = 0;
            for (int i2 = 0; i2 < this.type.length; ++i2) {
                if (this.type[i2] == 0) continue;
                result |= 1 << i2;
            }
            return result;
        }

        void extractFrom(DateTimeMatcher source, int fieldMask) {
            for (int i2 = 0; i2 < this.type.length; ++i2) {
                if ((fieldMask & 1 << i2) != 0) {
                    this.type[i2] = source.type[i2];
                    this.original.copyFieldFrom(source.original, i2);
                    continue;
                }
                this.type[i2] = 0;
                this.original.clearField(i2);
            }
        }

        int getDistance(DateTimeMatcher other, int includeMask, DistanceInfo distanceInfo) {
            int result = 0;
            distanceInfo.clear();
            for (int i2 = 0; i2 < 16; ++i2) {
                int otherType;
                int myType = (includeMask & 1 << i2) == 0 ? 0 : this.type[i2];
                if (myType == (otherType = other.type[i2])) continue;
                if (myType == 0) {
                    result += 65536;
                    distanceInfo.addExtra(i2);
                    continue;
                }
                if (otherType == 0) {
                    result += 4096;
                    distanceInfo.addMissing(i2);
                    continue;
                }
                result += Math.abs(myType - otherType);
            }
            return result;
        }

        @Override
        public int compareTo(DateTimeMatcher that) {
            int result = this.original.compareTo(that.original);
            return result > 0 ? -1 : (result < 0 ? 1 : 0);
        }

        public boolean equals(Object other) {
            return this == other || other != null && other instanceof DateTimeMatcher && this.original.equals(((DateTimeMatcher)other).original);
        }

        public int hashCode() {
            return this.original.hashCode();
        }
    }

    private static class SkeletonFields {
        private byte[] chars = new byte[16];
        private byte[] lengths = new byte[16];
        private static final byte DEFAULT_CHAR = 0;
        private static final byte DEFAULT_LENGTH = 0;

        private SkeletonFields() {
        }

        public void clear() {
            Arrays.fill(this.chars, (byte)0);
            Arrays.fill(this.lengths, (byte)0);
        }

        void copyFieldFrom(SkeletonFields other, int field) {
            this.chars[field] = other.chars[field];
            this.lengths[field] = other.lengths[field];
        }

        void clearField(int field) {
            this.chars[field] = 0;
            this.lengths[field] = 0;
        }

        char getFieldChar(int field) {
            return (char)this.chars[field];
        }

        int getFieldLength(int field) {
            return this.lengths[field];
        }

        void populate(int field, String value) {
            for (char ch : value.toCharArray()) {
                assert (ch == value.charAt(0));
            }
            this.populate(field, value.charAt(0), value.length());
        }

        void populate(int field, char ch, int length) {
            assert (ch <= '\u007f');
            assert (length <= 127);
            this.chars[field] = (byte)ch;
            this.lengths[field] = (byte)length;
        }

        public boolean isFieldEmpty(int field) {
            return this.lengths[field] == 0;
        }

        public String toString() {
            return this.appendTo(new StringBuilder(), false, false).toString();
        }

        public String toString(boolean skipDayPeriod) {
            return this.appendTo(new StringBuilder(), false, skipDayPeriod).toString();
        }

        public String toCanonicalString() {
            return this.appendTo(new StringBuilder(), true, false).toString();
        }

        public String toCanonicalString(boolean skipDayPeriod) {
            return this.appendTo(new StringBuilder(), true, skipDayPeriod).toString();
        }

        public StringBuilder appendTo(StringBuilder sb) {
            return this.appendTo(sb, false, false);
        }

        private StringBuilder appendTo(StringBuilder sb, boolean canonical, boolean skipDayPeriod) {
            for (int i2 = 0; i2 < 16; ++i2) {
                if (skipDayPeriod && i2 == 10) continue;
                this.appendFieldTo(i2, sb, canonical);
            }
            return sb;
        }

        public StringBuilder appendFieldTo(int field, StringBuilder sb) {
            return this.appendFieldTo(field, sb, false);
        }

        private StringBuilder appendFieldTo(int field, StringBuilder sb, boolean canonical) {
            char ch = (char)this.chars[field];
            int length = this.lengths[field];
            if (canonical) {
                ch = DateTimePatternGenerator.getCanonicalChar(field, ch);
            }
            for (int i2 = 0; i2 < length; ++i2) {
                sb.append(ch);
            }
            return sb;
        }

        public int compareTo(SkeletonFields other) {
            for (int i2 = 0; i2 < 16; ++i2) {
                int charDiff = this.chars[i2] - other.chars[i2];
                if (charDiff != 0) {
                    return charDiff;
                }
                int lengthDiff = this.lengths[i2] - other.lengths[i2];
                if (lengthDiff == 0) continue;
                return lengthDiff;
            }
            return 0;
        }

        public boolean equals(Object other) {
            return this == other || other != null && other instanceof SkeletonFields && this.compareTo((SkeletonFields)other) == 0;
        }

        public int hashCode() {
            return Arrays.hashCode(this.chars) ^ Arrays.hashCode(this.lengths);
        }
    }

    private static enum DTPGflags {
        FIX_FRACTIONAL_SECONDS,
        SKELETON_USES_CAP_J;

    }

    private static class PatternWithSkeletonFlag {
        public String pattern;
        public boolean skeletonWasSpecified;

        public PatternWithSkeletonFlag(String pat, boolean skelSpecified) {
            this.pattern = pat;
            this.skeletonWasSpecified = skelSpecified;
        }

        public String toString() {
            return this.pattern + "," + this.skeletonWasSpecified;
        }
    }

    private static class PatternWithMatcher {
        public String pattern;
        public DateTimeMatcher matcherWithSkeleton;

        public PatternWithMatcher(String pat, DateTimeMatcher matcher) {
            this.pattern = pat;
            this.matcherWithSkeleton = matcher;
        }
    }

    @Deprecated
    public static class FormatParser {
        private static final UnicodeSet SYNTAX_CHARS = new UnicodeSet("[a-zA-Z]").freeze();
        private static final UnicodeSet QUOTING_CHARS = new UnicodeSet("[[[:script=Latn:][:script=Cyrl:]]&[[:L:][:M:]]]").freeze();
        private transient PatternTokenizer tokenizer = new PatternTokenizer().setSyntaxCharacters(SYNTAX_CHARS).setExtraQuotingCharacters(QUOTING_CHARS).setUsingQuote(true);
        private List<Object> items = new ArrayList<Object>();

        @Deprecated
        public FormatParser() {
        }

        @Deprecated
        public final FormatParser set(String string) {
            return this.set(string, false);
        }

        @Deprecated
        public FormatParser set(String string, boolean strict) {
            this.items.clear();
            if (string.length() == 0) {
                return this;
            }
            this.tokenizer.setPattern(string);
            StringBuffer buffer = new StringBuffer();
            StringBuffer variable = new StringBuffer();
            while (true) {
                buffer.setLength(0);
                int status = this.tokenizer.next(buffer);
                if (status == 0) break;
                if (status == 1) {
                    if (variable.length() != 0 && buffer.charAt(0) != variable.charAt(0)) {
                        this.addVariable(variable, false);
                    }
                    variable.append(buffer);
                    continue;
                }
                this.addVariable(variable, false);
                this.items.add(buffer.toString());
            }
            this.addVariable(variable, false);
            return this;
        }

        private void addVariable(StringBuffer variable, boolean strict) {
            if (variable.length() != 0) {
                this.items.add(new VariableField(variable.toString(), strict));
                variable.setLength(0);
            }
        }

        @Deprecated
        public List<Object> getItems() {
            return this.items;
        }

        @Deprecated
        public String toString() {
            return this.toString(0, this.items.size());
        }

        @Deprecated
        public String toString(int start, int limit) {
            StringBuilder result = new StringBuilder();
            for (int i2 = start; i2 < limit; ++i2) {
                Object item = this.items.get(i2);
                if (item instanceof String) {
                    String itemString = (String)item;
                    result.append(this.tokenizer.quoteLiteral(itemString));
                    continue;
                }
                result.append(this.items.get(i2).toString());
            }
            return result.toString();
        }

        @Deprecated
        public boolean hasDateAndTimeFields() {
            int foundMask = 0;
            for (Object item : this.items) {
                if (!(item instanceof VariableField)) continue;
                int type = ((VariableField)item).getType();
                foundMask |= 1 << type;
            }
            boolean isDate = (foundMask & 0x3FF) != 0;
            boolean isTime = (foundMask & 0xFC00) != 0;
            return isDate && isTime;
        }

        @Deprecated
        public Object quoteLiteral(String string) {
            return this.tokenizer.quoteLiteral(string);
        }
    }

    @Deprecated
    public static class VariableField {
        private final String string;
        private final int canonicalIndex;

        @Deprecated
        public VariableField(String string) {
            this(string, false);
        }

        @Deprecated
        public VariableField(String string, boolean strict) {
            this.canonicalIndex = DateTimePatternGenerator.getCanonicalIndex(string, strict);
            if (this.canonicalIndex < 0) {
                throw new IllegalArgumentException("Illegal datetime field:\t" + string);
            }
            this.string = string;
        }

        @Deprecated
        public int getType() {
            return types[this.canonicalIndex][1];
        }

        @Deprecated
        public static String getCanonicalCode(int type) {
            try {
                return CANONICAL_ITEMS[type];
            }
            catch (Exception e2) {
                return String.valueOf(type);
            }
        }

        @Deprecated
        public boolean isNumeric() {
            return types[this.canonicalIndex][2] > 0;
        }

        private int getCanonicalIndex() {
            return this.canonicalIndex;
        }

        @Deprecated
        public String toString() {
            return this.string;
        }
    }

    public static enum DisplayWidth {
        WIDE(""),
        ABBREVIATED("-short"),
        NARROW("-narrow");

        @Deprecated
        private static int COUNT;
        private final String cldrKey;

        private DisplayWidth(String cldrKey) {
            this.cldrKey = cldrKey;
        }

        private String cldrKey() {
            return this.cldrKey;
        }

        static {
            COUNT = DisplayWidth.values().length;
        }
    }

    public static final class PatternInfo {
        public static final int OK = 0;
        public static final int BASE_CONFLICT = 1;
        public static final int CONFLICT = 2;
        public int status;
        public String conflictingPattern;
    }

    private static class DayPeriodAllowedHoursSink
    extends UResource.Sink {
        HashMap<String, String[]> tempMap;

        private DayPeriodAllowedHoursSink(HashMap<String, String[]> tempMap) {
            this.tempMap = tempMap;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table timeData = value.getTable();
            int i2 = 0;
            while (timeData.getKeyAndValue(i2, key, value)) {
                String regionOrLocale = key.toString();
                UResource.Table formatList = value.getTable();
                int j2 = 0;
                while (formatList.getKeyAndValue(j2, key, value)) {
                    if (key.contentEquals("allowed")) {
                        this.tempMap.put(regionOrLocale, value.getStringArrayOrStringAsArray());
                    }
                    ++j2;
                }
                ++i2;
            }
        }
    }

    private class AvailableFormatsSink
    extends UResource.Sink {
        PatternInfo returnInfo;

        public AvailableFormatsSink(PatternInfo returnInfo) {
            this.returnInfo = returnInfo;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean isRoot) {
            UResource.Table formatsTable = value.getTable();
            int i2 = 0;
            while (formatsTable.getKeyAndValue(i2, key, value)) {
                String formatKey = key.toString();
                if (!DateTimePatternGenerator.this.isAvailableFormatSet(formatKey)) {
                    DateTimePatternGenerator.this.setAvailableFormat(formatKey);
                    String formatValue = value.toString();
                    DateTimePatternGenerator.this.addPatternWithSkeleton(formatValue, formatKey, !isRoot, this.returnInfo);
                }
                ++i2;
            }
        }
    }

    private class AppendItemNamesSink
    extends UResource.Sink {
        private AppendItemNamesSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table itemsTable = value.getTable();
            int i2 = 0;
            while (itemsTable.getKeyAndValue(i2, key, value)) {
                int fieldAndWidth;
                if (value.getType() == 2 && (fieldAndWidth = DateTimePatternGenerator.getCLDRFieldAndWidthNumber(key)) != -1) {
                    int field = fieldAndWidth / DisplayWidth.COUNT;
                    DisplayWidth width = CLDR_FIELD_WIDTH[fieldAndWidth % DisplayWidth.COUNT];
                    UResource.Table detailsTable = value.getTable();
                    int j2 = 0;
                    while (detailsTable.getKeyAndValue(j2, key, value)) {
                        if (key.contentEquals("dn")) {
                            if (DateTimePatternGenerator.this.getFieldDisplayName(field, width) != null) break;
                            DateTimePatternGenerator.this.setFieldDisplayName(field, width, value.toString());
                            break;
                        }
                        ++j2;
                    }
                }
                ++i2;
            }
        }
    }

    private class AppendItemFormatsSink
    extends UResource.Sink {
        private AppendItemFormatsSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table itemsTable = value.getTable();
            int i2 = 0;
            while (itemsTable.getKeyAndValue(i2, key, value)) {
                int field = DateTimePatternGenerator.getAppendFormatNumber(key);
                assert (field != -1);
                if (DateTimePatternGenerator.this.getAppendItemFormat(field) == null) {
                    DateTimePatternGenerator.this.setAppendItemFormat(field, value.toString());
                }
                ++i2;
            }
        }
    }
}

