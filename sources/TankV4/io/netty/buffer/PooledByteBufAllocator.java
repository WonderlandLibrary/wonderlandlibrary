package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PooledByteBufAllocator extends AbstractByteBufAllocator {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
   private static final int DEFAULT_NUM_HEAP_ARENA;
   private static final int DEFAULT_NUM_DIRECT_ARENA;
   private static final int DEFAULT_PAGE_SIZE;
   private static final int DEFAULT_MAX_ORDER;
   private static final int MIN_PAGE_SIZE = 4096;
   private static final int MAX_CHUNK_SIZE = 1073741824;
   public static final PooledByteBufAllocator DEFAULT;
   private final PoolArena[] heapArenas;
   private final PoolArena[] directArenas;
   final ThreadLocal threadCache;

   public PooledByteBufAllocator() {
      this(false);
   }

   public PooledByteBufAllocator(boolean var1) {
      this(var1, DEFAULT_NUM_HEAP_ARENA, DEFAULT_NUM_DIRECT_ARENA, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ORDER);
   }

   public PooledByteBufAllocator(int var1, int var2, int var3, int var4) {
      this(false, var1, var2, var3, var4);
   }

   public PooledByteBufAllocator(boolean var1, int var2, int var3, int var4, int var5) {
      super(var1);
      this.threadCache = new ThreadLocal(this) {
         private final AtomicInteger index;
         final PooledByteBufAllocator this$0;

         {
            this.this$0 = var1;
            this.index = new AtomicInteger();
         }

         protected PoolThreadCache initialValue() {
            int var1 = this.index.getAndIncrement();
            PoolArena var2;
            if (PooledByteBufAllocator.access$000(this.this$0) != null) {
               var2 = PooledByteBufAllocator.access$000(this.this$0)[Math.abs(var1 % PooledByteBufAllocator.access$000(this.this$0).length)];
            } else {
               var2 = null;
            }

            PoolArena var3;
            if (PooledByteBufAllocator.access$100(this.this$0) != null) {
               var3 = PooledByteBufAllocator.access$100(this.this$0)[Math.abs(var1 % PooledByteBufAllocator.access$100(this.this$0).length)];
            } else {
               var3 = null;
            }

            return new PoolThreadCache(var2, var3);
         }

         protected Object initialValue() {
            return this.initialValue();
         }
      };
      int var6 = validateAndCalculateChunkSize(var4, var5);
      if (var2 < 0) {
         throw new IllegalArgumentException("nHeapArena: " + var2 + " (expected: >= 0)");
      } else if (var3 < 0) {
         throw new IllegalArgumentException("nDirectArea: " + var3 + " (expected: >= 0)");
      } else {
         int var7 = validateAndCalculatePageShifts(var4);
         int var8;
         if (var2 > 0) {
            this.heapArenas = newArenaArray(var2);

            for(var8 = 0; var8 < this.heapArenas.length; ++var8) {
               this.heapArenas[var8] = new PoolArena.HeapArena(this, var4, var5, var7, var6);
            }
         } else {
            this.heapArenas = null;
         }

         if (var3 > 0) {
            this.directArenas = newArenaArray(var3);

            for(var8 = 0; var8 < this.directArenas.length; ++var8) {
               this.directArenas[var8] = new PoolArena.DirectArena(this, var4, var5, var7, var6);
            }
         } else {
            this.directArenas = null;
         }

      }
   }

   private static PoolArena[] newArenaArray(int var0) {
      return new PoolArena[var0];
   }

   private static int validateAndCalculatePageShifts(int var0) {
      if (var0 < 4096) {
         throw new IllegalArgumentException("pageSize: " + var0 + " (expected: 4096+)");
      } else {
         boolean var1 = false;
         int var2 = 0;

         for(int var3 = var0; var3 != 0; var3 >>= 1) {
            if ((var3 & 1) != 0) {
               if (var1) {
                  throw new IllegalArgumentException("pageSize: " + var0 + " (expected: power of 2");
               }

               var1 = true;
            } else if (!var1) {
               ++var2;
            }
         }

         return var2;
      }
   }

   private static int validateAndCalculateChunkSize(int var0, int var1) {
      if (var1 > 14) {
         throw new IllegalArgumentException("maxOrder: " + var1 + " (expected: 0-14)");
      } else {
         int var2 = var0;

         for(int var3 = var1; var3 > 0; --var3) {
            if (var2 > 536870912) {
               throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", var0, var1, 1073741824));
            }

            var2 <<= 1;
         }

         return var2;
      }
   }

   protected ByteBuf newHeapBuffer(int var1, int var2) {
      PoolThreadCache var3 = (PoolThreadCache)this.threadCache.get();
      PoolArena var4 = var3.heapArena;
      Object var5;
      if (var4 != null) {
         var5 = var4.allocate(var3, var1, var2);
      } else {
         var5 = new UnpooledHeapByteBuf(this, var1, var2);
      }

      return toLeakAwareBuffer((ByteBuf)var5);
   }

   protected ByteBuf newDirectBuffer(int var1, int var2) {
      PoolThreadCache var3 = (PoolThreadCache)this.threadCache.get();
      PoolArena var4 = var3.directArena;
      Object var5;
      if (var4 != null) {
         var5 = var4.allocate(var3, var1, var2);
      } else if (PlatformDependent.hasUnsafe()) {
         var5 = new UnpooledUnsafeDirectByteBuf(this, var1, var2);
      } else {
         var5 = new UnpooledDirectByteBuf(this, var1, var2);
      }

      return toLeakAwareBuffer((ByteBuf)var5);
   }

   public boolean isDirectBufferPooled() {
      return this.directArenas != null;
   }

   static PoolArena[] access$000(PooledByteBufAllocator var0) {
      return var0.heapArenas;
   }

   static PoolArena[] access$100(PooledByteBufAllocator var0) {
      return var0.directArenas;
   }

   static {
      int var0 = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
      Throwable var1 = null;

      try {
         validateAndCalculatePageShifts(var0);
      } catch (Throwable var7) {
         var1 = var7;
         var0 = 8192;
      }

      DEFAULT_PAGE_SIZE = var0;
      int var2 = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
      Throwable var3 = null;

      try {
         validateAndCalculateChunkSize(DEFAULT_PAGE_SIZE, var2);
      } catch (Throwable var6) {
         var3 = var6;
         var2 = 11;
      }

      DEFAULT_MAX_ORDER = var2;
      Runtime var4 = Runtime.getRuntime();
      int var5 = DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER;
      DEFAULT_NUM_HEAP_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", (int)Math.min((long)var4.availableProcessors(), Runtime.getRuntime().maxMemory() / (long)var5 / 2L / 3L)));
      DEFAULT_NUM_DIRECT_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", (int)Math.min((long)var4.availableProcessors(), PlatformDependent.maxDirectMemory() / (long)var5 / 2L / 3L)));
      if (logger.isDebugEnabled()) {
         logger.debug("-Dio.netty.allocator.numHeapArenas: {}", (Object)DEFAULT_NUM_HEAP_ARENA);
         logger.debug("-Dio.netty.allocator.numDirectArenas: {}", (Object)DEFAULT_NUM_DIRECT_ARENA);
         if (var1 == null) {
            logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)DEFAULT_PAGE_SIZE);
         } else {
            logger.debug("-Dio.netty.allocator.pageSize: {}", DEFAULT_PAGE_SIZE, var1);
         }

         if (var3 == null) {
            logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)DEFAULT_MAX_ORDER);
         } else {
            logger.debug("-Dio.netty.allocator.maxOrder: {}", DEFAULT_MAX_ORDER, var3);
         }

         logger.debug("-Dio.netty.allocator.chunkSize: {}", (Object)(DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER));
      }

      DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
   }
}
