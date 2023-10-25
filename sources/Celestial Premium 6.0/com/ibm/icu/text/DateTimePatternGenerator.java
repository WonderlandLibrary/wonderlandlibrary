/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.PatternTokenizer;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DateTimePatternGenerator
implements Freezable<DateTimePatternGenerator>,
Cloneable {
    private static final boolean DEBUG = false;
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
    public static final int TYPE_LIMIT = 16;
    public static final int MATCH_NO_OPTIONS = 0;
    public static final int MATCH_HOUR_FIELD_LENGTH = 2048;
    public static final int MATCH_MINUTE_FIELD_LENGTH = 4096;
    public static final int MATCH_SECOND_FIELD_LENGTH = 8192;
    public static final int MATCH_ALL_FIELDS_LENGTH = 65535;
    private TreeMap<DateTimeMatcher, PatternWithSkeletonFlag> skeleton2pattern = new TreeMap();
    private TreeMap<String, PatternWithSkeletonFlag> basePattern_pattern = new TreeMap();
    private String decimal = "?";
    private String dateTimeFormat = "{1} {0}";
    private String[] appendItemFormats = new String[16];
    private String[] appendItemNames = new String[16];
    private char defaultHourFormatChar;
    private boolean frozen;
    private transient DateTimeMatcher current;
    private transient FormatParser fp;
    private transient DistanceInfo _distanceInfo;
    private static final int FRACTIONAL_MASK = 16384;
    private static final int SECOND_AND_FRACTIONAL_MASK = 24576;
    private static ICUCache<String, DateTimePatternGenerator> DTPNG_CACHE = new SimpleCache<String, DateTimePatternGenerator>();
    private static final String[] CLDR_FIELD_APPEND = new String[]{"Era", "Year", "Quarter", "Month", "Week", "*", "Day-Of-Week", "Day", "*", "*", "*", "Hour", "Minute", "Second", "*", "Timezone"};
    private static final String[] CLDR_FIELD_NAME = new String[]{"era", "year", "*", "month", "week", "*", "weekday", "day", "*", "*", "dayperiod", "hour", "minute", "second", "*", "zone"};
    private static final String[] FIELD_NAME = new String[]{"Era", "Year", "Quarter", "Month", "Week_in_Year", "Week_in_Month", "Weekday", "Day", "Day_Of_Year", "Day_of_Week_in_Month", "Dayperiod", "Hour", "Minute", "Second", "Fractional_Second", "Zone"};
    private static final String[] CANONICAL_ITEMS = new String[]{"G", "y", "Q", "M", "w", "W", "E", "d", "D", "F", "H", "m", "s", "S", "v"};
    private static final Set<String> CANONICAL_SET = new HashSet<String>(Arrays.asList(CANONICAL_ITEMS));
    private Set<String> cldrAvailableFormatKeys;
    private static final int DATE_MASK = 1023;
    private static final int TIME_MASK = 64512;
    private static final int DELTA = 16;
    private static final int NUMERIC = 256;
    private static final int NONE = 0;
    private static final int NARROW = -257;
    private static final int SHORT = -258;
    private static final int LONG = -259;
    private static final int EXTRA_FIELD = 65536;
    private static final int MISSING_FIELD = 4096;
    private static final int[][] types = new int[][]{{71, 0, -258, 1, 3}, {71, 0, -259, 4}, {121, 1, 256, 1, 20}, {89, 1, 272, 1, 20}, {117, 1, 288, 1, 20}, {85, 1, -258, 1, 3}, {85, 1, -259, 4}, {85, 1, -257, 5}, {81, 2, 256, 1, 2}, {81, 2, -258, 3}, {81, 2, -259, 4}, {113, 2, 272, 1, 2}, {113, 2, -242, 3}, {113, 2, -243, 4}, {77, 3, 256, 1, 2}, {77, 3, -258, 3}, {77, 3, -259, 4}, {77, 3, -257, 5}, {76, 3, 272, 1, 2}, {76, 3, -274, 3}, {76, 3, -275, 4}, {76, 3, -273, 5}, {108, 3, 272, 1, 1}, {119, 4, 256, 1, 2}, {87, 5, 272, 1}, {69, 6, -258, 1, 3}, {69, 6, -259, 4}, {69, 6, -257, 5}, {99, 6, 288, 1, 2}, {99, 6, -290, 3}, {99, 6, -291, 4}, {99, 6, -289, 5}, {101, 6, 272, 1, 2}, {101, 6, -274, 3}, {101, 6, -275, 4}, {101, 6, -273, 5}, {100, 7, 256, 1, 2}, {68, 8, 272, 1, 3}, {70, 9, 288, 1}, {103, 7, 304, 1, 20}, {97, 10, -258, 1}, {72, 11, 416, 1, 2}, {107, 11, 432, 1, 2}, {104, 11, 256, 1, 2}, {75, 11, 272, 1, 2}, {109, 12, 256, 1, 2}, {115, 13, 256, 1, 2}, {83, 14, 272, 1, 1000}, {65, 13, 288, 1, 1000}, {118, 15, -290, 1}, {118, 15, -291, 4}, {122, 15, -258, 1, 3}, {122, 15, -259, 4}, {90, 15, -274, 1, 3}, {90, 15, -275, 4}, {86, 15, -274, 1, 3}, {86, 15, -275, 4}};

    public static DateTimePatternGenerator getEmptyInstance() {
        return new DateTimePatternGenerator();
    }

    protected DateTimePatternGenerator() {
        for (int i = 0; i < 16; ++i) {
            this.appendItemFormats[i] = "{0} \u251c{2}: {1}\u2524";
            this.appendItemNames[i] = "F" + i;
        }
        this.defaultHourFormatChar = (char)72;
        this.frozen = false;
        this.current = new DateTimeMatcher();
        this.fp = new FormatParser();
        this._distanceInfo = new DistanceInfo();
        this.complete();
        this.cldrAvailableFormatKeys = new HashSet<String>(20);
    }

    public static DateTimePatternGenerator getInstance() {
        return DateTimePatternGenerator.getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public static DateTimePatternGenerator getInstance(ULocale uLocale) {
        return DateTimePatternGenerator.getFrozenInstance(uLocale).cloneAsThawed();
    }

    public static DateTimePatternGenerator getFrozenInstance(ULocale uLocale) {
        String value;
        ICUResourceBundle itemBundle;
        String localeKey = uLocale.toString();
        DateTimePatternGenerator result = DTPNG_CACHE.get(localeKey);
        if (result != null) {
            return result;
        }
        result = new DateTimePatternGenerator();
        PatternInfo returnInfo = new PatternInfo();
        String shortTimePattern = null;
        block8: for (int i = 0; i <= 3; ++i) {
            SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateInstance(i, uLocale);
            result.addPattern(df.toPattern(), false, returnInfo);
            df = (SimpleDateFormat)DateFormat.getTimeInstance(i, uLocale);
            result.addPattern(df.toPattern(), false, returnInfo);
            if (i != 3) continue;
            shortTimePattern = df.toPattern();
            FormatParser fp = new FormatParser();
            fp.set(shortTimePattern);
            List<Object> items = fp.getItems();
            for (int idx = 0; idx < items.size(); ++idx) {
                VariableField fld;
                Object item = items.get(idx);
                if (!(item instanceof VariableField) || (fld = (VariableField)item).getType() != 11) continue;
                result.defaultHourFormatChar = fld.toString().charAt(0);
                continue block8;
            }
        }
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", uLocale);
        String calendarTypeToUse = uLocale.getKeywordValue("calendar");
        if (calendarTypeToUse == null) {
            String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", uLocale, true);
            calendarTypeToUse = preferredCalendarTypes[0];
        }
        if (calendarTypeToUse == null) {
            calendarTypeToUse = "gregorian";
        }
        try {
            itemBundle = rb.getWithFallback("calendar/" + calendarTypeToUse + "/appendItems");
            for (int i = 0; i < itemBundle.getSize(); ++i) {
                ICUResourceBundle formatBundle = (ICUResourceBundle)itemBundle.get(i);
                String formatName = itemBundle.get(i).getKey();
                value = formatBundle.getString();
                result.setAppendItemFormat(DateTimePatternGenerator.getAppendFormatNumber(formatName), value);
            }
        }
        catch (MissingResourceException e) {
            // empty catch block
        }
        try {
            itemBundle = rb.getWithFallback("fields");
            for (int i = 0; i < 16; ++i) {
                if (!DateTimePatternGenerator.isCLDRFieldName(i)) continue;
                ICUResourceBundle fieldBundle = itemBundle.getWithFallback(CLDR_FIELD_NAME[i]);
                ICUResourceBundle dnBundle = fieldBundle.getWithFallback("dn");
                value = dnBundle.getString();
                result.setAppendItemName(i, value);
            }
        }
        catch (MissingResourceException e) {
            // empty catch block
        }
        UResourceBundle availFormatsBundle = null;
        try {
            availFormatsBundle = rb.getWithFallback("calendar/" + calendarTypeToUse + "/availableFormats");
        }
        catch (MissingResourceException e) {
            // empty catch block
        }
        boolean override = true;
        while (availFormatsBundle != null) {
            for (int i = 0; i < availFormatsBundle.getSize(); ++i) {
                String formatKey = availFormatsBundle.get(i).getKey();
                if (result.isAvailableFormatSet(formatKey)) continue;
                result.setAvailableFormat(formatKey);
                String formatValue = availFormatsBundle.get(i).getString();
                result.addPatternWithSkeleton(formatValue, formatKey, override, returnInfo);
            }
            ICUResourceBundle pbundle = (ICUResourceBundle)((ICUResourceBundle)availFormatsBundle).getParent();
            if (pbundle == null) break;
            try {
                availFormatsBundle = pbundle.getWithFallback("calendar/" + calendarTypeToUse + "/availableFormats");
            }
            catch (MissingResourceException e) {
                availFormatsBundle = null;
            }
            if (availFormatsBundle == null || !pbundle.getULocale().getBaseName().equals("root")) continue;
            override = false;
        }
        if (shortTimePattern != null) {
            DateTimePatternGenerator.hackTimes(result, returnInfo, shortTimePattern);
        }
        result.setDateTimeFormat(Calendar.getDateTimePattern(Calendar.getInstance(uLocale), uLocale, 2));
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(uLocale);
        result.setDecimal(String.valueOf(dfs.getDecimalSeparator()));
        result.freeze();
        DTPNG_CACHE.put(localeKey, result);
        return result;
    }

    public char getDefaultHourFormatChar() {
        return this.defaultHourFormatChar;
    }

    public void setDefaultHourFormatChar(char defaultHourFormatChar) {
        this.defaultHourFormatChar = defaultHourFormatChar;
    }

    private static void hackTimes(DateTimePatternGenerator result, PatternInfo returnInfo, String hackPattern) {
        result.fp.set(hackPattern);
        StringBuilder mmss = new StringBuilder();
        boolean gotMm = false;
        for (int i = 0; i < result.fp.items.size(); ++i) {
            Object item = result.fp.items.get(i);
            if (item instanceof String) {
                if (!gotMm) continue;
                mmss.append(result.fp.quoteLiteral(item.toString()));
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
                result.addPattern(mmss.toString(), false, returnInfo);
                break;
            }
            if (gotMm || ch == 'z' || ch == 'Z' || ch == 'v' || ch == 'V') break;
        }
        BitSet variables = new BitSet();
        BitSet nuke = new BitSet();
        for (int i = 0; i < result.fp.items.size(); ++i) {
            Object item = result.fp.items.get(i);
            if (!(item instanceof VariableField)) continue;
            variables.set(i);
            char ch = item.toString().charAt(0);
            if (ch != 's' && ch != 'S') continue;
            nuke.set(i);
            for (int j = i - 1; j >= 0 && !variables.get(j); ++j) {
                nuke.set(i);
            }
        }
        String hhmm = DateTimePatternGenerator.getFilteredPattern(result.fp, nuke);
        result.addPattern(hhmm, false, returnInfo);
    }

    private static String getFilteredPattern(FormatParser fp, BitSet nuke) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fp.items.size(); ++i) {
            if (nuke.get(i)) continue;
            Object item = fp.items.get(i);
            if (item instanceof String) {
                result.append(fp.quoteLiteral(item.toString()));
                continue;
            }
            result.append(item.toString());
        }
        return result.toString();
    }

    public static int getAppendFormatNumber(String string) {
        for (int i = 0; i < CLDR_FIELD_APPEND.length; ++i) {
            if (!CLDR_FIELD_APPEND[i].equals(string)) continue;
            return i;
        }
        return -1;
    }

    private static boolean isCLDRFieldName(int index) {
        if (index < 0 && index >= 16) {
            return false;
        }
        return CLDR_FIELD_NAME[index].charAt(0) != '*';
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
        skeleton = skeleton.replaceAll("j", String.valueOf(this.defaultHourFormatChar));
        DateTimePatternGenerator dateTimePatternGenerator = this;
        synchronized (dateTimePatternGenerator) {
            this.current.set(skeleton, this.fp, false);
            PatternWithMatcher bestWithMatcher = this.getBestRaw(this.current, -1, this._distanceInfo, skipMatcher);
            if (this._distanceInfo.missingFieldMask == 0 && this._distanceInfo.extraFieldMask == 0) {
                return this.adjustFieldTypes(bestWithMatcher, this.current, false, options);
            }
            int neededFields = this.current.getFieldMask();
            datePattern = this.getBestAppending(this.current, neededFields & 0x3FF, this._distanceInfo, skipMatcher, options);
            timePattern = this.getBestAppending(this.current, neededFields & 0xFC00, this._distanceInfo, skipMatcher, options);
        }
        if (datePattern == null) {
            return timePattern == null ? "" : timePattern;
        }
        if (timePattern == null) {
            return datePattern;
        }
        return MessageFormat.format(this.getDateTimeFormat(), timePattern, datePattern);
    }

    public DateTimePatternGenerator addPattern(String pattern, boolean override, PatternInfo returnInfo) {
        return this.addPatternWithSkeleton(pattern, null, override, returnInfo);
    }

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
            return this.adjustFieldTypes(patternNoMatcher, this.current.set(skeleton, this.fp, false), false, options);
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
        this.checkFrozen();
        this.appendItemNames[field] = value;
    }

    public String getAppendItemName(int field) {
        return this.appendItemNames[field];
    }

    public static boolean isSingleField(String skeleton) {
        char first = skeleton.charAt(0);
        for (int i = 1; i < skeleton.length(); ++i) {
            if (skeleton.charAt(i) == first) continue;
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
            result.appendItemNames = (String[])this.appendItemNames.clone();
            result.current = new DateTimeMatcher();
            result.fp = new FormatParser();
            result._distanceInfo = new DistanceInfo();
            result.frozen = false;
            return result;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Internal Error");
        }
    }

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

    private String getBestAppending(DateTimeMatcher source, int missingFields, DistanceInfo distInfo, DateTimeMatcher skipMatcher, int options) {
        String resultPattern = null;
        if (missingFields != 0) {
            PatternWithMatcher resultPatternWithMatcher = this.getBestRaw(source, missingFields, distInfo, skipMatcher);
            resultPattern = this.adjustFieldTypes(resultPatternWithMatcher, source, false, options);
            while (distInfo.missingFieldMask != 0) {
                if ((distInfo.missingFieldMask & 0x6000) == 16384 && (missingFields & 0x6000) == 24576) {
                    resultPatternWithMatcher.pattern = resultPattern;
                    resultPattern = this.adjustFieldTypes(resultPatternWithMatcher, source, true, options);
                    distInfo.missingFieldMask &= 0xFFFFBFFF;
                    continue;
                }
                int startingMask = distInfo.missingFieldMask;
                PatternWithMatcher tempWithMatcher = this.getBestRaw(source, distInfo.missingFieldMask, distInfo, skipMatcher);
                String temp = this.adjustFieldTypes(tempWithMatcher, source, false, options);
                int foundMask = startingMask & ~distInfo.missingFieldMask;
                int topField = this.getTopBitNumber(foundMask);
                resultPattern = MessageFormat.format(this.getAppendFormat(topField), resultPattern, temp, this.getAppendName(topField));
            }
        }
        return resultPattern;
    }

    private String getAppendName(int foundMask) {
        return "'" + this.appendItemNames[foundMask] + "'";
    }

    private String getAppendFormat(int foundMask) {
        return this.appendItemFormats[foundMask];
    }

    private int getTopBitNumber(int foundMask) {
        int i = 0;
        while (foundMask != 0) {
            foundMask >>>= 1;
            ++i;
        }
        return i - 1;
    }

    private void complete() {
        PatternInfo patternInfo = new PatternInfo();
        for (int i = 0; i < CANONICAL_ITEMS.length; ++i) {
            this.addPattern(String.valueOf(CANONICAL_ITEMS[i]), false, patternInfo);
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

    private String adjustFieldTypes(PatternWithMatcher patternWithMatcher, DateTimeMatcher inputRequest, boolean fixFractionalSeconds, int options) {
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
            if (fixFractionalSeconds && type == 13) {
                String newField = inputRequest.original[14];
                fieldBuilder.append(this.decimal);
                fieldBuilder.append(newField);
            } else if (inputRequest.type[type] != 0) {
                String reqField = inputRequest.original[type];
                int reqFieldLen = reqField.length();
                if (reqField.charAt(0) == 'E' && reqFieldLen < 3) {
                    reqFieldLen = 3;
                }
                int adjFieldLen = reqFieldLen;
                DateTimeMatcher matcherWithSkeleton = patternWithMatcher.matcherWithSkeleton;
                if (type == 11 && (options & 0x800) == 0 || type == 12 && (options & 0x1000) == 0 || type == 13 && (options & 0x2000) == 0) {
                    adjFieldLen = fieldBuilder.length();
                } else if (matcherWithSkeleton != null) {
                    String skelField = matcherWithSkeleton.origStringForField(type);
                    int skelFieldLen = skelField.length();
                    boolean patFieldIsNumeric = variableField.isNumeric();
                    boolean skelFieldIsNumeric = matcherWithSkeleton.fieldIsNumeric(type);
                    if (skelFieldLen == reqFieldLen || patFieldIsNumeric && !skelFieldIsNumeric || skelFieldIsNumeric && !patFieldIsNumeric) {
                        adjFieldLen = fieldBuilder.length();
                    }
                }
                char c = type != 11 && type != 3 && type != 6 && type != 1 ? reqField.charAt(0) : fieldBuilder.charAt(0);
                fieldBuilder = new StringBuilder();
                for (int i = adjFieldLen; i > 0; --i) {
                    fieldBuilder.append(c);
                }
            }
            newPattern.append((CharSequence)fieldBuilder);
        }
        return newPattern.toString();
    }

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
        for (int i = 0; i < 16; ++i) {
            if ((mask & 1 << i) == 0) continue;
            if (result.length() != 0) {
                result.append(" | ");
            }
            result.append(FIELD_NAME[i]);
            result.append(" ");
        }
        return result.toString();
    }

    private static String getName(String s) {
        boolean string;
        int i = DateTimePatternGenerator.getCanonicalIndex(s, true);
        String name = FIELD_NAME[types[i][1]];
        int subtype = types[i][2];
        boolean bl = string = subtype < 0;
        if (string) {
            subtype = -subtype;
        }
        name = subtype < 0 ? name + ":S" : name + ":N";
        return name;
    }

    private static int getCanonicalIndex(String s, boolean strict) {
        int len = s.length();
        if (len == 0) {
            return -1;
        }
        char ch = s.charAt(0);
        for (int i = 1; i < len; ++i) {
            if (s.charAt(i) == ch) continue;
            return -1;
        }
        int bestRow = -1;
        for (int i = 0; i < types.length; ++i) {
            int[] row = types[i];
            if (row[0] != ch) continue;
            bestRow = i;
            if (row[3] > len || row[row.length - 1] < len) continue;
            return i;
        }
        return strict ? -1 : bestRow;
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

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class DateTimeMatcher
    implements Comparable<DateTimeMatcher> {
        private int[] type = new int[16];
        private String[] original = new String[16];
        private String[] baseOriginal = new String[16];

        private DateTimeMatcher() {
        }

        public String origStringForField(int field) {
            return this.original[field];
        }

        public boolean fieldIsNumeric(int field) {
            return this.type[field] > 0;
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                if (this.original[i].length() == 0) continue;
                result.append(this.original[i]);
            }
            return result.toString();
        }

        public String toCanonicalString() {
            StringBuilder result = new StringBuilder();
            block0: for (int i = 0; i < 16; ++i) {
                if (this.original[i].length() == 0) continue;
                for (int j = 0; j < types.length; ++j) {
                    int[] row = types[j];
                    if (row[1] != i) continue;
                    char originalChar = this.original[i].charAt(0);
                    char repeatChar = originalChar == 'h' || originalChar == 'K' ? (char)'h' : (char)row[0];
                    result.append(Utility.repeat(String.valueOf(repeatChar), this.original[i].length()));
                    continue block0;
                }
            }
            return result.toString();
        }

        String getBasePattern() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 16; ++i) {
                if (this.baseOriginal[i].length() == 0) continue;
                result.append(this.baseOriginal[i]);
            }
            return result.toString();
        }

        DateTimeMatcher set(String pattern, FormatParser fp, boolean allowDuplicateFields) {
            for (int i = 0; i < 16; ++i) {
                this.type[i] = 0;
                this.original[i] = "";
                this.baseOriginal[i] = "";
            }
            fp.set(pattern);
            for (Object obj : fp.getItems()) {
                VariableField item;
                String field;
                if (!(obj instanceof VariableField) || (field = (item = (VariableField)obj).toString()).charAt(0) == 'a') continue;
                int canonicalIndex = item.getCanonicalIndex();
                int[] row = types[canonicalIndex];
                int typeValue = row[1];
                if (this.original[typeValue].length() != 0) {
                    if (allowDuplicateFields) continue;
                    throw new IllegalArgumentException("Conflicting fields:\t" + this.original[typeValue] + ", " + field + "\t in " + pattern);
                }
                this.original[typeValue] = field;
                char repeatChar = (char)row[0];
                int repeatCount = row[3];
                if ("GEzvQ".indexOf(repeatChar) >= 0) {
                    repeatCount = 1;
                }
                this.baseOriginal[typeValue] = Utility.repeat(String.valueOf(repeatChar), repeatCount);
                int subTypeValue = row[2];
                if (subTypeValue > 0) {
                    subTypeValue += field.length();
                }
                this.type[typeValue] = subTypeValue;
            }
            return this;
        }

        int getFieldMask() {
            int result = 0;
            for (int i = 0; i < this.type.length; ++i) {
                if (this.type[i] == 0) continue;
                result |= 1 << i;
            }
            return result;
        }

        void extractFrom(DateTimeMatcher source, int fieldMask) {
            for (int i = 0; i < this.type.length; ++i) {
                if ((fieldMask & 1 << i) != 0) {
                    this.type[i] = source.type[i];
                    this.original[i] = source.original[i];
                    continue;
                }
                this.type[i] = 0;
                this.original[i] = "";
            }
        }

        int getDistance(DateTimeMatcher other, int includeMask, DistanceInfo distanceInfo) {
            int result = 0;
            distanceInfo.clear();
            for (int i = 0; i < this.type.length; ++i) {
                int otherType;
                int myType = (includeMask & 1 << i) == 0 ? 0 : this.type[i];
                if (myType == (otherType = other.type[i])) continue;
                if (myType == 0) {
                    result += 65536;
                    distanceInfo.addExtra(i);
                    continue;
                }
                if (otherType == 0) {
                    result += 4096;
                    distanceInfo.addMissing(i);
                    continue;
                }
                result += Math.abs(myType - otherType);
            }
            return result;
        }

        @Override
        public int compareTo(DateTimeMatcher that) {
            for (int i = 0; i < this.original.length; ++i) {
                int comp = this.original[i].compareTo(that.original[i]);
                if (comp == 0) continue;
                return -comp;
            }
            return 0;
        }

        public boolean equals(Object other) {
            if (!(other instanceof DateTimeMatcher)) {
                return false;
            }
            DateTimeMatcher that = (DateTimeMatcher)other;
            for (int i = 0; i < this.original.length; ++i) {
                if (this.original[i].equals(that.original[i])) continue;
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result = 0;
            for (int i = 0; i < this.original.length; ++i) {
                result ^= this.original[i].hashCode();
            }
            return result;
        }
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

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class FormatParser {
        private transient PatternTokenizer tokenizer = new PatternTokenizer().setSyntaxCharacters(new UnicodeSet("[a-zA-Z]")).setExtraQuotingCharacters(new UnicodeSet("[[[:script=Latn:][:script=Cyrl:]]&[[:L:][:M:]]]")).setUsingQuote(true);
        private List<Object> items = new ArrayList<Object>();

        public final FormatParser set(String string) {
            return this.set(string, false);
        }

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

        public List<Object> getItems() {
            return this.items;
        }

        public String toString() {
            return this.toString(0, this.items.size());
        }

        public String toString(int start, int limit) {
            StringBuilder result = new StringBuilder();
            for (int i = start; i < limit; ++i) {
                Object item = this.items.get(i);
                if (item instanceof String) {
                    String itemString = (String)item;
                    result.append(this.tokenizer.quoteLiteral(itemString));
                    continue;
                }
                result.append(this.items.get(i).toString());
            }
            return result.toString();
        }

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

        public Object quoteLiteral(String string) {
            return this.tokenizer.quoteLiteral(string);
        }
    }

    public static class VariableField {
        private final String string;
        private final int canonicalIndex;

        public VariableField(String string) {
            this(string, false);
        }

        public VariableField(String string, boolean strict) {
            this.canonicalIndex = DateTimePatternGenerator.getCanonicalIndex(string, strict);
            if (this.canonicalIndex < 0) {
                throw new IllegalArgumentException("Illegal datetime field:\t" + string);
            }
            this.string = string;
        }

        public int getType() {
            return types[this.canonicalIndex][1];
        }

        public static String getCanonicalCode(int type) {
            try {
                return CANONICAL_ITEMS[type];
            }
            catch (Exception e) {
                return String.valueOf(type);
            }
        }

        public boolean isNumeric() {
            return types[this.canonicalIndex][2] > 0;
        }

        private int getCanonicalIndex() {
            return this.canonicalIndex;
        }

        public String toString() {
            return this.string;
        }
    }

    public static final class PatternInfo {
        public static final int OK = 0;
        public static final int BASE_CONFLICT = 1;
        public static final int CONFLICT = 2;
        public int status;
        public String conflictingPattern;
    }
}

