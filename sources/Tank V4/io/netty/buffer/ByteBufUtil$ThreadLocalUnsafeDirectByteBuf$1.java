package io.netty.buffer;

import io.netty.util.Recycler;

final class ByteBufUtil$ThreadLocalUnsafeDirectByteBuf$1 extends Recycler {
   protected ByteBufUtil$ThreadLocalUnsafeDirectByteBuf newObject(Recycler.Handle var1) {
      return new ByteBufUtil$ThreadLocalUnsafeDirectByteBuf(var1, (ByteBufUtil$1)null);
   }

   protected Object newObject(Recycler.Handle var1) {
      return this.newObject(var1);
   }
}
