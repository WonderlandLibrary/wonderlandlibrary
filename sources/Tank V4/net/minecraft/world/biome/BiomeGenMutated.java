package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenMutated extends BiomeGenBase {
   protected BiomeGenBase baseBiome;

   public BiomeGenBase.TempCategory getTempCategory() {
      return this.baseBiome.getTempCategory();
   }

   public float getSpawningChance() {
      return this.baseBiome.getSpawningChance();
   }

   public Class getBiomeClass() {
      return this.baseBiome.getBiomeClass();
   }

   public int getGrassColorAtPos(BlockPos var1) {
      return this.baseBiome.getGrassColorAtPos(var1);
   }

   public int getFoliageColorAtPos(BlockPos var1) {
      return this.baseBiome.getFoliageColorAtPos(var1);
   }

   public boolean isEqualTo(BiomeGenBase var1) {
      return this.baseBiome.isEqualTo(var1);
   }

   public BiomeGenMutated(int var1, BiomeGenBase var2) {
      super(var1);
      this.baseBiome = var2;
      this.func_150557_a(var2.color, true);
      this.biomeName = var2.biomeName + " M";
      this.topBlock = var2.topBlock;
      this.fillerBlock = var2.fillerBlock;
      this.fillerBlockMetadata = var2.fillerBlockMetadata;
      this.minHeight = var2.minHeight;
      this.maxHeight = var2.maxHeight;
      this.temperature = var2.temperature;
      this.rainfall = var2.rainfall;
      this.waterColorMultiplier = var2.waterColorMultiplier;
      this.enableSnow = var2.enableSnow;
      this.enableRain = var2.enableRain;
      this.spawnableCreatureList = Lists.newArrayList((Iterable)var2.spawnableCreatureList);
      this.spawnableMonsterList = Lists.newArrayList((Iterable)var2.spawnableMonsterList);
      this.spawnableCaveCreatureList = Lists.newArrayList((Iterable)var2.spawnableCaveCreatureList);
      this.spawnableWaterCreatureList = Lists.newArrayList((Iterable)var2.spawnableWaterCreatureList);
      this.temperature = var2.temperature;
      this.rainfall = var2.rainfall;
      this.minHeight = var2.minHeight + 0.1F;
      this.maxHeight = var2.maxHeight + 0.2F;
   }

   public void decorate(World var1, Random var2, BlockPos var3) {
      this.baseBiome.theBiomeDecorator.decorate(var1, var2, this, var3);
   }

   public WorldGenAbstractTree genBigTreeChance(Random var1) {
      return this.baseBiome.genBigTreeChance(var1);
   }

   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      this.baseBiome.genTerrainBlocks(var1, var2, var3, var4, var5, var6);
   }
}
