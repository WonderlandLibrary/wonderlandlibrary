/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.UnicodeSet;

class Quantifier
implements UnicodeMatcher {
    private UnicodeMatcher matcher;
    private int minCount;
    private int maxCount;
    public static final int MAX = Integer.MAX_VALUE;

    public Quantifier(UnicodeMatcher theMatcher, int theMinCount, int theMaxCount) {
        if (theMatcher == null || theMinCount < 0 || theMaxCount < 0 || theMinCount > theMaxCount) {
            throw new IllegalArgumentException();
        }
        this.matcher = theMatcher;
        this.minCount = theMinCount;
        this.maxCount = theMaxCount;
    }

    public int matches(Replaceable text, int[] offset, int limit, boolean incremental) {
        int count;
        int start = offset[0];
        for (count = 0; count < this.maxCount; ++count) {
            int pos = offset[0];
            int m = this.matcher.matches(text, offset, limit, incremental);
            if (m == 2) {
                if (pos != offset[0]) continue;
                break;
            }
            if (!incremental || m != 1) break;
            return 1;
        }
        if (incremental && offset[0] == limit) {
            return 1;
        }
        if (count >= this.minCount) {
            return 2;
        }
        offset[0] = start;
        return 0;
    }

    public String toPattern(boolean escapeUnprintable) {
        StringBuilder result = new StringBuilder();
        result.append(this.matcher.toPattern(escapeUnprintable));
        if (this.minCount == 0) {
            if (this.maxCount == 1) {
                return result.append('?').toString();
            }
            if (this.maxCount == Integer.MAX_VALUE) {
                return result.append('*').toString();
            }
        } else if (this.minCount == 1 && this.maxCount == Integer.MAX_VALUE) {
            return result.append('+').toString();
        }
        result.append('{');
        result.append(Utility.hex(this.minCount, 1));
        result.append(',');
        if (this.maxCount != Integer.MAX_VALUE) {
            result.append(Utility.hex(this.maxCount, 1));
        }
        result.append('}');
        return result.toString();
    }

    public boolean matchesIndexValue(int v) {
        return this.minCount == 0 || this.matcher.matchesIndexValue(v);
    }

    public void addMatchSetTo(UnicodeSet toUnionTo) {
        if (this.maxCount > 0) {
            this.matcher.addMatchSetTo(toUnionTo);
        }
    }
}

