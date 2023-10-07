/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.util.Arrays;

public final class BitVector
implements Cloneable {
    private static final int BITSPERSLOT = 64;
    private static final int SLOTSQUANTA = 4;
    private static final int BITSHIFT = 6;
    private static final int BITMASK = 63;
    private long[] bits;

    public BitVector() {
        this.bits = new long[4];
    }

    public BitVector(long length) {
        int need = (int)BitVector.growthNeeded(length);
        this.bits = new long[need];
    }

    public BitVector(long[] bits) {
        this.bits = (long[])bits.clone();
    }

    public void copy(BitVector other) {
        this.bits = (long[])other.bits.clone();
    }

    private static long slotsNeeded(long length) {
        return length + 63L >> 6;
    }

    private static long growthNeeded(long length) {
        return (BitVector.slotsNeeded(length) + 4L - 1L) / 4L * 4L;
    }

    private long slot(int index) {
        return 0 <= index && index < this.bits.length ? this.bits[index] : 0L;
    }

    public void resize(long length) {
        int need = (int)BitVector.growthNeeded(length);
        if (this.bits.length != need) {
            this.bits = Arrays.copyOf(this.bits, need);
        }
        int shift = (int)(length & 0x3FL);
        int slot = (int)(length >> 6);
        if (shift != 0) {
            int n = slot++;
            this.bits[n] = this.bits[n] & (1L << shift) - 1L;
        }
        while (slot < this.bits.length) {
            this.bits[slot] = 0L;
            ++slot;
        }
    }

    public void set(long bit) {
        int n = (int)(bit >> 6);
        this.bits[n] = this.bits[n] | 1L << (int)(bit & 0x3FL);
    }

    public void clear(long bit) {
        int n = (int)(bit >> 6);
        this.bits[n] = this.bits[n] & (1L << (int)(bit & 0x3FL) ^ 0xFFFFFFFFFFFFFFFFL);
    }

    public void toggle(long bit) {
        int n = (int)(bit >> 6);
        this.bits[n] = this.bits[n] ^ 1L << (int)(bit & 0x3FL);
    }

    public void setTo(long length) {
        if (0L < length) {
            int lastWord = (int)(length >> 6);
            long lastBits = (1L << (int)(length & 0x3FL)) - 1L;
            Arrays.fill(this.bits, 0, lastWord, -1L);
            if (lastBits != 0L) {
                int n = lastWord;
                this.bits[n] = this.bits[n] | lastBits;
            }
        }
    }

    public void clearAll() {
        Arrays.fill(this.bits, 0L);
    }

    public boolean isSet(long bit) {
        return (this.bits[(int)(bit >> 6)] & 1L << (int)(bit & 0x3FL)) != 0L;
    }

    public boolean isClear(long bit) {
        return (this.bits[(int)(bit >> 6)] & 1L << (int)(bit & 0x3FL)) == 0L;
    }

    public void shiftLeft(long shift, long length) {
        if (shift != 0L) {
            int leftShift = (int)(shift & 0x3FL);
            int rightShift = 64 - leftShift;
            int slotShift = (int)(shift >> 6);
            int slotCount = this.bits.length - slotShift;
            if (leftShift == 0) {
                int slot = 0;
                int from = slotShift;
                while (slot < slotCount) {
                    this.bits[slot] = this.slot(from);
                    ++slot;
                    ++from;
                }
            } else {
                int from = slotShift;
                for (int slot = 0; slot < slotCount; ++slot) {
                    this.bits[slot] = this.slot(from) >>> leftShift | this.slot(++from) << rightShift;
                }
            }
        }
        this.resize(length);
    }

    public void shiftRight(long shift, long length) {
        this.resize(length);
        if (shift != 0L) {
            int rightShift = (int)(shift & 0x3FL);
            int leftShift = 64 - rightShift;
            int slotShift = (int)(shift >> 6);
            if (leftShift == 0) {
                int slot = this.bits.length;
                int from = slot - slotShift;
                while (slot >= slotShift) {
                    this.bits[--slot] = this.slot(--from);
                }
            } else {
                int slot = this.bits.length;
                int from = slot - slotShift;
                while (slot > 0) {
                    this.bits[--slot] = this.slot(--from - 1) >>> leftShift | this.slot(from) << rightShift;
                }
            }
        }
        this.resize(length);
    }

    public void setRange(long fromIndex, long toIndex) {
        if (fromIndex < toIndex) {
            int firstWord = (int)(fromIndex >> 6);
            int lastWord = (int)(toIndex - 1L >> 6);
            long firstBits = -1L << (int)fromIndex;
            long lastBits = -1L >>> (int)(-toIndex);
            if (firstWord == lastWord) {
                int n = firstWord;
                this.bits[n] = this.bits[n] | firstBits & lastBits;
            } else {
                int n = firstWord;
                this.bits[n] = this.bits[n] | firstBits;
                Arrays.fill(this.bits, firstWord + 1, lastWord, -1L);
                int n2 = lastWord;
                this.bits[n2] = this.bits[n2] | lastBits;
            }
        }
    }
}

