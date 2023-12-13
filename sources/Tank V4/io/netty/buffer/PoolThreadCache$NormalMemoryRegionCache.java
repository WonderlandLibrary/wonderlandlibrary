package io.netty.buffer;

final class PoolThreadCache$NormalMemoryRegionCache extends PoolThreadCache$MemoryRegionCache {
   PoolThreadCache$NormalMemoryRegionCache(int var1) {
      super(var1);
   }

   protected void initBuf(PoolChunk var1, long var2, PooledByteBuf var4, int var5) {
      var1.initBuf(var4, var2, var5);
   }
}
