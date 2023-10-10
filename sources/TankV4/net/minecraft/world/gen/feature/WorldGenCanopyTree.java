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

public class WorldGenCanopyTree extends WorldGenAbstractTree {
   private static final IBlockState field_181641_b;
   private static final IBlockState field_181640_a;

   static {
      field_181640_a = Blocks.log2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK);
      field_181641_b = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
   }

   private void func_150526_a(World var1, int var2, int var3, int var4) {
      BlockPos var5 = new BlockPos(var2, var3, var4);
      Block var6 = var1.getBlockState(var5).getBlock();
      if (var6.getMaterial() == Material.air) {
         this.setBlockAndNotifyAdequately(var1, var5, field_181641_b);
      }

   }

   public WorldGenCanopyTree(boolean var1) {
      super(var1);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = var2.nextInt(3) + var2.nextInt(2) + 6;
      int var5 = var3.getX();
      int var6 = var3.getY();
      int var7 = var3.getZ();
      if (var6 >= 1 && var6 + var4 + 1 < 256) {
         BlockPos var8 = var3.down();
         Block var9 = var1.getBlockState(var8).getBlock();
         if (var9 != Blocks.grass && var9 != Blocks.dirt) {
            return false;
         } else if (var4 == 0) {
            return false;
         } else {
            this.func_175921_a(var1, var8);
            this.func_175921_a(var1, var8.east());
            this.func_175921_a(var1, var8.south());
            this.func_175921_a(var1, var8.south().east());
            EnumFacing var10 = EnumFacing.Plane.HORIZONTAL.random(var2);
            int var11 = var4 - var2.nextInt(4);
            int var12 = 2 - var2.nextInt(3);
            int var13 = var5;
            int var14 = var7;
            int var15 = var6 + var4 - 1;

            int var16;
            int var17;
            for(var16 = 0; var16 < var4; ++var16) {
               if (var16 >= var11 && var12 > 0) {
                  var13 += var10.getFrontOffsetX();
                  var14 += var10.getFrontOffsetZ();
                  --var12;
               }

               var17 = var6 + var16;
               BlockPos var18 = new BlockPos(var13, var17, var14);
               Material var19 = var1.getBlockState(var18).getBlock().getMaterial();
               if (var19 == Material.air || var19 == Material.leaves) {
                  this.func_181639_b(var1, var18);
                  this.func_181639_b(var1, var18.east());
                  this.func_181639_b(var1, var18.south());
                  this.func_181639_b(var1, var18.east().south());
               }
            }

            for(var16 = -2; var16 <= 0; ++var16) {
               for(var17 = -2; var17 <= 0; ++var17) {
                  byte var22 = -1;
                  this.func_150526_a(var1, var13 + var16, var15 + var22, var14 + var17);
                  this.func_150526_a(var1, 1 + var13 - var16, var15 + var22, var14 + var17);
                  this.func_150526_a(var1, var13 + var16, var15 + var22, 1 + var14 - var17);
                  this.func_150526_a(var1, 1 + var13 - var16, var15 + var22, 1 + var14 - var17);
                  if ((var16 > -2 || var17 > -1) && (var16 != -1 || var17 != -2)) {
                     byte var23 = 1;
                     this.func_150526_a(var1, var13 + var16, var15 + var23, var14 + var17);
                     this.func_150526_a(var1, 1 + var13 - var16, var15 + var23, var14 + var17);
                     this.func_150526_a(var1, var13 + var16, var15 + var23, 1 + var14 - var17);
                     this.func_150526_a(var1, 1 + var13 - var16, var15 + var23, 1 + var14 - var17);
                  }
               }
            }

            if (var2.nextBoolean()) {
               this.func_150526_a(var1, var13, var15 + 2, var14);
               this.func_150526_a(var1, var13 + 1, var15 + 2, var14);
               this.func_150526_a(var1, var13 + 1, var15 + 2, var14 + 1);
               this.func_150526_a(var1, var13, var15 + 2, var14 + 1);
            }

            for(var16 = -3; var16 <= 4; ++var16) {
               for(var17 = -3; var17 <= 4; ++var17) {
                  if ((var16 != -3 || var17 != -3) && (var16 != -3 || var17 != 4) && (var16 != 4 || var17 != -3) && (var16 != 4 || var17 != 4) && (Math.abs(var16) < 3 || Math.abs(var17) < 3)) {
                     this.func_150526_a(var1, var13 + var16, var15, var14 + var17);
                  }
               }
            }

            for(var16 = -1; var16 <= 2; ++var16) {
               for(var17 = -1; var17 <= 2; ++var17) {
                  if ((var16 < 0 || var16 > 1 || var17 < 0 || var17 > 1) && var2.nextInt(3) <= 0) {
                     int var24 = var2.nextInt(3) + 2;

                     int var25;
                     for(var25 = 0; var25 < var24; ++var25) {
                        this.func_181639_b(var1, new BlockPos(var5 + var16, var15 - var25 - 1, var7 + var17));
                     }

                     int var20;
                     for(var25 = -1; var25 <= 1; ++var25) {
                        for(var20 = -1; var20 <= 1; ++var20) {
                           this.func_150526_a(var1, var13 + var16 + var25, var15, var14 + var17 + var20);
                        }
                     }

                     for(var25 = -2; var25 <= 2; ++var25) {
                        for(var20 = -2; var20 <= 2; ++var20) {
                           if (Math.abs(var25) != 2 || Math.abs(var20) != 2) {
                              this.func_150526_a(var1, var13 + var16 + var25, var15 - 1, var14 + var17 + var20);
                           }
                        }
                     }
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private void func_181639_b(World var1, BlockPos var2) {
      if (this.func_150523_a(var1.getBlockState(var2).getBlock())) {
         this.setBlockAndNotifyAdequately(var1, var2, field_181640_a);
      }

   }
}
