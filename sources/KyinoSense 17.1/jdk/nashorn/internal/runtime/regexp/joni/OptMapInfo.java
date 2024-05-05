/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.MinMaxLen;
import jdk.nashorn.internal.runtime.regexp.joni.OptAnchorInfo;

final class OptMapInfo {
    final MinMaxLen mmd = new MinMaxLen();
    final OptAnchorInfo anchor = new OptAnchorInfo();
    int value;
    final byte[] map = new byte[256];
    private static final int z = 32768;
    static final short[] ByteValTable = new short[]{5, 1, 1, 1, 1, 1, 1, 1, 1, 10, 10, 1, 1, 10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 12, 4, 7, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 5, 5, 5, 5, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 1};

    OptMapInfo() {
    }

    void clear() {
        this.mmd.clear();
        this.anchor.clear();
        this.value = 0;
        for (int i = 0; i < this.map.length; ++i) {
            this.map[i] = 0;
        }
    }

    void copy(OptMapInfo other) {
        this.mmd.copy(other.mmd);
        this.anchor.copy(other.anchor);
        this.value = other.value;
        System.arraycopy(other.map, 0, this.map, 0, other.map.length);
    }

    void addChar(int c) {
        int c_ = c & 0xFF;
        if (this.map[c_] == 0) {
            this.map[c_] = 1;
            this.value += OptMapInfo.positionValue(c_);
        }
    }

    void addCharAmb(char[] chars, int p, int end, int caseFoldFlag) {
        this.addChar(chars[p]);
        char[] items = EncodingHelper.caseFoldCodesByString(caseFoldFlag & 0xBFFFFFFF, chars[p]);
        for (int i = 0; i < items.length; ++i) {
            this.addChar(items[i]);
        }
    }

    void select(OptMapInfo alt) {
        if (alt.value == 0) {
            return;
        }
        if (this.value == 0) {
            this.copy(alt);
            return;
        }
        int v1 = 32768 / this.value;
        int v2 = 32768 / alt.value;
        if (this.mmd.compareDistanceValue(alt.mmd, v1, v2) > 0) {
            this.copy(alt);
        }
    }

    void altMerge(OptMapInfo other) {
        if (this.value == 0) {
            return;
        }
        if (other.value == 0 || this.mmd.max < other.mmd.max) {
            this.clear();
            return;
        }
        this.mmd.altMerge(other.mmd);
        int val = 0;
        for (int i = 0; i < 256; ++i) {
            if (other.map[i] != 0) {
                this.map[i] = 1;
            }
            if (this.map[i] == 0) continue;
            val += OptMapInfo.positionValue(i);
        }
        this.value = val;
        this.anchor.altMerge(other.anchor);
    }

    static int positionValue(int i) {
        if (i < ByteValTable.length) {
            return ByteValTable[i];
        }
        return 4;
    }
}

