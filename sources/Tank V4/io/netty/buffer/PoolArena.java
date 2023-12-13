package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;

abstract class PoolArena {
   final PooledByteBufAllocator parent;
   private final int pageSize;
   private final int maxOrder;
   private final int pageShifts;
   private final int chunkSize;
   private final int subpageOverflowMask;
   private final PoolSubpage[] tinySubpagePools;
   private final PoolSubpage[] smallSubpagePools;
   private final PoolChunkList q050;
   private final PoolChunkList q025;
   private final PoolChunkList q000;
   private final PoolChunkList qInit;
   private final PoolChunkList q075;
   private final PoolChunkList q100;
   static final boolean $assertionsDisabled = !PoolArena.class.desiredAssertionStatus();

   protected PoolArena(PooledByteBufAllocator var1, int var2, int var3, int var4, int var5) {
      this.parent = var1;
      this.pageSize = var2;
      this.maxOrder = var3;
      this.pageShifts = var4;
      this.chunkSize = var5;
      this.subpageOverflowMask = ~(var2 - 1);
      this.tinySubpagePools = this.newSubpagePoolArray(32);

      int var6;
      for(var6 = 0; var6 < this.tinySubpagePools.length; ++var6) {
         this.tinySubpagePools[var6] = this.newSubpagePoolHead(var2);
      }

      this.smallSubpagePools = this.newSubpagePoolArray(var4 - 9);

      for(var6 = 0; var6 < this.smallSubpagePools.length; ++var6) {
         this.smallSubpagePools[var6] = this.newSubpagePoolHead(var2);
      }

      this.q100 = new PoolChunkList(this, (PoolChunkList)null, 100, Integer.MAX_VALUE);
      this.q075 = new PoolChunkList(this, this.q100, 75, 100);
      this.q050 = new PoolChunkList(this, this.q075, 50, 100);
      this.q025 = new PoolChunkList(this, this.q050, 25, 75);
      this.q000 = new PoolChunkList(this, this.q025, 1, 50);
      this.qInit = new PoolChunkList(this, this.q000, Integer.MIN_VALUE, 25);
      this.q100.prevList = this.q075;
      this.q075.prevList = this.q050;
      this.q050.prevList = this.q025;
      this.q025.prevList = this.q000;
      this.q000.prevList = null;
      this.qInit.prevList = this.qInit;
   }

   private PoolSubpage newSubpagePoolHead(int var1) {
      PoolSubpage var2 = new PoolSubpage(var1);
      var2.prev = var2;
      var2.next = var2;
      return var2;
   }

   private PoolSubpage[] newSubpagePoolArray(int var1) {
      return new PoolSubpage[var1];
   }

   PooledByteBuf allocate(PoolThreadCache var1, int var2, int var3) {
      PooledByteBuf var4 = this.newByteBuf(var3);
      this.allocate(var1, var4, var2);
      return var4;
   }

   private void allocate(PoolThreadCache var1, PooledByteBuf var2, int var3) {
      int var4 = this.normalizeCapacity(var3);
      if ((var4 & this.subpageOverflowMask) == 0) {
         int var5;
         PoolSubpage[] var6;
         if ((var4 & -512) == 0) {
            var5 = var4 >>> 4;
            var6 = this.tinySubpagePools;
         } else {
            var5 = 0;

            for(int var7 = var4 >>> 10; var7 != 0; ++var5) {
               var7 >>>= 1;
            }

            var6 = this.smallSubpagePools;
         }

         synchronized(this){}
         PoolSubpage var8 = var6[var5];
         PoolSubpage var9 = var8.next;
         if (var9 != var8) {
            if ($assertionsDisabled || var9.doNotDestroy && var9.elemSize == var4) {
               long var10 = var9.allocate();
               if (!$assertionsDisabled && var10 < 0L) {
                  throw new AssertionError();
               }

               var9.chunk.initBufWithSubpage(var2, var10, var3);
               return;
            }

            throw new AssertionError();
         }
      } else if (var4 > this.chunkSize) {
         this.allocateHuge(var2, var3);
         return;
      }

      this.allocateNormal(var2, var3, var4);
   }

   private synchronized void allocateNormal(PooledByteBuf var1, int var2, int var3) {
      if (!this.q050.allocate(var1, var2, var3) && !this.q025.allocate(var1, var2, var3) && !this.q000.allocate(var1, var2, var3) && !this.qInit.allocate(var1, var2, var3) && !this.q075.allocate(var1, var2, var3) && !this.q100.allocate(var1, var2, var3)) {
         PoolChunk var4 = this.newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
         long var5 = var4.allocate(var3);
         if (!$assertionsDisabled && var5 <= 0L) {
            throw new AssertionError();
         } else {
            var4.initBuf(var1, var5, var2);
            this.qInit.add(var4);
         }
      }
   }

   private void allocateHuge(PooledByteBuf var1, int var2) {
      var1.initUnpooled(this.newUnpooledChunk(var2), var2);
   }

   void free(PoolChunk var1, long var2) {
      if (var1.unpooled) {
         this.destroyChunk(var1);
      } else {
         synchronized(this){}
         var1.parent.free(var1, var2);
      }

   }

   PoolSubpage findSubpagePoolHead(int var1) {
      int var2;
      PoolSubpage[] var3;
      if ((var1 & -512) == 0) {
         var2 = var1 >>> 4;
         var3 = this.tinySubpagePools;
      } else {
         var2 = 0;

         for(var1 >>>= 10; var1 != 0; ++var2) {
            var1 >>>= 1;
         }

         var3 = this.smallSubpagePools;
      }

      return var3[var2];
   }

   private int normalizeCapacity(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("capacity: " + var1 + " (expected: 0+)");
      } else if (var1 >= this.chunkSize) {
         return var1;
      } else if ((var1 & -512) != 0) {
         int var2 = var1 | var1 >>> 1;
         var2 |= var2 >>> 2;
         var2 |= var2 >>> 4;
         var2 |= var2 >>> 8;
         var2 |= var2 >>> 16;
         ++var2;
         if (var2 < 0) {
            var2 >>>= 1;
         }

         return var2;
      } else {
         return (var1 & 15) == 0 ? var1 : (var1 & -16) + 16;
      }
   }

   void reallocate(PooledByteBuf var1, int var2, boolean var3) {
      if (var2 >= 0 && var2 <= var1.maxCapacity()) {
         int var4 = var1.length;
         if (var4 != var2) {
            PoolChunk var5 = var1.chunk;
            long var6 = var1.handle;
            Object var8 = var1.memory;
            int var9 = var1.offset;
            int var10 = var1.readerIndex();
            int var11 = var1.writerIndex();
            this.allocate((PoolThreadCache)this.parent.threadCache.get(), var1, var2);
            if (var2 > var4) {
               this.memoryCopy(var8, var9, var1.memory, var1.offset, var4);
            } else if (var2 < var4) {
               if (var10 < var2) {
                  if (var11 > var2) {
                     var11 = var2;
                  }

                  this.memoryCopy(var8, var9 + var10, var1.memory, var1.offset + var10, var11 - var10);
               } else {
                  var11 = var2;
                  var10 = var2;
               }
            }

            var1.setIndex(var10, var11);
            if (var3) {
               this.free(var5, var6);
            }

         }
      } else {
         throw new IllegalArgumentException("newCapacity: " + var2);
      }
   }

   protected abstract PoolChunk newChunk(int var1, int var2, int var3, int var4);

   protected abstract PoolChunk newUnpooledChunk(int var1);

   protected abstract PooledByteBuf newByteBuf(int var1);

   protected abstract void memoryCopy(Object var1, int var2, Object var3, int var4, int var5);

   protected abstract void destroyChunk(PoolChunk var1);

   public synchronized String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Chunk(s) at 0~25%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.qInit);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 0~50%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q000);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 25~75%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q025);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 50~100%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q050);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 75~100%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q075);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 100%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q100);
      var1.append(StringUtil.NEWLINE);
      var1.append("tiny subpages:");

      int var2;
      PoolSubpage var3;
      PoolSubpage var4;
      for(var2 = 1; var2 < this.tinySubpagePools.length; ++var2) {
         var3 = this.tinySubpagePools[var2];
         if (var3.next != var3) {
            var1.append(StringUtil.NEWLINE);
            var1.append(var2);
            var1.append(": ");
            var4 = var3.next;

            do {
               var1.append(var4);
               var4 = var4.next;
            } while(var4 != var3);
         }
      }

      var1.append(StringUtil.NEWLINE);
      var1.append("small subpages:");

      for(var2 = 1; var2 < this.smallSubpagePools.length; ++var2) {
         var3 = this.smallSubpagePools[var2];
         if (var3.next != var3) {
            var1.append(StringUtil.NEWLINE);
            var1.append(var2);
            var1.append(": ");
            var4 = var3.next;

            do {
               var1.append(var4);
               var4 = var4.next;
            } while(var4 != var3);
         }
      }

      var1.append(StringUtil.NEWLINE);
      return var1.toString();
   }

   static final class DirectArena extends PoolArena {
      private static final boolean HAS_UNSAFE = PlatformDependent.hasUnsafe();

      DirectArena(PooledByteBufAllocator var1, int var2, int var3, int var4, int var5) {
         super(var1, var2, var3, var4, var5);
      }

      protected PoolChunk newChunk(int var1, int var2, int var3, int var4) {
         return new PoolChunk(this, ByteBuffer.allocateDirect(var4), var1, var2, var3, var4);
      }

      protected PoolChunk newUnpooledChunk(int var1) {
         return new PoolChunk(this, ByteBuffer.allocateDirect(var1), var1);
      }

      protected void destroyChunk(PoolChunk var1) {
         PlatformDependent.freeDirectBuffer((ByteBuffer)var1.memory);
      }

      protected PooledByteBuf newByteBuf(int var1) {
         return (PooledByteBuf)(HAS_UNSAFE ? PooledUnsafeDirectByteBuf.newInstance(var1) : PooledDirectByteBuf.newInstance(var1));
      }

      protected void memoryCopy(ByteBuffer var1, int var2, ByteBuffer var3, int var4, int var5) {
         if (var5 != 0) {
            if (HAS_UNSAFE) {
               PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(var1) + (long)var2, PlatformDependent.directBufferAddress(var3) + (long)var4, (long)var5);
            } else {
               var1 = var1.duplicate();
               var3 = var3.duplicate();
               var1.position(var2).limit(var2 + var5);
               var3.position(var4);
               var3.put(var1);
            }

         }
      }

      protected void memoryCopy(Object var1, int var2, Object var3, int var4, int var5) {
         this.memoryCopy((ByteBuffer)var1, var2, (ByteBuffer)var3, var4, var5);
      }
   }

   static final class HeapArena extends PoolArena {
      HeapArena(PooledByteBufAllocator var1, int var2, int var3, int var4, int var5) {
         super(var1, var2, var3, var4, var5);
      }

      protected PoolChunk newChunk(int var1, int var2, int var3, int var4) {
         return new PoolChunk(this, new byte[var4], var1, var2, var3, var4);
      }

      protected PoolChunk newUnpooledChunk(int var1) {
         return new PoolChunk(this, new byte[var1], var1);
      }

      protected void destroyChunk(PoolChunk var1) {
      }

      protected PooledByteBuf newByteBuf(int var1) {
         return PooledHeapByteBuf.newInstance(var1);
      }

      protected void memoryCopy(byte[] var1, int var2, byte[] var3, int var4, int var5) {
         if (var5 != 0) {
            System.arraycopy(var1, var2, var3, var4, var5);
         }
      }

      protected void memoryCopy(Object var1, int var2, Object var3, int var4, int var5) {
         this.memoryCopy((byte[])var1, var2, (byte[])var3, var4, var5);
      }
   }
}
