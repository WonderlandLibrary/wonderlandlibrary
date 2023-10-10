package io.netty.buffer;

import io.netty.util.Recycler;

final class ByteBufUtil$ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf {
   private static final Recycler RECYCLER = new ByteBufUtil$ThreadLocalUnsafeDirectByteBuf$1();
   private final Recycler.Handle handle;

   static ByteBufUtil$ThreadLocalUnsafeDirectByteBuf newInstance() {
      ByteBufUtil$ThreadLocalUnsafeDirectByteBuf var0 = (ByteBufUtil$ThreadLocalUnsafeDirectByteBuf)RECYCLER.get();
      var0.setRefCnt(1);
      return var0;
   }

   private ByteBufUtil$ThreadLocalUnsafeDirectByteBuf(Recycler.Handle var1) {
      super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
      this.handle = var1;
   }

   protected void deallocate() {
      if (this.capacity() > ByteBufUtil.access$100()) {
         super.deallocate();
      } else {
         this.clear();
         RECYCLER.recycle(this, this.handle);
      }

   }

   ByteBufUtil$ThreadLocalUnsafeDirectByteBuf(Recycler.Handle var1, ByteBufUtil$1 var2) {
      this(var1);
   }
}
