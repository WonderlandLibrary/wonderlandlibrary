package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {
   protected WorldGenerator bigMushroomGen;
   protected int cactiPerChunk;
   protected WorldGenerator mushroomRedGen;
   protected int clayPerChunk;
   protected WorldGenerator goldGen;
   protected int flowersPerChunk;
   protected int sandPerChunk;
   protected WorldGenerator mushroomBrownGen;
   protected int mushroomsPerChunk;
   protected int bigMushroomsPerChunk;
   protected WorldGenerator gravelGen;
   protected WorldGenerator gravelAsSandGen;
   protected WorldGenerator redstoneGen;
   protected int waterlilyPerChunk;
   protected Random randomGenerator;
   protected WorldGenerator ironGen;
   protected int deadBushPerChunk;
   public boolean generateLakes;
   protected int treesPerChunk;
   protected WorldGenerator clayGen = new WorldGenClay(4);
   protected WorldGenerator sandGen;
   protected WorldGenerator dioriteGen;
   protected int sandPerChunk2;
   protected int grassPerChunk;
   protected WorldGenerator dirtGen;
   protected WorldGenerator graniteGen;
   protected WorldGenerator reedGen;
   protected World currentWorld;
   protected WorldGenerator waterlilyGen;
   protected WorldGenerator cactusGen;
   protected WorldGenerator lapisGen;
   protected WorldGenerator andesiteGen;
   protected BlockPos field_180294_c;
   protected WorldGenFlowers yellowFlowerGen;
   protected WorldGenerator coalGen;
   protected WorldGenerator diamondGen;
   protected int reedsPerChunk;
   protected ChunkProviderSettings chunkProviderSettings;

   protected void genStandardOre1(int var1, WorldGenerator var2, int var3, int var4) {
      int var5;
      if (var4 < var3) {
         var5 = var3;
         var3 = var4;
         var4 = var5;
      } else if (var4 == var3) {
         if (var3 < 255) {
            ++var4;
         } else {
            --var3;
         }
      }

      for(var5 = 0; var5 < var1; ++var5) {
         BlockPos var6 = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(var4 - var3) + var3, this.randomGenerator.nextInt(16));
         var2.generate(this.currentWorld, this.randomGenerator, var6);
      }

   }

   public BiomeDecorator() {
      this.sandGen = new WorldGenSand(Blocks.sand, 7);
      this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
      this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
      this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
      this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
      this.bigMushroomGen = new WorldGenBigMushroom();
      this.reedGen = new WorldGenReed();
      this.cactusGen = new WorldGenCactus();
      this.waterlilyGen = new WorldGenWaterlily();
      this.flowersPerChunk = 2;
      this.grassPerChunk = 1;
      this.sandPerChunk = 1;
      this.sandPerChunk2 = 3;
      this.clayPerChunk = 1;
      this.generateLakes = true;
   }

   protected void genStandardOre2(int var1, WorldGenerator var2, int var3, int var4) {
      for(int var5 = 0; var5 < var1; ++var5) {
         BlockPos var6 = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(var4) + this.randomGenerator.nextInt(var4) + var3 - var4, this.randomGenerator.nextInt(16));
         var2.generate(this.currentWorld, this.randomGenerator, var6);
      }

   }

   public void decorate(World var1, Random var2, BiomeGenBase var3, BlockPos var4) {
      if (this.currentWorld != null) {
         throw new RuntimeException("Already decorating");
      } else {
         this.currentWorld = var1;
         String var5 = var1.getWorldInfo().getGeneratorOptions();
         if (var5 != null) {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(var5).func_177864_b();
         } else {
            this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
         }

         this.randomGenerator = var2;
         this.field_180294_c = var4;
         this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
         this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
         this.graniteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
         this.dioriteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
         this.andesiteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
         this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
         this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
         this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
         this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
         this.diamondGen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
         this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);
         this.genDecorations(var3);
         this.currentWorld = null;
         this.randomGenerator = null;
      }
   }

   protected void generateOres() {
      this.genStandardOre1(this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
      this.genStandardOre1(this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
      this.genStandardOre2(this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
   }

   protected void genDecorations(BiomeGenBase var1) {
      this.generateOres();

      int var2;
      int var3;
      int var4;
      for(var2 = 0; var2 < this.sandPerChunk2; ++var2) {
         var3 = this.randomGenerator.nextInt(16) + 8;
         var4 = this.randomGenerator.nextInt(16) + 8;
         this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(var3, 0, var4)));
      }

      for(var2 = 0; var2 < this.clayPerChunk; ++var2) {
         var3 = this.randomGenerator.nextInt(16) + 8;
         var4 = this.randomGenerator.nextInt(16) + 8;
         this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(var3, 0, var4)));
      }

      for(var2 = 0; var2 < this.sandPerChunk; ++var2) {
         var3 = this.randomGenerator.nextInt(16) + 8;
         var4 = this.randomGenerator.nextInt(16) + 8;
         this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(var3, 0, var4)));
      }

      var2 = this.treesPerChunk;
      if (this.randomGenerator.nextInt(10) == 0) {
         ++var2;
      }

      int var5;
      BlockPos var7;
      for(var3 = 0; var3 < var2; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         WorldGenAbstractTree var6 = var1.genBigTreeChance(this.randomGenerator);
         var6.func_175904_e();
         var7 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5));
         if (var6.generate(this.currentWorld, this.randomGenerator, var7)) {
            var6.func_180711_a(this.currentWorld, this.randomGenerator, var7);
         }
      }

      for(var3 = 0; var3 < this.bigMushroomsPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)));
      }

      BlockPos var8;
      int var12;
      int var13;
      for(var3 = 0; var3 < this.flowersPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() + 32;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);
            var8 = this.field_180294_c.add(var4, var13, var5);
            BlockFlower.EnumFlowerType var9 = var1.pickRandomFlower(this.randomGenerator, var8);
            BlockFlower var10 = var9.getBlockType().getBlock();
            if (var10.getMaterial() != Material.air) {
               this.yellowFlowerGen.setGeneratedBlock(var10, var9);
               this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, var8);
            }
         }
      }

      for(var3 = 0; var3 < this.grassPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);
            var1.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var13, var5));
         }
      }

      for(var3 = 0; var3 < this.deadBushPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);
            (new WorldGenDeadBush()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var13, var5));
         }
      }

      for(var3 = 0; var3 < this.waterlilyPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);

            BlockPos var15;
            for(var8 = this.field_180294_c.add(var4, var13, var5); var8.getY() > 0; var8 = var15) {
               var15 = var8.down();
               if (!this.currentWorld.isAirBlock(var15)) {
                  break;
               }
            }

            this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, var8);
         }
      }

      for(var3 = 0; var3 < this.mushroomsPerChunk; ++var3) {
         if (this.randomGenerator.nextInt(4) == 0) {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            BlockPos var14 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5));
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, var14);
         }

         if (this.randomGenerator.nextInt(8) == 0) {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
            if (var12 > 0) {
               var13 = this.randomGenerator.nextInt(var12);
               var8 = this.field_180294_c.add(var4, var13, var5);
               this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, var8);
            }
         }
      }

      if (this.randomGenerator.nextInt(4) == 0) {
         var3 = this.randomGenerator.nextInt(16) + 8;
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.currentWorld.getHeight(this.field_180294_c.add(var3, 0, var4)).getY() * 2;
         if (var5 > 0) {
            var12 = this.randomGenerator.nextInt(var5);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var12, var4));
         }
      }

      if (this.randomGenerator.nextInt(8) == 0) {
         var3 = this.randomGenerator.nextInt(16) + 8;
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.currentWorld.getHeight(this.field_180294_c.add(var3, 0, var4)).getY() * 2;
         if (var5 > 0) {
            var12 = this.randomGenerator.nextInt(var5);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var12, var4));
         }
      }

      for(var3 = 0; var3 < this.reedsPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var13, var5));
         }
      }

      for(var3 = 0; var3 < 10; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);
            this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var13, var5));
         }
      }

      if (this.randomGenerator.nextInt(32) == 0) {
         var3 = this.randomGenerator.nextInt(16) + 8;
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.currentWorld.getHeight(this.field_180294_c.add(var3, 0, var4)).getY() * 2;
         if (var5 > 0) {
            var12 = this.randomGenerator.nextInt(var5);
            (new WorldGenPumpkin()).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var3, var12, var4));
         }
      }

      for(var3 = 0; var3 < this.cactiPerChunk; ++var3) {
         var4 = this.randomGenerator.nextInt(16) + 8;
         var5 = this.randomGenerator.nextInt(16) + 8;
         var12 = this.currentWorld.getHeight(this.field_180294_c.add(var4, 0, var5)).getY() * 2;
         if (var12 > 0) {
            var13 = this.randomGenerator.nextInt(var12);
            this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(var4, var13, var5));
         }
      }

      if (this.generateLakes) {
         for(var3 = 0; var3 < 50; ++var3) {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var12 = this.randomGenerator.nextInt(248) + 8;
            if (var12 > 0) {
               var13 = this.randomGenerator.nextInt(var12);
               var8 = this.field_180294_c.add(var4, var13, var5);
               (new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, var8);
            }
         }

         for(var3 = 0; var3 < 20; ++var3) {
            var4 = this.randomGenerator.nextInt(16) + 8;
            var5 = this.randomGenerator.nextInt(16) + 8;
            var12 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
            var7 = this.field_180294_c.add(var4, var12, var5);
            (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, var7);
         }
      }

   }
}
