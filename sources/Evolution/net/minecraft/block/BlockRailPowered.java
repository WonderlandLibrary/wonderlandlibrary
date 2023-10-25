package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.<EnumRailDirection>create("shape", EnumRailDirection.class, new Predicate<EnumRailDirection>()
    {
        public boolean apply(@Nullable BlockRailBase.EnumRailDirection p_apply_1_)
        {
            return p_apply_1_ != EnumRailDirection.NORTH_EAST && p_apply_1_ != EnumRailDirection.NORTH_WEST && p_apply_1_ != EnumRailDirection.SOUTH_EAST && p_apply_1_ != EnumRailDirection.SOUTH_WEST;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockRailPowered()
    {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, Boolean.valueOf(false)));
    }

    @SuppressWarnings("incomplete-switch")
    protected boolean findPoweredRailSignal(World worldIn, BlockPos pos, IBlockState state, boolean p_176566_4_, int p_176566_5_)
    {
        if (p_176566_5_ >= 8)
        {
            return false;
        }
        else
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            boolean flag = true;
            EnumRailDirection blockrailbase$enumraildirection = (EnumRailDirection)state.getValue(SHAPE);

            switch (blockrailbase$enumraildirection)
            {
                case NORTH_SOUTH:
                    if (p_176566_4_)
                    {
                        ++k;
                    }
                    else
                    {
                        --k;
                    }

                    break;

                case EAST_WEST:
                    if (p_176566_4_)
                    {
                        --i;
                    }
                    else
                    {
                        ++i;
                    }

                    break;

                case ASCENDING_EAST:
                    if (p_176566_4_)
                    {
                        --i;
                    }
                    else
                    {
                        ++i;
                        ++j;
                        flag = false;
                    }

                    blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                    break;

                case ASCENDING_WEST:
                    if (p_176566_4_)
                    {
                        --i;
                        ++j;
                        flag = false;
                    }
                    else
                    {
                        ++i;
                    }

                    blockrailbase$enumraildirection = EnumRailDirection.EAST_WEST;
                    break;

                case ASCENDING_NORTH:
                    if (p_176566_4_)
                    {
                        ++k;
                    }
                    else
                    {
                        --k;
                        ++j;
                        flag = false;
                    }

                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
                    break;

                case ASCENDING_SOUTH:
                    if (p_176566_4_)
                    {
                        ++k;
                        ++j;
                        flag = false;
                    }
                    else
                    {
                        --k;
                    }

                    blockrailbase$enumraildirection = EnumRailDirection.NORTH_SOUTH;
            }

            if (this.isSameRailWithPower(worldIn, new BlockPos(i, j, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection))
            {
                return true;
            }
            else
            {
                return flag && this.isSameRailWithPower(worldIn, new BlockPos(i, j - 1, k), p_176566_4_, p_176566_5_, blockrailbase$enumraildirection);
            }
        }
    }

    protected boolean isSameRailWithPower(World worldIn, BlockPos pos, boolean p_176567_3_, int distance, EnumRailDirection p_176567_5_)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() != this)
        {
            return false;
        }
        else
        {
            EnumRailDirection blockrailbase$enumraildirection = (EnumRailDirection)iblockstate.getValue(SHAPE);

            if (p_176567_5_ != EnumRailDirection.EAST_WEST || blockrailbase$enumraildirection != EnumRailDirection.NORTH_SOUTH && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_NORTH && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_SOUTH)
            {
                if (p_176567_5_ != EnumRailDirection.NORTH_SOUTH || blockrailbase$enumraildirection != EnumRailDirection.EAST_WEST && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_EAST && blockrailbase$enumraildirection != EnumRailDirection.ASCENDING_WEST)
                {
                    if (((Boolean)iblockstate.getValue(POWERED)).booleanValue())
                    {
                        return worldIn.isBlockPowered(pos) ? true : this.findPoweredRailSignal(worldIn, pos, iblockstate, p_176567_3_, distance + 1);
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }

    protected void updateState(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        boolean flag = ((Boolean)state.getValue(POWERED)).booleanValue();
        boolean flag1 = worldIn.isBlockPowered(pos) || this.findPoweredRailSignal(worldIn, pos, state, true, 0) || this.findPoweredRailSignal(worldIn, pos, state, false, 0);

        if (flag1 != flag)
        {
            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(flag1)), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);

            if (((EnumRailDirection)state.getValue(SHAPE)).isAscending())
            {
                worldIn.notifyNeighborsOfStateChange(pos.up(), this, false);
            }
        }
    }

    public IProperty<EnumRailDirection> getShapeProperty()
    {
        return SHAPE;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, EnumRailDirection.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumRailDirection)state.getValue(SHAPE)).getMetadata();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:
                switch ((EnumRailDirection)state.getValue(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_WEST);

                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_EAST);

                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_NORTH);

                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_WEST);

                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_EAST);

                    case NORTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_EAST);

                    case NORTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_WEST);
                }

            case COUNTERCLOCKWISE_90:
                switch ((EnumRailDirection)state.getValue(SHAPE))
                {
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, EnumRailDirection.EAST_WEST);

                    case EAST_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH);

                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_NORTH);

                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_WEST);

                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_EAST);

                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_EAST);

                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_EAST);

                    case NORTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_WEST);

                    case NORTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_WEST);
                }

            case CLOCKWISE_90:
                switch ((EnumRailDirection)state.getValue(SHAPE))
                {
                    case NORTH_SOUTH:
                        return state.withProperty(SHAPE, EnumRailDirection.EAST_WEST);

                    case EAST_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_SOUTH);

                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_NORTH);

                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_EAST);

                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_WEST);

                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_WEST);

                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_WEST);

                    case NORTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_EAST);

                    case NORTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_EAST);
                }

            default:
                return state;
        }
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        EnumRailDirection blockrailbase$enumraildirection = (EnumRailDirection)state.getValue(SHAPE);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_NORTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_SOUTH:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_NORTH);

                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_EAST);

                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_WEST);

                    case NORTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_WEST);

                    case NORTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_EAST);

                    default:
                        return super.withMirror(state, mirrorIn);
                }

            case FRONT_BACK:
                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_WEST);

                    case ASCENDING_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.ASCENDING_EAST);

                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;

                    case SOUTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_WEST);

                    case SOUTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.SOUTH_EAST);

                    case NORTH_WEST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_EAST);

                    case NORTH_EAST:
                        return state.withProperty(SHAPE, EnumRailDirection.NORTH_WEST);
                }
        }

        return super.withMirror(state, mirrorIn);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {SHAPE, POWERED});
    }
}
