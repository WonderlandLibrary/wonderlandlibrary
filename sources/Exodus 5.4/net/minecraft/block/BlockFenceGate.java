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

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, OPEN, POWERED, IN_WALL);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (iBlockState.getValue(OPEN).booleanValue()) {
            return null;
        }
        EnumFacing.Axis axis = iBlockState.getValue(FACING).getAxis();
        return axis == EnumFacing.Axis.Z ? new AxisAlignedBB(blockPos.getX(), blockPos.getY(), (float)blockPos.getZ() + 0.375f, blockPos.getX() + 1, (float)blockPos.getY() + 1.5f, (float)blockPos.getZ() + 0.625f) : new AxisAlignedBB((float)blockPos.getX() + 0.375f, blockPos.getY(), blockPos.getZ(), (float)blockPos.getX() + 0.625f, (float)blockPos.getY() + 1.5f, blockPos.getZ() + 1);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getHorizontalIndex();
        if (iBlockState.getValue(POWERED).booleanValue()) {
            n |= 8;
        }
        if (iBlockState.getValue(OPEN).booleanValue()) {
            n |= 4;
        }
        return n;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(n)).withProperty(OPEN, (n & 4) != 0).withProperty(POWERED, (n & 8) != 0);
    }

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        EnumFacing.Axis axis = iBlockState.getValue(FACING).getAxis();
        if (axis == EnumFacing.Axis.Z && (iBlockAccess.getBlockState(blockPos.west()).getBlock() == Blocks.cobblestone_wall || iBlockAccess.getBlockState(blockPos.east()).getBlock() == Blocks.cobblestone_wall) || axis == EnumFacing.Axis.X && (iBlockAccess.getBlockState(blockPos.north()).getBlock() == Blocks.cobblestone_wall || iBlockAccess.getBlockState(blockPos.south()).getBlock() == Blocks.cobblestone_wall)) {
            iBlockState = iBlockState.withProperty(IN_WALL, true);
        }
        return iBlockState;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos.down()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(world, blockPos) : false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        boolean bl;
        if (!world.isRemote && ((bl = world.isBlockPowered(blockPos)) || block.canProvidePower())) {
            if (bl && !iBlockState.getValue(OPEN).booleanValue() && !iBlockState.getValue(POWERED).booleanValue()) {
                world.setBlockState(blockPos, iBlockState.withProperty(OPEN, true).withProperty(POWERED, true), 2);
                world.playAuxSFXAtEntity(null, 1003, blockPos, 0);
            } else if (!bl && iBlockState.getValue(OPEN).booleanValue() && iBlockState.getValue(POWERED).booleanValue()) {
                world.setBlockState(blockPos, iBlockState.withProperty(OPEN, false).withProperty(POWERED, false), 2);
                world.playAuxSFXAtEntity(null, 1006, blockPos, 0);
            } else if (bl != iBlockState.getValue(POWERED)) {
                world.setBlockState(blockPos, iBlockState.withProperty(POWERED, bl), 2);
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return true;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return iBlockAccess.getBlockState(blockPos).getValue(OPEN);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        EnumFacing.Axis axis = iBlockAccess.getBlockState(blockPos).getValue(FACING).getAxis();
        if (axis == EnumFacing.Axis.Z) {
            this.setBlockBounds(0.0f, 0.0f, 0.375f, 1.0f, 1.0f, 0.625f);
        } else {
            this.setBlockBounds(0.375f, 0.0f, 0.0f, 0.625f, 1.0f, 1.0f);
        }
    }

    public BlockFenceGate(BlockPlanks.EnumType enumType) {
        super(Material.wood, enumType.func_181070_c());
        this.setDefaultState(this.blockState.getBaseState().withProperty(OPEN, false).withProperty(POWERED, false).withProperty(IN_WALL, false));
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (iBlockState.getValue(OPEN).booleanValue()) {
            iBlockState = iBlockState.withProperty(OPEN, false);
            world.setBlockState(blockPos, iBlockState, 2);
        } else {
            EnumFacing enumFacing2 = EnumFacing.fromAngle(entityPlayer.rotationYaw);
            if (iBlockState.getValue(FACING) == enumFacing2.getOpposite()) {
                iBlockState = iBlockState.withProperty(FACING, enumFacing2);
            }
            iBlockState = iBlockState.withProperty(OPEN, true);
            world.setBlockState(blockPos, iBlockState, 2);
        }
        world.playAuxSFXAtEntity(entityPlayer, iBlockState.getValue(OPEN) != false ? 1003 : 1006, blockPos, 0);
        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        return this.getDefaultState().withProperty(FACING, entityLivingBase.getHorizontalFacing()).withProperty(OPEN, false).withProperty(POWERED, false).withProperty(IN_WALL, false);
    }
}

