/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.MeasureFormat;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.TimeUnit;
import com.ibm.icu.util.TimeUnitAmount;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.ObjectStreamException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;

@Deprecated
public class TimeUnitFormat
extends MeasureFormat {
    @Deprecated
    public static final int FULL_NAME = 0;
    @Deprecated
    public static final int ABBREVIATED_NAME = 1;
    private static final int TOTAL_STYLES = 2;
    private static final long serialVersionUID = -3707773153184971529L;
    private NumberFormat format = super.getNumberFormatInternal();
    private ULocale locale;
    private int style;
    private transient Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns;
    private transient PluralRules pluralRules;
    private transient boolean isReady;
    private static final String DEFAULT_PATTERN_FOR_SECOND = "{0} s";
    private static final String DEFAULT_PATTERN_FOR_MINUTE = "{0} min";
    private static final String DEFAULT_PATTERN_FOR_HOUR = "{0} h";
    private static final String DEFAULT_PATTERN_FOR_DAY = "{0} d";
    private static final String DEFAULT_PATTERN_FOR_WEEK = "{0} w";
    private static final String DEFAULT_PATTERN_FOR_MONTH = "{0} m";
    private static final String DEFAULT_PATTERN_FOR_YEAR = "{0} y";

    @Deprecated
    public TimeUnitFormat() {
        this(ULocale.getDefault(), 0);
    }

    @Deprecated
    public TimeUnitFormat(ULocale locale) {
        this(locale, 0);
    }

    @Deprecated
    public TimeUnitFormat(Locale locale) {
        this(locale, 0);
    }

    @Deprecated
    public TimeUnitFormat(ULocale locale, int style) {
        super(locale, style == 0 ? MeasureFormat.FormatWidth.WIDE : MeasureFormat.FormatWidth.SHORT);
        if (style < 0 || style >= 2) {
            throw new IllegalArgumentException("style should be either FULL_NAME or ABBREVIATED_NAME style");
        }
        this.style = style;
        this.isReady = false;
    }

    private TimeUnitFormat(ULocale locale, int style, NumberFormat numberFormat) {
        this(locale, style);
        if (numberFormat != null) {
            this.setNumberFormat((NumberFormat)numberFormat.clone());
        }
    }

    @Deprecated
    public TimeUnitFormat(Locale locale, int style) {
        this(ULocale.forLocale(locale), style);
    }

    @Deprecated
    public TimeUnitFormat setLocale(ULocale locale) {
        this.setLocale(locale, locale);
        this.clearCache();
        return this;
    }

    @Deprecated
    public TimeUnitFormat setLocale(Locale locale) {
        return this.setLocale(ULocale.forLocale(locale));
    }

    @Deprecated
    public TimeUnitFormat setNumberFormat(NumberFormat format) {
        if (format == this.format) {
            return this;
        }
        if (format == null) {
            if (this.locale == null) {
                this.isReady = false;
            } else {
                this.format = NumberFormat.getNumberInstance(this.locale);
            }
        } else {
            this.format = format;
        }
        this.clearCache();
        return this;
    }

    @Override
    @Deprecated
    public NumberFormat getNumberFormat() {
        return (NumberFormat)this.format.clone();
    }

    @Override
    NumberFormat getNumberFormatInternal() {
        return this.format;
    }

    @Override
    LocalizedNumberFormatter getNumberFormatter() {
        return ((DecimalFormat)this.format).toNumberFormatter();
    }

    @Override
    @Deprecated
    public TimeUnitAmount parseObject(String source, ParsePosition pos) {
        if (!this.isReady) {
            this.setup();
        }
        Integer resultNumber = null;
        TimeUnit resultTimeUnit = null;
        int oldPos = pos.getIndex();
        int newPos = -1;
        int longestParseDistance = 0;
        String countOfLongestMatch = null;
        for (TimeUnit timeUnit : this.timeUnitToCountToPatterns.keySet()) {
            Map<String, Object[]> countToPattern = this.timeUnitToCountToPatterns.get(timeUnit);
            for (Map.Entry<String, Object[]> patternEntry : countToPattern.entrySet()) {
                String count = patternEntry.getKey();
                for (int styl = 0; styl < 2; ++styl) {
                    int parseDistance;
                    MessageFormat pattern = (MessageFormat)patternEntry.getValue()[styl];
                    pos.setErrorIndex(-1);
                    pos.setIndex(oldPos);
                    Object parsed = pattern.parseObject(source, pos);
                    if (pos.getErrorIndex() != -1 || pos.getIndex() == oldPos) continue;
                    Number temp = null;
                    if (((Object[])parsed).length != 0) {
                        Object tempObj = ((Object[])parsed)[0];
                        if (tempObj instanceof Number) {
                            temp = (Number)tempObj;
                        } else {
                            try {
                                temp = this.format.parse(tempObj.toString());
                            }
                            catch (ParseException e2) {
                                continue;
                            }
                        }
                    }
                    if ((parseDistance = pos.getIndex() - oldPos) <= longestParseDistance) continue;
                    resultNumber = temp;
                    resultTimeUnit = timeUnit;
                    newPos = pos.getIndex();
                    longestParseDistance = parseDistance;
                    countOfLongestMatch = count;
                }
            }
        }
        if (resultNumber == null && longestParseDistance != 0) {
            resultNumber = countOfLongestMatch.equals("zero") ? Integer.valueOf(0) : (countOfLongestMatch.equals("one") ? Integer.valueOf(1) : (countOfLongestMatch.equals("two") ? Integer.valueOf(2) : Integer.valueOf(3)));
        }
        if (longestParseDistance == 0) {
            pos.setIndex(oldPos);
            pos.setErrorIndex(0);
            return null;
        }
        pos.setIndex(newPos);
        pos.setErrorIndex(-1);
        return new TimeUnitAmount((Number)resultNumber, resultTimeUnit);
    }

    private void setup() {
        if (this.locale == null) {
            this.locale = this.format != null ? this.format.getLocale(null) : ULocale.getDefault(ULocale.Category.FORMAT);
            this.setLocale(this.locale, this.locale);
        }
        if (this.format == null) {
            this.format = NumberFormat.getNumberInstance(this.locale);
        }
        this.pluralRules = PluralRules.forLocale(this.locale);
        this.timeUnitToCountToPatterns = new HashMap<TimeUnit, Map<String, Object[]>>();
        Set<String> pluralKeywords = this.pluralRules.getKeywords();
        this.setup("units/duration", this.timeUnitToCountToPatterns, 0, pluralKeywords);
        this.setup("unitsShort/duration", this.timeUnitToCountToPatterns, 1, pluralKeywords);
        this.isReady = true;
    }

    private void setup(String resourceKey, Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns, int style, Set<String> pluralKeywords) {
        try {
            ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/unit", this.locale);
            TimeUnitFormatSetupSink sink = new TimeUnitFormatSetupSink(timeUnitToCountToPatterns, style, pluralKeywords, this.locale);
            resource.getAllItemsWithFallback(resourceKey, sink);
        }
        catch (MissingResourceException resource) {
            // empty catch block
        }
        TimeUnit[] timeUnits = TimeUnit.values();
        Set<String> keywords = this.pluralRules.getKeywords();
        for (int i2 = 0; i2 < timeUnits.length; ++i2) {
            TimeUnit timeUnit = timeUnits[i2];
            Map<String, Object[]> countToPatterns = timeUnitToCountToPatterns.get(timeUnit);
            if (countToPatterns == null) {
                countToPatterns = new TreeMap<String, Object[]>();
                timeUnitToCountToPatterns.put(timeUnit, countToPatterns);
            }
            for (String pluralCount : keywords) {
                if (countToPatterns.get(pluralCount) != null && countToPatterns.get(pluralCount)[style] != null) continue;
                this.searchInTree(resourceKey, style, timeUnit, pluralCount, pluralCount, countToPatterns);
            }
        }
    }

    private void searchInTree(String resourceKey, int styl, TimeUnit timeUnit, String srcPluralCount, String searchPluralCount, Map<String, Object[]> countToPatterns) {
        ULocale parentLocale;
        String srcTimeUnitName = timeUnit.toString();
        for (parentLocale = this.locale; parentLocale != null; parentLocale = parentLocale.getFallback()) {
            try {
                ICUResourceBundle unitsRes = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/unit", parentLocale);
                unitsRes = unitsRes.getWithFallback(resourceKey);
                ICUResourceBundle oneUnitRes = unitsRes.getWithFallback(srcTimeUnitName);
                String pattern = oneUnitRes.getStringWithFallback(searchPluralCount);
                MessageFormat messageFormat = new MessageFormat(pattern, this.locale);
                Object[] pair = countToPatterns.get(srcPluralCount);
                if (pair == null) {
                    pair = new Object[2];
                    countToPatterns.put(srcPluralCount, pair);
                }
                pair[styl] = messageFormat;
                return;
            }
            catch (MissingResourceException unitsRes) {
                continue;
            }
        }
        if (parentLocale == null && resourceKey.equals("unitsShort")) {
            this.searchInTree("units", styl, timeUnit, srcPluralCount, searchPluralCount, countToPatterns);
            if (countToPatterns.get(srcPluralCount) != null && countToPatterns.get(srcPluralCount)[styl] != null) {
                return;
            }
        }
        if (searchPluralCount.equals("other")) {
            MessageFormat messageFormat = null;
            if (timeUnit == TimeUnit.SECOND) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_SECOND, this.locale);
            } else if (timeUnit == TimeUnit.MINUTE) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_MINUTE, this.locale);
            } else if (timeUnit == TimeUnit.HOUR) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_HOUR, this.locale);
            } else if (timeUnit == TimeUnit.WEEK) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_WEEK, this.locale);
            } else if (timeUnit == TimeUnit.DAY) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_DAY, this.locale);
            } else if (timeUnit == TimeUnit.MONTH) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_MONTH, this.locale);
            } else if (timeUnit == TimeUnit.YEAR) {
                messageFormat = new MessageFormat(DEFAULT_PATTERN_FOR_YEAR, this.locale);
            }
            Object[] pair = countToPatterns.get(srcPluralCount);
            if (pair == null) {
                pair = new Object[2];
                countToPatterns.put(srcPluralCount, pair);
            }
            pair[styl] = messageFormat;
        } else {
            this.searchInTree(resourceKey, styl, timeUnit, srcPluralCount, "other", countToPatterns);
        }
    }

    @Override
    @Deprecated
    public Object clone() {
        TimeUnitFormat result = (TimeUnitFormat)super.clone();
        result.format = (NumberFormat)this.format.clone();
        return result;
    }

    private Object writeReplace() throws ObjectStreamException {
        return super.toTimeUnitProxy();
    }

    private Object readResolve() throws ObjectStreamException {
        return new TimeUnitFormat(this.locale, this.style, this.format);
    }

    private static final class TimeUnitFormatSetupSink
    extends UResource.Sink {
        Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns;
        int style;
        Set<String> pluralKeywords;
        ULocale locale;
        boolean beenHere;

        TimeUnitFormatSetupSink(Map<TimeUnit, Map<String, Object[]>> timeUnitToCountToPatterns, int style, Set<String> pluralKeywords, ULocale locale) {
            this.timeUnitToCountToPatterns = timeUnitToCountToPatterns;
            this.style = style;
            this.pluralKeywords = pluralKeywords;
            this.locale = locale;
            this.beenHere = false;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            if (this.beenHere) {
                return;
            }
            this.beenHere = true;
            UResource.Table units = value.getTable();
            int i2 = 0;
            while (units.getKeyAndValue(i2, key, value)) {
                block17: {
                    TimeUnit timeUnit;
                    block11: {
                        String timeUnitName;
                        block16: {
                            block15: {
                                block14: {
                                    block13: {
                                        block12: {
                                            block10: {
                                                timeUnitName = key.toString();
                                                timeUnit = null;
                                                if (!timeUnitName.equals("year")) break block10;
                                                timeUnit = TimeUnit.YEAR;
                                                break block11;
                                            }
                                            if (!timeUnitName.equals("month")) break block12;
                                            timeUnit = TimeUnit.MONTH;
                                            break block11;
                                        }
                                        if (!timeUnitName.equals("day")) break block13;
                                        timeUnit = TimeUnit.DAY;
                                        break block11;
                                    }
                                    if (!timeUnitName.equals("hour")) break block14;
                                    timeUnit = TimeUnit.HOUR;
                                    break block11;
                                }
                                if (!timeUnitName.equals("minute")) break block15;
                                timeUnit = TimeUnit.MINUTE;
                                break block11;
                            }
                            if (!timeUnitName.equals("second")) break block16;
                            timeUnit = TimeUnit.SECOND;
                            break block11;
                        }
                        if (!timeUnitName.equals("week")) break block17;
                        timeUnit = TimeUnit.WEEK;
                    }
                    Map<String, Object[]> countToPatterns = this.timeUnitToCountToPatterns.get(timeUnit);
                    if (countToPatterns == null) {
                        countToPatterns = new TreeMap<String, Object[]>();
                        this.timeUnitToCountToPatterns.put(timeUnit, countToPatterns);
                    }
                    UResource.Table countsToPatternTable = value.getTable();
                    int j2 = 0;
                    while (countsToPatternTable.getKeyAndValue(j2, key, value)) {
                        String pluralCount = key.toString();
                        if (this.pluralKeywords.contains(pluralCount)) {
                            Object[] pair = countToPatterns.get(pluralCount);
                            if (pair == null) {
                                pair = new Object[2];
                                countToPatterns.put(pluralCount, pair);
                            }
                            if (pair[this.style] == null) {
                                String pattern = value.getString();
                                MessageFormat messageFormat = new MessageFormat(pattern, this.locale);
                                pair[this.style] = messageFormat;
                            }
                        }
                        ++j2;
                    }
                }
                ++i2;
            }
        }
    }
}

