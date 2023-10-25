/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered
extends BlockRailBase {
    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>(){

        @Override
        public boolean apply(@Nullable BlockRailBase.EnumRailDirection p_apply_1_) {
            return p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, false));
    }

    protected boolean findPoweredRailSignal(World worldIn, BlockPos pos, IBlockState state, boolean p_176566_4_, int p_176566_5_) {
        if (p_176566_5_ >= 8) {
            return false;
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(SHAPE);
        switch (blockrailbase$enumraildirection) {
            case NORTH_SOUTH: {
                if (p_176566_4_) {
                    ++k;
                    break;
                }
                --k;
                break;
            }
            case EAST_WEST: {
                if (p_176566_4_) {
                    --i;
                    break;
                }
                ++i;
                break;
            }
            case ASCENDING_EAST: {
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
            case ASCENDING_WEST: {
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
            case ASCENDING_NORTH: {
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
            case ASCENDING_SOUTH: {
                if (p_176566_4_) {
                    ++k;
                    ++j;
                    flag = false;
                } else {
                    --k;
                }
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }
        }
        if (this.isSameRailWithPower(worldIn, new BlockPos(i, j, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection)) {
            return true;
        }
        return flag && this.isSameRailWithPower(worldIn, new BlockPos(i, j - 1, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection);
    }

    protected boolean isSameRailWithPower(World worldIn, BlockPos pos, boolean p_176567_3_, int distance, BlockRailBase.EnumRailDirection p_176567_5_) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) {
            return false;
        }
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(SHAPE);
        if (p_176567_5_ != BlockRailBase.EnumRailDirection.EAST_WEST || blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_SOUTH && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_NORTH && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) {
            if (p_176567_5_ != BlockRailBase.EnumRailDirection.NORTH_SOUTH || blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.EAST_WEST && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_EAST && blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.ASCENDING_WEST) {
                if (iblockstate.getValue(POWERED).booleanValue()) {
                    return worldIn.isBlockPowered(pos) ? true : this.findPoweredRailSignal(worldIn, pos, iblockstate, p_176567_3_, distance + 1);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    protected void updateState(IBlockState p_189541_1_, World p_189541_2_, BlockPos p_189541_3_, Block p_189541_4_) {
        boolean flag1;
        boolean flag = p_189541_1_.getValue(POWERED);
        boolean bl = flag1 = p_189541_2_.isBlockPowered(p_189541_3_) || this.findPoweredRailSignal(p_189541_2_, p_189541_3_, p_189541_1_, true, 0) || this.findPoweredRailSignal(p_189541_2_, p_189541_3_, p_189541_1_, false, 0);
        if (flag1 != flag) {
            p_189541_2_.setBlockState(p_189541_3_, p_189541_1_.withProperty(POWERED, flag1), 3);
            p_189541_2_.notifyNeighborsOfStateChange(p_189541_3_.down(), this, false);
            if (p_189541_1_.getValue(SHAPE).isAscending()) {
                p_189541_2_.notifyNeighborsOfStateChange(p_189541_3_.up(), this, false);
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
        int i = 0;
        i |= state.getValue(SHAPE).getMetadata();
        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                switch (state.getValue(SHAPE)) {
                    case ASCENDING_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    }
                    case ASCENDING_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    }
                    case ASCENDING_NORTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    }
                    case ASCENDING_SOUTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    }
                    case NORTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    }
                }
            }
            case COUNTERCLOCKWISE_90: {
                switch (state.getValue(SHAPE)) {
                    case NORTH_SOUTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
                    }
                    case EAST_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                    }
                    case ASCENDING_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    }
                    case ASCENDING_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    }
                    case ASCENDING_NORTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    }
                    case ASCENDING_SOUTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    }
                    case NORTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    }
                }
            }
            case CLOCKWISE_90: {
                switch (state.getValue(SHAPE)) {
                    case NORTH_SOUTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);
                    }
                    case EAST_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                    }
                    case ASCENDING_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    }
                    case ASCENDING_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    }
                    case ASCENDING_NORTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    }
                    case ASCENDING_SOUTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    }
                    case NORTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    }
                }
            }
        }
        return state;
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(SHAPE);
        block0 : switch (mirrorIn) {
            case LEFT_RIGHT: {
                switch (blockrailbase$enumraildirection) {
                    case ASCENDING_NORTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);
                    }
                    case ASCENDING_SOUTH: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                    }
                    case NORTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    }
                    case NORTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    }
                }
                return super.withMirror(state, mirrorIn);
            }
            case FRONT_BACK: {
                switch (blockrailbase$enumraildirection) {
                    case ASCENDING_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);
                    }
                    case ASCENDING_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);
                    }
                    default: {
                        break block0;
                    }
                    case SOUTH_EAST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                    }
                    case SOUTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
                    }
                    case NORTH_WEST: {
                        return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);
                    }
                    case NORTH_EAST: 
                }
                return state.withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }
        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, SHAPE, POWERED);
    }
}

