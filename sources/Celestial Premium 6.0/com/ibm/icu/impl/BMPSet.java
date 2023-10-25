/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.text.UnicodeSet;

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
        for (int i = 1; i <= 16; ++i) {
            this.list4kStarts[i] = this.findCodePoint(i << 12, this.list4kStarts[i - 1], this.listLength - 1);
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

    public boolean contains(int c) {
        if (c <= 255) {
            return this.latin1Contains[c];
        }
        if (c <= 2047) {
            return (this.table7FF[c & 0x3F] & 1 << (c >> 6)) != 0;
        }
        if (c < 55296 || c >= 57344 && c <= 65535) {
            int lead = c >> 12;
            int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
            if (twoBits <= 1) {
                return 0 != twoBits;
            }
            return this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1]);
        }
        if (c <= 0x10FFFF) {
            return this.containsSlow(c, this.list4kStarts[13], this.list4kStarts[17]);
        }
        return false;
    }

    public final int span(CharSequence s, int start, int end, UnicodeSet.SpanCondition spanCondition) {
        int i;
        int limit = Math.min(s.length(), end);
        if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
            for (i = start; i < limit; ++i) {
                char c2;
                char c = s.charAt(i);
                if (c <= '\u00ff') {
                    if (this.latin1Contains[c]) continue;
                } else if (c <= '\u07ff') {
                    if ((this.table7FF[c & 0x3F] & 1 << (c >> 6)) != 0) continue;
                } else if (c < '\ud800' || c >= '\udc00' || i + 1 == limit || (c2 = s.charAt(i + 1)) < '\udc00' || c2 >= '\ue000') {
                    int lead = c >> 12;
                    int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                    if (!(twoBits <= 1 ? twoBits == 0 : !this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1]))) continue;
                } else {
                    int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
                    if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) {
                        ++i;
                        continue;
                    }
                }
                break;
            }
        } else {
            while (i < limit) {
                char c2;
                char c = s.charAt(i);
                if (c <= '\u00ff') {
                    if (this.latin1Contains[c]) {
                        break;
                    }
                } else if (c <= '\u07ff') {
                    if ((this.table7FF[c & 0x3F] & 1 << (c >> 6)) != 0) {
                        break;
                    }
                } else if (c < '\ud800' || c >= '\udc00' || i + 1 == limit || (c2 = s.charAt(i + 1)) < '\udc00' || c2 >= '\ue000') {
                    int lead = c >> 12;
                    int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                    if (twoBits <= 1 ? twoBits != 0 : this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1])) {
                        break;
                    }
                } else {
                    int supplementary = UCharacterProperty.getRawSupplementary(c, c2);
                    if (this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) break;
                    ++i;
                }
                ++i;
            }
        }
        return i - start;
    }

    public final int spanBack(CharSequence s, int limit, UnicodeSet.SpanCondition spanCondition) {
        block10: {
            limit = Math.min(s.length(), limit);
            if (UnicodeSet.SpanCondition.NOT_CONTAINED != spanCondition) {
                do {
                    char c2;
                    char c;
                    if ((c = s.charAt(--limit)) <= '\u00ff') {
                        if (this.latin1Contains[c]) continue;
                        break block10;
                    }
                    if (c <= '\u07ff') {
                        if ((this.table7FF[c & 0x3F] & 1 << (c >> 6)) != 0) continue;
                        break block10;
                    }
                    if (c < '\ud800' || c < '\udc00' || 0 == limit || (c2 = s.charAt(limit - 1)) < '\ud800' || c2 >= '\udc00') {
                        int lead = c >> 12;
                        int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                        if (!(twoBits <= 1 ? twoBits == 0 : !this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1]))) continue;
                        break block10;
                    }
                    int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
                    if (!this.containsSlow(supplementary, this.list4kStarts[16], this.list4kStarts[17])) break block10;
                    --limit;
                } while (0 != limit);
                return 0;
            }
            do {
                char c2;
                char c;
                if ((c = s.charAt(--limit)) <= '\u00ff') {
                    if (!this.latin1Contains[c]) continue;
                    break block10;
                }
                if (c <= '\u07ff') {
                    if ((this.table7FF[c & 0x3F] & 1 << (c >> 6)) == 0) continue;
                    break block10;
                }
                if (c < '\ud800' || c < '\udc00' || 0 == limit || (c2 = s.charAt(limit - 1)) < '\ud800' || c2 >= '\udc00') {
                    int lead = c >> 12;
                    int twoBits = this.bmpBlockBits[c >> 6 & 0x3F] >> lead & 0x10001;
                    if (!(twoBits <= 1 ? twoBits != 0 : this.containsSlow(c, this.list4kStarts[lead], this.list4kStarts[lead + 1]))) continue;
                    break block10;
                }
                int supplementary = UCharacterProperty.getRawSupplementary(c2, c);
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
            int n = trail;
            table[n] = table[n] | bits;
            return;
        }
        int limitLead = limit >> 6;
        int limitTrail = limit & 0x3F;
        if (lead == limitLead) {
            while (trail < limitTrail) {
                int n = trail++;
                table[n] = table[n] | bits;
            }
        } else {
            if (trail > 0) {
                do {
                    int n = trail++;
                    table[n] = table[n] | bits;
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
                    int n = trail++;
                    table[n] = table[n] | bits;
                }
            }
            bits = 1 << limitLead;
            trail = 0;
            while (trail < limitTrail) {
                int n = trail++;
                table[n] = table[n] | bits;
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
                    int n = (start >>= 6) & 0x3F;
                    this.bmpBlockBits[n] = this.bmpBlockBits[n] | 65537 << (start >> 6);
                    minStart = start = start + 1 << 6;
                }
                if (start < limit) {
                    if (start < (limit & 0xFFFFFFC0)) {
                        BMPSet.set32x64Bits(this.bmpBlockBits, start >> 6, limit >> 6);
                    }
                    if (0 != (limit & 0x3F)) {
                        int n = (limit >>= 6) & 0x3F;
                        this.bmpBlockBits[n] = this.bmpBlockBits[n] | 65537 << (limit >> 6);
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

    private int findCodePoint(int c, int lo, int hi) {
        int i;
        if (c < this.list[lo]) {
            return lo;
        }
        if (lo >= hi || c >= this.list[hi - 1]) {
            return hi;
        }
        while ((i = lo + hi >>> 1) != lo) {
            if (c < this.list[i]) {
                hi = i;
                continue;
            }
            lo = i;
        }
        return hi;
    }

    private final boolean containsSlow(int c, int lo, int hi) {
        return 0 != (this.findCodePoint(c, lo, hi) & 1);
    }
}

