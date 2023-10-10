/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.Trie2_16;
import com.ibm.icu.impl.Trie2_32;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Trie2
implements Iterable<Range> {
    private static ValueMapper defaultValueMapper = new ValueMapper(){

        @Override
        public int map(int in) {
            return in;
        }
    };
    UTrie2Header header;
    char[] index;
    int data16;
    int[] data32;
    int indexLength;
    int dataLength;
    int index2NullOffset;
    int initialValue;
    int errorValue;
    int highStart;
    int highValueIndex;
    int dataNullOffset;
    int fHash;
    static final int UTRIE2_OPTIONS_VALUE_BITS_MASK = 15;
    static final int UTRIE2_SHIFT_1 = 11;
    static final int UTRIE2_SHIFT_2 = 5;
    static final int UTRIE2_SHIFT_1_2 = 6;
    static final int UTRIE2_OMITTED_BMP_INDEX_1_LENGTH = 32;
    static final int UTRIE2_CP_PER_INDEX_1_ENTRY = 2048;
    static final int UTRIE2_INDEX_2_BLOCK_LENGTH = 64;
    static final int UTRIE2_INDEX_2_MASK = 63;
    static final int UTRIE2_DATA_BLOCK_LENGTH = 32;
    static final int UTRIE2_DATA_MASK = 31;
    static final int UTRIE2_INDEX_SHIFT = 2;
    static final int UTRIE2_DATA_GRANULARITY = 4;
    static final int UTRIE2_INDEX_2_OFFSET = 0;
    static final int UTRIE2_LSCP_INDEX_2_OFFSET = 2048;
    static final int UTRIE2_LSCP_INDEX_2_LENGTH = 32;
    static final int UTRIE2_INDEX_2_BMP_LENGTH = 2080;
    static final int UTRIE2_UTF8_2B_INDEX_2_OFFSET = 2080;
    static final int UTRIE2_UTF8_2B_INDEX_2_LENGTH = 32;
    static final int UTRIE2_INDEX_1_OFFSET = 2112;
    static final int UTRIE2_MAX_INDEX_1_LENGTH = 512;
    static final int UTRIE2_BAD_UTF8_DATA_OFFSET = 128;
    static final int UTRIE2_DATA_START_OFFSET = 192;
    static final int UNEWTRIE2_INDEX_GAP_OFFSET = 2080;
    static final int UNEWTRIE2_INDEX_GAP_LENGTH = 576;
    static final int UNEWTRIE2_MAX_INDEX_2_LENGTH = 35488;
    static final int UNEWTRIE2_INDEX_1_LENGTH = 544;
    static final int UNEWTRIE2_MAX_DATA_LENGTH = 1115264;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Trie2 createFromSerialized(ByteBuffer bytes) throws IOException {
        ByteOrder outerByteOrder = bytes.order();
        try {
            Trie2 This;
            ValueWidth width;
            UTrie2Header header = new UTrie2Header();
            header.signature = bytes.getInt();
            switch (header.signature) {
                case 1416784178: {
                    break;
                }
                case 845771348: {
                    boolean isBigEndian = outerByteOrder == ByteOrder.BIG_ENDIAN;
                    bytes.order(isBigEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
                    header.signature = 1416784178;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Buffer does not contain a serialized UTrie2");
                }
            }
            header.options = bytes.getChar();
            header.indexLength = bytes.getChar();
            header.shiftedDataLength = bytes.getChar();
            header.index2NullOffset = bytes.getChar();
            header.dataNullOffset = bytes.getChar();
            header.shiftedHighStart = bytes.getChar();
            if ((header.options & 0xF) > 1) {
                throw new IllegalArgumentException("UTrie2 serialized format error.");
            }
            if ((header.options & 0xF) == 0) {
                width = ValueWidth.BITS_16;
                This = new Trie2_16();
            } else {
                width = ValueWidth.BITS_32;
                This = new Trie2_32();
            }
            This.header = header;
            This.indexLength = header.indexLength;
            This.dataLength = header.shiftedDataLength << 2;
            This.index2NullOffset = header.index2NullOffset;
            This.dataNullOffset = header.dataNullOffset;
            This.highStart = header.shiftedHighStart << 11;
            This.highValueIndex = This.dataLength - 4;
            if (width == ValueWidth.BITS_16) {
                This.highValueIndex += This.indexLength;
            }
            int indexArraySize = This.indexLength;
            if (width == ValueWidth.BITS_16) {
                indexArraySize += This.dataLength;
            }
            This.index = ICUBinary.getChars(bytes, indexArraySize, 0);
            if (width == ValueWidth.BITS_16) {
                This.data16 = This.indexLength;
            } else {
                This.data32 = ICUBinary.getInts(bytes, This.dataLength, 0);
            }
            switch (width) {
                case BITS_16: {
                    This.data32 = null;
                    This.initialValue = This.index[This.dataNullOffset];
                    This.errorValue = This.index[This.data16 + 128];
                    break;
                }
                case BITS_32: {
                    This.data16 = 0;
                    This.initialValue = This.data32[This.dataNullOffset];
                    This.errorValue = This.data32[128];
                    break;
                }
                default: {
                    throw new IllegalArgumentException("UTrie2 serialized format error.");
                }
            }
            Trie2 trie2 = This;
            return trie2;
        }
        finally {
            bytes.order(outerByteOrder);
        }
    }

    public static int getVersion(InputStream is, boolean littleEndianOk) throws IOException {
        if (!is.markSupported()) {
            throw new IllegalArgumentException("Input stream must support mark().");
        }
        is.mark(4);
        byte[] sig = new byte[4];
        int read = is.read(sig);
        is.reset();
        if (read != sig.length) {
            return 0;
        }
        if (sig[0] == 84 && sig[1] == 114 && sig[2] == 105 && sig[3] == 101) {
            return 1;
        }
        if (sig[0] == 84 && sig[1] == 114 && sig[2] == 105 && sig[3] == 50) {
            return 2;
        }
        if (littleEndianOk) {
            if (sig[0] == 101 && sig[1] == 105 && sig[2] == 114 && sig[3] == 84) {
                return 1;
            }
            if (sig[0] == 50 && sig[1] == 105 && sig[2] == 114 && sig[3] == 84) {
                return 2;
            }
        }
        return 0;
    }

    public abstract int get(int var1);

    public abstract int getFromU16SingleLead(char var1);

    public final boolean equals(Object other) {
        if (!(other instanceof Trie2)) {
            return false;
        }
        Trie2 OtherTrie = (Trie2)other;
        Iterator<Range> otherIter = OtherTrie.iterator();
        for (Range rangeFromThis : this) {
            if (!otherIter.hasNext()) {
                return false;
            }
            Range rangeFromOther = otherIter.next();
            if (rangeFromThis.equals(rangeFromOther)) continue;
            return false;
        }
        if (otherIter.hasNext()) {
            return false;
        }
        return this.errorValue == OtherTrie.errorValue && this.initialValue == OtherTrie.initialValue;
    }

    public int hashCode() {
        if (this.fHash == 0) {
            int hash = Trie2.initHash();
            for (Range r2 : this) {
                hash = Trie2.hashInt(hash, r2.hashCode());
            }
            if (hash == 0) {
                hash = 1;
            }
            this.fHash = hash;
        }
        return this.fHash;
    }

    @Override
    public Iterator<Range> iterator() {
        return this.iterator(defaultValueMapper);
    }

    public Iterator<Range> iterator(ValueMapper mapper) {
        return new Trie2Iterator(mapper);
    }

    public Iterator<Range> iteratorForLeadSurrogate(char lead, ValueMapper mapper) {
        return new Trie2Iterator(lead, mapper);
    }

    public Iterator<Range> iteratorForLeadSurrogate(char lead) {
        return new Trie2Iterator(lead, defaultValueMapper);
    }

    protected int serializeHeader(DataOutputStream dos) throws IOException {
        int bytesWritten = 0;
        dos.writeInt(this.header.signature);
        dos.writeShort(this.header.options);
        dos.writeShort(this.header.indexLength);
        dos.writeShort(this.header.shiftedDataLength);
        dos.writeShort(this.header.index2NullOffset);
        dos.writeShort(this.header.dataNullOffset);
        dos.writeShort(this.header.shiftedHighStart);
        bytesWritten += 16;
        for (int i2 = 0; i2 < this.header.indexLength; ++i2) {
            dos.writeChar(this.index[i2]);
        }
        return bytesWritten += this.header.indexLength;
    }

    public CharSequenceIterator charSequenceIterator(CharSequence text, int index) {
        return new CharSequenceIterator(text, index);
    }

    int rangeEnd(int start, int limitp, int val2) {
        int c2;
        int limit = Math.min(this.highStart, limitp);
        for (c2 = start + 1; c2 < limit && this.get(c2) == val2; ++c2) {
        }
        if (c2 >= this.highStart) {
            c2 = limitp;
        }
        return c2 - 1;
    }

    private static int initHash() {
        return -2128831035;
    }

    private static int hashByte(int h2, int b2) {
        h2 *= 16777619;
        return h2 ^= b2;
    }

    private static int hashUChar32(int h2, int c2) {
        h2 = Trie2.hashByte(h2, c2 & 0xFF);
        h2 = Trie2.hashByte(h2, c2 >> 8 & 0xFF);
        h2 = Trie2.hashByte(h2, c2 >> 16);
        return h2;
    }

    private static int hashInt(int h2, int i2) {
        h2 = Trie2.hashByte(h2, i2 & 0xFF);
        h2 = Trie2.hashByte(h2, i2 >> 8 & 0xFF);
        h2 = Trie2.hashByte(h2, i2 >> 16 & 0xFF);
        h2 = Trie2.hashByte(h2, i2 >> 24 & 0xFF);
        return h2;
    }

    class Trie2Iterator
    implements Iterator<Range> {
        private ValueMapper mapper;
        private Range returnValue = new Range();
        private int nextStart;
        private int limitCP;
        private boolean doingCodePoints = true;
        private boolean doLeadSurrogates = true;

        Trie2Iterator(ValueMapper vm) {
            this.mapper = vm;
            this.nextStart = 0;
            this.limitCP = 0x110000;
            this.doLeadSurrogates = true;
        }

        Trie2Iterator(char leadSurrogate, ValueMapper vm) {
            if (leadSurrogate < '\ud800' || leadSurrogate > '\udbff') {
                throw new IllegalArgumentException("Bad lead surrogate value.");
            }
            this.mapper = vm;
            this.nextStart = leadSurrogate - 55232 << 10;
            this.limitCP = this.nextStart + 1024;
            this.doLeadSurrogates = false;
        }

        @Override
        public Range next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.nextStart >= this.limitCP) {
                this.doingCodePoints = false;
                this.nextStart = 55296;
            }
            int endOfRange = 0;
            int val2 = 0;
            int mappedVal = 0;
            if (this.doingCodePoints) {
                val2 = Trie2.this.get(this.nextStart);
                mappedVal = this.mapper.map(val2);
                endOfRange = Trie2.this.rangeEnd(this.nextStart, this.limitCP, val2);
                while (endOfRange < this.limitCP - 1 && this.mapper.map(val2 = Trie2.this.get(endOfRange + 1)) == mappedVal) {
                    endOfRange = Trie2.this.rangeEnd(endOfRange + 1, this.limitCP, val2);
                }
            } else {
                val2 = Trie2.this.getFromU16SingleLead((char)this.nextStart);
                mappedVal = this.mapper.map(val2);
                endOfRange = this.rangeEndLS((char)this.nextStart);
                while (endOfRange < 56319 && this.mapper.map(val2 = Trie2.this.getFromU16SingleLead((char)(endOfRange + 1))) == mappedVal) {
                    endOfRange = this.rangeEndLS((char)(endOfRange + 1));
                }
            }
            this.returnValue.startCodePoint = this.nextStart;
            this.returnValue.endCodePoint = endOfRange;
            this.returnValue.value = mappedVal;
            this.returnValue.leadSurrogate = !this.doingCodePoints;
            this.nextStart = endOfRange + 1;
            return this.returnValue;
        }

        @Override
        public boolean hasNext() {
            return this.doingCodePoints && (this.doLeadSurrogates || this.nextStart < this.limitCP) || this.nextStart < 56320;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private int rangeEndLS(char startingLS) {
            int c2;
            if (startingLS >= '\udbff') {
                return 56319;
            }
            int val2 = Trie2.this.getFromU16SingleLead(startingLS);
            for (c2 = startingLS + '\u0001'; c2 <= 56319 && Trie2.this.getFromU16SingleLead((char)c2) == val2; ++c2) {
            }
            return c2 - 1;
        }
    }

    static class UTrie2Header {
        int signature;
        int options;
        int indexLength;
        int shiftedDataLength;
        int index2NullOffset;
        int dataNullOffset;
        int shiftedHighStart;

        UTrie2Header() {
        }
    }

    static enum ValueWidth {
        BITS_16,
        BITS_32;

    }

    public class CharSequenceIterator
    implements Iterator<CharSequenceValues> {
        private CharSequence text;
        private int textLength;
        private int index;
        private CharSequenceValues fResults = new CharSequenceValues();

        CharSequenceIterator(CharSequence t2, int index) {
            this.text = t2;
            this.textLength = this.text.length();
            this.set(index);
        }

        public void set(int i2) {
            if (i2 < 0 || i2 > this.textLength) {
                throw new IndexOutOfBoundsException();
            }
            this.index = i2;
        }

        @Override
        public final boolean hasNext() {
            return this.index < this.textLength;
        }

        public final boolean hasPrevious() {
            return this.index > 0;
        }

        @Override
        public CharSequenceValues next() {
            int c2 = Character.codePointAt(this.text, this.index);
            int val2 = Trie2.this.get(c2);
            this.fResults.index = this.index++;
            this.fResults.codePoint = c2;
            this.fResults.value = val2;
            if (c2 >= 65536) {
                ++this.index;
            }
            return this.fResults;
        }

        public CharSequenceValues previous() {
            int c2 = Character.codePointBefore(this.text, this.index);
            int val2 = Trie2.this.get(c2);
            --this.index;
            if (c2 >= 65536) {
                --this.index;
            }
            this.fResults.index = this.index;
            this.fResults.codePoint = c2;
            this.fResults.value = val2;
            return this.fResults;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Trie2.CharSequenceIterator does not support remove().");
        }
    }

    public static class CharSequenceValues {
        public int index;
        public int codePoint;
        public int value;
    }

    public static interface ValueMapper {
        public int map(int var1);
    }

    public static class Range {
        public int startCodePoint;
        public int endCodePoint;
        public int value;
        public boolean leadSurrogate;

        public boolean equals(Object other) {
            if (other == null || !other.getClass().equals(this.getClass())) {
                return false;
            }
            Range tother = (Range)other;
            return this.startCodePoint == tother.startCodePoint && this.endCodePoint == tother.endCodePoint && this.value == tother.value && this.leadSurrogate == tother.leadSurrogate;
        }

        public int hashCode() {
            int h2 = Trie2.initHash();
            h2 = Trie2.hashUChar32(h2, this.startCodePoint);
            h2 = Trie2.hashUChar32(h2, this.endCodePoint);
            h2 = Trie2.hashInt(h2, this.value);
            h2 = Trie2.hashByte(h2, this.leadSurrogate ? 1 : 0);
            return h2;
        }
    }
}

