/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StaticUnicodeSets;
import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.impl.number.parse.ParsingUtils;
import com.ibm.icu.impl.number.parse.SymbolMatcher;
import com.ibm.icu.text.DecimalFormatSymbols;

public class MinusSignMatcher
extends SymbolMatcher {
    private static final MinusSignMatcher DEFAULT = new MinusSignMatcher(false);
    private static final MinusSignMatcher DEFAULT_ALLOW_TRAILING = new MinusSignMatcher(true);
    private final boolean allowTrailing;

    public static MinusSignMatcher getInstance(DecimalFormatSymbols symbols, boolean allowTrailing) {
        String symbolString = symbols.getMinusSignString();
        if (ParsingUtils.safeContains(MinusSignMatcher.DEFAULT.uniSet, symbolString)) {
            return allowTrailing ? DEFAULT_ALLOW_TRAILING : DEFAULT;
        }
        return new MinusSignMatcher(symbolString, allowTrailing);
    }

    private MinusSignMatcher(String symbolString, boolean allowTrailing) {
        super(symbolString, MinusSignMatcher.DEFAULT.uniSet);
        this.allowTrailing = allowTrailing;
    }

    private MinusSignMatcher(boolean allowTrailing) {
        super(StaticUnicodeSets.Key.MINUS_SIGN);
        this.allowTrailing = allowTrailing;
    }

    @Override
    protected boolean isDisabled(ParsedNumber result) {
        return !this.allowTrailing && result.seenNumber();
    }

    @Override
    protected void accept(StringSegment segment, ParsedNumber result) {
        result.flags |= 1;
        result.setCharsConsumed(segment);
    }

    public String toString() {
        return "<MinusSignMatcher>";
    }
}

