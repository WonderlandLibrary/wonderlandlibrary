/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

public final class USerializedSet {
    private char[] array = new char[8];
    private int arrayOffset;
    private int bmpLength;
    private int length;

    public final boolean getSet(char[] src, int srcStart) {
        this.array = null;
        this.length = 0;
        this.bmpLength = 0;
        this.arrayOffset = 0;
        this.length = src[srcStart++];
        if ((this.length & 0x8000) != 0) {
            this.length &= Short.MAX_VALUE;
            if (src.length < srcStart + 1 + this.length) {
                this.length = 0;
                throw new IndexOutOfBoundsException();
            }
            this.bmpLength = src[srcStart++];
        } else {
            if (src.length < srcStart + this.length) {
                this.length = 0;
                throw new IndexOutOfBoundsException();
            }
            this.bmpLength = this.length;
        }
        this.array = new char[this.length];
        System.arraycopy(src, srcStart, this.array, 0, this.length);
        return true;
    }

    public final void setToOne(int c2) {
        if (0x10FFFF < c2) {
            return;
        }
        if (c2 < 65535) {
            this.length = 2;
            this.bmpLength = 2;
            this.array[0] = (char)c2;
            this.array[1] = (char)(c2 + 1);
        } else if (c2 == 65535) {
            this.bmpLength = 1;
            this.length = 3;
            this.array[0] = 65535;
            this.array[1] = '\u0001';
            this.array[2] = '\u0000';
        } else if (c2 < 0x10FFFF) {
            this.bmpLength = 0;
            this.length = 4;
            this.array[0] = (char)(c2 >> 16);
            this.array[1] = (char)c2;
            this.array[2] = (char)(++c2 >> 16);
            this.array[3] = (char)c2;
        } else {
            this.bmpLength = 0;
            this.length = 2;
            this.array[0] = 16;
            this.array[1] = 65535;
        }
    }

    public final boolean getRange(int rangeIndex, int[] range) {
        if (rangeIndex < 0) {
            return false;
        }
        if (this.array == null) {
            this.array = new char[8];
        }
        if (range == null || range.length < 2) {
            throw new IllegalArgumentException();
        }
        if ((rangeIndex *= 2) < this.bmpLength) {
            range[0] = this.array[rangeIndex++];
            range[1] = rangeIndex < this.bmpLength ? this.array[rangeIndex] - '\u0001' : (rangeIndex < this.length ? (this.array[rangeIndex] << 16 | this.array[rangeIndex + 1]) - 1 : 0x10FFFF);
            return true;
        }
        rangeIndex -= this.bmpLength;
        int suppLength = this.length - this.bmpLength;
        if ((rangeIndex *= 2) < suppLength) {
            int offset = this.arrayOffset + this.bmpLength;
            range[0] = this.array[offset + rangeIndex] << 16 | this.array[offset + rangeIndex + 1];
            range[1] = (rangeIndex += 2) < suppLength ? (this.array[offset + rangeIndex] << 16 | this.array[offset + rangeIndex + 1]) - 1 : 0x10FFFF;
            return true;
        }
        return false;
    }

    public final boolean contains(int c2) {
        int i2;
        if (c2 > 0x10FFFF) {
            return false;
        }
        if (c2 <= 65535) {
            int i3;
            for (i3 = 0; i3 < this.bmpLength && (char)c2 >= this.array[i3]; ++i3) {
            }
            return (i3 & 1) != 0;
        }
        char high = (char)(c2 >> 16);
        char low = (char)c2;
        for (i2 = this.bmpLength; i2 < this.length && (high > this.array[i2] || high == this.array[i2] && low >= this.array[i2 + 1]); i2 += 2) {
        }
        return (i2 + this.bmpLength & 2) != 0;
    }

    public final int countRanges() {
        return (this.bmpLength + (this.length - this.bmpLength) / 2 + 1) / 2;
    }
}

