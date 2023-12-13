package io.netty.util.concurrent;

import java.util.Arrays;

final class DefaultFutureListeners {
   private GenericFutureListener[] listeners = new GenericFutureListener[2];
   private int size;
   private int progressiveSize;

   public DefaultFutureListeners(GenericFutureListener var1, GenericFutureListener var2) {
      this.listeners[0] = var1;
      this.listeners[1] = var2;
      this.size = 2;
      if (var1 instanceof GenericProgressiveFutureListener) {
         ++this.progressiveSize;
      }

      if (var2 instanceof GenericProgressiveFutureListener) {
         ++this.progressiveSize;
      }

   }

   public void add(GenericFutureListener var1) {
      GenericFutureListener[] var2 = this.listeners;
      int var3 = this.size;
      if (var3 == var2.length) {
         this.listeners = var2 = (GenericFutureListener[])Arrays.copyOf(var2, var3 << 1);
      }

      var2[var3] = var1;
      this.size = var3 + 1;
      if (var1 instanceof GenericProgressiveFutureListener) {
         ++this.progressiveSize;
      }

   }

   public void remove(GenericFutureListener var1) {
      GenericFutureListener[] var2 = this.listeners;
      int var3 = this.size;

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var2[var4] == var1) {
            int var5 = var3 - var4 - 1;
            if (var5 > 0) {
               System.arraycopy(var2, var4 + 1, var2, var4, var5);
            }

            --var3;
            var2[var3] = null;
            this.size = var3;
            if (var1 instanceof GenericProgressiveFutureListener) {
               --this.progressiveSize;
            }

            return;
         }
      }

   }

   public GenericFutureListener[] listeners() {
      return this.listeners;
   }

   public int size() {
      return this.size;
   }

   public int progressiveSize() {
      return this.progressiveSize;
   }
}
