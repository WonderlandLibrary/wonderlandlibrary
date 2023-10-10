/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationFCD;
import com.ibm.icu.impl.coll.CollationIterator;
import com.ibm.icu.impl.coll.UTF16CollationIterator;

public final class FCDUTF16CollationIterator
extends UTF16CollationIterator {
    private CharSequence rawSeq;
    private static final int rawStart = 0;
    private int segmentStart;
    private int segmentLimit;
    private int rawLimit;
    private final Normalizer2Impl nfcImpl;
    private StringBuilder normalized;
    private int checkDir;

    public FCDUTF16CollationIterator(CollationData d2) {
        super(d2);
        this.nfcImpl = d2.nfcImpl;
    }

    public FCDUTF16CollationIterator(CollationData data, boolean numeric, CharSequence s2, int p2) {
        super(data, numeric, s2, p2);
        this.rawSeq = s2;
        this.segmentStart = p2;
        this.rawLimit = s2.length();
        this.nfcImpl = data.nfcImpl;
        this.checkDir = 1;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CollationIterator && ((CollationIterator)this).equals(other) && other instanceof FCDUTF16CollationIterator)) {
            return false;
        }
        FCDUTF16CollationIterator o2 = (FCDUTF16CollationIterator)other;
        if (this.checkDir != o2.checkDir) {
            return false;
        }
        if (this.checkDir == 0 && this.seq == this.rawSeq != (o2.seq == o2.rawSeq)) {
            return false;
        }
        if (this.checkDir != 0 || this.seq == this.rawSeq) {
            return this.pos - 0 == o2.pos - 0;
        }
        return this.segmentStart - 0 == o2.segmentStart - 0 && this.pos - this.start == o2.pos - o2.start;
    }

    @Override
    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    @Override
    public void resetToOffset(int newOffset) {
        this.reset();
        this.seq = this.rawSeq;
        this.segmentStart = this.pos = 0 + newOffset;
        this.start = this.pos;
        this.limit = this.rawLimit;
        this.checkDir = 1;
    }

    @Override
    public int getOffset() {
        if (this.checkDir != 0 || this.seq == this.rawSeq) {
            return this.pos - 0;
        }
        if (this.pos == this.start) {
            return this.segmentStart - 0;
        }
        return this.segmentLimit - 0;
    }

    @Override
    public void setText(boolean numeric, CharSequence s2, int p2) {
        super.setText(numeric, s2, p2);
        this.rawSeq = s2;
        this.segmentStart = p2;
        this.rawLimit = this.limit = s2.length();
        this.checkDir = 1;
    }

    @Override
    public int nextCodePoint() {
        char trail;
        char c2;
        while (true) {
            if (this.checkDir > 0) {
                if (this.pos == this.limit) {
                    return -1;
                }
                if (!CollationFCD.hasTccc(c2 = this.seq.charAt(this.pos++)) || !CollationFCD.maybeTibetanCompositeVowel(c2) && (this.pos == this.limit || !CollationFCD.hasLccc(this.seq.charAt(this.pos)))) break;
                --this.pos;
                this.nextSegment();
                c2 = this.seq.charAt(this.pos++);
                break;
            }
            if (this.checkDir == 0 && this.pos != this.limit) {
                c2 = this.seq.charAt(this.pos++);
                break;
            }
            this.switchToForward();
        }
        if (Character.isHighSurrogate(c2) && this.pos != this.limit && Character.isLowSurrogate(trail = this.seq.charAt(this.pos))) {
            ++this.pos;
            return Character.toCodePoint(c2, trail);
        }
        return c2;
    }

    @Override
    public int previousCodePoint() {
        char lead;
        char c2;
        while (true) {
            if (this.checkDir < 0) {
                if (this.pos == this.start) {
                    return -1;
                }
                if (!CollationFCD.hasLccc(c2 = this.seq.charAt(--this.pos)) || !CollationFCD.maybeTibetanCompositeVowel(c2) && (this.pos == this.start || !CollationFCD.hasTccc(this.seq.charAt(this.pos - 1)))) break;
                ++this.pos;
                this.previousSegment();
                c2 = this.seq.charAt(--this.pos);
                break;
            }
            if (this.checkDir == 0 && this.pos != this.start) {
                c2 = this.seq.charAt(--this.pos);
                break;
            }
            this.switchToBackward();
        }
        if (Character.isLowSurrogate(c2) && this.pos != this.start && Character.isHighSurrogate(lead = this.seq.charAt(this.pos - 1))) {
            --this.pos;
            return Character.toCodePoint(lead, c2);
        }
        return c2;
    }

    @Override
    protected long handleNextCE32() {
        char c2;
        while (true) {
            if (this.checkDir > 0) {
                if (this.pos == this.limit) {
                    return -4294967104L;
                }
                if (!CollationFCD.hasTccc(c2 = this.seq.charAt(this.pos++)) || !CollationFCD.maybeTibetanCompositeVowel(c2) && (this.pos == this.limit || !CollationFCD.hasLccc(this.seq.charAt(this.pos)))) break;
                --this.pos;
                this.nextSegment();
                c2 = this.seq.charAt(this.pos++);
                break;
            }
            if (this.checkDir == 0 && this.pos != this.limit) {
                c2 = this.seq.charAt(this.pos++);
                break;
            }
            this.switchToForward();
        }
        return this.makeCodePointAndCE32Pair(c2, this.trie.getFromU16SingleLead(c2));
    }

    @Override
    protected void forwardNumCodePoints(int num) {
        while (num > 0 && this.nextCodePoint() >= 0) {
            --num;
        }
    }

    @Override
    protected void backwardNumCodePoints(int num) {
        while (num > 0 && this.previousCodePoint() >= 0) {
            --num;
        }
    }

    private void switchToForward() {
        assert (this.checkDir < 0 && this.seq == this.rawSeq || this.checkDir == 0 && this.pos == this.limit);
        if (this.checkDir < 0) {
            this.start = this.segmentStart = this.pos;
            if (this.pos == this.segmentLimit) {
                this.limit = this.rawLimit;
                this.checkDir = 1;
            } else {
                this.checkDir = 0;
            }
        } else {
            if (this.seq != this.rawSeq) {
                this.seq = this.rawSeq;
                this.start = this.segmentStart = this.segmentLimit;
                this.pos = this.segmentStart;
            }
            this.limit = this.rawLimit;
            this.checkDir = 1;
        }
    }

    private void nextSegment() {
        block6: {
            assert (this.checkDir > 0 && this.seq == this.rawSeq && this.pos != this.limit);
            int p2 = this.pos;
            int prevCC = 0;
            do {
                int q2 = p2;
                int c2 = Character.codePointAt(this.seq, p2);
                p2 += Character.charCount(c2);
                int fcd16 = this.nfcImpl.getFCD16(c2);
                int leadCC = fcd16 >> 8;
                if (leadCC == 0 && q2 != this.pos) {
                    this.limit = this.segmentLimit = q2;
                    break block6;
                }
                if (leadCC != 0 && (prevCC > leadCC || CollationFCD.isFCD16OfTibetanCompositeVowel(fcd16))) {
                    do {
                        q2 = p2;
                        if (p2 == this.rawLimit) break;
                        c2 = Character.codePointAt(this.seq, p2);
                        p2 += Character.charCount(c2);
                    } while (this.nfcImpl.getFCD16(c2) > 255);
                    this.normalize(this.pos, q2);
                    this.pos = this.start;
                    break block6;
                }
                prevCC = fcd16 & 0xFF;
            } while (p2 != this.rawLimit && prevCC != 0);
            this.limit = this.segmentLimit = p2;
        }
        assert (this.pos != this.limit);
        this.checkDir = 0;
    }

    private void switchToBackward() {
        assert (this.checkDir > 0 && this.seq == this.rawSeq || this.checkDir == 0 && this.pos == this.start);
        if (this.checkDir > 0) {
            this.limit = this.segmentLimit = this.pos;
            if (this.pos == this.segmentStart) {
                this.start = 0;
                this.checkDir = -1;
            } else {
                this.checkDir = 0;
            }
        } else {
            if (this.seq != this.rawSeq) {
                this.seq = this.rawSeq;
                this.limit = this.segmentLimit = this.segmentStart;
                this.pos = this.segmentLimit;
            }
            this.start = 0;
            this.checkDir = -1;
        }
    }

    private void previousSegment() {
        block6: {
            assert (this.checkDir < 0 && this.seq == this.rawSeq && this.pos != this.start);
            int p2 = this.pos;
            int nextCC = 0;
            do {
                int q2 = p2;
                int c2 = Character.codePointBefore(this.seq, p2);
                p2 -= Character.charCount(c2);
                int fcd16 = this.nfcImpl.getFCD16(c2);
                int trailCC = fcd16 & 0xFF;
                if (trailCC == 0 && q2 != this.pos) {
                    this.start = this.segmentStart = q2;
                    break block6;
                }
                if (trailCC != 0 && (nextCC != 0 && trailCC > nextCC || CollationFCD.isFCD16OfTibetanCompositeVowel(fcd16))) {
                    do {
                        q2 = p2;
                        if (fcd16 <= 255 || p2 == 0) break;
                        c2 = Character.codePointBefore(this.seq, p2);
                        p2 -= Character.charCount(c2);
                    } while ((fcd16 = this.nfcImpl.getFCD16(c2)) != 0);
                    this.normalize(q2, this.pos);
                    this.pos = this.limit;
                    break block6;
                }
                nextCC = fcd16 >> 8;
            } while (p2 != 0 && nextCC != 0);
            this.start = this.segmentStart = p2;
        }
        assert (this.pos != this.start);
        this.checkDir = 0;
    }

    private void normalize(int from, int to) {
        if (this.normalized == null) {
            this.normalized = new StringBuilder();
        }
        this.nfcImpl.decompose(this.rawSeq, from, to, this.normalized, to - from);
        this.segmentStart = from;
        this.segmentLimit = to;
        this.seq = this.normalized;
        this.start = 0;
        this.limit = this.start + this.normalized.length();
    }
}

