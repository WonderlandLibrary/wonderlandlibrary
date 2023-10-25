/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class BytesTrie
implements Cloneable,
Iterable<Entry> {
    private static Result[] valueResults_ = new Result[]{Result.INTERMEDIATE_VALUE, Result.FINAL_VALUE};
    static final int kMaxBranchLinearSubNodeLength = 5;
    static final int kMinLinearMatch = 16;
    static final int kMaxLinearMatchLength = 16;
    static final int kMinValueLead = 32;
    private static final int kValueIsFinal = 1;
    static final int kMinOneByteValueLead = 16;
    static final int kMaxOneByteValue = 64;
    static final int kMinTwoByteValueLead = 81;
    static final int kMaxTwoByteValue = 6911;
    static final int kMinThreeByteValueLead = 108;
    static final int kFourByteValueLead = 126;
    static final int kMaxThreeByteValue = 0x11FFFF;
    static final int kFiveByteValueLead = 127;
    static final int kMaxOneByteDelta = 191;
    static final int kMinTwoByteDeltaLead = 192;
    static final int kMinThreeByteDeltaLead = 240;
    static final int kFourByteDeltaLead = 254;
    static final int kFiveByteDeltaLead = 255;
    static final int kMaxTwoByteDelta = 12287;
    static final int kMaxThreeByteDelta = 917503;
    private byte[] bytes_;
    private int root_;
    private int pos_;
    private int remainingMatchLength_;

    public BytesTrie(byte[] trieBytes, int offset) {
        this.bytes_ = trieBytes;
        this.pos_ = this.root_ = offset;
        this.remainingMatchLength_ = -1;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public BytesTrie reset() {
        this.pos_ = this.root_;
        this.remainingMatchLength_ = -1;
        return this;
    }

    public BytesTrie saveState(State state) {
        State.access$002(state, this.bytes_);
        state.root = this.root_;
        state.pos = this.pos_;
        state.remainingMatchLength = this.remainingMatchLength_;
        return this;
    }

    public BytesTrie resetToState(State state) {
        if (this.bytes_ != state.bytes || this.bytes_ == null || this.root_ != state.root) {
            throw new IllegalArgumentException("incompatible trie state");
        }
        this.pos_ = state.pos;
        this.remainingMatchLength_ = state.remainingMatchLength;
        return this;
    }

    public Result current() {
        int node;
        int pos = this.pos_;
        if (pos < 0) {
            return Result.NO_MATCH;
        }
        return this.remainingMatchLength_ < 0 && (node = this.bytes_[pos] & 0xFF) >= 32 ? valueResults_[node & 1] : Result.NO_VALUE;
    }

    public Result first(int inByte) {
        this.remainingMatchLength_ = -1;
        if (inByte < 0) {
            inByte += 256;
        }
        return this.nextImpl(this.root_, inByte);
    }

    public Result next(int inByte) {
        int length;
        int pos = this.pos_;
        if (pos < 0) {
            return Result.NO_MATCH;
        }
        if (inByte < 0) {
            inByte += 256;
        }
        if ((length = this.remainingMatchLength_) >= 0) {
            if (inByte == (this.bytes_[pos++] & 0xFF)) {
                int node;
                this.remainingMatchLength_ = --length;
                this.pos_ = pos;
                return length < 0 && (node = this.bytes_[pos] & 0xFF) >= 32 ? valueResults_[node & 1] : Result.NO_VALUE;
            }
            this.stop();
            return Result.NO_MATCH;
        }
        return this.nextImpl(pos, inByte);
    }

    public Result next(byte[] s, int sIndex, int sLimit) {
        if (sIndex >= sLimit) {
            return this.current();
        }
        int pos = this.pos_;
        if (pos < 0) {
            return Result.NO_MATCH;
        }
        int length = this.remainingMatchLength_;
        block0: while (true) {
            int node;
            if (sIndex == sLimit) {
                this.remainingMatchLength_ = length;
                this.pos_ = pos;
                return length < 0 && (node = this.bytes_[pos] & 0xFF) >= 32 ? valueResults_[node & 1] : Result.NO_VALUE;
            }
            byte inByte = s[sIndex++];
            if (length >= 0) {
                if (inByte != this.bytes_[pos]) {
                    this.stop();
                    return Result.NO_MATCH;
                }
                ++pos;
                --length;
                continue;
            }
            this.remainingMatchLength_ = length;
            while (true) {
                if ((node = this.bytes_[pos++] & 0xFF) < 16) {
                    Result result = this.branchNext(pos, node, inByte & 0xFF);
                    if (result == Result.NO_MATCH) {
                        return Result.NO_MATCH;
                    }
                    if (sIndex == sLimit) {
                        return result;
                    }
                    if (result == Result.FINAL_VALUE) {
                        this.stop();
                        return Result.NO_MATCH;
                    }
                    inByte = s[sIndex++];
                    pos = this.pos_;
                    continue;
                }
                if (node < 32) {
                    length = node - 16;
                    if (inByte != this.bytes_[pos]) {
                        this.stop();
                        return Result.NO_MATCH;
                    }
                    ++pos;
                    --length;
                    continue block0;
                }
                if ((node & 1) != 0) {
                    this.stop();
                    return Result.NO_MATCH;
                }
                pos = BytesTrie.skipValue(pos, node);
                if (!$assertionsDisabled && (this.bytes_[pos] & 0xFF) >= 32) break block0;
            }
            break;
        }
        throw new AssertionError();
    }

    public int getValue() {
        int pos = this.pos_;
        int leadByte = this.bytes_[pos++] & 0xFF;
        assert (leadByte >= 32);
        return BytesTrie.readValue(this.bytes_, pos, leadByte >> 1);
    }

    public long getUniqueValue() {
        int pos = this.pos_;
        if (pos < 0) {
            return 0L;
        }
        long uniqueValue = BytesTrie.findUniqueValue(this.bytes_, pos + this.remainingMatchLength_ + 1, 0L);
        return uniqueValue << 31 >> 31;
    }

    public int getNextBytes(Appendable out) {
        int node;
        int pos = this.pos_;
        if (pos < 0) {
            return 0;
        }
        if (this.remainingMatchLength_ >= 0) {
            BytesTrie.append(out, this.bytes_[pos] & 0xFF);
            return 1;
        }
        if ((node = this.bytes_[pos++] & 0xFF) >= 32) {
            if ((node & 1) != 0) {
                return 0;
            }
            pos = BytesTrie.skipValue(pos, node);
            node = this.bytes_[pos++] & 0xFF;
            assert (node < 32);
        }
        if (node < 16) {
            if (node == 0) {
                node = this.bytes_[pos++] & 0xFF;
            }
            BytesTrie.getNextBranchBytes(this.bytes_, pos, ++node, out);
            return node;
        }
        BytesTrie.append(out, this.bytes_[pos] & 0xFF);
        return 1;
    }

    public Iterator iterator() {
        return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, 0);
    }

    public Iterator iterator(int maxStringLength) {
        return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, maxStringLength);
    }

    public static Iterator iterator(byte[] trieBytes, int offset, int maxStringLength) {
        return new Iterator(trieBytes, offset, -1, maxStringLength);
    }

    private void stop() {
        this.pos_ = -1;
    }

    private static int readValue(byte[] bytes, int pos, int leadByte) {
        int value = leadByte < 81 ? leadByte - 16 : (leadByte < 108 ? leadByte - 81 << 8 | bytes[pos] & 0xFF : (leadByte < 126 ? leadByte - 108 << 16 | (bytes[pos] & 0xFF) << 8 | bytes[pos + 1] & 0xFF : (leadByte == 126 ? (bytes[pos] & 0xFF) << 16 | (bytes[pos + 1] & 0xFF) << 8 | bytes[pos + 2] & 0xFF : bytes[pos] << 24 | (bytes[pos + 1] & 0xFF) << 16 | (bytes[pos + 2] & 0xFF) << 8 | bytes[pos + 3] & 0xFF)));
        return value;
    }

    private static int skipValue(int pos, int leadByte) {
        assert (leadByte >= 32);
        if (leadByte >= 162) {
            pos = leadByte < 216 ? ++pos : (leadByte < 252 ? (pos += 2) : (pos += 3 + (leadByte >> 1 & 1)));
        }
        return pos;
    }

    private static int skipValue(byte[] bytes, int pos) {
        int leadByte = bytes[pos++] & 0xFF;
        return BytesTrie.skipValue(pos, leadByte);
    }

    private static int jumpByDelta(byte[] bytes, int pos) {
        int delta;
        if ((delta = bytes[pos++] & 0xFF) >= 192) {
            if (delta < 240) {
                delta = delta - 192 << 8 | bytes[pos++] & 0xFF;
            } else if (delta < 254) {
                delta = delta - 240 << 16 | (bytes[pos] & 0xFF) << 8 | bytes[pos + 1] & 0xFF;
                pos += 2;
            } else if (delta == 254) {
                delta = (bytes[pos] & 0xFF) << 16 | (bytes[pos + 1] & 0xFF) << 8 | bytes[pos + 2] & 0xFF;
                pos += 3;
            } else {
                delta = bytes[pos] << 24 | (bytes[pos + 1] & 0xFF) << 16 | (bytes[pos + 2] & 0xFF) << 8 | bytes[pos + 3] & 0xFF;
                pos += 4;
            }
        }
        return pos + delta;
    }

    private static int skipDelta(byte[] bytes, int pos) {
        int delta;
        if ((delta = bytes[pos++] & 0xFF) >= 192) {
            pos = delta < 240 ? ++pos : (delta < 254 ? (pos += 2) : (pos += 3 + (delta & 1)));
        }
        return pos;
    }

    private Result branchNext(int pos, int length, int inByte) {
        if (length == 0) {
            length = this.bytes_[pos++] & 0xFF;
        }
        ++length;
        while (length > 5) {
            if (inByte < (this.bytes_[pos++] & 0xFF)) {
                length >>= 1;
                pos = BytesTrie.jumpByDelta(this.bytes_, pos);
                continue;
            }
            length -= length >> 1;
            pos = BytesTrie.skipDelta(this.bytes_, pos);
        }
        do {
            if (inByte == (this.bytes_[pos++] & 0xFF)) {
                Result result;
                int node = this.bytes_[pos] & 0xFF;
                assert (node >= 32);
                if ((node & 1) != 0) {
                    result = Result.FINAL_VALUE;
                } else {
                    int delta;
                    ++pos;
                    if ((node >>= 1) < 81) {
                        delta = node - 16;
                    } else if (node < 108) {
                        delta = node - 81 << 8 | this.bytes_[pos++] & 0xFF;
                    } else if (node < 126) {
                        delta = node - 108 << 16 | (this.bytes_[pos] & 0xFF) << 8 | this.bytes_[pos + 1] & 0xFF;
                        pos += 2;
                    } else if (node == 126) {
                        delta = (this.bytes_[pos] & 0xFF) << 16 | (this.bytes_[pos + 1] & 0xFF) << 8 | this.bytes_[pos + 2] & 0xFF;
                        pos += 3;
                    } else {
                        delta = this.bytes_[pos] << 24 | (this.bytes_[pos + 1] & 0xFF) << 16 | (this.bytes_[pos + 2] & 0xFF) << 8 | this.bytes_[pos + 3] & 0xFF;
                        pos += 4;
                    }
                    node = this.bytes_[pos += delta] & 0xFF;
                    result = node >= 32 ? valueResults_[node & 1] : Result.NO_VALUE;
                }
                this.pos_ = pos;
                return result;
            }
            pos = BytesTrie.skipValue(this.bytes_, pos);
        } while (--length > 1);
        if (inByte == (this.bytes_[pos++] & 0xFF)) {
            this.pos_ = pos;
            int node = this.bytes_[pos] & 0xFF;
            return node >= 32 ? valueResults_[node & 1] : Result.NO_VALUE;
        }
        this.stop();
        return Result.NO_MATCH;
    }

    private Result nextImpl(int pos, int inByte) {
        block4: {
            while (true) {
                int node;
                if ((node = this.bytes_[pos++] & 0xFF) < 16) {
                    return this.branchNext(pos, node, inByte);
                }
                if (node < 32) {
                    int length = node - 16;
                    if (inByte == (this.bytes_[pos++] & 0xFF)) {
                        this.remainingMatchLength_ = --length;
                        this.pos_ = pos;
                        return length < 0 && (node = this.bytes_[pos] & 0xFF) >= 32 ? valueResults_[node & 1] : Result.NO_VALUE;
                    }
                    break block4;
                }
                if ((node & 1) != 0) break block4;
                pos = BytesTrie.skipValue(pos, node);
                assert ((this.bytes_[pos] & 0xFF) < 32);
            }
        }
        this.stop();
        return Result.NO_MATCH;
    }

    private static long findUniqueValueFromBranch(byte[] bytes, int pos, int length, long uniqueValue) {
        while (length > 5) {
            if ((uniqueValue = BytesTrie.findUniqueValueFromBranch(bytes, BytesTrie.jumpByDelta(bytes, ++pos), length >> 1, uniqueValue)) == 0L) {
                return 0L;
            }
            length -= length >> 1;
            pos = BytesTrie.skipDelta(bytes, pos);
        }
        do {
            int n = ++pos;
            int node = bytes[n] & 0xFF;
            boolean isFinal = (node & 1) != 0;
            int value = BytesTrie.readValue(bytes, ++pos, node >> 1);
            pos = BytesTrie.skipValue(pos, node);
            if (isFinal) {
                if (uniqueValue != 0L) {
                    if (value == (int)(uniqueValue >> 1)) continue;
                    return 0L;
                }
                uniqueValue = (long)value << 1 | 1L;
                continue;
            }
            if ((uniqueValue = BytesTrie.findUniqueValue(bytes, pos + value, uniqueValue)) != 0L) continue;
            return 0L;
        } while (--length > 1);
        return (long)(pos + 1) << 33 | uniqueValue & 0x1FFFFFFFFL;
    }

    private static long findUniqueValue(byte[] bytes, int pos, long uniqueValue) {
        while (true) {
            int node;
            if ((node = bytes[pos++] & 0xFF) < 16) {
                if (node == 0) {
                    node = bytes[pos++] & 0xFF;
                }
                if ((uniqueValue = BytesTrie.findUniqueValueFromBranch(bytes, pos, node + 1, uniqueValue)) == 0L) {
                    return 0L;
                }
                pos = (int)(uniqueValue >>> 33);
                continue;
            }
            if (node < 32) {
                pos += node - 16 + 1;
                continue;
            }
            boolean isFinal = (node & 1) != 0;
            int value = BytesTrie.readValue(bytes, pos, node >> 1);
            if (uniqueValue != 0L) {
                if (value != (int)(uniqueValue >> 1)) {
                    return 0L;
                }
            } else {
                uniqueValue = (long)value << 1 | 1L;
            }
            if (isFinal) {
                return uniqueValue;
            }
            pos = BytesTrie.skipValue(pos, node);
        }
    }

    private static void getNextBranchBytes(byte[] bytes, int pos, int length, Appendable out) {
        while (length > 5) {
            BytesTrie.getNextBranchBytes(bytes, BytesTrie.jumpByDelta(bytes, ++pos), length >> 1, out);
            length -= length >> 1;
            pos = BytesTrie.skipDelta(bytes, pos);
        }
        do {
            BytesTrie.append(out, bytes[pos++] & 0xFF);
            pos = BytesTrie.skipValue(bytes, pos);
        } while (--length > 1);
        BytesTrie.append(out, bytes[pos] & 0xFF);
    }

    private static void append(Appendable out, int c) {
        try {
            out.append((char)c);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static final class Iterator
    implements java.util.Iterator<Entry> {
        private byte[] bytes_;
        private int pos_;
        private int initialPos_;
        private int remainingMatchLength_;
        private int initialRemainingMatchLength_;
        private int maxLength_;
        private Entry entry_;
        private ArrayList<Long> stack_ = new ArrayList();

        private Iterator(byte[] trieBytes, int offset, int remainingMatchLength, int maxStringLength) {
            this.bytes_ = trieBytes;
            this.pos_ = this.initialPos_ = offset;
            this.remainingMatchLength_ = this.initialRemainingMatchLength_ = remainingMatchLength;
            this.maxLength_ = maxStringLength;
            this.entry_ = new Entry(this.maxLength_ != 0 ? this.maxLength_ : 32);
            int length = this.remainingMatchLength_;
            if (length >= 0) {
                if (this.maxLength_ > 0 && ++length > this.maxLength_) {
                    length = this.maxLength_;
                }
                this.entry_.append(this.bytes_, this.pos_, length);
                this.pos_ += length;
                this.remainingMatchLength_ -= length;
            }
        }

        public Iterator reset() {
            this.pos_ = this.initialPos_;
            this.remainingMatchLength_ = this.initialRemainingMatchLength_;
            int length = this.remainingMatchLength_ + 1;
            if (this.maxLength_ > 0 && length > this.maxLength_) {
                length = this.maxLength_;
            }
            this.entry_.truncateString(length);
            this.pos_ += length;
            this.remainingMatchLength_ -= length;
            this.stack_.clear();
            return this;
        }

        @Override
        public boolean hasNext() {
            return this.pos_ >= 0 || !this.stack_.isEmpty();
        }

        @Override
        public Entry next() {
            int pos = this.pos_;
            if (pos < 0) {
                if (this.stack_.isEmpty()) {
                    throw new NoSuchElementException();
                }
                long top = this.stack_.remove(this.stack_.size() - 1);
                int length = (int)top;
                pos = (int)(top >> 32);
                this.entry_.truncateString(length & 0xFFFF);
                if ((length >>>= 16) > 1) {
                    if ((pos = this.branchNext(pos, length)) < 0) {
                        return this.entry_;
                    }
                } else {
                    this.entry_.append(this.bytes_[pos++]);
                }
            }
            if (this.remainingMatchLength_ >= 0) {
                return this.truncateAndStop();
            }
            while (true) {
                int node;
                if ((node = this.bytes_[pos++] & 0xFF) >= 32) {
                    boolean isFinal = (node & 1) != 0;
                    this.entry_.value = BytesTrie.readValue(this.bytes_, pos, node >> 1);
                    this.pos_ = isFinal || this.maxLength_ > 0 && this.entry_.length == this.maxLength_ ? -1 : BytesTrie.skipValue(pos, node);
                    return this.entry_;
                }
                if (this.maxLength_ > 0 && this.entry_.length == this.maxLength_) {
                    return this.truncateAndStop();
                }
                if (node < 16) {
                    if (node == 0) {
                        node = this.bytes_[pos++] & 0xFF;
                    }
                    if ((pos = this.branchNext(pos, node + 1)) >= 0) continue;
                    return this.entry_;
                }
                int length = node - 16 + 1;
                if (this.maxLength_ > 0 && this.entry_.length + length > this.maxLength_) {
                    this.entry_.append(this.bytes_, pos, this.maxLength_ - this.entry_.length);
                    return this.truncateAndStop();
                }
                this.entry_.append(this.bytes_, pos, length);
                pos += length;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private Entry truncateAndStop() {
            this.pos_ = -1;
            this.entry_.value = -1;
            return this.entry_;
        }

        private int branchNext(int pos, int length) {
            int node;
            while (length > 5) {
                this.stack_.add((long)BytesTrie.skipDelta(this.bytes_, ++pos) << 32 | (long)(length - (length >> 1) << 16) | (long)this.entry_.length);
                length >>= 1;
                pos = BytesTrie.jumpByDelta(this.bytes_, pos);
            }
            byte trieByte = this.bytes_[pos++];
            boolean isFinal = ((node = this.bytes_[pos++] & 0xFF) & 1) != 0;
            int value = BytesTrie.readValue(this.bytes_, pos, node >> 1);
            pos = BytesTrie.skipValue(pos, node);
            this.stack_.add((long)pos << 32 | (long)(length - 1 << 16) | (long)this.entry_.length);
            this.entry_.append(trieByte);
            if (isFinal) {
                this.pos_ = -1;
                this.entry_.value = value;
                return -1;
            }
            return pos + value;
        }
    }

    public static final class Entry {
        public int value;
        private byte[] bytes;
        private int length;

        private Entry(int capacity) {
            this.bytes = new byte[capacity];
        }

        public int bytesLength() {
            return this.length;
        }

        public byte byteAt(int index) {
            return this.bytes[index];
        }

        public void copyBytesTo(byte[] dest, int destOffset) {
            System.arraycopy(this.bytes, 0, dest, destOffset, this.length);
        }

        public ByteBuffer bytesAsByteBuffer() {
            return ByteBuffer.wrap(this.bytes, 0, this.length).asReadOnlyBuffer();
        }

        private void ensureCapacity(int len) {
            if (this.bytes.length < len) {
                byte[] newBytes = new byte[Math.min(2 * this.bytes.length, 2 * len)];
                System.arraycopy(this.bytes, 0, newBytes, 0, this.length);
                this.bytes = newBytes;
            }
        }

        private void append(byte b) {
            this.ensureCapacity(this.length + 1);
            this.bytes[this.length++] = b;
        }

        private void append(byte[] b, int off, int len) {
            this.ensureCapacity(this.length + len);
            System.arraycopy(b, off, this.bytes, this.length, len);
            this.length += len;
        }

        private void truncateString(int newLength) {
            this.length = newLength;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Result {
        NO_MATCH,
        NO_VALUE,
        FINAL_VALUE,
        INTERMEDIATE_VALUE;


        public boolean matches() {
            return this != NO_MATCH;
        }

        public boolean hasValue() {
            return this.ordinal() >= 2;
        }

        public boolean hasNext() {
            return (this.ordinal() & 1) != 0;
        }
    }

    public static final class State {
        private byte[] bytes;
        private int root;
        private int pos;
        private int remainingMatchLength;

        static /* synthetic */ byte[] access$002(State x0, byte[] x1) {
            x0.bytes = x1;
            return x1;
        }
    }
}

