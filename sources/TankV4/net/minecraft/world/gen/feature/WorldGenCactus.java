package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator {
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for(int var4 = 0; var4 < 10; ++var4) {
         BlockPos var5 = var3.add(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if (var1.isAirBlock(var5)) {
            int var6 = 1 + var2.nextInt(var2.nextInt(3) + 1);

            for(int var7 = 0; var7 < var6; ++var7) {
               if (Blocks.cactus.canBlockStay(var1, var5)) {
                  var1.setBlockState(var5.up(var7), Blocks.cactus.getDefaultState(), 2);
               }
            }
         }
      }

      return true;
   }
}
