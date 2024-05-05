package net.minecraft.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

public abstract interface IBlockAccess
{
  public abstract TileEntity getTileEntity(BlockPos paramBlockPos);
  
  public abstract int getCombinedLight(BlockPos paramBlockPos, int paramInt);
  
  public abstract IBlockState getBlockState(BlockPos paramBlockPos);
  
  public abstract boolean isAirBlock(BlockPos paramBlockPos);
  
  public abstract BiomeGenBase getBiomeGenForCoords(BlockPos paramBlockPos);
  
  public abstract boolean extendedLevelsInChunkCache();
  
  public abstract int getStrongPower(BlockPos paramBlockPos, EnumFacing paramEnumFacing);
  
  public abstract WorldType getWorldType();
}
