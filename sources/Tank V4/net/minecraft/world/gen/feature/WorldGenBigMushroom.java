package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator {
   private Block mushroomType;

   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (this.mushroomType == null) {
         this.mushroomType = var2.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block;
      }

      int var4 = var2.nextInt(3) + 4;
      boolean var5 = true;
      if (var3.getY() >= 1 && var3.getY() + var4 + 1 < 256) {
         int var9;
         int var10;
         for(int var6 = var3.getY(); var6 <= var3.getY() + 1 + var4; ++var6) {
            byte var7 = 3;
            if (var6 <= var3.getY() + 3) {
               var7 = 0;
            }

            BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

            for(var9 = var3.getX() - var7; var9 <= var3.getX() + var7 && var5; ++var9) {
               for(var10 = var3.getZ() - var7; var10 <= var3.getZ() + var7 && var5; ++var10) {
                  if (var6 >= 0 && var6 < 256) {
                     Block var11 = var1.getBlockState(var8.func_181079_c(var9, var6, var10)).getBlock();
                     if (var11.getMaterial() != Material.air && var11.getMaterial() != Material.leaves) {
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
            if (var19 != Blocks.dirt && var19 != Blocks.grass && var19 != Blocks.mycelium) {
               return false;
            } else {
               int var20 = var3.getY() + var4;
               if (this.mushroomType == Blocks.red_mushroom_block) {
                  var20 = var3.getY() + var4 - 3;
               }

               int var21;
               for(var21 = var20; var21 <= var3.getY() + var4; ++var21) {
                  var9 = 1;
                  if (var21 < var3.getY() + var4) {
                     ++var9;
                  }

                  if (this.mushroomType == Blocks.brown_mushroom_block) {
                     var9 = 3;
                  }

                  var10 = var3.getX() - var9;
                  int var23 = var3.getX() + var9;
                  int var12 = var3.getZ() - var9;
                  int var13 = var3.getZ() + var9;

                  for(int var14 = var10; var14 <= var23; ++var14) {
                     for(int var15 = var12; var15 <= var13; ++var15) {
                        int var16 = 5;
                        if (var14 == var10) {
                           --var16;
                        } else if (var14 == var23) {
                           ++var16;
                        }

                        if (var15 == var12) {
                           var16 -= 3;
                        } else if (var15 == var13) {
                           var16 += 3;
                        }

                        BlockHugeMushroom.EnumType var17 = BlockHugeMushroom.EnumType.byMetadata(var16);
                        if (this.mushroomType == Blocks.brown_mushroom_block || var21 < var3.getY() + var4) {
                           if ((var14 == var10 || var14 == var23) && (var15 == var12 || var15 == var13)) {
                              continue;
                           }

                           if (var14 == var3.getX() - (var9 - 1) && var15 == var12) {
                              var17 = BlockHugeMushroom.EnumType.NORTH_WEST;
                           }

                           if (var14 == var10 && var15 == var3.getZ() - (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumType.NORTH_WEST;
                           }

                           if (var14 == var3.getX() + (var9 - 1) && var15 == var12) {
                              var17 = BlockHugeMushroom.EnumType.NORTH_EAST;
                           }

                           if (var14 == var23 && var15 == var3.getZ() - (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumType.NORTH_EAST;
                           }

                           if (var14 == var3.getX() - (var9 - 1) && var15 == var13) {
                              var17 = BlockHugeMushroom.EnumType.SOUTH_WEST;
                           }

                           if (var14 == var10 && var15 == var3.getZ() + (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumType.SOUTH_WEST;
                           }

                           if (var14 == var3.getX() + (var9 - 1) && var15 == var13) {
                              var17 = BlockHugeMushroom.EnumType.SOUTH_EAST;
                           }

                           if (var14 == var23 && var15 == var3.getZ() + (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumType.SOUTH_EAST;
                           }
                        }

                        if (var17 == BlockHugeMushroom.EnumType.CENTER && var21 < var3.getY() + var4) {
                           var17 = BlockHugeMushroom.EnumType.ALL_INSIDE;
                        }

                        if (var3.getY() >= var3.getY() + var4 - 1 || var17 != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                           BlockPos var18 = new BlockPos(var14, var21, var15);
                           if (!var1.getBlockState(var18).getBlock().isFullBlock()) {
                              this.setBlockAndNotifyAdequately(var1, var18, this.mushroomType.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, var17));
                           }
                        }
                     }
                  }
               }

               for(var21 = 0; var21 < var4; ++var21) {
                  Block var22 = var1.getBlockState(var3.up(var21)).getBlock();
                  if (!var22.isFullBlock()) {
                     this.setBlockAndNotifyAdequately(var1, var3.up(var21), this.mushroomType.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   public WorldGenBigMushroom(Block var1) {
      super(true);
      this.mushroomType = var1;
   }

   public WorldGenBigMushroom() {
      super(false);
   }
}
