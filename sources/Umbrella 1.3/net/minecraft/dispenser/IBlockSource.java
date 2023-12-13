/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.dispenser;

import net.minecraft.block.Block;
import net.minecraft.dispenser.ILocatableSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource
extends ILocatableSource {
    @Override
    public double getX();

    @Override
    public double getY();

    @Override
    public double getZ();

    public BlockPos getBlockPos();

    public Block getBlock();

    public int getBlockMetadata();

    public TileEntity getBlockTileEntity();
}

