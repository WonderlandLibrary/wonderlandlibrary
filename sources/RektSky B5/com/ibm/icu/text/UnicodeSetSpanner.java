/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.OutputInt;

public class UnicodeSetSpanner {
    private final UnicodeSet unicodeSet;

    public UnicodeSetSpanner(UnicodeSet source) {
        this.unicodeSet = source;
    }

    public UnicodeSet getUnicodeSet() {
        return this.unicodeSet;
    }

    public boolean equals(Object other) {
        return other instanceof UnicodeSetSpanner && this.unicodeSet.equals(((UnicodeSetSpanner)other).unicodeSet);
    }

    public int hashCode() {
        return this.unicodeSet.hashCode();
    }

    public int countIn(CharSequence sequence) {
        return this.countIn(sequence, CountMethod.MIN_ELEMENTS, UnicodeSet.SpanCondition.SIMPLE);
    }

    public int countIn(CharSequence sequence, CountMethod countMethod) {
        return this.countIn(sequence, countMethod, UnicodeSet.SpanCondition.SIMPLE);
    }

    public int countIn(CharSequence sequence, CountMethod countMethod, UnicodeSet.SpanCondition spanCondition) {
        int endOfSpan;
        int count = 0;
        int start = 0;
        UnicodeSet.SpanCondition skipSpan = spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED ? UnicodeSet.SpanCondition.SIMPLE : UnicodeSet.SpanCondition.NOT_CONTAINED;
        int length = sequence.length();
        OutputInt spanCount = null;
        while (start != length && (endOfSpan = this.unicodeSet.span(sequence, start, skipSpan)) != length) {
            if (countMethod == CountMethod.WHOLE_SPAN) {
                start = this.unicodeSet.span(sequence, endOfSpan, spanCondition);
                ++count;
                continue;
            }
            if (spanCount == null) {
                spanCount = new OutputInt();
            }
            start = this.unicodeSet.spanAndCount(sequence, endOfSpan, spanCondition, spanCount);
            count += spanCount.value;
        }
        return count;
    }

    public String deleteFrom(CharSequence sequence) {
        return this.replaceFrom(sequence, "", CountMethod.WHOLE_SPAN, UnicodeSet.SpanCondition.SIMPLE);
    }

    public String deleteFrom(CharSequence sequence, UnicodeSet.SpanCondition spanCondition) {
        return this.replaceFrom(sequence, "", CountMethod.WHOLE_SPAN, spanCondition);
    }

    public String replaceFrom(CharSequence sequence, CharSequence replacement) {
        return this.replaceFrom(sequence, replacement, CountMethod.MIN_ELEMENTS, UnicodeSet.SpanCondition.SIMPLE);
    }

    public String replaceFrom(CharSequence sequence, CharSequence replacement, CountMethod countMethod) {
        return this.replaceFrom(sequence, replacement, countMethod, UnicodeSet.SpanCondition.SIMPLE);
    }

    public String replaceFrom(CharSequence sequence, CharSequence replacement, CountMethod countMethod, UnicodeSet.SpanCondition spanCondition) {
        UnicodeSet.SpanCondition copySpan = spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED ? UnicodeSet.SpanCondition.SIMPLE : UnicodeSet.SpanCondition.NOT_CONTAINED;
        boolean remove = replacement.length() == 0;
        StringBuilder result = new StringBuilder();
        int length = sequence.length();
        OutputInt spanCount = null;
        int endCopy = 0;
        while (endCopy != length) {
            int endModify;
            if (countMethod == CountMethod.WHOLE_SPAN) {
                endModify = this.unicodeSet.span(sequence, endCopy, spanCondition);
            } else {
                if (spanCount == null) {
                    spanCount = new OutputInt();
                }
                endModify = this.unicodeSet.spanAndCount(sequence, endCopy, spanCondition, spanCount);
            }
            if (!remove && endModify != 0) {
                if (countMethod == CountMethod.WHOLE_SPAN) {
                    result.append(replacement);
                } else {
                    for (int i2 = spanCount.value; i2 > 0; --i2) {
                        result.append(replacement);
                    }
                }
            }
            if (endModify == length) break;
            endCopy = this.unicodeSet.span(sequence, endModify, copySpan);
            result.append(sequence.subSequence(endModify, endCopy));
        }
        return result.toString();
    }

    public CharSequence trim(CharSequence sequence) {
        return this.trim(sequence, TrimOption.BOTH, UnicodeSet.SpanCondition.SIMPLE);
    }

    public CharSequence trim(CharSequence sequence, TrimOption trimOption) {
        return this.trim(sequence, trimOption, UnicodeSet.SpanCondition.SIMPLE);
    }

    public CharSequence trim(CharSequence sequence, TrimOption trimOption, UnicodeSet.SpanCondition spanCondition) {
        int endLeadContained;
        int length = sequence.length();
        if (trimOption != TrimOption.TRAILING) {
            endLeadContained = this.unicodeSet.span(sequence, spanCondition);
            if (endLeadContained == length) {
                return "";
            }
        } else {
            endLeadContained = 0;
        }
        int startTrailContained = trimOption != TrimOption.LEADING ? this.unicodeSet.spanBack(sequence, spanCondition) : length;
        return endLeadContained == 0 && startTrailContained == length ? sequence : sequence.subSequence(endLeadContained, startTrailContained);
    }

    public static enum TrimOption {
        LEADING,
        BOTH,
        TRAILING;

    }

    public static enum CountMethod {
        WHOLE_SPAN,
        MIN_ELEMENTS;

    }
}

