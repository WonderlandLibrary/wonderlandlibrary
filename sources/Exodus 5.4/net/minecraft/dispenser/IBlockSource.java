/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.dispenser;

import net.minecraft.dispenser.ILocatableSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public interface IBlockSource
extends ILocatableSource {
    public int getBlockMetadata();

    public BlockPos getBlockPos();

    public <T extends TileEntity> T getBlockTileEntity();

    @Override
    public double getZ();

    @Override
    public double getX();

    @Override
    public double getY();
}

