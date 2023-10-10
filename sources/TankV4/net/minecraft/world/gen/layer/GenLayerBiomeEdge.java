package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiomeEdge extends GenLayer {
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] var5 = this.parent.getInts(var1 - 1, var2 - 1, var3 + 2, var4 + 2);
      int[] var6 = IntCache.getIntCache(var3 * var4);

      for(int var7 = 0; var7 < var4; ++var7) {
         for(int var8 = 0; var8 < var3; ++var8) {
            this.initChunkSeed((long)(var8 + var1), (long)(var7 + var2));
            int var9 = var5[var8 + 1 + (var7 + 1) * (var3 + 2)];
            int var10007 = BiomeGenBase.extremeHills.biomeID;
            if (BiomeGenBase.extremeHillsEdge.biomeID == 0 && BiomeGenBase.mesaPlateau_F.biomeID != BiomeGenBase.mesa.biomeID && BiomeGenBase.mesaPlateau.biomeID != BiomeGenBase.mesa.biomeID && BiomeGenBase.megaTaiga.biomeID != BiomeGenBase.taiga.biomeID) {
               int var10;
               int var11;
               int var12;
               int var13;
               if (var9 == BiomeGenBase.desert.biomeID) {
                  var10 = var5[var8 + 1 + (var7 + 1 - 1) * (var3 + 2)];
                  var11 = var5[var8 + 1 + 1 + (var7 + 1) * (var3 + 2)];
                  var12 = var5[var8 + 1 - 1 + (var7 + 1) * (var3 + 2)];
                  var13 = var5[var8 + 1 + (var7 + 1 + 1) * (var3 + 2)];
                  if (var10 != BiomeGenBase.icePlains.biomeID && var11 != BiomeGenBase.icePlains.biomeID && var12 != BiomeGenBase.icePlains.biomeID && var13 != BiomeGenBase.icePlains.biomeID) {
                     var6[var8 + var7 * var3] = var9;
                  } else {
                     var6[var8 + var7 * var3] = BiomeGenBase.extremeHillsPlus.biomeID;
                  }
               } else if (var9 == BiomeGenBase.swampland.biomeID) {
                  var10 = var5[var8 + 1 + (var7 + 1 - 1) * (var3 + 2)];
                  var11 = var5[var8 + 1 + 1 + (var7 + 1) * (var3 + 2)];
                  var12 = var5[var8 + 1 - 1 + (var7 + 1) * (var3 + 2)];
                  var13 = var5[var8 + 1 + (var7 + 1 + 1) * (var3 + 2)];
                  if (var10 != BiomeGenBase.desert.biomeID && var11 != BiomeGenBase.desert.biomeID && var12 != BiomeGenBase.desert.biomeID && var13 != BiomeGenBase.desert.biomeID && var10 != BiomeGenBase.coldTaiga.biomeID && var11 != BiomeGenBase.coldTaiga.biomeID && var12 != BiomeGenBase.coldTaiga.biomeID && var13 != BiomeGenBase.coldTaiga.biomeID && var10 != BiomeGenBase.icePlains.biomeID && var11 != BiomeGenBase.icePlains.biomeID && var12 != BiomeGenBase.icePlains.biomeID && var13 != BiomeGenBase.icePlains.biomeID) {
                     if (var10 != BiomeGenBase.jungle.biomeID && var13 != BiomeGenBase.jungle.biomeID && var11 != BiomeGenBase.jungle.biomeID && var12 != BiomeGenBase.jungle.biomeID) {
                        var6[var8 + var7 * var3] = var9;
                     } else {
                        var6[var8 + var7 * var3] = BiomeGenBase.jungleEdge.biomeID;
                     }
                  } else {
                     var6[var8 + var7 * var3] = BiomeGenBase.plains.biomeID;
                  }
               } else {
                  var6[var8 + var7 * var3] = var9;
               }
            }
         }
      }

      return var6;
   }

   public GenLayerBiomeEdge(long var1, GenLayer var3) {
      super(var1);
      this.parent = var3;
   }
}
