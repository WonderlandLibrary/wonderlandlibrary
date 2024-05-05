package net.minecraft.dispenser;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public abstract interface IBlockSource
  extends ILocatableSource
{
  public abstract double getX();
  
  public abstract double getY();
  
  public abstract double getZ();
  
  public abstract BlockPos getBlockPos();
  
  public abstract Block getBlock();
  
  public abstract int getBlockMetadata();
  
  public abstract TileEntity getBlockTileEntity();
}
