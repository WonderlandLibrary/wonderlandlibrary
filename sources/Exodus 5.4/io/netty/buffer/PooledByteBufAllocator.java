/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolThreadCache;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class PooledByteBufAllocator
extends AbstractByteBufAllocator {
    private static final int DEFAULT_NUM_DIRECT_ARENA;
    public static final PooledByteBufAllocator DEFAULT;
    private static final int DEFAULT_MAX_CACHED_BUFFER_CAPACITY;
    private static final int DEFAULT_SMALL_CACHE_SIZE;
    final PoolThreadLocalCache threadCache = new PoolThreadLocalCache();
    private static final int DEFAULT_NORMAL_CACHE_SIZE;
    private static final int MIN_PAGE_SIZE = 4096;
    private static final int DEFAULT_NUM_HEAP_ARENA;
    private final PoolArena<ByteBuffer>[] directArenas;
    private final int tinyCacheSize;
    private static final int DEFAULT_TINY_CACHE_SIZE;
    private static final int DEFAULT_CACHE_TRIM_INTERVAL;
    private static final int DEFAULT_PAGE_SIZE;
    private static final int DEFAULT_MAX_ORDER;
    private static final int MAX_CHUNK_SIZE = 0x40000000;
    private final int normalCacheSize;
    private final int smallCacheSize;
    private static final InternalLogger logger;
    private final PoolArena<byte[]>[] heapArenas;

    @Deprecated
    public PooledByteBufAllocator(boolean bl, int n, int n2, int n3, int n4, int n5, int n6, int n7, long l) {
        this(bl, n, n2, n3, n4, n5, n6, n7);
    }

    static {
        logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
        int n = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
        Throwable throwable = null;
        try {
            PooledByteBufAllocator.validateAndCalculatePageShifts(n);
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            n = 8192;
        }
        DEFAULT_PAGE_SIZE = n;
        int n2 = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
        Throwable throwable3 = null;
        try {
            PooledByteBufAllocator.validateAndCalculateChunkSize(DEFAULT_PAGE_SIZE, n2);
        }
        catch (Throwable throwable4) {
            throwable3 = throwable4;
            n2 = 11;
        }
        DEFAULT_MAX_ORDER = n2;
        Runtime runtime = Runtime.getRuntime();
        int n3 = DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER;
        DEFAULT_NUM_HEAP_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", (int)Math.min((long)runtime.availableProcessors(), Runtime.getRuntime().maxMemory() / (long)n3 / 2L / 3L)));
        DEFAULT_NUM_DIRECT_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", (int)Math.min((long)runtime.availableProcessors(), PlatformDependent.maxDirectMemory() / (long)n3 / 2L / 3L)));
        DEFAULT_TINY_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.tinyCacheSize", 512);
        DEFAULT_SMALL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.smallCacheSize", 256);
        DEFAULT_NORMAL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.normalCacheSize", 64);
        DEFAULT_MAX_CACHED_BUFFER_CAPACITY = SystemPropertyUtil.getInt("io.netty.allocator.maxCachedBufferCapacity", 32768);
        DEFAULT_CACHE_TRIM_INTERVAL = SystemPropertyUtil.getInt("io.netty.allocator.cacheTrimInterval", 8192);
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.allocator.numHeapArenas: {}", (Object)DEFAULT_NUM_HEAP_ARENA);
            logger.debug("-Dio.netty.allocator.numDirectArenas: {}", (Object)DEFAULT_NUM_DIRECT_ARENA);
            if (throwable == null) {
                logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)DEFAULT_PAGE_SIZE);
            } else {
                logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)DEFAULT_PAGE_SIZE, (Object)throwable);
            }
            if (throwable3 == null) {
                logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)DEFAULT_MAX_ORDER);
            } else {
                logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)DEFAULT_MAX_ORDER, (Object)throwable3);
            }
            logger.debug("-Dio.netty.allocator.chunkSize: {}", (Object)(DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER));
            logger.debug("-Dio.netty.allocator.tinyCacheSize: {}", (Object)DEFAULT_TINY_CACHE_SIZE);
            logger.debug("-Dio.netty.allocator.smallCacheSize: {}", (Object)DEFAULT_SMALL_CACHE_SIZE);
            logger.debug("-Dio.netty.allocator.normalCacheSize: {}", (Object)DEFAULT_NORMAL_CACHE_SIZE);
            logger.debug("-Dio.netty.allocator.maxCachedBufferCapacity: {}", (Object)DEFAULT_MAX_CACHED_BUFFER_CAPACITY);
            logger.debug("-Dio.netty.allocator.cacheTrimInterval: {}", (Object)DEFAULT_CACHE_TRIM_INTERVAL);
        }
        DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
    }

    private static int validateAndCalculatePageShifts(int n) {
        if (n < 4096) {
            throw new IllegalArgumentException("pageSize: " + n + " (expected: " + 4096 + "+)");
        }
        if ((n & n - 1) != 0) {
            throw new IllegalArgumentException("pageSize: " + n + " (expected: power of 2)");
        }
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    @Deprecated
    public void freeThreadLocalCache() {
        this.threadCache.remove();
    }

    private static <T> PoolArena<T>[] newArenaArray(int n) {
        return new PoolArena[n];
    }

    @Override
    protected ByteBuf newDirectBuffer(int n, int n2) {
        PoolThreadCache poolThreadCache = (PoolThreadCache)this.threadCache.get();
        PoolArena<ByteBuffer> poolArena = poolThreadCache.directArena;
        AbstractReferenceCountedByteBuf abstractReferenceCountedByteBuf = poolArena != null ? poolArena.allocate(poolThreadCache, n, n2) : (PlatformDependent.hasUnsafe() ? new UnpooledUnsafeDirectByteBuf((ByteBufAllocator)this, n, n2) : new UnpooledDirectByteBuf((ByteBufAllocator)this, n, n2));
        return PooledByteBufAllocator.toLeakAwareBuffer(abstractReferenceCountedByteBuf);
    }

    public PooledByteBufAllocator() {
        this(false);
    }

    public PooledByteBufAllocator(boolean bl, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        super(bl);
        int n8;
        this.tinyCacheSize = n5;
        this.smallCacheSize = n6;
        this.normalCacheSize = n7;
        int n9 = PooledByteBufAllocator.validateAndCalculateChunkSize(n3, n4);
        if (n < 0) {
            throw new IllegalArgumentException("nHeapArena: " + n + " (expected: >= 0)");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("nDirectArea: " + n2 + " (expected: >= 0)");
        }
        int n10 = PooledByteBufAllocator.validateAndCalculatePageShifts(n3);
        if (n > 0) {
            this.heapArenas = PooledByteBufAllocator.newArenaArray(n);
            for (n8 = 0; n8 < this.heapArenas.length; ++n8) {
                this.heapArenas[n8] = new PoolArena.HeapArena(this, n3, n4, n10, n9);
            }
        } else {
            this.heapArenas = null;
        }
        if (n2 > 0) {
            this.directArenas = PooledByteBufAllocator.newArenaArray(n2);
            for (n8 = 0; n8 < this.directArenas.length; ++n8) {
                this.directArenas[n8] = new PoolArena.DirectArena(this, n3, n4, n10, n9);
            }
        } else {
            this.directArenas = null;
        }
    }

    private static int validateAndCalculateChunkSize(int n, int n2) {
        if (n2 > 14) {
            throw new IllegalArgumentException("maxOrder: " + n2 + " (expected: 0-14)");
        }
        int n3 = n;
        for (int i = n2; i > 0; --i) {
            if (n3 > 0x20000000) {
                throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", n, n2, 0x40000000));
            }
            n3 <<= 1;
        }
        return n3;
    }

    @Override
    protected ByteBuf newHeapBuffer(int n, int n2) {
        PoolThreadCache poolThreadCache = (PoolThreadCache)this.threadCache.get();
        PoolArena<byte[]> poolArena = poolThreadCache.heapArena;
        AbstractReferenceCountedByteBuf abstractReferenceCountedByteBuf = poolArena != null ? poolArena.allocate(poolThreadCache, n, n2) : new UnpooledHeapByteBuf((ByteBufAllocator)this, n, n2);
        return PooledByteBufAllocator.toLeakAwareBuffer(abstractReferenceCountedByteBuf);
    }

    public PooledByteBufAllocator(int n, int n2, int n3, int n4) {
        this(false, n, n2, n3, n4);
    }

    public PooledByteBufAllocator(boolean bl) {
        this(bl, DEFAULT_NUM_HEAP_ARENA, DEFAULT_NUM_DIRECT_ARENA, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ORDER);
    }

    @Deprecated
    public boolean hasThreadLocalCache() {
        return this.threadCache.isSet();
    }

    @Override
    public boolean isDirectBufferPooled() {
        return this.directArenas != null;
    }

    public PooledByteBufAllocator(boolean bl, int n, int n2, int n3, int n4) {
        this(bl, n, n2, n3, n4, DEFAULT_TINY_CACHE_SIZE, DEFAULT_SMALL_CACHE_SIZE, DEFAULT_NORMAL_CACHE_SIZE);
    }

    final class PoolThreadLocalCache
    extends FastThreadLocal<PoolThreadCache> {
        private final AtomicInteger index = new AtomicInteger();

        PoolThreadLocalCache() {
        }

        @Override
        protected void onRemoval(PoolThreadCache poolThreadCache) {
            poolThreadCache.free();
        }

        @Override
        protected PoolThreadCache initialValue() {
            int n = this.index.getAndIncrement();
            PoolArena poolArena = PooledByteBufAllocator.this.heapArenas != null ? PooledByteBufAllocator.this.heapArenas[Math.abs(n % PooledByteBufAllocator.this.heapArenas.length)] : null;
            PoolArena poolArena2 = PooledByteBufAllocator.this.directArenas != null ? PooledByteBufAllocator.this.directArenas[Math.abs(n % PooledByteBufAllocator.this.directArenas.length)] : null;
            return new PoolThreadCache(poolArena, poolArena2, PooledByteBufAllocator.this.tinyCacheSize, PooledByteBufAllocator.this.smallCacheSize, PooledByteBufAllocator.this.normalCacheSize, DEFAULT_MAX_CACHED_BUFFER_CAPACITY, DEFAULT_CACHE_TRIM_INTERVAL);
        }
    }
}

