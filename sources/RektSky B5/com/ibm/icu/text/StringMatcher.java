/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.RuleBasedTransliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.UnicodeReplacer;
import com.ibm.icu.text.UnicodeSet;

class StringMatcher
implements UnicodeMatcher,
UnicodeReplacer {
    private String pattern;
    private int matchStart;
    private int matchLimit;
    private int segmentNumber;
    private final RuleBasedTransliterator.Data data;

    public StringMatcher(String theString, int segmentNum, RuleBasedTransliterator.Data theData) {
        this.data = theData;
        this.pattern = theString;
        this.matchLimit = -1;
        this.matchStart = -1;
        this.segmentNumber = segmentNum;
    }

    public StringMatcher(String theString, int start, int limit, int segmentNum, RuleBasedTransliterator.Data theData) {
        this(theString.substring(start, limit), segmentNum, theData);
    }

    @Override
    public int matches(Replaceable text, int[] offset, int limit, boolean incremental) {
        int[] cursor = new int[]{offset[0]};
        if (limit < cursor[0]) {
            for (int i2 = this.pattern.length() - 1; i2 >= 0; --i2) {
                char keyChar = this.pattern.charAt(i2);
                UnicodeMatcher subm = this.data.lookupMatcher(keyChar);
                if (subm == null) {
                    if (cursor[0] > limit && keyChar == text.charAt(cursor[0])) {
                        cursor[0] = cursor[0] - 1;
                        continue;
                    }
                    return 0;
                }
                int m2 = subm.matches(text, cursor, limit, incremental);
                if (m2 == 2) continue;
                return m2;
            }
            if (this.matchStart < 0) {
                this.matchStart = cursor[0] + 1;
                this.matchLimit = offset[0] + 1;
            }
        } else {
            for (int i3 = 0; i3 < this.pattern.length(); ++i3) {
                if (incremental && cursor[0] == limit) {
                    return 1;
                }
                char keyChar = this.pattern.charAt(i3);
                UnicodeMatcher subm = this.data.lookupMatcher(keyChar);
                if (subm == null) {
                    if (cursor[0] < limit && keyChar == text.charAt(cursor[0])) {
                        cursor[0] = cursor[0] + 1;
                        continue;
                    }
                    return 0;
                }
                int m3 = subm.matches(text, cursor, limit, incremental);
                if (m3 == 2) continue;
                return m3;
            }
            this.matchStart = offset[0];
            this.matchLimit = cursor[0];
        }
        offset[0] = cursor[0];
        return 2;
    }

    @Override
    public String toPattern(boolean escapeUnprintable) {
        StringBuffer result = new StringBuffer();
        StringBuffer quoteBuf = new StringBuffer();
        if (this.segmentNumber > 0) {
            result.append('(');
        }
        for (int i2 = 0; i2 < this.pattern.length(); ++i2) {
            char keyChar = this.pattern.charAt(i2);
            UnicodeMatcher m2 = this.data.lookupMatcher(keyChar);
            if (m2 == null) {
                Utility.appendToRule(result, keyChar, false, escapeUnprintable, quoteBuf);
                continue;
            }
            Utility.appendToRule(result, m2.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
        }
        if (this.segmentNumber > 0) {
            result.append(')');
        }
        Utility.appendToRule(result, -1, true, escapeUnprintable, quoteBuf);
        return result.toString();
    }

    @Override
    public boolean matchesIndexValue(int v2) {
        if (this.pattern.length() == 0) {
            return true;
        }
        int c2 = UTF16.charAt(this.pattern, 0);
        UnicodeMatcher m2 = this.data.lookupMatcher(c2);
        return m2 == null ? (c2 & 0xFF) == v2 : m2.matchesIndexValue(v2);
    }

    @Override
    public void addMatchSetTo(UnicodeSet toUnionTo) {
        int ch;
        for (int i2 = 0; i2 < this.pattern.length(); i2 += UTF16.getCharCount(ch)) {
            ch = UTF16.charAt(this.pattern, i2);
            UnicodeMatcher matcher = this.data.lookupMatcher(ch);
            if (matcher == null) {
                toUnionTo.add(ch);
                continue;
            }
            matcher.addMatchSetTo(toUnionTo);
        }
    }

    @Override
    public int replace(Replaceable text, int start, int limit, int[] cursor) {
        int outLen = 0;
        int dest = limit;
        if (this.matchStart >= 0 && this.matchStart != this.matchLimit) {
            text.copy(this.matchStart, this.matchLimit, dest);
            outLen = this.matchLimit - this.matchStart;
        }
        text.replace(start, limit, "");
        return outLen;
    }

    @Override
    public String toReplacerPattern(boolean escapeUnprintable) {
        StringBuffer rule = new StringBuffer("$");
        Utility.appendNumber(rule, this.segmentNumber, 10, 1);
        return rule.toString();
    }

    public void resetMatch() {
        this.matchLimit = -1;
        this.matchStart = -1;
    }

    @Override
    public void addReplacementSetTo(UnicodeSet toUnionTo) {
    }
}

