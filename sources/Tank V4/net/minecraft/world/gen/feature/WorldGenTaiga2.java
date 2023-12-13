package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree {
   private static final IBlockState field_181645_a;
   private static final IBlockState field_181646_b;

   static {
      field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
      field_181646_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);
   }

   public WorldGenTaiga2(boolean var1) {
      super(var1);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = var2.nextInt(4) + 6;
      int var5 = 1 + var2.nextInt(2);
      int var6 = var4 - var5;
      int var7 = 2 + var2.nextInt(2);
      boolean var8 = true;
      if (var3.getY() >= 1 && var3.getY() + var4 + 1 <= 256) {
         int var13;
         int var21;
         for(int var9 = var3.getY(); var9 <= var3.getY() + 1 + var4 && var8; ++var9) {
            boolean var10 = true;
            if (var9 - var3.getY() < var5) {
               var21 = 0;
            } else {
               var21 = var7;
            }

            BlockPos.MutableBlockPos var11 = new BlockPos.MutableBlockPos();

            for(int var12 = var3.getX() - var21; var12 <= var3.getX() + var21 && var8; ++var12) {
               for(var13 = var3.getZ() - var21; var13 <= var3.getZ() + var21 && var8; ++var13) {
                  if (var9 >= 0 && var9 < 256) {
                     Block var14 = var1.getBlockState(var11.func_181079_c(var12, var9, var13)).getBlock();
                     if (var14.getMaterial() != Material.air && var14.getMaterial() != Material.leaves) {
                        var8 = false;
                     }
                  } else {
                     var8 = false;
                  }
               }
            }
         }

         if (!var8) {
            return false;
         } else {
            Block var20 = var1.getBlockState(var3.down()).getBlock();
            if ((var20 == Blocks.grass || var20 == Blocks.dirt || var20 == Blocks.farmland) && var3.getY() < 256 - var4 - 1) {
               this.func_175921_a(var1, var3.down());
               var21 = var2.nextInt(2);
               int var22 = 1;
               byte var23 = 0;

               int var24;
               for(var13 = 0; var13 <= var6; ++var13) {
                  var24 = var3.getY() + var4 - var13;

                  for(int var15 = var3.getX() - var21; var15 <= var3.getX() + var21; ++var15) {
                     int var16 = var15 - var3.getX();

                     for(int var17 = var3.getZ() - var21; var17 <= var3.getZ() + var21; ++var17) {
                        int var18 = var17 - var3.getZ();
                        if (Math.abs(var16) != var21 || Math.abs(var18) != var21 || var21 <= 0) {
                           BlockPos var19 = new BlockPos(var15, var24, var17);
                           if (!var1.getBlockState(var19).getBlock().isFullBlock()) {
                              this.setBlockAndNotifyAdequately(var1, var19, field_181646_b);
                           }
                        }
                     }
                  }

                  if (var21 >= var22) {
                     var21 = var23;
                     var23 = 1;
                     ++var22;
                     if (var22 > var7) {
                        var22 = var7;
                     }
                  } else {
                     ++var21;
                  }
               }

               var13 = var2.nextInt(3);

               for(var24 = 0; var24 < var4 - var13; ++var24) {
                  Block var25 = var1.getBlockState(var3.up(var24)).getBlock();
                  if (var25.getMaterial() == Material.air || var25.getMaterial() == Material.leaves) {
                     this.setBlockAndNotifyAdequately(var1, var3.up(var24), field_181645_a);
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
}
