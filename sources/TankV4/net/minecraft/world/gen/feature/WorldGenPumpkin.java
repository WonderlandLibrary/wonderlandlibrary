package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenPumpkin extends WorldGenerator {
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for(int var4 = 0; var4 < 64; ++var4) {
         BlockPos var5 = var3.add(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if (var1.isAirBlock(var5) && var1.getBlockState(var5.down()).getBlock() == Blocks.grass && Blocks.pumpkin.canPlaceBlockAt(var1, var5)) {
            var1.setBlockState(var5, Blocks.pumpkin.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(var2)), 2);
         }
      }

      return true;
   }
}
