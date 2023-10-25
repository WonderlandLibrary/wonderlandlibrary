/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa
extends BlockDirectional
implements IGrowable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, AGE);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        if (!this.canBlockStay(world, blockPos, iBlockState)) {
            this.dropBlock(world, blockPos, iBlockState);
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        if (!enumFacing.getAxis().isHorizontal()) {
            enumFacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumFacing.getOpposite()).withProperty(AGE, 0);
    }

    public BlockCocoa() {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AGE, 0));
        this.setTickRandomly(true);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    private void dropBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        world.setBlockState(blockPos, Blocks.air.getDefaultState(), 3);
        this.dropBlockAsItem(world, blockPos, iBlockState, 0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        EnumFacing enumFacing = iBlockState.getValue(FACING);
        int n = iBlockState.getValue(AGE);
        int n2 = 4 + n * 2;
        int n3 = 5 + n * 2;
        float f = (float)n2 / 2.0f;
        switch (enumFacing) {
            case SOUTH: {
                this.setBlockBounds((8.0f - f) / 16.0f, (12.0f - (float)n3) / 16.0f, (15.0f - (float)n2) / 16.0f, (8.0f + f) / 16.0f, 0.75f, 0.9375f);
                break;
            }
            case NORTH: {
                this.setBlockBounds((8.0f - f) / 16.0f, (12.0f - (float)n3) / 16.0f, 0.0625f, (8.0f + f) / 16.0f, 0.75f, (1.0f + (float)n2) / 16.0f);
                break;
            }
            case WEST: {
                this.setBlockBounds(0.0625f, (12.0f - (float)n3) / 16.0f, (8.0f - f) / 16.0f, (1.0f + (float)n2) / 16.0f, 0.75f, (8.0f + f) / 16.0f);
                break;
            }
            case EAST: {
                this.setBlockBounds((15.0f - (float)n2) / 16.0f, (12.0f - (float)n3) / 16.0f, (8.0f - f) / 16.0f, 0.9375f, 0.75f, (8.0f + f) / 16.0f);
            }
        }
    }

    @Override
    public boolean canGrow(World world, BlockPos blockPos, IBlockState iBlockState, boolean bl) {
        return iBlockState.getValue(AGE) < 2;
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        world.setBlockState(blockPos, iBlockState.withProperty(AGE, iBlockState.getValue(AGE) + 1), 2);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getHorizontalIndex();
        return n |= iBlockState.getValue(AGE) << 2;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        int n2 = iBlockState.getValue(AGE);
        int n3 = 1;
        if (n2 >= 2) {
            n3 = 3;
        }
        int n4 = 0;
        while (n4 < n3) {
            BlockCocoa.spawnAsEntity(world, blockPos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()));
            ++n4;
        }
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        int n;
        if (!this.canBlockStay(world, blockPos, iBlockState)) {
            this.dropBlock(world, blockPos, iBlockState);
        } else if (world.rand.nextInt(5) == 0 && (n = iBlockState.getValue(AGE).intValue()) < 2) {
            world.setBlockState(blockPos, iBlockState.withProperty(AGE, n + 1), 2);
        }
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        return EnumDyeColor.BROWN.getDyeDamage();
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return Items.dye;
    }

    public boolean canBlockStay(World world, BlockPos blockPos, IBlockState iBlockState) {
        IBlockState iBlockState2 = world.getBlockState(blockPos = blockPos.offset(iBlockState.getValue(FACING)));
        return iBlockState2.getBlock() == Blocks.log && iBlockState2.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        this.setBlockBoundsBasedOnState(world, blockPos);
        return super.getSelectedBoundingBox(world, blockPos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.setBlockBoundsBasedOnState(world, blockPos);
        return super.getCollisionBoundingBox(world, blockPos, iBlockState);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState iBlockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        EnumFacing enumFacing = EnumFacing.fromAngle(entityLivingBase.rotationYaw);
        world.setBlockState(blockPos, iBlockState.withProperty(FACING, enumFacing), 2);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(n)).withProperty(AGE, (n & 0xF) >> 2);
    }
}

