package io.netty.buffer;

import io.netty.util.internal.StringUtil;

final class PoolChunkList {
   private final PoolArena arena;
   private final PoolChunkList nextList;
   PoolChunkList prevList;
   private final int minUsage;
   private final int maxUsage;
   private PoolChunk head;
   static final boolean $assertionsDisabled = !PoolChunkList.class.desiredAssertionStatus();

   PoolChunkList(PoolArena var1, PoolChunkList var2, int var3, int var4) {
      this.arena = var1;
      this.nextList = var2;
      this.minUsage = var3;
      this.maxUsage = var4;
   }

   boolean allocate(PooledByteBuf var1, int var2, int var3) {
      if (this.head == null) {
         return false;
      } else {
         PoolChunk var4 = this.head;

         do {
            long var5 = var4.allocate(var3);
            if (var5 >= 0L) {
               var4.initBuf(var1, var5, var2);
               if (var4.usage() >= this.maxUsage) {
                  this.remove(var4);
                  this.nextList.add(var4);
               }

               return true;
            }

            var4 = var4.next;
         } while(var4 != null);

         return false;
      }
   }

   void free(PoolChunk var1, long var2) {
      var1.free(var2);
      if (var1.usage() < this.minUsage) {
         this.remove(var1);
         if (this.prevList == null) {
            if (!$assertionsDisabled && var1.usage() != 0) {
               throw new AssertionError();
            }

            this.arena.destroyChunk(var1);
         } else {
            this.prevList.add(var1);
         }
      }

   }

   void add(PoolChunk var1) {
      if (var1.usage() >= this.maxUsage) {
         this.nextList.add(var1);
      } else {
         var1.parent = this;
         if (this.head == null) {
            this.head = var1;
            var1.prev = null;
            var1.next = null;
         } else {
            var1.prev = null;
            var1.next = this.head;
            this.head.prev = var1;
            this.head = var1;
         }

      }
   }

   private void remove(PoolChunk var1) {
      if (var1 == this.head) {
         this.head = var1.next;
         if (this.head != null) {
            this.head.prev = null;
         }
      } else {
         PoolChunk var2 = var1.next;
         var1.prev.next = var2;
         if (var2 != null) {
            var2.prev = var1.prev;
         }
      }

   }

   public String toString() {
      if (this.head == null) {
         return "none";
      } else {
         StringBuilder var1 = new StringBuilder();
         PoolChunk var2 = this.head;

         while(true) {
            var1.append(var2);
            var2 = var2.next;
            if (var2 == null) {
               return var1.toString();
            }

            var1.append(StringUtil.NEWLINE);
         }
      }
   }
}
