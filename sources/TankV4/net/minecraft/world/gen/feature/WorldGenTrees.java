package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {
   private final IBlockState metaWood;
   private final IBlockState metaLeaves;
   private static final IBlockState field_181653_a;
   private final boolean vinesGrow;
   private static final IBlockState field_181654_b;
   private final int minTreeHeight;

   private void func_181650_b(World var1, BlockPos var2, PropertyBool var3) {
      this.func_181651_a(var1, var2, var3);
      int var4 = 4;

      for(var2 = var2.down(); var1.getBlockState(var2).getBlock().getMaterial() == Material.air && var4 > 0; --var4) {
         this.func_181651_a(var1, var2, var3);
         var2 = var2.down();
      }

   }

   private void func_181651_a(World var1, BlockPos var2, PropertyBool var3) {
      this.setBlockAndNotifyAdequately(var1, var2, Blocks.vine.getDefaultState().withProperty(var3, true));
   }

   public WorldGenTrees(boolean var1) {
      this(var1, 4, field_181653_a, field_181654_b, false);
   }

   private void func_181652_a(World var1, int var2, BlockPos var3, EnumFacing var4) {
      this.setBlockAndNotifyAdequately(var1, var3, Blocks.cocoa.getDefaultState().withProperty(BlockCocoa.AGE, var2).withProperty(BlockCocoa.FACING, var4));
   }

   static {
      field_181653_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
      field_181654_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = var2.nextInt(3) + this.minTreeHeight;
      boolean var5 = true;
      if (var3.getY() >= 1 && var3.getY() + var4 + 1 <= 256) {
         byte var7;
         int var9;
         int var10;
         for(int var6 = var3.getY(); var6 <= var3.getY() + 1 + var4; ++var6) {
            var7 = 1;
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
            Block var19 = var1.getBlockState(var3.down()).getBlock();
            if ((var19 == Blocks.grass || var19 == Blocks.dirt || var19 == Blocks.farmland) && var3.getY() < 256 - var4 - 1) {
               this.func_175921_a(var1, var3.down());
               var7 = 3;
               byte var20 = 0;

               int var11;
               int var13;
               int var14;
               BlockPos var16;
               for(var9 = var3.getY() - var7 + var4; var9 <= var3.getY() + var4; ++var9) {
                  var10 = var9 - (var3.getY() + var4);
                  var11 = var20 + 1 - var10 / 2;

                  for(int var12 = var3.getX() - var11; var12 <= var3.getX() + var11; ++var12) {
                     var13 = var12 - var3.getX();

                     for(var14 = var3.getZ() - var11; var14 <= var3.getZ() + var11; ++var14) {
                        int var15 = var14 - var3.getZ();
                        if (Math.abs(var13) != var11 || Math.abs(var15) != var11 || var2.nextInt(2) != 0 && var10 != 0) {
                           var16 = new BlockPos(var12, var9, var14);
                           Block var17 = var1.getBlockState(var16).getBlock();
                           if (var17.getMaterial() == Material.air || var17.getMaterial() == Material.leaves || var17.getMaterial() == Material.vine) {
                              this.setBlockAndNotifyAdequately(var1, var16, this.metaLeaves);
                           }
                        }
                     }
                  }
               }

               for(var9 = 0; var9 < var4; ++var9) {
                  Block var21 = var1.getBlockState(var3.up(var9)).getBlock();
                  if (var21.getMaterial() == Material.air || var21.getMaterial() == Material.leaves || var21.getMaterial() == Material.vine) {
                     this.setBlockAndNotifyAdequately(var1, var3.up(var9), this.metaWood);
                     if (this.vinesGrow && var9 > 0) {
                        if (var2.nextInt(3) > 0 && var1.isAirBlock(var3.add(-1, var9, 0))) {
                           this.func_181651_a(var1, var3.add(-1, var9, 0), BlockVine.EAST);
                        }

                        if (var2.nextInt(3) > 0 && var1.isAirBlock(var3.add(1, var9, 0))) {
                           this.func_181651_a(var1, var3.add(1, var9, 0), BlockVine.WEST);
                        }

                        if (var2.nextInt(3) > 0 && var1.isAirBlock(var3.add(0, var9, -1))) {
                           this.func_181651_a(var1, var3.add(0, var9, -1), BlockVine.SOUTH);
                        }

                        if (var2.nextInt(3) > 0 && var1.isAirBlock(var3.add(0, var9, 1))) {
                           this.func_181651_a(var1, var3.add(0, var9, 1), BlockVine.NORTH);
                        }
                     }
                  }
               }

               if (this.vinesGrow) {
                  for(var9 = var3.getY() - 3 + var4; var9 <= var3.getY() + var4; ++var9) {
                     var10 = var9 - (var3.getY() + var4);
                     var11 = 2 - var10 / 2;
                     BlockPos.MutableBlockPos var24 = new BlockPos.MutableBlockPos();

                     for(var13 = var3.getX() - var11; var13 <= var3.getX() + var11; ++var13) {
                        for(var14 = var3.getZ() - var11; var14 <= var3.getZ() + var11; ++var14) {
                           var24.func_181079_c(var13, var9, var14);
                           if (var1.getBlockState(var24).getBlock().getMaterial() == Material.leaves) {
                              BlockPos var26 = var24.west();
                              var16 = var24.east();
                              BlockPos var27 = var24.north();
                              BlockPos var18 = var24.south();
                              if (var2.nextInt(4) == 0 && var1.getBlockState(var26).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(var1, var26, BlockVine.EAST);
                              }

                              if (var2.nextInt(4) == 0 && var1.getBlockState(var16).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(var1, var16, BlockVine.WEST);
                              }

                              if (var2.nextInt(4) == 0 && var1.getBlockState(var27).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(var1, var27, BlockVine.SOUTH);
                              }

                              if (var2.nextInt(4) == 0 && var1.getBlockState(var18).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(var1, var18, BlockVine.NORTH);
                              }
                           }
                        }
                     }
                  }

                  if (var2.nextInt(5) == 0 && var4 > 5) {
                     for(var9 = 0; var9 < 2; ++var9) {
                        Iterator var22 = EnumFacing.Plane.HORIZONTAL.iterator();

                        while(var22.hasNext()) {
                           Object var23 = var22.next();
                           if (var2.nextInt(4 - var9) == 0) {
                              EnumFacing var25 = ((EnumFacing)var23).getOpposite();
                              this.func_181652_a(var1, var2.nextInt(3), var3.add(var25.getFrontOffsetX(), var4 - 5 + var9, var25.getFrontOffsetZ()), (EnumFacing)var23);
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

   public WorldGenTrees(boolean var1, int var2, IBlockState var3, IBlockState var4, boolean var5) {
      super(var1);
      this.minTreeHeight = var2;
      this.metaWood = var3;
      this.metaLeaves = var4;
      this.vinesGrow = var5;
   }
}
