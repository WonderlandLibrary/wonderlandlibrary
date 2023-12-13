/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.DateIntervalFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class DateIntervalInfo
implements Cloneable,
Freezable<DateIntervalInfo>,
Serializable {
    static final int currentSerialVersion = 1;
    static final String[] CALENDAR_FIELD_TO_PATTERN_LETTER = new String[]{"G", "y", "M", "w", "W", "d", "D", "E", "F", "a", "h", "H", "m", "s", "S", "z", " ", "Y", "e", "u", "g", "A", " ", " "};
    private static final long serialVersionUID = 1L;
    private static final int MINIMUM_SUPPORTED_CALENDAR_FIELD = 13;
    private static String CALENDAR_KEY = "calendar";
    private static String INTERVAL_FORMATS_KEY = "intervalFormats";
    private static String FALLBACK_STRING = "fallback";
    private static String LATEST_FIRST_PREFIX = "latestFirst:";
    private static String EARLIEST_FIRST_PREFIX = "earliestFirst:";
    private static final ICUCache<String, DateIntervalInfo> DIICACHE = new SimpleCache<String, DateIntervalInfo>();
    private String fFallbackIntervalPattern;
    private boolean fFirstDateInPtnIsLaterDate = false;
    private Map<String, Map<String, PatternInfo>> fIntervalPatterns = null;
    private volatile transient boolean frozen = false;
    private transient boolean fIntervalPatternsReadOnly = false;

    @Deprecated
    public DateIntervalInfo() {
        this.fIntervalPatterns = new HashMap<String, Map<String, PatternInfo>>();
        this.fFallbackIntervalPattern = "{0} \u2013 {1}";
    }

    public DateIntervalInfo(ULocale locale) {
        this.initializeData(locale);
    }

    public DateIntervalInfo(Locale locale) {
        this(ULocale.forLocale(locale));
    }

    private void initializeData(ULocale locale) {
        String key = locale.toString();
        DateIntervalInfo dii = DIICACHE.get(key);
        if (dii == null) {
            this.setup(locale);
            this.fIntervalPatternsReadOnly = true;
            DIICACHE.put(key, ((DateIntervalInfo)this.clone()).freeze());
        } else {
            this.initializeFromReadOnlyPatterns(dii);
        }
    }

    private void initializeFromReadOnlyPatterns(DateIntervalInfo dii) {
        this.fFallbackIntervalPattern = dii.fFallbackIntervalPattern;
        this.fFirstDateInPtnIsLaterDate = dii.fFirstDateInPtnIsLaterDate;
        this.fIntervalPatterns = dii.fIntervalPatterns;
        this.fIntervalPatternsReadOnly = true;
    }

    private void setup(ULocale locale) {
        int DEFAULT_HASH_SIZE = 19;
        this.fIntervalPatterns = new HashMap<String, Map<String, PatternInfo>>(DEFAULT_HASH_SIZE);
        this.fFallbackIntervalPattern = "{0} \u2013 {1}";
        try {
            String calendarTypeToUse = locale.getKeywordValue("calendar");
            if (calendarTypeToUse == null) {
                String[] preferredCalendarTypes = Calendar.getKeywordValuesForLocale("calendar", locale, true);
                calendarTypeToUse = preferredCalendarTypes[0];
            }
            if (calendarTypeToUse == null) {
                calendarTypeToUse = "gregorian";
            }
            DateIntervalSink sink = new DateIntervalSink(this);
            ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
            String fallbackPattern = resource.getStringWithFallback(CALENDAR_KEY + "/" + calendarTypeToUse + "/" + INTERVAL_FORMATS_KEY + "/" + FALLBACK_STRING);
            this.setFallbackIntervalPattern(fallbackPattern);
            HashSet<String> loadedCalendarTypes = new HashSet<String>();
            while (calendarTypeToUse != null) {
                if (loadedCalendarTypes.contains(calendarTypeToUse)) {
                    throw new ICUException("Loop in calendar type fallback: " + calendarTypeToUse);
                }
                loadedCalendarTypes.add(calendarTypeToUse);
                String pathToIntervalFormats = CALENDAR_KEY + "/" + calendarTypeToUse;
                resource.getAllItemsWithFallback(pathToIntervalFormats, sink);
                calendarTypeToUse = sink.getAndResetNextCalendarType();
            }
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
    }

    private static int splitPatternInto2Part(String intervalPattern) {
        int i2;
        boolean inQuote = false;
        char prevCh = '\u0000';
        int count = 0;
        int[] patternRepeated = new int[58];
        int PATTERN_CHAR_BASE = 65;
        boolean foundRepetition = false;
        for (i2 = 0; i2 < intervalPattern.length(); ++i2) {
            char ch = intervalPattern.charAt(i2);
            if (ch != prevCh && count > 0) {
                int repeated = patternRepeated[prevCh - PATTERN_CHAR_BASE];
                if (repeated != 0) {
                    foundRepetition = true;
                    break;
                }
                patternRepeated[prevCh - PATTERN_CHAR_BASE] = 1;
                count = 0;
            }
            if (ch == '\'') {
                if (i2 + 1 < intervalPattern.length() && intervalPattern.charAt(i2 + 1) == '\'') {
                    ++i2;
                    continue;
                }
                inQuote = !inQuote;
                continue;
            }
            if (inQuote || (ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) continue;
            prevCh = ch;
            ++count;
        }
        if (count > 0 && !foundRepetition && patternRepeated[prevCh - PATTERN_CHAR_BASE] == 0) {
            count = 0;
        }
        return i2 - count;
    }

    public void setIntervalPattern(String skeleton, int lrgDiffCalUnit, String intervalPattern) {
        if (this.frozen) {
            throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
        }
        if (lrgDiffCalUnit > 13) {
            throw new IllegalArgumentException("calendar field is larger than MINIMUM_SUPPORTED_CALENDAR_FIELD");
        }
        if (this.fIntervalPatternsReadOnly) {
            this.fIntervalPatterns = DateIntervalInfo.cloneIntervalPatterns(this.fIntervalPatterns);
            this.fIntervalPatternsReadOnly = false;
        }
        PatternInfo ptnInfo = this.setIntervalPatternInternally(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[lrgDiffCalUnit], intervalPattern);
        if (lrgDiffCalUnit == 11) {
            this.setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[9], ptnInfo);
            this.setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[10], ptnInfo);
        } else if (lrgDiffCalUnit == 5 || lrgDiffCalUnit == 7) {
            this.setIntervalPattern(skeleton, CALENDAR_FIELD_TO_PATTERN_LETTER[5], ptnInfo);
        }
    }

    private PatternInfo setIntervalPatternInternally(String skeleton, String lrgDiffCalUnit, String intervalPattern) {
        Map<String, PatternInfo> patternsOfOneSkeleton = this.fIntervalPatterns.get(skeleton);
        boolean emptyHash = false;
        if (patternsOfOneSkeleton == null) {
            patternsOfOneSkeleton = new HashMap<String, PatternInfo>();
            emptyHash = true;
        }
        boolean order = this.fFirstDateInPtnIsLaterDate;
        if (intervalPattern.startsWith(LATEST_FIRST_PREFIX)) {
            order = true;
            int prefixLength = LATEST_FIRST_PREFIX.length();
            intervalPattern = intervalPattern.substring(prefixLength, intervalPattern.length());
        } else if (intervalPattern.startsWith(EARLIEST_FIRST_PREFIX)) {
            order = false;
            int earliestFirstLength = EARLIEST_FIRST_PREFIX.length();
            intervalPattern = intervalPattern.substring(earliestFirstLength, intervalPattern.length());
        }
        PatternInfo itvPtnInfo = DateIntervalInfo.genPatternInfo(intervalPattern, order);
        patternsOfOneSkeleton.put(lrgDiffCalUnit, itvPtnInfo);
        if (emptyHash) {
            this.fIntervalPatterns.put(skeleton, patternsOfOneSkeleton);
        }
        return itvPtnInfo;
    }

    private void setIntervalPattern(String skeleton, String lrgDiffCalUnit, PatternInfo ptnInfo) {
        Map<String, PatternInfo> patternsOfOneSkeleton = this.fIntervalPatterns.get(skeleton);
        patternsOfOneSkeleton.put(lrgDiffCalUnit, ptnInfo);
    }

    @Deprecated
    public static PatternInfo genPatternInfo(String intervalPattern, boolean laterDateFirst) {
        int splitPoint = DateIntervalInfo.splitPatternInto2Part(intervalPattern);
        String firstPart = intervalPattern.substring(0, splitPoint);
        String secondPart = null;
        if (splitPoint < intervalPattern.length()) {
            secondPart = intervalPattern.substring(splitPoint, intervalPattern.length());
        }
        return new PatternInfo(firstPart, secondPart, laterDateFirst);
    }

    public PatternInfo getIntervalPattern(String skeleton, int field) {
        PatternInfo intervalPattern;
        if (field > 13) {
            throw new IllegalArgumentException("no support for field less than SECOND");
        }
        Map<String, PatternInfo> patternsOfOneSkeleton = this.fIntervalPatterns.get(skeleton);
        if (patternsOfOneSkeleton != null && (intervalPattern = patternsOfOneSkeleton.get(CALENDAR_FIELD_TO_PATTERN_LETTER[field])) != null) {
            return intervalPattern;
        }
        return null;
    }

    public String getFallbackIntervalPattern() {
        return this.fFallbackIntervalPattern;
    }

    public void setFallbackIntervalPattern(String fallbackPattern) {
        if (this.frozen) {
            throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
        }
        int firstPatternIndex = fallbackPattern.indexOf("{0}");
        int secondPatternIndex = fallbackPattern.indexOf("{1}");
        if (firstPatternIndex == -1 || secondPatternIndex == -1) {
            throw new IllegalArgumentException("no pattern {0} or pattern {1} in fallbackPattern");
        }
        if (firstPatternIndex > secondPatternIndex) {
            this.fFirstDateInPtnIsLaterDate = true;
        }
        this.fFallbackIntervalPattern = fallbackPattern;
    }

    public boolean getDefaultOrder() {
        return this.fFirstDateInPtnIsLaterDate;
    }

    public Object clone() {
        if (this.frozen) {
            return this;
        }
        return this.cloneUnfrozenDII();
    }

    private Object cloneUnfrozenDII() {
        try {
            DateIntervalInfo other = (DateIntervalInfo)super.clone();
            other.fFallbackIntervalPattern = this.fFallbackIntervalPattern;
            other.fFirstDateInPtnIsLaterDate = this.fFirstDateInPtnIsLaterDate;
            if (this.fIntervalPatternsReadOnly) {
                other.fIntervalPatterns = this.fIntervalPatterns;
                other.fIntervalPatternsReadOnly = true;
            } else {
                other.fIntervalPatterns = DateIntervalInfo.cloneIntervalPatterns(this.fIntervalPatterns);
                other.fIntervalPatternsReadOnly = false;
            }
            other.frozen = false;
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException("clone is not supported", e2);
        }
    }

    private static Map<String, Map<String, PatternInfo>> cloneIntervalPatterns(Map<String, Map<String, PatternInfo>> patterns) {
        HashMap<String, Map<String, PatternInfo>> result = new HashMap<String, Map<String, PatternInfo>>();
        for (Map.Entry<String, Map<String, PatternInfo>> skeletonEntry : patterns.entrySet()) {
            String skeleton = skeletonEntry.getKey();
            Map<String, PatternInfo> patternsOfOneSkeleton = skeletonEntry.getValue();
            HashMap<String, PatternInfo> oneSetPtn = new HashMap<String, PatternInfo>();
            for (Map.Entry<String, PatternInfo> calEntry : patternsOfOneSkeleton.entrySet()) {
                String calField = calEntry.getKey();
                PatternInfo value = calEntry.getValue();
                oneSetPtn.put(calField, value);
            }
            result.put(skeleton, oneSetPtn);
        }
        return result;
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public DateIntervalInfo freeze() {
        this.fIntervalPatternsReadOnly = true;
        this.frozen = true;
        return this;
    }

    @Override
    public DateIntervalInfo cloneAsThawed() {
        DateIntervalInfo result = (DateIntervalInfo)this.cloneUnfrozenDII();
        return result;
    }

    static void parseSkeleton(String skeleton, int[] skeletonFieldWidth) {
        int PATTERN_CHAR_BASE = 65;
        for (int i2 = 0; i2 < skeleton.length(); ++i2) {
            int n2 = skeleton.charAt(i2) - PATTERN_CHAR_BASE;
            skeletonFieldWidth[n2] = skeletonFieldWidth[n2] + 1;
        }
    }

    private static boolean stringNumeric(int fieldWidth, int anotherFieldWidth, char patternLetter) {
        return patternLetter == 'M' && (fieldWidth <= 2 && anotherFieldWidth > 2 || fieldWidth > 2 && anotherFieldWidth <= 2);
    }

    DateIntervalFormat.BestMatchInfo getBestSkeleton(String inputSkeleton) {
        String bestSkeleton = inputSkeleton;
        int[] inputSkeletonFieldWidth = new int[58];
        int[] skeletonFieldWidth = new int[58];
        int DIFFERENT_FIELD = 4096;
        int STRING_NUMERIC_DIFFERENCE = 256;
        int BASE = 65;
        boolean replaceZWithV = false;
        if (inputSkeleton.indexOf(122) != -1) {
            inputSkeleton = inputSkeleton.replace('z', 'v');
            replaceZWithV = true;
        }
        DateIntervalInfo.parseSkeleton(inputSkeleton, inputSkeletonFieldWidth);
        int bestDistance = Integer.MAX_VALUE;
        int bestFieldDifference = 0;
        for (String skeleton : this.fIntervalPatterns.keySet()) {
            for (int i2 = 0; i2 < skeletonFieldWidth.length; ++i2) {
                skeletonFieldWidth[i2] = 0;
            }
            DateIntervalInfo.parseSkeleton(skeleton, skeletonFieldWidth);
            int distance = 0;
            int fieldDifference = 1;
            for (int i3 = 0; i3 < inputSkeletonFieldWidth.length; ++i3) {
                int inputFieldWidth = inputSkeletonFieldWidth[i3];
                int fieldWidth = skeletonFieldWidth[i3];
                if (inputFieldWidth == fieldWidth) continue;
                if (inputFieldWidth == 0) {
                    fieldDifference = -1;
                    distance += 4096;
                    continue;
                }
                if (fieldWidth == 0) {
                    fieldDifference = -1;
                    distance += 4096;
                    continue;
                }
                if (DateIntervalInfo.stringNumeric(inputFieldWidth, fieldWidth, (char)(i3 + 65))) {
                    distance += 256;
                    continue;
                }
                distance += Math.abs(inputFieldWidth - fieldWidth);
            }
            if (distance < bestDistance) {
                bestSkeleton = skeleton;
                bestDistance = distance;
                bestFieldDifference = fieldDifference;
            }
            if (distance != 0) continue;
            bestFieldDifference = 0;
            break;
        }
        if (replaceZWithV && bestFieldDifference != -1) {
            bestFieldDifference = 2;
        }
        return new DateIntervalFormat.BestMatchInfo(bestSkeleton, bestFieldDifference);
    }

    public boolean equals(Object a2) {
        if (a2 instanceof DateIntervalInfo) {
            DateIntervalInfo dtInfo = (DateIntervalInfo)a2;
            return this.fIntervalPatterns.equals(dtInfo.fIntervalPatterns);
        }
        return false;
    }

    public int hashCode() {
        return this.fIntervalPatterns.hashCode();
    }

    @Deprecated
    public Map<String, Set<String>> getPatterns() {
        LinkedHashMap<String, Set<String>> result = new LinkedHashMap<String, Set<String>>();
        for (Map.Entry<String, Map<String, PatternInfo>> entry : this.fIntervalPatterns.entrySet()) {
            result.put(entry.getKey(), new LinkedHashSet<String>(entry.getValue().keySet()));
        }
        return result;
    }

    @Deprecated
    public Map<String, Map<String, PatternInfo>> getRawPatterns() {
        LinkedHashMap<String, Map<String, PatternInfo>> result = new LinkedHashMap<String, Map<String, PatternInfo>>();
        for (Map.Entry<String, Map<String, PatternInfo>> entry : this.fIntervalPatterns.entrySet()) {
            result.put(entry.getKey(), new LinkedHashMap<String, PatternInfo>(entry.getValue()));
        }
        return result;
    }

    static /* synthetic */ String access$100() {
        return CALENDAR_KEY;
    }

    private static final class DateIntervalSink
    extends UResource.Sink {
        private static final String ACCEPTED_PATTERN_LETTERS = "yMdahHms";
        DateIntervalInfo dateIntervalInfo;
        String nextCalendarType;
        private static final String DATE_INTERVAL_PATH_PREFIX = "/LOCALE/" + DateIntervalInfo.access$100() + "/";
        private static final String DATE_INTERVAL_PATH_SUFFIX = "/" + DateIntervalInfo.access$000();

        public DateIntervalSink(DateIntervalInfo dateIntervalInfo) {
            this.dateIntervalInfo = dateIntervalInfo;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table dateIntervalData = value.getTable();
            int i2 = 0;
            while (dateIntervalData.getKeyAndValue(i2, key, value)) {
                if (key.contentEquals(INTERVAL_FORMATS_KEY)) {
                    if (value.getType() == 3) {
                        this.nextCalendarType = this.getCalendarTypeFromPath(value.getAliasString());
                        break;
                    }
                    if (value.getType() == 2) {
                        UResource.Table skeletonData = value.getTable();
                        int j2 = 0;
                        while (skeletonData.getKeyAndValue(j2, key, value)) {
                            if (value.getType() == 2) {
                                this.processSkeletonTable(key, value);
                            }
                            ++j2;
                        }
                        break;
                    }
                }
                ++i2;
            }
        }

        public void processSkeletonTable(UResource.Key key, UResource.Value value) {
            String currentSkeleton = key.toString();
            UResource.Table patternData = value.getTable();
            int k2 = 0;
            while (patternData.getKeyAndValue(k2, key, value)) {
                CharSequence patternLetter;
                if (value.getType() == 0 && (patternLetter = this.validateAndProcessPatternLetter(key)) != null) {
                    String lrgDiffCalUnit = patternLetter.toString();
                    this.setIntervalPatternIfAbsent(currentSkeleton, lrgDiffCalUnit, value);
                }
                ++k2;
            }
        }

        public String getAndResetNextCalendarType() {
            String tmpCalendarType = this.nextCalendarType;
            this.nextCalendarType = null;
            return tmpCalendarType;
        }

        private String getCalendarTypeFromPath(String path) {
            if (path.startsWith(DATE_INTERVAL_PATH_PREFIX) && path.endsWith(DATE_INTERVAL_PATH_SUFFIX)) {
                return path.substring(DATE_INTERVAL_PATH_PREFIX.length(), path.length() - DATE_INTERVAL_PATH_SUFFIX.length());
            }
            throw new ICUException("Malformed 'intervalFormat' alias path: " + path);
        }

        private CharSequence validateAndProcessPatternLetter(CharSequence patternLetter) {
            if (patternLetter.length() != 1) {
                return null;
            }
            char letter = patternLetter.charAt(0);
            if (ACCEPTED_PATTERN_LETTERS.indexOf(letter) < 0) {
                return null;
            }
            if (letter == CALENDAR_FIELD_TO_PATTERN_LETTER[11].charAt(0)) {
                patternLetter = CALENDAR_FIELD_TO_PATTERN_LETTER[10];
            }
            return patternLetter;
        }

        private void setIntervalPatternIfAbsent(String currentSkeleton, String lrgDiffCalUnit, UResource.Value intervalPattern) {
            Map patternsOfOneSkeleton = (Map)this.dateIntervalInfo.fIntervalPatterns.get(currentSkeleton);
            if (patternsOfOneSkeleton == null || !patternsOfOneSkeleton.containsKey(lrgDiffCalUnit)) {
                this.dateIntervalInfo.setIntervalPatternInternally(currentSkeleton, lrgDiffCalUnit, intervalPattern.toString());
            }
        }
    }

    public static final class PatternInfo
    implements Cloneable,
    Serializable {
        static final int currentSerialVersion = 1;
        private static final long serialVersionUID = 1L;
        private final String fIntervalPatternFirstPart;
        private final String fIntervalPatternSecondPart;
        private final boolean fFirstDateInPtnIsLaterDate;

        public PatternInfo(String firstPart, String secondPart, boolean firstDateInPtnIsLaterDate) {
            this.fIntervalPatternFirstPart = firstPart;
            this.fIntervalPatternSecondPart = secondPart;
            this.fFirstDateInPtnIsLaterDate = firstDateInPtnIsLaterDate;
        }

        public String getFirstPart() {
            return this.fIntervalPatternFirstPart;
        }

        public String getSecondPart() {
            return this.fIntervalPatternSecondPart;
        }

        public boolean firstDateInPtnIsLaterDate() {
            return this.fFirstDateInPtnIsLaterDate;
        }

        public boolean equals(Object a2) {
            if (a2 instanceof PatternInfo) {
                PatternInfo patternInfo = (PatternInfo)a2;
                return Utility.objectEquals(this.fIntervalPatternFirstPart, patternInfo.fIntervalPatternFirstPart) && Utility.objectEquals(this.fIntervalPatternSecondPart, patternInfo.fIntervalPatternSecondPart) && this.fFirstDateInPtnIsLaterDate == patternInfo.fFirstDateInPtnIsLaterDate;
            }
            return false;
        }

        public int hashCode() {
            int hash;
            int n2 = hash = this.fIntervalPatternFirstPart != null ? this.fIntervalPatternFirstPart.hashCode() : 0;
            if (this.fIntervalPatternSecondPart != null) {
                hash ^= this.fIntervalPatternSecondPart.hashCode();
            }
            if (this.fFirstDateInPtnIsLaterDate) {
                hash ^= 0xFFFFFFFF;
            }
            return hash;
        }

        @Deprecated
        public String toString() {
            return "{first=\u00ab" + this.fIntervalPatternFirstPart + "\u00bb, second=\u00ab" + this.fIntervalPatternSecondPart + "\u00bb, reversed:" + this.fFirstDateInPtnIsLaterDate + "}";
        }
    }
}

