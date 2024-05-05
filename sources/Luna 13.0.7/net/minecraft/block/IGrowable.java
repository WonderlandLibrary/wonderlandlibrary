package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract interface IGrowable
{
  public abstract boolean isStillGrowing(World paramWorld, BlockPos paramBlockPos, IBlockState paramIBlockState, boolean paramBoolean);
  
  public abstract boolean canUseBonemeal(World paramWorld, Random paramRandom, BlockPos paramBlockPos, IBlockState paramIBlockState);
  
  public abstract void grow(World paramWorld, Random paramRandom, BlockPos paramBlockPos, IBlockState paramIBlockState);
}
