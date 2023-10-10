package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees {
   private final IBlockState woodMetadata;
   private final IBlockState leavesMetadata;

   public WorldGenShrub(IBlockState var1, IBlockState var2) {
      super(false);
      this.woodMetadata = var1;
      this.leavesMetadata = var2;
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      Block var4;
      while(((var4 = var1.getBlockState(var3).getBlock()).getMaterial() == Material.air || var4.getMaterial() == Material.leaves) && var3.getY() > 0) {
         var3 = var3.down();
      }

      Block var5 = var1.getBlockState(var3).getBlock();
      if (var5 == Blocks.dirt || var5 == Blocks.grass) {
         var3 = var3.up();
         this.setBlockAndNotifyAdequately(var1, var3, this.woodMetadata);

         for(int var6 = var3.getY(); var6 <= var3.getY() + 2; ++var6) {
            int var7 = var6 - var3.getY();
            int var8 = 2 - var7;

            for(int var9 = var3.getX() - var8; var9 <= var3.getX() + var8; ++var9) {
               int var10 = var9 - var3.getX();

               for(int var11 = var3.getZ() - var8; var11 <= var3.getZ() + var8; ++var11) {
                  int var12 = var11 - var3.getZ();
                  if (Math.abs(var10) != var8 || Math.abs(var12) != var8 || var2.nextInt(2) != 0) {
                     BlockPos var13 = new BlockPos(var9, var6, var11);
                     if (!var1.getBlockState(var13).getBlock().isFullBlock()) {
                        this.setBlockAndNotifyAdequately(var1, var13, this.leavesMetadata);
                     }
                  }
               }
            }
         }
      }

      return true;
   }
}
