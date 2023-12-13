package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiver extends GenLayer {
   private int func_151630_c(int var1) {
      return var1 >= 2 ? 2 + (var1 & 1) : var1;
   }

   public GenLayerRiver(long var1, GenLayer var3) {
      super(var1);
      super.parent = var3;
   }

   public int[] getInts(int var1, int var2, int var3, int var4) {
      int var5 = var1 - 1;
      int var6 = var2 - 1;
      int var7 = var3 + 2;
      int var8 = var4 + 2;
      int[] var9 = this.parent.getInts(var5, var6, var7, var8);
      int[] var10 = IntCache.getIntCache(var3 * var4);

      for(int var11 = 0; var11 < var4; ++var11) {
         for(int var12 = 0; var12 < var3; ++var12) {
            int var13 = this.func_151630_c(var9[var12 + 0 + (var11 + 1) * var7]);
            int var14 = this.func_151630_c(var9[var12 + 2 + (var11 + 1) * var7]);
            int var15 = this.func_151630_c(var9[var12 + 1 + (var11 + 0) * var7]);
            int var16 = this.func_151630_c(var9[var12 + 1 + (var11 + 2) * var7]);
            int var17 = this.func_151630_c(var9[var12 + 1 + (var11 + 1) * var7]);
            if (var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16) {
               var10[var12 + var11 * var3] = -1;
            } else {
               var10[var12 + var11 * var3] = BiomeGenBase.river.biomeID;
            }
         }
      }

      return var10;
   }
}
