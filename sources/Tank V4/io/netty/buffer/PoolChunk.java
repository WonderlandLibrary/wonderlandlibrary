package io.netty.buffer;

final class PoolChunk {
   private static final int ST_UNUSED = 0;
   private static final int ST_BRANCH = 1;
   private static final int ST_ALLOCATED = 2;
   private static final int ST_ALLOCATED_SUBPAGE = 3;
   private static final long multiplier = 25214903917L;
   private static final long addend = 11L;
   private static final long mask = 281474976710655L;
   final PoolArena arena;
   final Object memory;
   final boolean unpooled;
   private final int[] memoryMap;
   private final PoolSubpage[] subpages;
   private final int subpageOverflowMask;
   private final int pageSize;
   private final int pageShifts;
   private final int chunkSize;
   private final int maxSubpageAllocs;
   private long random = (System.nanoTime() ^ 25214903917L) & 281474976710655L;
   private int freeBytes;
   PoolChunkList parent;
   PoolChunk prev;
   PoolChunk next;
   static final boolean $assertionsDisabled = !PoolChunk.class.desiredAssertionStatus();

   PoolChunk(PoolArena var1, Object var2, int var3, int var4, int var5, int var6) {
      this.unpooled = false;
      this.arena = var1;
      this.memory = var2;
      this.pageSize = var3;
      this.pageShifts = var5;
      this.chunkSize = var6;
      this.subpageOverflowMask = ~(var3 - 1);
      this.freeBytes = var6;
      int var7 = var6 >>> var5;
      this.maxSubpageAllocs = 1 << var4;
      this.memoryMap = new int[this.maxSubpageAllocs << 1];
      int var8 = 1;

      for(int var9 = 0; var9 <= var4; ++var9) {
         int var10 = var7 >>> var9;

         for(int var11 = 0; var11 < var7; var11 += var10) {
            this.memoryMap[var8++] = var11 << 17 | var10 << 2 | 0;
         }
      }

      this.subpages = this.newSubpageArray(this.maxSubpageAllocs);
   }

   PoolChunk(PoolArena var1, Object var2, int var3) {
      this.unpooled = true;
      this.arena = var1;
      this.memory = var2;
      this.memoryMap = null;
      this.subpages = null;
      this.subpageOverflowMask = 0;
      this.pageSize = 0;
      this.pageShifts = 0;
      this.chunkSize = var3;
      this.maxSubpageAllocs = 0;
   }

   private PoolSubpage[] newSubpageArray(int var1) {
      return new PoolSubpage[var1];
   }

   int usage() {
      if (this.freeBytes == 0) {
         return 100;
      } else {
         int var1 = (int)((long)this.freeBytes * 100L / (long)this.chunkSize);
         return var1 == 0 ? 99 : 100 - var1;
      }
   }

   long allocate(int var1) {
      int var2 = this.memoryMap[1];
      return (var1 & this.subpageOverflowMask) != 0 ? this.allocateRun(var1, 1, var2) : this.allocateSubpage(var1, 1, var2);
   }

   private long allocateRun(int var1, int var2, int var3) {
      while((var3 & 2) == 0) {
         if ((var3 & 1) == 0) {
            return this.allocateRunSimple(var1, var2, var3);
         }

         int var4 = var2 << 1 ^ this.nextRandom();
         long var5 = this.allocateRun(var1, var4, this.memoryMap[var4]);
         if (var5 > 0L) {
            return var5;
         }

         var2 = var4 ^ 1;
         var3 = this.memoryMap[var2];
      }

      return -1L;
   }

   private long allocateRunSimple(int var1, int var2, int var3) {
      int var4 = this.runLength(var3);
      if (var1 > var4) {
         return -1L;
      } else {
         while(var1 != var4) {
            int var5 = var2 << 1 ^ this.nextRandom();
            int var6 = var5 ^ 1;
            this.memoryMap[var2] = var3 & -4 | 1;
            this.memoryMap[var6] = this.memoryMap[var6] & -4 | 0;
            var4 >>>= 1;
            var2 = var5;
            var3 = this.memoryMap[var5];
         }

         this.memoryMap[var2] = var3 & -4 | 2;
         this.freeBytes -= var4;
         return (long)var2;
      }
   }

   private long allocateSubpage(int var1, int var2, int var3) {
      int var4 = var3 & 3;
      if (var4 == 1) {
         int var8 = var2 << 1 ^ this.nextRandom();
         long var9 = this.branchSubpage(var1, var8);
         return var9 > 0L ? var9 : this.branchSubpage(var1, var8 ^ 1);
      } else if (var4 == 0) {
         return this.allocateSubpageSimple(var1, var2, var3);
      } else if (var4 == 3) {
         PoolSubpage var5 = this.subpages[this.subpageIdx(var2)];
         int var6 = var5.elemSize;
         return var1 != var6 ? -1L : var5.allocate();
      } else {
         return -1L;
      }
   }

   private long allocateSubpageSimple(int var1, int var2, int var3) {
      int var4;
      int var5;
      for(var4 = this.runLength(var3); var4 != this.pageSize; var3 = this.memoryMap[var5]) {
         var5 = var2 << 1 ^ this.nextRandom();
         int var6 = var5 ^ 1;
         this.memoryMap[var2] = var3 & -4 | 1;
         this.memoryMap[var6] = this.memoryMap[var6] & -4 | 0;
         var4 >>>= 1;
         var2 = var5;
      }

      this.memoryMap[var2] = var3 & -4 | 3;
      this.freeBytes -= var4;
      var5 = this.subpageIdx(var2);
      PoolSubpage var7 = this.subpages[var5];
      if (var7 == null) {
         var7 = new PoolSubpage(this, var2, this.runOffset(var3), this.pageSize, var1);
         this.subpages[var5] = var7;
      } else {
         var7.init(var1);
      }

      return var7.allocate();
   }

   private long branchSubpage(int var1, int var2) {
      int var3 = this.memoryMap[var2];
      return (var3 & 3) != 2 ? this.allocateSubpage(var1, var2, var3) : -1L;
   }

   void free(long var1) {
      int var3 = (int)var1;
      int var4 = (int)(var1 >>> 32);
      int var5 = this.memoryMap[var3];
      int var6 = var5 & 3;
      if (var6 == 3) {
         if (!$assertionsDisabled && var4 == 0) {
            throw new AssertionError();
         }

         PoolSubpage var7 = this.subpages[this.subpageIdx(var3)];
         if (!$assertionsDisabled && (var7 == null || !var7.doNotDestroy)) {
            throw new AssertionError();
         }

         if (var7.free(var4 & 1073741823)) {
            return;
         }
      } else {
         if (!$assertionsDisabled && var6 != 2) {
            throw new AssertionError("state: " + var6);
         }

         if (!$assertionsDisabled && var4 != 0) {
            throw new AssertionError();
         }
      }

      this.freeBytes += this.runLength(var5);

      while(true) {
         this.memoryMap[var3] = var5 & -4 | 0;
         if (var3 == 1) {
            if (!$assertionsDisabled && this.freeBytes != this.chunkSize) {
               throw new AssertionError();
            } else {
               return;
            }
         }

         if ((this.memoryMap[siblingIdx(var3)] & 3) != 0) {
            return;
         }

         var3 = parentIdx(var3);
         var5 = this.memoryMap[var3];
      }
   }

   void initBuf(PooledByteBuf var1, long var2, int var4) {
      int var5 = (int)var2;
      int var6 = (int)(var2 >>> 32);
      if (var6 == 0) {
         int var7 = this.memoryMap[var5];
         if (!$assertionsDisabled && (var7 & 3) != 2) {
            throw new AssertionError(String.valueOf(var7 & 3));
         }

         var1.init(this, var2, this.runOffset(var7), var4, this.runLength(var7));
      } else {
         this.initBufWithSubpage(var1, var2, var6, var4);
      }

   }

   void initBufWithSubpage(PooledByteBuf var1, long var2, int var4) {
      this.initBufWithSubpage(var1, var2, (int)(var2 >>> 32), var4);
   }

   private void initBufWithSubpage(PooledByteBuf var1, long var2, int var4, int var5) {
      if (!$assertionsDisabled && var4 == 0) {
         throw new AssertionError();
      } else {
         int var6 = (int)var2;
         int var7 = this.memoryMap[var6];
         if (!$assertionsDisabled && (var7 & 3) != 3) {
            throw new AssertionError();
         } else {
            PoolSubpage var8 = this.subpages[this.subpageIdx(var6)];
            if (!$assertionsDisabled && !var8.doNotDestroy) {
               throw new AssertionError();
            } else if (!$assertionsDisabled && var5 > var8.elemSize) {
               throw new AssertionError();
            } else {
               var1.init(this, var2, this.runOffset(var7) + (var4 & 1073741823) * var8.elemSize, var5, var8.elemSize);
            }
         }
      }
   }

   private static int parentIdx(int var0) {
      return var0 >>> 1;
   }

   private static int siblingIdx(int var0) {
      return var0 ^ 1;
   }

   private int runLength(int var1) {
      return (var1 >>> 2 & 32767) << this.pageShifts;
   }

   private int runOffset(int var1) {
      return var1 >>> 17 << this.pageShifts;
   }

   private int subpageIdx(int var1) {
      return var1 - this.maxSubpageAllocs;
   }

   private int nextRandom() {
      this.random = this.random * 25214903917L + 11L & 281474976710655L;
      return (int)(this.random >>> 47) & 1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Chunk(");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(": ");
      var1.append(this.usage());
      var1.append("%, ");
      var1.append(this.chunkSize - this.freeBytes);
      var1.append('/');
      var1.append(this.chunkSize);
      var1.append(')');
      return var1.toString();
   }
}
