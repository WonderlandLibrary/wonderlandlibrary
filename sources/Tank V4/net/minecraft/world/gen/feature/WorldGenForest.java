package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenForest extends WorldGenAbstractTree {
   private static final IBlockState field_181629_a;
   private boolean useExtraRandomHeight;
   private static final IBlockState field_181630_b;

   public WorldGenForest(boolean var1, boolean var2) {
      super(var1);
      this.useExtraRandomHeight = var2;
   }

   static {
      field_181629_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
      field_181630_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLeaf.CHECK_DECAY, false);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      int var4 = var2.nextInt(3) + 5;
      if (this.useExtraRandomHeight) {
         var4 += var2.nextInt(7);
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
            Block var17 = var1.getBlockState(var3.down()).getBlock();
            if ((var17 == Blocks.grass || var17 == Blocks.dirt || var17 == Blocks.farmland) && var3.getY() < 256 - var4 - 1) {
               this.func_175921_a(var1, var3.down());

               int var18;
               for(var18 = var3.getY() - 3 + var4; var18 <= var3.getY() + var4; ++var18) {
                  int var19 = var18 - (var3.getY() + var4);
                  var9 = 1 - var19 / 2;

                  for(var10 = var3.getX() - var9; var10 <= var3.getX() + var9; ++var10) {
                     int var11 = var10 - var3.getX();

                     for(int var12 = var3.getZ() - var9; var12 <= var3.getZ() + var9; ++var12) {
                        int var13 = var12 - var3.getZ();
                        if (Math.abs(var11) != var9 || Math.abs(var13) != var9 || var2.nextInt(2) != 0 && var19 != 0) {
                           BlockPos var14 = new BlockPos(var10, var18, var12);
                           Block var15 = var1.getBlockState(var14).getBlock();
                           if (var15.getMaterial() == Material.air || var15.getMaterial() == Material.leaves) {
                              this.setBlockAndNotifyAdequately(var1, var14, field_181630_b);
                           }
                        }
                     }
                  }
               }

               for(var18 = 0; var18 < var4; ++var18) {
                  Block var20 = var1.getBlockState(var3.up(var18)).getBlock();
                  if (var20.getMaterial() == Material.air || var20.getMaterial() == Material.leaves) {
                     this.setBlockAndNotifyAdequately(var1, var3.up(var18), field_181629_a);
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
