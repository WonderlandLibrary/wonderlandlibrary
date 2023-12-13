/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.OutputInt;

public final class BMPSet {
    public static int U16_SURROGATE_OFFSET = 56613888;
    private boolean[] latin1Contains;
    private int[] table7FF;
    private int[] bmpBlockBits;
    private int[] list4kStarts;
    private final int[] list;
    private final int listLength;

    public BMPSet(int[] parentList, int parentListLength) {
        this.list = parentList;
        this.listLength = parentListLength;
        this.latin1Contains = new boolean[256];
        this.table7FF = new int[64];
        this.bmpBlockBits = new int[64];
        this.list4kStarts = new int[18];
        this.list4kStarts[0] = this.findCodePoint(2048, 0, this.listLength - 1);
        for (int i2 = 1; i2 <= 16; ++i2) {
            this.list4kStarts[i2] = this.findCodePoint(i2 << 12, this.list4kStarts[i2 - 1], this.listLength - 1);
        }
        this.list4kStarts[17] = this.listLength - 1;
        this.initBits();
    }

    public BMPSet(BMPSet otherBMPSet, int[] newParentList, int newParentListLength) {
        this.list = newParentList;
        this.listLength = newParentListLength;
        this.latin1Contains = (boolean[])otherBMPSet.latin1Contains.clone();
        this.table7FF = (int[])otherBMPSet.table7FF.clone();
        this.bmpBlockBits = (int[])otherBMPSet.bmpBlockBits.clone();
        this.list4kStarts = (int[])otherBMPSet.list4kStarts.clone();
    }

    public boolean contains(int c2) {
        if (c2 <= 255) {
            return this.latin1Contains[c2];
        }
        if (c2 <= 2047) {
            return (this.table7FF[c2 & 0x3F] & 1 << (c2 >> 6)) != 0;
        }
        if (c2 < 55296 || c2 >= 57344 && c2 <= 65535) {
            int lead = c2 >> 12;
            int twoBits = this.bmpBlockBits[c2 >> 6 & 0x3F] >> lead & 0x10001;
            if (twoBits <= 1) {
                return 0 != twoBits;
            }
            return this.containsSlow(c2, this.list4kStarts[lead], this.list4kStarts[lead + 1]);
        }
        if (c2 <= 0x10FFFF) {
            return this.containsSlow(c2, this.list4kStarts[13], this.list4kStarts[17]);
        }
        return false;
    }

    public final int span(CharSequence s2, int start, UnicodeSet.SpanCondition spanCondition, OutputInt outCount) {
        int supplementary;
        int lead;
        int i2;
        int limit = s2.length();
        int numSupplementary = 0;
        if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
            for (i2 = start; i2 < limit; ++i2) {
                char c2;
                char c3 = s2.charAt(i2);
                if (c3 <= '\u00ff') {
                    if (this.latin1Contains[c3]) continue;
                } else if (c3 <= '\u07ff') {
                    if ((this.table7FF[c3 & 0x3F] & 1 << (c3 >> 6)) != 0) continue;
                } else if (c3 < '\ud800' || c3 >= '\udc00' || i2 + 1 == limit || (c2 = s2.charAt(i2 + 1)) < '\udc00' || c2 >= '\ue000') {
                    lead = c3 >> 12;
                    int twoBits = this.bmpBlockBits[c3 >> 6 & 0x3F] >> lead & 0x10001;
                    if (!(twoBits <= 1 ? twoBits == 0 : !this.containsSlow(c3, this.list4kStarts[lead], this.list4kStarts[lead + 1]))) continue;
                } else {
                    supplementary = Character.toCodePoint(c3, c2);
                    if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
                        ++numSupplementary;
                        ++i2;
                        continue;
                    }
                }
                break;
            }
        } else {
            while (i2 < limit) {
                char c2;
                char c4 = s2.charAt(i2);
                if (c4 <= '\u00ff') {
                    if (this.latin1Contains[c4]) {
                        break;
                    }
                } else if (c4 <= '\u07ff') {
                    if ((this.table7FF[c4 & 0x3F] & 1 << (c4 >> 6)) != 0) {
                        break;
                    }
                } else if (c4 < '\ud800' || c4 >= '\udc00' || i2 + 1 == limit || (c2 = s2.charAt(i2 + 1)) < '\udc00' || c2 >= '\ue000') {
                    lead = c4 >> 12;
                    int twoBits = this.bmpBlockBits[c4 >> 6 & 0x3F] >> lead & 0x10001;
                    if (twoBits <= 1 ? twoBits != 0 : this.containsSlow(c4, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
                        break;
                    }
                } else {
                    supplementary = Character.toCodePoint(c4, c2);
                    if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) break;
                    ++numSupplementary;
                    ++i2;
                }
                ++i2;
            }
        }
        if (outCount != null) {
            int spanLength = i2 - start;
            outCount.value = spanLength - numSupplementary;
        }
        return i2;
    }

    public final int spanBack(CharSequence s2, int limit, UnicodeSet.SpanCondition spanCondition) {
        block10: {
            if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
                do {
                    char c2;
                    char c3;
                    if ((c3 = s2.charAt(--limit)) <= '\u00ff') {
                        if (this.latin1Contains[c3]) continue;
                        break block10;
                    }
                    if (c3 <= '\u07ff') {
                        if ((this.table7FF[c3 & 0x3F] & 1 << (c3 >> 6)) != 0) continue;
                        break block10;
                    }
                    if (c3 < '\ud800' || c3 < '\udc00' || 0 == limit || (c2 = s2.charAt(limit - 1)) < '\ud800' || c2 >= '\udc00') {
                        int lead = c3 >> 12;
                        int twoBits = this.bmpBlockBits[c3 >> 6 & 0x3F] >> lead & 0x10001;
                        if (!(twoBits <= 1 ? twoBits == 0 : !this.containsSlow(c3, this.list4kStarts[lead], this.list4kStarts[lead + 1]))) continue;
                        break block10;
                    }
                    int supplementary = Character.toCodePoint(c2, c3);
                    if (!this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) break block10;
                    --limit;
                } while (0 != limit);
                return 0;
            }
            do {
                char c2;
                char c4;
                if ((c4 = s2.charAt(--limit)) <= '\u00ff') {
                    if (!this.latin1Contains[c4]) continue;
                    break block10;
                }
                if (c4 <= '\u07ff') {
                    if ((this.table7FF[c4 & 0x3F] & 1 << (c4 >> 6)) == 0) continue;
                    break block10;
                }
                if (c4 < '\ud800' || c4 < '\udc00' || 0 == limit || (c2 = s2.charAt(limit - 1)) < '\ud800' || c2 >= '\udc00') {
                    int lead = c4 >> 12;
                    int twoBits = this.bmpBlockBits[c4 >> 6 & 0x3F] >> lead & 0x10001;
                    if (!(twoBits <= 1 ? twoBits != 0 : this.containsSlow(c4, this.list4kStarts[lead], this.list4kStarts[lead + 1]))) continue;
                    break block10;
                }
                int supplementary = Character.toCodePoint(c2, c4);
                if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) break block10;
                --limit;
            } while (0 != limit);
            return 0;
        }
        return limit + 1;
    }

    private static void set32x64Bits(int[] table, int start, int limit) {
        assert (64 == table.length);
        int lead = start >> 6;
        int trail = start & 0x3F;
        int bits = 1 << lead;
        if (start + 1 == limit) {
            int n2 = trail;
            table[n2] = table[n2] | bits;
            return;
        }
        int limitLead = limit >> 6;
        int limitTrail = limit & 0x3F;
        if (lead == limitLead) {
            while (trail < limitTrail) {
                int n3 = trail++;
                table[n3] = table[n3] | bits;
            }
        } else {
            if (trail > 0) {
                do {
                    int n4 = trail++;
                    table[n4] = table[n4] | bits;
                } while (trail < 64);
                ++lead;
            }
            if (lead < limitLead) {
                bits = ~((1 << lead) - 1);
                if (limitLead < 32) {
                    bits &= (1 << limitLead) - 1;
                }
                trail = 0;
                while (trail < 64) {
                    int n5 = trail++;
                    table[n5] = table[n5] | bits;
                }
            }
            bits = 1 << limitLead;
            trail = 0;
            while (trail < limitTrail) {
                int n6 = trail++;
                table[n6] = table[n6] | bits;
            }
        }
    }

    private void initBits() {
        int start;
        int limit;
        int listIndex = 0;
        do {
            start = this.list[listIndex++];
            limit = listIndex < this.listLength ? this.list[listIndex++] : 0x110000;
            if (start >= 256) break;
            do {
                this.latin1Contains[start++] = true;
            } while (start < limit && start < 256);
        } while (limit <= 256);
        while (start < 2048) {
            BMPSet.set32x64Bits(this.table7FF, start, limit <= 2048 ? limit : 2048);
            if (limit > 2048) {
                start = 2048;
                break;
            }
            start = this.list[listIndex++];
            if (listIndex < this.listLength) {
                limit = this.list[listIndex++];
                continue;
            }
            limit = 0x110000;
        }
        int minStart = 2048;
        while (start < 65536) {
            if (limit > 65536) {
                limit = 65536;
            }
            if (start < minStart) {
                start = minStart;
            }
            if (start < limit) {
                if (0 != (start & 0x3F)) {
                    int n2 = (start >>= 6) & 0x3F;
                    this.bmpBlockBits[n2] = this.bmpBlockBits[n2] | 65537 << (start >> 6);
                    minStart = start = start + 1 << 6;
                }
                if (start < limit) {
                    if (start < (limit & 0xFFFFFFC0)) {
                        BMPSet.set32x64Bits(this.bmpBlockBits, start >> 6, limit >> 6);
                    }
                    if (0 != (limit & 0x3F)) {
                        int n3 = (limit >>= 6) & 0x3F;
                        this.bmpBlockBits[n3] = this.bmpBlockBits[n3] | 65537 << (limit >> 6);
                        minStart = limit = limit + 1 << 6;
                    }
                }
            }
            if (limit == 65536) break;
            start = this.list[listIndex++];
            if (listIndex < this.listLength) {
                limit = this.list[listIndex++];
                continue;
            }
            limit = 0x110000;
        }
    }

    private int findCodePoint(int c2, int lo, int hi) {
        int i2;
        if (c2 < this.list[lo]) {
            return lo;
        }
        if (lo >= hi || c2 >= this.list[hi - 1]) {
            return hi;
        }
        while ((i2 = lo + hi >>> 1) != lo) {
            if (c2 < this.list[i2]) {
                hi = i2;
                continue;
            }
            lo = i2;
        }
        return hi;
    }

    private final boolean containsSlow(int c2, int lo, int hi) {
        return 0 != (this.findCodePoint(c2, lo, hi) & 1);
    }
}

