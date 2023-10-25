package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IGrowable {
  boolean canGrow(World paramWorld, BlockPos paramBlockPos, IBlockState paramIBlockState, boolean paramBoolean);
  
  boolean canUseBonemeal(World paramWorld, Random paramRandom, BlockPos paramBlockPos, IBlockState paramIBlockState);
  
  void grow(World paramWorld, Random paramRandom, BlockPos paramBlockPos, IBlockState paramIBlockState);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\IGrowable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */