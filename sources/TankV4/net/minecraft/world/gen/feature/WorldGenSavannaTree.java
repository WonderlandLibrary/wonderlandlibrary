package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenSavannaTree extends WorldGenAbstractTree {
   private static final IBlockState field_181644_b;
   private static final IBlockState field_181643_a;

   public WorldGenSavannaTree(boolean var1) {
      super(var1);
   }

   private void func_181642_b(World var1, BlockPos var2) {
      this.setBlockAndNotifyAdequately(var1, var2, field_181643_a);
   }

   static {
      field_181643_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
      field_181644_b = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, false);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = var2.nextInt(3) + var2.nextInt(3) + 5;
      boolean var5 = true;
      if (var3.getY() >= 1 && var3.getY() + var4 + 1 <= 256) {
         int var9;
         int var10;
         for(int var6 = var3.getY(); var6 <= var3.getY() + 1 + var4; ++var6) {
            byte var7 = 1;
            if (var6 == var3.getY()) {
               var7 = 0;
            }

            if (var6 >= var3.getY() + 1 + var4 - 2) {
               var7 = 2;
            }

            BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

            for(var9 = var3.getX() - var7; var9 <= var3.getX() + var7 && var5; ++var9) {
               for(var10 = var3.getZ() - var7; var10 <= var3.getZ() + var7 && var5; ++var10) {
                  if (var6 >= 0 && var6 < 256) {
                     if (!this.func_150523_a(var1.getBlockState(var8.func_181079_c(var9, var6, var10)).getBlock())) {
                        var5 = false;
                     }
                  } else {
                     var5 = false;
                  }
               }
            }
         }

         if (!var5) {
            return false;
         } else {
            Block var21 = var1.getBlockState(var3.down()).getBlock();
            if ((var21 == Blocks.grass || var21 == Blocks.dirt) && var3.getY() < 256 - var4 - 1) {
               this.func_175921_a(var1, var3.down());
               EnumFacing var22 = EnumFacing.Plane.HORIZONTAL.random(var2);
               int var23 = var4 - var2.nextInt(4) - 1;
               var9 = 3 - var2.nextInt(3);
               var10 = var3.getX();
               int var11 = var3.getZ();
               int var12 = 0;

               int var14;
               for(int var13 = 0; var13 < var4; ++var13) {
                  var14 = var3.getY() + var13;
                  if (var13 >= var23 && var9 > 0) {
                     var10 += var22.getFrontOffsetX();
                     var11 += var22.getFrontOffsetZ();
                     --var9;
                  }

                  BlockPos var15 = new BlockPos(var10, var14, var11);
                  Material var16 = var1.getBlockState(var15).getBlock().getMaterial();
                  if (var16 == Material.air || var16 == Material.leaves) {
                     this.func_181642_b(var1, var15);
                     var12 = var14;
                  }
               }

               BlockPos var24 = new BlockPos(var10, var12, var11);

               int var25;
               for(var14 = -3; var14 <= 3; ++var14) {
                  for(var25 = -3; var25 <= 3; ++var25) {
                     if (Math.abs(var14) != 3 || Math.abs(var25) != 3) {
                        this.func_175924_b(var1, var24.add(var14, 0, var25));
                     }
                  }
               }

               var24 = var24.up();

               for(var14 = -1; var14 <= 1; ++var14) {
                  for(var25 = -1; var25 <= 1; ++var25) {
                     this.func_175924_b(var1, var24.add(var14, 0, var25));
                  }
               }

               this.func_175924_b(var1, var24.east(2));
               this.func_175924_b(var1, var24.west(2));
               this.func_175924_b(var1, var24.south(2));
               this.func_175924_b(var1, var24.north(2));
               var10 = var3.getX();
               var11 = var3.getZ();
               EnumFacing var26 = EnumFacing.Plane.HORIZONTAL.random(var2);
               if (var26 != var22) {
                  var25 = var23 - var2.nextInt(2) - 1;
                  int var27 = 1 + var2.nextInt(3);
                  var12 = 0;

                  int var18;
                  for(int var17 = var25; var17 < var4 && var27 > 0; --var27) {
                     if (var17 >= 1) {
                        var18 = var3.getY() + var17;
                        var10 += var26.getFrontOffsetX();
                        var11 += var26.getFrontOffsetZ();
                        BlockPos var19 = new BlockPos(var10, var18, var11);
                        Material var20 = var1.getBlockState(var19).getBlock().getMaterial();
                        if (var20 == Material.air || var20 == Material.leaves) {
                           this.func_181642_b(var1, var19);
                           var12 = var18;
                        }
                     }

                     ++var17;
                  }

                  if (var12 > 0) {
                     BlockPos var28 = new BlockPos(var10, var12, var11);

                     int var29;
                     for(var18 = -2; var18 <= 2; ++var18) {
                        for(var29 = -2; var29 <= 2; ++var29) {
                           if (Math.abs(var18) != 2 || Math.abs(var29) != 2) {
                              this.func_175924_b(var1, var28.add(var18, 0, var29));
                           }
                        }
                     }

                     var28 = var28.up();

                     for(var18 = -1; var18 <= 1; ++var18) {
                        for(var29 = -1; var29 <= 1; ++var29) {
                           this.func_175924_b(var1, var28.add(var18, 0, var29));
                        }
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void func_175924_b(World var1, BlockPos var2) {
      Material var3 = var1.getBlockState(var2).getBlock().getMaterial();
      if (var3 == Material.air || var3 == Material.leaves) {
         this.setBlockAndNotifyAdequately(var1, var2, field_181644_b);
      }

   }
}
