package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenFlowers extends WorldGenerator {
   private BlockFlower flower;
   private IBlockState field_175915_b;

   public void setGeneratedBlock(BlockFlower var1, BlockFlower.EnumFlowerType var2) {
      this.flower = var1;
      this.field_175915_b = var1.getDefaultState().withProperty(var1.getTypeProperty(), var2);
   }

   public boolean generate(World var1, Random var2, BlockPos var3) {
      for(int var4 = 0; var4 < 64; ++var4) {
         BlockPos var5 = var3.add(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if (var1.isAirBlock(var5) && (!var1.provider.getHasNoSky() || var5.getY() < 255) && this.flower.canBlockStay(var1, var5, this.field_175915_b)) {
            var1.setBlockState(var5, this.field_175915_b, 2);
         }
      }

      return true;
   }

   public WorldGenFlowers(BlockFlower var1, BlockFlower.EnumFlowerType var2) {
      this.setGeneratedBlock(var1, var2);
   }
}
