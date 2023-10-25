/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.CompactDecimalDataCache;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CompactDecimalFormat
extends DecimalFormat {
    private static final long serialVersionUID = 4716293295276629682L;
    private static final int POSITIVE_PREFIX = 0;
    private static final int POSITIVE_SUFFIX = 1;
    private static final int AFFIX_SIZE = 2;
    private static final CompactDecimalDataCache cache = new CompactDecimalDataCache();
    private final Map<String, DecimalFormat.Unit[]> units;
    private final long[] divisor;
    private final String[] currencyAffixes;
    private final PluralRules pluralRules;

    public static CompactDecimalFormat getInstance(ULocale locale, CompactStyle style) {
        return new CompactDecimalFormat(locale, style);
    }

    public static CompactDecimalFormat getInstance(Locale locale, CompactStyle style) {
        return new CompactDecimalFormat(ULocale.forLocale(locale), style);
    }

    CompactDecimalFormat(ULocale locale, CompactStyle style) {
        DecimalFormat format = (DecimalFormat)NumberFormat.getInstance(locale);
        CompactDecimalDataCache.Data data = this.getData(locale, style);
        this.units = data.units;
        this.divisor = data.divisors;
        this.applyPattern(format.toPattern());
        this.setDecimalFormatSymbols(format.getDecimalFormatSymbols());
        this.setMaximumSignificantDigits(3);
        this.setSignificantDigitsUsed(true);
        if (style == CompactStyle.SHORT) {
            this.setGroupingUsed(false);
        }
        this.pluralRules = PluralRules.forLocale(locale);
        DecimalFormat currencyFormat = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
        this.currencyAffixes = new String[2];
        this.currencyAffixes[0] = currencyFormat.getPositivePrefix();
        this.currencyAffixes[1] = currencyFormat.getPositiveSuffix();
        this.setCurrency(null);
    }

    public CompactDecimalFormat(String pattern, DecimalFormatSymbols formatSymbols, String[] prefix, String[] suffix, long[] divisor, Collection<String> debugCreationErrors, CompactStyle style, String[] currencyAffixes) {
        if (prefix.length < 15) {
            this.recordError(debugCreationErrors, "Must have at least 15 prefix items.");
        }
        if (prefix.length != suffix.length || prefix.length != divisor.length) {
            this.recordError(debugCreationErrors, "Prefix, suffix, and divisor arrays must have the same length.");
        }
        long oldDivisor = 0L;
        HashMap<String, Integer> seen = new HashMap<String, Integer>();
        for (int i = 0; i < prefix.length; ++i) {
            String key;
            Integer old;
            long roundTrip;
            int log;
            if (prefix[i] == null || suffix[i] == null) {
                this.recordError(debugCreationErrors, "Prefix or suffix is null for " + i);
            }
            if ((log = (int)Math.log10(divisor[i])) > i) {
                this.recordError(debugCreationErrors, "Divisor[" + i + "] must be less than or equal to 10^" + i + ", but is: " + divisor[i]);
            }
            if ((roundTrip = (long)Math.pow(10.0, log)) != divisor[i]) {
                this.recordError(debugCreationErrors, "Divisor[" + i + "] must be a power of 10, but is: " + divisor[i]);
            }
            if ((old = (Integer)seen.get(key = prefix[i] + "\uffff" + suffix[i] + "\uffff" + (i - log))) != null) {
                this.recordError(debugCreationErrors, "Collision between values for " + i + " and " + old + " for [prefix/suffix/index-log(divisor)" + key.replace('\uffff', ';'));
            } else {
                seen.put(key, i);
            }
            if (divisor[i] < oldDivisor) {
                this.recordError(debugCreationErrors, "Bad divisor, the divisor for 10E" + i + "(" + divisor[i] + ") is less than the divisor for the divisor for 10E" + (i - 1) + "(" + oldDivisor + ")");
            }
            oldDivisor = divisor[i];
        }
        this.units = this.otherPluralVariant(prefix, suffix);
        this.divisor = (long[])divisor.clone();
        this.applyPattern(pattern);
        this.setDecimalFormatSymbols(formatSymbols);
        this.setMaximumSignificantDigits(2);
        this.setSignificantDigitsUsed(true);
        this.setGroupingUsed(false);
        this.currencyAffixes = (String[])currencyAffixes.clone();
        this.pluralRules = null;
        this.setCurrency(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        CompactDecimalFormat other = (CompactDecimalFormat)obj;
        return this.mapsAreEqual(this.units, other.units) && Arrays.equals(this.divisor, other.divisor) && Arrays.equals(this.currencyAffixes, other.currencyAffixes) && this.pluralRules.equals(other.pluralRules);
    }

    private boolean mapsAreEqual(Map<String, DecimalFormat.Unit[]> lhs, Map<String, DecimalFormat.Unit[]> rhs) {
        if (lhs.size() != rhs.size()) {
            return false;
        }
        for (Map.Entry<String, DecimalFormat.Unit[]> entry : lhs.entrySet()) {
            Object[] value = rhs.get(entry.getKey());
            if (value != null && Arrays.equals(entry.getValue(), value)) continue;
            return false;
        }
        return true;
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        Amount amount = this.toAmount(number);
        DecimalFormat.Unit unit = amount.getUnit();
        unit.writePrefix(toAppendTo);
        super.format(amount.getQty(), toAppendTo, pos);
        unit.writeSuffix(toAppendTo);
        return toAppendTo;
    }

    @Override
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        if (!(obj instanceof Number)) {
            throw new IllegalArgumentException();
        }
        Number number = (Number)obj;
        Amount amount = this.toAmount(number.doubleValue());
        return super.formatToCharacterIterator(amount.getQty(), amount.getUnit());
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format((double)number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(BigInteger number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }

    @Override
    public StringBuffer format(BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }

    @Override
    public StringBuffer format(com.ibm.icu.math.BigDecimal number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }

    @Override
    public Number parse(String text, ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException();
    }

    private void readObject(ObjectInputStream in) throws IOException {
        throw new NotSerializableException();
    }

    private Amount toAmount(double number) {
        int base;
        boolean negative = this.isNumberNegative(number);
        int n = base = (number = this.adjustNumberAsInFormatting(number)) <= 1.0 ? 0 : (int)Math.log10(number);
        if (base >= 15) {
            base = 14;
        }
        String pluralVariant = this.getPluralForm(number /= (double)this.divisor[base]);
        if (negative) {
            number = -number;
        }
        return new Amount(number, CompactDecimalDataCache.getUnit(this.units, pluralVariant, base));
    }

    private void recordError(Collection<String> creationErrors, String errorMessage) {
        if (creationErrors == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        creationErrors.add(errorMessage);
    }

    private Map<String, DecimalFormat.Unit[]> otherPluralVariant(String[] prefix, String[] suffix) {
        HashMap<String, DecimalFormat.Unit[]> result = new HashMap<String, DecimalFormat.Unit[]>();
        DecimalFormat.Unit[] units = new DecimalFormat.Unit[prefix.length];
        for (int i = 0; i < units.length; ++i) {
            units[i] = new DecimalFormat.Unit(prefix[i], suffix[i]);
        }
        result.put("other", units);
        return result;
    }

    private String getPluralForm(double number) {
        if (this.pluralRules == null) {
            return "other";
        }
        return this.pluralRules.select(number);
    }

    private CompactDecimalDataCache.Data getData(ULocale locale, CompactStyle style) {
        CompactDecimalDataCache.DataBundle bundle = cache.get(locale);
        switch (style) {
            case SHORT: {
                return bundle.shortData;
            }
            case LONG: {
                return bundle.longData;
            }
        }
        return bundle.shortData;
    }

    private static class Amount {
        private final double qty;
        private final DecimalFormat.Unit unit;

        public Amount(double qty, DecimalFormat.Unit unit) {
            this.qty = qty;
            this.unit = unit;
        }

        public double getQty() {
            return this.qty;
        }

        public DecimalFormat.Unit getUnit() {
            return this.unit;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum CompactStyle {
        SHORT,
        LONG;

    }
}

