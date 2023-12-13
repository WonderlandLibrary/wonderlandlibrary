package io.netty.buffer;

final class PoolThreadCache$SubPageMemoryRegionCache extends PoolThreadCache$MemoryRegionCache {
   PoolThreadCache$SubPageMemoryRegionCache(int var1) {
      super(var1);
   }

   protected void initBuf(PoolChunk var1, long var2, PooledByteBuf var4, int var5) {
      var1.initBufWithSubpage(var4, var2, var5);
   }
}
