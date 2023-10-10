/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.parse.NumberParseMatcher;
import com.ibm.icu.impl.number.parse.ParsedNumber;
import java.util.ArrayList;
import java.util.List;

public class SeriesMatcher
implements NumberParseMatcher {
    protected List<NumberParseMatcher> matchers = null;
    protected boolean frozen = false;

    public void addMatcher(NumberParseMatcher matcher) {
        assert (!this.frozen);
        if (this.matchers == null) {
            this.matchers = new ArrayList<NumberParseMatcher>();
        }
        this.matchers.add(matcher);
    }

    public void freeze() {
        this.frozen = true;
    }

    public int length() {
        return this.matchers == null ? 0 : this.matchers.size();
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        assert (this.frozen);
        if (this.matchers == null) {
            return false;
        }
        ParsedNumber backup = new ParsedNumber();
        backup.copyFrom(result);
        int initialOffset = segment.getOffset();
        boolean maybeMore = true;
        int i2 = 0;
        while (i2 < this.matchers.size()) {
            NumberParseMatcher matcher = this.matchers.get(i2);
            int matcherOffset = segment.getOffset();
            maybeMore = segment.length() != 0 ? matcher.match(segment, result) : true;
            boolean success = segment.getOffset() != matcherOffset;
            boolean isFlexible = matcher instanceof NumberParseMatcher.Flexible;
            if (success && isFlexible) continue;
            if (success) {
                if (++i2 >= this.matchers.size() || segment.getOffset() == result.charEnd || result.charEnd <= matcherOffset) continue;
                segment.setOffset(result.charEnd);
                continue;
            }
            if (isFlexible) {
                ++i2;
                continue;
            }
            segment.setOffset(initialOffset);
            result.copyFrom(backup);
            return maybeMore;
        }
        return maybeMore;
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        assert (this.frozen);
        if (this.matchers == null) {
            return false;
        }
        assert (!(this.matchers.get(0) instanceof NumberParseMatcher.Flexible));
        return this.matchers.get(0).smokeTest(segment);
    }

    @Override
    public void postProcess(ParsedNumber result) {
        assert (this.frozen);
        if (this.matchers == null) {
            return;
        }
        for (int i2 = 0; i2 < this.matchers.size(); ++i2) {
            NumberParseMatcher matcher = this.matchers.get(i2);
            matcher.postProcess(result);
        }
    }

    public String toString() {
        return "<SeriesMatcher " + this.matchers + ">";
    }
}

