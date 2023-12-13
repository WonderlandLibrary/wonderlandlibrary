package io.netty.buffer;

import io.netty.util.Recycler;

final class ByteBufUtil$ThreadLocalDirectByteBuf$1 extends Recycler {
   protected ByteBufUtil$ThreadLocalDirectByteBuf newObject(Recycler.Handle var1) {
      return new ByteBufUtil$ThreadLocalDirectByteBuf(var1, (ByteBufUtil$1)null);
   }

   protected Object newObject(Recycler.Handle var1) {
      return this.newObject(var1);
   }
}
