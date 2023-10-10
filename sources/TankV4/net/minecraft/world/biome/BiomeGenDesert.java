package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

public class BiomeGenDesert extends BiomeGenBase {
   public BiomeGenDesert(int var1) {
      super(var1);
      this.spawnableCreatureList.clear();
      this.topBlock = Blocks.sand.getDefaultState();
      this.fillerBlock = Blocks.sand.getDefaultState();
      this.theBiomeDecorator.treesPerChunk = -999;
      this.theBiomeDecorator.deadBushPerChunk = 2;
      this.theBiomeDecorator.reedsPerChunk = 50;
      this.theBiomeDecorator.cactiPerChunk = 10;
      this.spawnableCreatureList.clear();
   }

   public void decorate(World var1, Random var2, BlockPos var3) {
      super.decorate(var1, var2, var3);
      if (var2.nextInt(1000) == 0) {
         int var4 = var2.nextInt(16) + 8;
         int var5 = var2.nextInt(16) + 8;
         BlockPos var6 = var1.getHeight(var3.add(var4, 0, var5)).up();
         (new WorldGenDesertWells()).generate(var1, var2, var6);
      }

   }
}
