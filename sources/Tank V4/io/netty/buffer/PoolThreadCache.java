package io.netty.buffer;

final class PoolThreadCache {
   final PoolArena heapArena;
   final PoolArena directArena;

   PoolThreadCache(PoolArena var1, PoolArena var2) {
      this.heapArena = var1;
      this.directArena = var2;
   }
}
