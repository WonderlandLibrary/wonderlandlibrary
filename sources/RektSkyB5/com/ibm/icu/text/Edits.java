/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import java.nio.BufferOverflowException;
import java.util.Arrays;

public final class Edits {
    private static final int MAX_UNCHANGED_LENGTH = 4096;
    private static final int MAX_UNCHANGED = 4095;
    private static final int MAX_SHORT_CHANGE_OLD_LENGTH = 6;
    private static final int MAX_SHORT_CHANGE_NEW_LENGTH = 7;
    private static final int SHORT_CHANGE_NUM_MASK = 511;
    private static final int MAX_SHORT_CHANGE = 28671;
    private static final int LENGTH_IN_1TRAIL = 61;
    private static final int LENGTH_IN_2TRAIL = 62;
    private static final int STACK_CAPACITY = 100;
    private char[] array = new char[100];
    private int length;
    private int delta;
    private int numChanges;

    public void reset() {
        this.numChanges = 0;
        this.delta = 0;
        this.length = 0;
    }

    private void setLastUnit(int last) {
        this.array[this.length - 1] = (char)last;
    }

    private int lastUnit() {
        return this.length > 0 ? this.array[this.length - 1] : 65535;
    }

    public void addUnchanged(int unchangedLength) {
        if (unchangedLength < 0) {
            throw new IllegalArgumentException("addUnchanged(" + unchangedLength + "): length must not be negative");
        }
        int last = this.lastUnit();
        if (last < 4095) {
            int remaining = 4095 - last;
            if (remaining >= unchangedLength) {
                this.setLastUnit(last + unchangedLength);
                return;
            }
            this.setLastUnit(4095);
            unchangedLength -= remaining;
        }
        while (unchangedLength >= 4096) {
            this.append(4095);
            unchangedLength -= 4096;
        }
        if (unchangedLength > 0) {
            this.append(unchangedLength - 1);
        }
    }

    public void addReplace(int oldLength, int newLength) {
        if (oldLength < 0 || newLength < 0) {
            throw new IllegalArgumentException("addReplace(" + oldLength + ", " + newLength + "): both lengths must be non-negative");
        }
        if (oldLength == 0 && newLength == 0) {
            return;
        }
        ++this.numChanges;
        int newDelta = newLength - oldLength;
        if (newDelta != 0) {
            if (newDelta > 0 && this.delta >= 0 && newDelta > Integer.MAX_VALUE - this.delta || newDelta < 0 && this.delta < 0 && newDelta < Integer.MIN_VALUE - this.delta) {
                throw new IndexOutOfBoundsException();
            }
            this.delta += newDelta;
        }
        if (0 < oldLength && oldLength <= 6 && newLength <= 7) {
            int u2 = oldLength << 12 | newLength << 9;
            int last = this.lastUnit();
            if (4095 < last && last < 28671 && (last & 0xFFFFFE00) == u2 && (last & 0x1FF) < 511) {
                this.setLastUnit(last + 1);
                return;
            }
            this.append(u2);
            return;
        }
        int head = 28672;
        if (oldLength < 61 && newLength < 61) {
            head |= oldLength << 6;
            this.append(head |= newLength);
        } else if (this.array.length - this.length >= 5 || this.growArray()) {
            int limit = this.length + 1;
            if (oldLength < 61) {
                head |= oldLength << 6;
            } else if (oldLength <= Short.MAX_VALUE) {
                head |= 0xF40;
                this.array[limit++] = (char)(0x8000 | oldLength);
            } else {
                head |= 62 + (oldLength >> 30) << 6;
                this.array[limit++] = (char)(0x8000 | oldLength >> 15);
                this.array[limit++] = (char)(0x8000 | oldLength);
            }
            if (newLength < 61) {
                head |= newLength;
            } else if (newLength <= Short.MAX_VALUE) {
                head |= 0x3D;
                this.array[limit++] = (char)(0x8000 | newLength);
            } else {
                head |= 62 + (newLength >> 30);
                this.array[limit++] = (char)(0x8000 | newLength >> 15);
                this.array[limit++] = (char)(0x8000 | newLength);
            }
            this.array[this.length] = (char)head;
            this.length = limit;
        }
    }

    private void append(int r2) {
        if (this.length < this.array.length || this.growArray()) {
            this.array[this.length++] = (char)r2;
        }
    }

    private boolean growArray() {
        int newCapacity;
        if (this.array.length == 100) {
            newCapacity = 2000;
        } else {
            if (this.array.length == Integer.MAX_VALUE) {
                throw new BufferOverflowException();
            }
            newCapacity = this.array.length >= 0x3FFFFFFF ? Integer.MAX_VALUE : 2 * this.array.length;
        }
        if (newCapacity - this.array.length < 5) {
            throw new BufferOverflowException();
        }
        this.array = Arrays.copyOf(this.array, newCapacity);
        return true;
    }

    public int lengthDelta() {
        return this.delta;
    }

    public boolean hasChanges() {
        return this.numChanges != 0;
    }

    public int numberOfChanges() {
        return this.numChanges;
    }

    public Iterator getCoarseChangesIterator() {
        return new Iterator(this.array, this.length, true, true);
    }

    public Iterator getCoarseIterator() {
        return new Iterator(this.array, this.length, false, true);
    }

    public Iterator getFineChangesIterator() {
        return new Iterator(this.array, this.length, true, false);
    }

    public Iterator getFineIterator() {
        return new Iterator(this.array, this.length, false, false);
    }

    public Edits mergeAndAppend(Edits ab, Edits bc) {
        Iterator abIter = ab.getFineIterator();
        Iterator bcIter = bc.getFineIterator();
        boolean abHasNext = true;
        boolean bcHasNext = true;
        int aLength = 0;
        int ab_bLength = 0;
        int bc_bLength = 0;
        int cLength = 0;
        int pending_aLength = 0;
        int pending_cLength = 0;
        while (true) {
            if (bc_bLength == 0 && bcHasNext && (bcHasNext = bcIter.next())) {
                bc_bLength = bcIter.oldLength();
                cLength = bcIter.newLength();
                if (bc_bLength == 0) {
                    if (ab_bLength == 0 || !abIter.hasChange()) {
                        this.addReplace(pending_aLength, pending_cLength + cLength);
                        pending_cLength = 0;
                        pending_aLength = 0;
                        continue;
                    }
                    pending_cLength += cLength;
                    continue;
                }
            }
            if (ab_bLength == 0) {
                if (abHasNext && (abHasNext = abIter.next())) {
                    aLength = abIter.oldLength();
                    ab_bLength = abIter.newLength();
                    if (ab_bLength == 0) {
                        if (bc_bLength == bcIter.oldLength() || !bcIter.hasChange()) {
                            this.addReplace(pending_aLength + aLength, pending_cLength);
                            pending_cLength = 0;
                            pending_aLength = 0;
                            continue;
                        }
                        pending_aLength += aLength;
                        continue;
                    }
                } else {
                    if (bc_bLength == 0) break;
                    throw new IllegalArgumentException("The ab output string is shorter than the bc input string.");
                }
            }
            if (bc_bLength == 0) {
                throw new IllegalArgumentException("The bc input string is shorter than the ab output string.");
            }
            if (!abIter.hasChange() && !bcIter.hasChange()) {
                if (pending_aLength != 0 || pending_cLength != 0) {
                    this.addReplace(pending_aLength, pending_cLength);
                    pending_cLength = 0;
                    pending_aLength = 0;
                }
                int unchangedLength = aLength <= cLength ? aLength : cLength;
                this.addUnchanged(unchangedLength);
                ab_bLength = aLength -= unchangedLength;
                bc_bLength = cLength -= unchangedLength;
                continue;
            }
            if (!abIter.hasChange() && bcIter.hasChange()) {
                if (ab_bLength >= bc_bLength) {
                    this.addReplace(pending_aLength + bc_bLength, pending_cLength + cLength);
                    pending_cLength = 0;
                    pending_aLength = 0;
                    aLength = ab_bLength -= bc_bLength;
                    bc_bLength = 0;
                    continue;
                }
            } else if (abIter.hasChange() && !bcIter.hasChange()) {
                if (ab_bLength <= bc_bLength) {
                    this.addReplace(pending_aLength + aLength, pending_cLength + ab_bLength);
                    pending_cLength = 0;
                    pending_aLength = 0;
                    cLength = bc_bLength -= ab_bLength;
                    ab_bLength = 0;
                    continue;
                }
            } else if (ab_bLength == bc_bLength) {
                this.addReplace(pending_aLength + aLength, pending_cLength + cLength);
                pending_cLength = 0;
                pending_aLength = 0;
                bc_bLength = 0;
                ab_bLength = 0;
                continue;
            }
            pending_aLength += aLength;
            pending_cLength += cLength;
            if (ab_bLength < bc_bLength) {
                bc_bLength -= ab_bLength;
                ab_bLength = 0;
                cLength = 0;
                continue;
            }
            ab_bLength -= bc_bLength;
            bc_bLength = 0;
            aLength = 0;
        }
        if (pending_aLength != 0 || pending_cLength != 0) {
            this.addReplace(pending_aLength, pending_cLength);
        }
        return this;
    }

    public static final class Iterator {
        private final char[] array;
        private int index;
        private final int length;
        private int remaining;
        private final boolean onlyChanges_;
        private final boolean coarse;
        private int dir;
        private boolean changed;
        private int oldLength_;
        private int newLength_;
        private int srcIndex;
        private int replIndex;
        private int destIndex;

        private Iterator(char[] a2, int len, boolean oc, boolean crs) {
            this.array = a2;
            this.length = len;
            this.onlyChanges_ = oc;
            this.coarse = crs;
        }

        private int readLength(int head) {
            if (head < 61) {
                return head;
            }
            if (head < 62) {
                assert (this.index < this.length);
                assert (this.array[this.index] >= '\u8000');
                return this.array[this.index++] & Short.MAX_VALUE;
            }
            assert (this.index + 2 <= this.length);
            assert (this.array[this.index] >= '\u8000');
            assert (this.array[this.index + 1] >= '\u8000');
            int len = (head & 1) << 30 | (this.array[this.index] & Short.MAX_VALUE) << 15 | this.array[this.index + 1] & Short.MAX_VALUE;
            this.index += 2;
            return len;
        }

        private void updateNextIndexes() {
            this.srcIndex += this.oldLength_;
            if (this.changed) {
                this.replIndex += this.newLength_;
            }
            this.destIndex += this.newLength_;
        }

        private void updatePreviousIndexes() {
            this.srcIndex -= this.oldLength_;
            if (this.changed) {
                this.replIndex -= this.newLength_;
            }
            this.destIndex -= this.newLength_;
        }

        private boolean noNext() {
            this.dir = 0;
            this.changed = false;
            this.newLength_ = 0;
            this.oldLength_ = 0;
            return false;
        }

        public boolean next() {
            return this.next(this.onlyChanges_);
        }

        /*
         * Enabled aggressive block sorting
         */
        private boolean next(boolean onlyChanges) {
            char u2;
            block19: {
                if (this.dir > 0) {
                    this.updateNextIndexes();
                } else {
                    if (this.dir < 0 && this.remaining > 0) {
                        ++this.index;
                        this.dir = 1;
                        return true;
                    }
                    this.dir = 1;
                }
                if (this.remaining >= 1) {
                    if (this.remaining > 1) {
                        --this.remaining;
                        return true;
                    }
                    this.remaining = 0;
                }
                if (this.index >= this.length) {
                    return this.noNext();
                }
                if ((u2 = this.array[this.index++]) <= '\u0fff') {
                    this.changed = false;
                    this.oldLength_ = u2 + '\u0001';
                    while (this.index < this.length && (u2 = this.array[this.index]) <= '\u0fff') {
                        ++this.index;
                        this.oldLength_ += u2 + '\u0001';
                    }
                    this.newLength_ = this.oldLength_;
                    if (!onlyChanges) {
                        return true;
                    }
                    this.updateNextIndexes();
                    if (this.index >= this.length) {
                        return this.noNext();
                    }
                    ++this.index;
                }
                this.changed = true;
                if (u2 <= '\u6fff') {
                    int oldLen = u2 >> 12;
                    int newLen = u2 >> 9 & 7;
                    int num = (u2 & 0x1FF) + 1;
                    if (this.coarse) {
                        this.oldLength_ = num * oldLen;
                        this.newLength_ = num * newLen;
                        break block19;
                    } else {
                        this.oldLength_ = oldLen;
                        this.newLength_ = newLen;
                        if (num > 1) {
                            this.remaining = num;
                        }
                        return true;
                    }
                }
                assert (u2 <= Short.MAX_VALUE);
                this.oldLength_ = this.readLength(u2 >> 6 & 0x3F);
                this.newLength_ = this.readLength(u2 & 0x3F);
                if (!this.coarse) {
                    return true;
                }
            }
            while (this.index < this.length && (u2 = this.array[this.index]) > '\u0fff') {
                ++this.index;
                if (u2 <= '\u6fff') {
                    int num = (u2 & 0x1FF) + 1;
                    this.oldLength_ += (u2 >> 12) * num;
                    this.newLength_ += (u2 >> 9 & 7) * num;
                    continue;
                }
                assert (u2 <= Short.MAX_VALUE);
                this.oldLength_ += this.readLength(u2 >> 6 & 0x3F);
                this.newLength_ += this.readLength(u2 & 0x3F);
            }
            return true;
        }

        /*
         * Enabled aggressive block sorting
         */
        private boolean previous() {
            int headIndex;
            char u2;
            block21: {
                if (this.dir >= 0) {
                    if (this.dir > 0) {
                        if (this.remaining > 0) {
                            --this.index;
                            this.dir = -1;
                            return true;
                        }
                        this.updateNextIndexes();
                    }
                    this.dir = -1;
                }
                if (this.remaining > 0) {
                    u2 = this.array[this.index];
                    assert ('\u0fff' < u2 && u2 <= '\u6fff');
                    if (this.remaining <= (u2 & 0x1FF)) {
                        ++this.remaining;
                        this.updatePreviousIndexes();
                        return true;
                    }
                    this.remaining = 0;
                }
                if (this.index <= 0) {
                    return this.noNext();
                }
                if ((u2 = this.array[--this.index]) <= '\u0fff') {
                    this.changed = false;
                    this.oldLength_ = u2 + '\u0001';
                    while (this.index > 0 && (u2 = this.array[this.index - 1]) <= '\u0fff') {
                        --this.index;
                        this.oldLength_ += u2 + '\u0001';
                    }
                    this.newLength_ = this.oldLength_;
                    this.updatePreviousIndexes();
                    return true;
                }
                this.changed = true;
                if (u2 <= '\u6fff') {
                    int oldLen = u2 >> 12;
                    int newLen = u2 >> 9 & 7;
                    int num = (u2 & 0x1FF) + 1;
                    if (this.coarse) {
                        this.oldLength_ = num * oldLen;
                        this.newLength_ = num * newLen;
                        break block21;
                    } else {
                        this.oldLength_ = oldLen;
                        this.newLength_ = newLen;
                        if (num > 1) {
                            this.remaining = 1;
                        }
                        this.updatePreviousIndexes();
                        return true;
                    }
                }
                if (u2 <= Short.MAX_VALUE) {
                    this.oldLength_ = this.readLength(u2 >> 6 & 0x3F);
                    this.newLength_ = this.readLength(u2 & 0x3F);
                } else {
                    assert (this.index > 0);
                    while ((u2 = this.array[--this.index]) > Short.MAX_VALUE) {
                    }
                    assert (u2 > '\u6fff');
                    headIndex = this.index++;
                    this.oldLength_ = this.readLength(u2 >> 6 & 0x3F);
                    this.newLength_ = this.readLength(u2 & 0x3F);
                    this.index = headIndex;
                }
                if (!this.coarse) {
                    this.updatePreviousIndexes();
                    return true;
                }
            }
            while (this.index > 0 && (u2 = this.array[this.index - 1]) > '\u0fff') {
                --this.index;
                if (u2 <= '\u6fff') {
                    int num = (u2 & 0x1FF) + 1;
                    this.oldLength_ += (u2 >> 12) * num;
                    this.newLength_ += (u2 >> 9 & 7) * num;
                    continue;
                }
                if (u2 > Short.MAX_VALUE) continue;
                ++this.index;
                this.oldLength_ += this.readLength(u2 >> 6 & 0x3F);
                this.newLength_ += this.readLength(u2 & 0x3F);
                this.index = headIndex;
            }
            this.updatePreviousIndexes();
            return true;
        }

        public boolean findSourceIndex(int i2) {
            return this.findIndex(i2, true) == 0;
        }

        public boolean findDestinationIndex(int i2) {
            return this.findIndex(i2, false) == 0;
        }

        private int findIndex(int i2, boolean findSource) {
            int spanLength;
            int spanStart;
            if (i2 < 0) {
                return -1;
            }
            if (findSource) {
                spanStart = this.srcIndex;
                spanLength = this.oldLength_;
            } else {
                spanStart = this.destIndex;
                spanLength = this.newLength_;
            }
            if (i2 < spanStart) {
                if (i2 >= spanStart / 2) {
                    while (true) {
                        boolean hasPrevious = this.previous();
                        assert (hasPrevious);
                        int n2 = spanStart = findSource ? this.srcIndex : this.destIndex;
                        if (i2 >= spanStart) {
                            return 0;
                        }
                        if (this.remaining <= 0) continue;
                        spanLength = findSource ? this.oldLength_ : this.newLength_;
                        char u2 = this.array[this.index];
                        assert ('\u0fff' < u2 && u2 <= '\u6fff');
                        int num = (u2 & 0x1FF) + 1 - this.remaining;
                        int len = num * spanLength;
                        if (i2 >= spanStart - len) {
                            int n3 = (spanStart - i2 - 1) / spanLength + 1;
                            this.srcIndex -= n3 * this.oldLength_;
                            this.replIndex -= n3 * this.newLength_;
                            this.destIndex -= n3 * this.newLength_;
                            this.remaining += n3;
                            return 0;
                        }
                        this.srcIndex -= num * this.oldLength_;
                        this.replIndex -= num * this.newLength_;
                        this.destIndex -= num * this.newLength_;
                        this.remaining = 0;
                    }
                }
                this.dir = 0;
                this.destIndex = 0;
                this.replIndex = 0;
                this.srcIndex = 0;
                this.newLength_ = 0;
                this.oldLength_ = 0;
                this.remaining = 0;
                this.index = 0;
            } else if (i2 < spanStart + spanLength) {
                return 0;
            }
            while (this.next(false)) {
                if (findSource) {
                    spanStart = this.srcIndex;
                    spanLength = this.oldLength_;
                } else {
                    spanStart = this.destIndex;
                    spanLength = this.newLength_;
                }
                if (i2 < spanStart + spanLength) {
                    return 0;
                }
                if (this.remaining <= 1) continue;
                int len = this.remaining * spanLength;
                if (i2 < spanStart + len) {
                    int n4 = (i2 - spanStart) / spanLength;
                    this.srcIndex += n4 * this.oldLength_;
                    this.replIndex += n4 * this.newLength_;
                    this.destIndex += n4 * this.newLength_;
                    this.remaining -= n4;
                    return 0;
                }
                this.oldLength_ *= this.remaining;
                this.newLength_ *= this.remaining;
                this.remaining = 0;
            }
            return 1;
        }

        public int destinationIndexFromSourceIndex(int i2) {
            int where = this.findIndex(i2, true);
            if (where < 0) {
                return 0;
            }
            if (where > 0 || i2 == this.srcIndex) {
                return this.destIndex;
            }
            if (this.changed) {
                return this.destIndex + this.newLength_;
            }
            return this.destIndex + (i2 - this.srcIndex);
        }

        public int sourceIndexFromDestinationIndex(int i2) {
            int where = this.findIndex(i2, false);
            if (where < 0) {
                return 0;
            }
            if (where > 0 || i2 == this.destIndex) {
                return this.srcIndex;
            }
            if (this.changed) {
                return this.srcIndex + this.oldLength_;
            }
            return this.srcIndex + (i2 - this.destIndex);
        }

        public boolean hasChange() {
            return this.changed;
        }

        public int oldLength() {
            return this.oldLength_;
        }

        public int newLength() {
            return this.newLength_;
        }

        public int sourceIndex() {
            return this.srcIndex;
        }

        public int replacementIndex() {
            return this.replIndex;
        }

        public int destinationIndex() {
            return this.destIndex;
        }

        @Deprecated
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append("{ src[");
            sb.append(this.srcIndex);
            sb.append("..");
            sb.append(this.srcIndex + this.oldLength_);
            if (this.changed) {
                sb.append("] \u21dd dest[");
            } else {
                sb.append("] \u2261 dest[");
            }
            sb.append(this.destIndex);
            sb.append("..");
            sb.append(this.destIndex + this.newLength_);
            if (this.changed) {
                sb.append("], repl[");
                sb.append(this.replIndex);
                sb.append("..");
                sb.append(this.replIndex + this.newLength_);
                sb.append("] }");
            } else {
                sb.append("] (no-change) }");
            }
            return sb.toString();
        }
    }
}

