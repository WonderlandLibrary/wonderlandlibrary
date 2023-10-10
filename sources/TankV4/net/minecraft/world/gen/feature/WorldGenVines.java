package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator {
   public boolean generate(World var1, Random var2, BlockPos var3) {
      for(; var3.getY() < 128; var3 = var3.up()) {
         if (var1.isAirBlock(var3)) {
            EnumFacing[] var7;
            int var6 = (var7 = EnumFacing.Plane.HORIZONTAL.facings()).length;

            for(int var5 = 0; var5 < var6; ++var5) {
               EnumFacing var4 = var7[var5];
               if (Blocks.vine.canPlaceBlockOnSide(var1, var3, var4)) {
                  IBlockState var8 = Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, var4 == EnumFacing.NORTH).withProperty(BlockVine.EAST, var4 == EnumFacing.EAST).withProperty(BlockVine.SOUTH, var4 == EnumFacing.SOUTH).withProperty(BlockVine.WEST, var4 == EnumFacing.WEST);
                  var1.setBlockState(var3, var8, 2);
                  break;
               }
            }
         } else {
            var3 = var3.add(var2.nextInt(4) - var2.nextInt(4), 0, var2.nextInt(4) - var2.nextInt(4));
         }
      }

      return true;
   }
}
