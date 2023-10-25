/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;

public final class CompactByteArray
implements Cloneable {
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

    public CompactByteArray() {
        this(0);
    }

    public CompactByteArray(byte defaultValue) {
        int i;
        this.values = new byte[65536];
        this.indices = new char[512];
        this.hashes = new int[512];
        for (i = 0; i < 65536; ++i) {
            this.values[i] = defaultValue;
        }
        for (i = 0; i < 512; ++i) {
            this.indices[i] = (char)(i << 7);
            this.hashes[i] = 0;
        }
        this.isCompact = false;
        this.defaultValue = defaultValue;
    }

    public CompactByteArray(char[] indexArray, byte[] newValues) {
        if (indexArray.length != 512) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        for (int i = 0; i < 512; ++i) {
            char index = indexArray[i];
            if (index >= '\u0000' && index < newValues.length + 128) continue;
            throw new IllegalArgumentException("Index out of bounds.");
        }
        this.indices = indexArray;
        this.values = newValues;
        this.isCompact = true;
    }

    public CompactByteArray(String indexArray, String valueArray) {
        this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToByteArray(valueArray));
    }

    public byte elementAt(char index) {
        return this.values[(this.indices[index >> 7] & 0xFFFF) + (index & 0x7F)];
    }

    public void setElementAt(char index, byte value) {
        if (this.isCompact) {
            this.expand();
        }
        this.values[index] = value;
        this.touchBlock(index >> 7, value);
    }

    public void setElementAt(char start, char end, byte value) {
        if (this.isCompact) {
            this.expand();
        }
        for (int i = start; i <= end; ++i) {
            this.values[i] = value;
            this.touchBlock(i >> 7, value);
        }
    }

    public void compact() {
        this.compact(false);
    }

    public void compact(boolean exhaustive) {
        if (!this.isCompact) {
            int limitCompacted = 0;
            int iBlockStart = 0;
            int iUntouched = 65535;
            int i = 0;
            while (i < this.indices.length) {
                this.indices[i] = 65535;
                boolean touched = this.blockTouched(i);
                if (!touched && iUntouched != 65535) {
                    this.indices[i] = iUntouched;
                } else {
                    int jBlockStart = 0;
                    int j = 0;
                    j = 0;
                    while (j < limitCompacted) {
                        if (this.hashes[i] == this.hashes[j] && CompactByteArray.arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 128)) {
                            this.indices[i] = (char)jBlockStart;
                            break;
                        }
                        ++j;
                        jBlockStart += 128;
                    }
                    if (this.indices[i] == '\uffff') {
                        System.arraycopy(this.values, iBlockStart, this.values, jBlockStart, 128);
                        this.indices[i] = (char)jBlockStart;
                        this.hashes[j] = this.hashes[i];
                        ++limitCompacted;
                        if (!touched) {
                            iUntouched = (char)jBlockStart;
                        }
                    }
                }
                ++i;
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
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (source[i] == target[i + delta]) continue;
            return false;
        }
        return true;
    }

    private final void touchBlock(int i, int value) {
        this.hashes[i] = this.hashes[i] + (value << 1) | 1;
    }

    private final boolean blockTouched(int i) {
        return this.hashes[i] != 0;
    }

    public char[] getIndexArray() {
        return this.indices;
    }

    public byte[] getValueArray() {
        return this.values;
    }

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
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

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
        for (int i = 0; i < 65536; ++i) {
            if (this.elementAt((char)i) == other.elementAt((char)i)) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 0;
        int increment = Math.min(3, this.values.length / 16);
        for (int i = 0; i < this.values.length; i += increment) {
            result = result * 37 + this.values[i];
        }
        return result;
    }

    private void expand() {
        if (this.isCompact) {
            int i;
            this.hashes = new int[512];
            byte[] tempArray = new byte[65536];
            for (i = 0; i < 65536; ++i) {
                byte value;
                tempArray[i] = value = this.elementAt((char)i);
                this.touchBlock(i >> 7, value);
            }
            for (i = 0; i < 512; ++i) {
                this.indices[i] = (char)(i << 7);
            }
            this.values = null;
            this.values = tempArray;
            this.isCompact = false;
        }
    }
}

