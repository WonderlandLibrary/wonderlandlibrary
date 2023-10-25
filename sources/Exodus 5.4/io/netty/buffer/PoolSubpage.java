/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.PoolChunk;

final class PoolSubpage<T> {
    private int numAvail;
    private int nextAvail;
    private final int pageSize;
    private final int memoryMapIdx;
    private int maxNumElems;
    boolean doNotDestroy;
    final PoolChunk<T> chunk;
    private int bitmapLength;
    PoolSubpage<T> prev;
    private final long[] bitmap;
    int elemSize;
    PoolSubpage<T> next;
    private final int runOffset;

    public String toString() {
        if (!this.doNotDestroy) {
            return "(" + this.memoryMapIdx + ": not in use)";
        }
        return String.valueOf('(') + this.memoryMapIdx + ": " + (this.maxNumElems - this.numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + this.elemSize + ')';
    }

    void init(int n) {
        this.doNotDestroy = true;
        this.elemSize = n;
        if (n != 0) {
            this.maxNumElems = this.numAvail = this.pageSize / n;
            this.nextAvail = 0;
            this.bitmapLength = this.maxNumElems >>> 6;
            if ((this.maxNumElems & 0x3F) != 0) {
                ++this.bitmapLength;
            }
            for (int i = 0; i < this.bitmapLength; ++i) {
                this.bitmap[i] = 0L;
            }
        }
        this.addToPool();
    }

    private int findNextAvail() {
        long[] lArray = this.bitmap;
        int n = this.bitmapLength;
        for (int i = 0; i < n; ++i) {
            long l = lArray[i];
            if ((l ^ 0xFFFFFFFFFFFFFFFFL) == 0L) continue;
            return this.findNextAvail0(i, l);
        }
        return -1;
    }

    PoolSubpage(int n) {
        this.chunk = null;
        this.memoryMapIdx = -1;
        this.runOffset = -1;
        this.elemSize = -1;
        this.pageSize = n;
        this.bitmap = null;
    }

    private int getNextAvail() {
        int n = this.nextAvail;
        if (n >= 0) {
            this.nextAvail = -1;
            return n;
        }
        return this.findNextAvail();
    }

    PoolSubpage(PoolChunk<T> poolChunk, int n, int n2, int n3, int n4) {
        this.chunk = poolChunk;
        this.memoryMapIdx = n;
        this.runOffset = n2;
        this.pageSize = n3;
        this.bitmap = new long[n3 >>> 10];
        this.init(n4);
    }

    boolean free(int n) {
        if (this.elemSize == 0) {
            return true;
        }
        int n2 = n >>> 6;
        int n3 = n & 0x3F;
        assert ((this.bitmap[n2] >>> n3 & 1L) != 0L);
        int n4 = n2;
        this.bitmap[n4] = this.bitmap[n4] ^ 1L << n3;
        this.setNextAvail(n);
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

    private void removeFromPool() {
        assert (this.prev != null && this.next != null);
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
    }

    private void setNextAvail(int n) {
        this.nextAvail = n;
    }

    private void addToPool() {
        PoolSubpage poolSubpage = this.chunk.arena.findSubpagePoolHead(this.elemSize);
        assert (this.prev == null && this.next == null);
        this.prev = poolSubpage;
        this.next = poolSubpage.next;
        this.next.prev = this;
        poolSubpage.next = this;
    }

    private int findNextAvail0(int n, long l) {
        int n2 = this.maxNumElems;
        int n3 = n << 6;
        for (int i = 0; i < 64; ++i) {
            if ((l & 1L) == 0L) {
                int n4 = n3 | i;
                if (n4 >= n2) break;
                return n4;
            }
            l >>>= 1;
        }
        return -1;
    }

    long allocate() {
        if (this.elemSize == 0) {
            return this.toHandle(0);
        }
        if (this.numAvail == 0 || !this.doNotDestroy) {
            return -1L;
        }
        int n = this.getNextAvail();
        int n2 = n >>> 6;
        int n3 = n & 0x3F;
        assert ((this.bitmap[n2] >>> n3 & 1L) == 0L);
        int n4 = n2;
        this.bitmap[n4] = this.bitmap[n4] | 1L << n3;
        if (--this.numAvail == 0) {
            this.removeFromPool();
        }
        return this.toHandle(n);
    }

    private long toHandle(int n) {
        return 0x4000000000000000L | (long)n << 32 | (long)this.memoryMapIdx;
    }
}

