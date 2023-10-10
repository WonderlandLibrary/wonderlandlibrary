package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenAbstractTree extends WorldGenerator {
   protected boolean func_150523_a(Block var1) {
      Material var2 = var1.getMaterial();
      return var2 == Material.air || var2 == Material.leaves || var1 == Blocks.grass || var1 == Blocks.dirt || var1 == Blocks.log || var1 == Blocks.log2 || var1 == Blocks.sapling || var1 == Blocks.vine;
   }

   public WorldGenAbstractTree(boolean var1) {
      super(var1);
   }

   public void func_180711_a(World var1, Random var2, BlockPos var3) {
   }

   protected void func_175921_a(World var1, BlockPos var2) {
      if (var1.getBlockState(var2).getBlock() != Blocks.dirt) {
         this.setBlockAndNotifyAdequately(var1, var2, Blocks.dirt.getDefaultState());
      }

   }
}
