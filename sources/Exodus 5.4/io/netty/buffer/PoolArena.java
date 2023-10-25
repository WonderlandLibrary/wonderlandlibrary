/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.PoolChunk;
import io.netty.buffer.PoolChunkList;
import io.netty.buffer.PoolSubpage;
import io.netty.buffer.PoolThreadCache;
import io.netty.buffer.PooledByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.PooledDirectByteBuf;
import io.netty.buffer.PooledHeapByteBuf;
import io.netty.buffer.PooledUnsafeDirectByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;

abstract class PoolArena<T> {
    final int subpageOverflowMask;
    static final int numTinySubpagePools = 32;
    final int chunkSize;
    private final PoolChunkList<T> q100;
    private final PoolChunkList<T> qInit;
    private final PoolSubpage<T>[] tinySubpagePools;
    final PooledByteBufAllocator parent;
    final int pageSize;
    private final PoolChunkList<T> q000;
    private final PoolChunkList<T> q025;
    private final PoolChunkList<T> q075;
    private final PoolSubpage<T>[] smallSubpagePools;
    private final PoolChunkList<T> q050;
    final int pageShifts;
    private final int maxOrder;
    final int numSmallSubpagePools;

    protected abstract PooledByteBuf<T> newByteBuf(int var1);

    int normalizeCapacity(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("capacity: " + n + " (expected: 0+)");
        }
        if (n >= this.chunkSize) {
            return n;
        }
        if (!PoolArena.isTiny(n)) {
            int n2 = n;
            --n2;
            n2 |= n2 >>> 1;
            n2 |= n2 >>> 2;
            n2 |= n2 >>> 4;
            n2 |= n2 >>> 8;
            n2 |= n2 >>> 16;
            if (++n2 < 0) {
                n2 >>>= 1;
            }
            return n2;
        }
        if ((n & 0xF) == 0) {
            return n;
        }
        return (n & 0xFFFFFFF0) + 16;
    }

    protected abstract void memoryCopy(T var1, int var2, T var3, int var4, int var5);

    private void allocate(PoolThreadCache poolThreadCache, PooledByteBuf<T> pooledByteBuf, int n) {
        int n2 = this.normalizeCapacity(n);
        if (this.isTinyOrSmall(n2)) {
            PoolSubpage<T>[] poolSubpageArray;
            int n3;
            if (PoolArena.isTiny(n2)) {
                if (poolThreadCache.allocateTiny(this, pooledByteBuf, n, n2)) {
                    return;
                }
                n3 = PoolArena.tinyIdx(n2);
                poolSubpageArray = this.tinySubpagePools;
            } else {
                if (poolThreadCache.allocateSmall(this, pooledByteBuf, n, n2)) {
                    return;
                }
                n3 = PoolArena.smallIdx(n2);
                poolSubpageArray = this.smallSubpagePools;
            }
            PoolArena poolArena = this;
            synchronized (poolArena) {
                PoolSubpage<T> poolSubpage = poolSubpageArray[n3];
                PoolSubpage poolSubpage2 = poolSubpage.next;
                if (poolSubpage2 != poolSubpage) {
                    assert (poolSubpage2.doNotDestroy && poolSubpage2.elemSize == n2);
                    long l = poolSubpage2.allocate();
                    assert (l >= 0L);
                    poolSubpage2.chunk.initBufWithSubpage(pooledByteBuf, l, n);
                    return;
                }
            }
        } else if (n2 <= this.chunkSize) {
            if (poolThreadCache.allocateNormal(this, pooledByteBuf, n, n2)) {
                return;
            }
        } else {
            this.allocateHuge(pooledByteBuf, n);
            return;
        }
        this.allocateNormal(pooledByteBuf, n, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    void free(PoolChunk<T> poolChunk, long l, int n) {
        if (poolChunk.unpooled) {
            this.destroyChunk(poolChunk);
            return;
        }
        PoolThreadCache poolThreadCache = (PoolThreadCache)this.parent.threadCache.get();
        if (poolThreadCache.add(this, poolChunk, l, n)) {
            return;
        }
        PoolArena poolArena = this;
        synchronized (poolArena) {
            poolChunk.parent.free(poolChunk, l);
            return;
        }
    }

    static boolean isTiny(int n) {
        return (n & 0xFFFFFE00) == 0;
    }

    PoolSubpage<T> findSubpagePoolHead(int n) {
        PoolSubpage<T>[] poolSubpageArray;
        int n2;
        if (PoolArena.isTiny(n)) {
            n2 = n >>> 4;
            poolSubpageArray = this.tinySubpagePools;
        } else {
            n2 = 0;
            n >>>= 10;
            while (n != 0) {
                n >>>= 1;
                ++n2;
            }
            poolSubpageArray = this.smallSubpagePools;
        }
        return poolSubpageArray[n2];
    }

    abstract boolean isDirect();

    void reallocate(PooledByteBuf<T> pooledByteBuf, int n, boolean bl) {
        if (n < 0 || n > pooledByteBuf.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + n);
        }
        int n2 = pooledByteBuf.length;
        if (n2 == n) {
            return;
        }
        PoolChunk poolChunk = pooledByteBuf.chunk;
        long l = pooledByteBuf.handle;
        Object t = pooledByteBuf.memory;
        int n3 = pooledByteBuf.offset;
        int n4 = pooledByteBuf.maxLength;
        int n5 = pooledByteBuf.readerIndex();
        int n6 = pooledByteBuf.writerIndex();
        this.allocate((PoolThreadCache)this.parent.threadCache.get(), pooledByteBuf, n);
        if (n > n2) {
            this.memoryCopy(t, n3, pooledByteBuf.memory, pooledByteBuf.offset, n2);
        } else if (n < n2) {
            if (n5 < n) {
                if (n6 > n) {
                    n6 = n;
                }
                this.memoryCopy(t, n3 + n5, pooledByteBuf.memory, pooledByteBuf.offset + n5, n6 - n5);
            } else {
                n5 = n6 = n;
            }
        }
        pooledByteBuf.setIndex(n5, n6);
        if (bl) {
            this.free(poolChunk, l, n4);
        }
    }

    protected abstract PoolChunk<T> newChunk(int var1, int var2, int var3, int var4);

    static int tinyIdx(int n) {
        return n >>> 4;
    }

    public synchronized String toString() {
        PoolSubpage poolSubpage;
        PoolSubpage<T> poolSubpage2;
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Chunk(s) at 0~25%:");
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.qInit);
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("Chunk(s) at 0~50%:");
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.q000);
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("Chunk(s) at 25~75%:");
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.q025);
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("Chunk(s) at 50~100%:");
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.q050);
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("Chunk(s) at 75~100%:");
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.q075);
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("Chunk(s) at 100%:");
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append(this.q100);
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("tiny subpages:");
        for (n = 1; n < this.tinySubpagePools.length; ++n) {
            poolSubpage2 = this.tinySubpagePools[n];
            if (poolSubpage2.next == poolSubpage2) continue;
            stringBuilder.append(StringUtil.NEWLINE);
            stringBuilder.append(n);
            stringBuilder.append(": ");
            poolSubpage = poolSubpage2.next;
            do {
                stringBuilder.append(poolSubpage);
            } while ((poolSubpage = poolSubpage.next) != poolSubpage2);
        }
        stringBuilder.append(StringUtil.NEWLINE);
        stringBuilder.append("small subpages:");
        for (n = 1; n < this.smallSubpagePools.length; ++n) {
            poolSubpage2 = this.smallSubpagePools[n];
            if (poolSubpage2.next == poolSubpage2) continue;
            stringBuilder.append(StringUtil.NEWLINE);
            stringBuilder.append(n);
            stringBuilder.append(": ");
            poolSubpage = poolSubpage2.next;
            do {
                stringBuilder.append(poolSubpage);
            } while ((poolSubpage = poolSubpage.next) != poolSubpage2);
        }
        stringBuilder.append(StringUtil.NEWLINE);
        return stringBuilder.toString();
    }

    private void allocateHuge(PooledByteBuf<T> pooledByteBuf, int n) {
        pooledByteBuf.initUnpooled(this.newUnpooledChunk(n), n);
    }

    private PoolSubpage<T> newSubpagePoolHead(int n) {
        PoolSubpage poolSubpage = new PoolSubpage(n);
        poolSubpage.prev = poolSubpage;
        poolSubpage.next = poolSubpage;
        return poolSubpage;
    }

    boolean isTinyOrSmall(int n) {
        return (n & this.subpageOverflowMask) == 0;
    }

    private PoolSubpage<T>[] newSubpagePoolArray(int n) {
        return new PoolSubpage[n];
    }

    protected PoolArena(PooledByteBufAllocator pooledByteBufAllocator, int n, int n2, int n3, int n4) {
        int n5;
        this.parent = pooledByteBufAllocator;
        this.pageSize = n;
        this.maxOrder = n2;
        this.pageShifts = n3;
        this.chunkSize = n4;
        this.subpageOverflowMask = ~(n - 1);
        this.tinySubpagePools = this.newSubpagePoolArray(32);
        for (n5 = 0; n5 < this.tinySubpagePools.length; ++n5) {
            this.tinySubpagePools[n5] = this.newSubpagePoolHead(n);
        }
        this.numSmallSubpagePools = n3 - 9;
        this.smallSubpagePools = this.newSubpagePoolArray(this.numSmallSubpagePools);
        for (n5 = 0; n5 < this.smallSubpagePools.length; ++n5) {
            this.smallSubpagePools[n5] = this.newSubpagePoolHead(n);
        }
        this.q100 = new PoolChunkList(this, null, 100, Integer.MAX_VALUE);
        this.q075 = new PoolChunkList<T>(this, this.q100, 75, 100);
        this.q050 = new PoolChunkList<T>(this, this.q075, 50, 100);
        this.q025 = new PoolChunkList<T>(this, this.q050, 25, 75);
        this.q000 = new PoolChunkList<T>(this, this.q025, 1, 50);
        this.qInit = new PoolChunkList<T>(this, this.q000, Integer.MIN_VALUE, 25);
        this.q100.prevList = this.q075;
        this.q075.prevList = this.q050;
        this.q050.prevList = this.q025;
        this.q025.prevList = this.q000;
        this.q000.prevList = null;
        this.qInit.prevList = this.qInit;
    }

    static int smallIdx(int n) {
        int n2 = 0;
        int n3 = n >>> 10;
        while (n3 != 0) {
            n3 >>>= 1;
            ++n2;
        }
        return n2;
    }

    protected abstract void destroyChunk(PoolChunk<T> var1);

    private synchronized void allocateNormal(PooledByteBuf<T> pooledByteBuf, int n, int n2) {
        if (this.q050.allocate(pooledByteBuf, n, n2) || this.q025.allocate(pooledByteBuf, n, n2) || this.q000.allocate(pooledByteBuf, n, n2) || this.qInit.allocate(pooledByteBuf, n, n2) || this.q075.allocate(pooledByteBuf, n, n2) || this.q100.allocate(pooledByteBuf, n, n2)) {
            return;
        }
        PoolChunk<T> poolChunk = this.newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
        long l = poolChunk.allocate(n2);
        assert (l > 0L);
        poolChunk.initBuf(pooledByteBuf, l, n);
        this.qInit.add(poolChunk);
    }

    protected abstract PoolChunk<T> newUnpooledChunk(int var1);

    PooledByteBuf<T> allocate(PoolThreadCache poolThreadCache, int n, int n2) {
        PooledByteBuf<T> pooledByteBuf = this.newByteBuf(n2);
        this.allocate(poolThreadCache, pooledByteBuf, n);
        return pooledByteBuf;
    }

    static final class HeapArena
    extends PoolArena<byte[]> {
        @Override
        boolean isDirect() {
            return false;
        }

        @Override
        protected void destroyChunk(PoolChunk<byte[]> poolChunk) {
        }

        @Override
        protected PoolChunk<byte[]> newChunk(int n, int n2, int n3, int n4) {
            return new PoolChunk<byte[]>(this, new byte[n4], n, n2, n3, n4);
        }

        @Override
        protected PooledByteBuf<byte[]> newByteBuf(int n) {
            return PooledHeapByteBuf.newInstance(n);
        }

        HeapArena(PooledByteBufAllocator pooledByteBufAllocator, int n, int n2, int n3, int n4) {
            super(pooledByteBufAllocator, n, n2, n3, n4);
        }

        @Override
        protected PoolChunk<byte[]> newUnpooledChunk(int n) {
            return new PoolChunk<byte[]>(this, new byte[n], n);
        }

        @Override
        protected void memoryCopy(byte[] byArray, int n, byte[] byArray2, int n2, int n3) {
            if (n3 == 0) {
                return;
            }
            System.arraycopy(byArray, n, byArray2, n2, n3);
        }
    }

    static final class DirectArena
    extends PoolArena<ByteBuffer> {
        private static final boolean HAS_UNSAFE = PlatformDependent.hasUnsafe();

        @Override
        boolean isDirect() {
            return true;
        }

        DirectArena(PooledByteBufAllocator pooledByteBufAllocator, int n, int n2, int n3, int n4) {
            super(pooledByteBufAllocator, n, n2, n3, n4);
        }

        @Override
        protected PooledByteBuf<ByteBuffer> newByteBuf(int n) {
            if (HAS_UNSAFE) {
                return PooledUnsafeDirectByteBuf.newInstance(n);
            }
            return PooledDirectByteBuf.newInstance(n);
        }

        @Override
        protected void destroyChunk(PoolChunk<ByteBuffer> poolChunk) {
            PlatformDependent.freeDirectBuffer((ByteBuffer)poolChunk.memory);
        }

        @Override
        protected PoolChunk<ByteBuffer> newChunk(int n, int n2, int n3, int n4) {
            return new PoolChunk<ByteBuffer>(this, ByteBuffer.allocateDirect(n4), n, n2, n3, n4);
        }

        @Override
        protected void memoryCopy(ByteBuffer byteBuffer, int n, ByteBuffer byteBuffer2, int n2, int n3) {
            if (n3 == 0) {
                return;
            }
            if (HAS_UNSAFE) {
                PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(byteBuffer) + (long)n, PlatformDependent.directBufferAddress(byteBuffer2) + (long)n2, n3);
            } else {
                byteBuffer = byteBuffer.duplicate();
                byteBuffer2 = byteBuffer2.duplicate();
                byteBuffer.position(n).limit(n + n3);
                byteBuffer2.position(n2);
                byteBuffer2.put(byteBuffer);
            }
        }

        @Override
        protected PoolChunk<ByteBuffer> newUnpooledChunk(int n) {
            return new PoolChunk<ByteBuffer>(this, ByteBuffer.allocateDirect(n), n);
        }
    }
}

