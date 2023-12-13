/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.number.FormattedNumber;
import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.MessagePattern;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.text.RbnfLenientScanner;
import com.ibm.icu.text.UFormat;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Map;

public class PluralFormat
extends UFormat {
    private static final long serialVersionUID = 1L;
    private ULocale ulocale = null;
    private PluralRules pluralRules = null;
    private String pattern = null;
    private transient MessagePattern msgPattern;
    private Map<String, String> parsedValues = null;
    private NumberFormat numberFormat = null;
    private transient double offset = 0.0;
    private transient PluralSelectorAdapter pluralRulesWrapper = new PluralSelectorAdapter();

    public PluralFormat() {
        this.init(null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT), null);
    }

    public PluralFormat(ULocale ulocale) {
        this.init(null, PluralRules.PluralType.CARDINAL, ulocale, null);
    }

    public PluralFormat(Locale locale) {
        this(ULocale.forLocale(locale));
    }

    public PluralFormat(PluralRules rules) {
        this.init(rules, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT), null);
    }

    public PluralFormat(ULocale ulocale, PluralRules rules) {
        this.init(rules, PluralRules.PluralType.CARDINAL, ulocale, null);
    }

    public PluralFormat(Locale locale, PluralRules rules) {
        this(ULocale.forLocale(locale), rules);
    }

    public PluralFormat(ULocale ulocale, PluralRules.PluralType type) {
        this.init(null, type, ulocale, null);
    }

    public PluralFormat(Locale locale, PluralRules.PluralType type) {
        this(ULocale.forLocale(locale), type);
    }

    public PluralFormat(String pattern) {
        this.init(null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT), null);
        this.applyPattern(pattern);
    }

    public PluralFormat(ULocale ulocale, String pattern) {
        this.init(null, PluralRules.PluralType.CARDINAL, ulocale, null);
        this.applyPattern(pattern);
    }

    public PluralFormat(PluralRules rules, String pattern) {
        this.init(rules, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT), null);
        this.applyPattern(pattern);
    }

    public PluralFormat(ULocale ulocale, PluralRules rules, String pattern) {
        this.init(rules, PluralRules.PluralType.CARDINAL, ulocale, null);
        this.applyPattern(pattern);
    }

    public PluralFormat(ULocale ulocale, PluralRules.PluralType type, String pattern) {
        this.init(null, type, ulocale, null);
        this.applyPattern(pattern);
    }

    PluralFormat(ULocale ulocale, PluralRules.PluralType type, String pattern, NumberFormat numberFormat) {
        this.init(null, type, ulocale, numberFormat);
        this.applyPattern(pattern);
    }

    private void init(PluralRules rules, PluralRules.PluralType type, ULocale locale, NumberFormat numberFormat) {
        this.ulocale = locale;
        this.pluralRules = rules == null ? PluralRules.forLocale(this.ulocale, type) : rules;
        this.resetPattern();
        this.numberFormat = numberFormat == null ? NumberFormat.getInstance(this.ulocale) : numberFormat;
    }

    private void resetPattern() {
        this.pattern = null;
        if (this.msgPattern != null) {
            this.msgPattern.clear();
        }
        this.offset = 0.0;
    }

    public void applyPattern(String pattern) {
        this.pattern = pattern;
        if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern();
        }
        try {
            this.msgPattern.parsePluralStyle(pattern);
            this.offset = this.msgPattern.getPluralOffset(0);
        }
        catch (RuntimeException e2) {
            this.resetPattern();
            throw e2;
        }
    }

    public String toPattern() {
        return this.pattern;
    }

    static int findSubMessage(MessagePattern pattern, int partIndex, PluralSelector selector, Object context, double number) {
        int count = pattern.countParts();
        MessagePattern.Part part = pattern.getPart(partIndex);
        double offset = part.getType().hasNumericValue() ? pattern.getNumericValue(part) : 0.0;
        String keyword = null;
        boolean haveKeywordMatch = false;
        int msgStart = 0;
        do {
            int n2 = ++partIndex;
            ++partIndex;
            part = pattern.getPart(n2);
            MessagePattern.Part.Type type = part.getType();
            if (type == MessagePattern.Part.Type.ARG_LIMIT) break;
            assert (type == MessagePattern.Part.Type.ARG_SELECTOR);
            if (pattern.getPartType(partIndex).hasNumericValue()) {
                if (number == pattern.getNumericValue(part = pattern.getPart(partIndex++))) {
                    return partIndex;
                }
            } else if (!haveKeywordMatch) {
                if (pattern.partSubstringMatches(part, "other")) {
                    if (msgStart == 0) {
                        msgStart = partIndex;
                        if (keyword != null && keyword.equals("other")) {
                            haveKeywordMatch = true;
                        }
                    }
                } else {
                    if (keyword == null) {
                        keyword = selector.select(context, number - offset);
                        if (msgStart != 0 && keyword.equals("other")) {
                            haveKeywordMatch = true;
                        }
                    }
                    if (!haveKeywordMatch && pattern.partSubstringMatches(part, keyword)) {
                        msgStart = partIndex;
                        haveKeywordMatch = true;
                    }
                }
            }
            partIndex = pattern.getLimitPartIndex(partIndex);
        } while (++partIndex < count);
        return msgStart;
    }

    public final String format(double number) {
        return this.format(number, number);
    }

    @Override
    public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
        if (!(number instanceof Number)) {
            throw new IllegalArgumentException("'" + number + "' is not a Number");
        }
        Number numberObject = (Number)number;
        toAppendTo.append(this.format(numberObject, numberObject.doubleValue()));
        return toAppendTo;
    }

    private String format(Number numberObject, double number) {
        PluralRules.IFixedDecimal dec;
        String numberString;
        Object result;
        if (this.msgPattern == null || this.msgPattern.countParts() == 0) {
            return this.numberFormat.format(numberObject);
        }
        double numberMinusOffset = number - this.offset;
        if (this.numberFormat instanceof DecimalFormat) {
            LocalizedNumberFormatter f2 = ((DecimalFormat)this.numberFormat).toNumberFormatter();
            result = this.offset == 0.0 ? f2.format(numberObject) : f2.format(numberMinusOffset);
            numberString = ((FormattedNumber)result).toString();
            dec = ((FormattedNumber)result).getFixedDecimal();
        } else {
            numberString = this.offset == 0.0 ? this.numberFormat.format(numberObject) : this.numberFormat.format(numberMinusOffset);
            dec = new PluralRules.FixedDecimal(numberMinusOffset);
        }
        int partIndex = PluralFormat.findSubMessage(this.msgPattern, 0, this.pluralRulesWrapper, dec, number);
        result = null;
        int prevIndex = this.msgPattern.getPart(partIndex).getLimit();
        while (true) {
            MessagePattern.Part part = this.msgPattern.getPart(++partIndex);
            MessagePattern.Part.Type type = part.getType();
            int index = part.getIndex();
            if (type == MessagePattern.Part.Type.MSG_LIMIT) {
                if (result == null) {
                    return this.pattern.substring(prevIndex, index);
                }
                return ((StringBuilder)result).append(this.pattern, prevIndex, index).toString();
            }
            if (type == MessagePattern.Part.Type.REPLACE_NUMBER || type == MessagePattern.Part.Type.SKIP_SYNTAX && this.msgPattern.jdkAposMode()) {
                if (result == null) {
                    result = new StringBuilder();
                }
                ((StringBuilder)result).append(this.pattern, prevIndex, index);
                if (type == MessagePattern.Part.Type.REPLACE_NUMBER) {
                    ((StringBuilder)result).append(numberString);
                }
                prevIndex = part.getLimit();
                continue;
            }
            if (type != MessagePattern.Part.Type.ARG_START) continue;
            if (result == null) {
                result = new StringBuilder();
            }
            ((StringBuilder)result).append(this.pattern, prevIndex, index);
            prevIndex = index;
            partIndex = this.msgPattern.getLimitPartIndex(partIndex);
            index = this.msgPattern.getPart(partIndex).getLimit();
            MessagePattern.appendReducedApostrophes(this.pattern, prevIndex, index, (StringBuilder)result);
            prevIndex = index;
        }
    }

    public Number parse(String text, ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }

    String parseType(String source, RbnfLenientScanner scanner, FieldPosition pos) {
        if (this.msgPattern == null || this.msgPattern.countParts() == 0) {
            pos.setBeginIndex(-1);
            pos.setEndIndex(-1);
            return null;
        }
        int partIndex = 0;
        int count = this.msgPattern.countParts();
        int startingAt = pos.getBeginIndex();
        if (startingAt < 0) {
            startingAt = 0;
        }
        String keyword = null;
        String matchedWord = null;
        int matchedIndex = -1;
        while (partIndex < count) {
            int currMatchIndex;
            MessagePattern.Part partLimit;
            MessagePattern.Part partStart;
            MessagePattern.Part partSelector;
            if ((partSelector = this.msgPattern.getPart(partIndex++)).getType() != MessagePattern.Part.Type.ARG_SELECTOR || (partStart = this.msgPattern.getPart(partIndex++)).getType() != MessagePattern.Part.Type.MSG_START || (partLimit = this.msgPattern.getPart(partIndex++)).getType() != MessagePattern.Part.Type.MSG_LIMIT) continue;
            String currArg = this.pattern.substring(partStart.getLimit(), partLimit.getIndex());
            if (scanner != null) {
                int[] scannerMatchResult = scanner.findText(source, currArg, startingAt);
                currMatchIndex = scannerMatchResult[0];
            } else {
                currMatchIndex = source.indexOf(currArg, startingAt);
            }
            if (currMatchIndex < 0 || currMatchIndex < matchedIndex || matchedWord != null && currArg.length() <= matchedWord.length()) continue;
            matchedIndex = currMatchIndex;
            matchedWord = currArg;
            keyword = this.pattern.substring(partStart.getLimit(), partLimit.getIndex());
        }
        if (keyword != null) {
            pos.setBeginIndex(matchedIndex);
            pos.setEndIndex(matchedIndex + matchedWord.length());
            return keyword;
        }
        pos.setBeginIndex(-1);
        pos.setEndIndex(-1);
        return null;
    }

    @Deprecated
    public void setLocale(ULocale ulocale) {
        if (ulocale == null) {
            ulocale = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        this.init(null, PluralRules.PluralType.CARDINAL, ulocale, null);
    }

    public void setNumberFormat(NumberFormat format) {
        this.numberFormat = format;
    }

    public boolean equals(Object rhs) {
        if (this == rhs) {
            return true;
        }
        if (rhs == null || this.getClass() != rhs.getClass()) {
            return false;
        }
        PluralFormat pf = (PluralFormat)rhs;
        return Utility.objectEquals(this.ulocale, pf.ulocale) && Utility.objectEquals(this.pluralRules, pf.pluralRules) && Utility.objectEquals(this.msgPattern, pf.msgPattern) && Utility.objectEquals(this.numberFormat, pf.numberFormat);
    }

    public boolean equals(PluralFormat rhs) {
        return this.equals((Object)rhs);
    }

    public int hashCode() {
        return this.pluralRules.hashCode() ^ this.parsedValues.hashCode();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("locale=" + this.ulocale);
        buf.append(", rules='" + this.pluralRules + "'");
        buf.append(", pattern='" + this.pattern + "'");
        buf.append(", format='" + this.numberFormat + "'");
        return buf.toString();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.pluralRulesWrapper = new PluralSelectorAdapter();
        this.parsedValues = null;
        if (this.pattern != null) {
            this.applyPattern(this.pattern);
        }
    }

    private final class PluralSelectorAdapter
    implements PluralSelector {
        private PluralSelectorAdapter() {
        }

        @Override
        public String select(Object context, double number) {
            PluralRules.IFixedDecimal dec = (PluralRules.IFixedDecimal)context;
            return PluralFormat.this.pluralRules.select(dec);
        }
    }

    static interface PluralSelector {
        public String select(Object var1, double var2);
    }
}

