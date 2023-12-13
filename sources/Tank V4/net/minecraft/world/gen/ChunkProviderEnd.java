package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEnd implements IChunkProvider {
   double[] noiseData3;
   public NoiseGeneratorOctaves noiseGen5;
   double[] noiseData2;
   private Random endRNG;
   double[] noiseData1;
   private BiomeGenBase[] biomesForGeneration;
   private World endWorld;
   public NoiseGeneratorOctaves noiseGen4;
   private double[] densities;
   double[] noiseData5;
   private NoiseGeneratorOctaves noiseGen3;
   double[] noiseData4;
   private NoiseGeneratorOctaves noiseGen2;
   private NoiseGeneratorOctaves noiseGen1;

   public void recreateStructures(Chunk var1, int var2, int var3) {
   }

   public boolean canSave() {
      return true;
   }

   public String makeString() {
      return "RandomLevelSource";
   }

   public int getLoadedChunkCount() {
      return 0;
   }

   private double[] initializeNoiseField(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         var1 = new double[var5 * var6 * var7];
      }

      double var8 = 684.412D;
      double var10 = 684.412D;
      this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, var2, var4, var5, var7, 1.121D, 1.121D, 0.5D);
      this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, var2, var4, var5, var7, 200.0D, 200.0D, 0.5D);
      var8 *= 2.0D;
      this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, var2, var3, var4, var5, var6, var7, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
      this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, var2, var3, var4, var5, var6, var7, var8, var10, var8);
      this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, var2, var3, var4, var5, var6, var7, var8, var10, var8);
      int var12 = 0;

      for(int var13 = 0; var13 < var5; ++var13) {
         for(int var14 = 0; var14 < var7; ++var14) {
            float var15 = (float)(var13 + var2) / 1.0F;
            float var16 = (float)(var14 + var4) / 1.0F;
            float var17 = 100.0F - MathHelper.sqrt_float(var15 * var15 + var16 * var16) * 8.0F;
            if (var17 > 80.0F) {
               var17 = 80.0F;
            }

            if (var17 < -100.0F) {
               var17 = -100.0F;
            }

            for(int var18 = 0; var18 < var6; ++var18) {
               double var19 = 0.0D;
               double var21 = this.noiseData2[var12] / 512.0D;
               double var23 = this.noiseData3[var12] / 512.0D;
               double var25 = (this.noiseData1[var12] / 10.0D + 1.0D) / 2.0D;
               if (var25 < 0.0D) {
                  var19 = var21;
               } else if (var25 > 1.0D) {
                  var19 = var23;
               } else {
                  var19 = var21 + (var23 - var21) * var25;
               }

               var19 -= 8.0D;
               var19 += (double)var17;
               byte var27 = 2;
               double var28;
               if (var18 > var6 / 2 - var27) {
                  var28 = (double)((float)(var18 - (var6 / 2 - var27)) / 64.0F);
                  var28 = MathHelper.clamp_double(var28, 0.0D, 1.0D);
                  var19 = var19 * (1.0D - var28) + -3000.0D * var28;
               }

               var27 = 8;
               if (var18 < var27) {
                  var28 = (double)((float)(var27 - var18) / ((float)var27 - 1.0F));
                  var19 = var19 * (1.0D - var28) + -30.0D * var28;
               }

               var1[var12] = var19;
               ++var12;
            }
         }
      }

      return var1;
   }

   public List getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      return this.endWorld.getBiomeGenForCoords(var2).getSpawnableList(var1);
   }

   public ChunkProviderEnd(World var1, long var2) {
      this.endWorld = var1;
      this.endRNG = new Random(var2);
      this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
      this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
      this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
      this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
      this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
   }

   public Chunk provideChunk(BlockPos var1) {
      return this.provideChunk(var1.getX() >> 4, var1.getZ() >> 4);
   }

   public void populate(IChunkProvider var1, int var2, int var3) {
      BlockFalling.fallInstantly = true;
      BlockPos var4 = new BlockPos(var2 * 16, 0, var3 * 16);
      this.endWorld.getBiomeGenForCoords(var4.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, var4);
      BlockFalling.fallInstantly = false;
   }

   public boolean unloadQueuedChunks() {
      return false;
   }

   public boolean chunkExists(int var1, int var2) {
      return true;
   }

   public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4) {
      return false;
   }

   public void func_180520_a(int var1, int var2, ChunkPrimer var3) {
      byte var4 = 2;
      int var5 = var4 + 1;
      byte var6 = 33;
      int var7 = var4 + 1;
      this.densities = this.initializeNoiseField(this.densities, var1 * var4, 0, var2 * var4, var5, var6, var7);

      for(int var8 = 0; var8 < var4; ++var8) {
         for(int var9 = 0; var9 < var4; ++var9) {
            for(int var10 = 0; var10 < 32; ++var10) {
               double var11 = 0.25D;
               double var13 = this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 0];
               double var15 = this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 0];
               double var17 = this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 0];
               double var19 = this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 0];
               double var21 = (this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 1] - var13) * var11;
               double var23 = (this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 1] - var15) * var11;
               double var25 = (this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 1] - var17) * var11;
               double var27 = (this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 1] - var19) * var11;

               for(int var29 = 0; var29 < 4; ++var29) {
                  double var30 = 0.125D;
                  double var32 = var13;
                  double var34 = var15;
                  double var36 = (var17 - var13) * var30;
                  double var38 = (var19 - var15) * var30;

                  for(int var40 = 0; var40 < 8; ++var40) {
                     double var41 = 0.125D;
                     double var43 = var32;
                     double var45 = (var34 - var32) * var41;

                     for(int var47 = 0; var47 < 8; ++var47) {
                        IBlockState var48 = null;
                        if (var43 > 0.0D) {
                           var48 = Blocks.end_stone.getDefaultState();
                        }

                        int var49 = var40 + var8 * 8;
                        int var50 = var29 + var10 * 4;
                        int var51 = var47 + var9 * 8;
                        var3.setBlockState(var49, var50, var51, var48);
                        var43 += var45;
                     }

                     var32 += var36;
                     var34 += var38;
                  }

                  var13 += var21;
                  var15 += var23;
                  var17 += var25;
                  var19 += var27;
               }
            }
         }
      }

   }

   public Chunk provideChunk(int var1, int var2) {
      this.endRNG.setSeed((long)var1 * 341873128712L + (long)var2 * 132897987541L);
      ChunkPrimer var3 = new ChunkPrimer();
      this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, var1 * 16, var2 * 16, 16, 16);
      this.func_180520_a(var1, var2, var3);
      this.func_180519_a(var3);
      Chunk var4 = new Chunk(this.endWorld, var3, var1, var2);
      byte[] var5 = var4.getBiomeArray();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
      }

      var4.generateSkylightMap();
      return var4;
   }

   public void saveExtraData() {
   }

   public boolean saveChunks(boolean var1, IProgressUpdate var2) {
      return true;
   }

   public void func_180519_a(ChunkPrimer var1) {
      for(int var2 = 0; var2 < 16; ++var2) {
         for(int var3 = 0; var3 < 16; ++var3) {
            byte var4 = 1;
            int var5 = -1;
            IBlockState var6 = Blocks.end_stone.getDefaultState();
            IBlockState var7 = Blocks.end_stone.getDefaultState();

            for(int var8 = 127; var8 >= 0; --var8) {
               IBlockState var9 = var1.getBlockState(var2, var8, var3);
               if (var9.getBlock().getMaterial() == Material.air) {
                  var5 = -1;
               } else if (var9.getBlock() == Blocks.stone) {
                  if (var5 == -1) {
                     if (var4 <= 0) {
                        var6 = Blocks.air.getDefaultState();
                        var7 = Blocks.end_stone.getDefaultState();
                     }

                     var5 = var4;
                     if (var8 >= 0) {
                        var1.setBlockState(var2, var8, var3, var6);
                     } else {
                        var1.setBlockState(var2, var8, var3, var7);
                     }
                  } else if (var5 > 0) {
                     --var5;
                     var1.setBlockState(var2, var8, var3, var7);
                  }
               }
            }
         }
      }

   }

   public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3) {
      return null;
   }
}
