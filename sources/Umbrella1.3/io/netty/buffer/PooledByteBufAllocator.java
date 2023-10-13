/*
 * Decompiled with CFR 0.150.
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
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class PooledByteBufAllocator
extends AbstractByteBufAllocator {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
    private static final int DEFAULT_NUM_HEAP_ARENA;
    private static final int DEFAULT_NUM_DIRECT_ARENA;
    private static final int DEFAULT_PAGE_SIZE;
    private static final int DEFAULT_MAX_ORDER;
    private static final int MIN_PAGE_SIZE = 4096;
    private static final int MAX_CHUNK_SIZE = 0x40000000;
    public static final PooledByteBufAllocator DEFAULT;
    private final PoolArena<byte[]>[] heapArenas;
    private final PoolArena<ByteBuffer>[] directArenas;
    final ThreadLocal<PoolThreadCache> threadCache = new ThreadLocal<PoolThreadCache>(){
        private final AtomicInteger index = new AtomicInteger();

        @Override
        protected PoolThreadCache initialValue() {
            int idx = this.index.getAndIncrement();
            PoolArena heapArena = PooledByteBufAllocator.this.heapArenas != null ? PooledByteBufAllocator.this.heapArenas[Math.abs(idx % PooledByteBufAllocator.this.heapArenas.length)] : null;
            PoolArena directArena = PooledByteBufAllocator.this.directArenas != null ? PooledByteBufAllocator.this.directArenas[Math.abs(idx % PooledByteBufAllocator.this.directArenas.length)] : null;
            return new PoolThreadCache(heapArena, directArena);
        }
    };

    public PooledByteBufAllocator() {
        this(false);
    }

    public PooledByteBufAllocator(boolean preferDirect) {
        this(preferDirect, DEFAULT_NUM_HEAP_ARENA, DEFAULT_NUM_DIRECT_ARENA, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ORDER);
    }

    public PooledByteBufAllocator(int nHeapArena, int nDirectArena, int pageSize, int maxOrder) {
        this(false, nHeapArena, nDirectArena, pageSize, maxOrder);
    }

    public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder) {
        super(preferDirect);
        int i;
        int chunkSize = PooledByteBufAllocator.validateAndCalculateChunkSize(pageSize, maxOrder);
        if (nHeapArena < 0) {
            throw new IllegalArgumentException("nHeapArena: " + nHeapArena + " (expected: >= 0)");
        }
        if (nDirectArena < 0) {
            throw new IllegalArgumentException("nDirectArea: " + nDirectArena + " (expected: >= 0)");
        }
        int pageShifts = PooledByteBufAllocator.validateAndCalculatePageShifts(pageSize);
        if (nHeapArena > 0) {
            this.heapArenas = PooledByteBufAllocator.newArenaArray(nHeapArena);
            for (i = 0; i < this.heapArenas.length; ++i) {
                this.heapArenas[i] = new PoolArena.HeapArena(this, pageSize, maxOrder, pageShifts, chunkSize);
            }
        } else {
            this.heapArenas = null;
        }
        if (nDirectArena > 0) {
            this.directArenas = PooledByteBufAllocator.newArenaArray(nDirectArena);
            for (i = 0; i < this.directArenas.length; ++i) {
                this.directArenas[i] = new PoolArena.DirectArena(this, pageSize, maxOrder, pageShifts, chunkSize);
            }
        } else {
            this.directArenas = null;
        }
    }

    private static <T> PoolArena<T>[] newArenaArray(int size) {
        return new PoolArena[size];
    }

    private static int validateAndCalculatePageShifts(int pageSize) {
        if (pageSize < 4096) {
            throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: 4096+)");
        }
        boolean found1 = false;
        int pageShifts = 0;
        for (int i = pageSize; i != 0; i >>= 1) {
            if ((i & 1) != 0) {
                if (!found1) {
                    found1 = true;
                    continue;
                }
                throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: power of 2");
            }
            if (found1) continue;
            ++pageShifts;
        }
        return pageShifts;
    }

    private static int validateAndCalculateChunkSize(int pageSize, int maxOrder) {
        if (maxOrder > 14) {
            throw new IllegalArgumentException("maxOrder: " + maxOrder + " (expected: 0-14)");
        }
        int chunkSize = pageSize;
        for (int i = maxOrder; i > 0; --i) {
            if (chunkSize > 0x20000000) {
                throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", pageSize, maxOrder, 0x40000000));
            }
            chunkSize <<= 1;
        }
        return chunkSize;
    }

    @Override
    protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
        PoolThreadCache cache = this.threadCache.get();
        PoolArena<byte[]> heapArena = cache.heapArena;
        AbstractReferenceCountedByteBuf buf = heapArena != null ? heapArena.allocate(cache, initialCapacity, maxCapacity) : new UnpooledHeapByteBuf((ByteBufAllocator)this, initialCapacity, maxCapacity);
        return PooledByteBufAllocator.toLeakAwareBuffer(buf);
    }

    @Override
    protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
        PoolThreadCache cache = this.threadCache.get();
        PoolArena<ByteBuffer> directArena = cache.directArena;
        AbstractReferenceCountedByteBuf buf = directArena != null ? directArena.allocate(cache, initialCapacity, maxCapacity) : (PlatformDependent.hasUnsafe() ? new UnpooledUnsafeDirectByteBuf((ByteBufAllocator)this, initialCapacity, maxCapacity) : new UnpooledDirectByteBuf((ByteBufAllocator)this, initialCapacity, maxCapacity));
        return PooledByteBufAllocator.toLeakAwareBuffer(buf);
    }

    @Override
    public boolean isDirectBufferPooled() {
        return this.directArenas != null;
    }

    static {
        int defaultPageSize = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
        Throwable pageSizeFallbackCause = null;
        try {
            PooledByteBufAllocator.validateAndCalculatePageShifts(defaultPageSize);
        }
        catch (Throwable t) {
            pageSizeFallbackCause = t;
            defaultPageSize = 8192;
        }
        DEFAULT_PAGE_SIZE = defaultPageSize;
        int defaultMaxOrder = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
        Throwable maxOrderFallbackCause = null;
        try {
            PooledByteBufAllocator.validateAndCalculateChunkSize(DEFAULT_PAGE_SIZE, defaultMaxOrder);
        }
        catch (Throwable t) {
            maxOrderFallbackCause = t;
            defaultMaxOrder = 11;
        }
        DEFAULT_MAX_ORDER = defaultMaxOrder;
        Runtime runtime = Runtime.getRuntime();
        int defaultChunkSize = DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER;
        DEFAULT_NUM_HEAP_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", (int)Math.min((long)runtime.availableProcessors(), Runtime.getRuntime().maxMemory() / (long)defaultChunkSize / 2L / 3L)));
        DEFAULT_NUM_DIRECT_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", (int)Math.min((long)runtime.availableProcessors(), PlatformDependent.maxDirectMemory() / (long)defaultChunkSize / 2L / 3L)));
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.allocator.numHeapArenas: {}", (Object)DEFAULT_NUM_HEAP_ARENA);
            logger.debug("-Dio.netty.allocator.numDirectArenas: {}", (Object)DEFAULT_NUM_DIRECT_ARENA);
            if (pageSizeFallbackCause == null) {
                logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)DEFAULT_PAGE_SIZE);
            } else {
                logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)DEFAULT_PAGE_SIZE, (Object)pageSizeFallbackCause);
            }
            if (maxOrderFallbackCause == null) {
                logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)DEFAULT_MAX_ORDER);
            } else {
                logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)DEFAULT_MAX_ORDER, (Object)maxOrderFallbackCause);
            }
            logger.debug("-Dio.netty.allocator.chunkSize: {}", (Object)(DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER));
        }
        DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
    }
}

