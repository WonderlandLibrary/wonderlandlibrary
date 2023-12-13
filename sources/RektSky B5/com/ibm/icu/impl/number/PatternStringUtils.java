/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.AffixUtils;
import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.impl.number.Padder;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.text.DecimalFormatSymbols;
import java.math.BigDecimal;

public class PatternStringUtils {
    public static String propertiesToPatternString(DecimalFormatProperties properties) {
        int grouping2;
        int grouping1;
        int grouping;
        StringBuilder sb = new StringBuilder();
        int dosMax = 100;
        int groupingSize = Math.min(properties.getSecondaryGroupingSize(), dosMax);
        int firstGroupingSize = Math.min(properties.getGroupingSize(), dosMax);
        int paddingWidth = Math.min(properties.getFormatWidth(), dosMax);
        Padder.PadPosition paddingLocation = properties.getPadPosition();
        String paddingString = properties.getPadString();
        int minInt = Math.max(Math.min(properties.getMinimumIntegerDigits(), dosMax), 0);
        int maxInt = Math.min(properties.getMaximumIntegerDigits(), dosMax);
        int minFrac = Math.max(Math.min(properties.getMinimumFractionDigits(), dosMax), 0);
        int maxFrac = Math.min(properties.getMaximumFractionDigits(), dosMax);
        int minSig = Math.min(properties.getMinimumSignificantDigits(), dosMax);
        int maxSig = Math.min(properties.getMaximumSignificantDigits(), dosMax);
        boolean alwaysShowDecimal = properties.getDecimalSeparatorAlwaysShown();
        int exponentDigits = Math.min(properties.getMinimumExponentDigits(), dosMax);
        boolean exponentShowPlusSign = properties.getExponentSignAlwaysShown();
        String pp = properties.getPositivePrefix();
        String ppp = properties.getPositivePrefixPattern();
        String ps = properties.getPositiveSuffix();
        String psp = properties.getPositiveSuffixPattern();
        String np = properties.getNegativePrefix();
        String npp = properties.getNegativePrefixPattern();
        String ns = properties.getNegativeSuffix();
        String nsp = properties.getNegativeSuffixPattern();
        if (ppp != null) {
            sb.append(ppp);
        }
        AffixUtils.escape(pp, sb);
        int afterPrefixPos = sb.length();
        if (groupingSize != Math.min(dosMax, -1) && firstGroupingSize != Math.min(dosMax, -1) && groupingSize != firstGroupingSize) {
            grouping = groupingSize;
            grouping1 = groupingSize;
            grouping2 = firstGroupingSize;
        } else if (groupingSize != Math.min(dosMax, -1)) {
            grouping = groupingSize;
            grouping1 = 0;
            grouping2 = groupingSize;
        } else if (firstGroupingSize != Math.min(dosMax, -1)) {
            grouping = groupingSize;
            grouping1 = 0;
            grouping2 = firstGroupingSize;
        } else {
            grouping = 0;
            grouping1 = 0;
            grouping2 = 0;
        }
        int groupingLength = grouping1 + grouping2 + 1;
        BigDecimal roundingInterval = properties.getRoundingIncrement();
        StringBuilder digitsString = new StringBuilder();
        int digitsStringScale = 0;
        if (maxSig != Math.min(dosMax, -1)) {
            while (digitsString.length() < minSig) {
                digitsString.append('@');
            }
            while (digitsString.length() < maxSig) {
                digitsString.append('#');
            }
        } else if (roundingInterval != null) {
            digitsStringScale = -roundingInterval.scale();
            String str = roundingInterval.scaleByPowerOfTen(roundingInterval.scale()).toPlainString();
            if (str.charAt(0) == '-') {
                digitsString.append(str, 1, str.length());
            } else {
                digitsString.append(str);
            }
        }
        while (digitsString.length() + digitsStringScale < minInt) {
            digitsString.insert(0, '0');
        }
        while (-digitsStringScale < minFrac) {
            digitsString.append('0');
            --digitsStringScale;
        }
        int m0 = Math.max(groupingLength, digitsString.length() + digitsStringScale);
        m0 = maxInt != dosMax ? Math.max(maxInt, m0) - 1 : m0 - 1;
        int mN = maxFrac != dosMax ? Math.min(-maxFrac, digitsStringScale) : digitsStringScale;
        for (int magnitude = m0; magnitude >= mN; --magnitude) {
            int di = digitsString.length() + digitsStringScale - magnitude - 1;
            if (di < 0 || di >= digitsString.length()) {
                sb.append('#');
            } else {
                sb.append(digitsString.charAt(di));
            }
            if (magnitude > grouping2 && grouping > 0 && (magnitude - grouping2) % grouping == 0) {
                sb.append(',');
                continue;
            }
            if (magnitude > 0 && magnitude == grouping2) {
                sb.append(',');
                continue;
            }
            if (magnitude != 0 || !alwaysShowDecimal && mN >= 0) continue;
            sb.append('.');
        }
        if (exponentDigits != Math.min(dosMax, -1)) {
            sb.append('E');
            if (exponentShowPlusSign) {
                sb.append('+');
            }
            for (int i2 = 0; i2 < exponentDigits; ++i2) {
                sb.append('0');
            }
        }
        int beforeSuffixPos = sb.length();
        if (psp != null) {
            sb.append(psp);
        }
        AffixUtils.escape(ps, sb);
        if (paddingWidth != -1) {
            while (paddingWidth - sb.length() > 0) {
                sb.insert(afterPrefixPos, '#');
                ++beforeSuffixPos;
            }
            switch (paddingLocation) {
                case BEFORE_PREFIX: {
                    int addedLength = PatternStringUtils.escapePaddingString(paddingString, sb, 0);
                    sb.insert(0, '*');
                    afterPrefixPos += addedLength + 1;
                    beforeSuffixPos += addedLength + 1;
                    break;
                }
                case AFTER_PREFIX: {
                    int addedLength = PatternStringUtils.escapePaddingString(paddingString, sb, afterPrefixPos);
                    sb.insert(afterPrefixPos, '*');
                    afterPrefixPos += addedLength + 1;
                    beforeSuffixPos += addedLength + 1;
                    break;
                }
                case BEFORE_SUFFIX: {
                    PatternStringUtils.escapePaddingString(paddingString, sb, beforeSuffixPos);
                    sb.insert(beforeSuffixPos, '*');
                    break;
                }
                case AFTER_SUFFIX: {
                    sb.append('*');
                    PatternStringUtils.escapePaddingString(paddingString, sb, sb.length());
                }
            }
        }
        if (np != null || ns != null || npp == null && nsp != null || npp != null && (npp.length() != 1 || npp.charAt(0) != '-' || nsp.length() != 0)) {
            sb.append(';');
            if (npp != null) {
                sb.append(npp);
            }
            AffixUtils.escape(np, sb);
            sb.append(sb, afterPrefixPos, beforeSuffixPos);
            if (nsp != null) {
                sb.append(nsp);
            }
            AffixUtils.escape(ns, sb);
        }
        return sb.toString();
    }

    private static int escapePaddingString(CharSequence input, StringBuilder output, int startIndex) {
        if (input == null || input.length() == 0) {
            input = " ";
        }
        int startLength = output.length();
        if (input.length() == 1) {
            if (input.equals("'")) {
                output.insert(startIndex, "''");
            } else {
                output.insert(startIndex, input);
            }
        } else {
            output.insert(startIndex, '\'');
            int offset = 1;
            for (int i2 = 0; i2 < input.length(); ++i2) {
                char ch = input.charAt(i2);
                if (ch == '\'') {
                    output.insert(startIndex + offset, "''");
                    offset += 2;
                    continue;
                }
                output.insert(startIndex + offset, ch);
                ++offset;
            }
            output.insert(startIndex + offset, '\'');
        }
        return output.length() - startLength;
    }

    public static String convertLocalized(String input, DecimalFormatSymbols symbols, boolean toLocalized) {
        int i2;
        if (input == null) {
            return null;
        }
        String[][] table = new String[21][2];
        int standIdx = toLocalized ? 0 : 1;
        int localIdx = toLocalized ? 1 : 0;
        table[0][standIdx] = "%";
        table[0][localIdx] = symbols.getPercentString();
        table[1][standIdx] = "\u2030";
        table[1][localIdx] = symbols.getPerMillString();
        table[2][standIdx] = ".";
        table[2][localIdx] = symbols.getDecimalSeparatorString();
        table[3][standIdx] = ",";
        table[3][localIdx] = symbols.getGroupingSeparatorString();
        table[4][standIdx] = "-";
        table[4][localIdx] = symbols.getMinusSignString();
        table[5][standIdx] = "+";
        table[5][localIdx] = symbols.getPlusSignString();
        table[6][standIdx] = ";";
        table[6][localIdx] = Character.toString(symbols.getPatternSeparator());
        table[7][standIdx] = "@";
        table[7][localIdx] = Character.toString(symbols.getSignificantDigit());
        table[8][standIdx] = "E";
        table[8][localIdx] = symbols.getExponentSeparator();
        table[9][standIdx] = "*";
        table[9][localIdx] = Character.toString(symbols.getPadEscape());
        table[10][standIdx] = "#";
        table[10][localIdx] = Character.toString(symbols.getDigit());
        for (i2 = 0; i2 < 10; ++i2) {
            table[11 + i2][standIdx] = Character.toString((char)(48 + i2));
            table[11 + i2][localIdx] = symbols.getDigitStringsLocal()[i2];
        }
        for (i2 = 0; i2 < table.length; ++i2) {
            table[i2][localIdx] = table[i2][localIdx].replace('\'', '\u2019');
        }
        StringBuilder result = new StringBuilder();
        int state = 0;
        block2: for (int offset = 0; offset < input.length(); ++offset) {
            char ch = input.charAt(offset);
            if (ch == '\'') {
                if (state == 0) {
                    result.append('\'');
                    state = 1;
                    continue;
                }
                if (state == 1) {
                    result.append('\'');
                    state = 0;
                    continue;
                }
                if (state == 2) {
                    state = 3;
                    continue;
                }
                if (state == 3) {
                    result.append('\'');
                    result.append('\'');
                    state = 1;
                    continue;
                }
                if (state == 4) {
                    state = 5;
                    continue;
                }
                assert (state == 5);
                result.append('\'');
                result.append('\'');
                state = 4;
                continue;
            }
            if (state == 0 || state == 3 || state == 4) {
                for (String[] pair : table) {
                    if (!input.regionMatches(offset, pair[0], 0, pair[0].length())) continue;
                    offset += pair[0].length() - 1;
                    if (state == 3 || state == 4) {
                        result.append('\'');
                        state = 0;
                    }
                    result.append(pair[1]);
                    continue block2;
                }
                for (String[] pair : table) {
                    if (!input.regionMatches(offset, pair[1], 0, pair[1].length())) continue;
                    if (state == 0) {
                        result.append('\'');
                        state = 4;
                    }
                    result.append(ch);
                    continue block2;
                }
                if (state == 3 || state == 4) {
                    result.append('\'');
                    state = 0;
                }
                result.append(ch);
                continue;
            }
            assert (state == 1 || state == 2 || state == 5);
            result.append(ch);
            state = 2;
        }
        if (state == 3 || state == 4) {
            result.append('\'');
            state = 0;
        }
        if (state != 0) {
            throw new IllegalArgumentException("Malformed localized pattern: unterminated quote");
        }
        return result.toString();
    }

    public static void patternInfoToStringBuilder(AffixPatternProvider patternInfo, boolean isPrefix, int signum, NumberFormatter.SignDisplay signDisplay, StandardPlural plural, boolean perMilleReplacesPercent, StringBuilder output) {
        boolean plusReplacesMinusSign = signum != -1 && (signDisplay == NumberFormatter.SignDisplay.ALWAYS || signDisplay == NumberFormatter.SignDisplay.ACCOUNTING_ALWAYS || signum == 1 && (signDisplay == NumberFormatter.SignDisplay.EXCEPT_ZERO || signDisplay == NumberFormatter.SignDisplay.ACCOUNTING_EXCEPT_ZERO)) && !patternInfo.positiveHasPlusSign();
        boolean useNegativeAffixPattern = patternInfo.hasNegativeSubpattern() && (signum == -1 || patternInfo.negativeHasMinusSign() && plusReplacesMinusSign);
        int flags = 0;
        if (useNegativeAffixPattern) {
            flags |= 0x200;
        }
        if (isPrefix) {
            flags |= 0x100;
        }
        if (plural != null) {
            assert (plural.ordinal() == (0xFF & plural.ordinal()));
            flags |= plural.ordinal();
        }
        boolean prependSign = !isPrefix || useNegativeAffixPattern ? false : (signum == -1 ? signDisplay != NumberFormatter.SignDisplay.NEVER : plusReplacesMinusSign);
        int length = patternInfo.length(flags) + (prependSign ? 1 : 0);
        output.setLength(0);
        for (int index = 0; index < length; ++index) {
            int candidate = prependSign && index == 0 ? 45 : (prependSign ? (int)patternInfo.charAt(flags, index - 1) : (int)patternInfo.charAt(flags, index));
            if (plusReplacesMinusSign && candidate == 45) {
                candidate = 43;
            }
            if (perMilleReplacesPercent && candidate == 37) {
                candidate = 8240;
            }
            output.append((char)candidate);
        }
    }
}

