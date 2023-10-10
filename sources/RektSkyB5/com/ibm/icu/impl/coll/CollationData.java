/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Trie2_32;
import com.ibm.icu.impl.coll.Collation;
import com.ibm.icu.impl.coll.UVector32;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ICUException;

public final class CollationData {
    static final int REORDER_RESERVED_BEFORE_LATIN = 4110;
    static final int REORDER_RESERVED_AFTER_LATIN = 4111;
    static final int MAX_NUM_SPECIAL_REORDER_CODES = 8;
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    static final int JAMO_CE32S_LENGTH = 67;
    Trie2_32 trie;
    int[] ce32s;
    long[] ces;
    String contexts;
    public CollationData base;
    int[] jamoCE32s = new int[67];
    public Normalizer2Impl nfcImpl;
    long numericPrimary = 0x12000000L;
    public boolean[] compressibleBytes;
    UnicodeSet unsafeBackwardSet;
    public char[] fastLatinTable;
    char[] fastLatinTableHeader;
    int numScripts;
    char[] scriptsIndex;
    char[] scriptStarts;
    public long[] rootElements;

    CollationData(Normalizer2Impl nfc) {
        this.nfcImpl = nfc;
    }

    public int getCE32(int c2) {
        return this.trie.get(c2);
    }

    int getCE32FromSupplementary(int c2) {
        return this.trie.get(c2);
    }

    boolean isDigit(int c2) {
        return c2 < 1632 ? c2 <= 57 && 48 <= c2 : Collation.hasCE32Tag(this.getCE32(c2), 10);
    }

    public boolean isUnsafeBackward(int c2, boolean numeric) {
        return this.unsafeBackwardSet.contains(c2) || numeric && this.isDigit(c2);
    }

    public boolean isCompressibleLeadByte(int b2) {
        return this.compressibleBytes[b2];
    }

    public boolean isCompressiblePrimary(long p2) {
        return this.isCompressibleLeadByte((int)p2 >>> 24);
    }

    int getCE32FromContexts(int index) {
        return this.contexts.charAt(index) << 16 | this.contexts.charAt(index + 1);
    }

    int getIndirectCE32(int ce32) {
        assert (Collation.isSpecialCE32(ce32));
        int tag = Collation.tagFromCE32(ce32);
        if (tag == 10) {
            ce32 = this.ce32s[Collation.indexFromCE32(ce32)];
        } else if (tag == 13) {
            ce32 = -1;
        } else if (tag == 11) {
            ce32 = this.ce32s[0];
        }
        return ce32;
    }

    int getFinalCE32(int ce32) {
        if (Collation.isSpecialCE32(ce32)) {
            ce32 = this.getIndirectCE32(ce32);
        }
        return ce32;
    }

    long getCEFromOffsetCE32(int c2, int ce32) {
        long dataCE = this.ces[Collation.indexFromCE32(ce32)];
        return Collation.makeCE(Collation.getThreeBytePrimaryForOffsetData(c2, dataCE));
    }

    long getSingleCE(int c2) {
        CollationData d2;
        int ce32 = this.getCE32(c2);
        if (ce32 == 192) {
            d2 = this.base;
            ce32 = this.base.getCE32(c2);
        } else {
            d2 = this;
        }
        while (Collation.isSpecialCE32(ce32)) {
            switch (Collation.tagFromCE32(ce32)) {
                case 4: 
                case 7: 
                case 8: 
                case 9: 
                case 12: 
                case 13: {
                    throw new UnsupportedOperationException(String.format("there is not exactly one collation element for U+%04X (CE32 0x%08x)", c2, ce32));
                }
                case 0: 
                case 3: {
                    throw new AssertionError((Object)String.format("unexpected CE32 tag for U+%04X (CE32 0x%08x)", c2, ce32));
                }
                case 1: {
                    return Collation.ceFromLongPrimaryCE32(ce32);
                }
                case 2: {
                    return Collation.ceFromLongSecondaryCE32(ce32);
                }
                case 5: {
                    if (Collation.lengthFromCE32(ce32) == 1) {
                        ce32 = d2.ce32s[Collation.indexFromCE32(ce32)];
                        break;
                    }
                    throw new UnsupportedOperationException(String.format("there is not exactly one collation element for U+%04X (CE32 0x%08x)", c2, ce32));
                }
                case 6: {
                    if (Collation.lengthFromCE32(ce32) == 1) {
                        return d2.ces[Collation.indexFromCE32(ce32)];
                    }
                    throw new UnsupportedOperationException(String.format("there is not exactly one collation element for U+%04X (CE32 0x%08x)", c2, ce32));
                }
                case 10: {
                    ce32 = d2.ce32s[Collation.indexFromCE32(ce32)];
                    break;
                }
                case 11: {
                    assert (c2 == 0);
                    ce32 = d2.ce32s[0];
                    break;
                }
                case 14: {
                    return d2.getCEFromOffsetCE32(c2, ce32);
                }
                case 15: {
                    return Collation.unassignedCEFromCodePoint(c2);
                }
            }
        }
        return Collation.ceFromSimpleCE32(ce32);
    }

    int getFCD16(int c2) {
        return this.nfcImpl.getFCD16(c2);
    }

    long getFirstPrimaryForGroup(int script) {
        int index = this.getScriptIndex(script);
        return index == 0 ? 0L : (long)this.scriptStarts[index] << 16;
    }

    public long getLastPrimaryForGroup(int script) {
        int index = this.getScriptIndex(script);
        if (index == 0) {
            return 0L;
        }
        long limit = this.scriptStarts[index + 1];
        return (limit << 16) - 1L;
    }

    public int getGroupForPrimary(long p2) {
        int i2;
        if ((p2 >>= 16) < (long)this.scriptStarts[1] || (long)this.scriptStarts[this.scriptStarts.length - 1] <= p2) {
            return -1;
        }
        char index = '\u0001';
        while (p2 >= (long)this.scriptStarts[index + 1]) {
            ++index;
        }
        for (i2 = 0; i2 < this.numScripts; ++i2) {
            if (this.scriptsIndex[i2] != index) continue;
            return i2;
        }
        for (i2 = 0; i2 < 8; ++i2) {
            if (this.scriptsIndex[this.numScripts + i2] != index) continue;
            return 4096 + i2;
        }
        return -1;
    }

    private int getScriptIndex(int script) {
        if (script < 0) {
            return 0;
        }
        if (script < this.numScripts) {
            return this.scriptsIndex[script];
        }
        if (script < 4096) {
            return 0;
        }
        if ((script -= 4096) < 8) {
            return this.scriptsIndex[this.numScripts + script];
        }
        return 0;
    }

    public int[] getEquivalentScripts(int script) {
        int index = this.getScriptIndex(script);
        if (index == 0) {
            return EMPTY_INT_ARRAY;
        }
        if (script >= 4096) {
            return new int[]{script};
        }
        int length = 0;
        for (int i2 = 0; i2 < this.numScripts; ++i2) {
            if (this.scriptsIndex[i2] != index) continue;
            ++length;
        }
        int[] dest = new int[length];
        if (length == 1) {
            dest[0] = script;
            return dest;
        }
        length = 0;
        for (int i3 = 0; i3 < this.numScripts; ++i3) {
            if (this.scriptsIndex[i3] != index) continue;
            dest[length++] = i3;
        }
        return dest;
    }

    void makeReorderRanges(int[] reorder, UVector32 ranges) {
        this.makeReorderRanges(reorder, false, ranges);
    }

    private void makeReorderRanges(int[] reorder, boolean latinMustMove, UVector32 ranges) {
        char index;
        int i2;
        ranges.removeAllElements();
        int length = reorder.length;
        if (length == 0 || length == 1 && reorder[0] == 103) {
            return;
        }
        short[] table = new short[this.scriptStarts.length - 1];
        char index2 = this.scriptsIndex[this.numScripts + 4110 - 4096];
        if (index2 != '\u0000') {
            table[index2] = 255;
        }
        if ((index2 = this.scriptsIndex[this.numScripts + 4111 - 4096]) != '\u0000') {
            table[index2] = 255;
        }
        assert (this.scriptStarts.length >= 2);
        assert (this.scriptStarts[0] == '\u0000');
        int lowStart = this.scriptStarts[1];
        assert (lowStart == 768);
        int highLimit = this.scriptStarts[this.scriptStarts.length - 1];
        assert (highLimit == 65280);
        int specials = 0;
        for (i2 = 0; i2 < length; ++i2) {
            int reorderCode = reorder[i2] - 4096;
            if (0 > reorderCode || reorderCode >= 8) continue;
            specials |= 1 << reorderCode;
        }
        for (i2 = 0; i2 < 8; ++i2) {
            index = this.scriptsIndex[this.numScripts + i2];
            if (index == '\u0000' || (specials & 1 << i2) != 0) continue;
            lowStart = this.addLowScriptRange(table, index, lowStart);
        }
        int skippedReserved = 0;
        if (specials == 0 && reorder[0] == 25 && !latinMustMove) {
            index = this.scriptsIndex[25];
            assert (index != '\u0000');
            int start = this.scriptStarts[index];
            assert (lowStart <= start);
            skippedReserved = start - lowStart;
            lowStart = start;
        }
        boolean hasReorderToEnd = false;
        int i3 = 0;
        while (i3 < length) {
            int index3;
            int script;
            if ((script = reorder[i3++]) == 103) {
                hasReorderToEnd = true;
                while (i3 < length) {
                    if ((script = reorder[--length]) == 103) {
                        throw new IllegalArgumentException("setReorderCodes(): duplicate UScript.UNKNOWN");
                    }
                    if (script == -1) {
                        throw new IllegalArgumentException("setReorderCodes(): UScript.DEFAULT together with other scripts");
                    }
                    index3 = this.getScriptIndex(script);
                    if (index3 == 0) continue;
                    if (table[index3] != 0) {
                        throw new IllegalArgumentException("setReorderCodes(): duplicate or equivalent script " + CollationData.scriptCodeString(script));
                    }
                    highLimit = this.addHighScriptRange(table, index3, highLimit);
                }
                break;
            }
            if (script == -1) {
                throw new IllegalArgumentException("setReorderCodes(): UScript.DEFAULT together with other scripts");
            }
            index3 = this.getScriptIndex(script);
            if (index3 == 0) continue;
            if (table[index3] != 0) {
                throw new IllegalArgumentException("setReorderCodes(): duplicate or equivalent script " + CollationData.scriptCodeString(script));
            }
            lowStart = this.addLowScriptRange(table, index3, lowStart);
        }
        for (i3 = 1; i3 < this.scriptStarts.length - 1; ++i3) {
            short leadByte = table[i3];
            if (leadByte != 0) continue;
            int start = this.scriptStarts[i3];
            if (!hasReorderToEnd && start > lowStart) {
                lowStart = start;
            }
            lowStart = this.addLowScriptRange(table, i3, lowStart);
        }
        if (lowStart > highLimit) {
            if (lowStart - (skippedReserved & 0xFF00) <= highLimit) {
                this.makeReorderRanges(reorder, true, ranges);
                return;
            }
            throw new ICUException("setReorderCodes(): reordering too many partial-primary-lead-byte scripts");
        }
        int offset = 0;
        int i4 = 1;
        while (true) {
            short newLeadByte;
            int nextOffset = offset;
            while (i4 < this.scriptStarts.length - 1 && ((newLeadByte = table[i4]) == 255 || (nextOffset = newLeadByte - (this.scriptStarts[i4] >> 8)) == offset)) {
                ++i4;
            }
            if (offset != 0 || i4 < this.scriptStarts.length - 1) {
                ranges.addElement(this.scriptStarts[i4] << 16 | offset & 0xFFFF);
            }
            if (i4 == this.scriptStarts.length - 1) break;
            offset = nextOffset;
            ++i4;
        }
    }

    private int addLowScriptRange(short[] table, int index, int lowStart) {
        char start = this.scriptStarts[index];
        if ((start & 0xFF) < (lowStart & 0xFF)) {
            lowStart += 256;
        }
        table[index] = (short)(lowStart >> 8);
        char limit = this.scriptStarts[index + 1];
        lowStart = (lowStart & 0xFF00) + ((limit & 0xFF00) - (start & 0xFF00)) | limit & 0xFF;
        return lowStart;
    }

    private int addHighScriptRange(short[] table, int index, int highLimit) {
        char limit = this.scriptStarts[index + 1];
        if ((limit & 0xFF) > (highLimit & 0xFF)) {
            highLimit -= 256;
        }
        char start = this.scriptStarts[index];
        highLimit = (highLimit & 0xFF00) - ((limit & 0xFF00) - (start & 0xFF00)) | start & 0xFF;
        table[index] = (short)(highLimit >> 8);
        return highLimit;
    }

    private static String scriptCodeString(int script) {
        return script < 4096 ? Integer.toString(script) : "0x" + Integer.toHexString(script);
    }
}

