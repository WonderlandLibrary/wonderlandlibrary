/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.StaticUnicodeSets;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public final class ScientificNumberFormatter {
    private final String preExponent;
    private final DecimalFormat fmt;
    private final Style style;
    private static final Style SUPER_SCRIPT = new SuperscriptStyle();

    public static ScientificNumberFormatter getSuperscriptInstance(ULocale locale) {
        return ScientificNumberFormatter.getInstanceForLocale(locale, SUPER_SCRIPT);
    }

    public static ScientificNumberFormatter getSuperscriptInstance(DecimalFormat df) {
        return ScientificNumberFormatter.getInstance(df, SUPER_SCRIPT);
    }

    public static ScientificNumberFormatter getMarkupInstance(ULocale locale, String beginMarkup, String endMarkup) {
        return ScientificNumberFormatter.getInstanceForLocale(locale, new MarkupStyle(beginMarkup, endMarkup));
    }

    public static ScientificNumberFormatter getMarkupInstance(DecimalFormat df, String beginMarkup, String endMarkup) {
        return ScientificNumberFormatter.getInstance(df, new MarkupStyle(beginMarkup, endMarkup));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String format(Object number) {
        DecimalFormat decimalFormat = this.fmt;
        synchronized (decimalFormat) {
            return this.style.format(this.fmt.formatToCharacterIterator(number), this.preExponent);
        }
    }

    private static String getPreExponent(DecimalFormatSymbols dfs) {
        StringBuilder preExponent = new StringBuilder();
        preExponent.append(dfs.getExponentMultiplicationSign());
        char[] digits = dfs.getDigits();
        preExponent.append(digits[1]).append(digits[0]);
        return preExponent.toString();
    }

    private static ScientificNumberFormatter getInstance(DecimalFormat decimalFormat, Style style) {
        DecimalFormatSymbols dfs = decimalFormat.getDecimalFormatSymbols();
        return new ScientificNumberFormatter((DecimalFormat)decimalFormat.clone(), ScientificNumberFormatter.getPreExponent(dfs), style);
    }

    private static ScientificNumberFormatter getInstanceForLocale(ULocale locale, Style style) {
        DecimalFormat decimalFormat = (DecimalFormat)DecimalFormat.getScientificInstance(locale);
        return new ScientificNumberFormatter(decimalFormat, ScientificNumberFormatter.getPreExponent(decimalFormat.getDecimalFormatSymbols()), style);
    }

    private ScientificNumberFormatter(DecimalFormat decimalFormat, String preExponent, Style style) {
        this.fmt = decimalFormat;
        this.preExponent = preExponent;
        this.style = style;
    }

    private static class SuperscriptStyle
    extends Style {
        private static final char[] SUPERSCRIPT_DIGITS = new char[]{'\u2070', '\u00b9', '\u00b2', '\u00b3', '\u2074', '\u2075', '\u2076', '\u2077', '\u2078', '\u2079'};
        private static final char SUPERSCRIPT_PLUS_SIGN = '\u207a';
        private static final char SUPERSCRIPT_MINUS_SIGN = '\u207b';

        private SuperscriptStyle() {
        }

        @Override
        String format(AttributedCharacterIterator iterator, String preExponent) {
            int copyFromOffset = 0;
            StringBuilder result = new StringBuilder();
            iterator.first();
            while (iterator.current() != '\uffff') {
                int limit;
                int start;
                Map<AttributedCharacterIterator.Attribute, Object> attributeSet = iterator.getAttributes();
                if (attributeSet.containsKey(NumberFormat.Field.EXPONENT_SYMBOL)) {
                    SuperscriptStyle.append(iterator, copyFromOffset, iterator.getRunStart(NumberFormat.Field.EXPONENT_SYMBOL), result);
                    copyFromOffset = iterator.getRunLimit(NumberFormat.Field.EXPONENT_SYMBOL);
                    iterator.setIndex(copyFromOffset);
                    result.append(preExponent);
                    continue;
                }
                if (attributeSet.containsKey(NumberFormat.Field.EXPONENT_SIGN)) {
                    start = iterator.getRunStart(NumberFormat.Field.EXPONENT_SIGN);
                    limit = iterator.getRunLimit(NumberFormat.Field.EXPONENT_SIGN);
                    int aChar = SuperscriptStyle.char32AtAndAdvance(iterator);
                    if (StaticUnicodeSets.get(StaticUnicodeSets.Key.MINUS_SIGN).contains(aChar)) {
                        SuperscriptStyle.append(iterator, copyFromOffset, start, result);
                        result.append('\u207b');
                    } else if (StaticUnicodeSets.get(StaticUnicodeSets.Key.PLUS_SIGN).contains(aChar)) {
                        SuperscriptStyle.append(iterator, copyFromOffset, start, result);
                        result.append('\u207a');
                    } else {
                        throw new IllegalArgumentException();
                    }
                    copyFromOffset = limit;
                    iterator.setIndex(copyFromOffset);
                    continue;
                }
                if (attributeSet.containsKey(NumberFormat.Field.EXPONENT)) {
                    start = iterator.getRunStart(NumberFormat.Field.EXPONENT);
                    limit = iterator.getRunLimit(NumberFormat.Field.EXPONENT);
                    SuperscriptStyle.append(iterator, copyFromOffset, start, result);
                    SuperscriptStyle.copyAsSuperscript(iterator, start, limit, result);
                    copyFromOffset = limit;
                    iterator.setIndex(copyFromOffset);
                    continue;
                }
                iterator.next();
            }
            SuperscriptStyle.append(iterator, copyFromOffset, iterator.getEndIndex(), result);
            return result.toString();
        }

        private static void copyAsSuperscript(AttributedCharacterIterator iterator, int start, int limit, StringBuilder result) {
            int oldIndex = iterator.getIndex();
            iterator.setIndex(start);
            while (iterator.getIndex() < limit) {
                int aChar = SuperscriptStyle.char32AtAndAdvance(iterator);
                int digit = UCharacter.digit(aChar);
                if (digit < 0) {
                    throw new IllegalArgumentException();
                }
                result.append(SUPERSCRIPT_DIGITS[digit]);
            }
            iterator.setIndex(oldIndex);
        }

        private static int char32AtAndAdvance(AttributedCharacterIterator iterator) {
            char c1 = iterator.current();
            char c2 = iterator.next();
            if (UCharacter.isHighSurrogate(c1) && UCharacter.isLowSurrogate(c2)) {
                iterator.next();
                return UCharacter.toCodePoint(c1, c2);
            }
            return c1;
        }
    }

    private static class MarkupStyle
    extends Style {
        private final String beginMarkup;
        private final String endMarkup;

        MarkupStyle(String beginMarkup, String endMarkup) {
            this.beginMarkup = beginMarkup;
            this.endMarkup = endMarkup;
        }

        @Override
        String format(AttributedCharacterIterator iterator, String preExponent) {
            int copyFromOffset = 0;
            StringBuilder result = new StringBuilder();
            iterator.first();
            while (iterator.current() != '\uffff') {
                Map<AttributedCharacterIterator.Attribute, Object> attributeSet = iterator.getAttributes();
                if (attributeSet.containsKey(NumberFormat.Field.EXPONENT_SYMBOL)) {
                    MarkupStyle.append(iterator, copyFromOffset, iterator.getRunStart(NumberFormat.Field.EXPONENT_SYMBOL), result);
                    copyFromOffset = iterator.getRunLimit(NumberFormat.Field.EXPONENT_SYMBOL);
                    iterator.setIndex(copyFromOffset);
                    result.append(preExponent);
                    result.append(this.beginMarkup);
                    continue;
                }
                if (attributeSet.containsKey(NumberFormat.Field.EXPONENT)) {
                    int limit = iterator.getRunLimit(NumberFormat.Field.EXPONENT);
                    MarkupStyle.append(iterator, copyFromOffset, limit, result);
                    copyFromOffset = limit;
                    iterator.setIndex(copyFromOffset);
                    result.append(this.endMarkup);
                    continue;
                }
                iterator.next();
            }
            MarkupStyle.append(iterator, copyFromOffset, iterator.getEndIndex(), result);
            return result.toString();
        }
    }

    private static abstract class Style {
        private Style() {
        }

        abstract String format(AttributedCharacterIterator var1, String var2);

        static void append(AttributedCharacterIterator iterator, int start, int limit, StringBuilder result) {
            int oldIndex = iterator.getIndex();
            iterator.setIndex(start);
            for (int i2 = start; i2 < limit; ++i2) {
                result.append(iterator.current());
                iterator.next();
            }
            iterator.setIndex(oldIndex);
        }
    }
}

