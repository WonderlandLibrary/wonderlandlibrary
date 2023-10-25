/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever
extends Block {
    public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected static final AxisAlignedBB LEVER_NORTH_AABB = new AxisAlignedBB(0.3125, 0.2f, 0.625, 0.6875, 0.8f, 1.0);
    protected static final AxisAlignedBB LEVER_SOUTH_AABB = new AxisAlignedBB(0.3125, 0.2f, 0.0, 0.6875, 0.8f, 0.375);
    protected static final AxisAlignedBB LEVER_WEST_AABB = new AxisAlignedBB(0.625, 0.2f, 0.3125, 1.0, 0.8f, 0.6875);
    protected static final AxisAlignedBB LEVER_EAST_AABB = new AxisAlignedBB(0.0, 0.2f, 0.3125, 0.375, 0.8f, 0.6875);
    protected static final AxisAlignedBB LEVER_UP_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.6f, 0.75);
    protected static final AxisAlignedBB LEVER_DOWN_AABB = new AxisAlignedBB(0.25, 0.4f, 0.25, 0.75, 1.0, 0.75);

    protected BlockLever() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH).withProperty(POWERED, false));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return BlockLever.canAttachTo(worldIn, pos, side);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (!BlockLever.canAttachTo(worldIn, pos, enumfacing)) continue;
            return true;
        }
        return false;
    }

    protected static boolean canAttachTo(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
        return BlockButton.canPlaceBlock(p_181090_0_, p_181090_1_, p_181090_2_);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, false);
        if (BlockLever.canAttachTo(worldIn, pos, facing)) {
            return iblockstate.withProperty(FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        }
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing == facing || !BlockLever.canAttachTo(worldIn, pos, enumfacing)) continue;
            return iblockstate.withProperty(FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
        }
        if (worldIn.getBlockState(pos.down()).isFullyOpaque()) {
            return iblockstate.withProperty(FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
        }
        return iblockstate;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        if (this.checkCanSurvive(worldIn, pos, state) && !BlockLever.canAttachTo(worldIn, pos, state.getValue(FACING).getFacing())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean checkCanSurvive(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
        if (this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
            return true;
        }
        this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
        p_181091_1_.setBlockToAir(p_181091_2_);
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            default: {
                return LEVER_EAST_AABB;
            }
            case WEST: {
                return LEVER_WEST_AABB;
            }
            case SOUTH: {
                return LEVER_SOUTH_AABB;
            }
            case NORTH: {
                return LEVER_NORTH_AABB;
            }
            case UP_Z: 
            case UP_X: {
                return LEVER_UP_AABB;
            }
            case DOWN_X: 
            case DOWN_Z: 
        }
        return LEVER_DOWN_AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty(POWERED);
        worldIn.setBlockState(pos, state, 3);
        float f = state.getValue(POWERED) != false ? 0.6f : 0.5f;
        worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, f);
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        EnumFacing enumfacing = state.getValue(FACING).getFacing();
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(POWERED).booleanValue()) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            EnumFacing enumfacing = state.getValue(FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWERED) != false ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!blockState.getValue(POWERED).booleanValue()) {
            return 0;
        }
        return blockState.getValue(FACING).getFacing() == side ? 15 : 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumOrientation.byMetadata(meta & 7)).withProperty(POWERED, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getMetadata();
        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }
        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180: {
                switch (state.getValue(FACING)) {
                    case EAST: {
                        return state.withProperty(FACING, EnumOrientation.WEST);
                    }
                    case WEST: {
                        return state.withProperty(FACING, EnumOrientation.EAST);
                    }
                    case SOUTH: {
                        return state.withProperty(FACING, EnumOrientation.NORTH);
                    }
                    case NORTH: {
                        return state.withProperty(FACING, EnumOrientation.SOUTH);
                    }
                }
                return state;
            }
            case COUNTERCLOCKWISE_90: {
                switch (state.getValue(FACING)) {
                    case EAST: {
                        return state.withProperty(FACING, EnumOrientation.NORTH);
                    }
                    case WEST: {
                        return state.withProperty(FACING, EnumOrientation.SOUTH);
                    }
                    case SOUTH: {
                        return state.withProperty(FACING, EnumOrientation.EAST);
                    }
                    case NORTH: {
                        return state.withProperty(FACING, EnumOrientation.WEST);
                    }
                    case UP_Z: {
                        return state.withProperty(FACING, EnumOrientation.UP_X);
                    }
                    case UP_X: {
                        return state.withProperty(FACING, EnumOrientation.UP_Z);
                    }
                    case DOWN_X: {
                        return state.withProperty(FACING, EnumOrientation.DOWN_Z);
                    }
                    case DOWN_Z: {
                        return state.withProperty(FACING, EnumOrientation.DOWN_X);
                    }
                }
            }
            case CLOCKWISE_90: {
                switch (state.getValue(FACING)) {
                    case EAST: {
                        return state.withProperty(FACING, EnumOrientation.SOUTH);
                    }
                    case WEST: {
                        return state.withProperty(FACING, EnumOrientation.NORTH);
                    }
                    case SOUTH: {
                        return state.withProperty(FACING, EnumOrientation.WEST);
                    }
                    case NORTH: {
                        return state.withProperty(FACING, EnumOrientation.EAST);
                    }
                    case UP_Z: {
                        return state.withProperty(FACING, EnumOrientation.UP_X);
                    }
                    case UP_X: {
                        return state.withProperty(FACING, EnumOrientation.UP_Z);
                    }
                    case DOWN_X: {
                        return state.withProperty(FACING, EnumOrientation.DOWN_Z);
                    }
                    case DOWN_Z: {
                        return state.withProperty(FACING, EnumOrientation.DOWN_X);
                    }
                }
            }
        }
        return state;
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING).getFacing()));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, POWERED);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumOrientation implements IStringSerializable
    {
        DOWN_X(0, "down_x", EnumFacing.DOWN),
        EAST(1, "east", EnumFacing.EAST),
        WEST(2, "west", EnumFacing.WEST),
        SOUTH(3, "south", EnumFacing.SOUTH),
        NORTH(4, "north", EnumFacing.NORTH),
        UP_Z(5, "up_z", EnumFacing.UP),
        UP_X(6, "up_x", EnumFacing.UP),
        DOWN_Z(7, "down_z", EnumFacing.DOWN);

        private static final EnumOrientation[] META_LOOKUP;
        private final int meta;
        private final String name;
        private final EnumFacing facing;

        private EnumOrientation(int meta, String name, EnumFacing facing) {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public int getMetadata() {
            return this.meta;
        }

        public EnumFacing getFacing() {
            return this.facing;
        }

        public String toString() {
            return this.name;
        }

        public static EnumOrientation byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }
            return META_LOOKUP[meta];
        }

        public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
            switch (clickedSide) {
                case DOWN: {
                    switch (entityFacing.getAxis()) {
                        case X: {
                            return DOWN_X;
                        }
                        case Z: {
                            return DOWN_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                }
                case UP: {
                    switch (entityFacing.getAxis()) {
                        case X: {
                            return UP_X;
                        }
                        case Z: {
                            return UP_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                }
                case NORTH: {
                    return NORTH;
                }
                case SOUTH: {
                    return SOUTH;
                }
                case WEST: {
                    return WEST;
                }
                case EAST: {
                    return EAST;
                }
            }
            throw new IllegalArgumentException("Invalid facing: " + clickedSide);
        }

        @Override
        public String getName() {
            return this.name;
        }

        static {
            META_LOOKUP = new EnumOrientation[EnumOrientation.values().length];
            EnumOrientation[] arrenumOrientation = EnumOrientation.values();
            int n = arrenumOrientation.length;
            for (int i = 0; i < n; ++i) {
                EnumOrientation blocklever$enumorientation;
                EnumOrientation.META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation = arrenumOrientation[i];
            }
        }
    }
}

