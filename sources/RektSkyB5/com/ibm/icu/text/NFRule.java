/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.NFSubstitution;
import com.ibm.icu.text.PluralFormat;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.text.RbnfLenientScanner;
import com.ibm.icu.text.RuleBasedNumberFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.List;

final class NFRule {
    static final int NEGATIVE_NUMBER_RULE = -1;
    static final int IMPROPER_FRACTION_RULE = -2;
    static final int PROPER_FRACTION_RULE = -3;
    static final int MASTER_RULE = -4;
    static final int INFINITY_RULE = -5;
    static final int NAN_RULE = -6;
    static final Long ZERO = 0L;
    private long baseValue;
    private int radix = 10;
    private short exponent = 0;
    private char decimalPoint = '\u0000';
    private String ruleText = null;
    private PluralFormat rulePatternFormat = null;
    private NFSubstitution sub1 = null;
    private NFSubstitution sub2 = null;
    private final RuleBasedNumberFormat formatter;
    private static final String[] RULE_PREFIXES = new String[]{"<<", "<%", "<#", "<0", ">>", ">%", ">#", ">0", "=%", "=#", "=0"};

    public static void makeRules(String description, NFRuleSet owner, NFRule predecessor, RuleBasedNumberFormat ownersOwner, List<NFRule> returnList) {
        int brack2;
        NFRule rule1 = new NFRule(ownersOwner, description);
        description = rule1.ruleText;
        int brack1 = description.indexOf(91);
        int n2 = brack2 = brack1 < 0 ? -1 : description.indexOf(93);
        if (brack2 < 0 || brack1 > brack2 || rule1.baseValue == -3L || rule1.baseValue == -1L || rule1.baseValue == -5L || rule1.baseValue == -6L) {
            rule1.extractSubstitutions(owner, description, predecessor);
        } else {
            NFRule rule2 = null;
            StringBuilder sbuf = new StringBuilder();
            if (rule1.baseValue > 0L && rule1.baseValue % NFRule.power(rule1.radix, rule1.exponent) == 0L || rule1.baseValue == -2L || rule1.baseValue == -4L) {
                rule2 = new NFRule(ownersOwner, null);
                if (rule1.baseValue >= 0L) {
                    rule2.baseValue = rule1.baseValue++;
                    if (!owner.isFractionSet()) {
                        // empty if block
                    }
                } else if (rule1.baseValue == -2L) {
                    rule2.baseValue = -3L;
                } else if (rule1.baseValue == -4L) {
                    rule2.baseValue = rule1.baseValue;
                    rule1.baseValue = -2L;
                }
                rule2.radix = rule1.radix;
                rule2.exponent = rule1.exponent;
                sbuf.append(description.substring(0, brack1));
                if (brack2 + 1 < description.length()) {
                    sbuf.append(description.substring(brack2 + 1));
                }
                rule2.extractSubstitutions(owner, sbuf.toString(), predecessor);
            }
            sbuf.setLength(0);
            sbuf.append(description.substring(0, brack1));
            sbuf.append(description.substring(brack1 + 1, brack2));
            if (brack2 + 1 < description.length()) {
                sbuf.append(description.substring(brack2 + 1));
            }
            rule1.extractSubstitutions(owner, sbuf.toString(), predecessor);
            if (rule2 != null) {
                if (rule2.baseValue >= 0L) {
                    returnList.add(rule2);
                } else {
                    owner.setNonNumericalRule(rule2);
                }
            }
        }
        if (rule1.baseValue >= 0L) {
            returnList.add(rule1);
        } else {
            owner.setNonNumericalRule(rule1);
        }
    }

    public NFRule(RuleBasedNumberFormat formatter, String ruleText) {
        this.formatter = formatter;
        this.ruleText = ruleText == null ? null : this.parseRuleDescriptor(ruleText);
    }

    private String parseRuleDescriptor(String description) {
        int p2 = description.indexOf(":");
        if (p2 != -1) {
            String descriptor = description.substring(0, p2);
            ++p2;
            while (p2 < description.length() && PatternProps.isWhiteSpace(description.charAt(p2))) {
                ++p2;
            }
            description = description.substring(p2);
            int descriptorLength = descriptor.length();
            char firstChar = descriptor.charAt(0);
            char lastChar = descriptor.charAt(descriptorLength - 1);
            if (firstChar >= '0' && firstChar <= '9' && lastChar != 'x') {
                long tempValue = 0L;
                char c2 = '\u0000';
                for (p2 = 0; p2 < descriptorLength; ++p2) {
                    c2 = descriptor.charAt(p2);
                    if (c2 >= '0' && c2 <= '9') {
                        tempValue = tempValue * 10L + (long)(c2 - 48);
                        continue;
                    }
                    if (c2 == '/' || c2 == '>') break;
                    if (PatternProps.isWhiteSpace(c2) || c2 == ',' || c2 == '.') continue;
                    throw new IllegalArgumentException("Illegal character " + c2 + " in rule descriptor");
                }
                this.setBaseValue(tempValue);
                if (c2 == '/') {
                    tempValue = 0L;
                    ++p2;
                    while (p2 < descriptorLength) {
                        c2 = descriptor.charAt(p2);
                        if (c2 >= '0' && c2 <= '9') {
                            tempValue = tempValue * 10L + (long)(c2 - 48);
                        } else {
                            if (c2 == '>') break;
                            if (!PatternProps.isWhiteSpace(c2) && c2 != ',' && c2 != '.') {
                                throw new IllegalArgumentException("Illegal character " + c2 + " in rule descriptor");
                            }
                        }
                        ++p2;
                    }
                    this.radix = (int)tempValue;
                    if (this.radix == 0) {
                        throw new IllegalArgumentException("Rule can't have radix of 0");
                    }
                    this.exponent = this.expectedExponent();
                }
                if (c2 == '>') {
                    while (p2 < descriptorLength) {
                        c2 = descriptor.charAt(p2);
                        if (c2 != '>' || this.exponent <= 0) {
                            throw new IllegalArgumentException("Illegal character in rule descriptor");
                        }
                        this.exponent = (short)(this.exponent - 1);
                        ++p2;
                    }
                }
            } else if (descriptor.equals("-x")) {
                this.setBaseValue(-1L);
            } else if (descriptorLength == 3) {
                if (firstChar == '0' && lastChar == 'x') {
                    this.setBaseValue(-3L);
                    this.decimalPoint = descriptor.charAt(1);
                } else if (firstChar == 'x' && lastChar == 'x') {
                    this.setBaseValue(-2L);
                    this.decimalPoint = descriptor.charAt(1);
                } else if (firstChar == 'x' && lastChar == '0') {
                    this.setBaseValue(-4L);
                    this.decimalPoint = descriptor.charAt(1);
                } else if (descriptor.equals("NaN")) {
                    this.setBaseValue(-6L);
                } else if (descriptor.equals("Inf")) {
                    this.setBaseValue(-5L);
                }
            }
        }
        if (description.length() > 0 && description.charAt(0) == '\'') {
            description = description.substring(1);
        }
        return description;
    }

    private void extractSubstitutions(NFRuleSet owner, String ruleText, NFRule predecessor) {
        int pluralRuleEnd;
        this.ruleText = ruleText;
        this.sub1 = this.extractSubstitution(owner, predecessor);
        this.sub2 = this.sub1 == null ? null : this.extractSubstitution(owner, predecessor);
        ruleText = this.ruleText;
        int pluralRuleStart = ruleText.indexOf("$(");
        int n2 = pluralRuleEnd = pluralRuleStart >= 0 ? ruleText.indexOf(")$", pluralRuleStart) : -1;
        if (pluralRuleEnd >= 0) {
            PluralRules.PluralType pluralType;
            int endType = ruleText.indexOf(44, pluralRuleStart);
            if (endType < 0) {
                throw new IllegalArgumentException("Rule \"" + ruleText + "\" does not have a defined type");
            }
            String type = this.ruleText.substring(pluralRuleStart + 2, endType);
            if ("cardinal".equals(type)) {
                pluralType = PluralRules.PluralType.CARDINAL;
            } else if ("ordinal".equals(type)) {
                pluralType = PluralRules.PluralType.ORDINAL;
            } else {
                throw new IllegalArgumentException(type + " is an unknown type");
            }
            this.rulePatternFormat = this.formatter.createPluralFormat(pluralType, ruleText.substring(endType + 1, pluralRuleEnd));
        }
    }

    private NFSubstitution extractSubstitution(NFRuleSet owner, NFRule predecessor) {
        int subEnd;
        int subStart = NFRule.indexOfAnyRulePrefix(this.ruleText);
        if (subStart == -1) {
            return null;
        }
        if (this.ruleText.startsWith(">>>", subStart)) {
            subEnd = subStart + 2;
        } else {
            char c2 = this.ruleText.charAt(subStart);
            subEnd = this.ruleText.indexOf(c2, subStart + 1);
            if (c2 == '<' && subEnd != -1 && subEnd < this.ruleText.length() - 1 && this.ruleText.charAt(subEnd + 1) == c2) {
                ++subEnd;
            }
        }
        if (subEnd == -1) {
            return null;
        }
        NFSubstitution result = NFSubstitution.makeSubstitution(subStart, this, predecessor, owner, this.formatter, this.ruleText.substring(subStart, subEnd + 1));
        this.ruleText = this.ruleText.substring(0, subStart) + this.ruleText.substring(subEnd + 1);
        return result;
    }

    final void setBaseValue(long newBaseValue) {
        this.baseValue = newBaseValue;
        this.radix = 10;
        if (this.baseValue >= 1L) {
            this.exponent = this.expectedExponent();
            if (this.sub1 != null) {
                this.sub1.setDivisor(this.radix, this.exponent);
            }
            if (this.sub2 != null) {
                this.sub2.setDivisor(this.radix, this.exponent);
            }
        } else {
            this.exponent = 0;
        }
    }

    private short expectedExponent() {
        if (this.radix == 0 || this.baseValue < 1L) {
            return 0;
        }
        short tempResult = (short)(Math.log(this.baseValue) / Math.log(this.radix));
        if (NFRule.power(this.radix, (short)(tempResult + 1)) <= this.baseValue) {
            return (short)(tempResult + 1);
        }
        return tempResult;
    }

    private static int indexOfAnyRulePrefix(String ruleText) {
        int result = -1;
        if (ruleText.length() > 0) {
            for (String string : RULE_PREFIXES) {
                int pos = ruleText.indexOf(string);
                if (pos == -1 || result != -1 && pos >= result) continue;
                result = pos;
            }
        }
        return result;
    }

    public boolean equals(Object that) {
        if (that instanceof NFRule) {
            NFRule that2 = (NFRule)that;
            return this.baseValue == that2.baseValue && this.radix == that2.radix && this.exponent == that2.exponent && this.ruleText.equals(that2.ruleText) && Utility.objectEquals(this.sub1, that2.sub1) && Utility.objectEquals(this.sub2, that2.sub2);
        }
        return false;
    }

    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.baseValue == -1L) {
            result.append("-x: ");
        } else if (this.baseValue == -2L) {
            result.append('x').append(this.decimalPoint == '\u0000' ? (char)'.' : (char)this.decimalPoint).append("x: ");
        } else if (this.baseValue == -3L) {
            result.append('0').append(this.decimalPoint == '\u0000' ? (char)'.' : (char)this.decimalPoint).append("x: ");
        } else if (this.baseValue == -4L) {
            result.append('x').append(this.decimalPoint == '\u0000' ? (char)'.' : (char)this.decimalPoint).append("0: ");
        } else if (this.baseValue == -5L) {
            result.append("Inf: ");
        } else if (this.baseValue == -6L) {
            result.append("NaN: ");
        } else {
            result.append(String.valueOf(this.baseValue));
            if (this.radix != 10) {
                result.append('/').append(this.radix);
            }
            int numCarets = this.expectedExponent() - this.exponent;
            for (int i2 = 0; i2 < numCarets; ++i2) {
                result.append('>');
            }
            result.append(": ");
        }
        if (this.ruleText.startsWith(" ") && (this.sub1 == null || this.sub1.getPos() != 0)) {
            result.append('\'');
        }
        StringBuilder ruleTextCopy = new StringBuilder(this.ruleText);
        if (this.sub2 != null) {
            ruleTextCopy.insert(this.sub2.getPos(), this.sub2.toString());
        }
        if (this.sub1 != null) {
            ruleTextCopy.insert(this.sub1.getPos(), this.sub1.toString());
        }
        result.append(ruleTextCopy.toString());
        result.append(';');
        return result.toString();
    }

    public final char getDecimalPoint() {
        return this.decimalPoint;
    }

    public final long getBaseValue() {
        return this.baseValue;
    }

    public long getDivisor() {
        return NFRule.power(this.radix, this.exponent);
    }

    public void doFormat(long number, StringBuilder toInsertInto, int pos, int recursionCount) {
        int pluralRuleStart = this.ruleText.length();
        int lengthOffset = 0;
        if (this.rulePatternFormat == null) {
            toInsertInto.insert(pos, this.ruleText);
        } else {
            pluralRuleStart = this.ruleText.indexOf("$(");
            int pluralRuleEnd = this.ruleText.indexOf(")$", pluralRuleStart);
            int initialLength = toInsertInto.length();
            if (pluralRuleEnd < this.ruleText.length() - 1) {
                toInsertInto.insert(pos, this.ruleText.substring(pluralRuleEnd + 2));
            }
            toInsertInto.insert(pos, this.rulePatternFormat.format(number / NFRule.power(this.radix, this.exponent)));
            if (pluralRuleStart > 0) {
                toInsertInto.insert(pos, this.ruleText.substring(0, pluralRuleStart));
            }
            lengthOffset = this.ruleText.length() - (toInsertInto.length() - initialLength);
        }
        if (this.sub2 != null) {
            this.sub2.doSubstitution(number, toInsertInto, pos - (this.sub2.getPos() > pluralRuleStart ? lengthOffset : 0), recursionCount);
        }
        if (this.sub1 != null) {
            this.sub1.doSubstitution(number, toInsertInto, pos - (this.sub1.getPos() > pluralRuleStart ? lengthOffset : 0), recursionCount);
        }
    }

    public void doFormat(double number, StringBuilder toInsertInto, int pos, int recursionCount) {
        int pluralRuleStart = this.ruleText.length();
        int lengthOffset = 0;
        if (this.rulePatternFormat == null) {
            toInsertInto.insert(pos, this.ruleText);
        } else {
            double pluralVal;
            pluralRuleStart = this.ruleText.indexOf("$(");
            int pluralRuleEnd = this.ruleText.indexOf(")$", pluralRuleStart);
            int initialLength = toInsertInto.length();
            if (pluralRuleEnd < this.ruleText.length() - 1) {
                toInsertInto.insert(pos, this.ruleText.substring(pluralRuleEnd + 2));
            }
            pluralVal = 0.0 <= (pluralVal = number) && pluralVal < 1.0 ? (double)Math.round(pluralVal * (double)NFRule.power(this.radix, this.exponent)) : (pluralVal /= (double)NFRule.power(this.radix, this.exponent));
            toInsertInto.insert(pos, this.rulePatternFormat.format((long)pluralVal));
            if (pluralRuleStart > 0) {
                toInsertInto.insert(pos, this.ruleText.substring(0, pluralRuleStart));
            }
            lengthOffset = this.ruleText.length() - (toInsertInto.length() - initialLength);
        }
        if (this.sub2 != null) {
            this.sub2.doSubstitution(number, toInsertInto, pos - (this.sub2.getPos() > pluralRuleStart ? lengthOffset : 0), recursionCount);
        }
        if (this.sub1 != null) {
            this.sub1.doSubstitution(number, toInsertInto, pos - (this.sub1.getPos() > pluralRuleStart ? lengthOffset : 0), recursionCount);
        }
    }

    static long power(long base, short exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Exponent can not be negative");
        }
        if (base < 0L) {
            throw new IllegalArgumentException("Base can not be negative");
        }
        long result = 1L;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result *= base;
            }
            base *= base;
            exponent = (short)(exponent >> 1);
        }
        return result;
    }

    public boolean shouldRollBack(long number) {
        if (!(this.sub1 != null && this.sub1.isModulusSubstitution() || this.sub2 != null && this.sub2.isModulusSubstitution())) {
            return false;
        }
        long divisor = NFRule.power(this.radix, this.exponent);
        return number % divisor == 0L && this.baseValue % divisor != 0L;
    }

    public Number doParse(String text, ParsePosition parsePosition, boolean isFractionRule, double upperBound, int nonNumericalExecutedRuleMask) {
        ParsePosition pp = new ParsePosition(0);
        int sub1Pos = this.sub1 != null ? this.sub1.getPos() : this.ruleText.length();
        int sub2Pos = this.sub2 != null ? this.sub2.getPos() : this.ruleText.length();
        String workText = this.stripPrefix(text, this.ruleText.substring(0, sub1Pos), pp);
        int prefixLength = text.length() - workText.length();
        if (pp.getIndex() == 0 && sub1Pos != 0) {
            return ZERO;
        }
        if (this.baseValue == -5L) {
            parsePosition.setIndex(pp.getIndex());
            return Double.POSITIVE_INFINITY;
        }
        if (this.baseValue == -6L) {
            parsePosition.setIndex(pp.getIndex());
            return Double.NaN;
        }
        int highWaterMark = 0;
        double result = 0.0;
        int start = 0;
        double tempBaseValue = Math.max(0L, this.baseValue);
        do {
            pp.setIndex(0);
            double partialResult = this.matchToDelimiter(workText, start, tempBaseValue, this.ruleText.substring(sub1Pos, sub2Pos), this.rulePatternFormat, pp, this.sub1, upperBound, nonNumericalExecutedRuleMask).doubleValue();
            if (pp.getIndex() == 0 && this.sub1 != null) continue;
            start = pp.getIndex();
            String workText2 = workText.substring(pp.getIndex());
            ParsePosition pp2 = new ParsePosition(0);
            partialResult = this.matchToDelimiter(workText2, 0, partialResult, this.ruleText.substring(sub2Pos), this.rulePatternFormat, pp2, this.sub2, upperBound, nonNumericalExecutedRuleMask).doubleValue();
            if (pp2.getIndex() == 0 && this.sub2 != null || prefixLength + pp.getIndex() + pp2.getIndex() <= highWaterMark) continue;
            highWaterMark = prefixLength + pp.getIndex() + pp2.getIndex();
            result = partialResult;
        } while (sub1Pos != sub2Pos && pp.getIndex() > 0 && pp.getIndex() < workText.length() && pp.getIndex() != start);
        parsePosition.setIndex(highWaterMark);
        if (isFractionRule && highWaterMark > 0 && this.sub1 == null) {
            result = 1.0 / result;
        }
        if (result == (double)((long)result)) {
            return (long)result;
        }
        return new Double(result);
    }

    private String stripPrefix(String text, String prefix, ParsePosition pp) {
        if (prefix.length() == 0) {
            return text;
        }
        int pfl = this.prefixLength(text, prefix);
        if (pfl != 0) {
            pp.setIndex(pp.getIndex() + pfl);
            return text.substring(pfl);
        }
        return text;
    }

    private Number matchToDelimiter(String text, int startPos, double baseVal, String delimiter, PluralFormat pluralFormatDelimiter, ParsePosition pp, NFSubstitution sub, double upperBound, int nonNumericalExecutedRuleMask) {
        if (!this.allIgnorable(delimiter)) {
            ParsePosition tempPP = new ParsePosition(0);
            int[] temp = this.findText(text, delimiter, pluralFormatDelimiter, startPos);
            int dPos = temp[0];
            int dLen = temp[1];
            while (dPos >= 0) {
                String subText = text.substring(0, dPos);
                if (subText.length() > 0) {
                    Number tempResult = sub.doParse(subText, tempPP, baseVal, upperBound, this.formatter.lenientParseEnabled(), nonNumericalExecutedRuleMask);
                    if (tempPP.getIndex() == dPos) {
                        pp.setIndex(dPos + dLen);
                        return tempResult;
                    }
                }
                tempPP.setIndex(0);
                temp = this.findText(text, delimiter, pluralFormatDelimiter, dPos + dLen);
                dPos = temp[0];
                dLen = temp[1];
            }
            pp.setIndex(0);
            return ZERO;
        }
        if (sub == null) {
            return baseVal;
        }
        ParsePosition tempPP = new ParsePosition(0);
        Number result = ZERO;
        Number tempResult = sub.doParse(text, tempPP, baseVal, upperBound, this.formatter.lenientParseEnabled(), nonNumericalExecutedRuleMask);
        if (tempPP.getIndex() != 0) {
            pp.setIndex(tempPP.getIndex());
            if (tempResult != null) {
                result = tempResult;
            }
        }
        return result;
    }

    private int prefixLength(String str, String prefix) {
        if (prefix.length() == 0) {
            return 0;
        }
        RbnfLenientScanner scanner = this.formatter.getLenientScanner();
        if (scanner != null) {
            return scanner.prefixLength(str, prefix);
        }
        if (str.startsWith(prefix)) {
            return prefix.length();
        }
        return 0;
    }

    private int[] findText(String str, String key, PluralFormat pluralFormatKey, int startingAt) {
        RbnfLenientScanner scanner = this.formatter.getLenientScanner();
        if (pluralFormatKey != null) {
            FieldPosition position = new FieldPosition(0);
            position.setBeginIndex(startingAt);
            pluralFormatKey.parseType(str, scanner, position);
            int start = position.getBeginIndex();
            if (start >= 0) {
                int pluralRuleStart = this.ruleText.indexOf("$(");
                int pluralRuleSuffix = this.ruleText.indexOf(")$", pluralRuleStart) + 2;
                int matchLen = position.getEndIndex() - start;
                String prefix = this.ruleText.substring(0, pluralRuleStart);
                String suffix = this.ruleText.substring(pluralRuleSuffix);
                if (str.regionMatches(start - prefix.length(), prefix, 0, prefix.length()) && str.regionMatches(start + matchLen, suffix, 0, suffix.length())) {
                    return new int[]{start - prefix.length(), matchLen + prefix.length() + suffix.length()};
                }
            }
            return new int[]{-1, 0};
        }
        if (scanner != null) {
            return scanner.findText(str, key, startingAt);
        }
        return new int[]{str.indexOf(key, startingAt), key.length()};
    }

    private boolean allIgnorable(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        RbnfLenientScanner scanner = this.formatter.getLenientScanner();
        return scanner != null && scanner.allIgnorable(str);
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
        if (this.sub1 != null) {
            this.sub1.setDecimalFormatSymbols(newSymbols);
        }
        if (this.sub2 != null) {
            this.sub2.setDecimalFormatSymbols(newSymbols);
        }
    }
}

