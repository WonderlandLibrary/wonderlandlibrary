/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTorch
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>(){

        public boolean apply(EnumFacing enumFacing) {
            return enumFacing != EnumFacing.DOWN;
        }
    });

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        if (this.canPlaceAt(world, blockPos, enumFacing)) {
            return this.getDefaultState().withProperty(FACING, enumFacing);
        }
        for (EnumFacing enumFacing2 : EnumFacing.Plane.HORIZONTAL) {
            if (!world.isBlockNormalCube(blockPos.offset(enumFacing2.getOpposite()), true)) continue;
            return this.getDefaultState().withProperty(FACING, enumFacing2);
        }
        return this.getDefaultState();
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    private boolean canPlaceAt(World world, BlockPos blockPos, EnumFacing enumFacing) {
        BlockPos blockPos2 = blockPos.offset(enumFacing.getOpposite());
        boolean bl = enumFacing.getAxis().isHorizontal();
        return bl && world.isBlockNormalCube(blockPos2, true) || enumFacing.equals(EnumFacing.UP) && this.canPlaceOn(world, blockPos2);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    protected boolean checkForDrop(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (iBlockState.getBlock() == this && this.canPlaceAt(world, blockPos, iBlockState.getValue(FACING))) {
            return true;
        }
        if (world.getBlockState(blockPos).getBlock() == this) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
        }
        return false;
    }

    private boolean canPlaceOn(World world, BlockPos blockPos) {
        if (World.doesBlockHaveSolidTopSurface(world, blockPos)) {
            return true;
        }
        Block block = world.getBlockState(blockPos).getBlock();
        return block instanceof BlockFence || block == Blocks.glass || block == Blocks.cobblestone_wall || block == Blocks.stained_glass;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.checkForDrop(world, blockPos, iBlockState);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        switch (iBlockState.getValue(FACING)) {
            case EAST: {
                n |= 1;
                break;
            }
            case WEST: {
                n |= 2;
                break;
            }
            case SOUTH: {
                n |= 3;
                break;
            }
            case NORTH: {
                n |= 4;
                break;
            }
            default: {
                n |= 5;
            }
        }
        return n;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, BlockPos blockPos, Vec3 vec3, Vec3 vec32) {
        EnumFacing enumFacing = world.getBlockState(blockPos).getValue(FACING);
        float f = 0.15f;
        if (enumFacing == EnumFacing.EAST) {
            this.setBlockBounds(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
        } else if (enumFacing == EnumFacing.WEST) {
            this.setBlockBounds(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
        } else if (enumFacing == EnumFacing.SOUTH) {
            this.setBlockBounds(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
        } else if (enumFacing == EnumFacing.NORTH) {
            this.setBlockBounds(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
        } else {
            f = 0.1f;
            this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
        }
        return super.collisionRayTrace(world, blockPos, vec3, vec32);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        IBlockState iBlockState = this.getDefaultState();
        switch (n) {
            case 1: {
                iBlockState = iBlockState.withProperty(FACING, EnumFacing.EAST);
                break;
            }
            case 2: {
                iBlockState = iBlockState.withProperty(FACING, EnumFacing.WEST);
                break;
            }
            case 3: {
                iBlockState = iBlockState.withProperty(FACING, EnumFacing.SOUTH);
                break;
            }
            case 4: {
                iBlockState = iBlockState.withProperty(FACING, EnumFacing.NORTH);
                break;
            }
            default: {
                iBlockState = iBlockState.withProperty(FACING, EnumFacing.UP);
            }
        }
        return iBlockState;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        double d = (double)blockPos.getX() + 0.5;
        double d2 = (double)blockPos.getY() + 0.7;
        double d3 = (double)blockPos.getZ() + 0.5;
        double d4 = 0.22;
        double d5 = 0.27;
        if (enumFacing.getAxis().isHorizontal()) {
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d + d5 * (double)enumFacing2.getFrontOffsetX(), d2 + d4, d3 + d5 * (double)enumFacing2.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
            world.spawnParticle(EnumParticleTypes.FLAME, d + d5 * (double)enumFacing2.getFrontOffsetX(), d2 + d4, d3 + d5 * (double)enumFacing2.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
        } else {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
            world.spawnParticle(EnumParticleTypes.FLAME, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        for (EnumFacing enumFacing : FACING.getAllowedValues()) {
            if (!this.canPlaceAt(world, blockPos, enumFacing)) continue;
            return true;
        }
        return false;
    }

    protected BlockTorch() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    protected boolean onNeighborChangeInternal(World world, BlockPos blockPos, IBlockState iBlockState) {
        if (!this.checkForDrop(world, blockPos, iBlockState)) {
            return true;
        }
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        EnumFacing.Axis axis = enumFacing.getAxis();
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        boolean bl = false;
        if (axis.isHorizontal() && !world.isBlockNormalCube(blockPos.offset(enumFacing2), true)) {
            bl = true;
        } else if (axis.isVertical() && !this.canPlaceOn(world, blockPos.offset(enumFacing2))) {
            bl = true;
        }
        if (bl) {
            this.dropBlockAsItem(world, blockPos, iBlockState, 0);
            world.setBlockToAir(blockPos);
            return true;
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        this.onNeighborChangeInternal(world, blockPos, iBlockState);
    }
}

