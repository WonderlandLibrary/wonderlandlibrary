/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StaticUnicodeSets;
import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.DecimalQuantity_DualStorageBCD;
import com.ibm.icu.impl.number.Grouper;
import com.ibm.icu.impl.number.parse.NumberParseMatcher;
import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.UnicodeSet;

public class DecimalMatcher
implements NumberParseMatcher {
    private final boolean requireGroupingMatch;
    private final boolean groupingDisabled;
    private final boolean integerOnly;
    private final int grouping1;
    private final int grouping2;
    private final String groupingSeparator;
    private final String decimalSeparator;
    private final UnicodeSet groupingUniSet;
    private final UnicodeSet decimalUniSet;
    private final UnicodeSet separatorSet;
    private final UnicodeSet leadSet;
    private final String[] digitStrings;

    public static DecimalMatcher getInstance(DecimalFormatSymbols symbols, Grouper grouper, int parseFlags) {
        return new DecimalMatcher(symbols, grouper, parseFlags);
    }

    private DecimalMatcher(DecimalFormatSymbols symbols, Grouper grouper, int parseFlags) {
        if (0 != (parseFlags & 2)) {
            this.groupingSeparator = symbols.getMonetaryGroupingSeparatorString();
            this.decimalSeparator = symbols.getMonetaryDecimalSeparatorString();
        } else {
            this.groupingSeparator = symbols.getGroupingSeparatorString();
            this.decimalSeparator = symbols.getDecimalSeparatorString();
        }
        boolean strictSeparators = 0 != (parseFlags & 4);
        StaticUnicodeSets.Key groupingKey = strictSeparators ? StaticUnicodeSets.Key.STRICT_ALL_SEPARATORS : StaticUnicodeSets.Key.ALL_SEPARATORS;
        this.groupingUniSet = StaticUnicodeSets.get(groupingKey);
        StaticUnicodeSets.Key decimalKey = StaticUnicodeSets.chooseFrom(this.decimalSeparator, strictSeparators ? StaticUnicodeSets.Key.STRICT_COMMA : StaticUnicodeSets.Key.COMMA, strictSeparators ? StaticUnicodeSets.Key.STRICT_PERIOD : StaticUnicodeSets.Key.PERIOD);
        this.decimalUniSet = decimalKey != null ? StaticUnicodeSets.get(decimalKey) : (!this.decimalSeparator.isEmpty() ? new UnicodeSet().add(this.decimalSeparator.codePointAt(0)).freeze() : UnicodeSet.EMPTY);
        if (groupingKey != null && decimalKey != null) {
            this.separatorSet = this.groupingUniSet;
            this.leadSet = StaticUnicodeSets.get(strictSeparators ? StaticUnicodeSets.Key.DIGITS_OR_ALL_SEPARATORS : StaticUnicodeSets.Key.DIGITS_OR_STRICT_ALL_SEPARATORS);
        } else {
            this.separatorSet = new UnicodeSet().addAll(this.groupingUniSet).addAll(this.decimalUniSet).freeze();
            this.leadSet = null;
        }
        int cpZero = symbols.getCodePointZero();
        this.digitStrings = cpZero == -1 || !UCharacter.isDigit(cpZero) || UCharacter.digit(cpZero) != 0 ? symbols.getDigitStringsLocal() : null;
        this.requireGroupingMatch = 0 != (parseFlags & 8);
        this.groupingDisabled = 0 != (parseFlags & 0x20);
        this.integerOnly = 0 != (parseFlags & 0x10);
        this.grouping1 = grouper.getPrimary();
        this.grouping2 = grouper.getSecondary();
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        return this.match(segment, result, 0);
    }

    public boolean match(StringSegment segment, ParsedNumber result, int exponentSign) {
        if (result.seenNumber() && exponentSign == 0) {
            return false;
        }
        if (exponentSign != 0) assert (result.quantity != null);
        int initialOffset = segment.getOffset();
        boolean maybeMore = false;
        DecimalQuantity_DualStorageBCD digitsConsumed = null;
        int digitsAfterDecimalPlace = 0;
        String actualGroupingString = null;
        String actualDecimalString = null;
        int currGroupOffset = 0;
        int currGroupSepType = 0;
        int currGroupCount = 0;
        int prevGroupOffset = -1;
        int prevGroupSepType = -1;
        int prevGroupCount = -1;
        while (segment.length() > 0) {
            int overlap;
            maybeMore = false;
            byte digit = -1;
            int cp = segment.getCodePoint();
            if (UCharacter.isDigit(cp)) {
                segment.adjustOffset(Character.charCount(cp));
                digit = (byte)UCharacter.digit(cp);
            }
            if (digit == -1 && this.digitStrings != null) {
                for (int i2 = 0; i2 < this.digitStrings.length; ++i2) {
                    String str = this.digitStrings[i2];
                    if (str.isEmpty()) continue;
                    overlap = segment.getCommonPrefixLength(str);
                    if (overlap == str.length()) {
                        segment.adjustOffset(overlap);
                        digit = (byte)i2;
                        break;
                    }
                    maybeMore = maybeMore || overlap == segment.length();
                }
            }
            if (digit >= 0) {
                if (digitsConsumed == null) {
                    digitsConsumed = new DecimalQuantity_DualStorageBCD();
                }
                digitsConsumed.appendDigit(digit, 0, true);
                ++currGroupCount;
                if (actualDecimalString == null) continue;
                ++digitsAfterDecimalPlace;
                continue;
            }
            boolean isDecimal = false;
            boolean isGrouping = false;
            if (actualDecimalString == null && !this.decimalSeparator.isEmpty()) {
                overlap = segment.getCommonPrefixLength(this.decimalSeparator);
                boolean bl = maybeMore = maybeMore || overlap == segment.length();
                if (overlap == this.decimalSeparator.length()) {
                    isDecimal = true;
                    actualDecimalString = this.decimalSeparator;
                }
            }
            if (actualGroupingString != null) {
                overlap = segment.getCommonPrefixLength(actualGroupingString);
                boolean bl = maybeMore = maybeMore || overlap == segment.length();
                if (overlap == actualGroupingString.length()) {
                    isGrouping = true;
                }
            }
            if (!this.groupingDisabled && actualGroupingString == null && actualDecimalString == null && !this.groupingSeparator.isEmpty()) {
                overlap = segment.getCommonPrefixLength(this.groupingSeparator);
                boolean bl = maybeMore = maybeMore || overlap == segment.length();
                if (overlap == this.groupingSeparator.length()) {
                    isGrouping = true;
                    actualGroupingString = this.groupingSeparator;
                }
            }
            if (!isGrouping && actualDecimalString == null && this.decimalUniSet.contains(cp)) {
                isDecimal = true;
                actualDecimalString = UCharacter.toString(cp);
            }
            if (!this.groupingDisabled && actualGroupingString == null && actualDecimalString == null && this.groupingUniSet.contains(cp)) {
                isGrouping = true;
                actualGroupingString = UCharacter.toString(cp);
            }
            if (!isDecimal && !isGrouping || isDecimal && this.integerOnly || currGroupSepType == 2 && isGrouping) break;
            boolean prevValidSecondary = this.validateGroup(prevGroupSepType, prevGroupCount, false);
            boolean currValidPrimary = this.validateGroup(currGroupSepType, currGroupCount, true);
            if (!prevValidSecondary || isDecimal && !currValidPrimary) {
                if (isGrouping && currGroupCount == 0) {
                    assert (currGroupSepType == 1);
                    break;
                }
                if (!this.requireGroupingMatch) break;
                digitsConsumed = null;
                break;
            }
            if (this.requireGroupingMatch && currGroupCount == 0 && currGroupSepType == 1) break;
            prevGroupOffset = currGroupOffset;
            prevGroupCount = currGroupCount;
            prevGroupSepType = isDecimal ? -1 : currGroupSepType;
            if (currGroupCount != 0) {
                currGroupOffset = segment.getOffset();
            }
            currGroupSepType = isGrouping ? 1 : 2;
            currGroupCount = 0;
            if (isGrouping) {
                segment.adjustOffset(actualGroupingString.length());
                continue;
            }
            segment.adjustOffset(actualDecimalString.length());
        }
        if (currGroupSepType != 2 && currGroupCount == 0) {
            maybeMore = true;
            segment.setOffset(currGroupOffset);
            currGroupOffset = prevGroupOffset;
            currGroupSepType = prevGroupSepType;
            currGroupCount = prevGroupCount;
            prevGroupOffset = -1;
            prevGroupSepType = 0;
            prevGroupCount = 1;
        }
        boolean prevValidSecondary = this.validateGroup(prevGroupSepType, prevGroupCount, false);
        boolean currValidPrimary = this.validateGroup(currGroupSepType, currGroupCount, true);
        if (!this.requireGroupingMatch) {
            int digitsToRemove = 0;
            if (!prevValidSecondary) {
                segment.setOffset(prevGroupOffset);
                digitsToRemove += prevGroupCount;
                digitsToRemove += currGroupCount;
            } else if (!(currValidPrimary || prevGroupSepType == 0 && prevGroupCount == 0)) {
                maybeMore = true;
                segment.setOffset(currGroupOffset);
                digitsToRemove += currGroupCount;
            }
            if (digitsToRemove != 0) {
                digitsConsumed.adjustMagnitude(-digitsToRemove);
                digitsConsumed.truncate();
            }
            prevValidSecondary = true;
            currValidPrimary = true;
        }
        if (!(currGroupSepType == 2 || prevValidSecondary && currValidPrimary)) {
            digitsConsumed = null;
        }
        if (digitsConsumed == null) {
            maybeMore = maybeMore || segment.length() == 0;
            segment.setOffset(initialOffset);
            return maybeMore;
        }
        digitsConsumed.adjustMagnitude(-digitsAfterDecimalPlace);
        if (exponentSign != 0 && segment.getOffset() != initialOffset) {
            boolean overflow = false;
            if (digitsConsumed.fitsInLong()) {
                long exponentLong = digitsConsumed.toLong(false);
                assert (exponentLong >= 0L);
                if (exponentLong <= Integer.MAX_VALUE) {
                    int exponentInt = (int)exponentLong;
                    try {
                        result.quantity.adjustMagnitude(exponentSign * exponentInt);
                    }
                    catch (ArithmeticException e2) {
                        overflow = true;
                    }
                } else {
                    overflow = true;
                }
            } else {
                overflow = true;
            }
            if (overflow) {
                if (exponentSign == -1) {
                    result.quantity.clear();
                } else {
                    result.quantity = null;
                    result.flags |= 0x80;
                }
            }
        } else {
            result.quantity = digitsConsumed;
        }
        if (actualDecimalString != null) {
            result.flags |= 0x20;
        }
        result.setCharsConsumed(segment);
        return segment.length() == 0 || maybeMore;
    }

    private boolean validateGroup(int sepType, int count, boolean isPrimary) {
        if (this.requireGroupingMatch) {
            if (sepType == -1) {
                return true;
            }
            if (sepType == 0) {
                if (isPrimary) {
                    return true;
                }
                return count != 0 && count <= this.grouping2;
            }
            if (sepType == 1) {
                if (isPrimary) {
                    return count == this.grouping1;
                }
                return count == this.grouping2;
            }
            assert (sepType == 2);
            return true;
        }
        if (sepType == 1) {
            return count != 1;
        }
        return true;
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        if (this.digitStrings == null && this.leadSet != null) {
            return segment.startsWith(this.leadSet);
        }
        if (segment.startsWith(this.separatorSet) || UCharacter.isDigit(segment.getCodePoint())) {
            return true;
        }
        if (this.digitStrings == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.digitStrings.length; ++i2) {
            if (!segment.startsWith(this.digitStrings[i2])) continue;
            return true;
        }
        return false;
    }

    @Override
    public void postProcess(ParsedNumber result) {
    }

    public String toString() {
        return "<DecimalMatcher>";
    }
}

