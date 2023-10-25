/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl
implements IBlockSource {
    private final World worldObj;
    private final BlockPos pos;

    public BlockSourceImpl(World world, BlockPos blockPos) {
        this.worldObj = world;
        this.pos = blockPos;
    }

    @Override
    public double getY() {
        return (double)this.pos.getY() + 0.5;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public <T extends TileEntity> T getBlockTileEntity() {
        return (T)this.worldObj.getTileEntity(this.pos);
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public double getZ() {
        return (double)this.pos.getZ() + 0.5;
    }

    @Override
    public double getX() {
        return (double)this.pos.getX() + 0.5;
    }

    @Override
    public int getBlockMetadata() {
        IBlockState iBlockState = this.worldObj.getBlockState(this.pos);
        return iBlockState.getBlock().getMetaFromState(iBlockState);
    }
}

