/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor
extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<DoorHalf> HALF = PropertyEnum.create("half", DoorHalf.class);
    protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
    protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
    protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);
    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0, 0.8125, 0.0, 1.0, 1.0, 1.0);

    protected BlockTrapDoor(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, false).withProperty(HALF, DoorHalf.BOTTOM));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB axisalignedbb;
        if (state.getValue(OPEN).booleanValue()) {
            switch (state.getValue(FACING)) {
                default: {
                    axisalignedbb = NORTH_OPEN_AABB;
                    break;
                }
                case SOUTH: {
                    axisalignedbb = SOUTH_OPEN_AABB;
                    break;
                }
                case WEST: {
                    axisalignedbb = WEST_OPEN_AABB;
                    break;
                }
                case EAST: {
                    axisalignedbb = EAST_OPEN_AABB;
                    break;
                }
            }
        } else {
            axisalignedbb = state.getValue(HALF) == DoorHalf.TOP ? TOP_AABB : BOTTOM_AABB;
        }
        return axisalignedbb;
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
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(OPEN) == false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (this.blockMaterial == Material.IRON) {
            return false;
        }
        state = state.cycleProperty(OPEN);
        worldIn.setBlockState(pos, state, 2);
        this.playSound(playerIn, worldIn, pos, state.getValue(OPEN));
        return true;
    }

    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_) {
        if (p_185731_4_) {
            int i = this.blockMaterial == Material.IRON ? 1037 : 1007;
            worldIn.playEvent(player, i, pos, 0);
        } else {
            int j = this.blockMaterial == Material.IRON ? 1036 : 1013;
            worldIn.playEvent(player, j, pos, 0);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        boolean flag1;
        boolean flag;
        if (!worldIn.isRemote && ((flag = worldIn.isBlockPowered(pos)) || blockIn.getDefaultState().canProvidePower()) && (flag1 = state.getValue(OPEN).booleanValue()) != flag) {
            worldIn.setBlockState(pos, state.withProperty(OPEN, flag), 2);
            this.playSound(null, worldIn, pos, flag);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState();
        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN, false);
            iblockstate = iblockstate.withProperty(HALF, hitY > 0.5f ? DoorHalf.TOP : DoorHalf.BOTTOM);
        } else {
            iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, false);
            iblockstate = iblockstate.withProperty(HALF, facing == EnumFacing.UP ? DoorHalf.BOTTOM : DoorHalf.TOP);
        }
        if (worldIn.isBlockPowered(pos)) {
            iblockstate = iblockstate.withProperty(OPEN, true);
        }
        return iblockstate;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    protected static EnumFacing getFacing(int meta) {
        switch (meta & 3) {
            case 0: {
                return EnumFacing.NORTH;
            }
            case 1: {
                return EnumFacing.SOUTH;
            }
            case 2: {
                return EnumFacing.WEST;
            }
        }
        return EnumFacing.EAST;
    }

    protected static int getMetaForFacing(EnumFacing facing) {
        switch (facing) {
            case NORTH: {
                return 0;
            }
            case SOUTH: {
                return 1;
            }
            case WEST: {
                return 2;
            }
        }
        return 3;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, BlockTrapDoor.getFacing(meta)).withProperty(OPEN, (meta & 4) != 0).withProperty(HALF, (meta & 8) == 0 ? DoorHalf.BOTTOM : DoorHalf.TOP);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= BlockTrapDoor.getMetaForFacing(state.getValue(FACING));
        if (state.getValue(OPEN).booleanValue()) {
            i |= 4;
        }
        if (state.getValue(HALF) == DoorHalf.TOP) {
            i |= 8;
        }
        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, FACING, OPEN, HALF);
    }

    @Override
    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return (p_193383_4_ == EnumFacing.UP && p_193383_2_.getValue(HALF) == DoorHalf.TOP || p_193383_4_ == EnumFacing.DOWN && p_193383_2_.getValue(HALF) == DoorHalf.BOTTOM) && p_193383_2_.getValue(OPEN) == false ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum DoorHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private DoorHalf(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}

