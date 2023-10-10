/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationIterator;

public class UTF16CollationIterator
extends CollationIterator {
    protected CharSequence seq;
    protected int start;
    protected int pos;
    protected int limit;

    public UTF16CollationIterator(CollationData d2) {
        super(d2);
    }

    public UTF16CollationIterator(CollationData d2, boolean numeric, CharSequence s2, int p2) {
        super(d2, numeric);
        this.seq = s2;
        this.start = 0;
        this.pos = p2;
        this.limit = s2.length();
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        UTF16CollationIterator o2 = (UTF16CollationIterator)other;
        return this.pos - this.start == o2.pos - o2.start;
    }

    @Override
    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    @Override
    public void resetToOffset(int newOffset) {
        this.reset();
        this.pos = this.start + newOffset;
    }

    @Override
    public int getOffset() {
        return this.pos - this.start;
    }

    public void setText(boolean numeric, CharSequence s2, int p2) {
        this.reset(numeric);
        this.seq = s2;
        this.start = 0;
        this.pos = p2;
        this.limit = s2.length();
    }

    @Override
    public int nextCodePoint() {
        char trail;
        char c2;
        if (this.pos == this.limit) {
            return -1;
        }
        if (Character.isHighSurrogate(c2 = this.seq.charAt(this.pos++)) && this.pos != this.limit && Character.isLowSurrogate(trail = this.seq.charAt(this.pos))) {
            ++this.pos;
            return Character.toCodePoint(c2, trail);
        }
        return c2;
    }

    @Override
    public int previousCodePoint() {
        char lead;
        char c2;
        if (this.pos == this.start) {
            return -1;
        }
        if (Character.isLowSurrogate(c2 = this.seq.charAt(--this.pos)) && this.pos != this.start && Character.isHighSurrogate(lead = this.seq.charAt(this.pos - 1))) {
            --this.pos;
            return Character.toCodePoint(lead, c2);
        }
        return c2;
    }

    @Override
    protected long handleNextCE32() {
        if (this.pos == this.limit) {
            return -4294967104L;
        }
        char c2 = this.seq.charAt(this.pos++);
        return this.makeCodePointAndCE32Pair(c2, this.trie.getFromU16SingleLead(c2));
    }

    @Override
    protected char handleGetTrailSurrogate() {
        if (this.pos == this.limit) {
            return '\u0000';
        }
        char trail = this.seq.charAt(this.pos);
        if (Character.isLowSurrogate(trail)) {
            ++this.pos;
        }
        return trail;
    }

    @Override
    protected void forwardNumCodePoints(int num) {
        while (num > 0 && this.pos != this.limit) {
            char c2 = this.seq.charAt(this.pos++);
            --num;
            if (!Character.isHighSurrogate(c2) || this.pos == this.limit || !Character.isLowSurrogate(this.seq.charAt(this.pos))) continue;
            ++this.pos;
        }
    }

    @Override
    protected void backwardNumCodePoints(int num) {
        while (num > 0 && this.pos != this.start) {
            char c2 = this.seq.charAt(--this.pos);
            --num;
            if (!Character.isLowSurrogate(c2) || this.pos == this.start || !Character.isHighSurrogate(this.seq.charAt(this.pos - 1))) continue;
            --this.pos;
        }
    }
}

