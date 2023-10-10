/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ICUUncheckedIOException;
import java.io.IOException;

public class FilteredNormalizer2
extends Normalizer2 {
    private Normalizer2 norm2;
    private UnicodeSet set;

    public FilteredNormalizer2(Normalizer2 n2, UnicodeSet filterSet) {
        this.norm2 = n2;
        this.set = filterSet;
    }

    @Override
    public StringBuilder normalize(CharSequence src, StringBuilder dest) {
        if (dest == src) {
            throw new IllegalArgumentException();
        }
        dest.setLength(0);
        this.normalize(src, dest, UnicodeSet.SpanCondition.SIMPLE);
        return dest;
    }

    @Override
    public Appendable normalize(CharSequence src, Appendable dest) {
        if (dest == src) {
            throw new IllegalArgumentException();
        }
        return this.normalize(src, dest, UnicodeSet.SpanCondition.SIMPLE);
    }

    @Override
    public StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second) {
        return this.normalizeSecondAndAppend(first, second, true);
    }

    @Override
    public StringBuilder append(StringBuilder first, CharSequence second) {
        return this.normalizeSecondAndAppend(first, second, false);
    }

    @Override
    public String getDecomposition(int c2) {
        return this.set.contains(c2) ? this.norm2.getDecomposition(c2) : null;
    }

    @Override
    public String getRawDecomposition(int c2) {
        return this.set.contains(c2) ? this.norm2.getRawDecomposition(c2) : null;
    }

    @Override
    public int composePair(int a2, int b2) {
        return this.set.contains(a2) && this.set.contains(b2) ? this.norm2.composePair(a2, b2) : -1;
    }

    @Override
    public int getCombiningClass(int c2) {
        return this.set.contains(c2) ? this.norm2.getCombiningClass(c2) : 0;
    }

    @Override
    public boolean isNormalized(CharSequence s2) {
        UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
        int prevSpanLimit = 0;
        while (prevSpanLimit < s2.length()) {
            int spanLimit = this.set.span(s2, prevSpanLimit, spanCondition);
            if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                spanCondition = UnicodeSet.SpanCondition.SIMPLE;
            } else {
                if (!this.norm2.isNormalized(s2.subSequence(prevSpanLimit, spanLimit))) {
                    return false;
                }
                spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
            prevSpanLimit = spanLimit;
        }
        return true;
    }

    @Override
    public Normalizer.QuickCheckResult quickCheck(CharSequence s2) {
        Normalizer.QuickCheckResult result = Normalizer.YES;
        UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
        int prevSpanLimit = 0;
        while (prevSpanLimit < s2.length()) {
            int spanLimit = this.set.span(s2, prevSpanLimit, spanCondition);
            if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                spanCondition = UnicodeSet.SpanCondition.SIMPLE;
            } else {
                Normalizer.QuickCheckResult qcResult = this.norm2.quickCheck(s2.subSequence(prevSpanLimit, spanLimit));
                if (qcResult == Normalizer.NO) {
                    return qcResult;
                }
                if (qcResult == Normalizer.MAYBE) {
                    result = qcResult;
                }
                spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
            prevSpanLimit = spanLimit;
        }
        return result;
    }

    @Override
    public int spanQuickCheckYes(CharSequence s2) {
        UnicodeSet.SpanCondition spanCondition = UnicodeSet.SpanCondition.SIMPLE;
        int prevSpanLimit = 0;
        while (prevSpanLimit < s2.length()) {
            int spanLimit = this.set.span(s2, prevSpanLimit, spanCondition);
            if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                spanCondition = UnicodeSet.SpanCondition.SIMPLE;
            } else {
                int yesLimit = prevSpanLimit + this.norm2.spanQuickCheckYes(s2.subSequence(prevSpanLimit, spanLimit));
                if (yesLimit < spanLimit) {
                    return yesLimit;
                }
                spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
            prevSpanLimit = spanLimit;
        }
        return s2.length();
    }

    @Override
    public boolean hasBoundaryBefore(int c2) {
        return !this.set.contains(c2) || this.norm2.hasBoundaryBefore(c2);
    }

    @Override
    public boolean hasBoundaryAfter(int c2) {
        return !this.set.contains(c2) || this.norm2.hasBoundaryAfter(c2);
    }

    @Override
    public boolean isInert(int c2) {
        return !this.set.contains(c2) || this.norm2.isInert(c2);
    }

    private Appendable normalize(CharSequence src, Appendable dest, UnicodeSet.SpanCondition spanCondition) {
        StringBuilder tempDest = new StringBuilder();
        try {
            int prevSpanLimit = 0;
            while (prevSpanLimit < src.length()) {
                int spanLimit = this.set.span(src, prevSpanLimit, spanCondition);
                int spanLength = spanLimit - prevSpanLimit;
                if (spanCondition == UnicodeSet.SpanCondition.NOT_CONTAINED) {
                    if (spanLength != 0) {
                        dest.append(src, prevSpanLimit, spanLimit);
                    }
                    spanCondition = UnicodeSet.SpanCondition.SIMPLE;
                } else {
                    if (spanLength != 0) {
                        dest.append(this.norm2.normalize(src.subSequence(prevSpanLimit, spanLimit), tempDest));
                    }
                    spanCondition = UnicodeSet.SpanCondition.NOT_CONTAINED;
                }
                prevSpanLimit = spanLimit;
            }
        }
        catch (IOException e2) {
            throw new ICUUncheckedIOException(e2);
        }
        return dest;
    }

    private StringBuilder normalizeSecondAndAppend(StringBuilder first, CharSequence second, boolean doNormalize) {
        if (first == second) {
            throw new IllegalArgumentException();
        }
        if (first.length() == 0) {
            if (doNormalize) {
                return this.normalize(second, first);
            }
            return first.append(second);
        }
        int prefixLimit = this.set.span(second, 0, UnicodeSet.SpanCondition.SIMPLE);
        if (prefixLimit != 0) {
            CharSequence prefix = second.subSequence(0, prefixLimit);
            int suffixStart = this.set.spanBack(first, Integer.MAX_VALUE, UnicodeSet.SpanCondition.SIMPLE);
            if (suffixStart == 0) {
                if (doNormalize) {
                    this.norm2.normalizeSecondAndAppend(first, prefix);
                } else {
                    this.norm2.append(first, prefix);
                }
            } else {
                StringBuilder middle = new StringBuilder(first.subSequence(suffixStart, first.length()));
                if (doNormalize) {
                    this.norm2.normalizeSecondAndAppend(middle, prefix);
                } else {
                    this.norm2.append(middle, prefix);
                }
                first.delete(suffixStart, Integer.MAX_VALUE).append((CharSequence)middle);
            }
        }
        if (prefixLimit < second.length()) {
            CharSequence rest = second.subSequence(prefixLimit, second.length());
            if (doNormalize) {
                this.normalize(rest, first, UnicodeSet.SpanCondition.NOT_CONTAINED);
            } else {
                first.append(rest);
            }
        }
        return first;
    }
}

