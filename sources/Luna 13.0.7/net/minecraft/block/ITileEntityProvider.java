package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract interface ITileEntityProvider
{
  public abstract TileEntity createNewTileEntity(World paramWorld, int paramInt);
}
