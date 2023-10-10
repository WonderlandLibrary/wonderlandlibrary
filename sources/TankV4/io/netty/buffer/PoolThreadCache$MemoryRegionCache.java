package io.netty.buffer;

abstract class PoolThreadCache$MemoryRegionCache {
   private final PoolThreadCache$MemoryRegionCache$Entry[] entries;
   private final int maxUnusedCached;
   private int head;
   private int tail;
   private int maxEntriesInUse;
   private int entriesInUse;

   PoolThreadCache$MemoryRegionCache(int var1) {
      this.entries = new PoolThreadCache$MemoryRegionCache$Entry[powerOfTwo(var1)];

      for(int var2 = 0; var2 < this.entries.length; ++var2) {
         this.entries[var2] = new PoolThreadCache$MemoryRegionCache$Entry((PoolThreadCache$1)null);
      }

      this.maxUnusedCached = var1 / 2;
   }

   private static int powerOfTwo(int var0) {
      if (var0 <= 2) {
         return 2;
      } else {
         --var0;
         var0 |= var0 >> 1;
         var0 |= var0 >> 2;
         var0 |= var0 >> 4;
         var0 |= var0 >> 8;
         var0 |= var0 >> 16;
         ++var0;
         return var0;
      }
   }

   protected abstract void initBuf(PoolChunk var1, long var2, PooledByteBuf var4, int var5);

   public boolean add(PoolChunk var1, long var2) {
      PoolThreadCache$MemoryRegionCache$Entry var4 = this.entries[this.tail];
      if (var4.chunk != null) {
         return false;
      } else {
         --this.entriesInUse;
         var4.chunk = var1;
         var4.handle = var2;
         this.tail = this.nextIdx(this.tail);
         return true;
      }
   }

   public boolean allocate(PooledByteBuf var1, int var2) {
      PoolThreadCache$MemoryRegionCache$Entry var3 = this.entries[this.head];
      if (var3.chunk == null) {
         return false;
      } else {
         ++this.entriesInUse;
         if (this.maxEntriesInUse < this.entriesInUse) {
            this.maxEntriesInUse = this.entriesInUse;
         }

         this.initBuf(var3.chunk, var3.handle, var1, var2);
         var3.chunk = null;
         this.head = this.nextIdx(this.head);
         return true;
      }
   }

   public int free() {
      int var1 = 0;
      this.entriesInUse = 0;
      this.maxEntriesInUse = 0;

      for(int var2 = this.head; this.entries[var2] == null; var2 = this.nextIdx(var2)) {
         ++var1;
      }

      return var1;
   }

   private void trim() {
      int var1 = this.size() - this.maxEntriesInUse;
      this.entriesInUse = 0;
      this.maxEntriesInUse = 0;
      if (var1 > this.maxUnusedCached) {
         for(int var2 = this.head; var1 > 0; --var1) {
            if (this.entries[var2] == null) {
               return;
            }

            var2 = this.nextIdx(var2);
         }

      }
   }

   private int size() {
      return this.tail - this.head & this.entries.length - 1;
   }

   private int nextIdx(int var1) {
      return var1 + 1 & this.entries.length - 1;
   }

   static void access$100(PoolThreadCache$MemoryRegionCache var0) {
      var0.trim();
   }
}
