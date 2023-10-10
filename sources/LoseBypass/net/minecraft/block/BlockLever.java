/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever
extends Block {
    public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockLever() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH).withProperty(POWERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return BlockLever.func_181090_a(worldIn, pos, side.getOpposite());
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumfacing = enumFacingArray[n2];
            if (BlockLever.func_181090_a(worldIn, pos, enumfacing)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    protected static boolean func_181090_a(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
        return BlockButton.func_181088_a(p_181090_0_, p_181090_1_, p_181090_2_);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing;
        IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, false);
        if (BlockLever.func_181090_a(worldIn, pos, facing.getOpposite())) {
            return iblockstate.withProperty(FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
        }
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (iterator.hasNext()) continue;
            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) return iblockstate;
            return iblockstate.withProperty(FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
        } while ((enumfacing = iterator.next()) == facing || !BlockLever.func_181090_a(worldIn, pos, enumfacing.getOpposite()));
        return iblockstate.withProperty(FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
    }

    public static int getMetadataForFacing(EnumFacing facing) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                return 0;
            }
            case 2: {
                return 5;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 3;
            }
            case 5: {
                return 2;
            }
            case 6: {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!this.func_181091_e(worldIn, pos, state)) return;
        if (BlockLever.func_181090_a(worldIn, pos, state.getValue(FACING).getFacing().getOpposite())) return;
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

    private boolean func_181091_e(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
        if (this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
            return true;
        }
        this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
        p_181091_1_.setBlockToAir(p_181091_2_);
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        float f = 0.1875f;
        switch (worldIn.getBlockState(pos).getValue(FACING)) {
            case EAST: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                return;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                return;
            }
            case SOUTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                return;
            }
            case NORTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
                return;
            }
            case UP_Z: 
            case UP_X: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
                return;
            }
            case DOWN_X: 
            case DOWN_Z: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.4f, 0.5f - f, 0.5f + f, 1.0f, 0.5f + f);
                return;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        state = state.cycleProperty(POWERED);
        worldIn.setBlockState(pos, state, 3);
        worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3f, state.getValue(POWERED) != false ? 0.6f : 0.5f);
        worldIn.notifyNeighborsOfStateChange(pos, this);
        EnumFacing enumfacing = state.getValue(FACING).getFacing();
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(POWERED).booleanValue()) {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing enumfacing = state.getValue(FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (state.getValue(POWERED) == false) return 0;
        return 15;
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        if (!state.getValue(POWERED).booleanValue()) {
            return 0;
        }
        if (state.getValue(FACING).getFacing() != side) return 0;
        return 15;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState().withProperty(FACING, EnumOrientation.byMetadata(meta & 7));
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
        if (state.getValue(POWERED) == false) return i |= state.getValue(FACING).getMetadata();
        i |= 8;
        return i |= state.getValue(FACING).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, POWERED);
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
            if (meta >= 0) {
                if (meta < META_LOOKUP.length) return META_LOOKUP[meta];
            }
            meta = 0;
            return META_LOOKUP[meta];
        }

        public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
            switch (1.$SwitchMap$net$minecraft$util$EnumFacing[clickedSide.ordinal()]) {
                case 1: {
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
                case 2: {
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
                case 3: {
                    return NORTH;
                }
                case 4: {
                    return SOUTH;
                }
                case 5: {
                    return WEST;
                }
                case 6: {
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
            EnumOrientation[] enumOrientationArray = EnumOrientation.values();
            int n = enumOrientationArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumOrientation blocklever$enumorientation;
                EnumOrientation.META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation = enumOrientationArray[n2];
                ++n2;
            }
        }
    }
}

