/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered
extends BlockRailBase {
    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>(){

        public boolean apply(BlockRailBase.EnumRailDirection enumRailDirection) {
            return enumRailDirection != BlockRailBase.EnumRailDirection.NORTH_EAST && enumRailDirection != BlockRailBase.EnumRailDirection.NORTH_WEST && enumRailDirection != BlockRailBase.EnumRailDirection.SOUTH_EAST && enumRailDirection != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SHAPE, POWERED);
    }

    protected boolean func_176567_a(World world, BlockPos blockPos, boolean bl, int n, BlockRailBase.EnumRailDirection enumRailDirection) {
        IBlockState iBlockState = world.getBlockState(blockPos);
        if (iBlockState.getBlock() != this) {
            return false;
        }
        BlockRailBase.EnumRailDirection enumRailDirection2 = iBlockState.getValue(SHAPE);
        return enumRailDirection != BlockRailBase.EnumRailDirection.EAST_WEST || enumRailDirection2 != BlockRailBase.EnumRailDirection.NORTH_SOUTH && enumRailDirection2 != BlockRailBase.EnumRailDirection.ASCENDING_NORTH && enumRailDirection2 != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH ? (enumRailDirection != BlockRailBase.EnumRailDirection.NORTH_SOUTH || enumRailDirection2 != BlockRailBase.EnumRailDirection.EAST_WEST && enumRailDirection2 != BlockRailBase.EnumRailDirection.ASCENDING_EAST && enumRailDirection2 != BlockRailBase.EnumRailDirection.ASCENDING_WEST ? (iBlockState.getValue(POWERED).booleanValue() ? (world.isBlockPowered(blockPos) ? true : this.func_176566_a(world, blockPos, iBlockState, bl, n + 1)) : false) : false) : false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(n & 7)).withProperty(POWERED, (n & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(SHAPE).getMetadata();
        if (iBlockState.getValue(POWERED).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, false));
    }

    @Override
    protected void onNeighborChangedInternal(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        boolean bl;
        boolean bl2 = iBlockState.getValue(POWERED);
        boolean bl3 = bl = world.isBlockPowered(blockPos) || this.func_176566_a(world, blockPos, iBlockState, true, 0) || this.func_176566_a(world, blockPos, iBlockState, false, 0);
        if (bl != bl2) {
            world.setBlockState(blockPos, iBlockState.withProperty(POWERED, bl), 3);
            world.notifyNeighborsOfStateChange(blockPos.down(), this);
            if (iBlockState.getValue(SHAPE).isAscending()) {
                world.notifyNeighborsOfStateChange(blockPos.up(), this);
            }
        }
    }

    @Override
    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    protected boolean func_176566_a(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl, int n) {
        if (n >= 8) {
            return false;
        }
        int n2 = blockPos.getX();
        int n3 = blockPos.getY();
        int n4 = blockPos.getZ();
        boolean bl2 = true;
        BlockRailBase.EnumRailDirection enumRailDirection = iBlockState.getValue(SHAPE);
        switch (enumRailDirection) {
            case NORTH_SOUTH: {
                if (bl) {
                    ++n4;
                    break;
                }
                --n4;
                break;
            }
            case EAST_WEST: {
                if (bl) {
                    --n2;
                    break;
                }
                ++n2;
                break;
            }
            case ASCENDING_EAST: {
                if (bl) {
                    --n2;
                } else {
                    ++n2;
                    ++n3;
                    bl2 = false;
                }
                enumRailDirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_WEST: {
                if (bl) {
                    --n2;
                    ++n3;
                    bl2 = false;
                } else {
                    ++n2;
                }
                enumRailDirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_NORTH: {
                if (bl) {
                    ++n4;
                } else {
                    --n4;
                    ++n3;
                    bl2 = false;
                }
                enumRailDirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                break;
            }
            case ASCENDING_SOUTH: {
                if (bl) {
                    ++n4;
                    ++n3;
                    bl2 = false;
                } else {
                    --n4;
                }
                enumRailDirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }
        }
        return this.func_176567_a(world, new BlockPos(n2, n3, n4), bl, n, enumRailDirection) ? true : bl2 && this.func_176567_a(world, new BlockPos(n2, n3 - 1, n4), bl, n, enumRailDirection);
    }
}

