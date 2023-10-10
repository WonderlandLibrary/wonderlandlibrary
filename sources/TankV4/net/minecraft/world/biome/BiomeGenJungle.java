package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenJungle extends BiomeGenBase {
   private boolean field_150614_aC;
   private static final IBlockState field_181620_aE;
   private static final IBlockState field_181622_aG;
   private static final IBlockState field_181621_aF;

   public BiomeGenJungle(int var1, boolean var2) {
      super(var1);
      this.field_150614_aC = var2;
      if (var2) {
         this.theBiomeDecorator.treesPerChunk = 2;
      } else {
         this.theBiomeDecorator.treesPerChunk = 50;
      }

      this.theBiomeDecorator.grassPerChunk = 25;
      this.theBiomeDecorator.flowersPerChunk = 4;
      if (!var2) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
      }

      this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
   }

   public WorldGenerator getRandomWorldGenForGrass(Random var1) {
      return var1.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
   }

   public void decorate(World var1, Random var2, BlockPos var3) {
      super.decorate(var1, var2, var3);
      int var4 = var2.nextInt(16) + 8;
      int var5 = var2.nextInt(16) + 8;
      int var6 = var2.nextInt(var1.getHeight(var3.add(var4, 0, var5)).getY() * 2);
      (new WorldGenMelon()).generate(var1, var2, var3.add(var4, var6, var5));
      WorldGenVines var7 = new WorldGenVines();

      for(var5 = 0; var5 < 50; ++var5) {
         var6 = var2.nextInt(16) + 8;
         boolean var8 = true;
         int var9 = var2.nextInt(16) + 8;
         var7.generate(var1, var2, var3.add(var6, 128, var9));
      }

   }

   public WorldGenAbstractTree genBigTreeChance(Random var1) {
      return (WorldGenAbstractTree)(var1.nextInt(10) == 0 ? this.worldGeneratorBigTree : (var1.nextInt(2) == 0 ? new WorldGenShrub(field_181620_aE, field_181622_aG) : (!this.field_150614_aC && var1.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, field_181620_aE, field_181621_aF) : new WorldGenTrees(false, 4 + var1.nextInt(7), field_181620_aE, field_181621_aF, true))));
   }

   static {
      field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
      field_181621_aF = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false);
      field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
   }
}
