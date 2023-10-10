package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class FixedRecvByteBufAllocator implements RecvByteBufAllocator {
   private final RecvByteBufAllocator.Handle handle;

   public FixedRecvByteBufAllocator(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("bufferSize must greater than 0: " + var1);
      } else {
         this.handle = new FixedRecvByteBufAllocator.HandleImpl(var1);
      }
   }

   public RecvByteBufAllocator.Handle newHandle() {
      return this.handle;
   }

   private static final class HandleImpl implements RecvByteBufAllocator.Handle {
      private final int bufferSize;

      HandleImpl(int var1) {
         this.bufferSize = var1;
      }

      public ByteBuf allocate(ByteBufAllocator var1) {
         return var1.ioBuffer(this.bufferSize);
      }

      public int guess() {
         return this.bufferSize;
      }

      public void record(int var1) {
      }
   }
}
