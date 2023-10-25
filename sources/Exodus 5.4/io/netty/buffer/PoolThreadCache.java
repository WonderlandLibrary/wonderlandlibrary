/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolChunk;
import io.netty.buffer.PooledByteBuf;
import io.netty.util.ThreadDeathWatcher;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;

final class PoolThreadCache {
    private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
    private final int freeSweepAllocationThreshold;
    final PoolArena<byte[]> heapArena;
    private final int numShiftsNormalHeap;
    private final MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
    private int allocations;
    final PoolArena<ByteBuffer> directArena;
    private final MemoryRegionCache<byte[]>[] normalHeapCaches;
    private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
    private final int numShiftsNormalDirect;
    private final MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
    private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
    private final Thread thread = Thread.currentThread();
    private final Runnable freeTask = new Runnable(){

        @Override
        public void run() {
            PoolThreadCache.this.free0();
        }
    };

    private MemoryRegionCache<?> cacheForNormal(PoolArena<?> poolArena, int n) {
        if (poolArena.isDirect()) {
            int n2 = PoolThreadCache.log2(n >> this.numShiftsNormalDirect);
            return PoolThreadCache.cache(this.normalDirectCaches, n2);
        }
        int n3 = PoolThreadCache.log2(n >> this.numShiftsNormalHeap);
        return PoolThreadCache.cache(this.normalHeapCaches, n3);
    }

    private static int free(MemoryRegionCache<?>[] memoryRegionCacheArray) {
        if (memoryRegionCacheArray == null) {
            return 0;
        }
        int n = 0;
        for (MemoryRegionCache<?> memoryRegionCache : memoryRegionCacheArray) {
            n += PoolThreadCache.free(memoryRegionCache);
        }
        return n;
    }

    private static void trim(MemoryRegionCache<?>[] memoryRegionCacheArray) {
        if (memoryRegionCacheArray == null) {
            return;
        }
        for (MemoryRegionCache<?> memoryRegionCache : memoryRegionCacheArray) {
            PoolThreadCache.trim(memoryRegionCache);
        }
    }

    void trim() {
        PoolThreadCache.trim(this.tinySubPageDirectCaches);
        PoolThreadCache.trim(this.smallSubPageDirectCaches);
        PoolThreadCache.trim(this.normalDirectCaches);
        PoolThreadCache.trim(this.tinySubPageHeapCaches);
        PoolThreadCache.trim(this.smallSubPageHeapCaches);
        PoolThreadCache.trim(this.normalHeapCaches);
    }

    private void free0() {
        int n = PoolThreadCache.free(this.tinySubPageDirectCaches) + PoolThreadCache.free(this.smallSubPageDirectCaches) + PoolThreadCache.free(this.normalDirectCaches) + PoolThreadCache.free(this.tinySubPageHeapCaches) + PoolThreadCache.free(this.smallSubPageHeapCaches) + PoolThreadCache.free(this.normalHeapCaches);
        if (n > 0 && logger.isDebugEnabled()) {
            logger.debug("Freed {} thread-local buffer(s) from thread: {}", (Object)n, (Object)this.thread.getName());
        }
    }

    boolean allocateTiny(PoolArena<?> poolArena, PooledByteBuf<?> pooledByteBuf, int n, int n2) {
        return this.allocate(this.cacheForTiny(poolArena, n2), pooledByteBuf, n);
    }

    boolean allocateSmall(PoolArena<?> poolArena, PooledByteBuf<?> pooledByteBuf, int n, int n2) {
        return this.allocate(this.cacheForSmall(poolArena, n2), pooledByteBuf, n);
    }

    boolean allocateNormal(PoolArena<?> poolArena, PooledByteBuf<?> pooledByteBuf, int n, int n2) {
        return this.allocate(this.cacheForNormal(poolArena, n2), pooledByteBuf, n);
    }

    PoolThreadCache(PoolArena<byte[]> poolArena, PoolArena<ByteBuffer> poolArena2, int n, int n2, int n3, int n4, int n5) {
        if (n4 < 0) {
            throw new IllegalArgumentException("maxCachedBufferCapacity: " + n4 + " (expected: >= 0)");
        }
        if (n5 < 1) {
            throw new IllegalArgumentException("freeSweepAllocationThreshold: " + n4 + " (expected: > 0)");
        }
        this.freeSweepAllocationThreshold = n5;
        this.heapArena = poolArena;
        this.directArena = poolArena2;
        if (poolArena2 != null) {
            this.tinySubPageDirectCaches = PoolThreadCache.createSubPageCaches(n, 32);
            this.smallSubPageDirectCaches = PoolThreadCache.createSubPageCaches(n2, poolArena2.numSmallSubpagePools);
            this.numShiftsNormalDirect = PoolThreadCache.log2(poolArena2.pageSize);
            this.normalDirectCaches = PoolThreadCache.createNormalCaches(n3, n4, poolArena2);
        } else {
            this.tinySubPageDirectCaches = null;
            this.smallSubPageDirectCaches = null;
            this.normalDirectCaches = null;
            this.numShiftsNormalDirect = -1;
        }
        if (poolArena != null) {
            this.tinySubPageHeapCaches = PoolThreadCache.createSubPageCaches(n, 32);
            this.smallSubPageHeapCaches = PoolThreadCache.createSubPageCaches(n2, poolArena.numSmallSubpagePools);
            this.numShiftsNormalHeap = PoolThreadCache.log2(poolArena.pageSize);
            this.normalHeapCaches = PoolThreadCache.createNormalCaches(n3, n4, poolArena);
        } else {
            this.tinySubPageHeapCaches = null;
            this.smallSubPageHeapCaches = null;
            this.normalHeapCaches = null;
            this.numShiftsNormalHeap = -1;
        }
        ThreadDeathWatcher.watch(this.thread, this.freeTask);
    }

    boolean add(PoolArena<?> poolArena, PoolChunk poolChunk, long l, int n) {
        MemoryRegionCache<?> memoryRegionCache = poolArena.isTinyOrSmall(n) ? (PoolArena.isTiny(n) ? this.cacheForTiny(poolArena, n) : this.cacheForSmall(poolArena, n)) : this.cacheForNormal(poolArena, n);
        if (memoryRegionCache == null) {
            return false;
        }
        return memoryRegionCache.add(poolChunk, l);
    }

    private static <T> NormalMemoryRegionCache<T>[] createNormalCaches(int n, int n2, PoolArena<T> poolArena) {
        if (n > 0) {
            int n3 = Math.min(poolArena.chunkSize, n2);
            int n4 = Math.max(1, n3 / poolArena.pageSize);
            NormalMemoryRegionCache[] normalMemoryRegionCacheArray = new NormalMemoryRegionCache[n4];
            for (int i = 0; i < normalMemoryRegionCacheArray.length; ++i) {
                normalMemoryRegionCacheArray[i] = new NormalMemoryRegionCache(n);
            }
            return normalMemoryRegionCacheArray;
        }
        return null;
    }

    private MemoryRegionCache<?> cacheForSmall(PoolArena<?> poolArena, int n) {
        int n2 = PoolArena.smallIdx(n);
        if (poolArena.isDirect()) {
            return PoolThreadCache.cache(this.smallSubPageDirectCaches, n2);
        }
        return PoolThreadCache.cache(this.smallSubPageHeapCaches, n2);
    }

    private static int free(MemoryRegionCache<?> memoryRegionCache) {
        if (memoryRegionCache == null) {
            return 0;
        }
        return memoryRegionCache.free();
    }

    private MemoryRegionCache<?> cacheForTiny(PoolArena<?> poolArena, int n) {
        int n2 = PoolArena.tinyIdx(n);
        if (poolArena.isDirect()) {
            return PoolThreadCache.cache(this.tinySubPageDirectCaches, n2);
        }
        return PoolThreadCache.cache(this.tinySubPageHeapCaches, n2);
    }

    private static <T> SubPageMemoryRegionCache<T>[] createSubPageCaches(int n, int n2) {
        if (n > 0) {
            SubPageMemoryRegionCache[] subPageMemoryRegionCacheArray = new SubPageMemoryRegionCache[n2];
            for (int i = 0; i < subPageMemoryRegionCacheArray.length; ++i) {
                subPageMemoryRegionCacheArray[i] = new SubPageMemoryRegionCache(n);
            }
            return subPageMemoryRegionCacheArray;
        }
        return null;
    }

    void free() {
        ThreadDeathWatcher.unwatch(this.thread, this.freeTask);
        this.free0();
    }

    private boolean allocate(MemoryRegionCache<?> memoryRegionCache, PooledByteBuf pooledByteBuf, int n) {
        if (memoryRegionCache == null) {
            return false;
        }
        boolean bl = memoryRegionCache.allocate(pooledByteBuf, n);
        if (++this.allocations >= this.freeSweepAllocationThreshold) {
            this.allocations = 0;
            this.trim();
        }
        return bl;
    }

    private static <T> MemoryRegionCache<T> cache(MemoryRegionCache<T>[] memoryRegionCacheArray, int n) {
        if (memoryRegionCacheArray == null || n > memoryRegionCacheArray.length - 1) {
            return null;
        }
        return memoryRegionCacheArray[n];
    }

    private static int log2(int n) {
        int n2 = 0;
        while (n > 1) {
            n >>= 1;
            ++n2;
        }
        return n2;
    }

    private static void trim(MemoryRegionCache<?> memoryRegionCache) {
        if (memoryRegionCache == null) {
            return;
        }
        ((MemoryRegionCache)memoryRegionCache).trim();
    }

    private static final class NormalMemoryRegionCache<T>
    extends MemoryRegionCache<T> {
        NormalMemoryRegionCache(int n) {
            super(n);
        }

        @Override
        protected void initBuf(PoolChunk<T> poolChunk, long l, PooledByteBuf<T> pooledByteBuf, int n) {
            poolChunk.initBuf(pooledByteBuf, l, n);
        }
    }

    private static abstract class MemoryRegionCache<T> {
        private int maxEntriesInUse;
        private final int maxUnusedCached;
        private final Entry<T>[] entries;
        private int tail;
        private int entriesInUse;
        private int head;

        private static int powerOfTwo(int n) {
            if (n <= 2) {
                return 2;
            }
            --n;
            n |= n >> 1;
            n |= n >> 2;
            n |= n >> 4;
            n |= n >> 8;
            n |= n >> 16;
            return ++n;
        }

        MemoryRegionCache(int n) {
            this.entries = new Entry[MemoryRegionCache.powerOfTwo(n)];
            for (int i = 0; i < this.entries.length; ++i) {
                this.entries[i] = new Entry();
            }
            this.maxUnusedCached = n / 2;
        }

        public int free() {
            int n = 0;
            this.entriesInUse = 0;
            this.maxEntriesInUse = 0;
            int n2 = this.head;
            while (true) {
                if (MemoryRegionCache.freeEntry(this.entries[n2])) {
                    ++n;
                } else {
                    return n;
                }
                n2 = this.nextIdx(n2);
            }
        }

        private static boolean freeEntry(Entry entry) {
            PoolChunk poolChunk = entry.chunk;
            if (poolChunk == null) {
                return false;
            }
            PoolArena poolArena = poolChunk.arena;
            synchronized (poolArena) {
                poolChunk.parent.free(poolChunk, entry.handle);
            }
            entry.chunk = null;
            return true;
        }

        protected abstract void initBuf(PoolChunk<T> var1, long var2, PooledByteBuf<T> var4, int var5);

        private int size() {
            return this.tail - this.head & this.entries.length - 1;
        }

        public boolean allocate(PooledByteBuf<T> pooledByteBuf, int n) {
            Entry<T> entry = this.entries[this.head];
            if (entry.chunk == null) {
                return false;
            }
            ++this.entriesInUse;
            if (this.maxEntriesInUse < this.entriesInUse) {
                this.maxEntriesInUse = this.entriesInUse;
            }
            this.initBuf(entry.chunk, entry.handle, pooledByteBuf, n);
            entry.chunk = null;
            this.head = this.nextIdx(this.head);
            return true;
        }

        private int nextIdx(int n) {
            return n + 1 & this.entries.length - 1;
        }

        private void trim() {
            int n;
            this.entriesInUse = 0;
            this.maxEntriesInUse = 0;
            if (n <= this.maxUnusedCached) {
                return;
            }
            int n2 = this.head;
            for (n = this.size() - this.maxEntriesInUse; n > 0; --n) {
                if (!MemoryRegionCache.freeEntry(this.entries[n2])) {
                    return;
                }
                n2 = this.nextIdx(n2);
            }
        }

        public boolean add(PoolChunk<T> poolChunk, long l) {
            Entry<T> entry = this.entries[this.tail];
            if (entry.chunk != null) {
                return false;
            }
            --this.entriesInUse;
            entry.chunk = poolChunk;
            entry.handle = l;
            this.tail = this.nextIdx(this.tail);
            return true;
        }

        private static final class Entry<T> {
            long handle;
            PoolChunk<T> chunk;

            private Entry() {
            }
        }
    }

    private static final class SubPageMemoryRegionCache<T>
    extends MemoryRegionCache<T> {
        SubPageMemoryRegionCache(int n) {
            super(n);
        }

        @Override
        protected void initBuf(PoolChunk<T> poolChunk, long l, PooledByteBuf<T> pooledByteBuf, int n) {
            poolChunk.initBufWithSubpage(pooledByteBuf, l, n);
        }
    }
}

