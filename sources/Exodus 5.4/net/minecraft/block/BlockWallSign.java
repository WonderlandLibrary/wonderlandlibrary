/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWallSign
extends BlockSign {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    @Override
    public IBlockState getStateFromMeta(int n) {
        EnumFacing enumFacing = EnumFacing.getFront(n);
        if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumFacing);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        EnumFacing enumFacing = iBlockAccess.getBlockState(blockPos).getValue(FACING);
        float f = 0.28125f;
        float f2 = 0.78125f;
        float f3 = 0.0f;
        float f4 = 1.0f;
        float f5 = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        switch (enumFacing) {
            case NORTH: {
                this.setBlockBounds(f3, f, 1.0f - f5, f4, f2, 1.0f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(f3, f, 0.0f, f4, f2, f5);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f5, f, f3, 1.0f, f2, f4);
                break;
            }
            case EAST: {
                this.setBlockBounds(0.0f, f, f3, f5, f2, f4);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        if (!world.getBlockState(blockPos.offset(enumFacing.getOpposite())).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
        super.onNeighborBlockChange(world, blockPos, iBlockState, block);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return iBlockState.getValue(FACING).getIndex();
    }

    public BlockWallSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }
}

