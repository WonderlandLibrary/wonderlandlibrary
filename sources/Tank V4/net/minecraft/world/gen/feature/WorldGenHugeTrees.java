package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree {
   protected final IBlockState leavesMetadata;
   protected final int baseHeight;
   protected final IBlockState woodMetadata;
   protected int extraRandomHeight;

   public WorldGenHugeTrees(boolean var1, int var2, int var3, IBlockState var4, IBlockState var5) {
      super(var1);
      this.baseHeight = var2;
      this.extraRandomHeight = var3;
      this.woodMetadata = var4;
      this.leavesMetadata = var5;
   }

   protected void func_175925_a(World var1, BlockPos var2, int var3) {
      int var4 = var3 * var3;

      for(int var5 = -var3; var5 <= var3 + 1; ++var5) {
         for(int var6 = -var3; var6 <= var3 + 1; ++var6) {
            int var7 = var5 - 1;
            int var8 = var6 - 1;
            if (var5 * var5 + var6 * var6 <= var4 || var7 * var7 + var8 * var8 <= var4 || var5 * var5 + var8 * var8 <= var4 || var7 * var7 + var6 * var6 <= var4) {
               BlockPos var9 = var2.add(var5, 0, var6);
               Material var10 = var1.getBlockState(var9).getBlock().getMaterial();
               if (var10 == Material.air || var10 == Material.leaves) {
                  this.setBlockAndNotifyAdequately(var1, var9, this.leavesMetadata);
               }
            }
         }
      }

   }

   protected boolean func_175929_a(World var1, Random var2, BlockPos var3, int var4) {
      return var3 >= var4 && var3 != var1;
   }

   protected int func_150533_a(Random var1) {
      int var2 = var1.nextInt(3) + this.baseHeight;
      if (this.extraRandomHeight > 1) {
         var2 += var1.nextInt(this.extraRandomHeight);
      }

      return var2;
   }

   protected void func_175928_b(World var1, BlockPos var2, int var3) {
      int var4 = var3 * var3;

      for(int var5 = -var3; var5 <= var3; ++var5) {
         for(int var6 = -var3; var6 <= var3; ++var6) {
            if (var5 * var5 + var6 * var6 <= var4) {
               BlockPos var7 = var2.add(var5, 0, var6);
               Material var8 = var1.getBlockState(var7).getBlock().getMaterial();
               if (var8 == Material.air || var8 == Material.leaves) {
                  this.setBlockAndNotifyAdequately(var1, var7, this.leavesMetadata);
               }
            }
         }
      }

   }
}
