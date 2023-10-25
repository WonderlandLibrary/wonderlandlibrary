/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

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
    public static final PropertyBool POWERED;
    public static final PropertyEnum<EnumOrientation> FACING;

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing enumFacing = enumFacingArray[n2];
            if (BlockLever.func_181090_a(world, blockPos, enumFacing)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (world.isRemote) {
            return true;
        }
        iBlockState = iBlockState.cycleProperty(POWERED);
        world.setBlockState(blockPos, iBlockState, 3);
        world.playSoundEffect((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, "random.click", 0.3f, iBlockState.getValue(POWERED) != false ? 0.6f : 0.5f);
        world.notifyNeighborsOfStateChange(blockPos, this);
        EnumFacing enumFacing2 = iBlockState.getValue(FACING).getFacing();
        world.notifyNeighborsOfStateChange(blockPos.offset(enumFacing2.getOpposite()), this);
        return true;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, POWERED);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    protected static boolean func_181090_a(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return BlockButton.func_181088_a(world, blockPos, enumFacing);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return BlockLever.func_181090_a(world, blockPos, enumFacing.getOpposite());
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (this.func_181091_e(world, blockPos, iBlockState) && !BlockLever.func_181090_a(world, blockPos, iBlockState.getValue(FACING).getFacing().getOpposite())) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumOrientation.byMetadata(n & 7)).withProperty(POWERED, (n & 8) > 0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        float f = 0.1875f;
        switch (iBlockAccess.getBlockState(blockPos).getValue(FACING)) {
            case EAST: {
                this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
                break;
            }
            case WEST: {
                this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
                break;
            }
            case SOUTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
                break;
            }
            case NORTH: {
                this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
                break;
            }
            case UP_Z: 
            case UP_X: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
                break;
            }
            case DOWN_X: 
            case DOWN_Z: {
                f = 0.25f;
                this.setBlockBounds(0.5f - f, 0.4f, 0.5f - f, 0.5f + f, 1.0f, 0.5f + f);
            }
        }
    }

    private boolean func_181091_e(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (this.canPlaceBlockAt(world, blockPos)) {
            return true;
        }
        this.dropBlockAsItem(world, blockPos, iBlockState, 0);
        world.setBlockToAir(blockPos);
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        IBlockState iBlockState = this.getDefaultState().withProperty(POWERED, false);
        if (BlockLever.func_181090_a(world, blockPos, enumFacing.getOpposite())) {
            return iBlockState.withProperty(FACING, EnumOrientation.forFacings(enumFacing, entityLivingBase.getHorizontalFacing()));
        }
        for (EnumFacing enumFacing2 : EnumFacing.Plane.HORIZONTAL) {
            if (enumFacing2 == enumFacing || !BlockLever.func_181090_a(world, blockPos, enumFacing2.getOpposite())) continue;
            return iBlockState.withProperty(FACING, EnumOrientation.forFacings(enumFacing2, entityLivingBase.getHorizontalFacing()));
        }
        if (World.doesBlockHaveSolidTopSurface(world, blockPos.down())) {
            return iBlockState.withProperty(FACING, EnumOrientation.forFacings(EnumFacing.UP, entityLivingBase.getHorizontalFacing()));
        }
        return iBlockState;
    }

    static {
        FACING = PropertyEnum.create("facing", EnumOrientation.class);
        POWERED = PropertyBool.create("powered");
    }

    protected BlockLever() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH).withProperty(POWERED, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getMetadata();
        if (iBlockState.getValue(POWERED).booleanValue()) {
            n |= 8;
        }
        return n;
    }

    @Override
    public int getStrongPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return iBlockState.getValue(POWERED) == false ? 0 : (iBlockState.getValue(FACING).getFacing() == enumFacing ? 15 : 0);
    }

    public static int getMetadataForFacing(EnumFacing enumFacing) {
        switch (enumFacing) {
            case DOWN: {
                return 0;
            }
            case UP: {
                return 5;
            }
            case NORTH: {
                return 4;
            }
            case SOUTH: {
                return 3;
            }
            case WEST: {
                return 2;
            }
            case EAST: {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (iBlockState.getValue(POWERED).booleanValue()) {
            world.notifyNeighborsOfStateChange(blockPos, this);
            EnumFacing enumFacing = iBlockState.getValue(FACING).getFacing();
            world.notifyNeighborsOfStateChange(blockPos.offset(enumFacing.getOpposite()), this);
        }
        super.breakBlock(world, blockPos, iBlockState);
    }

    @Override
    public int getWeakPower(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing) {
        return iBlockState.getValue(POWERED) != false ? 15 : 0;
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

        private final String name;
        private final int meta;
        private static final EnumOrientation[] META_LOOKUP;
        private final EnumFacing facing;

        public EnumFacing getFacing() {
            return this.facing;
        }

        public static EnumOrientation byMetadata(int n) {
            if (n < 0 || n >= META_LOOKUP.length) {
                n = 0;
            }
            return META_LOOKUP[n];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public static EnumOrientation forFacings(EnumFacing enumFacing, EnumFacing enumFacing2) {
            switch (enumFacing) {
                case DOWN: {
                    switch (enumFacing2.getAxis()) {
                        case X: {
                            return DOWN_X;
                        }
                        case Z: {
                            return DOWN_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + enumFacing2 + " for facing " + enumFacing);
                }
                case UP: {
                    switch (enumFacing2.getAxis()) {
                        case X: {
                            return UP_X;
                        }
                        case Z: {
                            return UP_Z;
                        }
                    }
                    throw new IllegalArgumentException("Invalid entityFacing " + enumFacing2 + " for facing " + enumFacing);
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
            throw new IllegalArgumentException("Invalid facing: " + enumFacing);
        }

        private EnumOrientation(int n2, String string2, EnumFacing enumFacing) {
            this.meta = n2;
            this.name = string2;
            this.facing = enumFacing;
        }

        static {
            META_LOOKUP = new EnumOrientation[EnumOrientation.values().length];
            EnumOrientation[] enumOrientationArray = EnumOrientation.values();
            int n = enumOrientationArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumOrientation enumOrientation;
                EnumOrientation.META_LOOKUP[enumOrientation.getMetadata()] = enumOrientation = enumOrientationArray[n2];
                ++n2;
            }
        }
    }
}

