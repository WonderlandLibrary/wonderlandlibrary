/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.util.BytesTrie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CharsTrie
implements Cloneable,
Iterable<Entry> {
    private static BytesTrie.Result[] valueResults_ = new BytesTrie.Result[]{BytesTrie.Result.INTERMEDIATE_VALUE, BytesTrie.Result.FINAL_VALUE};
    static final int kMaxBranchLinearSubNodeLength = 5;
    static final int kMinLinearMatch = 48;
    static final int kMaxLinearMatchLength = 16;
    static final int kMinValueLead = 64;
    static final int kNodeTypeMask = 63;
    static final int kValueIsFinal = 32768;
    static final int kMaxOneUnitValue = 16383;
    static final int kMinTwoUnitValueLead = 16384;
    static final int kThreeUnitValueLead = 32767;
    static final int kMaxTwoUnitValue = 0x3FFEFFFF;
    static final int kMaxOneUnitNodeValue = 255;
    static final int kMinTwoUnitNodeValueLead = 16448;
    static final int kThreeUnitNodeValueLead = 32704;
    static final int kMaxTwoUnitNodeValue = 0xFDFFFF;
    static final int kMaxOneUnitDelta = 64511;
    static final int kMinTwoUnitDeltaLead = 64512;
    static final int kThreeUnitDeltaLead = 65535;
    static final int kMaxTwoUnitDelta = 0x3FEFFFF;
    private CharSequence chars_;
    private int root_;
    private int pos_;
    private int remainingMatchLength_;

    public CharsTrie(CharSequence trieChars, int offset) {
        this.chars_ = trieChars;
        this.pos_ = this.root_ = offset;
        this.remainingMatchLength_ = -1;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public CharsTrie reset() {
        this.pos_ = this.root_;
        this.remainingMatchLength_ = -1;
        return this;
    }

    public CharsTrie saveState(State state) {
        state.chars = this.chars_;
        state.root = this.root_;
        state.pos = this.pos_;
        state.remainingMatchLength = this.remainingMatchLength_;
        return this;
    }

    public CharsTrie resetToState(State state) {
        if (this.chars_ != state.chars || this.chars_ == null || this.root_ != state.root) {
            throw new IllegalArgumentException("incompatible trie state");
        }
        this.pos_ = state.pos;
        this.remainingMatchLength_ = state.remainingMatchLength;
        return this;
    }

    public BytesTrie.Result current() {
        char node;
        int pos = this.pos_;
        if (pos < 0) {
            return BytesTrie.Result.NO_MATCH;
        }
        return this.remainingMatchLength_ < 0 && (node = this.chars_.charAt(pos)) >= '@' ? valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
    }

    public BytesTrie.Result first(int inUnit) {
        this.remainingMatchLength_ = -1;
        return this.nextImpl(this.root_, inUnit);
    }

    public BytesTrie.Result firstForCodePoint(int cp) {
        return cp <= 65535 ? this.first(cp) : (this.first(UTF16.getLeadSurrogate(cp)).hasNext() ? this.next(UTF16.getTrailSurrogate(cp)) : BytesTrie.Result.NO_MATCH);
    }

    public BytesTrie.Result next(int inUnit) {
        int pos = this.pos_;
        if (pos < 0) {
            return BytesTrie.Result.NO_MATCH;
        }
        int length = this.remainingMatchLength_;
        if (length >= 0) {
            if (inUnit == this.chars_.charAt(pos++)) {
                char node;
                this.remainingMatchLength_ = --length;
                this.pos_ = pos;
                return length < 0 && (node = this.chars_.charAt(pos)) >= '@' ? valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
            }
            this.stop();
            return BytesTrie.Result.NO_MATCH;
        }
        return this.nextImpl(pos, inUnit);
    }

    public BytesTrie.Result nextForCodePoint(int cp) {
        return cp <= 65535 ? this.next(cp) : (this.next(UTF16.getLeadSurrogate(cp)).hasNext() ? this.next(UTF16.getTrailSurrogate(cp)) : BytesTrie.Result.NO_MATCH);
    }

    public BytesTrie.Result next(CharSequence s, int sIndex, int sLimit) {
        if (sIndex >= sLimit) {
            return this.current();
        }
        int pos = this.pos_;
        if (pos < 0) {
            return BytesTrie.Result.NO_MATCH;
        }
        int length = this.remainingMatchLength_;
        block0: while (true) {
            int node;
            if (sIndex == sLimit) {
                this.remainingMatchLength_ = length;
                this.pos_ = pos;
                return length < 0 && (node = this.chars_.charAt(pos)) >= 64 ? valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
            }
            char inUnit = s.charAt(sIndex++);
            if (length >= 0) {
                if (inUnit != this.chars_.charAt(pos)) {
                    this.stop();
                    return BytesTrie.Result.NO_MATCH;
                }
                ++pos;
                --length;
                continue;
            }
            this.remainingMatchLength_ = length;
            node = this.chars_.charAt(pos++);
            while (true) {
                if (node < 48) {
                    BytesTrie.Result result = this.branchNext(pos, node, inUnit);
                    if (result == BytesTrie.Result.NO_MATCH) {
                        return BytesTrie.Result.NO_MATCH;
                    }
                    if (sIndex == sLimit) {
                        return result;
                    }
                    if (result == BytesTrie.Result.FINAL_VALUE) {
                        this.stop();
                        return BytesTrie.Result.NO_MATCH;
                    }
                    inUnit = s.charAt(sIndex++);
                    pos = this.pos_;
                    node = this.chars_.charAt(pos++);
                    continue;
                }
                if (node < 64) {
                    length = node - 48;
                    if (inUnit != this.chars_.charAt(pos)) {
                        this.stop();
                        return BytesTrie.Result.NO_MATCH;
                    }
                    ++pos;
                    --length;
                    continue block0;
                }
                if ((node & 0x8000) != 0) {
                    this.stop();
                    return BytesTrie.Result.NO_MATCH;
                }
                pos = CharsTrie.skipNodeValue(pos, node);
                node &= 0x3F;
            }
            break;
        }
    }

    public int getValue() {
        int pos = this.pos_;
        char leadUnit = this.chars_.charAt(pos++);
        assert (leadUnit >= '@');
        return (leadUnit & 0x8000) != 0 ? CharsTrie.readValue(this.chars_, pos, leadUnit & 0x7FFF) : CharsTrie.readNodeValue(this.chars_, pos, leadUnit);
    }

    public long getUniqueValue() {
        int pos = this.pos_;
        if (pos < 0) {
            return 0L;
        }
        long uniqueValue = CharsTrie.findUniqueValue(this.chars_, pos + this.remainingMatchLength_ + 1, 0L);
        return uniqueValue << 31 >> 31;
    }

    public int getNextChars(Appendable out) {
        int node;
        int pos = this.pos_;
        if (pos < 0) {
            return 0;
        }
        if (this.remainingMatchLength_ >= 0) {
            CharsTrie.append(out, this.chars_.charAt(pos));
            return 1;
        }
        if ((node = this.chars_.charAt(pos++)) >= 64) {
            if ((node & 0x8000) != 0) {
                return 0;
            }
            pos = CharsTrie.skipNodeValue(pos, node);
            node &= 0x3F;
        }
        if (node < 48) {
            if (node == 0) {
                node = this.chars_.charAt(pos++);
            }
            CharsTrie.getNextBranchChars(this.chars_, pos, ++node, out);
            return node;
        }
        CharsTrie.append(out, this.chars_.charAt(pos));
        return 1;
    }

    public Iterator iterator() {
        return new Iterator(this.chars_, this.pos_, this.remainingMatchLength_, 0);
    }

    public Iterator iterator(int maxStringLength) {
        return new Iterator(this.chars_, this.pos_, this.remainingMatchLength_, maxStringLength);
    }

    public static Iterator iterator(CharSequence trieChars, int offset, int maxStringLength) {
        return new Iterator(trieChars, offset, -1, maxStringLength);
    }

    private void stop() {
        this.pos_ = -1;
    }

    private static int readValue(CharSequence chars, int pos, int leadUnit) {
        int value = leadUnit < 16384 ? leadUnit : (leadUnit < 32767 ? leadUnit - 16384 << 16 | chars.charAt(pos) : chars.charAt(pos) << 16 | chars.charAt(pos + 1));
        return value;
    }

    private static int skipValue(int pos, int leadUnit) {
        if (leadUnit >= 16384) {
            pos = leadUnit < 32767 ? ++pos : (pos += 2);
        }
        return pos;
    }

    private static int skipValue(CharSequence chars, int pos) {
        char leadUnit = chars.charAt(pos++);
        return CharsTrie.skipValue(pos, leadUnit & 0x7FFF);
    }

    private static int readNodeValue(CharSequence chars, int pos, int leadUnit) {
        assert (64 <= leadUnit && leadUnit < 32768);
        int value = leadUnit < 16448 ? (leadUnit >> 6) - 1 : (leadUnit < 32704 ? (leadUnit & 0x7FC0) - 16448 << 10 | chars.charAt(pos) : chars.charAt(pos) << 16 | chars.charAt(pos + 1));
        return value;
    }

    private static int skipNodeValue(int pos, int leadUnit) {
        assert (64 <= leadUnit && leadUnit < 32768);
        if (leadUnit >= 16448) {
            pos = leadUnit < 32704 ? ++pos : (pos += 2);
        }
        return pos;
    }

    private static int jumpByDelta(CharSequence chars, int pos) {
        int delta;
        if ((delta = chars.charAt(pos++)) >= 64512) {
            if (delta == 65535) {
                delta = chars.charAt(pos) << 16 | chars.charAt(pos + 1);
                pos += 2;
            } else {
                delta = delta - 64512 << 16 | chars.charAt(pos++);
            }
        }
        return pos + delta;
    }

    private static int skipDelta(CharSequence chars, int pos) {
        char delta;
        if ((delta = chars.charAt(pos++)) >= '\ufc00') {
            pos = delta == '\uffff' ? (pos += 2) : ++pos;
        }
        return pos;
    }

    private BytesTrie.Result branchNext(int pos, int length, int inUnit) {
        if (length == 0) {
            length = this.chars_.charAt(pos++);
        }
        ++length;
        while (length > 5) {
            if (inUnit < this.chars_.charAt(pos++)) {
                length >>= 1;
                pos = CharsTrie.jumpByDelta(this.chars_, pos);
                continue;
            }
            length -= length >> 1;
            pos = CharsTrie.skipDelta(this.chars_, pos);
        }
        do {
            if (inUnit == this.chars_.charAt(pos++)) {
                BytesTrie.Result result;
                int node = this.chars_.charAt(pos);
                if ((node & 0x8000) != 0) {
                    result = BytesTrie.Result.FINAL_VALUE;
                } else {
                    int delta;
                    ++pos;
                    if (node < 16384) {
                        delta = node;
                    } else if (node < 32767) {
                        delta = node - 16384 << 16 | this.chars_.charAt(pos++);
                    } else {
                        delta = this.chars_.charAt(pos) << 16 | this.chars_.charAt(pos + 1);
                        pos += 2;
                    }
                    node = this.chars_.charAt(pos += delta);
                    result = node >= 64 ? valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
                }
                this.pos_ = pos;
                return result;
            }
            pos = CharsTrie.skipValue(this.chars_, pos);
        } while (--length > 1);
        if (inUnit == this.chars_.charAt(pos++)) {
            this.pos_ = pos;
            char node = this.chars_.charAt(pos);
            return node >= '@' ? valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
        }
        this.stop();
        return BytesTrie.Result.NO_MATCH;
    }

    /*
     * Enabled aggressive block sorting
     */
    private BytesTrie.Result nextImpl(int pos, int inUnit) {
        int node = this.chars_.charAt(pos++);
        while (true) {
            if (node < 48) {
                return this.branchNext(pos, node, inUnit);
            }
            if (node < 64) {
                BytesTrie.Result result;
                int length = node - 48;
                if (inUnit != this.chars_.charAt(pos++)) break;
                this.remainingMatchLength_ = --length;
                this.pos_ = pos;
                if (length < 0) {
                    char c = this.chars_.charAt(pos);
                    node = c;
                    if (c >= '@') {
                        result = valueResults_[node >> 15];
                        return result;
                    }
                }
                result = BytesTrie.Result.NO_VALUE;
                return result;
            }
            if ((node & 0x8000) != 0) break;
            pos = CharsTrie.skipNodeValue(pos, node);
            node &= 0x3F;
        }
        this.stop();
        return BytesTrie.Result.NO_MATCH;
    }

    private static long findUniqueValueFromBranch(CharSequence chars, int pos, int length, long uniqueValue) {
        while (length > 5) {
            if ((uniqueValue = CharsTrie.findUniqueValueFromBranch(chars, CharsTrie.jumpByDelta(chars, ++pos), length >> 1, uniqueValue)) == 0L) {
                return 0L;
            }
            length -= length >> 1;
            pos = CharsTrie.skipDelta(chars, pos);
        }
        do {
            int n = ++pos;
            int node = chars.charAt(n);
            boolean isFinal = (node & 0x8000) != 0;
            int value = CharsTrie.readValue(chars, ++pos, node &= 0x7FFF);
            pos = CharsTrie.skipValue(pos, node);
            if (isFinal) {
                if (uniqueValue != 0L) {
                    if (value == (int)(uniqueValue >> 1)) continue;
                    return 0L;
                }
                uniqueValue = (long)value << 1 | 1L;
                continue;
            }
            if ((uniqueValue = CharsTrie.findUniqueValue(chars, pos + value, uniqueValue)) != 0L) continue;
            return 0L;
        } while (--length > 1);
        return (long)(pos + 1) << 33 | uniqueValue & 0x1FFFFFFFFL;
    }

    private static long findUniqueValue(CharSequence chars, int pos, long uniqueValue) {
        int node = chars.charAt(pos++);
        while (true) {
            if (node < 48) {
                if (node == 0) {
                    node = chars.charAt(pos++);
                }
                if ((uniqueValue = CharsTrie.findUniqueValueFromBranch(chars, pos, node + '\u0001', uniqueValue)) == 0L) {
                    return 0L;
                }
                pos = (int)(uniqueValue >>> 33);
                node = chars.charAt(pos++);
                continue;
            }
            if (node < 64) {
                pos += node - 48 + 1;
                node = chars.charAt(pos++);
                continue;
            }
            boolean isFinal = (node & 0x8000) != 0;
            int value = isFinal ? CharsTrie.readValue(chars, pos, node & 0x7FFF) : CharsTrie.readNodeValue(chars, pos, node);
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
            pos = CharsTrie.skipNodeValue(pos, node);
            node &= 0x3F;
        }
    }

    private static void getNextBranchChars(CharSequence chars, int pos, int length, Appendable out) {
        while (length > 5) {
            CharsTrie.getNextBranchChars(chars, CharsTrie.jumpByDelta(chars, ++pos), length >> 1, out);
            length -= length >> 1;
            pos = CharsTrie.skipDelta(chars, pos);
        }
        do {
            CharsTrie.append(out, chars.charAt(pos++));
            pos = CharsTrie.skipValue(chars, pos);
        } while (--length > 1);
        CharsTrie.append(out, chars.charAt(pos));
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
        private CharSequence chars_;
        private int pos_;
        private int initialPos_;
        private int remainingMatchLength_;
        private int initialRemainingMatchLength_;
        private boolean skipValue_;
        private StringBuilder str_ = new StringBuilder();
        private int maxLength_;
        private Entry entry_ = new Entry();
        private ArrayList<Long> stack_ = new ArrayList();

        private Iterator(CharSequence trieChars, int offset, int remainingMatchLength, int maxStringLength) {
            this.chars_ = trieChars;
            this.pos_ = this.initialPos_ = offset;
            this.remainingMatchLength_ = this.initialRemainingMatchLength_ = remainingMatchLength;
            this.maxLength_ = maxStringLength;
            int length = this.remainingMatchLength_;
            if (length >= 0) {
                if (this.maxLength_ > 0 && ++length > this.maxLength_) {
                    length = this.maxLength_;
                }
                this.str_.append(this.chars_, this.pos_, this.pos_ + length);
                this.pos_ += length;
                this.remainingMatchLength_ -= length;
            }
        }

        public Iterator reset() {
            this.pos_ = this.initialPos_;
            this.remainingMatchLength_ = this.initialRemainingMatchLength_;
            this.skipValue_ = false;
            int length = this.remainingMatchLength_ + 1;
            if (this.maxLength_ > 0 && length > this.maxLength_) {
                length = this.maxLength_;
            }
            this.str_.setLength(length);
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
                this.str_.setLength(length & 0xFFFF);
                if ((length >>>= 16) > 1) {
                    if ((pos = this.branchNext(pos, length)) < 0) {
                        return this.entry_;
                    }
                } else {
                    this.str_.append(this.chars_.charAt(pos++));
                }
            }
            if (this.remainingMatchLength_ >= 0) {
                return this.truncateAndStop();
            }
            while (true) {
                int node;
                if ((node = this.chars_.charAt(pos++)) >= 64) {
                    if (this.skipValue_) {
                        pos = CharsTrie.skipNodeValue(pos, node);
                        node &= 0x3F;
                        this.skipValue_ = false;
                    } else {
                        boolean isFinal = (node & 0x8000) != 0;
                        this.entry_.value = isFinal ? CharsTrie.readValue(this.chars_, pos, node & 0x7FFF) : CharsTrie.readNodeValue(this.chars_, pos, node);
                        if (isFinal || this.maxLength_ > 0 && this.str_.length() == this.maxLength_) {
                            this.pos_ = -1;
                        } else {
                            this.pos_ = pos - 1;
                            this.skipValue_ = true;
                        }
                        this.entry_.chars = this.str_;
                        return this.entry_;
                    }
                }
                if (this.maxLength_ > 0 && this.str_.length() == this.maxLength_) {
                    return this.truncateAndStop();
                }
                if (node < 48) {
                    if (node == 0) {
                        node = this.chars_.charAt(pos++);
                    }
                    if ((pos = this.branchNext(pos, node + 1)) >= 0) continue;
                    return this.entry_;
                }
                int length = node - 48 + 1;
                if (this.maxLength_ > 0 && this.str_.length() + length > this.maxLength_) {
                    this.str_.append(this.chars_, pos, pos + this.maxLength_ - this.str_.length());
                    return this.truncateAndStop();
                }
                this.str_.append(this.chars_, pos, pos + length);
                pos += length;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private Entry truncateAndStop() {
            this.pos_ = -1;
            this.entry_.chars = this.str_;
            this.entry_.value = -1;
            return this.entry_;
        }

        private int branchNext(int pos, int length) {
            int node;
            while (length > 5) {
                this.stack_.add((long)CharsTrie.skipDelta(this.chars_, ++pos) << 32 | (long)(length - (length >> 1) << 16) | (long)this.str_.length());
                length >>= 1;
                pos = CharsTrie.jumpByDelta(this.chars_, pos);
            }
            char trieUnit = this.chars_.charAt(pos++);
            boolean isFinal = ((node = this.chars_.charAt(pos++)) & 0x8000) != 0;
            int value = CharsTrie.readValue(this.chars_, pos, node &= 0x7FFF);
            pos = CharsTrie.skipValue(pos, node);
            this.stack_.add((long)pos << 32 | (long)(length - 1 << 16) | (long)this.str_.length());
            this.str_.append(trieUnit);
            if (isFinal) {
                this.pos_ = -1;
                this.entry_.chars = this.str_;
                this.entry_.value = value;
                return -1;
            }
            return pos + value;
        }
    }

    public static final class Entry {
        public CharSequence chars;
        public int value;

        private Entry() {
        }
    }

    public static final class State {
        private CharSequence chars;
        private int root;
        private int pos;
        private int remainingMatchLength;
    }
}

