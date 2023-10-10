// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util.datetime;

import java.util.Set;
import java.text.DateFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ListIterator;
import java.text.ParsePosition;
import java.text.ParseException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.Locale;
import java.io.Serializable;

public class FastDateParser implements DateParser, Serializable
{
    private static final long serialVersionUID = 3L;
    static final Locale JAPANESE_IMPERIAL;
    private final String pattern;
    private final TimeZone timeZone;
    private final Locale locale;
    private final int century;
    private final int startYear;
    private transient List<StrategyAndWidth> patterns;
    private static final Comparator<String> LONGER_FIRST_LOWERCASE;
    private static final ConcurrentMap<Locale, Strategy>[] caches;
    private static final Strategy ABBREVIATED_YEAR_STRATEGY;
    private static final Strategy NUMBER_MONTH_STRATEGY;
    private static final Strategy LITERAL_YEAR_STRATEGY;
    private static final Strategy WEEK_OF_YEAR_STRATEGY;
    private static final Strategy WEEK_OF_MONTH_STRATEGY;
    private static final Strategy DAY_OF_YEAR_STRATEGY;
    private static final Strategy DAY_OF_MONTH_STRATEGY;
    private static final Strategy DAY_OF_WEEK_STRATEGY;
    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY;
    private static final Strategy HOUR_OF_DAY_STRATEGY;
    private static final Strategy HOUR24_OF_DAY_STRATEGY;
    private static final Strategy HOUR12_STRATEGY;
    private static final Strategy HOUR_STRATEGY;
    private static final Strategy MINUTE_STRATEGY;
    private static final Strategy SECOND_STRATEGY;
    private static final Strategy MILLISECOND_STRATEGY;
    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale) {
        this(pattern, timeZone, locale, null);
    }
    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;
        final Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
        int centuryStartYear;
        if (centuryStart != null) {
            definingCalendar.setTime(centuryStart);
            centuryStartYear = definingCalendar.get(1);
        }
        else if (locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            centuryStartYear = 0;
        }
        else {
            definingCalendar.setTime(new Date());
            centuryStartYear = definingCalendar.get(1) - 80;
        }
        this.century = centuryStartYear / 100 * 100;
        this.startYear = centuryStartYear - this.century;
        this.init(definingCalendar);
    }
    
    private void init(final Calendar definingCalendar) {
        this.patterns = new ArrayList<StrategyAndWidth>();
        final StrategyParser fm = new StrategyParser(definingCalendar);
        while (true) {
            final StrategyAndWidth field = fm.getNextStrategy();
            if (field == null) {
                break;
            }
            this.patterns.add(field);
        }
    }
    
    private static boolean isFormatLetter(final char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
    
    @Override
    public String getPattern() {
        return this.pattern;
    }
    
    @Override
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    @Override
    public Locale getLocale() {
        return this.locale;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDateParser)) {
            return false;
        }
        final FastDateParser other = (FastDateParser)obj;
        return this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale);
    }
    
    @Override
    public int hashCode() {
        return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
    }
    
    @Override
    public String toString() {
        return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
        this.init(definingCalendar);
    }
    
    @Override
    public Object parseObject(final String source) throws ParseException {
        return this.parse(source);
    }
    
    @Override
    public Date parse(final String source) throws ParseException {
        final ParsePosition pp = new ParsePosition(0);
        final Date date = this.parse(source, pp);
        if (date != null) {
            return date;
        }
        if (this.locale.equals(FastDateParser.JAPANESE_IMPERIAL)) {
            throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source, pp.getErrorIndex());
        }
        throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return this.parse(source, pos);
    }
    
    @Override
    public Date parse(final String source, final ParsePosition pos) {
        final Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
        cal.clear();
        return this.parse(source, pos, cal) ? cal.getTime() : null;
    }
    
    @Override
    public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
        final ListIterator<StrategyAndWidth> lt = this.patterns.listIterator();
        while (lt.hasNext()) {
            final StrategyAndWidth strategyAndWidth = lt.next();
            final int maxWidth = strategyAndWidth.getMaxWidth(lt);
            if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth)) {
                return false;
            }
        }
        return true;
    }
    
    private static StringBuilder simpleQuote(final StringBuilder sb, final String value) {
        for (int i = 0; i < value.length(); ++i) {
            final char c = value.charAt(i);
            switch (c) {
                case '$':
                case '(':
                case ')':
                case '*':
                case '+':
                case '.':
                case '?':
                case '[':
                case '\\':
                case '^':
                case '{':
                case '|': {
                    sb.append('\\');
                    break;
                }
            }
            sb.append(c);
        }
        return sb;
    }
    
    private static Map<String, Integer> appendDisplayNames(final Calendar cal, final Locale locale, final int field, final StringBuilder regex) {
        final Map<String, Integer> values = new HashMap<String, Integer>();
        final Map<String, Integer> displayNames = cal.getDisplayNames(field, 0, locale);
        final TreeSet<String> sorted = new TreeSet<String>(FastDateParser.LONGER_FIRST_LOWERCASE);
        for (final Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
            final String key = displayName.getKey().toLowerCase(locale);
            if (sorted.add(key)) {
                values.put(key, displayName.getValue());
            }
        }
        for (final String symbol : sorted) {
            simpleQuote(regex, symbol).append('|');
        }
        return values;
    }
    
    private int adjustYear(final int twoDigitYear) {
        final int trial = this.century + twoDigitYear;
        return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
    }
    
    private Strategy getStrategy(final char f, final int width, final Calendar definingCalendar) {
        switch (f) {
            default: {
                throw new IllegalArgumentException("Format '" + f + "' not supported");
            }
            case 'D': {
                return FastDateParser.DAY_OF_YEAR_STRATEGY;
            }
            case 'E': {
                return this.getLocaleSpecificStrategy(7, definingCalendar);
            }
            case 'F': {
                return FastDateParser.DAY_OF_WEEK_IN_MONTH_STRATEGY;
            }
            case 'G': {
                return this.getLocaleSpecificStrategy(0, definingCalendar);
            }
            case 'H': {
                return FastDateParser.HOUR_OF_DAY_STRATEGY;
            }
            case 'K': {
                return FastDateParser.HOUR_STRATEGY;
            }
            case 'M': {
                return (width >= 3) ? this.getLocaleSpecificStrategy(2, definingCalendar) : FastDateParser.NUMBER_MONTH_STRATEGY;
            }
            case 'S': {
                return FastDateParser.MILLISECOND_STRATEGY;
            }
            case 'W': {
                return FastDateParser.WEEK_OF_MONTH_STRATEGY;
            }
            case 'a': {
                return this.getLocaleSpecificStrategy(9, definingCalendar);
            }
            case 'd': {
                return FastDateParser.DAY_OF_MONTH_STRATEGY;
            }
            case 'h': {
                return FastDateParser.HOUR12_STRATEGY;
            }
            case 'k': {
                return FastDateParser.HOUR24_OF_DAY_STRATEGY;
            }
            case 'm': {
                return FastDateParser.MINUTE_STRATEGY;
            }
            case 's': {
                return FastDateParser.SECOND_STRATEGY;
            }
            case 'u': {
                return FastDateParser.DAY_OF_WEEK_STRATEGY;
            }
            case 'w': {
                return FastDateParser.WEEK_OF_YEAR_STRATEGY;
            }
            case 'Y':
            case 'y': {
                return (width > 2) ? FastDateParser.LITERAL_YEAR_STRATEGY : FastDateParser.ABBREVIATED_YEAR_STRATEGY;
            }
            case 'X': {
                return ISO8601TimeZoneStrategy.getStrategy(width);
            }
            case 'Z': {
                if (width == 2) {
                    return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
                }
                return this.getLocaleSpecificStrategy(15, definingCalendar);
            }
            case 'z': {
                return this.getLocaleSpecificStrategy(15, definingCalendar);
            }
        }
    }
    
    private static ConcurrentMap<Locale, Strategy> getCache(final int field) {
        synchronized (FastDateParser.caches) {
            if (FastDateParser.caches[field] == null) {
                FastDateParser.caches[field] = new ConcurrentHashMap<Locale, Strategy>(3);
            }
            return FastDateParser.caches[field];
        }
    }
    
    private Strategy getLocaleSpecificStrategy(final int field, final Calendar definingCalendar) {
        final ConcurrentMap<Locale, Strategy> cache = getCache(field);
        Strategy strategy = cache.get(this.locale);
        if (strategy == null) {
            strategy = ((field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale));
            final Strategy inCache = cache.putIfAbsent(this.locale, strategy);
            if (inCache != null) {
                return inCache;
            }
        }
        return strategy;
    }
    
    static {
        JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
        LONGER_FIRST_LOWERCASE = ((left, right) -> right.compareTo(left));
        caches = new ConcurrentMap[17];
        ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1) {
            @Override
            int modify(final FastDateParser parser, final int iValue) {
                return (iValue < 100) ? parser.adjustYear(iValue) : iValue;
            }
        };
        NUMBER_MONTH_STRATEGY = new NumberStrategy(2) {
            @Override
            int modify(final FastDateParser parser, final int iValue) {
                return iValue - 1;
            }
        };
        LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
        WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
        WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
        DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
        DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
        DAY_OF_WEEK_STRATEGY = new NumberStrategy(7) {
            @Override
            int modify(final FastDateParser parser, final int iValue) {
                return (iValue != 7) ? (iValue + 1) : 1;
            }
        };
        DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
        HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
        HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11) {
            @Override
            int modify(final FastDateParser parser, final int iValue) {
                return (iValue == 24) ? 0 : iValue;
            }
        };
        HOUR12_STRATEGY = new NumberStrategy(10) {
            @Override
            int modify(final FastDateParser parser, final int iValue) {
                return (iValue == 12) ? 0 : iValue;
            }
        };
        HOUR_STRATEGY = new NumberStrategy(10);
        MINUTE_STRATEGY = new NumberStrategy(12);
        SECOND_STRATEGY = new NumberStrategy(13);
        MILLISECOND_STRATEGY = new NumberStrategy(14);
    }
    
    private static class StrategyAndWidth
    {
        final Strategy strategy;
        final int width;
        
        StrategyAndWidth(final Strategy strategy, final int width) {
            this.strategy = strategy;
            this.width = width;
        }
        
        int getMaxWidth(final ListIterator<StrategyAndWidth> lt) {
            if (!this.strategy.isNumber() || !lt.hasNext()) {
                return 0;
            }
            final Strategy nextStrategy = lt.next().strategy;
            lt.previous();
            return nextStrategy.isNumber() ? this.width : 0;
        }
    }
    
    private class StrategyParser
    {
        private final Calendar definingCalendar;
        private int currentIdx;
        
        StrategyParser(final Calendar definingCalendar) {
            this.definingCalendar = definingCalendar;
        }
        
        StrategyAndWidth getNextStrategy() {
            if (this.currentIdx >= FastDateParser.this.pattern.length()) {
                return null;
            }
            final char c = FastDateParser.this.pattern.charAt(this.currentIdx);
            if (isFormatLetter(c)) {
                return this.letterPattern(c);
            }
            return this.literal();
        }
        
        private StrategyAndWidth letterPattern(final char c) {
            final int begin = this.currentIdx;
            while (++this.currentIdx < FastDateParser.this.pattern.length() && FastDateParser.this.pattern.charAt(this.currentIdx) == c) {}
            final int width = this.currentIdx - begin;
            return new StrategyAndWidth(FastDateParser.this.getStrategy(c, width, this.definingCalendar), width);
        }
        
        private StrategyAndWidth literal() {
            boolean activeQuote = false;
            final StringBuilder sb = new StringBuilder();
            while (this.currentIdx < FastDateParser.this.pattern.length()) {
                final char c = FastDateParser.this.pattern.charAt(this.currentIdx);
                if (!activeQuote && isFormatLetter(c)) {
                    break;
                }
                if (c == '\'' && (++this.currentIdx == FastDateParser.this.pattern.length() || FastDateParser.this.pattern.charAt(this.currentIdx) != '\'')) {
                    activeQuote = !activeQuote;
                }
                else {
                    ++this.currentIdx;
                    sb.append(c);
                }
            }
            if (activeQuote) {
                throw new IllegalArgumentException("Unterminated quote");
            }
            final String formatField = sb.toString();
            return new StrategyAndWidth(new CopyQuotedStrategy(formatField), formatField.length());
        }
    }
    
    private abstract static class Strategy
    {
        boolean isNumber() {
            return false;
        }
        
        abstract boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth);
    }
    
    private abstract static class PatternStrategy extends Strategy
    {
        private Pattern pattern;
        
        void createPattern(final StringBuilder regex) {
            this.createPattern(regex.toString());
        }
        
        void createPattern(final String regex) {
            this.pattern = Pattern.compile(regex);
        }
        
        @Override
        boolean isNumber() {
            return false;
        }
        
        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            final Matcher matcher = this.pattern.matcher(source.substring(pos.getIndex()));
            if (!matcher.lookingAt()) {
                pos.setErrorIndex(pos.getIndex());
                return false;
            }
            pos.setIndex(pos.getIndex() + matcher.end(1));
            this.setCalendar(parser, calendar, matcher.group(1));
            return true;
        }
        
        abstract void setCalendar(final FastDateParser parser, final Calendar cal, final String value);
    }
    
    private static class CopyQuotedStrategy extends Strategy
    {
        private final String formatField;
        
        CopyQuotedStrategy(final String formatField) {
            this.formatField = formatField;
        }
        
        @Override
        boolean isNumber() {
            return false;
        }
        
        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            for (int idx = 0; idx < this.formatField.length(); ++idx) {
                final int sIdx = idx + pos.getIndex();
                if (sIdx == source.length()) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
                if (this.formatField.charAt(idx) != source.charAt(sIdx)) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
            }
            pos.setIndex(this.formatField.length() + pos.getIndex());
            return true;
        }
    }
    
    private static class CaseInsensitiveTextStrategy extends PatternStrategy
    {
        private final int field;
        final Locale locale;
        private final Map<String, Integer> lKeyValues;
        
        CaseInsensitiveTextStrategy(final int field, final Calendar definingCalendar, final Locale locale) {
            this.field = field;
            this.locale = locale;
            final StringBuilder regex = new StringBuilder();
            regex.append("((?iu)");
            this.lKeyValues = appendDisplayNames(definingCalendar, locale, field, regex);
            regex.setLength(regex.length() - 1);
            regex.append(")");
            this.createPattern(regex);
        }
        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            final Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
            cal.set(this.field, iVal);
        }
    }
    
    private static class NumberStrategy extends Strategy
    {
        private final int field;
        
        NumberStrategy(final int field) {
            this.field = field;
        }
        
        @Override
        boolean isNumber() {
            return true;
        }
        
        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            int idx = pos.getIndex();
            int last = source.length();
            if (maxWidth == 0) {
                while (idx < last) {
                    final char c = source.charAt(idx);
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                    ++idx;
                }
                pos.setIndex(idx);
            }
            else {
                final int end = idx + maxWidth;
                if (last > end) {
                    last = end;
                }
            }
            while (idx < last) {
                final char c = source.charAt(idx);
                if (!Character.isDigit(c)) {
                    break;
                }
                ++idx;
            }
            if (pos.getIndex() == idx) {
                pos.setErrorIndex(idx);
                return false;
            }
            final int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
            pos.setIndex(idx);
            calendar.set(this.field, this.modify(parser, value));
            return true;
        }
        
        int modify(final FastDateParser parser, final int iValue) {
            return iValue;
        }
    }
    
    static class TimeZoneStrategy extends PatternStrategy
    {
        private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
        private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
        private final Locale locale;
        private final Map<String, TzInfo> tzNames;
        private static final int ID = 0;
        
        TimeZoneStrategy(final Locale locale) {
            this.tzNames = new HashMap<String, TzInfo>();
            this.locale = locale;
            final StringBuilder sb = new StringBuilder();
            sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
            final Set<String> sorted = new TreeSet<String>(FastDateParser.LONGER_FIRST_LOWERCASE);
            final String[][] zoneStrings;
            final String[][] zones = zoneStrings = DateFormatSymbols.getInstance(locale).getZoneStrings();
            for (final String[] zoneNames : zoneStrings) {
                final String tzId = zoneNames[0];
                if (!tzId.equalsIgnoreCase("GMT")) {
                    final TimeZone tz = TimeZone.getTimeZone(tzId);
                    TzInfo tzInfo;
                    final TzInfo standard = tzInfo = new TzInfo(tz, false);
                    for (int i = 1; i < zoneNames.length; ++i) {
                        switch (i) {
                            case 3: {
                                tzInfo = new TzInfo(tz, true);
                                break;
                            }
                            case 5: {
                                tzInfo = standard;
                                break;
                            }
                        }
                        if (zoneNames[i] != null) {
                            final String key = zoneNames[i].toLowerCase(locale);
                            if (sorted.add(key)) {
                                this.tzNames.put(key, tzInfo);
                            }
                        }
                    }
                }
            }
            for (final String zoneName : sorted) {
                simpleQuote(sb.append('|'), zoneName);
            }
            sb.append(")");
            this.createPattern(sb);
        }
        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            if (value.charAt(0) == '+' || value.charAt(0) == '-') {
                final TimeZone tz = TimeZone.getTimeZone("GMT" + value);
                cal.setTimeZone(tz);
            }
            else if (value.regionMatches(true, 0, "GMT", 0, 3)) {
                final TimeZone tz = TimeZone.getTimeZone(value.toUpperCase());
                cal.setTimeZone(tz);
            }
            else {
                final TzInfo tzInfo = this.tzNames.get(value.toLowerCase(this.locale));
                cal.set(16, tzInfo.dstOffset);
                cal.set(15, tzInfo.zone.getRawOffset());
            }
        }
        
        private static class TzInfo
        {
            TimeZone zone;
            int dstOffset;
            
            TzInfo(final TimeZone tz, final boolean useDst) {
                this.zone = tz;
                this.dstOffset = (useDst ? tz.getDSTSavings() : 0);
            }
        }
    }
    
    private static class ISO8601TimeZoneStrategy extends PatternStrategy
    {
        private static final Strategy ISO_8601_1_STRATEGY;
        private static final Strategy ISO_8601_2_STRATEGY;
        private static final Strategy ISO_8601_3_STRATEGY;
        
        ISO8601TimeZoneStrategy(final String pattern) {
            this.createPattern(pattern);
        }
        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            if (value.equals("Z")) {
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            else {
                cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
            }
        }
        
        static Strategy getStrategy(final int tokenLen) {
            switch (tokenLen) {
                case 1: {
                    return ISO8601TimeZoneStrategy.ISO_8601_1_STRATEGY;
                }
                case 2: {
                    return ISO8601TimeZoneStrategy.ISO_8601_2_STRATEGY;
                }
                case 3: {
                    return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
                }
                default: {
                    throw new IllegalArgumentException("invalid number of X");
                }
            }
        }
        
        static {
            ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
            ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
            ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
        }
    }
}
