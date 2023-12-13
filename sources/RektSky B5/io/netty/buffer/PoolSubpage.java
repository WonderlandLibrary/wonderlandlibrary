/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.PoolChunk;

final class PoolSubpage<T> {
    final PoolChunk<T> chunk;
    private final int memoryMapIdx;
    private final int runOffset;
    private final int pageSize;
    private final long[] bitmap;
    PoolSubpage<T> prev;
    PoolSubpage<T> next;
    boolean doNotDestroy;
    int elemSize;
    private int maxNumElems;
    private int bitmapLength;
    private int nextAvail;
    private int numAvail;

    PoolSubpage(int pageSize) {
        this.chunk = null;
        this.memoryMapIdx = -1;
        this.runOffset = -1;
        this.elemSize = -1;
        this.pageSize = pageSize;
        this.bitmap = null;
    }

    PoolSubpage(PoolChunk<T> chunk, int memoryMapIdx, int runOffset, int pageSize, int elemSize) {
        this.chunk = chunk;
        this.memoryMapIdx = memoryMapIdx;
        this.runOffset = runOffset;
        this.pageSize = pageSize;
        this.bitmap = new long[pageSize >>> 10];
        this.init(elemSize);
    }

    void init(int elemSize) {
        this.doNotDestroy = true;
        this.elemSize = elemSize;
        if (elemSize != 0) {
            this.maxNumElems = this.numAvail = this.pageSize / elemSize;
            this.nextAvail = 0;
            this.bitmapLength = this.maxNumElems >>> 6;
            if ((this.maxNumElems & 0x3F) != 0) {
                ++this.bitmapLength;
            }
            for (int i2 = 0; i2 < this.bitmapLength; ++i2) {
                this.bitmap[i2] = 0L;
            }
        }
        this.addToPool();
    }

    long allocate() {
        if (this.elemSize == 0) {
            return this.toHandle(0);
        }
        if (this.numAvail == 0 || !this.doNotDestroy) {
            return -1L;
        }
        int bitmapIdx = this.getNextAvail();
        int q2 = bitmapIdx >>> 6;
        int r2 = bitmapIdx & 0x3F;
        assert ((this.bitmap[q2] >>> r2 & 1L) == 0L);
        int n2 = q2;
        this.bitmap[n2] = this.bitmap[n2] | 1L << r2;
        if (--this.numAvail == 0) {
            this.removeFromPool();
        }
        return this.toHandle(bitmapIdx);
    }

    boolean free(int bitmapIdx) {
        if (this.elemSize == 0) {
            return true;
        }
        int q2 = bitmapIdx >>> 6;
        int r2 = bitmapIdx & 0x3F;
        assert ((this.bitmap[q2] >>> r2 & 1L) != 0L);
        int n2 = q2;
        this.bitmap[n2] = this.bitmap[n2] ^ 1L << r2;
        this.setNextAvail(bitmapIdx);
        if (this.numAvail++ == 0) {
            this.addToPool();
            return true;
        }
        if (this.numAvail != this.maxNumElems) {
            return true;
        }
        if (this.prev == this.next) {
            return true;
        }
        this.doNotDestroy = false;
        this.removeFromPool();
        return false;
    }

    private void addToPool() {
        PoolSubpage head = this.chunk.arena.findSubpagePoolHead(this.elemSize);
        assert (this.prev == null && this.next == null);
        this.prev = head;
        this.next = head.next;
        this.next.prev = this;
        head.next = this;
    }

    private void removeFromPool() {
        assert (this.prev != null && this.next != null);
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
    }

    private void setNextAvail(int bitmapIdx) {
        this.nextAvail = bitmapIdx;
    }

    private int getNextAvail() {
        int nextAvail = this.nextAvail;
        if (nextAvail >= 0) {
            this.nextAvail = -1;
            return nextAvail;
        }
        return this.findNextAvail();
    }

    private int findNextAvail() {
        long[] bitmap = this.bitmap;
        int bitmapLength = this.bitmapLength;
        for (int i2 = 0; i2 < bitmapLength; ++i2) {
            long bits = bitmap[i2];
            if ((bits ^ 0xFFFFFFFFFFFFFFFFL) == 0L) continue;
            return this.findNextAvail0(i2, bits);
        }
        return -1;
    }

    private int findNextAvail0(int i2, long bits) {
        int maxNumElems = this.maxNumElems;
        int baseVal = i2 << 6;
        for (int j2 = 0; j2 < 64; ++j2) {
            if ((bits & 1L) == 0L) {
                int val2 = baseVal | j2;
                if (val2 >= maxNumElems) break;
                return val2;
            }
            bits >>>= 1;
        }
        return -1;
    }

    private long toHandle(int bitmapIdx) {
        return 0x4000000000000000L | (long)bitmapIdx << 32 | (long)this.memoryMapIdx;
    }

    public String toString() {
        if (!this.doNotDestroy) {
            return "(" + this.memoryMapIdx + ": not in use)";
        }
        return String.valueOf('(') + this.memoryMapIdx + ": " + (this.maxNumElems - this.numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + this.elemSize + ')';
    }
}

