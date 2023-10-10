package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenSwamp extends BiomeGenBase {
   public WorldGenAbstractTree genBigTreeChance(Random var1) {
      return this.worldGeneratorSwamp;
   }

   public int getFoliageColorAtPos(BlockPos var1) {
      return 6975545;
   }

   public int getGrassColorAtPos(BlockPos var1) {
      double var2 = GRASS_COLOR_NOISE.func_151601_a((double)var1.getX() * 0.0225D, (double)var1.getZ() * 0.0225D);
      return var2 < -0.1D ? 5011004 : 6975545;
   }

   public BlockFlower.EnumFlowerType pickRandomFlower(Random var1, BlockPos var2) {
      return BlockFlower.EnumFlowerType.BLUE_ORCHID;
   }

   protected BiomeGenSwamp(int var1) {
      super(var1);
      this.theBiomeDecorator.treesPerChunk = 2;
      this.theBiomeDecorator.flowersPerChunk = 1;
      this.theBiomeDecorator.deadBushPerChunk = 1;
      this.theBiomeDecorator.mushroomsPerChunk = 8;
      this.theBiomeDecorator.reedsPerChunk = 10;
      this.theBiomeDecorator.clayPerChunk = 1;
      this.theBiomeDecorator.waterlilyPerChunk = 4;
      this.theBiomeDecorator.sandPerChunk2 = 0;
      this.theBiomeDecorator.sandPerChunk = 0;
      this.theBiomeDecorator.grassPerChunk = 5;
      this.waterColorMultiplier = 14745518;
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 1, 1, 1));
   }

   public void genTerrainBlocks(World var1, Random var2, ChunkPrimer var3, int var4, int var5, double var6) {
      double var8 = GRASS_COLOR_NOISE.func_151601_a((double)var4 * 0.25D, (double)var5 * 0.25D);
      if (var8 > 0.0D) {
         int var10 = var4 & 15;
         int var11 = var5 & 15;

         for(int var12 = 255; var12 >= 0; --var12) {
            if (var3.getBlockState(var11, var12, var10).getBlock().getMaterial() != Material.air) {
               if (var12 == 62 && var3.getBlockState(var11, var12, var10).getBlock() != Blocks.water) {
                  var3.setBlockState(var11, var12, var10, Blocks.water.getDefaultState());
                  if (var8 < 0.12D) {
                     var3.setBlockState(var11, var12 + 1, var10, Blocks.waterlily.getDefaultState());
                  }
               }
               break;
            }
         }
      }

      this.generateBiomeTerrain(var1, var2, var3, var4, var5, var6);
   }
}
