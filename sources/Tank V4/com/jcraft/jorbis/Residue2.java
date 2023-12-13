package com.jcraft.jorbis;

class Residue2 extends Residue0 {
   int inverse(Block var1, Object var2, float[][] var3, int[] var4, int var5) {
      boolean var6 = false;

      int var7;
      for(var7 = 0; var7 < var5 && var4[var7] == 0; ++var7) {
      }

      return var7 == var5 ? 0 : _2inverse(var1, var2, var3, var5);
   }
}
