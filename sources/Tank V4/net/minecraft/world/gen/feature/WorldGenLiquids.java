package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator {
   private Block block;

   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (var1.getBlockState(var3.up()).getBlock() != Blocks.stone) {
         return false;
      } else if (var1.getBlockState(var3.down()).getBlock() != Blocks.stone) {
         return false;
      } else if (var1.getBlockState(var3).getBlock().getMaterial() != Material.air && var1.getBlockState(var3).getBlock() != Blocks.stone) {
         return false;
      } else {
         int var4 = 0;
         if (var1.getBlockState(var3.west()).getBlock() == Blocks.stone) {
            ++var4;
         }

         if (var1.getBlockState(var3.east()).getBlock() == Blocks.stone) {
            ++var4;
         }

         if (var1.getBlockState(var3.north()).getBlock() == Blocks.stone) {
            ++var4;
         }

         if (var1.getBlockState(var3.south()).getBlock() == Blocks.stone) {
            ++var4;
         }

         int var5 = 0;
         if (var1.isAirBlock(var3.west())) {
            ++var5;
         }

         if (var1.isAirBlock(var3.east())) {
            ++var5;
         }

         if (var1.isAirBlock(var3.north())) {
            ++var5;
         }

         if (var1.isAirBlock(var3.south())) {
            ++var5;
         }

         if (var4 == 3 && var5 == 1) {
            var1.setBlockState(var3, this.block.getDefaultState(), 2);
            var1.forceBlockUpdateTick(this.block, var3, var2);
         }

         return true;
      }
   }

   public WorldGenLiquids(Block var1) {
      this.block = var1;
   }
}
