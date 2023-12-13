package com.jcraft.jorbis;

class Residue1 extends Residue0 {
   int inverse(Block var1, Object var2, float[][] var3, int[] var4, int var5) {
      int var6 = 0;

      for(int var7 = 0; var7 < var5; ++var7) {
         if (var4[var7] != 0) {
            var3[var6++] = var3[var7];
         }
      }

      if (var6 != 0) {
         return _01inverse(var1, var2, var3, var6, 1);
      } else {
         return 0;
      }
   }
}
