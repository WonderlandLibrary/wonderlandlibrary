/*
 * Decompiled with CFR 0.150.
 */
package io.netty.buffer;

import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolChunkList;
import io.netty.buffer.PoolSubpage;
import io.netty.buffer.PooledByteBuf;

final class PoolChunk<T> {
    private static final int ST_UNUSED = 0;
    private static final int ST_BRANCH = 1;
    private static final int ST_ALLOCATED = 2;
    private static final int ST_ALLOCATED_SUBPAGE = 3;
    private static final long multiplier = 25214903917L;
    private static final long addend = 11L;
    private static final long mask = 0xFFFFFFFFFFFFL;
    final PoolArena<T> arena;
    final T memory;
    final boolean unpooled;
    private final int[] memoryMap;
    private final PoolSubpage<T>[] subpages;
    private final int subpageOverflowMask;
    private final int pageSize;
    private final int pageShifts;
    private final int chunkSize;
    private final int maxSubpageAllocs;
    private long random = (System.nanoTime() ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
    private int freeBytes;
    PoolChunkList<T> parent;
    PoolChunk<T> prev;
    PoolChunk<T> next;

    PoolChunk(PoolArena<T> arena, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize) {
        this.unpooled = false;
        this.arena = arena;
        this.memory = memory;
        this.pageSize = pageSize;
        this.pageShifts = pageShifts;
        this.chunkSize = chunkSize;
        this.subpageOverflowMask = ~(pageSize - 1);
        this.freeBytes = chunkSize;
        int chunkSizeInPages = chunkSize >>> pageShifts;
        this.maxSubpageAllocs = 1 << maxOrder;
        this.memoryMap = new int[this.maxSubpageAllocs << 1];
        int memoryMapIndex = 1;
        for (int i = 0; i <= maxOrder; ++i) {
            int runSizeInPages = chunkSizeInPages >>> i;
            for (int j = 0; j < chunkSizeInPages; j += runSizeInPages) {
                this.memoryMap[memoryMapIndex++] = j << 17 | runSizeInPages << 2 | 0;
            }
        }
        this.subpages = this.newSubpageArray(this.maxSubpageAllocs);
    }

    PoolChunk(PoolArena<T> arena, T memory, int size) {
        this.unpooled = true;
        this.arena = arena;
        this.memory = memory;
        this.memoryMap = null;
        this.subpages = null;
        this.subpageOverflowMask = 0;
        this.pageSize = 0;
        this.pageShifts = 0;
        this.chunkSize = size;
        this.maxSubpageAllocs = 0;
    }

    private PoolSubpage<T>[] newSubpageArray(int size) {
        return new PoolSubpage[size];
    }

    int usage() {
        if (this.freeBytes == 0) {
            return 100;
        }
        int freePercentage = (int)((long)this.freeBytes * 100L / (long)this.chunkSize);
        if (freePercentage == 0) {
            return 99;
        }
        return 100 - freePercentage;
    }

    long allocate(int normCapacity) {
        int firstVal = this.memoryMap[1];
        if ((normCapacity & this.subpageOverflowMask) != 0) {
            return this.allocateRun(normCapacity, 1, firstVal);
        }
        return this.allocateSubpage(normCapacity, 1, firstVal);
    }

    private long allocateRun(int normCapacity, int curIdx, int val) {
        while (true) {
            if ((val & 2) != 0) {
                return -1L;
            }
            if ((val & 1) == 0) break;
            int nextIdx = curIdx << 1 ^ this.nextRandom();
            long res = this.allocateRun(normCapacity, nextIdx, this.memoryMap[nextIdx]);
            if (res > 0L) {
                return res;
            }
            curIdx = nextIdx ^ 1;
            val = this.memoryMap[curIdx];
        }
        return this.allocateRunSimple(normCapacity, curIdx, val);
    }

    private long allocateRunSimple(int normCapacity, int curIdx, int val) {
        int runLength = this.runLength(val);
        if (normCapacity > runLength) {
            return -1L;
        }
        while (true) {
            if (normCapacity == runLength) {
                this.memoryMap[curIdx] = val & 0xFFFFFFFC | 2;
                this.freeBytes -= runLength;
                return curIdx;
            }
            int nextIdx = curIdx << 1 ^ this.nextRandom();
            int unusedIdx = nextIdx ^ 1;
            this.memoryMap[curIdx] = val & 0xFFFFFFFC | 1;
            this.memoryMap[unusedIdx] = this.memoryMap[unusedIdx] & 0xFFFFFFFC | 0;
            runLength >>>= 1;
            curIdx = nextIdx;
            val = this.memoryMap[curIdx];
        }
    }

    private long allocateSubpage(int normCapacity, int curIdx, int val) {
        int state = val & 3;
        if (state == 1) {
            int nextIdx = curIdx << 1 ^ this.nextRandom();
            long res = this.branchSubpage(normCapacity, nextIdx);
            if (res > 0L) {
                return res;
            }
            return this.branchSubpage(normCapacity, nextIdx ^ 1);
        }
        if (state == 0) {
            return this.allocateSubpageSimple(normCapacity, curIdx, val);
        }
        if (state == 3) {
            PoolSubpage<T> subpage = this.subpages[this.subpageIdx(curIdx)];
            int elemSize = subpage.elemSize;
            if (normCapacity != elemSize) {
                return -1L;
            }
            return subpage.allocate();
        }
        return -1L;
    }

    private long allocateSubpageSimple(int normCapacity, int curIdx, int val) {
        int runLength = this.runLength(val);
        while (true) {
            if (runLength == this.pageSize) {
                this.memoryMap[curIdx] = val & 0xFFFFFFFC | 3;
                this.freeBytes -= runLength;
                int subpageIdx = this.subpageIdx(curIdx);
                PoolSubpage<T> subpage = this.subpages[subpageIdx];
                if (subpage == null) {
                    subpage = new PoolSubpage(this, curIdx, this.runOffset(val), this.pageSize, normCapacity);
                    this.subpages[subpageIdx] = subpage;
                } else {
                    subpage.init(normCapacity);
                }
                return subpage.allocate();
            }
            int nextIdx = curIdx << 1 ^ this.nextRandom();
            int unusedIdx = nextIdx ^ 1;
            this.memoryMap[curIdx] = val & 0xFFFFFFFC | 1;
            this.memoryMap[unusedIdx] = this.memoryMap[unusedIdx] & 0xFFFFFFFC | 0;
            runLength >>>= 1;
            curIdx = nextIdx;
            val = this.memoryMap[curIdx];
        }
    }

    private long branchSubpage(int normCapacity, int nextIdx) {
        int nextVal = this.memoryMap[nextIdx];
        if ((nextVal & 3) != 2) {
            return this.allocateSubpage(normCapacity, nextIdx, nextVal);
        }
        return -1L;
    }

    void free(long handle) {
        int memoryMapIdx = (int)handle;
        int bitmapIdx = (int)(handle >>> 32);
        int val = this.memoryMap[memoryMapIdx];
        int state = val & 3;
        if (state == 3) {
            assert (bitmapIdx != 0);
            PoolSubpage<T> subpage = this.subpages[this.subpageIdx(memoryMapIdx)];
            assert (subpage != null && subpage.doNotDestroy);
            if (subpage.free(bitmapIdx & 0x3FFFFFFF)) {
                return;
            }
        } else {
            assert (state == 2) : "state: " + state;
            assert (bitmapIdx == 0);
        }
        this.freeBytes += this.runLength(val);
        while (true) {
            this.memoryMap[memoryMapIdx] = val & 0xFFFFFFFC | 0;
            if (memoryMapIdx == 1) {
                assert (this.freeBytes == this.chunkSize);
                return;
            }
            if ((this.memoryMap[PoolChunk.siblingIdx(memoryMapIdx)] & 3) != 0) break;
            memoryMapIdx = PoolChunk.parentIdx(memoryMapIdx);
            val = this.memoryMap[memoryMapIdx];
        }
    }

    void initBuf(PooledByteBuf<T> buf, long handle, int reqCapacity) {
        int memoryMapIdx = (int)handle;
        int bitmapIdx = (int)(handle >>> 32);
        if (bitmapIdx == 0) {
            int val = this.memoryMap[memoryMapIdx];
            assert ((val & 3) == 2) : String.valueOf(val & 3);
            buf.init(this, handle, this.runOffset(val), reqCapacity, this.runLength(val));
        } else {
            this.initBufWithSubpage(buf, handle, bitmapIdx, reqCapacity);
        }
    }

    void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int reqCapacity) {
        this.initBufWithSubpage(buf, handle, (int)(handle >>> 32), reqCapacity);
    }

    private void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int bitmapIdx, int reqCapacity) {
        assert (bitmapIdx != 0);
        int memoryMapIdx = (int)handle;
        int val = this.memoryMap[memoryMapIdx];
        assert ((val & 3) == 3);
        PoolSubpage<T> subpage = this.subpages[this.subpageIdx(memoryMapIdx)];
        assert (subpage.doNotDestroy);
        assert (reqCapacity <= subpage.elemSize);
        buf.init(this, handle, this.runOffset(val) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize, reqCapacity, subpage.elemSize);
    }

    private static int parentIdx(int memoryMapIdx) {
        return memoryMapIdx >>> 1;
    }

    private static int siblingIdx(int memoryMapIdx) {
        return memoryMapIdx ^ 1;
    }

    private int runLength(int val) {
        return (val >>> 2 & 0x7FFF) << this.pageShifts;
    }

    private int runOffset(int val) {
        return val >>> 17 << this.pageShifts;
    }

    private int subpageIdx(int memoryMapIdx) {
        return memoryMapIdx - this.maxSubpageAllocs;
    }

    private int nextRandom() {
        this.random = this.random * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
        return (int)(this.random >>> 47) & 1;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Chunk(");
        buf.append(Integer.toHexString(System.identityHashCode(this)));
        buf.append(": ");
        buf.append(this.usage());
        buf.append("%, ");
        buf.append(this.chunkSize - this.freeBytes);
        buf.append('/');
        buf.append(this.chunkSize);
        buf.append(')');
        return buf.toString();
    }
}

