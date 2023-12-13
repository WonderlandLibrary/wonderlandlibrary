/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationFCD;
import com.ibm.icu.impl.coll.IterCollationIterator;
import com.ibm.icu.text.UCharacterIterator;

public final class FCDIterCollationIterator
extends IterCollationIterator {
    private State state = State.ITER_CHECK_FWD;
    private int start;
    private int pos;
    private int limit;
    private final Normalizer2Impl nfcImpl;
    private StringBuilder s;
    private StringBuilder normalized;

    public FCDIterCollationIterator(CollationData data, boolean numeric, UCharacterIterator ui, int startIndex) {
        super(data, numeric, ui);
        this.start = startIndex;
        this.nfcImpl = data.nfcImpl;
    }

    @Override
    public void resetToOffset(int newOffset) {
        super.resetToOffset(newOffset);
        this.start = newOffset;
        this.state = State.ITER_CHECK_FWD;
    }

    @Override
    public int getOffset() {
        if (this.state.compareTo(State.ITER_CHECK_BWD) <= 0) {
            return this.iter.getIndex();
        }
        if (this.state == State.ITER_IN_FCD_SEGMENT) {
            return this.pos;
        }
        if (this.pos == 0) {
            return this.start;
        }
        return this.limit;
    }

    @Override
    public int nextCodePoint() {
        while (true) {
            int c2;
            if (this.state == State.ITER_CHECK_FWD) {
                c2 = this.iter.next();
                if (c2 < 0) {
                    return c2;
                }
                if (CollationFCD.hasTccc(c2) && (CollationFCD.maybeTibetanCompositeVowel(c2) || CollationFCD.hasLccc(this.iter.current()))) {
                    this.iter.previous();
                    if (this.nextSegment()) continue;
                    return -1;
                }
                if (FCDIterCollationIterator.isLeadSurrogate(c2)) {
                    int trail = this.iter.next();
                    if (FCDIterCollationIterator.isTrailSurrogate(trail)) {
                        return Character.toCodePoint((char)c2, (char)trail);
                    }
                    if (trail >= 0) {
                        this.iter.previous();
                    }
                }
                return c2;
            }
            if (this.state == State.ITER_IN_FCD_SEGMENT && this.pos != this.limit) {
                c2 = this.iter.nextCodePoint();
                this.pos += Character.charCount(c2);
                assert (c2 >= 0);
                return c2;
            }
            if (this.state.compareTo(State.IN_NORM_ITER_AT_LIMIT) >= 0 && this.pos != this.normalized.length()) {
                c2 = this.normalized.codePointAt(this.pos);
                this.pos += Character.charCount(c2);
                return c2;
            }
            this.switchToForward();
        }
    }

    @Override
    public int previousCodePoint() {
        while (true) {
            int c2;
            if (this.state == State.ITER_CHECK_BWD) {
                c2 = this.iter.previous();
                if (c2 < 0) {
                    this.pos = 0;
                    this.start = 0;
                    this.state = State.ITER_IN_FCD_SEGMENT;
                    return -1;
                }
                if (CollationFCD.hasLccc(c2)) {
                    int prev = -1;
                    if (CollationFCD.maybeTibetanCompositeVowel(c2) || CollationFCD.hasTccc(prev = this.iter.previous())) {
                        this.iter.next();
                        if (prev >= 0) {
                            this.iter.next();
                        }
                        if (this.previousSegment()) continue;
                        return -1;
                    }
                    if (FCDIterCollationIterator.isTrailSurrogate(c2)) {
                        if (prev < 0) {
                            prev = this.iter.previous();
                        }
                        if (FCDIterCollationIterator.isLeadSurrogate(prev)) {
                            return Character.toCodePoint((char)prev, (char)c2);
                        }
                    }
                    if (prev >= 0) {
                        this.iter.next();
                    }
                }
                return c2;
            }
            if (this.state == State.ITER_IN_FCD_SEGMENT && this.pos != this.start) {
                c2 = this.iter.previousCodePoint();
                this.pos -= Character.charCount(c2);
                assert (c2 >= 0);
                return c2;
            }
            if (this.state.compareTo(State.IN_NORM_ITER_AT_LIMIT) >= 0 && this.pos != 0) {
                c2 = this.normalized.codePointBefore(this.pos);
                this.pos -= Character.charCount(c2);
                return c2;
            }
            this.switchToBackward();
        }
    }

    @Override
    protected long handleNextCE32() {
        int c2;
        while (true) {
            if (this.state == State.ITER_CHECK_FWD) {
                c2 = this.iter.next();
                if (c2 < 0) {
                    return -4294967104L;
                }
                if (!CollationFCD.hasTccc(c2) || !CollationFCD.maybeTibetanCompositeVowel(c2) && !CollationFCD.hasLccc(this.iter.current())) break;
                this.iter.previous();
                if (this.nextSegment()) continue;
                c2 = -1;
                return 192L;
            }
            if (this.state == State.ITER_IN_FCD_SEGMENT && this.pos != this.limit) {
                c2 = this.iter.next();
                ++this.pos;
                assert (c2 >= 0);
                break;
            }
            if (this.state.compareTo(State.IN_NORM_ITER_AT_LIMIT) >= 0 && this.pos != this.normalized.length()) {
                c2 = this.normalized.charAt(this.pos++);
                break;
            }
            this.switchToForward();
        }
        return this.makeCodePointAndCE32Pair(c2, this.trie.getFromU16SingleLead((char)c2));
    }

    @Override
    protected char handleGetTrailSurrogate() {
        if (this.state.compareTo(State.ITER_IN_FCD_SEGMENT) <= 0) {
            int trail = this.iter.next();
            if (FCDIterCollationIterator.isTrailSurrogate(trail)) {
                if (this.state == State.ITER_IN_FCD_SEGMENT) {
                    ++this.pos;
                }
            } else if (trail >= 0) {
                this.iter.previous();
            }
            return (char)trail;
        }
        assert (this.pos < this.normalized.length());
        char trail = this.normalized.charAt(this.pos);
        if (Character.isLowSurrogate(trail)) {
            ++this.pos;
        }
        return trail;
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
        assert (this.state == State.ITER_CHECK_BWD || this.state == State.ITER_IN_FCD_SEGMENT && this.pos == this.limit || this.state.compareTo(State.IN_NORM_ITER_AT_LIMIT) >= 0 && this.pos == this.normalized.length());
        if (this.state == State.ITER_CHECK_BWD) {
            this.start = this.pos = this.iter.getIndex();
            this.state = this.pos == this.limit ? State.ITER_CHECK_FWD : State.ITER_IN_FCD_SEGMENT;
        } else {
            if (this.state != State.ITER_IN_FCD_SEGMENT) {
                if (this.state == State.IN_NORM_ITER_AT_START) {
                    this.iter.moveIndex(this.limit - this.start);
                }
                this.start = this.limit;
            }
            this.state = State.ITER_CHECK_FWD;
        }
    }

    private boolean nextSegment() {
        int c2;
        assert (this.state == State.ITER_CHECK_FWD);
        this.pos = this.iter.getIndex();
        if (this.s == null) {
            this.s = new StringBuilder();
        } else {
            this.s.setLength(0);
        }
        int prevCC = 0;
        while ((c2 = this.iter.nextCodePoint()) >= 0) {
            int fcd16 = this.nfcImpl.getFCD16(c2);
            int leadCC = fcd16 >> 8;
            if (leadCC == 0 && this.s.length() != 0) {
                this.iter.previousCodePoint();
                break;
            }
            this.s.appendCodePoint(c2);
            if (leadCC != 0 && (prevCC > leadCC || CollationFCD.isFCD16OfTibetanCompositeVowel(fcd16))) {
                while ((c2 = this.iter.nextCodePoint()) >= 0) {
                    if (this.nfcImpl.getFCD16(c2) <= 255) {
                        this.iter.previousCodePoint();
                        break;
                    }
                    this.s.appendCodePoint(c2);
                }
                this.normalize(this.s);
                this.start = this.pos;
                this.limit = this.pos + this.s.length();
                this.state = State.IN_NORM_ITER_AT_LIMIT;
                this.pos = 0;
                return true;
            }
            prevCC = fcd16 & 0xFF;
            if (prevCC != 0) continue;
            break;
        }
        this.limit = this.pos + this.s.length();
        assert (this.pos != this.limit);
        this.iter.moveIndex(-this.s.length());
        this.state = State.ITER_IN_FCD_SEGMENT;
        return true;
    }

    private void switchToBackward() {
        assert (this.state == State.ITER_CHECK_FWD || this.state == State.ITER_IN_FCD_SEGMENT && this.pos == this.start || this.state.compareTo(State.IN_NORM_ITER_AT_LIMIT) >= 0 && this.pos == 0);
        if (this.state == State.ITER_CHECK_FWD) {
            this.limit = this.pos = this.iter.getIndex();
            this.state = this.pos == this.start ? State.ITER_CHECK_BWD : State.ITER_IN_FCD_SEGMENT;
        } else {
            if (this.state != State.ITER_IN_FCD_SEGMENT) {
                if (this.state == State.IN_NORM_ITER_AT_LIMIT) {
                    this.iter.moveIndex(this.start - this.limit);
                }
                this.limit = this.start;
            }
            this.state = State.ITER_CHECK_BWD;
        }
    }

    private boolean previousSegment() {
        int c2;
        assert (this.state == State.ITER_CHECK_BWD);
        this.pos = this.iter.getIndex();
        if (this.s == null) {
            this.s = new StringBuilder();
        } else {
            this.s.setLength(0);
        }
        int nextCC = 0;
        while ((c2 = this.iter.previousCodePoint()) >= 0) {
            int fcd16 = this.nfcImpl.getFCD16(c2);
            int trailCC = fcd16 & 0xFF;
            if (trailCC == 0 && this.s.length() != 0) {
                this.iter.nextCodePoint();
                break;
            }
            this.s.appendCodePoint(c2);
            if (trailCC != 0 && (nextCC != 0 && trailCC > nextCC || CollationFCD.isFCD16OfTibetanCompositeVowel(fcd16))) {
                while (fcd16 > 255 && (c2 = this.iter.previousCodePoint()) >= 0) {
                    fcd16 = this.nfcImpl.getFCD16(c2);
                    if (fcd16 == 0) {
                        this.iter.nextCodePoint();
                        break;
                    }
                    this.s.appendCodePoint(c2);
                }
                this.s.reverse();
                this.normalize(this.s);
                this.limit = this.pos;
                this.start = this.pos - this.s.length();
                this.state = State.IN_NORM_ITER_AT_START;
                this.pos = this.normalized.length();
                return true;
            }
            nextCC = fcd16 >> 8;
            if (nextCC != 0) continue;
            break;
        }
        this.start = this.pos - this.s.length();
        assert (this.pos != this.start);
        this.iter.moveIndex(this.s.length());
        this.state = State.ITER_IN_FCD_SEGMENT;
        return true;
    }

    private void normalize(CharSequence s2) {
        if (this.normalized == null) {
            this.normalized = new StringBuilder();
        }
        this.nfcImpl.decompose(s2, this.normalized);
    }

    private static enum State {
        ITER_CHECK_FWD,
        ITER_CHECK_BWD,
        ITER_IN_FCD_SEGMENT,
        IN_NORM_ITER_AT_LIMIT,
        IN_NORM_ITER_AT_START;

    }
}

