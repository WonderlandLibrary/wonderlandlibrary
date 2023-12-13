/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StaticUnicodeSets;
import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.DecimalQuantity_DualStorageBCD;
import com.ibm.icu.impl.number.Grouper;
import com.ibm.icu.impl.number.parse.DecimalMatcher;
import com.ibm.icu.impl.number.parse.NumberParseMatcher;
import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.impl.number.parse.ParsingUtils;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.UnicodeSet;

public class ScientificMatcher
implements NumberParseMatcher {
    private final String exponentSeparatorString;
    private final DecimalMatcher exponentMatcher;
    private final String customMinusSign;
    private final String customPlusSign;

    public static ScientificMatcher getInstance(DecimalFormatSymbols symbols, Grouper grouper) {
        return new ScientificMatcher(symbols, grouper);
    }

    private ScientificMatcher(DecimalFormatSymbols symbols, Grouper grouper) {
        this.exponentSeparatorString = symbols.getExponentSeparator();
        this.exponentMatcher = DecimalMatcher.getInstance(symbols, grouper, 48);
        String minusSign = symbols.getMinusSignString();
        this.customMinusSign = ParsingUtils.safeContains(ScientificMatcher.minusSignSet(), minusSign) ? null : minusSign;
        String plusSign = symbols.getPlusSignString();
        this.customPlusSign = ParsingUtils.safeContains(ScientificMatcher.plusSignSet(), plusSign) ? null : plusSign;
    }

    private static UnicodeSet minusSignSet() {
        return StaticUnicodeSets.get(StaticUnicodeSets.Key.MINUS_SIGN);
    }

    private static UnicodeSet plusSignSet() {
        return StaticUnicodeSets.get(StaticUnicodeSets.Key.PLUS_SIGN);
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        if (!result.seenNumber()) {
            return false;
        }
        int overlap1 = segment.getCommonPrefixLength(this.exponentSeparatorString);
        if (overlap1 == this.exponentSeparatorString.length()) {
            boolean wasNull;
            int overlap2;
            if (segment.length() == overlap1) {
                return true;
            }
            segment.adjustOffset(overlap1);
            int exponentSign = 1;
            if (segment.startsWith(ScientificMatcher.minusSignSet())) {
                exponentSign = -1;
                segment.adjustOffsetByCodePoint();
            } else if (segment.startsWith(ScientificMatcher.plusSignSet())) {
                segment.adjustOffsetByCodePoint();
            } else if (segment.startsWith(this.customMinusSign)) {
                overlap2 = segment.getCommonPrefixLength(this.customMinusSign);
                if (overlap2 != this.customMinusSign.length()) {
                    segment.adjustOffset(-overlap1);
                    return true;
                }
                exponentSign = -1;
                segment.adjustOffset(overlap2);
            } else if (segment.startsWith(this.customPlusSign)) {
                overlap2 = segment.getCommonPrefixLength(this.customPlusSign);
                if (overlap2 != this.customPlusSign.length()) {
                    segment.adjustOffset(-overlap1);
                    return true;
                }
                segment.adjustOffset(overlap2);
            }
            boolean bl = wasNull = result.quantity == null;
            if (wasNull) {
                result.quantity = new DecimalQuantity_DualStorageBCD();
            }
            int digitsOffset = segment.getOffset();
            boolean digitsReturnValue = this.exponentMatcher.match(segment, result, exponentSign);
            if (wasNull) {
                result.quantity = null;
            }
            if (segment.getOffset() != digitsOffset) {
                result.flags |= 8;
            } else {
                segment.adjustOffset(-overlap1);
            }
            return digitsReturnValue;
        }
        return overlap1 == segment.length();
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        return segment.startsWith(this.exponentSeparatorString);
    }

    @Override
    public void postProcess(ParsedNumber result) {
    }

    public String toString() {
        return "<ScientificMatcher " + this.exponentSeparatorString + ">";
    }
}

