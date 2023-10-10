package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeGenSavanna extends BiomeGenBase {
   private static final WorldGenSavannaTree field_150627_aC = new WorldGenSavannaTree(false);

   public void decorate(World var1, Random var2, BlockPos var3) {
      DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);

      for(int var4 = 0; var4 < 7; ++var4) {
         int var5 = var2.nextInt(16) + 8;
         int var6 = var2.nextInt(16) + 8;
         int var7 = var2.nextInt(var1.getHeight(var3.add(var5, 0, var6)).getY() + 32);
         DOUBLE_PLANT_GENERATOR.generate(var1, var2, var3.add(var5, var7, var6));
      }

      super.decorate(var1, var2, var3);
   }

   public WorldGenAbstractTree genBigTreeChance(Random var1) {
      return (WorldGenAbstractTree)(var1.nextInt(5) > 0 ? field_150627_aC : this.worldGeneratorTrees);
   }

   protected BiomeGenSavanna(int var1) {
      super(var1);
      this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 1, 2, 6));
      this.theBiomeDecorator.treesPerChunk = 1;
      this.theBiomeDecorator.flowersPerChunk = 4;
      this.theBiomeDecorator.grassPerChunk = 20;
   }

   protected BiomeGenBase createMutatedBiome(int var1) {
      BiomeGenSavanna.Mutated var2 = new BiomeGenSavanna.Mutated(var1, this);
      var2.temperature = (this.temperature + 1.0F) * 0.5F;
      var2.minHeight = this.minHeight * 0.5F + 0.3F;
      var2.maxHeight = this.maxHeight * 0.5F + 1.2F;
      return var2;
   }

   public static class Mutated extends BiomeGenMutated {
      public Mutated(int var1, BiomeGenBase var2) {
         super(var1, var2);
         this.theBiomeDecorator.treesPerChunk = 2;
         this.theBiomeDecorator.flowersPerChunk = 2;
         this.theBiomeDecorator.grassPerChunk = 5;
      }

      public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
         this.topBlock = Blocks.grass.getDefaultState();
         this.fillerBlock = Blocks.dirt.getDefaultState();
         if (var6 > 1.75D) {
            this.topBlock = Blocks.stone.getDefaultState();
            this.fillerBlock = Blocks.stone.getDefaultState();
         } else if (var6 > -0.5D) {
            this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
         }

         this.generateBiomeTerrain(var1, var2, var3, var4, var5, var6);
      }

      public void decorate(World var1, Random var2, BlockPos var3) {
         this.theBiomeDecorator.decorate(var1, var2, this, var3);
      }
   }
}
