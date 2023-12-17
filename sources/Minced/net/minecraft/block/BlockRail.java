// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRail extends BlockRailBase
{
    public static final PropertyEnum<EnumRailDirection> SHAPE;
    
    protected BlockRail() {
        super(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_SOUTH));
    }
    
    @Override
    protected void updateState(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn) {
        if (blockIn.getDefaultState().canProvidePower() && new Rail(worldIn, pos, state).countAdjacentRails() == 3) {
            this.updateDir(worldIn, pos, state, false);
        }
    }
    
    @Override
    public IProperty<EnumRailDirection> getShapeProperty() {
        return BlockRail.SHAPE;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRail.SHAPE, EnumRailDirection.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockRail.SHAPE).getMetadata();
    }
    
    @Override
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        Label_0406: {
            switch (rot) {
                case CLOCKWISE_180: {
                    switch (state.getValue(BlockRail.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (state.getValue(BlockRail.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_SOUTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_SOUTH);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (state.getValue(BlockRail.SHAPE)) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_SOUTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.EAST_WEST);
                        }
                        case EAST_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_SOUTH);
                        }
                        default: {
                            break Label_0406;
                        }
                    }
                    break;
                }
            }
        }
        return state;
    }
    
    @Override
    public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
        final EnumRailDirection blockrailbase$enumraildirection = state.getValue(BlockRail.SHAPE);
        Label_0317: {
            switch (mirrorIn) {
                case LEFT_RIGHT: {
                    switch (blockrailbase$enumraildirection) {
                        case ASCENDING_NORTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_SOUTH);
                        }
                        case ASCENDING_SOUTH: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_NORTH);
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        default: {
                            return super.withMirror(state, mirrorIn);
                        }
                    }
                    break;
                }
                case FRONT_BACK: {
                    switch (blockrailbase$enumraildirection) {
                        case ASCENDING_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_WEST);
                        }
                        case ASCENDING_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.ASCENDING_EAST);
                        }
                        default: {
                            break Label_0317;
                        }
                        case SOUTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_WEST);
                        }
                        case SOUTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.SOUTH_EAST);
                        }
                        case NORTH_WEST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_EAST);
                        }
                        case NORTH_EAST: {
                            return state.withProperty(BlockRail.SHAPE, EnumRailDirection.NORTH_WEST);
                        }
                    }
                    break;
                }
            }
        }
        return super.withMirror(state, mirrorIn);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockRail.SHAPE });
    }
    
    static {
        SHAPE = PropertyEnum.create("shape", EnumRailDirection.class);
    }
}
