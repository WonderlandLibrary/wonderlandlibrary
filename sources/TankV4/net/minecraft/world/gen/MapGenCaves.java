package net.minecraft.world.gen;

import com.google.common.base.Objects;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCaves extends MapGenBase {
   protected void func_180703_a(long var1, int var3, int var4, ChunkPrimer var5, double var6, double var8, double var10) {
      this.func_180702_a(var1, var3, var4, var5, var6, var8, var10, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
   }

   protected void func_180702_a(long var1, int var3, int var4, ChunkPrimer var5, double var6, double var8, double var10, float var12, float var13, float var14, int var15, int var16, double var17) {
      double var19 = (double)(var3 * 16 + 8);
      double var21 = (double)(var4 * 16 + 8);
      float var23 = 0.0F;
      float var24 = 0.0F;
      Random var25 = new Random(var1);
      if (var16 <= 0) {
         int var26 = this.range * 16 - 16;
         var16 = var26 - var25.nextInt(var26 / 4);
      }

      boolean var63 = false;
      if (var15 == -1) {
         var15 = var16 / 2;
         var63 = true;
      }

      int var27 = var25.nextInt(var16 / 2) + var16 / 4;

      for(boolean var28 = var25.nextInt(6) == 0; var15 < var16; ++var15) {
         double var29 = 1.5D + (double)(MathHelper.sin((float)var15 * 3.1415927F / (float)var16) * var12 * 1.0F);
         double var31 = var29 * var17;
         float var33 = MathHelper.cos(var14);
         float var34 = MathHelper.sin(var14);
         var6 += (double)(MathHelper.cos(var13) * var33);
         var8 += (double)var34;
         var10 += (double)(MathHelper.sin(var13) * var33);
         if (var28) {
            var14 *= 0.92F;
         } else {
            var14 *= 0.7F;
         }

         var14 += var24 * 0.1F;
         var13 += var23 * 0.1F;
         var24 *= 0.9F;
         var23 *= 0.75F;
         var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
         var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;
         if (!var63 && var15 == var27 && var12 > 1.0F && var16 > 0) {
            this.func_180702_a(var25.nextLong(), var3, var4, var5, var6, var8, var10, var25.nextFloat() * 0.5F + 0.5F, var13 - 1.5707964F, var14 / 3.0F, var15, var16, 1.0D);
            this.func_180702_a(var25.nextLong(), var3, var4, var5, var6, var8, var10, var25.nextFloat() * 0.5F + 0.5F, var13 + 1.5707964F, var14 / 3.0F, var15, var16, 1.0D);
            return;
         }

         if (var63 || var25.nextInt(4) != 0) {
            double var35 = var6 - var19;
            double var37 = var10 - var21;
            double var39 = (double)(var16 - var15);
            double var41 = (double)(var12 + 2.0F + 16.0F);
            if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
               return;
            }

            if (var6 >= var19 - 16.0D - var29 * 2.0D && var10 >= var21 - 16.0D - var29 * 2.0D && var6 <= var19 + 16.0D + var29 * 2.0D && var10 <= var21 + 16.0D + var29 * 2.0D) {
               int var43 = MathHelper.floor_double(var6 - var29) - var3 * 16 - 1;
               int var44 = MathHelper.floor_double(var6 + var29) - var3 * 16 + 1;
               int var45 = MathHelper.floor_double(var8 - var31) - 1;
               int var46 = MathHelper.floor_double(var8 + var31) + 1;
               int var47 = MathHelper.floor_double(var10 - var29) - var4 * 16 - 1;
               int var48 = MathHelper.floor_double(var10 + var29) - var4 * 16 + 1;
               if (var43 < 0) {
                  var43 = 0;
               }

               if (var44 > 16) {
                  var44 = 16;
               }

               if (var45 < 1) {
                  var45 = 1;
               }

               if (var46 > 248) {
                  var46 = 248;
               }

               if (var47 < 0) {
                  var47 = 0;
               }

               if (var48 > 16) {
                  var48 = 16;
               }

               boolean var49 = false;

               int var51;
               for(int var50 = var43; !var49 && var50 < var44; ++var50) {
                  for(var51 = var47; !var49 && var51 < var48; ++var51) {
                     for(int var52 = var46 + 1; !var49 && var52 >= var45 - 1; --var52) {
                        if (var52 >= 0 && var52 < 256) {
                           IBlockState var53 = var5.getBlockState(var50, var52, var51);
                           if (var53.getBlock() == Blocks.flowing_water || var53.getBlock() == Blocks.water) {
                              var49 = true;
                           }

                           if (var52 != var45 - 1 && var50 != var43 && var50 != var44 - 1 && var51 != var47 && var51 != var48 - 1) {
                              var52 = var45;
                           }
                        }
                     }
                  }
               }

               if (!var49) {
                  BlockPos.MutableBlockPos var64 = new BlockPos.MutableBlockPos();

                  for(var51 = var43; var51 < var44; ++var51) {
                     double var65 = ((double)(var51 + var3 * 16) + 0.5D - var6) / var29;

                     for(int var54 = var47; var54 < var48; ++var54) {
                        double var55 = ((double)(var54 + var4 * 16) + 0.5D - var10) / var29;
                        boolean var57 = false;
                        if (var65 * var65 + var55 * var55 < 1.0D) {
                           for(int var58 = var46; var58 > var45; --var58) {
                              double var59 = ((double)(var58 - 1) + 0.5D - var8) / var31;
                              if (var59 > -0.7D && var65 * var65 + var59 * var59 + var55 * var55 < 1.0D) {
                                 IBlockState var61 = var5.getBlockState(var51, var58, var54);
                                 IBlockState var62 = (IBlockState)Objects.firstNonNull(var5.getBlockState(var51, var58 + 1, var54), Blocks.air.getDefaultState());
                                 if (var61.getBlock() == Blocks.grass || var61.getBlock() == Blocks.mycelium) {
                                    var57 = true;
                                 }

                                 if (var62 != false) {
                                    if (var58 - 1 < 10) {
                                       var5.setBlockState(var51, var58, var54, Blocks.lava.getDefaultState());
                                    } else {
                                       var5.setBlockState(var51, var58, var54, Blocks.air.getDefaultState());
                                       if (var62.getBlock() == Blocks.sand) {
                                          var5.setBlockState(var51, var58 + 1, var54, var62.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                                       }

                                       if (var57 && var5.getBlockState(var51, var58 - 1, var54).getBlock() == Blocks.dirt) {
                                          var64.func_181079_c(var51 + var3 * 16, 0, var54 + var4 * 16);
                                          var5.setBlockState(var51, var58 - 1, var54, this.worldObj.getBiomeGenForCoords(var64).topBlock.getBlock().getDefaultState());
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  if (var63) {
                     break;
                  }
               }
            }
         }
      }

   }

   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, ChunkPrimer var6) {
      int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
      if (this.rand.nextInt(7) != 0) {
         var7 = 0;
      }

      for(int var8 = 0; var8 < var7; ++var8) {
         double var9 = (double)(var2 * 16 + this.rand.nextInt(16));
         double var11 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
         double var13 = (double)(var3 * 16 + this.rand.nextInt(16));
         int var15 = 1;
         if (this.rand.nextInt(4) == 0) {
            this.func_180703_a(this.rand.nextLong(), var4, var5, var6, var9, var11, var13);
            var15 += this.rand.nextInt(4);
         }

         for(int var16 = 0; var16 < var15; ++var16) {
            float var17 = this.rand.nextFloat() * 3.1415927F * 2.0F;
            float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
            float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
            if (this.rand.nextInt(10) == 0) {
               var19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
            }

            this.func_180702_a(this.rand.nextLong(), var4, var5, var6, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
         }
      }

   }
}
