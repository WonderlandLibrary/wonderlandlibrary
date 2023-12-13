/*
 * Decompiled with CFR 0.152.
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

        @Override
        public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_) {
            return p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, false));
    }

    protected boolean func_176566_a(World worldIn, BlockPos pos, IBlockState state, boolean p_176566_4_, int p_176566_5_) {
        if (p_176566_5_ >= 8) {
            return false;
        }
        int i2 = pos.getX();
        int j2 = pos.getY();
        int k2 = pos.getZ();
        boolean flag = true;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(SHAPE);
        switch (blockrailbase$enumraildirection) {
            case NORTH_SOUTH: {
                if (p_176566_4_) {
                    ++k2;
                    break;
                }
                --k2;
                break;
            }
            case EAST_WEST: {
                if (p_176566_4_) {
                    --i2;
                    break;
                }
                ++i2;
                break;
            }
            case ASCENDING_EAST: {
                if (p_176566_4_) {
                    --i2;
                } else {
                    ++i2;
                    ++j2;
                    flag = false;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_WEST: {
                if (p_176566_4_) {
                    --i2;
                    ++j2;
                    flag = false;
                } else {
                    ++i2;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;
            }
            case ASCENDING_NORTH: {
                if (p_176566_4_) {
                    ++k2;
                } else {
                    --k2;
                    ++j2;
                    flag = false;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                break;
            }
            case ASCENDING_SOUTH: {
                if (p_176566_4_) {
                    ++k2;
                    ++j2;
                    flag = false;
                } else {
                    --k2;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }
        }
        return this.func_176567_a(worldIn, new BlockPos(i2, j2, k2), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection) ? true : flag && this.func_176567_a(worldIn, new BlockPos(i2, j2 - 1, k2), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection);
    }

    protected boolean func_176567_a(World worldIn, BlockPos p_176567_2_, boolean p_176567_3_, int distance, BlockRailBase.EnumRailDirection p_176567_5_) {
        IBlockState iblockstate = worldIn.getBlockState(p_176567_2_);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(SHAPE);
        return p_176567_5_ != BlockRailBase.EnumRailDirection.EAST_WEST || blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_SOUTH && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_NORTH && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH ? (p_176567_5_ != BlockRailBase.EnumRailDirection.NORTH_SOUTH || blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.EAST_WEST && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_EAST && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_WEST ? (iblockstate.getValue(POWERED).booleanValue() ? (worldIn.isBlockPowered(p_176567_2_) ? true : this.func_176566_a(worldIn, p_176567_2_, iblockstate, p_176567_3_, distance + 1)) : false) : false) : false;
    }

    @Override
    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean flag1;
        boolean flag = state.getValue(POWERED);
        boolean bl = flag1 = worldIn.isBlockPowered(pos) || this.func_176566_a(worldIn, pos, state, true, 0) || this.func_176566_a(worldIn, pos, state, false, 0);
        if (flag1 != flag) {
            worldIn.setBlockState(pos, state.withProperty(POWERED, flag1), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
            if (state.getValue(SHAPE).isAscending()) {
                worldIn.notifyNeighborsOfStateChange(pos.up(), this);
            }
        }
    }

    @Override
    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7)).withProperty(POWERED, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i2 = 0;
        i2 |= state.getValue(SHAPE).getMetadata();
        if (state.getValue(POWERED).booleanValue()) {
            i2 |= 8;
        }
        return i2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SHAPE, POWERED);
    }
}

