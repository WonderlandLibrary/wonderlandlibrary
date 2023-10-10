/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.DontCareFieldPosition;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.impl.number.LongNameHandler;
import com.ibm.icu.number.FormattedNumber;
import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.number.Precision;
import com.ibm.icu.text.CurrencyFormat;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.ListFormatter;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.NumberingSystem;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.text.TimeUnitFormat;
import com.ibm.icu.text.UFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ICUUncheckedIOException;
import com.ibm.icu.util.Measure;
import com.ibm.icu.util.MeasureUnit;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;

public class MeasureFormat
extends UFormat {
    static final long serialVersionUID = -7182021401701778240L;
    private final transient FormatWidth formatWidth;
    private final transient PluralRules rules;
    private final transient NumericFormatters numericFormatters;
    private final transient NumberFormat numberFormat;
    private final transient LocalizedNumberFormatter numberFormatter;
    private static final SimpleCache<ULocale, NumericFormatters> localeToNumericDurationFormatters = new SimpleCache();
    private static final Map<MeasureUnit, Integer> hmsTo012 = new HashMap<MeasureUnit, Integer>();
    private static final int MEASURE_FORMAT = 0;
    private static final int TIME_UNIT_FORMAT = 1;
    private static final int CURRENCY_FORMAT = 2;
    static final int NUMBER_FORMATTER_STANDARD = 1;
    static final int NUMBER_FORMATTER_CURRENCY = 2;
    static final int NUMBER_FORMATTER_INTEGER = 3;
    private transient NumberFormatterCacheEntry formatter1 = null;
    private transient NumberFormatterCacheEntry formatter2 = null;
    private transient NumberFormatterCacheEntry formatter3 = null;
    private static final Map<ULocale, String> localeIdToRangeFormat;

    public static MeasureFormat getInstance(ULocale locale, FormatWidth formatWidth) {
        return MeasureFormat.getInstance(locale, formatWidth, NumberFormat.getInstance(locale));
    }

    public static MeasureFormat getInstance(Locale locale, FormatWidth formatWidth) {
        return MeasureFormat.getInstance(ULocale.forLocale(locale), formatWidth);
    }

    public static MeasureFormat getInstance(ULocale locale, FormatWidth formatWidth, NumberFormat format) {
        return new MeasureFormat(locale, formatWidth, format, null, null);
    }

    public static MeasureFormat getInstance(Locale locale, FormatWidth formatWidth, NumberFormat format) {
        return MeasureFormat.getInstance(ULocale.forLocale(locale), formatWidth, format);
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition fpos) {
        int prevLength = toAppendTo.length();
        fpos.setBeginIndex(0);
        fpos.setEndIndex(0);
        if (obj instanceof Collection) {
            Collection coll = (Collection)obj;
            Measure[] measures = new Measure[coll.size()];
            int idx = 0;
            for (Object o2 : coll) {
                if (!(o2 instanceof Measure)) {
                    throw new IllegalArgumentException(obj.toString());
                }
                measures[idx++] = (Measure)o2;
            }
            this.formatMeasuresInternal(toAppendTo, fpos, measures);
        } else if (obj instanceof Measure[]) {
            this.formatMeasuresInternal(toAppendTo, fpos, (Measure[])obj);
        } else if (obj instanceof Measure) {
            FormattedNumber result = this.formatMeasure((Measure)obj);
            result.populateFieldPosition(fpos);
            result.appendTo(toAppendTo);
        } else {
            throw new IllegalArgumentException(obj.toString());
        }
        if (prevLength > 0 && fpos.getEndIndex() != 0) {
            fpos.setBeginIndex(fpos.getBeginIndex() + prevLength);
            fpos.setEndIndex(fpos.getEndIndex() + prevLength);
        }
        return toAppendTo;
    }

    @Override
    public Measure parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

    public final String formatMeasures(Measure ... measures) {
        return this.formatMeasures(new StringBuilder(), DontCareFieldPosition.INSTANCE, measures).toString();
    }

    public StringBuilder formatMeasurePerUnit(Measure measure, MeasureUnit perUnit, StringBuilder appendTo, FieldPosition pos) {
        FormattedNumber result = this.getUnitFormatterFromCache(1, measure.getUnit(), perUnit).format(measure.getNumber());
        DecimalFormat.fieldPositionHelper(result, pos, appendTo.length());
        result.appendTo(appendTo);
        return appendTo;
    }

    public StringBuilder formatMeasures(StringBuilder appendTo, FieldPosition fpos, Measure ... measures) {
        int prevLength = appendTo.length();
        this.formatMeasuresInternal(appendTo, fpos, measures);
        if (prevLength > 0 && fpos.getEndIndex() > 0) {
            fpos.setBeginIndex(fpos.getBeginIndex() + prevLength);
            fpos.setEndIndex(fpos.getEndIndex() + prevLength);
        }
        return appendTo;
    }

    private void formatMeasuresInternal(Appendable appendTo, FieldPosition fieldPosition, Measure ... measures) {
        Number[] hms;
        if (measures.length == 0) {
            return;
        }
        if (measures.length == 1) {
            FormattedNumber result = this.formatMeasure(measures[0]);
            result.populateFieldPosition(fieldPosition);
            result.appendTo(appendTo);
            return;
        }
        if (this.formatWidth == FormatWidth.NUMERIC && (hms = MeasureFormat.toHMS(measures)) != null) {
            this.formatNumeric(hms, appendTo);
            return;
        }
        ListFormatter listFormatter = ListFormatter.getInstance(this.getLocale(), this.formatWidth.getListFormatterStyle());
        if (fieldPosition != DontCareFieldPosition.INSTANCE) {
            this.formatMeasuresSlowTrack(listFormatter, appendTo, fieldPosition, measures);
            return;
        }
        String[] results = new String[measures.length];
        for (int i2 = 0; i2 < measures.length; ++i2) {
            results[i2] = i2 == measures.length - 1 ? this.formatMeasure(measures[i2]).toString() : this.formatMeasureInteger(measures[i2]).toString();
        }
        ListFormatter.FormattedListBuilder builder = listFormatter.format(Arrays.asList(results), -1);
        builder.appendTo(appendTo);
    }

    public String getUnitDisplayName(MeasureUnit unit) {
        return LongNameHandler.getUnitDisplayName(this.getLocale(), unit, this.formatWidth.unitWidth);
    }

    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MeasureFormat)) {
            return false;
        }
        MeasureFormat rhs = (MeasureFormat)other;
        return this.getWidth() == rhs.getWidth() && this.getLocale().equals(rhs.getLocale()) && this.getNumberFormatInternal().equals(rhs.getNumberFormatInternal());
    }

    public final int hashCode() {
        return (this.getLocale().hashCode() * 31 + this.getNumberFormatInternal().hashCode()) * 31 + this.getWidth().hashCode();
    }

    public FormatWidth getWidth() {
        return this.formatWidth;
    }

    public final ULocale getLocale() {
        return this.getLocale(ULocale.VALID_LOCALE);
    }

    public NumberFormat getNumberFormat() {
        return (NumberFormat)this.numberFormat.clone();
    }

    NumberFormat getNumberFormatInternal() {
        return this.numberFormat;
    }

    public static MeasureFormat getCurrencyFormat(ULocale locale) {
        return new CurrencyFormat(locale);
    }

    public static MeasureFormat getCurrencyFormat(Locale locale) {
        return MeasureFormat.getCurrencyFormat(ULocale.forLocale(locale));
    }

    public static MeasureFormat getCurrencyFormat() {
        return MeasureFormat.getCurrencyFormat(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    MeasureFormat withLocale(ULocale locale) {
        return MeasureFormat.getInstance(locale, this.getWidth());
    }

    MeasureFormat withNumberFormat(NumberFormat format) {
        return new MeasureFormat(this.getLocale(), this.formatWidth, format, this.rules, this.numericFormatters);
    }

    MeasureFormat(ULocale locale, FormatWidth formatWidth) {
        this(locale, formatWidth, null, null, null);
    }

    private MeasureFormat(ULocale locale, FormatWidth formatWidth, NumberFormat numberFormat, PluralRules rules, NumericFormatters formatters) {
        this.setLocale(locale, locale);
        this.formatWidth = formatWidth;
        if (rules == null) {
            rules = PluralRules.forLocale(locale);
        }
        this.rules = rules;
        numberFormat = numberFormat == null ? NumberFormat.getInstance(locale) : (NumberFormat)numberFormat.clone();
        this.numberFormat = numberFormat;
        if (formatters == null && formatWidth == FormatWidth.NUMERIC && (formatters = localeToNumericDurationFormatters.get(locale)) == null) {
            formatters = MeasureFormat.loadNumericFormatters(locale);
            localeToNumericDurationFormatters.put(locale, formatters);
        }
        this.numericFormatters = formatters;
        if (!(numberFormat instanceof DecimalFormat)) {
            throw new IllegalArgumentException();
        }
        this.numberFormatter = (LocalizedNumberFormatter)((DecimalFormat)numberFormat).toNumberFormatter().unitWidth(formatWidth.unitWidth);
    }

    MeasureFormat(ULocale locale, FormatWidth formatWidth, NumberFormat numberFormat, PluralRules rules) {
        this(locale, formatWidth, numberFormat, rules, null);
        if (formatWidth == FormatWidth.NUMERIC) {
            throw new IllegalArgumentException("The format width 'numeric' is not allowed by this constructor");
        }
    }

    private static NumericFormatters loadNumericFormatters(ULocale locale) {
        ICUResourceBundle r2 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/unit", locale);
        return new NumericFormatters(MeasureFormat.loadNumericDurationFormat(r2, "hm"), MeasureFormat.loadNumericDurationFormat(r2, "ms"), MeasureFormat.loadNumericDurationFormat(r2, "hms"));
    }

    private synchronized LocalizedNumberFormatter getUnitFormatterFromCache(int type, MeasureUnit unit, MeasureUnit perUnit) {
        LocalizedNumberFormatter formatter;
        if (this.formatter1 != null) {
            if (this.formatter1.type == type && this.formatter1.unit == unit && this.formatter1.perUnit == perUnit) {
                return this.formatter1.formatter;
            }
            if (this.formatter2 != null) {
                if (this.formatter2.type == type && this.formatter2.unit == unit && this.formatter2.perUnit == perUnit) {
                    return this.formatter2.formatter;
                }
                if (this.formatter3 != null && this.formatter3.type == type && this.formatter3.unit == unit && this.formatter3.perUnit == perUnit) {
                    return this.formatter3.formatter;
                }
            }
        }
        if (type == 1) {
            formatter = (LocalizedNumberFormatter)((LocalizedNumberFormatter)((LocalizedNumberFormatter)this.getNumberFormatter().unit(unit)).perUnit(perUnit)).unitWidth(this.formatWidth.unitWidth);
        } else if (type == 2) {
            formatter = (LocalizedNumberFormatter)((LocalizedNumberFormatter)((LocalizedNumberFormatter)NumberFormatter.withLocale(this.getLocale()).unit(unit)).perUnit(perUnit)).unitWidth(this.formatWidth.currencyWidth);
        } else {
            assert (type == 3);
            formatter = (LocalizedNumberFormatter)((LocalizedNumberFormatter)((LocalizedNumberFormatter)((LocalizedNumberFormatter)this.getNumberFormatter().unit(unit)).perUnit(perUnit)).unitWidth(this.formatWidth.unitWidth)).rounding(Precision.integer().withMode(RoundingMode.DOWN));
        }
        this.formatter3 = this.formatter2;
        this.formatter2 = this.formatter1;
        this.formatter1 = new NumberFormatterCacheEntry();
        this.formatter1.type = type;
        this.formatter1.unit = unit;
        this.formatter1.perUnit = perUnit;
        this.formatter1.formatter = formatter;
        return formatter;
    }

    synchronized void clearCache() {
        this.formatter1 = null;
        this.formatter2 = null;
        this.formatter3 = null;
    }

    LocalizedNumberFormatter getNumberFormatter() {
        return this.numberFormatter;
    }

    private FormattedNumber formatMeasure(Measure measure) {
        MeasureUnit unit = measure.getUnit();
        if (unit instanceof Currency) {
            return this.getUnitFormatterFromCache(2, unit, null).format(measure.getNumber());
        }
        return this.getUnitFormatterFromCache(1, unit, null).format(measure.getNumber());
    }

    private FormattedNumber formatMeasureInteger(Measure measure) {
        return this.getUnitFormatterFromCache(3, measure.getUnit(), null).format(measure.getNumber());
    }

    private void formatMeasuresSlowTrack(ListFormatter listFormatter, Appendable appendTo, FieldPosition fieldPosition, Measure ... measures) {
        String[] results = new String[measures.length];
        FieldPosition fpos = new FieldPosition(fieldPosition.getFieldAttribute(), fieldPosition.getField());
        int fieldPositionFoundIndex = -1;
        for (int i2 = 0; i2 < measures.length; ++i2) {
            FormattedNumber result = i2 == measures.length - 1 ? this.formatMeasure(measures[i2]) : this.formatMeasureInteger(measures[i2]);
            if (fieldPositionFoundIndex == -1) {
                result.populateFieldPosition(fpos);
                if (fpos.getEndIndex() != 0) {
                    fieldPositionFoundIndex = i2;
                }
            }
            results[i2] = result.toString();
        }
        ListFormatter.FormattedListBuilder builder = listFormatter.format(Arrays.asList(results), fieldPositionFoundIndex);
        if (builder.getOffset() != -1) {
            fieldPosition.setBeginIndex(fpos.getBeginIndex() + builder.getOffset());
            fieldPosition.setEndIndex(fpos.getEndIndex() + builder.getOffset());
        }
        builder.appendTo(appendTo);
    }

    private static DateFormat loadNumericDurationFormat(ICUResourceBundle r2, String type) {
        r2 = r2.getWithFallback(String.format("durationUnits/%s", type));
        SimpleDateFormat result = new SimpleDateFormat(r2.getString().replace("h", "H"));
        result.setTimeZone(TimeZone.GMT_ZONE);
        return result;
    }

    private static Number[] toHMS(Measure[] measures) {
        Number[] result = new Number[3];
        int lastIdx = -1;
        for (Measure m2 : measures) {
            if (m2.getNumber().doubleValue() < 0.0) {
                return null;
            }
            Integer idxObj = hmsTo012.get(m2.getUnit());
            if (idxObj == null) {
                return null;
            }
            int idx = idxObj;
            if (idx <= lastIdx) {
                return null;
            }
            lastIdx = idx;
            result[idx] = m2.getNumber();
        }
        return result;
    }

    private void formatNumeric(Number[] hms, Appendable appendable) {
        int startIndex = -1;
        int endIndex = -1;
        for (int i2 = 0; i2 < hms.length; ++i2) {
            if (hms[i2] != null) {
                endIndex = i2;
                if (startIndex != -1) continue;
                startIndex = endIndex;
                continue;
            }
            hms[i2] = 0;
        }
        long millis = (long)(((Math.floor(hms[0].doubleValue()) * 60.0 + Math.floor(hms[1].doubleValue())) * 60.0 + Math.floor(hms[2].doubleValue())) * 1000.0);
        Date d2 = new Date(millis);
        if (startIndex == 0 && endIndex == 2) {
            this.formatNumeric(d2, this.numericFormatters.getHourMinuteSecond(), DateFormat.Field.SECOND, hms[endIndex], appendable);
        } else if (startIndex == 1 && endIndex == 2) {
            this.formatNumeric(d2, this.numericFormatters.getMinuteSecond(), DateFormat.Field.SECOND, hms[endIndex], appendable);
        } else if (startIndex == 0 && endIndex == 1) {
            this.formatNumeric(d2, this.numericFormatters.getHourMinute(), DateFormat.Field.MINUTE, hms[endIndex], appendable);
        } else {
            throw new IllegalStateException();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void formatNumeric(Date duration, DateFormat formatter, DateFormat.Field smallestField, Number smallestAmount, Appendable appendTo) {
        String draft;
        FieldPosition intFieldPosition = new FieldPosition(0);
        FormattedNumber result = this.getNumberFormatter().format(smallestAmount);
        result.populateFieldPosition(intFieldPosition);
        String smallestAmountFormatted = result.toString();
        if (intFieldPosition.getBeginIndex() == 0 && intFieldPosition.getEndIndex() == 0) {
            throw new IllegalStateException();
        }
        FieldPosition smallestFieldPosition = new FieldPosition(smallestField);
        DateFormat dateFormat = formatter;
        synchronized (dateFormat) {
            draft = formatter.format(duration, new StringBuffer(), smallestFieldPosition).toString();
        }
        try {
            if (smallestFieldPosition.getBeginIndex() != 0 || smallestFieldPosition.getEndIndex() != 0) {
                appendTo.append(draft, 0, smallestFieldPosition.getBeginIndex());
                appendTo.append(smallestAmountFormatted, 0, intFieldPosition.getBeginIndex());
                appendTo.append(draft, smallestFieldPosition.getBeginIndex(), smallestFieldPosition.getEndIndex());
                appendTo.append(smallestAmountFormatted, intFieldPosition.getEndIndex(), smallestAmountFormatted.length());
                appendTo.append(draft, smallestFieldPosition.getEndIndex(), draft.length());
            } else {
                appendTo.append(draft);
            }
        }
        catch (IOException e2) {
            throw new ICUUncheckedIOException(e2);
        }
    }

    Object toTimeUnitProxy() {
        return new MeasureProxy(this.getLocale(), this.formatWidth, this.getNumberFormatInternal(), 1);
    }

    Object toCurrencyProxy() {
        return new MeasureProxy(this.getLocale(), this.formatWidth, this.getNumberFormatInternal(), 2);
    }

    private Object writeReplace() throws ObjectStreamException {
        return new MeasureProxy(this.getLocale(), this.formatWidth, this.getNumberFormatInternal(), 0);
    }

    private static FormatWidth fromFormatWidthOrdinal(int ordinal) {
        FormatWidth[] values = FormatWidth.values();
        if (ordinal < 0 || ordinal >= values.length) {
            return FormatWidth.SHORT;
        }
        return values[ordinal];
    }

    @Deprecated
    public static String getRangeFormat(ULocale forLocale, FormatWidth width) {
        if (forLocale.getLanguage().equals("fr")) {
            return MeasureFormat.getRangeFormat(ULocale.ROOT, width);
        }
        String result = localeIdToRangeFormat.get(forLocale);
        if (result == null) {
            ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", forLocale);
            ULocale realLocale = rb.getULocale();
            if (!forLocale.equals(realLocale) && (result = localeIdToRangeFormat.get(forLocale)) != null) {
                localeIdToRangeFormat.put(forLocale, result);
                return result;
            }
            NumberingSystem ns = NumberingSystem.getInstance(forLocale);
            String resultString = null;
            try {
                resultString = rb.getStringWithFallback("NumberElements/" + ns.getName() + "/miscPatterns/range");
            }
            catch (MissingResourceException ex) {
                resultString = rb.getStringWithFallback("NumberElements/latn/patterns/range");
            }
            result = SimpleFormatterImpl.compileToStringMinMaxArguments(resultString, new StringBuilder(), 2, 2);
            localeIdToRangeFormat.put(forLocale, result);
            if (!forLocale.equals(realLocale)) {
                localeIdToRangeFormat.put(realLocale, result);
            }
        }
        return result;
    }

    static {
        hmsTo012.put(MeasureUnit.HOUR, 0);
        hmsTo012.put(MeasureUnit.MINUTE, 1);
        hmsTo012.put(MeasureUnit.SECOND, 2);
        localeIdToRangeFormat = new ConcurrentHashMap<ULocale, String>();
    }

    static class MeasureProxy
    implements Externalizable {
        private static final long serialVersionUID = -6033308329886716770L;
        private ULocale locale;
        private FormatWidth formatWidth;
        private NumberFormat numberFormat;
        private int subClass;
        private HashMap<Object, Object> keyValues;

        public MeasureProxy(ULocale locale, FormatWidth width, NumberFormat numberFormat, int subClass) {
            this.locale = locale;
            this.formatWidth = width;
            this.numberFormat = numberFormat;
            this.subClass = subClass;
            this.keyValues = new HashMap();
        }

        public MeasureProxy() {
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(0);
            out.writeUTF(this.locale.toLanguageTag());
            out.writeByte(this.formatWidth.ordinal());
            out.writeObject(this.numberFormat);
            out.writeByte(this.subClass);
            out.writeObject(this.keyValues);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            in.readByte();
            this.locale = ULocale.forLanguageTag(in.readUTF());
            this.formatWidth = MeasureFormat.fromFormatWidthOrdinal(in.readByte() & 0xFF);
            this.numberFormat = (NumberFormat)in.readObject();
            if (this.numberFormat == null) {
                throw new InvalidObjectException("Missing number format.");
            }
            this.subClass = in.readByte() & 0xFF;
            this.keyValues = (HashMap)in.readObject();
            if (this.keyValues == null) {
                throw new InvalidObjectException("Missing optional values map.");
            }
        }

        private TimeUnitFormat createTimeUnitFormat() throws InvalidObjectException {
            int style;
            if (this.formatWidth == FormatWidth.WIDE) {
                style = 0;
            } else if (this.formatWidth == FormatWidth.SHORT) {
                style = 1;
            } else {
                throw new InvalidObjectException("Bad width: " + (Object)((Object)this.formatWidth));
            }
            TimeUnitFormat result = new TimeUnitFormat(this.locale, style);
            result.setNumberFormat(this.numberFormat);
            return result;
        }

        private Object readResolve() throws ObjectStreamException {
            switch (this.subClass) {
                case 0: {
                    return MeasureFormat.getInstance(this.locale, this.formatWidth, this.numberFormat);
                }
                case 1: {
                    return this.createTimeUnitFormat();
                }
                case 2: {
                    return MeasureFormat.getCurrencyFormat(this.locale);
                }
            }
            throw new InvalidObjectException("Unknown subclass: " + this.subClass);
        }
    }

    static class NumberFormatterCacheEntry {
        int type;
        MeasureUnit unit;
        MeasureUnit perUnit;
        LocalizedNumberFormatter formatter;

        NumberFormatterCacheEntry() {
        }
    }

    static class NumericFormatters {
        private DateFormat hourMinute;
        private DateFormat minuteSecond;
        private DateFormat hourMinuteSecond;

        public NumericFormatters(DateFormat hourMinute, DateFormat minuteSecond, DateFormat hourMinuteSecond) {
            this.hourMinute = hourMinute;
            this.minuteSecond = minuteSecond;
            this.hourMinuteSecond = hourMinuteSecond;
        }

        public DateFormat getHourMinute() {
            return this.hourMinute;
        }

        public DateFormat getMinuteSecond() {
            return this.minuteSecond;
        }

        public DateFormat getHourMinuteSecond() {
            return this.hourMinuteSecond;
        }
    }

    public static enum FormatWidth {
        WIDE(ListFormatter.Style.DURATION, NumberFormatter.UnitWidth.FULL_NAME, NumberFormatter.UnitWidth.FULL_NAME),
        SHORT(ListFormatter.Style.DURATION_SHORT, NumberFormatter.UnitWidth.SHORT, NumberFormatter.UnitWidth.ISO_CODE),
        NARROW(ListFormatter.Style.DURATION_NARROW, NumberFormatter.UnitWidth.NARROW, NumberFormatter.UnitWidth.SHORT),
        NUMERIC(ListFormatter.Style.DURATION_NARROW, NumberFormatter.UnitWidth.NARROW, NumberFormatter.UnitWidth.SHORT),
        DEFAULT_CURRENCY(ListFormatter.Style.DURATION, NumberFormatter.UnitWidth.FULL_NAME, NumberFormatter.UnitWidth.SHORT);

        private final ListFormatter.Style listFormatterStyle;
        final NumberFormatter.UnitWidth unitWidth;
        final NumberFormatter.UnitWidth currencyWidth;

        private FormatWidth(ListFormatter.Style style, NumberFormatter.UnitWidth unitWidth, NumberFormatter.UnitWidth currencyWidth) {
            this.listFormatterStyle = style;
            this.unitWidth = unitWidth;
            this.currencyWidth = currencyWidth;
        }

        ListFormatter.Style getListFormatterStyle() {
            return this.listFormatterStyle;
        }
    }
}

