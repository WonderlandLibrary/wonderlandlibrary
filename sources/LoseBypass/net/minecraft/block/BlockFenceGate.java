/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate
extends BlockDirectional {
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");

    public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
        super(Material.wood, p_i46394_1_.func_181070_c());
        this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, false).withProperty(POWERED, false).withProperty(IN_WALL, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = state.getValue(FACING).getAxis();
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            if (worldIn.getBlockState(pos.west()).getBlock() == Blocks.cobblestone_wall) return state.withProperty(IN_WALL, true);
            if (worldIn.getBlockState(pos.east()).getBlock() == Blocks.cobblestone_wall) return state.withProperty(IN_WALL, true);
        }
        if (enumfacing$axis != EnumFacing.Axis.X) return state;
        if (worldIn.getBlockState(pos.north()).getBlock() == Blocks.cobblestone_wall) return state.withProperty(IN_WALL, true);
        if (worldIn.getBlockState(pos.south()).getBlock() != Blocks.cobblestone_wall) return state;
        return state.withProperty(IN_WALL, true);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if (!worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid()) return false;
        boolean bl = super.canPlaceBlockAt(worldIn, pos);
        return bl;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        AxisAlignedBB axisAlignedBB;
        if (state.getValue(OPEN).booleanValue()) {
            return null;
        }
        EnumFacing.Axis enumfacing$axis = state.getValue(FACING).getAxis();
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            axisAlignedBB = new AxisAlignedBB(pos.getX(), pos.getY(), (float)pos.getZ() + 0.375f, pos.getX() + 1, (float)pos.getY() + 1.5f, (float)pos.getZ() + 0.625f);
            return axisAlignedBB;
        }
        axisAlignedBB = new AxisAlignedBB((float)pos.getX() + 0.375f, pos.getY(), pos.getZ(), (float)pos.getX() + 0.625f, (float)pos.getY() + 1.5f, pos.getZ() + 1);
        return axisAlignedBB;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = worldIn.getBlockState(pos).getValue(FACING).getAxis();
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            this.setBlockBounds(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
            return;
        }
        this.setBlockBounds(0.375f, 0.0f, 0.0f, 0.625f, 1.0f, 1.0f);
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
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getValue(OPEN);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(OPEN, false).withProperty(POWERED, false).withProperty(IN_WALL, false);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(OPEN).booleanValue()) {
            state = state.withProperty(OPEN, false);
            worldIn.setBlockState(pos, state, 2);
        } else {
            EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
            if (state.getValue(FACING) == enumfacing.getOpposite()) {
                state = state.withProperty(FACING, enumfacing);
            }
            state = state.withProperty(OPEN, true);
            worldIn.setBlockState(pos, state, 2);
        }
        worldIn.playAuxSFXAtEntity(playerIn, state.getValue(OPEN) != false ? 1003 : 1006, pos, 0);
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (worldIn.isRemote) return;
        boolean flag = worldIn.isBlockPowered(pos);
        if (!flag) {
            if (!neighborBlock.canProvidePower()) return;
        }
        if (flag && !state.getValue(OPEN).booleanValue() && !state.getValue(POWERED).booleanValue()) {
            worldIn.setBlockState(pos, state.withProperty(OPEN, true).withProperty(POWERED, true), 2);
            worldIn.playAuxSFXAtEntity(null, 1003, pos, 0);
            return;
        }
        if (!flag && state.getValue(OPEN).booleanValue() && state.getValue(POWERED).booleanValue()) {
            worldIn.setBlockState(pos, state.withProperty(OPEN, false).withProperty(POWERED, false), 2);
            worldIn.playAuxSFXAtEntity(null, 1006, pos, 0);
            return;
        }
        if (flag == state.getValue(POWERED)) return;
        worldIn.setBlockState(pos, state.withProperty(POWERED, flag), 2);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean bl;
        IBlockState iBlockState = this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(OPEN, (meta & 4) != 0);
        if ((meta & 8) != 0) {
            bl = true;
            return iBlockState.withProperty(POWERED, bl);
        }
        bl = false;
        return iBlockState.withProperty(POWERED, bl);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i |= state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(POWERED).booleanValue()) {
            i |= 8;
        }
        if (state.getValue(OPEN) == false) return i;
        i |= 4;
        return i;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, OPEN, POWERED, IN_WALL);
    }
}

