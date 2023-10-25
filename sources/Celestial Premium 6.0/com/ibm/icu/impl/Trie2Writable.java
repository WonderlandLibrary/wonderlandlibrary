/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Trie2;
import com.ibm.icu.impl.Trie2_16;
import com.ibm.icu.impl.Trie2_32;

public class Trie2Writable
extends Trie2 {
    private static final int UTRIE2_MAX_INDEX_LENGTH = 65535;
    private static final int UTRIE2_MAX_DATA_LENGTH = 262140;
    private static final int UNEWTRIE2_INITIAL_DATA_LENGTH = 16384;
    private static final int UNEWTRIE2_MEDIUM_DATA_LENGTH = 131072;
    private static final int UNEWTRIE2_INDEX_2_NULL_OFFSET = 2656;
    private static final int UNEWTRIE2_INDEX_2_START_OFFSET = 2720;
    private static final int UNEWTRIE2_DATA_NULL_OFFSET = 192;
    private static final int UNEWTRIE2_DATA_START_OFFSET = 256;
    private static final int UNEWTRIE2_DATA_0800_OFFSET = 2176;
    private int[] index1 = new int[544];
    private int[] index2 = new int[35488];
    private int[] data;
    private int index2Length;
    private int dataCapacity;
    private int firstFreeBlock;
    private int index2NullOffset;
    private boolean isCompacted;
    private int[] map = new int[34852];
    private boolean UTRIE2_DEBUG = false;

    public Trie2Writable(int initialValueP, int errorValueP) {
        this.init(initialValueP, errorValueP);
    }

    private void init(int initialValueP, int errorValueP) {
        int j;
        int i;
        this.initialValue = initialValueP;
        this.errorValue = errorValueP;
        this.highStart = 0x110000;
        this.data = new int[16384];
        this.dataCapacity = 16384;
        this.initialValue = initialValueP;
        this.errorValue = errorValueP;
        this.highStart = 0x110000;
        this.firstFreeBlock = 0;
        this.isCompacted = false;
        for (i = 0; i < 128; ++i) {
            this.data[i] = this.initialValue;
        }
        while (i < 192) {
            this.data[i] = this.errorValue;
            ++i;
        }
        for (i = 192; i < 256; ++i) {
            this.data[i] = this.initialValue;
        }
        this.dataNullOffset = 192;
        this.dataLength = 256;
        i = 0;
        for (j = 0; j < 128; j += 32) {
            this.index2[i] = j;
            this.map[i] = 1;
            ++i;
        }
        while (j < 192) {
            this.map[i] = 0;
            ++i;
            j += 32;
        }
        this.map[i++] = 34845;
        j += 32;
        while (j < 256) {
            this.map[i] = 0;
            ++i;
            j += 32;
        }
        for (i = 4; i < 2080; ++i) {
            this.index2[i] = 192;
        }
        for (i = 0; i < 576; ++i) {
            this.index2[2080 + i] = -1;
        }
        for (i = 0; i < 64; ++i) {
            this.index2[2656 + i] = 192;
        }
        this.index2NullOffset = 2656;
        this.index2Length = 2720;
        i = 0;
        j = 0;
        while (i < 32) {
            this.index1[i] = j;
            ++i;
            j += 64;
        }
        while (i < 544) {
            this.index1[i] = 2656;
            ++i;
        }
        for (i = 128; i < 2048; i += 32) {
            this.set(i, this.initialValue);
        }
    }

    public Trie2Writable(Trie2 source) {
        this.init(source.initialValue, source.errorValue);
        for (Trie2.Range r : source) {
            this.setRange(r, true);
        }
    }

    private boolean isInNullBlock(int c, boolean forLSCP) {
        int i2 = Character.isHighSurrogate((char)c) && forLSCP ? 320 + (c >> 5) : this.index1[c >> 11] + (c >> 5 & 0x3F);
        int block = this.index2[i2];
        return block == this.dataNullOffset;
    }

    private int allocIndex2Block() {
        int newBlock = this.index2Length;
        int newTop = newBlock + 64;
        if (newTop > this.index2.length) {
            throw new IllegalStateException("Internal error in Trie2 creation.");
        }
        this.index2Length = newTop;
        System.arraycopy(this.index2, this.index2NullOffset, this.index2, newBlock, 64);
        return newBlock;
    }

    private int getIndex2Block(int c, boolean forLSCP) {
        if (c >= 55296 && c < 56320 && forLSCP) {
            return 2048;
        }
        int i1 = c >> 11;
        int i2 = this.index1[i1];
        if (i2 == this.index2NullOffset) {
            this.index1[i1] = i2 = this.allocIndex2Block();
        }
        return i2;
    }

    private int allocDataBlock(int copyBlock) {
        int newBlock;
        if (this.firstFreeBlock != 0) {
            newBlock = this.firstFreeBlock;
            this.firstFreeBlock = -this.map[newBlock >> 5];
        } else {
            newBlock = this.dataLength;
            int newTop = newBlock + 32;
            if (newTop > this.dataCapacity) {
                int capacity;
                if (this.dataCapacity < 131072) {
                    capacity = 131072;
                } else if (this.dataCapacity < 1115264) {
                    capacity = 1115264;
                } else {
                    throw new IllegalStateException("Internal error in Trie2 creation.");
                }
                int[] newData = new int[capacity];
                System.arraycopy(this.data, 0, newData, 0, this.dataLength);
                this.data = newData;
                this.dataCapacity = capacity;
            }
            this.dataLength = newTop;
        }
        System.arraycopy(this.data, copyBlock, this.data, newBlock, 32);
        this.map[newBlock >> 5] = 0;
        return newBlock;
    }

    private void releaseDataBlock(int block) {
        this.map[block >> 5] = -this.firstFreeBlock;
        this.firstFreeBlock = block;
    }

    private boolean isWritableBlock(int block) {
        return block != this.dataNullOffset && 1 == this.map[block >> 5];
    }

    private void setIndex2Entry(int i2, int block) {
        int n = block >> 5;
        this.map[n] = this.map[n] + 1;
        int oldBlock = this.index2[i2];
        int n2 = oldBlock >> 5;
        this.map[n2] = this.map[n2] - 1;
        if (0 == this.map[n2]) {
            this.releaseDataBlock(oldBlock);
        }
        this.index2[i2] = block;
    }

    private int getDataBlock(int c, boolean forLSCP) {
        int i2 = this.getIndex2Block(c, forLSCP);
        int oldBlock = this.index2[i2 += c >> 5 & 0x3F];
        if (this.isWritableBlock(oldBlock)) {
            return oldBlock;
        }
        int newBlock = this.allocDataBlock(oldBlock);
        this.setIndex2Entry(i2, newBlock);
        return newBlock;
    }

    public Trie2Writable set(int c, int value) {
        if (c < 0 || c > 0x10FFFF) {
            throw new IllegalArgumentException("Invalid code point.");
        }
        this.set(c, true, value);
        this.fHash = 0;
        return this;
    }

    private Trie2Writable set(int c, boolean forLSCP, int value) {
        if (this.isCompacted) {
            this.uncompact();
        }
        int block = this.getDataBlock(c, forLSCP);
        this.data[block + (c & 31)] = value;
        return this;
    }

    private void uncompact() {
        Trie2Writable tempTrie = new Trie2Writable(this);
        this.index1 = tempTrie.index1;
        this.index2 = tempTrie.index2;
        this.data = tempTrie.data;
        this.index2Length = tempTrie.index2Length;
        this.dataCapacity = tempTrie.dataCapacity;
        this.isCompacted = tempTrie.isCompacted;
        this.header = tempTrie.header;
        this.index = tempTrie.index;
        this.data16 = tempTrie.data16;
        this.data32 = tempTrie.data32;
        this.indexLength = tempTrie.indexLength;
        this.dataLength = tempTrie.dataLength;
        this.index2NullOffset = tempTrie.index2NullOffset;
        this.initialValue = tempTrie.initialValue;
        this.errorValue = tempTrie.errorValue;
        this.highStart = tempTrie.highStart;
        this.highValueIndex = tempTrie.highValueIndex;
        this.dataNullOffset = tempTrie.dataNullOffset;
    }

    private void writeBlock(int block, int value) {
        int limit = block + 32;
        while (block < limit) {
            this.data[block++] = value;
        }
    }

    private void fillBlock(int block, int start, int limit, int value, int initialValue, boolean overwrite) {
        int pLimit = block + limit;
        if (overwrite) {
            for (int i = block + start; i < pLimit; ++i) {
                this.data[i] = value;
            }
        } else {
            for (int i = block + start; i < pLimit; ++i) {
                if (this.data[i] != initialValue) continue;
                this.data[i] = value;
            }
        }
    }

    public Trie2Writable setRange(int start, int end, int value, boolean overwrite) {
        int block;
        if (start > 0x10FFFF || start < 0 || end > 0x10FFFF || end < 0 || start > end) {
            throw new IllegalArgumentException("Invalid code point range.");
        }
        if (!overwrite && value == this.initialValue) {
            return this;
        }
        this.fHash = 0;
        if (this.isCompacted) {
            this.uncompact();
        }
        int limit = end + 1;
        if ((start & 0x1F) != 0) {
            block = this.getDataBlock(start, true);
            int nextStart = start + 32 & 0xFFFFFFE0;
            if (nextStart <= limit) {
                this.fillBlock(block, start & 0x1F, 32, value, this.initialValue, overwrite);
                start = nextStart;
            } else {
                this.fillBlock(block, start & 0x1F, limit & 0x1F, value, this.initialValue, overwrite);
                return this;
            }
        }
        int rest = limit & 0x1F;
        int repeatBlock = value == this.initialValue ? this.dataNullOffset : -1;
        while (start < (limit &= 0xFFFFFFE0)) {
            boolean setRepeatBlock = false;
            if (value == this.initialValue && this.isInNullBlock(start, true)) {
                start += 32;
                continue;
            }
            int i2 = this.getIndex2Block(start, true);
            block = this.index2[i2 += start >> 5 & 0x3F];
            if (this.isWritableBlock(block)) {
                if (overwrite && block >= 2176) {
                    setRepeatBlock = true;
                } else {
                    this.fillBlock(block, 0, 32, value, this.initialValue, overwrite);
                }
            } else if (this.data[block] != value && (overwrite || block == this.dataNullOffset)) {
                setRepeatBlock = true;
            }
            if (setRepeatBlock) {
                if (repeatBlock >= 0) {
                    this.setIndex2Entry(i2, repeatBlock);
                } else {
                    repeatBlock = this.getDataBlock(start, true);
                    this.writeBlock(repeatBlock, value);
                }
            }
            start += 32;
        }
        if (rest > 0) {
            block = this.getDataBlock(start, true);
            this.fillBlock(block, 0, rest, value, this.initialValue, overwrite);
        }
        return this;
    }

    public Trie2Writable setRange(Trie2.Range range, boolean overwrite) {
        this.fHash = 0;
        if (range.leadSurrogate) {
            for (int c = range.startCodePoint; c <= range.endCodePoint; ++c) {
                if (!overwrite && this.getFromU16SingleLead((char)c) != this.initialValue) continue;
                this.setForLeadSurrogateCodeUnit((char)c, range.value);
            }
        } else {
            this.setRange(range.startCodePoint, range.endCodePoint, range.value, overwrite);
        }
        return this;
    }

    public Trie2Writable setForLeadSurrogateCodeUnit(char codeUnit, int value) {
        this.fHash = 0;
        this.set(codeUnit, false, value);
        return this;
    }

    public int get(int codePoint) {
        if (codePoint < 0 || codePoint > 0x10FFFF) {
            return this.errorValue;
        }
        return this.get(codePoint, true);
    }

    private int get(int c, boolean fromLSCP) {
        if (c >= this.highStart && (c < 55296 || c >= 56320 || fromLSCP)) {
            return this.data[this.dataLength - 4];
        }
        int i2 = c >= 55296 && c < 56320 && fromLSCP ? 320 + (c >> 5) : this.index1[c >> 11] + (c >> 5 & 0x3F);
        int block = this.index2[i2];
        return this.data[block + (c & 0x1F)];
    }

    public int getFromU16SingleLead(char c) {
        return this.get(c, false);
    }

    private boolean equal_int(int[] a, int s, int t, int length) {
        for (int i = 0; i < length; ++i) {
            if (a[s + i] == a[t + i]) continue;
            return false;
        }
        return true;
    }

    private int findSameIndex2Block(int index2Length, int otherBlock) {
        for (int block = 0; block <= (index2Length -= 64); ++block) {
            if (!this.equal_int(this.index2, block, otherBlock, 64)) continue;
            return block;
        }
        return -1;
    }

    private int findSameDataBlock(int dataLength, int otherBlock, int blockLength) {
        for (int block = 0; block <= (dataLength -= blockLength); block += 4) {
            if (!this.equal_int(this.data, block, otherBlock, blockLength)) continue;
            return block;
        }
        return -1;
    }

    private int findHighStart(int highValue) {
        int prevBlock;
        int prevI2Block;
        if (highValue == this.initialValue) {
            prevI2Block = this.index2NullOffset;
            prevBlock = this.dataNullOffset;
        } else {
            prevI2Block = -1;
            prevBlock = -1;
        }
        int prev = 0x110000;
        int i1 = 544;
        int c = prev;
        while (c > 0) {
            int i2Block;
            if ((i2Block = this.index1[--i1]) == prevI2Block) {
                c -= 2048;
                continue;
            }
            prevI2Block = i2Block;
            if (i2Block == this.index2NullOffset) {
                if (highValue != this.initialValue) {
                    return c;
                }
                c -= 2048;
                continue;
            }
            int i2 = 64;
            while (i2 > 0) {
                int block;
                if ((block = this.index2[i2Block + --i2]) == prevBlock) {
                    c -= 32;
                    continue;
                }
                prevBlock = block;
                if (block == this.dataNullOffset) {
                    if (highValue != this.initialValue) {
                        return c;
                    }
                    c -= 32;
                    continue;
                }
                int j = 32;
                while (j > 0) {
                    int value;
                    if ((value = this.data[block + --j]) != highValue) {
                        return c;
                    }
                    --c;
                }
            }
        }
        return 0;
    }

    private void compactData() {
        int newStart = 192;
        int start = 0;
        int i = 0;
        while (start < newStart) {
            this.map[i] = start;
            start += 32;
            ++i;
        }
        int blockLength = 64;
        int blockCount = blockLength >> 5;
        start = newStart;
        while (start < this.dataLength) {
            int overlap;
            int mapIndex;
            if (start == 2176) {
                blockLength = 32;
                blockCount = 1;
            }
            if (this.map[start >> 5] <= 0) {
                start += blockLength;
                continue;
            }
            int movedStart = this.findSameDataBlock(newStart, start, blockLength);
            if (movedStart >= 0) {
                mapIndex = start >> 5;
                for (i = blockCount; i > 0; --i) {
                    this.map[mapIndex++] = movedStart;
                    movedStart += 32;
                }
                start += blockLength;
                continue;
            }
            for (overlap = blockLength - 4; overlap > 0 && !this.equal_int(this.data, newStart - overlap, start, overlap); overlap -= 4) {
            }
            if (overlap > 0 || newStart < start) {
                movedStart = newStart - overlap;
                mapIndex = start >> 5;
                for (i = blockCount; i > 0; --i) {
                    this.map[mapIndex++] = movedStart;
                    movedStart += 32;
                }
                start += overlap;
                for (i = blockLength - overlap; i > 0; --i) {
                    this.data[newStart++] = this.data[start++];
                }
                continue;
            }
            mapIndex = start >> 5;
            for (i = blockCount; i > 0; --i) {
                this.map[mapIndex++] = start;
                start += 32;
            }
            newStart = start;
        }
        for (i = 0; i < this.index2Length; ++i) {
            if (i == 2080) {
                i += 576;
            }
            this.index2[i] = this.map[this.index2[i] >> 5];
        }
        this.dataNullOffset = this.map[this.dataNullOffset >> 5];
        while ((newStart & 3) != 0) {
            this.data[newStart++] = this.initialValue;
        }
        if (this.UTRIE2_DEBUG) {
            System.out.printf("compacting UTrie2: count of 32-bit data words %d->%d\n", this.dataLength, newStart);
        }
        this.dataLength = newStart;
    }

    private void compactIndex2() {
        int newStart = 2080;
        int start = 0;
        int i = 0;
        while (start < newStart) {
            this.map[i] = start;
            start += 64;
            ++i;
        }
        newStart += 32 + (this.highStart - 65536 >> 11);
        start = 2656;
        while (start < this.index2Length) {
            int overlap;
            int movedStart = this.findSameIndex2Block(newStart, start);
            if (movedStart >= 0) {
                this.map[start >> 6] = movedStart;
                start += 64;
                continue;
            }
            for (overlap = 63; overlap > 0 && !this.equal_int(this.index2, newStart - overlap, start, overlap); --overlap) {
            }
            if (overlap > 0 || newStart < start) {
                this.map[start >> 6] = newStart - overlap;
                start += overlap;
                for (i = 64 - overlap; i > 0; --i) {
                    this.index2[newStart++] = this.index2[start++];
                }
                continue;
            }
            this.map[start >> 6] = start;
            newStart = start += 64;
        }
        for (i = 0; i < 544; ++i) {
            this.index1[i] = this.map[this.index1[i] >> 6];
        }
        this.index2NullOffset = this.map[this.index2NullOffset >> 6];
        while ((newStart & 3) != 0) {
            this.index2[newStart++] = 262140;
        }
        if (this.UTRIE2_DEBUG) {
            System.out.printf("compacting UTrie2: count of 16-bit index-2 words %d->%d\n", this.index2Length, newStart);
        }
        this.index2Length = newStart;
    }

    private void compactTrie() {
        int highValue = this.get(0x10FFFF);
        int localHighStart = this.findHighStart(highValue);
        if ((localHighStart = localHighStart + 2047 & 0xFFFFF800) == 0x110000) {
            highValue = this.errorValue;
        }
        this.highStart = localHighStart;
        if (this.UTRIE2_DEBUG) {
            System.out.printf("UTrie2: highStart U+%04x  highValue 0x%x  initialValue 0x%x\n", this.highStart, highValue, this.initialValue);
        }
        if (this.highStart < 0x110000) {
            int suppHighStart = this.highStart <= 65536 ? 65536 : this.highStart;
            this.setRange(suppHighStart, 0x10FFFF, this.initialValue, true);
        }
        this.compactData();
        if (this.highStart > 65536) {
            this.compactIndex2();
        } else if (this.UTRIE2_DEBUG) {
            System.out.printf("UTrie2: highStart U+%04x  count of 16-bit index-2 words %d->%d\n", this.highStart, this.index2Length, 2112);
        }
        this.data[this.dataLength++] = highValue;
        while ((this.dataLength & 3) != 0) {
            this.data[this.dataLength++] = this.initialValue;
        }
        this.isCompacted = true;
    }

    public Trie2_16 toTrie2_16() {
        Trie2_16 frozenTrie = new Trie2_16();
        this.freeze(frozenTrie, Trie2.ValueWidth.BITS_16);
        return frozenTrie;
    }

    public Trie2_32 toTrie2_32() {
        Trie2_32 frozenTrie = new Trie2_32();
        this.freeze(frozenTrie, Trie2.ValueWidth.BITS_32);
        return frozenTrie;
    }

    private void freeze(Trie2 dest, Trie2.ValueWidth valueBits) {
        int i;
        if (!this.isCompacted) {
            this.compactTrie();
        }
        int allIndexesLength = this.highStart <= 65536 ? 2112 : this.index2Length;
        int dataMove = valueBits == Trie2.ValueWidth.BITS_16 ? allIndexesLength : 0;
        if (allIndexesLength > 65535 || dataMove + this.dataNullOffset > 65535 || dataMove + 2176 > 65535 || dataMove + this.dataLength > 262140) {
            throw new UnsupportedOperationException("Trie2 data is too large.");
        }
        int indexLength = allIndexesLength;
        if (valueBits == Trie2.ValueWidth.BITS_16) {
            indexLength += this.dataLength;
        } else {
            dest.data32 = new int[this.dataLength];
        }
        dest.index = new char[indexLength];
        dest.indexLength = allIndexesLength;
        dest.dataLength = this.dataLength;
        dest.index2NullOffset = this.highStart <= 65536 ? 65535 : 0 + this.index2NullOffset;
        dest.initialValue = this.initialValue;
        dest.errorValue = this.errorValue;
        dest.highStart = this.highStart;
        dest.highValueIndex = dataMove + this.dataLength - 4;
        dest.dataNullOffset = dataMove + this.dataNullOffset;
        dest.header = new Trie2.UTrie2Header();
        dest.header.signature = 1416784178;
        dest.header.options = valueBits == Trie2.ValueWidth.BITS_16 ? 0 : 1;
        dest.header.indexLength = dest.indexLength;
        dest.header.shiftedDataLength = dest.dataLength >> 2;
        dest.header.index2NullOffset = dest.index2NullOffset;
        dest.header.dataNullOffset = dest.dataNullOffset;
        dest.header.shiftedHighStart = dest.highStart >> 11;
        int destIdx = 0;
        for (i = 0; i < 2080; ++i) {
            dest.index[destIdx++] = (char)(this.index2[i] + dataMove >> 2);
        }
        if (this.UTRIE2_DEBUG) {
            System.out.println("\n\nIndex2 for BMP limit is " + Integer.toHexString(destIdx));
        }
        for (i = 0; i < 2; ++i) {
            dest.index[destIdx++] = (char)(dataMove + 128);
        }
        while (i < 32) {
            dest.index[destIdx++] = (char)(dataMove + this.index2[i << 1]);
            ++i;
        }
        if (this.UTRIE2_DEBUG) {
            System.out.println("Index2 for UTF-8 2byte values limit is " + Integer.toHexString(destIdx));
        }
        if (this.highStart > 65536) {
            int index1Length = this.highStart - 65536 >> 11;
            int index2Offset = 2112 + index1Length;
            for (i = 0; i < index1Length; ++i) {
                dest.index[destIdx++] = (char)(0 + this.index1[i + 32]);
            }
            if (this.UTRIE2_DEBUG) {
                System.out.println("Index 1 for supplementals, limit is " + Integer.toHexString(destIdx));
            }
            for (i = 0; i < this.index2Length - index2Offset; ++i) {
                dest.index[destIdx++] = (char)(dataMove + this.index2[index2Offset + i] >> 2);
            }
            if (this.UTRIE2_DEBUG) {
                System.out.println("Index 2 for supplementals, limit is " + Integer.toHexString(destIdx));
            }
        }
        switch (valueBits) {
            case BITS_16: {
                assert (destIdx == dataMove);
                dest.data16 = destIdx;
                for (i = 0; i < this.dataLength; ++i) {
                    dest.index[destIdx++] = (char)this.data[i];
                }
                break;
            }
            case BITS_32: {
                for (i = 0; i < this.dataLength; ++i) {
                    dest.data32[i] = this.data[i];
                }
                break;
            }
        }
    }
}

