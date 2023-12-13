package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator {
   private final boolean field_94524_b;
   private final Block field_150553_a;

   public WorldGenHellLava(Block var1, boolean var2) {
      this.field_150553_a = var1;
      this.field_94524_b = var2;
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      if (var1.getBlockState(var3.up()).getBlock() != Blocks.netherrack) {
         return false;
      } else if (var1.getBlockState(var3).getBlock().getMaterial() != Material.air && var1.getBlockState(var3).getBlock() != Blocks.netherrack) {
         return false;
      } else {
         int var4 = 0;
         if (var1.getBlockState(var3.west()).getBlock() == Blocks.netherrack) {
            ++var4;
         }

         if (var1.getBlockState(var3.east()).getBlock() == Blocks.netherrack) {
            ++var4;
         }

         if (var1.getBlockState(var3.north()).getBlock() == Blocks.netherrack) {
            ++var4;
         }

         if (var1.getBlockState(var3.south()).getBlock() == Blocks.netherrack) {
            ++var4;
         }

         if (var1.getBlockState(var3.down()).getBlock() == Blocks.netherrack) {
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

         if (var1.isAirBlock(var3.down())) {
            ++var5;
         }

         if (!this.field_94524_b && var4 == 4 && var5 == 1 || var4 == 5) {
            var1.setBlockState(var3, this.field_150553_a.getDefaultState(), 2);
            var1.forceBlockUpdateTick(this.field_150553_a, var3, var2);
         }

         return true;
      }
   }
}
