package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenClay extends WorldGenerator {
   private Block field_150546_a;
   private int numberOfBlocks;

   public WorldGenClay(int var1) {
      this.field_150546_a = Blocks.clay;
      this.numberOfBlocks = var1;
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (var1.getBlockState(var3).getBlock().getMaterial() != Material.water) {
         return false;
      } else {
         int var4 = var2.nextInt(this.numberOfBlocks - 2) + 2;
         byte var5 = 1;

         for(int var6 = var3.getX() - var4; var6 <= var3.getX() + var4; ++var6) {
            for(int var7 = var3.getZ() - var4; var7 <= var3.getZ() + var4; ++var7) {
               int var8 = var6 - var3.getX();
               int var9 = var7 - var3.getZ();
               if (var8 * var8 + var9 * var9 <= var4 * var4) {
                  for(int var10 = var3.getY() - var5; var10 <= var3.getY() + var5; ++var10) {
                     BlockPos var11 = new BlockPos(var6, var10, var7);
                     Block var12 = var1.getBlockState(var11).getBlock();
                     if (var12 == Blocks.dirt || var12 == Blocks.clay) {
                        var1.setBlockState(var11, this.field_150546_a.getDefaultState(), 2);
                     }
                  }
               }
            }
         }

         return true;
      }
   }
}
