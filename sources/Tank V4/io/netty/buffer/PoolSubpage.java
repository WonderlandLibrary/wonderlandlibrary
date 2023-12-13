package io.netty.buffer;

final class PoolSubpage {
   final PoolChunk chunk;
   final int memoryMapIdx;
   final int runOffset;
   final int pageSize;
   final long[] bitmap;
   PoolSubpage prev;
   PoolSubpage next;
   boolean doNotDestroy;
   int elemSize;
   int maxNumElems;
   int nextAvail;
   int bitmapLength;
   int numAvail;
   static final boolean $assertionsDisabled = !PoolSubpage.class.desiredAssertionStatus();

   PoolSubpage(int var1) {
      this.chunk = null;
      this.memoryMapIdx = -1;
      this.runOffset = -1;
      this.elemSize = -1;
      this.pageSize = var1;
      this.bitmap = null;
   }

   PoolSubpage(PoolChunk var1, int var2, int var3, int var4, int var5) {
      this.chunk = var1;
      this.memoryMapIdx = var2;
      this.runOffset = var3;
      this.pageSize = var4;
      this.bitmap = new long[var4 >>> 10];
      this.init(var5);
   }

   void init(int var1) {
      this.doNotDestroy = true;
      this.elemSize = var1;
      if (var1 != 0) {
         this.maxNumElems = this.numAvail = this.pageSize / var1;
         this.nextAvail = 0;
         this.bitmapLength = this.maxNumElems >>> 6;
         if ((this.maxNumElems & 63) != 0) {
            ++this.bitmapLength;
         }

         for(int var2 = 0; var2 < this.bitmapLength; ++var2) {
            this.bitmap[var2] = 0L;
         }
      }

      this.addToPool();
   }

   long allocate() {
      if (this.elemSize == 0) {
         return this.toHandle(0);
      } else if (this.numAvail != 0 && this.doNotDestroy) {
         int var1 = this.nextAvail;
         int var2 = var1 >>> 6;
         int var3 = var1 & 63;
         if (!$assertionsDisabled && (this.bitmap[var2] >>> var3 & 1L) != 0L) {
            throw new AssertionError();
         } else {
            long[] var10000 = this.bitmap;
            var10000[var2] |= 1L << var3;
            if (--this.numAvail == 0) {
               this.removeFromPool();
               this.nextAvail = -1;
            } else {
               this.nextAvail = this.findNextAvailable();
            }

            return this.toHandle(var1);
         }
      } else {
         return -1L;
      }
   }

   boolean free(int var1) {
      if (this.elemSize == 0) {
         return true;
      } else {
         int var2 = var1 >>> 6;
         int var3 = var1 & 63;
         if (!$assertionsDisabled && (this.bitmap[var2] >>> var3 & 1L) == 0L) {
            throw new AssertionError();
         } else {
            long[] var10000 = this.bitmap;
            var10000[var2] ^= 1L << var3;
            if (this.numAvail++ == 0) {
               this.nextAvail = var1;
               this.addToPool();
               return true;
            } else if (this.numAvail != this.maxNumElems) {
               return true;
            } else if (this.prev == this.next) {
               return true;
            } else {
               this.doNotDestroy = false;
               this.removeFromPool();
               return false;
            }
         }
      }
   }

   private void addToPool() {
      PoolSubpage var1 = this.chunk.arena.findSubpagePoolHead(this.elemSize);
      if ($assertionsDisabled || this.prev == null && this.next == null) {
         this.prev = var1;
         this.next = var1.next;
         this.next.prev = this;
         var1.next = this;
      } else {
         throw new AssertionError();
      }
   }

   private void removeFromPool() {
      if ($assertionsDisabled || this.prev != null && this.next != null) {
         this.prev.next = this.next;
         this.next.prev = this.prev;
         this.next = null;
         this.prev = null;
      } else {
         throw new AssertionError();
      }
   }

   private int findNextAvailable() {
      int var1 = -1;

      for(int var2 = 0; var2 < this.bitmapLength; ++var2) {
         long var3 = this.bitmap[var2];
         if (~var3 != 0L) {
            for(int var5 = 0; var5 < 64; ++var5) {
               if ((var3 & 1L) == 0L) {
                  var1 = var2 << 6 | var5;
                  return var1 < this.maxNumElems ? var1 : -1;
               }

               var3 >>>= 1;
            }
         }
      }

      return var1 < this.maxNumElems ? var1 : -1;
   }

   private long toHandle(int var1) {
      return 4611686018427387904L | (long)var1 << 32 | (long)this.memoryMapIdx;
   }

   public String toString() {
      return !this.doNotDestroy ? "(" + this.memoryMapIdx + ": not in use)" : String.valueOf('(') + this.memoryMapIdx + ": " + (this.maxNumElems - this.numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + this.elemSize + ')';
   }
}
