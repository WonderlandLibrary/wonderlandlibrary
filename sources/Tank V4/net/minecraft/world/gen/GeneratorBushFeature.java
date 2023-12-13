package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorBushFeature extends WorldGenerator {
   private BlockBush field_175908_a;

   public boolean generate(World var1, Random var2, BlockPos var3) {
      for(int var4 = 0; var4 < 64; ++var4) {
         BlockPos var5 = var3.add(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if (var1.isAirBlock(var5) && (!var1.provider.getHasNoSky() || var5.getY() < 255) && this.field_175908_a.canBlockStay(var1, var5, this.field_175908_a.getDefaultState())) {
            var1.setBlockState(var5, this.field_175908_a.getDefaultState(), 2);
         }
      }

      return true;
   }

   public GeneratorBushFeature(BlockBush var1) {
      this.field_175908_a = var1;
   }
}
