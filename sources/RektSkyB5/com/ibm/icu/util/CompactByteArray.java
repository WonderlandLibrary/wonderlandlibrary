/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.ICUCloneNotSupportedException;

@Deprecated
public final class CompactByteArray
implements Cloneable {
    @Deprecated
    public static final int UNICODECOUNT = 65536;
    private static final int BLOCKSHIFT = 7;
    private static final int BLOCKCOUNT = 128;
    private static final int INDEXSHIFT = 9;
    private static final int INDEXCOUNT = 512;
    private static final int BLOCKMASK = 127;
    private byte[] values;
    private char[] indices;
    private int[] hashes;
    private boolean isCompact;
    byte defaultValue;

    @Deprecated
    public CompactByteArray() {
        this(0);
    }

    @Deprecated
    public CompactByteArray(byte defaultValue) {
        int i2;
        this.values = new byte[65536];
        this.indices = new char[512];
        this.hashes = new int[512];
        for (i2 = 0; i2 < 65536; ++i2) {
            this.values[i2] = defaultValue;
        }
        for (i2 = 0; i2 < 512; ++i2) {
            this.indices[i2] = (char)(i2 << 7);
            this.hashes[i2] = 0;
        }
        this.isCompact = false;
        this.defaultValue = defaultValue;
    }

    @Deprecated
    public CompactByteArray(char[] indexArray, byte[] newValues) {
        if (indexArray.length != 512) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        for (int i2 = 0; i2 < 512; ++i2) {
            char index = indexArray[i2];
            if (index < newValues.length + 128) continue;
            throw new IllegalArgumentException("Index out of bounds.");
        }
        this.indices = indexArray;
        this.values = newValues;
        this.isCompact = true;
    }

    @Deprecated
    public CompactByteArray(String indexArray, String valueArray) {
        this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToByteArray(valueArray));
    }

    @Deprecated
    public byte elementAt(char index) {
        return this.values[(this.indices[index >> 7] & 0xFFFF) + (index & 0x7F)];
    }

    @Deprecated
    public void setElementAt(char index, byte value) {
        if (this.isCompact) {
            this.expand();
        }
        this.values[index] = value;
        this.touchBlock(index >> 7, value);
    }

    @Deprecated
    public void setElementAt(char start, char end, byte value) {
        if (this.isCompact) {
            this.expand();
        }
        for (int i2 = start; i2 <= end; ++i2) {
            this.values[i2] = value;
            this.touchBlock(i2 >> 7, value);
        }
    }

    @Deprecated
    public void compact() {
        this.compact(false);
    }

    @Deprecated
    public void compact(boolean exhaustive) {
        if (!this.isCompact) {
            int limitCompacted = 0;
            int iBlockStart = 0;
            int iUntouched = 65535;
            int i2 = 0;
            while (i2 < this.indices.length) {
                this.indices[i2] = 65535;
                boolean touched = this.blockTouched(i2);
                if (!touched && iUntouched != 65535) {
                    this.indices[i2] = iUntouched;
                } else {
                    int jBlockStart = 0;
                    int j2 = 0;
                    j2 = 0;
                    while (j2 < limitCompacted) {
                        if (this.hashes[i2] == this.hashes[j2] && CompactByteArray.arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 128)) {
                            this.indices[i2] = (char)jBlockStart;
                            break;
                        }
                        ++j2;
                        jBlockStart += 128;
                    }
                    if (this.indices[i2] == '\uffff') {
                        System.arraycopy(this.values, iBlockStart, this.values, jBlockStart, 128);
                        this.indices[i2] = (char)jBlockStart;
                        this.hashes[j2] = this.hashes[i2];
                        ++limitCompacted;
                        if (!touched) {
                            iUntouched = (char)jBlockStart;
                        }
                    }
                }
                ++i2;
                iBlockStart += 128;
            }
            int newSize = limitCompacted * 128;
            byte[] result = new byte[newSize];
            System.arraycopy(this.values, 0, result, 0, newSize);
            this.values = result;
            this.isCompact = true;
            this.hashes = null;
        }
    }

    static final boolean arrayRegionMatches(byte[] source, int sourceStart, byte[] target, int targetStart, int len) {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i2 = sourceStart; i2 < sourceEnd; ++i2) {
            if (source[i2] == target[i2 + delta]) continue;
            return false;
        }
        return true;
    }

    private final void touchBlock(int i2, int value) {
        this.hashes[i2] = this.hashes[i2] + (value << 1) | 1;
    }

    private final boolean blockTouched(int i2) {
        return this.hashes[i2] != 0;
    }

    @Deprecated
    public char[] getIndexArray() {
        return this.indices;
    }

    @Deprecated
    public byte[] getValueArray() {
        return this.values;
    }

    @Deprecated
    public Object clone() {
        try {
            CompactByteArray other = (CompactByteArray)super.clone();
            other.values = (byte[])this.values.clone();
            other.indices = (char[])this.indices.clone();
            if (this.hashes != null) {
                other.hashes = (int[])this.hashes.clone();
            }
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    @Deprecated
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CompactByteArray other = (CompactByteArray)obj;
        for (int i2 = 0; i2 < 65536; ++i2) {
            if (this.elementAt((char)i2) == other.elementAt((char)i2)) continue;
            return false;
        }
        return true;
    }

    @Deprecated
    public int hashCode() {
        int result = 0;
        int increment = Math.min(3, this.values.length / 16);
        for (int i2 = 0; i2 < this.values.length; i2 += increment) {
            result = result * 37 + this.values[i2];
        }
        return result;
    }

    private void expand() {
        if (this.isCompact) {
            int i2;
            this.hashes = new int[512];
            byte[] tempArray = new byte[65536];
            for (i2 = 0; i2 < 65536; ++i2) {
                byte value;
                tempArray[i2] = value = this.elementAt((char)i2);
                this.touchBlock(i2 >> 7, value);
            }
            for (i2 = 0; i2 < 512; ++i2) {
                this.indices[i2] = (char)(i2 << 7);
            }
            this.values = null;
            this.values = tempArray;
            this.isCompact = false;
        }
    }
}

