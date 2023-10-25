package net.minecraft.dispenser;

import net.minecraft.dispenser.ILocatableSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource
extends ILocatableSource {
    public double getX();

    public double getY();

    public double getZ();

    public BlockPos getBlockPos();

    public int getBlockMetadata();

    public <T extends TileEntity> T getBlockTileEntity();
}
