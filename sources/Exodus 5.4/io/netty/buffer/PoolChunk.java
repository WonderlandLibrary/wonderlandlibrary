/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolChunkList;
import io.netty.buffer.PoolSubpage;
import io.netty.buffer.PooledByteBuf;

final class PoolChunk<T> {
    final PoolArena<T> arena;
    PoolChunk<T> next;
    final T memory;
    private final int chunkSize;
    private final byte unusable;
    private final int pageShifts;
    private final int maxOrder;
    private int freeBytes;
    final boolean unpooled;
    private final int maxSubpageAllocs;
    private final int pageSize;
    PoolChunk<T> prev;
    private final int log2ChunkSize;
    private final PoolSubpage<T>[] subpages;
    private final byte[] memoryMap;
    PoolChunkList<T> parent;
    private final int subpageOverflowMask;
    private final byte[] depthMap;

    private int runLength(int n) {
        return 1 << this.log2ChunkSize - this.depth(n);
    }

    private int allocateNode(int n) {
        int n2 = 1;
        int n3 = -(1 << n);
        byte by = this.value(n2);
        if (by > n) {
            return -1;
        }
        while (by < n || (n2 & n3) == 0) {
            by = this.value(n2 <<= 1);
            if (by <= n) continue;
            by = this.value(n2 ^= 1);
        }
        byte by2 = this.value(n2);
        assert (by2 == n && (n2 & n3) == 1 << n) : String.format("val = %d, id & initial = %d, d = %d", by2, n2 & n3, n);
        this.setValue(n2, this.unusable);
        this.updateParentsAlloc(n2);
        return n2;
    }

    private PoolSubpage<T>[] newSubpageArray(int n) {
        return new PoolSubpage[n];
    }

    private int runOffset(int n) {
        int n2 = n ^ 1 << this.depth(n);
        return n2 * this.runLength(n);
    }

    private int subpageIdx(int n) {
        return n ^ this.maxSubpageAllocs;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chunk(");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(": ");
        stringBuilder.append(this.usage());
        stringBuilder.append("%, ");
        stringBuilder.append(this.chunkSize - this.freeBytes);
        stringBuilder.append('/');
        stringBuilder.append(this.chunkSize);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    void initBufWithSubpage(PooledByteBuf<T> pooledByteBuf, long l, int n) {
        this.initBufWithSubpage(pooledByteBuf, l, (int)(l >>> 32), n);
    }

    private void setValue(int n, byte by) {
        this.memoryMap[n] = by;
    }

    long allocate(int n) {
        if ((n & this.subpageOverflowMask) != 0) {
            return this.allocateRun(n);
        }
        return this.allocateSubpage(n);
    }

    private void updateParentsAlloc(int n) {
        while (n > 1) {
            byte by;
            int n2 = n >>> 1;
            byte by2 = this.value(n);
            byte by3 = by2 < (by = this.value(n ^ 1)) ? by2 : by;
            this.setValue(n2, by3);
            n = n2;
        }
    }

    int usage() {
        int n = this.freeBytes;
        if (n == 0) {
            return 100;
        }
        int n2 = (int)((long)n * 100L / (long)this.chunkSize);
        if (n2 == 0) {
            return 99;
        }
        return 100 - n2;
    }

    private void initBufWithSubpage(PooledByteBuf<T> pooledByteBuf, long l, int n, int n2) {
        assert (n != 0);
        int n3 = (int)l;
        PoolSubpage<T> poolSubpage = this.subpages[this.subpageIdx(n3)];
        assert (poolSubpage.doNotDestroy);
        assert (n2 <= poolSubpage.elemSize);
        pooledByteBuf.init(this, l, this.runOffset(n3) + (n & 0x3FFFFFFF) * poolSubpage.elemSize, n2, poolSubpage.elemSize);
    }

    void initBuf(PooledByteBuf<T> pooledByteBuf, long l, int n) {
        int n2 = (int)l;
        int n3 = (int)(l >>> 32);
        if (n3 == 0) {
            byte by = this.value(n2);
            assert (by == this.unusable) : String.valueOf(by);
            pooledByteBuf.init(this, l, this.runOffset(n2), n, this.runLength(n2));
        } else {
            this.initBufWithSubpage(pooledByteBuf, l, n3, n);
        }
    }

    private byte depth(int n) {
        return this.depthMap[n];
    }

    private long allocateSubpage(int n) {
        int n2 = this.maxOrder;
        int n3 = this.allocateNode(n2);
        if (n3 < 0) {
            return n3;
        }
        PoolSubpage<T>[] poolSubpageArray = this.subpages;
        int n4 = this.pageSize;
        this.freeBytes -= n4;
        int n5 = this.subpageIdx(n3);
        PoolSubpage<T> poolSubpage = poolSubpageArray[n5];
        if (poolSubpage == null) {
            poolSubpage = new PoolSubpage(this, n3, this.runOffset(n3), n4, n);
            poolSubpageArray[n5] = poolSubpage;
        } else {
            poolSubpage.init(n);
        }
        return poolSubpage.allocate();
    }

    private byte value(int n) {
        return this.memoryMap[n];
    }

    private long allocateRun(int n) {
        int n2 = this.maxOrder - (PoolChunk.log2(n) - this.pageShifts);
        int n3 = this.allocateNode(n2);
        if (n3 < 0) {
            return n3;
        }
        this.freeBytes -= this.runLength(n3);
        return n3;
    }

    PoolChunk(PoolArena<T> poolArena, T t, int n, int n2, int n3, int n4) {
        this.unpooled = false;
        this.arena = poolArena;
        this.memory = t;
        this.pageSize = n;
        this.pageShifts = n3;
        this.maxOrder = n2;
        this.chunkSize = n4;
        this.unusable = (byte)(n2 + 1);
        this.log2ChunkSize = PoolChunk.log2(n4);
        this.subpageOverflowMask = ~(n - 1);
        this.freeBytes = n4;
        assert (n2 < 30) : "maxOrder should be < 30, but is: " + n2;
        this.maxSubpageAllocs = 1 << n2;
        this.memoryMap = new byte[this.maxSubpageAllocs << 1];
        this.depthMap = new byte[this.memoryMap.length];
        int n5 = 1;
        for (int i = 0; i <= n2; ++i) {
            int n6 = 1 << i;
            for (int j = 0; j < n6; ++j) {
                this.memoryMap[n5] = (byte)i;
                this.depthMap[n5] = (byte)i;
                ++n5;
            }
        }
        this.subpages = this.newSubpageArray(this.maxSubpageAllocs);
    }

    private void updateParentsFree(int n) {
        int n2 = this.depth(n) + 1;
        while (n > 1) {
            int n3 = n >>> 1;
            byte by = this.value(n);
            byte by2 = this.value(n ^ 1);
            if (by == --n2 && by2 == n2) {
                this.setValue(n3, (byte)(n2 - 1));
            } else {
                byte by3 = by < by2 ? by : by2;
                this.setValue(n3, by3);
            }
            n = n3;
        }
    }

    void free(long l) {
        int n = (int)l;
        int n2 = (int)(l >>> 32);
        if (n2 != 0) {
            PoolSubpage<T> poolSubpage = this.subpages[this.subpageIdx(n)];
            assert (poolSubpage != null && poolSubpage.doNotDestroy);
            if (poolSubpage.free(n2 & 0x3FFFFFFF)) {
                return;
            }
        }
        this.freeBytes += this.runLength(n);
        this.setValue(n, this.depth(n));
        this.updateParentsFree(n);
    }

    private static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    PoolChunk(PoolArena<T> poolArena, T t, int n) {
        this.unpooled = true;
        this.arena = poolArena;
        this.memory = t;
        this.memoryMap = null;
        this.depthMap = null;
        this.subpages = null;
        this.subpageOverflowMask = 0;
        this.pageSize = 0;
        this.pageShifts = 0;
        this.maxOrder = 0;
        this.unusable = (byte)(this.maxOrder + 1);
        this.chunkSize = n;
        this.log2ChunkSize = PoolChunk.log2(this.chunkSize);
        this.maxSubpageAllocs = 0;
    }
}

