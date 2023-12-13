package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenOcean extends BiomeGenBase {
   public BiomeGenBase.TempCategory getTempCategory() {
      return BiomeGenBase.TempCategory.OCEAN;
   }

   public BiomeGenOcean(int var1) {
      super(var1);
      this.spawnableCreatureList.clear();
   }

   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      super.genTerrainBlocks(var1, var2, var3, var4, var5, var6);
   }
}
