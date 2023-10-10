package net.minecraft.dispenser;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource extends ILocatableSource {
   TileEntity getBlockTileEntity();

   double getY();

   double getZ();

   int getBlockMetadata();

   BlockPos getBlockPos();

   double getX();
}
