package io.netty.buffer;

import io.netty.util.concurrent.FastThreadLocal;
import java.util.concurrent.atomic.AtomicInteger;

final class PooledByteBufAllocator$PoolThreadLocalCache extends FastThreadLocal {
   private final AtomicInteger index;
   final PooledByteBufAllocator this$0;

   PooledByteBufAllocator$PoolThreadLocalCache(PooledByteBufAllocator var1) {
      this.this$0 = var1;
      this.index = new AtomicInteger();
   }

   protected PoolThreadCache initialValue() {
      int var1 = this.index.getAndIncrement();
      PoolArena var2;
      if (PooledByteBufAllocator.access$000(this.this$0) != null) {
         var2 = PooledByteBufAllocator.access$000(this.this$0)[Math.abs(var1 % PooledByteBufAllocator.access$000(this.this$0).length)];
      } else {
         var2 = null;
      }

      PoolArena var3;
      if (PooledByteBufAllocator.access$100(this.this$0) != null) {
         var3 = PooledByteBufAllocator.access$100(this.this$0)[Math.abs(var1 % PooledByteBufAllocator.access$100(this.this$0).length)];
      } else {
         var3 = null;
      }

      return new PoolThreadCache(var2, var3, PooledByteBufAllocator.access$200(this.this$0), PooledByteBufAllocator.access$300(this.this$0), PooledByteBufAllocator.access$400(this.this$0), PooledByteBufAllocator.access$500(), PooledByteBufAllocator.access$600());
   }

   protected void onRemoval(PoolThreadCache var1) {
      var1.free();
   }

   protected void onRemoval(Object var1) throws Exception {
      this.onRemoval((PoolThreadCache)var1);
   }

   protected Object initialValue() throws Exception {
      return this.initialValue();
   }
}
