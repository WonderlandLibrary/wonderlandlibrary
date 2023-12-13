/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.RuleBasedTransliterator;
import com.ibm.icu.text.StringMatcher;
import com.ibm.icu.text.StringReplacer;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.UnicodeReplacer;
import com.ibm.icu.text.UnicodeSet;

class TransliterationRule {
    private StringMatcher anteContext;
    private StringMatcher key;
    private StringMatcher postContext;
    private UnicodeReplacer output;
    private String pattern;
    UnicodeMatcher[] segments;
    private int anteContextLength;
    private int keyLength;
    byte flags;
    static final int ANCHOR_START = 1;
    static final int ANCHOR_END = 2;
    private final RuleBasedTransliterator.Data data;

    public TransliterationRule(String input, int anteContextPos, int postContextPos, String output, int cursorPos, int cursorOffset, UnicodeMatcher[] segs, boolean anchorStart, boolean anchorEnd, RuleBasedTransliterator.Data theData) {
        this.data = theData;
        if (anteContextPos < 0) {
            this.anteContextLength = 0;
        } else {
            if (anteContextPos > input.length()) {
                throw new IllegalArgumentException("Invalid ante context");
            }
            this.anteContextLength = anteContextPos;
        }
        if (postContextPos < 0) {
            this.keyLength = input.length() - this.anteContextLength;
        } else {
            if (postContextPos < this.anteContextLength || postContextPos > input.length()) {
                throw new IllegalArgumentException("Invalid post context");
            }
            this.keyLength = postContextPos - this.anteContextLength;
        }
        if (cursorPos < 0) {
            cursorPos = output.length();
        } else if (cursorPos > output.length()) {
            throw new IllegalArgumentException("Invalid cursor position");
        }
        this.segments = segs;
        this.pattern = input;
        this.flags = 0;
        if (anchorStart) {
            this.flags = (byte)(this.flags | 1);
        }
        if (anchorEnd) {
            this.flags = (byte)(this.flags | 2);
        }
        this.anteContext = null;
        if (this.anteContextLength > 0) {
            this.anteContext = new StringMatcher(this.pattern.substring(0, this.anteContextLength), 0, this.data);
        }
        this.key = null;
        if (this.keyLength > 0) {
            this.key = new StringMatcher(this.pattern.substring(this.anteContextLength, this.anteContextLength + this.keyLength), 0, this.data);
        }
        int postContextLength = this.pattern.length() - this.keyLength - this.anteContextLength;
        this.postContext = null;
        if (postContextLength > 0) {
            this.postContext = new StringMatcher(this.pattern.substring(this.anteContextLength + this.keyLength), 0, this.data);
        }
        this.output = new StringReplacer(output, cursorPos + cursorOffset, this.data);
    }

    public int getAnteContextLength() {
        return this.anteContextLength + ((this.flags & 1) != 0 ? 1 : 0);
    }

    final int getIndexValue() {
        if (this.anteContextLength == this.pattern.length()) {
            return -1;
        }
        int c2 = UTF16.charAt(this.pattern, this.anteContextLength);
        return this.data.lookupMatcher(c2) == null ? c2 & 0xFF : -1;
    }

    final boolean matchesIndexValue(int v2) {
        StringMatcher m2 = this.key != null ? this.key : this.postContext;
        return m2 != null ? m2.matchesIndexValue(v2) : true;
    }

    public boolean masks(TransliterationRule r2) {
        int len = this.pattern.length();
        int left = this.anteContextLength;
        int left2 = r2.anteContextLength;
        int right = this.pattern.length() - left;
        int right2 = r2.pattern.length() - left2;
        if (left == left2 && right == right2 && this.keyLength <= r2.keyLength && r2.pattern.regionMatches(0, this.pattern, 0, len)) {
            return this.flags == r2.flags || (this.flags & 1) == 0 && (this.flags & 2) == 0 || (r2.flags & 1) != 0 && (r2.flags & 2) != 0;
        }
        return left <= left2 && (right < right2 || right == right2 && this.keyLength <= r2.keyLength) && r2.pattern.regionMatches(left2 - left, this.pattern, 0, len);
    }

    static final int posBefore(Replaceable str, int pos) {
        return pos > 0 ? pos - UTF16.getCharCount(str.char32At(pos - 1)) : pos - 1;
    }

    static final int posAfter(Replaceable str, int pos) {
        return pos >= 0 && pos < str.length() ? pos + UTF16.getCharCount(str.char32At(pos)) : pos + 1;
    }

    public int matchAndReplace(Replaceable text, Transliterator.Position pos, boolean incremental) {
        int match;
        if (this.segments != null) {
            for (int i2 = 0; i2 < this.segments.length; ++i2) {
                ((StringMatcher)this.segments[i2]).resetMatch();
            }
        }
        int[] intRef = new int[1];
        int anteLimit = TransliterationRule.posBefore(text, pos.contextStart);
        intRef[0] = TransliterationRule.posBefore(text, pos.start);
        if (this.anteContext != null && (match = this.anteContext.matches(text, intRef, anteLimit, false)) != 2) {
            return 0;
        }
        int oText = intRef[0];
        int minOText = TransliterationRule.posAfter(text, oText);
        if ((this.flags & 1) != 0 && oText != anteLimit) {
            return 0;
        }
        intRef[0] = pos.start;
        if (this.key != null && (match = this.key.matches(text, intRef, pos.limit, incremental)) != 2) {
            return match;
        }
        int keyLimit = intRef[0];
        if (this.postContext != null) {
            if (incremental && keyLimit == pos.limit) {
                return 1;
            }
            match = this.postContext.matches(text, intRef, pos.contextLimit, incremental);
            if (match != 2) {
                return match;
            }
        }
        oText = intRef[0];
        if ((this.flags & 2) != 0) {
            if (oText != pos.contextLimit) {
                return 0;
            }
            if (incremental) {
                return 1;
            }
        }
        int newLength = this.output.replace(text, pos.start, keyLimit, intRef);
        int lenDelta = newLength - (keyLimit - pos.start);
        int newStart = intRef[0];
        pos.limit += lenDelta;
        pos.contextLimit += lenDelta;
        pos.start = Math.max(minOText, Math.min(Math.min(oText += lenDelta, pos.limit), newStart));
        return 2;
    }

    public String toRule(boolean escapeUnprintable) {
        boolean emitBraces;
        StringBuffer rule = new StringBuffer();
        StringBuffer quoteBuf = new StringBuffer();
        boolean bl = emitBraces = this.anteContext != null || this.postContext != null;
        if ((this.flags & 1) != 0) {
            rule.append('^');
        }
        Utility.appendToRule(rule, this.anteContext, escapeUnprintable, quoteBuf);
        if (emitBraces) {
            Utility.appendToRule(rule, 123, true, escapeUnprintable, quoteBuf);
        }
        Utility.appendToRule(rule, this.key, escapeUnprintable, quoteBuf);
        if (emitBraces) {
            Utility.appendToRule(rule, 125, true, escapeUnprintable, quoteBuf);
        }
        Utility.appendToRule(rule, this.postContext, escapeUnprintable, quoteBuf);
        if ((this.flags & 2) != 0) {
            rule.append('$');
        }
        Utility.appendToRule(rule, " > ", true, escapeUnprintable, quoteBuf);
        Utility.appendToRule(rule, this.output.toReplacerPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
        Utility.appendToRule(rule, 59, true, escapeUnprintable, quoteBuf);
        return rule.toString();
    }

    public String toString() {
        return '{' + this.toRule(true) + '}';
    }

    void addSourceTargetSet(UnicodeSet filter, UnicodeSet sourceSet, UnicodeSet targetSet, UnicodeSet revisiting) {
        int limit = this.anteContextLength + this.keyLength;
        UnicodeSet tempSource = new UnicodeSet();
        UnicodeSet temp = new UnicodeSet();
        int i2 = this.anteContextLength;
        while (i2 < limit) {
            int ch = UTF16.charAt(this.pattern, i2);
            i2 += UTF16.getCharCount(ch);
            UnicodeMatcher matcher = this.data.lookupMatcher(ch);
            if (matcher == null) {
                if (!filter.contains(ch)) {
                    return;
                }
                tempSource.add(ch);
                continue;
            }
            try {
                if (!filter.containsSome((UnicodeSet)matcher)) {
                    return;
                }
                matcher.addMatchSetTo(tempSource);
            }
            catch (ClassCastException e2) {
                temp.clear();
                matcher.addMatchSetTo(temp);
                if (!filter.containsSome(temp)) {
                    return;
                }
                tempSource.addAll(temp);
            }
        }
        sourceSet.addAll(tempSource);
        this.output.addReplacementSetTo(targetSet);
    }
}

