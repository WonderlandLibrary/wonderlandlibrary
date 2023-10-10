/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.SharedObject;
import com.ibm.icu.impl.coll.UVector32;
import java.util.Arrays;

public final class CollationSettings
extends SharedObject {
    public static final int CHECK_FCD = 1;
    public static final int NUMERIC = 2;
    static final int SHIFTED = 4;
    static final int ALTERNATE_MASK = 12;
    static final int MAX_VARIABLE_SHIFT = 4;
    static final int MAX_VARIABLE_MASK = 112;
    static final int UPPER_FIRST = 256;
    public static final int CASE_FIRST = 512;
    public static final int CASE_FIRST_AND_UPPER_MASK = 768;
    public static final int CASE_LEVEL = 1024;
    public static final int BACKWARD_SECONDARY = 2048;
    static final int STRENGTH_SHIFT = 12;
    static final int STRENGTH_MASK = 61440;
    static final int MAX_VAR_SPACE = 0;
    static final int MAX_VAR_PUNCT = 1;
    static final int MAX_VAR_SYMBOL = 2;
    static final int MAX_VAR_CURRENCY = 3;
    public int options = 8208;
    public long variableTop;
    public byte[] reorderTable;
    long minHighNoReorder;
    long[] reorderRanges;
    public int[] reorderCodes = EMPTY_INT_ARRAY;
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    public int fastLatinOptions = -1;
    public char[] fastLatinPrimaries = new char[384];

    CollationSettings() {
    }

    @Override
    public CollationSettings clone() {
        CollationSettings newSettings = (CollationSettings)super.clone();
        newSettings.fastLatinPrimaries = (char[])this.fastLatinPrimaries.clone();
        return newSettings;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        CollationSettings o2 = (CollationSettings)other;
        if (this.options != o2.options) {
            return false;
        }
        if ((this.options & 0xC) != 0 && this.variableTop != o2.variableTop) {
            return false;
        }
        return Arrays.equals(this.reorderCodes, o2.reorderCodes);
    }

    public int hashCode() {
        int h2 = this.options << 8;
        if ((this.options & 0xC) != 0) {
            h2 = (int)((long)h2 ^ this.variableTop);
        }
        h2 ^= this.reorderCodes.length;
        for (int i2 = 0; i2 < this.reorderCodes.length; ++i2) {
            h2 ^= this.reorderCodes[i2] << i2;
        }
        return h2;
    }

    public void resetReordering() {
        this.reorderTable = null;
        this.minHighNoReorder = 0L;
        this.reorderRanges = null;
        this.reorderCodes = EMPTY_INT_ARRAY;
    }

    void aliasReordering(CollationData data, int[] codesAndRanges, int codesLength, byte[] table) {
        int[] codes;
        if (codesLength == codesAndRanges.length) {
            codes = codesAndRanges;
        } else {
            codes = new int[codesLength];
            System.arraycopy(codesAndRanges, 0, codes, 0, codesLength);
        }
        int rangesStart = codesLength;
        int rangesLimit = codesAndRanges.length;
        int rangesLength = rangesLimit - rangesStart;
        if (table != null && (rangesLength == 0 ? !CollationSettings.reorderTableHasSplitBytes(table) : rangesLength >= 2 && (codesAndRanges[rangesStart] & 0xFFFF) == 0 && (codesAndRanges[rangesLimit - 1] & 0xFFFF) != 0)) {
            int firstSplitByteRangeIndex;
            this.reorderTable = table;
            this.reorderCodes = codes;
            for (firstSplitByteRangeIndex = rangesStart; firstSplitByteRangeIndex < rangesLimit && (codesAndRanges[firstSplitByteRangeIndex] & 0xFF0000) == 0; ++firstSplitByteRangeIndex) {
            }
            if (firstSplitByteRangeIndex == rangesLimit) {
                assert (!CollationSettings.reorderTableHasSplitBytes(table));
                this.minHighNoReorder = 0L;
                this.reorderRanges = null;
            } else {
                assert (table[codesAndRanges[firstSplitByteRangeIndex] >>> 24] == 0);
                this.minHighNoReorder = (long)codesAndRanges[rangesLimit - 1] & 0xFFFF0000L;
                this.setReorderRanges(codesAndRanges, firstSplitByteRangeIndex, rangesLimit - firstSplitByteRangeIndex);
            }
            return;
        }
        this.setReordering(data, codes);
    }

    public void setReordering(CollationData data, int[] codes) {
        int rangesStart;
        if (codes.length == 0 || codes.length == 1 && codes[0] == 103) {
            this.resetReordering();
            return;
        }
        UVector32 rangesList = new UVector32();
        data.makeReorderRanges(codes, rangesList);
        int rangesLength = rangesList.size();
        if (rangesLength == 0) {
            this.resetReordering();
            return;
        }
        int[] ranges = rangesList.getBuffer();
        assert (rangesLength >= 2);
        assert ((ranges[0] & 0xFFFF) == 0 && (ranges[rangesLength - 1] & 0xFFFF) != 0);
        this.minHighNoReorder = (long)ranges[rangesLength - 1] & 0xFFFF0000L;
        byte[] table = new byte[256];
        int b2 = 0;
        int firstSplitByteRangeIndex = -1;
        for (int i2 = 0; i2 < rangesLength; ++i2) {
            int pair = ranges[i2];
            int limit1 = pair >>> 24;
            while (b2 < limit1) {
                table[b2] = (byte)(b2 + pair);
                ++b2;
            }
            if ((pair & 0xFF0000) == 0) continue;
            table[limit1] = 0;
            b2 = limit1 + 1;
            if (firstSplitByteRangeIndex >= 0) continue;
            firstSplitByteRangeIndex = i2;
        }
        while (b2 <= 255) {
            table[b2] = (byte)b2;
            ++b2;
        }
        if (firstSplitByteRangeIndex < 0) {
            rangesLength = 0;
            rangesStart = 0;
        } else {
            rangesStart = firstSplitByteRangeIndex;
            rangesLength -= firstSplitByteRangeIndex;
        }
        this.setReorderArrays(codes, ranges, rangesStart, rangesLength, table);
    }

    private void setReorderArrays(int[] codes, int[] ranges, int rangesStart, int rangesLength, byte[] table) {
        if (codes == null) {
            codes = EMPTY_INT_ARRAY;
        }
        assert (codes.length == 0 == (table == null));
        this.reorderTable = table;
        this.reorderCodes = codes;
        this.setReorderRanges(ranges, rangesStart, rangesLength);
    }

    private void setReorderRanges(int[] ranges, int rangesStart, int rangesLength) {
        if (rangesLength == 0) {
            this.reorderRanges = null;
        } else {
            this.reorderRanges = new long[rangesLength];
            int i2 = 0;
            do {
                this.reorderRanges[i2++] = (long)ranges[rangesStart++] & 0xFFFFFFFFL;
            } while (i2 < rangesLength);
        }
    }

    public void copyReorderingFrom(CollationSettings other) {
        if (!other.hasReordering()) {
            this.resetReordering();
            return;
        }
        this.minHighNoReorder = other.minHighNoReorder;
        this.reorderTable = other.reorderTable;
        this.reorderRanges = other.reorderRanges;
        this.reorderCodes = other.reorderCodes;
    }

    public boolean hasReordering() {
        return this.reorderTable != null;
    }

    private static boolean reorderTableHasSplitBytes(byte[] table) {
        assert (table[0] == 0);
        for (int i2 = 1; i2 < 256; ++i2) {
            if (table[i2] != 0) continue;
            return true;
        }
        return false;
    }

    public long reorder(long p2) {
        byte b2 = this.reorderTable[(int)p2 >>> 24];
        if (b2 != 0 || p2 <= 1L) {
            return ((long)b2 & 0xFFL) << 24 | p2 & 0xFFFFFFL;
        }
        return this.reorderEx(p2);
    }

    private long reorderEx(long p2) {
        long r2;
        assert (this.minHighNoReorder > 0L);
        if (p2 >= this.minHighNoReorder) {
            return p2;
        }
        long q2 = p2 | 0xFFFFL;
        int i2 = 0;
        while (q2 >= (r2 = this.reorderRanges[i2])) {
            ++i2;
        }
        return p2 + ((long)((short)r2) << 24);
    }

    public void setStrength(int value) {
        int noStrength = this.options & 0xFFFF0FFF;
        switch (value) {
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 15: {
                this.options = noStrength | value << 12;
                break;
            }
            default: {
                throw new IllegalArgumentException("illegal strength value " + value);
            }
        }
    }

    public void setStrengthDefault(int defaultOptions) {
        int noStrength = this.options & 0xFFFF0FFF;
        this.options = noStrength | defaultOptions & 0xF000;
    }

    static int getStrength(int options) {
        return options >> 12;
    }

    public int getStrength() {
        return CollationSettings.getStrength(this.options);
    }

    public void setFlag(int bit, boolean value) {
        this.options = value ? (this.options |= bit) : (this.options &= ~bit);
    }

    public void setFlagDefault(int bit, int defaultOptions) {
        this.options = this.options & ~bit | defaultOptions & bit;
    }

    public boolean getFlag(int bit) {
        return (this.options & bit) != 0;
    }

    public void setCaseFirst(int value) {
        assert (value == 0 || value == 512 || value == 768);
        int noCaseFirst = this.options & 0xFFFFFCFF;
        this.options = noCaseFirst | value;
    }

    public void setCaseFirstDefault(int defaultOptions) {
        int noCaseFirst = this.options & 0xFFFFFCFF;
        this.options = noCaseFirst | defaultOptions & 0x300;
    }

    public int getCaseFirst() {
        return this.options & 0x300;
    }

    public void setAlternateHandlingShifted(boolean value) {
        int noAlternate = this.options & 0xFFFFFFF3;
        this.options = value ? noAlternate | 4 : noAlternate;
    }

    public void setAlternateHandlingDefault(int defaultOptions) {
        int noAlternate = this.options & 0xFFFFFFF3;
        this.options = noAlternate | defaultOptions & 0xC;
    }

    public boolean getAlternateHandling() {
        return (this.options & 0xC) != 0;
    }

    public void setMaxVariable(int value, int defaultOptions) {
        int noMax = this.options & 0xFFFFFF8F;
        switch (value) {
            case 0: 
            case 1: 
            case 2: 
            case 3: {
                this.options = noMax | value << 4;
                break;
            }
            case -1: {
                this.options = noMax | defaultOptions & 0x70;
                break;
            }
            default: {
                throw new IllegalArgumentException("illegal maxVariable value " + value);
            }
        }
    }

    public int getMaxVariable() {
        return (this.options & 0x70) >> 4;
    }

    static boolean isTertiaryWithCaseBits(int options) {
        return (options & 0x600) == 512;
    }

    static int getTertiaryMask(int options) {
        return CollationSettings.isTertiaryWithCaseBits(options) ? 65343 : 16191;
    }

    static boolean sortsTertiaryUpperCaseFirst(int options) {
        return (options & 0x700) == 768;
    }

    public boolean dontCheckFCD() {
        return (this.options & 1) == 0;
    }

    boolean hasBackwardSecondary() {
        return (this.options & 0x800) != 0;
    }

    public boolean isNumeric() {
        return (this.options & 2) != 0;
    }
}

