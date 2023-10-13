/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl
implements IBlockSource {
    private final World worldObj;
    private final BlockPos pos;
    private static final String __OBFID = "CL_00001194";

    public BlockSourceImpl(World worldIn, BlockPos p_i46023_2_) {
        this.worldObj = worldIn;
        this.pos = p_i46023_2_;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }

    @Override
    public double getX() {
        return (double)this.pos.getX() + 0.5;
    }

    @Override
    public double getY() {
        return (double)this.pos.getY() + 0.5;
    }

    @Override
    public double getZ() {
        return (double)this.pos.getZ() + 0.5;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public Block getBlock() {
        return this.worldObj.getBlockState(this.pos).getBlock();
    }

    @Override
    public int getBlockMetadata() {
        IBlockState var1 = this.worldObj.getBlockState(this.pos);
        return var1.getBlock().getMetaFromState(var1);
    }

    @Override
    public TileEntity getBlockTileEntity() {
        return this.worldObj.getTileEntity(this.pos);
    }
}

