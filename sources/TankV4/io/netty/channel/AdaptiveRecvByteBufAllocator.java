package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.ArrayList;

public class AdaptiveRecvByteBufAllocator implements RecvByteBufAllocator {
   static final int DEFAULT_MINIMUM = 64;
   static final int DEFAULT_INITIAL = 1024;
   static final int DEFAULT_MAXIMUM = 65536;
   private static final int INDEX_INCREMENT = 4;
   private static final int INDEX_DECREMENT = 1;
   private static final int[] SIZE_TABLE;
   public static final AdaptiveRecvByteBufAllocator DEFAULT;
   private final int minIndex;
   private final int maxIndex;
   private final int initial;

   private static int getSizeTableIndex(int var0) {
      int var1 = 0;
      int var2 = SIZE_TABLE.length - 1;

      while(var2 >= var1) {
         if (var2 == var1) {
            return var2;
         }

         int var3 = var1 + var2 >>> 1;
         int var4 = SIZE_TABLE[var3];
         int var5 = SIZE_TABLE[var3 + 1];
         if (var0 > var5) {
            var1 = var3 + 1;
         } else {
            if (var0 >= var4) {
               if (var0 == var4) {
                  return var3;
               }

               return var3 + 1;
            }

            var2 = var3 - 1;
         }
      }

      return var1;
   }

   private AdaptiveRecvByteBufAllocator() {
      this(64, 1024, 65536);
   }

   public AdaptiveRecvByteBufAllocator(int var1, int var2, int var3) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("minimum: " + var1);
      } else if (var2 < var1) {
         throw new IllegalArgumentException("initial: " + var2);
      } else if (var3 < var2) {
         throw new IllegalArgumentException("maximum: " + var3);
      } else {
         int var4 = getSizeTableIndex(var1);
         if (SIZE_TABLE[var4] < var1) {
            this.minIndex = var4 + 1;
         } else {
            this.minIndex = var4;
         }

         int var5 = getSizeTableIndex(var3);
         if (SIZE_TABLE[var5] > var3) {
            this.maxIndex = var5 - 1;
         } else {
            this.maxIndex = var5;
         }

         this.initial = var2;
      }
   }

   public RecvByteBufAllocator.Handle newHandle() {
      return new AdaptiveRecvByteBufAllocator.HandleImpl(this.minIndex, this.maxIndex, this.initial);
   }

   static int access$000(int var0) {
      return getSizeTableIndex(var0);
   }

   static int[] access$100() {
      return SIZE_TABLE;
   }

   static {
      ArrayList var0 = new ArrayList();

      int var1;
      for(var1 = 16; var1 < 512; var1 += 16) {
         var0.add(var1);
      }

      for(var1 = 512; var1 > 0; var1 <<= 1) {
         var0.add(var1);
      }

      SIZE_TABLE = new int[var0.size()];

      for(var1 = 0; var1 < SIZE_TABLE.length; ++var1) {
         SIZE_TABLE[var1] = (Integer)var0.get(var1);
      }

      DEFAULT = new AdaptiveRecvByteBufAllocator();
   }

   private static final class HandleImpl implements RecvByteBufAllocator.Handle {
      private final int minIndex;
      private final int maxIndex;
      private int index;
      private int nextReceiveBufferSize;
      private boolean decreaseNow;

      HandleImpl(int var1, int var2, int var3) {
         this.minIndex = var1;
         this.maxIndex = var2;
         this.index = AdaptiveRecvByteBufAllocator.access$000(var3);
         this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.access$100()[this.index];
      }

      public ByteBuf allocate(ByteBufAllocator var1) {
         return var1.ioBuffer(this.nextReceiveBufferSize);
      }

      public int guess() {
         return this.nextReceiveBufferSize;
      }

      public void record(int var1) {
         if (var1 <= AdaptiveRecvByteBufAllocator.access$100()[Math.max(0, this.index - 1 - 1)]) {
            if (this.decreaseNow) {
               this.index = Math.max(this.index - 1, this.minIndex);
               this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.access$100()[this.index];
               this.decreaseNow = false;
            } else {
               this.decreaseNow = true;
            }
         } else if (var1 >= this.nextReceiveBufferSize) {
            this.index = Math.min(this.index + 4, this.maxIndex);
            this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.access$100()[this.index];
            this.decreaseNow = false;
         }

      }
   }
}
