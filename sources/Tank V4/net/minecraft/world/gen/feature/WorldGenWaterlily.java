package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator {
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for(int var4 = 0; var4 < 10; ++var4) {
         int var5 = var3.getX() + var2.nextInt(8) - var2.nextInt(8);
         int var6 = var3.getY() + var2.nextInt(4) - var2.nextInt(4);
         int var7 = var3.getZ() + var2.nextInt(8) - var2.nextInt(8);
         if (var1.isAirBlock(new BlockPos(var5, var6, var7)) && Blocks.waterlily.canPlaceBlockAt(var1, new BlockPos(var5, var6, var7))) {
            var1.setBlockState(new BlockPos(var5, var6, var7), Blocks.waterlily.getDefaultState(), 2);
         }
      }

      return true;
   }
}
