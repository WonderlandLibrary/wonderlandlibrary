package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
final class Hashing {
   private static final int C1 = -862048943;
   private static final int C2 = 461845907;
   private static int MAX_TABLE_SIZE = 1073741824;

   private Hashing() {
   }

   static int smear(int var0) {
      return 461845907 * Integer.rotateLeft(var0 * -862048943, 15);
   }

   static int smearedHash(@Nullable Object var0) {
      return smear(var0 == null ? 0 : var0.hashCode());
   }

   static int closedTableSize(int var0, double var1) {
      var0 = Math.max(var0, 2);
      int var3 = Integer.highestOneBit(var0);
      if (var0 > (int)(var1 * (double)var3)) {
         var3 <<= 1;
         return var3 > 0 ? var3 : MAX_TABLE_SIZE;
      } else {
         return var3;
      }
   }

   static boolean needsResizing(int var0, int var1, double var2) {
      return (double)var0 > var2 * (double)var1 && var1 < MAX_TABLE_SIZE;
   }
}
