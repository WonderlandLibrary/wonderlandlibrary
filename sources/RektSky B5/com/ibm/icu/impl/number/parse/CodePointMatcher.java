/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.parse.NumberParseMatcher;
import com.ibm.icu.impl.number.parse.ParsedNumber;

public class CodePointMatcher
implements NumberParseMatcher {
    private final int cp;

    public static CodePointMatcher getInstance(int cp) {
        return new CodePointMatcher(cp);
    }

    private CodePointMatcher(int cp) {
        this.cp = cp;
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        if (segment.startsWith(this.cp)) {
            segment.adjustOffsetByCodePoint();
            result.setCharsConsumed(segment);
        }
        return false;
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        return segment.startsWith(this.cp);
    }

    @Override
    public void postProcess(ParsedNumber result) {
    }

    public String toString() {
        return "<CodePointMatcher U+" + Integer.toHexString(this.cp) + ">";
    }
}

