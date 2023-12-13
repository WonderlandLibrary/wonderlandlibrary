/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.impl.number.parse.ValidationMatcher;

public class RequireDecimalSeparatorValidator
extends ValidationMatcher {
    private static final RequireDecimalSeparatorValidator A = new RequireDecimalSeparatorValidator(true);
    private static final RequireDecimalSeparatorValidator B = new RequireDecimalSeparatorValidator(false);
    private final boolean patternHasDecimalSeparator;

    public static RequireDecimalSeparatorValidator getInstance(boolean patternHasDecimalSeparator) {
        return patternHasDecimalSeparator ? A : B;
    }

    private RequireDecimalSeparatorValidator(boolean patternHasDecimalSeparator) {
        this.patternHasDecimalSeparator = patternHasDecimalSeparator;
    }

    @Override
    public void postProcess(ParsedNumber result) {
        boolean parseHasDecimalSeparator;
        boolean bl = parseHasDecimalSeparator = 0 != (result.flags & 0x20);
        if (parseHasDecimalSeparator != this.patternHasDecimalSeparator) {
            result.flags |= 0x100;
        }
    }

    public String toString() {
        return "<RequireDecimalSeparator>";
    }
}

