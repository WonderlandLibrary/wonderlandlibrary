package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree {
   private static final IBlockState field_181649_b;
   private static final IBlockState field_181648_a;

   private void func_181647_a(World var1, BlockPos var2, PropertyBool var3) {
      IBlockState var4 = Blocks.vine.getDefaultState().withProperty(var3, true);
      this.setBlockAndNotifyAdequately(var1, var2, var4);
      int var5 = 4;

      for(var2 = var2.down(); var1.getBlockState(var2).getBlock().getMaterial() == Material.air && var5 > 0; --var5) {
         this.setBlockAndNotifyAdequately(var1, var2, var4);
         var2 = var2.down();
      }

   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4;
      for(var4 = var2.nextInt(4) + 5; var1.getBlockState(var3.down()).getBlock().getMaterial() == Material.water; var3 = var3.down()) {
      }

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
               var7 = 3;
            }

            BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

            for(var9 = var3.getX() - var7; var9 <= var3.getX() + var7 && var5; ++var9) {
               for(var10 = var3.getZ() - var7; var10 <= var3.getZ() + var7 && var5; ++var10) {
                  if (var6 >= 0 && var6 < 256) {
                     Block var11 = var1.getBlockState(var8.func_181079_c(var9, var6, var10)).getBlock();
                     if (var11.getMaterial() != Material.air && var11.getMaterial() != Material.leaves) {
                        if (var11 != Blocks.water && var11 != Blocks.flowing_water) {
                           var5 = false;
                        } else if (var6 > var3.getY()) {
                           var5 = false;
                        }
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
            Block var18 = var1.getBlockState(var3.down()).getBlock();
            if ((var18 == Blocks.grass || var18 == Blocks.dirt) && var3.getY() < 256 - var4 - 1) {
               this.func_175921_a(var1, var3.down());

               int var12;
               BlockPos var14;
               int var19;
               int var20;
               int var22;
               for(var19 = var3.getY() - 3 + var4; var19 <= var3.getY() + var4; ++var19) {
                  var20 = var19 - (var3.getY() + var4);
                  var9 = 2 - var20 / 2;

                  for(var10 = var3.getX() - var9; var10 <= var3.getX() + var9; ++var10) {
                     var22 = var10 - var3.getX();

                     for(var12 = var3.getZ() - var9; var12 <= var3.getZ() + var9; ++var12) {
                        int var13 = var12 - var3.getZ();
                        if (Math.abs(var22) != var9 || Math.abs(var13) != var9 || var2.nextInt(2) != 0 && var20 != 0) {
                           var14 = new BlockPos(var10, var19, var12);
                           if (!var1.getBlockState(var14).getBlock().isFullBlock()) {
                              this.setBlockAndNotifyAdequately(var1, var14, field_181649_b);
                           }
                        }
                     }
                  }
               }

               for(var19 = 0; var19 < var4; ++var19) {
                  Block var21 = var1.getBlockState(var3.up(var19)).getBlock();
                  if (var21.getMaterial() == Material.air || var21.getMaterial() == Material.leaves || var21 == Blocks.flowing_water || var21 == Blocks.water) {
                     this.setBlockAndNotifyAdequately(var1, var3.up(var19), field_181648_a);
                  }
               }

               for(var19 = var3.getY() - 3 + var4; var19 <= var3.getY() + var4; ++var19) {
                  var20 = var19 - (var3.getY() + var4);
                  var9 = 2 - var20 / 2;
                  BlockPos.MutableBlockPos var23 = new BlockPos.MutableBlockPos();

                  for(var22 = var3.getX() - var9; var22 <= var3.getX() + var9; ++var22) {
                     for(var12 = var3.getZ() - var9; var12 <= var3.getZ() + var9; ++var12) {
                        var23.func_181079_c(var22, var19, var12);
                        if (var1.getBlockState(var23).getBlock().getMaterial() == Material.leaves) {
                           BlockPos var24 = var23.west();
                           var14 = var23.east();
                           BlockPos var15 = var23.north();
                           BlockPos var16 = var23.south();
                           if (var2.nextInt(4) == 0 && var1.getBlockState(var24).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(var1, var24, BlockVine.EAST);
                           }

                           if (var2.nextInt(4) == 0 && var1.getBlockState(var14).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(var1, var14, BlockVine.WEST);
                           }

                           if (var2.nextInt(4) == 0 && var1.getBlockState(var15).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(var1, var15, BlockVine.SOUTH);
                           }

                           if (var2.nextInt(4) == 0 && var1.getBlockState(var16).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(var1, var16, BlockVine.NORTH);
                           }
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

   public WorldGenSwamp() {
      super(false);
   }

   static {
      field_181648_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
      field_181649_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, false);
   }
}
