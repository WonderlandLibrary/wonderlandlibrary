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

public class WorldGenTaiga1 extends WorldGenAbstractTree {
   private static final IBlockState field_181636_a;
   private static final IBlockState field_181637_b;

   static {
      field_181636_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
      field_181637_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = var2.nextInt(5) + 7;
      int var5 = var4 - var2.nextInt(2) - 3;
      int var6 = var4 - var5;
      int var7 = 1 + var2.nextInt(var6 + 1);
      boolean var8 = true;
      if (var3.getY() >= 1 && var3.getY() + var4 + 1 <= 256) {
         int var12;
         int var13;
         int var18;
         for(int var9 = var3.getY(); var9 <= var3.getY() + 1 + var4 && var8; ++var9) {
            boolean var10 = true;
            if (var9 - var3.getY() < var5) {
               var18 = 0;
            } else {
               var18 = var7;
            }

            BlockPos.MutableBlockPos var11 = new BlockPos.MutableBlockPos();

            for(var12 = var3.getX() - var18; var12 <= var3.getX() + var18 && var8; ++var12) {
               for(var13 = var3.getZ() - var18; var13 <= var3.getZ() + var18 && var8; ++var13) {
                  if (var9 >= 0 && var9 < 256) {
                     if (!this.func_150523_a(var1.getBlockState(var11.func_181079_c(var12, var9, var13)).getBlock())) {
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
            Block var17 = var1.getBlockState(var3.down()).getBlock();
            if ((var17 == Blocks.grass || var17 == Blocks.dirt) && var3.getY() < 256 - var4 - 1) {
               this.func_175921_a(var1, var3.down());
               var18 = 0;

               int var19;
               for(var19 = var3.getY() + var4; var19 >= var3.getY() + var5; --var19) {
                  for(var12 = var3.getX() - var18; var12 <= var3.getX() + var18; ++var12) {
                     var13 = var12 - var3.getX();

                     for(int var14 = var3.getZ() - var18; var14 <= var3.getZ() + var18; ++var14) {
                        int var15 = var14 - var3.getZ();
                        if (Math.abs(var13) != var18 || Math.abs(var15) != var18 || var18 <= 0) {
                           BlockPos var16 = new BlockPos(var12, var19, var14);
                           if (!var1.getBlockState(var16).getBlock().isFullBlock()) {
                              this.setBlockAndNotifyAdequately(var1, var16, field_181637_b);
                           }
                        }
                     }
                  }

                  if (var18 >= 1 && var19 == var3.getY() + var5 + 1) {
                     --var18;
                  } else if (var18 < var7) {
                     ++var18;
                  }
               }

               for(var19 = 0; var19 < var4 - 1; ++var19) {
                  Block var20 = var1.getBlockState(var3.up(var19)).getBlock();
                  if (var20.getMaterial() == Material.air || var20.getMaterial() == Material.leaves) {
                     this.setBlockAndNotifyAdequately(var1, var3.up(var19), field_181636_a);
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

   public WorldGenTaiga1() {
      super(false);
   }
}
