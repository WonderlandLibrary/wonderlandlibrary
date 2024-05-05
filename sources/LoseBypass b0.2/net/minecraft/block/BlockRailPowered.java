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
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.NORTH_EAST) return false;
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.NORTH_WEST) return false;
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.SOUTH_EAST) return false;
            if (p_apply_1_ == BlockRailBase.EnumRailDirection.SOUTH_WEST) return false;
            return true;
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
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(SHAPE);
        switch (2.$SwitchMap$net$minecraft$block$BlockRailBase$EnumRailDirection[blockrailbase$enumraildirection.ordinal()]) {
            case 1: {
                if (p_176566_4_) {
                    ++k;
                    break;
                }
                --k;
                break;
            }
            case 2: {
                if (p_176566_4_) {
                    --i;
                    break;
                }
                ++i;
                break;
            }
            case 3: {
                if (p_176566_4_) {
                    --i;
                } else {
                    ++i;
                    ++j;
                    flag = false;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;
            }
            case 4: {
                if (p_176566_4_) {
                    --i;
                    ++j;
                    flag = false;
                } else {
                    ++i;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;
            }
            case 5: {
                if (p_176566_4_) {
                    ++k;
                } else {
                    --k;
                    ++j;
                    flag = false;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                break;
            }
            case 6: {
                if (p_176566_4_) {
                    ++k;
                    ++j;
                    flag = false;
                } else {
                    --k;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                break;
            }
        }
        if (this.func_176567_a(worldIn, new BlockPos(i, j, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection)) {
            return true;
        }
        if (!flag) return false;
        if (!this.func_176567_a(worldIn, new BlockPos(i, j - 1, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection)) return false;
        return true;
    }

    protected boolean func_176567_a(World worldIn, BlockPos p_176567_2_, boolean p_176567_3_, int distance, BlockRailBase.EnumRailDirection p_176567_5_) {
        IBlockState iblockstate = worldIn.getBlockState(p_176567_2_);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(SHAPE);
        if (p_176567_5_ == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) return false;
            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_NORTH) return false;
            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) return false;
        }
        if (p_176567_5_ == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) return false;
            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_EAST) return false;
            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_WEST) return false;
        }
        if (!iblockstate.getValue(POWERED).booleanValue()) {
            return false;
        }
        if (worldIn.isBlockPowered(p_176567_2_)) {
            return true;
        }
        boolean bl = this.func_176566_a(worldIn, p_176567_2_, iblockstate, p_176567_3_, distance + 1);
        return bl;
    }

    @Override
    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        boolean flag = state.getValue(POWERED);
        boolean flag1 = worldIn.isBlockPowered(pos) || this.func_176566_a(worldIn, pos, state, true, 0) || this.func_176566_a(worldIn, pos, state, false, 0);
        if (flag1 == flag) return;
        worldIn.setBlockState(pos, state.withProperty(POWERED, flag1), 3);
        worldIn.notifyNeighborsOfStateChange(pos.down(), this);
        if (!state.getValue(SHAPE).isAscending()) return;
        worldIn.notifyNeighborsOfStateChange(pos.up(), this);
    }

    @Override
    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7));
        if ((meta & 8) > 0) {
            bl = true;
            return iBlockState.withProperty(POWERED, bl);
        }
        bl = false;
        return iBlockState.withProperty(POWERED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(POWERED) == false) return i |= state.getValue(SHAPE).getMetadata();
        i |= 8;
        return i |= state.getValue(SHAPE).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, SHAPE, POWERED);
    }
}

