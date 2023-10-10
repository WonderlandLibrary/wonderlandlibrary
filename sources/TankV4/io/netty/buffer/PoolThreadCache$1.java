package io.netty.buffer;

class PoolThreadCache$1 implements Runnable {
   final PoolThreadCache this$0;

   PoolThreadCache$1(PoolThreadCache var1) {
      this.this$0 = var1;
   }

   public void run() {
      PoolThreadCache.access$000(this.this$0);
   }
}
