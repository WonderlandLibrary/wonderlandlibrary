/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.ConstantMultiFieldModifier;
import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.UnicodeSet;

public class CurrencySpacingEnabledModifier
extends ConstantMultiFieldModifier {
    private static final UnicodeSet UNISET_DIGIT = new UnicodeSet("[:digit:]").freeze();
    private static final UnicodeSet UNISET_NOTS = new UnicodeSet("[:^S:]").freeze();
    static final byte PREFIX = 0;
    static final byte SUFFIX = 1;
    static final short IN_CURRENCY = 0;
    static final short IN_NUMBER = 1;
    private final UnicodeSet afterPrefixUnicodeSet;
    private final String afterPrefixInsert;
    private final UnicodeSet beforeSuffixUnicodeSet;
    private final String beforeSuffixInsert;

    public CurrencySpacingEnabledModifier(NumberStringBuilder prefix, NumberStringBuilder suffix, boolean overwrite, boolean strong, DecimalFormatSymbols symbols) {
        super(prefix, suffix, overwrite, strong);
        if (prefix.length() > 0 && prefix.fieldAt(prefix.length() - 1) == NumberFormat.Field.CURRENCY) {
            int prefixCp = prefix.getLastCodePoint();
            UnicodeSet prefixUnicodeSet = CurrencySpacingEnabledModifier.getUnicodeSet(symbols, (short)0, (byte)0);
            if (prefixUnicodeSet.contains(prefixCp)) {
                this.afterPrefixUnicodeSet = CurrencySpacingEnabledModifier.getUnicodeSet(symbols, (short)1, (byte)0);
                this.afterPrefixUnicodeSet.freeze();
                this.afterPrefixInsert = CurrencySpacingEnabledModifier.getInsertString(symbols, (byte)0);
            } else {
                this.afterPrefixUnicodeSet = null;
                this.afterPrefixInsert = null;
            }
        } else {
            this.afterPrefixUnicodeSet = null;
            this.afterPrefixInsert = null;
        }
        if (suffix.length() > 0 && suffix.fieldAt(0) == NumberFormat.Field.CURRENCY) {
            int suffixCp = suffix.getLastCodePoint();
            UnicodeSet suffixUnicodeSet = CurrencySpacingEnabledModifier.getUnicodeSet(symbols, (short)0, (byte)1);
            if (suffixUnicodeSet.contains(suffixCp)) {
                this.beforeSuffixUnicodeSet = CurrencySpacingEnabledModifier.getUnicodeSet(symbols, (short)1, (byte)1);
                this.beforeSuffixUnicodeSet.freeze();
                this.beforeSuffixInsert = CurrencySpacingEnabledModifier.getInsertString(symbols, (byte)1);
            } else {
                this.beforeSuffixUnicodeSet = null;
                this.beforeSuffixInsert = null;
            }
        } else {
            this.beforeSuffixUnicodeSet = null;
            this.beforeSuffixInsert = null;
        }
    }

    @Override
    public int apply(NumberStringBuilder output, int leftIndex, int rightIndex) {
        int length = 0;
        if (rightIndex - leftIndex > 0 && this.afterPrefixUnicodeSet != null && this.afterPrefixUnicodeSet.contains(output.codePointAt(leftIndex))) {
            length += output.insert(leftIndex, this.afterPrefixInsert, null);
        }
        if (rightIndex - leftIndex > 0 && this.beforeSuffixUnicodeSet != null && this.beforeSuffixUnicodeSet.contains(output.codePointBefore(rightIndex))) {
            length += output.insert(rightIndex + length, this.beforeSuffixInsert, null);
        }
        length += super.apply(output, leftIndex, rightIndex + length);
        return length;
    }

    public static int applyCurrencySpacing(NumberStringBuilder output, int prefixStart, int prefixLen, int suffixStart, int suffixLen, DecimalFormatSymbols symbols) {
        boolean hasNumber;
        int length = 0;
        boolean hasPrefix = prefixLen > 0;
        boolean hasSuffix = suffixLen > 0;
        boolean bl = hasNumber = suffixStart - prefixStart - prefixLen > 0;
        if (hasPrefix && hasNumber) {
            length += CurrencySpacingEnabledModifier.applyCurrencySpacingAffix(output, prefixStart + prefixLen, (byte)0, symbols);
        }
        if (hasSuffix && hasNumber) {
            length += CurrencySpacingEnabledModifier.applyCurrencySpacingAffix(output, suffixStart + length, (byte)1, symbols);
        }
        return length;
    }

    private static int applyCurrencySpacingAffix(NumberStringBuilder output, int index, byte affix, DecimalFormatSymbols symbols) {
        NumberFormat.Field affixField;
        NumberFormat.Field field = affixField = affix == 0 ? output.fieldAt(index - 1) : output.fieldAt(index);
        if (affixField != NumberFormat.Field.CURRENCY) {
            return 0;
        }
        int affixCp = affix == 0 ? output.codePointBefore(index) : output.codePointAt(index);
        UnicodeSet affixUniset = CurrencySpacingEnabledModifier.getUnicodeSet(symbols, (short)0, affix);
        if (!affixUniset.contains(affixCp)) {
            return 0;
        }
        int numberCp = affix == 0 ? output.codePointAt(index) : output.codePointBefore(index);
        UnicodeSet numberUniset = CurrencySpacingEnabledModifier.getUnicodeSet(symbols, (short)1, affix);
        if (!numberUniset.contains(numberCp)) {
            return 0;
        }
        String spacingString = CurrencySpacingEnabledModifier.getInsertString(symbols, affix);
        return output.insert(index, spacingString, null);
    }

    private static UnicodeSet getUnicodeSet(DecimalFormatSymbols symbols, short position, byte affix) {
        String pattern = symbols.getPatternForCurrencySpacing(position == 0 ? 0 : 1, affix == 1);
        if (pattern.equals("[:digit:]")) {
            return UNISET_DIGIT;
        }
        if (pattern.equals("[:^S:]")) {
            return UNISET_NOTS;
        }
        return new UnicodeSet(pattern);
    }

    private static String getInsertString(DecimalFormatSymbols symbols, byte affix) {
        return symbols.getPatternForCurrencySpacing(2, affix == 1);
    }
}

